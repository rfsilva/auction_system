# 📘 Documento de Requisitos – PARTE 3  
## Use Cases, Regras, Fluxos, Integrações e Critérios de Aceite  
## Papel: **Vendedor**

---

# ## 3. Participante – Vendedor

O **Vendedor** é o usuário responsável por cadastrar, gerenciar e operar seus produtos dentro da plataforma. É também responsável por acompanhar vendas, organizar estoque, atender compradores via chat WebSocket, tratar devoluções e garantir a conformidade dos anúncios.  
Esta seção descreve **todos os requisitos funcionais, regras de negócio e critérios de aceite**, no estilo waterfall.

---

# ### 3.1. Atribuições Gerais do Vendedor

- Criar e gerenciar catálogo de produtos.
- Controlar estoque e preços.
- Definir regras de frete e logística.
- Acompanhar métricas de vendas.
- Operar pedidos (separação, envio, atualização).
- Atender compradores via chat (WebSocket).
- Criar promoções, cupons e campanhas.
- Gerenciar reputação e avaliações.
- Solicitar suporte, mediação e resolução de conflitos.
- Gerenciar dados fiscais e informações comerciais.

---

# ### 3.2. Use Cases Detalhados – Vendedor

---

## **UC-V01 – Registro e Aprovação do Vendedor**

### **Descrição**
Permite ao usuário solicitar habilitação como vendedor na plataforma.

### **Atores**
- Vendedor
- Administrador
- Sistema de Validação Fiscal

### **Pré-condições**
- Usuário possui conta ativa na plataforma.

### **Fluxo Principal**
1. Usuário solicita habilitação como vendedor.
2. Sistema solicita:
   - documentos fiscais (CPF/CNPJ)
   - informações bancárias
   - endereço comercial
   - termo de responsabilidade
3. Sistema valida informações automaticamente.
4. Aprovação automática ou manual (admin).
5. Perfil de vendedor é ativado.

### **Fluxos Alternativos**
- Dados fiscais inválidos → reprovação automática.
- Divergência cadastral → envio para análise manual.

### **Regras de Negócio**
- **RN-V01:** Validação fiscal obrigatória via API de governo.
- **RN-V02:** Dados bancários só podem ser usados após verificação de titularidade.
- **RN-V03:** Vendedor só pode anunciar após aprovação completa.

### **Critérios de Aceite**
- Cadastro concluído em no máximo 10 minutos.
- Mensagens claras em caso de pendência.
- Auditoria completa do processo.

---

## **UC-V02 – Cadastro de Produto**

### **Fluxo Principal**
1. Vendedor acessa painel de produtos.
2. Clica em “Criar novo”.
3. Informa:
   - título
   - descrição
   - fotos
   - categorias
   - variantes
   - preço
   - estoque
4. Produto entra em moderação automática.
5. Após aprovação, torna-se público.

### **Regras de Negócio**
- **RN-V04:** Mínimo de 1 imagem por item.
- **RN-V05:** Título entre 10 e 150 caracteres.
- **RN-V06:** Descrição obrigatória com mínimo de 50 caracteres.
- **RN-V07:** Preço não pode ser 0.
- **RN-V08:** Estoque não pode ser negativo.
- **RN-V09:** Produtos de categorias sensíveis exigem moderação manual.

### **Critérios de Aceite**
- Criação em < 5 segundos.
- Imagens otimizadas automaticamente.
- Notificação enviada ao vendedor após aprovação.

---

## **UC-V03 – Gerenciar Produtos Existentes**

### **Ações**
- Editar descrição, título, preço ou estoque.
- Pausar anúncio.
- Excluir produto.
- Duplicar produto.
- Configurar variantes.

### **Regras de Negócio**
- **RN-V10:** Produtos com pedidos em andamento não podem ter preço alterado.
- **RN-V11:** Exclusão só permitida se não houver pedidos pendentes.
- **RN-V12:** Alteração de estoque deve registrar histórico.

### **Critérios de Aceite**
- Atualizações devem refletir imediatamente no catálogo.
- Logs completos para auditoria.

---

## **UC-V04 – Controle de Estoque**

### **Fluxo**
1. Vendedor acessa painel de estoque.
2. Ajusta quantidades por produto e variante.
3. Sistema valida alterações.
4. Histórico é salvo.

### **Regras**
- **RN-V13:** Alterações em lote devem validar limites.
- **RN-V14:** Estoque não pode ser inferior ao número de pedidos abertos.

---

## **UC-V05 – Gestão de Pedidos**

### **Ações**
- Visualizar pedidos recebidos.
- Filtrar por status:  
  nova venda → separação → enviado → entregue → disputa.
- Imprimir etiquetas.
- Atualizar status manualmente (quando permitido).
- Solicitar coleta.
- Registrar código de rastreio.

### **Regras**
- **RN-V15:** Atualização manual só é permitida até o envio.
- **RN-V16:** Código de rastreio é obrigatório para marcar como enviado.
- **RN-V17:** Atrasos devem gerar alerta automático.

### **Critérios**
- Alteração de status refletida em SSE do comprador instantaneamente.
- Tempo máximo de resposta < 2 segundos.

---

## **UC-V06 – Atendimento via Chat (WebSocket)**

### **Descrição**
Canal direto entre vendedor e comprador para tirar dúvidas.

### **Fluxo**
1. Vendedor recebe notificação de mensagem.
2. Sistema abre canal WebSocket.
3. Mensagens são trocadas em tempo real.
4. Histórico é armazenado.

