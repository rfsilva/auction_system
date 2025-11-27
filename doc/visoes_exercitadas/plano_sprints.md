# 🏁 Plano de Execução — Sprints do MVP  
**Time:**  
- 1 Desenvolvedor Sênior  
- 1 Desenvolvedor Pleno  
- 1 Arquiteto (suporte contínuo)

**Sprint padrão:** 2 semanas  
**Horizonte do MVP:** 8 Sprints (≈ 4 meses)

---

# 📌 Sprint 1 — Fundamentos Técnicos + Arquitetura Base
### Objetivo
Criar o esqueleto técnico estável para suportar o MVP sem retrabalho.

### Entregáveis
- Estrutura de repositórios (FE, BFF, BE)
- Branch strategy + proteções (main/dev)
- CI/CD básico para os 3 componentes
- Ambiente dev: Docker Compose local
- Primeiras rotas de health-check
- Gateways configurados para roteamento base
- ADRs essenciais e definição final da arquitetura

### Histórias / Enablers
- E1.1 Criar repositórios
- E1.2 Configurar pipelines iniciais
- E1.3 Implementar arquitetura base no BE
- E1.4 Implementar BFF com rota teste
- E1.5 Implementar FE Shell + MFE base

---

# 📌 Sprint 2 — Identidade & Autenticação (Parte 1)
### Objetivo
Fechar backend + BFF + FE com fluxo mínimo de autenticação e segurança.

### Entregáveis
- Registro de usuário (BE)
- Confirmação de e-mail (mock)
- Login com JWT (BE)
- BFF validando token
- Frontend com formulários iniciais de login/registro
- Controle de sessão
- Proteção de rotas no FE

### Histórias / Enablers
- A2.1 Registro
- A2.2 Login
- A2.3 JWT + Refresh strategy
- FE-Auth01 Form de Login/Registro
- FE-Auth02 Guards + Interceptors
- BFF-Auth01 Pass-through + validação

---

# 📌 Sprint 3 — Perfis e Permissões + Navegação Inicial
### Objetivo
Estabelecer a base de perfis e autorizações e criar a estrutura de navegação.

### Entregáveis
- Perfis: visitante, participante/comprador, vendedor, administrador
- Política por perfil no BE
- Middleware de papéis no BFF
- Menu e navegação dinâmica no FE
- Página inicial (Catálogo placeholder)

### Histórias
- A2.3 Gestão de perfis
- A2.4 Middleware de permissões
- FE-Nav01 Shell + roteamento
- FE-Nav02 Menu baseado em papel

---

# 📌 Sprint 4 — Catálogo de Leilões (Listagem e Detalhes)
### Objetivo
Disponibilizar consulta pública aos leilões — requisito central do MVP.

### Entregáveis
- Cadastrar categorias (admin)
- Listagem de leilões
- Detalhe do leilão
- Filtros + paginação
- BFF agregando dados e padronizando respostas

### Histórias
- C3.1 Listar leilões
- C3.2 Filtros
- C3.3 Detalhes do leilão
- BFF-Catalog01 Agregação
- FE-Catalog01 Tela de listagem
- FE-Catalog02 Tela de detalhes

---

# 📌 Sprint 5 — Criação e Publicação de Leilões (Vendedor)
### Objetivo
Permitir que vendedores comecem a operar leilões.

### Entregáveis
- Criar leilão (rascunho)
- Editar rascunho
- Publicar leilão
- Validações de regra obrigatórias
- Interface mínima para vendedores

### Histórias
- L4.1 Criar leilão
- L4.2 Editar rascunho
- L4.3 Publicar
- FE-Lot01 Tela de criação
- FE-Lot02 Tela de edição
- BFF-Lot01 Validações

---

# 📌 Sprint 6 — Lances (Tempo Real via SSE ou WebSocket)
### Objetivo
Habilitar o coração do sistema: disputa de lances em tempo real.

### Entregáveis
- Modelo de lances no BE
- Validações (incremento, bloqueio, auto-lance proibido)
- Feed em tempo real (SSE)
- BFF repassando eventos
- FE atualizando tela sem refresh

### Histórias
- L4.3 Dar lance
- L4.4 Validações do lance
- L4.5 Histórico
- RTX01 SSE backend
- RTX02 Listener no BFF
- FE-Lance01 Subscription SSE
- FE-Lance02 Atualização dinâmica

---

# 📌 Sprint 7 — Encerramento, Definição do Vencedor e Ordens de Compra
### Objetivo
Fechar o ciclo de um leilão.

### Entregáveis
- Scheduler de encerramento
- Regras de encerramento (produtos com horário próprio)
- Definição do vencedor
- Geração da ordem de compra
- Tela do comprador com suas vitórias

### Histórias
- L4.6 Encerramento automático
- P5.1 Ordem de compra
- FE-Pedido01 Tela “meus arremates”
- BFF-Pedido01 Agregação de dados

---

# 📌 Sprint 8 — Administração + Auditoria (Fechamento do MVP)
### Objetivo
Dar autonomia à operação.

### Entregáveis
- Painel do administrador
- Gestão de usuários
- Suspensões e desbloqueios
- Logs de auditoria
- Monitoramento básico

### Histórias
- ADM6.1 Usuários
- ADM6.2 Bloqueio
- ADM6.3 Auditoria
- FE-ADM01 Painel
- FE-ADM02 Ações moderadoras

---

# 📌 Pós-MVP — Sprints Futuras (Tema Diferenciais)
**Somente depois que o MVP estiver validado:**
- IA de recomendação
- Lances automáticos (sniper bot)
- Antifraude
- Notificações omnichannel
- Integração real com pagamentos
- Ranking de vendedores e compradores
- Área de fidelização

