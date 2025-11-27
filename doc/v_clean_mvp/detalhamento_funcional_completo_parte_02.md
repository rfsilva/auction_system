# 📘 Documento de Requisitos – PARTE 2  
## Use Cases, Regras, Fluxos, Integrações e Critérios de Aceite  
## Papel: Participante – **Comprador**

---

# ## 2. Participante – Comprador

O **Comprador** é o usuário autenticado que acessa a plataforma para pesquisar, selecionar e adquirir itens, interagir com vendedores e acompanhar pedidos em tempo real.  
Este documento aprofunda todos os requisitos funcionais relacionados a este papel.

---

# ### 2.1. Atribuições Gerais do Comprador

- Realizar autenticação.
- Navegar pelo catálogo e filtros avançados.
- Adicionar, remover e gerenciar itens no carrinho.
- Finalizar compras.
- Receber notificações e atualizações via **SSE** (status do pedido).
- Interagir com vendedores via **WebSocket**.
- Consultar histórico de compras.
- Avaliar produtos e vendedores.
- Gerenciar dados de perfil, endereços e notificações.

---

# ### 2.2. Use Cases Detalhados

---

## **UC-C01 – Autenticação do Comprador**

### **Descrição**
Permite que o usuário logado acesse a plataforma através de email/senha ou autenticação social.

### **Atores**
- Comprador
- Sistema de Autenticação (Auth Service)

### **Pré-condições**
- Usuário possui cadastro válido.
- Conta não está bloqueada ou pendente de validação.

### **Fluxo Principal**
1. Comprador informa email e senha.
2. Sistema valida credenciais.
3. Sistema retorna JWT (access + refresh).
4. Sessão é iniciada.
5. Dados de perfil mínimos são carregados.
6. Usuário é redirecionado à página principal.

### **Fluxos Alternativos**
- Credenciais inválidas → erro sem revelar qual campo está incorreto.
- Conta bloqueada → exibir mensagem.
- MFA habilitado → solicitar fator adicional.

### **Regras de Negócio**
- **RN-C01:** Hash seguro obrigatório (PBKDF2, bcrypt, argon2).
- **RN-C02:** 5 tentativas falhas resultam em bloqueio temporário.
- **RN-C03:** Token expira em 15min; refresh expira em 7 dias.

### **Integrações**
- Auth Server
- Auditoria

### **Critérios de Aceite**
- Login deve ocorrer em < 2s.
- Mensagens não podem revelar existência de email.
- Renovação automática de token se refresh válido.

---

## **UC-C02 – Visualizar Catálogo**

### **Descrição**
Exibe lista completa de produtos com paginação.

### **Atores**
- Comprador
- Vendedor (fonte dos itens)

### **Pré-condições**
- Catálogo possui itens ativos.

### **Fluxo Principal**
1. Comprador acessa catálogo.
2. Sistema consulta serviço de catálogo.
3. Lista paginada é exibida com miniatura, preço e disponibilidade.

### **Regras de Negócio**
- **RN-C04:** Itens sem estoque não podem ser adicionados ao carrinho.
- **RN-C05:** Itens com desconto devem exibir identificação visual obrigatória.

### **Integrações**
- Serviço de Catálogo
- Cache distribuído

### **Critérios de Aceite**
- Página carrega em < 500ms.
- Itens sem estoque aparecem como “Indisponível”.

---

## **UC-C03 – Aplicar Filtros e Pesquisa Avançada**

### **Descrição**
Permite usar filtros e busca textual para refinar o catálogo.

### **Regras de Negócio**
- **RN-C06:** Filtro de preço deve permitir ranges arbitrários.
- **RN-C07:** Ordenação por relevância considera: texto > vendas > avaliações.

### **Critérios de Aceite**
- Tempo de resposta < 1 segundo.
- Se filtros retornarem zero resultados, deve exibir mensagem clara.

---

## **UC-C04 – Visualizar Detalhes do Item**

