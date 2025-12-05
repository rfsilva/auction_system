# 📘 Documento Funcional — Sistema de Leilão Eletrônico  
## Parte 1 — Introdução, Objetivo, Escopo e Contexto Geral

---

# 1. Introdução

Este documento descreve, de forma detalhada e estruturada, os requisitos funcionais do Sistema de Leilão Eletrônico.  
O documento segue o formato clássico de especificação funcional adotado em metodologias waterfall, contendo:

- descrição completa dos atores,
- casos de uso,
- regras de negócio,
- fluxos detalhados,
- integrações,
- critérios de aceite,
- visão geral do comportamento do sistema.

O objetivo é garantir clareza, rastreabilidade e entendimento profundo de todas as funcionalidades necessárias para construção do sistema, bem como apoiar o posterior trabalho de decomposição em épicos, histórias e refinamento para metodologia ágil.

---

# 2. Objetivo do Sistema

O Sistema de Leilão Eletrônico tem como objetivo:

- possibilitar que vendedores cadastrem produtos e lotes para disputa,
- permitir que compradores participem de leilões em tempo real,
- garantir transparência, segurança, lisura e rastreabilidade das disputas,
- prover uma experiência fluida de acompanhamento de preços, lances e encerramentos,
- registrar arremates, documentos e comprovantes,
- gerar notificações e fornecer meios de contato e acompanhamento,
- atender às regras de negócio específicas de leilão, taxa do leiloeiro, horários e políticas de disputa,
- permitir gestão administrativa completa da plataforma.

O sistema atende tanto um público geral (visitantes e compradores), quanto vendedores e administradores da plataforma.

---

# 3. Escopo Geral do Documento

Este documento contempla:

## 3.1. Funcionalidades Principais
- Cadastro, autenticação e validação de usuários (compradores e vendedores).
- Cadastro, gestão e publicação de produtos.
- Agrupamento de produtos em lotes.
- Definição de horários de início e encerramento (lote e produto).
- Disputa de leilões em tempo real (lances).
- Arremate e fechamento automático de leilão.
- Cálculo de frete via API externa.
- Aplicação de taxa do leiloeiro.
- Geração e armazenamento de documentos de arremate.
- Notificações (email/SMS/push).
- Área administrativa completa:
  - gestão de usuários,
  - gestão de produtos e lotes,
  - gestão de auditoria,
  - regras e parametrizações.

## 3.2. Atores do Sistema
- **Visitante**: Usuário não autenticado, com acesso limitado às informações.
- **Participante — Comprador**: Usuário autenticado, habilitado a dar lances e arrematar produtos.
- **Participante — Vendedor**: Usuário autenticado, responsável pelo cadastro e gestão de produtos e lotes.
- **Administrador**: Usuário privilegiado da plataforma, responsável pela gestão global e auditoria do sistema.

## 3.3. Funcionalidades Excluídas do MVP
- Pagamento integrado (gateway financeiro).
- Relatórios avançados (BI).
- MFE e BFF (removidos por decisão arquitetural).
- Marketplace paralelo.
- Funcionalidades premium ou assinaturas.

---

# 4. Visão Geral do Sistema

O sistema opera como uma plataforma de intermediação digital entre **vendedores**, que desejam leiloar produtos, e **compradores**, interessados em disputar estes itens.

Cada produto pertence a um vendedor e pode compor um lote, que organiza a disputa.

### 4.1. Fluxo de Leilão (Resumo)
1. O vendedor cadastra produtos.
2. O vendedor cria um lote e adiciona produtos.
3. Produtos e lotes recebem datas/horários de:
   - início de visualização,
   - início da disputa,
   - encerramento.
4. O administrador valida e publica (opcional, conforme regra).
5. Visitantes visualizam o catálogo.
6. Compradores autenticados participam da disputa.
7. Lances são processados em tempo real (via SSE/WebSocket).
8. Encerramento ocorre automaticamente:
   - por produto (horário próprio),
   - ou por lote (caso algum produto não tenha horário próprio).
9. O sistema determina o vencedor.
10. Documentos de arremate são gerados.
11. Notificações são enviadas.
12. Vendedor e comprador recebem orientações sobre entrega/frete.
13. Administrador monitora, audita e intervém quando necessário.

---

# 5. Premissas do Sistema

