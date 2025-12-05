# Sistema de Leilão — Plataforma de Leilões Online

Este repositório contém um sistema de leilão eletrônico (MVP) com foco em leilões em tempo real, baixa latência, segurança e escalabilidade. A plataforma atende compradores, vendedores e administradores, permitindo cadastro de produtos, agrupamento em lotes, lances por produto, encerramento e arremate, cálculo de frete via API externa e cobrança de taxa de leiloeiro definida por contrato.

## 🎯 Visão Geral
- **Objetivo**: Realizar leilões online com atualização em tempo real dos lances, garantindo ordem, consistência e auditabilidade
- **Público-alvo**: Compradores cadastrados, vendedores profissionais e equipe de administração
- **Diretrizes**: Entrega por valor (MVP), custo de infra baixo, segurança (2FA, JWT, RBAC), observabilidade desde o início

## ⚠️ **IMPORTANTE - Mudanças Recentes (Sprint S2.3)**

**🔄 Sistema migrou para navegação baseada em LOTES**

- **Antes**: Catálogo direto de produtos
- **Agora**: Catálogo de lotes → Produtos do lote
- **Impacto**: Endpoints de catálogo público de produtos foram **DEPRECIADOS**

📋 **[Guia de Migração Completo](./doc/MIGRATION_CATALOG_DEPRECATION.md)**

### Endpoints Depreciados
- ❌ `GET /api/catalogo/produtos` → Use `GET /api/lotes/catalogo-publico`
- ❌ `GET /api/catalogo/produtos/{id}` → Use `GET /api/lotes/{loteId}`
- ✅ `GET /api/catalogo/categorias` → Mantido (sem mudança)

---

## 🚀 Funcionalidades Principais (MVP)

### 👥 **Para Compradores**
- Visualizar lotes e produtos organizados
- Ver lances em tempo real
- Enviar lances em produtos específicos
- Acompanhar histórico de participação
- Receber confirmação de arremate

### 🏪 **Para Vendedores**
- Cadastrar produtos individuais
- Criar e gerenciar lotes de produtos
- Definir regras e horários de encerramento
- Acompanhar resultados e comissões
- Gestão de contratos com diferentes taxas

### 👨‍💼 **Para Administradores**
- Gestão de usuários e permissões
- Aprovação e curadoria de conteúdo
- Ativação de vendedores via contratos
- Auditoria e relatórios básicos
- Monitoramento de contratos vencendo

### 🔗 **Integrações**
- API externa de frete
- Cobrança de taxa de leiloeiro conforme contrato
- Sistema de notificações em tempo real

---

## 🏗️ Arquitetura em Alto Nível

### Frontend
- **Framework**: Angular 18+ com componentes standalone
- **Arquitetura**: Aplicação SPA com lazy loading
- **Navegação**: Sistema baseado em lotes (novo padrão)
- **Tempo Real**: SSE para atualizações de lances

### Backend
- **Framework**: Java 21 + Spring Boot 3
- **Arquitetura**: Modular monolítico (preparado para microserviços)
- **Módulos**: Auth, Produtos, Lotes, Contratos, Vendedores, Admin
- **API**: RESTful com documentação OpenAPI

### Dados
- **Principal**: PostgreSQL (H2 para desenvolvimento)
- **Cache**: Redis (opcional para produção)
- **Arquivos**: Sistema de arquivos local (S3 para produção)
- **Migrations**: Flyway para versionamento

### Infraestrutura
- **Desenvolvimento**: Docker Compose
- **Produção**: AWS (ECS Fargate, RDS, S3, CloudFront)
- **CI/CD**: GitHub Actions
- **Observabilidade**: Logs estruturados, métricas, tracing

---

## 🔄 Fluxo de Navegação Atual

```
🏠 Home
├── 📦 Catálogo de Lotes (público)
│   ├── 🔍 Filtros por categoria
│   ├── ⏰ Ordenação por encerramento
│   └── 📄 Paginação configurável
│
├── 📋 Detalhes do Lote
│   ├── ℹ️ Informações do lote
│   ├── ⏱️ Tempo restante
│   └── 📦 Produtos do lote
│
└── 🔐 Área Privada (autenticada)
    ├── 👤 Perfil do usuário
    ├── 🏪 Meus produtos (vendedores)
    ├── 📦 Meus lotes (vendedores)
    ├── 📋 Meus contratos (vendedores)
    └── 👨‍💼 Administração (admins)
```

---

## 🚨 Comunicação em Tempo Real

### Estratégia Atual (MVP)
- **SSE (Server-Sent Events)**: Para atualizações de lances e status
- **Vantagens**: Simples, leve, adequado para MVP
- **Limitações**: Unidirecional (servidor → cliente)

### Evolução Futura
- **WebSockets**: Para interações bidirecionais intensas
- **Escalabilidade**: API Gateway WebSockets + Redis
- **Casos de uso**: Chat, lances simultâneos em alta escala

---

## 🔒 Segurança

### Autenticação e Autorização
- **JWT**: Tokens com refresh automático
- **RBAC**: Controle baseado em roles (VISITOR, BUYER, SELLER, ADMIN)
- **2FA**: Planejado para versões futuras

