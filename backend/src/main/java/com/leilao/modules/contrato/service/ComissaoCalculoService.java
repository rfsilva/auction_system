package com.leilao.modules.contrato.service;

import com.leilao.modules.contrato.dto.*;
import com.leilao.modules.contrato.entity.Contrato;
import com.leilao.modules.contrato.repository.ContratoEstatisticasRepository;
import com.leilao.modules.vendedor.service.VendedorService;
import com.leilao.shared.enums.ContractStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service específico para cálculos de comissões
 * História 2: Sistema de Cálculo de Comissões - Sprint S2.2
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ComissaoCalculoService {

    private final ContratoEstatisticasRepository estatisticasRepository;
    private final VendedorService vendedorService;
    private final MessageSourceAccessor messageSourceAccessor;

    /**
     * Calcula comissões por período com filtros avançados
     * História 2: Sistema de Cálculo de Comissões
     * 
     * @param inicio Data de início do período
     * @param fim Data de fim do período
     * @param vendedorId Filtro por vendedor específico (opcional)
     * @param categoria Filtro por categoria específica (opcional)
     * @param status Filtro por status do contrato (opcional)
     * @return Relatório detalhado de comissões
     */
    public ComissaoRelatorioDto calcularComissoesPorPeriodo(
            LocalDate inicio, LocalDate fim, String vendedorId, 
            String categoria, String status) {
        
        log.info("Calculando comissões para período: {} a {}, vendedor: {}, categoria: {}, status: {}", 
                inicio, fim, vendedorId, categoria, status);

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.atTime(23, 59, 59);

        // Converter status String para enum
        ContractStatus statusEnum = parseStatus(status);

        // Buscar contratos no período
        List<Contrato> contratos = estatisticasRepository.findContratosParaRelatorioComissoes(
                inicioDateTime, fimDateTime, vendedorId, categoria, statusEnum);

        log.info("Encontrados {} contratos para cálculo de comissões", contratos.size());

        // Calcular comissões por contrato
        List<ComissaoDto> comissoesPorContrato = contratos.stream()
                .map(contrato -> calcularComissaoContrato(contrato, inicio, fim))
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
     * Calcula comissões por vendedor no período
     * História 2: Sistema de Cálculo de Comissões
     */
    public Map<String, ComissaoResumoDto> calcularComissoesPorVendedor(
            LocalDate inicio, LocalDate fim, String categoria) {
        
        log.info("Calculando comissões por vendedor para período: {} a {}, categoria: {}", 
                inicio, fim, categoria);

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.atTime(23, 59, 59);

        // Buscar todos os contratos no período
        List<Contrato> contratos = estatisticasRepository.findContratosParaRelatorioComissoes(
                inicioDateTime, fimDateTime, null, categoria, null);

        // Agrupar por vendedor e calcular comissões
        return contratos.stream()
                .collect(Collectors.groupingBy(Contrato::getSellerId))
                .entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> {
                        List<ComissaoDto> comissoesVendedor = entry.getValue().stream()
                                .map(contrato -> calcularComissaoContrato(contrato, inicio, fim))
                                .collect(Collectors.toList());
                        return calcularResumoComissoes(comissoesVendedor);
                    }
                ));
    }

    /**
     * Calcula projeções de receita baseadas em contratos ativos
     * História 2: Sistema de Cálculo de Comissões
     */
    public BigDecimal calcularProjecaoReceita(int meses, String categoria) {
        log.info("Calculando projeção de receita para {} meses, categoria: {}", meses, categoria);

        LocalDateTime agora = LocalDateTime.now();
        List<Contrato> contratosAtivos = estatisticasRepository.findContratosAtivosParaReceita(agora);

        // Filtrar por categoria se especificada
        if (categoria != null && !categoria.trim().isEmpty()) {
            contratosAtivos = contratosAtivos.stream()
                    .filter(c -> categoria.equals(c.getCategoria()))
                    .collect(Collectors.toList());
        }

        // Calcular projeção baseada em:
        // 1. Número de contratos ativos
        // 2. Taxa média de comissão
        // 3. Estimativa de vendas por contrato
        // 4. Período de projeção

        BigDecimal numeroContratos = BigDecimal.valueOf(contratosAtivos.size());
        BigDecimal taxaMediaComissao = calcularTaxaMediaComissao(contratosAtivos);
        BigDecimal vendasEstimadasPorContrato = BigDecimal.valueOf(2000); // R$ 2000 por mês por contrato
        BigDecimal multiplicadorPeriodo = BigDecimal.valueOf(meses);

        BigDecimal projecaoReceita = numeroContratos
                .multiply(vendasEstimadasPorContrato)
                .multiply(taxaMediaComissao)
                .multiply(multiplicadorPeriodo)
                .setScale(2, RoundingMode.HALF_UP);

        log.info("Projeção de receita calculada: R$ {} para {} meses", projecaoReceita, meses);

        return projecaoReceita;
    }

    /**
     * Calcula breakdown detalhado por contrato
     * História 2: Sistema de Cálculo de Comissões
     */
    public List<ComissaoDto> calcularBreakdownDetalhado(
            LocalDate inicio, LocalDate fim, String vendedorId) {
        
        log.info("Calculando breakdown detalhado para vendedor: {} no período: {} a {}", 
                vendedorId, inicio, fim);

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.atTime(23, 59, 59);

        List<Contrato> contratos = estatisticasRepository.findContratosParaRelatorioComissoes(
                inicioDateTime, fimDateTime, vendedorId, null, null);

        return contratos.stream()
                .map(contrato -> calcularComissaoContrato(contrato, inicio, fim))
                .sorted((c1, c2) -> c2.getTotalComissoes().compareTo(c1.getTotalComissoes()))
                .collect(Collectors.toList());
    }

    /**
     * Calcula comissão para um contrato específico
     */
    private ComissaoDto calcularComissaoContrato(Contrato contrato, LocalDate inicio, LocalDate fim) {
        // Buscar informações do vendedor
        String vendedorNome = "Vendedor " + contrato.getSellerId();
        String vendedorEmpresa = null;
        
        try {
            var vendedor = vendedorService.buscarPorId(contrato.getSellerId());
            if (vendedor != null) {
                vendedorNome = vendedor.getUsuarioNome();
                vendedorEmpresa = vendedor.getCompanyName();
            }
        } catch (Exception e) {
            log.warn("Erro ao buscar vendedor {}: {}", contrato.getSellerId(), e.getMessage());
        }

        // Simular dados de vendas baseados no período e contrato
        // Em implementação real, estes dados viriam do módulo de vendas/arremates
        long diasPeriodo = ChronoUnit.DAYS.between(inicio, fim) + 1;
        BigDecimal fatorTempo = BigDecimal.valueOf(Math.min(diasPeriodo / 30.0, 1.0)); // Máximo 1 mês
        
        BigDecimal totalVendas = BigDecimal.valueOf(Math.random() * 5000 + 1000)
                .multiply(fatorTempo)
                .setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal totalComissoes = totalVendas
                .multiply(contrato.getFeeRate())
                .setScale(2, RoundingMode.HALF_UP);
        
        Long numeroTransacoes = Math.max(1L, (long) (Math.random() * 10 * fatorTempo.doubleValue()));

        // Simular divisão entre realizadas e projetadas
        BigDecimal percentualRealizado = BigDecimal.valueOf(0.6 + Math.random() * 0.3); // 60-90%
        BigDecimal comissoesRealizadas = totalComissoes
                .multiply(percentualRealizado)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal comissoesProjetadas = totalComissoes
                .subtract(comissoesRealizadas)
                .setScale(2, RoundingMode.HALF_UP);

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
                .comissoesRealizadas(comissoesRealizadas)
                .comissoesProjetadas(comissoesProjetadas)
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
     * Calcula taxa média de comissão de uma lista de contratos
     */
    private BigDecimal calcularTaxaMediaComissao(List<Contrato> contratos) {
        if (contratos.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal somaComissoes = contratos.stream()
                .map(Contrato::getFeeRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return somaComissoes.divide(
                BigDecimal.valueOf(contratos.size()), 
                4, 
                RoundingMode.HALF_UP
        );
    }

    /**
     * Converte status String para enum
     */
    private ContractStatus parseStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return null;
        }

        try {
            return ContractStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            String warningMessage = messageSourceAccessor.getMessage("statistics.status.invalid", 
                    new Object[]{status}, LocaleContextHolder.getLocale());
            log.warn(warningMessage);
            return null;
        }
    }

    /**
     * Formata descrição do período
     */
    private String formatarDescricaoPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio.equals(fim)) {
            return inicio.toString();
        }
        
        String separator = messageSourceAccessor.getMessage("date.range.separator", 
                LocaleContextHolder.getLocale());
        return inicio.toString() + separator + fim.toString();
    }
}