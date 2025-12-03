# Sprint S2.2 — Dashboard de Contratos e Visibilidade Administrativa

**Sprint:** S2.2 (Complemento da Sprint 2.01)  
**Duração:** 1 semana  
**Equipe:** Dev Pleno + Dev Sênior  
**Prioridade:** Alta (Complementa modelo de negócio)

## 🎯 Objetivo da Sprint
Implementar a camada de visibilidade administrativa que estava pendente da Sprint 2.01, fornecendo dashboards, relatórios e métricas para que administradores possam acompanhar contratos, comissões e projeções de receita da plataforma.

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
- I18N estruturado - novas mensagens devem ser catalogadas corretamente e traduzidas por MessageSourceAccessor

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

---

## 📋 Histórias Detalhadas

### História 1: Endpoints de Estatísticas de Contratos
- **Tipo:** Backend
- **Descrição:** Criar endpoints para fornecer estatísticas consolidadas de contratos para o dashboard administrativo.
- **Tasks / Sub-tasks:**
  1. Criar ContratoEstatisticasDto com métricas principais - 1 SP
  2. Implementar endpoint GET /contratos/estatisticas - 2 SP
  3. Implementar queries de agregação no repository - 2 SP
  4. Adicionar testes unitários para estatísticas - 1 SP
- **Story Points:** 6 SP

**Critérios de Aceite:**
- ✅ Endpoint retorna: total de contratos por status, vendedores ativos, receita projetada
- ✅ Performance < 500ms para consultas de estatísticas
- ✅ Dados atualizados em tempo real
- ✅ Tratamento de erros adequado

### História 2: Sistema de Cálculo de Comissões
- **Tipo:** Backend
- **Descrição:** Implementar cálculos de comissões por período e projeções de receita baseadas nos contratos ativos.
- **Tasks / Sub-tasks:**
  1. Criar ComissaoDto e ComissaoCalculoService - 2 SP
  2. Implementar endpoint GET /contratos/comissoes com filtros de período - 2 SP
  3. Implementar lógica de cálculo de comissões realizadas vs projetadas - 3 SP
  4. Adicionar endpoint para projeções de receita - 1 SP
- **Story Points:** 8 SP

**Critérios de Aceite:**
- ✅ Calcula comissões por período (dia, semana, mês)
- ✅ Separa comissões realizadas de projetadas
- ✅ Permite filtros por vendedor, categoria, status
- ✅ Retorna breakdown detalhado por contrato

### História 3: Relatórios de Contratos Vencendo
- **Tipo:** Backend
- **Descrição:** Implementar sistema de relatórios para contratos próximos ao vencimento com diferentes níveis de urgência.
- **Tasks / Sub-tasks:**
  1. Completar implementação do ContratoSchedulerService - 1 SP
  2. Criar endpoint GET /contratos/vencendo com parâmetros configuráveis - 2 SP
  3. Implementar notificações automáticas para contratos vencendo - 2 SP
  4. Criar relatório exportável (CSV/PDF) - 2 SP
- **Story Points:** 7 SP

**Critérios de Aceite:**
- ✅ Lista contratos vencendo em 7, 15, 30 dias (configurável)
- ✅ Notificações automáticas para administradores
- ✅ Exportação em CSV e PDF
- ✅ Scheduler funcionando corretamente

### História 4: Dashboard Administrativo de Contratos
- **Tipo:** Frontend
- **Descrição:** Criar interface completa de dashboard para administradores visualizarem métricas, gráficos e relatórios de contratos.
- **Tasks / Sub-tasks:**
  1. Criar AdminDashboardComponent com layout responsivo - 3 SP
  2. Implementar cards de métricas principais - 2 SP
  3. Integrar Chart.js e criar gráficos interativos - 4 SP
  4. Criar tabela de contratos vencendo com ações - 2 SP
  5. Implementar filtros e período selecionável - 2 SP
  6. Adicionar auto-refresh e loading states - 1 SP
- **Story Points:** 14 SP

**Critérios de Aceite:**
- ✅ Dashboard carrega em < 2 segundos
- ✅ Gráficos interativos e responsivos
- ✅ Auto-refresh a cada 30 segundos
- ✅ Filtros funcionais por período
- ✅ Ações rápidas para contratos vencendo

### História 5: Integração e Otimização
- **Tipo:** Enabler
- **Descrição:** Otimizar performance, adicionar cache e integrar todos os componentes do dashboard.
- **Tasks / Sub-tasks:**
  1. Implementar cache Redis para estatísticas - 2 SP
  2. Otimizar queries com índices específicos - 1 SP
  3. Adicionar compressão de resposta para relatórios - 1 SP
  4. Implementar rate limiting para endpoints de relatórios - 1 SP
  5. Testes de integração completos - 2 SP
- **Story Points:** 7 SP

