# SISTEMA DE LEILÃO ELETRÔNICO — EVOLUÇÃO 2.0
## Documento Funcional (Detalhado)  
### Referente à ampliação dos módulos do MVP (versão 1.0 já concluída)

---

# 1. OBJETIVO

Esta documentação descreve a **Evolução 2.0** do Sistema de Leilão Eletrônico, considerando que a **versão 1.0 (MVP)** já está totalmente implementada.  
O objetivo da v2.0 é:

- Criar **dashboards inteligentes** para cada perfil (Comprador, Vendedor e Administrador).  
- Expandir funcionalidades baseadas em engajamento, histórico e dados já coletados na v1.0.  
- Fornecer insights operacionais, métricas avançadas, projeções e visão consolidada.  
- Evoluir as regras e capacidades do motor de leilão.  
- Aproveitar integralmente os componentes, integrações e modelos da versão 1.0.

---

# 2. ESCOPO DA EVOLUÇÃO 2.0

## 2.1. Escopo Funcional

### **NOVOS MÓDULOS**
1. Dashboard do Comprador  
2. Dashboard do Vendedor  
3. Dashboard do Administrador  
4. Sistema de Recomendação (baseado em histórico e comportamento)  
5. Motor de Insights & Alertas Inteligentes  
6. Métricas Avançadas e Telemetria  
7. Consolidação de Histórico Enriquecido  
8. Ranking e Gamificação (opcional-configurável)  
9. Refinamento das APIs v1.0 para suportar agregações e filtros avançados  
10. Mecanismo de Análise de Risco / Fraude (Admin)

### **Módulos reutilizados da versão 1.0**
- Autenticação (Keycloak/OAuth2/OpenID Connect)
- Catálogo e Produto  
- Leilões (estrutura + real time SSE/WebSocket)  
- Fluxo do Comprador  
- Fluxo do Vendedor  
- Administração  
- Auditoria (incluindo itens, usuários e eventos)  
- Observabilidade & Métricas (Prometheus + Logs)  
- Favoritos, Histórico, Perfis  
- Relatórios Operacionais

Todos esses componentes serão **estendidos**, e não reescritos.

---

# 3. MÓDULOS DA EVOLUÇÃO 2.0

---

# 3.1. DASHBOARD DO COMPRADOR

## 3.1.1. Objetivo
Fornecer ao comprador uma visão agregada do seu comportamento, performance e oportunidades relevantes no sistema.

## 3.1.2. Dados de Origem (componentes v1.0)
- Histórico de Lances  
- Histórico de Leilões Participados  
- Favoritos  
- Itens Visualizados  
- Alertas de Leilões  
- Usuário / Perfil (v1.0)

## 3.1.3. Funcionalidades
- **Visão geral do comprador** (KPIs do perfil)
- **Resumo de atividade recente**
  - Qtd. leilões participados nos últimos 7 dias  
  - Média de lances por leilão  
- **Performance**
  - Win Rate  
  - Média de último lance  
  - Disputa por categoria
- **Próximos Leilões de Interesse**
  - Baseado nos favoritos  
  - Baseado em comportamento (v2.0)
- **Alertas inteligentes**
  - Leilões prestes a começar  
  - Leilões em finalização  
  - Leilões historicamente de interesse do comprador  
- **Recomendações personalizadas** (v2.0)

## 3.1.4. Regras de Negócio
1. O comprador só vê dados próprios.  
2. KPIs são calculados sempre com base em dados já auditados (v1.0).  
3. Recomendações devem considerar:
   - Histórico de favoritos  
   - Tipos de item mais visualizados  
   - Leilões com maior taxa de engajamento similares

## 3.1.5. Casos de Uso

### **UC-COMP-01 — Visualizar Dashboard do Comprador**
**Fluxo Principal:**  
1. Usuário acessa a home após login  
2. Sistema consulta agregações no serviço de analytics (v2.0)  
3. Sistema renderiza cards KPI  
4. Sistema apresenta recomendações  
5. Sistema exibe alertas em tempo real  

### **UC-COMP-02 — Ver Recomendações**
1. Comprador solicita lista de recomendações  
2. Sistema avalia perfil + histórico + similaridade de categorias  
3. Sistema retorna lista priorizada  

---

# 3.2. DASHBOARD DO VENDEDOR

## 3.2.1. Objetivo
Permitir ao vendedor acompanhar performance, engajamento e andamento de seus leilões e itens.

## 3.2.2. Dados reutilizados (v1.0)
- Itens  
- Leilões  
- Lances realizados em seus itens  
- Disputas / ocorrências  
- Auditoria de itens  
- Estatísticas por categoria

## 3.2.3. Funcionalidades
- **Performance dos Leilões por Período**
- **Visão de Engajamento**
  - Qtd. interessados  
  - Qtd. lances  
  - Média do lance  
- **Status consolidado dos itens**
- **Alertas**
  - Baixo engajamento  
  - Problemas de documentação  
- **Insight automático**
  - Comparação com vendedores similares (anonimizado)

