package com.leilao.modules.auth.service;

import com.leilao.modules.auth.dto.LoginRequest;
import com.leilao.modules.auth.dto.RegisterRequest;
import com.leilao.modules.auth.dto.AuthResponse;
import com.leilao.modules.auth.entity.Usuario;
import com.leilao.modules.auth.repository.UsuarioRepository;
import com.leilao.shared.enums.UserRole;
import com.leilao.shared.enums.UserStatus;
import com.leilao.shared.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private Usuario usuario;
    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(UUID.randomUUID());
        usuario.setNome("João Silva");
        usuario.setEmail("joao@teste.com");
        usuario.setSenhaHash("$2a$10$hashedPassword");
        usuario.setStatus(UserStatus.ACTIVE);
        usuario.setRoles(Set.of(UserRole.PARTICIPANT));
        usuario.setEmailVerificado(true);
        usuario.setTentativasLogin(0);

        loginRequest = new LoginRequest("joao@teste.com", "password123");
        registerRequest = new RegisterRequest("João Silva", "joao@teste.com", "password123", "11999999999");
    }

    @Test
    void login_ComCredenciaisValidas_DeveRetornarAuthResponse() {
        // Arrange
        when(usuarioRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(usuario));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtService.generateToken(usuario)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(usuario)).thenReturn("refresh-token");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        AuthResponse response = authService.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("access-token", response.getToken());
        assertEquals("refresh-token", response.getRefreshToken());
        assertNotNull(response.getUser());
        assertEquals(usuario.getNome(), response.getUser().getName());
        assertEquals(usuario.getEmail(), response.getUser().getEmail());

        verify(usuarioRepository).save(usuario);
        assertEquals(0, usuario.getTentativasLogin());
        assertNotNull(usuario.getUltimoLogin());
    }

    @Test
    void login_ComUsuarioInexistente_DeveLancarBadCredentialsException() {
        // Arrange
        when(usuarioRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    void login_ComUsuarioBloqueado_DeveLancarBusinessException() {
        // Arrange
        usuario.setBloqueadoAte(LocalDateTime.now().plusMinutes(30));
        when(usuarioRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(usuario));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, 
                () -> authService.login(loginRequest));
        assertTrue(exception.getMessage().contains("temporariamente bloqueada"));
        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    void login_ComUsuarioInativo_DeveLancarBusinessException() {
        // Arrange
        usuario.setStatus(UserStatus.INACTIVE);
        when(usuarioRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(usuario));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, 
                () -> authService.login(loginRequest));
        assertTrue(exception.getMessage().contains("não está ativa"));
        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    void login_ComCredenciaisInvalidas_DeveIncrementarTentativas() {
        // Arrange
        when(usuarioRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(usuario));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
        
        verify(usuarioRepository).save(usuario);
        assertEquals(1, usuario.getTentativasLogin());
    }

    @Test
    void login_ComMuitasTentativasFalhas_DeveBloquerUsuario() {
        // Arrange
        usuario.setTentativasLogin(4); // Próxima tentativa será a 5ª
        when(usuarioRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(usuario));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
        
        verify(usuarioRepository).save(usuario);
        assertEquals(5, usuario.getTentativasLogin());
        assertNotNull(usuario.getBloqueadoAte());
        assertTrue(usuario.getBloqueadoAte().isAfter(LocalDateTime.now()));
    }

    @Test
    void register_ComDadosValidos_DeveRetornarAuthResponse() {
        // Arrange
        when(usuarioRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("$2a$10$hashedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(jwtService.generateToken(any(Usuario.class))).thenReturn("access-token");
        when(jwtService.generateRefreshToken(any(Usuario.class))).thenReturn("refresh-token");

        // Act
        AuthResponse response = authService.register(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals("access-token", response.getToken());
        assertEquals("refresh-token", response.getRefreshToken());
        assertNotNull(response.getUser());

        verify(usuarioRepository).save(any(Usuario.class));
        verify(passwordEncoder).encode(registerRequest.getPassword());
    }

    @Test
    void register_ComEmailExistente_DeveLancarBusinessException() {
        // Arrange
        when(usuarioRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, 
                () -> authService.register(registerRequest));
        assertEquals("Email já está em uso", exception.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void checkEmailExists_ComEmailExistente_DeveRetornarTrue() {
        // Arrange
        when(usuarioRepository.existsByEmail("joao@teste.com")).thenReturn(true);

        // Act
        boolean exists = authService.checkEmailExists("joao@teste.com");

        // Assert
        assertTrue(exists);
    }

    @Test
    void checkEmailExists_ComEmailInexistente_DeveRetornarFalse() {
        // Arrange
        when(usuarioRepository.existsByEmail("inexistente@teste.com")).thenReturn(false);

        // Act
        boolean exists = authService.checkEmailExists("inexistente@teste.com");

        // Assert
        assertFalse(exists);
    }
}