# 📘 Parte 7 — Regras de Negócio do Participante (Comprador)  
Documento de Requisitos Funcionais — Versão Consolidada (Waterfall)

---

# 7. REGRAS DE NEGÓCIO — PARTICIPANTE (COMPRADOR)

As regras abaixo definem o comportamento esperado para todas as ações do papel **Participante → Comprador**, abrangendo cadastro, compras, pagamentos, relacionamento com vendedor, pós-venda e segurança.

---

## 7.1. Cadastro, Conta e Perfil

### **RN-P1 — Obrigatoriedade de Cadastro Completo**
- Para finalizar uma compra, o usuário deve ter:
  - Nome completo  
  - E-mail validado  
  - CPF válido  
  - Endereço completo (quando o item exigir entrega física)  
  - Ao menos um método de pagamento ativo  
- O sistema impede compras caso algum dos itens acima esteja ausente.

### **RN-P2 — Validação de E-mail**
- A conta do usuário só se torna “ativa para compra” após confirmação de e-mail.
- Links de confirmação expiram em 24h.
- Após expiração, o usuário pode solicitar novo link.

### **RN-P3 — Edição de Dados do Perfil**
- O participante pode editar:
  - Nome  
  - Telefone  
  - Endereço  
  - Imagem de perfil  
- Não pode editar:
  - CPF  
  - E-mail (somente via processo de troca com nova validação)

### **RN-P4 — Inativação de Conta**
- O participante pode inativar a própria conta desde que:
  - Não existam pedidos em andamento
  - Não existam disputas abertas
- Dados continuam armazenados pelo prazo legal.

---

## 7.2. Navegação, Busca e Catálogo

### **RN-P5 — Acesso ao Catálogo**
- Qualquer participante autenticado pode visualizar todos os produtos permitidos pela política da plataforma.

### **RN-P6 — Filtros e Ordenações**
- O sistema deve permitir filtros por:
  - Categoria  
  - Preço  
  - Avaliação do vendedor  
  - Localização do vendedor (quando aplicável)
- Ordenações disponíveis:
  - Menor preço  
  - Maior preço  
  - Mais vendidos  
  - Mais bem avaliados  
  - Lançamentos  

### **RN-P7 — Disponibilidade do Produto**
- Produtos fora de estoque:
  - Não podem ser adicionados ao carrinho
  - Devem indicar status "Indisponível"
- Produtos com estoque limitado devem exibir quantidade restante.

---

## 7.3. Carrinho e Pré-Compra

### **RN-P8 — Persistência do Carrinho**
- Itens adicionados ao carrinho devem persistir entre sessões.
- Carrinho associado ao usuário, não ao dispositivo.

### **RN-P9 — Validação de Estoque no Momento da Compra**
- Ao iniciar o checkout, o sistema deve validar:
  - Estoque atualizado  
  - Status do produto  
  - Preço atual (prevenção de inconsistências)

### **RN-P10 — Quantidade Máxima por Produto**
- O sistema deve permitir que o vendedor configure limites por compra.
- O carrinho deve respeitar o limite definido pelo vendedor.

---

## 7.4. Checkout e Pagamento

### **RN-P11 — Métodos de Pagamento Aceitos**
O comprador pode utilizar:
- Cartão de crédito  
- Cartão de débito (quando apoiado pelo provedor)  
- PIX  
- Saldo interno (se a plataforma possuir)  
- Boleto (opcional conforme operação)

### **RN-P12 — Autorização de Pagamento**
- A compra só é considerada confirmada após:
  - Autorização do provedor de pagamento  
  - Registro de “pedido criado” na plataforma  
- Em caso de falha de autorização, o pedido não é criado.

### **RN-P13 — Regras para PIX**
- QR code válido por até 15 minutos.
- Após expiração, deve ser possível gerar outro.

### **RN-P14 — Preço Final**
O preço final deve considerar:
- Valor do produto  
- Frete  
- Taxas aplicáveis  
- Descontos e cupons  
- Cashback (se existir)

