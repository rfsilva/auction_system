# Refinamento Sprint 5 — Sistema de Leilão Eletrônico

**Sprint:** 5  
**Duração:** 2 semanas  
**Dev Pleno + Sênior + Arquiteto**  

## Objetivo da Sprint
Garantir monitoramento do sistema, métricas de operação, relatórios gerenciais, ajustes de performance e finalização do MVP com estabilidade e qualidade.

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

### História 1: Monitoramento e Health Checks
- **Tipo:** Enabler
- **Descrição:** Implementar monitoramento contínuo e endpoints de saúde do sistema.
- **Tasks / Sub-tasks:**
  1. Criar endpoints de health check para backend (DB, filas, serviços externos) - 2 SP  
  2. Integrar logs e métricas no Prometheus/Grafana ou similar - 3 SP  
  3. Alertas para falhas críticas (ex.: falha no motor de lances, pagamento ou notificações) - 2 SP  
  4. Testar cenários de falha simulada - 2 SP  
- **Story Points:** 9 SP

### História 2: Métricas de Sistema e KPIs
- **Tipo:** Enabler
- **Descrição:** Coletar métricas de uso, performance, lances, arremates e erros.
- **Tasks / Sub-tasks:**
  1. Criar dashboards com número de lances, produtos arrematados, tempo médio de fechamento - 3 SP  
  2. Métricas de performance: latência de lances, taxa de rejeição, throughput - 2 SP  
  3. Integrar métricas ao painel administrativo para acompanhamento diário - 2 SP  
  4. Testar precisão dos dados e consistência histórica - 2 SP  
- **Story Points:** 9 SP

### História 3: Relatórios Gerenciais
- **Tipo:** Funcional
- **Descrição:** Gerar relatórios para vendedores, compradores e administração.
- **Tasks / Sub-tasks:**
  1. Relatório de produtos arrematados por período e vendedor - 2 SP  
  2. Relatório financeiro: taxas do leiloeiro, repasses e pagamentos - 2 SP  
  3. Relatórios de disputas abertas e resolvidas - 2 SP  
  4. Exportação em PDF/Excel - 1 SP  
  5. Testes de geração e consistência dos dados - 2 SP  
- **Story Points:** 9 SP

### História 4: Ajustes de Performance
- **Tipo:** Enabler
- **Descrição:** Otimizar pontos críticos do sistema (motor de lances, SSE/WebSocket, consultas).
- **Tasks / Sub-tasks:**
  1. Analisar logs e métricas de performance das sprints anteriores - 2 SP  
  2. Ajustar queries e endpoints de maior carga - 3 SP  
  3. Otimizar motor de lances para alta concorrência - 3 SP  
  4. Testes de stress simulando cenários de pico - 2 SP  
- **Story Points:** 10 SP

### História 5: Finalização e Homologação do MVP
- **Tipo:** Funcional
- **Descrição:** Revisar e validar todas funcionalidades do MVP, corrigir bugs críticos.
- **Tasks / Sub-tasks:**
  1. Testes ponta a ponta de todos fluxos: cadastro, lances, arremate, pagamentos, disputas - 3 SP  
  2. Correção de inconsistências ou bugs críticos - 3 SP  
  3. Validação final com stakeholders - 2 SP  
  4. Documentação de release e notas de homologação - 1 SP  
- **Story Points:** 9 SP

---

## Observações
- **Dependências:**  
  - Sprint 1-4 concluídas: motor de lances, pagamentos, disputas, auditoria, notificações.  
  - Logs e métricas configurados corretamente.

- **Critérios de Aceite (Sprint 5):**
  1. Sistema monitorado com alertas funcionando.  
  2. Dashboards de métricas e KPIs consistentes e confiáveis.  
  3. Relatórios gerenciais gerados corretamente em todos formatos.  
  4. Performance otimizada com latência de lances <500ms e SSE/WebSocket <1s.  
  5. MVP funcional e homologado com bugs críticos corrigidos.  

---

**Story Points Totais Sprint 5:** 46 SP
