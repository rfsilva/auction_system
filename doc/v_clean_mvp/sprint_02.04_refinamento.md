# Sprint S2.4 — Otimizações e Funcionalidades Avançadas do Sistema de Lotes

**Sprint:** S2.4 (Complemento da Sprint 2.03)  
**Duração:** 1 semana  
**Equipe:** Dev Pleno + Dev Sênior  
**Prioridade:** Média (Otimizações e melhorias)

## 🎯 Objetivo da Sprint
Implementar otimizações de performance, melhorias de UX, funcionalidades avançadas de filtros e preparar o sistema para futuras evoluções, incluindo visualização de lotes encerrados e melhorias na experiência do vendedor.

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
- I18N estruturado para Português, Inglês, Espanhol e Italiano - Manter equalizado e atualizado em todos os idiomas

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
- NUNCA realize commits no GIT! Essa etapa será feita por um humano

---

## 📋 Histórias Detalhadas

### História 1: Filtros Avançados e Visualização de Lotes Encerrados
- **Tipo:** Frontend + Backend
- **Descrição:** Implementar filtros avançados incluindo opção de visualizar lotes encerrados e melhorar a experiência de busca.
- **Tasks / Sub-tasks:**
  1. Adicionar filtro de status (Ativos, Encerrados, Todos) - 2 SP
  2. Implementar busca por texto em lotes - 2 SP
  3. Filtros por faixa de tempo restante - 2 SP
  4. Ordenação avançada (preço, número de produtos, data criação) - 2 SP
  5. Salvar preferências de filtro do usuário - 2 SP
- **Story Points:** 10 SP

**Critérios de Aceite:**
- ✅ Usuários podem escolher visualizar lotes ativos, encerrados ou todos
- ✅ Busca por texto funciona em título e descrição dos lotes
- ✅ Filtros por tempo restante (< 1 dia, < 1 semana, < 1 mês)
- ✅ Múltiplas opções de ordenação disponíveis
- ✅ Preferências de filtro são salvas por usuário

### História 2: Otimizações de Performance e UX
- **Tipo:** Frontend + Backend
- **Descrição:** Implementar lazy loading, skeleton loading, cache otimizado e melhorias gerais de performance.
- **Tasks / Sub-tasks:**
  1. Implementar lazy loading nas imagens dos lotes e produtos - 2 SP
  2. Adicionar skeleton loading nos cards - 2 SP
  3. Otimizar queries com índices específicos - 2 SP
  4. Implementar cache Redis para lotes e produtos - 2 SP
  5. Adicionar breadcrumbs na navegação - 1 SP
  6. Implementar scroll infinito como opção - 3 SP
- **Story Points:** 12 SP

**Critérios de Aceite:**
- ✅ Lazy loading funcionando nas imagens
- ✅ Skeleton loading durante carregamentos
- ✅ Queries otimizadas (< 200ms)
- ✅ Cache implementado com TTL apropriado
- ✅ Breadcrumbs funcionais
- ✅ Scroll infinito como alternativa à paginação

### História 3: Melhorias na Experiência do Vendedor
- **Tipo:** Frontend + Backend
- **Descrição:** Adicionar funcionalidade para vendedor definir imagem de destaque do lote e melhorar gestão de produtos no lote.
- **Tasks / Sub-tasks:**
  1. Adicionar campo imagem_destaque na entidade Lote - 1 SP
  2. Implementar upload de imagem de destaque do lote - 3 SP
  3. Atualizar formulário de lote com seleção de imagem - 2 SP
  4. Implementar reordenação de produtos no lote - 3 SP
  5. Adicionar preview do lote como será exibido publicamente - 2 SP
- **Story Points:** 11 SP

**Critérios de Aceite:**
- ✅ Vendedor pode definir imagem de destaque do lote
- ✅ Upload de imagem funcional e seguro
- ✅ Formulário de lote atualizado com nova funcionalidade
- ✅ Vendedor pode reordenar produtos dentro do lote
- ✅ Preview público do lote disponível para vendedor

