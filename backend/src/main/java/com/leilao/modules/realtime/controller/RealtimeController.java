package com.leilao.modules.realtime.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Controller para comunicação em tempo real (SSE e WebSocket)
 */
@RestController
@RequestMapping("/realtime")
@CrossOrigin(origins = "*")
public class RealtimeController {

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    /**
     * Endpoint SSE para espectadores (read-only)
     */
    @GetMapping(value = "/sse/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamEvents() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((ex) -> emitters.remove(emitter));
        
        emitters.add(emitter);
        
        // Enviar evento de conexão
        try {
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data("Conectado ao stream de eventos"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
        
        return emitter;
    }

    /**
     * Endpoint para testar broadcast de eventos
     */
    @PostMapping("/broadcast")
    public String broadcastEvent(@RequestParam String message) {
        broadcastToAll("test-event", message);
        return "Evento enviado para " + emitters.size() + " clientes conectados";
    }

    /**
     * Método para broadcast de eventos para todos os clientes SSE conectados
     */
    private void broadcastToAll(String eventName, Object data) {
        emitters.removeIf(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name(eventName)
                        .data(data));
                return false;
            } catch (IOException e) {
                return true; // Remove emitter com erro
            }
        });
    }

    /**
     * Simulação de eventos periódicos para teste
     */
    @PostMapping("/start-simulation")
    public String startSimulation() {
        executor.scheduleAtFixedRate(() -> {
            String message = "Evento simulado - " + System.currentTimeMillis();
            broadcastToAll("simulation", message);
        }, 0, 5, TimeUnit.SECONDS);
        
        return "Simulação iniciada - eventos a cada 5 segundos";
    }
}