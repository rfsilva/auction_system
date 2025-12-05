-- =====================================================
-- Sistema de Leilão Eletrônico - Massa de Dados V2
-- Entidade: USUÁRIOS
-- Versão: 2.0 - Usuários para Desenvolvimento
-- Data: 2024-12-19
-- Descrição: Massa de dados de usuários com diferentes perfis e cenários
-- =====================================================

-- =====================================================
-- USUÁRIOS ADMINISTRADORES
-- =====================================================

-- Admin Secundário
INSERT INTO tb_usuario (id, nome, email, senha_hash, status, email_verificado, telefone_verificado, telefone, ultimo_login) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440001',
    'Maria Silva Santos',
    'maria.admin@leilao.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    TRUE,
    TRUE,
    '(11) 98765-4321',
    DATE_SUB(NOW(), INTERVAL 2 HOUR)
);

INSERT INTO tb_usuario_role (usuario_id, role) VALUES 
    ('550e8400-e29b-41d4-a716-446655440001', 'ADMIN');

-- Admin de Suporte
INSERT INTO tb_usuario (id, nome, email, senha_hash, status, email_verificado, telefone_verificado, telefone, ultimo_login) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440002',
    'João Carlos Oliveira',
    'joao.suporte@leilao.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    TRUE,
    FALSE,
    '(11) 97654-3210',
    DATE_SUB(NOW(), INTERVAL 1 DAY)
);

INSERT INTO tb_usuario_role (usuario_id, role) VALUES 
    ('550e8400-e29b-41d4-a716-446655440002', 'ADMIN');

-- =====================================================
-- USUÁRIOS VENDEDORES
-- =====================================================

-- Vendedor Ativo - Empresa Grande
INSERT INTO tb_usuario (id, nome, email, senha_hash, status, email_verificado, telefone_verificado, telefone, ultimo_login) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440010',
    'Carlos Eduardo Ferreira',
    'carlos.vendedor@empresaabc.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    TRUE,
    TRUE,
    '(11) 99876-5432',
    DATE_SUB(NOW(), INTERVAL 30 MINUTE)
);

INSERT INTO tb_usuario_role (usuario_id, role) VALUES 
    ('550e8400-e29b-41d4-a716-446655440010', 'SELLER'),
    ('550e8400-e29b-41d4-a716-446655440010', 'PARTICIPANT');

-- Vendedor Ativo - Loja Pequena
INSERT INTO tb_usuario (id, nome, email, senha_hash, status, email_verificado, telefone_verificado, telefone, ultimo_login) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440011',
    'Ana Paula Costa',
    'ana@lojadaana.com.br',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    TRUE,
    TRUE,
    '(11) 98765-1234',
    DATE_SUB(NOW(), INTERVAL 2 HOUR)
);

INSERT INTO tb_usuario_role (usuario_id, role) VALUES 
    ('550e8400-e29b-41d4-a716-446655440011', 'SELLER'),
    ('550e8400-e29b-41d4-a716-446655440011', 'PARTICIPANT');

-- Vendedor Novo - Aguardando Verificação
INSERT INTO tb_usuario (id, nome, email, senha_hash, status, email_verificado, telefone_verificado, telefone, ultimo_login) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440012',
    'Roberto Silva Antunes',
    'roberto@antiqualhas.net',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    TRUE,
    FALSE,
    '(11) 97531-8642',
    DATE_SUB(NOW(), INTERVAL 1 DAY)
);

INSERT INTO tb_usuario_role (usuario_id, role) VALUES 
    ('550e8400-e29b-41d4-a716-446655440012', 'SELLER'),
    ('550e8400-e29b-41d4-a716-446655440012', 'PARTICIPANT');

-- Vendedor Suspenso
INSERT INTO tb_usuario (id, nome, email, senha_hash, status, email_verificado, telefone_verificado, telefone, ultimo_login) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440013',
    'Pedro Henrique Alves',
    'pedro@vendassuspeitas.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'SUSPENDED',
    TRUE,
    TRUE,
    '(11) 96420-7531',
    DATE_SUB(NOW(), INTERVAL 7 DAY)
);

