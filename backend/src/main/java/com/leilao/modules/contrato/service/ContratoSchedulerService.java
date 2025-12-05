package com.leilao.modules.contrato.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Service para tarefas agendadas relacionadas a contratos
 * História 3: Relatórios de Contratos Vencendo - Sprint S2.2
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ContratoSchedulerService {

    private final ContratoService contratoService;
    private final ContratoVencendoService contratoVencendoService;

    /**
     * Atualiza contratos expirados a cada hora
     * Executa a cada hora (3600000 ms)
     */
    @Scheduled(fixedRate = 3600000)
    public void atualizarContratosExpirados() {
        try {
            log.info("Iniciando verificação de contratos expirados");
            long startTime = System.currentTimeMillis();
            
            contratoService.atualizarContratosExpirados();
            
            long duration = System.currentTimeMillis() - startTime;
            log.info("Verificação de contratos expirados concluída em {}ms", duration);
            
        } catch (Exception e) {
            log.error("Erro ao atualizar contratos expirados", e);
        }
    }

    /**
     * Verifica contratos próximos ao vencimento e envia notificações
     * Executa diariamente às 9h (cron: segundo minuto hora dia mês dia-da-semana)
     * História 3: Implementar notificações automáticas para contratos vencendo
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void verificarContratosProximosVencimento() {
        try {
            log.info("Iniciando verificação de contratos próximos ao vencimento");
            long startTime = System.currentTimeMillis();
            
            // Enviar notificações automáticas para contratos vencendo
            contratoVencendoService.enviarNotificacoesAutomaticas();
            
            long duration = System.currentTimeMillis() - startTime;
            log.info("Verificação de contratos próximos ao vencimento concluída em {}ms", duration);
            
        } catch (Exception e) {
            log.error("Erro ao verificar contratos próximos ao vencimento", e);
        }
    }

    /**
     * Executa limpeza de dados antigos semanalmente
     * Executa aos domingos às 2h da manhã
     */
    @Scheduled(cron = "0 0 2 * * SUN")
    public void limpezaSemanal() {
        try {
            log.info("Iniciando limpeza semanal de dados de contratos");
            long startTime = System.currentTimeMillis();
            
            // Aqui poderia implementar limpeza de logs antigos, cache, etc.
            // Por enquanto, apenas log informativo
            log.info("Limpeza semanal executada (placeholder para futuras implementações)");
            
            long duration = System.currentTimeMillis() - startTime;
            log.info("Limpeza semanal concluída em {}ms", duration);
            
        } catch (Exception e) {
            log.error("Erro durante limpeza semanal", e);
        }
    }

    /**
     * Verifica saúde do sistema de notificações
     * Executa a cada 6 horas
     */
    @Scheduled(fixedRate = 21600000) // 6 horas em millisegundos
    public void verificarSaudeNotificacoes() {
        try {
            log.debug("Verificando saúde do sistema de notificações");
            
            // Aqui poderia implementar verificações de saúde:
            // - Conectividade com servidor de email
            // - Status do Redis
            // - Filas de notificação
            
            log.debug("Verificação de saúde das notificações concluída");
            
        } catch (Exception e) {
            log.warn("Problema detectado na verificação de saúde das notificações", e);
        }
    }
}