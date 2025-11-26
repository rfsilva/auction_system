# Refinamento Sprint 2 — Sistema de Leilão Eletrônico

**Sprint:** 2  
**Duração:** 2 semanas  
**Dev Pleno + Sênior + Arquiteto**  

## Objetivo da Sprint
Implementar funcionalidades básicas de catálogo, produtos e leilões estáticos, permitindo criação de produtos, lotes e visualização por visitantes e compradores, sem ainda lidar com lances dinâmicos.

---

## Histórias Detalhadas
- **Regras Gerais** 
  1. Backend: Se precisar criar entity nova, localizar primeiro a tabela em V1 do migrations. Se não encontrar, criar migration para criação da tabela.
  2. Backend: Se for necessário criar tabela, usar prefixo "tb_" e nome no singular
  3. Backend: Não criar estruturas do tipo TYPE, TRIGGER, PROCEDURE, FUNCTION no migrations
  4. Backend: Sempre que possível, aplicar Lombok para eliminar verbosidade de código
  5. Frontend: Sempre que um novo componente for criado, respeitar a separação entre TS, HTML e CSS

### História 1: CRUD de Produtos
- **Tipo:** Funcional
- **Descrição:** Vendedor pode criar, editar, visualizar e excluir produtos.
- **Tasks / Sub-tasks:**
  1. Criar entidade Produto e persistência no DB - 2 SP  
  2. Implementar API REST CRUD para produtos - 3 SP  
  3. Frontend: criar formulário de cadastro e edição - 2 SP  
  4. Frontend: criar listagem de produtos (catálogo) - 2 SP  
  5. Testes unitários e integração backend - 2 SP  
  6. Testes end-to-end no frontend - 2 SP  
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
