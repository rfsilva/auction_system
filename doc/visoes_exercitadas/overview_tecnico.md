# Overview Técnico & Arquitetura — Sistema de Leilão Eletrônico (MVP)

**Versão:** 1.0
**Data:** 17/11/2025
**Autor:** Assistente (para Rodrigo)

---

## 1. Objetivo

Documento em formato Markdown que apresenta um **overview técnico** e um **desenho de arquitetura** para o MVP do sistema de leilão eletrônico, considerando as tecnologias e restrições apresentadas:

* Front-end em **Micro Frontends (MFE)** com **Angular 18+** (Shell + módulos)
* Backend em **Microserviços Java 21 + Spring Boot 3**
* Banco **PostgreSQL**
* Repositórios e CI/CD: **GitHub + GitHub Actions**
* Infra: **AWS** com foco em custo reduzido para MVP
* Integração com **API externa de frete**
* Taxa de leiloeiro aplicada por contrato (vendedor)

---

## 2. Princípios de Projeto (MVP)

* **Entregas por valor:** priorizar caminhos críticos: cadastro, publicação de produtos/lotes, lances por produto (tempo real básico), encerramento, arremate e emissão de nota.
* **Baixo custo infra:** usar serviços gerenciados com escalonamento automático e/ou serverless onde possível.
* **Desacoplamento:** MFEs independentes e microserviços por domínio para permitir entrega paralela.
* **Observabilidade desde o início:** logs estruturados, métricas e tracing distribuído.
* **Segurança:** 2FA, JWT, RBAC, criptografia at-rest/in-transit.

---

## 3. Componentes Principais

### 3.1 Front-end (MFE)

* **Shell (container)**: responsável por roteamento global, autenticação/SSO compartilhada, layout, carregamento dinâmico de MFEs e UX comum.
* **MFE Produtos/Lotes**: listagem, filtros, detalhe do produto (cronômetro individual), envio de documentos (vendedor).
* **MFE Lances/Leilão**: interface de bidding, websocket client, histórico de lances por produto.
* **MFE Perfil/Conta**: cadastro, 2FA, documentos, pagamentos.
* **MFE Admin**: painel restrito para aprovações e relatórios.

Observação: cada MFE é deploy independente e entregue como bundle hospedado em S3 + CloudFront; shell faz composition (module federation ou similar).

### 3.2 Backend (Microserviços)

* **Identity Service**: cadastro, autenticação, 2FA, emissão de JWT/refresh tokens, integração com external IdP se necessário.
* **Vendors Service**: cadastro de vendedor, contratos, taxa leiloeiro.
* **Products Service**: CRUD de produtos; associações a lote; metadados (peso, dimensões, encerramento por produto).
* **Lots Service**: gerenciamento de lotes (agrupamento de produtos), status do lote.
* **Auction Service**: orquestra fluxos de lances, persistência de lances, regras de incremento, anti-sniping por produto, publicação de eventos de lance. **Componente crítico para consistência.**
* **Billing Service**: cálculo de taxa de leiloeiro, geração de fatura / integração com gateway de pagamento.
* **Shipping Adapter Service**: proxy para API externa de frete — padroniza e faz caching de cotações.
* **Document Service**: armazenamento e versionamento de documentos (laudos, contratos) — usa S3.
* **Notification Service**: fila de envio para e-mail/SMS/push (via SNS/SES/Twilio).

### 3.3 Infraestrutura de Mensageria / Eventos

* **Event Bus (Kafka ou AWS MSK)** — recomendado para escala, mas para MVP pode-se usar **Amazon SQS + SNS** para filas e publicação/assinatura.
* **EventBridge** — para timers/cron e orquestração de encerramento de produto/lote quando preferir eventos serverless.

### 3.4 Data Stores

* **Primary relational DB:** PostgreSQL (RDS Serverless v2 ou RDS provisioned pequeno para MVP)

  * Schema por serviço (schema-per-service) ou banco único com schemas separados.
