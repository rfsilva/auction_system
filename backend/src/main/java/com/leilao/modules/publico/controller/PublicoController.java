package com.leilao.modules.publico.controller;

import com.leilao.modules.lote.dto.LoteDto;
import com.leilao.modules.lote.service.LoteService;
import com.leilao.modules.produto.service.ProdutoService;
import com.leilao.shared.dto.ApiResponse;
import com.leilao.shared.util.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller publico para endpoints que nao requerem autenticacao
 * FASE 1 - Reorganizacao de Rotas: Consolidacao de endpoints publicos
 */
@Slf4j
@RestController
@RequestMapping("/public")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PublicoController {
    
    private final LoteService loteService;
    private final ProdutoService produtoService;
    
    /**
     * Lista lotes disponiveis no catalogo publico
     */
    @GetMapping("/catalogo/lotes")
    public ResponseEntity<ApiResponse<Page<LoteDto>>> buscarLotes(
            @RequestParam(required = false) String termo,
            @RequestParam(required = false) String categoria,
            @RequestParam(defaultValue = "proximidade_encerramento") String ordenacao,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Buscando lotes publicos - termo: {}, categoria: {}, ordenacao: {}", 
                termo, categoria, ordenacao);
        
        try {
            Page<LoteDto> lotes;
            
            if (categoria != null || !"proximidade_encerramento".equals(ordenacao)) {
                lotes = loteService.buscarCatalogoPublico(termo, categoria, ordenacao, pageable);
            } else {
                lotes = loteService.listarLotesPublicos(termo, pageable);
            }
            
            String message = MessageUtils.getMessage("catalog.lots.success");
            return ResponseEntity.ok(ApiResponse.success(message, lotes));
            
        } catch (Exception e) {
            log.error("Erro ao buscar lotes publicos: {}", e.getMessage(), e);
            String errorMessage = MessageUtils.getMessage("error.500");
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(errorMessage));
        }
    }
    
    /**
     * Busca lote especifico no catalogo publico
     */
    @GetMapping("/catalogo/lotes/{id}")
    public ResponseEntity<ApiResponse<LoteDto>> buscarLote(@PathVariable String id) {
        
        log.info("Buscando lote publico por ID: {}", id);
        
        try {
            LoteDto lote = loteService.buscarPorId(id);
            String message = MessageUtils.getMessage("lot.found");
            
            return ResponseEntity.ok(ApiResponse.success(message, lote));
            
        } catch (Exception e) {
            log.error("Erro ao buscar lote publico {}: {}", id, e.getMessage(), e);
            String errorMessage = MessageUtils.getMessage("error.500");
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(errorMessage));
        }
    }
    
    /**
     * Busca lotes em destaque (encerrando em breve)
     */
    @GetMapping("/catalogo/lotes/destaque")
    public ResponseEntity<ApiResponse<List<LoteDto>>> buscarLotesDestaque() {
        
        log.info("Buscando lotes em destaque");
        
        try {
            List<LoteDto> lotes = loteService.buscarLotesDestaque();
            String message = MessageUtils.getMessage("lots.featured.success");
            
            return ResponseEntity.ok(ApiResponse.success(message, lotes));
            
        } catch (Exception e) {
            log.error("Erro ao buscar lotes em destaque: {}", e.getMessage(), e);
            String errorMessage = MessageUtils.getMessage("error.500");
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(errorMessage));
        }
    }
    
    /**
     * Lista categorias disponiveis no catalogo
     */
    @GetMapping("/catalogo/categorias")
    public ResponseEntity<ApiResponse<List<String>>> listarCategorias() {
        
        log.info("Listando categorias disponiveis");
        
        try {
            List<String> categorias = produtoService.listarCategoriasAtivas();
            String message = MessageUtils.getMessage("product.categories.list.success");
            
            return ResponseEntity.ok(ApiResponse.success(message, categorias));
            
        } catch (Exception e) {
            log.error("Erro ao listar categorias: {}", e.getMessage(), e);
            String errorMessage = MessageUtils.getMessage("error.500");
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(errorMessage));
        }
    }
    
    /**
     * Retorna informacoes institucionais da plataforma
     */
    @GetMapping("/sobre")
    public ResponseEntity<ApiResponse<Map<String, String>>> sobre() {
        
        log.info("Acessando pagina sobre");
        
        Map<String, String> info = new HashMap<>();
        info.put("empresa", MessageUtils.getMessage("company.name"));
        info.put("descricao", MessageUtils.getMessage("company.description"));
        info.put("missao", MessageUtils.getMessage("company.mission"));
        info.put("visao", MessageUtils.getMessage("company.vision"));
        info.put("valores", MessageUtils.getMessage("company.values"));
        info.put("fundacao", MessageUtils.getMessage("company.founded"));
        info.put("sede", MessageUtils.getMessage("company.headquarters"));
        
        return ResponseEntity.ok(ApiResponse.success(info));
    }
    
    /**
     * Retorna informacoes de contato da plataforma
     */
    @GetMapping("/contato")
    public ResponseEntity<ApiResponse<Map<String, String>>> contato() {
        
        log.info("Acessando pagina de contato");
        
        Map<String, String> contato = new HashMap<>();
        contato.put("email", MessageUtils.getMessage("contact.email"));
        contato.put("telefone", MessageUtils.getMessage("contact.phone"));
        contato.put("whatsapp", MessageUtils.getMessage("contact.whatsapp"));
        contato.put("endereco", MessageUtils.getMessage("contact.address"));
        contato.put("cep", MessageUtils.getMessage("contact.zipcode"));
        contato.put("horarioAtendimento", MessageUtils.getMessage("contact.business.hours"));
        contato.put("suporte", MessageUtils.getMessage("contact.support.email"));
        contato.put("comercial", MessageUtils.getMessage("contact.commercial.email"));
        
        return ResponseEntity.ok(ApiResponse.success(contato));
    }
}