**Critérios de Aceite:**
- ✅ Cache reduz tempo de resposta em 70%
- ✅ Queries otimizadas < 200ms
- ✅ Rate limiting configurado
- ✅ Testes de integração passando

---

## 🔧 Especificações Técnicas Detalhadas

### Backend - Novos Endpoints

#### 1. Estatísticas de Contratos
```java
GET /api/contratos/estatisticas
Response: {
  "totalContratos": 150,
  "contratosPorStatus": {
    "ACTIVE": 120,
    "DRAFT": 15,
    "EXPIRED": 10,
    "CANCELLED": 5
  },
  "vendedoresAtivos": 85,
  "receitaProjetadaMes": 45000.00,
  "receitaRealizadaMes": 38500.00,
  "taxaMediaComissao": 0.065,
  "contratosVencendo30Dias": 12
}
```

#### 2. Cálculo de Comissões
```java
GET /api/contratos/comissoes?inicio=2024-01-01&fim=2024-01-31&vendedor=123
Response: {
  "periodo": {
    "inicio": "2024-01-01",
    "fim": "2024-01-31"
  },
  "resumo": {
    "totalComissoes": 15750.00,
    "totalVendas": 250000.00,
    "numeroTransacoes": 45
  },
  "porContrato": [
    {
      "contratoId": "abc-123",
      "vendedorNome": "João Silva",
      "categoria": "Eletrônicos",
      "taxaComissao": 0.05,
      "vendas": 50000.00,
      "comissoes": 2500.00,
      "transacoes": 8
    }
  ]
}
```

#### 3. Contratos Vencendo
```java
GET /api/contratos/vencendo?dias=30&incluirNotificados=false
Response: {
  "contratos": [
    {
      "id": "contract-123",
      "vendedorNome": "Maria Santos",
      "categoria": "Geral",
      "validTo": "2024-02-15",
      "diasRestantes": 7,
      "status": "ACTIVE",
      "urgencia": "ALTA",
      "notificado": false
    }
  ],
  "resumo": {
    "total": 12,
    "urgenciaAlta": 3,
    "urgenciaMedia": 5,
    "urgenciaBaixa": 4
  }
}
```

### Frontend - Componentes

#### 1. AdminDashboardComponent
```typescript
@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent implements OnInit {
  estatisticas$ = new BehaviorSubject<ContratoEstatisticas | null>(null);
  comissoes$ = new BehaviorSubject<ComissaoRelatorio | null>(null);
  contratosVencendo$ = new BehaviorSubject<ContratoVencendo[]>([]);
  
  // Configurações de período
  periodoSelecionado = 'mes';
  autoRefresh = true;
  
  // Gráficos
  chartContratosPorStatus: Chart | null = null;
  chartComissoesPorMes: Chart | null = null;
}
```

#### 2. Layout do Dashboard
```html
<div class="admin-dashboard">
  <!-- Cards de Métricas -->
  <div class="metrics-row">
    <app-metric-card 
      title="Contratos Ativos" 
      [value]="estatisticas?.totalContratos" 
      icon="fas fa-file-contract"
      color="primary">
    </app-metric-card>
    
    <app-metric-card 
      title="Receita do Mês" 
      [value]="estatisticas?.receitaRealizadaMes | currency" 
      icon="fas fa-dollar-sign"
      color="success">
    </app-metric-card>
  </div>
  
  <!-- Gráficos -->
  <div class="charts-row">
    <div class="chart-container">
      <canvas #chartContratos></canvas>
    </div>
    <div class="chart-container">
      <canvas #chartComissoes></canvas>
    </div>
  </div>
  
  <!-- Tabela de Contratos Vencendo -->
  <app-contratos-vencendo-table 
    [contratos]="contratosVencendo$ | async">
  </app-contratos-vencendo-table>
</div>
```

---

## 🎯 Critérios de Aceite da Sprint

### Funcionais
1. ✅ Dashboard carrega todas as métricas em < 2 segundos
2. ✅ Gráficos são interativos e responsivos
3. ✅ Relatórios de comissões precisos e filtráveis
4. ✅ Contratos vencendo listados com urgência correta
5. ✅ Notificações automáticas funcionando
6. ✅ Exportação de relatórios em CSV/PDF

### Técnicos
1. ✅ Cache implementado com TTL adequado
2. ✅ Queries otimizadas < 200ms
3. ✅ Rate limiting configurado
4. ✅ Testes unitários > 80% cobertura
5. ✅ Documentação API atualizada

### UX/UI
1. ✅ Interface intuitiva e responsiva
2. ✅ Loading states em todas as operações
3. ✅ Tratamento de erros amigável
4. ✅ Auto-refresh configurável
5. ✅ Filtros funcionais e claros

