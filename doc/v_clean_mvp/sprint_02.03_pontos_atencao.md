# Sprint S2.3 - Pontos de Atenção e Discussões

## 🚨 Pontos Críticos para Discussão

### 1. **Impacto na Experiência do Usuário**
**Questão:** A mudança de catálogo de produtos para catálogo de lotes representa uma mudança significativa na jornada do usuário.

**Pontos para discussão:**
- ✅ **Confirmado:** Usuários agora navegam Lotes → Produtos (ao invés de diretamente produtos)
- ❓ **Dúvida:** Como tratar produtos que não estão em lotes? Devem ser invisíveis no catálogo público?
- ❓ **Dúvida:** Produtos avulsos (sem lote) devem ter uma seção separada ou ficam apenas na área do vendedor?

**Sugestão:** Criar regra de negócio que produtos só ficam visíveis publicamente se estiverem em um lote ativo.

---

### 2. **Sistema de Favoritos - Escopo e Funcionalidades**
**Questão:** O sistema de favoritos foi definido de forma básica, mas pode ter implicações maiores.

**Pontos para discussão:**
- ✅ **Confirmado:** Usuários logados podem favoritar lotes
- ❓ **Dúvida:** Devemos implementar notificações quando lotes favoritos estão próximos do fim?
- ❓ **Dúvida:** Limite máximo de lotes favoritos por usuário?
- ❓ **Dúvida:** Favoritos devem influenciar na ordenação/recomendação de outros lotes?

**Sugestão:** Implementar apenas o básico nesta sprint (favoritar/desfavoritar) e deixar notificações para sprint futura.

---

### 3. **Definição de "Lotes em Destaque"**
**Questão:** A lógica para definir quais lotes aparecem em destaque precisa ser bem definida.

**Pontos para discussão:**
- ✅ **Confirmado:** Usuários não logados veem lotes próximos ao encerramento
- ✅ **Confirmado:** Usuários logados veem favoritos + próximos ao encerramento
- ❓ **Dúvida:** Critério de "próximo ao encerramento" - quantas horas/dias?
- ❓ **Dúvida:** Se usuário tem muitos favoritos, como priorizar quais mostrar?
- ❓ **Dúvida:** Incluir critérios como "mais visualizados" ou "mais lances"?

**Sugestão:** 
- Próximo ao encerramento = 48 horas
- Favoritos ordenados por proximidade do encerramento
- Máximo 3 favoritos + 3 próximos ao encerramento

---

### 4. **Impacto nas Rotas e SEO**
**Questão:** Mudança significativa na estrutura de URLs pode afetar SEO e bookmarks existentes.

**Pontos para discussão:**
- ✅ **Confirmado:** Remover rota `/auctions`
- ✅ **Confirmado:** Manter `/catalogo` mas com conteúdo de lotes
- ❓ **Dúvida:** Implementar redirects para URLs antigas?
- ❓ **Dúvida:** Estrutura de URL para detalhes: `/lotes/{id}` ou `/leilao/{id}`?
- ❓ **Dúvida:** URLs amigáveis com slug do título do lote?

**Sugestão:** 
- URLs: `/lotes/{id}` e `/lotes/{id}/produtos`
- Implementar redirects básicos se necessário
- Deixar URLs amigáveis para versão futura

---

### 5. **Performance e Carga de Dados**
**Questão:** Catálogo de lotes pode ter diferentes necessidades de performance que catálogo de produtos.

**Pontos para discussão:**
- ✅ **Confirmado:** Lazy loading para imagens
- ✅ **Confirmado:** Cache para lotes em destaque
- ❓ **Dúvida:** Paginação: quantos lotes por página é ideal?
- ❓ **Dúvida:** Carregar contagem de produtos por lote pode ser custoso?
- ❓ **Dúvida:** Imagens de destaque dos lotes - como definir qual usar?

**Sugestão:**
- 20 lotes por página (similar aos produtos atuais)
- Cache de contagem de produtos por lote
- Imagem de destaque = primeira imagem do primeiro produto do lote

---

### 6. **Dados Mockados vs Dados Reais**
**Questão:** Remoção de dados mockados pode deixar algumas seções vazias.

**Pontos para discussão:**
- ✅ **Confirmado:** Remover estatísticas falsas da home
- ❓ **Dúvida:** O que colocar no lugar das estatísticas mockadas?
- ❓ **Dúvida:** Como tratar seções vazias quando não há lotes ativos?
- ❓ **Dúvida:** Manter seção de "features" do sistema ou remover?

**Sugestão:**
- Substituir estatísticas por seção de lotes em destaque
- Manter seção de features (não são dados mockados, são características do sistema)
- Estados vazios com call-to-action para cadastro de vendedores

