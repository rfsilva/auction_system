# 📱 Requisitos Funcionais – Cenário Web + App (Participante / Comprador)

## RF-A1 — Autenticação e Acesso (Web/App)
- O sistema deve permitir login via e-mail/senha, OAuth (Google/Apple) e autenticação biométrica no app.
- Deve permitir recuperação de senha via e-mail.
- Sessões do app devem ter refresh token para evitar logins constantes.
- Após login, o comprador deve ver um dashboard com seus leilões ativos, favoritos e recomendações.

---

## RF-A2 — Onboarding do App
- O usuário deve visualizar um tutorial inicial explicando:
  - Como participar de leilões.
  - Como funciona o tempo real.
  - Como configurar alertas.
- O tutorial deve poder ser ignorado e reaberto depois.

---

## RF-L1 — Listagem de Leilões (Web/App)
- O comprador deve conseguir visualizar todos os leilões em andamento, futuros e encerrados.
- A listagem deve ter filtros idênticos entre web e app.
- O app deve permitir navegação “infinite scroll”.

---

## RF-L2 — Detalhamento do Leilão (Web/App)
- Exibir fotos, descrição, preço inicial, incrementos, tempo restante, vendedor, regras.
- Exibir gráfico simples de evolução de lances (no app, adaptado para mobile).
- Exibir status em tempo real via SSE/WebSocket tanto no app quanto na web.

---

## RF-L3 — Favoritar Leilões (Web/App)
- O comprador pode marcar leilões como favoritos.
- Os favoritos devem sincronizar entre app e web.
- O app deve permitir ativar **notificações push** quando:
  - Um leilão favorito iniciar.
  - Um item favorito ficar prestes a terminar.
  - Alguém superar o lance do usuário.

---

## RF-L4 — Participar de Leilão (Tempo Real) (Web/App)
- O usuário deve enviar lances em tempo real.
- O app deve ter ação rápida (“tap and bid”).
- O sistema deve bloquear lances inválidos ou abaixo do mínimo.
- O app deve vibrar quando:
  - O lance for aceito.
  - O usuário for superado.
  - O leilão estiver nos últimos 10 segundos.

---

## RF-L5 — Compra Direta / Buy Now (se aplicável)
- Disponível tanto no app quanto na web.
- Confirmação em dois passos no app para evitar toques acidentais.

---

## RF-N1 — Notificações (App)
### Push:
- Lance superado.
- Leilão favorito prestes a começar.
- Últimos 10 minutos.
- Últimos 10 segundos (configurável).
- Item recém-adicionado pelo vendedor e relacionado ao histórico do usuário.

### In-app:
- Alterações de regras.
- Mensagens do vendedor e do administrador.
- Sugestões de leilões relevantes.

---

## RF-H1 — Histórico do Usuário (Web/App)
- Histórico de lances sincronizado.
- Histórico de compras.
- Histórico de participação em leilões perdidos.

---

## RF-U1 — Perfil do Comprador (Web/App)
- Alterar nome, e-mail, telefone, notificações.
- No app: permitir alteração de preferência de notificações push.
- Configurar alertas específicos:
  - Preço máximo.
  - Tipo de item de interesse.
  - Categorias favoritas.

---

## RF-D1 — Dashboard do Comprador (Web/App)
- Visualização rápida de:
  - Leilões ativos que o usuário está participando.
  - Seus lances ativos.
  - Itens onde ele está ganhando/perdendo.
  - Favoritos.
  - Recomendações.

---

## RF-S1 — Sincronização Web/App
- Sessões independentes, mas sincronizadas.
- Um lance enviado pelo app deve aparecer instantaneamente na web.
- Favoritos devem sincronizar automaticamente.
- Histórico é unificado.

---

## RF-P1 — Pagamentos (Futuro)
- O app deve permitir pagamento nativo (Pix/Apple Pay/Google Pay).
- Pagamentos devem refletir no backend e web normalmente.

---

## RF-E1 — Conectividade e Offline (App)
- App deve exibir modo offline quando não houver internet.
- Deve permitir navegação parcial em:
  - Favoritos.
  - Histórico.
  - Leilões visitados recentemente (cache).
- Lances não podem ser enviados offline (óbvio), e o app deve alertar.

---

## RF-T1 — Telemetria e Observabilidade (App/Web)
- App deve enviar métricas:
  - Latência de lance.
  - Performance do websocket.
  - Tempo de visualização por leilão.
- Eventos devem ser compatíveis com o backend utilizado na versão web.

---

## RF-C1 — Comunicação com o Vendedor (Web/App)
- Chat assíncrono:
  - Dúvidas sobre item.
  - Notificações quando vendedor responder.

---

## RF-SEG1 — Segurança (App/Web)
- MFA opcional.
- Detecção de comportamento suspeito.
- Proteção contra automação de lances.
- Criptografia local para tokens.

