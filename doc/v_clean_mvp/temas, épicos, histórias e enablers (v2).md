# BACKLOG CONSOLIDADO — SISTEMA DE LEILÃO ELETRÔNICO
## Tabela única para validação de temas, épicos e histórias

| ID | Tipo | Tema | Épico | Item | Descrição |
|----|------|-------|--------|-------|------------|

### ---------------------------
### 🧩 TEMA 1 — GESTÃO DE USUÁRIOS E ACESSO
### ---------------------------

| H1-01 | História | Gestão de Usuários | Cadastro e Identificação | Cadastro de Usuário | Visitante cria conta com dados pessoais, senha e aceite de termos. |
| H1-02 | História | Gestão de Usuários | Cadastro e Identificação | Verificação de Duplicidade | Sistema valida CPF/CNPJ já existente. |
| H1-03 | História | Gestão de Usuários | Perfis e Papéis | Atribuição de Papéis | Admin define se o usuário é comprador, vendedor ou ambos. |
| H1-04 | História | Gestão de Usuários | Perfis e Papéis | Gestão de Permissões | Sistema aplica permissões automáticas por papel. |
| H1-05 | História | Gestão de Usuários | Documentação e KYC | Envio de Documentos | Participante envia documentos obrigatórios (RG, CNH, comprovante). |
| H1-06 | História | Gestão de Usuários | Documentação e KYC | Validação Manual | Admin revisa documentação e aprova/nega. |
| H1-07 | História | Gestão de Usuários | Login e Segurança | Autenticação | Login com MFA opcional. |
| H1-08 | História | Gestão de Usuários | Login e Segurança | Recuperação de Acesso | Recuperação de senha via e-mail. |

### ---------------------------
### 🧩 TEMA 2 — GESTÃO DE LEILÕES
### ---------------------------

| H2-01 | História | Gestão de Leilões | Cadastro de Leilões | Criar Leilão | Vendedor cria leilão com título, descrição, regras e datas. |
| H2-02 | História | Gestão de Leilões | Cadastro de Leilões | Validar Datas | Sistema valida início > agora, fim > início, duração mínima. |
| H2-03 | História | Gestão de Leilões | Publicação e Visibilidade | Publicar Leilão | Vendedor envia para aprovação e admin publica. |
| H2-04 | História | Gestão de Leilões | Publicação e Visibilidade | Regras de Cancelamento | Leilão pode ser suspenso por admin com justificativa. |

### ---------------------------
### 🧩 TEMA 3 — GESTÃO DE LOTES
### ---------------------------

| H3-01 | História | Gestão de Lotes | Cadastro de Lotes | Criar Lote | Vendedor cadastra lote com fotos, descrição e preço mínimo. |
| H3-02 | História | Gestão de Lotes | Cadastro de Lotes | Upload de Anexos | Upload de documentos PDF/IMG. |
| H3-03 | História | Gestão de Lotes | Edição e Manutenção | Editar Lote | Permite atualização antes da abertura. |
| H3-04 | História | Gestão de Lotes | Regras de Lote | Incremento e Preço | Define incremento mínimo e preço de reserva. |

### ---------------------------
### 🧩 TEMA 4 — PARTICIPAÇÃO E DISPUTA
### ---------------------------

| H4-01 | História | Participação | Habilitação | Solicitar Habilitação | Comprador solicita habilitação para um leilão. |
| H4-02 | História | Participação | Habilitação | Validar Habilitação | Admin valida documentação e requisitos. |
| H4-03 | História | Participação | Lances | Enviar Lance | Comprador envia lance manual. |
| H4-04 | História | Participação | Lances | Validar Incremento | Sistema valida incremento mínimo. |
| H4-05 | História | Participação | Lances | Lance Automático | Cadastro e execução de auto-lance. |
| H4-06 | História | Participação | Encerramento | Determinar Vencedor | Sistema calcula e publica o vencedor. |
| H4-07 | História | Participação | Encerramento | Relatório de Disputa | Geração do relatório oficial do lote. |

### ---------------------------
### 🧩 TEMA 5 — PAGAMENTOS E LIQUIDAÇÃO
### ---------------------------

