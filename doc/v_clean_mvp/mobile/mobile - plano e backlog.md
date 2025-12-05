# 📱 Plano de Evolução — App Mobile (Pós MVP Web)

## 1. 🎯 Objetivo Geral
Expandir o ecossistema do Leilão Eletrônico com um aplicativo mobile dedicado ao **Participante/Comprador**, oferecendo:
- Acesso rápido aos leilões
- Lances mais ágeis
- Notificações em tempo real (push + SSE/WebSocket)
- Experiência otimizada para mobilidade
- Redução de atrito no fluxo de compra

O App **não substitui** o portal Web — ele complementa e reforça a participação ativa nos leilões.

---

# 2. 🧩 Premissas Técnicas do App

## 2.1. 📱 Framework sugerido
**Flutter** (mais recomendado)
- Build único para Android e iOS  
- Performance alta  
- Fácil integração com REST + SSE/WebSockets  
- Componentização moderna e eficiente  

Alternativas possíveis:
- React Native (boa integração com ecossistema JS)  
- Nativo (Swift/Kotlin) – só se houver exigência de performance extrema  

Premissa adotada neste plano: **Flutter**.

---

## 2.2. 🔌 Integração com Backend
O backend atual já fornece:

- Login/JWT  
- Catálogo  
- Lances  
- SSE (eventos do leilão)  
- WebSocket (notificações em tempo real)  
- Histórico  
- Perfis 

O App usará **os mesmos endpoints**, apenas adaptados com:
- Módulo de "Mobile Session"
- Rate-limiting específico
- Token refresh otimizado
- Push Notifications (Firebase Cloud Messaging)

---

## 2.3. 🔔 Notificações Push
Será necessário criar no backend:
- Endpoint `/notifications/mobile/register`
- Vincular `deviceToken` ao usuário autenticado
- Envio via:
  - Firebase Messaging (Android/iOS)
  - Tópicos (leilões que o usuário segue)
  - Push em "perda de lance"
  - Push em "leilão iniciando"

---

## 2.4. 🌐 Padrões de Tempo Real
O App usará:
- **SSE** → streaming do estado do leilão  
- **WebSocket** → lances e interações críticas  
- **Push Notification** → eventos "fora do app"  

---

## 2.5. 🔐 Segurança no App
- KeyStore/Keychain para armazenar tokens  
- Refresh Token automático  
- SSL Pinning (Opcional nível hardening)  
- Device Binding (Opcional para V2.5)

---

# 3. 🏗️ Arquitetura do App

## 3.1. Camadas
- **UI** (Flutter Widgets)  
- **State Management** (Bloc / Riverpod)  
- **Services**
  - AuthService  
  - AuctionService  
  - BidService  
  - FavoritesService  
  - NotificationService  
- **Realtime Layer**
  - SSEClient  
  - WebSocketClient  
- **Persistence (local)**
  - Hive/SharedPreferences para cache

---

# 4. 🎯 Temas, Épicos e Histórias do APP

Abaixo está o **backlog completão**.

---

# 4.1. 📚 TEMAS

1. **APP Foundation & Infrastructure**
2. **Autenticação & Sessão Mobile**
3. **Catálogo Mobile**
4. **Leilão Mobile (Tempo Real)**
5. **Fluxo de Lances Mobile**
6. **Notificações Push**
7. **Favoritos, Histórico e Perfil Mobile**
8. **Observabilidade, Métricas e Telemetria**
9. **Publicação e Distribuição (Stores)**

---

# 4.2. 🧱 ÉPICOS + HISTÓRIAS (com enablers)

## 🧱 Tema 1 — APP Foundation & Infrastructure

### Épico 1.1 — Setup do Projeto Mobile
**Histórias:**
- **H1.1.1 — Criar projeto base Flutter**
- **H1.1.2 — Definir estrutura de pastas e camadas**
- **H1.1.3 — Configurar state management (Bloc/Riverpod)**
- **H1.1.4 — Configurar integração REST básica**
- **H1.1.5 — Implementar módulo de logs locais**

