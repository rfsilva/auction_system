package com.leilao.modules.publico.controller;

import com.leilao.modules.lote.dto.LoteDto;
import com.leilao.modules.lote.service.LoteService;
import com.leilao.modules.produto.service.ProdutoService;
import com.leilao.shared.dto.ApiResponse;
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
            
            return ResponseEntity.ok(ApiResponse.success(
                "Lotes encontrados com sucesso", 
                lotes
            ));
            
        } catch (Exception e) {
            log.error("Erro ao buscar lotes publicos: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro interno do servidor"));
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
            
            return ResponseEntity.ok(ApiResponse.success(
                "Lote encontrado com sucesso", 
                lote
            ));
            
        } catch (Exception e) {
            log.error("Erro ao buscar lote publico {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro interno do servidor"));
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
            
            return ResponseEntity.ok(ApiResponse.success(
                "Lotes em destaque", 
                lotes
            ));
            
        } catch (Exception e) {
            log.error("Erro ao buscar lotes em destaque: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro interno do servidor"));
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
            
            return ResponseEntity.ok(ApiResponse.success(
                "Categorias disponiveis", 
                categorias
            ));
            
        } catch (Exception e) {
            log.error("Erro ao listar categorias: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro interno do servidor"));
        }
    }
    
    /**
     * Retorna informacoes institucionais da plataforma
     */
    @GetMapping("/sobre")
    public ResponseEntity<ApiResponse<Map<String, String>>> sobre() {
        
        log.info("Acessando pagina sobre");
        
        Map<String, String> info = new HashMap<>();
        info.put("empresa", "Sistema de Leilao Online");
        info.put("descricao", "Plataforma moderna para leiloes online com seguranca e transparencia");
        info.put("missao", "Conectar vendedores e compradores atraves de leiloes justos e transparentes");
        info.put("visao", "Ser a principal plataforma de leiloes online do mercado");
        info.put("valores", "Transparencia, Seguranca, Inovacao e Excelencia no Atendimento");
        info.put("fundacao", "2024");
        info.put("sede", "Brasil");
        
        return ResponseEntity.ok(ApiResponse.success(
            "Informacoes institucionais", 
            info
        ));
    }
    
    /**
     * Retorna informacoes de contato da plataforma
     */
    @GetMapping("/contato")
    public ResponseEntity<ApiResponse<Map<String, String>>> contato() {
        
        log.info("Acessando pagina de contato");
        
        Map<String, String> contato = new HashMap<>();
        contato.put("email", "contato@leilao.com.br");
        contato.put("telefone", "+55 (11) 9999-9999");
        contato.put("whatsapp", "+55 (11) 9999-9999");
        contato.put("endereco", "Rua dos Leiloes, 123 - Sao Paulo/SP");
        contato.put("cep", "01234-567");
        contato.put("horarioAtendimento", "Segunda a Sexta: 8h as 18h");
        contato.put("suporte", "suporte@leilao.com.br");
        contato.put("comercial", "comercial@leilao.com.br");
        
        return ResponseEntity.ok(ApiResponse.success(
            "Informacoes de contato", 
            contato
        ));
    }
}