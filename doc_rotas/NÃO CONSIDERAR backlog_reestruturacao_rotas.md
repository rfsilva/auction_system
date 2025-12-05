# Backlog: Reestruturação de Rotas Públicas vs Autenticadas

**Tema:** Arquitetura e Segurança  
**Épico:** Separação Clara de Rotas por Nível de Acesso  
**Prioridade:** Alta (Fundação Arquitetural)  
**Estimativa Total:** 89 SP (3-4 sprints)

## 🎯 Objetivo do Épico
Reestruturar completamente a arquitetura de rotas do frontend e endpoints do backend para criar uma separação clara e intuitiva entre áreas públicas, autenticadas e administrativas, melhorando segurança, UX e manutenibilidade.

---

## 📘 Contexto e Justificativa

### 🚨 Problemas Atuais Identificados
1. **Estrutura Confusa**: Rotas públicas e privadas misturadas no mesmo nível
2. **Inconsistências de Acesso**: Alguns endpoints têm proteção inadequada
3. **UX Prejudicada**: URLs não são intuitivas sobre permissões necessárias
4. **Manutenibilidade**: Dificulta adição de novas funcionalidades
5. **Segurança**: Risco de exposição acidental de dados sensíveis

### 🎯 Benefícios Esperados
- ✅ **Clareza Arquitetural**: URLs intuitivas indicam nível de acesso
- ✅ **Segurança Aprimorada**: Controle de acesso mais granular
- ✅ **UX Melhorada**: Navegação contextual por role
- ✅ **Manutenibilidade**: Código mais organizado e testável
- ✅ **Escalabilidade**: Facilita adição de novas áreas e funcionalidades

---

## 🏗️ Arquitetura Proposta

### Frontend - Nova Estrutura de Rotas
```
/ (público)
├── /catalogo (público)
├── /sobre (público)
├── /contato (público)
├── /auth/login (público)
├── /auth/register (público)
├── /app/* (usuário autenticado)
├── /vendedor/* (role: SELLER)
└── /admin/* (role: ADMIN)
```

### Backend - Nova Estrutura de Endpoints
```
/public/* (sem autenticação)
/auth/* (autenticação)
/api/user/* (role: USER)
/api/seller/* (role: SELLER)
/api/admin/* (role: ADMIN)
```

---

## 📋 Épicos e Histórias

### 🏛️ **Épico 1: Fundação Arquitetural**
**Objetivo:** Criar a base técnica para nova estrutura de rotas e guards

#### História 1.1: Novos Guards de Autorização
- **Tipo:** Frontend - Enabler
- **Descrição:** Criar guards específicos para roles (SELLER, ADMIN) e reestruturar sistema de autorização
- **Story Points:** 8 SP
- **Prioridade:** Crítica

#### História 1.2: Reestruturação de Security Config
- **Tipo:** Backend - Enabler  
- **Descrição:** Atualizar configuração de segurança para suportar nova estrutura de endpoints
- **Story Points:** 5 SP
- **Prioridade:** Crítica

#### História 1.3: Componentes Base de Layout
- **Tipo:** Frontend - Enabler
- **Descrição:** Criar layouts específicos para cada área (público, usuário, vendedor, admin)
- **Story Points:** 12 SP
- **Prioridade:** Alta

---

### 🌐 **Épico 2: Área Pública**
**Objetivo:** Implementar área pública com páginas institucionais e catálogo

#### História 2.1: Reestruturação do Catálogo Público
- **Tipo:** Frontend + Backend
- **Descrição:** Migrar catálogo para nova estrutura com URLs semânticas e SEO-friendly
- **Story Points:** 8 SP
- **Prioridade:** Alta

#### História 2.2: Páginas Institucionais
- **Tipo:** Frontend
- **Descrição:** Criar páginas Sobre, Contato, Termos e Privacidade
- **Story Points:** 10 SP
- **Prioridade:** Média

#### História 2.3: Endpoints Públicos
- **Tipo:** Backend
- **Descrição:** Criar PublicController com endpoints para catálogo e páginas institucionais
- **Story Points:** 6 SP
- **Prioridade:** Alta

---

### 👤 **Épico 3: Área do Usuário**
**Objetivo:** Implementar área privada para usuários autenticados

