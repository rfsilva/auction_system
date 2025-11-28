package com.leilao.modules.vendedor.service;

import com.leilao.modules.vendedor.entity.Vendedor;
import com.leilao.modules.vendedor.repository.VendedorRepository;
import com.leilao.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service para operações com Vendedor
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VendedorService {

    private final VendedorRepository vendedorRepository;

    /**
     * Busca vendedor por ID do usuário
     */
    @Transactional(readOnly = true)
    public Vendedor buscarPorUsuarioId(String usuarioId) {
        return vendedorRepository.findByUsuarioId(usuarioId)
            .orElseThrow(() -> new BusinessException("Vendedor não encontrado para o usuário: " + usuarioId));
    }

    /**
     * Verifica se o usuário é um vendedor
     */
    @Transactional(readOnly = true)
    public boolean isVendedor(String usuarioId) {
        return vendedorRepository.existsByUsuarioId(usuarioId);
    }

    /**
     * Obtém o ID do vendedor pelo ID do usuário
     */
    @Transactional(readOnly = true)
    public String obterVendedorIdPorUsuarioId(String usuarioId) {
        Vendedor vendedor = buscarPorUsuarioId(usuarioId);
        return vendedor.getId();
    }
}