- Produtos devem pertencer a um vendedor válido.
- Lotes podem conter múltiplos produtos.
- Cada produto pode possuir seu próprio horário de encerramento.
- O horário do produto **pode ser anterior** ao horário do lote.
- A disputa de lances é sempre individual por produto.
- Um lote só é considerado "encerrado" quando:
  - todas as disputas dos seus produtos estiverem encerradas **ou**
  - a data/hora global do lote for atingida.
- Lances devem ser processados de forma consistente e atômica.
- O sistema deve garantir experiência de disputa em tempo real.
- Visitantes nunca podem dar lances.
- Compradores precisam ter cadastro validado (dupla validação).
- O valor total final deve considerar:
  - valor do lance vencedor,
  - taxa do leiloeiro,
  - frete calculado via API externa.
- Todo encerramento deve ser auditável.
- Toda modificação relevante deve gerar registro de auditoria.

---

# 6. Restrições Gerais

- O sistema deve operar com zonas de horário consistentes (ex.: Brasília).
- Deve suportar múltiplos usuários simultâneos visualizando e disputando.
- SSE será utilizado para operações unidirecionais (atualização de preços, contagem regressiva).
- WebSocket poderá ser utilizado futuramente para disputas muito intensas.
- Todas as APIs devem ser REST.
- Nenhuma funcionalidade depende de BFF ou MFE no MVP.
- O sistema deve ser responsivo (desktop/mobile).
- Vendedores e compradores devem ter documentação validada.

---

# 7. Atores do Sistema (Visão Resumida)

## 7.1. Visitante
- Acessa catálogo público.
- Visualiza produtos.
- Visualiza lotes e horários.
- Consulta repositórios de regras e termos.
- Não participa da disputa.

## 7.2. Participante — Comprador
- Realiza cadastro e login.
- Passa por dupla validação.
- Pode dar lances.
- Pode arrematar produtos.
- Recebe documentos e notificações.
- Acompanha disputas em tempo real.

## 7.3. Participante — Vendedor
- Cadastra produtos.
- Cria lotes.
- Define horários.
- Acompanha desempenho da disputa.
- Gera e recebe documentos de venda.

## 7.4. Administrador
- Gerencia usuários.
- Gerencia produtos e lotes.
- Publica conteúdo.
- Acompanha disputas.
- Intervém quando necessário.
- Garante integridade do sistema.

---

# 8. Contexto de Operação

O sistema deve operar como uma plataforma centralizada, orientada a APIs, com:

- back-end monolítico modularizado,
- front-end Angular,
- banco PostgreSQL,
- integrações externas,
- infraestrutura AWS,
- atualização de dados em tempo real via SSE.

O foco é garantir:

- confiabilidade,
- performance,
- segurança,
- integridade dos dados.

---

# 9. Encerramento da Parte 1

Esta é a primeira seção do documento funcional e estabelece:
- visão geral,
- escopo,
- premissas,
- atores,
- contexto.

A próxima parte descreverá **todas as Regras Gerais e Regras de Negócio Globais**, detalhando profundamente o funcionamento do leilão.

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

# 📘 Documento Funcional Detalhado — Parte 4  
## Sistema de Eventos — Regras de Negócio, Integrações e Critérios de Aceite  
### **(Consolidado — Seção 4 de N)**

---

# 4. Regras de Negócio Gerais

## 4.1. Padrões Gerais do Sistema
- **RN-GEN-001 — Identificação dos Papéis**  
  O sistema deve identificar e aplicar automaticamente permissões específicas a cada papel (Visitante, Participante, Comprador, Vendedor, Administrador).  
- **RN-GEN-002 — Controle de Sessão**  
  Sessões expiram em 30 minutos de inatividade.  
- **RN-GEN-003 — Segurança**  
  Dados sensíveis devem ser trafegados em TLS 1.2+ e armazenados criptografados sempre que necessário.  
- **RN-GEN-004 — Log de Ações Sensíveis**  
  Tudo que envolve cadastro, venda, configuração, permissão ou pagamento deve ser logado.  
- **RN-GEN-005 — Auditoria Temporal**  
  Todos os registros possuem `created_at`, `updated_at` e `deleted_at` (soft delete).  
- **RN-GEN-006 — Notificações em Tempo Real**  
  SSE ou WebSocket devem ser utilizados para atualização em tempo real de:  
  - Alterações de disponibilidade de ingressos.  
  - Confirmações de compra.  
  - Atualizações feitas por vendedores e administradores.

---

# 5. Regras de Negócio por Papel

