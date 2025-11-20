# 📘 Documento Funcional Detalhado — Parte 6  
## Sistema de Eventos — Fluxos Alternativos + Exceções + Pré/Pós-Condições (Detalhados)  
### **(Consolidado — Seção 6 de N)**

---

# 12. Fluxos Alternativos e Exceções (Profundos)

Esta seção expande o nível de detalhamento dos fluxos de cada caso de uso, incluindo:

- Caminhos não triviais  
- Estados intermediários  
- Regras de concorrência  
- Erros sistêmicos  
- Erros do usuário  
- Interrupções inesperadas  
- Recuperações  
- Comportamentos offline/degradação  

---

# 12.1. Visitante — Fluxos Alternativos e Exceções Detalhados

---

## UC-VIS-001 — Consultar Lista de Eventos

### **Fluxos Alternativos Detalhados**

#### **FA1 — Falha na API de consulta**
1. Ao tentar carregar a lista, o backend retorna erro 5xx.
2. Sistema exibe um placeholder indicando “Não foi possível carregar a lista de eventos”.
3. Sistema agenda uma nova tentativa automática:
   - Delay progressivo (5s → 10s → 30s → desistência).
4. Visitante pode tentar atualizar manualmente clicando em “Recarregar”.

#### **FA2 — Catálogo vazio**
1. Backend retorna lista vazia.  
2. Sistema exibe mensagem: “Nenhum evento encontrado”.

#### **FA3 — Filtros inválidos**
1. Visitante insere filtros combinados impossíveis (ex: preço mínimo > máximo).  
2. Sistema:
   - Exibe ajuste automático sugerido  
   - Aplica filtro valido  
   - Mostra aviso ao visitante.

### **Fluxos de Exceção**

#### **EX1 — Perda de Conexão**
1. Sistema identifica ausência de resposta do servidor.
2. Tenta reconectar silenciosamente.
3. Caso não recupere:
   - Exibe banner: “Conexão perdida. Tentando reconectar...”.

#### **EX2 — Inconsistência de dados recebidos**
1. Backend retorna campos faltantes.
2. UI exibe fallback padrão:
   - “Informação indisponível”.
   - Oculta elementos dependentes.

---

## UC-VIS-002 — Visualizar Detalhes do Evento

### **Fluxos Alternativos Estendidos**
#### **FA1 — Evento publicado parcialmente**
1. Algumas seções do evento ainda estão sendo editadas.  
2. Sistema exibe:
   - Seções completas normalmente  
   - Seções incompletas com placeholder “Informação em atualização”.

#### **FA2 — Evento com imagens indisponíveis**
- Exibe placeholder neutro + botão “Tentar novamente”.

### **Fluxos de Exceção**
#### **EX1 — Evento despublicado durante acesso**
1. Visitante abre tela → status muda no backend.
2. SSE/WebSocket envia notificação de despublicação.
3. UI apresenta modal:
   > “Este evento foi desativado e não está mais disponível.”

---

# 12.2. Comprador — Fluxos Alternativos e Exceções Detalhados

---

## UC-COM-003 — Adicionar Ingressos ao Carrinho

### **Fluxos Alternativos Estendidos**

#### **FA1 — Estoque alterado durante a seleção**
1. Comprador seleciona 4 ingressos.  
2. Antes da validação, backend atualiza lote (ex: só restam 2).  
3. Sistema responde:
   - Ajusta automaticamente a quantidade para 2.  
   - Exibe aviso: “A quantidade foi ajustada devido à alta demanda.”

#### **FA2 — Limite por usuário atingido**
1. Comprador tenta adicionar ingressos acima do limite.
2. Sistema exibe:
   > “Você já atingiu o limite máximo permitido para este lote.”

#### **FA3 — Expediente crítico de eventos em alta demanda**
Quando lotes entram em "modo disputa":
- Carrinho reserva por 5 minutos ao invés de 15  
- Permite autorefresh do estoque a cada 10s

### **Fluxos de Exceção**

#### **EX1 — Queda da reserva**
1. Sistema tenta reservar o lote, mas serviço de reserva falha.  
2. Usuário recebe:
   > “Não foi possível reservar seus ingressos. Tente novamente.”

#### **EX2 — Expiração da reserva**
1. Cronômetro chega a 0.  
2. UI remove ingressos automaticamente.  
3. Caso o usuário esteja na etapa de pagamento, exibe:  
   > “A reserva expirou. Revise os ingressos antes de continuar.”

---

## UC-COM-004 — Finalizar Compra

### **Fluxos Alternativos Detalhados**

#### **FA1 — Pagamento pendente com fallback**
1. Gateway não confirma imediatamente.  
2. Sistema coloca pedido em **status PENDENTE**.  
3. UI exibe mensagem de espera.  
4. Backend verifica callback por até 2 minutos.  
5. Se não houver retorno:
   - Marca compra como **FALHA TEMPORÁRIA**.
   - Libera estoque.

#### **FA2 — Gateway lento**
1. Backend aguarda 15s.  
2. Se exceder:
   - Sistema mostra “Estamos finalizando sua compra…” e mantém tela aberta.
   - Front continua com "spinner" até a resposta chegar via SSE.

