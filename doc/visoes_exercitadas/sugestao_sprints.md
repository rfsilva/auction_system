# Plano de Execução em Sprints — MVP Sistema de Leilão Eletrônico

**Equipe:**  
- 1 Arquiteto de Soluções (você)  
- 1 Desenvolvedor Pleno  
- 1 Desenvolvedor Sênior  

**Duração da Sprint:** 2 semanas cada  
**Objetivo:** Evoluir o MVP de forma incremental, garantindo fundações técnicas sólidas e entregas funcionais de valor

---

## Sprint 1 — Fundamentos e Identidade
**Objetivo:** Criar a base técnica e o fluxo inicial de usuários

**Histórias/Enablers**
- EN-SEC-001 — Módulo de autenticação e JWT  
- EN-SEC-003 — MFA básico para vendedores  
- HIST-US-001 a HIST-US-004 — Cadastro e onboarding de usuários  
- EN-SEC-004 — Configuração básica de RBAC  
- EN-CAT-001 — Estrutura inicial do MFE (shell container + módulo de login)  
- Configuração inicial de repositórios e pipelines GitHub Actions

**Critérios de Aceite**
- Usuário consegue criar conta, validar email, completar onboarding e logar  
- MFA funcional para vendedor  
- RBAC inicial testado com roles mínimas

---

## Sprint 2 — Cadastro de Vendedores e Contratos
**Objetivo:** Permitir vendedores se cadastrarem e definirem taxas

**Histórias/Enablers**
- HIST-VEN-001 a HIST-VEN-004 — Cadastro de vendedores e contratos  
- EN-VEN-001 — Validação de CNPJ  
- EN-VEN-002 — Motor de regras de taxas  
- EN-CAT-002 — Microsserviço vendedor básico  
- Integração inicial com banco (PostgreSQL) para vendedores e contratos

**Critérios de Aceite**
- Vendedores cadastrados e ativos  
- Contratos salvos com taxa do leiloeiro  
- Admin consegue visualizar vendedores e contratos

---

## Sprint 3 — Catálogo de Produtos e Lotes
**Objetivo:** Criar produtos e lotes com regras básicas

**Histórias/Enablers**
- HIST-PROD-001 a HIST-PROD-005 — CRUD de produtos  
- HIST-LOTE-001 a HIST-LOTE-005 — CRUD de lotes  
- EN-CAT-003 — Storage de mídias (S3 ou mock)  
- Validação: produto não pode ter endTime posterior ao lote

**Critérios de Aceite**
- Produto publicado aparece no catálogo  
- Lote publicado lista produtos corretamente  
- Bloqueio de alteração de produto ativo no leilão implementado

---

## Sprint 4 — Motor de Lances e Realtime
**Objetivo:** Implementar core de lances e fluxo realtime para espectadores

**Histórias/Enablers**
- HIST-LAN-001 a HIST-LAN-005 — Registro de lances  
- HIST-RT-001 a HIST-RT-003 — Atualizações realtime SSE/WebSocket  
- EN-RT-001 — Canal de comunicação em tempo real  
- EN-LAN-001 — Motor de concorrência e lock

**Critérios de Aceite**
- Lances aceitos/rejeitados conforme regras  
- Atualização de preço em tempo real <1s para espectadores  
- Concorrência simulada sem perda de ordem

---

## Sprint 5 — Encerramento e Arremate
**Objetivo:** Finalizar produtos/lotes e gerar arremate

**Histórias/Enablers**
- HIST-ARR-001 a HIST-ARR-004 — Encerramento automático, cálculo de taxas, geração de PDF  
- EN-LAN-002 — Scheduler distribuído  
- RF-007 — Regras de encerramento de produto/lote  
- RF-010 — Notificações de arremate

**Critérios de Aceite**
- Produto/lote fecha corretamente  
- Vencedor definido  
- Notificações disparadas  
- PDF de arremate gerado

---

## Sprint 6 — Pagamento e Frete
**Objetivo:** Integração com gateway de pagamento e cálculo de frete

**Histórias/Enablers**
- HIST-US-011 — Pagamento via gateway externo  
- EN-PAY-001 — Integração com webhooks de pagamento  
- HIST-US-012 — Cálculo de frete via API externa  
- EN-FRETE-001 — Mock ou integração inicial de frete

**Critérios de Aceite**
- Pagamento registrado e status atualizado corretamente  
- Frete exibido e calculado para comprador  
- Arremate final com taxa e frete corretamente refletido

---

## Sprint 7 — Notificações e Documentos
**Objetivo:** Completar notificações e geração de documentos

**Histórias/Enablers**
- HIST-US-013 — Notificações email/push/SMS  
- HIST-US-014 — Documentos PDF (arremate, invoice)  
- EN-POS-001 — Workflow de disputas (primeiro nível)  
- Testes de envio de notificações

**Critérios de Aceite**
- Notificações enviadas no evento correto  
- PDFs gerados e armazenados corretamente  
- Usuário consegue consultar histórico

---

## Sprint 8 — Admin e Auditoria
**Objetivo:** Painel administrativo e logs

**Histórias/Enablers**
- HIST-ADM-001 a HIST-ADM-005 — Admin aprova KYC, visualiza logs, métricas e relatórios  
- EN-OBS-001 — Observabilidade e auditoria

**Critérios de Aceite**
- Admin acessa painel completo  
- Logs rastreáveis e auditáveis  
- Admin consegue aprovar/rejeitar usuários e vendedores

---

## Observações Gerais
- Ordem das sprints foi pensada para **fundar arquitetura → implementar core funcional → evoluir em camadas**  
- Cada sprint entrega **funcionalidade testável e incremental**  
- Permite ajustes futuros sem comprometer base técnica  
- Futuras evoluções (tema 8 — diferenciais) podem ser adicionadas após Sprint 8
