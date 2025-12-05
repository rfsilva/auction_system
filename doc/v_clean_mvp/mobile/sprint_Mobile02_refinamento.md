# 🟩 Sprint S13 — Refinamento Detalhado
**Objetivo da Sprint:**  
Entregar a primeira funcionalidade real do app para o comprador: visualizar leilões, filtrar, buscar, abrir detalhes e navegar entre itens.

---

# 📌 HISTÓRIA H-APP-010 — Catálogo Inicial de Leilões
**Como** Comprador  
**Quero** visualizar uma lista de leilões ativos  
**Para** encontrar rapidamente os itens disponíveis.

### Critérios de Aceite
- [ ] Catálogo deve exibir: imagem principal, nome do item, preço inicial, status, horário de início/fim.  
- [ ] Deve paginar infinitamente (scroll infinito).  
- [ ] Ao tocar no item → segue para detalhes do leilão.  
- [ ] A lista deve atualizar ao puxar para baixo (pull to refresh).  
- [ ] Deve funcionar mesmo em rede lenta (placeholder shimmer).  
- [ ] Deve exibir mensagem amigável quando não houver leilões ativos.  

### Tasks
- [ ] Criar provider/BLoC para carregamento do catálogo  
- [ ] Criar requisição GET `/auctions?status=active&page=X`  
- [ ] Criar componentes de cartão do item  
- [ ] Implementar scroll infinito  
- [ ] Implementar pull-to-refresh  
- [ ] Implementar shimmer de carregamento  
- [ ] Integrar navegação para detalhes  
- [ ] Testes manuais de UX  

### Tamanho (Planning Poker): **8 pontos**

---

# 📌 HISTÓRIA H-APP-011 — Busca de Produtos e Leilões
**Como** Comprador  
**Quero** buscar itens por nome, categoria ou palavras-chave  
**Para** encontrar leilões de forma mais rápida.

### Critérios de Aceite
- [ ] Barra de busca deve sugerir resultados enquanto digita (autocomplete).  
- [ ] Deve suportar busca por: título, categoria e descrição.  
- [ ] Histórico de buscas deve ser salvo localmente (opcional limpar histórico).  
- [ ] Busca deve abrir lista filtrada.  
- [ ] Em caso de nada encontrado → exibir tela específica.  

### Tasks
- [ ] Criar componente de search com debounce  
- [ ] Criar serviço GET `/search?query=X`  
- [ ] Implementar autocomplete  
- [ ] Criar tela de resultados  
- [ ] Persistir histórico no local storage  
- [ ] Criar botão de “limpar histórico”  
- [ ] Testes com diferentes velocidades de digitação  

### Tamanho: **5 pontos**

---

# 📌 HISTÓRIA H-APP-012 — Filtros do Catálogo
**Como** Comprador  
**Quero** aplicar filtros no catálogo  
**Para** refinar a listagem e encontrar o item ideal.

### Filtros suportados (versão MVP do app)
- Categoria  
- Faixa de valor  
- Status do leilão (próximos / ativos / finalizados)  
- Ordem (relevância / menor preço / mais próximos de começar)  

### Critérios de Aceite
- [ ] Bottom sheet com filtros.  
- [ ] Aplicar filtros deve recarregar catálogo.  
- [ ] Deve permitir limpar todos os filtros.  
- [ ] Filtros aplicados devem ser exibidos como chips na tela inicial.  

### Tasks
- [ ] Criar componente de bottom sheet  
- [ ] Criar UI dos filtros  
- [ ] Salvar estado dos filtros  
- [ ] Enviar parâmetros na API `/auctions`  
- [ ] Criar chips dos filtros aplicados  
- [ ] Testes diversos com catálogo grande  

### Tamanho: **5 pontos**

---

# 📌 HISTÓRIA H-APP-013 — Tela de Detalhes do Leilão (Versão 1 - Estática)
**Como** Comprador  
**Quero** visualizar informações completas de um leilão  
**Para** decidir se vale a pena entrar ou acompanhar.

### Critérios de Aceite
- [ ] Exibir:  
  - imagens  
  - título  
  - descrição  
  - preço inicial  
  - status  
  - horário de início/fim  
  - lances e regras básicas (texto estático por enquanto)  
- [ ] Botão “Participar do Leilão” (irá abrir na Sprint S14).  
- [ ] Placeholder enquanto carrega.  
- [ ] Mensagem de erro caso ID do leilão esteja inválido.  
- [ ] Layout igual ao da Web (adaptado para mobile).  

### Tasks
- [ ] Criar provider/BLoC de detalhes  
- [ ] Requisição GET `/auction/{id}`  
- [ ] Criar galeria de imagens  
- [ ] Criar layout estático da descrição e informações  
- [ ] Inserir botão “Participar” (sem lógica ainda)  
- [ ] Testes manuais  

### Tamanho: **8 pontos**

---

# 📌 HISTÓRIA H-APP-014 — Favoritar Itens (Versão 1 - Local)
**Como** Comprador  
**Quero** favoritar um leilão  
**Para** consultar depois rapidamente.

### Critérios de Aceite
- [ ] Ao favoritar → salvar localmente (versão 1).  
- [ ] Ao desfavoritar → remover localmente.  
- [ ] Ícone deve refletir favoritado / não favoritado.  
- [ ] Tela do catálogo deve refletir favoritos também.  
- [ ] Sincronização com backend será feita na Sprint S16.  

### Tasks
- [ ] Criar storage local (Hive / shared prefs)  
- [ ] Criar repo local de favoritos  
- [ ] Criar botão “favoritar” nos cards  
- [ ] Conectar com detalhes do leilão  
- [ ] Criar evento global para atualizar catálogo  
- [ ] Testes de UX  

### Tamanho: **3 pontos**

---

# ⚙️ ENABLERS S13

---

## 🛠️ EN-APP-10 — Ajustes na Arquitetura de Navegação
**Descrição:**  
Criação dos módulos e rotas para catálogo, detalhes e busca.

### Tasks
- [ ] Adicionar rotas no router principal  
- [ ] Criar navegação com deep links  
- [ ] Criar arquitetura modular por feature  
- [ ] Ajustar controladores globais  

### Tamanho: **3 pontos**

---

## 🛠️ EN-APP-11 — Componente Base de Listagens
**Descrição:**  
Criar widget padrão reutilizável para listas com scroll infinito, shimmer e handling de erro.

### Tasks
- [ ] Criar componente genérico  
- [ ] Criar estados: loading / empty / error / loaded  
- [ ] Ajustar responsividade  
- [ ] Documentar uso no README do módulo  

### Tamanho: **5 pontos**

---

# 📊 **Resumo da Sprint S13**
| Item | Pontos |
|------|--------|
| H-APP-010 | 8 |
| H-APP-011 | 5 |
| H-APP-012 | 5 |
| H-APP-013 | 8 |
| H-APP-014 | 3 |
| EN-APP-10 | 3 |
| EN-APP-11 | 5 |
| **Total estimado** | **37 pontos** |

> Mais uma sprint cheia, mas totalmente viável considerando sinergia com o que já foi construído na S12.

---

