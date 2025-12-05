# Sprint S2.3 — Reestruturação das Telas Públicas para Sistema de Lotes

**Sprint:** S2.3 (Complemento das Sprints 2.01 e 2.02)  
**Duração:** 1 semana  
**Equipe:** Dev Pleno + Dev Sênior  
**Prioridade:** Alta (Experiência do usuário público)

## 🎯 Objetivo da Sprint
Reestruturar as telas públicas do sistema para focar na experiência baseada em lotes, removendo elementos mockados e criando uma jornada de usuário centrada na navegação por lotes e seus produtos, com destaque para lotes próximos ao encerramento.

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

---

## 📋 Histórias Detalhadas

### História 1: Reestruturação da Navegação e Remoção de Elementos Mockados
- **Tipo:** Frontend
- **Descrição:** Remover a opção "Leilões" do menu principal e limpar elementos mockados das telas públicas.
- **Tasks / Sub-tasks:**
  1. Remover link "Leilões" do menu principal - 1 SP
  2. Limpar dados mockados da página home - 1 SP
  3. Atualizar rotas removendo /auctions - 1 SP
  4. Revisar e limpar componentes não utilizados - 1 SP
- **Story Points:** 4 SP

**Critérios de Aceite:**
- ✅ Menu principal não possui mais a opção "Leilões"
- ✅ Página home sem dados mockados (estatísticas falsas)
- ✅ Rotas limpas e organizadas
- ✅ Componentes não utilizados removidos

### História 2: Transformação do Catálogo em Catálogo de Lotes
- **Tipo:** Frontend + Backend
- **Descrição:** Transformar o catálogo atual de produtos em um catálogo de lotes, onde o usuário navega por lotes e depois acessa os produtos do lote.
- **Tasks / Sub-tasks:**
  1. Criar endpoint GET /lotes/catalogo-publico - 2 SP
  2. Atualizar LoteService para catálogo público - 1 SP
  3. Refatorar CatalogoComponent para exibir lotes - 3 SP
  4. Criar componente LoteCardComponent - 2 SP
  5. Implementar filtros específicos para lotes - 2 SP
- **Story Points:** 10 SP

**Critérios de Aceite:**
- ✅ Catálogo exibe lotes ao invés de produtos
- ✅ Cada lote mostra informações resumidas (título, descrição, tempo restante, número de produtos)
- ✅ Filtros funcionais por categoria, status, tempo restante
- ✅ Performance adequada na listagem de lotes

### História 3: Página de Detalhes do Lote com Produtos
- **Tipo:** Frontend + Backend
- **Descrição:** Criar página de detalhes do lote onde o usuário pode navegar pelos produtos que compõem o lote.
- **Tasks / Sub-tasks:**
  1. Criar endpoint GET /lotes/{id}/produtos-publico - 2 SP
  2. Criar LoteDetalhesComponent - 3 SP
  3. Implementar navegação entre produtos do lote - 2 SP
  4. Adicionar informações do lote (tempo restante, regras, etc.) - 2 SP
  5. Implementar rota /lotes/{id} pública - 1 SP
- **Story Points:** 10 SP

**Critérios de Aceite:**
- ✅ Página de detalhes do lote acessível publicamente
- ✅ Lista todos os produtos do lote com imagens e informações básicas
- ✅ Informações do lote (tempo restante, descrição, regras)
- ✅ Navegação intuitiva entre produtos
- ✅ Responsividade completa

### História 4: Sistema de Favoritos de Lotes
- **Tipo:** Frontend + Backend
- **Descrição:** Implementar sistema para usuários logados favoritarem lotes de interesse.
- **Tasks / Sub-tasks:**
  1. Criar entidade LoteFavorito - 2 SP
  2. Criar endpoints de favoritos (adicionar/remover/listar) - 3 SP
  3. Implementar botão de favoritar nos cards de lote - 2 SP
  4. Criar página "Meus Lotes Favoritos" - 2 SP
  5. Adicionar indicadores visuais de lotes favoritados - 1 SP
- **Story Points:** 10 SP

**Critérios de Aceite:**
- ✅ Usuários logados podem favoritar/desfavoritar lotes
- ✅ Indicador visual nos lotes favoritados
- ✅ Página dedicada aos lotes favoritos
- ✅ Persistência dos favoritos no banco de dados
- ✅ Performance adequada nas operações de favoritos

