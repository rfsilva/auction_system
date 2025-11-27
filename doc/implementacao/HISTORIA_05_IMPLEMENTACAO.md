# 📡 HISTÓRIA 5 - IMPLEMENTAÇÃO SSE/WebSocket

## 📋 Resumo da História
**Tipo:** Enabler  
**Descrição:** Criar canal de comunicação em tempo real para futuras funcionalidades de leilão.  
**Story Points:** 9 SP

### Tasks Implementadas
1. ✅ **Criar endpoint SSE para broadcast de eventos** - 2 SP
2. ✅ **Criar endpoint WebSocket (simples) para testes** - 3 SP  
3. ✅ **Criar cliente Angular para receber eventos de teste** - 2 SP
4. ✅ **Testar latência e concorrência com mock de eventos** - 2 SP

---

## 🏗️ Arquitetura Implementada

### Backend (Spring Boot)
```
backend/src/main/java/com/leilao/modules/realtime/
├── controller/
│   ├── RealtimeController.java      # SSE endpoints
│   └── WebSocketController.java     # WebSocket STOMP endpoints
└── config/
    └── WebSocketConfig.java         # Configuração WebSocket/STOMP
```

### Frontend (Angular)
```
frontend/src/app/
├── core/services/
│   └── realtime.service.ts          # Serviço de comunicação realtime
└── pages/realtime-test/
    ├── realtime-test.component.ts   # Componente de teste
    └── realtime-test.component.scss # Estilos do componente
```

---

## 🔧 Funcionalidades Implementadas

### 1. Server-Sent Events (SSE)
- **Endpoint:** `GET /api/realtime/sse/events`
- **Características:**
  - Conexão unidirecional (servidor → cliente)
  - Reconexão automática
  - Ideal para espectadores
  - Suporte a múltiplos tipos de eventos

**Eventos Suportados:**
- `connected` - Confirmação de conexão
- `test-event` - Eventos de teste manual
- `simulation` - Eventos de simulação automática

### 2. WebSocket com STOMP
- **Endpoint:** `WS /ws`
- **Características:**
  - Comunicação bidirecional
  - Protocolo STOMP para estruturação
  - Ideal para participantes ativos
  - Suporte a tópicos e filas

**Tópicos Disponíveis:**
- `/topic/test` - Mensagens de teste
- `/topic/bids` - Simulação de lances
- `/topic/broadcast` - Broadcast geral
- `/topic/simulation` - Eventos de simulação

**Endpoints de Aplicação:**
- `/app/test` - Envio de mensagens de teste
- `/app/bid` - Envio de lances simulados
- `/app/ping` - Teste de latência

### 3. Cliente Angular Reativo
- **Serviço:** `RealtimeService`
- **Características:**
  - Signals do Angular 17+ para reatividade
  - Gerenciamento automático de conexões
  - Métricas de latência em tempo real
  - Histórico de eventos

---

## 🧪 Testes Implementados

### 1. Teste de Conectividade
- Conexão/desconexão SSE
- Conexão/desconexão WebSocket
- Status em tempo real das conexões

### 2. Teste de Funcionalidade
- Broadcast manual de eventos
- Simulação automática de eventos
- Envio de mensagens via WebSocket
- Simulação de lances

### 3. Teste de Performance
- **Latência:** Medição de tempo de resposta
- **Concorrência:** Envio de múltiplas mensagens simultâneas
- **Estatísticas:** Min, Max, Média de latência

### 4. Testes Automatizados
- Testes unitários para controllers
- Testes de integração para endpoints
- Validação de JSON responses

---

## 🚀 Como Usar

### 1. Iniciar o Backend
```bash
cd backend
mvn spring-boot:run
```

### 2. Iniciar o Frontend
```bash
cd frontend
npm start
```

### 3. Acessar Interface de Teste
- URL: `http://localhost:4200/realtime-test`
- Interface completa para testar todas as funcionalidades