### **Fluxo Principal**
1. Comprador abre a página de item.
2. Sistema exibe:
   - imagens (carregamento progressivo)
   - descrição completa
   - variações do produto
   - avaliações
   - política do vendedor
3. Sistema exibe seção "Itens relacionados".

### **Regras de Negócio**
- **RN-C08:** Avaliações só podem ser exibidas após moderação antifraude.
- **RN-C09:** Variantes obrigatórias devem ser selecionadas antes de avançar.

### **Critérios de Aceite**
- Página deve carregar em até 2s.
- Todas as imagens devem ser otimizadas.

---

## **UC-C05 – Adicionar Item ao Carrinho**

### **Pré-condições**
- Item tem estoque.
- Variantes selecionadas (quando aplicável).

### **Fluxo**
1. Comprador define quantidade.
2. Sistema valida estoque.
3. Item é adicionado ao carrinho persistido.
4. Carrinho atualizado é exibido.

### **Regras de Negócio**
- **RN-C10:** Quantidade não pode exceder estoque disponível.
- **RN-C11:** Carrinho deve ser salvo no backend, não apenas no navegador.

### **Critérios de Aceite**
- Atualização instantânea.
- Mensagem clara de erro em caso de estoque insuficiente.

---

## **UC-C06 – Gerenciar Carrinho**

### **Ações**
- Alterar quantidade.
- Remover item.
- Limpar carrinho.
- Calcular frete.
- Aplicar cupom.

### **Regras de Negócio**
- **RN-C12:** Cupom não pode ser acumulado com outros cupons.
- **RN-C13:** Frete deve ser recalculado sempre que o CEP mudar.

### **Critérios de Aceite**
- Backend deve garantir consistência do carrinho.
- Operações devem refletir imediatamente.

---

## **UC-C07 – Checkout**

### **Fluxo Principal**
1. Selecionar endereço de entrega.
2. Selecionar método de envio.
3. Selecionar meio de pagamento.
4. Revisar dados.
5. Confirmar compra.
6. Pagamento é autorizado.
7. Pedido é gerado.

### **Regras de Negócio**
- **RN-C14:** Todas as compras passam por antifraude.
- **RN-C15:** Pedido só é criado após sucesso no pagamento.
- **RN-C16:** Valores são congelados na confirmação do pedido.

### **Integrações**
- Gateway de Pagamento
- Serviço de Antifraude
- Serviço de Pedidos
- Sistema de Notificações

### **Critérios de Aceite**
- Processo completo em no máximo 5s.
- Erros de pagamento devem ser claros e amigáveis.

---

## **UC-C08 – Acompanhar Pedido (via SSE)**

### **Descrição**
Comprador recebe atualizações automáticas do status do pedido.

### **Fluxo**
1. Cliente abre conexão SSE.
2. Backend envia eventos em tempo real.
3. Front atualiza timeline visual.

### **Regras de Negócio**
- **RN-C17:** Eventual atraso máximo de 1s entre backend e cliente.
- **RN-C18:** Reconexão automática obrigatória em caso de falha.

### **Critérios de Aceite**
- Timeline atualiza sem recarregar a página.
- Cada evento exibe data e hora exatas.

---

## **UC-C09 – Chat com Vendedor (via WebSocket)**

### **Descrição**
Canal de comunicação direta e instantânea.

### **Regras de Negócio**
- **RN-C19:** Mensagens devem ser criptografadas.
- **RN-C20:** Sistema deve detectar e suprimir ofensas graves.

### **Critérios de Aceite**
- Conversas devem ter latência < 300ms.
- Histórico deve ser persistido.

---

## **UC-C10 – Cancelar Pedido**

### **Fluxo Principal**
1. Comprador solicita cancelamento.
2. Sistema valida status.
3. Se permitido:
   - Estorno é iniciado.
   - Vendedor é notificado.
   - Status é atualizado.

### **Regras de Negócio**
- **RN-C21:** Pedido enviado não pode ser cancelado.
- **RN-C22:** Estorno deve ser realizado no método original.

