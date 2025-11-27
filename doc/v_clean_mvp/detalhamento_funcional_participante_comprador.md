# 📘 Detalhamento Funcional — Participante → Comprador  
**Documento funcional detalhado (MVP + visão estendida)**  
**Versão:** 1.0  
**Papel:** Usuário autenticado com permissão para participar de leilões e realizar compras.

---

# 1. 📌 Identidade do Papel
O **Participante / Comprador** é o usuário autenticado que:

- Realiza lances.
- Interage com produtos e lotes.
- Acompanha leilões em tempo real.
- Pode arrematar itens.
- Efetua pagamento dos itens arrematados.
- Recebe notificações sobre eventos relevantes.
- Mantém seu perfil, documentos e dados cadastrais atualizados.

Ele é o principal ator da etapa comercial do leilão, sendo essencial garantir clareza, transparência, segurança e rastreabilidade das ações realizadas.

---

# 2. 🎯 Atribuições Gerais do Comprador

O Comprador pode:

- Navegar em lotes e produtos.
- Visualizar informações públicas e restritas.
- Acompanhar disputas em tempo real (via SSE ou WebSocket).
- Enviar lances (manuais).
- Confirmar arremates e efetuar pagamento.
- Gerenciar informações pessoais e documentos.
- Receber e consultar notificações.
- Configurar preferências (listas de interesse, alertas, etc.).

---

# 3. 🧭 Funcionalidades Detalhadas

Abaixo estão todas as funcionalidades divididas por áreas lógicas.

---

# 3.1. ✨ Cadastro e Perfil do Comprador

## Funcionalidades:
- Atualizar informações básicas (nome, telefone, endereço).
- Atualizar e validar documentos pessoais (RG/CPF).
- Enviar documentos obrigatórios quando solicitado.
- Configurar preferências de notificação:
  - E-mail
  - SMS (opcional futuro)
  - Push no navegador (opcional futuro)

## Regras de Negócio:
- Documentos enviados devem passar por validação manual ou automática.
- O comprador só participa de leilão após aprovação documental, se o leilão exigir.
- Alterações de dados sensíveis devem gerar log de auditoria.

---

# 3.2. 🔍 Navegação e Exploração de Lotes e Produtos

## Funcionalidades:
- Listar lotes:
  - Ativos
  - Futuros
  - Encerrados (para consulta histórica)
- Listar produtos de um lote.
- Ver detalhes de lote:
  - Nome
  - Descrição
  - Data/hora de abertura e encerramento
  - Taxas aplicáveis
  - Vendedor associado
- Ver detalhes do produto:
  - Imagens
  - Descrição técnica
  - Preço inicial
  - Histórico de lances
  - Lances atuais
  - Regras específicas do produto (ex.: encerramento antecipado)
  - Status atual (Em disputa / Arrematado / Encerrado)

## Regras de Negócio:
- Produtos podem ter encerramento diferente do lote (antes do lote).
- Informações exibidas devem respeitar status atual.
- Dados de preço e lances devem ser atualizados em tempo real.

---

# 3.3. 📡 Acompanhamento em Tempo Real (SSE/WebSocket)

## Funcionalidades:
O comprador deve ser capaz de ver:

- Incremento de lances.
- Mudança de status do produto.
- Avisos de encerramento iminente.
- Outbid (“Você foi superado”).

## Regras de Negócio:
- Todos os eventos devem ser empurrados para o cliente automaticamente.
- Em caso de falha de conexão:
  - O sistema deve tentar reconectar automaticamente.
- Para produtos encerrados:
  - O canal de atualizações deve ser finalizado.

---

# 3.4. 💸 Envio de Lances

## Funcionalidades:
- Enviar lance manual.
- Visualizar o valor mínimo necessário para o próximo lance.
- Receber confirmação imediata de aceitação do lance.
- Visualizar feedback de erro em tempo real:
  - Lance abaixo do mínimo.
  - Produto encerrado.
  - Usuário não habilitado.
  - Saldo ou documentação pendente (se aplicável).

## Regras de Negócio:
- O lance deve ser maior que o lance atual + incremento mínimo.
- O usuário deve estar autenticado e habilitado.
- Nenhum lance pode ser aceito após a data/hora de encerramento do produto.
- Todos os lances devem ser registrados com:
  - horário do servidor (UTC)
  - identificação do comprador
  - valor
  - ID do produto
- Rebates ou correções não são permitidos no MVP.
- Em caso de empate de timestamp (tecnicamente raro):
  - prevalece o lance que o sistema gravou primeiro (ordem natural do banco).

