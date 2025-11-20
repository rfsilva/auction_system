# 🧩 BACKLOG COMPLETO — TEMAS, ÉPICOS, HISTÓRIAS E ENABLERS

---

# 🟪 **TEMA 1 — Experiência do Visitante**
Garantir que o visitante (não autenticado) consiga navegar, visualizar itens, acessar catálogo, filtros e acompanhar leilões em andamento.

---

## 🟥 **Épico 1.1 — Navegação e Catálogo Público**
Permitir ao visitante explorar a plataforma sem autenticação.

### 🟦 História 1.1.1 — Visualizar catálogo geral
**Como** visitante  
**Quero** visualizar a lista pública de itens  
**Para** explorar o marketplace antes de me registrar

**Critérios de Aceite**
- Deve exibir lista paginada.
- Deve incluir imagem, título, preço inicial, status.
- Deve respeitar filtros públicos.
- Deve funcionar sem login.

---

### 🟦 História 1.1.2 — Buscar itens
**Como** visitante  
**Quero** buscar itens por palavra-chave  
**Para** encontrar conteúdo relevante

**Critérios de Aceite**
- Deve buscar por título, descrição e categoria.
- Deve retornar em até 2 segundos.
- Deve funcionar para visitantes e usuários logados.

---

### 🟦 História 1.1.3 — Filtrar itens
**Como** visitante  
**Quero** aplicar filtros (categoria, preço, status do leilão)  
**Para** refinar minha navegação

**Critérios de Aceite**
- Filtros aplicáveis combinados.
- Filtros resetáveis.
- Deve refletir no URL para compartilhamento.

---

## 🟥 **Épico 1.2 — Acompanhamento de Leilões em Tempo Real (Visitante)**

### 🟦 História 1.2.1 — Receber atualizações via SSE
**Como** visitante  
**Quero** ver atualizações ao vivo de lances  
**Para** acompanhar a dinâmica dos leilões

**Critérios de Aceite**
- SSE deve entregar eventos de *novo lance*, *alteração de status*, *encerramento*.
- Tela deve atualizar automaticamente.
- Não requer login.

---

### 🟦 História 1.2.2 — Visualizar página de item com status ao vivo
**Como** visitante  
**Quero** acessar a página do item  
**Para** entender o andamento do leilão

**Critérios de Aceite**
- Deve mostrar tempo restante, preço atual, histórico visível.
- Deve integrar SSE/WebSocket para atualização de preço em tempo real.

---

## 🟧 Enablers para o Tema 1
- **EN-01:** Setup de SSE no BFF.
- **EN-02:** Canal público WebSocket read-only (broadcast).
- **EN-03:** Indexação no backend (Elastic ou PostgreSQL FTS).
- **EN-04:** Paginação performática no catálogo (cursor-based).

---

# 🟪 **TEMA 2 — Gestão de Contas e Perfis**

---

## 🟥 **Épico 2.1 — Autenticação**
### História 2.1.1 — Cadastro de usuário
### História 2.1.2 — Login / Logout
### História 2.1.3 — Recuperação de senha
*(segue padrões já definidos na parte funcional)*

---

## 🟥 **Épico 2.2 — Perfis e Papéis**
### História 2.2.1 — Atualizar perfil
### História 2.2.2 — Upgrade de Visitante → Participante
### História 2.2.3 — Validação documental (se aplicável)

---

## 🟧 Enablers do Tema 2
- **EN-07:** JWT compartilhado entre Front, BFF e Backend
- **EN-08:** Middleware RBAC no BFF
- **EN-09:** Fluxo de refresh token seguro

---

# 🟪 **TEMA 3 — Experiência do Comprador**

---

## 🟥 **Épico 3.1 — Exploração e Qualificação de Itens**
### História 3.1.1 — Favoritar item
### História 3.1.2 — Criar alertas de novos itens
### História 3.1.3 — Receber notificações WebSocket de mudanças relevantes

---

## 🟥 **Épico 3.2 — Participação em Leilões**

### 🟦 História 3.2.1 — Dar lance
**Como** comprador  
**Quero** enviar um lance  
**Para** tentar ganhar o item

**Critérios de Aceite**
- Não pode enviar lance abaixo do mínimo.
- Backend valida atomicamente.
- Atualização via WebSocket/SSE para todos.
- Confirmação imediata.

---

### 🟦 História 3.2.2 — Auto-lance (configurar limite)
**Como** comprador  
**Quero** configurar um limite máximo  
**Para** automatizar meus lances

**Critérios de Aceite**
- Backend deve gerenciar fila de auto-lances.
- Deve respeitar incrementos mínimos.
- Deve notificar usuário sobre ultrapassagem.

