# Sprint S2.3 — Reestruturação das Telas Públicas para Sistema de Lotes (REVISADO)

**Sprint:** S2.3 (Complemento das Sprints 2.01 e 2.02)  
**Duração:** 1 semana  
**Equipe:** Dev Pleno + Dev Sênior  
**Prioridade:** Alta (Experiência do usuário público)

## 🎯 Objetivo da Sprint
Reestruturar as telas públicas do sistema para focar na experiência baseada em lotes, implementando sistema de favoritos para lotes e produtos, e criando uma jornada de usuário centrada na navegação por lotes ativos com seus produtos válidos.

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

### História 1: Reestruturação da Navegação e Limpeza de Elementos Mockados
- **Tipo:** Frontend
- **Descrição:** Remover a opção "Leilões" do menu principal e manter elementos mockados com identificação visual clara.
- **Tasks / Sub-tasks:**
  1. Remover link "Leilões" do menu principal - 1 SP
  2. Identificar visualmente elementos mockados na home - 1 SP
  3. Atualizar rotas removendo /auctions - 1 SP
  4. Revisar e limpar componentes não utilizados - 1 SP
- **Story Points:** 4 SP

**Critérios de Aceite:**
- ✅ Menu principal não possui mais a opção "Leilões"
- ✅ Elementos mockados claramente identificados com badges "MOCK" ou similar
- ✅ Rotas limpas e organizadas
- ✅ Componentes não utilizados removidos

### História 2: Transformação do Catálogo em Catálogo de Lotes
- **Tipo:** Frontend + Backend
- **Descrição:** Transformar o catálogo atual de produtos em um catálogo de lotes, onde apenas lotes com produtos válidos são exibidos.
- **Tasks / Sub-tasks:**
  1. Criar endpoint GET /lotes/catalogo-publico com regras de negócio - 3 SP
  2. Atualizar LoteService para catálogo público - 1 SP
  3. Refatorar CatalogoComponent para exibir lotes - 3 SP
  4. Criar componente LoteCardComponent com contagem de produtos - 2 SP
  5. Implementar paginação configurável (10, 20, 50 por página) - 2 SP
- **Story Points:** 11 SP

**Critérios de Aceite:**
- ✅ Catálogo exibe apenas lotes ativos com produtos válidos
- ✅ Cada lote mostra: título, descrição, tempo restante, quantidade de produtos válidos, imagem do primeiro produto
- ✅ Paginação configurável (10, 20, 50 lotes por página, padrão 10)
- ✅ Filtros funcionais por categoria, ordenação por proximidade de encerramento
- ✅ Performance adequada na listagem de lotes

### História 3: Página de Detalhes do Lote com Produtos Válidos
- **Tipo:** Frontend + Backend
- **Descrição:** Criar página de detalhes do lote onde o usuário pode navegar apenas pelos produtos válidos (publicados) que compõem o lote.
- **Tasks / Sub-tasks:**
  1. Criar endpoint GET /lotes/{id}/produtos-publico (apenas produtos válidos) - 2 SP
  2. Criar LoteDetalhesComponent com paginação de produtos - 4 SP
  3. Implementar navegação paginada entre produtos do lote (10, 20, 50 por página) - 2 SP
  4. Adicionar informações completas do lote - 2 SP
  5. Implementar rota /lotes/{id} pública - 1 SP
- **Story Points:** 11 SP

**Critérios de Aceite:**
- ✅ Página de detalhes do lote acessível publicamente
- ✅ Lista apenas produtos válidos/publicados do lote
- ✅ Paginação configurável para produtos (10, 20, 50 por página, padrão 20)
- ✅ Informações do lote (tempo restante, descrição, regras)
- ✅ Responsividade completa

### História 4: Sistema de Favoritos Integrado (Lotes e Produtos)
- **Tipo:** Frontend + Backend
- **Descrição:** Implementar sistema integrado onde usuários podem favoritar lotes e produtos, com regras de sincronização automática.
- **Tasks / Sub-tasks:**
  1. Criar entidades LoteFavorito e ProdutoFavorito - 3 SP
  2. Implementar regras de sincronização automática - 3 SP
  3. Criar endpoints de favoritos para lotes e produtos - 4 SP
  4. Implementar interface de favoritos nos componentes - 3 SP
  5. Criar página "Meus Favoritos" com abas (Lotes/Produtos) - 3 SP
