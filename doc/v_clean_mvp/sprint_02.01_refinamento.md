# Refinamento Sprint S2.1 — Sistema de Contratação e Gestão de Vendedores

**Sprint:** S2.1 (Refinamento da Sprint 2)  
**Duração:** 2 semanas  
**Equipe:** Dev Pleno + Sênior + Arquiteto  
**Prioridade:** Alta (Bloqueante para modelo de negócio)

## 🎯 Objetivo da Sprint
Implementar o sistema de contratação que permite à plataforma controlar quem pode vender, definir percentuais de comissão por categoria/tipo e garantir que todos os lotes estejam vinculados a contratos válidos, estabelecendo o modelo de receita da plataforma.

---

## 📝 Regras Gerais
  1. **Backend**:
	1.1. Se precisar criar entity nova, localizar primeiro a tabela em V1 do migrations. Se não encontrar, criar migration para criação da tabela.
	1.2. Se for necessário criar tabela, usar prefixo "tb_" e nome no singular
	1.3. Não criar estruturas do tipo TYPE, TRIGGER, PROCEDURE, FUNCTION no migrations
	1.4. Sempre que possível, aplicar Lombok para eliminar verbosidade de código
	1.5. Não gerar nem atualizar nenhum teste unitário ou integrado nesse momento.
	1.6. Criar collection do postman para testes de endpoints (novos ou atualizados) REST
  2. **Frontend**:
	2.1. Sempre que um novo componente for criado, não gerar HTML e CSS inline, separando os arquivos .html, .scss e .ts
	2.2. Formulários de CRUD (se criados ou atualizados) devem apresentar erros de validação claros, sendo: regras de negócio no topo do formulário, e erros de validação de campo em cada campo criticado
  3. **Integração**:
	3.1. Garantir consistência de chamadas REST entre frontend e backend através de testes integrados

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
  2. Criar endpoint para ativar vendedor via contrato - 2 SP
  3. Implementar regras: usuário vira vendedor quando tem contrato ativo - 2 SP
  4. Frontend: tela para selecionar usuário e criar contrato - 3 SP
  5. Notificações: avisar usuário quando vira vendedor - 1 SP
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

## 📊 Modelo de Dados Ajustado

### Ajustes na tb_contrato
```sql
-- Adicionar campos necessários para o novo modelo
ALTER TABLE tb_contrato ADD COLUMN categoria VARCHAR(100);
ALTER TABLE tb_contrato ADD COLUMN tipo_contrato ENUM('GERAL', 'CATEGORIA_ESPECIFICA') DEFAULT 'GERAL';
ALTER TABLE tb_contrato ADD COLUMN status ENUM('DRAFT', 'ACTIVE', 'EXPIRED', 'CANCELLED', 'SUSPENDED') DEFAULT 'DRAFT';
ALTER TABLE tb_contrato ADD COLUMN observacoes TEXT;
ALTER TABLE tb_contrato ADD COLUMN created_by VARCHAR(36);
ALTER TABLE tb_contrato ADD COLUMN updated_by VARCHAR(36);

-- Índices para performance
CREATE INDEX idx_tb_contrato_status ON tb_contrato(status);
CREATE INDEX idx_tb_contrato_categoria ON tb_contrato(categoria);
CREATE INDEX idx_tb_contrato_vigencia ON tb_contrato(valid_from, valid_to);
```

### Ajustes na tb_lote
```sql
-- Adicionar referência obrigatória ao contrato
ALTER TABLE tb_lote ADD COLUMN contract_id VARCHAR(36) NOT NULL;
ALTER TABLE tb_lote ADD CONSTRAINT fk_lote_contrato 
    FOREIGN KEY (contract_id) REFERENCES tb_contrato(id);

-- Índice para performance
CREATE INDEX idx_tb_lote_contract_id ON tb_lote(contract_id);
```

### Nova tabela tb_vendedor_contrato (relacionamento)
```sql
-- Tabela para relacionamento N:N entre vendedores e contratos
CREATE TABLE tb_vendedor_contrato (
    id VARCHAR(36) PRIMARY KEY,
    vendedor_id VARCHAR(36) NOT NULL,
    contrato_id VARCHAR(36) NOT NULL,
    data_ativacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_desativacao TIMESTAMP NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    motivo_desativacao VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (vendedor_id) REFERENCES tb_vendedor(id) ON DELETE CASCADE,
    FOREIGN KEY (contrato_id) REFERENCES tb_contrato(id) ON DELETE CASCADE,
    
    UNIQUE KEY uk_vendedor_contrato_ativo (vendedor_id, contrato_id, ativo)
);

-- Índices para performance
CREATE INDEX idx_tb_vendedor_contrato_vendedor ON tb_vendedor_contrato(vendedor_id);
CREATE INDEX idx_tb_vendedor_contrato_contrato ON tb_vendedor_contrato(contrato_id);
CREATE INDEX idx_tb_vendedor_contrato_ativo ON tb_vendedor_contrato(ativo);
```

