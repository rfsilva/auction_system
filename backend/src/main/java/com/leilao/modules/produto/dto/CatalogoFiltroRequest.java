package com.leilao.modules.produto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para filtros do catálogo público
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogoFiltroRequest {
    
    private String categoria;
    private BigDecimal precoMin;
    private BigDecimal precoMax;
    private String titulo;
    private String ordenacao = "recentes"; // recentes, preco_asc, preco_desc, terminando
    private Integer page = 0;
    private Integer size = 20;
}