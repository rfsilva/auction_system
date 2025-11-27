# Plano de Sprints — Sistema de Leilão Eletrônico

## Premissas
- 2 desenvolvedores: 1 pleno + 1 sênior  
- Arquiteto: responsável por decisões e revisões  
- Sprints: 2 semanas  
- MVP prioriza: cadastro, autenticação, catálogo, leilões ao vivo (SSE/WebSocket), lances, encerramento, notificações essenciais  

---

## Sprint 1 — Fundamentos e Bases (Infra + Arquitetura + Estruturas Core)
**Objetivo:** Criar fundações do sistema para suportar o MVP  
**Entregas:**
- Setup do projeto backend monolito modularizado  
- Setup do front Angular 18 (sem BFF, sem MFEs)  
- Setup de CI/CD básico  
- Setup de banco de dados (schema inicial)  
- Implementar módulo de usuários (cadastro, login + JWT, RBAC básico)  
- Criar modelo de domínio principal (Produto, Leilão, Lance)  
- Prova de conceito SSE/WebSocket  

**Esforço estimado:**
- Dev Sênior: 100%  
- Dev Pleno: 100%  
- Arquiteto: 40–50%  

---

## Sprint 2 — Catálogo + Estrutura dos Leilões (Parte 1)
**Objetivo:** Disponibilizar navegação pública e gerência inicial de leilões  
**Entregas:**
- Listagem pública de produtos e leilões ativos  
- Detalhe do produto + leilão  
- Backend: CRUD básico de produtos (vendedores/admins)  
- Backend: Estrutura de Leilão (status, datas, regras básicas)  
- Upload de imagens (simples)  
- UX/UI inicial para catálogo  
- Integração SSE para updates pré-ao vivo  

**Esforço estimado:**
- Dev Sênior: 100%  
- Dev Pleno: 100%  
- Arquiteto: 25–35%  

---

## Sprint 3 — Leilão Ao Vivo (Realtime Engine – Parte 1)
**Objetivo:** Tornar o leilão ao vivo com streaming de eventos  
**Entregas:**
- Engine de Leilão (início, andamento, encerramento)  
- Canal SSE para broadcast do estado do leilão  
- Prova de stress test inicial  
- Regras de incrementos mínimos e validação de horário  
- UI/UX: sala de leilão com updates automáticos  

**Esforço estimado:**
- Dev Sênior: 100%  
- Dev Pleno: 100%  
- Arquiteto: 35–40%  

---

## Sprint 4 — Leilão Ao Vivo (Realtime Engine – Parte 2 — Lances)
**Objetivo:** Permitir envio de lances e disputa entre compradores  
**Entregas:**
- Protocolo de envio de lances (REST + broadcast SSE)  
- Concorrência nos lances (lock otimista/pessimista)  
- Validações:
  - evitar lances simultâneos com preço igual  
  - bloquear lances após encerramento  
  - validar permissões e limites  
- UI/UX de envio de lance  
- Logs transacionais de lances  

**Esforço estimado:**
- Dev Sênior: 100%  
- Dev Pleno: 100%  
- Arquiteto: 40%  

---

## Sprint 5 — Encerramento, Finalização e Pós-Leilão
**Objetivo:** Fechar o ciclo completo do leilão  
**Entregas:**
- Regra de encerramento automático  
- Tela de resumo do vencedor  
- Notificação comprador/vendedor  
- Histórico completo do leilão  
- API de consulta pública  
- UI de histórico para compradores/vendedores  
- Fallback SSE → polling leve  

---

## Sprint 6 — Módulos do Vendedor (Painel Básico)
**Objetivo:** Permitir gestão de produtos e leilões pelos vendedores  
**Entregas:**
- Painel do vendedor:
  - criar produto  
  - cadastrar leilão  
  - definir regras (incremento mínimo, preço inicial, tempo)  
  - gerenciar imagens  
- Painel de desempenho pós-leilão (básico)  
- Relatórios mínimos: ativos, encerrados, itens sem lance  

---

## Sprint 7 — Módulo do Administrador (Governança Básica)
**Objetivo:** Criar governança do sistema  
**Entregas:**
- Painel do administrador:
  - gestão de usuários  
  - gestão de vendedores  
  - auditoria básica (acesso, lances, leilões)  
- Regras administrativas: bloquear conta, suspender vendedor, remover anúncios fraudulentos  
- Monitoramento ao vivo dos leilões  

---

## Sprint 8 — Estabilidade, Segurança e Hardening Técnico
**Objetivo:** Fortalecer a plataforma antes da abertura pública  
**Entregas:**
- Hardening de segurança (JWT, roles, logs, antiflood)  
- Testes de carga (SSE + concorrência)  
- Melhora na escalabilidade e tolerância a falhas  
- Otimizações de queries  
- Health checks, métricas e dashboards  
- Preparação para multi-instância  

---

## Sprint 9 — Diferenciais de Mercado (Geração de Valor Real)
**Objetivo:** Implementar diferenciais competitivos  
**Entregas:**
- Favoritar leilões  
- Alertas inteligentes (última chance, horário crítico)  
- Painel de comportamento comprador  
- Painel avançado do vendedor (analytics de venda)  
- Ranking de vendedores  
- Recomendações personalizadas (versão inicial)  

---

## Sprint 10 — Polimento final + GoLive
**Objetivo:** Preparar o sistema para produção  
**Entregas:**
- Correções finais e UX  
- Testes integrados ponta-a-ponta  
- Revisão de acessibilidade  
- Plano de rollback  
- Publicação em produção  

---

## Resumo do Roadmap (Tabela)

| Sprint | Foco | Valor entregue |
|-------|------|----------------|
| 1 | Fundações + Usuários/Auth | Base sólida + login funcionando |
| 2 | Catálogo + Produtos + Leilões estáticos | Catálogo navegável |
| 3 | Realtime (parte 1) | Leilão ao vivo em tempo real |
| 4 | Lances + Regras críticas | Disputa real com segurança |
| 5 | Encerramento + Pós-Leilão | Leilão completo do início ao fim |
| 6 | Painel do vendedor | Venda ativa e gestão própria |
| 7 | Painel do admin | Governança do marketplace |
| 8 | Segurança + Escalabilidade | Estabilidade da plataforma |
| 9 | Diferenciais de mercado | Competitividade e valor agregado |
| 10 | Polimento + GoLive | Disponibilização pública |
