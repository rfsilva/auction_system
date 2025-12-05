# 🏁 **Sprint 18 (S18) — Refinamento Técnico**
**Tema:** Pagamentos Mobile + Modo Offline Parcial + Otimizações de UX em Leilões  
**Duração:** 2 semanas  
**Time:** Dev Mobile + Dev Backend + QA + Arquiteto  
**Objetivo Macro:**  
Concluir a parte avançada de pagamentos no app, adicionar capacidades offline para consultas e pré-carregamento, otimizar a UX do modo de disputa em tempo real e trazer estabilidade operacional para momentos de pico.

---

# ✅ **1. Itens de Backlog da Sprint**

## **1.1. História — Pagamentos Mobile (Checkout Nativo / Webview Segura)**
**Como** comprador  
**Eu quero** realizar pagamento diretamente pelo app  
**Para** concluir minhas compras de forma segura e rápida.

### Escopo
- Suporte a **Cartão**, **PIX** e **boleto** como já previsto no MVP Web.  
- Uso de Webview segura **ou** flow nativo (dependendo do PSP).  
- Tokenização do cartão (se o PSP permitir).  
- Callback para confirmação de pagamento → App → BFF → Backend.

### Critérios de Aceite
- Pagamento concluído com retorno imediato de status.  
- Logs e auditoria alinhados à sprint S17.  
- Telemetria de erros do fluxo implantada.  
- Proteção contra double-submit.

---

## **1.2. História — Modo Offline Parcial**
**Como** comprador  
**Eu quero** navegar em algumas partes do app sem internet  
**Para** não perder tempo enquanto estou com sinal fraco.

### Dados disponíveis offline:
- Últimos leilões visitados  
- Catálogo pré-carregado  
- Favoritos  
- Perfil do usuário  
- Histórico básico (não atualiza sem internet)

### Restrições:
- Lances só funcionam online  
- Pagamento só funciona online  
- SSE/WebSocket só conecta com rede ativa

### Entregas Técnicas
- Implementar storage com IndexedDB / SQLite (dependendo de plataforma).  
- Criar serviço MobileSync com política de atualização (LRU + TTL).  
- Diferenciar UI offline vs online.

### Critérios de Aceite
- App detecta estado offline e troca UI automaticamente.  
- Tela de leilão carrega mesmo offline (modo leitura).  
- Cache respeita TTL configurável por feature flag.

---

## **1.3. História — Otimizações de UX no Leilão em Tempo Real**
**Como** comprador  
**Eu quero** uma experiência mais fluida no leilão ao vivo  
**Para** ter mais confiança e velocidade nos lances.

### Melhorias
- Atualização visual com batching (evitar flicker).  
- Estado do lance destacado com animação leve.  
- Timer calibrado com compensação de clock.  
- Aviso prévio quando a conexão estiver instável.

### Critérios de Aceite
- Lances chegam em < 300ms em condições normais.  
- Interface não trava com picos de mensagens SSE/WebSocket.  
- Testes de stress aprovados (500 mensagens/s).

---

## **1.4. Enabler — Sincronização Bidirecional App ↔ Backend**
**Escopo**
- Modelo de sincronização incremental.  
- Mecanismo de “Delta Sync” para reduzir tráfego.  
- Registro de conflitos (ex.: favorito removido no web).

### Critérios de Aceite
- Sincronização ocorre em background sem travar UI.  
- Delta de dados reduz consumo de rede.  
- Conflitos aparecem em log e são resolvidos via regra automática.

---

## **1.5. Enabler — Redução de Consumo de Bateria**
**Escopo Técnico**
- Otimizar frequência de polling (fallback ao SSE).  
- Desligar animações intensas em modo “baixo consumo”.  
- Ajustar intervalos de background sync.  

### Critérios de Aceite
- App passa em benchmark interno comparado à versão S17.  
- Conexões persistentes fecham corretamente no background.  
- Economia de pelo menos 15% de uso de bateria.

---

# 🧪 **2. Itens de QA**

- Testes de pagamento mobile (fluxos completos e falha).  
- Testes de modo offline (cache, fallback, sincronização).  
- Testes de leilão com stress WebSocket/SSE.  
- Testes de UX e acessibilidade.  
- Teste de performance e bateria.  

---

# 📊 **3. Riscos & Mitigações**

| Risco | Impacto | Mitigação |
|-------|---------|-----------|
| PSP mobile com comportamento inconsistente | Alto | Habilitar fallback via WebView segura |
| Modo offline causando dados desatualizados | Alto | TTL + Delta Sync + flags |
| Picos de SSE/WebSocket travando UI | Alto | Batching + deboucing + virtual DOM |
| Consumo de bateria acima do esperado | Médio | Desligar animações + otimizar ciclos de sync |

---

# 🚀 **4. Saídas Esperadas da Sprint**
- Pagamentos mobile funcionando ponta a ponta.  
- App com capacidade offline parcial avançada.  
- Leilão mais fluido, estável e reativo.  
- Sincronização mobile robusta e eficiente.  
- Base técnica para S19: **ranking ao vivo, gamificação e push avançado por segmento**.

---