* **Cache:** Redis (ElastiCache) para sessão/contagem de lances e locks (opcional para MVP usar cache in-memory ou DynamoDB/ElastiCache compartilhado).
* **Object Storage:** S3 (arquivos, imagens, documentos, bundles de MFE)

### 3.5 CI/CD e Repositório

* **GitHub Host:** monorepo ou multi-repos (recomendado: multi-repo por MFE e microserviço)
* **GitHub Actions:** pipelines para build, test e deploy. Deploy para ECS Fargate ou AWS Lambda (se optar por serverless para alguns serviços).

### 3.6 Observability

* **Logs:** CloudWatch Logs (ou ELK/Opensearch) com logs estruturados e auditoria de lances.
* **Tracing:** OpenTelemetry, coletado em TempoReal para traces distribuídos.
* **Métricas:** Prometheus → Grafana (ou CloudWatch Metrics para MVP)

---

## 4. Fluxos Críticos (Resumo Técnico)

### 4.1 Fluxo de envio de lance (simplificado)

1. Cliente (MFE Lances) envia `POST /products/{id}/bid` com JWT.
2. API Gateway/ALB encaminha para Auction Service (stateless handler).
3. Auction Service valida: sessão, saldo/limite, horário de encerramento do produto, incremento mínimo.
4. Auction Service publica evento `BidReceived` em Event Bus (SQS/SNS/Kafka) e persiste o lance (append-only table em PostgreSQL).
5. Subscribers: Auction Service (consuming), Notification Service (envia notify ao outbid), Websocket dispatcher (via API Gateway + WebSocket) publica atualização para clientes.
6. Se lance em últimos N segundos → Auction Service chama rotina de extensão (EventBridge ou scheduler interno).

### 4.2 Fluxo de encerramento de produto

1. EventBridge (cron/timer) ou Auction Service verifica `encerramento_datetime` do produto.
2. Ao atingir a hora, Auction Service fecha o produto: calcula vencedor, publica `ProductClosed` event.
3. Billing Service calcula taxa do leiloeiro (consultando Vendor Contract) e chama Shipping Adapter para cotação de frete.
4. Notification Service envia nota de arremate + fatura para comprador e vendedor.

### 4.3 Fluxo de pagamento

1. Comprador realiza checkout → Billing Service gera invoice e requisita pagamento ao Gateway.
2. Gateway dispara webhook para `POST /webhook/payment` que atualiza status da transação.
3. Em caso de falha, regras de retry e oferta ao segundo colocado (se aplicável).

---

## 5. Diagrama de Arquitetura (Mermaid)

Abaixo um diagrama mermaid representando o layout de componentes. (Se o viewer suportar Mermaid, renderizará o diagrama.)

```mermaid
flowchart LR
  subgraph Frontend
    Shell[Shell (Angular 18+)]
    MFE_Products[Products MFE]
    MFE_Bids[Bids MFE]
    MFE_Admin[Admin MFE]
  end

  subgraph CDN
    S3[S3 - bundles & assets]
    CF[CloudFront]
  end

  Shell --> CF
  MFE_Products --> CF
  MFE_Bids --> CF
  MFE_Admin --> CF
  CF --> S3

  subgraph AWS_Network
    ALB[ALB / API Gateway]
    WebSocket[API Gateway WebSocket]
    ECS[ECS Fargate Cluster]
    RDS[(PostgreSQL RDS)]
    SQS[SQS]
    SNS[SNS]
    S3Objs[S3 Documents]
    Redis[ElastiCache Redis]
    EventBridge[EventBridge]
  end

  CF --> ALB
  Shell --> ALB
  MFE_Products --> ALB

  ALB --> ECS
  WebSocket --> ECS
  ECS --> RDS
  ECS --> S3Objs
  ECS --> SQS
  SQS --> ECS
  ECS --> SNS
  ECS --> Redis
  EventBridge --> ECS

  subgraph Microservices[Services]
    Identity[Identity Service]
    Products[Products Service]
    Lots[Lots Service]
    Auction[Auction Service]
    Billing[Billing Service]
    Shipping[Shipping Adapter]
    Document[Document Service]
    Notification[Notification Service]
  end

  ALB --> Identity
  ALB --> Products
  ALB --> Lots
  ALB --> Auction
  ALB --> Billing
  ALB --> Shipping
  ALB --> Document
  ALB --> Notification

  Auction --> SQS
  Auction --> SNS
  Auction --> RDS
  Auction --> WebSocket
  Billing --> Gateway[(Payment Gateway)]
  Shipping --> ExternalFrete[(API Externa de Frete)]
  Notification --> SES[(SES / SMS Provider)]

  Gateway --> Billing
  ExternalFrete --> Shipping

  RDS -->|writes| Audit[(Append-only Audit Logs)]
  Redis -->|locks/counters| Auction

```

