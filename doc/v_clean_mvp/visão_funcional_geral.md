# Documento Funcional — Sistema de Leilão Eletrônico

**Versão:** 1.0  
**Data:** 2025-11-19  
**Escopo:** MVP  
**Natureza:** Documentação Funcional / Regras de Negócio

---

## Sumário
1. Introdução  
2. Atores do Sistema  
3. Funcionalidades Principais  
4. Regras de Negócio Gerais  
5. Fluxos Funcionais Essenciais  
6. Restrições Funcionais  
7. Eventos, notificações e comunicações  
8. Glossário Funcional

---

# 1. Introdução

Este documento descreve a visão funcional do **Sistema de Leilão Eletrônico**, com foco no MVP, esclarecendo atores, comportamentos, regras de negócio e principais fluxos. O objetivo é fornecer entendimento suficiente para o desenho de telas, APIs, validações server-side e testes funcionais.

A arquitetura técnica existe separadamente; aqui o foco é **o que o sistema faz** e **como deve se comportar** na perspectiva de negócio e do usuário.

---

# 2. Atores do Sistema

### 2.1 Visitante (não autenticado)
- Acessa catálogo de leilões abertos (visualização).  
- Visualiza detalhes de itens/lotes sem interagir com lances.  
- Pode iniciar o processo de cadastro.

### 2.2 Usuário Autenticado
- Loga via e-mail + senha.  
- Acessa seu painel pessoal.  

Dependendo da permissão, pode assumir papéis funcionais:

### 2.3 Participante (Buyer)
- Dá lances em leilões ativos.  
- Acompanha leilões em tempo real (SSE/WS).  
- Visualiza histórico de lances e itens vencidos/perdidos.  
- Realiza pagamento do item arrematado.  

### 2.4 Vendedor (Seller)
- Cadastra produtos.  
- Cria lotes (opcional).  
- Define preço inicial, incremento mínimo e agenda.  
- Acompanha lances recebidos.  
- Gera ou visualiza documentos (contratos, comprovantes).  

### 2.5 Administrador (Admin)
- Modera usuários, vendedores, catálogos e leilões.  
- Gerencia fraudes, denúncias e reaberturas (quando aplicável).  
- Consulta logs/auditorias.  

---

# 3. Funcionalidades Principais

### 3.1 Catálogo e Navegação
- Listagem de leilões ativos e futuros.  
- Filtros por categoria, preço, status.  
- Paginação com ordenação (mais próximos do fim, recém-abertos etc.).

### 3.2 Detalhes do Item / Lote
- Informações completas: descrição, fotos, preço atual, bids count.  
- Timer de contagem regressiva.  
- Histórico de lances (limitado no MVP a últimos 20).  
- Regras de incremento e preço mínimo.

### 3.3 Leilões (Auction Engine)
- Abertura automática conforme agendamento.  
- Accept/reject de lances com feedback imediato.  
- Propagação de eventos em tempo real.  
- Regra de anti-sniping (opcional).  
- Encerramento automático + definição de vencedor.  

### 3.4 Lances (Bids)
- Envio de lance via WebSocket.  
- Retorno da validação (aceito / rejeitado).  
- Registro append-only.  
- Atualização do preço atual do item.  

### 3.5 Pagamentos
- Após vencer o leilão, o comprador recebe instrução de pagamento.  
- Integração com gateway externo.  
- Status: `AWAITING_PAYMENT → PAID → CONFIRMED`.  

### 3.6 Vendedores (Sellers)
- Cadastro de produtos.  
- Upload de imagens.  
- Criação/edição de lotes.  
- Restrições quando o item tiver lances.  

### 3.7 Documentos
- Geração de PDFs (contratos, recibos, comprovantes).  
- Armazenamento no storage (S3).  
- Download pelo comprador ou vendedor.  

### 3.8 Auditoria
- Registro de ações críticas: login, publicação de item, lances, encerramento, pagamentos.  
- Registro não editável (append-only).  

---

# 4. Regras de Negócio Gerais

### 4.1 Regras para Lances
1. Um lance só pode ser enviado por usuário autenticado (Buyer).  
2. O valor do lance deve ser **>= (currentPrice + minIncrement)**.  
3. O backend é o _source of truth_: último valor exibido no front pode estar defasado.  
4. Em caso de empates, vence o lance que o servidor registrar primeiro (`serverSequenceId`).  
5. O lance só é aceito se o item estiver com status `ACTIVE`.  
6. Após encerramento, nenhum lance deve ser aceito (retornar erro específico).  

