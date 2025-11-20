# 10. Requisitos Não Funcionais (RNFs)

Os Requisitos Não Funcionais detalham atributos de qualidade, restrições técnicas, padrões arquiteturais, práticas obrigatórias e expectativas de comportamento global do sistema. Eles afetam todas as funcionalidades, todos os papéis e todos os módulos.

---

## 10.1. Performance e Escalabilidade

### RNF-001 — Tempo de Resposta
- O sistema deve responder **todas as operações críticas** (login, busca de produtos, listagem de carrinho, checkout) em **até 2 segundos** em condições normais de tráfego.
- Operações complexas (relatórios administrativos, exportações) podem levar até **10 segundos**, devendo exibir feedback apropriado de carregamento.

### RNF-002 — Capacidade de Carga
- A plataforma deve suportar, no MVP:
  - **5.000 usuários simultâneos navegando**,  
  - **500 compradores simultâneos realizando operações de compra**,  
  - **200 vendedores simultâneos gerenciando catálogo**,  
  - **50 administradores simultâneos em operações de backoffice**.

### RNF-003 — Escalabilidade Horizontal
- A arquitetura deve permitir adicionar novas instâncias do backend e frontend sem necessidade de reescrita do sistema.
- A comunicação deve ser stateless para possibilitar balanceamento de carga.

### RNF-004 — Suporte a Eventos em Tempo Real
- O sistema deve permitir escala horizontal para uso de SSE e/ou WebSockets sem impacto na consistência das atualizações.
- Eventos não podem ser perdidos; deve haver mecanismo de retry.

---

## 10.2. Segurança

### RNF-005 — Autenticação e Autorização
- O sistema deve utilizar **JWT** assinado para autenticação.
- Tokens devem expirar em período configurável (padrão 15 min + refresh de 1h).
- Perfis:
  - Visitante (não autenticado)
  - Comprador
  - Vendedor
  - Administrador
- Cada endpoint deve validar permissões estritamente.

### RNF-006 — Criptografia
- Todo tráfego deve ocorrer sobre HTTPS (TLS 1.2+).
- As senhas devem ser armazenadas utilizando **bcrypt ou Argon2**.

### RNF-007 — Proteção contra Ataques
O sistema deve implementar:
- Proteção contra SQL Injection,
- Proteção contra XSS,
- Proteção contra CSRF,
- Rate limiting em endpoints sensíveis,
- Bloqueio temporário de login após tentativas falhas consecutivas.

### RNF-008 — LGPD
- Dados pessoais devem ser tratados conforme LGPD.
- Logs devem mascarar dados pessoais.
- Deve existir mecanismo para exclusão de conta e dados sensíveis.

---

## 10.3. Observabilidade, Logs e Monitoramento

### RNF-009 — Padronização de Logs
- Todos os microsserviços (mesmo modularizados num único backend) devem seguir o mesmo formato de log:
  - timestamp,
  - requestId,
  - usuário,
  - tipo do evento,
  - severidade,
  - origem.

### RNF-010 — Rastreabilidade
- Toda requisição deve receber um **requestId** propagado entre frontend → backend.

### RNF-011 — Métricas
- O backend deve exportar métricas compatíveis com Prometheus.
- No mínimo:
  - tempo médio de resposta,
  - throughput,
  - erros por segundo,
  - consumo de recursos.

### RNF-012 — Auditoria
- Operações sensíveis (criar produto, excluir produto, alterar preço, banir usuário) devem ser auditadas.

---

## 10.4. Disponibilidade e Confiabilidade

### RNF-013 — Disponibilidade do Sistema
- O sistema deve ter Uptime mínimo de **99%**.

### RNF-014 — Tolerância a Falhas
- A plataforma deve continuar operando mesmo com a queda de uma instância do backend.
- Serviços críticos devem possuir retry automático e circuit breaker.

### RNF-015 — Consistência de Dados
- Durante operações de compra, a integridade deve ser total (sem double booking de estoque).
- O sistema deve usar locks otimizados ou transações distribuídas apenas quando necessário.

---

## 10.5. Usabilidade

### RNF-016 — Design Responsivo
- O frontend deve funcionar adequadamente em:
  - desktop,
  - tablets,
  - smartphones.

### RNF-017 — Acessibilidade
- O sistema deve seguir WCAG 2.1 AA:
  - contraste mínimo,
  - navegação por teclado,
  - textos alternativos,
  - labels acessíveis.

### RNF-018 — UX consistente
- Padrões de UI devem seguir um design system único.
- Feedback visual para:
  - processamento,
  - erro,
  - sucesso,
  - estados vazios.

---

## 10.6. Compatibilidade e Interoperabilidade

### RNF-019 — Browsers Suportados
- Chrome, Firefox, Safari e Edge — últimas duas versões estáveis.

### RNF-020 — APIs Padronizadas
- JSON padronizado com camelCase.
- Paginação consistente:
  - `page`, `size`, `totalPages`, `totalElements`.

---

## 10.7. Manutenibilidade e Evolução

### RNF-021 — Modularização Interna
- O backend deve seguir arquitetura modular desde o início, mesmo sendo um único serviço:
  - módulo de catálogo,
  - módulo de contas,
  - módulo de pedidos,
  - módulo administrativo,
  - módulo de notificações/eventos.

### RNF-022 — Código Limpo
- Deve seguir padrões:
  - SOLID,
  - separação clara de camadas,
  - testes unitários obrigatórios,
  - 70%+ cobertura mínima no MVP.

### RNF-023 — Documentação
- O backend deve expor documentação OpenAPI/Swagger sempre atualizada.

---

## 10.8. Implantação e Entrega Contínua

### RNF-024 — Deploy Automatizado
- A pipeline de CI/CD deve:
  - rodar testes,
  - validar linting,
  - criar artefatos,
  - realizar deploy automatizado no ambiente de teste.

### RNF-025 — Versionamento
- Versionamento semântico: MAJOR.MINOR.PATCH.

---

## 10.9. Armazenamento e Infraestrutura

### RNF-026 — Banco de Dados
- O banco deve garantir:
  - replicação primária-secundária,
  - isolamento de transações mínimo `READ COMMITTED`.

### RNF-027 — Mensageria e Eventos
- A solução deve usar:
  - SSE para notificações unidirecionais (ex.: atualização de estoque),
  - WebSocket para interações em tempo real quando necessário,
  - Fila/stream para eventos internos caso o volume cresça (Kafka ou equivalente no futuro).

---

## 10.10. Requisitos Operacionais

### RNF-028 — Backups
- Backups diários completos + incrementais de hora em hora.
- Teste de restauração mensal obrigatório.

### RNF-029 — Alertas
- Alertas automáticos para:
  - falhas,
  - degradação,
  - indisponibilidade,
  - estouro de limites de recursos.

---

## 10.11. Critérios de Qualidade do Produto

### RNF-030 — Confiabilidade Validada
- O sistema só pode ir para produção após:
  - testes funcionais completos,
  - testes de carga,
  - testes de segurança,
  - testes de usabilidade.

### RNF-031 — Não Regressão
- Qualquer nova feature deve passar por testes automatizados de regressão.

