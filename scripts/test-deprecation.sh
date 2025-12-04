#!/bin/bash

# Script de Teste - Validação da Depreciação do Catálogo de Produtos
# Data: 19/12/2024
# Objetivo: Validar comportamento dos endpoints depreciados

echo "🧪 TESTE DE DEPRECIAÇÃO - CATÁLOGO DE PRODUTOS"
echo "=============================================="
echo ""

BASE_URL="http://localhost:8080/api"
FRONTEND_URL="http://localhost:4200"

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Função para testar endpoint
test_endpoint() {
    local url=$1
    local expected_status=$2
    local description=$3
    
    echo -e "${BLUE}Testando:${NC} $description"
    echo -e "${BLUE}URL:${NC} $url"
    
    response=$(curl -s -w "HTTPSTATUS:%{http_code}" "$url")
    http_status=$(echo $response | tr -d '\n' | sed -e 's/.*HTTPSTATUS://')
    body=$(echo $response | sed -e 's/HTTPSTATUS\:.*//g')
    
    if [ "$http_status" -eq "$expected_status" ]; then
        echo -e "${GREEN}✅ PASSOU${NC} - Status: $http_status"
    else
        echo -e "${RED}❌ FALHOU${NC} - Esperado: $expected_status, Recebido: $http_status"
    fi
    
    if [ ! -z "$body" ]; then
        echo -e "${YELLOW}Response:${NC} $body" | head -c 200
        echo "..."
    fi
    
    echo ""
}

# Verificar se backend está rodando
echo "🔍 Verificando se o backend está rodando..."
if curl -s "$BASE_URL/actuator/health" > /dev/null 2>&1; then
    echo -e "${GREEN}✅ Backend está rodando${NC}"
else
    echo -e "${RED}❌ Backend não está rodando em $BASE_URL${NC}"
    echo "Execute: cd backend && mvn spring-boot:run"
    exit 1
fi

echo ""

# Teste 1: Endpoint de catálogo de produtos (deve retornar 301)
test_endpoint "$BASE_URL/catalogo/produtos" 301 "Catálogo de produtos (DEPRECIADO)"

# Teste 2: Endpoint de produto específico (deve retornar 301 ou 410)
test_endpoint "$BASE_URL/catalogo/produtos/123e4567-e89b-12d3-a456-426614174000" 404 "Produto específico (DEPRECIADO)"

# Teste 3: Endpoint de categorias (deve continuar funcionando)
test_endpoint "$BASE_URL/catalogo/categorias" 200 "Categorias (MANTIDO)"

# Teste 4: Novo endpoint de lotes (deve funcionar)
test_endpoint "$BASE_URL/lotes/catalogo-publico" 200 "Novo catálogo de lotes"

echo "=============================================="
echo "🏁 TESTES CONCLUÍDOS"
echo ""
echo "📋 PRÓXIMOS PASSOS:"
echo "1. Verificar logs do backend para warnings de depreciação"
echo "2. Testar frontend em $FRONTEND_URL"
echo "3. Validar redirects automáticos"
echo "4. Monitorar métricas de acesso"
echo ""
echo "📚 DOCUMENTAÇÃO:"
echo "- Guia de migração: doc/MIGRATION_CATALOG_DEPRECATION.md"
echo "- Collections Postman: backend/postman/04-Produtos-Updated.postman_collection.json"