### 4. Endpoints de API
```bash
# SSE Stream
curl -N http://localhost:8080/api/realtime/sse/events

# Broadcast manual
curl -X POST "http://localhost:8080/api/realtime/broadcast?message=teste"

# Iniciar simulação
curl -X POST http://localhost:8080/api/realtime/start-simulation

# Estatísticas
curl http://localhost:8080/api/realtime/stats

# Teste de latência
curl -X POST http://localhost:8080/api/realtime/ping \
  -H "Content-Type: application/json" \
  -d '{"timestamp": 1234567890}'
```

---

## 📊 Métricas e Monitoramento

### Métricas Coletadas
- **Conexões SSE ativas**
- **Eventos enviados/recebidos**
- **Latência de comunicação**
- **Taxa de reconexão**
- **Throughput de mensagens**

### Dashboard de Teste
A interface de teste (`/realtime-test`) fornece:
- Status das conexões em tempo real
- Log de eventos recebidos
- Estatísticas de latência
- Controles para testes de concorrência

---

## 🔍 Detalhes Técnicos

### SSE (Server-Sent Events)
```javascript
// Cliente JavaScript
const eventSource = new EventSource('/api/realtime/sse/events');
eventSource.addEventListener('test-event', (event) => {
  console.log('Evento recebido:', JSON.parse(event.data));
});
```

### WebSocket com STOMP
```javascript
// Envio de mensagem
const frame = `SEND\ndestination:/app/test\n\n${JSON.stringify(data)}\x00`;
websocket.send(frame);

// Subscrição a tópico
const subscribeFrame = `SUBSCRIBE\ndestination:/topic/test\nid:sub-1\n\n\x00`;
websocket.send(subscribeFrame);
```

### Configuração Spring
```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
```

---

## ✅ Critérios de Aceite Atendidos

### História 5 - Sprint 1
- [x] **SSE funcionando como PoC** ✅
  - Endpoint implementado e testado
  - Reconexão automática funcionando
  - Múltiplos tipos de eventos suportados

- [x] **WebSocket funcionando como PoC** ✅
  - Protocolo STOMP implementado
  - Comunicação bidirecional testada
  - Tópicos e subscrições funcionando

- [x] **Cliente Angular implementado** ✅
  - Serviço reativo com Signals
  - Interface de teste completa
  - Gerenciamento automático de conexões

- [x] **Testes de latência e concorrência** ✅
  - Métricas de latência coletadas
  - Testes de concorrência implementados
  - Dashboard de monitoramento funcional

---

## 🔄 Próximos Passos

### Sprint 2 - Integração com Leilões
- Integrar SSE/WebSocket com entidades de leilão
- Implementar eventos específicos de lances
- Adicionar autenticação aos canais WebSocket

### Sprint 3 - Otimizações
- Implementar Redis Pub/Sub para escalabilidade
- Adicionar compressão de mensagens
- Implementar rate limiting

### Sprint 4 - Produção
- Configurar load balancing para WebSocket
- Implementar métricas avançadas
- Adicionar alertas de monitoramento

---

## 🐛 Troubleshooting

### Problemas Comuns

1. **SSE não conecta**
   - Verificar se o backend está rodando
   - Verificar CORS no navegador
   - Verificar logs do servidor

2. **WebSocket falha**
   - Verificar se SockJS está habilitado
   - Verificar configuração de proxy
   - Verificar firewall/proxy corporativo

3. **Eventos não chegam**
   - Verificar subscrições aos tópicos
   - Verificar formato das mensagens
   - Verificar logs de erro no console

### Logs Úteis
```bash
# Backend logs
tail -f backend/logs/application.log | grep -i realtime

# Frontend logs
# Abrir DevTools → Console → Filtrar por "realtime"
```

---

## 📚 Referências

- [Server-Sent Events - MDN](https://developer.mozilla.org/en-US/docs/Web/API/Server-sent_events)
- [WebSocket API - MDN](https://developer.mozilla.org/en-US/docs/Web/API/WebSocket)
- [STOMP Protocol](https://stomp.github.io/)
- [Spring WebSocket Reference](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#websocket)
- [Angular Signals](https://angular.io/guide/signals)

---

**Status:** ✅ **CONCLUÍDO**  
**Data:** Dezembro 2024  
**Responsável:** Sistema de Leilão - Sprint 1