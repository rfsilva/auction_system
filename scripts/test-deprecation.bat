@echo off
REM Script de Teste - Validação da Depreciação do Catálogo de Produtos
REM Data: 19/12/2024
REM Objetivo: Validar comportamento dos endpoints depreciados

echo.
echo 🧪 TESTE DE DEPRECIAÇÃO - CATÁLOGO DE PRODUTOS
echo ==============================================
echo.

set BASE_URL=http://localhost:8080/api
set FRONTEND_URL=http://localhost:4200

REM Verificar se curl está disponível
curl --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ ERRO: curl não está disponível
    echo Instale curl ou use o Postman para testar
    pause
    exit /b 1
)

REM Verificar se backend está rodando
echo 🔍 Verificando se o backend está rodando...
curl -s "%BASE_URL%/actuator/health" >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ Backend está rodando
) else (
    echo ❌ Backend não está rodando em %BASE_URL%
    echo Execute: cd backend ^&^& mvn spring-boot:run
    pause
    exit /b 1
)

echo.

REM Teste 1: Endpoint de catálogo de produtos (deve retornar 301)
echo 🧪 Teste 1: Catálogo de produtos (DEPRECIADO)
echo URL: %BASE_URL%/catalogo/produtos
curl -I "%BASE_URL%/catalogo/produtos" 2>nul | findstr "HTTP"
echo.

REM Teste 2: Endpoint de produto específico (deve retornar 301 ou 410)
echo 🧪 Teste 2: Produto específico (DEPRECIADO)
echo URL: %BASE_URL%/catalogo/produtos/123e4567-e89b-12d3-a456-426614174000
curl -I "%BASE_URL%/catalogo/produtos/123e4567-e89b-12d3-a456-426614174000" 2>nul | findstr "HTTP"
echo.

REM Teste 3: Endpoint de categorias (deve continuar funcionando)
echo 🧪 Teste 3: Categorias (MANTIDO)
echo URL: %BASE_URL%/catalogo/categorias
curl -I "%BASE_URL%/catalogo/categorias" 2>nul | findstr "HTTP"
echo.

REM Teste 4: Novo endpoint de lotes (deve funcionar)
echo 🧪 Teste 4: Novo catálogo de lotes
echo URL: %BASE_URL%/lotes/catalogo-publico
curl -I "%BASE_URL%/lotes/catalogo-publico" 2>nul | findstr "HTTP"
echo.

echo ==============================================
echo 🏁 TESTES CONCLUÍDOS
echo.
echo 📋 PRÓXIMOS PASSOS:
echo 1. Verificar logs do backend para warnings de depreciação
echo 2. Testar frontend em %FRONTEND_URL%
echo 3. Validar redirects automáticos
echo 4. Monitorar métricas de acesso
echo.
echo 📚 DOCUMENTAÇÃO:
echo - Guia de migração: doc/MIGRATION_CATALOG_DEPRECATION.md
echo - Collections Postman: backend/postman/04-Produtos-Updated.postman_collection.json
echo.

pause