package com.leilao.core.config;

import com.leilao.shared.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Health indicator customizado para email que não falha em desenvolvimento
 */
@Component("mail")
@ConditionalOnProperty(name = "management.health.mail.enabled", havingValue = "true", matchIfMissing = false)
@RequiredArgsConstructor
public class CustomMailHealthIndicator implements HealthIndicator {

    private final EmailService emailService;

    @Override
    public Health health() {
        try {
            boolean connected = emailService.testConnection();
            
            if (connected) {
                return Health.up()
                        .withDetail("status", "Email service is available")
                        .build();
            } else {
                return Health.down()
                        .withDetail("status", "Email service connection failed")
                        .build();
            }
            
        } catch (Exception e) {
            return Health.down()
                    .withDetail("status", "Email service error")
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}