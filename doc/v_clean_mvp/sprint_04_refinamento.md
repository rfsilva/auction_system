# Refinamento Sprint 4 — Sistema de Leilão Eletrônico

**Sprint:** 4  
**Duração:** 2 semanas  
**Dev Pleno + Sênior + Arquiteto**  

## Objetivo da Sprint
Completar funcionalidades críticas de pós-arremate: pagamentos reais, contestação/disputa, auditoria e notificações avançadas. Ajustar fluxos para garantir consistência, rastreabilidade e conformidade.

---

## 📘 Contexto do Projeto

### 🛠️ Backend:
- Java 21 + Spring Boot 3 + API REST + DTO + Validation + Lombok + JPA + MySQL + Flyway
- Entidade JPA completa (com Lombok, constraints e relacionamentos)
- Usar String para campos UUID quando as colunas do banco são VARCHAR(36)
- DTOs (request/response), validadores e mappers
- Repository
- Service com regras de negócio
- Controller REST com todos os endpoints CRUD + filtros se aplicável
- Migrations (somente se necessário; primeiro valide se existe na V1)
- Regras de validação (negócio e campos)
- Mensagens de erro claras	
- I18N estruturado para Português, Inglês, Espanhol e Italiano

### 🎨 Frontend: 
- Angular 18 (standalone) + HttpClient com fetch + Reactive Forms + rotas
- Model (interface ou classe)
- Service TS com chamadas REST usando `HttpClient` (withFetch)
- Component de listagem + filtros
- Component de formulário (create/update)
- Component de detalhe (se fizer sentido)
- Component sem HTML e SCSS inline - criar arquivos separados
- Reactive Forms com validações
- Mensagens de erro (negócio no topo, campos em cada campo)
- Rotas completas do módulo	

### 🔗 Integrações:
- Geração da collection Postman dos endpoints criados/alterados
- Garantir consistência do contrato REST gerado no backend para uso no frontend  

### 🛢️ Banco de Dados:
- Migrations versionadas (V1 = legado), prefixo "tb_" e nome singular
- Evitar ao máximo queries nativas e named queries
- Não criar estruturas específicas do banco de dados (TYPE, TRIGGER, PROCEDURE, FUNCTION, etc.) no migrations
- Para entities novas, validar no migrations se tabela já implementada. Se não, criar, se sim e precisar atualizar, atualize em versão nova.

### ⚠️ Importante:
- Manter padrões de nomenclatura e pastas
- NÃO inventar regra que não esteja no documento funcional.
- Analise a história. SE a história tiver regra incompleta, liste os "pontos pendentes" no bloco ANOTAÇÕES.
- Mantenha código limpo e dentro dos padrões fornecidos.
- Comece lendo o material, identifique entidades e regras, e só então gere tudo.

## 📋 Histórias Detalhadas

### História 1: Pagamentos Reais via Gateway
- **Tipo:** Funcional
- **Descrição:** Permitir que o comprador finalize o pagamento do arremate com integração real ao gateway.
- **Tasks / Sub-tasks:**
  1. Implementar endpoint de checkout para arremate - 2 SP  
  2. Integrar com gateway real (authorize/capture/refund) - 3 SP  
  3. Configurar webhook para status de pagamento (`PAID`, `PENDING`, `FAILED`) - 2 SP  
  4. Atualizar status do arremate e repasse para vendedor conforme pagamento - 2 SP  
  5. Testes unitários e integração simulando transações reais - 3 SP  
- **Story Points:** 12 SP

### História 2: Disputa / Contestação de Arremate
- **Tipo:** Funcional
- **Descrição:** Gerenciar workflow de contestação de arremate para compradores e vendedores.
- **Tasks / Sub-tasks:**
  1. Criar entidade `Dispute` com campos: arremateId, userId, status, evidências, timestamps - 2 SP  
  2. Endpoint para abertura de disputa e upload de evidências - 2 SP  
  3. Painel administrativo para revisão e decisão de disputas - 2 SP  
  4. Bloqueio temporário de repasse enquanto disputa estiver aberta - 1 SP  
  5. Notificações automáticas de status da disputa para partes envolvidas - 1 SP  
  6. Testes de fluxo completo de disputa - 2 SP  
- **Story Points:** 10 SP

### História 3: Auditoria e Logs Detalhados
- **Tipo:** Enabler
- **Descrição:** Registrar todas as ações críticas (lances, encerramentos, arremates, alterações de produto/lote).
- **Tasks / Sub-tasks:**
  1. Criar entidade `AuditLog` com campos: entityType, entityId, action, performedBy, metadata, timestamp - 2 SP  
  2. Integrar logging append-only com traceId - 1 SP  
  3. Implementar exportação de logs para análise e conformidade LGPD - 1 SP  
  4. Testes de consistência de logs e reconciliação de eventos críticos - 2 SP  
- **Story Points:** 6 SP

### História 4: Notificações Avançadas
- **Tipo:** Funcional / Enabler
- **Descrição:** Enviar notificações detalhadas por eventos pós-arremate, disputas e alertas críticos.
- **Tasks / Sub-tasks:**
  1. Criar templates de notificações (email, push, SMS) para todos eventos de pós-arremate - 2 SP  
  2. Implementar retries automáticos e fallback por canal - 2 SP  
  3. Logar todas notificações enviadas com status de entrega - 1 SP  
  4. Testes de envio para múltiplos canais e validação de conteúdo - 2 SP  
- **Story Points:** 7 SP

### História 5: Ajustes de UX e Validações Finais
- **Tipo:** Funcional
- **Descrição:** Corrigir detalhes de experiência do usuário e validar todas regras de negócio em fluxo real.
- **Tasks / Sub-tasks:**
  1. Validar fluxos de arremate, pagamento e disputa com QA - 2 SP  
  2. Ajustar mensagens de erro e sucesso (lances, pagamentos, disputas) - 1 SP  
  3. Testes de ponta a ponta em ambiente de homologação - 2 SP  
- **Story Points:** 5 SP

---

## Observações
- **Dependências:**  
  - Sprint 1-3 concluídas: motor de lances, arremate, canal realtime, anti-sniping.  
  - Backend preparado para receber notificações e integrar gateway real.  

- **Critérios de Aceite (Sprint 4):**
  1. Pagamentos reais processados corretamente, status atualizado, repasse gerado.  
  2. Disputas podem ser abertas, analisadas e resolvidas com notificações corretas.  
  3. Todos eventos críticos são logados no AuditTrail.  
  4. Notificações pós-arremate, disputas e alertas críticos enviadas e logadas com sucesso.  
  5. Fluxos validados, mensagens consistentes, testes ponta a ponta aprovados.  

---

**Story Points Totais Sprint 4:** 40 SP
