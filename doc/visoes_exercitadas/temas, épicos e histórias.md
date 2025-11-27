# 🧩 Backlog Completo – Temas, Épicos e Histórias (Funcionais + Enablers)

Este documento representa a conversão integral da documentação funcional do Sistema de Leilão Eletrônico para um backlog estruturado no formato:

**Temas → Épicos → Histórias Funcionais / Enablers Técnicos**

---

# 🎯 TEMA 1 — Identidade, Acesso e Segurança

## Épico 1.1 — Cadastro e Gestão de Contas

### Histórias Funcionais
- **HIST-US-001 — Criar conta como Visitante**  
  Permitir que visitantes criem uma conta informando dados básicos.

- **HIST-US-002 — Validar e-mail (primeiro fator)**  
  Envio de e-mail com link/token para validação.

- **HIST-US-003 — Validar documento (segundo fator)**  
  Upload de documento + selfie para validação manual/automática.

- **HIST-US-004 — Completar perfil obrigatório**  
  Endereço, telefone e dados fiscais antes da ativação completa.

### Enablers
- **EN-SEC-001 — Módulo de autenticação (OAuth2 / Keycloak / Cognito).**
- **EN-SEC-002 — Pipeline de validação de documentos (OCR + antifraude).**
- **EN-SEC-003 — Implementação de MFA (e-mail/SMS/app).**
- **EN-SEC-004 — Segurança de API: rate limit, audit log, encryption.**

---

## Épico 1.2 — Login, Sessões e Perfis

### Histórias Funcionais
- **HIST-US-010 — Login com MFA.**
- **HIST-US-011 — Recuperação de senha.**
- **HIST-US-012 — Edição de perfil.**
- **HIST-US-013 — Admin gerencia status e auditoria de perfis.**

### Enablers
- **EN-SEC-010 — Uso de JWT compartilhado entre MFE + BFF + Microsserviços.**
- **EN-SEC-011 — RBAC completo (Visitante, Participante, Vendedor, Administrador).**

---

# 🎯 TEMA 2 — Gestão de Vendedores e Contratos

## Épico 2.1 — Cadastro de Vendedores

### Histórias Funcionais
- **HIST-VEN-001 — Cadastrar empresa vendedora.**
- **HIST-VEN-002 — Validar CNPJ e documentação societária.**
- **HIST-VEN-003 — Cadastrar representantes legais.**
- **HIST-VEN-004 — Configurar parâmetros comerciais (taxa do leiloeiro, SLA, política).**

### Enablers
- **EN-VEN-001 — Integração para validação de CNPJ.**
- **EN-VEN-002 — Motor de regras comerciais para taxas.**

---

# 🎯 TEMA 3 — Catálogo: Produtos e Lotes

## Épico 3.1 — Cadastro de Produtos

### Histórias Funcionais
- **HIST-PROD-001 — Criar produto.**
- **HIST-PROD-002 — Definir preço inicial e incremento mínimo.**
- **HIST-PROD-003 — Upload de fotos/vídeos/documentos.**
- **HIST-PROD-004 — Definir data/hora individual de encerramento.**
- **HIST-PROD-005 — Associar produto a lote.**

---

## Épico 3.2 — Gestão de Lotes

### Histórias Funcionais
- **HIST-LOTE-001 — Criar lote.**
- **HIST-LOTE-002 — Definir data/hora de encerramento.**
- **HIST-LOTE-003 — Controlar status (rascunho, publicado, encerrado).**
- **HIST-LOTE-004 — Vincular produtos.**
- **HIST-LOTE-005 — Dashboard de lotes para admin/vendedor.**

### Enablers
- **EN-CAT-001 — MFE de gestão do catálogo (Angular 18).**
- **EN-CAT-002 — Microsserviço Catálogo (CRUD + regras de encerramento).**
- **EN-CAT-003 — Storage de mídias (S3).**

---

# 🎯 TEMA 4 — Divulgação e Navegação dos Leilões

## Épico 4.1 — Busca e Exploração

### Histórias Funcionais
- **HIST-CAT-010 — Listar lotes ativos.**
- **HIST-CAT-011 — Listar produtos por lote.**
- **HIST-CAT-012 — Busca e filtros avançados.**
- **HIST-CAT-013 — Ver detalhes do produto.**

---

## Épico 4.2 — Informação em Tempo Real

### Histórias Funcionais
- **HIST-RT-001 — Atualização real-time de preço via SSE/WebSocket.**
- **HIST-RT-002 — Indicação visual de disputa.**
- **HIST-RT-003 — Últimos lances em tempo real.**

### Enablers
- **EN-RT-001 — Canal SSE/WebSocket com suporte a cluster.**
- **EN-RT-002 — Balanceamento sticky session (se WS).**

---

# 🎯 TEMA 5 — Lances e Arremates

## Épico 5.1 — Registro de Lances

### Histórias Funcionais
- **HIST-LAN-001 — Dar lance manual.**
- **HIST-LAN-002 — Validar lance.**
- **HIST-LAN-003 — Notificar participantes do lote.**
- **HIST-LAN-004 — Registrar histórico.**
- **HIST-LAN-005 — Garantir atomicidade do lance vencedor.**

---

## Épico 5.2 — Fechamento / Arremate

### Histórias Funcionais
- **HIST-ARR-001 — Encerrar produto automaticamente.**
- **HIST-ARR-002 — Encerrar lote quando todos produtos finalizarem.**
- **HIST-ARR-003 — Notificar vencedor e vendedor.**
- **HIST-ARR-004 — Emitir confirmação de arremate.**

### Enablers
- **EN-LAN-001 — Motor de concorrência com fila/lock.**
- **EN-LAN-002 — Scheduler distribuído.**

---

# 🎯 TEMA 6 — Pagamentos, Frete e Pós-Venda

## Épico 6.1 — Pagamentos

### Histórias Funcionais
- Processar pagamento.
- Aplicar taxa do leiloeiro.
- Gerar comprovantes.

## Épico 6.2 — Frete

### Histórias Funcionais
- Calcular frete via API externa.
- Exibir opções ao comprador.

## Épico 6.3 — Pós-Venda

### Histórias Funcionais
- Confirmar entrega.
- Abrir disputas.
- Avaliar vendedores.

### Enablers
- **EN-PAY-001 — Gateway de pagamento.**
- **EN-FRETE-001 — Integração de frete.**
- **EN-POS-001 — Workflow de disputas.**

---

# 🎯 TEMA 7 — Administração do Sistema

## Épico 7.1 — Painel Administrativo

### Histórias Funcionais
- Gerenciar usuários.
- Gerenciar vendedores.
- Administrar lotes/produtos.
- Acessar logs e auditorias.
- Configurar taxas.

## Épico 7.2 — Monitoramento e Auditoria

### Enablers
- **EN-ADM-001 — Microsserviço Admin.**
- **EN-OBS-001 — Observabilidade (logs/metrics/tracing).**

---

# 🎯 TEMA 8 — Diferenciais Futuramente Planejados (Não-MVP)

## Épicos
- FlashBid (oferta relâmpago)
- Lances automáticos inteligentes (bot)
- Gamificação
- Live streaming do leilão
- Recomendação personalizada (ML)
- Antifraude em tempo real
- Chat comprador-vendedor

### Enablers
- Engine de recomendação
- WebRTC (live)
- Motor de gamificação

---

# ✔ Fim do Backlog Markdown
