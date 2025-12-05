# 🏁 **Sprint 17 (S17) — Refinamento Técnico**
**Tema:** Integração Avançada do App com Backoffice + Segurança + Auditoria Mobile  
**Duração:** 2 semanas  
**Time:** Dev Mobile + Dev Backend + QA + Arquiteto  
**Objetivo Macro:**  
Garantir que todas as operações sensíveis realizadas via app (lances, pagamentos, atualizações de perfil, consulta de histórico e push notifications) estejam integradas com o backoffice, com trilhas de auditoria robustas, telemetria unificada e conformidade com padrões de segurança definidos no MVP Web.

---

# ✅ **1. Itens de Backlog da Sprint**

## **1.1. Enabler — Integração entre App e Backoffice**
- Definir contratos finais entre App → BFF → Backend → Backoffice.  
- Criar endpoints de sincronização de dados relevantes para operadores internos.  
- Garantir suporte a operações assíncronas (ex.: confirmação de pagamento).

### **Critérios de Aceite**
- Contratos documentados em OpenAPI.  
- Todos os fluxos críticos testados via Postman/Insomnia.  
- Logs de integração funcionais.

---

## **1.2. História — Auditoria Mobile para Ações Sensíveis**
**Como** operador do backoffice  
**Eu quero** rastrear operações sensíveis feitas no app  
**Para** garantir governança e segurança do ambiente.

### Ações que deverão gerar auditoria:
- Lances
- Cancelamento de lance (se existir)
- Atualização de dados do usuário
- Inclusão/remoção de favoritos
- Login/Logout
- Erros de pagamento
- Notificações enviadas/recebidas

### Critérios de Aceite
- Evento auditado segue padrão já definido no MVP Web.  
- Logs enviados para o Observability Hub (ELK/Grafana/Loki).  
- Painel de auditoria atualizado no backoffice.  

---

## **1.3. História — Segurança Mobile (Hardening)**
**Como** arquiteto  
**Eu quero** reforçar a segurança do app  
**Para** reduzir riscos de fraude, automação e engenharia reversa.

### Entregas Técnicas
- Implementação de App Integrity (Google Play Integrity + Apple DeviceCheck).  
- Detecção de root/jailbreak.  
- Encriptação local (Secure Storage).  
- Proteção contra capturas de tela (Android).  
- Token Binding entre app e BFF.

### Critérios de Aceite
- App rejeita execução em dispositivos violados (configurável por feature flag).  
- Token só funciona pelo app oficial.  
- Dados sensíveis não ficam expostos em storage.

---

## **1.4. História — Telemetria do App**
**Como** time de observabilidade  
**Eu quero** coletar métricas e comportamentos no app  
**Para** entender o uso real, gargalos e quedas de conversão.

### Eventos Mínimos
- Sessões  
- Erros/crashes  
- Tempo real no leilão  
- Eventos de clique importantes  
- Funil de participação em leilões  
- Avisos de push recebidos/abertos  

### Critérios de Aceite
- Envio de telemetria para o backend via endpoint dedicado.  
- Dashboards separados para web x mobile.  
- Alertas configurados para erros críticos.

---

## **1.5. Enabler — Infra para Push Notifications Escalável**
**Escopo**
- Criar camada para envio de push com priorização de mensagens (ex.: aviso de início de leilão).  
- Configurar fila/stream (Kafka/SQS/PubSub).  
- Criar API interna para operadores enviarem notificações segmentadas.

### Critérios de Aceite
- Teste de disparo em massa (1k+ usuários simultâneos).  
- Entrega garantida nos principais devices.  
- Registro do push em auditoria.

---

# 🧪 **2. Itens de QA**

- Testes de integração App → BFF → Backend → Backoffice.  
- Teste de carga mínima para push notifications.  
- Teste de auditoria (validar registros).  
- Teste de fluxo de pagamento com telemetria ativa.  
- Testes automatizados (unitários + e2e no mobile se aplicável).

---

# 📊 **3. Riscos & Mitigações**

| Risco | Impacto | Mitigação |
|-------|---------|-----------|
| Divergência entre contratos de API Web x Mobile | Alto | Convergir para OpenAPI único |
| Push travando em picos de leilão | Alto | Implementar fila + retry + DLQ |
| Auditoria duplicada ou faltando | Alto | Criar middleware dedicado |
| Segurança mobile insuficiente | Médio | Hardening + testes com ferramenta de pentest |

---

# 🚀 **4. Saídas Esperadas da Sprint**
- App totalmente alinhado com o backoffice.  
- Auditoria e métricas unificadas com a versão web.  
- App mais seguro e difícil de manipular.  
- Notificações escaláveis, auditadas e integradas.  
- Sistema pronto para S18 (integração de pagamentos mobile avançados + modo offline leve).

---
