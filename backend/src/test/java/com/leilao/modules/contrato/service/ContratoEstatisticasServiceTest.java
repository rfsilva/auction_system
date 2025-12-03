package com.leilao.modules.contrato.service;

import com.leilao.modules.contrato.dto.ContratoEstatisticasDto;
import com.leilao.modules.contrato.entity.Contrato;
import com.leilao.modules.contrato.repository.ContratoEstatisticasRepository;
import com.leilao.modules.contrato.repository.ContratoRepository;
import com.leilao.modules.vendedor.service.VendedorService;
import com.leilao.shared.enums.ContractStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para ContratoEstatisticasService
 * História 1: Endpoints de Estatísticas de Contratos - Sprint S2.2
 */
@ExtendWith(MockitoExtension.class)
class ContratoEstatisticasServiceTest {

    @Mock
    private ContratoRepository contratoRepository;

    @Mock
    private ContratoEstatisticasRepository estatisticasRepository;

    @Mock
    private VendedorService vendedorService;

    @InjectMocks
    private ContratoEstatisticasService estatisticasService;

    private LocalDateTime agora;

    @BeforeEach
    void setUp() {
        agora = LocalDateTime.now();
    }

    @Test
    void obterEstatisticasContratos_DeveRetornarEstatisticasCompletas() {
        // Arrange
        when(contratoRepository.count()).thenReturn(150L);
        when(estatisticasRepository.countVendedoresAtivos(any())).thenReturn(85L);
        when(estatisticasRepository.getMediaTaxaComissaoAtivos(any())).thenReturn(new BigDecimal("0.065"));
        when(estatisticasRepository.countContratosVencendoEm(any(), any())).thenReturn(12L);
        when(estatisticasRepository.countContratosCriadosNoPeriodo(any(), any())).thenReturn(25L);
        when(estatisticasRepository.countContratosExpiradosNoPeriodo(any(), any())).thenReturn(8L);
        when(estatisticasRepository.countCategoriasAtivas(any())).thenReturn(15L);

        // Mock para estatísticas por status
        List<Object[]> statusStats = Arrays.asList(
                new Object[]{ContractStatus.ACTIVE, 120L},
                new Object[]{ContractStatus.DRAFT, 15L},
                new Object[]{ContractStatus.EXPIRED, 10L},
                new Object[]{ContractStatus.CANCELLED, 5L}
        );
        when(estatisticasRepository.getEstatisticasContratosPorStatus()).thenReturn(statusStats);

        // Mock para contratos ativos (para receita projetada)
        List<Contrato> contratosAtivos = Arrays.asList(
                criarContratoMock("1", new BigDecimal("0.05")),
                criarContratoMock("2", new BigDecimal("0.07")),
                criarContratoMock("3", new BigDecimal("0.06"))
        );
        when(estatisticasRepository.findContratosAtivosParaReceita(any())).thenReturn(contratosAtivos);

        // Act
        ContratoEstatisticasDto resultado = estatisticasService.obterEstatisticasContratos();

        // Assert
        assertNotNull(resultado);
        assertEquals(150L, resultado.getTotalContratos());
        assertEquals(85L, resultado.getVendedoresAtivos());
        assertEquals(new BigDecimal("0.065"), resultado.getTaxaMediaComissao());
        assertEquals(12L, resultado.getContratosVencendo30Dias());
        assertEquals(25L, resultado.getContratosCriadosMes());
        assertEquals(8L, resultado.getContratosExpiradosMes());
        assertEquals(15L, resultado.getCategoriasAtivas());

        // Verificar estatísticas por status
        assertNotNull(resultado.getContratosPorStatus());
        assertEquals(120L, resultado.getContratosPorStatus().get(ContractStatus.ACTIVE));
        assertEquals(15L, resultado.getContratosPorStatus().get(ContractStatus.DRAFT));
        assertEquals(10L, resultado.getContratosPorStatus().get(ContractStatus.EXPIRED));
        assertEquals(5L, resultado.getContratosPorStatus().get(ContractStatus.CANCELLED));

        // Verificar receita projetada (baseada nos contratos mock)
        assertNotNull(resultado.getReceitaProjetadaMes());
        assertTrue(resultado.getReceitaProjetadaMes().compareTo(BigDecimal.ZERO) > 0);

        // Verificar receita realizada (simulada)
        assertNotNull(resultado.getReceitaRealizadaMes());
        assertEquals(new BigDecimal("25000.00"), resultado.getReceitaRealizadaMes());
    }

    @Test
    void obterEstatisticasContratos_ComTaxaMediaNull_DeveUsarZero() {
        // Arrange
        when(contratoRepository.count()).thenReturn(10L);
        when(estatisticasRepository.countVendedoresAtivos(any())).thenReturn(5L);
        when(estatisticasRepository.getMediaTaxaComissaoAtivos(any())).thenReturn(null); // Taxa nula
        when(estatisticasRepository.countContratosVencendoEm(any(), any())).thenReturn(2L);
        when(estatisticasRepository.countContratosCriadosNoPeriodo(any(), any())).thenReturn(3L);
        when(estatisticasRepository.countContratosExpiradosNoPeriodo(any(), any())).thenReturn(1L);
        when(estatisticasRepository.countCategoriasAtivas(any())).thenReturn(5L);
        when(estatisticasRepository.getEstatisticasContratosPorStatus()).thenReturn(Arrays.asList());
        when(estatisticasRepository.findContratosAtivosParaReceita(any())).thenReturn(Arrays.asList());

        // Act
        ContratoEstatisticasDto resultado = estatisticasService.obterEstatisticasContratos();

        // Assert
        assertNotNull(resultado);
        assertEquals(BigDecimal.ZERO, resultado.getTaxaMediaComissao());
    }

    @Test
    void obterEstatisticasContratos_ComErroNaConsulta_DeveLancarException() {
        // Arrange
        when(contratoRepository.count()).thenThrow(new RuntimeException("Erro de banco"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            estatisticasService.obterEstatisticasContratos();
        });

        assertEquals("Erro ao calcular estatísticas de contratos", exception.getMessage());
    }

    private Contrato criarContratoMock(String id, BigDecimal feeRate) {
        Contrato contrato = new Contrato();
        contrato.setId(id);
        contrato.setFeeRate(feeRate);
        contrato.setStatus(ContractStatus.ACTIVE);
        contrato.setActive(true);
        return contrato;
    }
}