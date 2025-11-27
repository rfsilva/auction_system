# Setup do Banco de Dados - Sistema de Leilão Eletrônico

## 📋 Visão Geral

Este documento descreve o setup completo do banco de dados PostgreSQL para o Sistema de Leilão Eletrônico, incluindo schema, migrations, configurações e scripts de automação.

## 🏗️ Arquitetura do Banco

### Tecnologias
- **PostgreSQL 15+** - Banco principal
- **Flyway** - Gerenciamento de migrations
- **Redis 7+** - Cache e pub/sub
- **UUID** - Chaves primárias
- **JSONB** - Dados semi-estruturados

### Schema Principal

O banco é composto por **15 tabelas principais**:

#### Core Tables
- `usuarios` - Usuários do sistema
- `vendedores` - Perfil de vendedor
- `compradores` - Perfil de comprador
- `contratos` - Contratos de comissão

#### Auction Tables
- `lotes` - Agrupamento de produtos
- `produtos` - Produtos para leilão
- `lances` - Lances dos usuários
- `arremates` - Produtos arrematados

#### Support Tables
- `documentos` - Documentos gerados
- `audit_log` - Log de auditoria
- `disputas` - Disputas de arremates
- `favoritos` - Produtos favoritados
- `notificacoes` - Notificações
- `pre_autorizacoes` - Pré-autorizações de pagamento
- `historico_produtos` - Histórico de alterações

## 🚀 Setup Rápido

### Opção 1: Docker Compose (Recomendado)

```bash
# Iniciar PostgreSQL e Redis
cd backend
docker-compose -f docker-compose.dev.yml up -d

# Aguardar containers iniciarem
docker-compose -f docker-compose.dev.yml logs -f postgres

# Executar migrations
mvn flyway:migrate
```

### Opção 2: Instalação Local

#### Windows
```cmd
# Executar script automatizado
cd backend\scripts
setup-db.bat
```

#### Linux/macOS
```bash
# Executar script automatizado
cd backend/scripts
chmod +x setup-db.sh
./setup-db.sh
```

### Opção 3: Manual

1. **Instalar PostgreSQL 15+**
2. **Criar banco e usuário:**
   ```sql
   CREATE DATABASE leilao_db;
   CREATE USER leilao_user WITH PASSWORD 'leilao_pass';
   GRANT ALL PRIVILEGES ON DATABASE leilao_db TO leilao_user;
   ```
3. **Executar migrations:**
   ```bash
   mvn flyway:migrate
   ```

## 📊 Estrutura Detalhada

### Tabelas Principais

