package com.leilao.modules.auth.service;

import com.leilao.modules.auth.entity.Usuario;
import com.leilao.modules.auth.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementação do UserDetailsService para integração com Spring Security
 * Retorna diretamente a entidade Usuario que implementa UserDetails
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        // Retorna diretamente o Usuario que implementa UserDetails
        // Isso permite que @AuthenticationPrincipal Usuario funcione corretamente
        return usuario;
    }
}