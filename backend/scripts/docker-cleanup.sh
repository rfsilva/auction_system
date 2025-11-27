#!/bin/bash

# =====================================================
# Script de Limpeza Docker
# Sistema de Leilão Eletrônico
# =====================================================

set -e

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== Limpeza de Containers Docker ===${NC}"
echo ""

# Função para parar containers relacionados ao leilão
stop_containers() {
    echo -e "${YELLOW}Parando containers relacionados ao leilão...${NC}"
    
    # Lista de containers que podem estar rodando
    CONTAINERS=(
        "leilao-mysql"
        "leilao-mysql-dev"
        "leilao-postgres"
        "leilao-redis"
        "leilao-redis-dev"
        "leilao-phpmyadmin"
        "leilao-phpmyadmin-dev"
        "leilao-pgadmin"
        "leilao-redis-commander"
        "leilao-redis-commander-dev"
        "auction_postgres"
        "auction_mysql"
    )
    
    for container in "${CONTAINERS[@]}"; do
        if docker ps -q -f name="$container" | grep -q .; then
            echo -e "Parando container: ${YELLOW}$container${NC}"
            docker stop "$container" 2>/dev/null || true
        fi
    done
}

# Função para remover containers
remove_containers() {
    echo -e "${YELLOW}Removendo containers parados...${NC}"
    
    # Lista de containers que podem existir
    CONTAINERS=(
        "leilao-mysql"
        "leilao-mysql-dev"
        "leilao-postgres"
        "leilao-redis"
        "leilao-redis-dev"
        "leilao-phpmyadmin"
        "leilao-phpmyadmin-dev"
        "leilao-pgadmin"
        "leilao-redis-commander"
        "leilao-redis-commander-dev"
        "auction_postgres"
        "auction_mysql"
    )
    
    for container in "${CONTAINERS[@]}"; do
        if docker ps -aq -f name="$container" | grep -q .; then
            echo -e "Removendo container: ${YELLOW}$container${NC}"
            docker rm "$container" 2>/dev/null || true
        fi
    done
}

# Função para limpar volumes órfãos
cleanup_volumes() {
    echo -e "${YELLOW}Limpando volumes órfãos...${NC}"
    docker volume prune -f 2>/dev/null || true
}

# Função para limpar networks órfãs
cleanup_networks() {
    echo -e "${YELLOW}Limpando networks órfãs...${NC}"
    docker network prune -f 2>/dev/null || true
}

# Função para mostrar status atual
show_status() {
    echo -e "${BLUE}=== Status Atual ===${NC}"
    
    echo -e "${YELLOW}Containers rodando:${NC}"
    docker ps --format "table {{.Names}}\t{{.Image}}\t{{.Status}}" | grep -E "(leilao|auction)" || echo "Nenhum container relacionado rodando"
    
    echo ""
    echo -e "${YELLOW}Volumes existentes:${NC}"
    docker volume ls | grep -E "(leilao|auction|mysql|postgres|redis)" || echo "Nenhum volume relacionado encontrado"
    
    echo ""
    echo -e "${YELLOW}Networks existentes:${NC}"
    docker network ls | grep -E "(leilao|auction)" || echo "Nenhuma network relacionada encontrada"
}

# Função principal
main() {
    echo -e "${BLUE}Iniciando limpeza...${NC}"
    echo ""
    
    stop_containers
    echo ""
    
    remove_containers
    echo ""
    
    cleanup_volumes
    echo ""
    
    cleanup_networks
    echo ""
    
    show_status
    echo ""
    
    echo -e "${GREEN}✅ Limpeza concluída!${NC}"
    echo -e "${YELLOW}Agora você pode executar:${NC}"
    echo "docker-compose -f docker-compose.dev.yml up mysql redis"
}

# Função de ajuda
show_help() {
    echo "Uso: $0 [opções]"
    echo ""
    echo "Opções:"
    echo "  -h, --help          Mostra esta ajuda"
    echo "  --status            Mostra apenas o status atual"
    echo "  --stop              Para apenas os containers"
    echo "  --remove            Remove apenas os containers parados"
    echo "  --volumes           Limpa apenas volumes órfãos"
    echo "  --networks          Limpa apenas networks órfãs"
    echo ""
    echo "Exemplo:"
    echo "  $0                  # Limpeza completa"
    echo "  $0 --status         # Apenas status"
    echo "  $0 --stop           # Apenas parar containers"
}

# Processar argumentos da linha de comando
case "${1:-}" in
    -h|--help)
        show_help
        exit 0
        ;;
    --status)
        show_status
        exit 0
        ;;
    --stop)
        stop_containers
        exit 0
        ;;
    --remove)
        remove_containers
        exit 0
        ;;
    --volumes)
        cleanup_volumes
        exit 0
        ;;
    --networks)
        cleanup_networks
        exit 0
        ;;
    "")
        main
        ;;
    *)
        echo -e "${RED}Opção desconhecida: $1${NC}"
        show_help
        exit 1
        ;;
esac