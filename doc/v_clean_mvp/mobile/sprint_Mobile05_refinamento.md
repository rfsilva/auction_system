# 🏁 Sprint 16 — Experiência Expandida do App: Favoritos, Alertas, Perfil e Histórico
**Duração:** 2 semanas  
**Equipe:** Mobile + Backend + Arquiteto  
**Objetivo:** Completar o núcleo da experiência do comprador no app, adicionando recursos complementares de conveniência, personalização e continuidade de uso.

---

# 🔥 HISTÓRIAS REFINADAS (S16)

---

## **H16.1 — Adicionar Suporte a Favoritar Itens pelo App**
**Descrição:** O usuário deve poder favoritar/desfavoritar itens de leilão e sincronizar com backend (compatível com Web).

### Tarefas
- [ ] Criar endpoint mobile `/api/mobile/user/favorites`
- [ ] Implementar método POST/DELETE para favoritar
- [ ] Exibir botão “⭐ Favorito” no app
- [ ] Criar cache local dos favoritos
- [ ] Testes unitários e integrados

### Critérios de Aceite
- Favoritar/desfavoritar deve refletir no backend em ≤ 500ms  
- Sincronização deve funcionar mesmo após reinstalação  
- Ícone deve atualizar instantaneamente

### Tamanho
**5 pontos**

---

## **H16.2 — Exibir Lista Completa de Favoritos**
**Descrição:** Criar página dedicada para exibir todos os itens favoritados do usuário.

### Tarefas
- [ ] Criar tela `FavoritesPage`
- [ ] Integrar com serviço de favoritos
- [ ] Exibir status do item (ativo / futuro / encerrado)
- [ ] Permitir navegação rápida para detalhe do item
- [ ] Criar skeleton loading

### Critérios de Aceite
- Lista deve carregar em < 1.5 segundos  
- Itens encerrados devem ter visual diferenciado  
- Deve permitir pull-to-refresh  

### Tamanho
**5 pontos**

---

## **H16.3 — Configurações de Notificações (Push)**
**Descrição:** O usuário deve controlar quais push notifications quer receber no app.

### Tipos:
- Lembrete de leilão (pré-início)
- Lance superado
- Lote abrindo agora
- Oferta relâmpago (futuro)

### Tarefas
- [ ] Tela “Configurações de Notificações”
- [ ] Opção liga/desliga por categoria
- [ ] Atualizar tabela `user_notification_settings`
- [ ] Criar endpoint `/api/mobile/user/notifications/settings`
- [ ] Testes E2E

### Critérios de Aceite
- Alterações devem refletir no backend imediatamente  
- Push não deve ser enviado se toggle estiver off  
- Tela deve refletir estado correto ao reabrir app  

### Tamanho
**8 pontos**

---

## **H16.4 — Histórico de Lances (Mobile)**
**Descrição:** Exibir ao usuário todo o histórico de lances feitos nos leilões.

### Dados exibidos:
- Item
- Valor do lance
- Data e hora
- Resultado (ganhou / perdeu / superado)
- Link para item

### Tarefas
- [ ] Criar endpoint otimizado `/api/mobile/user/bids`
- [ ] Criar tela `BidHistoryPage`
- [ ] Criar filtros (últimos 7 dias / mês / tudo)
- [ ] Mostrar estado do lance
- [ ] Criar paginação infinita (scroll)

### Critérios de Aceite
- Página deve carregar em < 2 segundos  
- Filtros aplicam em < 500ms  
- Deve exibir estado atualizado (se perdeu o item recentemente)  

### Tamanho
**8 pontos**

---

## **H16.5 — Tela de Perfil do Usuário**
**Descrição:** Permitir consulta e edição de informações básicas.

### Campos:
- Nome
- Email (somente leitura)
- Telefone
- Documento
- Endereço

### Tarefas
- [ ] Criar tela `UserProfilePage`
- [ ] Criar endpoint PATCH `/api/mobile/user/profile`
- [ ] Validações
- [ ] Máscaras de telefone/documento
- [ ] Integrar com push (atualização de device tokens)

### Critérios de Aceite
- Atualizações devem persistir corretamente  
- Validação deve bloquear dados incompletos  
- Mudança de telefone deve exigir confirmação (MVP: não)  

### Tamanho
**8 pontos**

---

## **H16.6 — Sessão: gerenciamento de múltiplos dispositivos**
**Descrição:** Permitir que o usuário veja seus dispositivos logados e encerre sessões (mesmo do app).

### Tarefas
- [ ] Criar endpoint `/api/mobile/user/devices`
- [ ] Listar deviceId, modelo, data login
- [ ] Permitir “Encerrar sessão”
- [ ] Atualizar revogação de token
- [ ] Testes

### Critérios de Aceite
- Encerrar sessão força logout remoto  
- Lista deve refletir estado real do Redis/sessions  
- App deve detectar perda de sessão e redirecionar para login  

### Tamanho
**5 pontos**

---

## **H16.7 — Modo Offline: Cache de Catálogo**
**Descrição:** O app deve manter cópia offline mínima dos itens recentes.

### Dados mantidos:
- Nome
- Imagem thumb
- Preço base
- Status do leilão

### Tarefas
- [ ] Criar store local persistente
- [ ] Cachear últimos 20 itens acessados
- [ ] Exibir catálogo reduzido offline
- [ ] Mostrar estado “Desatualizado”

### Critérios de Aceite
- Catálogo offline deve abrir mesmo sem internet  
- Deve ser substituído automaticamente quando o app voltar online  
- Versões antigas devem ser descartadas  

### Tamanho
**13 pontos**

---

## **H16.8 — Enabler: Otimização do Back-end para Consultas Mobile**
**Descrição:** Reduzir payloads e criar índices específicos.

### Tarefas
- [ ] Criar índices para consultas de favoritos
- [ ] Reduzir DTOs (compact mode)
- [ ] Habilitar compressão gzip/brotli
- [ ] Refatorar queries de histórico

### Critérios de Aceite
- Todas as respostas mobile devem ser ≤ 80% do tamanho original  
- Todas as consultas chave devem responder em ≤ 120 ms  
- Mobile e Web não podem se afetar mutuamente  

### Tamanho
**8 pontos**

---

# 📌 RESUMO DA SPRINT

| Item   | Pontos |
|--------|--------|
| H16.1  | 5 |
| H16.2  | 5 |
| H16.3  | 8 |
| H16.4  | 8 |
| H16.5  | 8 |
| H16.6  | 5 |
| H16.7  | 13 |
| H16.8  | 8 |
| **Total** | **60 pontos** |

---

# 📘 Observações do Arquiteto

- Essa sprint completa a base de **experiência contínua** do app.  
- A S16 fecha toda parte “mínima porém robusta” de um app de marketplace/leilões.  
- A S17 deve evoluir para performance, UI avançada, otimização de streaming, ranking e recursos premium.