---

### 🟦 História 3.2.3 — Cancelamento de lance (se permitido)
**Como** comprador  
**Quero** cancelar um lance recente  
**Para** corrigir erros

**Critérios de Aceite**
- Só permitido enquanto regras do leilão permitirem.
- Histórico preservado (auditoria).

---

## 🟥 **Épico 3.3 — Pós-Leilão (Comprador)**

### História 3.3.1 — Efetivar pagamento
### História 3.3.2 — Assinar termo digital
### História 3.3.3 — Acompanhar entrega / retirada

---

## 🟧 Enablers do Tema 3
- **EN-15:** Motor de priorização de lances (fila/lock otimista).
- **EN-16:** Worker de auto-lance.
- **EN-17:** WebSocket com canal privado autenticado.
- **EN-18:** Logs de auditoria imutáveis.

---

# 🟪 **TEMA 4 — Experiência do Vendedor**

---

## 🟥 **Épico 4.1 — Gestão de Inventário**

### História 4.1.1 — Cadastrar item
### História 4.1.2 — Editar item
### História 4.1.3 — Publicar item para revisão
### História 4.1.4 — Gerenciar fotos, categorias, atributos

---

## 🟥 **Épico 4.2 — Criação e Gestão de Leilões**

### História 4.2.1 — Configurar parâmetros do leilão  
(duração, preço inicial, incremento mínimo, reserva)

### História 4.2.2 — Pausar / retomar leilão
### História 4.2.3 — Cancelar leilão
### História 4.2.4 — Acessar painel em tempo real (WebSocket)

---

## 🟥 **Épico 4.3 — Pós-venda do Vendedor**
### História 4.3.1 — Avaliar comprador
### História 4.3.2 — Agendar retirada/entrega
### História 4.3.3 — Repassar informações fiscais

---

## 🟧 Enablers do Tema 4
- **EN-21:** Workflow de aprovação de itens
- **EN-22:** Scheduler para abertura/encerramento automático de leilões
- **EN-23:** Template engine para avisos/alertas
- **EN-24:** Canal WebSocket exclusivo para vendedores

---

# 🟪 **TEMA 5 — Governança e Administração**

---

## 🟥 **Épico 5.1 — Moderação e Auditoria**

### História 5.1.1 — Moderação de itens
### História 5.1.2 — Moderação de usuários
### História 5.1.3 — Acesso ao log de auditoria

---

## 🟥 **Épico 5.2 — Gestão geral da plataforma**

### História 5.2.1 — Painel administrativo com métricas
### História 5.2.2 — Gestão de categorias / regras
### História 5.2.3 — Configurar políticas do sistema

---

## 🟥 **Épico 5.3 — Administração de Leilões**

### História 5.3.1 — Encerramento manual
### História 5.3.2 — Intervenção em lances suspeitos
### História 5.3.3 — Regras antifraude configuráveis

---

## 🟧 Enablers do Tema 5
- **EN-31:** Motor de auditoria centralizado
- **EN-32:** Detecção de fraude via regras/heurísticas
- **EN-33:** Painel Admin (React) separado por RBAC

---

# 🟪 **TEMA 6 — Integrações e Infraestrutura**

---

## 🟥 **Épico 6.1 — Integração com Pagamentos**
### História 6.1.1 — Criar ordem de pagamento
### História 6.1.2 — Processar retorno webhook
### História 6.1.3 — Confirmar pagamento no sistema

---

## 🟥 **Épico 6.2 — Notificações**
### História 6.2.1 — Integração com push notifications
### História 6.2.2 — E-mail transactional
### História 6.2.3 — SMS/WhatsApp (futuro)

---

## 🟧 Enablers
- **EN-41:** Gateway de pagamentos
- **EN-42:** Fila de mensagens
- **EN-43:** Serviço de templates de notificação

---

# 🟪 **TEMA 7 — Qualidade, Segurança e Observabilidade**

---

## 🟥 Épico 7.1 — Segurança da Aplicação
### História 7.1.1 — OWASP enforcement
### História 7.1.2 — Rate limiting
### História 7.1.3 — Proteção contra automação de lances

---

## 🟥 Épico 7.2 — Observabilidade
### História 7.2.1 — Métricas Prometheus
### História 7.2.2 — Logs estruturados
### História 7.2.3 — Dashboard Grafana

---

## 🟥 Épico 7.3 — Performance
### História 7.3.1 — Cache de leitura
### História 7.3.2 — CDN para imagens
### História 7.3.3 — Tuning de banco

---

## 🟧 Enablers
- **EN-51:** Nginx + CDN
- **EN-52:** OpenTelemetry
- **EN-53:** Sharding/Partition em lances intensos