### Segurança
1. ✅ Apenas admins podem acessar dashboard
2. ✅ Rate limiting em endpoints de relatórios
3. ✅ Validação de permissões em todas as operações
4. ✅ Log de auditoria para ações sensíveis

---

## 📊 Estrutura de Dados

### DTOs Necessários

```java
// ContratoEstatisticasDto
public class ContratoEstatisticasDto {
    private Long totalContratos;
    private Map<ContractStatus, Long> contratosPorStatus;
    private Long vendedoresAtivos;
    private BigDecimal receitaProjetadaMes;
    private BigDecimal receitaRealizadaMes;
    private BigDecimal taxaMediaComissao;
    private Long contratosVencendo30Dias;
}

// ComissaoDto
public class ComissaoDto {
    private String contratoId;
    private String vendedorNome;
    private String categoria;
    private BigDecimal taxaComissao;
    private BigDecimal totalVendas;
    private BigDecimal totalComissoes;
    private Long numeroTransacoes;
    private LocalDate periodo;
}

// ComissaoRelatorioDto
public class ComissaoRelatorioDto {
    private PeriodoDto periodo;
    private ComissaoResumoDto resumo;
    private List<ComissaoDto> porContrato;
}

// ContratoVencendoDto
public class ContratoVencendoDto {
    private String id;
    private String vendedorNome;
    private String categoria;
    private LocalDateTime validTo;
    private Long diasRestantes;
    private ContractStatus status;
    private UrgenciaEnum urgencia;
    private Boolean notificado;
}

// UrgenciaEnum
public enum UrgenciaEnum {
    ALTA,    // <= 7 dias
    MEDIA,   // 8-15 dias
    BAIXA    // 16-30 dias
}
```

---

## 🚀 Plano de Implementação

### Dia 1-2: Backend Core (14 SP)
- **História 1:** Endpoints de Estatísticas (6 SP)
- **História 2:** Sistema de Cálculo de Comissões (8 SP)
- Configurar estrutura base dos DTOs
- Implementar queries de agregação

### Dia 3-4: Backend Avançado (7 SP)
- **História 3:** Relatórios de Contratos Vencendo (7 SP)
- Completar scheduler service
- Implementar notificações automáticas
- Criar exportação de relatórios

### Dia 5-7: Frontend e Integração (21 SP)
- **História 4:** Dashboard Administrativo (14 SP)
- **História 5:** Integração e Otimização (7 SP)
- Implementar componentes de dashboard
- Integrar Chart.js
- Otimizar performance e cache
- Testes de integração

---

## 🔧 Regras de Negócio Específicas

### Estatísticas
1. **Atualização**: Cache de 5 minutos para estatísticas gerais
2. **Performance**: Queries devem usar índices otimizados
3. **Precisão**: Dados financeiros com 2 casas decimais
4. **Período**: Estatísticas baseadas no mês corrente por padrão

### Comissões
1. **Cálculo**: Baseado no valor final de venda × taxa do contrato
2. **Período**: Filtros por dia, semana, mês, trimestre, ano
3. **Projeção**: Baseada em contratos ativos e histórico de vendas
4. **Breakdown**: Detalhamento por contrato, vendedor e categoria

### Contratos Vencendo
1. **Urgência Alta**: <= 7 dias para vencimento
2. **Urgência Média**: 8-15 dias para vencimento
3. **Urgência Baixa**: 16-30 dias para vencimento
4. **Notificações**: Automáticas aos 30, 15, 7 e 1 dia antes do vencimento

### Dashboard
1. **Auto-refresh**: A cada 30 segundos (configurável)
2. **Responsividade**: Funcional em desktop, tablet e mobile
3. **Performance**: Carregamento inicial < 2 segundos
4. **Interatividade**: Gráficos clicáveis com drill-down

---

## 📝 Anotações e Pontos Pendentes

### Dependências Externas
- **Chart.js**: Biblioteca para gráficos (adicionar ao package.json)
- **Redis**: Para cache de estatísticas (configurar se não existir)
- **Scheduler**: Verificar se @EnableScheduling está configurado

### Configurações Necessárias
- **Cache TTL**: Configurar tempo de vida do cache
- **Rate Limiting**: Definir limites por endpoint
- **Notificações**: Configurar templates de email
- **Exportação**: Configurar biblioteca para PDF (iText ou similar)

### Melhorias Futuras
- **Alertas**: Sistema de alertas configuráveis
- **Métricas Avançadas**: ROI por vendedor, tendências sazonais
- **Integração**: Webhook para sistemas externos
- **Mobile**: Versão mobile do dashboard

---

**Story Points Totais Sprint S2.2:** 42 SP  
**Estimativa:** 1 semana com 2 desenvolvedores  
**Dependências:** Sprint S2.1 deve estar 100% completa  
**Risco:** Baixo (funcionalidades complementares, não críticas)