# Documento Funcional — Sistema de Leilão Eletrônico (Visão Waterfall)

**Versão:** 2.0  
**Data:** 2025-11-19  
**Autor:** Equipe de Produto / Arquitetura  
**Objetivo:** Documento funcional detalhado que descreve todas as funcionalidades, regras de negócio, fluxos, restrições de permissão (RBAC), critérios de aceite e casos de teste para implementação do sistema de leilão eletrônico. Serve como fonte de verdade para o mapeamento waterfall → agile e para criação de backlog (temas, épicos, histórias).

---

## Sumário
1. Introdução e objetivos  
2. Escopo do documento  
3. Glossário e atores  
4. Visão geral funcional (capabilities)  
5. Requisitos funcionais detalhados (RF-xxx)  
6. Regras de negócio (RB-xxx) — normas, validações e políticas  
7. Casos de uso e fluxos operacionais (passo-a-passo)  
8. Dados e modelo lógico (entidades e atributos críticos)  
9. Segurança, autenticação e autorização (RBAC)  
10. Requisitos não-funcionais relevantes  
11. Critérios de aceite e testes funcionais recomendados  
12. Exceções, erros e tratamento de disputas  
13. Matrizes e tracabilidade (sugestões)  
14. Integrações externas e mensagens realtime  
15. Observabilidade, auditoria e retenção  
16. Roadmap de features e notas de implantação

---

## 1. Introdução e objetivos
Este documento descreve, de forma completa e atômica, as funcionalidades e regras do sistema de leilão eletrônico. Destina-se a orientar:

- engenharia (desenvolvimento e QA);  
- product owners / stakeholders;  
- operações e suporte;  
- auditoria / compliance.

O documento contempla o MVP funcional (cadastro, produtos, lotes, lances, arremate, pagamentos básicos, documentos e notificações) e define regras operacionais e de segurança que devem ser implementadas desde a primeira versão.

---

## 2. Escopo do documento
Inclui:
- Funcionalidades end-to-end do fluxo de leilão.  
- Regras de negócio detalhadas e exceções.  
- Papéis e permissões (RBAC).  
- Critérios de aceite e cenários de teste.  
- Integrações com sistemas externos (pagamento, frete, notificação).  
Exclui:
- Implementação técnica (código, infra-as-code) — tratada em documento técnico.  
- Features avançadas pós-MVP (AI, replay, gamificação), salvo quando referenciadas como roadmap.

---

## 3. Glossário e atores

### 3.1 Glossário (principais termos)
- **Auction / Leilão**: evento composto por lotes e produtos.  
- **Lote**: agrupamento lógico de produtos com um encerramento global possível; contém um conjunto de produtos.  
- **Produto**: item individual leiloado; pode ter encerramento próprio anterior ao lote.  
- **Bid / Lance**: proposta de preço feita por um comprador para um produto.  
- **Arremate / Sale**: conclusão de venda de um produto ao maior lance válido.  
- **KYC**: Know Your Customer — verificação de identidade.  
- **Anti-sniping**: extensão automática do tempo quando há lance nos últimos segundos.  
- **Fee / Taxa de leiloeiro**: percentual ou valor contratado entre plataforma e vendedor.  
- **Outbid**: situação onde um lance é superado por outro.

### 3.2 Atores
- **Visitante**: usuário não autenticado.  
- **Participante**: usuário autenticado, em onboarding ou sem KYC aprovado.  
- **Comprador**: usuário autenticado e com KYC aprovado (pode dar lances).  
- **Vendedor**: entidade que cadastra produtos e cria lotes.  
- **Administrador**: opera e audita a plataforma; aprova KYC, resolve disputas, aplica correções administrativas.  
- **Sistemas Externos**: gateway de pagamento, API de frete, serviço de notificações, storage (S3) e provedores de autenticação.

---

## 4. Visão geral funcional (capabilities)
O sistema deve suportar, no MVP:

