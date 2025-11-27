# 🧱 Ordenação do Backlog + Revisão dos Épicos e Histórias

A ideia aqui é garantir que o MVP nasce firmeza, sem gambiarra, sem complexidade desnecessária e na ordem certa.

---

# 1. Ordenação Sugerida do Backlog

1. **Tema Técnico Base (Enablers Core)**
   - Arquitetura
   - Segurança
   - CI/CD
   - Infra básica

2. **Tema: Identidade & Acesso**
   - Registro
   - Login
   - Perfis e permissões

3. **Tema: Estrutura de Leilões (MVP Core)**
   - Criar leilão
   - Listar leilões
   - Participar / Lances
   - Encerramento

4. **Tema: Transações & Pagamentos (MVP+)**
   - Ordem de compra
   - Pagamento manual
   - Aprovação do admin

5. **Tema: Administração**
   - Usuários
   - Leilões
   - Logs

6. **Tema: Diferenciais (pós-MVP)**
   - IA
   - Automação
   - Antifraude
   - Notificações avançadas

---

# 2. Épicos Revisados e Detalhados

## 🎯 Épico 1 — Fundamentos Técnicos & Arquitetura
### Escopo
- Setup de repositórios
- Setup infra (dev/stage/prod)
- BFF + Backend + Front Angular operacionais
- Observabilidade e logs
- Ambientes configurados

### Regras
- Tudo versionado
- Pipelines com gates mínimos
- Monitoramento desde o início

---

## 🎯 Épico 2 — Autenticação, Perfis & Controle de Acesso
### Escopo
- Registro
- Login
- Fluxo de e-mail
- Perfis e papéis
- Proteção das rotas

### Regras
- Visitante: só navega
- Participante: participa e compra
- Vendedor: cria e gerencia seus leilões
- Admin: geral

---

## 🎯 Épico 3 — Catálogo de Leilões
### Escopo
- Listagem
- Filtros
- Busca
- Detalhes
- Estado dos leilões

### Regras
- Visitante vê tudo
- Participante marca interesse
- Vendedor vê estatísticas próprias

---

## 🎯 Épico 4 — Operação de Leilões
### Escopo
- Criar leilão
- Editar rascunho
- Publicar
- Lances
- Incrementos
- Encerramento
- Definição do vencedor

### Regras do Lance
- Valor > lance atual
- Incremento mínimo
- Não pode dar lance no próprio leilão
- Histórico registrado

---

## 🎯 Épico 5 — Pagamentos
### Escopo
- Ordem de compra
- Pagamento manual
- Aprovação

### Regras
- Prazo para pagamento
- Falha = bloqueio
- Liberação após pagamento

---

## 🎯 Épico 6 — Administração / Backoffice
### Escopo
- Painel admin
- Usuários
- Ações disciplinares
- Logs
- Auditoria

### Regras
- Tudo que admin faz é auditado

---

## 🎯 Épico 7 — Diferenciais (pós-MVP)
- IA para recomendação
- Lances automáticos
- Antifraude
- Notificações avançadas

---

# 3. Histórias Detalhadas (Enablers + Funcionais)

---

# 🧱 ENABLERS — Épico 1

### **E1.1 — Criar repositórios e branch strategy**
- Repos para FE/BFF/BE
- Branches protegidas

### **E1.2 — Configurar CI/CD inicial**
- Build
- Test
- Deploy dev automático

### **E1.3 — Definir arquitetura em camadas**
- C4 atualizado
- Guidelines técnicas aprovadas

### **E1.4 — Observabilidade**
- Logs padronizados
- Correlation ID
- Health-check

---

# 👤 FUNCIONAIS — Épico 2 (Identidade & Acesso)

### **A2.1 — Registro**
- E-mail único
- Senha forte
- Confirmação por e-mail

### **A2.2 — Login**
- JWT com expiração
- Bloqueio após tentativas

### **A2.3 — Gestão de Perfis**
- Admin promove/rebaixa usuário
- Bloqueios

---

# 📦 Épico 3 — Catálogo

### **C3.1 — Listar leilões**
- Filtros: categoria, preço, status, vendedor

### **C3.2 — Visualizar detalhes**
- Histórico
- Tempo restante
- Lances

---

# 🎛️ Épico 4 — Operação de Leilões

### **L4.1 — Criar leilão**
- Não pode publicar sem dados obrigatórios
- Pode salvar rascunho

### **L4.2 — Publicar leilão**
- Não pode editar dados principais depois

### **L4.3 — Dar lance**
- Valor > atual
- Incremento mínimo
- Sem autopreenchimento
- Sem lance no próprio leilão

### **L4.4 — Encerramento**
- Automático por scheduler
- Manual por admin

---

# 💳 Épico 5 — Pagamentos

### **P5.1 — Ordem de compra**
- Vencedor recebe
- Válida por X dias

### **P5.2 — Registro de pagamento**
- Admin registra
- Bloqueios possíveis

---

# 🛠️ Épico 6 — Administração

### **ADM6.1 — Listar usuários**
### **ADM6.2 — Bloquear usuário**
### **ADM6.3 — Logs de auditoria**

---

# 🌟 Épico 7 — Diferenciais (Pós-MVP)

### **D7.1 — Lances automáticos com IA**
### **D7.2 — Antifraude**
### **D7.3 — Recomendação personalizada**

---

# 4. Quer que eu gere agora o plano de sprints baseado nesse backlog?
Só pedir: **"gera as sprints"**.
