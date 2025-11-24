# 🔒 DOCUMENTO COMPLEMENTAR - GAPS FUNCIONAIS CRÍTICOS

**Versão:** 1.0  
**Data:** 2025-01-27  
**Escopo:** Detalhamento dos requisitos funcionais identificados como gaps críticos  
**Prioridade:** ALTA - Implementação obrigatória para MVP

---

## 📋 SUMÁRIO
1. [Gestão de Fraudes e Segurança](#1-gestão-de-fraudes-e-segurança)
2. [Sistema Anti-Sniping](#2-sistema-anti-sniping)
3. [Gestão de Disputas](#3-gestão-de-disputas)
4. [Regras de Cancelamento](#4-regras-de-cancelamento)
5. [Funcionalidades Essenciais Complementares](#5-funcionalidades-essenciais-complementares)
6. [Impacto no Modelo de Dados](#6-impacto-no-modelo-de-dados)
7. [Histórias de Usuário](#7-histórias-de-usuário)
8. [Critérios de Aceite](#8-critérios-de-aceite)

---

## 🔴 1. GESTÃO DE FRAUDES E SEGURANÇA

### 1.1 Visão Geral
O sistema deve implementar mecanismos robustos de detecção e prevenção de fraudes para garantir a integridade dos leilões e proteger usuários legítimos.

### 1.2 Detecção de Padrões Suspeitos de Lance

#### 1.2.1 Regras de Detecção Automática

**Padrão 1: Lances Sequenciais Suspeitos**
- **Regra**: Detectar quando o mesmo usuário faz mais de 3 lances consecutivos no mesmo item
- **Ação**: Alertar administrador + aplicar cooldown de 30 segundos
- **Exceção**: Permitir se houver outros licitantes intercalados

**Padrão 2: Lances com Incrementos Mínimos Repetitivos**
- **Regra**: Detectar lances consecutivos com incremento exatamente mínimo (>5 vezes)
- **Ação**: Alertar para possível manipulação de preço
- **Threshold**: Configurável por categoria de produto

**Padrão 3: Lances em Horários Suspeitos**
- **Regra**: Múltiplos lances nos últimos 10 segundos do leilão
- **Ação**: Aplicar anti-sniping + log de auditoria
- **Monitoramento**: Padrão de comportamento por usuário

**Padrão 4: Velocidade de Lance Anômala**
- **Regra**: Mais de 10 lances por minuto do mesmo usuário
- **Ação**: Rate limiting + CAPTCHA
- **Escalação**: Bloqueio temporário após 3 violações

#### 1.2.2 Análise Comportamental

**Métricas de Risco por Usuário:**
```
Risk Score = (
  lance_frequency_score * 0.3 +
  timing_pattern_score * 0.2 +
  increment_pattern_score * 0.2 +
  device_consistency_score * 0.15 +
  network_pattern_score * 0.15
)
```

**Ações por Nível de Risco:**
- **Baixo (0-30)**: Monitoramento normal
- **Médio (31-60)**: Alertas + logs detalhados
- **Alto (61-80)**: CAPTCHA + cooldown
- **Crítico (81-100)**: Bloqueio temporário + revisão manual

### 1.3 Validação de Identidade (KYC Básico)

#### 1.3.1 Níveis de Verificação

**Nível 1 - Básico (Obrigatório para participar)**
- Email verificado
- Telefone verificado via SMS
- CPF/CNPJ válido (consulta Receita Federal)
- Endereço completo

**Nível 2 - Intermediário (Para lances > R$ 1.000)**
- Documento com foto (RG/CNH/Passaporte)
- Selfie para comparação facial
- Comprovante de endereço (últimos 3 meses)

**Nível 3 - Avançado (Para lances > R$ 10.000)**
- Comprovante de renda
- Análise de crédito básica
- Referências bancárias

#### 1.3.2 Processo de Verificação

**Fluxo de Verificação:**
1. **Submissão**: Usuário envia documentos via upload
2. **Validação Automática**: OCR + validação de dados
3. **Análise Manual**: Revisão por equipe (se necessário)
4. **Aprovação/Rejeição**: Notificação ao usuário
5. **Recurso**: Processo de contestação (se rejeitado)

**SLAs de Verificação:**
- Nível 1: Automático (tempo real)
- Nível 2: 24 horas (dias úteis)
- Nível 3: 48 horas (dias úteis)

### 1.4 Limites de Transação

#### 1.4.1 Limites por Usuário

**Usuário Não Verificado:**
- Lance máximo: R$ 500
- Total mensal: R$ 2.000
- Leilões simultâneos: 3

**Usuário Verificado Nível 1:**
- Lance máximo: R$ 5.000
- Total mensal: R$ 20.000
- Leilões simultâneos: 10

**Usuário Verificado Nível 2:**
- Lance máximo: R$ 50.000
- Total mensal: R$ 200.000
- Leilões simultâneos: 25

**Usuário Verificado Nível 3:**
- Lance máximo: R$ 500.000
- Total mensal: R$ 2.000.000
- Leilões simultâneos: 50

#### 1.4.2 Limites Dinâmicos

**Ajuste por Histórico:**
- Usuários com bom histórico: +20% nos limites
- Usuários com disputas: -30% nos limites
- Novos usuários: Limites reduzidos por 30 dias

### 1.5 Sistema de Blacklist

#### 1.5.1 Blacklist de IPs/Dispositivos

**Critérios para Blacklist:**
- Múltiplas contas do mesmo IP
- Tentativas de burlar verificação
- Atividade automatizada detectada
- Denúncias confirmadas

**Tipos de Bloqueio:**
- **Temporário**: 24h, 7 dias, 30 dias
- **Permanente**: Fraudes confirmadas
- **Condicional**: Liberação mediante verificação adicional

#### 1.5.2 Blacklist de Usuários

**Motivos para Blacklist:**
- Não pagamento recorrente
- Manipulação de leilões
- Documentos falsos
- Comportamento abusivo

**Processo de Blacklist:**
1. Investigação inicial
2. Notificação ao usuário
3. Direito de defesa (48h)
4. Decisão final
5. Recurso (se aplicável)

---

## ⏰ 2. SISTEMA ANTI-SNIPING

### 2.1 Visão Geral
O sistema anti-sniping visa garantir que todos os participantes tenham oportunidade justa de dar lances, evitando que leilões sejam decididos por lances de última hora.

### 2.2 Regras de Extensão Automática

#### 2.2.1 Configuração Base

**Janela de Extensão:**
- **Padrão**: 30 segundos antes do encerramento
- **Configurável**: Por categoria de produto (15s a 60s)
- **Mínimo**: 15 segundos (não configurável abaixo)

**Tempo de Extensão:**
- **Primeira extensão**: +2 minutos
- **Extensões subsequentes**: +1 minuto
- **Máximo de extensões**: 5 por leilão

#### 2.2.2 Lógica de Ativação

**Condições para Extensão:**
```
IF (tempo_restante <= janela_extensao) AND 
   (novo_lance_recebido) AND 
   (extensoes_utilizadas < maximo_extensoes)
THEN
   estender_leilao(tempo_extensao)
   notificar_participantes()
   log_extensao()
```

**Casos Especiais:**
- **Lances simultâneos**: Considerar timestamp do servidor
- **Falha de rede**: Não contar como extensão se lance não foi processado
- **Lances inválidos**: Não ativam extensão

### 2.3 Comunicação aos Usuários

#### 2.3.1 Notificações em Tempo Real

**Via SSE/WebSocket:**
```json
{
  "type": "AUCTION_EXTENDED",
  "auctionId": "uuid",
  "newEndTime": "2025-01-27T15:30:00Z",
  "extensionNumber": 1,
  "maxExtensions": 5,
  "reason": "BID_IN_SNIPE_WINDOW",
  "message": "Leilão estendido por 2 minutos devido a lance nos últimos 30 segundos"
}
```

**Via Interface:**
- Banner destacado no topo da página
- Atualização do timer em tempo real
- Histórico de extensões visível
- Indicador visual de janela de extensão

#### 2.3.2 Notificações por Email/SMS

**Gatilhos:**
- Primeira extensão do leilão
- Lance perdido por extensão
- Leilão encerrado após extensões

**Template de Notificação:**
```
🔔 Leilão Estendido - [Nome do Produto]

O leilão foi estendido por 2 minutos devido a um lance nos últimos 30 segundos.

Novo horário de encerramento: 27/01/2025 às 15:30
Lance atual: R$ 1.250,00
Extensões restantes: 4

[Ver Leilão] [Dar Lance]
```

### 2.4 Configurações Administrativas

#### 2.4.1 Parâmetros Configuráveis

**Por Categoria:**
- Janela de extensão (15-60 segundos)
- Tempo de extensão (1-5 minutos)
- Máximo de extensões (1-10)
- Ativar/desativar anti-sniping

**Por Valor do Item:**
- Itens > R$ 10.000: Extensão obrigatória
- Itens < R$ 100: Extensão opcional
- Configuração automática por faixa de preço

#### 2.4.2 Relatórios e Métricas

**Métricas de Anti-Sniping:**
- Taxa de leilões com extensão
- Número médio de extensões por leilão
- Impacto no valor final (antes/depois)
- Satisfação dos usuários

---

## ⚖️ 3. GESTÃO DE DISPUTAS

### 3.1 Visão Geral
Sistema para resolução de conflitos entre compradores e vendedores, garantindo transparência e justiça nas transações.

### 3.2 Tipos de Disputa

#### 3.2.1 Categorias de Disputa

**Categoria 1: Problemas com o Produto**
- Produto não conforme descrição
- Produto danificado
- Produto não entregue
- Qualidade inferior ao anunciado

**Categoria 2: Problemas com Pagamento**
- Pagamento não processado
- Cobrança indevida
- Estorno não realizado
- Taxas incorretas

**Categoria 3: Problemas com o Leilão**
- Manipulação de lances
- Encerramento incorreto
- Problemas técnicos
- Violação de regras

**Categoria 4: Problemas com Entrega**
- Atraso na entrega
- Produto perdido
- Endereço incorreto
- Problemas com transportadora

### 3.3 Processo de Disputa

#### 3.3.1 Abertura de Disputa

**Prazo para Abertura:**
- Problemas com produto: 7 dias após recebimento
- Problemas com pagamento: 30 dias após transação
- Problemas com leilão: 24 horas após encerramento
- Problemas com entrega: 15 dias após prazo previsto

**Informações Obrigatórias:**
- Número do leilão/transação
- Categoria da disputa
- Descrição detalhada
- Evidências (fotos, documentos, prints)
- Solução desejada

#### 3.3.2 Fluxo de Resolução

**Etapa 1: Tentativa de Acordo (48h)**
1. Sistema notifica vendedor
2. Vendedor tem 24h para responder
3. Negociação direta via plataforma
4. Acordo registrado no sistema

**Etapa 2: Mediação (72h)**
1. Mediador da plataforma analisa caso
2. Solicita informações adicionais (se necessário)
3. Propõe solução baseada em políticas
4. Partes têm 24h para aceitar/recusar

**Etapa 3: Decisão Final (48h)**
1. Análise completa por equipe especializada
2. Decisão baseada em evidências e políticas
3. Comunicação da decisão às partes
4. Execução da solução (estorno, reenvio, etc.)

### 3.4 SLAs e Prazos

#### 3.4.1 Tempos de Resposta

**Por Prioridade:**
- **Crítica** (problemas de pagamento): 2 horas
- **Alta** (produto não entregue): 4 horas
- **Média** (produto não conforme): 8 horas
- **Baixa** (dúvidas gerais): 24 horas

**Por Valor da Transação:**
- > R$ 10.000: Prioridade crítica
- R$ 1.000 - R$ 10.000: Prioridade alta
- R$ 100 - R$ 1.000: Prioridade média
- < R$ 100: Prioridade baixa

#### 3.4.2 Resolução Total

**Meta de Resolução:**
- 80% dos casos em até 5 dias úteis
- 95% dos casos em até 10 dias úteis
- 100% dos casos em até 15 dias úteis

### 3.5 Recursos e Apelações

#### 3.5.1 Direito de Recurso

**Prazo**: 5 dias úteis após decisão
**Custo**: Gratuito para primeira apelação
**Análise**: Equipe diferente da decisão original
**Decisão final**: Sem possibilidade de novo recurso

---

## ❌ 4. REGRAS DE CANCELAMENTO

### 4.1 Visão Geral
Definição clara de quando e como leilões podem ser cancelados, protegendo tanto vendedores quanto compradores.

### 4.2 Cancelamento pelo Vendedor

#### 4.2.1 Antes do Início do Leilão

**Permitido sem restrições:**
- Até 24 horas antes do início
- Sem penalidades
- Notificação automática aos interessados

**Permitido com restrições:**
- Entre 24h e 2h antes do início
- Taxa de cancelamento: R$ 50
- Justificativa obrigatória

**Não permitido:**
- Menos de 2 horas antes do início
- Apenas em casos excepcionais (força maior)

#### 4.2.2 Após Início do Leilão

**Sem lances:**
- Permitido até 1 hora após início
- Taxa de cancelamento: R$ 100
- Justificativa obrigatória

**Com lances:**
- Apenas casos excepcionais
- Aprovação administrativa obrigatória
- Compensação aos licitantes
- Possível suspensão do vendedor

#### 4.2.3 Motivos Válidos para Cancelamento

**Força Maior:**
- Produto danificado/perdido
- Problemas legais com o produto
- Emergências pessoais/familiares
- Desastres naturais

**Problemas Técnicos:**
- Erro na descrição do produto
- Preço inicial incorreto
- Categoria incorreta
- Problemas com imagens

### 4.3 Cancelamento pelo Administrador

#### 4.3.1 Motivos para Cancelamento Administrativo

**Violações de Política:**
- Produto proibido/ilegal
- Descrição enganosa
- Manipulação de lances
- Denúncias confirmadas

**Problemas Técnicos:**
- Falhas no sistema
- Problemas de conectividade
- Erros de processamento
- Vulnerabilidades de segurança

#### 4.3.2 Processo de Cancelamento Administrativo

1. **Identificação do problema**
2. **Análise preliminar** (30 minutos)
3. **Decisão de cancelamento**
4. **Notificação imediata** a todos os participantes
5. **Relatório de cancelamento**
6. **Ações corretivas** (se aplicável)

### 4.4 Compensações e Penalidades

#### 4.4.1 Compensação aos Licitantes

**Leilão cancelado com lances:**
- Crédito de R$ 10 por lance dado
- Prioridade em leilões similares
- Notificação de produtos relacionados

**Leilão cancelado por fraude:**
- Crédito de R$ 50 por participante
- Investigação completa
- Relatório de transparência

#### 4.4.2 Penalidades ao Vendedor

**Cancelamento sem justificativa:**
- 1º cancelamento: Advertência
- 2º cancelamento: Suspensão 7 dias
- 3º cancelamento: Suspensão 30 dias
- 4º cancelamento: Banimento permanente

**Cancelamento por violação:**
- Suspensão imediata
- Análise de todos os leilões ativos
- Possível banimento permanente

---

## 🔧 5. FUNCIONALIDADES ESSENCIAIS COMPLEMENTARES

### 5.1 Recuperação de Senha

#### 5.1.1 Fluxo de Recuperação

**Processo Padrão:**
1. Usuário clica em "Esqueci minha senha"
2. Informa email cadastrado
3. Sistema envia token temporário (6 dígitos)
4. Token válido por 15 minutos
5. Usuário define nova senha
6. Confirmação por email

**Validações de Segurança:**
- Máximo 3 tentativas por hora
- Nova senha diferente das últimas 5
- Complexidade mínima obrigatória
- Log de todas as tentativas

#### 5.1.2 Recuperação por SMS

**Para usuários com telefone verificado:**
- Opção alternativa ao email
- Token de 6 dígitos via SMS
- Válido por 10 minutos
- Máximo 2 tentativas por dia

### 5.2 Gestão de Sessão

#### 5.2.1 Controle de Sessões

**Timeout de Sessão:**
- Sessão inativa: 30 minutos
- Sessão ativa (em leilão): 2 horas
- Aviso 5 minutos antes do timeout
- Renovação automática durante lances

**Sessões Simultâneas:**
- Máximo 3 dispositivos por usuário
- Notificação de novo login
- Opção de encerrar outras sessões
- Log de todos os acessos

#### 5.2.2 Logout Automático

**Gatilhos:**
- Inatividade prolongada
- Tentativas de acesso suspeitas
- Mudança de senha
- Solicitação do usuário

### 5.3 Validação de Dados

#### 5.3.1 Validação de CPF/CNPJ

**Processo de Validação:**
1. Validação de formato
2. Cálculo de dígitos verificadores
3. Consulta à base da Receita Federal
4. Verificação de situação cadastral
5. Armazenamento do resultado

**Frequência de Validação:**
- Nova validação a cada 30 dias
- Validação imediata em transações > R$ 1.000
- Revalidação após alterações cadastrais

#### 5.3.2 Sanitização de Inputs

**Campos de Texto:**
- Remoção de scripts maliciosos
- Limitação de caracteres especiais
- Validação de encoding UTF-8
- Filtro de palavras proibidas

**Uploads de Arquivo:**
- Validação de tipo MIME
- Scan de vírus/malware
- Limitação de tamanho
- Renomeação automática

### 5.4 Configurações do Sistema

#### 5.4.1 Parâmetros Configuráveis

**Taxas e Valores:**
- Taxa de comissão por categoria
- Valores mínimos de lance
- Limites de transação
- Taxas de cancelamento

**Tempos e Prazos:**
- Duração padrão de leilões
- Prazos de pagamento
- Timeouts de sessão
- Janelas de anti-sniping

**Regras de Negócio:**
- Incrementos mínimos
- Limites de verificação
- Critérios de risco
- Políticas de disputa

#### 5.4.2 Interface Administrativa

**Painel de Configurações:**
- Interface web para administradores
- Histórico de alterações
- Aprovação de mudanças críticas
- Backup automático de configurações

---

## 💾 6. IMPACTO NO MODELO DE DADOS

### 6.1 Novas Entidades Necessárias

#### 6.1.1 Entidade FRAUD_DETECTION

```sql
CREATE TABLE fraud_detection (
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES usuario(id),
    auction_id UUID REFERENCES produto(product_id),
    detection_type VARCHAR(50) NOT NULL,
    risk_score INTEGER NOT NULL,
    details JSONB,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT NOW(),
    resolved_at TIMESTAMP,
    resolved_by UUID REFERENCES usuario(id)
);
```

#### 6.1.2 Entidade BLACKLIST

```sql
CREATE TABLE blacklist (
    id UUID PRIMARY KEY,
    entity_type VARCHAR(20) NOT NULL, -- 'USER', 'IP', 'DEVICE'
    entity_value VARCHAR(255) NOT NULL,
    reason VARCHAR(500) NOT NULL,
    blocked_by UUID REFERENCES usuario(id),
    blocked_at TIMESTAMP DEFAULT NOW(),
    expires_at TIMESTAMP,
    status VARCHAR(20) DEFAULT 'ACTIVE'
);
```

#### 6.1.3 Entidade AUCTION_EXTENSION

```sql
CREATE TABLE auction_extension (
    id UUID PRIMARY KEY,
    auction_id UUID REFERENCES produto(product_id),
    extension_number INTEGER NOT NULL,
    original_end_time TIMESTAMP NOT NULL,
    new_end_time TIMESTAMP NOT NULL,
    trigger_bid_id UUID REFERENCES lance(bid_id),
    created_at TIMESTAMP DEFAULT NOW()
);
```

#### 6.1.4 Entidade DISPUTE

```sql
CREATE TABLE dispute (
    id UUID PRIMARY KEY,
    sale_id UUID REFERENCES arremate(sale_id),
    buyer_id UUID REFERENCES comprador(buyer_id),
    seller_id UUID REFERENCES vendedor(seller_id),
    category VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    evidence JSONB,
    status VARCHAR(20) DEFAULT 'OPEN',
    priority VARCHAR(10) DEFAULT 'MEDIUM',
    assigned_to UUID REFERENCES usuario(id),
    created_at TIMESTAMP DEFAULT NOW(),
    resolved_at TIMESTAMP,
    resolution TEXT
);
```

### 6.2 Modificações em Entidades Existentes

#### 6.2.1 Tabela USUARIO

```sql
-- Adicionar campos de verificação
ALTER TABLE usuario ADD COLUMN kyc_level INTEGER DEFAULT 0;
ALTER TABLE usuario ADD COLUMN kyc_verified_at TIMESTAMP;
ALTER TABLE usuario ADD COLUMN risk_score INTEGER DEFAULT 0;
ALTER TABLE usuario ADD COLUMN last_risk_update TIMESTAMP;
ALTER TABLE usuario ADD COLUMN transaction_limits JSONB;
```

#### 6.2.2 Tabela PRODUTO

```sql
-- Adicionar campos de anti-sniping
ALTER TABLE produto ADD COLUMN anti_sniping_enabled BOOLEAN DEFAULT true;
ALTER TABLE produto ADD COLUMN snipe_window_seconds INTEGER DEFAULT 30;
ALTER TABLE produto ADD COLUMN max_extensions INTEGER DEFAULT 5;
ALTER TABLE produto ADD COLUMN extensions_used INTEGER DEFAULT 0;
```

#### 6.2.3 Tabela LANCE

```sql
-- Adicionar campos de detecção de fraude
ALTER TABLE lance ADD COLUMN device_fingerprint VARCHAR(255);
ALTER TABLE lance ADD COLUMN risk_flags JSONB;
ALTER TABLE lance ADD COLUMN validation_status VARCHAR(20) DEFAULT 'VALID';
```

---

## 📝 7. HISTÓRIAS DE USUÁRIO

### 7.1 Gestão de Fraudes

#### H-FRAUD-01: Detecção Automática de Padrões Suspeitos
**Como** sistema  
**Quero** detectar automaticamente padrões suspeitos de lance  
**Para** prevenir fraudes e manipulação de leilões

**Critérios de Aceite:**
- Sistema detecta lances sequenciais do mesmo usuário
- Sistema detecta incrementos mínimos repetitivos
- Sistema detecta velocidade anômala de lances
- Alertas são gerados automaticamente
- Logs detalhados são mantidos

#### H-FRAUD-02: Sistema de KYC
**Como** administrador  
**Quero** verificar a identidade dos usuários  
**Para** garantir a legitimidade dos participantes

**Critérios de Aceite:**
- Usuário pode enviar documentos para verificação
- Sistema valida documentos automaticamente quando possível
- Processo de aprovação manual para casos complexos
- Níveis de verificação definem limites de transação
- Notificações são enviadas sobre status da verificação

#### H-FRAUD-03: Sistema de Blacklist
**Como** administrador  
**Quero** bloquear usuários, IPs e dispositivos suspeitos  
**Para** prevenir atividades fraudulentas

**Critérios de Aceite:**
- Posso adicionar entradas à blacklist com motivo
- Sistema bloqueia automaticamente acessos da blacklist
- Posso definir bloqueios temporários ou permanentes
- Usuários bloqueados recebem notificação adequada
- Processo de recurso está disponível

### 7.2 Anti-Sniping

#### H-SNIPE-01: Extensão Automática de Leilão
**Como** sistema  
**Quero** estender automaticamente leilões quando há lances de última hora  
**Para** garantir oportunidade justa a todos os participantes

**Critérios de Aceite:**
- Leilão é estendido quando lance é feito na janela de sniping
- Tempo de extensão é configurável
- Máximo de extensões é respeitado
- Participantes são notificados em tempo real
- Histórico de extensões é mantido

#### H-SNIPE-02: Configuração de Anti-Sniping
**Como** administrador  
**Quero** configurar parâmetros de anti-sniping por categoria  
**Para** adequar as regras a diferentes tipos de produto

**Critérios de Aceite:**
- Posso definir janela de extensão por categoria
- Posso definir tempo de extensão por categoria
- Posso definir máximo de extensões por categoria
- Posso ativar/desativar anti-sniping por categoria
- Configurações são aplicadas automaticamente

### 7.3 Gestão de Disputas

#### H-DISPUTE-01: Abertura de Disputa
**Como** comprador ou vendedor  
**Quero** abrir uma disputa sobre uma transação  
**Para** resolver problemas com a compra/venda

**Critérios de Aceite:**
- Posso selecionar categoria da disputa
- Posso anexar evidências (fotos, documentos)
- Posso descrever o problema detalhadamente
- Sistema valida prazo para abertura
- Outra parte é notificada automaticamente

#### H-DISPUTE-02: Mediação de Disputa
**Como** mediador  
**Quero** analisar e mediar disputas  
**Para** resolver conflitos entre usuários

**Critérios de Aceite:**
- Posso visualizar todas as informações da disputa
- Posso solicitar informações adicionais das partes
- Posso propor soluções baseadas em políticas
- Posso executar soluções aprovadas
- Histórico completo é mantido

### 7.4 Cancelamento de Leilões

#### H-CANCEL-01: Cancelamento pelo Vendedor
**Como** vendedor  
**Quero** cancelar meu leilão quando necessário  
**Para** evitar problemas com produtos indisponíveis

**Critérios de Aceite:**
- Posso cancelar leilão antes do início sem restrições
- Sistema aplica taxas conforme regras
- Participantes são notificados automaticamente
- Justificativa é obrigatória em alguns casos
- Histórico de cancelamentos é mantido

#### H-CANCEL-02: Cancelamento Administrativo
**Como** administrador  
**Quero** cancelar leilões que violam políticas  
**Para** manter a integridade da plataforma

**Critérios de Aceite:**
- Posso cancelar qualquer leilão com justificativa
- Participantes são compensados conforme regras
- Vendedor é penalizado se aplicável
- Relatório de cancelamento é gerado
- Ações corretivas são registradas

---

## ✅ 8. CRITÉRIOS DE ACEITE DETALHADOS

### 8.1 Critérios Técnicos

#### 8.1.1 Performance
- Detecção de fraude deve processar em < 100ms
- Sistema de blacklist deve responder em < 50ms
- Extensões de leilão devem ser aplicadas em < 1s
- Abertura de disputa deve processar em < 2s

#### 8.1.2 Disponibilidade
- Sistema de fraude deve ter 99.9% de uptime
- Anti-sniping deve funcionar 24/7 sem falhas
- Blacklist deve ser consultada em todas as operações
- Disputas devem ser acessíveis a qualquer momento

#### 8.1.3 Segurança
- Todos os dados sensíveis devem ser criptografados
- Logs de auditoria devem ser imutáveis
- Acesso a funções administrativas deve ser logado
- Comunicações devem usar HTTPS/WSS

### 8.2 Critérios Funcionais

#### 8.2.1 Usabilidade
- Interface de disputa deve ser intuitiva
- Notificações devem ser claras e acionáveis
- Processo de verificação deve ser simples
- Configurações devem ter valores padrão sensatos

#### 8.2.2 Conformidade
- Sistema deve atender LGPD
- Logs devem ter retenção adequada
- Dados pessoais devem ser protegidos
- Direito ao esquecimento deve ser respeitado

### 8.3 Critérios de Negócio

#### 8.3.1 Eficácia
- Taxa de detecção de fraude > 95%
- Tempo médio de resolução de disputa < 5 dias
- Taxa de cancelamento de leilão < 2%
- Satisfação do usuário > 4.5/5

#### 8.3.2 Eficiência
- Custo de operação de fraude < 1% da receita
- Automação de 80% das verificações KYC
- Resolução automática de 60% das disputas simples
- Redução de 90% em lances suspeitos

---

## 🎯 PRÓXIMOS PASSOS

### Implementação Prioritária (Sprint 2-3)
1. Sistema básico de detecção de fraude
2. KYC nível 1 (verificação básica)
3. Anti-sniping com configuração padrão
4. Blacklist básica de IPs/usuários

### Implementação Secundária (Sprint 4-5)
1. Sistema completo de disputas
2. KYC níveis 2 e 3
3. Regras avançadas de cancelamento
4. Relatórios e métricas

### Implementação Futura (Pós-MVP)
1. Machine Learning para detecção de fraude
2. Integração com bureaus de crédito
3. Sistema de reputação avançado
4. Análise preditiva de comportamento

---

**Documento aprovado por:** [Arquiteto/Product Owner]  
**Data de aprovação:** [Data]  
**Próxima revisão:** [Data + 30 dias]