INSERT INTO tb_usuario_role (usuario_id, role) VALUES 
    ('550e8400-e29b-41d4-a716-446655440013', 'SELLER'),
    ('550e8400-e29b-41d4-a716-446655440013', 'PARTICIPANT');

-- =====================================================
-- USUÁRIOS COMPRADORES
-- =====================================================

-- Comprador VIP - Ativo
INSERT INTO tb_usuario (id, nome, email, senha_hash, status, email_verificado, telefone_verificado, telefone, ultimo_login) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440020',
    'Fernanda Rodrigues Lima',
    'fernanda.lima@email.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    TRUE,
    TRUE,
    '(11) 99123-4567',
    DATE_SUB(NOW(), INTERVAL 15 MINUTE)
);

INSERT INTO tb_usuario_role (usuario_id, role) VALUES 
    ('550e8400-e29b-41d4-a716-446655440020', 'BUYER'),
    ('550e8400-e29b-41d4-a716-446655440020', 'PARTICIPANT');

-- Comprador Regular
INSERT INTO tb_usuario (id, nome, email, senha_hash, status, email_verificado, telefone_verificado, telefone, ultimo_login) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440021',
    'Marcos Antonio Pereira',
    'marcos.pereira@gmail.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    TRUE,
    FALSE,
    '(11) 98234-5678',
    DATE_SUB(NOW(), INTERVAL 1 HOUR)
);

INSERT INTO tb_usuario_role (usuario_id, role) VALUES 
    ('550e8400-e29b-41d4-a716-446655440021', 'BUYER'),
    ('550e8400-e29b-41d4-a716-446655440021', 'PARTICIPANT');

-- Comprador Novo - Verificação Pendente
INSERT INTO tb_usuario (id, nome, email, senha_hash, status, email_verificado, telefone_verificado, telefone, ultimo_login) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440022',
    'Juliana Santos Oliveira',
    'juliana.santos@hotmail.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'PENDING_VERIFICATION',
    FALSE,
    FALSE,
    '(11) 97345-6789',
    NULL
);

INSERT INTO tb_usuario_role (usuario_id, role) VALUES 
    ('550e8400-e29b-41d4-a716-446655440022', 'PARTICIPANT');

-- Comprador Colecionador
INSERT INTO tb_usuario (id, nome, email, senha_hash, status, email_verificado, telefone_verificado, telefone, ultimo_login) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440023',
    'Ricardo Mendes Colecionador',
    'ricardo.colecoes@yahoo.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    TRUE,
    TRUE,
    '(11) 96456-7890',
    DATE_SUB(NOW(), INTERVAL 3 HOUR)
);

INSERT INTO tb_usuario_role (usuario_id, role) VALUES 
    ('550e8400-e29b-41d4-a716-446655440023', 'BUYER'),
    ('550e8400-e29b-41d4-a716-446655440023', 'PARTICIPANT');

-- =====================================================
-- USUÁRIOS PARTICIPANTES (SÓ VISUALIZAM)
-- =====================================================

-- Participante Curioso
INSERT INTO tb_usuario (id, nome, email, senha_hash, status, email_verificado, telefone_verificado, telefone, ultimo_login) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440030',
    'Luiza Fernandes Silva',
    'luiza.curiosa@outlook.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    TRUE,
    FALSE,
    '(11) 95567-8901',
    DATE_SUB(NOW(), INTERVAL 6 HOUR)
);

INSERT INTO tb_usuario_role (usuario_id, role) VALUES 
    ('550e8400-e29b-41d4-a716-446655440030', 'PARTICIPANT');

-- Participante Estudante
INSERT INTO tb_usuario (id, nome, email, senha_hash, status, email_verificado, telefone_verificado, telefone, ultimo_login) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440031',
    'Gabriel Costa Estudante',
    'gabriel.estudante@usp.br',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    TRUE,
    FALSE,
    NULL,
    DATE_SUB(NOW(), INTERVAL 12 HOUR)
);