### **RN-P15 — Cancelamento Automático**
Pedidos são cancelados automaticamente quando:
- Boleto não é pago no prazo  
- PIX não é liquidado  
- Autorização do cartão expira

---

## 7.5. Pedidos, Entregas e Acompanhamento

### **RN-P16 — Status do Pedido**
Os estados possíveis são:
1. Aguardando Pagamento  
2. Pagamento Confirmado  
3. Em Processamento  
4. Enviado  
5. Entregue  
6. Concluído  
7. Cancelado  
8. Em Disputa

### **RN-P17 — Rastreamento**
- O participante pode acompanhar a entrega em tempo real via:
  - SSE (eventos de atualização)
  - WebSocket (tempo real quando disponível)
- Alterações devem ser registradas no histórico do pedido.

### **RN-P18 — Confirmação de Entrega**
- O comprador pode confirmar manualmente a entrega.
- A entrega também é confirmada automaticamente quando:
  - Operadora logística informa “entregue”
  - Passado o prazo máximo definido (configurável)

---

## 7.6. Cancelamentos, Devoluções e Disputas

### **RN-P19 — Cancelamento pelo Comprador**
Permitido quando:
- Pedido ainda está em processamento  
- Pedido ainda **não foi enviado**

### **RN-P20 — Solicitação de Devolução**
- A devolução pode ser solicitada em até X dias após entrega (configurável).
- O sistema deve exigir justificativa e fotos opcionais.

### **RN-P21 — Regras de Disputa**
- Toda disputa deve:
  - Ter justificativa obrigatória  
  - Ser registrada com timestamp  
  - Ter canal de interação comprador ↔ vendedor  
- Administrador pode intervir a qualquer momento.

---

## 7.7. Avaliações e Relacionamento com Vendedores

### **RN-P22 — Avaliação do Produto**
- Comprador só pode avaliar após a entrega.
- Avaliações incluem:
  - Nota (1 a 5)  
  - Comentário (opcional)  
  - Anexos (até 3 imagens)

### **RN-P23 — Avaliação do Vendedor**
- A avaliação do vendedor deve ser calculada a partir da média das últimas avaliações válidas.

### **RN-P24 — Denúncias**
- O comprador pode denunciar:
  - Produto irregular  
  - Comportamento inadequado do vendedor  
  - Conteúdo ofensivo
- Denúncias são encaminhadas à moderação.

---

## 7.8. Segurança, Fraude e Privacidade

### **RN-P25 — Proteção contra Compras Suspeitas**
- Compras classificadas como risco elevado devem:
  - Entrar em auditoria automática  
  - Ser retidas até análise manual  
  - Ter notificação ao usuário

### **RN-P26 — Dados Sensíveis**
- CPF, dados de pagamento e endereço devem ser ocultados sempre que possível nas telas internas.
- Logs nunca podem armazenar dados completos de cartões.

### **RN-P27 — Tentativas de Acesso Indevido**
- Bloqueio temporário após múltiplas falhas de validação de pagamento ou autenticação.

---

## 7.9. Notificações

### **RN-P28 — Notificações Obrigatórias**
O comprador deve receber notificações sobre:
- Criação do pedido  
- Pagamento aceito ou recusado  
- Pedido enviado  
- Pedido entregue  
- Mensagens em disputas  
- Problemas com pagamento  

### **RN-P29 — Múltiplos Canais**
Notificações podem ocorrer por:
- E-mail  
- Painel interno  
- Push (quando ativado)  

---

## 7.10. Regras de Expiração, Histórico e Registros

### **RN-P30 — Histórico de Pedidos**
- O comprador deve visualizar todo o histórico de compras.

### **RN-P31 — Retenção de Dados**
- Pedidos devem ser mantidos por prazo legal mesmo após exclusão da conta.

### **RN-P32 — Logs de Auditoria**
- Toda ação financeira ou sensível deve gerar log imutável para auditoria.

---

# ✔ Encerramento da Parte 7

Esta parte encerra as regras de negócio completas referentes ao papel **Participante — Comprador**, mantendo compatibilidade com engenharia de requisitos tradicional, futura modelagem ágil e geração de backlog.

