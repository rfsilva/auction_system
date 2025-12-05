package com.leilao.modules.contrato.controller;

import com.leilao.modules.contrato.dto.ComissaoRelatorioDto;
import com.leilao.modules.contrato.dto.ContratoEstatisticasDto;
import com.leilao.modules.contrato.service.ContratoEstatisticasService;
import com.leilao.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Controller para estatísticas e relatórios de contratos com suporte a i18n
 * História 1: Endpoints de Estatísticas de Contratos - Sprint S2.2
 * História 2: Sistema de Cálculo de Comissões - Sprint S2.2
 * REFATORAÇÃO: Movido para /api/admin/contratos/estatisticas/** para organização das rotas
 */
@RestController
@RequestMapping("/api/admin/contratos/estatisticas")
@RequiredArgsConstructor
@Slf4j
public class ContratoEstatisticasController {

    private final ContratoEstatisticasService estatisticasService;
    private final MessageSourceAccessor messageSourceAccessor;

    /**
     * Endpoint para obter estatísticas consolidadas de contratos
     * História 1: Endpoints de Estatísticas de Contratos
     * 
     * Retorna: total de contratos por status, vendedores ativos, receita projetada
     * Performance: < 500ms para consultas de estatísticas (com cache)
     * Cache: 5 minutos para estatísticas gerais
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ContratoEstatisticasDto>> obterEstatisticas() {
        log.info("Solicitação de estatísticas de contratos recebida");
        
        long startTime = System.currentTimeMillis();
        
        ContratoEstatisticasDto estatisticas = estatisticasService.obterEstatisticasContratos();
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        log.info("Estatísticas calculadas em {}ms", duration);
        
        if (duration > 500) {
            log.warn("Performance degradada: estatísticas levaram {}ms (limite: 500ms)", duration);
        }
        
        String successMessage = messageSourceAccessor.getMessage("statistics.success", 
                LocaleContextHolder.getLocale());
        
        return ResponseEntity.ok(ApiResponse.success(successMessage, estatisticas));
    }

    /**
     * Endpoint para cálculo de comissões por período
     * História 2: Sistema de Cálculo de Comissões
     * 
     * Permite filtros por vendedor, categoria, status
     * Calcula comissões por período (dia, semana, mês)
     * Separa comissões realizadas de projetadas
     */
    @GetMapping("/comissoes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ComissaoRelatorioDto>> obterRelatorioComissoes(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(required = false) String vendedor,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String status) {
        
        log.info("Solicitação de relatório de comissões: {} a {}, vendedor: {}, categoria: {}, status: {}", 
                inicio, fim, vendedor, categoria, status);
        
        // Validar período
        if (inicio.isAfter(fim)) {
            String errorMessage = messageSourceAccessor.getMessage("validation.date.start.before.end", 
                    LocaleContextHolder.getLocale());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(errorMessage));
        }
        
        // Validar período máximo (1 ano)
        if (inicio.plusYears(1).isBefore(fim)) {
            String errorMessage = messageSourceAccessor.getMessage("validation.period.max.one.year", 
                    LocaleContextHolder.getLocale());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(errorMessage));
        }
        
        long startTime = System.currentTimeMillis();
        
        ComissaoRelatorioDto relatorio = estatisticasService.obterRelatorioComissoes(
                inicio, fim, vendedor, categoria, status);
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        log.info("Relatório de comissões gerado em {}ms", duration);
        
        String successMessage = messageSourceAccessor.getMessage("commission.report.success", 
                LocaleContextHolder.getLocale());
        
        return ResponseEntity.ok(ApiResponse.success(successMessage, relatorio));
    }

    /**
     * Endpoint para projeções de receita baseadas nos contratos ativos
     * História 2: Sistema de Cálculo de Comissões
     */
    @GetMapping("/projecoes-receita")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ContratoEstatisticasDto>> obterProjecoesReceita() {
        log.info("Solicitação de projeções de receita recebida");
        
        // Reutiliza o método de estatísticas que já calcula projeções
        ContratoEstatisticasDto estatisticas = estatisticasService.obterEstatisticasContratos();
        
        String successMessage = messageSourceAccessor.getMessage("revenue.projections.success", 
                LocaleContextHolder.getLocale());
        
        return ResponseEntity.ok(ApiResponse.success(successMessage, estatisticas));
    }
}