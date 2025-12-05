# Backlog Sprint S2.3 - Reestruturação das Telas Públicas

## 📊 Resumo do Backlog

**Sprint:** S2.3  
**Objetivo:** Reestruturar telas públicas para sistema baseado em lotes  
**Total de Story Points:** 54 SP  
**Duração Estimada:** 1 semana (2 desenvolvedores)  
**Prioridade:** Alta

---

## 🎯 Épico: Experiência Pública Baseada em Lotes

### Objetivo do Épico
Transformar a experiência pública do sistema de um catálogo de produtos para um catálogo de lotes, onde usuários navegam por lotes e depois acessam os produtos dentro de cada lote, criando uma experiência mais próxima de um leilão real.

### Valor de Negócio
- **Experiência mais realista:** Simula melhor um ambiente de leilão real
- **Engajamento maior:** Usuários exploram mais produtos dentro de cada lote
- **Preparação para lances:** Estrutura adequada para futuro sistema de lances
- **Diferenciação:** Experiência única comparada a e-commerces tradicionais

---

## 📋 Histórias do Backlog

### **História 1: Limpeza e Reestruturação da Navegação**
**ID:** S2.3-H01  
**Tipo:** Frontend  
**Prioridade:** Alta  
**Story Points:** 4 SP  

**Como** visitante do sistema  
**Eu quero** uma navegação limpa e focada em lotes  
**Para que** eu possa encontrar facilmente os leilões disponíveis  

#### Critérios de Aceite
- [ ] Menu principal não possui mais a opção "Leilões"
- [ ] Página home não exibe mais dados mockados/falsos
- [ ] Rotas antigas (/auctions) são removidas ou redirecionadas
- [ ] Componentes não utilizados são removidos do código
- [ ] Navegação é intuitiva e focada em lotes

#### Tasks Técnicas
1. **Remover link "Leilões" do menu** (1 SP)
   - Atualizar `main-layout.component.html`
   - Remover referências no CSS
   - Testar responsividade do menu

2. **Limpar dados mockados da home** (1 SP)
   - Remover estatísticas falsas
   - Limpar seções com dados simulados
   - Manter apenas features reais do sistema

3. **Atualizar sistema de rotas** (1 SP)
   - Remover rota `/auctions`
   - Implementar redirects se necessário
   - Atualizar `app.routes.ts`

4. **Limpeza de componentes** (1 SP)
   - Identificar componentes não utilizados
   - Remover imports desnecessários
   - Limpar arquivos órfãos

#### Definição de Pronto
- Menu atualizado e testado
- Home sem dados mockados
- Rotas limpas e funcionais
- Código limpo sem componentes órfãos
- Testes passando

---

### **História 2: Catálogo de Lotes Público**
**ID:** S2.3-H02  
**Tipo:** Frontend + Backend  
**Prioridade:** Alta  
**Story Points:** 10 SP  

**Como** visitante do sistema  
**Eu quero** navegar por um catálogo de lotes  
**Para que** eu possa ver os leilões disponíveis e escolher quais explorar  

#### Critérios de Aceite
- [ ] Catálogo exibe lotes ao invés de produtos individuais
- [ ] Cada lote mostra: título, descrição resumida, tempo restante, número de produtos, preço total atual
- [ ] Filtros funcionais por categoria, status, ordenação
- [ ] Paginação eficiente (20 lotes por página)
- [ ] Performance adequada (< 2s carregamento)
- [ ] Responsividade completa

#### Tasks Técnicas
1. **Endpoint de catálogo público** (2 SP)
   - `GET /api/lotes/catalogo-publico`
   - Filtros por categoria, status, ordenação
   - Paginação integrada
   - Apenas lotes ACTIVE visíveis

2. **Atualizar LoteService** (1 SP)
   - Método `buscarCatalogoPublico()`
   - Integração com novo endpoint
   - Cache de 2 minutos

3. **Refatorar CatalogoComponent** (3 SP)
   - Mudar de produtos para lotes
   - Atualizar template HTML
   - Ajustar lógica de filtros
   - Manter paginação existente

4. **Criar LoteCardComponent** (2 SP)
   - Card específico para lotes
   - Informações: título, tempo restante, produtos, preço
   - Botão para acessar detalhes
   - Responsivo

5. **Implementar filtros de lotes** (2 SP)
   - Filtro por categoria
   - Ordenação (próximo encerramento, mais produtos, etc.)
   - Busca por título
   - Integração com backend

#### Definição de Pronto
- Endpoint funcionando e testado
- Catálogo exibindo lotes corretamente
- Filtros funcionais
- Performance dentro do SLA
- Responsividade testada
- Testes unitários implementados

---

### **História 3: Página de Detalhes do Lote**
**ID:** S2.3-H03  
**Tipo:** Frontend + Backend  
**Prioridade:** Alta  
**Story Points:** 10 SP  

**Como** visitante interessado em um lote  
**Eu quero** ver todos os produtos que compõem o lote  
**Para que** eu possa avaliar se vale a pena participar do leilão  