### História 4: Sistema de Notificações e Alertas
- **Tipo:** Frontend + Backend
- **Descrição:** Implementar sistema básico de notificações para lotes favoritos e alertas importantes.
- **Tasks / Sub-tasks:**
  1. Criar entidade Notificacao - 2 SP
  2. Implementar notificações para lotes favoritos próximos ao fim - 3 SP
  3. Sistema de alertas no frontend - 2 SP
  4. Notificações por email (básico) - 3 SP
  5. Página de gerenciamento de notificações - 2 SP
- **Story Points:** 12 SP

**Critérios de Aceite:**
- ✅ Usuários recebem notificações de lotes favoritos próximos ao fim
- ✅ Sistema de alertas visual no frontend
- ✅ Notificações por email funcionais
- ✅ Usuário pode gerenciar preferências de notificação
- ✅ Histórico de notificações disponível

### História 5: SEO e Melhorias de Acessibilidade
- **Tipo:** Frontend
- **Descrição:** Implementar SEO básico, melhorar acessibilidade e preparar para indexação pelos motores de busca.
- **Tasks / Sub-tasks:**
  1. Meta tags dinâmicas para cada lote - 2 SP
  2. Structured data (JSON-LD) para lotes - 2 SP
  3. Melhorias de acessibilidade (ARIA, navegação por teclado) - 2 SP
  4. Sitemap dinâmico - 1 SP
  5. Open Graph para compartilhamento social - 1 SP
- **Story Points:** 8 SP

**Critérios de Aceite:**
- ✅ Meta tags apropriadas em todas as páginas
- ✅ Structured data implementado para lotes
- ✅ Navegação por teclado funcional
- ✅ Sitemap gerado automaticamente
- ✅ Compartilhamento social com preview adequado

### História 6: Analytics e Métricas
- **Tipo:** Frontend + Backend
- **Descrição:** Implementar sistema básico de analytics para acompanhar visualizações de lotes e comportamento dos usuários.
- **Tasks / Sub-tasks:**
  1. Tracking de visualizações de lotes - 2 SP
  2. Métricas de produtos mais visualizados - 2 SP
  3. Dashboard básico de analytics para vendedores - 3 SP
  4. Relatório de performance de lotes - 2 SP
  5. Integração com Google Analytics (básico) - 1 SP
- **Story Points:** 10 SP

**Critérios de Aceite:**
- ✅ Visualizações de lotes são registradas
- ✅ Métricas de produtos disponíveis
- ✅ Vendedores podem ver analytics básicos de seus lotes
- ✅ Relatórios de performance funcionais
- ✅ Google Analytics integrado

---

## 🔧 Especificações Técnicas Detalhadas

### Backend - Novos Endpoints

#### 1. Filtros Avançados
```java
GET /api/lotes/catalogo-publico?status=ACTIVE&busca=eletrônicos&tempoRestante=1week&ordenacao=precoAsc&page=0&size=10
```

#### 2. Analytics
```java
POST /api/lotes/{id}/visualizar
GET /api/lotes/{id}/analytics
GET /api/vendedores/meus-lotes/analytics
```

#### 3. Notificações
```java
GET /api/notificacoes
POST /api/notificacoes/marcar-lida/{id}
PUT /api/notificacoes/preferencias
```

### Frontend - Novos Componentes

#### 1. FiltrosAvancadosComponent
```typescript
@Component({
  selector: 'app-filtros-avancados',
  templateUrl: './filtros-avancados.component.html'
})
export class FiltrosAvancadosComponent {
  filtrosForm = this.fb.group({
    status: ['ACTIVE'],
    busca: [''],
    tempoRestante: [''],
    ordenacao: ['proximoEncerramento'],
    categoria: ['']
  });
}
```

#### 2. NotificacoesComponent
```typescript
@Component({
  selector: 'app-notificacoes',
  templateUrl: './notificacoes.component.html'
})
export class NotificacoesComponent {
  notificacoes$ = this.notificacoesService.listarNotificacoes();
  naoLidas$ = this.notificacoesService.contarNaoLidas();
}
```

---

## 🎯 Critérios de Aceite da Sprint