#### **FA3 — Erro de anti-fraude**
1. Gateway retorna “suspeita de fraude”.
2. Sistema:
   - Bloqueia compra.
   - Notifica usuário.
   - Envia alerta ao Admin.

### **Fluxos de Exceção Profundos**

#### **EX1 — Perda de conexão durante pagamento**
1. Pagamento foi enviado mas front caiu.  
2. Sistema mantém estado na base.  
3. Ao usuário retornar:
   - UI consulta status da última compra.  
   - Retoma fluxo adequado:
     - **Sucesso** → mostra ingressos  
     - **Falha** → permite nova tentativa  
     - **Pendente** → exibe tela de acompanhamento  

#### **EX2 — Estoque negativo por corrida de concorrência**
_Embora evitado pelo design, documenta-se o comportamento exigido:_

1. Dois compradores tentam comprar o último ingresso.  
2. Serviço de estoque recebe requisições simultâneas.
3. Regra de concorrência impede a venda duplicada.  
4. Comprador que chegou depois recebe:  
   > “Esgotado enquanto você finalizava a compra.”

---

# 12.3. Vendedor — Fluxos Alternativos e Exceções Detalhados

---

## UC-VEN-002 — Editar Evento

### **Fluxos Alternativos Detalhados**

#### **FA1 — Atualização de preço com compras já realizadas**
1. Vendedor tenta reduzir preço de lote.  
2. Sistema aceita para novos compradores.  
3. Compradores antigos **não** são afetados (regra de RN-VEN-005).  

#### **FA2 — Tentativa de remover informações obrigatórias**
- Sistema bloqueia alteração.
- Exibe indicações de campos obrigatórios.

### **Fluxos de Exceção**

#### **EX1 — Evento em processamento crítico**
1. Evento está sofrendo ajustes automáticos (alta demanda).  
2. Vendedor recebe aviso:  
   > “A edição está temporariamente bloqueada devido ao alto tráfego.”

#### **EX2 — Imagem corrompida**
- Sistema recusa upload e informa erro técnico.

---

## UC-VEN-003 — Gerenciar Vendas

### **Fluxos Alternativos**

#### **FA1 — Filtros inválidos**
Sistema limpa os filtros e restabelece padrão.

#### **FA2 — Modo realtime indisponível**
1. SSE/WebSocket cai.  
2. UI exibe banner: “Modo ao vivo indisponível, carregando manualmente”.  
3. Dados passam a atualizar via polling a cada 30s.

---

# 12.4. Administrador — Fluxos Alternativos e Exceções

---

## UC-ADM-001 — Gerenciar Usuários

### **Fluxos Alternativos**

#### **FA1 — Tentativa de bloquear usuário que está realizando compra**
1. Admin pressiona “bloquear”.
2. Sistema verifica se há compra em andamento.
3. Se houver:
   - Bloqueio adiado até término.
   - Exibe: “Ação aguardando finalização”.

### **Fluxos de Exceção**

#### **EX1 — Falha na auditoria**
1. Sistema tenta registrar operação.
2. Auditoria offline.
3. Operação é mantida, mas registrada para retry automático.

---

# 13. Pré-Condições e Pós-Condições (Niveladas e Estruturadas)

---

## 13.1. Pré-Condições Globais

- Sistema deve estar operacional.  
- Banco de dados acessível.  
- Serviço de estoque em modo consistente.  
- Gateway de pagamento ativo (para fluxos de compra).  
- Serviços de reserva funcionando.  
- Telecom SSE/WebSocket disponível.  

---

## 13.2. Pós-Condições Globais

- Todas as ações persistem logs de auditoria.  
- Eventos críticos disparam notificações SSE.  
- Consistência eventual garantida para dashboards.  
- Carrinho não mantém itens expirados.  

---

## 13.3. Pré e Pós Condições por Papel

### Visitante
- **Pré**: permissão pública  
- **Pós**: nenhum dado persistido  

### Comprador
- **Pré**: login, conta ativa  
- **Pós**:  
  - Reservas criadas  
  - Pedidos registrados  
  - Ingressos emitidos  

### Vendedor
- **Pré**: conta com papel de vendedor  
- **Pós**:  
  - Eventos criados/editados  
  - Lotes atualizados  
  - Relatórios acessados  

### Administrador
- **Pré**: perfil admin  
- **Pós**:  
  - Usuários gerenciados  
  - Eventos moderados  
  - Configurações aplicadas  

---

# 14. Regras de Concorrência e Sincronização

- Estoque controlado por **transações atômicas**.  
- Reserva sempre vence disputa sobre visualização.  
- Dois compradores não podem efetivar compra do mesmo ingresso.  
- SSE/WebSocket notifica:
  - Mudança de estoque  
  - Mudança de lote  
  - Publicação/despublicação de evento  

---

# FIM DA PARTE 6

Pronto, chefe! Quando quiser começar **Parte 7** (modelos de dados completos, tabelas, atributos, validações e relacionamentos), manda só um **“bora pra parte 7”**