#### Critérios de Aceite
- [ ] Página acessível publicamente via `/lotes/{id}`
- [ ] Exibe informações completas do lote (título, descrição, tempo restante, regras)
- [ ] Lista todos os produtos do lote com imagens e informações básicas
- [ ] Navegação fácil entre produtos do lote
- [ ] Breadcrumbs para navegação (Home > Catálogo > Lote)
- [ ] Responsividade completa
- [ ] Estados de loading apropriados

#### Tasks Técnicas
1. **Endpoint de detalhes públicos** (2 SP)
   - `GET /api/lotes/{id}/produtos-publico`
   - Retorna lote + produtos
   - Apenas dados públicos
   - Validação de lote ativo

2. **Criar LoteDetalhesComponent** (3 SP)
   - Componente standalone
   - Template com informações do lote
   - Lista de produtos
   - Estados de loading/erro

3. **Navegação entre produtos** (2 SP)
   - Carousel ou grid de produtos
   - Navegação anterior/próximo
   - Modal ou seção expandida para detalhes
   - Otimização para mobile

4. **Informações do lote** (2 SP)
   - Seção com detalhes completos
   - Contador de tempo restante
   - Regras do lote
   - Informações do vendedor (públicas)

5. **Rota pública** (1 SP)
   - Adicionar rota `/lotes/{id}`
   - Sem necessidade de autenticação
   - Breadcrumbs funcionais
   - Meta tags para SEO

#### Definição de Pronto
- Endpoint implementado e testado
- Página de detalhes funcional
- Navegação entre produtos operacional
- Breadcrumbs funcionando
- Responsividade testada
- SEO básico implementado

---

### **História 4: Sistema de Favoritos de Lotes**
**ID:** S2.3-H04  
**Tipo:** Frontend + Backend  
**Prioridade:** Média  
**Story Points:** 10 SP  

**Como** usuário logado  
**Eu quero** favoritar lotes de meu interesse  
**Para que** eu possa acompanhar facilmente os leilões que me interessam  

#### Critérios de Aceite
- [ ] Usuários logados podem favoritar/desfavoritar lotes
- [ ] Botão de favorito visível nos cards de lote
- [ ] Indicador visual para lotes já favoritados
- [ ] Página "Meus Lotes Favoritos" acessível
- [ ] Favoritos persistem entre sessões
- [ ] Performance adequada nas operações

#### Tasks Técnicas
1. **Entidade LoteFavorito** (2 SP)
   - Criar entidade JPA
   - Migration para tabela
   - Relacionamentos com Usuario e Lote
   - Constraint de unicidade

2. **Endpoints de favoritos** (3 SP)
   - `POST /api/lotes/{id}/favoritar`
   - `DELETE /api/lotes/{id}/desfavoritar`
   - `GET /api/lotes/meus-favoritos`
   - Validações de autenticação

3. **Botão de favoritar** (2 SP)
   - Adicionar ao LoteCardComponent
   - Estados: não favoritado, favoritado, loading
   - Feedback visual imediato
   - Tratamento de erros

4. **Página de favoritos** (2 SP)
   - Componente MeusFavoritosComponent
   - Lista de lotes favoritos
   - Opção de remover favoritos
   - Estados vazios

5. **Indicadores visuais** (1 SP)
   - Ícone de coração nos cards
   - Estados hover e ativo
   - Animações suaves
   - Consistência visual

#### Definição de Pronto
- Entidade e endpoints funcionando
- Botões de favoritar operacionais
- Página de favoritos implementada
- Indicadores visuais funcionais
- Testes de integração passando

---

### **História 5: Home Inteligente com Lotes em Destaque**
**ID:** S2.3-H05  
**Tipo:** Frontend + Backend  
**Prioridade:** Alta  
**Story Points:** 12 SP  

**Como** visitante ou usuário logado  
**Eu quero** ver lotes relevantes na página inicial  
**Para que** eu possa rapidamente encontrar leilões de meu interesse  

#### Critérios de Aceite
- [ ] Usuários não logados veem lotes próximos ao encerramento (48h)
- [ ] Usuários logados veem lotes favoritos + próximos ao encerramento
- [ ] Seção "Lotes em Destaque" bem definida
- [ ] Call-to-actions apropriados para cada tipo de usuário
- [ ] Performance < 2 segundos no carregamento
- [ ] Cache implementado para otimização

#### Tasks Técnicas
1. **Endpoint para não logados** (2 SP)
   - `GET /api/lotes/destaque`
   - Lotes próximos ao encerramento (48h)
   - Limite de 6 lotes
   - Cache de 5 minutos

2. **Endpoint para logados** (2 SP)
   - `GET /api/lotes/meus-interesses`
   - 3 favoritos + 3 próximos ao encerramento
   - Fallback se não há favoritos
   - Cache personalizado por usuário

3. **Refatorar HomeComponent** (3 SP)
   - Lógica inteligente baseada em autenticação
   - Seção de lotes em destaque
   - Estados de loading
   - Tratamento de erros

4. **Seção "Lotes em Destaque"** (2 SP)
   - Layout responsivo
   - Cards otimizados para destaque
   - Navegação para catálogo completo
   - Animações suaves

