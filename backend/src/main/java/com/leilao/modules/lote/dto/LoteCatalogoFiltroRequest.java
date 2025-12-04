package com.leilao.modules.lote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para filtros do catálogo público de lotes
 * História 02: Transformação do Catálogo em Catálogo de Lotes
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoteCatalogoFiltroRequest {
    
    private String termo; // Busca por título ou descrição
    private String categoria; // Filtro por categoria
    private String ordenacao; // proximidade_encerramento, recentes, alfabetica
    
    // Paginação
    private Integer page;
    private Integer size;
    private String sort;
    private String direction;
    
    // Valores padrão
    public Integer getPage() {
        return page != null ? page : 0;
    }
    
    public Integer getSize() {
        return size != null ? size : 10; // Padrão 10 lotes por página
    }
    
    public String getSort() {
        return sort != null ? sort : "loteEndDateTime";
    }
    
    public String getDirection() {
        return direction != null ? direction : "asc";
    }
    
    public String getOrdenacao() {
        return ordenacao != null ? ordenacao : "proximidade_encerramento";
    }
}