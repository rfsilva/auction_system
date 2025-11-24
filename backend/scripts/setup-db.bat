@echo off
setlocal enabledelayedexpansion

REM =====================================================
REM Script de Setup do Banco de Dados - Windows
REM Sistema de Leilão Eletrônico
REM =====================================================

echo === Sistema de Leilao Eletronico - Setup do Banco ===
echo.

REM Configurações padrão
if "%DB_HOST%"=="" set DB_HOST=localhost
if "%DB_PORT%"=="" set DB_PORT=5432
if "%DB_NAME%"=="" set DB_NAME=leilao_db
if "%DB_USER%"=="" set DB_USER=leilao_user
if "%DB_PASSWORD%"=="" set DB_PASSWORD=leilao_pass
if "%POSTGRES_USER%"=="" set POSTGRES_USER=postgres

echo Configuracoes:
echo Host: %DB_HOST%
echo Porta: %DB_PORT%
echo Banco: %DB_NAME%
echo Usuario: %DB_USER%
echo.

REM Verificar se PostgreSQL está instalado
echo Verificando PostgreSQL...
where psql >nul 2>nul
if %errorlevel% neq 0 (
    echo ERRO: PostgreSQL nao encontrado no PATH
    echo Por favor, instale o PostgreSQL e adicione ao PATH
    pause
    exit /b 1
)

REM Verificar se PostgreSQL está rodando
echo Verificando se PostgreSQL esta rodando...
pg_isready -h %DB_HOST% -p %DB_PORT% -U %POSTGRES_USER% >nul 2>nul
if %errorlevel% neq 0 (
    echo ERRO: PostgreSQL nao esta rodando ou nao esta acessivel
    echo Por favor, inicie o PostgreSQL e tente novamente
    pause
    exit /b 1
)
echo PostgreSQL esta rodando!

REM Criar usuário e banco
echo.
echo Criando usuario e banco de dados...
echo Pode ser solicitada a senha do usuario postgres

REM Criar usuário
psql -h %DB_HOST% -p %DB_PORT% -U %POSTGRES_USER% -c "DO $$ BEGIN IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = '%DB_USER%') THEN CREATE ROLE %DB_USER% LOGIN PASSWORD '%DB_PASSWORD%'; RAISE NOTICE 'Usuario %DB_USER% criado'; ELSE RAISE NOTICE 'Usuario %DB_USER% ja existe'; END IF; END $$;"

if %errorlevel% neq 0 (
    echo ERRO: Falha ao criar usuario
    pause
    exit /b 1
)

REM Criar banco
psql -h %DB_HOST% -p %DB_PORT% -U %POSTGRES_USER% -c "SELECT 'CREATE DATABASE %DB_NAME% OWNER %DB_USER%' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = '%DB_NAME%')\gexec"

if %errorlevel% neq 0 (
    echo ERRO: Falha ao criar banco de dados
    pause
    exit /b 1
)

REM Conceder privilégios
psql -h %DB_HOST% -p %DB_PORT% -U %POSTGRES_USER% -c "GRANT ALL PRIVILEGES ON DATABASE %DB_NAME% TO %DB_USER%;"

echo Usuario e banco criados com sucesso!

REM Executar migrations
echo.
echo Executando migrations...
cd /d "%~dp0\.."

REM Verificar se Maven está disponível
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo AVISO: Maven nao encontrado no PATH
    echo Execute manualmente: mvn flyway:migrate
    goto :verify
)

REM Executar Flyway
mvn flyway:migrate -Dflyway.url=jdbc:postgresql://%DB_HOST%:%DB_PORT%/%DB_NAME% -Dflyway.user=%DB_USER% -Dflyway.password=%DB_PASSWORD% -Dflyway.locations=classpath:db/migration -Dflyway.schemas=public

if %errorlevel% neq 0 (
    echo ERRO: Falha ao executar migrations
    pause
    exit /b 1
)

echo Migrations executadas com sucesso!

:verify
REM Verificar instalação
echo.
echo Verificando instalacao...

REM Contar tabelas
for /f %%i in ('psql -h %DB_HOST% -p %DB_PORT% -U %DB_USER% -d %DB_NAME% -t -c "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_type = 'BASE TABLE';"') do set TABLE_COUNT=%%i

set TABLE_COUNT=%TABLE_COUNT: =%

if %TABLE_COUNT% gtr 10 (
    echo Banco configurado corretamente ^(%TABLE_COUNT% tabelas criadas^)
) else (
    echo ERRO: Problema na instalacao ^(apenas %TABLE_COUNT% tabelas encontradas^)
    pause
    exit /b 1
)

REM Mostrar informações de conexão
echo.
echo === Informacoes de Conexao ===
echo Host: %DB_HOST%
echo Porta: %DB_PORT%
echo Banco: %DB_NAME%
echo Usuario: %DB_USER%
echo Senha: %DB_PASSWORD%
echo.
echo String de conexao para aplicacao:
echo jdbc:postgresql://%DB_HOST%:%DB_PORT%/%DB_NAME%
echo.

echo Setup do banco concluido com sucesso!
echo Agora voce pode iniciar a aplicacao Spring Boot
echo.
pause