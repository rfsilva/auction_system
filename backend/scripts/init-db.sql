-- =====================================================
-- Script de Inicialização para Docker
-- Sistema de Leilão Eletrônico
-- =====================================================

-- Criar extensões necessárias
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "unaccent";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- Configurar timezone
SET timezone = 'America/Sao_Paulo';

-- Configurar encoding
SET client_encoding = 'UTF8';

-- Conceder privilégios ao usuário
GRANT ALL PRIVILEGES ON DATABASE leilao_db TO leilao_user;
GRANT ALL ON SCHEMA public TO leilao_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO leilao_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO leilao_user;

-- Alterar owner do schema public
ALTER SCHEMA public OWNER TO leilao_user;

-- Mensagem de sucesso
SELECT 'Database initialized successfully for Docker!' AS status;