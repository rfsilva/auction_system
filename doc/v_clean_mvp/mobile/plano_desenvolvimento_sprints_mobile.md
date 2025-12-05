# 📱 Plano de Sprints — APP Mobile (Evolução Pós-Web)

Este documento descreve o planejamento de sprints, backlog refinado e dependências para o desenvolvimento do aplicativo mobile que complementa o MVP Web 1.0 já existente.

---

# 🟧 S12 — Sprint 1: Fundação do App e Onboarding

**Objetivo:** Criar a base estrutural do app, permitir login, onboarding e setup técnico.

## Histórias
- **H-APP-001** — Splash screen + validação de versão  
- **H-APP-002** — Onboarding básico  
- **H-APP-003** — Login via API  
- **H-APP-004** — Recuperação de senha  
- **H-APP-005** — Persistência do token + sessão segura  

## Enablers
- **EN-APP-01** — Setup do repositório mobile  
- **EN-APP-02** — CI/CD com Firebase App Distribution  
- **EN-APP-03** — Base de arquitetura (Clean + BLoC/Provider)  
- **EN-APP-04** — Config inicial de Push Notifications (FCM)  

---

# 🟧 S13 — Sprint 2: Catálogo de Leilões e UX Inicial

**Objetivo:** Exibir leilões e itens, com lista, busca e detalhes.

## Histórias
- **H-APP-006** — Lista de leilões  
- **H-APP-007** — Filtro, busca e ordenação  
- **H-APP-008** — Tela de detalhes do leilão  
- **H-APP-009** — Visualização dos itens com fotos  

## Enablers
- **EN-APP-05** — Ajuste na API para payload otimizado  
- **EN-APP-06** — Cache local (Hive ou SQLite)  

---

# 🟧 S14 — Sprint 3: Participação no Pregão (Base)

**Objetivo:** Permitir que o comprador acompanhe o leilão em tempo real.

## Histórias
- **H-APP-010** — Sala do leilão (real-time viewer)  
- **H-APP-011** — Atualização automática do lance atual  
- **H-APP-012** — Entrada no leilão + validações  

## Enablers
- **EN-APP-07** — WebSocket/Socket.IO no app  
- **EN-APP-08** — Mecanismo de reconexão  

---

# 🟧 S15 — Sprint 4: Fluxo de Lances

**Objetivo:** Habilitar envio de lances no app.

## Histórias
- **H-APP-013** — Fazer lance manual  
- **H-APP-014** — Validação de saldo ou pré-autorização  
- **H-APP-015** — Feedback de "ganhando/perdendo"  
- **H-APP-016** — Histórico de lances  

## Enablers
- **EN-APP-09** — Ajustes no payload do WebSocket  
- **EN-APP-10** — Anti-double-tap para evitar flooding  

---

# 🟧 S16 — Sprint 5: Notificações Inteligentes

**Objetivo:** Melhorar experiência com notificações contextualizadas.

## Histórias
- **H-APP-017** — Notificação de início de leilão  
- **H-APP-018** — Notificação ao perder lance  
- **H-APP-019** — Notificação de vitória  
- **H-APP-020** — Centro de notificações  

## Enablers
- **EN-APP-11** — Regras de notificação no backend  
- **EN-APP-12** — Tópicos FCM (por leilão/item)  

---

# 🟧 S17 — Sprint 6: Pagamentos

**Objetivo:** Permitir pagamento da entrada ou quitação após vitória.

## Histórias
- **H-APP-021** — Tela de pagamento (Pix + cartão)  
- **H-APP-022** — Confirmação + recibo  
- **H-APP-023** — Histórico de pagamentos  

## Enablers
- **EN-APP-13** — Tokenização via gateway de pagamento  
- **EN-APP-14** — Rotas específicas para mobile  

---

# 🟧 S18 — Sprint 7: Minha Conta + Preferências

**Objetivo:** Fornecer opções do comprador no mobile.

## Histórias
- **H-APP-024** — Meu perfil  
- **H-APP-025** — Meus leilões ativos e finalizados  
- **H-APP-026** — Wishlist / Itens acompanhados  
- **H-APP-027** — Preferências de notificação  

## Enablers
- **EN-APP-15** — APIs unificadas para mobile  

---

# 🟧 S19 — Sprint 8: Otimizações e Hardening

**Objetivo:** Estabilizar o app, melhorar performance e corrigir bugs.

## Histórias
- **H-APP-028** — Performance geral  
- **H-APP-029** — Correções de QA  
- **H-APP-030** — Ajustes UX/UI pós-teste  
- **H-APP-031** — Otimização de rede e imagens  

## Enablers
- **EN-APP-16** — Observabilidade  
- **EN-APP-17** — Crashlytics + Analytics  

---

# 🟧 S20 — Sprint 9: Publicação

**Objetivo:** Submeter o app às lojas.

## Histórias
- **H-APP-032** — Ajustes finais (ícone, splash, políticas)  
- **H-APP-033** — Setup das lojas (Google + Apple)  
- **H-APP-034** — Publicação + resolução de pendências  

---

# 🟩 Resumo Geral das Sprints

| Sprint | Entrega Principal |
|--------|-------------------|
| **S12** | Login + Fundamentos |
| **S13** | Catálogo |
| **S14** | Real-time / Sala do leilão |
| **S15** | Envio de lances |
| **S16** | Notificações |
| **S17** | Pagamentos |
| **S18** | Minha conta |
| **S19** | Hardening |
| **S20** | Publicação |

---

# 📌 Observações Técnicas Gerais
- App desenvolvido em **Flutter** (recomendado pela velocidade + Android + iOS).  
- Backend Web atual é mantido como **fonte única da verdade**.  
- WebSocket e APIs reutilizam 100% do backend já construído.  
- Painel Web continua sendo plataforma principal para administração.

---

# ✔️ Documento final pronto para refinamentos adicionais conforme o time evoluir.
