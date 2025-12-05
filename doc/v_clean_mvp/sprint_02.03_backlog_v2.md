# Backlog Sprint S2.3 - Reestruturação das Telas Públicas (REVISADO)

## 📊 Resumo do Backlog

**Sprint:** S2.3 (Revisado com esclarecimentos)  
**Objetivo:** Reestruturar telas públicas para sistema baseado em lotes com favoritos integrados  
**Total de Story Points:** 54 SP  
**Duração Estimada:** 1 semana (2 desenvolvedores)  
**Prioridade:** Alta

---

## 🎯 Épico: Experiência Pública Baseada em Lotes com Favoritos Integrados

### Objetivo do Épico
Transformar a experiência pública do sistema de um catálogo de produtos para um catálogo de lotes, implementando sistema integrado de favoritos (lotes ↔ produtos) e criando uma experiência mais próxima de um leilão real.

### Valor de Negócio
- **Experiência realista:** Simula ambiente de leilão real com lotes
- **Engajamento maior:** Sistema de favoritos integrado aumenta retenção
- **Controle de qualidade:** Apenas produtos válidos são exibidos publicamente
- **Preparação futura:** Base sólida para sistema de lances

---

## 📋 Histórias do Backlog Revisado

### **História 1: Limpeza e Reestruturação da Navegação**
**ID:** S2.3-H01  
**Tipo:** Frontend  
**Prioridade:** Alta  
**Story Points:** 4 SP  

**Como** visitante do sistema  
**Eu quero** uma navegação limpa focada em lotes  
**Para que** eu possa encontrar facilmente os leilões disponíveis sem confusão  

#### Critérios de Aceite Revisados
- [ ] Menu principal não possui mais a opção "Leilões"
- [ ] Elementos mockados são mantidos mas claramente identificados com badges "MOCK"
- [ ] Rotas antigas (/auctions) são removidas
- [ ] Componentes não utilizados são removidos do código
- [ ] Navegação é intuitiva e focada em lotes

#### Tasks Técnicas
1. **Remover link "Leilões" do menu** (1 SP)
   - Atualizar `main-layout.component.html`
   - Remover referências no CSS
   - Testar responsividade do menu

2. **Identificar elementos mockados** (1 SP)
   - Adicionar badges "MOCK" em estatísticas da home
   - Manter funcionalidades mas com identificação visual
   - Criar componente MockBadgeComponent reutilizável

3. **Atualizar sistema de rotas** (1 SP)
   - Remover rota `/auctions`
   - Atualizar `app.routes.ts`
   - Testar redirecionamentos

4. **Limpeza de componentes** (1 SP)
   - Identificar componentes não utilizados
   - Remover imports desnecessários
   - Limpar arquivos órfãos

---

### **História 2: Catálogo de Lotes com Produtos Válidos**
**ID:** S2.3-H02  
**Tipo:** Frontend + Backend  
**Prioridade:** Alta  
**Story Points:** 11 SP  

**Como** visitante do sistema  
**Eu quero** navegar por um catálogo de lotes que contêm apenas produtos válidos  
**Para que** eu possa ver apenas leilões reais e disponíveis  

#### Critérios de Aceite Revisados
- [ ] Catálogo exibe apenas lotes ativos com produtos válidos (ACTIVE/PUBLISHED)
- [ ] Cada lote mostra: título, descrição, tempo restante, quantidade de produtos válidos, imagem do primeiro produto
- [ ] Paginação configurável (10, 20, 50 lotes por página, padrão 10)
- [ ] Filtros por categoria e ordenação por proximidade de encerramento
- [ ] Performance adequada (< 2s carregamento)

#### Tasks Técnicas
1. **Endpoint de catálogo com regras de negócio** (3 SP)
   - `GET /api/lotes/catalogo-publico`
   - Apenas lotes ACTIVE com produtos válidos
   - Contagem de produtos válidos por lote
   - Filtros e paginação configurável

