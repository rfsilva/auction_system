# 📘 Documento Funcional Detalhado — Parte 4  
## Sistema de Eventos — Regras de Negócio, Integrações e Critérios de Aceite  
### **(Consolidado — Seção 4 de N)**

---

# 4. Regras de Negócio Gerais

## 4.1. Padrões Gerais do Sistema
- **RN-GEN-001 — Identificação dos Papéis**  
  O sistema deve identificar e aplicar automaticamente permissões específicas a cada papel (Visitante, Participante, Comprador, Vendedor, Administrador).  
- **RN-GEN-002 — Controle de Sessão**  
  Sessões expiram em 30 minutos de inatividade.  
- **RN-GEN-003 — Segurança**  
  Dados sensíveis devem ser trafegados em TLS 1.2+ e armazenados criptografados sempre que necessário.  
- **RN-GEN-004 — Log de Ações Sensíveis**  
  Tudo que envolve cadastro, venda, configuração, permissão ou pagamento deve ser logado.  
- **RN-GEN-005 — Auditoria Temporal**  
  Todos os registros possuem `created_at`, `updated_at` e `deleted_at` (soft delete).  
- **RN-GEN-006 — Notificações em Tempo Real**  
  SSE ou WebSocket devem ser utilizados para atualização em tempo real de:  
  - Alterações de disponibilidade de ingressos.  
  - Confirmações de compra.  
  - Atualizações feitas por vendedores e administradores.

---

# 5. Regras de Negócio por Papel

## 5.1. Visitante
- **RN-VIS-001 — Acesso Livre**  
  Visitantes podem navegar por todos os eventos públicos sem autenticação.  
- **RN-VIS-002 — Conteúdo Restrito**  
  Visitantes NÃO podem realizar compras, inscrições ou acessar dados privilegiados.  
- **RN-VIS-003 — Filtragem e Busca**  
  Visitantes podem buscar eventos por:
  - Nome
  - Categoria
  - Data
  - Local
  - Faixa de preço  
- **RN-VIS-004 — Visualização de Detalhes**  
  Devem ver descrição, agenda, imagens, mapa e disponibilidade.  
- **RN-VIS-005 — CTA de Cadastro**  
  Toda ação bloqueada deve redirecionar para uma página de login/cadastro.

---

## 5.2. Participante / Comprador
- **RN-COM-001 — Autenticação Necessária**  
  Apenas usuários autenticados podem comprar ingressos.  
- **RN-COM-002 — Carrinho Multi-Evento**  
  Compradores podem colocar ingressos de múltiplos eventos no carrinho, desde que não exceda limite por evento.  
- **RN-COM-003 — Limite por Pessoa**  
  Cada evento pode configurar um limite, e o sistema deve bloquear ultrapassagens.  
- **RN-COM-004 — Reserva Temporária**  
  Itens no carrinho ficam reservados por 15 minutos. Após isso, voltam ao estoque.  
- **RN-COM-005 — Pagamento Homologado**  
  Pagamentos devem seguir integração PCI Compliance (veja Seção 7).  
- **RN-COM-006 — Confirmação Imediata**  
  Ao concluir uma compra, o sistema deve:
  - Disparar confirmação
  - Atualizar estoque
  - Registrar dados fiscais (se aplicável)  
- **RN-COM-007 — Histórico do Usuário**  
  Comprador deve acessar todas as compras realizadas, inclusive ingressos cancelados ou expirados.  
- **RN-COM-008 — Cancelamento**  
  Compradores podem solicitar cancelamento se:
  - O evento permitir  
  - O prazo limite não tiver expirado  
  - A normativa local permitir  
- **RN-COM-009 — Uso de Voucher**  
  Compradores podem inserir códigos promocionais válidos.  
- **RN-COM-010 — Prevenção de Fraude**  
  Compras suspeitas devem ser marcadas e bloqueadas até validação.

---

## 5.3. Vendedor
- **RN-VEN-001 — Criação de Eventos**  
  Vendedores podem criar eventos com:
  - Nome, descrição, imagem  
  - Local, data, horários  
  - Lotes e disponibilidade  
  - Regras específicas do evento  
- **RN-VEN-002 — Precificação por Lote**  
  Cada lote de ingresso pode possuir:
  - Valor diferente  
  - Quantidade definida  
  - Validade/expiração  
