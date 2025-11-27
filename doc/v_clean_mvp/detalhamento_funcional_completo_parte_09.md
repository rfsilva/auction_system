# 📘 Parte 9 — Regras de Negócio do Administrador  
Documento de Requisitos Funcionais — Versão Consolidada (Waterfall)

---

# 9. REGRAS DE NEGÓCIO — ADMINISTRADOR DO SISTEMA

O Administrador representa o papel de maior privilégio dentro da plataforma.  
Suas regras de negócio envolvem governança, auditoria, moderação, segurança, gestão de vendedores, gestão de compradores, controle de leilões, finanças e monitoramento operacional.

---

## 9.1. Gestão de Usuários

### **RN-A1 — Acesso Total ao Cadastro de Usuários**
O administrador pode visualizar:
- Todos os usuários (visitantes convertidos, participantes, compradores, vendedores)  
- Status de cada conta  
- Histórico de atividades  
- Logs relevantes

### **RN-A2 — Alteração de Status de Conta**
O admin pode alterar o status de:
- Compradores  
- Vendedores  

Status possíveis:
1. **Ativo**  
2. **Pendente**  
3. **Suspenso** (temporário)  
4. **Bloqueado** (permanente)

### **RN-A3 — Justificativa Obrigatória**
Qualquer suspensão ou bloqueio deve registrar:
- Motivo  
- Data  
- Usuário administrador responsável  
- Evidências anexadas (quando aplicável)

### **RN-A4 — Reativação de Contas**
Reativação só é permitida mediante:
- Revisão das evidências  
- Expiração da penalidade  
- Autorização formal (quando necessário)

---

## 9.2. Gestão de Vendedores

### **RN-A5 — Validação de Documentação**
O administrador pode:
- Aprovar  
- Rejeitar  
- Solicitar complementar  
- Suspender temporariamente vendedores  
com base na conformidade documental.

### **RN-A6 — Auditoria de Produtos**
O admin pode:
- Acessar todos os produtos publicados  
- Remover produtos irregulares  
- Desativar produtos suspeitos  
- Inserir advertências para vendedores reincidentes

### **RN-A7 — Penalidades Automáticas e Manuais**
O administrador pode aplicar penalidades por:
- Violação de políticas  
- Tentativa de fraude  
- Publicação de itens proibidos  
- Comportamento abusivo com compradores  
- Reincidência

As penalidades seguem uma matriz de severidade.

---

## 9.3. Gestão de Produtos e Lotes

### **RN-A8 — Acesso Total aos Catálogos**
O admin pode gerenciar:
- Produtos individuais  
- Lotes  
- Agrupamentos  
- Categorias  

### **RN-A9 — Cancelamento de Lotes ou Produtos**
O admin pode:
- Cancelar lotes antes do início do leilão  
- Encerrar leilões ativos em caso de fraude  
- Bloquear produtos reportados  

### **RN-A10 — Alterações Manuais**
O admin pode corrigir:
- Títulos  
- Categorias  
- Descrições  
quando detectado erro evidente ou violação das regras.

---

## 9.4. Gestão de Leilões Ativos

### **RN-A11 — Acompanhamento em Tempo Real**
O administrador tem acesso privilegiado ao painel de:
- Lances ativos  
- Evolução de preço  
- Conflitos entre compradores  
- Eventos SSE ou WebSocket (monitoramento técnico)

### **RN-A12 — Intervenção no Leilão**
O admin pode:
- Pausar um leilão  
- Prolongar um tempo extra (quando permitido pelas regras)  
- Cancelar lances suspeitos  
- Remover usuários que tentam manipular o processo  

### **RN-A13 — Auditoria de Histórico de Lances**
Todos os lances ficam disponíveis para auditoria, incluindo:
- Timestamp  
- Origem  
- Usuário  
- Valor  
- Canal (SSE, WebSocket, REST)

---

## 9.5. Disputas, Reclamações e Mediação

### **RN-A14 — Abertura e Tratamento de Disputas**
O admin pode atuar como mediador entre:
- Comprador  
- Vendedor  

### **RN-A15 — Registro de Evidências**
Cada disputa deve conter:
- Mensagens trocadas  
- Fotos e comprovantes  
- Logs de envio  
- Histórico de prazos  
- Parecer final do administrador