2. **Atualizar LoteService** (1 SP)
   - Método `buscarCatalogoPublico()`
   - Integração com novo endpoint
   - Tratamento de paginação configurável

3. **Refatorar CatalogoComponent** (3 SP)
   - Mudar de produtos para lotes
   - Implementar paginação configurável
   - Ajustar filtros para lotes
   - Estados de loading e erro

4. **Criar LoteCardComponent** (2 SP)
   - Card específico para lotes
   - Exibir quantidade de produtos válidos
   - Imagem do primeiro produto como destaque
   - Tempo restante em destaque

5. **Paginação configurável** (2 SP)
   - Seletor de itens por página (10, 20, 50)
   - Padrão 10 lotes por página
   - Persistir preferência do usuário
   - Responsividade

---

### **História 3: Página de Detalhes com Produtos Válidos**
**ID:** S2.3-H03  
**Tipo:** Frontend + Backend  
**Prioridade:** Alta  
**Story Points:** 11 SP  

**Como** visitante interessado em um lote  
**Eu quero** ver apenas os produtos válidos que compõem o lote  
**Para que** eu possa avaliar corretamente o que está sendo leiloado  

#### Critérios de Aceite Revisados
- [ ] Página acessível publicamente via `/lotes/{id}`
- [ ] Exibe apenas produtos com status ACTIVE ou PUBLISHED
- [ ] Paginação configurável para produtos (10, 20, 50 por página, padrão 20)
- [ ] Informações completas do lote (título, descrição, tempo restante, regras)
- [ ] Breadcrumbs para navegação (Home > Catálogo > Lote)
- [ ] Responsividade completa

#### Tasks Técnicas
1. **Endpoint de produtos válidos** (2 SP)
   - `GET /api/lotes/{id}/produtos-publico`
   - Filtrar apenas produtos ACTIVE/PUBLISHED
   - Paginação configurável
   - Ordenação por data de criação

2. **Criar LoteDetalhesComponent** (4 SP)
   - Componente standalone
   - Exibição de informações do lote
   - Lista paginada de produtos válidos
   - Estados de loading/erro/vazio

3. **Paginação de produtos** (2 SP)
   - Seletor de itens por página (10, 20, 50)
   - Padrão 20 produtos por página
   - Navegação eficiente
   - Contadores de produtos

4. **Informações do lote** (2 SP)
   - Seção com detalhes completos
   - Contador de tempo restante em tempo real
   - Regras do lote
   - Informações públicas do vendedor

5. **Rota pública e breadcrumbs** (1 SP)
   - Rota `/lotes/{id}` sem autenticação
   - Breadcrumbs funcionais
   - Meta tags básicas

---

### **História 4: Sistema de Favoritos Integrado (Lotes ↔ Produtos)**
**ID:** S2.3-H04  
**Tipo:** Frontend + Backend  
**Prioridade:** Alta  
**Story Points:** 16 SP  

**Como** usuário logado  
**Eu quero** favoritar lotes e produtos com sincronização automática  
**Para que** eu possa acompanhar facilmente meus interesses sem inconsistências  

#### Critérios de Aceite Revisados
- [ ] Usuários podem favoritar lotes e produtos independentemente
- [ ] Favoritar produto automaticamente favorita o lote (se não favoritado)
- [ ] Desfavoritar lote automaticamente desfavorita todos os produtos do lote
- [ ] Página "Meus Favoritos" com abas separadas (Lotes/Produtos)
- [ ] Filtros por favoritos funcionais no catálogo
- [ ] Indicadores visuais consistentes

#### Tasks Técnicas
1. **Entidades de favoritos** (3 SP)
   - Criar `LoteFavorito` e `ProdutoFavorito`
   - Migrations para tabelas
   - Relacionamentos e constraints
   - Índices para performance