---

# 3.5. 🏆 Arremate

## Funcionalidades:
- O comprador deve visualizar:
  - Quando arrematou um produto.
  - Quando perdeu um produto.
  - Quando foi superado.
- Acesso à tela de pagamento dos itens arrematados.
- Confirmar aceite das regras antes do pagamento.
- Visualizar:
  - Valor final do arremate
  - Taxas aplicáveis
  - Frete estimado (via API externa)
  - Totalização completa

## Regras de Negócio:
- O arremate é atribuído ao maior lance válido no instante do encerramento.
- Um arremate só é definitivo após:
  - Registro no backend
  - Comunicação para o comprador
- Se o comprador não pagar no prazo definido:
  - Penalidades podem ser aplicadas (regra opcional)
  - O item pode ir para “segunda colocação” (futuro)
- É obrigatório exibir todas as taxas antes da confirmação de pagamento.

---

# 3.6. 💳 Pagamento

## Funcionalidades:
- Exibir métodos de pagamento suportados:
  - Pix (MVP)
  - Boleto (futuro)
  - Cartão (futuro)
- Gerar QR Code ou chave Pix.
- Gerar comprovante.
- Exibir prazo limite de pagamento.

## Regras de Negócio:
- O pagamento deve ser reconciliado automaticamente:
  - via callback,
  - ou via polling no backend (se necessário).
- Após pagamento confirmado:
  - o status do arremate muda para “Pago”.
- Pagamentos fora do prazo ficam como “Expirado”.

---

# 3.7. 📦 Pós-Arremate / Logística

## Funcionalidades:
- Visualizar status da entrega:
  - Aguardando envio
  - Em transporte
  - Entregue
- Consultar cálculo de frete (API externa).
- Acompanhar documento fiscal (se aplicável).
- Baixar comprovantes.

## Regras de Negócio:
- O frete deve ser calculado:
  - por CEP + peso/medidas do produto.
- Caso o vendedor faça logística própria:
  - o front deve indicar isso claramente.
- O comprador deve sempre ter acesso ao histórico.

---

# 3.8. 🔔 Notificações

## Funcionalidades:
- Visualizar notificações na interface.
- Marcar como lida.
- Receber notificações por e-mail:
  - Outbid
  - Arremate
  - Lembrete de pagamento
  - Alterações de status

## Regras de Negócio:
- Notificações críticas devem ser enviadas por e-mail obrigatoriamente.
- Notificações no sistema não devem expirar.
- Devem existir categorias:
  - Sistema
  - Financeiro
  - Logística
  - Leilão

---

# 3.9. 📜 Histórico e Auditoria

## Funcionalidades:
- Visualizar histórico de:
  - lances enviados
  - lances vencidos
  - arremates
  - pagamentos
  - notificações recebidas

## Regras de Negócio:
- Todo registro deve incluir timestamp UTC.
- Usuário só enxerga seus próprios registros.
- Histórico deve suportar filtros:
  - por período
  - por lote
  - por produto

---

# 4. 🔒 Restrições do Papel Comprador

O Comprador **NÃO PODE**:

- Criar ou editar lotes.
- Criar ou editar produtos.
- Editar taxas.
- Aprovar documentos de outros usuários.
- Gerenciar vendedores.
- Gerenciar operações administrativas.

Essas permissões são exclusivas do Administrador e/ou Vendedor.

---

# 5. 🧱 Requisitos Não Funcionais Associados ao Comprador

- **Disponibilidade:** 99% para telas públicas de leilão.
- **Performance:**
  - Atualizações em tempo real devem chegar < 1s após evento.
- **Segurança:**
  - Todos os lances devem ser enviados com token válido.
  - Antifraude simples: rate limit por usuário.
- **Escalabilidade:**
  - Mecanismo de SSE/WebSocket deve suportar thousands de conexões simultâneas.
- **Auditoria completa** para todos os eventos de compra e lance.

---

# 6. 📌 Resumo Executivo

O papel de Comprador é um dos mais sensíveis do sistema.  
É ele quem:
- gera receita,  
- movimenta o leilão,  
- interage com todos os componentes críticos.

Por isso este detalhamento cobre tanto ações diretas (dar lance, pagar) quanto indiretas (receber notificação, ver atualizações em tempo real, validar perfil).

---

**Se quiser agora, posso gerar as histórias funcionais derivadas deste documento, o mapeamento para épicos, ou começar a documentação do Vendedor ou Administrador.**
