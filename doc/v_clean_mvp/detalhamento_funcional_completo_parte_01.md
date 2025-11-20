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