2. **Regras de sincronização** (3 SP)
   - Lógica: produto → lote (automático)
   - Lógica: lote → produtos (remove todos)
   - Service com regras de negócio
   - Testes unitários das regras

3. **Endpoints de favoritos** (4 SP)
   - `POST/DELETE /api/lotes/{id}/favoritar`
   - `POST/DELETE /api/produtos/{id}/favoritar`
   - `GET /api/favoritos/lotes`
   - `GET /api/favoritos/produtos`

4. **Interface de favoritos** (3 SP)
   - Botões nos cards de lote e produto
   - Estados visuais (favoritado/não favoritado)
   - Feedback imediato
   - Tratamento de erros

5. **Página "Meus Favoritos"** (3 SP)
   - Componente com abas (Lotes/Produtos)
   - Listagem com paginação
   - Opções de remoção
   - Estados vazios apropriados

---

### **História 5: Home Inteligente com Lotes em Destaque (1 semana)**
**ID:** S2.3-H05  
**Tipo:** Frontend + Backend  
**Prioridade:** Alta  
**Story Points:** 12 SP  

**Como** visitante ou usuário logado  
**Eu quero** ver lotes relevantes que encerram em 1 semana na página inicial  
**Para que** eu possa rapidamente encontrar leilões urgentes de meu interesse  

#### Critérios de Aceite Revisados
- [ ] Usuários não logados veem lotes encerrando em 1 semana
- [ ] Usuários logados veem lotes favoritos (mais próximos primeiro) + lotes encerrando
- [ ] Elementos mockados mantidos com identificação visual clara
- [ ] Performance < 2 segundos no carregamento
- [ ] Seções bem definidas e responsivas

#### Tasks Técnicas
1. **Endpoint para não logados** (2 SP)
   - `GET /api/lotes/destaque`
   - Lotes encerrando em 1 semana (7 dias)
   - Limite de 6 lotes
   - Cache de 5 minutos

2. **Endpoint para logados** (2 SP)
   - `GET /api/lotes/meus-interesses`
   - Lotes favoritos ordenados por proximidade
   - Complementar com lotes encerrando se necessário
   - Cache personalizado por usuário

3. **Refatorar HomeComponent** (3 SP)
   - Lógica baseada em status de autenticação
   - Seção "Lotes Encerrando em Breve"
   - Seção "Seus Lotes de Interesse" (logados)
   - Estados de loading e erro

4. **Seções de destaque** (2 SP)
   - Layout responsivo para lotes
   - Cards otimizados para home
   - Call-to-actions apropriados
   - Navegação para catálogo completo

5. **Elementos mockados identificados** (1 SP)
   - Manter estatísticas com badge "MOCK"
   - Seção de features (não é mock)
   - Identificação visual clara
   - Preparação para remoção futura

6. **Call-to-actions inteligentes** (2 SP)
   - Botões para cadastro (não logados)
   - Links para favoritos (logados)
   - Mensagens contextuais
   - Navegação otimizada

---

## 📊 Resumo por Desenvolvedor

### **Desenvolvedor Backend (27 SP)**
- História 1: Limpeza de rotas (1 SP)
- História 2: Endpoint de catálogo (3 SP)
- História 3: Endpoint de produtos válidos (2 SP)
- História 4: Sistema de favoritos completo (10 SP)
- História 5: Endpoints de destaque (4 SP)
- **Testes e documentação:** (7 SP)

### **Desenvolvedor Frontend (27 SP)**
- História 1: Interface e navegação (3 SP)
- História 2: Catálogo refatorado (8 SP)
- História 3: Página de detalhes (9 SP)
- História 4: Interface de favoritos (6 SP)
- História 5: Home inteligente (8 SP)
- **Testes e ajustes:** (3 SP)

---

## 🔧 Regras de Negócio Implementadas

### **Visibilidade de Produtos**
1. **Produtos sem lote:** Não são exibidos publicamente (apenas área do vendedor)
2. **Produtos válidos:** Apenas ACTIVE e PUBLISHED são exibidos
3. **Contagem:** Lotes mostram quantidade de produtos válidos
4. **Imagem destaque:** Primeira imagem do primeiro produto válido do lote