## 5.1. Visitante
- **RN-VIS-001 — Acesso Livre**  
  Visitantes podem navegar por todos os eventos públicos sem autenticação.  
- **RN-VIS-002 — Conteúdo Restrito**  
  Visitantes NÃO podem realizar compras, inscrições ou acessar dados privilegiados.  
- **RN-VIS-003 — Filtragem e Busca**  
  Visitantes podem buscar eventos por:
  - Nome
  - Categoria
  - Data
  - Local
  - Faixa de preço  
- **RN-VIS-004 — Visualização de Detalhes**  
  Devem ver descrição, agenda, imagens, mapa e disponibilidade.  
- **RN-VIS-005 — CTA de Cadastro**  
  Toda ação bloqueada deve redirecionar para uma página de login/cadastro.

---

## 5.2. Participante / Comprador
- **RN-COM-001 — Autenticação Necessária**  
  Apenas usuários autenticados podem comprar ingressos.  
- **RN-COM-002 — Carrinho Multi-Evento**  
  Compradores podem colocar ingressos de múltiplos eventos no carrinho, desde que não exceda limite por evento.  
- **RN-COM-003 — Limite por Pessoa**  
  Cada evento pode configurar um limite, e o sistema deve bloquear ultrapassagens.  
- **RN-COM-004 — Reserva Temporária**  
  Itens no carrinho ficam reservados por 15 minutos. Após isso, voltam ao estoque.  
- **RN-COM-005 — Pagamento Homologado**  
  Pagamentos devem seguir integração PCI Compliance (veja Seção 7).  
- **RN-COM-006 — Confirmação Imediata**  
  Ao concluir uma compra, o sistema deve:
  - Disparar confirmação
  - Atualizar estoque
  - Registrar dados fiscais (se aplicável)  
- **RN-COM-007 — Histórico do Usuário**  
  Comprador deve acessar todas as compras realizadas, inclusive ingressos cancelados ou expirados.  
- **RN-COM-008 — Cancelamento**  
  Compradores podem solicitar cancelamento se:
  - O evento permitir  
  - O prazo limite não tiver expirado  
  - A normativa local permitir  
- **RN-COM-009 — Uso de Voucher**  
  Compradores podem inserir códigos promocionais válidos.  
- **RN-COM-010 — Prevenção de Fraude**  
  Compras suspeitas devem ser marcadas e bloqueadas até validação.

---

## 5.3. Vendedor
- **RN-VEN-001 — Criação de Eventos**  
  Vendedores podem criar eventos com:
  - Nome, descrição, imagem  
  - Local, data, horários  
  - Lotes e disponibilidade  
  - Regras específicas do evento  
- **RN-VEN-002 — Precificação por Lote**  
  Cada lote de ingresso pode possuir:
  - Valor diferente  
  - Quantidade definida  
  - Validade/expiração  
- **RN-VEN-003 — Publicação**  
  Evento só pode ser publicado se estiver com todos os dados obrigatórios em dia.  
- **RN-VEN-004 — Gestão de Vendas**  
  Vendedor pode visualizar:
  - Métricas por lote  
  - Compradores  
  - Cancelamentos  
  - Receita estimada e real  
- **RN-VEN-005 — Ajuste de Estoque**  
  Estoque só pode ser aumentado, nunca reduzido abaixo do já vendido.  
- **RN-VEN-006 — Controle de Check-in**  
  Vendedor pode habilitar/desabilitar modalidades de entrada: QR Code, lista nominal, ou ambas.

---

## 5.4. Administrador
- **RN-ADM-001 — Configurações Globais**  
  Admin controla:
  - Parâmetros gerais do sistema  
  - Limites globais  
  - Regras fiscais  
  - Templates de e-mail/notificação  
- **RN-ADM-002 — Gestão de Papéis**  
  Admin pode promover, despromover e bloquear usuários.  
- **RN-ADM-003 — Gestão de Eventos de Terceiros**  
  Admin pode visualizar, editar ou suspender eventos de vendedores.  
- **RN-ADM-004 — Auditoria Completa**  
  Deve acessar logs completos e trilhas de auditoria.  
- **RN-ADM-005 — Moderação de Conteúdo**  
  Pode editar, ocultar ou excluir conteúdos inadequados.  
- **RN-ADM-006 — Gerenciamento de Gateway de Pagamento**  
  Admin parametriza:
  - Chaves  
  - Webhooks  
  - Credenciais  
  - Modos de sandbox/produção  

