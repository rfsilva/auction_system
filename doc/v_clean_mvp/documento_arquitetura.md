# Documento de Arquitetura — Sistema de Leilão Eletrônico

**Versão:** 1.0  
**Data:** 2025-11-19  
**Autor:** Arquitetura / Produto

---

## Sumário
1. Visão Geral  
2. Objetivos da Arquitetura  
3. Decisões Arquiteturais Principais  
4. Visão Técnica (componentes e responsabilidades)  
5. Realtime: Estratégia e justificativa  
6. Módulos do Backend (monolito modular)  
7. Infraestrutura e implantação (MVP)  
8. Segurança e conformidade  
9. Observabilidade e operações  
10. Dados e persistência  
11. Regras críticas do Auction Engine  
12. Integrações externas  
13. Restrições, riscos e mitigações  
14. Roadmap de evolução  
15. Próximos passos

---

## 1. Visão Geral
O Sistema de Leilão Eletrônico permite que compradores e vendedores publiquem, visualizem e disputem produtos em leilões online com suporte a operações em tempo real, arremate, pagamento e geração de documentos. O front-end será uma aplicação Angular 18+ (single app). O backend será um **monolito modular** em Spring Boot 3 (Java 21), organizado em módulos por domínio para permitir evolução e futura separação.

---

## 2. Objetivos da Arquitetura
- Entregar o MVP rapidamente com risco controlado.  
- Garantir baixa latência nas atualizações de lance.  
- Facilitar escalabilidade horizontal do backend quando necessário.  
- Preservar capacidade de decomposição futura em microsserviços.  
- Manter custos de infra compatíveis com MVP.

---

## 3. Decisões Arquiteturais Principais
1. **Sem BFF:** Front chama backend diretamente via REST/HTTP + WebSocket/SSE.  
2. **Frontend único (Angular 18):** standalone components, `provideHttpClient(withFetch())`, rotas e páginas monolíticas.  
3. **Backend monolito modular:** módulos por domínio (Auth, Catalog, Auction, Payments, Docs, Audit, etc.).  
4. **Realtime híbrido:** SSE para espectadores, WebSocket para participantes (bidders). Backend gerencia ambos.  
5. **Redis:** Pub/Sub para broadcast entre instâncias e locks distribuídos para operações críticas (ex.: encerramento).  
6. **PostgreSQL:** fonte de verdade para transações e histórico.  
7. **S3 (ou equivalente):** storage de mídias e documentos (invoices, termos).  
8. **Infra AWS (MVP):** S3, RDS, ElastiCache, ECS/EKS ou EC2; CloudFront para frontend se necessário.

---

## 4. Visão Técnica — Componentes e Responsabilidades
### Frontend (Angular 18)
- Pages: Home/Catalog, Product Details, Auction Live, My Bids, Seller Dashboard, Admin Panel.  
- Services: AuthService, ApiService (withFetch), RealtimeService (SSE/WS), UiComponents.  
- Responsabilidades: UI, validação cliente, manejo de tokens, conexão realtime, UX de lances.

### Backend (Spring Boot 3 — Monolito Modular)
- Controllers (REST endpoints) por módulo.  
- Service Layer contendo regras de negócio.  
- Repositories (JPA/Hibernate) para persistência.  
- Event Bus Adapter (Redis Pub/Sub) para broadcast/locks.  
- Scheduler para jobs de encerramento.  
- PDF/Document worker para geração/armazenamento.  
- Notification worker para e-mail/push.

### Infraestruturas auxiliares
- PostgreSQL (persistência relacional).  
- Redis (locks, pub/sub).  
- S3 (documentos e imagens).  
- Payment Gateway (externo).  
- Freight API (externo).  
- Notification provider (SMTP/SES, push).

---

## 5. Realtime — Estratégia e Justificativa
- **Espectadores (read-only):** usar **SSE** (Server-Sent Events). SSE é simples de implementar, reconecta automaticamente, funciona bem para grande quantidade de espectadores e é adequado quando a comunicação é majoritariamente unidirecional do servidor ao cliente.
- **Bidders (users que enviam lances):** usar **WebSocket** para comunicação bidirecional com feedback imediato (aceitação/rejeição do lance).  
- **Escalabilidade:** Redis Pub/Sub replica eventos entre instâncias do backend; cada instância mantém as conexões SSE/WS com seus clientes. Para WebSocket em múltiplas instâncias, considerar ALB com suporte a WebSocket ou arquitetura com sticky sessions; alternativa mais escalável é usar um layer de mensagens (socket server) ou managed service posteriormente.
- **Fallback:** caso WebSocket indisponível, usar polling leve com ETag/If-Modified.

---

## 6. Módulos do Backend (Monolito Modular)
1. **Auth & Users** — login, registro, JWT, refresh tokens, RBAC.  
2. **Seller & Contracts** — cadastro vendedor, contratos, feeRate versionado.  
3. **Catalog (Products & Lots)** — CRUD produtos, lotes, regras de publicação.  
4. **Auction Engine** — regras de leilão, encerramento, anti-sniping, winner selection.  
5. **Bid Service** — validação de lances, persistência append-only, ordering.  
6. **Payment Service** — integração com gateway, webhooks, ledger para repasses.  
7. **Document Service** — geração de PDFs, hash, armazenamento S3.  
8. **Notification Service** — e-mails, push, retries.  
9. **Audit & Logging** — gravação append-only de eventos sensíveis.  
10. **Scheduler** — jobs de encerramento, retries e tarefas periódicas.  
11. **Realtime Adapter** — WS/SSE handlers, Redis Pub/Sub integration.

---

