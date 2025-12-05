package com.leilao.modules.contrato.service;

import com.leilao.modules.contrato.dto.*;
import com.leilao.modules.contrato.entity.Contrato;
import com.leilao.modules.contrato.repository.ContratoEstatisticasRepository;
import com.leilao.modules.contrato.repository.ContratoRepository;
import com.leilao.modules.vendedor.service.VendedorService;
import com.leilao.shared.enums.ContractStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service para estatísticas de contratos com suporte a i18n
 * História 1: Endpoints de Estatísticas de Contratos - Sprint S2.2
 * VERSÃO CORRIGIDA - Com suporte completo a i18n usando MessageSourceAccessor
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContratoEstatisticasService {

    private final ContratoRepository contratoRepository;
    private final ContratoEstatisticasRepository estatisticasRepository;
    private final VendedorService vendedorService;
    private final MessageSourceAccessor messageSourceAccessor;

    /**
     * Obtém estatísticas consolidadas de contratos
     * Cache de 5 minutos conforme especificação
     */
    @Cacheable(value = "contrato-estatisticas", unless = "#result == null")
    public ContratoEstatisticasDto obterEstatisticasContratos() {
        log.info("Calculando estatísticas de contratos");
        
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime inicioMes = agora.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime fimMes = agora.withDayOfMonth(agora.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);
        LocalDateTime em30Dias = agora.plusDays(30);

        try {
            // Total de contratos
            Long totalContratos = contratoRepository.count();

            // Contratos por status
            Map<ContractStatus, Long> contratosPorStatus = obterContratosPorStatus();

            // Vendedores ativos
            Long vendedoresAtivos = estatisticasRepository.countVendedoresAtivos(agora);

            // Taxa média de comissão
            BigDecimal taxaMediaComissao = estatisticasRepository.getMediaTaxaComissaoAtivos(agora);
            if (taxaMediaComissao == null) {
                taxaMediaComissao = BigDecimal.ZERO;
            }

            // Contratos vencendo em 30 dias
            Long contratosVencendo30Dias = estatisticasRepository.countContratosVencendoEm(agora, em30Dias);

            // Contratos criados no mês
            Long contratosCriadosMes = estatisticasRepository.countContratosCriadosNoPeriodo(inicioMes, fimMes);

            // Contratos expirados no mês
            Long contratosExpiradosMes = estatisticasRepository.countContratosExpiradosNoPeriodo(inicioMes, fimMes);

            // Categorias ativas
            Long categoriasAtivas = estatisticasRepository.countCategoriasAtivas(agora);

            // Receita projetada e realizada (simulada por enquanto)
            BigDecimal receitaProjetadaMes = calcularReceitaProjetada(agora);
            BigDecimal receitaRealizadaMes = calcularReceitaRealizada(inicioMes, fimMes);

            return ContratoEstatisticasDto.builder()
                    .totalContratos(totalContratos)
                    .contratosPorStatus(contratosPorStatus)
                    .vendedoresAtivos(vendedoresAtivos)
                    .receitaProjetadaMes(receitaProjetadaMes)
                    .receitaRealizadaMes(receitaRealizadaMes)
                    .taxaMediaComissao(taxaMediaComissao)
                    .contratosVencendo30Dias(contratosVencendo30Dias)
                    .contratosCriadosMes(contratosCriadosMes)
                    .contratosExpiradosMes(contratosExpiradosMes)
                    .categoriasAtivas(categoriasAtivas)
                    .build();

        } catch (Exception e) {
            log.error("Erro ao calcular estatísticas de contratos", e);
            String errorMessage = messageSourceAccessor.getMessage("statistics.calculation.error", 
                    LocaleContextHolder.getLocale());
            throw new RuntimeException(errorMessage, e);
        }
    }

    /**
     * Obtém relatório de comissões por período
     * CORRIGIDO: Converte status String para enum e usa i18n
     */
    public ComissaoRelatorioDto obterRelatorioComissoes(LocalDate inicio, LocalDate fim, 
                                                       String vendedorId, String categoria, String status) {
        log.info("Gerando relatório de comissões para período: {} a {}", inicio, fim);

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.atTime(23, 59, 59);

        // Converter status String para enum
        ContractStatus statusEnum = null;
        if (status != null && !status.trim().isEmpty()) {
            try {
                statusEnum = ContractStatus.valueOf(status.toUpperCase());
                log.debug("Status convertido: {} -> {}", status, statusEnum);
            } catch (IllegalArgumentException e) {
                String warningMessage = messageSourceAccessor.getMessage("statistics.status.invalid", 
                        new Object[]{status}, LocaleContextHolder.getLocale());
                log.warn(warningMessage);
                // statusEnum permanece null, o que significa que o filtro será ignorado
            }
        }

        // Buscar contratos no período
        List<Contrato> contratos = estatisticasRepository.findContratosParaRelatorioComissoes(
                inicioDateTime, fimDateTime, vendedorId, categoria, statusEnum);

        log.info("Encontrados {} contratos para o relatório", contratos.size());

        // Converter para DTOs de comissão
        List<ComissaoDto> comissoesPorContrato = contratos.stream()
                .map(contrato -> convertToComissaoDto(contrato, inicio, fim))
                .collect(Collectors.toList());

        // Calcular resumo
        ComissaoResumoDto resumo = calcularResumoComissoes(comissoesPorContrato);

        // Criar período
        PeriodoDto periodo = PeriodoDto.builder()
                .inicio(inicio)
                .fim(fim)
                .descricao(formatarDescricaoPeriodo(inicio, fim))
                .build();

        return ComissaoRelatorioDto.builder()
                .periodo(periodo)
                .resumo(resumo)
                .porContrato(comissoesPorContrato)
                .build();
    }

    /**
     * Obtém contratos por status
     */
    private Map<ContractStatus, Long> obterContratosPorStatus() {
        List<Object[]> resultados = estatisticasRepository.getEstatisticasContratosPorStatus();
        Map<ContractStatus, Long> contratosPorStatus = new HashMap<>();

        // Inicializar todos os status com 0
        for (ContractStatus status : ContractStatus.values()) {
            contratosPorStatus.put(status, 0L);
        }

        // Preencher com os dados reais
        for (Object[] resultado : resultados) {
            ContractStatus status = (ContractStatus) resultado[0];
            Long count = (Long) resultado[1];
            contratosPorStatus.put(status, count);
        }

        return contratosPorStatus;
    }

    /**
     * Calcula receita projetada baseada em contratos ativos
     */
    private BigDecimal calcularReceitaProjetada(LocalDateTime agora) {
        List<Contrato> contratosAtivos = estatisticasRepository.findContratosAtivosParaReceita(agora);
        
        // Por enquanto, simulamos baseado na taxa média e número de contratos
        // Em uma implementação real, isso seria baseado em vendas históricas e projeções
        BigDecimal receitaBase = BigDecimal.valueOf(contratosAtivos.size() * 1000); // R$ 1000 por contrato ativo
        BigDecimal taxaMedia = contratosAtivos.stream()
                .map(Contrato::getFeeRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(Math.max(1, contratosAtivos.size())), 4, RoundingMode.HALF_UP);
        
        return receitaBase.multiply(taxaMedia).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula receita realizada no período
     */
    private BigDecimal calcularReceitaRealizada(LocalDateTime inicio, LocalDateTime fim) {
        // Por enquanto simulada - em implementação real seria baseada em vendas efetivas
        // Seria necessário integrar com módulo de vendas/arremates
        return BigDecimal.valueOf(25000.00).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Converte contrato para DTO de comissão
     */
    private ComissaoDto convertToComissaoDto(Contrato contrato, LocalDate inicio, LocalDate fim) {
        // Buscar informações do vendedor
        String vendedorNome = messageSourceAccessor.getMessage("statistics.seller.default", 
                new Object[]{contrato.getSellerId()}, LocaleContextHolder.getLocale());
        String vendedorEmpresa = null;
        
        try {
            var vendedor = vendedorService.buscarPorId(contrato.getSellerId());
            if (vendedor != null) {
                vendedorNome = vendedor.getUsuarioNome();
                vendedorEmpresa = vendedor.getCompanyName();
            }
        } catch (Exception e) {
            String errorMessage = messageSourceAccessor.getMessage("statistics.seller.error", 
                    new Object[]{contrato.getSellerId(), e.getMessage()}, LocaleContextHolder.getLocale());
            log.warn(errorMessage);
        }

        // Simular dados de vendas (em implementação real viria do módulo de vendas)
        BigDecimal totalVendas = BigDecimal.valueOf(Math.random() * 10000).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalComissoes = totalVendas.multiply(contrato.getFeeRate()).setScale(2, RoundingMode.HALF_UP);
        Long numeroTransacoes = (long) (Math.random() * 20 + 1);

        return ComissaoDto.builder()
                .contratoId(contrato.getId())
                .vendedorNome(vendedorNome)
                .vendedorEmpresa(vendedorEmpresa)
                .categoria(contrato.getCategoria())
                .taxaComissao(contrato.getFeeRate())
                .totalVendas(totalVendas)
                .totalComissoes(totalComissoes)
                .numeroTransacoes(numeroTransacoes)
                .periodoInicio(inicio)
                .periodoFim(fim)
                .comissoesRealizadas(totalComissoes.multiply(BigDecimal.valueOf(0.7)).setScale(2, RoundingMode.HALF_UP))
                .comissoesProjetadas(totalComissoes.multiply(BigDecimal.valueOf(0.3)).setScale(2, RoundingMode.HALF_UP))
                .build();
    }

    /**
     * Calcula resumo das comissões
     */
    private ComissaoResumoDto calcularResumoComissoes(List<ComissaoDto> comissoes) {
        BigDecimal totalComissoes = comissoes.stream()
                .map(ComissaoDto::getTotalComissoes)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalVendas = comissoes.stream()
                .map(ComissaoDto::getTotalVendas)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Long numeroTransacoes = comissoes.stream()
                .mapToLong(ComissaoDto::getNumeroTransacoes)
                .sum();

        BigDecimal comissoesRealizadas = comissoes.stream()
                .map(ComissaoDto::getComissoesRealizadas)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal comissoesProjetadas = comissoes.stream()
                .map(ComissaoDto::getComissoesProjetadas)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal taxaMediaComissao = totalVendas.compareTo(BigDecimal.ZERO) > 0 
                ? totalComissoes.divide(totalVendas, 4, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        return ComissaoResumoDto.builder()
                .totalComissoes(totalComissoes)
                .totalVendas(totalVendas)
                .numeroTransacoes(numeroTransacoes)
                .taxaMediaComissao(taxaMediaComissao)
                .comissoesRealizadas(comissoesRealizadas)
                .comissoesProjetadas(comissoesProjetadas)
                .contratosComVendas((long) comissoes.size())
                .build();
    }

    /**
     * Formata descrição do período com suporte a i18n
     * CORRIGIDO: Usa locale do usuário para formatação
     */
    private String formatarDescricaoPeriodo(LocalDate inicio, LocalDate fim) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        
        if (inicio.equals(fim)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", currentLocale);
            return inicio.format(formatter);
        }
        
        if (inicio.getYear() == fim.getYear() && inicio.getMonth() == fim.getMonth()) {
            YearMonth yearMonth = YearMonth.from(inicio);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", currentLocale);
            return yearMonth.format(formatter);
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", currentLocale);
        String separator = messageSourceAccessor.getMessage("date.range.separator", currentLocale);
        return inicio.format(formatter) + separator + fim.format(formatter);
    }
}