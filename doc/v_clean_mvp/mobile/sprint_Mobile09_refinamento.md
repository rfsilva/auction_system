# 🏁 **Sprint 20 (S20) — Refinamento Técnico**
**Tema:** Modo Replay + Clipping de Lances + Destaques do Leilão + Base Premium  
**Duração:** 2 semanas  
**Time:** Dev Mobile + Dev Backend + Dev Front-Web (quando aplicável) + QA + Arquiteto  
**Objetivo Macro:**  
Entregar o **Modo Replay completo**, com recortes (clipping), destaques automáticos, timeline interativa e preparar a base técnica para o futuro **modo premium** do app.

---

# ✅ **1. Itens de Backlog da Sprint**

---

## **1.1. História — Modo Replay Completo do Leilão**
**Como** comprador  
**Eu quero** assistir a um replay completo do leilão  
**Para** entender a dinâmica, revisar oportunidades e aprender para os próximos leilões.

### Escopo Funcional
- Reprodução completa do leilão:
  - sequência histórica dos lances  
  - eventos críticos (início, pico, encerramento)  
  - timecodes sincronizados  
- Controles:
  - play/pause  
  - avanço rápido  
  - marcação de pontos importantes  
- UI dedicada com timeline horizontal.

### Regras
- Dados do replay devem vir de estrutura otimizada (event log).  
- Replay deve funcionar offline quando cache estiver disponível.  
- Replay nunca revela dados pessoais de outros participantes.

### Critérios de Aceite
- Reprodução fiel com tolerância máxima de 200ms.  
- Timeline navegável com precisão.  
- Evento “climax” destacado automaticamente.  
- Dados carregam em menos de 2 segundos.

---

## **1.2. História — Clipping de Lances (Recortes de Trechos Importantes)**
**Como** comprador  
**Eu quero** ver trechos recortados automaticamente  
**Para** acessar rapidamente os momentos mais importantes do leilão.

### Escopo
- Identificação automática dos trechos:
  - sequência de lances rápidos  
  - disputa entre 2+ usuários  
  - aumento significativo  
  - “final eletrizante”  
- Geração de recortes de 5 a 20 segundos.  
- Visualização separada ou dentro do replay.

### Critérios de Aceite
- Pelo menos 3 tipos de clipping devem existir.  
- Trechos devem carregar instantaneamente.  
- Ícones ou labels indicam cada tipo de trecho.

---

## **1.3. História — Destaques do Leilão (Auto Highlights)**
**Como** visitante ou comprador  
**Eu quero** visualizar um resumo destacado do leilão  
**Para** entender rapidamente como foi a disputa.

### Escopo
- Geração de:
  - resumo (texto)  
  - timeline com calor (heatmap de intensidade)  
  - top momentos  
  - top participantes  
- Armazenamento dos destaques para uso futuro no app e web.

### Regras
- Destaques são gerados automaticamente ao final.  
- Conteúdo pode ser reprocessado caso regras mudem.

### Critérios de Aceite
- Destaques aparecem em <= 30 segundos após o encerramento do leilão.  
- Ranking e calor devem refletir o real comportamento dos lances.  
- Não expor dados sensíveis.

---

## **1.4. Enabler — Event Log Otimizado para Replay**
### Entregas
- Reestruturação/normalização do log de eventos:
  - `auction_event_log`  
  - compressão por batching  
  - marcação de eventos críticos  
- Pré-indexação dos eventos:
  - timeline real  
  - timeline computada  
  - índice de navegação rápida  
- Criação de serializer rápido para replay.

### Critérios de Aceite
- Carregamento do replay precisa consultar apenas **1 endpoint**.  
- Resposta com tamanho máximo de 500 KB (para UX rápida).  
- Backend deve aguentar centenas de replays simultâneos.

---

## **1.5. Enabler — Base Técnica para Modo Premium**
### Objetivo
Preparar a arquitetura para, futuramente, liberar funcionalidades premium pagas.

### Escopo
- Flags de feature por usuário.  
- Controle de acesso granular (ex: replay completo ser premium).  
- Base para “paywall controlado”:  
  - controle de trial  
  - camadas de acesso diferenciadas  
- API e contratos preparados.

### Critérios de Aceite
- Nenhuma feature atual é bloqueada.  
- Estrutura flexível para add novas features depois.  
- Documentação da estrutura premium entregue.

---

# 🧪 **2. Itens de QA**

- Testes do replay com diferentes tamanhos de leilão.  
- Teste de clipping automático com cenários variados.  
- Testes de performance do event log.  
- Testes de concorrência em replays simultâneos.  
- Teste de carregamento offline (quando aplicável).  
- Teste de regressão visual da timeline e heatmap.

---

# 📊 **3. Riscos & Mitigações**

| Risco | Impacto | Mitigação |
|-------|---------|-----------|
| Replay pesado afetar o app | Alto | Compressão + pré-indexação + streaming leve |
| Clipping gerar trechos irrelevantes | Médio | Ajustar heurísticas e regras de detecção |
| Destaques demorarem para processar | Médio | Criar worker assíncrono dedicado |
| Estrutura premium conflitar com UX atual | Médio | Feature Flags + dado sempre opcional |

---

# 🚀 **4. Saídas Esperadas da Sprint**
- Modo Replay completo funcionando com excelente UX.  
- Clipping automático integrado ao replay.  
- Destaques totalmente funcional para cada leilão.  
- Base premium **ativada**, porém sem bloquear nada.  
- App e backend preparados para a evolução da versão 2.0+.

---