## 7. Infraestrutura e Implantação (MVP)
- **Deployment target (MVP):** ECS Fargate (ou EC2 auto-scaling simples).  
- **CI/CD:** GitHub Actions — build/test/push containers para GHCR, deploy para dev/stage/prod.  
- **Networking:** ALB para HTTP/HTTPS; WebSocket via ALB; CORS configurado só para domínios autorizados.  
- **Storage:** S3 para assets e documentos; lifecycle policy para arquivos antigos.  
- **Database:** RDS PostgreSQL com tamanho pequeno para MVP; backups automáticos; ponto de restauração.  
- **Cache/Locks:** ElastiCache Redis (single/multi-AZ conforme budget).  
- **Monitoring:** CloudWatch (logs, metrics) + Grafana/Prometheus (opcional).  
- **Secrets:** AWS Secrets Manager / Parameter Store.

---

## 8. Segurança e Conformidade
- **Autenticação:** JWT (RS256) com refresh tokens rotativos.  
- **Autorização:** RBAC no backend (roles: VISITOR, PARTICIPANT, BUYER, SELLER, ADMIN).  
- **Criptografia:** TLS em trânsito; dados sensíveis (senha) hashed com bcrypt/argon2.  
- **Proteção:** rate-limiting, validações server-side, sanitização de inputs.  
- **Documentos:** links expiráveis para download de PDFs.  
- **LGPD:** endpoints para exportar/excluir dados pessoais; logs com retenção controlada.  
- **Auditoria:** todas as ações críticas gravadas (append-only) com traceId e usuário.

---

## 9. Observabilidade e Operações
- **Logging:** logs estruturados JSON (requestId/correlationId) em todos os módulos.  
- **Tracing:** OpenTelemetry para traços distribuídos (backend).  
- **Metrics:** métricas de latência e taxa de erros exportadas (Prometheus / CloudWatch).  
- **Alerting:** thresholds para erros, latência e uso de recursos.  
- **Runbooks:** procedimentos para incidentes em leilões ativos (rollback, failover, comunicação).

---

## 10. Dados e Persistência
- **Modelo principal:** PostgreSQL com tabelas para users, sellers, products, lots, bids, sales, payments, documents, audit.  
- **Padrões:** transações ACID para operações críticas (lance + persistência); uso de serializable/optimistic locking conforme cenário; append-only para lances e audit.  
- **Backup & Recovery:** snapshots diários do RDS; plano de restauração testado.

---

## 11. Regras Críticas do Auction Engine
- Lance válido quando `amount >= currentPrice + minIncrement`.  
- Persistência append-only com `serverSequenceId` para ordenação determinística.  
- Empates resolvidos por `serverSequenceId` (menor ganha) ou timestamp (se aplicável).  
- Anti-sniping: se habilitado, estende `endDateTime` por configuração quando lance recebido dentro da janela.  
- Encerramento: job adquiri lock distribuído (Redis) antes de fechar o item; fechamento idempotente.  
- Vendedor não pode alterar campos essenciais do produto enquanto estiver ACTIVE em leilão (retornar 403 PRODUCT_LOCKED_ACTIVE_AUCTION).  
- Remoção de produto de lote ativo proibida se produto já tiver recebido lance (retornar 403 LOTE_LOCKED_HAS_BIDS).

---

## 12. Integrações Externas
- **Payment Gateway:** authorize, capture, refund, webhooks (HMAC).  
- **Freight API:** cotação de frete por CEP/medidas; TTL nas cotações (cache 1h).  
- **Notification Provider:** SMTP/SES e push providers.  
- **Storage:** S3 para arquivos; lifecycle e versioning habilitáveis.

---

## 13. Restrições, Riscos e Mitigações
### Riscos técnicos principais
- **Acoplamento do monolito:** disciplina modular e ADRs para evitar bagunça. Mitigação: revisão de PRs e padrões de package/module.  
- **Escalabilidade de WebSocket:** se crescer, considerar extrair socket layer ou usar managed socket service; mitigar com Redis Pub/Sub e ALB sticky sessions.  
- **Concorrência em encerramento:** mitigar com locks distribuídos (Redis) e testes E2E de stress.  
- **Custos infra:** escolher instâncias pequenas no MVP, monitorar e ajustar.  

### Restrições
- Sem BFF → backend assume agregações que poderiam ser delegadas.  
- Frontend single app → deploy mais simples mas maior bundle.

---

## 14. Roadmap de Evolução
- **Curto prazo:** estabilizar MVP, testes de carga, hardening de segurança.  
- **Médio prazo:** extrair Auction Engine e Payment em serviços independentes se necessário; otimizar realtime layer; adicionar antifraude.  
- **Longo prazo:** event sourcing para histórico, ML para recomendações, live streaming integrado.

---

## 15. Próximos Passos Imediatos
1. Validar decisões com time (devs + ops).  
2. Gerar ADRs para as decisões chave (sem BFF, uso de SSE/WS, monolito modular).  
3. Criar skeleton do monolito com módulos mínimos (Auth, Catalog, Auction, Bid).  
4. Implementar Redis Pub/Sub e lock pattern.  
5. Implementar testes de carga básicos para o fluxo de lances.  
6. Preparar runbook para incidentes em leilões ativos.

---

### Observações finais
Esse documento representa a visão arquitetural para o MVP conforme as decisões recentes: sem gateway, sem BFF, frontend Angular single app e backend monolito modular. Mantive práticas que permitem evolução incremental e eventual decomposição em microsserviços. Se quiser que eu gere os ADRs, os diagramas C4 (separadamente), ou o documento técnico com contratos de API (OpenAPI) eu já faço no próximo passo.

