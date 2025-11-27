# 📘 Detalhamento Funcional — Papel: **Administrador**
O Administrador é o “chefão” operacional do sistema. Ele garante que tudo tá funcionando, gerencia usuários, monitora as operações e controla parâmetros sensíveis.  
É o único papel com acesso total aos módulos críticos.

---

## 👤 1. Perfil do Administrador
- Usuário interno autorizado pela organização.
- Possui privilégios máximos dentro do sistema.
- Atua na governança, monitoramento e regulação das atividades de compradores, vendedores e visitantes autenticados.
- Pode visualizar, auditar, configurar e corrigir o funcionamento da plataforma.

---

# 🧩 2. Ações e Funcionalidades por Macroprocesso

---

## 🔐 2.1. Gestão de Acessos e Usuários
Funções relacionadas ao controle de perfis, permissões e integridade dos usuários do sistema.

### **2.1.1. Visualizar Usuários**
- Buscar usuários por filtros:
  - Nome, email, status (ativo/inativo), papel (comprador, vendedor), data de cadastro.
- Paginação e ordenação.
- Exibir dados completos do usuário:
  - Identificação, dados pessoais permitidos, atividades recentes, status do cadastro, pendências.

### **2.1.2. Aprovar Cadastro (somente vendedores)**
- Analisar documentação enviada pelo vendedor.
- Validar:
  - Identidade  
  - Informações comerciais  
  - Dados bancários  
- Aprovar ou rejeitar, registrando justificativa.

### **2.1.3. Bloquear / Desbloquear Usuário**
- Aplicado a compradores e vendedores.
- Motivos:
  - Violação de regras de uso  
  - Comportamento suspeito  
  - Indisponibilidade técnica  
- Gera log automático.

### **2.1.4. Resetar Senha**
- Envia link de redefinição.
- Permite execução manual em caso de suporte.

---

## 🛒 2.2. Gestão de Produtos e Catálogo
Controle sobre o conteúdo exposto por vendedores.

### **2.2.1. Visualizar Produtos**
- Filtrar por vendedor, status, preço, categoria.
- Visualizar detalhes:
  - Fotos, descrição, preço, estoque, histórico de alterações.

### **2.2.2. Moderar Produtos**
- Aprovar produtos que exigem validação (se aplicável).
- Remover produtos inadequados:
  - Conteúdo ofensivo  
  - Informação falsa  
  - Irregularidade legal  
- Bloqueio temporário ou definitivo.
- Registrar justificativa obrigatória.

---

## 💰 2.3. Gestão de Transações
Operações relacionadas ao fluxo financeiro (compra/venda) e seus registros.

### **2.3.1. Visualizar Transações**
- Consultar qualquer transação com:
  - Valor
  - Itens
  - Vendedor
  - Comprador
  - Data
  - Status (pendente, concluída, cancelada)
- Filtros avançados:
  - Período, faixa de valor, vendedor, tipo de operação.

### **2.3.2. Analisar Transações Suspeitas**
- Ver transações sinalizadas automaticamente por regras antifraude.
- Classificar:
  - “Confirmada segura”
  - “Fraude confirmada”
  - “Em revisão”
- Ações possíveis:
  - Cancelamento da transação  
  - Suspensão temporária de usuário  
  - Escalonamento para time de segurança  

### **2.3.3. Intervir em Transações**
- Cancelar compras mediante justificativa.
- Reembolsar pedidos seguindo política.
- Atualizar status manualmente em caso de falha sistêmica.

---

## 🏛 2.4. Gestão de Conteúdo e Comunicação
Administração dos elementos informativos da plataforma.

### **2.4.1. Editar Páginas Institucionais**
- Termos de uso  
- Política de privacidade  
- FAQ  
- Regras da plataforma

### **2.4.2. Gerenciar Avisos Globais**
- Criar anúncios que aparecem para todos os usuários:
  - Avisos de manutenção  
  - Mudanças de política  
  - Informativos gerais

### **2.4.3. Moderação de Comentários / Avaliações**
- Remover conteúdo inapropriado.
- Notificar usuários que violaram normas.
- Reverter avaliações se houver abuso.

---

## 🛠 2.5. Configurações Sistêmicas e Operacionais
Configurações mais delicadas e estratégicas.

### **2.5.1. Parametrização do Sistema**
- Ajustar regras:
  - Limites de valor por transação  
  - Limites diários de compra  
  - Políticas de comissão  
  - Configurações antifraude  
  - Tempo máximo de sessão (front-end)  

### **2.5.2. Configurar Integrações**
- Gateways de pagamento  
- Sistema de notificação (email, push, SMS)  
- Integradores externos  

### **2.5.3. Monitorar Filas, Serviços e SSE/WebSocket**
- Ver status dos canais assíncronos.
- Identificar gargalos.
- Reiniciar workers (se permitido pela política interna).

---

## 📊 2.6. Auditoria e Relatórios
O Administrador tem acesso privilegiado à informação consolidada.

### **2.6.1. Visualizar Logs do Sistema**
- Acessar histórico de:
  - Logins  
  - Ações administrativas  
  - Interações críticas (aprovação, banimento, cancelamento)  

### **2.6.2. Exportar Relatórios**
- CSV / XLSX / PDF.
- Exemplos de relatórios:
  - Crescimento de usuários  
  - Vendas por período  
  - Produtos mais reportados  
  - Falhas por módulo  
  - Métricas de engajamento  

### **2.6.3. Monitoramento de Performance**
- Acessar dashboards:
  - Latência média  
  - Taxa de erro  
  - Disponibilidade  
  - Tarefas pendentes nas filas  

---

# 🔒 3. Regras de Negócio — Administrador

### **RN-ADM-01 — Justificativas Obrigatórias**
Toda ação restritiva (bloqueio, remoção, cancelamento, intervenção) deve conter justificativa textual.

### **RN-ADM-02 — Rastreamento Completo**
Toda operação do administrador deve ser logada com:
- ID do admin  
- Timestamp  
- IP  
- Ação  
- Payload afetado  

### **RN-ADM-03 — Não Pode Alterar Dados Sensíveis de Outros Papéis**
O administrador **não** pode:
- Criar transações  
- Comprar produtos  
- Vender produtos  
- Modificar informações pessoais sensíveis sem consentimento do proprietário  
   - Ex.: documentos, dados bancários.  
(Exceto campos requeridos para cumprimento de lei.)

### **RN-ADM-04 — Alterações em Regras Afetam Todo o Sistema**
Toda configuração global deve:
- Disparar evento de auditoria  
- Possuir rollback  
- Notificar vendedores, se aplicável  

### **RN-ADM-05 — Políticas de Segurança**
O admin deve usar:
- MFA obrigatória  
- Acessos com expiração  
- Sessões curtas em casos específicos  

---

# 🔚 4. Encerrando o Bloco do Administrador
Esse documento cobre:
- Perfis  
- Ações detalhadas  
- Casos de uso  
- Regras de negócio estritas  
- Atribuições exclusivas  

Pronto pra integração no documento funcional completo.
