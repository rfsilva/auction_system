# 📘 Detalhamento Funcional — Papel: **Vendedor**
**Versão:** 1.0  
**Contexto:** O Vendedor é a entidade responsável por cadastrar produtos, criar lotes, acompanhar disputas, visualizar relatórios e gerenciar informações comerciais e documentais relacionadas aos leilões em que ele é o ofertante.

---

# 1. 🧠 Identidade do Papel
O **Vendedor** é um usuário autenticado e aprovado pela plataforma que oferece produtos para disputa nos leilões.  
Ele pode:

- Cadastrar produtos (completo, incluindo documentação e fotos).
- Criar lotes.
- Configurar regras de encerramento.
- Acompanhar a dinâmica do leilão.
- Ver lances recebidos.
- Ver compradores vencedores.
- Gerar documentos operacionais e fiscais (quando aplicável).
- Interagir com a plataforma para resolver pendências.
- Gerenciar seu perfil, documentos e contratos comerciais.

Ele **NÃO** participa da disputa de lances e **NÃO** visualiza dados privados dos compradores.

---

# 2. 🎯 Atribuições Gerais do Vendedor

O Vendedor é responsável por:

- Oferta dos produtos.
- Veracidade das informações cadastradas.
- Conformidade documental.
- Disponibilidade, integridade e entrega dos itens arrematados.
- Relação comercial com a plataforma (taxas, prazos, políticas).

A plataforma deve fornecer ferramentas de gestão claras, rastreáveis e seguras.

---

# 3. 🧭 Funcionalidades Detalhadas

As funcionalidades a seguir estão agrupadas de forma lógica para apoiar a construção do sistema e a futura derivação de histórias.

---

# 3.1. 🪪 Cadastro, Perfil e Documentação do Vendedor

## Funcionalidades
- Atualizar informações cadastrais:
  - Razão social / nome
  - CNPJ / CPF
  - Endereço completo
  - Contato comercial
  - Dados bancários (para repasse)
- Upload de documentos:
  - Contratos
  - Certidões (quando aplicável)
  - Termos obrigatórios
- Assinatura/aceite digital do contrato com a plataforma.
- Acompanhar status de aprovação cadastral.

## Regras de Negócio
- O vendedor só pode publicar produtos/lotes após aprovação administrativa.
- Dados bancários devem passar por validação:
  - Titularidade
  - Formato
  - Tipo de conta
- Toda alteração sensível exige auditoria:
  - Dados bancários
  - Documento fiscal
  - Jurídico

---

# 3.2. 📦 Cadastro e Gerenciamento de Produtos

## Funcionalidades
- Criar novo produto com:
  - Título
  - Descrição detalhada
  - Categoria
  - Características técnicas
  - Estado de conservação
  - Peso/dimensões (para frete)
  - Fotos (múltiplas)
  - Documentos anexos
  - Preço inicial
  - Incremento mínimo de lance
  - Data/hora de encerramento individual (independente do lote)

- Editar produto enquanto ainda não publicado.
- Excluir produto ainda não vinculado a lote.
- Gerar versão prévia do anúncio.
- Visualizar situação atual:
  - Publicado / Não publicado / Em validação

## Regras de Negócio
- Produto só pode ser publicado se todas as informações obrigatórias estiverem preenchidas.
- Produtos publicados só podem sofrer edição restrita:
  - Fotos adicionais
  - Descrição complementar
  - Correções ortográficas
  - *Nunca alterar informações que alterem a disputa*, como:
    - Preço inicial
    - Incremento mínimo
    - Horário de encerramento
- Um produto sempre pertence a **um lote**.
- O vendedor define o **encerramento individual** do produto, que pode ser anterior ao encerramento do lote.

---

# 3.3. 📦📦 Cadastro e Gestão de Lotes

## Funcionalidades
- Criar lote com:
  - Nome
  - Descrição
  - Data/hora de abertura
  - Data/hora de encerramento do lote
  - Lista de produtos
- Configurar taxas específicas de vendedor (somente leitura no MVP — taxa é definida por contrato).
- Editar lote enquanto não publicado.
- Publicar lote.

## Regras de Negócio
- Um lote pode ter vários produtos, cada um com seu próprio horário de encerramento.
- O lote fecha oficialmente quando:
  - Todos os produtos forem encerrados **OU**
  - A data/hora do lote chegar.
- Lote publicado não pode:
  - Inserir produtos
  - Remover produtos
  - Alterar horários de encerramento dos produtos