---

# 6. Critérios de Aceite (CA)

## 6.1. Critérios de Aceite Globais
- **CA-GEN-001** — Toda tela deve ser responsiva.  
- **CA-GEN-002** — Ações devem responder em no máximo 3 segundos para 95% dos acessos.  
- **CA-GEN-003** — Logs devem ser registrados em até 2 segundos após ação sensível.  
- **CA-GEN-004** — Comunicação SSE/WebSocket deve reconectar automaticamente.  
- **CA-GEN-005** — Erros devem ser apresentados com mensagens claras ao usuário.

---

# 7. Integrações

## 7.1. Pagamentos
- **INT-PAG-001 — Gateway**  
  Sistema integra com plataforma de pagamento via:
  - Webhook  
  - REST API  
  - Tokenização  
- **INT-PAG-002 — Segurança**  
  Nenhum dado sensível de cartão é armazenado.  
- **INT-PAG-003 — Tentativas de Pagamento**  
  Três tentativas antes de bloquear o pedido.

## 7.2. Notificações
- **INT-NOT-001 — E-mail + Push + SSE/WebSocket**  
  Notificações para:
  - Confirmação de compra  
  - Cancelamento  
  - Alterações de evento  

## 7.3. Logs e Observabilidade
- **INT-OBS-001** — Logs estruturados  
- **INT-OBS-002** — Tracing distribuído via OpenTelemetry  
- **INT-OBS-003** — Métricas expostas em /metrics  

---

# 8. Casos de Uso Globais (Visão Consolidada)

## 8.1. Visitante
- UC-VIS-001 — Consultar eventos  
- UC-VIS-002 — Visualizar detalhes  
- UC-VIS-003 — Buscar e filtrar eventos  

## 8.2. Participante / Comprador
- UC-COM-001 — Criar conta  
- UC-COM-002 — Autenticar  
- UC-COM-003 — Inserir ingressos no carrinho  
- UC-COM-004 — Finalizar compra  
- UC-COM-005 — Consultar histórico  
- UC-COM-006 — Cancelar compra  

## 8.3. Vendedor
- UC-VEN-001 — Criar evento  
- UC-VEN-002 — Editar evento  
- UC-VEN-003 — Gerenciar vendas  
- UC-VEN-004 — Ajustar lotes  
- UC-VEN-005 — Monitorar performance  

## 8.4. Administrador
- UC-ADM-001 — Gerenciar usuários  
- UC-ADM-002 — Moderação  
- UC-ADM-003 — Gerenciar regras e configurações  
- UC-ADM-004 — Auditoria  

---

# 9. Anexos Técnicos
*(Caso queira, eu gero na próxima parte)*

---

**FIM DA PARTE 4 — quer continuar pra parte 5? Só mandar!**  
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

# 📘 Parte 9 — Regras de Negócio do Administrador  
Documento de Requisitos Funcionais — Versão Consolidada (Waterfall)

---

# 9. REGRAS DE NEGÓCIO — ADMINISTRADOR DO SISTEMA

O Administrador representa o papel de maior privilégio dentro da plataforma.  
Suas regras de negócio envolvem governança, auditoria, moderação, segurança, gestão de vendedores, gestão de compradores, controle de leilões, finanças e monitoramento operacional.

---

## 9.1. Gestão de Usuários

### **RN-A1 — Acesso Total ao Cadastro de Usuários**
O administrador pode visualizar:
- Todos os usuários (visitantes convertidos, participantes, compradores, vendedores)  
- Status de cada conta  
- Histórico de atividades  
- Logs relevantes

### **RN-A2 — Alteração de Status de Conta**
O admin pode alterar o status de:
- Compradores  
- Vendedores  

Status possíveis:
1. **Ativo**  
2. **Pendente**  
3. **Suspenso** (temporário)  
4. **Bloqueado** (permanente)

### **RN-A3 — Justificativa Obrigatória**
Qualquer suspensão ou bloqueio deve registrar:
- Motivo  
- Data  
- Usuário administrador responsável  
- Evidências anexadas (quando aplicável)

### **RN-A4 — Reativação de Contas**
Reativação só é permitida mediante:
- Revisão das evidências  
- Expiração da penalidade  
- Autorização formal (quando necessário)

---

## 9.2. Gestão de Vendedores

### **RN-A5 — Validação de Documentação**
O administrador pode:
- Aprovar  
- Rejeitar  
- Solicitar complementar  
- Suspender temporariamente vendedores  
com base na conformidade documental.

