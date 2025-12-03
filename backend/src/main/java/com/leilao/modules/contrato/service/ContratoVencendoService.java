package com.leilao.modules.contrato.service;

import com.leilao.modules.contrato.dto.*;
import com.leilao.modules.contrato.entity.Contrato;
import com.leilao.modules.contrato.repository.ContratoRepository;
import com.leilao.modules.vendedor.service.VendedorService;
import com.leilao.shared.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para gerenciamento de contratos próximos ao vencimento
 * História 3: Relatórios de Contratos Vencendo - Sprint S2.2
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContratoVencendoService {

    private final ContratoRepository contratoRepository;
    private final VendedorService vendedorService;
    private final EmailService emailService;
    private final MessageSourceAccessor messageSourceAccessor;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Obtém relatório de contratos vencendo
     */
    public ContratoVencendoRelatorioDto obterContratosVencendo(
            Integer dias, Boolean incluirNotificados, String vendedorId,
            String categoria, ContratoVencendoDto.UrgenciaEnum urgencia) {

        log.info(
                "Obtendo contratos vencendo - dias: {}, incluirNotificados: {}, vendedor: {}, categoria: {}, urgencia: {}",
                dias, incluirNotificados, vendedorId, categoria, urgencia
        );

        // Valores padrão com variáveis efetivamente finais
        final int diasFiltro = (dias != null) ? dias : 30;
        final boolean incluirNotificadosFiltro = (incluirNotificados != null) ? incluirNotificados : true;

        final LocalDateTime agora = LocalDateTime.now();
        final LocalDateTime dataLimite = agora.plusDays(diasFiltro);

        // Buscar contratos vencendo
        final List<Contrato> contratos =
                contratoRepository.findContratosExpirandoEntre(agora, dataLimite);

        // Aplicar filtros adicionais
        final List<ContratoVencendoDto> contratosDto = contratos.stream()
                .filter(contrato -> aplicarFiltros(contrato, vendedorId, categoria, urgencia, agora))
                .map(contrato -> convertToVencendoDto(contrato, agora))
                .filter(dto -> incluirNotificadosFiltro || !Boolean.TRUE.equals(dto.getNotificado()))
                .collect(Collectors.toList());

        // Calcular resumo
        final ContratoVencendoResumoDto resumo = calcularResumo(contratosDto);

        // Criar filtros utilizados
        final ContratoVencendoFiltroDto filtros = ContratoVencendoFiltroDto.builder()
                .dias(diasFiltro)
                .incluirNotificados(incluirNotificadosFiltro)
                .vendedorId(vendedorId)
                .categoria(categoria)
                .urgencia(urgencia)
                .build();

        return ContratoVencendoRelatorioDto.builder()
                .contratos(contratosDto)
                .resumo(resumo)
                .filtros(filtros)
                .build();
    }

    /**
     * Envia notificações automáticas para contratos vencendo
     * História 3: Implementar notificações automáticas para contratos vencendo
     */
    @Transactional
    public void enviarNotificacoesAutomaticas() {
        log.info("Iniciando envio de notificações automáticas para contratos vencendo");

        LocalDateTime agora = LocalDateTime.now();
        
        // Notificar contratos vencendo em 30, 15, 7 e 1 dia
        int[] diasNotificacao = {30, 15, 7, 1};
        
        int totalNotificacoes = 0;
        
        for (int dias : diasNotificacao) {
            int notificacoesEnviadas = enviarNotificacoesPorPeriodo(agora, dias);
            totalNotificacoes += notificacoesEnviadas;
        }
        
        log.info("Envio de notificações automáticas concluído - {} notificações enviadas", totalNotificacoes);
    }

    /**
     * Envia notificações para um período específico
     */
    private int enviarNotificacoesPorPeriodo(LocalDateTime agora, int dias) {
        LocalDateTime inicioJanela = agora.plusDays(dias).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime fimJanela = agora.plusDays(dias).withHour(23).withMinute(59).withSecond(59);

        List<Contrato> contratos = contratoRepository.findContratosExpirandoEntre(inicioJanela, fimJanela);
        
        log.info("Encontrados {} contratos vencendo em {} dias", contratos.size(), dias);

        int notificacoesEnviadas = 0;
        
        for (Contrato contrato : contratos) {
            try {
                boolean enviado = enviarNotificacaoVencimento(contrato, dias);
                if (enviado) {
                    notificacoesEnviadas++;
                }
            } catch (Exception e) {
                log.error("Erro ao enviar notificação para contrato {}: {}", contrato.getId(), e.getMessage());
            }
        }
        
        log.info("Enviadas {} notificações para contratos vencendo em {} dias", notificacoesEnviadas, dias);
        return notificacoesEnviadas;
    }

    /**
     * Envia notificação de vencimento para um contrato específico
     * História 3: Implementar notificações automáticas para contratos vencendo
     */
    private boolean enviarNotificacaoVencimento(Contrato contrato, int dias) {
        try {
            // Buscar informações do vendedor
            var vendedor = vendedorService.buscarPorId(contrato.getSellerId());
            if (vendedor == null) {
                log.warn("Vendedor não encontrado para contrato {}", contrato.getId());
                return false;
            }

            // Preparar dados para o email
            String vendedorNome = vendedor.getUsuarioNome();
            String vendedorEmail = "vendedor@example.com"; // TODO: Obter email real do vendedor
            String categoria = contrato.getCategoria() != null ? contrato.getCategoria() : "Geral";
            String diasTexto = String.valueOf(dias);
            String dataVencimento = contrato.getValidTo() != null ? 
                    contrato.getValidTo().format(DATE_FORMATTER) : "Não definida";

            // Determinar urgência para personalizar a mensagem
            String urgenciaTexto = getUrgenciaTexto(dias);
            
            // Preparar assunto do email
            String assunto = String.format("⚠️ Contrato vencendo em %s dias - %s", diasTexto, urgenciaTexto);
            
            // Preparar corpo do email
            String corpo = construirCorpoEmail(vendedorNome, categoria, diasTexto, dataVencimento, 
                    contrato.getId(), urgenciaTexto);

            // Enviar email usando o EmailService
            try {
                emailService.sendSimpleEmail(vendedorEmail, assunto, corpo);
                
                log.info("Notificação enviada com sucesso para vendedor {} - Contrato {} vence em {} dias", 
                        vendedorNome, contrato.getId(), dias);
                
                return true;
                
            } catch (Exception emailException) {
                log.error("Erro ao enviar email para vendedor {}: {}", vendedorNome, emailException.getMessage());
                return false;
            }

        } catch (Exception e) {
            log.error("Erro ao processar notificação para contrato {}: {}", contrato.getId(), e.getMessage());
            return false;
        }
    }

    /**
     * Constrói o corpo do email de notificação
     */
    private String construirCorpoEmail(String vendedorNome, String categoria, String diasTexto, 
                                     String dataVencimento, String contratoId, String urgenciaTexto) {
        
        StringBuilder corpo = new StringBuilder();
        
        corpo.append("Olá ").append(vendedorNome).append(",\n\n");
        
        corpo.append("Este é um aviso automático sobre o vencimento do seu contrato.\n\n");
        
        corpo.append("📋 DETALHES DO CONTRATO:\n");
        corpo.append("• ID do Contrato: ").append(contratoId).append("\n");
        corpo.append("• Categoria: ").append(categoria).append("\n");
        corpo.append("• Data de Vencimento: ").append(dataVencimento).append("\n");
        corpo.append("• Dias Restantes: ").append(diasTexto).append(" dias\n");
        corpo.append("• Urgência: ").append(urgenciaTexto).append("\n\n");
        
        if (Integer.parseInt(diasTexto) <= 7) {
            corpo.append("🚨 AÇÃO URGENTE NECESSÁRIA!\n");
            corpo.append("Seu contrato vence em poucos dias. Entre em contato conosco imediatamente ");
            corpo.append("para renovar ou discutir os próximos passos.\n\n");
        } else if (Integer.parseInt(diasTexto) <= 15) {
            corpo.append("⚠️ ATENÇÃO NECESSÁRIA\n");
            corpo.append("Seu contrato vence em breve. Recomendamos que entre em contato conosco ");
            corpo.append("para planejar a renovação ou transição.\n\n");
        } else {
            corpo.append("📅 AVISO ANTECIPADO\n");
            corpo.append("Este é um aviso antecipado sobre o vencimento do seu contrato. ");
            corpo.append("Você tem tempo para planejar adequadamente.\n\n");
        }
        
        corpo.append("Para renovar seu contrato ou obter mais informações, ");
        corpo.append("acesse sua conta no sistema ou entre em contato conosco.\n\n");
        
        corpo.append("Atenciosamente,\n");
        corpo.append("Equipe do Sistema de Leilão\n\n");
        
        corpo.append("---\n");
        corpo.append("Esta é uma mensagem automática. Por favor, não responda a este email.\n");
        corpo.append("Em caso de dúvidas, utilize os canais oficiais de suporte.");
        
        return corpo.toString();
    }

    /**
     * Obtém texto de urgência baseado nos dias restantes
     */
    private String getUrgenciaTexto(int dias) {
        if (dias <= 7) {
            return "URGÊNCIA ALTA";
        } else if (dias <= 15) {
            return "URGÊNCIA MÉDIA";
        } else {
            return "URGÊNCIA BAIXA";
        }
    }

    /**
     * Aplica filtros adicionais aos contratos
     */
    private boolean aplicarFiltros(Contrato contrato, String vendedorId, String categoria, 
                                 ContratoVencendoDto.UrgenciaEnum urgencia, LocalDateTime agora) {
        
        // Filtro por vendedor
        if (vendedorId != null && !vendedorId.equals(contrato.getSellerId())) {
            return false;
        }

        // Filtro por categoria
        if (categoria != null && !categoria.equals(contrato.getCategoria())) {
            return false;
        }

        // Filtro por urgência
        if (urgencia != null) {
            ContratoVencendoDto.UrgenciaEnum contratoUrgencia = calcularUrgencia(contrato, agora);
            if (!urgencia.equals(contratoUrgencia)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Converte Contrato para ContratoVencendoDto
     */
    private ContratoVencendoDto convertToVencendoDto(Contrato contrato, LocalDateTime agora) {
        long diasRestantes = ChronoUnit.DAYS.between(agora, contrato.getValidTo());
        ContratoVencendoDto.UrgenciaEnum urgencia = calcularUrgencia(contrato, agora);

        // Buscar nome do vendedor
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

        return ContratoVencendoDto.builder()
                .id(contrato.getId())
                .vendedorNome(vendedorNome)
                .vendedorEmpresa(vendedorEmpresa)
                .categoria(contrato.getCategoria())
                .validTo(contrato.getValidTo())
                .diasRestantes(diasRestantes)
                .status(contrato.getStatus().name())
                .urgencia(urgencia)
                .notificado(false) // TODO: Implementar controle de notificações enviadas
                .taxaComissao(contrato.getFeeRate())
                .createdAt(contrato.getCreatedAt())
                .build();
    }

    /**
     * Calcula o nível de urgência baseado nos dias restantes
     */
    private ContratoVencendoDto.UrgenciaEnum calcularUrgencia(Contrato contrato, LocalDateTime agora) {
        if (contrato.getValidTo() == null) {
            return ContratoVencendoDto.UrgenciaEnum.BAIXA;
        }

        long diasRestantes = ChronoUnit.DAYS.between(agora, contrato.getValidTo());

        if (diasRestantes <= 7) {
            return ContratoVencendoDto.UrgenciaEnum.ALTA;
        } else if (diasRestantes <= 15) {
            return ContratoVencendoDto.UrgenciaEnum.MEDIA;
        } else {
            return ContratoVencendoDto.UrgenciaEnum.BAIXA;
        }
    }

    /**
     * Calcula resumo estatístico dos contratos vencendo
     */
    private ContratoVencendoResumoDto calcularResumo(List<ContratoVencendoDto> contratos) {
        // Calculando de forma simples para evitar problemas com lambdas
        int total = contratos.size();
        int urgenciaAlta = 0;
        int urgenciaMedia = 0;
        int urgenciaBaixa = 0;
        int notificados = 0;

        for (ContratoVencendoDto contrato : contratos) {
            switch (contrato.getUrgencia()) {
                case ALTA:
                    urgenciaAlta++;
                    break;
                case MEDIA:
                    urgenciaMedia++;
                    break;
                case BAIXA:
                    urgenciaBaixa++;
                    break;
            }
            
            if (contrato.getNotificado()) {
                notificados++;
            }
        }

        int pendentesNotificacao = total - notificados;

        return ContratoVencendoResumoDto.builder()
                .total((long) total)
                .urgenciaAlta((long) urgenciaAlta)
                .urgenciaMedia((long) urgenciaMedia)
                .urgenciaBaixa((long) urgenciaBaixa)
                .notificados((long) notificados)
                .pendentesNotificacao((long) pendentesNotificacao)
                .build();
    }
}