- Lote só pode ser cancelado por administrador.

---

# 3.4. 🎥 Acompanhamento do Leilão em Tempo Real

## Funcionalidades
- Acompanhar disputa dos produtos:
  - Valor atual
  - Histórico de lances
  - Número de compradores ativos
  - Notificações de encerramento
- Ver em tempo real:
  - Quem está liderando (apenas identificador genérico do comprador)
  - Evolução de lances

## Regras de Negócio
- Vendedor **não vê** dados pessoais dos compradores.
- Vendedor **não interfere** na disputa.
- A comunicação em tempo real pode ser por:
  - SSE
  - WebSocket

---

# 3.5. 🏆 Pós-Leilão — Resultados e Arremates

## Funcionalidades
- Acessar lista de produtos arrematados.
- Visualizar dados do comprador vencedor:
  - Nome
  - Documento (ocultado parcialmente)
  - Contato (somente quando permitido)
- Acessar comprovantes de pagamento (quando confirmado).
- Gerar relatórios de arremate.

## Regras de Negócio
- Dados sensíveis do comprador seguem LGPD:
  - Apenas informações estritamente necessárias.
- O vendedor só vê o comprador após pagamento confirmado.
- O vendedor pode:
  - Marcar item como "enviado"
  - Informar dados de rastreio
- Alterações pós-arremate devem gerar auditoria.

---

# 3.6. 💰 Financeiro do Vendedor

## Funcionalidades
- Visualizar:
  - Vendas realizadas
  - Taxas aplicadas (taxa do leiloeiro)
  - Totais líquidos
- Acompanhar repasses financeiros.
- Download de extratos.
- Consultar pendências de pagamento de compradores.

## Regras de Negócio
- Taxa do leiloeiro:
  - Definida por contrato da plataforma com o vendedor.
  - Não alterável pelo vendedor no sistema.
- O repasse só é liberado após:
  - Confirmação de pagamento pelo comprador
  - Prazos contratuais

---

# 3.7. 📮 Logística e Entrega

## Funcionalidades
- Consultar cálculo automático de frete via API externa.
- Atualizar status de entrega.
- Anexar comprovantes (nota fiscal, comprovante físico).
- Acompanhar comunicação logística (se existir).

## Regras de Negócio
- O frete é calculado com:
  - CEP do vendedor
  - CEP do comprador
  - Dimensões do produto
  - Peso
- O vendedor deve marcar o produto como enviado antes de informar rastreio.
- Após “entregue”, o status não pode ser revertido sem intervenção administrativa.

---

# 3.8. 🔔 Notificações e Comunicação

## Funcionalidades
- Receber notificações sobre:
  - Produtos aprovados ou rejeitados pela administração
  - Lotes publicados
  - Erros ou pendências no cadastro
  - Arremates realizados
  - Pagamentos realizados
  - Pendências logísticas

- Visualizar histórico de notificações.

## Regras de Negócio
- Notificações relevantes são enviadas também por e-mail.
- Histórico deve ser completo, sem expiração.

---

# 3.9. 📜 Auditoria e Histórico

## Funcionalidades
- Consultar histórico de:
  - Produtos cadastrados
  - Edições
  - Publicações
  - Arremates
  - Documentos enviados
  - Pagamentos recebidos
  - Movimentações de entrega

## Regras de Negócio
- Toda ação sensível gera log:
  - Alterações cadastrais
  - Atualizações financeiras
  - Correções de informações
  - Mudanças de status logístico

---

# 4. 🔒 Restrições do Vendedor

O Vendedor **não pode**:

- Dar lances em seus próprios produtos.
- Editar produtos/lotes após publicação (salvo alterações não críticas).
- Ver dados completos do comprador antes do pagamento.
- Alterar taxas.
- Encerrar produto manualmente.
- Alterar dados financeiros do comprador.
- Modificar regras do leilão (incremento, horários, etc.) após publicação.

---

# 5. 📌 Resumo Executivo
O Vendedor é o provedor de produtos no ecossistema do leilão.  
Ele precisa de:

- Ferramentas claras para cadastrar itens.
- Controle e transparência sobre a disputa.
- Visão financeira detalhada.
- Operação logística integrada.
- Processos auditados e estruturados.

Este documento serve como base para geração de histórias, derivação de épicos, testes funcionais e validação com stakeholders.

---

**Se quiser, posso gerar agora o mesmo nível de detalhamento para o Administrador**, ou podemos entrar já no refinamento de histórias do Vendedor.