1. Gestão de identidade e KYC (registro, login, 2FA, upload de documentos).  
2. Gestão de vendedores (contratos, taxas, documentos).  
3. CRUD de produtos com suporte a encerramento individual.  
4. CRUD de lotes (associação de produtos, encerramento global).  
5. Motor de lances com garantia de ordem, persistência append-only, validação e tratamento de concorrência.  
6. Capacidade realtime (WebSocket para bidders, SSE para espectadores).  
7. Encerramento por produto e consolidação de lote; arremate automático.  
8. Cálculo de frete via API externa.  
9. Aplicação de taxa do leiloeiro conforme contrato e geração de faturas/invoices.  
10. Processamento de pagamento via gateway e confirmação via webhooks.  
11. Geração de documentos (PDFs) e armazenamento seguro com links expiráveis.  
12. Notificações (email, push, in-app) com retry/fallback.  
13. Auditoria e logs imutáveis para compliance.  
14. Painel administrativo para gestão e resolução de disputas.

---

## 5. Requisitos funcionais detalhados (RF-xxx)
Cada requisito tem ID, descrição, validações, fluxos e critério de aceite.

### RF-001 — Cadastro e conversão (visitante → participante)
- **Descrição:** criar conta com nome, email, telefone, senha; confirmação via email.  
- **Validações:** email único; senha mínima 8 chars; telefonia opcional; captcha para evitar bots.  
- **Fluxo:** criar conta → enviar token (24h) → ativar conta → status `ACTIVE`.  
- **Critério de aceite:** conta criada e ativada; registro em logs; email de boas-vindas enviado.

### RF-002 — Autenticação e Sessão
- **Descrição:** login com email+senha; suporte a 2FA (TOTP) obrigatório para vendedores; refresh tokens.  
- **Validações:** bloqueio por N tentativas; JWT RS256; refresh token armazenado (rotating).  
- **Critério de aceite:** login retorna access token e refresh token; resposta com perfil.

### RF-003 — Onboarding KYC (dupla validação)
- **Descrição:** envio e validação de documentos (RG/CNH/CNPJ/comprovante).  
- **Estados:** `PENDING`, `APPROVED`, `REJECTED`.  
- **Critério de aceite:** documento recebido e workflow de revisão disparado; KYC aprovado permite dar lances.

### RF-004 — Cadastro de vendedores e contratos
- **Descrição:** vendedor registra dados (PF/PJ), anexa contrato que define `feeRate`.  
- **Regras:** contrato versionado; `feeRate` por vendedor e por contrato; recuperação de histórico.  
- **Critério de aceite:** vendedor ativo com contrato vigente.

### RF-005 — CRUD Produtos (revisado)
- **Descrição:** criar/editar/excluir produto com atributos (title, description, images, weight, dimensions, initialPrice, endDateTime (nullable), status).  
- **Regras adicionais (bloqueio em leilão ativo):**  
  - Se produto estiver associado a um lote PUBLICADO **e** o produto/lote estiver em estado de leilão ativo (hora atual entre abertura e encerramento), o vendedor **NÃO PODE ALTERAR**: title, description, images, weight, dimensions, initialPrice, endDateTime.  
  - Alterações permitidas durante leilão ativo: campos não-críticos (ex.: `sellerNotes`), que serão auditadas e tratadas como append-only; adição de documentos complementares é permitida, desde que não altere dados essenciais.  
  - Tentativa de alteração proibida retorna `403 PRODUCT_LOCKED_ACTIVE_AUCTION`.  
- **Critério de aceite:** produto publicado aparece no catálogo; regra de bloqueio testada.

