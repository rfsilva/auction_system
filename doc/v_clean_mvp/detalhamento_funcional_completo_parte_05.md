# 📘 Documento Funcional Detalhado — Parte 5  
## Sistema de Eventos — Fluxos Detalhados dos Casos de Uso  
### **(Consolidado — Seção 5 de N)**

---

# 10. Fluxos Detalhados dos Casos de Uso

A seguir estão os fluxos **completamente detalhados**, com caminhos básicos, alternativos e exceções, conforme boas práticas de documentação funcional clássica.

---

# 10.1. Casos de Uso — Visitante

---

## UC-VIS-001 — Consultar Lista de Eventos

### **Descrição**
Permite ao Visitante visualizar todos os eventos disponíveis no catálogo público.

### **Ator Primário**
Visitante

### **Pré-Condições**
- Sistema online
- Catálogo de eventos carregado na base

### **Pós-Condições**
- Nenhuma alteração persistida

### **Fluxo Principal**
1. Visitante acessa a página inicial.
2. Sistema exibe lista de eventos disponíveis.
3. Visitante pode rolar a página para visualizar mais itens.
4. Sistema carrega mais eventos sob demanda (scroll infinito ou paginação).
5. Visitante seleciona um evento para ver detalhes (opcional).

### **Fluxos Alternativos**
- **FA1 — Falha de conexão com backend**
  1. Sistema exibe mensagem: *"Não foi possível carregar os eventos."*
  2. Tenta novamente após 5 segundos.

### **Regras de Negócio Associadas**
- RN-VIS-001  
- RN-VIS-003  
- RN-GEN-002  

### **Critérios de Aceite**
- CA-GEN-001  
- CA-GEN-005  

---

## UC-VIS-002 — Visualizar Detalhes do Evento

### **Descrição**
Apresenta ao Visitante todas as informações públicas de um evento.

### **Fluxo Principal**
1. Visitante seleciona um evento na lista.
2. Sistema carrega dados completos do evento.
3. Sistema exibe:
   - Descrição
   - Categoria
   - Local
   - Data e horário
   - Lotes e disponibilidade
   - Imagens
4. Visitante navega pelos detalhes.

### **Fluxos Alternativos**
- **FA1 — Evento despublicado enquanto Visitante tenta acessar**
  - Sistema exibe mensagem: *"Este evento não está disponível no momento."*

### **Regras de Negócio**
- RN-VIS-004

### **Critérios de Aceite**
- Detalhes devem carregar em ≤ 2s.
- Imagens devem ter fallback.

---

## UC-VIS-003 — Buscar e Filtrar Eventos

### **Fluxo Principal**
1. Visitante acessa buscador.
2. Digita termo ou aplica filtros.
3. Sistema refina a lista.
4. Resultados aparecem em tempo real.

### **Regras de Negócio**
- RN-VIS-003

---

# 10.2. Casos de Uso — Comprador

---

## UC-COM-001 — Criar Conta

### **Fluxo Principal**
1. Usuário preenche dados obrigatórios.
2. Sistema valida:
   - Formato de e-mail
   - Força da senha
3. Sistema cria conta.
4. Sistema dispara e-mail de confirmação.

### **Fluxo Alternativo**
- **FA1 — E-mail já existente**  
  Sistema exibe erro e sugere login.

### **Regras de Negócio**
- RN-COM-001  

---

## UC-COM-002 — Autenticar

### **Fluxo Principal**
1. Usuário insere e-mail e senha.
2. Sistema valida credenciais.
3. Gera token JWT.
4. Inicia sessão.

### **Fluxos Alternativos**
- **Senha incorreta** → mensagem clara.
- **Conta bloqueada** → aviso de suporte.

---

## UC-COM-003 — Adicionar Ingressos ao Carrinho

### **Fluxo Principal**
1. Comprador seleciona evento e lote.  
2. Escolhe quantidade.  
3. Sistema valida:
   - Disponibilidade
   - Limite por usuário  
4. Sistema adiciona ao carrinho e inicia reserva de 15 min.
5. Interface exibe cronômetro regressivo.

### **Fluxos Alternativos**
- **FA1 — Estoque insuficiente**  
  Exibe quantidade permitida.  
- **FA2 — Tempo de reserva expirado**  
  Itens removidos automaticamente.

### **Regras de Negócio**
- RN-COM-002  
- RN-COM-003  
- RN-COM-004  

---

## UC-COM-004 — Finalizar Compra

### **Fluxo Principal**
1. Comprador abre o carrinho.  
2. Sistema atualiza disponibilidade em tempo real (SSE/WebSocket).  
3. Comprador confirma itens.  
4. Sistema cria ordem de pagamento.  
5. Gateway processa.  
6. Sistema recebe callback (webhook).  
7. Sistema:
   - Confirma pagamento  
   - Atualiza estoque  
   - Gera ingresso  
   - Registra auditoria  