### **RN-A6 — Auditoria de Produtos**
O admin pode:
- Acessar todos os produtos publicados  
- Remover produtos irregulares  
- Desativar produtos suspeitos  
- Inserir advertências para vendedores reincidentes

### **RN-A7 — Penalidades Automáticas e Manuais**
O administrador pode aplicar penalidades por:
- Violação de políticas  
- Tentativa de fraude  
- Publicação de itens proibidos  
- Comportamento abusivo com compradores  
- Reincidência

As penalidades seguem uma matriz de severidade.

---

## 9.3. Gestão de Produtos e Lotes

### **RN-A8 — Acesso Total aos Catálogos**
O admin pode gerenciar:
- Produtos individuais  
- Lotes  
- Agrupamentos  
- Categorias  

### **RN-A9 — Cancelamento de Lotes ou Produtos**
O admin pode:
- Cancelar lotes antes do início do leilão  
- Encerrar leilões ativos em caso de fraude  
- Bloquear produtos reportados  

### **RN-A10 — Alterações Manuais**
O admin pode corrigir:
- Títulos  
- Categorias  
- Descrições  
quando detectado erro evidente ou violação das regras.

---

## 9.4. Gestão de Leilões Ativos

### **RN-A11 — Acompanhamento em Tempo Real**
O administrador tem acesso privilegiado ao painel de:
- Lances ativos  
- Evolução de preço  
- Conflitos entre compradores  
- Eventos SSE ou WebSocket (monitoramento técnico)

### **RN-A12 — Intervenção no Leilão**
O admin pode:
- Pausar um leilão  
- Prolongar um tempo extra (quando permitido pelas regras)  
- Cancelar lances suspeitos  
- Remover usuários que tentam manipular o processo  

### **RN-A13 — Auditoria de Histórico de Lances**
Todos os lances ficam disponíveis para auditoria, incluindo:
- Timestamp  
- Origem  
- Usuário  
- Valor  
- Canal (SSE, WebSocket, REST)

---

## 9.5. Disputas, Reclamações e Mediação

### **RN-A14 — Abertura e Tratamento de Disputas**
O admin pode atuar como mediador entre:
- Comprador  
- Vendedor  

### **RN-A15 — Registro de Evidências**
Cada disputa deve conter:
- Mensagens trocadas  
- Fotos e comprovantes  
- Logs de envio  
- Histórico de prazos  
- Parecer final do administrador

### **RN-A16 — Decisão Final**
A decisão do administrador:
- Pode determinar devolução  
- Pode determinar cancelamento  
- Pode determinar liberação do pagamento  
- É registrada e vinculada ao caso permanentemente  

### **RN-A17 — Prevenção de Abusos**
O admin pode investigar:
- Fraudes sistemáticas  
- Manipulações de preço  
- Condutas abusivas  

---

## 9.6. Pagamentos, Contabilidade e Financeiro

### **RN-A18 — Acesso ao Financeiro**
O admin pode visualizar:
- Extrato da plataforma  
- Extrato dos vendedores  
- Tarifas aplicadas  
- tarifas do leiloeiro (configuração por contrato)  
- Histórico de saques  
- Pagamentos pendentes  

### **RN-A19 — Liberação Manual de Valores**
O admin pode liberar valores retidos mediante:
- Comprovação de entrega  
- Solução de disputa  
- Revisão de suspeita de fraude  

### **RN-A20 — Configuração de Tarifas**
O admin pode configurar:
- Percentual da taxa do leiloeiro  
- Comissão da plataforma  
- Tarifas extras  
- Descontos promocionais  

### **RN-A21 — Auditoria de Movimentações**
Cada movimentação financeira deve registrar:
- Valor  
- Origem  
- Destino  
- Identificador da operação  
- Justificativa  
- Admin responsável (se manual)  

---

## 9.7. Integrações e Operações Técnicas

### **RN-A22 — Gestão de Integrações Externas**
Admin controla:
- API de cálculo de frete  
- Gateways de pagamento  
- Plataformas antifraude  
- Serviços de notificação  

### **RN-A23 — Monitoramento Operacional**
O administrador tem acesso ao painel técnico (em modo somente leitura):
- Logs  
- Métricas  
- Status dos serviços  
- Filas  
- Eventos SSE  
- Conexões WebSocket  
- Health check dos microsserviços  

