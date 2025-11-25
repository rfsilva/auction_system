# Refinamento Sprint 1 — Sistema de Leilão Eletrônico

**Sprint:** 1  
**Duração:** 2 semanas  
**Dev Pleno + Sênior + Arquiteto**  

## Objetivo da Sprint
Criar fundações do sistema para suportar o MVP, incluindo setup de projetos, arquitetura, módulo de usuários, autenticação, RBAC básico e prova de conceito SSE/WebSocket.

---

## Histórias Detalhadas
- **Regras Gerais** 
  1. Se precisar criar entity nova, localizar primeiro a tabela em V1 do migrations. Se não encontrar, criar migration para criação da tabela.
  2. Se for necessário criar tabela, usar prefixo "tb_" e nome no singular
  3. Não criar estruturas do tipo TYPE, TRIGGER, PROCEDURE, FUNCTION no migrations

### História 1: Setup do projeto backend (modularizado)
- **Tipo:** Enabler
- **Descrição:** Configurar projeto backend em arquitetura modular, pronto para evoluir em microsserviços futuros.
- **Tasks / Sub-tasks:**
  1. Criar a pasta backend
  2. Criar estrutura de pastas modular (core, modules, shared) - 2 SP  
  3. Configurar build tool (Maven/Gradle) - 1 SP  
  4. Configurar base de dependências (Spring Boot, JWT, WebSocket, JPA/Hibernate) - 2 SP  
  5. Criar README inicial e documentação mínima do projeto - 1 SP  
- **Story Points:** 6 SP

### História 2: Setup do projeto frontend Angular
- **Tipo:** Enabler
- **Descrição:** Criar projeto Angular 18 standalone, pronto para consumo do backend.
- **Tasks / Sub-tasks:**
  1. Criar a pasta frontend
  2. Criar projeto Angular com `standalone components` - 1 SP  
  3. Configurar `HttpClient` com `withFetch()` - 1 SP  
  4. Criar layout base e roteamento inicial - 2 SP  
  5. Configurar CI/CD básico para frontend - 2 SP  
- **Story Points:** 6 SP

### História 3: Setup do banco de dados
- **Tipo:** Enabler
- **Descrição:** Criar schema inicial e scripts de migrations.
- **Tasks / Sub-tasks:**
  1. Criar schema principal (usuarios, produtos, leiloes, lances) - 2 SP  
  2. Configurar migrations automáticas (Flyway ou Liquibase) - 2 SP  
  3. Configurar conexão segura e profiles (dev, test) - 1 SP  
- **Story Points:** 5 SP

### História 4: Implementar módulo de usuários
- **Tipo:** Funcional
- **Descrição:** Cadastro, login, JWT e RBAC básico
- **Tasks / Sub-tasks:**
  1. Criar entidade Usuario e persistência - 2 SP  
  2. Implementar cadastro (email, senha, nome, telefone) - 2 SP  
  3. Implementar login com JWT (access + refresh token) - 3 SP  
  4. Implementar roles básicas (VISITANTE, PARTICIPANTE, VENDEDOR, ADMIN) - 2 SP  
  5. Testes unitários e integração inicial - 2 SP  
  6. Frontend: separar ts, html e css
  7. Frontend: Implementar tela de login com JWT e integrar com backend
  8. Frontend: Implementar cadastro de usuários, respeitando as roles do backend
- **Story Points:** 11 SP

### História 5: Implementar prova de conceito SSE/WebSocket
- **Tipo:** Enabler
- **Descrição:** Criar canal de comunicação em tempo real para futuras funcionalidades de leilão.
- **Tasks / Sub-tasks:**
  1. Criar endpoint SSE para broadcast de eventos - 2 SP  
  2. Criar endpoint WebSocket (simples) para testes - 3 SP  
  3. Criar cliente Angular para receber eventos de teste - 2 SP  
  4. Testar latência e concorrência com mock de eventos - 2 SP  5
- **Story Points:** 9 SP

### História 6: Configuração CI/CD inicial
- **Tipo:** Enabler
- **Descrição:** Automatizar build, testes e deploy inicial para backend e frontend.
- **Tasks / Sub-tasks:**
  1. Configurar pipeline backend (build, testes unitários) - 2 SP  
  2. Configurar pipeline frontend (build, lint, testes) - 2 SP  
  3. Configurar deploy inicial em ambiente de dev - 2 SP  
- **Story Points:** 6 SP

---

## Observações
- **Dependências:**  
  - Backend precisa de DB antes de iniciar testes unitários de usuário.  
  - Frontend precisa do backend mockado para consumir endpoints de autenticação.  
  - SSE/WebSocket será integrado no backend real, mas inicia como PoC nesta sprint.

- **Critérios de Aceite (Sprint 1):**
  1. Backend modularizado compilando e com testes unitários passando.  
  2. Frontend Angular inicial compilando e navegável.  
  3. Cadastro e login com JWT funcionando com roles mínimas.  
  4. SSE/WebSocket funcionando como PoC com broadcast de eventos mockados.  
  5. Pipelines CI/CD funcionando para backend e frontend.  

---

**Story Points Totais Sprint 1:** 43 SP