### **Fluxos Alternativos**
- **FA1 — Pagamento recusado**  
  - Ordem marcada como *falha*.  
  - Estoque liberado.  
  - Comprador avisado.  

### **Regras de Negócio**
- RN-COM-005  
- RN-COM-006  
- RN-COM-010  

### **Critérios de Aceite**
- 95% das compras finalizam em ≤ 5s.  
- Estoque nunca pode ficar negativo.

---

## UC-COM-005 — Histórico de Compras

### **Fluxo Principal**
1. Comprador acessa histórico.  
2. Sistema exibe lista cronológica.  
3. Comprador seleciona uma compra para detalhes:
   - Itens
   - Valor
   - Status  
   - Ingressos (QR Code)

---

## UC-COM-006 — Cancelar Compra

### **Fluxo Principal**
1. Comprador acessa compra.  
2. Se elegível para cancelamento:
   - Sistema exibe botão **Cancelar**.  
3. Sistema revoga ingresso.  
4. Atualiza estoque (se permitido).  
5. Gateway estorna pagamento (conforme regras).  

### **Regras de Negócio**
- RN-COM-008  

---

# 10.3. Casos de Uso — Vendedor

---

## UC-VEN-001 — Criar Evento

### **Fluxo Principal**
1. Vendedor abre painel de criação.  
2. Preenche:
   - Nome  
   - Descrição  
   - Data  
   - Local  
   - Categorias  
   - Imagens  
3. Sistema valida obrigatórios.  
4. Sistema salva rascunho.  
5. Vendedor cria lotes.  
6. Sistema libera opção **Publicar**.  

### **Fluxo Alternativo**
- Dados faltando → botão **Publicar** fica desabilitado.

---

## UC-VEN-002 — Editar Evento

1. Vendedor acessa evento.  
2. Edita os dados.  
3. Sistema valida.  
4. Sistema salva versão.  
5. Se já publicado, atualizações entram em *modo seguro*:
   - Não afetam quem já comprou  
   - Só acrescentam informações  

---

## UC-VEN-003 — Gerenciar Vendas

1. Vendedor abre dashboard.  
2. Sistema mostra:  
   - Total vendido  
   - Receita  
   - Lotes ativos  
3. Vendedor pode aplicar filtros.  

---

## UC-VEN-004 — Ajustar Lotes

### Regras importantes
- Estoque só aumenta.  
- Nunca reduz abaixo do vendido.  

---

## UC-VEN-005 — Monitorar Performance

- Dados atualizados via SSE (tempo real).  

---

# 10.4. Casos de Uso — Administrador

---

## UC-ADM-001 — Gerenciar Usuários

1. Admin visualiza lista de usuários.  
2. Pode:
   - Promover  
   - Bloquear  
   - Desabilitar vendedor  
3. Sistema registra auditoria.

---

## UC-ADM-002 — Moderação

- Pode remover conteúdo inadequado.  
- Pode suspender evento irregular.

---

## UC-ADM-003 — Configurações Globais

- Define regras de:
  - Limites  
  - Cancelamentos  
  - Pagamentos  
  - Registros fiscais  

---

## UC-ADM-004 — Auditoria

- Acesso completo a logs estruturados e trilhas.

---

# 11. Mapeamento UC → Regras de Negócio

| Caso de Uso | Regras Relacionadas |
|-------------|---------------------|
| UC-VIS-001 | RN-VIS-001, RN-VIS-003 |
| UC-VIS-002 | RN-VIS-004 |
| UC-VIS-003 | RN-VIS-003 |
| UC-COM-001 | RN-COM-001 |
| UC-COM-002 | RN-COM-001 |
| UC-COM-003 | RN-COM-002, RN-COM-003, RN-COM-004 |
| UC-COM-004 | RN-COM-005, RN-COM-006, RN-COM-010 |
| UC-COM-005 | RN-COM-007 |
| UC-COM-006 | RN-COM-008 |
| UC-VEN-001 | RN-VEN-001 |
| UC-VEN-002 | RN-VEN-001 |
| UC-VEN-003 | RN-VEN-004 |
| UC-VEN-004 | RN-VEN-005 |
| UC-VEN-005 | RN-VEN-004 |
| UC-ADM-001 | RN-ADM-002 |
| UC-ADM-002 | RN-ADM-005 |
| UC-ADM-003 | RN-ADM-001 |
| UC-ADM-004 | RN-ADM-004 |

---

# FIM DA PARTE 5  
Quer seguir pra **Parte 6** (Fluxos Alternativos e Exceções Detalhadas por Papel + Pré/Pós Condições Profundas)?  
Só mandar um **"bora parte 6"**!
