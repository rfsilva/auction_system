##🟪 TEMA 1 — Experiência do Visitante

| Tema | Épico | História / Enabler | Tipo | Descrição | Critérios de Aceite |
|------|-------|---------------------|------|-----------|----------------------|
| Experiência do Visitante | Navegação e Catálogo Público | Visualizar catálogo geral | História | Visitante visualiza lista de itens pública | Lista paginada; título, imagem, preço; filtros funcionam; sem login |
| Experiência do Visitante | Navegação e Catálogo Público | Buscar itens | História | Visitante busca itens por keyword | Busca por título/descrição; resposta < 2s; funciona sem login |
| Experiência do Visitante | Navegação e Catálogo Público | Filtrar itens | História | Filtros por categoria, preço, status | Combinação de filtros; reset; refletir URL |
| Experiência do Visitante | Acompanhamento de Leilões | Receber atualizações via SSE | História | Atualizações em tempo real via SSE | Eventos de lance/status; atualizar tela; sem login |
| Experiência do Visitante | Acompanhamento de Leilões | Visualizar página de item ao vivo | História | Página do item com dados ao vivo | Tempo restante, preço atual; SSE/WebSocket active |
| Experiência do Visitante | Enablers | EN-01 Setup SSE | Enabler | Configuração do canal SSE público | SSE estável e broadcast eficiente |
| Experiência do Visitante | Enablers | EN-02 Canal WebSocket público RO | Enabler | WebSocket para assinantes anônimos | Conexão escalável e segura |
| Experiência do Visitante | Enablers | EN-03 Indexação backend | Enabler | Busca eficiente | Indexação full-text |
| Experiência do Visitante | Enablers | EN-04 Paginação performática | Enabler | Cursor-based pagination | Respostas rápidas e estáveis |
