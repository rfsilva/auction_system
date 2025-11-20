# Detalhamento Funcional — Papel: VISITANTE (não autenticado)

**Versão:** 1.0  
**Data:** 2025-11-19  
**Escopo:** comportamento, permissões, fluxos UI e contratos mínimos API para o ator *Visitante* no MVP do Sistema de Leilão Eletrônico.

> Observação: "Visitante" aqui significa usuário não autenticado no sistema (sem JWT). Algumas ações explicadas permitem transição para "Participante" (cadastro) — nestes pontos fica claro o fluxo de conversão.

---

## Sumário
1. Objetivo e visão geral do papel  
2. Permissões (what can / cannot)  
3. Principais telas / componentes UI acessíveis  
4. Fluxos funcionais detalhados  
   - Visualizar catálogo / buscar  
   - Visualizar detalhe de produto / lote  
   - Assistir leilão (read-only / SSE)  
   - Iniciar cadastro (conversão)  
5. Regras de apresentação de dados e limitações (PII, informações agregadas)  
6. Contratos API relevantes (ex.: endpoints, request/response mínimos)  
7. Mensagens de erro e respostas esperadas  
8. Critérios de aceite e casos de teste (visíveis / automatizáveis)  
9. Requisitos não-funcionais (latência, escala, segurança)  
10. Observabilidade, auditoria e analytics para visitantes  
11. Acessibilidade e internacionalização (i18n)  
12. Edge cases e exceções operacionais

---

## 1. Objetivo e visão geral
O Visitante deve conseguir **descobrir** leilões e produtos, entender o estado atual (preço, tempo restante, número de lances), e iniciar o cadastro quando desejar participar. O visitante **não** pode interagir com o leilão (não pode dar lances, não vê dados sensíveis) — apenas consumir informação pública.

---

## 2. Permissões (resumo)

**Pode (ações permitidas):**
- Visualizar lista pública de lotes/leilões.
- Filtrar e buscar por produtos/lotes.
- Visualizar página de detalhe de produto/lote (descrição, fotos, preço atual, timer, últimos lances resumidos).
- Abrir conexão SSE para receber atualizações read-only do estado do leilão (se permitido para visitantes).
- Iniciar fluxo de cadastro (Sign Up) para se tornar Participante/Buyer.
- Visualizar FAQs, regras do site, políticas (privacidade, termos).
- Compartilhar link do produto/leilão (social share).

**Não pode (proibições):**
- Enviar ou confirmar lances.
- Ver detalhes pessoais de outros usuários (e.g., dados do seller que sejam PII além do nome público).
- Acessar páginas restritas (meus lances, checkout, área de vendedor, admin).
- Receber notificações push/personalizadas in-app relacionadas a conta (até se autenticar).

---

## 3. Telas / Componentes UI acessíveis (detalhe)

### 3.1 Home / Lista de Leilões (Catalog)
- Componente: `AuctionList`  
- Dados exibidos por item: `title`, `thumbnail`, `currentPrice` (ou startPrice), `timeRemaining` (HH:MM:SS), `status` (ACTIVE/FUTURE/CLOSED), `bidCount` (opcional), `sellerName` (público).  
- Filtros: `category`, `priceRange`, `status`, `searchText`.  
- Ordenação: `endingSoon`, `newest`, `mostBids`.

### 3.2 Página de Detalhe do Produto / Lote
- Componentes:
  - `ImageGallery` (thumbnail + zoom)
  - `ProductSummary` (title, subtitle, category)
  - `PricePanel` (currentPrice, minIncrement, nextPriceIndicative)
  - `CountdownTimer` (timeRemaining)
  - `RecentBidsFeed` (últimos N lances com valores e timestamp relativos — **sem** informações PII do bidder)
  - `SellerBox` (nome público, rating — sem contato direto)
  - `CallToAction` (botão: "Entrar / Cadastrar para ofertar")
- Observações UX:
  - Se visitante, botão de "Dar lance" aparece, mas ao clicar abre modal de conversão para cadastro/login. Não permitir envio direto.

### 3.3 Página de Busca e Filtros Avançados
- `SearchInput` com sugestões.
- Facets com contadores aproximados (não obrigatórios no MVP).

