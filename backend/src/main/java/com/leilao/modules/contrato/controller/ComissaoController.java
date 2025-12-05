package com.leilao.modules.contrato.controller;

import com.leilao.modules.contrato.dto.ComissaoDto;
import com.leilao.modules.contrato.dto.ComissaoRelatorioDto;
import com.leilao.modules.contrato.dto.ComissaoResumoDto;
import com.leilao.modules.contrato.service.ComissaoCalculoService;
import com.leilao.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Controller específico para cálculos avançados de comissões
 * História 2: Sistema de Cálculo de Comissões - Sprint S2.2
 * REFATORAÇÃO: Movido para /api/admin/contratos/comissoes/** para organização das rotas
 */
@RestController
@RequestMapping("/api/admin/contratos/comissoes")
@RequiredArgsConstructor
@Slf4j
public class ComissaoController {

    private final ComissaoCalculoService comissaoCalculoService;
    private final MessageSourceAccessor messageSourceAccessor;

    /**
     * Endpoint avançado para cálculo de comissões com filtros detalhados
     * História 2: Sistema de Cálculo de Comissões
     * 
     * Calcula comissões por período (dia, semana, mês)
     * Separa comissões realizadas de projetadas
     * Permite filtros por vendedor, categoria, status
     * Retorna breakdown detalhado por contrato
     */
    @GetMapping("/detalhado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ComissaoRelatorioDto>> calcularComissoesDetalhado(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(required = false) String vendedor,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String status) {
        
        log.info("Cálculo detalhado de comissões: {} a {}, vendedor: {}, categoria: {}, status: {}", 
                inicio, fim, vendedor, categoria, status);

        // Validações
        if (inicio.isAfter(fim)) {
            String errorMessage = messageSourceAccessor.getMessage("validation.date.start.before.end", 
                    LocaleContextHolder.getLocale());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(errorMessage));
        }

