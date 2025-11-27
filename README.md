# Auction System — Resumo do Projeto

Este repositório descreve um sistema de leilão eletrônico (MVP) com foco em leilões em tempo real, baixa latência, segurança e escalabilidade. A plataforma atende compradores, vendedores e administradores, permitindo cadastro de produtos, agrupamento em lotes, lances por produto, encerramento e arremate, cálculo de frete via API externa e cobrança de taxa de leiloeiro definida por contrato.

## Visão Geral
- Objetivo: realizar leilões online com atualização em tempo real dos lances, garantindo ordem, consistência e auditabilidade.
- Público-alvo: compradores cadastrados, vendedores profissionais e equipe de administração.
- Diretrizes: entrega por valor (MVP), custo de infra baixo, segurança (2FA, JWT, RBAC), observabilidade desde o início.

## Funcionalidades Principais (MVP)
- Comprador: visualizar itens, ver lances em tempo real, enviar lances, acompanhar histórico, receber confirmação de arremate.
- Vendedor: cadastrar produtos, criar/gerenciar lotes, definir regras e horários de encerramento, acompanhar resultados.
- Admin: gestão de usuários, aprovação/curadoria, auditoria e relatórios básicos.
- Integrações: API externa de frete; cobrança de taxa de leiloeiro conforme contrato do vendedor.

## Arquitetura em Alto Nível
- Frontend: Micro Frontends (Angular 18+) — Shell para autenticação/roteamento + MFEs de Produtos/Lotes, Lances, Perfil e Admin.
- Backend: Microserviços em Java 21 + Spring Boot 3 (Identity, Vendors, Products, Lots, Auction, Billing, Shipping Adapter, Document, Notification).
- Dados: PostgreSQL (RDS) como primário; S3 para arquivos; Redis opcional para cache/locks; mensageria com SNS/SQS (ou Kafka em evolução).
- Infra: AWS com foco em baixo custo no MVP (ECS Fargate/Lambda quando aplicável, CloudFront/S3 para web). CI/CD via GitHub Actions.
- Observabilidade: logs estruturados, métricas e tracing distribuído (OpenTelemetry), auditoria de lances.

## Comunicação em Tempo Real (SSE vs WebSocket)
- SSE: ótimo para espectadores (atualizações servidor→cliente, leve e simples). Viável para MVP com menor concorrência; não suporta canal bidirecional.
- WebSocket: recomendado para produto final e cenários intensos; canal bidirecional com baixa latência, escala bem via API Gateway WebSockets.
- Decisão: para MVP com poucos usuários, SSE pode ser adotado para broadcast; para escala e robustez, WebSockets são preferidos. Modelo sugerido com BFF terminando WebSockets e sincronizando com backend via Redis/SNS/SQS.

## Fluxos Críticos (Resumo)
1) Envio de lance: cliente POST /products/{id}/bid → validações no Auction Service → persiste lance → publica eventos → notifica clientes (WebSocket/SSE) e serviços.
2) Encerramento de produto: timer (EventBridge) ou rotina verifica horário → fecha produto → calcula vencedor → Billing calcula taxas → notificação e fatura.
3) Pagamento: checkout → fatura → webhook do gateway atualiza status → retries/segundo colocado conforme regra.

## Segurança
- Autenticação com JWT/refresh e 2FA; RBAC por perfis; criptografia in-transit/at-rest; logs de auditoria; proteção contra abuso no fluxo de lances.

## Roadmap Evolutivo
- Fase 1 (MVP): cadastros básicos, leilão com lances em tempo real (SSE ou WebSocket simples), encerramento por produto, nota de arremate, integração de frete.
- Fase 2: migração/otimização para WebSockets gerenciados, billing avançado, escalabilidade horizontal, observabilidade completa, relatórios, anti-sniping.
- Fase 3: alta escala (Kafka/MSK), recomendador, leilões simultâneos em grande volume, features avançadas (chat, apps mobile).

## Referências e Documentos
- Overview técnico e arquitetura: doc/overview_tecnico.md
- Arquitetura técnica completa: doc/arquitetura_tecnica_completa.md
- Escopo funcional inicial: doc/initial_functional_scope.md
- Análises SSE vs WebSocket: doc/analise_sse_websocket.txt e doc/analise_sse_websocket_2.md
