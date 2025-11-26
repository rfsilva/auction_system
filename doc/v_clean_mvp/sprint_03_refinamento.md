# Refinamento Sprint 3 — Sistema de Leilão Eletrônico

**Sprint:** 3  
**Duração:** 2 semanas  
**Dev Pleno + Sênior + Arquiteto**  

## Objetivo da Sprint
Implementar funcionalidades críticas de leilão em tempo real, incluindo motor de lances, fechamento de produtos e lotes, arremate e notificações. Garantir concorrência, consistência e regras de negócio de anti-sniping.

---

## Histórias Detalhadas
- **Regras Gerais** 
  1. Backend: Se precisar criar entity nova, localizar primeiro a tabela em V1 do migrations. Se não encontrar, criar migration para criação da tabela.
  2. Backend: Se for necessário criar tabela, usar prefixo "tb_" e nome no singular
  3. Backend: Não criar estruturas do tipo TYPE, TRIGGER, PROCEDURE, FUNCTION no migrations
  4. Backend: Sempre que possível, aplicar Lombok para eliminar verbosidade de código
  5. Frontend: Sempre que um novo componente for criado, respeitar a separação entre TS, HTML e CSS

### História 1: Motor de Lances (Bid Engine)
- **Tipo:** Funcional
- **Descrição:** Receber, validar e persistir lances para produtos.
- **Tasks / Sub-tasks:**
  1. Criar entidade Lance com persistência append-only - 2 SP  
  2. Implementar API REST/WebSocket para envio de lances - 3 SP  
  3. Validar usuário autenticado e verificado (status VERIFIED) - 1 SP  
  4. Validar valor do lance >= nextPrice / minIncrement - 1 SP  
  5. Validar horário do lance (não aceitar após encerramento, anti-sniping) - 2 SP  
  6. Persistir metadados (IP, UserAgent, timestamp, sequenceNumber) - 1 SP  
  7. Testes unitários e integração do motor de lances - 2 SP  
- **Story Points:** 12 SP

### História 2: Canal Realtime (SSE/WebSocket)
- **Tipo:** Enabler
- **Descrição:** Disparar eventos de lances e atualizações para clientes em tempo real.
- **Tasks / Sub-tasks:**
  1. Implementar SSE para espectadores - 2 SP  
  2. Implementar WebSocket para participantes ativos - 2 SP  
  3. Broadcast de eventos: `BidReceived`, `BidAccepted`, `BidRejected` - 2 SP  
  4. Fallback para polling se conexão falhar - 1 SP  
  5. Testes de latência e concorrência (<1s) - 2 SP  
- **Story Points:** 9 SP

### História 3: Encerramento de Produtos e Lotes
- **Tipo:** Funcional
- **Descrição:** Detectar e processar fechamento de produtos e lotes.
- **Tasks / Sub-tasks:**
  1. Criar scheduler para monitorar endDateTime de produtos e lotes - 2 SP  
  2. Aplicar anti-sniping (extend) quando configurado - 2 SP  
  3. Determinar vencedor (highest bid) - 1 SP  
  4. Atualizar status do produto (`SOLD` / `UNSOLD`) - 1 SP  
  5. Testes unitários de encerramento e anti-sniping - 2 SP  
- **Story Points:** 8 SP

### História 4: Arremate e Cálculo de Taxas
- **Tipo:** Funcional
- **Descrição:** Definir vencedor, calcular taxas e gerar documentos de arremate.
- **Tasks / Sub-tasks:**
  1. Calcular taxa do leiloeiro conforme contrato do vendedor - 2 SP  
  2. Gerar PDF de arremate com hash e metadados - 2 SP  
  3. Registrar arremate no banco (Sale entity) - 1 SP  
  4. Notificar comprador e vendedor via eventos/real-time - 2 SP  
  5. Testes unitários e integração com motor de lances - 2 SP  
- **Story Points:** 9 SP

### História 5: Preparar Integração com Pagamento (Gateway)
- **Tipo:** Enabler
- **Descrição:** Preparar endpoints e lógica de pagamento para arremates futuros.
- **Tasks / Sub-tasks:**
  1. Criar stub/mock de integração com gateway de pagamento - 2 SP  
  2. Criar API para iniciar pagamento e receber webhook de status - 2 SP  
  3. Testes unitários e simulação de fluxo de pagamento - 2 SP  
- **Story Points:** 6 SP

### História 6: Notificações Pós-Arremate
- **Tipo:** Funcional / Enabler
- **Descrição:** Envio de notificações para comprador e vendedor após fechamento.
- **Tasks / Sub-tasks:**
  1. Criar eventos `ProductClosed`, `SaleGenerated` - 2 SP  
  2. Integrar com serviço de envio de emails / push - 2 SP  
  3. Testes de entrega e logs de notificação - 1 SP  
- **Story Points:** 5 SP

---

## Observações
- **Dependências:**  
  - Sprint 1 e 2 concluídas: CRUD de produtos/lotes, catálogo público.  
  - Backend modular pronto para lidar com concorrência.  

- **Critérios de Aceite (Sprint 3):**
  1. Usuários podem dar lances válidos; invalidos retornam código de erro correto.  
  2. Atualizações de preço chegam em <1s para espectadores e participantes via SSE/WebSocket.  
  3. Encerramento de produtos e lotes processado corretamente com anti-sniping.  
  4. Arremates geram documentos válidos com cálculo correto de taxa.  
  5. Notificações pós-arremate enviadas e logadas.  
  6. Integração com mock de gateway de pagamento funcionando.  

---

**Story Points Totais Sprint 3:** 49 SP
