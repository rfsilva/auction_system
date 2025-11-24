@echo off
setlocal enabledelayedexpansion

REM =====================================================
REM Script de Limpeza Docker - Windows
REM Sistema de Leilão Eletrônico
REM =====================================================

echo === Limpeza Docker - Sistema de Leilao ===
echo.

REM Verificar se Docker está instalado
where docker >nul 2>nul
if %errorlevel% neq 0 (
    echo ERRO: Docker nao encontrado no PATH
    echo Por favor, instale o Docker Desktop
    pause
    exit /b 1
)

echo Parando containers do leilao...

REM Parar containers específicos
docker stop leilao-postgres 2>nul
docker stop leilao-redis 2>nul
docker stop leilao-pgadmin 2>nul
docker stop leilao-redis-commander 2>nul

echo Containers parados!

echo.
echo Removendo containers...

REM Remover containers
docker rm leilao-postgres 2>nul
docker rm leilao-redis 2>nul
docker rm leilao-pgadmin 2>nul
docker rm leilao-redis-commander 2>nul

echo Containers removidos!

echo.
echo Removendo redes conflitantes...

REM Remover redes que podem estar conflitando
docker network rm backend_leilao-network 2>nul
docker network rm leilao-network 2>nul
docker network rm backend_default 2>nul

echo Redes removidas!

echo.
echo Deseja remover volumes tambem? (CUIDADO: dados serao perdidos!)
set /p choice="Digite Y para sim ou N para nao: "

if /i "%choice%"=="Y" (
    echo Removendo volumes...
    docker volume rm backend_postgres_data 2>nul
    docker volume rm backend_redis_data 2>nul
    docker volume rm backend_pgadmin_data 2>nul
    echo Volumes removidos!
) else (
    echo Volumes mantidos.
)

echo.
echo Executando limpeza geral do Docker...

REM Limpeza geral
docker container prune -f
docker network prune -f

echo.
echo Deseja remover imagens nao utilizadas?
set /p choice="Digite Y para sim ou N para nao: "

if /i "%choice%"=="Y" (
    docker image prune -f
    echo Imagens removidas!
)

echo.
echo Verificando redes Docker existentes:
docker network ls

echo.
echo === Limpeza concluida! ===
echo Agora voce pode executar:
echo docker-compose -f docker-compose.dev.yml up -d
echo.
pause