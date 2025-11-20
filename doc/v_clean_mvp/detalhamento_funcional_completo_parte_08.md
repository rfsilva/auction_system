# 📘 Parte 8 — Regras de Negócio do Vendedor  
Documento de Requisitos Funcionais — Versão Consolidada (Waterfall)

---

# 8. REGRAS DE NEGÓCIO — VENDEDOR

Esta seção descreve todas as regras de negócio relacionadas ao papel **Vendedor**, incluindo cadastro comercial, gestão de produtos, gerenciamento de pedidos, relacionamento com compradores, políticas de envio, disputas, reputação e segurança.

---

## 8.1. Cadastro e Habilitação do Vendedor

### **RN-V1 — Requisitos para se Tornar Vendedor**
Para se cadastrar como vendedor, o usuário deve fornecer:
- Dados pessoais válidos  
- Documentos fiscais: CPF e/ou CNPJ (quando aplicável)  
- Dados bancários para recebimento  
- Informações comerciais (nome da loja, descrição, categoria)  

### **RN-V2 — Validação de Documentos**
- O vendedor só pode publicar produtos após validação documental.
- Validação pode ser automática ou manual dependendo das regras da plataforma.

### **RN-V3 — Status do Vendedor**
O vendedor pode ter os seguintes status:
1. **Pendente** – aguardando validação  
2. **Ativo** – autorizado a vender  
3. **Suspenso** – impedido de vender por infração  
4. **Bloqueado** – remoção definitiva  

### **RN-V4 — Dados Bancários**
- Alterações de dados bancários exigem autenticação reforçada.
- Alterações não afetam pagamentos já programados.

---

## 8.2. Cadastro, Edição e Manutenção de Produtos

### **RN-V5 — Informações Obrigatórias do Produto**
Cada produto deve conter:
- Título  
- Categoria  
- Descrição detalhada  
- Valor  
- Quantidade em estoque  
- Imagens (mínimo 1)  
- Peso e dimensões (para cálculo de frete quando aplicável)

### **RN-V6 — Revisão de Conteúdo**
Produtos passam por verificação (automática ou manual) antes de aparecerem no catálogo.

### **RN-V7 — Alterações em Produtos**
- Produtos com pedidos associados:
  - Não podem ter preço alterado para pedidos já realizados.
- Estoque pode ser atualizado a qualquer momento.
- Alterações são refletidas imediatamente no catálogo.

### **RN-V8 — Produtos Proibidos**
O sistema deve impedir cadastro de itens em categorias bloqueadas pela política da plataforma (ex.: ilegais, adultos, medicamentos sem licença, etc.).

---

## 8.3. Gestão de Estoque

### **RN-V9 — Estoque Mínimo**
O vendedor pode definir:
- Estoque total  
- Estoque mínimo para alertas  
- Limite de compra por comprador

### **RN-V10 — Indisponibilidade**
Quando estoque chega a zero:
- Produto deve ser marcado automaticamente como "Indisponível"
- Produto não pode ser adicionado ao carrinho por compradores

---

## 8.4. Pedidos: Recebimento, Processamento e Expedição

### **RN-V11 — Aceite de Pedido**
Assim que o pagamento é confirmado:
- O pedido é exibido ao vendedor como **"Aguardando Processamento"**

### **RN-V12 — Tempo Máximo de Processamento**
Cada pedido deve ser processado em até **X horas/dias** (configurável).
- Após este prazo, o comprador pode solicitar cancelamento automático.

### **RN-V13 — Preparação e Embalagem**
O vendedor deve:
- Preparar o produto  
- Embalar conforme padrões da plataforma  
- Atualizar status para **"Em Processamento"**

### **RN-V14 — Geração de Etiqueta de Envio**
- O sistema gera automaticamente a etiqueta.
- A etiqueta deve ser usada para postagem.
- Tentativas de reutilizar etiquetas são bloqueadas.

### **RN-V15 — Confirmação de Envio**
O vendedor deve registrar:
- Data de envio  
- Código de rastreio (quando aplicável)

---

## 8.5. Entrega e Pós-Venda

### **RN-V16 — Acompanhamento**
O vendedor tem acesso ao progresso da entrega via:
- SSE (eventos push)  
- WebSocket (modo tempo real)  
- Painel administrativo