### **Critérios de Aceite**
- Cancelamento deve ser processado em até 24h.
- Mensagens claras sobre elegibilidade.

---

## **UC-C11 – Avaliações de Produtos e Vendedores**

### **Fluxo**
1. Comprador escolhe número de estrelas.
2. Escreve comentário.
3. Sistema modera o conteúdo.
4. Avaliação é publicada.

### **Regras**
- **RN-C23:** Avaliações só são permitidas após compra real.
- **RN-C24:** Comentários com palavras proibidas são barrados.

### **Critérios**
- Publicação em até 2 min após moderação.

---

## **UC-C12 – Gerenciar Perfil**

### **Ações**
- Alterar dados pessoais.
- Alterar senha.
- Gerenciar endereços.
- Ajustar notificações.

### **Regras**
- **RN-C25:** Endereços validados via API de CEP.
- **RN-C26:** Alteração de email exige verificação.

---

## **UC-C13 – Histórico de Compras**

### **Fluxo**
1. Carrega lista de pedidos.
2. Permite filtrar por status, data, faixa de preço, vendedor.

### **Regras**
- **RN-C27:** Histórico deve incluir pelo menos 24 meses.

---

# ### 2.3. Regras de Negócio Consolidado – Comprador

| Código | Regra | Descrição |
|-------|--------|-----------|
| RN-C01 | Hash seguro | Hash forte obrigatório |
| RN-C02 | Tentativas falhas | 5 tentativas → bloqueio |
| RN-C03 | Expiração de tokens | 15 min / 7 dias |
| RN-C04 | Estoque | Sem estoque → não adiciona |
| RN-C05 | Desconto | Exibir selo visual obrigatório |
| RN-C06 | Filtro | Range ilimitado |
| RN-C07 | Ordenação | texto > vendas > avaliação |
| RN-C08 | Moderação de reviews | Obrigatória |
| RN-C09 | Variantes | Seleção obrigatória |
| RN-C10 | Quantidade | Não ultrapassar estoque |
| RN-C11 | Carrinho persistido | Deve existir no backend |
| RN-C12 | Cupom único | Proibido acumular |
| RN-C13 | Frete recalculado | Sempre no CEP |
| RN-C14 | Antifraude | Obrigatório |
| RN-C15 | Pedido | Só após pagamento |
| RN-C16 | Congelamento | Congela valores no checkout |
| RN-C17 | SSE atraso | Máx. 1s |
| RN-C18 | Reconexão SSE | Obrigatória |
| RN-C19 | WS criptografado | Obrigatório |
| RN-C20 | Moderação chat | Necessária |
| RN-C21 | Cancelamento restrito | Enviado = não cancela |
| RN-C22 | Estorno | Mesmo método |
| RN-C23 | Avaliação real | Apenas compradores |
| RN-C24 | Linguagem | Conteúdo proibido bloqueado |
| RN-C25 | CEP | Validação oficial |
| RN-C26 | Email | Exige revalidação |
| RN-C27 | Histórico | 24 meses mínimos |

---

# ### 2.4. Integrações – Comprador

- Serviço de Autenticação (OAuth2/JWT)
- Serviço de Catálogo
- Serviço de Carrinho
- Serviço de Pedidos
- Serviço de Pagamentos
- Gateway de Pagamentos
- Serviço de Antifraude
- Serviço de Mensageria (SSE)
- Serviço de Chat (WebSocket)
- Serviço de Avaliações
- Serviço de Endereços/CEP
- Serviço de Notificações

---

# ### 2.5. Critérios de Aceite Globais do Comprador

- Todas as operações devem responder em < 2 segundos.
- SSE deve reconectar automaticamente.
- WebSocket deve reconectar e retomar contexto.
- Estados do carrinho e pedido nunca podem ficar inconsistentes.
- Todos os erros devem ser amigáveis e não técnicos.
- Todas as ações relevantes devem ser auditadas.
- Dados pessoais devem seguir LGPD.

---

