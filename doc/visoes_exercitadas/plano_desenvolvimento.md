# 🏗️ Plano de Desenvolvimento — Sistema de Leilão Online
Times:
- 1 Desenvolvedor Sênior  
- 1 Desenvolvedor Pleno  
- 1 Arquiteto (Rodrigo)

---

# 🎯 Temas do Produto
1. Core do Sistema de Leilão  
2. Gestão de Usuários e Perfis  
3. Pagamentos, Repasse e Compliance  
4. Notificações e Comunicação  
5. Observabilidade, Segurança e Infraestrutura  
6. Diferenciais e Inovações Futuras (Tema Futuro)

---

# 🧩 Épicos por Tema + Histórias + Enablers

---

## Tema 1 — Core do Sistema de Leilão

### Épico 1.1 — Estrutura e Funcionamento dos Leilões

#### Histórias Funcionais
- H1.1.1 — Criar leilão com título, descrição, fotos, preço inicial e regras.
- H1.1.2 — Editar leilão antes da abertura.
- H1.1.3 — Consultar lista de leilões ativos.
- H1.1.4 — Consultar detalhes de um leilão.
- H1.1.5 — Encerramento automático do leilão.

#### Enablers
- E1.1.A — Definição e criação do modelo de domínio (DDD).
- E1.1.B — Configuração da base com versionamento (Flyway).
- E1.1.C — Criação da API base com estrutura limpa (controllers/services/repos).
- E1.1.D — Auditoria de entidades (timestamps + createdBy).

---

### Épico 1.2 — Lances e Competição

#### Histórias Funcionais
- H1.2.1 — Usuário registrar um lance válido.
- H1.2.2 — Impedir lances inferiores ao atual.
- H1.2.3 — Registrar histórico de lances.
- H1.2.4 — Atualização em tempo real do maior lance.
- H1.2.5 — Notificar quando o usuário for superado (outbid).

#### Enablers
- E1.2.A — Configuração de WebSockets/EventStream.
- E1.2.B — Implementação de lock otimista para concorrência.
- E1.2.C — Testes de carga para lances simultâneos.
- E1.2.D — Integração de cache distribuído (Redis).

---

## Tema 2 — Gestão de Usuários e Perfis

### Épico 2.1 — Onboarding e Autenticação

#### Histórias Funcionais
- H2.1.1 — Cadastro de usuários.
- H2.1.2 — Login com JWT.
- H2.1.3 — Recuperação de senha.
- H2.1.4 — Verificação de e-mail.

#### Enablers
- E2.1.A — Configuração de Keycloak/Auth0 ou solução própria.
- E2.1.B — Implementação de RBAC básico.
- E2.1.C — Middleware global de autenticação.

---

### Épico 2.2 — Perfis, Reputação e Histórico

#### Histórias Funcionais
- H2.2.1 — Visualização de perfil público.
- H2.2.2 — Histórico de leilões passados.
- H2.2.3 — Sistema básico de avaliação (rating).

#### Enablers
- E2.2.A — Mapeamentos e índices de busca (Elasticsearch opcional).
- E2.2.B — Estrutura para analytics de comportamento.

---

## Tema 3 — Pagamentos, Repasse e Compliance

### Épico 3.1 — Pagamentos

#### Histórias Funcionais
- H3.1.1 — Compra do item após vencer o leilão.
- H3.1.2 — Integração com gateway de pagamento.
- H3.1.3 — Tratamento de falha no pagamento.

#### Enablers
- E3.1.A — Padrões de antifraude.
- E3.1.B — Webhooks + filas (Rabbit/Kafka).
- E3.1.C — Testes de pagamento simulados.

---

### Épico 3.2 — Repasse ao vendedor

#### Histórias Funcionais
- H3.2.1 — Sistema de escrow.
- H3.2.2 — Regras de liberação do pagamento.
- H3.2.3 — Painel financeiro.

#### Enablers
- E3.2.A — Ledger interno.
- E3.2.B — Logs imutáveis.

---

## Tema 4 — Notificações e Comunicação

### Épico 4.1 — Notificações gerais