### **Sistema de Favoritos Integrado**
1. **Favoritar produto:** Automaticamente favorita o lote (se não favoritado)
2. **Desfavoritar lote:** Remove todos os produtos favoritos do lote
3. **Independência:** Usuário pode favoritar lote sem favoritar produtos específicos
4. **Filtros:** Favoritos não influenciam ordenação, apenas filtros estratégicos

### **Lotes em Destaque**
1. **Critério temporal:** Encerramento dentro de 1 semana (7 dias)
2. **Usuários logados:** Favoritos com encerramento mais próximo primeiro
3. **Fallback:** Se poucos favoritos, complementa com lotes encerrando
4. **Cache:** 5 minutos para não logados, personalizado para logados

### **Paginação Configurável**
1. **Lotes:** 10, 20, 50 por página (padrão 10)
2. **Produtos:** 10, 20, 50 por página (padrão 20)
3. **Persistência:** Preferências salvas por usuário
4. **Performance:** Paginação eficiente no backend

---

## 🎯 Critérios de Aceite da Sprint

### **Funcionais**
- [ ] Navegação limpa sem "Leilões", elementos mockados identificados
- [ ] Catálogo exibe apenas lotes com produtos válidos
- [ ] Página de detalhes mostra apenas produtos válidos do lote
- [ ] Sistema de favoritos integrado (lotes ↔ produtos) funcionando
- [ ] Home inteligente com lotes encerrando em 1 semana
- [ ] Paginação configurável (10, 20, 50) funcionando

### **Técnicos**
- [ ] Regras de negócio de favoritos implementadas corretamente
- [ ] Apenas produtos ACTIVE/PUBLISHED são exibidos publicamente
- [ ] Performance < 2 segundos em todas as páginas
- [ ] Paginação eficiente no backend
- [ ] Endpoints testados e documentados
- [ ] Cache implementado para lotes em destaque

### **UX/UI**
- [ ] Elementos mockados claramente identificados
- [ ] Interface intuitiva para favoritos integrados
- [ ] Paginação configurável pelo usuário
- [ ] Estados de loading, erro e vazio apropriados
- [ ] Responsividade completa
- [ ] Breadcrumbs funcionais

---

## 🚀 Dependências e Riscos

### **Dependências**
- Sprints S2.1 e S2.2 devem estar completas
- Estrutura de lotes e produtos deve estar funcional
- Sistema de autenticação operacional

### **Riscos Identificados**
- **Alto:** Sistema de favoritos integrado adiciona complexidade nas regras de sincronização
- **Médio:** Performance com contagem de produtos válidos por lote
- **Baixo:** Mudança na UX pode necessitar ajustes após feedback

### **Mitigações**
- Testes extensivos das regras de favoritos
- Cache agressivo para contagens
- Monitoramento de performance
- Documentação clara das regras de negócio

---

## 📋 Checklist de Validação

### **Antes de Iniciar**
- [ ] Confirmar regras de favoritos integrados
- [ ] Validar critério de 1 semana para destaque
- [ ] Definir estratégia de cache
- [ ] Confirmar estrutura de paginação configurável

### **Durante Desenvolvimento**
- [ ] Testar regras de sincronização de favoritos
- [ ] Validar performance com produtos válidos
- [ ] Testar paginação configurável
- [ ] Verificar identificação visual de elementos mockados

### **Antes do Deploy**
- [ ] Testar integração completa de favoritos
- [ ] Validar performance em produção
- [ ] Verificar responsividade
- [ ] Testar com dados reais

---

**Backlog revisado e aprovado para Sprint S2.3**  
**Data:** 2024-12-19  
**Revisão baseada em:** Esclarecimentos do stakeholder  
**Próximo passo:** Início da implementação