        if (inicio.plusYears(1).isBefore(fim)) {
            String errorMessage = messageSourceAccessor.getMessage("validation.period.max.one.year", 
                    LocaleContextHolder.getLocale());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(errorMessage));
        }

        long startTime = System.currentTimeMillis();

        ComissaoRelatorioDto relatorio = comissaoCalculoService.calcularComissoesPorPeriodo(
                inicio, fim, vendedor, categoria, status);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        log.info("Cálculo detalhado de comissões concluído em {}ms - {} contratos processados", 
                duration, relatorio.getPorContrato().size());

        String successMessage = messageSourceAccessor.getMessage("commission.detailed.success", 
                new Object[]{relatorio.getPorContrato().size()}, LocaleContextHolder.getLocale());

        return ResponseEntity.ok(ApiResponse.success(successMessage, relatorio));
    }

    /**
     * Endpoint para cálculo de comissões agrupadas por vendedor
     * História 2: Sistema de Cálculo de Comissões
     */
    @GetMapping("/por-vendedor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, ComissaoResumoDto>>> calcularComissoesPorVendedor(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(required = false) String categoria) {
        
        log.info("Cálculo de comissões por vendedor: {} a {}, categoria: {}", inicio, fim, categoria);

        // Validações
        if (inicio.isAfter(fim)) {
            String errorMessage = messageSourceAccessor.getMessage("validation.date.start.before.end", 
                    LocaleContextHolder.getLocale());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(errorMessage));
        }

        long startTime = System.currentTimeMillis();

        Map<String, ComissaoResumoDto> comissoesPorVendedor = 
                comissaoCalculoService.calcularComissoesPorVendedor(inicio, fim, categoria);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        log.info("Cálculo de comissões por vendedor concluído em {}ms - {} vendedores processados", 
                duration, comissoesPorVendedor.size());

        String successMessage = messageSourceAccessor.getMessage("commission.by.seller.success", 
                new Object[]{comissoesPorVendedor.size()}, LocaleContextHolder.getLocale());

        return ResponseEntity.ok(ApiResponse.success(successMessage, comissoesPorVendedor));
    }

    /**
     * Endpoint para projeções de receita personalizadas
     * História 2: Sistema de Cálculo de Comissões
     */
    @GetMapping("/projecao")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BigDecimal>> calcularProjecaoReceita(
            @RequestParam(defaultValue = "3") Integer meses,
            @RequestParam(required = false) String categoria) {
        
        log.info("Cálculo de projeção de receita para {} meses, categoria: {}", meses, categoria);

        // Validações
        if (meses < 1 || meses > 24) {
            String errorMessage = messageSourceAccessor.getMessage("validation.months.range", 
                    LocaleContextHolder.getLocale());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(errorMessage));
        }

        long startTime = System.currentTimeMillis();

        BigDecimal projecaoReceita = comissaoCalculoService.calcularProjecaoReceita(meses, categoria);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        log.info("Projeção de receita calculada em {}ms: R$ {} para {} meses", 
                duration, projecaoReceita, meses);

        String successMessage = messageSourceAccessor.getMessage("revenue.projection.success", 
                new Object[]{meses, projecaoReceita}, LocaleContextHolder.getLocale());

        return ResponseEntity.ok(ApiResponse.success(successMessage, projecaoReceita));
    }

    /**
     * Endpoint para breakdown detalhado de um vendedor específico
     * História 2: Sistema de Cálculo de Comissões
     */
    @GetMapping("/breakdown/{vendedorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ComissaoDto>>> obterBreakdownVendedor(
            @PathVariable String vendedorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        
        log.info("Breakdown detalhado para vendedor: {} no período: {} a {}", vendedorId, inicio, fim);

        // Validações
        if (inicio.isAfter(fim)) {
            String errorMessage = messageSourceAccessor.getMessage("validation.date.start.before.end", 
                    LocaleContextHolder.getLocale());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(errorMessage));
        }

        if (vendedorId == null || vendedorId.trim().isEmpty()) {
            String errorMessage = messageSourceAccessor.getMessage("validation.seller.required", 
                    LocaleContextHolder.getLocale());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(errorMessage));
        }

        long startTime = System.currentTimeMillis();

        List<ComissaoDto> breakdown = comissaoCalculoService.calcularBreakdownDetalhado(
                inicio, fim, vendedorId);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        log.info("Breakdown detalhado calculado em {}ms - {} contratos encontrados", 
                duration, breakdown.size());

        String successMessage = messageSourceAccessor.getMessage("commission.breakdown.success", 
                new Object[]{breakdown.size(), vendedorId}, LocaleContextHolder.getLocale());

        return ResponseEntity.ok(ApiResponse.success(successMessage, breakdown));
    }

    /**
     * Endpoint para comparação de performance entre períodos
     * História 2: Sistema de Cálculo de Comissões
     */
    @GetMapping("/comparacao")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, ComissaoResumoDto>>> compararPeriodos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodo1Inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodo1Fim,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodo2Inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodo2Fim,
            @RequestParam(required = false) String categoria) {
        
        log.info("Comparação de períodos: [{} a {}] vs [{} a {}], categoria: {}", 
                periodo1Inicio, periodo1Fim, periodo2Inicio, periodo2Fim, categoria);

        // Validações
        if (periodo1Inicio.isAfter(periodo1Fim) || periodo2Inicio.isAfter(periodo2Fim)) {
            String errorMessage = messageSourceAccessor.getMessage("validation.date.start.before.end", 
                    LocaleContextHolder.getLocale());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(errorMessage));
        }

        long startTime = System.currentTimeMillis();

        // Calcular comissões para ambos os períodos
        ComissaoRelatorioDto relatorio1 = comissaoCalculoService.calcularComissoesPorPeriodo(
                periodo1Inicio, periodo1Fim, null, categoria, null);
        
        ComissaoRelatorioDto relatorio2 = comissaoCalculoService.calcularComissoesPorPeriodo(
                periodo2Inicio, periodo2Fim, null, categoria, null);

        Map<String, ComissaoResumoDto> comparacao = Map.of(
                "periodo1", relatorio1.getResumo(),
                "periodo2", relatorio2.getResumo()
        );

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        log.info("Comparação de períodos concluída em {}ms", duration);

        String successMessage = messageSourceAccessor.getMessage("commission.comparison.success", 
                LocaleContextHolder.getLocale());

        return ResponseEntity.ok(ApiResponse.success(successMessage, comparacao));
    }
}