### História 5: Página Home Inteligente com Lotes em Destaque
- **Tipo:** Frontend + Backend
- **Descrição:** Reestruturar a página home para exibir lotes em destaque baseados no status do usuário (logado/não logado).
- **Tasks / Sub-tasks:**
  1. Criar endpoint GET /lotes/destaque para usuários não logados - 2 SP
  2. Criar endpoint GET /lotes/meus-interesses para usuários logados - 2 SP
  3. Refatorar HomeComponent com lógica inteligente - 3 SP
  4. Criar seção "Lotes em Destaque" - 2 SP
  5. Implementar seção "Seus Lotes de Interesse" - 2 SP
  6. Adicionar call-to-actions apropriados - 1 SP
- **Story Points:** 12 SP

**Critérios de Aceite:**
- ✅ Usuários não logados veem lotes próximos ao encerramento
- ✅ Usuários logados veem lotes favoritos + próximos ao encerramento
- ✅ Seções bem definidas e responsivas
- ✅ Call-to-actions que direcionam para cadastro ou login
- ✅ Performance < 2 segundos no carregamento

### História 6: Otimizações e Melhorias de UX
- **Tipo:** Frontend + Backend
- **Descrição:** Implementar melhorias de UX, otimizações de performance e ajustes finais na experiência pública.
- **Tasks / Sub-tasks:**
  1. Implementar lazy loading nas imagens dos lotes - 1 SP
  2. Adicionar skeleton loading nos cards - 1 SP
  3. Otimizar queries de lotes públicos - 2 SP
  4. Implementar cache para lotes em destaque - 1 SP
  5. Adicionar breadcrumbs na navegação - 1 SP
  6. Melhorar SEO das páginas públicas - 2 SP
- **Story Points:** 8 SP

**Critérios de Aceite:**
- ✅ Lazy loading funcionando nas imagens
- ✅ Estados de loading visuais
- ✅ Performance otimizada (< 2s carregamento inicial)
- ✅ Cache implementado para dados frequentes
- ✅ Navegação clara com breadcrumbs
- ✅ Meta tags e SEO básico implementado

---

## 🔧 Especificações Técnicas Detalhadas

### Backend - Novos Endpoints

#### 1. Catálogo Público de Lotes
```java
GET /api/lotes/catalogo-publico?categoria=&status=&ordenacao=&page=0&size=20
Response: {
  "success": true,
  "data": {
    "content": [
      {
        "id": "lote-123",
        "title": "Lote de Eletrônicos Vintage",
        "description": "Coleção de eletrônicos raros dos anos 80",
        "loteEndDateTime": "2024-02-15T18:00:00",
        "status": "ACTIVE",
        "totalProdutos": 15,
        "precoInicialTotal": 5000.00,
        "precoAtualTotal": 7500.00,
        "tempoRestante": 86400,
        "categoria": "Eletrônicos",
        "imagemDestaque": "https://example.com/lote-123-thumb.jpg",
        "vendedorNome": "João Silva"
      }
    ],
    "totalElements": 50,
    "totalPages": 3,
    "number": 0,
    "size": 20
  }
}
```

#### 2. Detalhes do Lote com Produtos
```java
GET /api/lotes/{id}/produtos-publico
Response: {
  "success": true,
  "data": {
    "lote": {
      "id": "lote-123",
      "title": "Lote de Eletrônicos Vintage",
      "description": "Descrição completa do lote...",
      "loteEndDateTime": "2024-02-15T18:00:00",
      "status": "ACTIVE",
      "tempoRestante": 86400,
      "regras": "Regras específicas do lote...",
      "vendedorNome": "João Silva"
    },
    "produtos": [
      {
        "id": "produto-456",
        "title": "Walkman Sony Vintage",
        "description": "Walkman em perfeito estado...",
        "images": ["url1.jpg", "url2.jpg"],
        "initialPrice": 200.00,
        "currentPrice": 350.00,
        "status": "ACTIVE"
      }
    ]
  }
}
```

#### 3. Sistema de Favoritos
```java
POST /api/lotes/{id}/favoritar
DELETE /api/lotes/{id}/desfavoritar
GET /api/lotes/meus-favoritos
```

#### 4. Lotes em Destaque
```java
GET /api/lotes/destaque?limit=6
GET /api/lotes/meus-interesses?limit=6
```

### Frontend - Componentes