### 3.4 Headline & Help
- Barra com link para regras do leilão, FAQ, política de uso.
- Banner com aviso quando leilões de alto risco / lotes privados.

---

## 4. Fluxos funcionais detalhados

### 4.1 Fluxo: Visualizar Catálogo (visitante)
**Passos:**
1. Visitante acessa `/` ou `/catalog`.  
2. Front chama `GET /api/v1/auctions?status=active&page=1&size=20&sort=endingSoon`.  
3. Backend retorna listagem paginada com dados públicos.  
4. Front renderiza cartões com `CountdownTimer` calculado pelo `endDateTime` do item.  
5. Visitante aplica filtro → front refaz chamada à API.

**Validações / Regras:**
- Itens com `status != PUBLIC` (e.g., private) **não** aparecem.
- `timeRemaining` calculado do `endDateTime` em UTC; se `endDateTime` expirado, o item é tratado como `CLOSED` (front obtém status do backend).

**Critério de Aceite:**
- Listagem carrega em < 1.2s (página média) em ambiente de dev; paginação correta; filtros funcionais.

---

### 4.2 Fluxo: Visualizar Detalhe de Produto
**Passos:**
1. Visitante abre `/product/{productId}`.  
2. Front chama `GET /api/v1/products/{productId}`.  
3. Backend retorna `product` (campos públicos), `lot` (se aplicável) e `currentSummary` (currentPrice, bidCount, endDateTime).  
4. Front abre SSE (opcional) em `/sse/auctions/{productId}` para receber atualizações do `currentPrice` e `timeRemaining`.  
5. RecentBidsFeed exibe últimos 5 lances (valores e tempos relativos), sem identificar o bidder.

**Validações / Regras:**
- Se o produto estiver em lote privado, visitante recebe 403 ou redirect para explicação (dependendo de política).
- O visitante **não** pode ver dados sensíveis do seller (CPF, email, telefone).

**Critério de Aceite:**
- Página carrega com dados consistentes; SSE fornece updates de preço; botões de CTA de cadastro disponíveis.

---

### 4.3 Fluxo: Assistir Leilão (SSE read-only)
**Passos:**
1. Ao abrir a página do produto, front estabelece conex. SSE: `GET /sse/auctions/{productId}`.  
2. Backend envia eventos: `priceUpdate`, `bidSummary`, `auctionExtended`, `auctionClosed`.  
3. Front atualiza `PricePanel` e `CountdownTimer`.

**Regras:**
- SSE não deve exigir autenticação para informações públicas (a menos que lotes sejam privados).  
- SSE deve reconectar automaticamente com backoff exponencial.  
- Evento deve incluir `correlationId`, `serverSequenceId`, `timestamp`.

**Critério de Aceite:**
- Em ambiente controlado, visitante vê atualização de preço em <1s após evento internal publish.

---

### 4.4 Fluxo: Iniciar Cadastro (conversão)
**Passos:**
1. Visitante clica `Cadastrar / Entrar para ofertar`.  
2. Modal direciona para `/signup` (form simples: name, email, phone, password).  
3. Backend cria usuário em estado `PENDING_VERIFICATION` e envia email com token (24h).  
4. Visitante confirma email → status `ACTIVE`.  
5. Para dar lances, sistema exige KYC (verificar RF-003) — o visitante convertido receberá instruções.

**Regras:**
- Email único; senha mínima 8 chars.  
- CAPTCHA anti-bot pode ser exigido.

**Critério de Aceite:**
- Fluxo de cadastro cria registro, envia email; usuário autenticado consegue iniciar sessão.

---

## 5. Regras de apresentação de dados e limitações (PII, sensibilidade)

- Visitante **não** vê PII (CPF, email, telefone) de sellers ou bidders.  
- Identificador de seller exibido como `Nome Comercial` + `rating` (opcional).  
- Últimos lances mostrados com tempo relativo (`há 12s`) e **sem** identificação do usuário.  
- Quantidade de lances (`bidCount`) pode ser apresentada, mas detalhes restritos.

---

## 6. Contratos API relevantes (exemplos mínimos)

/** Lista de leilões **/
