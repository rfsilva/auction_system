# Arquitetura de Solução — Sistema de Leilão Eletrônico

**Documento Técnico Consolidado em Markdown**

---

# 🏗️ Arquitetura de Solução — Sistema de Leilão Eletrônico

**Documento Técnico Consolidado (com diferenciais)**

## 📌 Sumário

1. Visão Geral
2. Visão Funcional
3. Requisitos Técnicos
4. Arquitetura Lógica
5. Arquitetura Física / Infraestrutura
6. Comunicação Frontend ↔ Backend (SSE vs WebSocket)
7. Fluxos Críticos
8. Considerações de Alta Disponibilidade
9. Segurança e Compliance
10. Diferenciais Avançados (incorporados)
11. Roadmap Evolutivo

---

# 1. 🎯 Visão Geral

O sistema tem como objetivo permitir **leilões eletrônicos em tempo real**, suportando:

* Lances simultâneos
* Atualização instantânea dos preços
* Fluxo seguro, auditável e confiável
* Escalabilidade horizontal para múltiplos leilões ativos

O foco do MVP é garantir **experiência fluida**, **baixa latência** e **consistência absoluta dos lances**.

---

# 2. 🧩 Visão Funcional

## Funcionalidades principais (MVP)

### **Para Compradores**

* Visualizar itens disponíveis
* Acompanhar evolução dos lances *em tempo real*
* Dar lances conforme regras do leilão
* Visualizar histórico do item
* Receber notificação de lance vencedor

### **Para Vendedores**

* Criar e configurar leilões
* Definir regras de incremento
* Iniciar/encerrar leilões
* Visualizar resultados

### **Administração**

* Gerenciamento de usuários
* Auditoria
* Relatórios básicos

---

# 3. 🧱 Requisitos Técnicos

## Requisitos funcionais chave

* Atualização em tempo real de preços
* Garantia de ordem dos lances
* Registro imutável de lances
* Baixa latência e alto throughput

## Requisitos não funcionais

* Suporte a cluster
* Escalabilidade horizontal
* Consistência transacional
* Observabilidade
* Alta disponibilidade (HA)

---

# 4. 🗂️ Arquitetura Lógica

```
Frontend (Angular 18)
     ↓ (HTTP REST + WebSocket/SSE)
Backend Leilões (Spring Boot)
     ↓
Message Broker (opcional futuro)
     ↓
Serviços Auxiliares (Autenticação, Pagamentos, Catálogo)
     ↓
Banco de Dados (PostgreSQL)
```

### Comunicação real-time

* **MVP:** WebSocket (preferido)
* **Alternativa:** SSE (viável para espectadores)

---

# 5. ☁️ Arquitetura Física / Infraestrutura

## Componentes principais

* Kubernetes (EKS) ou EC2 com auto-scaling
* Load Balancer L4/L7
* Auto-scaling horizontal dos serviços
* Redis/Kafka (futuro) para sincronização entre nós

### Topologia (sem BFF, comunicação direta)

```
[Angular SPA]
       |
   HTTP / WebSocket
       |
   [Backend Leilões]
       |
  PostgreSQL Cluster
```

---

# 6. 🔄 Comunicação Front-end ↔ Back-end

## Comparativo SSE vs WebSocket

| Critério                         | SSE          | WebSocket |
| -------------------------------- | ------------ | --------- |
| Comunicação bidirecional         | ❌            | ✔️        |
| Envio do cliente → servidor      | ❌            | ✔️        |
| Escalabilidade em cluster        | Média        | Alta      |
| Compatível com LB sem stickiness | ✔️           | ❌         |
| Ideal para                       | Espectadores | Lances    |

📌 **Decisão final:**
**WebSocket é obrigatório para compradores que dão lance.**
**SSE opcional para espectadores.**

---

# 7. 🔁 Fluxos Críticos

## Fluxo 1 — Acompanhamento do Leilão

1. Usuário abre a página
2. Front abre canal WebSocket (ou SSE)
3. Backend envia atualizações
4. Tela atualiza valores em tempo real

## Fluxo 2 — Envio de Lance

1. Cliente envia lance via WebSocket
2. Backend valida e persiste
3. Backend envia broadcast
4. Front atualiza imediatamente

---

# 8. 🧬 Considerações de Alta Disponibilidade

## Para WebSockets:

* Exige **sticky-session** no load balancer
* Ou sincronização via **Redis Pub/Sub** ou **Kafka**
* Reconexão automática do front
* Reenvio idempotente de lances

## Para SSE:

* Sem necessidade de stickiness
* Escala facilmente

---

# 9. 🔐 Segurança e Compliance

* JWT + HTTPS
* Controle de permissões RBAC
* Auditoria completa dos lances
* Logs com hash encadeado (opcional)
* TLS obrigatório
* Rate limiting por IP/usuário

---

# 10. ⭐ Diferenciais Avançados

## 10.1. Experiência avançada do usuário

* Modo "Espectador Inteligente"
* Indicadores de disputa
* Assistente de estratégia
* Alertas automáticos de momento ideal

## 10.2. Segurança e antifraude

* Detecção de comportamento suspeito
* Hashing de lances para integridade
* Auditoria imutável

## 10.3. Inteligência e análises

* Painel BI
* Previsão de preço via ML
* Análise de comportamento dos lances

## 10.4. APIs e Extensibilidade

* API pública
* Webhooks
* Plugins de estratégia automática
* Integrações ERP

## 10.5. Governança e Operação

* Métricas avançadas
* Degradação progressiva
* Failover inteligente de sessões

## 10.6. Funcionalidades inteligentes futuras

* Chat IA durante leilão
* Insights de comportamento
* Sistema de risco

---

# 11. 🛣️ Roadmap Evolutivo

## MVP

* CRUD de leilões
* Fluxo de lances
* WebSocket básico
* Persistência transacional

## V1

* SSE para espectadores
* Auditoria avançada
* Observabilidade

## V2

* Webhooks e API pública
* Painéis BI
* Estratégias automáticas
