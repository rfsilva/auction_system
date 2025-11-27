# Sistema de Leilão Eletrônico - Backend

Backend modular em Spring Boot 3 para o Sistema de Leilão Eletrônico.

## 🏗️ Arquitetura

O projeto segue uma arquitetura **monolito modular** organizada por domínios, permitindo evolução futura para microsserviços.

### Estrutura de Módulos

```
src/main/java/com/leilao/
├── core/                    # Configurações centrais
│   └── config/             # Configurações Spring
├── modules/                # Módulos de domínio
│   ├── auth/              # Autenticação e autorização
│   ├── catalog/           # Catálogo de produtos
│   ├── auction/           # Engine de leilões
│   ├── bid/               # Serviço de lances
│   ├── payment/           # Pagamentos
│   ├── notification/      # Notificações
│   ├── document/          # Geração de documentos
│   └── realtime/          # Comunicação tempo real
└── shared/                # Componentes compartilhados
    ├── dto/               # DTOs comuns
    ├── exception/         # Tratamento de exceções
    └── util/              # Utilitários
```

## 🚀 Tecnologias

- **Java 21**
- **Spring Boot 3.2.0**
- **Spring Security** (JWT)
- **Spring Data JPA** (Hibernate)
- **PostgreSQL** (produção)
- **H2** (testes)
- **Redis** (cache e pub/sub)
- **WebSocket + SSE** (tempo real)
- **Flyway** (migrations)
- **Maven** (build)

## 🛠️ Setup de Desenvolvimento

### Pré-requisitos

- Java 21+
- Maven 3.8+
- PostgreSQL 15+
- Redis 7+

### Configuração do Banco de Dados

1. Criar banco PostgreSQL:
```sql
CREATE DATABASE leilao_db;
CREATE USER leilao_user WITH PASSWORD 'leilao_pass';
GRANT ALL PRIVILEGES ON DATABASE leilao_db TO leilao_user;
```

2. Configurar Redis (local):
```bash
# Instalar Redis
# Ubuntu/Debian
sudo apt install redis-server

# macOS
brew install redis

# Iniciar Redis
redis-server
```

### Executar a Aplicação

```bash
# Clonar o repositório
git clone <repository-url>
cd backend

# Executar com Maven
mvn spring-boot:run

# Ou executar com profile específico
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

A aplicação estará disponível em: `http://localhost:8080/api`

## 📋 Profiles de Configuração

### Development (dev)
- Banco PostgreSQL local
- Logs detalhados
- H2 Console habilitado para testes
- CORS permissivo

### Test (test)
- Banco H2 em memória
- Configurações otimizadas para testes
- Mocks habilitados
- Flyway desabilitado

### Production (prod)
- Configurações de produção
- Logs otimizados
- Segurança reforçada
- Variáveis de ambiente obrigatórias

## 🔌 Endpoints Principais

### Autenticação
- `POST /api/auth/login` - Login
- `POST /api/auth/register` - Registro
- `POST /api/auth/refresh` - Refresh token
- `POST /api/auth/logout` - Logout

### Tempo Real
- `GET /api/realtime/sse/events` - SSE para espectadores
- `WS /ws` - WebSocket para bidders
- `POST /api/realtime/broadcast` - Teste de broadcast

### Monitoramento
- `GET /api/actuator/health` - Health check
- `GET /api/actuator/info` - Informações da aplicação

## 🧪 Testes

### ✅ Status dos Testes
```
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

### Executar Testes

```bash
# Executar todos os testes
mvn test

# Executar testes específicos
mvn test -Dtest=AuthControllerTest
mvn test -Dtest=RealtimeControllerTest
mvn test -Dtest=LeilaoApplicationTests

# Executar com coverage
mvn test jacoco:report

