#!/bin/bash

# =====================================================
# Script de Limpeza Forçada Docker
# Sistema de Leilão Eletrônico
# =====================================================

set -e

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${RED}=== LIMPEZA FORÇADA DE CONTAINERS DOCKER ===${NC}"
echo -e "${YELLOW}⚠️  ATENÇÃO: Este script fará uma limpeza agressiva!${NC}"
echo ""

# Função para confirmar ação
confirm() {
    read -p "Deseja continuar? (y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        echo "Operação cancelada."
        exit 1
    fi
}

# Mostrar containers problemáticos
echo -e "${BLUE}Containers que podem estar causando problema:${NC}"
docker ps -a --format "table {{.Names}}\t{{.Image}}\t{{.Status}}" | grep -E "(leilao|mysql|redis|postgres)" || echo "Nenhum encontrado"
echo ""

confirm

echo -e "${YELLOW}1. Parando TODOS os containers...${NC}"
docker stop $(docker ps -aq) 2>/dev/null || echo "Nenhum container rodando"

echo -e "${YELLOW}2. Removendo containers específicos...${NC}"
# IDs específicos que aparecem no erro
docker rm c9ea23d0a4f0 2>/dev/null || echo "Container c9ea23d0a4f0 não encontrado"
docker rm 9675dc17b283 2>/dev/null || echo "Container 9675dc17b283 não encontrado"

# Containers por nome
CONTAINERS=(
    "leilao-mysql"
    "leilao-mysql-dev" 
    "leilao-redis"
    "leilao-redis-dev"
    "leilao-phpmyadmin"
    "leilao-phpmyadmin-dev"
    "leilao-pgadmin"
    "leilao-redis-commander"
    "leilao-redis-commander-dev"
    "auction_mysql"
    "auction_postgres"
)

for container in "${CONTAINERS[@]}"; do
    if docker ps -aq -f name="$container" | grep -q .; then
        echo "Removendo: $container"
        docker rm -f "$container" 2>/dev/null || true
    fi
done

echo -e "${YELLOW}3. Removendo TODOS os containers parados...${NC}"
docker container prune -f

echo -e "${YELLOW}4. Removendo volumes relacionados...${NC}"
# Volumes específicos
VOLUMES=(
    "backend_mysql_dev_data"
    "backend_redis_dev_data"
    "backend_mysql_data"
    "backend_redis_data"
    "mysql_dev_data"
    "redis_dev_data"
    "mysql_data"
    "redis_data"
    "mysqldata"
    "pgdata"
)

for volume in "${VOLUMES[@]}"; do
    if docker volume ls -q | grep -q "^$volume$"; then
        echo "Removendo volume: $volume"
        docker volume rm "$volume" 2>/dev/null || true
    fi
done

echo -e "${YELLOW}5. Limpando todos os volumes órfãos...${NC}"
docker volume prune -f

echo -e "${YELLOW}6. Removendo networks relacionadas...${NC}"
NETWORKS=(
    "backend_leilao-dev-network"
    "backend_leilao-network"
    "leilao-dev-network"
    "leilao-network"
)

for network in "${NETWORKS[@]}"; do
    if docker network ls | grep -q "$network"; then
        echo "Removendo network: $network"
        docker network rm "$network" 2>/dev/null || true
    fi
done

echo -e "${YELLOW}7. Limpando networks órfãs...${NC}"
docker network prune -f

echo -e "${YELLOW}8. Limpando imagens não utilizadas...${NC}"
docker image prune -f

echo -e "${YELLOW}9. Limpeza completa do sistema Docker...${NC}"
docker system prune -f

echo ""
echo -e "${GREEN}✅ Limpeza forçada concluída!${NC}"
echo ""

echo -e "${BLUE}=== Status Final ===${NC}"
echo -e "${YELLOW}Containers:${NC}"
docker ps -a --format "table {{.Names}}\t{{.Image}}\t{{.Status}}" | head -10

echo ""
echo -e "${YELLOW}Volumes:${NC}"
docker volume ls | head -10

echo ""
echo -e "${YELLOW}Networks:${NC}"
docker network ls

echo ""
echo -e "${GREEN}Agora tente executar:${NC}"
echo -e "${BLUE}cd backend${NC}"
echo -e "${BLUE}docker-compose -f docker-compose.dev.yml up mysql redis${NC}"
echo ""
echo -e "${YELLOW}Ou use o docker-compose.yml da raiz do projeto:${NC}"
echo -e "${BLUE}cd .. && docker-compose up mysql${NC}"