---

### 7. **Integração com Sistema de Lances (Futuro)**
**Questão:** As mudanças devem preparar o terreno para futuro sistema de lances.

**Pontos para discussão:**
- ❓ **Dúvida:** Estrutura atual de lotes suporta bem sistema de lances?
- ❓ **Dúvida:** Precisamos de campos adicionais nos lotes para lances?
- ❓ **Dúvida:** Como tratar lotes que já encerraram mas ainda são interessantes para visualização?

**Sugestão:** Manter estrutura atual, adicionar campos conforme necessário em sprints futuras.

---

## 🔧 Decisões Técnicas Necessárias

### 1. **Estrutura de Entidades**
```sql
-- Tabela de favoritos (nova)
CREATE TABLE tb_lote_favorito (
    id VARCHAR(36) PRIMARY KEY,
    usuario_id VARCHAR(36) NOT NULL,
    lote_id VARCHAR(36) NOT NULL,
    favoritado_em DATETIME NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id),
    FOREIGN KEY (lote_id) REFERENCES tb_lote(id),
    UNIQUE KEY uk_usuario_lote (usuario_id, lote_id)
);
```

**Decisão necessária:** Confirmar estrutura da tabela de favoritos.

### 2. **Campos Adicionais em Lote**
```java
// Campos que podem ser necessários
private String imagemDestaque;
private String regras;
private Boolean destaque; // para lotes em destaque manual
private Integer visualizacoes; // para métricas futuras
```

**Decisão necessária:** Quais campos adicionais implementar agora vs futuro.

### 3. **Cache Strategy**
```yaml
# Configurações de cache propostas
cache:
  lotes-destaque:
    ttl: 5m
  lotes-catalogo:
    ttl: 2m
  lote-detalhes:
    ttl: 1m
```

**Decisão necessária:** TTL apropriado para cada tipo de cache.

---

## 📱 Considerações de UX/UI

### 1. **Fluxo de Navegação**
```
Atual: Home → Catálogo → Produto
Novo:  Home → Catálogo → Lote → Produtos do Lote
```

**Impacto:** Adiciona uma camada na navegação. Pode ser positivo (mais contexto) ou negativo (mais cliques).

### 2. **Cards de Lote vs Cards de Produto**
**Diferenças principais:**
- Lote: Tempo restante, número de produtos, preço total
- Produto: Preço individual, status específico

**Decisão necessária:** Layout e informações dos cards de lote.

### 3. **Estados Vazios**
**Cenários a considerar:**
- Nenhum lote ativo
- Usuário sem favoritos
- Lote sem produtos
- Busca sem resultados

**Decisão necessária:** Mensagens e call-to-actions para cada estado vazio.

---

## 🚀 Riscos e Mitigações

### 1. **Risco: Mudança Radical na UX**
**Impacto:** Usuários podem estranhar a nova navegação
**Mitigação:** 
- Manter breadcrumbs claros
- Adicionar tooltips explicativos
- Documentar mudanças para usuários

### 2. **Risco: Performance com Muitos Lotes**
**Impacto:** Catálogo pode ficar lento com muitos lotes
**Mitigação:**
- Implementar paginação eficiente
- Cache agressivo
- Lazy loading

### 3. **Risco: SEO Impact**
**Impacto:** Mudança de URLs pode afetar indexação
**Mitigação:**
- Implementar redirects
- Atualizar sitemap
- Meta tags apropriadas

---

## 📋 Checklist de Validações

### Antes de Iniciar a Sprint
- [ ] Confirmar estrutura de favoritos
- [ ] Definir critérios de "lotes em destaque"
- [ ] Validar estrutura de URLs
- [ ] Confirmar campos adicionais necessários
- [ ] Definir estratégia de cache

### Durante a Implementação
- [ ] Testar performance com dados reais
- [ ] Validar responsividade em todos os dispositivos
- [ ] Testar estados vazios
- [ ] Verificar acessibilidade básica
- [ ] Testar integração com sistema de autenticação

### Antes do Deploy
- [ ] Testar redirects de URLs antigas
- [ ] Validar meta tags para SEO
- [ ] Testar performance em produção
- [ ] Verificar logs de erro
- [ ] Testar com usuários reais (se possível)

---

## 🎯 Próximos Passos Sugeridos

1. **Revisar e aprovar** este documento com stakeholders
2. **Definir decisões pendentes** marcadas com ❓
3. **Validar mockups/wireframes** das novas telas
4. **Confirmar cronograma** e recursos disponíveis
5. **Iniciar implementação** seguindo o plano da sprint

---

**Documento criado para discussão e alinhamento antes do início da Sprint S2.3**