- **Story Points:** 16 SP

**Critérios de Aceite:**
- ✅ Usuários logados podem favoritar lotes e produtos independentemente
- ✅ Favoritar produto automaticamente favorita o lote (se não favoritado)
- ✅ Desfavoritar lote automaticamente desfavorita todos os produtos do lote
- ✅ Página "Meus Favoritos" com abas separadas
- ✅ Filtros por favoritos funcionais

### História 5: Página Home Inteligente com Lotes em Destaque
- **Tipo:** Frontend + Backend
- **Descrição:** Reestruturar a página home para exibir lotes em destaque baseados no status do usuário, com critério de 1 semana para encerramento.
- **Tasks / Sub-tasks:**
  1. Criar endpoint GET /lotes/destaque (encerramento em 1 semana) - 2 SP
  2. Criar endpoint GET /lotes/meus-interesses para usuários logados - 2 SP
  3. Refatorar HomeComponent com lógica inteligente - 3 SP
  4. Criar seção "Lotes Encerrando em Breve" - 2 SP
  5. Implementar seção "Seus Lotes de Interesse" - 2 SP
  6. Manter elementos mockados com identificação visual - 1 SP
- **Story Points:** 12 SP

**Critérios de Aceite:**
- ✅ Usuários não logados veem lotes encerrando em 1 semana
- ✅ Usuários logados veem lotes favoritos (mais próximos primeiro) + lotes encerrando
- ✅ Elementos mockados claramente identificados
- ✅ Performance < 2 segundos no carregamento

---

## 🔧 Especificações Técnicas Detalhadas

### Backend - Regras de Negócio Implementadas

#### 1. Visibilidade de Produtos
```java
// Apenas produtos válidos são exibidos publicamente
@Query("SELECT p FROM Produto p WHERE p.loteId = :loteId AND p.status IN ('ACTIVE', 'PUBLISHED') ORDER BY p.createdAt")
List<Produto> findProdutosValidosDoLote(@Param("loteId") String loteId);
```

#### 2. Sistema de Favoritos Integrado
```java
// Regra: Favoritar produto automaticamente favorita o lote
public void favoritarProduto(String usuarioId, String produtoId) {
    // 1. Favoritar o produto
    produtoFavoritoRepository.save(new ProdutoFavorito(usuarioId, produtoId));
    
    // 2. Verificar se lote já está favoritado
    Produto produto = produtoRepository.findById(produtoId);
    if (!loteFavoritoRepository.existsByUsuarioIdAndLoteId(usuarioId, produto.getLoteId())) {
        // 3. Favoritar o lote automaticamente
        loteFavoritoRepository.save(new LoteFavorito(usuarioId, produto.getLoteId()));
    }
}

// Regra: Desfavoritar lote remove todos os produtos favoritos do lote
public void desfavoritarLote(String usuarioId, String loteId) {
    // 1. Desfavoritar o lote
    loteFavoritoRepository.deleteByUsuarioIdAndLoteId(usuarioId, loteId);
    
    // 2. Desfavoritar todos os produtos do lote
    List<String> produtoIds = produtoRepository.findProdutoIdsByLoteId(loteId);
    produtoFavoritoRepository.deleteByUsuarioIdAndProdutoIdIn(usuarioId, produtoIds);
}
```

#### 3. Lotes em Destaque (1 semana)
```java
@Query("SELECT l FROM Lote l WHERE l.status = 'ACTIVE' AND l.loteEndDateTime BETWEEN :now AND :oneWeekFromNow ORDER BY l.loteEndDateTime ASC")
List<Lote> findLotesEncerrando1Semana(@Param("now") LocalDateTime now, @Param("oneWeekFromNow") LocalDateTime oneWeekFromNow);
```

### Frontend - Componentes Atualizados

#### 1. Paginação Configurável
```typescript
export interface PaginacaoConfig {
  opcoesPorPagina: number[];
  padraoLotes: number;
  padraoProdutos: number;
}

// Configuração padrão
const PAGINACAO_CONFIG: PaginacaoConfig = {
  opcoesPorPagina: [10, 20, 50],
  padraoLotes: 10,
  padraoProdutos: 20
};
```