| H5-01 | História | Pagamentos | Instruções | Enviar Instruções | Sistema envia dados bancários ao vencedor. |
| H5-02 | História | Pagamentos | Liquidação | Confirmar Pagamento | API com banco atualiza status. |
| H5-03 | História | Pagamentos | Liquidação | Notificação de Atraso | Sistema alerta comprador após prazo. |
| H5-04 | História | Pagamentos | Repasse ao Vendedor | Emissão de Comprovante | Sistema gera comprovante de repasse. |

### ---------------------------
### 🧩 TEMA 6 — REGRAS DE NEGÓCIO E COMPLIANCE
### ---------------------------

| H6-01 | História | Regras e Compliance | Filtros e Critérios | Regras de Habilitação | Sistema verifica requisitos automaticamente. |
| H6-02 | História | Regras e Compliance | Regras de Lances | Lance Mínimo | Regra configurável via painel admin. |
| H6-03 | História | Regras e Compliance | Auditoria | Regras de Auditoria | Controle de mudanças sensíveis. |
| H6-04 | História | Regras e Compliance | Antifraude | Validação de Padrões | Sistema detecta comportamento suspeito. |

### ---------------------------
### 🧩 TEMA 7 — AUDITORIA E RELATÓRIOS
### ---------------------------

| H7-01 | História | Auditoria | Trilhas | Log de Atividades | Registro completo de ações (quem/onde/quando). |
| H7-02 | História | Auditoria | Relatórios | Relatório Gerencial | Painel de acompanhamento. |
| H7-03 | História | Auditoria | Relatórios | Relatório Legal | Exportação conforme legislação. |

### ---------------------------
### 🧩 TEMA 8 — SEGURANÇA E AUTORIZAÇÃO
### ---------------------------

| H8-01 | História | Segurança | Autenticação | MFA | Configurar MFA opcional/obrigatório. |
| H8-02 | História | Segurança | Autorização | RBAC | Autorização por papel / permissão. |
| H8-03 | História | Segurança | Sessões | Controle de Sessão | Expiração inteligente e segurança. |

### ---------------------------
### 🧩 TEMA 9 — INTEGRAÇÕES
### ---------------------------

| H9-01 | História | Integrações | Bancos | API Bancária | Consulta e reconciliação bancária. |
| H9-02 | História | Integrações | Pagamentos | Gateway | Integração com gateway (Pix, cartão, boleto). |
| H9-03 | História | Integrações | Documentos | Repositório | Upload para storage externo. |
| H9-04 | História | Integrações | Analytics | Observabilidade | Streaming de eventos para analytics. |

### ---------------------------
### 🧩 TEMA 10 — ADMINISTRAÇÃO, CATÁLOGOS E CONFIGURAÇÕES
### ---------------------------

| H10-01 | História | Admin | Configs | Regras Operacionais | Configurar incrementos, prazos, taxas. |
| H10-02 | História | Admin | Catálogos | Gerenciar Categorias | Categorias de leilões e lotes. |
| H10-03 | História | Admin | Painel | Painel Administrativo | Gerenciamento geral do sistema. |

### ---------------------------
### 🧩 TEMA 11 — ENABLERS TÉCNICOS
### ---------------------------

| EN11-01 | Enabler | Enablers | Arquitetura | BFF/Backend | Definir e documentar arquitetura completa. |
| EN11-02 | Enabler | Enablers | Arquitetura | Domain Model | Criar o domínio (DDD). |
| EN11-03 | Enabler | Enablers | CI/CD | Pipeline | Criar pipeline CI/CD. |
| EN11-04 | Enabler | Enablers | Testes | Automação de Testes | Testes de API e UI. |
| EN11-05 | Enabler | Enablers | Observabilidade | Correlation-Id | Implementar rastreamento distribuído. |
| EN11-06 | Enabler | Enablers | Logs | Logging Estruturado | Padronizar logs. |
| EN11-07 | Enabler | Enablers | Segurança | Hardening | Regras OWASP, rate limiting, etc. |
| EN11-08 | Enabler | Enablers | Monitoramento | APM | Telemetria completa. |
| EN11-09 | Enabler | Enablers | API | OpenAPI | Padronizar contratos. |