### Funcionais
1. ✅ Filtros avançados funcionais incluindo lotes encerrados
2. ✅ Performance otimizada com lazy loading e cache
3. ✅ Vendedor pode definir imagem de destaque do lote
4. ✅ Sistema de notificações básico operacional
5. ✅ SEO implementado com meta tags dinâmicas
6. ✅ Analytics básico funcionando

### Técnicos
1. ✅ Queries otimizadas < 200ms
2. ✅ Cache Redis implementado
3. ✅ Lazy loading funcionando
4. ✅ Structured data válido
5. ✅ Acessibilidade melhorada
6. ✅ Tracking de analytics funcionando

### UX/UI
1. ✅ Skeleton loading implementado
2. ✅ Breadcrumbs funcionais
3. ✅ Scroll infinito como opção
4. ✅ Interface de notificações intuitiva
5. ✅ Preview de lote para vendedor
6. ✅ Dashboard de analytics claro

---

## 🚀 Plano de Implementação

### Dia 1-2: Filtros e Performance (22 SP)
- **História 1:** Filtros Avançados (10 SP)
- **História 2:** Otimizações de Performance (12 SP)

### Dia 3-4: Vendedor e Notificações (23 SP)
- **História 3:** Melhorias do Vendedor (11 SP)
- **História 4:** Sistema de Notificações (12 SP)

### Dia 5-7: SEO e Analytics (18 SP)
- **História 5:** SEO e Acessibilidade (8 SP)
- **História 6:** Analytics e Métricas (10 SP)

---

## 📊 Estrutura de Dados Adicional

### Novas Entidades

```java
// Notificacao
@Entity
@Table(name = "tb_notificacao")
public class Notificacao {
    @Id
    private String id;
    
    @Column(name = "usuario_id", nullable = false)
    private String usuarioId;
    
    @Column(name = "tipo", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoNotificacao tipo;
    
    @Column(name = "titulo", nullable = false)
    private String titulo;
    
    @Column(name = "mensagem", nullable = false)
    private String mensagem;
    
    @Column(name = "lida", nullable = false)
    private Boolean lida = false;
    
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
    
    @Column(name = "data_leitura")
    private LocalDateTime dataLeitura;
}

// LoteVisualizacao (para analytics)
@Entity
@Table(name = "tb_lote_visualizacao")
public class LoteVisualizacao {
    @Id
    private String id;
    
    @Column(name = "lote_id", nullable = false)
    private String loteId;
    
    @Column(name = "usuario_id")
    private String usuarioId; // null para usuários não logados
    
    @Column(name = "ip_address")
    private String ipAddress;
    
    @Column(name = "user_agent")
    private String userAgent;
    
    @Column(name = "data_visualizacao", nullable = false)
    private LocalDateTime dataVisualizacao;
}

// Adicionar campo em Lote
@Entity
@Table(name = "tb_lote")
public class Lote {
    // ... campos existentes
    
    @Column(name = "imagem_destaque")
    private String imagemDestaque;
    
    @Column(name = "total_visualizacoes")
    private Long totalVisualizacoes = 0L;
}
```

---

## 📝 Regras de Negócio Adicionais

### Filtros Avançados
1. **Status "Todos":** Apenas para usuários logados
2. **Lotes Encerrados:** Exibidos apenas quando explicitamente solicitado
3. **Busca:** Funciona em título, descrição e tags dos produtos do lote
4. **Preferências:** Salvas no localStorage + backend para usuários logados

### Notificações
1. **Lotes Favoritos:** Notificação 24h antes do encerramento
2. **Frequência:** Máximo 1 notificação por lote por usuário
3. **Tipos:** LOTE_ENCERRANDO, NOVO_PRODUTO_LOTE_FAVORITO, LOTE_CANCELADO
4. **Email:** Apenas se usuário optou por receber

### Analytics
1. **Visualizações:** Contadas apenas uma vez por usuário por sessão
2. **Anonimização:** IPs são hasheados após 30 dias
3. **Retenção:** Dados mantidos por 1 ano
4. **Privacidade:** Usuários podem optar por não serem rastreados

---

**Story Points Totais Sprint S2.4:** 63 SP  
**Estimativa:** 1 semana com 2 desenvolvedores  
**Dependências:** Sprint S2.3 deve estar completa  
**Risco:** Baixo (funcionalidades complementares)