## 3.2.4. Regras de Negócio
- Vendedor só vê seus próprios itens e leilões.  
- Insight de comparação deve ser anonimizado.  
- Sugestões de título, descrição ou categoria são opcionais.

## 3.2.5. Casos de Uso

### **UC-VEND-01 — Visualizar Dashboard do Vendedor**
### **UC-VEND-02 — Consultar Engajamento por Item**
### **UC-VEND-03 — Receber Alertas de Baixo Engajamento**

---

# 3.3. DASHBOARD DO ADMINISTRADOR

## 3.3.1. Objetivo
Dar ao admin visão macro operacional e capacidade rápida de detecção de problemas.

## 3.3.2. Dados reutilizados (v1.0)
- Auditoria (Envers, tabela *_AUD)  
- Logs estruturados  
- Métricas Prometheus já disponíveis  
- Relatórios operacionais  
- Usuários ativos  
- Leilões por status  
- Latência, throughput e erros

## 3.3.3. Funcionalidades
- **Visão de Saúde do Sistema**
  - Erros  
  - Timeouts  
  - Performance das APIs  
- **Visão Operacional**
  - Quantidade de leilões criados  
  - Taxa de conclusão  
  - Engajamento médio  
- **Detecção de Risco / Fraude (v2.0)**
  - Padrões anômalos de lances  
  - Disparos em lote  
  - Múltiplas contas relacionadas  
- **Monitoramento de Usuários Sensíveis**

## 3.3.4. Casos de Uso
- UC-ADMIN-01 — Visualizar Dashboard Admin  
- UC-ADMIN-02 — Acompanhar Saúde do Sistema  
- UC-ADMIN-03 — Detectar Fraude  

---

# 4. SISTEMA DE RECOMENDAÇÃO (v2.0)

### Objetivo
Analisar histórico do usuário, categorias e padrões coletivos para sugerir leilões relevantes.

### Dados utilizados
- Histórico de favoritos (v1.0)  
- Histórico de lances (v1.0)  
- Categorias dos itens (v1.0)  
- Perfis semelhantes (v2.0)

### Algoritmo inicial
- Regras determinísticas  
- Similaridade por categoria  
- Ordenação por engajamento

---

# 5. MOTOR DE INSIGHTS & ALERTAS

Fornece:
- Alertas de risco  
- Alertas de oportunidades  
- Análise de padrões  
- Sugestões de melhoria  

Tipos de alerta:
1. Engajamento baixo (vendedor)  
2. Finalização iminente (comprador)  
3. Erros operacionais (admin)  
4. Anomalias de atividade (admin)

---

# 6. RELATÓRIOS AVANÇADOS (v2.0)

Relatórios adicionais:
- Engajamento por categoria  
- Conversão de compradores  
- Eficiência de vendedores  
- Tempo médio de disputa  
- Preço final × preço esperado  

---

# 7. CRITÉRIOS DE ACEITE (geral da v2.0)

### Para dashboards
- KPIs carregam em < 1s com dados pré-agregados  
- Vendedor só vê seus dados  
- Admin vê tudo  
- Comprador vê apenas seu histórico  
- Recomendações têm relevância mínima (regra determinística)

### Para insights
- Alertas devem ser registráveis e auditáveis  
- Cada insight deve possuir:
  - descrição  
  - regra aplicada  
  - timestamp  
  - origem

### Para relatórios avançados
- Todos exportáveis para PDF/CSV  
- Tempo máximo de geração < 5s (para dataset médio)

---

# 8. INTEGRAÇÕES (v2.0)

- Serviço Analytics (novo)  
- Serviço de Aggregation API (estende backend v1.0)  
- Expansão do módulo de auditoria  
- Integração com observabilidade (Prometheus/Grafana)

---

# 9. MODELO DE DADOS — EXTENSÕES

Novas tabelas sugeridas:
- DASH_EVENT  
- INSIGHT  
- RECOMMENDATION  
- METRIC_AGGREGATED (pré-cálculo)  
- FRAUD_PATTERN  

Tabelas v1.0 reutilizadas:
- USER  
- ITEM  
- AUCTION  
- BID  
- FAVORITE  
- HISTORY  
- AUDIT_*  

---

# 10. RASTREABILIDADE v1.0 x v2.0

| Módulo v2.0 | Dados/Serviços v1.0 Reutilizados | Extensões v2.0 |
|-------------|----------------------------------|----------------|
| Dashboard Comprador | histórico, favoritos, lances | insights, recomendações |
| Dashboard Vendedor | itens, leilões, lances | engajamento, alertas |
| Dashboard Admin | auditoria, métricas | fraude, telemetria |
| Insights | leilões, lances | motor de regras |
| Recomendação | favoritos, histórico | modelo de recomendação |
| Relatórios Avançados | relatórios existentes | agregações novas |

---

# 11. CONCLUSÃO

A versão 2.0 expande a inteligência, visibilidade e capacidade operacional do sistema, **sem reescrever o que já existe**, mas **empilhando valor sobre a base sólida construída na v1.0**.
