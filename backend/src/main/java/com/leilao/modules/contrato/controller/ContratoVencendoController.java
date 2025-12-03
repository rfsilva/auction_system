package com.leilao.modules.contrato.controller;

import com.leilao.modules.contrato.dto.ContratoVencendoDto;
import com.leilao.modules.contrato.dto.ContratoVencendoRelatorioDto;
import com.leilao.modules.contrato.service.ContratoVencendoService;
import com.leilao.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para relatórios de contratos vencendo
 * História 3: Relatórios de Contratos Vencendo - Sprint S2.2
 */
@RestController
@RequestMapping("/contratos")
@RequiredArgsConstructor
@Slf4j
public class ContratoVencendoController {

    private final ContratoVencendoService contratoVencendoService;
    private final MessageSourceAccessor messageSourceAccessor;

    /**
     * Endpoint para obter contratos próximos ao vencimento
     * História 3: Relatórios de Contratos Vencendo
     * 
     * Lista contratos vencendo em 7, 15, 30 dias (configurável)
     * Permite filtros por vendedor, categoria, urgência
     * Inclui/exclui contratos já notificados
     */
    @GetMapping("/vencendo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ContratoVencendoRelatorioDto>> obterContratosVencendo(
            @RequestParam(defaultValue = "30") Integer dias,
            @RequestParam(defaultValue = "true") Boolean incluirNotificados,
            @RequestParam(required = false) String vendedor,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String urgencia) {
        
        log.info("Solicitação de contratos vencendo - dias: {}, incluirNotificados: {}, vendedor: {}, categoria: {}, urgencia: {}", 
                dias, incluirNotificados, vendedor, categoria, urgencia);

        // Validar parâmetros
        if (dias != null && (dias < 1 || dias > 365)) {
            String errorMessage = messageSourceAccessor.getMessage("validation.days.range", 
                    LocaleContextHolder.getLocale());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(errorMessage));
        }

        // Converter urgência string para enum
        ContratoVencendoDto.UrgenciaEnum urgenciaEnum = null;
        if (urgencia != null && !urgencia.trim().isEmpty()) {
            try {
                urgenciaEnum = ContratoVencendoDto.UrgenciaEnum.valueOf(urgencia.toUpperCase());
            } catch (IllegalArgumentException e) {
                String errorMessage = messageSourceAccessor.getMessage("validation.urgency.invalid", 
                        new Object[]{urgencia}, LocaleContextHolder.getLocale());
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(errorMessage));
            }
        }

        long startTime = System.currentTimeMillis();

        ContratoVencendoRelatorioDto relatorio = contratoVencendoService.obterContratosVencendo(
                dias, incluirNotificados, vendedor, categoria, urgenciaEnum);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        log.info("Relatório de contratos vencendo gerado em {}ms - {} contratos encontrados", 
                duration, relatorio.getContratos().size());

        String successMessage = messageSourceAccessor.getMessage("expiring.contracts.success", 
                new Object[]{relatorio.getContratos().size()}, LocaleContextHolder.getLocale());

        return ResponseEntity.ok(ApiResponse.success(successMessage, relatorio));
    }

    /**
     * Endpoint para forçar envio de notificações de contratos vencendo
     * História 3: Relatórios de Contratos Vencendo
     */
    @PostMapping("/vencendo/notificar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> enviarNotificacoes() {
        log.info("Solicitação manual de envio de notificações de contratos vencendo");

        try {
            contratoVencendoService.enviarNotificacoesAutomaticas();
            
            String successMessage = messageSourceAccessor.getMessage("notifications.sent.success", 
                    LocaleContextHolder.getLocale());
            
            return ResponseEntity.ok(ApiResponse.success(successMessage));
            
        } catch (Exception e) {
            log.error("Erro ao enviar notificações: {}", e.getMessage());
            
            String errorMessage = messageSourceAccessor.getMessage("notifications.sent.error", 
                    LocaleContextHolder.getLocale());
            
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(errorMessage));
        }
    }
}