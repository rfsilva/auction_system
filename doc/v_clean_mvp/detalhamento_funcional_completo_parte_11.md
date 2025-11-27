# 📘 PARTE 11 — Integrações, Interfaces e Contratos

## 1. Introdução

Esta seção documenta todas as **integrações**, **interfaces externas**, **pontos de entrada e saída**, e os **contratos** de comunicação entre sistemas envolvidos no Leilão Online.  
Esse material segue o padrão de documentação funcional clássica (“waterfall”), servindo como base para arquitetura, desenvolvimento e testes integrados.

Foco:
- APIs internas e externas  
- Webhooks  
- Notificações em tempo real (SSE / WebSocket)  
- Integrações para pagamento  
- Contratos JSON  
- Operações síncronas e assíncronas  
- Regras específicas por canal de integração  

---

# 2. Arquitetura de Integração

O sistema opera com três tipos de integração:

### **2.1. Integrações Síncronas (REST API)**
- Operações CRUD  
- Consultas de leilões  
- Dar lance  
- Consultar histórico  
- Administração  

Características:
- JSON  
- Autenticação JWT  
- Códigos HTTP padronizados  
- Idempotência para operações críticas  

---

### **2.2. Integrações Assíncronas (SSE / WebSocket)**
Usadas para:
- atualização de lances em tempo real  
- início / encerramento do leilão  
- extensões automáticas (anti-sniping)  
- notificações para participantes  

Características:
- Canal único por leilão (ex: `/ws/auctions/{id}`)  
- Eventos padronizados  
- Reconexão automática  

---

### **2.3. Integrações com Sistemas Externos**
1. **Gateway de Pagamentos**  
2. **Serviço de Validação de CPF**  
3. **Serviço OTP (SMS)**  
4. **Serviço de E-mail (SMTP ou API)**  

Todos com contratos padronizados.

---

# 3. Mapa Geral das Integrações

| Categoria | Tipo | Descrição |
|----------|------|-----------|
| Autenticação | REST | Login, refresh token, logout |
| Dados de Leilão | REST | Criar, listar, consultar, encerrar |
| Lances | REST + WebSocket | Registrar lance / receber em tempo real |
| Notificações | WebSocket / SSE | Push de eventos do leilão |
| Pagamento | REST externo | Autorização, captura, cancelamento |
| OTP | REST externo | Envio de código SMS |
| E-mail | REST/SMTP | Notificações offline |
| Validação CPF | REST externo | Autorização de identidade |

10. Mapeamento dos Fluxos de Integração
10.1. Criar Leilão

Vendedor cria leilão

API registra

Notificação opcional para seguidores

10.2. Dar Lance

REST registra lance

Módulo valida regras

Atualiza histórico

Envia evento:

WebSocket

SSE fallback

E-mail se usuário estiver offline

10.3. Início do Leilão

Scheduler ativa

Evento “auctionStarted” enviado via WebSocket/SSE

10.4. Encerramento

Scheduler encerra

Determina vencedor

Envia evento real-time

Dispara processo de pagamento

10.5. Pagamento

Comprador autoriza

Gateway valida

Admin/Vendedor confirma entrega

Sistema registra auditoria

12. Regras de Interface e Compatibilidade

Todos os endpoints devem manter retrocompatibilidade por no mínimo 6 meses

Versões devem seguir o padrão:

/api/v1/...

/api/v2/...

Eventos SSE/WS também são versionados

13. Segurança das Integrações

JWT com validade curta

Refresh tokens armazenados com segurança

Rate limiting por IP

Throttling em endpoints de lance

Logs obrigatórios em ações sensíveis

Criptografia TLS 1.2+

14. Conclusão

Este capítulo consolida todas as integrações e contratos do sistema de forma alinhada ao modelo waterfall tradicional, pronto para conversão para épicos, histórias e testes automatizados.