5. **Seção "Seus Interesses"** (2 SP)
   - Apenas para usuários logados
   - Lotes favoritos em destaque
   - Link para página de favoritos
   - Estados vazios apropriados

6. **Call-to-actions** (1 SP)
   - Botões para cadastro (não logados)
   - Links para favoritar (logados)
   - Navegação para catálogo
   - Mensagens motivacionais

#### Definição de Pronto
- Endpoints implementados e testados
- Home inteligente funcionando
- Seções bem definidas e responsivas
- Call-to-actions efetivos
- Performance dentro do SLA
- Cache funcionando corretamente

---

### **História 6: Otimizações e Melhorias de UX**
**ID:** S2.3-H06  
**Tipo:** Frontend + Backend  
**Prioridade:** Média  
**Story Points:** 8 SP  

**Como** usuário do sistema  
**Eu quero** uma experiência fluida e otimizada  
**Para que** eu possa navegar facilmente pelos lotes e produtos  

#### Critérios de Aceite
- [ ] Lazy loading implementado nas imagens
- [ ] Skeleton loading nos cards durante carregamento
- [ ] Queries otimizadas (< 200ms)
- [ ] Cache implementado para dados frequentes
- [ ] Breadcrumbs funcionais em toda navegação
- [ ] SEO básico implementado (meta tags)

#### Tasks Técnicas
1. **Lazy loading de imagens** (1 SP)
   - Intersection Observer
   - Placeholder durante carregamento
   - Otimização para mobile
   - Fallback para navegadores antigos

2. **Skeleton loading** (1 SP)
   - Componente SkeletonCardComponent
   - Estados de loading nos cards
   - Animações suaves
   - Consistência visual

3. **Otimização de queries** (2 SP)
   - Índices para lotes públicos
   - Otimização de joins
   - Queries de contagem eficientes
   - Monitoramento de performance

4. **Cache para lotes** (1 SP)
   - Cache Redis para lotes em destaque
   - TTL apropriado por tipo de consulta
   - Invalidação inteligente
   - Métricas de hit/miss

5. **Breadcrumbs** (1 SP)
   - Componente BreadcrumbComponent
   - Navegação Home > Catálogo > Lote
   - Responsividade
   - Acessibilidade

6. **SEO básico** (2 SP)
   - Meta tags dinâmicas por página
   - Open Graph para compartilhamento
   - Structured data básico
   - Sitemap atualizado

#### Definição de Pronto
- Lazy loading funcionando
- Skeleton loading implementado
- Queries otimizadas e monitoradas
- Cache funcionando corretamente
- Breadcrumbs operacionais
- SEO básico implementado

---

## 📊 Resumo por Desenvolvedor

### **Desenvolvedor Backend (26 SP)**
- História 1: Tasks backend (1 SP)
- História 2: Endpoints e services (3 SP)
- História 3: Endpoints de detalhes (2 SP)
- História 4: Sistema de favoritos completo (5 SP)
- História 5: Endpoints inteligentes (4 SP)
- História 6: Otimizações backend (3 SP)
- **Testes e documentação:** (8 SP)

### **Desenvolvedor Frontend (28 SP)**
- História 1: Limpeza e navegação (3 SP)
- História 2: Catálogo refatorado (7 SP)
- História 3: Página de detalhes (8 SP)
- História 4: Interface de favoritos (5 SP)
- História 5: Home inteligente (8 SP)
- História 6: UX e otimizações (5 SP)
- **Testes e ajustes:** (2 SP)

---

## 🎯 Critérios de Aceite da Sprint

### **Funcionais**
- [ ] Navegação limpa sem opção "Leilões"
- [ ] Catálogo exibe lotes ao invés de produtos
- [ ] Página de detalhes do lote funcional
- [ ] Sistema de favoritos operacional
- [ ] Home inteligente baseada no status do usuário
- [ ] Performance < 2 segundos em todas as páginas

### **Técnicos**
- [ ] Endpoints públicos implementados e testados
- [ ] Cache implementado com TTL apropriado
- [ ] Lazy loading funcionando
- [ ] Queries otimizadas (< 200ms)
- [ ] Testes unitários > 80% cobertura
- [ ] SEO básico implementado

### **UX/UI**
- [ ] Interface intuitiva e moderna
- [ ] Estados de loading visuais
- [ ] Breadcrumbs funcionais
- [ ] Responsividade completa
- [ ] Estados vazios apropriados
- [ ] Call-to-actions efetivos

---

## 🚀 Dependências e Riscos

### **Dependências**
- Sprints S2.1 e S2.2 devem estar completas
- Estrutura de lotes deve estar funcional
- Sistema de autenticação operacional

### **Riscos**
- **Alto:** Mudança significativa na UX pode confundir usuários
- **Médio:** Performance com muitos lotes pode ser impactada
- **Baixo:** Integração entre favoritos e cache pode ter conflitos

### **Mitigações**
- Testes extensivos com usuários
- Monitoramento de performance
- Cache inteligente e invalidação adequada

---

**Backlog aprovado para Sprint S2.3**  
**Data:** 2024-12-19  
**Próximo passo:** Refinamento técnico e início da implementação