---

## 6. Decisões Técnicas e Justificativas

* **Angular 18+ MFE:** facilita divisão de responsabilidades, deploys independentes e times paralelos. Module Federation (Webpack) ou abordagem de lazy loading com shell.
* **Java 21 + Spring Boot 3:** maturidade, ecosistema, suporte a reactive (WebFlux) se necessário.
* **PostgreSQL (RDS Serverless v2 recomendado para MVP):** maturidade e operações ACID.
* **ECS Fargate (Spot) para reduz custos:** containers gerenciados sem necessidade de EC2.
* **SQS/SNS/EventBridge para MVP em vez de Kafka:** menor custo operacional e simplicidade.
* **S3 + CloudFront:** hosting econômico para bundles MFE e assets.

---

## 7. Considerações de Consistência e Concurrency

* **Lances:** usar persistência append-only e event sourcing parcial; usar Redis para contadores/locks e garantir ordenação por timestamp + versioning.
* **Idempotência:** endpoints expostos (webhooks) devem ser idempotentes.
* **Compensações:** padrões SAGA via queues para transações distribuídas (pagamento e liquidação).

---

## 8. Segurança

* **Autenticação:** JWT (RS256) e refresh tokens armazenados com revogação.
* **2FA:** TOTP (authenticator) + SMS (via SNS/Twilio)
* **Segurança de APIs:** WAF, rate limiting no API Gateway/ALB, proteção contra DDoS (Shield AWS se necessário)
* **Criptografia:** TLS 1.2+; dados sensíveis em RDS criptografados at-rest via KMS; S3 com SSE.
* **LGPD:** endpoints para exportação e exclusão de dados; consent management.

---

## 9. Observabilidade e SLOs

* **SLOs (MVP):** disponibilidade 99.5%; latência de leitura < 200ms; latência de gravação (lance) < 1s (ideal).
* **Logs:** estrutura JSON + requestId para rastreabilidade.
* **Alerting:** CloudWatch alarms para erros críticos e picos de latência.

---

## 10. Plano de Implantação (MVP) — Passos Rápidos

1. Provisionar infra mínima (S3, CloudFront, RDS pequena, ECS cluster) com IaC (Terraform).
2. Configurar repositórios e GitHub Actions (build/test/deploy).
3. Implementar Identity + Products + Auction (API minimal) e Shell MFE.
4. Habilitar WebSocket e Notification flows.
5. Executar testes de carga contra Auction Service.
6. Go-live em ambiente controlado (feature flags).

---

## 11. Checklist de MVP (mínimo aceitável)

* [ ] Shell + 2 MFEs (Products e Bids) funcionando em produção
* [ ] Identity Service com 2FA
* [ ] Products Service e Auction Service com persistência em RDS
* [ ] WebSocket notifications (via API Gateway)
* [ ] Billing Service com cálculo de taxa (mock gateway)
* [ ] Shipping Adapter integrado (mock) com cache de cotações
* [ ] CI/CD via GitHub Actions
* [ ] Observability mínima (logs + métricas)

---

## 12. Próximos Passos Recomendados

* Gerar **especificação OpenAPI** de todos os endpoints críticos.
* Criar **diagramas de sequência** para fluxos de lance, encerramento e pagamento.
* Prototipar MFE Shell + Products MFE (POC com composition).
* Realizar POC de throughput para Auction Service (simular picos de lance).

---

*Fim do documento — Overview Técnico e Arquitetura (MVP).*