### RF-006 — CRUD Lotes (revisado)
- **Descrição:** criar lote, associar produtos, definir `loteEndDateTime`, publicar.  
- **Regras:**  
  - Produto não pode ter `endDateTime` posterior ao `loteEndDateTime`.  
  - Se lote publicado e ativo, vendedor não pode remover produto do lote se produto já recebeu lance.  
  - Alteração do `loteEndDateTime` não é permitida se houver lances em qualquer produto do lote (retorna `403 LOTE_LOCKED_HAS_BIDS`).  
- **Critério de aceite:** lote publicado com produtos associados; validações aplicadas.

### RF-007 — Regras de Encerramento e Anti-sniping
- **Descrição:** sistema encerra produtos em seus `endDateTime`; lote encerra no `loteEndDateTime` ou quando todas as regras forem satisfeitas. Anti-sniping opcional pode estender `endDateTime`.  
- **Regras:**  
  - Se `antiSnipingEnabled`, e lance ocorrer com `remainingTime <= antiSnipingWindow`, então `endDateTime += antiSnipingExtension`.  
  - Encerramento deve ser idempotente e transacional.  
- **Critério de aceite:** encerramento automático efetua arremate; extensões aplicadas quando configuradas.

### RF-008 — Motor de Lances (core)
- **Descrição:** receber, validar e persistir lances por produto.  
- **Regras obrigatórias:**  
  - `minIncrement` por produto/lote; `nextPrice = currentPrice + minIncrement`.  
  - Lance válido: `bidAmount >= nextPrice`.  
  - Usuário deve estar `VERIFIED` (KYC aprovado) para enviar lance.  
  - Persistência append-only com metadata (ip, ua, correlationId).  
  - Concorrência: lock otimista+CAS ou transação serializable; sequence number por produto para ordenar lances.  
- **Critério de aceite:** lances válidos aceitos e broadcast; inválidos rejeitados com motivo.

### RF-009 — Canal Realtime
- **Descrição:** disponibilizar atualizações de lances e eventos.  
- **Regras:**  
  - Bidders (usuários que podem lançar) usam WebSocket (full-duplex).  
  - Espectadores usam SSE.  
  - Fallback: polling leve com ETag/If-Modified.  
- **Critério de aceite:** atualização visível em <1s em ambiente controlado.

### RF-010 — Arremate e Notificações
- **Descrição:** definir vencedor, calcular taxa, estimar frete, gerar documentação e notificar.  
- **Regras:**  
  - `fee = finalPrice * feeRate` (feeRate do contrato do vendedor).  
  - Freight estimation via API externa; fallback para "estimativa indisponível".  
  - Gerar Invoice/PDF com hash SHA256, armazenar em S3 com links expiráveis.  
- **Critério de aceite:** documentos e notificações disparadas e logs registrados.

### RF-011 — Pagamento
- **Descrição:** fluxo de pagamento do arremate via gateway.  
- **Regras:**  
  - Status: `PENDING`, `AUTHORIZED`, `PAID`, `FAILED`.  
  - Webhooks aceitos via HMAC; idempotência (use paymentId).  
  - Se `FAILED` após X retries, aplicar regra de fallback (notificar vendedor/admin; oferecer ao segundo colocado se política permitir).  
- **Critério de aceite:** status refletido via webhook; repasse programado.

### RF-012 — Documentos e Comprovantes
- **Descrição:** emitir comprovante de lance e PDF de arremate.  
- **Regras:** versão imutável; hash gravado em audit log; links expiráveis; downloads autorizados via token curto.  
- **Critério de aceite:** documentos gerados e verificáveis.

### RF-013 — Notificações
- **Descrição:** disparo de eventos via filas; retry/fallback.  
- **Regras:** preferências por usuário; prioridade para eventos críticos (outbid, arremate).  
- **Critério de aceite:** entregas com retry logado.

### RF-014 — Auditoria e Logs
- **Descrição:** registrar todas ações críticas (lances, alterações de produto/lote, encerramentos, pagamentos).  
- **Regras:** append-only, retention policy (ex.: 7 anos), busca por traceId.  
- **Critério de aceite:** reconstrução de timeline possível.

