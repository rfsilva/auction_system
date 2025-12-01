# 🟧 Sprint S12 — Refinamento Detalhado
**Objetivo da Sprint:**  
Estabelecer as bases técnicas, visuais e de navegação do aplicativo mobile, habilitar autenticação completa (login, recuperar senha, sessão persistida) e garantir que o time consiga lançar builds internas para testes.

**Pilares da Sprint:**  
- Estrutura do app (arquitetura, navegação, tema, estados)  
- Autenticação básica  
- Onboarding inicial  
- Setup de CI/CD  
- Configuração inicial do FCM  
- Primeira versão installable pelos testers  

---

# 📌 HISTÓRIA H-APP-001 — Splash Screen + Validação de Versão
**Como** Visitante  
**Quero** visualizar uma splash screen e ter a versão do app validada automaticamente  
**Para** garantir que estou usando a versão mais atual ou ser instruído a atualizar.

### Critérios de Aceite
- [ ] A splash screen deve aparecer ao abrir o app, com logo oficial.  
- [ ] O backend deve confirmar se a versão instalada é válida.  
- [ ] Caso a versão seja inválida, deve exibir modal obrigatório redirecionando para loja.  
- [ ] Caso válida, o usuário segue para onboarding ou login.  
- [ ] Deve ter fallback offline (mensagem genérica se sem internet).  

### Tasks
- [ ] Criar splash screen estática (Flutter Native Launch + primeiro frame)  
- [ ] Criar serviço de "versionCheck" (API `/app/version`)  
- [ ] Implementar fluxo de fallback offline  
- [ ] Criar modal de atualização obrigatória  
- [ ] Conectar com backend (mock ou real no ambiente dev)  
- [ ] Testes manuais + ajustes responsivos  

### Tamanho (Planning Poker): **3 pontos**

---

# 📌 HISTÓRIA H-APP-002 — Onboarding Básico
**Como** Visitante  
**Quero** visualizar telas de onboarding  
**Para** entender rapidamente o funcionamento da plataforma.

### Critérios de Aceite
- [ ] Onboarding deve ter entre 2 e 4 telas.  
- [ ] Deve ter botões “Próximo”, “Pular” e “Começar Agora”.  
- [ ] Deve rodar somente na primeira abertura (usar flag local).  
- [ ] Após onboarding → vai para tela de login.  
- [ ] Layout deve seguir guideline visual do produto Web.  

### Tasks
- [ ] Criar componente de onboarding com swipe horizontal  
- [ ] Criar flag local (Hive/SharedPreferences) para exibir somente uma vez  
- [ ] Implementar botões Pular/Próximo  
- [ ] Criar telas com copy aprovado (placeholder inicialmente)  
- [ ] Navegação pós-onboarding → login  
- [ ] Testes manuais + UX review  

### Tamanho (Planning Poker): **5 pontos**

---

# 📌 HISTÓRIA H-APP-003 — Login via API
**Como** Usuário cadastrado  
**Quero** realizar login usando meu e-mail e senha  
**Para** acessar o app e participar dos leilões.

### Critérios de Aceite
- [ ] Deve validar e-mail e senha no backend.  
- [ ] Deve exibir mensagens corretas de erro:  
  - credenciais inválidas  
  - conta desativada  
  - servidor indisponível  
- [ ] Token JWT deve ser armazenado com segurança.  
- [ ] Após login → direcionar para catálogo (Sprint 13).  
- [ ] Loading e bloqueio de UI durante requisição.  

### Tasks
- [ ] Criar tela de login  
- [ ] Criar formulário com validação (email válido, senha não vazia)  
- [ ] Criar provider/BLoC para autenticação  
- [ ] Integrar com API `/auth/login`  
- [ ] Salvar token em local storage seguro  
- [ ] Testes de erro: offline, senha errada, etc.  
- [ ] Navegação pós-login  

### Tamanho (Planning Poker): **5 pontos**

---

# 📌 HISTÓRIA H-APP-004 — Recuperação de Senha
**Como** Usuário  
**Quero** solicitar recuperação de senha  
**Para** redefinir o acesso caso eu esqueça minha senha.

### Critérios de Aceite
- [ ] Tela deve solicitar e-mail.  
- [ ] API deve enviar instruções de redefinição (já existe no Web).  
- [ ] Deve exibir feedback claro de sucesso ou falha.  
- [ ] Caso o e-mail não exista → mensagem genérica.  
- [ ] Layout compatível com o login.  

