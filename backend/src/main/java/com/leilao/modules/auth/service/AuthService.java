package com.leilao.modules.auth.service;

import com.leilao.modules.auth.dto.*;
import com.leilao.modules.auth.entity.Usuario;
import com.leilao.modules.auth.repository.UsuarioRepository;
import com.leilao.modules.vendedor.entity.Vendedor;
import com.leilao.modules.vendedor.repository.VendedorRepository;
import com.leilao.shared.enums.UserRole;
import com.leilao.shared.enums.UserStatus;
import com.leilao.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Service para operações de autenticação e autorização
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final int LOCKOUT_DURATION_MINUTES = 30;

    private final UsuarioRepository usuarioRepository;
    private final VendedorRepository vendedorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Realiza o login do usuário
     */
    public AuthResponse login(LoginRequest request) {
        try {
            log.info("Tentativa de login para email: {}", request.getEmail());

            // Buscar usuário
            Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

            // Verificar se está bloqueado
            if (usuario.isBloqueado()) {
                throw new BusinessException("Conta temporariamente bloqueada. Tente novamente mais tarde.");
            }

            // Verificar status
            if (!usuario.isActive()) {
                throw new BusinessException("Conta não está ativa. Verifique seu email ou entre em contato com o suporte.");
            }

            // Autenticar
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // Reset tentativas de login em caso de sucesso
            usuario.setTentativasLogin(0);
            usuario.setBloqueadoAte(null);
            usuario.setUltimoLogin(LocalDateTime.now());
            usuarioRepository.save(usuario);

            // Gerar tokens
            String token = jwtService.generateToken(usuario);
            String refreshToken = jwtService.generateRefreshToken(usuario);

            // Converter para DTO
            UserDto userDto = convertToDto(usuario);

            log.info("Login realizado com sucesso para usuário: {}", usuario.getId());
            return new AuthResponse(token, refreshToken, userDto);

        } catch (AuthenticationException e) {
            handleFailedLogin(request.getEmail());
            throw new BadCredentialsException("Credenciais inválidas");
        }
    }

    /**
     * Registra um novo usuário
     */
    public AuthResponse register(RegisterRequest request) {
        log.info("Tentativa de registro para email: {}", request.getEmail());

        // Verificar se email já existe
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email já está em uso");
        }

        // Criar usuário
        Usuario usuario = new Usuario();
        usuario.setNome(request.getName());
        usuario.setEmail(request.getEmail());
        usuario.setSenhaHash(passwordEncoder.encode(request.getPassword()));
        usuario.setTelefone(request.getPhone());
        usuario.setStatus(UserStatus.ACTIVE); // Mudando para ACTIVE para facilitar os testes
        
        // Por padrão, todo usuário registrado pode ser vendedor e comprador
        usuario.setRoles(Set.of(UserRole.VISITOR, UserRole.PARTICIPANT, UserRole.BUYER, UserRole.SELLER));

        // Salvar usuário
        usuario = usuarioRepository.save(usuario);

        // Criar registro de vendedor automaticamente
        criarVendedorParaUsuario(usuario);

        // Gerar tokens
        String token = jwtService.generateToken(usuario);
        String refreshToken = jwtService.generateRefreshToken(usuario);

        // Converter para DTO
        UserDto userDto = convertToDto(usuario);

        log.info("Usuário registrado com sucesso: {}", usuario.getId());
        return new AuthResponse(token, refreshToken, userDto);
    }

    /**
     * Renova o token usando refresh token
     */
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        try {
            String email = jwtService.extractUsername(request.getRefreshToken());
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

            if (jwtService.isTokenValid(request.getRefreshToken(), usuario)) {
                String newToken = jwtService.generateToken(usuario);
                String newRefreshToken = jwtService.generateRefreshToken(usuario);
                
                UserDto userDto = convertToDto(usuario);
                
                return new AuthResponse(newToken, newRefreshToken, userDto);
            } else {
                throw new BusinessException("Refresh token inválido");
            }
        } catch (Exception e) {
            throw new BusinessException("Erro ao renovar token: " + e.getMessage());
        }
    }

    /**
     * Verifica se email existe
     */
    public boolean checkEmailExists(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    /**
     * Cria registro de vendedor para o usuário
     */
    private void criarVendedorParaUsuario(Usuario usuario) {
        try {
            // Verificar se já existe vendedor para este usuário
            if (!vendedorRepository.existsByUsuarioId(usuario.getId())) {
                Vendedor vendedor = new Vendedor();
                vendedor.setUsuarioId(usuario.getId());
                vendedor.setCompanyName(usuario.getNome()); // Usar nome do usuário como nome da empresa inicialmente
                vendedor.setVerificado(false);
                
                vendedorRepository.save(vendedor);
                log.info("Registro de vendedor criado para usuário: {}", usuario.getId());
            }
        } catch (Exception e) {
            log.error("Erro ao criar registro de vendedor para usuário {}: {}", usuario.getId(), e.getMessage());
            // Não falhar o registro do usuário por causa disso
        }
    }

    /**
     * Trata falha de login
     */
    private void handleFailedLogin(String email) {
        usuarioRepository.findByEmail(email).ifPresent(usuario -> {
            int tentativas = usuario.getTentativasLogin() + 1;
            usuario.setTentativasLogin(tentativas);

            if (tentativas >= MAX_LOGIN_ATTEMPTS) {
                usuario.setBloqueadoAte(LocalDateTime.now().plusMinutes(LOCKOUT_DURATION_MINUTES));
                log.warn("Usuário {} bloqueado por {} tentativas de login", email, tentativas);
            }

            usuarioRepository.save(usuario);
        });
    }

    /**
     * Converte Usuario para UserDto
     */
    public UserDto convertToDto(Usuario usuario) {
        UserDto dto = new UserDto();
        dto.setId(UUID.fromString(usuario.getId()));
        dto.setName(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setPhone(usuario.getTelefone());
        dto.setStatus(usuario.getStatus());
        dto.setRoles(usuario.getRoles());
        dto.setEmailVerificado(usuario.getEmailVerificado());
        dto.setTelefoneVerificado(usuario.getTelefoneVerificado());
        dto.setUltimoLogin(usuario.getUltimoLogin());
        return dto;
    }
}