### RF-015 — Administração
- **Descrição:** painel para aprovar KYC, moderar conteúdo, gerenciar disputas.  
- **Regras:** RBAC estrito; transações admin auditadas.  
- **Critério de aceite:** admin pode executar ações com logs.

### RF-016 — Segurança
- **Descrição:** TLS obrigatório, JWT RS256, WAF, rate limiting.  
- **Regras:** bloquear IP após N tentativas; proteger endpoints sensíveis; monitorar anomalias.  
- **Critério de aceite:** testes de penetração básicos aprovados.

### RF-017 — Regras Comerciais (Taxas e Repasse)
- **Descrição:** aplicar taxa do leiloeiro e gerar repasse do vendedor.  
- **Regras:** retenção até confirmação de pagamento; ledger interno para repasses; relatórios mensais.  
- **Critério de aceite:** cálculo e registro corretos.

---

## 6. Regras de negócio (RB-xxx) — normas, validações e políticas
Regras detalhadas e numeradas para referência.

### RB-001 — Ordem e consistência de lances
- Lances ordenados por (timestamp UTC, serverSequenceId).  
- Em empate, menor serverSequenceId vence.  
- Registro append-only; correções via processo administrativo com justificativa.

### RB-002 — Incrementos mínimos
- `minIncrement` configurável por produto ou herdado do lote.  
- Cálculo de `nextPrice` e validação estrita no backend.

### RB-003 — Encerramento (produto vs lote)
- Produto com `endDateTime` fecha naquele instante.  
- Lote fecha no `loteEndDateTime` ou quando todos os produtos fluidamente encerrados conforme política.  
- Anti-sniping: paramétrico por lote.

### RB-004 — Anti-fraud e limites
- Rate-limits por usuário/IP.  
- Regras para detectar multi-conta, padrões anômalos, bots.  
- Soft-block (captcha) e hard-block (suspensão) com fila de revisão.

### RB-005 — Taxa do leiloeiro
- Calculada no arremate; pode haver faixas progressivas.  
- Transparente no invoice.

### RB-006 — Pagamento e fallback
- Prazo de pagamento (`paymentWindow`) configurável.  
- Regras para oferta ao 2º colocado estritas e configuráveis.

### RB-007 — Documentos e evidências
- PDF com metadados + hash; armazenamento seguro; download com token.

### RB-008 — Privacidade e LGPD
- Acesso restrito a PII; endpoints para export/delete conforme LGPD.  
- Logs pseudo-anonimizados para análises.

### RB-009 — Notificações de tempo
- Alerts T-minus configuráveis; outbid em até 10s.

### RB-010 — Disputas
- Prazo para abrir disputa (`disputeWindow`) configurável; bloqueio de repasse até resolução.

### RB-011 — Visibilidade e privacidade de lotes
- Lotes privados (apenas para compradores aprovados) suportados.

### RB-012 — Integridade de produto em leilão ativo (nova)
- Enquanto produto estiver em leilão ativo (produto/lote publicado e não encerrado), campos essenciais NÃO podem ser alterados pelo vendedor: title, description, images, weight, dimensions, initialPrice, endDateTime.  
- Alterações solicitadas por vendedor durante leilão devem seguir fluxo administrativo via Admin (correção excepcional) e gerar audit log.  
- Tentativa de alteração retorna `403 PRODUCT_LOCKED_ACTIVE_AUCTION`.

### RB-013 — Bloqueio de remoção/alteração em lote ativo (nova)
- Vendedor não pode remover produto de lote publicado/ativo se produto já recebeu lance.  
- Alterar `loteEndDateTime` não é permitido com lances presentes; erro `403 LOTE_LOCKED_HAS_BIDS`.

---

## 7. Casos de uso e fluxos operacionais (passo-a-passo)
Fluxos detalhados com validações e critérios de sucesso.