### Tasks
- [ ] Criar tela de recuperação  
- [ ] Formulário com validação de email  
- [ ] Integrar com API `/auth/recover`  
- [ ] Criar tela de feedback de sucesso  
- [ ] Fluxo de navegação pós-sucesso  
- [ ] Testes diversos  

### Tamanho (Planning Poker): **3 pontos**

---

# 📌 HISTÓRIA H-APP-005 — Persistência do Token + Sessão Segura
**Como** Usuário autenticado  
**Quero** que minha sessão permaneça ativa  
**Para** não precisar logar sempre que abrir o app.

### Critérios de Aceite
- [ ] Token deve sempre ser carregado no boot do app.  
- [ ] Se token expirou → redirecionar para login.  
- [ ] Deve suportar renovação de token (se backend permitir).  
- [ ] O app não pode quebrar mesmo se storage corromper.  
- [ ] Tokens devem ser salvos com criptografia no storage.  

### Tasks
- [ ] Implementar Secure Storage (Flutter EncryptedSharedPreferences / flutter_secure_storage)  
- [ ] Criar bootstrap que checa token no app start  
- [ ] Validar expiração do token  
- [ ] Implementar flush seguro em caso de token inválido  
- [ ] Cobrir fluxo de logout automático  

### Tamanho (Planning Poker): **5 pontos**

---

# ⚙️ ENABLERS S12

---

## 🛠️ EN-APP-01 — Setup do Repositório Mobile
**Descrição:**  
Criar repositório do app, padrões de branch, linters, padrões arquiteturais.

### Tasks
- [ ] Criar repo Git  
- [ ] Configurar pipeline básico (build/check)  
- [ ] Configurar padrões: commitlint, husky, formatters  
- [ ] Criar estrutura dos módulos: core, features, shared  

### Tamanho: **3 pontos**

---

## 🛠️ EN-APP-02 — CI/CD (Firebase App Distribution)
### Critérios de Aceite
- [ ] Ao fazer merge na branch `develop`, deve gerar build interna.  
- [ ] Testers devem receber build automaticamente.  
- [ ] Logs de CI devem ficar acessíveis.  

### Tasks
- [ ] Configurar Fastlane ou Codemagic  
- [ ] Integração com Firebase App Distribution  
- [ ] Criar pipeline para Android  
- [ ] Criar pipeline para iOS (se disponível nesta etapa)  

### Tamanho: **5 pontos**

---

## 🛠️ EN-APP-03 — Base de Arquitetura Mobile
### Objetivo
Criar estrutura Clean Architecture + state manager oficial (BLoC ou Riverpod).

### Tasks
- [ ] Criar diretórios: `domain`, `data`, `presentation`  
- [ ] Implementar injeção de dependências  
- [ ] Criar modelos genéricos de erro/resultado  
- [ ] Criar tema global + tipografia  
- [ ] Criar rota base (router)  

### Tamanho: **5 pontos**

---

## 🛠️ EN-APP-04 — Configuração Inicial do FCM
### Critérios
- [ ] App recebe token FCM com sucesso.  
- [ ] Token é enviado ao backend após login (placeholder por enquanto).  
- [ ] Deve funcionar tanto Android quanto iOS (se aplicável).  

### Tasks
- [ ] Configurar Firebase no projeto  
- [ ] Implementar request de permissão no iOS  
- [ ] Criar listener de recebimento de token  
- [ ] Armazenar token localmente  
- [ ] Disponibilizar serviço para enviar token ao backend  

### Tamanho: **3 pontos**

---

# 📊 **Resumo do Tamanho da Sprint S12**
| Item | Pontos |
|------|--------|
| H-APP-001 | 3 |
| H-APP-002 | 5 |
| H-APP-003 | 5 |
| H-APP-004 | 3 |
| H-APP-005 | 5 |
| EN-APP-01 | 3 |
| EN-APP-02 | 5 |
| EN-APP-03 | 5 |
| EN-APP-04 | 3 |
| **Total Estimado** | **37 pontos** |

> Sprint está **no limite**, mas para um dev sênior + um pleno é **viável**, dado que metade é base técnica que anda rápido nas primeiras semanas.

---

Se quiser agora, trutão, já mando **a Sprint 13** refinada no mesmo formato!