### **RN-A24 — Intervenção Técnica**
Apenas administradores de nível técnico podem:
- Reiniciar serviços  
- Executar rotinas de manutenção  
- Forçar limpeza de cache  
- Regenerar índices

---

## 9.8. Auditoria, Segurança e Compliance

### **RN-A25 — Logs Imutáveis**
Todas as ações administrativas devem ser:
- Logadas  
- Imutáveis  
- Vinculadas ao administrador  

### **RN-A26 — Políticas de Segurança**
O admin deve seguir:
- MFA obrigatório  
- Rotação periódica de credenciais  
- Perfis de acesso restritos (RBAC)  

### **RN-A27 — Análise de Fraudes**
O admin pode revisar:
- Movimentações suspeitas  
- Padrões irregulares de compra  
- Múltiplas contas vinculadas  
- Leilões manipulados  

### **RN-A28 — Restrições de Acesso**
Ações críticas exigem:
- Confirmação via MFA  
- Justificativa textual  
- Registro via trilha de auditoria

---

## 9.9. Notificações do Administrador

### **RN-A29 — Importância das Notificações**
O administrador recebe alertas sobre:
- Suspeitas de fraude  
- Cancelamentos acima da média  
- Reclamações recentes  
- Falhas técnicas  
- Erros em integrações  

### **RN-A30 — Tipos de Alerta**
1. **Crítico** — intervenção imediata  
2. **Alto** — risco relevante  
3. **Médio** — acompanhamento  
4. **Baixo** — informativo  

---

## 9.10. Governança da Plataforma

### **RN-A31 — Políticas e Termos**
O administrador gerencia:
- Termos de uso  
- Políticas de privacidade  
- Política de vendedores  
- Política de devoluções  

### **RN-A32 — Histórico de Alterações**
Toda mudança deve:
- Gerar nova versão  
- Registrar quem alterou  
- Ter data de vigência  
- Permitir download de versões anteriores  

### **RN-A33 — Transparência**
O sistema deve fornecer relatórios consolidados sobre:
- Quantidade de leilões  
- Volume financeiro  
- Disputas  
- Vendedores punidos  
- Melhores vendedores e compradores  

---

# ✔ Encerramento da Parte 9
Esta parte consolida todas as regras de negócio para o papel **Administrador**, compondo o bloco final do conjunto de requisitos funcionais do sistema antes da fase de unificação e conversão para backlog ágil.

# 10. Requisitos Não Funcionais (RNFs)

Os Requisitos Não Funcionais detalham atributos de qualidade, restrições técnicas, padrões arquiteturais, práticas obrigatórias e expectativas de comportamento global do sistema. Eles afetam todas as funcionalidades, todos os papéis e todos os módulos.

---

## 10.1. Performance e Escalabilidade

### RNF-001 — Tempo de Resposta
- O sistema deve responder **todas as operações críticas** (login, busca de produtos, listagem de carrinho, checkout) em **até 2 segundos** em condições normais de tráfego.
- Operações complexas (relatórios administrativos, exportações) podem levar até **10 segundos**, devendo exibir feedback apropriado de carregamento.

### RNF-002 — Capacidade de Carga
- A plataforma deve suportar, no MVP:
  - **5.000 usuários simultâneos navegando**,  
  - **500 compradores simultâneos realizando operações de compra**,  
  - **200 vendedores simultâneos gerenciando catálogo**,  
  - **50 administradores simultâneos em operações de backoffice**.

### RNF-003 — Escalabilidade Horizontal
- A arquitetura deve permitir adicionar novas instâncias do backend e frontend sem necessidade de reescrita do sistema.
- A comunicação deve ser stateless para possibilitar balanceamento de carga.

### RNF-004 — Suporte a Eventos em Tempo Real
- O sistema deve permitir escala horizontal para uso de SSE e/ou WebSockets sem impacto na consistência das atualizações.
- Eventos não podem ser perdidos; deve haver mecanismo de retry.

---

## 10.2. Segurança

### RNF-005 — Autenticação e Autorização
- O sistema deve utilizar **JWT** assinado para autenticação.
- Tokens devem expirar em período configurável (padrão 15 min + refresh de 1h).
- Perfis:
  - Visitante (não autenticado)
  - Comprador
  - Vendedor
  - Administrador
- Cada endpoint deve validar permissões estritamente.

### RNF-006 — Criptografia
- Todo tráfego deve ocorrer sobre HTTPS (TLS 1.2+).
- As senhas devem ser armazenadas utilizando **bcrypt ou Argon2**.