### Nova tabela tb_comissao (para cálculos)
```sql
-- Tabela para registrar comissões calculadas
CREATE TABLE tb_comissao (
    id VARCHAR(36) PRIMARY KEY,
    lote_id VARCHAR(36) NOT NULL,
    contrato_id VARCHAR(36) NOT NULL,
    vendedor_id VARCHAR(36) NOT NULL,
    valor_venda DECIMAL(15,2) NOT NULL,
    percentual_comissao DECIMAL(5,4) NOT NULL,
    valor_comissao DECIMAL(15,2) NOT NULL,
    status ENUM('PENDENTE', 'CALCULADA', 'PAGA', 'CANCELADA') DEFAULT 'PENDENTE',
    data_calculo TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_pagamento TIMESTAMP NULL,
    observacoes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (lote_id) REFERENCES tb_lote(id),
    FOREIGN KEY (contrato_id) REFERENCES tb_contrato(id),
    FOREIGN KEY (vendedor_id) REFERENCES tb_vendedor(id)
);

-- Índices para performance
CREATE INDEX idx_tb_comissao_lote ON tb_comissao(lote_id);
CREATE INDEX idx_tb_comissao_vendedor ON tb_comissao(vendedor_id);
CREATE INDEX idx_tb_comissao_status ON tb_comissao(status);
CREATE INDEX idx_tb_comissao_data_calculo ON tb_comissao(data_calculo);
```

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

## 🚀 Impacto e Dependências

### Impacto na Sprint 2 Original
- **História 2 (CRUD Lotes)**: Precisa ser ajustada para incluir seleção de contrato
- **Testes existentes**: Podem quebrar, precisam ser atualizados
- **Frontend atual**: Formulário de lote precisa ser modificado

### Dependências
- **Pré-requisito**: Conclusão da História 1 antes das demais
- **Bloqueante**: Sistema de lotes atual não funcionará até ajustes
- **Sequencial**: História 2 → História 3 → demais podem ser paralelas

### Riscos
- **Alto**: Mudança significativa no modelo de negócio
- **Médio**: Impacto em funcionalidades já implementadas
- **Baixo**: Complexidade técnica (estrutura já existe)

---

## 📈 Métricas de Sucesso

### Negócio
- **Controle de Vendedores**: 100% dos vendedores com contrato
- **Receita Rastreável**: Todas as comissões calculadas corretamente
- **Flexibilidade**: Múltiplos tipos de contrato funcionando

### Técnico
- **Performance**: Consultas de contrato < 200ms
- **Disponibilidade**: Sistema funcionando 99.9%
- **Qualidade**: 0 bugs críticos em produção

---

## 🔄 Fluxo de Implementação Sugerido

### Fase 1 - Fundação (Semana 1)
1. **História 1**: CRUD de Contratos
2. **História 2**: Processo de Contratação
3. **Ajustes no AuthService**: Remover auto-atribuição de SELLER

### Fase 2 - Integração (Semana 2)
1. **História 3**: Validação de Contratos em Lotes
2. **História 5**: Múltiplos Contratos
3. **História 6**: Auditoria

### Fase 3 - Finalização
1. **História 4**: Dashboard
2. **Testes integrados**
3. **Documentação**

---

## 📝 Observações Importantes

### Regras Gerais da Sprint
1. **Backend**: Seguir padrões estabelecidos (prefixo "tb_", Lombok, etc.)
2. **Frontend**: Separar HTML, CSS e TypeScript
3. **Validações**: Implementar tanto no frontend quanto backend
4. **Auditoria**: Registrar todas as operações críticas
5. **Performance**: Otimizar consultas com índices apropriados

### Considerações de Negócio
- **Migração**: Vendedores existentes precisarão de contratos retroativos
- **Comunicação**: Usuários devem ser notificados sobre mudanças
- **Suporte**: Documentar processo para equipe de atendimento
- **Legal**: Contratos devem seguir legislação aplicável

---

**Story Points Totais Sprint S2.1:** 52 SP

**Observação Crítica**: Esta sprint é **fundamental** para o modelo de negócio da plataforma e deve ser implementada antes de continuar com outras funcionalidades de leilão. Sem ela, a plataforma não consegue gerar receita de forma controlada e auditável.