#### USUARIOS
```sql
CREATE TABLE usuarios (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    telefone VARCHAR(20),
    status user_status NOT NULL DEFAULT 'PENDING_VERIFICATION',
    roles user_role[] NOT NULL DEFAULT ARRAY['VISITOR'],
    email_verificado BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

#### PRODUTOS
```sql
CREATE TABLE produtos (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    seller_id UUID NOT NULL REFERENCES vendedores(id),
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    images JSONB,
    initial_price DECIMAL(15,2) NOT NULL CHECK (initial_price > 0),
    current_price DECIMAL(15,2) NOT NULL CHECK (current_price >= initial_price),
    end_datetime TIMESTAMP WITH TIME ZONE NOT NULL,
    status product_status NOT NULL DEFAULT 'DRAFT',
    anti_snipe_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

#### LANCES
```sql
CREATE TABLE lances (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    produto_id UUID NOT NULL REFERENCES produtos(id),
    user_id UUID NOT NULL REFERENCES usuarios(id),
    value DECIMAL(15,2) NOT NULL CHECK (value > 0),
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sequence_number BIGSERIAL,
    is_winning BOOLEAN NOT NULL DEFAULT FALSE
);
```

### Enums Utilizados

```sql
-- Status do usuário
CREATE TYPE user_status AS ENUM ('ACTIVE', 'INACTIVE', 'SUSPENDED', 'PENDING_VERIFICATION');

-- Roles do usuário
CREATE TYPE user_role AS ENUM ('VISITOR', 'PARTICIPANT', 'BUYER', 'SELLER', 'ADMIN');

-- Status do produto
CREATE TYPE product_status AS ENUM ('DRAFT', 'PENDING_APPROVAL', 'ACTIVE', 'SOLD', 'CANCELLED', 'EXPIRED');

-- Status do pagamento
CREATE TYPE payment_status AS ENUM ('PENDING', 'AUTHORIZED', 'CAPTURED', 'FAILED', 'REFUNDED', 'CANCELLED');
```

## 🔧 Configurações por Ambiente

### Development
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/leilao_db
    username: leilao_user
    password: leilao_pass
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
  flyway:
    enabled: true
    clean-disabled: false
```

### Test
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
  flyway:
    enabled: false
```

### Production
```yaml
spring:
  datasource:
    url: ${DATABASE_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
  flyway:
    enabled: true
    clean-disabled: true
```

## 📈 Migrations

### V1__Create_initial_schema.sql
- Criação de todas as tabelas
- Definição de enums
- Índices de performance
- Triggers para updated_at
- Usuário admin padrão

### V2__Insert_sample_data.sql
- Dados de exemplo para desenvolvimento
- Usuários de teste
- Produtos e lances de exemplo
- Configurações iniciais

### Executar Migrations

```bash
# Via Maven
mvn flyway:migrate

# Via linha de comando
mvn flyway:migrate \
  -Dflyway.url=jdbc:postgresql://localhost:5432/leilao_db \
  -Dflyway.user=leilao_user \
  -Dflyway.password=leilao_pass

# Informações sobre migrations
mvn flyway:info

# Validar migrations
mvn flyway:validate
```

## 🔍 Monitoramento e Manutenção

### Views de Monitoramento

```sql
-- Leilões ativos
SELECT * FROM v_leiloes_ativos;

-- Estatísticas do sistema
SELECT * FROM v_estatisticas_sistema;
```

### Funções de Manutenção

```sql
-- Verificar saúde do banco
SELECT * FROM check_database_health();

-- Limpeza de dados antigos
SELECT cleanup_old_data();
```

### Índices de Performance

```sql
-- Produtos ativos por data de término
CREATE INDEX idx_produtos_status_end_datetime ON produtos(status, end_datetime) WHERE status = 'ACTIVE';

-- Lances por produto e timestamp
CREATE INDEX idx_lances_produto_timestamp_desc ON lances(produto_id, timestamp DESC);

-- Usuários por email e status
CREATE INDEX idx_usuarios_email_status ON usuarios(email, status);
```

## 🔒 Segurança

### Configurações de Segurança
- Senhas hasheadas com bcrypt
- Conexões SSL em produção
- Usuários com privilégios mínimos
- Auditoria completa de ações

### Backup e Recovery
```bash
# Backup completo
pg_dump -h localhost -U leilao_user -d leilao_db | gzip > backup_$(date +%Y%m%d_%H%M%S).sql.gz

# Restore
gunzip -c backup_file.sql.gz | psql -h localhost -U leilao_user -d leilao_db
```

## 📊 Dados de Exemplo

O sistema inclui dados de exemplo para desenvolvimento:

### Usuários Padrão
- **Admin:** admin@leilao.com / password
- **Vendedor:** vendedor@leilao.com / password  
- **Comprador:** comprador@leilao.com / password
- **Participante:** participante@leilao.com / password

### Produtos de Exemplo
- iPhone 15 Pro Max (R$ 4.500 - ativo)
- MacBook Air M2 (R$ 7.200 - ativo)
- Rolex Submariner (R$ 35.000 - pendente aprovação)

## 🐛 Troubleshooting

### Problemas Comuns

1. **Erro de conexão:**
   ```
   Verificar se PostgreSQL está rodando:
   pg_isready -h localhost -p 5432 -U leilao_user
   ```

2. **Erro de permissão:**
   ```sql
   GRANT ALL PRIVILEGES ON DATABASE leilao_db TO leilao_user;
   GRANT ALL ON SCHEMA public TO leilao_user;
   ```

3. **Erro de migration:**
   ```bash
   mvn flyway:repair
   mvn flyway:migrate
   ```

4. **Erro de encoding:**
   ```sql
   -- Verificar encoding do banco
   SELECT datname, encoding, datcollate, datctype FROM pg_database WHERE datname = 'leilao_db';
   ```

### Logs Úteis

```bash
# Logs do PostgreSQL
tail -f /var/log/postgresql/postgresql-15-main.log

# Logs da aplicação
tail -f logs/leilao-dev.log

# Logs do Docker
docker-compose -f docker-compose.dev.yml logs -f postgres
```

## 📚 Recursos Adicionais

### Ferramentas de Administração
- **pgAdmin:** http://localhost:8081 (admin@leilao.com / admin123)
- **Redis Commander:** http://localhost:8082

### Documentação
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Flyway Documentation](https://flywaydb.org/documentation/)
- [Spring Boot Data JPA](https://spring.io/projects/spring-data-jpa)

### Performance
- Configurar `shared_buffers` para 25% da RAM
- Usar connection pooling (HikariCP)
- Monitorar slow queries
- Configurar índices adequados

---

## ✅ Checklist de Validação

- [ ] PostgreSQL 15+ instalado e rodando
- [ ] Banco `leilao_db` criado
- [ ] Usuário `leilao_user` com permissões
- [ ] Migrations executadas (15+ tabelas)
- [ ] Dados de exemplo carregados
- [ ] Conexão da aplicação funcionando
- [ ] Redis configurado e rodando
- [ ] Backup configurado (produção)

**Status:** ✅ História 3 - Setup do banco de dados **CONCLUÍDA**