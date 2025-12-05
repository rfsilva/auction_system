# Refinamento Sprint S2.1 — Sistema de Contratação e Gestão de Vendedores

**Sprint:** S2.1 (Refinamento da Sprint 2)  
**Duração:** 2 semanas  
**Equipe:** Dev Pleno + Sênior + Arquiteto  
**Prioridade:** Alta (Bloqueante para modelo de negócio)

## 🎯 Objetivo da Sprint
Implementar o sistema de contratação que permite à plataforma controlar quem pode vender, definir percentuais de comissão por categoria/tipo e garantir que todos os lotes estejam vinculados a contratos válidos, estabelecendo o modelo de receita da plataforma.

---

## 📘 Contexto do Projeto

### 🛠️ Backend:
- Java 21 + Spring Boot 3 + API REST + DTO + Validation + Lombok + JPA + MySQL + Flyway
- Entidade JPA completa (com Lombok, constraints e relacionamentos)
- Usar String para campos UUID quando as colunas do banco são VARCHAR(36)
- DTOs (request/response), validadores e mappers
- Repository
- Service com regras de negócio
- Controller REST com todos os endpoints CRUD + filtros se aplicável
- Migrations (somente se necessário; primeiro valide se existe na V1)
- Regras de validação (negócio e campos)
- Mensagens de erro claras	

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

### História 1: CRUD de Contratos (Administrador)
- **Tipo:** Funcional
- **Descrição:** Administrador pode criar, editar, visualizar e gerenciar contratos com vendedores.
- **Tasks / Sub-tasks:**
  1. Criar entidade Contrato (já existe na DB) e ajustar se necessário - 1 SP
  2. Implementar API REST CRUD para contratos - 3 SP
  3. Adicionar validações de negócio (vigência, percentuais) - 2 SP
  4. Frontend: criar formulário de contrato - 3 SP
  5. Frontend: criar listagem e gestão de contratos - 2 SP
  6. Implementar filtros (por vendedor, status, categoria) - 2 SP
- **Story Points:** 13 SP

### História 2: Processo de Contratação de Vendedores
- **Tipo:** Funcional
- **Descrição:** Sistema para transformar usuário comum em vendedor através de contrato.
- **Tasks / Sub-tasks:**
  1. Ajustar AuthService para não dar role SELLER automaticamente - 1 SP
  2. Implementar regras: usuário vira vendedor quando tem contrato ativo - 2 SP
  3. Frontend: tela para selecionar usuário e criar contrato - 3 SP
  4. Notificações: avisar usuário quando vira vendedor - 1 SP
- **Story Points:** 9 SP

### História 3: Validação de Contratos em Lotes
- **Tipo:** Funcional
- **Descrição:** Lotes devem obrigatoriamente estar vinculados a contratos válidos.
- **Tasks / Sub-tasks:**
  1. Ajustar entidade Lote para incluir contract_id obrigatório - 1 SP
  2. Implementar validações: lote deve ter contrato ativo - 2 SP
  3. Ajustar LoteService para validar contrato na criação - 2 SP
  4. Frontend: seleção de contrato no formulário de lote - 2 SP
  5. Implementar cálculo de comissão baseado no contrato - 2 SP
- **Story Points:** 9 SP

### História 4: Dashboard de Contratos e Comissões
- **Tipo:** Funcional
- **Descrição:** Painel para administradores visualizarem contratos, vendedores e projeções de receita.
- **Tasks / Sub-tasks:**
  1. Criar endpoints de estatísticas de contratos - 2 SP
  2. Implementar cálculos de comissões por período - 2 SP
  3. Frontend: dashboard com gráficos e métricas - 3 SP
  4. Relatórios de contratos vencendo - 2 SP
- **Story Points:** 9 SP

### História 5: Gestão de Múltiplos Contratos por Vendedor
- **Tipo:** Funcional
- **Descrição:** Vendedor pode ter múltiplos contratos ativos (diferentes categorias/percentuais).
- **Tasks / Sub-tasks:**
  1. Ajustar modelo para permitir múltiplos contratos por vendedor - 2 SP
  2. Implementar lógica de seleção de contrato por categoria - 2 SP
  3. Frontend: gestão de múltiplos contratos do vendedor - 2 SP
  4. Validações: não permitir contratos conflitantes - 1 SP
- **Story Points:** 7 SP

### História 6: Auditoria e Histórico de Contratos
- **Tipo:** Enabler
- **Descrição:** Rastreabilidade completa de alterações em contratos e ativações de vendedores.
- **Tasks / Sub-tasks:**
  1. Implementar log de alterações em contratos - 2 SP
  2. Histórico de ativação/desativação de vendedores - 1 SP
  3. Frontend: visualização de histórico - 2 SP
- **Story Points:** 5 SP

---

## 🔧 Regras de Negócio Detalhadas

### Contratos
1. **Vigência**: Todo contrato deve ter data de início e fim
2. **Percentuais**: Fee rate entre 0.01% e 50%
3. **Categorias**: Contrato pode ser específico para categorias ou geral
4. **Status**: DRAFT, ACTIVE, EXPIRED, CANCELLED, SUSPENDED
5. **Exclusividade**: Não pode haver contratos ativos conflitantes para mesmo vendedor/categoria

### Vendedores
1. **Ativação**: Usuário só vira vendedor quando tem pelo menos 1 contrato ativo
2. **Desativação**: Se todos os contratos expirarem/forem cancelados, perde role SELLER
3. **Múltiplos Contratos**: Pode ter contratos diferentes para categorias diferentes
4. **Validação**: Deve ter dados completos (empresa, CNPJ, etc.) para contratos

### Lotes
1. **Contrato Obrigatório**: Todo lote deve estar vinculado a um contrato válido
2. **Validação de Vigência**: Contrato deve estar ativo durante todo período do lote
3. **Categoria**: Se contrato for específico, lote deve respeitar categoria
4. **Comissão**: Calculada automaticamente baseada no percentual do contrato

---

## 🎯 Critérios de Aceite da Sprint

### Funcionais
1. ✅ Admin consegue criar/editar/listar contratos
2. ✅ Usuário comum não tem role SELLER automaticamente
3. ✅ Usuário vira vendedor apenas quando tem contrato ativo
4. ✅ Lote não pode ser criado sem contrato válido
5. ✅ Vendedor pode ter múltiplos contratos para categorias diferentes
6. ✅ Sistema calcula comissões baseado no contrato do lote
7. ✅ Dashboard mostra estatísticas de contratos e receitas

### Técnicos
1. ✅ Todas as validações de negócio implementadas
2. ✅ Auditoria completa de alterações
3. ✅ Performance adequada para consultas de contratos
4. ✅ Testes unitários para regras críticas
5. ✅ Interface responsiva e intuitiva

### Segurança
1. ✅ Apenas admins podem gerenciar contratos
2. ✅ Vendedores só veem seus próprios contratos
3. ✅ Validação de permissões em todas as operações
4. ✅ Log de auditoria para ações sensíveis

---

**Story Points Totais Sprint S2.1:** 52 SP