#### História 3.1: Dashboard do Usuário
- **Tipo:** Frontend
- **Descrição:** Criar dashboard personalizado para usuários com resumo de atividades
- **Story Points:** 12 SP
- **Prioridade:** Alta

#### História 3.2: Perfil e Configurações
- **Tipo:** Frontend + Backend
- **Descrição:** Migrar funcionalidades de perfil para nova estrutura /app/
- **Story Points:** 8 SP
- **Prioridade:** Alta

#### História 3.3: UserController
- **Tipo:** Backend
- **Descrição:** Criar controller específico para operações de usuário autenticado
- **Story Points:** 5 SP
- **Prioridade:** Alta

---

### 🏪 **Épico 4: Área do Vendedor**
**Objetivo:** Implementar área específica para vendedores com todas suas funcionalidades

#### História 4.1: Dashboard do Vendedor
- **Tipo:** Frontend
- **Descrição:** Criar dashboard específico com métricas e ações rápidas para vendedores
- **Story Points:** 15 SP
- **Prioridade:** Alta

#### História 4.2: Migração de Produtos e Lotes
- **Tipo:** Frontend + Backend
- **Descrição:** Migrar funcionalidades de produtos e lotes para /vendedor/ e /api/seller/
- **Story Points:** 18 SP
- **Prioridade:** Crítica

#### História 4.3: SellerController
- **Tipo:** Backend
- **Descrição:** Criar controller específico para operações de vendedor
- **Story Points:** 10 SP
- **Prioridade:** Crítica

#### História 4.4: Relatórios do Vendedor
- **Tipo:** Frontend + Backend
- **Descrição:** Implementar relatórios específicos para vendedores (vendas, produtos, performance)
- **Story Points:** 12 SP
- **Prioridade:** Média

---

### 👑 **Épico 5: Área Administrativa**
**Objetivo:** Implementar área administrativa com controle total do sistema

#### História 5.1: Dashboard Administrativo
- **Tipo:** Frontend
- **Descrição:** Criar dashboard completo para administradores com métricas do sistema
- **Story Points:** 20 SP
- **Prioridade:** Alta

#### História 5.2: Migração de Funcionalidades Admin
- **Tipo:** Frontend + Backend
- **Descrição:** Migrar todas as funcionalidades administrativas para /admin/ e /api/admin/
- **Story Points:** 15 SP
- **Prioridade:** Alta

#### História 5.3: AdminController
- **Tipo:** Backend
- **Descrição:** Criar controller específico para operações administrativas
- **Story Points:** 8 SP
- **Prioridade:** Alta

#### História 5.4: Ferramentas de Sistema
- **Tipo:** Frontend + Backend
- **Descrição:** Implementar logs, configurações e ferramentas de monitoramento
- **Story Points:** 10 SP
- **Prioridade:** Baixa

---

### 🔄 **Épico 6: Migração e Compatibilidade**
**Objetivo:** Garantir transição suave e manter compatibilidade

#### História 6.1: Sistema de Redirects
- **Tipo:** Frontend + Backend
- **Descrição:** Implementar redirects automáticos das rotas antigas para novas
- **Story Points:** 6 SP
- **Prioridade:** Alta

#### História 6.2: Atualização de Services
- **Tipo:** Frontend
- **Descrição:** Atualizar todos os services para usar novos endpoints
- **Story Points:** 8 SP
- **Prioridade:** Crítica

#### História 6.3: Navegação Contextual
- **Tipo:** Frontend
- **Descrição:** Implementar sistema de navegação que se adapta ao role do usuário
- **Story Points:** 10 SP
- **Prioridade:** Alta

#### História 6.4: Testes e Validação
- **Tipo:** QA
- **Descrição:** Atualizar todos os testes e validar funcionamento da nova estrutura
- **Story Points:** 12 SP
- **Prioridade:** Crítica

---

## 📊 Resumo do Backlog

### Por Épico
| Épico | Histórias | Story Points | Prioridade |
|-------|-----------|--------------|------------|
| 1. Fundação Arquitetural | 3 | 25 SP | Crítica |
| 2. Área Pública | 3 | 24 SP | Alta |
| 3. Área do Usuário | 3 | 25 SP | Alta |
| 4. Área do Vendedor | 4 | 55 SP | Alta |
| 5. Área Administrativa | 4 | 53 SP | Alta |
| 6. Migração e Compatibilidade | 4 | 36 SP | Alta |
| **TOTAL** | **21** | **218 SP** | - |