### **Regras**
- **RN-V18:** Histórico deve ser mantido por 12 meses.
- **RN-V19:** Vendedor não pode solicitar dados pessoais sensíveis no chat.
- **RN-V20:** Palavras ofensivas devem ser filtradas.

### **Critérios**
- Latência < 300ms.
- Reconexão automática do websocket.

---

## **UC-V07 – Promoções e Cupons**

### **Ações**
- Criar cupom (valor fixo, percentual, frete grátis).
- Criar campanhas por categoria.
- Configurar limites de uso.
- Definir regras de ativação (datas, quantidade mínima, etc.).

### **Regras**
- **RN-V21:** Cupom não pode deixar o valor final negativo.
- **RN-V22:** Uso máximo deve ser validado no backend.
- **RN-V23:** Promoções conflitantes devem ser rejeitadas.

---

## **UC-V08 – Avaliações e Reputação**

### **Fluxo**
1. Comprador envia avaliação.
2. Sistema modera conteúdo.
3. Vendedor pode responder.
4. Nota impacta reputação.

### **Regras**
- **RN-V24:** Vendedor não pode alterar avaliações.
- **RN-V25:** Respostas devem seguir regras de conduta.

---

## **UC-V09 – Resolução de Conflitos**

### **Descrição**
Mecanismo de mediação entre comprador e vendedor.

### **Fluxo**
1. Comprador abre disputa.
2. Vendedor recebe notificação.
3. Vendedor apresenta defesa e/ou proposta.
4. Mediação pode ser automática ou por administrador.

### **Regras**
- **RN-V26:** Prazo máximo para resposta: 48h.
- **RN-V27:** Faltas repetidas impactam reputação.
- **RN-V28:** Mediação desfavorável ao vendedor pode gerar penalidades.

---

## **UC-V10 – Relatórios e Métricas**

### **Ações**
- Extrato financeiro.
- Relatório de devoluções.
- Ranking de produtos.
- Taxa de conversão.
- SLA de atendimento.

### **Regras**
- **RN-V29:** Período máximo de consulta: 12 meses.
- **RN-V30:** Dados financeiros devem seguir normas LGPD e PCI-DSS.

---

## **UC-V11 – Gerenciamento de Dados Comerciais**

### **Ações**
- Atualizar dados fiscais.
- Atualizar dados bancários.
- Alterar endereço comercial.

### **Regras**
- **RN-V31:** Mudanças sensíveis exigem revalidação documental.
- **RN-V32:** Dados bancários só podem ser usados após validação via micropagamento.

---

# ### 3.3. Regras de Negócio Consolidado – Vendedor

| Código | Regra | Descrição |
|--------|--------|-----------|
| RN-V01 | Validação fiscal | Obrigatória via API oficial |
| RN-V02 | Verificação bancária | Antes de habilitar vendas |
| RN-V03 | Ativação | Só após aprovação |
| RN-V04 | Imagens | Mínimo 1 por item |
| RN-V05 | Título | 10 a 150 caracteres |
| RN-V06 | Descrição | >= 50 caracteres |
| RN-V07 | Preço | Não pode ser 0 |
| RN-V08 | Estoque | Não pode ser negativo |
| RN-V09 | Moderação sensível | Manual quando necessário |
| RN-V10 | Preço bloqueado | Não altera se houver pedidos |
| RN-V11 | Exclusão restrita | Não pode excluir com pedidos |
| RN-V12 | Log de estoque | Obrigatório |
| RN-V13 | Lotes | Validação necessária |
| RN-V14 | Estoque mínimo | >= pedidos ativos |
| RN-V15 | Status pedido | Atualização limitada |
| RN-V16 | Rastreamento | Obrigatório no envio |
| RN-V17 | Atrasos | Gera alerta |
| RN-V18 | Histórico chat | 12 meses |
| RN-V19 | Dados sensíveis | Proibidos |
| RN-V20 | Filtro ofensas | Obrigatório |
| RN-V21 | Cupom negativo | Proibido |
| RN-V22 | Limite de uso | Validado no backend |
| RN-V23 | Regras conflitantes | Rejeitadas |
| RN-V24 | Avaliações | Não podem ser alteradas |
| RN-V25 | Respostas | Devem seguir conduta |
| RN-V26 | Disputa | 48h para resposta |
| RN-V27 | Reputação | Falta gera penalidade |
| RN-V28 | Penalidades | Podem ser aplicadas | 
| RN-V29 | Relatórios | Janela de 12 meses |
| RN-V30 | Compliance | LGPD + PCI-DSS |
| RN-V31 | Revalidação | Mudanças sensíveis |
| RN-V32 | Micropagamento | Validação bancária |

---

# ### 3.4. Integrações – Vendedor

- Serviço de Autenticação
- Serviço de Produtos
- Serviço de Estoque
- Serviço de Pedidos
- Serviço de Fretes
- Serviço de Pagamentos
- Sistema de Mediação
- Sistema de Relatórios
- Serviço de Chat WebSocket
- Serviço de Notificações
- Sistema de Moderação
- Serviço de Validação Fiscal

---

# ### 3.5. Critérios de Aceite Globais – Vendedor

- Alterações de produtos e estoque devem ter impacto imediato no catálogo.
- Painel do vendedor deve carregar em < 2 segundos.
- Sistema deve impedir qualquer inconsistência entre estoque, pedidos e promoções.
- Chat deve ter latência < 300ms.
- Todas as ações relevantes devem ser auditadas.
- Dados fiscais e bancários devem seguir normas de segurança.
- Sistema deve alertar automaticamente sobre atrasos, baixa reputação, estoque crítico e pendências fiscais.

---