#### 1. CatalogoComponent (Refatorado)
```typescript
@Component({
  selector: 'app-catalogo',
  templateUrl: './catalogo.component.html',
  styleUrls: ['./catalogo.component.scss']
})
export class CatalogoComponent implements OnInit {
  lotes$ = new BehaviorSubject<Lote[]>([]);
  filtros = new FormGroup({
    categoria: new FormControl(''),
    status: new FormControl(''),
    ordenacao: new FormControl('proximoEncerramento')
  });
  
  // Paginação e estados
  currentPage = 0;
  totalPages = 0;
  loading = false;
}
```

#### 2. LoteDetalhesComponent (Novo)
```typescript
@Component({
  selector: 'app-lote-detalhes',
  templateUrl: './lote-detalhes.component.html',
  styleUrls: ['./lote-detalhes.component.scss']
})
export class LoteDetalhesComponent implements OnInit {
  lote$ = new BehaviorSubject<LoteDetalhes | null>(null);
  produtos$ = new BehaviorSubject<Produto[]>([]);
  isFavorito$ = new BehaviorSubject<boolean>(false);
  
  // Navegação entre produtos
  produtoAtualIndex = 0;
  
  favoritar() { /* implementação */ }
  proximoProduto() { /* implementação */ }
  produtoAnterior() { /* implementação */ }
}
```

#### 3. LoteCardComponent (Novo)
```typescript
@Component({
  selector: 'app-lote-card',
  templateUrl: './lote-card.component.html',
  styleUrls: ['./lote-card.component.scss']
})
export class LoteCardComponent {
  @Input() lote!: Lote;
  @Input() showFavoriteButton = false;
  @Input() isFavorito = false;
  
  @Output() favoritar = new EventEmitter<string>();
  @Output() desfavoritar = new EventEmitter<string>();
}
```

---

## 🎯 Critérios de Aceite da Sprint

### Funcionais
1. ✅ Menu principal sem opção "Leilões"
2. ✅ Catálogo exibe lotes ao invés de produtos
3. ✅ Página de detalhes do lote funcional
4. ✅ Sistema de favoritos operacional
5. ✅ Home inteligente baseada no status do usuário
6. ✅ Navegação intuitiva entre lotes e produtos

### Técnicos
1. ✅ Endpoints de lotes públicos implementados
2. ✅ Performance < 2 segundos no carregamento
3. ✅ Cache implementado para dados frequentes
4. ✅ Lazy loading nas imagens
5. ✅ Responsividade completa
6. ✅ SEO básico implementado

### UX/UI
1. ✅ Interface intuitiva e moderna
2. ✅ Estados de loading visuais
3. ✅ Feedback claro nas ações do usuário
4. ✅ Breadcrumbs para navegação
5. ✅ Call-to-actions efetivos
6. ✅ Experiência consistente em todos os dispositivos

### Segurança
1. ✅ Endpoints públicos sem exposição de dados sensíveis
2. ✅ Sistema de favoritos protegido por autenticação
3. ✅ Validação adequada em todos os inputs
4. ✅ Rate limiting nos endpoints públicos

---

## 📊 Estrutura de Dados

### DTOs Necessários

```java
// LoteCatalogoDto
public class LoteCatalogoDto {
    private String id;
    private String title;
    private String description;
    private LocalDateTime loteEndDateTime;
    private LoteStatus status;
    private Integer totalProdutos;
    private BigDecimal precoInicialTotal;
    private BigDecimal precoAtualTotal;
    private Long tempoRestante; // em segundos
    private String categoria;
    private String imagemDestaque;
    private String vendedorNome;
    private Boolean isFavorito; // apenas para usuários logados
}

// LoteDetalhesPublicoDto
public class LoteDetalhesPublicoDto {
    private LotePublicoDto lote;
    private List<ProdutoResumoDto> produtos;
}

// LotePublicoDto
public class LotePublicoDto {
    private String id;
    private String title;
    private String description;
    private LocalDateTime loteEndDateTime;
    private LoteStatus status;
    private Long tempoRestante;
    private String regras;
    private String vendedorNome;
    private String vendedorEmpresa;
}

// LoteFavoritoDto
public class LoteFavoritoDto {
    private String id;
    private String usuarioId;
    private String loteId;
    private LocalDateTime favoritadoEm;
}

// LoteDestaqueDto
public class LoteDestaqueDto {
    private String id;
    private String title;
    private String description;
    private LocalDateTime loteEndDateTime;
    private Long tempoRestante;
    private Integer totalProdutos;
    private BigDecimal precoAtualTotal;
    private String imagemDestaque;
    private String categoria;
    private Boolean isFavorito; // null para não logados
}
```