### RNF-007 — Proteção contra Ataques
O sistema deve implementar:
- Proteção contra SQL Injection,
- Proteção contra XSS,
- Proteção contra CSRF,
- Rate limiting em endpoints sensíveis,
- Bloqueio temporário de login após tentativas falhas consecutivas.

### RNF-008 — LGPD
- Dados pessoais devem ser tratados conforme LGPD.
- Logs devem mascarar dados pessoais.
- Deve existir mecanismo para exclusão de conta e dados sensíveis.

---

## 10.3. Observabilidade, Logs e Monitoramento

### RNF-009 — Padronização de Logs
- Todos os microsserviços (mesmo modularizados num único backend) devem seguir o mesmo formato de log:
  - timestamp,
  - requestId,
  - usuário,
  - tipo do evento,
  - severidade,
  - origem.

### RNF-010 — Rastreabilidade
- Toda requisição deve receber um **requestId** propagado entre frontend → backend.

### RNF-011 — Métricas
- O backend deve exportar métricas compatíveis com Prometheus.
- No mínimo:
  - tempo médio de resposta,
  - throughput,
  - erros por segundo,
  - consumo de recursos.

### RNF-012 — Auditoria
- Operações sensíveis (criar produto, excluir produto, alterar preço, banir usuário) devem ser auditadas.

---

## 10.4. Disponibilidade e Confiabilidade

### RNF-013 — Disponibilidade do Sistema
- O sistema deve ter Uptime mínimo de **99%**.

### RNF-014 — Tolerância a Falhas
- A plataforma deve continuar operando mesmo com a queda de uma instância do backend.
- Serviços críticos devem possuir retry automático e circuit breaker.

### RNF-015 — Consistência de Dados
- Durante operações de compra, a integridade deve ser total (sem double booking de estoque).
- O sistema deve usar locks otimizados ou transações distribuídas apenas quando necessário.

---

## 10.5. Usabilidade

### RNF-016 — Design Responsivo
- O frontend deve funcionar adequadamente em:
  - desktop,
  - tablets,
  - smartphones.

### RNF-017 — Acessibilidade
- O sistema deve seguir WCAG 2.1 AA:
  - contraste mínimo,
  - navegação por teclado,
  - textos alternativos,
  - labels acessíveis.

### RNF-018 — UX consistente
- Padrões de UI devem seguir um design system único.
- Feedback visual para:
  - processamento,
  - erro,
  - sucesso,
  - estados vazios.

---

## 10.6. Compatibilidade e Interoperabilidade

### RNF-019 — Browsers Suportados
- Chrome, Firefox, Safari e Edge — últimas duas versões estáveis.

### RNF-020 — APIs Padronizadas
- JSON padronizado com camelCase.
- Paginação consistente:
  - `page`, `size`, `totalPages`, `totalElements`.

---

## 10.7. Manutenibilidade e Evolução

### RNF-021 — Modularização Interna
- O backend deve seguir arquitetura modular desde o início, mesmo sendo um único serviço:
  - módulo de catálogo,
  - módulo de contas,
  - módulo de pedidos,
  - módulo administrativo,
  - módulo de notificações/eventos.

### RNF-022 — Código Limpo
- Deve seguir padrões:
  - SOLID,
  - separação clara de camadas,
  - testes unitários obrigatórios,
  - 70%+ cobertura mínima no MVP.

### RNF-023 — Documentação
- O backend deve expor documentação OpenAPI/Swagger sempre atualizada.

---

## 10.8. Implantação e Entrega Contínua

### RNF-024 — Deploy Automatizado
- A pipeline de CI/CD deve:
  - rodar testes,
  - validar linting,
  - criar artefatos,
  - realizar deploy automatizado no ambiente de teste.

### RNF-025 — Versionamento
- Versionamento semântico: MAJOR.MINOR.PATCH.

---

## 10.9. Armazenamento e Infraestrutura

### RNF-026 — Banco de Dados
- O banco deve garantir:
  - replicação primária-secundária,
  - isolamento de transações mínimo `READ COMMITTED`.

### RNF-027 — Mensageria e Eventos
- A solução deve usar:
  - SSE para notificações unidirecionais (ex.: atualização de estoque),
  - WebSocket para interações em tempo real quando necessário,
  - Fila/stream para eventos internos caso o volume cresça (Kafka ou equivalente no futuro).