### 4.2 Regras de Encerramento
1. O encerramento deve ocorrer no horário agendado ou após anti-sniping.  
2. Deve existir apenas um encerramento válido por item (lock distribuído).  
3. O vencedor é o maior lance válido.  
4. Caso não haja lances, o item encerra como `UNSOLD`.  

### 4.3 Regras para Sellers
1. Produto pertencente a leilão ativo **não pode ser removido ou editado** em campos críticos.  
2. Se o item já recebeu lance, não pode ser removido do lote.  
3. Taxa (feeRate) deve ser determinada a partir do contrato/versão vigente.  

### 4.4 Regras de Pagamento
1. O vencedor tem prazo para realizar o pagamento (configurável por item).  
2. Status pós-pagamento é atualizado via webhook do gateway.  
3. Caso o pagamento não ocorra no prazo, o item pode ser marcado como `PAYMENT_EXPIRED`.  

### 4.5 Regras de Documentação
1. Documentos gerados devem conter hash e carimbo de data/hora.  
2. Links de download devem ser temporários.  

---

# 5. Fluxos Funcionais Essenciais

## 5.1 Fluxo — Visualizar Catálogo
1. Usuário (autenticado ou não) acessa `/catalog`.  
2. Sistema retorna lista paginada de itens/leilões ativos ou futuros.  
3. Usuário aplica filtros.  
4. Front atualiza lista mantendo paginação.

## 5.2 Fluxo — Participar de Leilão (Realtime)
1. Usuário acessa página do item.  
2. SSE inicia conexão de leitura.  
3. WebSocket inicia (se autenticado).  
4. Lances de outros usuários chegam via SSE.  
5. Usuário envia lance pelo WebSocket.  
6. Backend valida; se aceito → broadcast do novo estado.  

## 5.3 Fluxo — Cadastro de Produto (Seller)
1. Seller acessa “Meus Produtos”.  
2. Clica em “Novo Produto”.  
3. Preenche título, descrição, categorias, fotos.  
4. Backend salva rascunho.  
5. Seller configura preço inicial e incremento mínimo.  
6. Produto é marcado como `READY` para agendamento.

## 5.4 Fluxo — Encerramento do Leilão
1. Scheduler detecta que o horário foi atingido.  
2. Backend adquire lock distribuído.  
3. Calcula último lance válido.  
4. Define vencedor (se existir).  
5. Atualiza status e registra auditoria.  
6. Broadcast do encerramento via Redis + SSE/WS.  

## 5.5 Fluxo — Pagamento
1. Vencedor visualiza item no painel.  
2. Clica em “Pagar Agora”.  
3. Sistema cria transação no gateway.  
4. Gateway retorna status inicial.  
5. Webhook atualiza status definitivo.  
6. Documento de recibo é gerado.

---

# 6. Restrições Funcionais

1. Visitantes não enviam lances.  
2. Sellers não podem dar lances em seus próprios itens.  
3. Admins podem mudar status de itens apenas em casos excepcionais.  
4. Não é permitido editar campos essenciais após início do leilão.  
5. Histórico completo do item deve ser sempre preservado.  

---

# 7. Eventos, Notificações e Comunicações

### 7.1 Eventos Internos
- `AUCTION_STARTED`  
- `BID_PLACED`  
- `BID_REJECTED`  
- `AUCTION_EXTENDED`  
- `AUCTION_FINISHED`  
- `PAYMENT_CONFIRMED`

### 7.2 Notificações ao Usuário
- E-mail de confirmação de arremate.  
- E-mail de expiração de pagamento.  
- Notificação ao seller sobre lances recebidos.  

---

# 8. Glossário Funcional

| Termo | Definição |
|------|-----------|
| Auction | Leilão referente a um item ou lote. |
| Bid | Lance enviado por um comprador. |
| Seller | Usuário que publica itens no sistema. |
| Buyer | Usuário que participa de leilões enviando lances. |
| Start Price | Valor inicial do leilão. |
| Min Increment | Valor mínimo que um lance deve adicionar ao preço atual. |
| Anti-Sniping | Regra que estende o fim do leilão em caso de lances nos últimos segundos. |
| Append-only | Modelo de gravação onde os registros não são atualizados, apenas inseridos. |

---

# Encerramento
Este documento cobre a visão funcional base do MVP.  
No próximo passo, você pode solicitar:

- detalhamento por **épico → feature → história**,  
- criação de **casos de uso**,  
- **refinamento** sprint a sprint,  
- ou ampliação das **regras de negócio** para cada módulo.

