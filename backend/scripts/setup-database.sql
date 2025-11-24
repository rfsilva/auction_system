-- =====================================================
-- Script de Setup do Banco de Dados
-- Sistema de Leilão Eletrônico
-- =====================================================

-- Criar banco de dados
CREATE DATABASE leilao_db
    WITH 
    OWNER = leilao_user
    ENCODING = 'UTF8'
    LC_COLLATE = 'pt_BR.UTF-8'
    LC_CTYPE = 'pt_BR.UTF-8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Comentário no banco
COMMENT ON DATABASE leilao_db IS 'Banco de dados do Sistema de Leilão Eletrônico';

-- Conectar ao banco criado
\c leilao_db;

-- Criar usuário se não existir
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'leilao_user') THEN
        CREATE ROLE leilao_user LOGIN PASSWORD 'leilao_pass';
    END IF;
END
$$;

-- Conceder privilégios
GRANT ALL PRIVILEGES ON DATABASE leilao_db TO leilao_user;
GRANT ALL ON SCHEMA public TO leilao_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO leilao_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO leilao_user;

-- Alterar owner do schema public
ALTER SCHEMA public OWNER TO leilao_user;

-- Configurações de timezone
SET timezone = 'America/Sao_Paulo';

-- Configurações de encoding
SET client_encoding = 'UTF8';

-- =====================================================
-- Configurações de Performance
-- =====================================================

-- Configurar parâmetros de performance (ajustar conforme ambiente)
-- ALTER SYSTEM SET shared_buffers = '256MB';
-- ALTER SYSTEM SET effective_cache_size = '1GB';
-- ALTER SYSTEM SET maintenance_work_mem = '64MB';
-- ALTER SYSTEM SET checkpoint_completion_target = 0.9;
-- ALTER SYSTEM SET wal_buffers = '16MB';
-- ALTER SYSTEM SET default_statistics_target = 100;

-- =====================================================
-- Extensões Necessárias
-- =====================================================

-- Extensão para UUIDs
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Extensão para funções de texto
CREATE EXTENSION IF NOT EXISTS "unaccent";

-- Extensão para busca full-text em português
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- =====================================================
-- Configurações de Backup
-- =====================================================

-- Criar diretório para backups (ajustar path conforme ambiente)
-- mkdir -p /var/backups/leilao_db

-- Script de backup automático (exemplo)
-- 0 2 * * * pg_dump -h localhost -U leilao_user -d leilao_db | gzip > /var/backups/leilao_db/backup_$(date +\%Y\%m\%d_\%H\%M\%S).sql.gz

-- =====================================================
-- Verificações de Saúde
-- =====================================================

-- Função para verificar saúde do banco
CREATE OR REPLACE FUNCTION check_database_health()
RETURNS TABLE (
    check_name TEXT,
    status TEXT,
    details TEXT
) AS $$
BEGIN
    -- Verificar conexões ativas
    RETURN QUERY
    SELECT 
        'active_connections'::TEXT,
        CASE WHEN count(*) < 80 THEN 'OK' ELSE 'WARNING' END::TEXT,
        ('Total connections: ' || count(*))::TEXT
    FROM pg_stat_activity 
    WHERE state = 'active';
    
    -- Verificar tamanho do banco
    RETURN QUERY
    SELECT 
        'database_size'::TEXT,
        'OK'::TEXT,
        pg_size_pretty(pg_database_size(current_database()))::TEXT;
    
    -- Verificar última execução do VACUUM
    RETURN QUERY
    SELECT 
        'last_vacuum'::TEXT,
        CASE 
            WHEN MAX(last_vacuum) > NOW() - INTERVAL '7 days' THEN 'OK'
            WHEN MAX(last_vacuum) > NOW() - INTERVAL '30 days' THEN 'WARNING'
            ELSE 'CRITICAL'
        END::TEXT,
        ('Last vacuum: ' || COALESCE(MAX(last_vacuum)::TEXT, 'Never'))::TEXT
    FROM pg_stat_user_tables;
    
END;
$$ LANGUAGE plpgsql;

-- =====================================================
-- Políticas de Retenção
-- =====================================================

-- Função para limpeza de dados antigos (executar periodicamente)
CREATE OR REPLACE FUNCTION cleanup_old_data()
RETURNS TEXT AS $$
DECLARE
    deleted_count INTEGER;
    result_text TEXT := '';