#### 2. Sistema de Favoritos Integrado
```typescript
export class FavoritosService {
  // Favoritar produto (automaticamente favorita lote)
  favoritarProduto(produtoId: string): Observable<void> {
    return this.http.post<void>(`/api/produtos/${produtoId}/favoritar`, {});
  }
  
  // Desfavoritar lote (automaticamente desfavorita produtos)
  desfavoritarLote(loteId: string): Observable<void> {
    return this.http.delete<void>(`/api/lotes/${loteId}/desfavoritar`);
  }
}
```

---

## 🎯 Critérios de Aceite da Sprint

### Funcionais
1. ✅ Menu principal sem opção "Leilões"
2. ✅ Catálogo exibe apenas lotes ativos com produtos válidos
3. ✅ Página de detalhes mostra apenas produtos válidos do lote
4. ✅ Sistema de favoritos integrado (lotes ↔ produtos)
5. ✅ Home inteligente com lotes encerrando em 1 semana
6. ✅ Paginação configurável (10, 20, 50 itens)

### Técnicos
1. ✅ Regras de negócio de favoritos implementadas
2. ✅ Apenas produtos válidos/publicados são exibidos
3. ✅ Performance < 2 segundos no carregamento
4. ✅ Paginação eficiente no backend
5. ✅ Endpoints testados e documentados

### UX/UI
1. ✅ Elementos mockados claramente identificados
2. ✅ Interface intuitiva para favoritos
3. ✅ Paginação configurável pelo usuário
4. ✅ Estados de loading visuais
5. ✅ Responsividade completa

---

## 📊 Estrutura de Dados Atualizada

### Entidades Necessárias

```java
// LoteFavorito
@Entity
@Table(name = "tb_lote_favorito")
public class LoteFavorito {
    @Id
    private String id;
    
    @Column(name = "usuario_id", nullable = false)
    private String usuarioId;
    
    @Column(name = "lote_id", nullable = false)
    private String loteId;
    
    @Column(name = "favoritado_em", nullable = false)
    private LocalDateTime favoritadoEm;
    
    // Constraint única
    @Table(uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "lote_id"}))
}

// ProdutoFavorito
@Entity
@Table(name = "tb_produto_favorito")
public class ProdutoFavorito {
    @Id
    private String id;
    
    @Column(name = "usuario_id", nullable = false)
    private String usuarioId;
    
    @Column(name = "produto_id", nullable = false)
    private String produtoId;
    
    @Column(name = "favoritado_em", nullable = false)
    private LocalDateTime favoritadoEm;
    
    // Constraint única
    @Table(uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "produto_id"}))
}
```

---

## 🚀 Plano de Implementação

### Dia 1-2: Backend e Estrutura Base (15 SP)
- **História 1:** Reestruturação da Navegação (4 SP)
- **História 2:** Catálogo de Lotes - Backend (6 SP)
- **História 4:** Sistema de Favoritos - Backend (5 SP)

### Dia 3-4: Frontend Principal (23 SP)
- **História 2:** Catálogo de Lotes - Frontend (5 SP)
- **História 3:** Página de Detalhes do Lote (11 SP)
- **História 4:** Sistema de Favoritos - Frontend (7 SP)

### Dia 5-7: Home e Finalizações (16 SP)
- **História 4:** Página de Favoritos (4 SP)
- **História 5:** Página Home Inteligente (12 SP)

---

## 📝 Anotações e Regras de Negócio

### Regras Implementadas
1. **Produtos sem lote:** Não são exibidos publicamente
2. **Produtos válidos:** Apenas ACTIVE e PUBLISHED são exibidos
3. **Favoritos sincronizados:** Produto → Lote (automático), Lote → Produtos (remove todos)
4. **Lotes em destaque:** Encerramento em 1 semana
5. **Paginação:** 10 lotes (padrão), 20 produtos (padrão), configurável

### Elementos Mockados Mantidos
- Estatísticas da home (com badge "MOCK")
- Seção de features (mantida, não é mock)
- Dados de exemplo onde necessário (claramente identificados)

---

**Story Points Totais Sprint S2.3:** 54 SP  
**Estimativa:** 1 semana com 2 desenvolvedores  
**Dependências:** Sprints S2.1 e S2.2 devem estar completas  
**Risco:** Médio (sistema de favoritos integrado adiciona complexidade)