### **RN-A16 — Decisão Final**
A decisão do administrador:
- Pode determinar devolução  
- Pode determinar cancelamento  
- Pode determinar liberação do pagamento  
- É registrada e vinculada ao caso permanentemente  

### **RN-A17 — Prevenção de Abusos**
O admin pode investigar:
- Fraudes sistemáticas  
- Manipulações de preço  
- Condutas abusivas  

---

## 9.6. Pagamentos, Contabilidade e Financeiro

### **RN-A18 — Acesso ao Financeiro**
O admin pode visualizar:
- Extrato da plataforma  
- Extrato dos vendedores  
- Tarifas aplicadas  
- tarifas do leiloeiro (configuração por contrato)  
- Histórico de saques  
- Pagamentos pendentes  

### **RN-A19 — Liberação Manual de Valores**
O admin pode liberar valores retidos mediante:
- Comprovação de entrega  
- Solução de disputa  
- Revisão de suspeita de fraude  

### **RN-A20 — Configuração de Tarifas**
O admin pode configurar:
- Percentual da taxa do leiloeiro  
- Comissão da plataforma  
- Tarifas extras  
- Descontos promocionais  

### **RN-A21 — Auditoria de Movimentações**
Cada movimentação financeira deve registrar:
- Valor  
- Origem  
- Destino  
- Identificador da operação  
- Justificativa  
- Admin responsável (se manual)  

---

## 9.7. Integrações e Operações Técnicas

### **RN-A22 — Gestão de Integrações Externas**
Admin controla:
- API de cálculo de frete  
- Gateways de pagamento  
- Plataformas antifraude  
- Serviços de notificação  

### **RN-A23 — Monitoramento Operacional**
O administrador tem acesso ao painel técnico (em modo somente leitura):
- Logs  
- Métricas  
- Status dos serviços  
- Filas  
- Eventos SSE  
- Conexões WebSocket  
- Health check dos microsserviços  

### **RN-A24 — Intervenção Técnica**
Apenas administradores de nível técnico podem:
- Reiniciar serviços  
- Executar rotinas de manutenção  
- Forçar limpeza de cache  
- Regenerar índices

---

## 9.8. Auditoria, Segurança e Compliance

### **RN-A25 — Logs Imutáveis**
Todas as ações administrativas devem ser:
- Logadas  
- Imutáveis  
- Vinculadas ao administrador  

### **RN-A26 — Políticas de Segurança**
O admin deve seguir:
- MFA obrigatório  
- Rotação periódica de credenciais  
- Perfis de acesso restritos (RBAC)  

### **RN-A27 — Análise de Fraudes**
O admin pode revisar:
- Movimentações suspeitas  
- Padrões irregulares de compra  
- Múltiplas contas vinculadas  
- Leilões manipulados  

### **RN-A28 — Restrições de Acesso**
Ações críticas exigem:
- Confirmação via MFA  
- Justificativa textual  
- Registro via trilha de auditoria

---

## 9.9. Notificações do Administrador

### **RN-A29 — Importância das Notificações**
O administrador recebe alertas sobre:
- Suspeitas de fraude  
- Cancelamentos acima da média  
- Reclamações recentes  
- Falhas técnicas  
- Erros em integrações  

### **RN-A30 — Tipos de Alerta**
1. **Crítico** — intervenção imediata  
2. **Alto** — risco relevante  
3. **Médio** — acompanhamento  
4. **Baixo** — informativo  

---

## 9.10. Governança da Plataforma

### **RN-A31 — Políticas e Termos**
O administrador gerencia:
- Termos de uso  
- Políticas de privacidade  
- Política de vendedores  
- Política de devoluções  

### **RN-A32 — Histórico de Alterações**
Toda mudança deve:
- Gerar nova versão  
- Registrar quem alterou  
- Ter data de vigência  
- Permitir download de versões anteriores  

### **RN-A33 — Transparência**
O sistema deve fornecer relatórios consolidados sobre:
- Quantidade de leilões  
- Volume financeiro  
- Disputas  
- Vendedores punidos  
- Melhores vendedores e compradores  

---

# ✔ Encerramento da Parte 9
Esta parte consolida todas as regras de negócio para o papel **Administrador**, compondo o bloco final do conjunto de requisitos funcionais do sistema antes da fase de unificação e conversão para backlog ágil.

