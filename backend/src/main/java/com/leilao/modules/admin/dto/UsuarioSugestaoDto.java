package com.leilao.modules.admin.dto;

import lombok.Data;

/**
 * DTO para sugestões de usuários na busca administrativa
 * História 2: Processo de Contratação de Vendedores
 */
@Data
public class UsuarioSugestaoDto {
    private String id;
    private String nome;
    private String email;
    private String telefone;
    private Boolean isVendedor;
    private Boolean temContratoAtivo;
}