### 7.1 Fluxo: Cadastro e KYC
1. Visitante => SignUp (email+senha).  
2. Sistema valida; cria `PENDING_VERIFICATION`.  
3. Token email enviado (24h).  
4. Usuário ativa conta (`ACTIVE`).  
5. Submete documentos KYC → `PENDING` → Admin aprova → `VERIFIED`.  
**Checks:** email único, captcha, tamanho e formato de documentos.

### 7.2 Fluxo: Criação de produto e lote
1. Vendedor cria produto (draft).  
2. Vendedor cria lote e associa produto(s).  
3. Define `productEndTime` (opcional) e `loteEndTime`.  
4. Validações: `productEndTime <= loteEndTime`.  
5. Publica lote → triggers: indexing, cache warming.  
6. RB-012 aplicado: bloqueios de edição para campos essenciais.

### 7.3 Fluxo: Observador (spectator)
1. Abertura de página do produto.  
2. Conexão SSE; recebe `lastPrice`, `topBidders`, `remainingTime`.  
3. UI atualiza automaticamente.

### 7.4 Fluxo: Envio de lance (bid)
1. Comprador (VERIFIED) clica em `placeBid`.  
2. Front abre/usa WebSocket; envia `placeBid(productId, value, clientSequenceId)`.  
3. Backend valida: KYC, nextPrice, horário, rate-limit.  
4. Persistir lance com `serverSequenceId`.  
5. Publicar evento `BidReceived` e broadcast.  
6. Responder `ACCEPTED`/`REJECTED` com motivo.  
**Concurrency:** CAS ou transação serializable; dedupe por `clientSequenceId`.

### 7.5 Fluxo: Encerramento e Arremate
1. Scheduler detecta `endDateTime`.  
2. Se `antiSniping` ativo e condicional atendida, estende `endDateTime`.  
3. Ao efetivo fechamento, determinar `winningBid` (maior, tie-breaker serverSequenceId).  
4. Gerar `Sale` com `fee`, `freightEstimate`, `invoice`.  
5. Notificar buyer & seller.  
6. Atualizar status `SOLD`/`UNSOLD`.  
**Edge cases:** vários lances no último ms; retries do scheduler; falha na geração de documentos.

### 7.6 Fluxo: Pagamento e repasse
1. Buyer inicia checkout → gateway.  
2. Webhook confirma `PAID` → atualizar `Sale.statusPayment`.  
3. Agendar repasse ao vendedor conforme regras (dedução de `fee` e impostos).  
4. Registrar ledger entries.

### 7.7 Fluxo: Contestação / disputa
1. Buyer abre disputa dentro de `disputeWindow`.  
2. Sistema cria caso; bloqueia repasse.  
3. Admin analisa evidências; decide (reembolso, manutenção, reversão).  
4. Aplicar decisão e notificar partes; gerar log de auditoria.

---

## 8. Dados e modelo lógico (entidades e atributos críticos)
Entidades essenciais e atributos mínimos (apenas modelagem lógica).

### Usuario
- id: UUID  
- name: string  
- email: string (unique)  
- passwordHash: string  
- phone: string  
- status: enum {PENDING, ACTIVE, VERIFIED, SUSPENDED}  
- roles: set {VISITOR, PARTICIPANT, BUYER, SELLER, ADMIN}  
- createdAt, updatedAt

### Vendedor (Seller)
- sellerId: UUID  
- userId: UUID  
- companyName / fullName  
- taxId (CNPJ/CPF)  
- contractId, feeRate  
- documents[] (links)  
- status

### Produto (Product)
- productId: UUID  
- sellerId: UUID  
- title, description, category  
- images[] (urls)  
- weight, dimensions  
- initialPrice, currentPrice  
- endDateTime (nullable)  
- status: enum {DRAFT, PUBLISHED, ACTIVE, SOLD, UNSOLD, CANCELLED}  
- sellerNotes: text (audit append-only)  
- createdAt, updatedAt