---

## 🚀 Plano de Implementação

### Dia 1-2: Backend e Estrutura Base (14 SP)
- **História 1:** Reestruturação da Navegação (4 SP)
- **História 2:** Catálogo de Lotes - Backend (5 SP)
- **História 4:** Sistema de Favoritos - Backend (5 SP)
- Implementar endpoints públicos de lotes
- Criar sistema de favoritos no backend

### Dia 3-4: Frontend Principal (20 SP)
- **História 2:** Catálogo de Lotes - Frontend (5 SP)
- **História 3:** Página de Detalhes do Lote (10 SP)
- **História 4:** Sistema de Favoritos - Frontend (5 SP)
- Refatorar componentes principais
- Implementar navegação por lotes

### Dia 5-7: Home Inteligente e Otimizações (20 SP)
- **História 5:** Página Home Inteligente (12 SP)
- **História 6:** Otimizações e Melhorias de UX (8 SP)
- Implementar lógica inteligente da home
- Otimizar performance e UX

---

## 🔧 Regras de Negócio Específicas

### Catálogo de Lotes
1. **Visibilidade**: Apenas lotes com status ACTIVE são exibidos publicamente
2. **Ordenação Padrão**: Lotes mais próximos do encerramento primeiro
3. **Filtros**: Por categoria, status (para admins), tempo restante
4. **Paginação**: 20 lotes por página por padrão

### Detalhes do Lote
1. **Acesso Público**: Qualquer usuário pode visualizar lotes ativos
2. **Produtos**: Exibe todos os produtos do lote com informações básicas
3. **Tempo Real**: Tempo restante atualizado dinamicamente
4. **Navegação**: Fácil navegação entre produtos do mesmo lote

### Sistema de Favoritos
1. **Autenticação**: Apenas usuários logados podem favoritar
2. **Limite**: Sem limite de lotes favoritos por usuário
3. **Persistência**: Favoritos mantidos entre sessões
4. **Notificações**: Base para futuras notificações de lotes favoritos

### Home Inteligente
1. **Usuário Não Logado**: 6 lotes próximos ao encerramento
2. **Usuário Logado**: 3 lotes favoritos + 3 próximos ao encerramento
3. **Fallback**: Se não há favoritos, exibe 6 próximos ao encerramento
4. **Atualização**: Cache de 5 minutos para lotes em destaque

### Performance
1. **Lazy Loading**: Imagens carregadas sob demanda
2. **Cache**: Lotes em destaque com TTL de 5 minutos
3. **Otimização**: Queries com índices apropriados
4. **Compressão**: Imagens otimizadas para web

---

## 📝 Anotações e Pontos Pendentes

### Dependências Técnicas
- **Lazy Loading**: Implementar intersection observer para imagens
- **Cache**: Configurar cache específico para lotes públicos
- **SEO**: Adicionar meta tags dinâmicas para cada lote
- **Analytics**: Preparar estrutura para tracking de visualizações

### Melhorias Futuras
- **Notificações Push**: Para lotes favoritos próximos ao encerramento
- **Compartilhamento Social**: Botões de compartilhamento de lotes
- **Histórico**: Lotes visualizados recentemente
- **Recomendações**: Sistema de recomendação baseado em interesses

### Considerações de UX
- **Breadcrumbs**: Navegação clara (Home > Catálogo > Lote > Produto)
- **Estados Vazios**: Mensagens apropriadas quando não há lotes
- **Loading States**: Skeletons para melhor percepção de performance
- **Acessibilidade**: ARIA labels e navegação por teclado

### Integrações Futuras
- **Sistema de Lances**: Preparar estrutura para futura implementação
- **Chat**: Base para chat entre interessados e vendedores
- **Avaliações**: Sistema de avaliação de lotes/vendedores
- **Pagamentos**: Integração com gateway de pagamento

---

**Story Points Totais Sprint S2.3:** 54 SP  
**Estimativa:** 1 semana com 2 desenvolvedores  
**Dependências:** Sprints S2.1 e S2.2 devem estar completas  
**Risco:** Médio (mudanças significativas na UX pública)