package com.leilao.modules.contrato.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Service para tarefas agendadas relacionadas a contratos
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ContratoSchedulerService {

    private final ContratoService contratoService;

    /**
     * Atualiza contratos expirados a cada hora
     */
    @Scheduled(fixedRate = 3600000) // 1 hora em millisegundos
    public void atualizarContratosExpirados() {
        try {
            log.info("Iniciando verificação de contratos expirados");
            contratoService.atualizarContratosExpirados();
            log.info("Verificação de contratos expirados concluída");
        } catch (Exception e) {
            log.error("Erro ao atualizar contratos expirados", e);
        }
    }

    /**
     * Verifica contratos que expiram em 7 dias (diário às 9h)
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void verificarContratosProximosVencimento() {
        try {
            log.info("Iniciando verificação de contratos próximos ao vencimento");
            // TODO: Implementar notificação de contratos próximos ao vencimento
            log.info("Verificação de contratos próximos ao vencimento concluída");
        } catch (Exception e) {
            log.error("Erro ao verificar contratos próximos ao vencimento", e);
        }
    }
}