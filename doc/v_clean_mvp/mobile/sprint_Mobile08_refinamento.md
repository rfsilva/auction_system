# 🏁 **Sprint 19 (S19) — Refinamento Técnico**
**Tema:** Ranking ao Vivo + Gamificação + Push Inteligente  
**Duração:** 2 semanas  
**Time:** Dev Mobile + Dev Backend + QA + Arquiteto  
**Objetivo Macro:**  
Estender a experiência do app criando **engajamento ativo**: ranking dinâmico, gamificação para compradores, notificações inteligentes e melhorias na camada de leilão ao vivo.

---

# ✅ **1. Itens de Backlog da Sprint**

---

## **1.1. História — Ranking ao Vivo dos Participantes**
**Como** comprador  
**Eu quero** visualizar o ranking de participantes durante um leilão  
**Para** entender minha posição e acompanhar a dinâmica competitiva.

### Escopo
- Ranking atualizado via SSE/WebSocket.  
- Exposição de métricas por leilão:
  - posição do usuário  
  - quantidade de lances  
  - agressividade (velocidade entre lances)  
  - maior lance já ofertado  
- Exibição visual otimizada e com animação leve.

### Critérios de Aceite
- Ranking atualiza em **tempo real** (max 1s de atraso).  
- UI se ajusta a listas com +100 participantes.  
- Usuário vê sempre sua posição mesmo se estiver longe do topo.  
- Performance alta com batching de mensagens.

---

## **1.2. História — Sistema de Gamificação (Badges + XP + Nível do Comprador)**
**Como** comprador  
**Eu quero** ganhar badges e XP pelas minhas ações  
**Para** ser recompensado e engajar mais nos leilões.

### Regras sugeridas
- Badge: **Primeiro Lance**  
- Badge: **Lance Relâmpago (<1s após aumento)**  
- Badge: **Top 3 no Ranking**  
- Badge: **Participante Frequente (10 leilões)**  
- XP por:
  - participar  
  - vencer  
  - lances válidos  
  - comportamento estável

### Critérios de Aceite
- XP atualizado em background.  
- Badge aparece imediatamente na UI quando desbloqueado.  
- Sincronização com backend via Delta Sync.  
- Regras documentadas para auditoria.

---

## **1.3. História — Push Inteligente (Segmentação + Tipo de Conteúdo)**
**Como** comprador  
**Eu quero** receber notificações relevantes  
**Para** saber quando um leilão que me interessa está acontecendo ou prestes a acabar.

### Tipos de push:
- “Leilão que você acompanha está esquentando!”  
- “Seu favorito baixou preço/entrada!”  
- “Últimos 3 minutos do leilão X!”  
- Segmentação por:
  - histórico  
  - favoritos  
  - categorias  
  - engajamento  
  - comportamento hora/dia

### Critérios de Aceite
- Push chega em até **5 segundos** após trigger.  
- Abuse-prevention: máximo 3 notificações por hora.  
- Logs disponíveis no backend e no app.  
- UI permite configurar granularidade pelo usuário (mínimo: on/off).

---

## **1.4. Enabler — Otimização da Estrutura SSE/WebSocket para Mensagens de Alto Volume**
### Escopo
- Reducer centralizado de eventos.  
- Compressão leve de payloads críticos.  
- Introdução de message throttling inteligente.

### Critérios de Aceite
- Suporte a picos de 1000 mensagens/minute por sessão.  
- UI permanece fluida durante o consumo.  
- Backend mantém fila estável sem gargalos.

---

## **1.5. Enabler — Camada de Persistência de Gamificação**
### Entregas
- Tabelas e entidades:
  - `user_badge`  
  - `user_xp`  
  - `badge_rules`  
- Versionamento de regras com controle temporal (auto histórico).  
- Integração com Envers (caso ativo na arquitetura).

### Critérios de Aceite
- Estrutura preparada para novas regras no futuro.  
- Zero downtime para ajustes.  
- Auditoria completa.

---

# 🧪 **2. Itens de QA**

- Testes do ranking ao vivo com volume alto e usuários concorrentes.  
- Teste completo de gamificação: desbloqueio, atualização, sincronização.  
- Testes de push (Android + iOS) — foreground, background e app fechado.  
- Teste de estabilidade nos canais SSE/WebSocket.  
- Teste de regras de throttle dos pushs.

---

# 📊 **3. Riscos & Mitigações**

| Risco | Impacto | Mitigação |
|-------|---------|-----------|
| Excesso de notificações irritar o usuário | Alto | Regras de limite + personalização de push |
| Ranking causar muita carga no backend | Alto | Batching, throttling e compressão |
| Gamificação mal calibrada gerar frustração | Médio | Sistema de XP rápido de ajustar via configuração |
| UI travar com muitas mensagens SSE | Alto | Reduzir FPS do loop + debouncing + lazy update |

---

# 🚀 **4. Saídas Esperadas da Sprint**
- Ranking ao vivo 100% funcional.  
- Sistema inicial de gamificação implantado.  
- Push inteligente operacional.  
- Arquitetura de SSE/WebSocket reforçada para alto volume.  
- Base preparada para Sprint 20: **modo replay, destaques, clipping de lances e módulos premium**.

---
