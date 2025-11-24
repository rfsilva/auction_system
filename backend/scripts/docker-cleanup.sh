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

echo -e "${BLUE}=== Limpeza Docker - Sistema de Leilão ===${NC}"
echo ""

# Função para parar containers relacionados
stop_containers() {
    echo -e "${YELLOW}Parando containers do leilão...${NC}"
    
    # Parar containers específicos se estiverem rodando
    containers=("leilao-postgres" "leilao-redis" "leilao-pgadmin" "leilao-redis-commander")
    
    for container in "${containers[@]}"; do
        if docker ps -q -f name="$container" | grep -q .; then
            echo "Parando container: $container"
            docker stop "$container" 2>/dev/null || true
        fi
    done
    
    echo -e "${GREEN}✅ Containers parados${NC}"
}

# Função para remover containers
remove_containers() {
    echo -e "${YELLOW}Removendo containers do leilão...${NC}"
    
    containers=("leilao-postgres" "leilao-redis" "leilao-pgladmin" "leilao-redis-commander")
    
    for container in "${containers[@]}"; do
        if docker ps -a -q -f name="$container" | grep -q .; then
            echo "Removendo container: $container"
            docker rm "$container" 2>/dev/null || true
        fi
    done
    
    echo -e "${GREEN}✅ Containers removidos${NC}"
}

# Função para remover redes
remove_networks() {
    echo -e "${YELLOW}Removendo redes conflitantes...${NC}"
    
    # Listar redes que podem estar conflitando
    networks=("backend_leilao-network" "leilao-network" "backend_default")
    
    for network in "${networks[@]}"; do
        if docker network ls -q -f name="$network" | grep -q .; then
            echo "Removendo rede: $network"
            docker network rm "$network" 2>/dev/null || true
        fi
    done
    
    echo -e "${GREEN}✅ Redes removidas${NC}"
}

# Função para limpar volumes (opcional)
remove_volumes() {
    echo -e "${YELLOW}Removendo volumes (CUIDADO: dados serão perdidos!)...${NC}"
    read -p "Tem certeza que deseja remover os volumes? (y/N): " -n 1 -r
    echo
    
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        volumes=("backend_postgres_data" "backend_redis_data" "backend_pgadmin_data")
        
        for volume in "${volumes[@]}"; do
            if docker volume ls -q -f name="$volume" | grep -q .; then
                echo "Removendo volume: $volume"
                docker volume rm "$volume" 2>/dev/null || true
            fi
        done
        
        echo -e "${GREEN}✅ Volumes removidos${NC}"
    else
        echo -e "${YELLOW}⚠️  Volumes mantidos${NC}"
    fi
}

# Função para limpeza geral do Docker
docker_prune() {
    echo -e "${YELLOW}Executando limpeza geral do Docker...${NC}"
    
    # Remover containers parados
    docker container prune -f
    
    # Remover redes não utilizadas
    docker network prune -f
    
    # Remover imagens não utilizadas (opcional)
    read -p "Remover imagens não utilizadas? (y/N): " -n 1 -r
    echo
    
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        docker image prune -f
    fi
    
    echo -e "${GREEN}✅ Limpeza geral concluída${NC}"
}

# Função para verificar conflitos de rede
check_network_conflicts() {
    echo -e "${YELLOW}Verificando conflitos de rede...${NC}"
    
    # Listar todas as redes Docker
    echo "Redes Docker existentes:"
    docker network ls
    
    echo ""
    echo "Verificando subnets em uso:"
    docker network ls -q | xargs docker network inspect | grep -E '"Subnet"|"Gateway"' | head -20
    
    echo -e "${GREEN}✅ Verificação concluída${NC}"
}

# Função principal
main() {
    echo -e "${BLUE}Iniciando limpeza...${NC}"
    echo ""
    
    stop_containers
    remove_containers
    remove_networks
    
    echo ""
    echo -e "${YELLOW}Deseja remover volumes também?${NC}"
    remove_volumes
    
    echo ""
    echo -e "${YELLOW}Deseja executar limpeza geral do Docker?${NC}"
    docker_prune
    
    echo ""
    check_network_conflicts
    
    echo ""
    echo -e "${GREEN}🎉 Limpeza concluída!${NC}"
    echo -e "${YELLOW}Agora você pode executar: docker-compose -f docker-compose.dev.yml up -d${NC}"
}

# Função de ajuda
show_help() {
    echo "Uso: $0 [opção]"
    echo ""
    echo "Opções:"
    echo "  -h, --help          Mostra esta ajuda"
    echo "  --stop-only         Apenas para os containers"
    echo "  --remove-containers Remove containers"
    echo "  --remove-networks   Remove redes"
    echo "  --remove-volumes    Remove volumes"
    echo "  --check-networks    Verifica conflitos de rede"
    echo "  --full-cleanup      Limpeza completa"
    echo ""
    echo "Exemplo:"
    echo "  $0                  # Limpeza interativa"
    echo "  $0 --full-cleanup   # Limpeza completa automática"
}

# Processar argumentos da linha de comando
case "${1:-}" in
    -h|--help)
        show_help
        exit 0
        ;;
    --stop-only)
        stop_containers
        ;;
    --remove-containers)
        stop_containers
        remove_containers
        ;;
    --remove-networks)
        remove_networks
        ;;
    --remove-volumes)
        remove_volumes
        ;;
    --check-networks)
        check_network_conflicts
        ;;
    --full-cleanup)
        stop_containers
        remove_containers
        remove_networks
        docker_prune
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