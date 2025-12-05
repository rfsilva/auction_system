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

        logger.debug("Processando requisição: {} {}", request.getMethod(), request.getRequestURI());
        logger.debug("Authorization header: {}", authHeader != null ? "Bearer ***" : "null");

        // Verificar se o header Authorization existe e tem o formato correto
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            logger.debug("Header Authorization ausente ou inválido");
            filterChain.doFilter(request, response);
            return;
        }

        // Extrair o token
        jwt = authHeader.substring(BEARER_PREFIX.length());
        logger.debug("Token JWT extraído: {}...", jwt.substring(0, Math.min(jwt.length(), 20)));

        try {
            // Extrair email do token
            userEmail = jwtService.extractUsername(jwt);
            logger.debug("Email extraído do token: {}", userEmail);

            // Se o email existe e não há autenticação no contexto
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Carregar detalhes do usuário
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                logger.debug("UserDetails carregado: {} (classe: {})", userDetails.getUsername(), userDetails.getClass().getSimpleName());

                // Verificar se o token é válido
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // Criar token de autenticação
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, // Este será o principal - deve ser o Usuario
                            null,
                            userDetails.getAuthorities()
                    );
                    
                    // Adicionar detalhes da requisição
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Definir autenticação no contexto
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    logger.info("Usuário autenticado com sucesso: {} (principal: {})", 
                        userEmail, authToken.getPrincipal().getClass().getSimpleName());
                } else {
                    logger.warn("Token inválido para usuário: {}", userEmail);
                }
            } else if (userEmail == null) {
                logger.warn("Email não encontrado no token");
            } else {
                logger.debug("Usuário já autenticado no contexto");
            }
        } catch (Exception e) {
            logger.error("Erro ao processar token JWT: {}", e.getMessage(), e);
            // Não interromper a cadeia de filtros, apenas não autenticar
        }

        filterChain.doFilter(request, response);
    }
}