### **RN-V17 — Prazo de Entrega**
O vendedor deve cumprir o prazo informado pelo cálculo da plataforma.

### **RN-V18 — Comprovação de Entrega**
O sistema aceita:
- Dados da operadora logística  
- Confirmação manual do comprador  
- Comprovação enviada pelo vendedor (em caso de disputa)

---

## 8.6. Cancelamentos, Devoluções e Disputas

### **RN-V19 — Cancelamento pelo Vendedor**
Permitido apenas quando:
- Produto está indisponível  
- Problema operacional impede envio  
- Comprador solicita e concorda

### **RN-V20 — Solicitações de Devolução**
O vendedor deve responder em até **X dias**:
- Aceitar devolução  
- Solicitar mais informações  
- Oferecer proposta alternativa (quando permitido)

### **RN-V21 — Resolução de Disputas**
Toda disputa deve registrar:
- Mensagens trocadas  
- Evidências enviadas  
- Prazos cumpridos  
- Decisão final do administrador

---

## 8.7. Comunicação com Compradores

### **RN-V22 — Canais de Comunicação**
Comunicação deve ocorrer exclusivamente por:
- Chat interno da plataforma  
- Mensagens registradas  
- Sem compartilhamento de dados pessoais como:
  - Telefone  
  - E-mail  
  - Redes sociais  

### **RN-V23 — Conduta Obrigatória**
O vendedor deve manter:
- Linguagem respeitosa  
- Clareza nas respostas  
- Cumprimento aos prazos  
O sistema pode registrar denúncias por:
- Linguagem ofensiva  
- Ameaças  
- Venda enganosa

---

## 8.8. Reputação e Avaliações

### **RN-V24 — Cálculo de Reputação**
A reputação do vendedor é calculada por:
- Média de avaliações (últimos 180 dias)  
- Taxa de entregas no prazo  
- Taxa de cancelamentos por falhas  
- Abertura de disputas  
- Qualidade das respostas no chat

### **RN-V25 — Penalidades por Baixa Reputação**
Dependendo do score:
- Redução da visibilidade dos produtos  
- Suspensão temporária  
- Investigação manual  
- Bloqueio permanente em casos graves  

---

## 8.9. Pagamentos ao Vendedor

### **RN-V26 — Liberação de Valores**
Valores só são liberados:
- Após confirmação de entrega  
- Ou após prazo de contestação expirar

### **RN-V27 — Tarifas da Plataforma**
A plataforma aplica:
- Comissão por venda  
- Taxas de pagamento  
- Tarifas logísticas (quando aplicável)

Valores devem ser exibidos de forma clara ao vendedor antes de publicar o produto.

### **RN-V28 — Saques**
- Saques seguem calendário definido pela plataforma.
- Saques manuais podem exigir autenticação reforçada.

---

## 8.10. Segurança e Compliance

### **RN-V29 — Ações Suspeitas**
O sistema deve analisar:
- Alterações de preço repentinas  
- Aumento suspeito de cancelamentos  
- Produtos com denúncias recorrentes  
- Padrões de fraude

### **RN-V30 — Autenticação Reforçada**
Exigida para:
- Alteração de dados bancários  
- Solicitação de saques  
- Exclusão de produtos com pedidos ativos  

### **RN-V31 — Armazenamento de Evidências**
O sistema deve guardar:
- Registros de envio  
- Rastreamentos  
- Comprovantes de postagem  
- Mensagens de disputa

---

## 8.11. Notificações ao Vendedor

### **RN-V32 — Notificações Obrigatórias**
O vendedor deve ser notificado sobre:
- Novo pedido  
- Pagamento confirmado  
- Prazo de expedição prestes a vencer  
- Mensagens do comprador  
- Disputas abertas  
- Avaliações recebidas  
- Penalidades ou suspensões  

### **RN-V33 — Canais Disponíveis**
- Painel interno  
- E-mail  
- Push (quando ativado)

---

# ✔ Encerramento da Parte 8
Esta parte encerra todas as regras de negócio relacionadas ao papel **Vendedor**, garantindo alinhamento com engenharia de requisitos clássica e futura integração com backlog ágil.

