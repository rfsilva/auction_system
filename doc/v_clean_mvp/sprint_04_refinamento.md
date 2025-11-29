# Refinamento Sprint 4 — Sistema de Leilão Eletrônico

**Sprint:** 4  
**Duração:** 2 semanas  
**Dev Pleno + Sênior + Arquiteto**  

## Objetivo da Sprint
Completar funcionalidades críticas de pós-arremate: pagamentos reais, contestação/disputa, auditoria e notificações avançadas. Ajustar fluxos para garantir consistência, rastreabilidade e conformidade.

---

## 📝 Regras Gerais
  1. **Backend**:
	1.1. Se precisar criar entity nova, localizar primeiro a tabela em V1 do migrations. Se não encontrar, criar migration para criação da tabela.
	1.2. Se for necessário criar tabela, usar prefixo "tb_" e nome no singular
	1.3. Não criar estruturas do tipo TYPE, TRIGGER, PROCEDURE, FUNCTION no migrations
	1.4. Sempre que possível, aplicar Lombok para eliminar verbosidade de código
	1.5. Não gerar nem atualizar nenhum teste unitário ou integrado nesse momento.
	1.6. Criar collection do postman para testes de endpoints (novos ou atualizados) REST
  2. **Frontend**:
	2.1. Sempre que um novo componente for criado, não gerar HTML e CSS inline, separando os arquivos .html, .scss e .ts
	2.2. Formulários de CRUD (se criados ou atualizados) devem apresentar erros de validação claros, sendo: regras de negócio no topo do formulário, e erros de validação de campo em cada campo criticado
  3. **Integração**:
	3.1. Garantir consistência de chamadas REST entre frontend e backend através de testes integrados

## Histórias Detalhadas

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