---

## 10.10. Requisitos Operacionais

### RNF-028 — Backups
- Backups diários completos + incrementais de hora em hora.
- Teste de restauração mensal obrigatório.

### RNF-029 — Alertas
- Alertas automáticos para:
  - falhas,
  - degradação,
  - indisponibilidade,
  - estouro de limites de recursos.

---

## 10.11. Critérios de Qualidade do Produto

### RNF-030 — Confiabilidade Validada
- O sistema só pode ir para produção após:
  - testes funcionais completos,
  - testes de carga,
  - testes de segurança,
  - testes de usabilidade.

### RNF-031 — Não Regressão
- Qualquer nova feature deve passar por testes automatizados de regressão.

# 📘 PARTE 11 — Integrações, Interfaces e Contratos

## 1. Introdução

Esta seção documenta todas as **integrações**, **interfaces externas**, **pontos de entrada e saída**, e os **contratos** de comunicação entre sistemas envolvidos no Leilão Online.  
Esse material segue o padrão de documentação funcional clássica (“waterfall”), servindo como base para arquitetura, desenvolvimento e testes integrados.

Foco:
- APIs internas e externas  
- Webhooks  
- Notificações em tempo real (SSE / WebSocket)  
- Integrações para pagamento  
- Contratos JSON  
- Operações síncronas e assíncronas  
- Regras específicas por canal de integração  

---

# 2. Arquitetura de Integração

O sistema opera com três tipos de integração:

### **2.1. Integrações Síncronas (REST API)**
- Operações CRUD  
- Consultas de leilões  
- Dar lance  
- Consultar histórico  
- Administração  

Características:
- JSON  
- Autenticação JWT  
- Códigos HTTP padronizados  
- Idempotência para operações críticas  

---

### **2.2. Integrações Assíncronas (SSE / WebSocket)**
Usadas para:
- atualização de lances em tempo real  
- início / encerramento do leilão  
- extensões automáticas (anti-sniping)  
- notificações para participantes  

Características:
- Canal único por leilão (ex: `/ws/auctions/{id}`)  
- Eventos padronizados  
- Reconexão automática  

---

### **2.3. Integrações com Sistemas Externos**
1. **Gateway de Pagamentos**  
2. **Serviço de Validação de CPF**  
3. **Serviço OTP (SMS)**  
4. **Serviço de E-mail (SMTP ou API)**  

Todos com contratos padronizados.

---

# 3. Mapa Geral das Integrações

| Categoria | Tipo | Descrição |
|----------|------|-----------|
| Autenticação | REST | Login, refresh token, logout |
| Dados de Leilão | REST | Criar, listar, consultar, encerrar |
| Lances | REST + WebSocket | Registrar lance / receber em tempo real |
| Notificações | WebSocket / SSE | Push de eventos do leilão |
| Pagamento | REST externo | Autorização, captura, cancelamento |
| OTP | REST externo | Envio de código SMS |
| E-mail | REST/SMTP | Notificações offline |
| Validação CPF | REST externo | Autorização de identidade |

10. Mapeamento dos Fluxos de Integração
10.1. Criar Leilão

Vendedor cria leilão

API registra

Notificação opcional para seguidores

10.2. Dar Lance

REST registra lance

Módulo valida regras

Atualiza histórico

Envia evento:

WebSocket

SSE fallback

E-mail se usuário estiver offline

10.3. Início do Leilão

Scheduler ativa

Evento “auctionStarted” enviado via WebSocket/SSE

10.4. Encerramento

Scheduler encerra

Determina vencedor

Envia evento real-time

Dispara processo de pagamento

10.5. Pagamento

Comprador autoriza

Gateway valida

Admin/Vendedor confirma entrega

Sistema registra auditoria

12. Regras de Interface e Compatibilidade

Todos os endpoints devem manter retrocompatibilidade por no mínimo 6 meses

Versões devem seguir o padrão:

/api/v1/...

/api/v2/...

Eventos SSE/WS também são versionados

13. Segurança das Integrações

JWT com validade curta

Refresh tokens armazenados com segurança

Rate limiting por IP

Throttling em endpoints de lance

Logs obrigatórios em ações sensíveis

Criptografia TLS 1.2+

14. Conclusão

Este capítulo consolida todas as integrações e contratos do sistema de forma alinhada ao modelo waterfall tradicional, pronto para conversão para épicos, histórias e testes automatizados.