**Enablers:**
- **E1.1.A — Pipeline CI/CD Mobile (build e testes)**  
- **E1.1.B — Configuração de ambientes (dev, stage, prod)**

---

## 🧱 Tema 2 — Autenticação & Sessão Mobile

### Épico 2.1 — Login Mobile
**Histórias:**
- **H2.1.1 — Tela de login mobile**
- **H2.1.2 — Consumo de API de auth existente**
- **H2.1.3 — Armazenamento seguro do token (KeyStore/Keychain)**
- **H2.1.4 — Auto-login / Sessão persistida**

**Enablers:**
- **E2.1.A — Adaptar backend para registrar deviceToken**
- **E2.1.B — Endpoint de refresh otimizado para mobile**

---

## 🧱 Tema 3 — Catálogo Mobile

### Épico 3.1 — Catálogo de Leilões no App
**Histórias:**
- **H3.1.1 — Tela de listagem de leilões ativos**
- **H3.1.2 — Tela de leilões futuros**
- **H3.1.3 — Tela de detalhes do item dentro do app**
- **H3.1.4 — Cache local do catálogo**
- **H3.1.5 — Filtro e busca mobile**

---

## 🧱 Tema 4 — Leilão Mobile (Tempo Real)

### Épico 4.1 — Leilão em Tempo Real no App
**Histórias:**
- **H4.1.1 — Assinatura SSE de estado do leilão**
- **H4.1.2 — Conexão WebSocket**
- **H4.1.3 — Renderização em tempo real dos dados**
- **H4.1.4 — Feedback visual de lances recebidos**
- **H4.1.5 — Tela de “contagem regressiva” mobile**

**Enablers:**
- **E4.1.A — Endpoint SSE específico para app (se necessário)**

---

## 🧱 Tema 5 — Fluxo de Lances Mobile

### Épico 5.1 — Lances via App (Alta Performance)
**Histórias:**
- **H5.1.1 — Botão de lance otimizado pra mobile**
- **H5.1.2 — Confirmação de lance**
- **H5.1.3 — Exibição de "maior lance" em tempo real**
- **H5.1.4 — Tratamento de erros (perdeu lance / bid invalid)**
- **H5.1.5 — Notificação de perda de lance**

---

## 🧱 Tema 6 — Notificações Push

### Épico 6.1 — Infraestrutura Push
**Histórias:**
- **H6.1.1 — Integração com Firebase Messaging**
- **H6.1.2 — Registro de deviceToken**
- **H6.1.3 — Push de lance superado**
- **H6.1.4 — Push de leilão começando**
- **H6.1.5 — Push de item favorito em promoção**

**Enablers:**
- **E6.1.A — Backend: envio push programático**
- **E6.1.B — Backend: tópicos de interesse (por item/leilão)**

---

## 🧱 Tema 7 — Favoritos, Histórico e Perfil Mobile

### Épico 7.1 — Favoritos
**Histórias:**
- **H7.1.1 — Adicionar item/leilão aos favoritos**
- **H7.1.2 — Listagem de favoritos**
- **H7.1.3 — Notificações sobre favoritos**

### Épico 7.2 — Histórico
**Histórias:**
- **H7.2.1 — Histórico de lances mobile**
- **H7.2.2 — Histórico de leilões participados**
- **H7.2.3 — Sincronização com backend**

### Épico 7.3 — Perfil
**Histórias:**
- **H7.3.1 — Tela de perfil mobile**
- **H7.3.2 — Edição de dados pessoais**
- **H7.3.3 — Verificação de telefone/e-mail (opcional)**

---

## 🧱 Tema 8 — Observabilidade, Telemetria e Métricas

