# Refinamento Sprint 2 — Sistema de Leilão Eletrônico

**Sprint:** 2  
**Duração:** 2 semanas  
**Dev Pleno + Sênior + Arquiteto**  

## Objetivo da Sprint
Implementar funcionalidades básicas de catálogo, produtos e leilões estáticos, permitindo criação de produtos, lotes e visualização por visitantes e compradores, sem ainda lidar com lances dinâmicos.

---
## 📘 Contexto do Projeto

### 🛠️ Backend:
- Java 21 + Spring Boot 3 + API REST + DTO + Validation + Lombok + JPA + MySQL + Flyway
- Entidade JPA completa (com Lombok, constraints e relacionamentos)
- DTOs (request/response), validadores e mappers
- Repository
- Service com regras de negócio
- Controller REST com todos os endpoints CRUD + filtros se aplicável
- Migrations (somente se necessário; primeiro valide se existe na V1)
- Regras de validação (negócio e campos)
- Mensagens de erro claras	
- I18N estruturado para Português, Inglês, Espanhol e Italiano

### 🎨 Frontend: 
- Angular 18 (standalone) + HttpClient com fetch + Reactive Forms + rotas
- Model (interface ou classe)
- Service TS com chamadas REST usando `HttpClient` (withFetch)
- Component de listagem + filtros
- Component de formulário (create/update)
- Component de detalhe (se fizer sentido)
- Component sem HTML e SCSS inline - criar arquivos separados
- Reactive Forms com validações
- Mensagens de erro (negócio no topo, campos em cada campo)
- Rotas completas do módulo	

### 🔗 Integrações:
- Geração da collection Postman dos endpoints criados/alterados
- Garantir consistência do contrato REST gerado no backend para uso no frontend  

### 🛢️ Banco de Dados:
- Migrations versionadas (V1 = legado), prefixo "tb_" e nome singular
- Evitar ao máximo queries nativas e named queries
- Não criar estruturas específicas do banco de dados (TYPE, TRIGGER, PROCEDURE, FUNCTION, etc.) no migrations
- Para entities novas, validar no migrations se tabela já implementada. Se não, criar, se sim e precisar atualizar, atualize em versão nova.

### ⚠️ Importante:
- Manter padrões de nomenclatura e pastas
- NÃO inventar regra que não esteja no documento funcional.
- Analise a história. SE a história tiver regra incompleta, liste os "pontos pendentes" no bloco ANOTAÇÕES.
- Mantenha código limpo e dentro dos padrões fornecidos.
- Comece lendo o material, identifique entidades e regras, e só então gere tudo.

## 📋 Histórias Detalhadas

### História 1: CRUD de Produtos
- **Tipo:** Funcional
- **Descrição:** Vendedor pode criar, editar, visualizar e excluir produtos.
- **Tasks / Sub-tasks:**
  1. Criar entidade Produto e persistência no DB - 2 SP  
  2. Implementar API REST CRUD para produtos - 3 SP  
  3. Implementar API REST CRUD específica para o catálogo de produtos (exibição público geral) - pública  - 2 SP
  4. Frontend: criar formulário de cadastro e edição - 2 SP  
  5. Frontend: criar listagem de produtos (catálogo) - 2 SP  
	5.1 A tela de catálogo precisa ser implementada com as melhores recomendações de UX. E o filtro deve aparecer suprimido (sanfona), e só exibido com o usuário clicando em expandir
  6. Testes unitários e integração backend - 2 SP  
  7. Testes end-to-end no frontend - 2 SP  
- **Story Points:** 13 SP

### História 2: CRUD de Lotes
- **Tipo:** Funcional
- **Descrição:** Vendedor cria lotes e associa produtos; define datas de encerramento.
- **Tasks / Sub-tasks:**
  1. Criar entidade Lote e persistência - 2 SP  
  2. Implementar API REST CRUD para lotes - 3 SP  
  3. Frontend: criar formulário de lote e associação de produtos - 3 SP  
  4. Frontend: criar listagem de lotes para vendedores - 2 SP  
  5. Testes unitários e integração backend - 2 SP  
- **Story Points:** 12 SP

### História 3: Visualização de Catálogo (Visitantes)
- **Tipo:** Funcional
- **Descrição:** Visitantes podem visualizar produtos e lotes públicos sem autenticação.
- **Tasks / Sub-tasks:**
  1. Criar endpoint público para listagem de produtos/lotes - 2 SP  
  2. Frontend: exibir catálogo com filtros básicos (categoria, preço) - 2 SP  
  3. Testes unitários e integração de API - 1 SP  
- **Story Points:** 5 SP

### História 4: Implementação de Regras de Negócio de Produto e Lote
- **Tipo:** Enabler/Funcional
- **Descrição:** Garantir validações de encerramento, status e visibilidade.
- **Tasks / Sub-tasks:**
  1. Validar que produto não pode ser publicado se já estiver em lote ativo - 2 SP  
  2. Validar que produto não tem encerramento posterior ao lote - 2 SP  
  3. Implementar status: `DRAFT`, `PUBLISHED`, `SOLD` / `UNSOLD` - 2 SP  
  4. Testes unitários e integração de regras de negócio - 2 SP  
- **Story Points:** 8 SP

### História 5: Integração inicial de documentos
- **Tipo:** Enabler
- **Descrição:** Preparar módulo para upload e armazenamento de imagens de produtos.
- **Tasks / Sub-tasks:**
  1. Criar API para upload de arquivos (imagens) - 2 SP  
  2. Configurar storage local (mock) ou S3 (dev) - 2 SP  
  3. Testes de upload e download - 1 SP  
- **Story Points:** 5 SP

### História 6: Setup inicial de notificações
- **Tipo:** Enabler
- **Descrição:** Preparar módulo de notificações (email / push) para alertas futuros.
- **Tasks / Sub-tasks:**
  1. Configurar serviço de envio de emails mock - 2 SP  
  2. Criar estrutura de eventos para notificações de produto/lote - 2 SP  
  3. Testes unitários de disparo de eventos - 1 SP  
- **Story Points:** 5 SP

---

## Observações
- **Dependências:**  
  - CRUD de produtos depende da entidade Produto criada na Sprint 1.  
  - Lotes dependem do módulo de produtos já criado.  
  - Catálogo público depende do backend de produtos/lotes pronto.  

- **Critérios de Aceite (Sprint 2):**
  1. Vendedor consegue criar, editar, excluir produtos e lotes.  
  2. Produtos e lotes exibidos corretamente no catálogo público para visitantes.  
  3. Regras de negócio de status e datas aplicadas corretamente.  
  4. Upload de imagens funcionando com armazenamento mock.  
  5. Estrutura de notificações criada e testada com eventos simulados.  

---

**Story Points Totais Sprint 2:** 48 SP