# Build sem testes (para desenvolvimento rápido)
mvn clean package -DskipTests
```

### Testes Implementados

#### AuthControllerTest (4 testes)
- ✅ `testLoginEndpoint()` - Testa endpoint de login
- ✅ `testRegisterEndpoint()` - Testa endpoint de registro
- ✅ `testRefreshEndpoint()` - Testa endpoint de refresh token
- ✅ `testLogoutEndpoint()` - Testa endpoint de logout

#### RealtimeControllerTest (2 testes)
- ✅ `testBroadcastEndpoint()` - Testa broadcast de eventos
- ✅ `testStartSimulationEndpoint()` - Testa simulação de eventos

#### LeilaoApplicationTests (1 teste)
- ✅ `contextLoads()` - Testa inicialização da aplicação

### Configuração de Testes

Os testes utilizam:
- **H2 Database** em memória
- **TestSecurityConfig** para configuração permissiva
- **Profile test** com configurações otimizadas
- **MockMvc** para testes de controllers

## 📦 Build e Deploy

```bash
# Build da aplicação
mvn clean package

# Build sem testes
mvn clean package -DskipTests

# Executar JAR gerado
java -jar target/leilao-backend-1.0.0-SNAPSHOT.jar
```

## 🔧 Configurações Importantes

### Variáveis de Ambiente (Produção)

```bash
# Database
DATABASE_URL=jdbc:postgresql://host:port/database
DB_USERNAME=username
DB_PASSWORD=password

# Redis
REDIS_HOST=redis-host
REDIS_PORT=6379
REDIS_PASSWORD=redis-password

# JWT
JWT_SECRET=your-secret-key

# CORS
CORS_ALLOWED_ORIGINS=https://yourdomain.com

# Storage (S3)
AWS_ACCESS_KEY_ID=your-access-key
AWS_SECRET_ACCESS_KEY=your-secret-key
S3_BUCKET_NAME=your-bucket

# Payment
STRIPE_PUBLIC_KEY=pk_...
STRIPE_SECRET_KEY=sk_...
```

## 🔄 Comunicação Tempo Real

### SSE (Server-Sent Events)
Para espectadores que apenas visualizam leilões:
```javascript
const eventSource = new EventSource('/api/realtime/sse/events');
eventSource.onmessage = function(event) {
    console.log('Evento recebido:', event.data);
};
```

### WebSocket
Para participantes que enviam lances:
```javascript
const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);
stompClient.connect({}, function(frame) {
    stompClient.subscribe('/topic/bids', function(message) {
        console.log('Lance recebido:', message.body);
    });
});
```

## 📚 Documentação da API

Após iniciar a aplicação, acesse:
- Swagger UI: `http://localhost:8080/api/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api/v3/api-docs`

## 🐛 Troubleshooting

### Problemas Comuns

1. **Erro de conexão com PostgreSQL**
   - Verificar se o PostgreSQL está rodando
   - Confirmar credenciais no `application-dev.yml`

2. **Erro de conexão com Redis**
   - Verificar se o Redis está rodando: `redis-cli ping`
   - Confirmar configurações de host/porta

3. **Erro de permissão JWT**
   - Verificar se o JWT_SECRET está configurado
   - Confirmar formato do token no header Authorization

4. **Testes falhando com 403**
   - Verificar se `TestSecurityConfig` está sendo importada
   - Confirmar profile `test` está ativo

## 🚧 Status do Desenvolvimento

### ✅ Implementado (Sprint 1 - História 1)
- [x] Estrutura modular do projeto
- [x] Configurações base (Security, Database, Redis, WebSocket)
- [x] Endpoints de autenticação (estrutura)
- [x] Prova de conceito SSE/WebSocket
- [x] Configuração de profiles (dev/test/prod)
- [x] Tratamento global de exceções
- [x] **Testes unitários funcionando (7 testes passando)**

### 🔄 Próximas Histórias (Sprint 1)
- [ ] História 2: Setup do projeto frontend Angular
- [ ] História 3: Setup do banco de dados
- [ ] História 4: Implementar módulo de usuários completo
- [ ] História 5: Completar SSE/WebSocket
- [ ] História 6: Configuração CI/CD inicial

### 📋 Próximas Sprints
- [ ] Módulo de catálogo
- [ ] Engine de leilões
- [ ] Sistema de lances
- [ ] Integração com pagamentos
- [ ] Geração de documentos

## 🤝 Contribuição

1. Seguir padrões de código estabelecidos
2. Escrever testes para novas funcionalidades
3. Documentar APIs com OpenAPI/Swagger
4. Seguir convenções de commit semântico
5. **Executar testes antes de commit: `mvn test`**

## 📄 Licença

Este projeto está sob licença MIT. Veja o arquivo [LICENSE](../LICENSE) para detalhes.