#### Histórias Funcionais
- H4.1.1 — Notificação por e-mail.
- H4.1.2 — Notificação por push.
- H4.1.3 — Notificação app-interna.

#### Enablers
- E4.1.A — Serviço de templates.
- E4.1.B — Filas de envio assíncrono.

---

## Tema 5 — Observabilidade, Segurança e Infraestrutura

### Épico 5.1 — Monitoramento e Logs

#### Histórias Funcionais
- H5.1.1 — Logs estruturados.
- H5.1.2 — Dashboards de métricas.
- H5.1.3 — Alertas.

#### Enablers
- E5.1.A — Stack ELK ou OpenSearch.
- E5.1.B — OpenTelemetry (traces e métricas).

---

### Épico 5.2 — Segurança

#### Enablers
- E5.2.A — Hardening da API.
- E5.2.B — Rate limiting.
- E5.2.C — Políticas de CORS + headers.

---

## Tema 6 — Diferenciais Futuros

### Ideias
- Leilões relâmpago com gamificação  
- Lances automatizados (auto-bid com limites)  
- Fraud detection com IA  
- Matching entre vendedores e potenciais compradores  
- VR/3D para exibição de produtos  

---

# 🚀 Plano de Sprints (8 Sprints)

Considerações:  
- Sprints de 2 semanas  
- 1 dev sênior + 1 dev pleno  
- Arquiteto atuando fortemente nas 3 primeiras sprints  
- Enablers nas primeiras sprints para sustentar escalabilidade e robustez

---

## Sprint 1 — Fundações Técnicas + Primeiro Módulo

### Enablers
- E1.1.A — Modelo de domínio (DDD)  
- E1.1.B — Flyway  
- E1.1.C — API base  
- E1.1.D — Auditoria  
- E2.1.A — Autenticação (base)

### Funcionais
- H2.1.1 — Cadastro  
- H2.1.2 — Login  
- H1.1.1 — Criar leilão (versão inicial)

---

## Sprint 2 — Leilões + Usuários

### Enablers
- E2.1.B — RBAC  
- E1.2.B — Lock otimista  
- E1.2.D — Redis básico

### Funcionais
- H1.1.3 — Listar leilões  
- H1.1.4 — Consultar detalhes  
- H2.2.1 — Perfil público  

---

## Sprint 3 — Lances (Parte 1)

### Enablers
- E1.2.A — WebSockets/EventStream  
- Ajustes de domínio

### Funcionais
- H1.2.1 — Registrar lance  
- H1.2.2 — Validar lance  
- H1.2.3 — Histórico de lances  

---

## Sprint 4 — Lances (Parte 2) + Notificações

### Enablers
- E4.1.A — Templates de e-mail  
- E4.1.B — Filas  

### Funcionais
- H1.2.4 — Atualização em tempo real  
- H1.2.5 — Notificar outbid  
- H4.1.1 — Notificações por e-mail  

---

## Sprint 5 — Pagamentos

### Enablers
- E3.1.A — Antifraude  
- E3.1.B — Webhooks  
- E3.1.C — Testes simulados  

### Funcionais
- H3.1.1 — Pagamento  
- H3.1.2 — Gateway externo  
- H3.1.3 — Tratamento de falha  

---

## Sprint 6 — Repasse & Financeiro

### Enablers
- E3.2.A — Ledger  
- E3.2.B — Logs imutáveis  

### Funcionais
- H3.2.1 — Escrow  
- H3.2.2 — Liberação de pagamento  
- H3.2.3 — Painel financeiro  

---

## Sprint 7 — Observabilidade

### Enablers
- E5.1.A — Logs estruturados  
- E5.1.B — OpenTelemetry  

### Funcionais
- H5.1.1 — Logs  
- H5.1.2 — Dashboards  
- H5.1.3 — Alertas  

---

## Sprint 8 — Segurança + Fechamentos

### Enablers
- E5.2.A — Hardening  
- E5.2.B — Rate Limiting  
- E5.2.C — CORS  

### Funcionais
- Ajustes finais  
- Refino de UX  
- Preparação de release  

---