BEGIN
    -- Limpar audit_log mais antigo que 2 anos
    DELETE FROM audit_log 
    WHERE timestamp < NOW() - INTERVAL '2 years';
    GET DIAGNOSTICS deleted_count = ROW_COUNT;
    result_text := result_text || 'Deleted ' || deleted_count || ' old audit_log records. ';
    
    -- Limpar notificações lidas mais antigas que 6 meses
    DELETE FROM notificacoes 
    WHERE status = 'READ' AND read_at < NOW() - INTERVAL '6 months';
    GET DIAGNOSTICS deleted_count = ROW_COUNT;
    result_text := result_text || 'Deleted ' || deleted_count || ' old notifications. ';
    
    -- Limpar pré-autorizações expiradas mais antigas que 1 ano
    DELETE FROM pre_autorizacoes 
    WHERE status = 'EXPIRED' AND expires_at < NOW() - INTERVAL '1 year';
    GET DIAGNOSTICS deleted_count = ROW_COUNT;
    result_text := result_text || 'Deleted ' || deleted_count || ' old pre-authorizations.';
    
    RETURN result_text;
END;
$$ LANGUAGE plpgsql;

-- =====================================================
-- Monitoramento
-- =====================================================

-- View para monitorar leilões ativos
CREATE OR REPLACE VIEW v_leiloes_ativos AS
SELECT 
    p.id,
    p.title,
    p.current_price,
    p.end_datetime,
    EXTRACT(EPOCH FROM (p.end_datetime - NOW())) / 3600 AS horas_restantes,
    COUNT(l.id) AS total_lances,
    MAX(l.timestamp) AS ultimo_lance
FROM produtos p
LEFT JOIN lances l ON p.id = l.produto_id
WHERE p.status = 'ACTIVE' 
  AND p.end_datetime > NOW()
GROUP BY p.id, p.title, p.current_price, p.end_datetime
ORDER BY p.end_datetime ASC;

-- View para estatísticas gerais
CREATE OR REPLACE VIEW v_estatisticas_sistema AS
SELECT 
    (SELECT COUNT(*) FROM usuarios WHERE status = 'ACTIVE') AS usuarios_ativos,
    (SELECT COUNT(*) FROM produtos WHERE status = 'ACTIVE') AS produtos_ativos,
    (SELECT COUNT(*) FROM lances WHERE timestamp > NOW() - INTERVAL '24 hours') AS lances_24h,
    (SELECT COUNT(*) FROM arremates WHERE created_at > NOW() - INTERVAL '30 days') AS vendas_30d,
    (SELECT SUM(final_price) FROM arremates WHERE created_at > NOW() - INTERVAL '30 days') AS volume_30d;

-- =====================================================
-- Índices de Performance Adicionais
-- =====================================================

-- Índices compostos para queries frequentes
CREATE INDEX IF NOT EXISTS idx_produtos_status_end_datetime ON produtos(status, end_datetime) WHERE status = 'ACTIVE';
CREATE INDEX IF NOT EXISTS idx_lances_produto_timestamp_desc ON lances(produto_id, timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_usuarios_email_status ON usuarios(email, status);
CREATE INDEX IF NOT EXISTS idx_notificacoes_user_status_created ON notificacoes(user_id, status, created_at);

-- =====================================================
-- Triggers de Auditoria Automática
-- =====================================================

-- Função genérica para auditoria
CREATE OR REPLACE FUNCTION audit_trigger_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO audit_log (entity_type, entity_id, action, performed_by, new_values, metadata)
        VALUES (TG_TABLE_NAME, NEW.id, 'CREATE', 
                COALESCE(current_setting('app.current_user_id', true)::UUID, NEW.id),
                row_to_json(NEW), 
                json_build_object('table', TG_TABLE_NAME, 'operation', TG_OP));
        RETURN NEW;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO audit_log (entity_type, entity_id, action, performed_by, old_values, new_values, metadata)
        VALUES (TG_TABLE_NAME, NEW.id, 'UPDATE',
                COALESCE(current_setting('app.current_user_id', true)::UUID, NEW.id),
                row_to_json(OLD), row_to_json(NEW),
                json_build_object('table', TG_TABLE_NAME, 'operation', TG_OP));
        RETURN NEW;
    ELSIF TG_OP = 'DELETE' THEN
        INSERT INTO audit_log (entity_type, entity_id, action, performed_by, old_values, metadata)
        VALUES (TG_TABLE_NAME, OLD.id, 'DELETE',
                COALESCE(current_setting('app.current_user_id', true)::UUID, OLD.id),
                row_to_json(OLD),
                json_build_object('table', TG_TABLE_NAME, 'operation', TG_OP));
        RETURN OLD;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- =====================================================
-- Configurações Finais
-- =====================================================

-- Atualizar estatísticas
ANALYZE;

-- Mensagem de sucesso
SELECT 'Database setup completed successfully!' AS status;