### Lote (Lot)
- loteId: UUID  
- sellerId: UUID  
- title, description  
- loteEndDateTime  
- status: enum {DRAFT, PUBLISHED, ACTIVE, CLOSED, CANCELLED}  
- products[] (productIds)  
- createdAt, updatedAt

### Lance (Bid)
- bidId: UUID  
- productId: UUID  
- userId: UUID  
- amount: decimal  
- currency  
- timestamp (UTC)  
- clientSequenceId (nullable)  
- serverSequenceId  
- ip, userAgent  
- createdAt

### Arremate (Sale)
- saleId: UUID  
- productId, winningBidId, buyerId  
- finalPrice, feeAmount, freightEstimate  
- statusPayment: enum {PENDING, AUTHORIZED, PAID, FAILED}  
- invoiceId, documentUrl  
- createdAt

### Documento
- docId, type, storageUrl, hash, createdAt, expiresAt

### Audit/Event
- eventId, entityType, entityId, action, performedBy, timestamp, metadata

---

## 9. Segurança, autenticação e autorização (RBAC)
Definição de papéis, permissões e regras.

### 9.1 Papéis primários
- VISITOR (não autenticado)  
- PARTICIPANT (autenticado, sem KYC)  
- BUYER (KYC aprovado)  
- SELLER (vendedor)  
- ADMIN (administração)

### 9.2 Restrições e regras principais (resumo)
- **Somente BUYER** pode enviar lances.  
- **SELLER** não pode modificar campos essenciais do produto durante leilão ativo (RB-012).  
- **SELLER** não pode remover um produto de um lote ativo se houver lances (RB-013).  
- **ADMIN** pode executar correções através de fluxo de exceção (auditadas).  
- Tokens JWT com claims: `userId`, `roles`, `sellerId` (se aplicável), `exp`, `iat`, `jti`.  
- Uso de `x-correlation-id` para rastreabilidade.

### 9.3 Matriz resumida (Ação × Papel)
| Ação | VISITOR | PARTICIPANT | BUYER | SELLER | ADMIN |
|------|---------|-------------|-------|--------|-------|
| Visualizar catálogo | ✔ | ✔ | ✔ | ✔ | ✔ |
| Registrar conta | ✔ | ✔ | ✔ | ✔ (como seller) | ✔ |
| Enviar documentos KYC | ❌ | ✔ | ✔ | ✔ | ✔ |
| Dar lance | ❌ | ❌ | ✔ | ❌ | ❌ |
| Criar produto | ❌ | ❌ | ❌ | ✔ | ✔ |
| Editar produto (pré-publicação) | ❌ | ❌ | ❌ | ✔ | ✔ |
| Editar produto (ativo) | ❌ | ❌ | ❌ | ❌ | ✔ (com justificativa) |
| Remover produto de lote ativo | ❌ | ❌ | ❌ | ❌ | ✔ |
| Aprovar KYC | ❌ | ❌ | ❌ | ❌ | ✔ |
| Gerenciar disputas | ❌ | ❌ | ✔ (abrir) | ❌ | ✔ (resolver) |

---

## 10. Requisitos não-funcionais relevantes
- **Performance:** processamento e broadcast de lance < 500ms em condições normais; atualizações para espectadores <1s.  
- **Escalabilidade:** Auction/Bid services horizontais; Redis/Kafka para coordenação.  
- **Disponibilidade:** MVP target 99.5% uptime.  
- **Segurança:** TLS 1.2+, JWT RS256, senhas com bcrypt/argon2; WAF; rate-limiting.  
- **Retenção de dados:** logs/auditoria por 7 anos (configurável).  
- **Conformidade:** LGPD — endpoints para export/delete.

---

## 11. Critérios de aceite e testes funcionais recomendados
Para cada RF, definir testes automatizados e manuais. Exemplos críticos:

