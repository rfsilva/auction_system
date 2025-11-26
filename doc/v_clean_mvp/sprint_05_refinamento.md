# Refinamento Sprint 5 — Sistema de Leilão Eletrônico

**Sprint:** 5  
**Duração:** 2 semanas  
**Dev Pleno + Sênior + Arquiteto**  

## Objetivo da Sprint
Garantir monitoramento do sistema, métricas de operação, relatórios gerenciais, ajustes de performance e finalização do MVP com estabilidade e qualidade.

---

## Histórias Detalhadas
- **Regras Gerais** 
  1. Backend: Se precisar criar entity nova, localizar primeiro a tabela em V1 do migrations. Se não encontrar, criar migration para criação da tabela.
  2. Backend: Se for necessário criar tabela, usar prefixo "tb_" e nome no singular
  3. Backend: Não criar estruturas do tipo TYPE, TRIGGER, PROCEDURE, FUNCTION no migrations
  4. Backend: Sempre que possível, aplicar Lombok para eliminar verbosidade de código
  5. Frontend: Sempre que um novo componente for criado, respeitar a separação entre TS, HTML e CSS

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
