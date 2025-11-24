-- =====================================================
-- Script de Correção para Arrays de Enum
-- Sistema de Leilão Eletrônico
-- =====================================================

-- Este script corrige o problema de tipo em arrays de enum
-- Execute apenas se você teve erro ao rodar as migrations

-- =====================================================
-- OPÇÃO 1: Corrigir tabela existente (se já foi criada)
-- =====================================================

-- Verificar se a tabela usuarios existe e tem o problema
DO $$
BEGIN
    IF EXISTS (SELECT FROM information_schema.tables WHERE table_name = 'usuarios') THEN
        -- Alterar a coluna para aceitar o tipo correto
        ALTER TABLE usuarios ALTER COLUMN roles SET DEFAULT ARRAY['VISITOR'::user_role];
        
        -- Atualizar registros existentes que podem ter problema
        UPDATE usuarios SET roles = ARRAY['VISITOR'::user_role] WHERE roles IS NULL;
        
        RAISE NOTICE 'Tabela usuarios corrigida com sucesso';
    ELSE
        RAISE NOTICE 'Tabela usuarios não existe ainda';
    END IF;
END
$$;

-- =====================================================
-- OPÇÃO 2: Recriar schema completo (se necessário)
-- =====================================================

-- Descomente as linhas abaixo apenas se necessário fazer reset completo
-- CUIDADO: Isso apagará todos os dados!

/*
-- Dropar todas as tabelas na ordem correta (respeitando foreign keys)
DROP TABLE IF EXISTS historico_produtos CASCADE;
DROP TABLE IF EXISTS pre_autorizacoes CASCADE;
DROP TABLE IF EXISTS notificacoes CASCADE;
DROP TABLE IF EXISTS favoritos CASCADE;
DROP TABLE IF EXISTS disputas CASCADE;
DROP TABLE IF EXISTS audit_log CASCADE;
DROP TABLE IF EXISTS documentos CASCADE;
DROP TABLE IF EXISTS arremates CASCADE;
DROP TABLE IF EXISTS lances CASCADE;
DROP TABLE IF EXISTS produtos CASCADE;
DROP TABLE IF EXISTS lotes CASCADE;
DROP TABLE IF EXISTS compradores CASCADE;
DROP TABLE IF EXISTS vendedores CASCADE;
DROP TABLE IF EXISTS contratos CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;

-- Dropar enums
DROP TYPE IF EXISTS pre_auth_status CASCADE;
DROP TYPE IF EXISTS notification_status CASCADE;
DROP TYPE IF EXISTS dispute_status CASCADE;
DROP TYPE IF EXISTS payment_status CASCADE;
DROP TYPE IF EXISTS lot_status CASCADE;
DROP TYPE IF EXISTS product_status CASCADE;
DROP TYPE IF EXISTS kyc_status CASCADE;
DROP TYPE IF EXISTS user_role CASCADE;
DROP TYPE IF EXISTS user_status CASCADE;

-- Dropar funções
DROP FUNCTION IF EXISTS update_updated_at_column() CASCADE;

-- Após executar este reset, execute novamente:
-- mvn flyway:migrate
*/

-- =====================================================
-- VERIFICAÇÃO FINAL
-- =====================================================

-- Verificar se a correção funcionou
SELECT 
    table_name,
    column_name,
    data_type,
    column_default
FROM information_schema.columns 
WHERE table_name = 'usuarios' 
  AND column_name = 'roles';

-- Testar inserção com enum correto
DO $$
BEGIN
    -- Tentar inserir um usuário de teste
    INSERT INTO usuarios (nome, email, senha_hash, status, roles, email_verificado) 
    VALUES (
        'Teste Correção',
        'teste.correcao@leilao.com',
        '$2a$10$test',
        'ACTIVE',
        ARRAY['VISITOR'::user_role],
        TRUE
    );
    
    -- Remover o usuário de teste
    DELETE FROM usuarios WHERE email = 'teste.correcao@leilao.com';
    
    RAISE NOTICE 'Teste de inserção com enum array funcionou corretamente!';
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'Erro no teste: %', SQLERRM;
END
$$;

-- =====================================================
-- INSTRUÇÕES DE USO
-- =====================================================

/*
COMO USAR ESTE SCRIPT:

1. Se você teve erro ao executar as migrations:
   psql -h localhost -U leilao_user -d leilao_db -f scripts/fix-enum-arrays.sql

2. Se você quer fazer reset completo (CUIDADO - apaga dados):
   - Descomente a seção "OPÇÃO 2" acima
   - Execute o script
   - Execute: mvn flyway:migrate

3. Verificar se funcionou:
   - O script mostra mensagens de sucesso/erro
   - Execute: mvn spring-boot:run
   - A aplicação deve iniciar sem erros
*/