### Por Prioridade
- **Crítica:** 6 histórias (58 SP)
- **Alta:** 12 histórias (135 SP)
- **Média:** 2 histórias (22 SP)
- **Baixa:** 1 história (10 SP)

### Por Tipo
- **Frontend:** 12 histórias (142 SP)
- **Backend:** 6 histórias (44 SP)
- **Frontend + Backend:** 2 histórias (32 SP)
- **Enabler:** 3 histórias (25 SP)

---

## 🚀 Plano de Sprints Sugerido

### **Sprint R1: Fundação** (25 SP - 1 semana)
- História 1.1: Novos Guards de Autorização (8 SP)
- História 1.2: Reestruturação de Security Config (5 SP)
- História 1.3: Componentes Base de Layout (12 SP)

### **Sprint R2: Área Pública e Usuário** (49 SP - 2 semanas)
- História 2.1: Reestruturação do Catálogo Público (8 SP)
- História 2.3: Endpoints Públicos (6 SP)
- História 3.1: Dashboard do Usuário (12 SP)
- História 3.2: Perfil e Configurações (8 SP)
- História 3.3: UserController (5 SP)
- História 2.2: Páginas Institucionais (10 SP)

### **Sprint R3: Área do Vendedor** (43 SP - 2 semanas)
- História 4.1: Dashboard do Vendedor (15 SP)
- História 4.2: Migração de Produtos e Lotes (18 SP)
- História 4.3: SellerController (10 SP)

### **Sprint R4: Área Admin e Migração** (53 SP - 2 semanas)
- História 5.1: Dashboard Administrativo (20 SP)
- História 5.2: Migração de Funcionalidades Admin (15 SP)
- História 5.3: AdminController (8 SP)
- História 6.2: Atualização de Services (8 SP)

### **Sprint R5: Finalização** (48 SP - 2 semanas)
- História 6.1: Sistema de Redirects (6 SP)
- História 6.3: Navegação Contextual (10 SP)
- História 6.4: Testes e Validação (12 SP)
- História 4.4: Relatórios do Vendedor (12 SP)
- História 5.4: Ferramentas de Sistema (10 SP)

---

## ⚠️ Riscos e Dependências

### Riscos Identificados
1. **Alto**: Quebra de funcionalidades existentes durante migração
2. **Médio**: Impacto em SEO devido a mudanças de URLs públicas
3. **Médio**: Resistência dos usuários às mudanças de navegação
4. **Baixo**: Performance degradada durante período de transição

### Dependências
1. **Crítica**: Todas as funcionalidades atuais devem estar estáveis
2. **Alta**: Backup completo do sistema antes da migração
3. **Alta**: Ambiente de staging para testes extensivos
4. **Média**: Comunicação prévia aos usuários sobre mudanças

### Mitigações
- Implementar feature flags para rollback rápido
- Manter rotas antigas funcionando durante período de transição
- Testes automatizados extensivos
- Monitoramento em tempo real durante deploy

---

## 📈 Métricas de Sucesso

### Técnicas
- ✅ 100% das rotas antigas redirecionam corretamente
- ✅ 0 quebras de funcionalidade existentes
- ✅ Tempo de carregamento mantido ou melhorado
- ✅ Cobertura de testes > 85%

### UX/UI
- ✅ Redução de 50% no tempo para encontrar funcionalidades
- ✅ Navegação intuitiva sem necessidade de treinamento
- ✅ URLs semânticas e amigáveis para SEO
- ✅ Feedback positivo dos usuários > 80%

### Segurança
- ✅ 100% dos endpoints protegidos adequadamente
- ✅ Controle de acesso granular funcionando
- ✅ Auditoria de segurança aprovada
- ✅ Zero vulnerabilidades de acesso indevido

---

**Estimativa Total:** 218 SP (5 sprints de 2 semanas cada)  
**Duração Prevista:** 10 semanas  
**Equipe Sugerida:** 2 desenvolvedores full-stack + 1 QA  
**Investimento:** Alto, mas com ROI significativo em manutenibilidade e UX