INSERT INTO tb_usuario_role (usuario_id, role) VALUES 
    ('550e8400-e29b-41d4-a716-446655440031', 'PARTICIPANT');

-- =====================================================
-- USUÁRIOS HÍBRIDOS (COMPRADOR E VENDEDOR)
-- =====================================================

-- Usuário que Compra e Vende
INSERT INTO tb_usuario (id, nome, email, senha_hash, status, email_verificado, telefone_verificado, telefone, ultimo_login) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440040',
    'Patricia Almeida Comerciante',
    'patricia@compraevende.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    TRUE,
    TRUE,
    '(11) 94678-9012',
    DATE_SUB(NOW(), INTERVAL 45 MINUTE)
);

INSERT INTO tb_usuario_role (usuario_id, role) VALUES 
    ('550e8400-e29b-41d4-a716-446655440040', 'BUYER'),
    ('550e8400-e29b-41d4-a716-446655440040', 'SELLER'),
    ('550e8400-e29b-41d4-a716-446655440040', 'PARTICIPANT');

-- =====================================================
-- USUÁRIOS COM PROBLEMAS (CENÁRIOS DE TESTE)
-- =====================================================

-- Usuário Bloqueado Temporariamente
INSERT INTO tb_usuario (id, nome, email, senha_hash, status, email_verificado, telefone_verificado, telefone, ultimo_login, tentativas_login, bloqueado_ate) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440050',
    'Bruno Tentativas Excessivas',
    'bruno.bloqueado@email.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    TRUE,
    FALSE,
    '(11) 93789-0123',
    DATE_SUB(NOW(), INTERVAL 2 DAY),
    5,
    DATE_ADD(NOW(), INTERVAL 30 MINUTE)
);

INSERT INTO tb_usuario_role (usuario_id, role) VALUES 
    ('550e8400-e29b-41d4-a716-446655440050', 'PARTICIPANT');

-- Usuário Inativo
INSERT INTO tb_usuario (id, nome, email, senha_hash, status, email_verificado, telefone_verificado, telefone, ultimo_login) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440051',
    'Carla Usuária Inativa',
    'carla.inativa@email.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'INACTIVE',
    TRUE,
    FALSE,
    '(11) 92890-1234',
    DATE_SUB(NOW(), INTERVAL 30 DAY)
);

INSERT INTO tb_usuario_role (usuario_id, role) VALUES 
    ('550e8400-e29b-41d4-a716-446655440051', 'PARTICIPANT');

-- =====================================================
-- COMENTÁRIOS FINAIS
-- =====================================================
/*
USUÁRIOS CRIADOS PARA DESENVOLVIMENTO:

ADMINISTRADORES (3 total):
- admin@leilao.com (root - V1)
- maria.admin@leilao.com (ativo)
- joao.suporte@leilao.com (ativo)

VENDEDORES (5 total):
- carlos.vendedor@empresaabc.com (ativo, verificado)
- ana@lojadaana.com.br (ativo, verificado)
- roberto@antiqualhas.net (ativo, não verificado)
- pedro@vendassuspeitas.com (suspenso)
- patricia@compraevende.com (híbrido)

COMPRADORES (5 total):
- fernanda.lima@email.com (VIP, ativo)
- marcos.pereira@gmail.com (regular)
- juliana.santos@hotmail.com (novo, pendente)
- ricardo.colecoes@yahoo.com (colecionador)
- patricia@compraevende.com (híbrido)

PARTICIPANTES (2 total):
- luiza.curiosa@outlook.com (curioso)
- gabriel.estudante@usp.br (estudante)

CENÁRIOS DE TESTE (2 total):
- bruno.bloqueado@email.com (bloqueado temporariamente)
- carla.inativa@email.com (inativo)

SENHA PADRÃO: password
TOTAL DE USUÁRIOS: 15 (incluindo root)
*/