- **RN-VEN-003 — Publicação**  
  Evento só pode ser publicado se estiver com todos os dados obrigatórios em dia.  
- **RN-VEN-004 — Gestão de Vendas**  
  Vendedor pode visualizar:
  - Métricas por lote  
  - Compradores  
  - Cancelamentos  
  - Receita estimada e real  
- **RN-VEN-005 — Ajuste de Estoque**  
  Estoque só pode ser aumentado, nunca reduzido abaixo do já vendido.  
- **RN-VEN-006 — Controle de Check-in**  
  Vendedor pode habilitar/desabilitar modalidades de entrada: QR Code, lista nominal, ou ambas.

---

## 5.4. Administrador
- **RN-ADM-001 — Configurações Globais**  
  Admin controla:
  - Parâmetros gerais do sistema  
  - Limites globais  
  - Regras fiscais  
  - Templates de e-mail/notificação  
- **RN-ADM-002 — Gestão de Papéis**  
  Admin pode promover, despromover e bloquear usuários.  
- **RN-ADM-003 — Gestão de Eventos de Terceiros**  
  Admin pode visualizar, editar ou suspender eventos de vendedores.  
- **RN-ADM-004 — Auditoria Completa**  
  Deve acessar logs completos e trilhas de auditoria.  
- **RN-ADM-005 — Moderação de Conteúdo**  
  Pode editar, ocultar ou excluir conteúdos inadequados.  
- **RN-ADM-006 — Gerenciamento de Gateway de Pagamento**  
  Admin parametriza:
  - Chaves  
  - Webhooks  
  - Credenciais  
  - Modos de sandbox/produção  

---

# 6. Critérios de Aceite (CA)

## 6.1. Critérios de Aceite Globais
- **CA-GEN-001** — Toda tela deve ser responsiva.  
- **CA-GEN-002** — Ações devem responder em no máximo 3 segundos para 95% dos acessos.  
- **CA-GEN-003** — Logs devem ser registrados em até 2 segundos após ação sensível.  
- **CA-GEN-004** — Comunicação SSE/WebSocket deve reconectar automaticamente.  
- **CA-GEN-005** — Erros devem ser apresentados com mensagens claras ao usuário.

---

# 7. Integrações

## 7.1. Pagamentos
- **INT-PAG-001 — Gateway**  
  Sistema integra com plataforma de pagamento via:
  - Webhook  
  - REST API  
  - Tokenização  
- **INT-PAG-002 — Segurança**  
  Nenhum dado sensível de cartão é armazenado.  
- **INT-PAG-003 — Tentativas de Pagamento**  
  Três tentativas antes de bloquear o pedido.

## 7.2. Notificações
- **INT-NOT-001 — E-mail + Push + SSE/WebSocket**  
  Notificações para:
  - Confirmação de compra  
  - Cancelamento  
  - Alterações de evento  

## 7.3. Logs e Observabilidade
- **INT-OBS-001** — Logs estruturados  
- **INT-OBS-002** — Tracing distribuído via OpenTelemetry  
- **INT-OBS-003** — Métricas expostas em /metrics  

---

# 8. Casos de Uso Globais (Visão Consolidada)

## 8.1. Visitante
- UC-VIS-001 — Consultar eventos  
- UC-VIS-002 — Visualizar detalhes  
- UC-VIS-003 — Buscar e filtrar eventos  

## 8.2. Participante / Comprador
- UC-COM-001 — Criar conta  
- UC-COM-002 — Autenticar  
- UC-COM-003 — Inserir ingressos no carrinho  
- UC-COM-004 — Finalizar compra  
- UC-COM-005 — Consultar histórico  
- UC-COM-006 — Cancelar compra  

## 8.3. Vendedor
- UC-VEN-001 — Criar evento  
- UC-VEN-002 — Editar evento  
- UC-VEN-003 — Gerenciar vendas  
- UC-VEN-004 — Ajustar lotes  
- UC-VEN-005 — Monitorar performance  

## 8.4. Administrador
- UC-ADM-001 — Gerenciar usuários  
- UC-ADM-002 — Moderação  
- UC-ADM-003 — Gerenciar regras e configurações  
- UC-ADM-004 — Auditoria  

---

# 9. Anexos Técnicos
*(Caso queira, eu gero na próxima parte)*

---

**FIM DA PARTE 4 — quer continuar pra parte 5? Só mandar!**  