### Testes RF-005 (Produto bloqueado)
1. **Cenário:** Vendedor tenta alterar title de produto que está em leilão ativo → Esperado: 403 PRODUCT_LOCKED_ACTIVE_AUCTION.  
2. **Cenário:** Vendedor adiciona `sellerNotes` → Esperado: sucesso; nota auditada.  
3. **Cenário:** Admin altera product via fluxo de exceção → Esperado: sucesso; auditLog gerado com justificativa.

### Testes RF-008 (Motor de lances)
- Lance válido/aceito; invalidos (baixo valor); concorrência (100 lances/s) sem perda de ordem; tie-breakers; dedupe por clientSequenceId.

### Testes RF-007 (Encerramento)
- Produto com anti-sniping recebe extensão; fechamento idempotente; geração de invoice.

### Testes de segurança
- Ataque de força bruta (bloqueio); token replay; fuzzing de APIs.

---

## 12. Exceções, erros e tratamento de disputas
### Códigos de erro padrão (exemplos)
- 400 BAD_REQUEST — payload inválido  
- 401 UNAUTHORIZED — token inválido/expirado  
- 403 FORBIDDEN — sem permissão (inclui PRODUCT_LOCKED_ACTIVE_AUCTION, LOTE_LOCKED_HAS_BIDS)  
- 409 CONFLICT — conflito de concorrência  
- 422 UNPROCESSABLE_ENTITY — regras de domínio violadas  
- 500 INTERNAL_ERROR — erro interno (traceId retornado)

### Disputas
- Workflow: abertura → coleta de evidências → bloqueio de repasse → análise → decisão → execução → notificação.  
- Metadados e logs obrigatórios.

---

## 13. Matrizes e traceability (sugestões)
- Criar matriz RF ↔ Epic ↔ Story (Jira) para mapping waterfall→agile.  
- Traceability matrix: RF ↔ TestCase ↔ Release.  
- RBAC matrix exportável (CSV) para import no sistema de identidade.

---

## 14. Integrações externas e mensagens realtime
### 14.1 Gateway de pagamento
- Endpoints: authorize, capture, refund, webhook.  
- Segurança: assinatura HMAC; retry idempotente.

### 14.2 API de frete
- Endpoint: getRates(origin, destination, weight, dimensions).  
- Cache: TTL 1h para cotação.

### 14.3 Serviço de notificações
- Enfileiramento (SNS/SQS, Kafka, or similar).  
- Retries e DLQ (dead-letter queue).

### 14.4 Mensagens realtime (esquema)
- `BidReceived { productId, bidId, userId, amount, timestamp, serverSequenceId }`  
- `BidAccepted { ... }`  
- `BidRejected { reason }`  
- `ProductClosed { productId, winningBidId, finalPrice }`  
- Todos eventos têm `correlationId` e `traceId`.

---

## 15. Observabilidade, auditoria e retenção
- Tracing distribuído (OpenTelemetry).  
- Logs estruturados (JSON).  
- Audit trail append-only com export para cold storage.  
- Retenção: logs 7 anos; documentos S3 com política de expiração configurável.

---

## 16. Roadmap de features e notas de implantação
- **MVP:** requisitos RF-001 a RF-017 implementados com RB-012/RB-013.  
- **V1:** melhorias antifraude, escalabilidade (kafka), painéis BI.  
- **V2+:** AI bidding assistant, replay, gamification.

**Notas:** planejar scripts de migração de dados e testes de carga antes do go-live; elaborar runbook para encerramento/rollback e para incidentes de leilão ativo.

---

### Observações finais
- Este documento é base para dividir RFs em épicos e histórias (de-para).  
- Recomenda-se criar artefato de rastreabilidade (planilha/CSV) e um conjunto inicial de testes automatizados (back-end + contract tests + E2E).  
- Todas as alterações administrativas em produtos/leilões ativos devem gerar justificativa e serem auditadas.

---