### Proteções
- **Rate Limiting**: Proteção contra abuso de APIs
- **CORS**: Configuração adequada para frontend
- **Validação**: Sanitização de inputs
- **Auditoria**: Logs de ações críticas

---

## 📊 Fluxos Críticos

### 1. Envio de Lance
```
Cliente → POST /products/{id}/bid → Validações → Persiste → Eventos → Notificações
```

### 2. Encerramento de Lote
```
Timer → Verifica horário → Fecha lote → Calcula vencedores → Billing → Notificações
```

### 3. Ativação de Vendedor
```
Admin → Cria contrato → Ativa vendedor → Notifica → Vendedor pode criar lotes
```

---

## 🗂️ Estrutura do Projeto

```
📁 auction-system/
├── 📁 backend/                 # API Java Spring Boot
│   ├── 📁 src/main/java/com/leilao/
│   │   ├── 📁 modules/         # Módulos funcionais
│   │   │   ├── 📁 auth/        # Autenticação e usuários
│   │   │   ├── 📁 produto/     # Gestão de produtos
│   │   │   ├── 📁 lote/        # Gestão de lotes
│   │   │   ├── 📁 contrato/    # Contratos e comissões
│   │   │   ├── 📁 vendedor/    # Gestão de vendedores
│   │   │   └── 📁 admin/       # Funcionalidades admin
│   │   ├── 📁 core/           # Configurações centrais
│   │   └── 📁 shared/         # Utilitários compartilhados
│   └── 📁 postman/            # Collections para testes
│
├── 📁 frontend/               # SPA Angular
│   ├── 📁 src/app/
│   │   ├── 📁 pages/          # Páginas da aplicação
│   │   ├── 📁 core/           # Serviços e guards
│   │   ├── 📁 shared/         # Componentes compartilhados
│   │   └── 📁 layouts/        # Layouts da aplicação
│   └── 📁 proxy.conf.json     # Configuração de proxy
│
└── 📁 doc/                    # Documentação
    ├── 📁 v_clean_mvp/        # Documentação do MVP
    ├── 📁 implementacao/      # Guias de implementação
    └── 📄 MIGRATION_CATALOG_DEPRECATION.md
```

---

## 🚀 Como Executar

### Pré-requisitos
- Java 21+
- Node.js 18+
- Docker e Docker Compose (opcional)

### Backend
```bash
cd backend
./mvnw spring-boot:run
# API disponível em: http://localhost:8080
```

### Frontend
```bash
cd frontend
npm install
npm start
# App disponível em: http://localhost:4200
```

### Com Docker
```bash
docker-compose up -d
# Backend: http://localhost:8080
# Frontend: http://localhost:4200
```

---

## 🧪 Testes

### Backend
```bash
cd backend
./mvnw test
```

### Frontend
```bash
cd frontend
npm test
```

### Postman Collections
- Importe as collections em `backend/postman/`
- Configure o environment com as variáveis necessárias
- Execute os testes de API

---

## 📈 Roadmap

### ✅ **Fase 1 - MVP (Atual)**
- [x] Sistema de autenticação e usuários
- [x] Gestão de produtos e lotes
- [x] Catálogo público baseado em lotes
- [x] Sistema de contratos para vendedores
- [x] Interface administrativa básica
- [x] Depreciação do catálogo direto de produtos

### 🔄 **Fase 2 - Leilões Ativos**
- [ ] Sistema de lances em tempo real
- [ ] Encerramento automático de lotes
- [ ] Cálculo de vencedores e arremates
- [ ] Sistema de notificações avançado
- [ ] Anti-sniping e regras de leilão

### 🚀 **Fase 3 - Escala e Features**
- [ ] WebSockets para alta concorrência
- [ ] Sistema de pagamentos integrado
- [ ] Cálculo de frete via APIs externas
- [ ] Relatórios e analytics avançados
- [ ] Aplicativo mobile

---

## 📚 Documentação

### Guias Principais
- [📋 Guia de Migração - Depreciação do Catálogo](./doc/MIGRATION_CATALOG_DEPRECATION.md)
- [🏗️ Arquitetura Técnica Completa](./doc/visoes_exercitadas/arquitetura/arquitetura_tecnica_completa.md)
- [📊 Backlog e Sprints](./doc/v_clean_mvp/)
- [🔧 Análise SSE vs WebSocket](./doc/visoes_exercitadas/analise_tecnica/)

### API Reference
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI Spec: `http://localhost:8080/v3/api-docs`

### Collections Postman
- [04-Produtos-Updated.postman_collection.json](./backend/postman/04-Produtos-Updated.postman_collection.json)
- [Historia02-Catalogo-Lotes.postman_collection.json](./backend/postman/Historia02-Catalogo-Lotes.postman_collection.json)

---

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

---

## 📄 Licença

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

---

## 📞 Suporte

- **Issues**: [GitHub Issues](https://github.com/seu-usuario/auction-system/issues)
- **Documentação**: [Wiki do Projeto](https://github.com/seu-usuario/auction-system/wiki)
- **Email**: dev-team@leilao.com

---

**Última atualização**: 19 de Dezembro de 2024  
**Versão**: Sprint S2.3 - Sistema baseado em Lotes