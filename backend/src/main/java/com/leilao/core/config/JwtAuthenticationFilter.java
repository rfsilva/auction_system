package com.leilao.core.config;

import com.leilao.modules.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro para autenticação JWT
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        final String jwt;
        final String userEmail;

        // Verificar se o header Authorization existe e tem o formato correto
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extrair o token
        jwt = authHeader.substring(BEARER_PREFIX.length());

        try {
            // Extrair email do token
            userEmail = jwtService.extractUsername(jwt);

            // Se o email existe e não há autenticação no contexto
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Carregar detalhes do usuário
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // Verificar se o token é válido
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // Criar token de autenticação
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    
                    // Adicionar detalhes da requisição
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Definir autenticação no contexto
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    logger.debug("Usuário autenticado: {}", userEmail);
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao processar token JWT: {}", e.getMessage());
            // Não interromper a cadeia de filtros, apenas não autenticar
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Verifica se o token é válido para o usuário
     */
    private boolean isTokenValid(String jwt, UserDetails userDetails) {
        try {
            // Aqui você pode adicionar validações específicas se necessário
            // Por exemplo, verificar se o token não está em uma blacklist
            return jwtService.isTokenValid(jwt, userDetails);
        } catch (Exception e) {
            logger.error("Erro ao validar token: {}", e.getMessage());
            return false;
        }
    }
}