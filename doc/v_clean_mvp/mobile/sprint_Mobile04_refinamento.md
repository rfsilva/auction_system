# 🏁 Sprint 15 — Leilões em Tempo Real (Mobile) + Push de Eventos Críticos
**Duração:** 2 semanas  
**Equipe:** Mobile + Backend + Arquiteto  
**Objetivo:** Entregar a capacidade completa de participar de leilões em tempo real pelo app, com SSE/WebSocket, atualização instantânea de lances e push notifications críticas.

---

# 🔥 HISTÓRIAS REFINADAS (S15)

---

## **H15.1 — Criar canal SSE/WebSocket dedicado para o App**
**Descrição:** Adaptar o canal de eventos do leilão (já existente no Web) para permitir consumo eficiente via app.

### Tarefas
- [ ] Criar endpoint `/api/mobile/auction/{id}/stream`
- [ ] Converter eventos Web → Mobile DTO
- [ ] Criar throttle para evitar overload no app
- [ ] Documentar novo contrato mobile
- [ ] Criar testes unitários e integrados do streaming

### Critérios de Aceite
- App deve receber eventos em < 200 ms após disparo
- Conexão deve se recuperar automaticamente (reconnect)
- Eventos só incluem o necessário (redução para ~30% do payload do Web)

### Tamanho
**8 pontos**

---

## **H15.2 — Implementar camada de consumo SSE/WebSocket no App**
**Descrição:** Criar serviço mobile para receber eventos de lances, encerramento, líder atual e preço.

### Tarefas
- [ ] Criar `RealTimeAuctionService`
- [ ] Implementar reconnect exponencial (1s → 2s → 4s → 8s)
- [ ] Criar store interna (estado reativo)
- [ ] Integrar com UI
- [ ] Criar logs offline
- [ ] Testar comportamento em background

### Critérios de Aceite
- App deve atualizar lance atual automaticamente sem refresh manual
- App deve manter estado mesmo com perda temporária de conexão
- Logs devem registrar reconexões

### Tamanho
**13 pontos**

---

## **H15.3 — Criar UI de leilão em tempo real no App**
**Descrição:** Construir a tela dinâmica com todas as informações necessárias para o comprador participar pelo app.

### Componentes
- Indicador ao vivo (“LIVE”)
- Valor atual do lance
- Tempo restante com contagem regressiva
- Histórico de lances (ultimos 10)
- Botão “Dar lance”
- Indicador de líder
- Painel de regras rápidas

### Tarefas
- [ ] Criar layout responsivo
- [ ] Animar atualização de preço
- [ ] Exibir aviso de “conexão fraca”
- [ ] Integrar com store de eventos
- [ ] Testes visuais

### Critérios de Aceite
- Tela deve atualizar em tempo real sem travar
- Contador não pode ter drift maior que 1s
- UI deve seguir estilo do design system mobile

### Tamanho
**13 pontos**

---

## **H15.4 — Implementar fluxo de envio de lances pelo App**
**Descrição:** Criar chamada segura e resiliente ao backend, respeitando fila e lock otimizados.

### Tarefas
- [ ] Criar endpoint `/api/mobile/auction/{id}/bid`
- [ ] Criar DTO reduzido (valor + deviceId + timestamp local)
- [ ] Validar limite de tentativas (rate limit)
- [ ] Tratar erros específicos (superado / encerrado / inválido)
- [ ] Integrar com SSE (para feedback imediato)

### Critérios de Aceite
- Resposta deve retornar em ≤200 ms
- Erros devem ser exibidos de forma amigável
- Lance deve aparecer no streaming imediatamente

### Tamanho
**8 pontos**

---

## **H15.5 — Push notification: alerta de “leilão prestes a começar” (mobile)**
**Descrição:** Enviar push para o usuário que marcou interesse no item.

### Tarefas
- [ ] Criar cron job 10 min antes do início
- [ ] Criar payload do push (simples, leve)
- [ ] Implementar opt-in/out por item
- [ ] Atualizar tabela `user_interest`
- [ ] Testes integrados

### Critérios de Aceite
- Push enviado exatamente 10 min antes do leilão
- Push só vai para usuários com opt-in ativo
- Push não dispara se o usuário já estiver online no Web/App

### Tamanho
**5 pontos**

---

## **H15.6 — Push notification: lance superado (outbid)**
**Descrição:** Enviar push para o comprador caso ele deixe de ser o líder.

### Tarefas
- [ ] Criar evento `OutbidEvent`
- [ ] Criar worker para gerar push
- [ ] Evitar spam (cooldown de 30s)
- [ ] Adicionar motivo no payload (valor atual, id do item)
- [ ] Testar fluxo E2E

### Critérios de Aceite
- Usuário não recebe mais que 1 push a cada 30s
- Mensagem deve indicar novo valor atual
- Push não deve ser enviado se a aba do app estiver aberta e recebendo SSE

### Tamanho
**8 pontos**

---

## **H15.7 — Enabler: Monitoramento e Métricas Reais de SSE/WebSocket**
**Descrição:** Permitir observar performance, quedas e reconexões do canal mobile.

### Tarefas
- [ ] Instrumentar Prometheus:  
  - conexões ativas  
  - médias de reconexão  
  - eventos enviados  
  - latência
- [ ] Criar dashboard no Grafana
- [ ] Criar alertas (latência >300ms)

### Critérios de Aceite
- Dashboard atualizado a cada 10s
- Alertas funcionando corretamente
- Métricas separadas Web vs Mobile

### Tamanho
**5 pontos**

---

## **H15.8 — Enabler: Testes de estresse no canal mobile de tempo real**
**Descrição:** Validar carga simultânea e comportamento em cenários de pico.

### Tarefas
- [ ] Simular 500 conexões simultâneas mobile
- [ ] Testar reconexões
- [ ] Testar burst de lances (pico)
- [ ] Gerar relatório técnico

### Critérios de Aceite
- Sistema deve suportar ≥ 500 conexões sem queda
- Eventos devem ser distribuídos com desvio < 300ms
- Nenhuma perda de evento é tolerada

### Tamanho
**8 pontos**

---

# 📌 RESUMO DA SPRINT

| Item   | Pontos |
|--------|--------|
| H15.1  | 8 |
| H15.2  | 13 |
| H15.3  | 13 |
| H15.4  | 8 |
| H15.5  | 5 |
| H15.6  | 8 |
| H15.7  | 5 |
| H15.8  | 8 |
| **Total** | **68 pontos** |

> Sprint pesada, truta — mas é o núcleo absoluto da experiência mobile de leilão em tempo real.

