@echo off
setlocal enabledelayedexpansion

REM =====================================================
REM Script de Limpeza Docker - Windows
REM Sistema de Leilão Eletrônico
REM =====================================================

echo === Limpeza de Containers Docker ===
echo.

REM Função para parar containers
echo Parando containers relacionados ao leilao...

REM Lista de containers que podem estar rodando
set CONTAINERS=leilao-mysql leilao-mysql-dev leilao-postgres leilao-redis leilao-redis-dev leilao-phpmyadmin leilao-phpmyadmin-dev leilao-pgadmin leilao-redis-commander leilao-redis-commander-dev auction_postgres auction_mysql

for %%c in (%CONTAINERS%) do (
    for /f %%i in ('docker ps -q -f name^=%%c 2^>nul') do (
        if not "%%i"=="" (
            echo Parando container: %%c
            docker stop %%c >nul 2>nul
        )
    )
)

echo.
echo Removendo containers parados...

REM Remover containers
for %%c in (%CONTAINERS%) do (
    for /f %%i in ('docker ps -aq -f name^=%%c 2^>nul') do (
        if not "%%i"=="" (
            echo Removendo container: %%c
            docker rm %%c >nul 2>nul
        )
    )
)

echo.
echo Limpando volumes orfaos...
docker volume prune -f >nul 2>nul

echo.
echo Limpando networks orfaos...
docker network prune -f >nul 2>nul

echo.
echo === Status Atual ===
echo.
echo Containers rodando:
docker ps --format "table {{.Names}}\t{{.Image}}\t{{.Status}}" | findstr /i "leilao auction" 2>nul
if %errorlevel% neq 0 echo Nenhum container relacionado rodando

echo.
echo Volumes existentes:
docker volume ls | findstr /i "leilao auction mysql postgres redis" 2>nul
if %errorlevel% neq 0 echo Nenhum volume relacionado encontrado

echo.
echo Networks existentes:
docker network ls | findstr /i "leilao auction" 2>nul
if %errorlevel% neq 0 echo Nenhuma network relacionada encontrada

echo.
echo Limpeza concluida!
echo Agora voce pode executar:
echo docker-compose -f docker-compose.dev.yml up mysql redis
echo.
pause