### Épico 8.1 — App Insights
**Histórias:**
- **H8.1.1 — Coleta de eventos de uso**
- **H8.1.2 — Monitoramento de erros**
- **H8.1.3 — Métricas de engajamento dos leilões**

**Enablers:**
- **E8.1.A — Integração com Firebase Analytics**
- **E8.1.B — Integração com Sentry**

---

## 🧱 Tema 9 — Publicação e Distribuição

### Épico 9.1 — Publicação nas Stores
**Histórias:**
- **H9.1.1 — Configuração do app no Google Play**
- **H9.1.2 — Configuração no Apple App Store**
- **H9.1.3 — Build e assinatura**
- **H9.1.4 — Preparar screenshots e descrição**
- **H9.1.5 — Política de privacidade + consentimentos**

---

# 5. 🎯 Resumo do Backlog (Tabela Consolidada)

| Tema | Épico | História / Enabler | Tipo |
|------|-------|---------------------|------|
| App Foundation | Setup | Criar projeto base Flutter | Funcional |
| App Foundation | Setup | Estrutura de pastas | Enabler |
| App Foundation | Setup | Config state management | Enabler |
| App Foundation | Setup | Configuração REST | Enabler |
| Autenticação | Login | Tela de login | Funcional |
| Autenticação | Login | Armazenamento seguro | Funcional |
| Autenticação | Login | Sessão persistida | Funcional |
| Catálogo | Lista | Leilões ativos | Funcional |
| Catálogo | Lista | Leilões futuros | Funcional |
| Catálogo | Detalhe | Tela detalhe item | Funcional |
| Tempo Real | SSE/WS | Conectar SSE | Enabler |
| Tempo Real | SSE/WS | Conectar WebSocket | Enabler |
| Lances | Fluxo | Botão de lance | Funcional |
| Lances | Fluxo | Confirmação | Funcional |
| Lances | Fluxo | Erros de lance | Funcional |
| Push | Infra | Firebase Messaging | Enabler |
| Push | Infra | Registro token | Enabler |
| Push | Eventos | Lance superado | Funcional |
| Favoritos | Funções | Adicionar favorito | Funcional |
| Favoritos | Funções | Notificar mudança | Funcional |
| Histórico | Funções | Histórico de lances | Funcional |
| Perfil | Dados | Editar perfil | Funcional |
| Observabilidade | Eventos | Analytics | Enabler |
| Observabilidade | Eventos | Error tracking | Enabler |
| Distribuição | Lojas | Publicação Android | Enabler |
| Distribuição | Lojas | Publicação iOS | Enabler |

---

# 6. 📅 Plano de Sprints (6 Sprints — 2 semanas cada)

## 🏁 Sprint 1 — Foundation Mobile
- Criar projeto
- Arquitetura
- State management
- REST básico
- Login simples

## 🏁 Sprint 2 — Autenticação Completa + Catálogo Inicial
- Sessão persistida
- Tela de catálogo
- Detalhes do item
- Cache básico

## 🏁 Sprint 3 — Tempo Real
- SSE
- WebSocket
- Atualização de lances ao vivo
- UI live bidding

## 🏁 Sprint 4 — Lances + Erros + UX Crítica
- Botão de lance
- Feedback de lance
- Tratamento de falhas
- Confirm dialogs

## 🏁 Sprint 5 — Push Notifications + Favoritos
- Firebase Messaging
- DeviceToken → Backend
- Push de lance superado
- Favoritos + notificações

## 🏁 Sprint 6 — Histórico + Perfil + Observabilidade + Publicação
- Histórico
- Perfil
- Analytics + Sentry
- Preparação para store

---

# 7. 🎉 Conclusão

O plano acima garante:

- Evolução segura  
- Zero retrabalho  
- Reaproveitamento total do backend  
- Tempo real robusto  
- Experiência impecável pro comprador  

E abre caminho perfeito para:

- Gamificação no app  
- Modo turbo de lances  
- Acompanhamento multi-leilão  

