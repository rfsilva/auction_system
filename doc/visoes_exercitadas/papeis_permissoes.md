# 🧩 Papeis do Sistema — Escopo Detalhado

Este documento descreve, de forma detalhada, os papéis do sistema de leilão eletrônico e suas respectivas permissões, limitações e responsabilidades.

---

# 👤 1. Visitante (Usuário não autenticado)

O Visitante acessa a plataforma sem login e possui acesso limitado, somente para fins de navegação e descoberta.

### ✔ Permissões
- Navegar pelos lotes disponíveis.
- Visualizar produtos dos lotes.
- Ver preços atuais (se política do sistema permitir).
- Ver histórico básico de lances (caso público).
- Ler regras gerais, termos e FAQ.
- Consultar informações públicas dos vendedores.
- Iniciar o fluxo de cadastro.
- Ver contagem regressiva dos leilões.

### ❌ Restrições
- Não pode dar lances.
- Não pode salvar favoritos.
- Não recebe notificações.
- Não acessa documentos restritos.
- Não vê informações detalhadas ou relatórios.

### 🎯 Objetivo do Papel
Explorar a plataforma e converter-se em usuário cadastrado.

---

# 🎟️ 2. Participante / Comprador

Usuário autenticado. Pode estar em dois estágios diferentes:

---

## 2.1 Participante (autenticado, porém não validado)

### ✔ Permissões
- Tudo que o Visitante pode acessar.
- Salvar produtos e lotes favoritos.
- Configurar preferências de notificação.
- Atualizar dados do perfil.
- Submeter documentos para validação (dupla verificação).
- Acompanhar lances em tempo real somente como espectador.
- Participar de chat público (se disponível).

### ❌ Restrições
- Não pode dar lances.
- Não pode arrematar.
- Não pode assinar contratos.

### 🎯 Objetivo
Completar onboarding para se tornar Comprador.

---

## 2.2 Comprador (autenticado + dupla validação aprovada)

### ✔ Permissões
- Tudo do Participante.
- Dar lances em tempo real.
- Usar auto-lance / estratégia automática (opcional).
- Receber notificações em tempo real (outbid, encerramento etc.).
- Assinar termos e contratos de lote.
- Arrematar produtos.
- Acompanhar status de pagamento e entrega.
- Avaliar o vendedor.
- Acessar documentos restritos.

### ❌ Restrições
- Não pode criar produtos ou lotes.
- Não administra outros usuários.
- Não gerencia vendedores ou configurações do sistema.

### 🎯 Objetivo
Participar dos leilões de forma plena e competitiva.

---

# 🧑‍🏭 3. Vendedor

Usuário responsável por cadastrar produtos, criar lotes e administrar suas vendas.

### ✔ Permissões
- Criar e editar produtos.
- Criar e configurar lotes.
- Definir regras do lote (preço mínimo, incrementos etc.).
- Definir encerramento no nível do lote ou do produto.
- Definir taxa de leiloeiro (conforme contrato).
- Anexar documentos a produtos/lotes.
- Ativar/desativar produtos e lotes.
- Ver lances em tempo real.
- Receber notificações operacionais.
- Informar dimensões/peso para cálculo de frete.
- Consultar API de frete.
- Acompanhar retirada/entrega do produto.
- Acessar dashboard de performance.
- Configurar mensagens automáticas.

### ❌ Restrições
- Não gerencia usuários fora de sua conta.
- Não controla regras globais da plataforma.
- Não tem acesso a auditorias completas.

### 🎯 Objetivo
Gerenciar de forma eficiente os leilões e manter boa reputação.

---

# 🛠️ 4. Administrador do Sistema

Papel de máxima autoridade operacional. Garante governança, segurança e compliance da plataforma.

### ✔ Permissões

#### 🛡 Gestão de Segurança
- Aprovar/rejeitar validações de identidade.
- Bloquear/banir usuários.
- Controlar níveis de acesso (RBAC).

#### ⚙ Gestão Operacional
- Configurar regras globais do sistema.
- Administrar taxas administrativas.
- Criar categorias, tags e listas auxiliares.
- Gerenciar parâmetros gerais (incrementos, janelas, políticas).

#### 📜 Auditoria e Compliance
- Acessar auditoria completa.
- Revisar histórico de lances.
- Emitir relatórios de conformidade.

#### 📣 Comunicação
- Criar banners e avisos.
- Enviar notificações globais.
- Responder chamados de suporte.

#### 📊 Analytics
- Monitorar KPIs gerais:
  - lances por minuto
  - número de compradores ativos
  - lotes ativos/encerrados
  - valor total arrematado

### ❌ Restrições
- Não pode dar lances.
- Não pode criar lotes como vendedor (exceto se possuir papel híbrido).

### 🎯 Objetivo
Garantir estabilidade, governança e integridade de todo o ecossistema.

---

# 📌 Resumo dos Papéis

| Papel            | Ver Conteúdo Público | Dar Lances | Criar Lotes | Admin Geral | Acesso Documentos Restritos |
|------------------|-----------------------|------------|-------------|-------------|------------------------------|
| Visitante        | ✔️                    | ❌          | ❌           | ❌           | ❌                            |
| Participante     | ✔️                    | ❌          | ❌           | ❌           | ❌                            |
| Comprador        | ✔️                    | ✔️          | ❌           | ❌           | ✔️                            |
| Vendedor         | ✔️ (dos seus)         | ❌          | ✔️           | ❌           | ✔️ (dos seus)                 |
| Administrador    | ✔️ (total)            | ❌          | ❌           | ✔️           | ✔️ (total)                    |

---
