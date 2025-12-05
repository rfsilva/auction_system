# 🏁 Sprint 14 — Conversão Web → App + Onboarding Mobile (S14)
**Duração:** 2 semanas  
**Equipe:** Mobile + Backend + UX  
**Objetivo:** Permitir que usuários da versão web migrem para o app com fluxo suave, usando QR Code e onboarding específico, incluindo dashboard inicial e push básico.

---

# 🔥 Histórias Refinadas

---

## **H14.1 — Criar endpoint de “assinatura de device” (vincular app ao usuário Web)**
**Descrição:** Registrar o device (ID + sistema + versão do app) vinculado ao usuário logado no app.

### Tarefas
- [ ] Criar tabela `user_device`
- [ ] Criar endpoint `POST /api/mobile/device/register`
- [ ] Validar token JWT Web ou App
- [ ] Salvar device + sistema + versão
- [ ] Logar tentativas duplicadas
- [ ] Criar evento `UserDeviceRegistered`

### Critérios de Aceite
- Aceitar tokens válidos (Web/App)
- Device duplicado não cria novo registro
- Logs estruturados
- Auditoria Envers habilitada

### Tamanho
**5 pontos**

---

## **H14.2 — Criar endpoint de “transferência de sessão” Web → App**
**Descrição:** Gerar token temporário via QR Code no web, app escaneia e ativa sessão automaticamente.

### Tarefas
- [ ] Criar endpoint `POST /api/mobile/session/transfer`
- [ ] Gerar token temporário (TTL: 60s)
- [ ] Validar QR Code
- [ ] Trocar token por JWT padrão
- [ ] Revogar token após uso
- [ ] Auditar operações

### Critérios de Aceite
- QR Code só pode ser usado 1 vez
- Expiração em 60s
- QR inválido → 401

### Tamanho
**8 pontos**

---

## **H14.3 — Criar tela de leitura de QR Code no app**
**Descrição:** Scanner nativo no app para ler QR gerado no web.

### Tarefas
- [ ] Implementar scanner nativo (Android/iOS)
- [ ] Permissão de câmera
- [ ] Chamar endpoint de transferência
- [ ] Exibir status (ok/expirado/inválido)
- [ ] Persistir usuário localmente (Keychain/SecureStore)

### Critérios de Aceite
- Scanner abre em ≤1s
- Mensagem amigável para erros
- Login automático ao ler QR válido

### Tamanho
**8 pontos**

---

## **H14.4 — Ajustar fluxo de onboarding do app**
**Descrição:** Diferenciar onboarding para novos usuários e usuários convertidos via QR.

### Tarefas
- [ ] Criar `OnboardingContextService`
- [ ] Detectar origem (QR / login normal / cadastro)
- [ ] Adaptar telas conforme contexto
- [ ] Salvar estado local
- [ ] Ajustar analytics de funil

### Critérios de Aceite
- Usuário vindo via QR cai direto na Home
- Usuário novo passa pelo onboarding completo
- Eventos de analytics disparados

### Tamanho
**5 pontos**

---

## **H14.5 — Criar mini-dashboard mobile do comprador**
**Descrição:** Dashboard compacto consumindo APIs do web.

### Tarefas
- [ ] Criar endpoint `GET /api/mobile/dashboard`
- [ ] Criar widgets:
  - Próximos leilões
  - Últimos lances
  - Status da conta
- [ ] Implementar pull-to-refresh
- [ ] Cache local (5 min)

### Critérios de Aceite
- Carregamento ≤1s com cache
- ≤3s sem cache
- Fallback em caso de erro

### Tamanho
**8 pontos**

---

## **H14.6 — Push notification inicial (evento de boas-vindas)**
**Descrição:** Enviar push básico na primeira autenticação via app.

### Tarefas
- [ ] Integrar Firebase/FCM (Android)
- [ ] Criar serviço de push no backend
- [ ] Registrar token do app em `user_device`
- [ ] Criar evento `SendWelcomePush`
- [ ] Criar template da mensagem

### Critérios de Aceite
- Push chega em até 10s
- Permissão negada → registrar fallback
- Erros logados

### Tamanho
**5 pontos**

---

## **H14.7 — Enabler: Criar camada mobile de DTOs (backend)**
**Descrição:** Padronizar contratos de API específicos do app.

### Tarefas
- [ ] Criar pacote `com.app.dto`
- [ ] Criar `MobileUserDTO`, `SessionTransferDTO`, `MobileDashboardDTO`
- [ ] Implementar mapeamentos
- [ ] Criar testes unitários

### Critérios de Aceite
- Contratos versionados (`/api/mobile/v1/...`)
- OpenAPI atualizado
- Não impactar web

### Tamanho
**3 pontos**

---

## **H14.8 — Enabler: Testes integrados mobile (E2E)**
**Descrição:** Testes E2E focados nos fluxos de sessão e device.

### Tarefas
- [ ] Testar QR expirado
- [ ] Testar troca válida
- [ ] Testar device duplicado
- [ ] Testar auditoria
- [ ] Criar mocks para app

### Critérios de Aceite
- Cobertura mínima 80% no módulo mobile
- Pipeline executando a suíte por completo

### Tamanho
**5 pontos**

---

# 📌 Resumo da Sprint

| Item  | Pontos |
|-------|--------|
| H14.1 | 5 |
| H14.2 | 8 |
| H14.3 | 8 |
| H14.4 | 5 |
| H14.5 | 8 |
| H14.6 | 5 |
| H14.7 | 3 |
| H14.8 | 5 |
| **Total** | **47 pontos** |

