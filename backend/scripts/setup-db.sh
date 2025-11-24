#!/bin/bash

# =====================================================
# Script de Setup do Banco de Dados
# Sistema de Leilão Eletrônico
# =====================================================

set -e

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configurações padrão
DB_HOST=${DB_HOST:-localhost}
DB_PORT=${DB_PORT:-5432}
DB_NAME=${DB_NAME:-leilao_db}
DB_USER=${DB_USER:-leilao_user}
DB_PASSWORD=${DB_PASSWORD:-leilao_pass}
POSTGRES_USER=${POSTGRES_USER:-postgres}

echo -e "${BLUE}=== Sistema de Leilão Eletrônico - Setup do Banco ===${NC}"
echo ""

# Função para verificar se PostgreSQL está rodando
check_postgres() {
    echo -e "${YELLOW}Verificando se PostgreSQL está rodando...${NC}"
    if ! pg_isready -h $DB_HOST -p $DB_PORT -U $POSTGRES_USER > /dev/null 2>&1; then
        echo -e "${RED}❌ PostgreSQL não está rodando ou não está acessível${NC}"
        echo -e "${YELLOW}Por favor, inicie o PostgreSQL e tente novamente${NC}"
        exit 1
    fi
    echo -e "${GREEN}✅ PostgreSQL está rodando${NC}"
}

# Função para criar usuário e banco
create_database() {
    echo -e "${YELLOW}Criando usuário e banco de dados...${NC}"
    
    # Criar usuário se não existir
    psql -h $DB_HOST -p $DB_PORT -U $POSTGRES_USER -c "
        DO \$\$
        BEGIN
            IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = '$DB_USER') THEN
                CREATE ROLE $DB_USER LOGIN PASSWORD '$DB_PASSWORD';
                RAISE NOTICE 'Usuário $DB_USER criado';
            ELSE
                RAISE NOTICE 'Usuário $DB_USER já existe';
            END IF;
        END
        \$\$;
    " 2>/dev/null || {
        echo -e "${RED}❌ Erro ao criar usuário${NC}"
        exit 1
    }
    
    # Criar banco se não existir
    psql -h $DB_HOST -p $DB_PORT -U $POSTGRES_USER -c "
        SELECT 'CREATE DATABASE $DB_NAME OWNER $DB_USER'
        WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = '$DB_NAME')\gexec
    " 2>/dev/null || {
        echo -e "${RED}❌ Erro ao criar banco de dados${NC}"
        exit 1
    }
    
    # Conceder privilégios
    psql -h $DB_HOST -p $DB_PORT -U $POSTGRES_USER -c "
        GRANT ALL PRIVILEGES ON DATABASE $DB_NAME TO $DB_USER;
    " 2>/dev/null
    
    echo -e "${GREEN}✅ Usuário e banco criados com sucesso${NC}"
}

# Função para executar migrations com Flyway
run_migrations() {
    echo -e "${YELLOW}Executando migrations com Flyway...${NC}"
    
    cd "$(dirname "$0")/.."
    
    if command -v mvn > /dev/null 2>&1; then
        mvn flyway:migrate \
            -Dflyway.url=jdbc:postgresql://$DB_HOST:$DB_PORT/$DB_NAME \
            -Dflyway.user=$DB_USER \
            -Dflyway.password=$DB_PASSWORD \
            -Dflyway.locations=classpath:db/migration \
            -Dflyway.schemas=public
        
        if [ $? -eq 0 ]; then
            echo -e "${GREEN}✅ Migrations executadas com sucesso${NC}"
        else
            echo -e "${RED}❌ Erro ao executar migrations${NC}"
            exit 1
        fi
    else
        echo -e "${YELLOW}⚠️  Maven não encontrado. Execute as migrations manualmente:${NC}"
        echo "mvn flyway:migrate"
    fi
}

# Função para verificar a instalação
verify_installation() {
    echo -e "${YELLOW}Verificando instalação...${NC}"
    
    # Verificar se as tabelas foram criadas
    TABLE_COUNT=$(psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -t -c "
        SELECT COUNT(*) FROM information_schema.tables 
        WHERE table_schema = 'public' AND table_type = 'BASE TABLE';
    " 2>/dev/null | tr -d ' ')
    
    if [ "$TABLE_COUNT" -gt 10 ]; then
        echo -e "${GREEN}✅ Banco configurado corretamente ($TABLE_COUNT tabelas criadas)${NC}"
        
        # Mostrar algumas estatísticas
        echo -e "${BLUE}📊 Estatísticas do banco:${NC}"
        psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c "
            SELECT 
                schemaname,
                tablename,
                n_tup_ins as inserções,
                n_tup_upd as atualizações,
                n_tup_del as exclusões
            FROM pg_stat_user_tables 
            ORDER BY tablename;
        " 2>/dev/null || echo "Não foi possível obter estatísticas"
        
    else
        echo -e "${RED}❌ Problema na instalação (apenas $TABLE_COUNT tabelas encontradas)${NC}"
        exit 1
    fi
}

# Função para mostrar informações de conexão
show_connection_info() {
    echo -e "${BLUE}=== Informações de Conexão ===${NC}"
    echo -e "Host: ${GREEN}$DB_HOST${NC}"
    echo -e "Porta: ${GREEN}$DB_PORT${NC}"
    echo -e "Banco: ${GREEN}$DB_NAME${NC}"
    echo -e "Usuário: ${GREEN}$DB_USER${NC}"
    echo -e "Senha: ${GREEN}$DB_PASSWORD${NC}"
    echo ""
    echo -e "${YELLOW}String de conexão para aplicação:${NC}"
    echo "jdbc:postgresql://$DB_HOST:$DB_PORT/$DB_NAME"
    echo ""
}

# Função principal
main() {
    echo -e "${BLUE}Iniciando setup do banco de dados...${NC}"
    echo ""
    
    check_postgres
    create_database
    run_migrations
    verify_installation
    show_connection_info
    
    echo -e "${GREEN}🎉 Setup do banco concluído com sucesso!${NC}"
    echo -e "${YELLOW}Agora você pode iniciar a aplicação Spring Boot${NC}"
}

# Função de ajuda
show_help() {
    echo "Uso: $0 [opções]"
    echo ""
    echo "Opções:"
    echo "  -h, --help          Mostra esta ajuda"
    echo "  --host HOST         Host do PostgreSQL (padrão: localhost)"
    echo "  --port PORT         Porta do PostgreSQL (padrão: 5432)"
    echo "  --dbname NAME       Nome do banco (padrão: leilao_db)"
    echo "  --user USER         Usuário do banco (padrão: leilao_user)"
    echo "  --password PASS     Senha do usuário (padrão: leilao_pass)"
    echo ""
    echo "Variáveis de ambiente:"
    echo "  DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD"
    echo ""
    echo "Exemplo:"
    echo "  $0 --host localhost --port 5432"
    echo "  DB_HOST=192.168.1.100 $0"
}

# Processar argumentos da linha de comando
while [[ $# -gt 0 ]]; do
    case $1 in
        -h|--help)
            show_help
            exit 0
            ;;
        --host)
            DB_HOST="$2"
            shift 2
            ;;
        --port)
            DB_PORT="$2"
            shift 2
            ;;
        --dbname)
            DB_NAME="$2"
            shift 2
            ;;
        --user)
            DB_USER="$2"
            shift 2
            ;;
        --password)
            DB_PASSWORD="$2"
            shift 2
            ;;
        *)
            echo -e "${RED}Opção desconhecida: $1${NC}"
            show_help
            exit 1
            ;;
    esac
done

# Executar função principal
main