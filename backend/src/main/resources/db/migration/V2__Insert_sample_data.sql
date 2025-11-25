-- =====================================================
-- Sistema de Leilão Eletrônico - Dados de Exemplo
-- Versão: 2.2 - Padronização tb_ + singular
-- Data: 2024-11-25
-- =====================================================

-- =====================================================
-- USUÁRIOS DE EXEMPLO
-- =====================================================

-- Usuário vendedor
INSERT INTO tb_usuario (id, nome, email, senha_hash, status, email_verificado, telefone) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440001',
    'João Silva Vendedor',
    'vendedor@leilao.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    TRUE,
    '(11) 99999-0001'
);

-- Roles para o vendedor
INSERT INTO tb_usuario_role (usuario_id, role) VALUES 
    ('550e8400-e29b-41d4-a716-446655440001', 'PARTICIPANT'),
    ('550e8400-e29b-41d4-a716-446655440001', 'SELLER');

-- Usuário comprador
INSERT INTO tb_usuario (id, nome, email, senha_hash, status, email_verificado, telefone) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440002',
    'Maria Santos Compradora',
    'comprador@leilao.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    TRUE,
    '(11) 99999-0002'
);

-- Roles para o comprador
INSERT INTO tb_usuario_role (usuario_id, role) VALUES 
    ('550e8400-e29b-41d4-a716-446655440002', 'PARTICIPANT'),
    ('550e8400-e29b-41d4-a716-446655440002', 'BUYER');

-- Usuário participante
INSERT INTO tb_usuario (id, nome, email, senha_hash, status, email_verificado, telefone) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440003',
    'Carlos Oliveira Participante',
    'participante@leilao.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    TRUE,
    '(11) 99999-0003'
);

-- Role para o participante
INSERT INTO tb_usuario_role (usuario_id, role) VALUES 
    ('550e8400-e29b-41d4-a716-446655440003', 'PARTICIPANT');

-- =====================================================
-- CONTRATOS DE EXEMPLO
-- =====================================================

INSERT INTO tb_contrato (id, seller_id, fee_rate, terms, valid_from, active) 
VALUES (
    '660e8400-e29b-41d4-a716-446655440001',
    '550e8400-e29b-41d4-a716-446655440001',
    0.05, -- 5% de comissão
    'Contrato padrão de comissão para vendedores. Taxa de 5% sobre o valor final de venda.',
    CURRENT_TIMESTAMP,
    TRUE
);

-- =====================================================
-- VENDEDORES DE EXEMPLO
-- =====================================================

INSERT INTO tb_vendedor (id, usuario_id, company_name, tax_id, contract_id, fee_rate, verificado) 
VALUES (
    '770e8400-e29b-41d4-a716-446655440001',
    '550e8400-e29b-41d4-a716-446655440001',
    'Silva Antiguidades Ltda',
    '12.345.678/0001-90',
    '660e8400-e29b-41d4-a716-446655440001',
    0.05,
    TRUE
);

-- =====================================================
-- COMPRADORES DE EXEMPLO
-- =====================================================

INSERT INTO tb_comprador (id, usuario_id, kyc_status, limite_credito) 
VALUES (
    '880e8400-e29b-41d4-a716-446655440001',
    '550e8400-e29b-41d4-a716-446655440002',
    'APPROVED',
    50000.00
);

INSERT INTO tb_comprador (id, usuario_id, kyc_status, limite_credito) 
VALUES (
    '880e8400-e29b-41d4-a716-446655440002',
    '550e8400-e29b-41d4-a716-446655440003',
    'APPROVED',
    25000.00
);

-- =====================================================
-- LOTES DE EXEMPLO
-- =====================================================

INSERT INTO tb_lote (id, seller_id, title, description, lote_end_datetime, status) 
VALUES (
    '990e8400-e29b-41d4-a716-446655440001',
    '770e8400-e29b-41d4-a716-446655440001',
    'Lote de Eletrônicos Premium',
    'Lote com produtos eletrônicos de alta qualidade, incluindo smartphones e notebooks.',
    CURRENT_TIMESTAMP + INTERVAL '7 days',
    'ACTIVE'
);

-- =====================================================
-- PRODUTOS DE EXEMPLO
-- =====================================================

-- iPhone 15 Pro Max
INSERT INTO tb_produto (
    id, seller_id, lote_id, title, description, images, weight, dimensions,
    initial_price, current_price, increment_min, end_datetime, status, moderated, categoria, tags
) VALUES (
    'aa0e8400-e29b-41d4-a716-446655440001',
    '770e8400-e29b-41d4-a716-446655440001',
    '990e8400-e29b-41d4-a716-446655440001',
    'iPhone 15 Pro Max 256GB Titânio Natural',
    'Smartphone Apple iPhone 15 Pro Max com 256GB de armazenamento, cor Titânio Natural, em perfeito estado de conservação. Acompanha caixa original, carregador e fones.',
    '["https://example.com/iphone1.jpg", "https://example.com/iphone2.jpg"]',
    0.221,
    '{"length": 15.99, "width": 7.69, "height": 0.83}',
    3500.00,
    4500.00,
    50.00,
    CURRENT_TIMESTAMP + INTERVAL '2 days 3 hours',
    'ACTIVE',
    TRUE,
    'Eletrônicos',
    'smartphone,apple,iphone,premium'
);

-- MacBook Air M2
INSERT INTO tb_produto (
    id, seller_id, lote_id, title, description, images, weight, dimensions,
    initial_price, current_price, increment_min, end_datetime, status, moderated, categoria, tags
) VALUES (
    'aa0e8400-e29b-41d4-a716-446655440002',
    '770e8400-e29b-41d4-a716-446655440001',
    '990e8400-e29b-41d4-a716-446655440001',
    'MacBook Air M2 2023 8GB 256GB',
    'Notebook Apple MacBook Air com chip M2, 8GB de RAM unificada, 256GB de SSD, tela Liquid Retina de 13 polegadas. Usado por apenas 6 meses, em excelente estado.',
    '["https://example.com/macbook1.jpg", "https://example.com/macbook2.jpg"]',
    1.24,
    '{"length": 30.41, "width": 21.24, "height": 1.13}',
    6000.00,
    7200.00,
    100.00,
    CURRENT_TIMESTAMP + INTERVAL '1 day 5 hours',
    'ACTIVE',
    TRUE,
    'Eletrônicos',
    'notebook,apple,macbook,laptop'
);

-- Produto futuro (ainda não iniciado)
INSERT INTO tb_produto (
    id, seller_id, title, description, images, weight, dimensions,
    initial_price, current_price, increment_min, end_datetime, status, moderated, categoria, tags
) VALUES (
    'aa0e8400-e29b-41d4-a716-446655440003',
    '770e8400-e29b-41d4-a716-446655440001',
    'Relógio Rolex Submariner Vintage',
    'Relógio Rolex Submariner modelo clássico dos anos 80, aço inoxidável, movimento automático. Acompanha certificado de autenticidade e caixa original.',
    '["https://example.com/rolex1.jpg", "https://example.com/rolex2.jpg"]',
    0.15,
    '{"diameter": 4.0, "thickness": 1.25}',
    35000.00,
    35000.00,
    500.00,
    CURRENT_TIMESTAMP + INTERVAL '5 days',
    'PENDING_APPROVAL',
    FALSE,
    'Relógios',
    'rolex,submariner,vintage,luxo'
);

-- =====================================================
-- LANCES DE EXEMPLO
-- =====================================================

-- Definir próximo valor da sequência
SELECT setval('tb_lance_sequence_number_seq', 1, false);

-- Lances no iPhone
INSERT INTO tb_lance (id, produto_id, user_id, value, timestamp, sequence_number, is_winning) 
VALUES (
    'bb0e8400-e29b-41d4-a716-446655440001',
    'aa0e8400-e29b-41d4-a716-446655440001',
    '550e8400-e29b-41d4-a716-446655440002',
    3550.00,
    CURRENT_TIMESTAMP - INTERVAL '2 hours',
    nextval('tb_lance_sequence_number_seq'),
    FALSE
);

INSERT INTO tb_lance (id, produto_id, user_id, value, timestamp, sequence_number, is_winning) 
VALUES (
    'bb0e8400-e29b-41d4-a716-446655440002',
    'aa0e8400-e29b-41d4-a716-446655440001',
    '550e8400-e29b-41d4-a716-446655440003',
    3600.00,
    CURRENT_TIMESTAMP - INTERVAL '1 hour 30 minutes',
    nextval('tb_lance_sequence_number_seq'),
    FALSE
);

INSERT INTO tb_lance (id, produto_id, user_id, value, timestamp, sequence_number, is_winning) 
VALUES (
    'bb0e8400-e29b-41d4-a716-446655440003',
    'aa0e8400-e29b-41d4-a716-446655440001',
    '550e8400-e29b-41d4-a716-446655440002',
    4500.00,
    CURRENT_TIMESTAMP - INTERVAL '15 minutes',
    nextval('tb_lance_sequence_number_seq'),
    TRUE
);

-- Lances no MacBook
INSERT INTO tb_lance (id, produto_id, user_id, value, timestamp, sequence_number, is_winning) 
VALUES (
    'bb0e8400-e29b-41d4-a716-446655440004',
    'aa0e8400-e29b-41d4-a716-446655440002',
    '550e8400-e29b-41d4-a716-446655440002',
    6100.00,
    CURRENT_TIMESTAMP - INTERVAL '3 hours',
    nextval('tb_lance_sequence_number_seq'),
    FALSE
);

INSERT INTO tb_lance (id, produto_id, user_id, value, timestamp, sequence_number, is_winning) 
VALUES (
    'bb0e8400-e29b-41d4-a716-446655440005',
    'aa0e8400-e29b-41d4-a716-446655440002',
    '550e8400-e29b-41d4-a716-446655440003',
    7200.00,
    CURRENT_TIMESTAMP - INTERVAL '45 minutes',
    nextval('tb_lance_sequence_number_seq'),
    TRUE
);

-- =====================================================
-- FAVORITOS DE EXEMPLO
-- =====================================================

INSERT INTO tb_favorito (user_id, produto_id) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440002',
    'aa0e8400-e29b-41d4-a716-446655440003'
);

INSERT INTO tb_favorito (user_id, produto_id) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440003',
    'aa0e8400-e29b-41d4-a716-446655440001'
);

-- =====================================================
-- NOTIFICAÇÕES DE EXEMPLO
-- =====================================================

INSERT INTO tb_notificacao (user_id, type, title, message, status, data) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440003',
    'BID_OUTBID',
    'Você foi superado!',
    'Seu lance no iPhone 15 Pro Max foi superado. O lance atual é R$ 4.500,00.',
    'SENT',
    '{"produto_id": "aa0e8400-e29b-41d4-a716-446655440001", "current_bid": 4500.00}'
);

INSERT INTO tb_notificacao (user_id, type, title, message, status, data) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440002',
    'AUCTION_ENDING',
    'Leilão terminando em breve!',
    'O leilão do MacBook Air M2 termina em 1 hora. Você está vencendo com R$ 7.200,00.',
    'SENT',
    '{"produto_id": "aa0e8400-e29b-41d4-a716-446655440002", "time_left": "1 hour"}'
);

-- =====================================================
-- AUDIT LOG DE EXEMPLO
-- =====================================================

INSERT INTO tb_audit_log (entity_type, entity_id, action, performed_by, old_values, new_values, metadata) 
VALUES (
    'PRODUTO',
    'aa0e8400-e29b-41d4-a716-446655440001',
    'PRICE_UPDATE',
    '550e8400-e29b-41d4-a716-446655440002',
    '{"current_price": 3600.00}',
    '{"current_price": 4500.00}',
    '{"bid_id": "bb0e8400-e29b-41d4-a716-446655440003", "increment": 900.00}'
);

INSERT INTO tb_audit_log (entity_type, entity_id, action, performed_by, metadata) 
VALUES (
    'USUARIO',
    '550e8400-e29b-41d4-a716-446655440002',
    'LOGIN',
    '550e8400-e29b-41d4-a716-446655440002',
    '{"ip_address": "192.168.1.100", "user_agent": "Mozilla/5.0..."}'
);

-- =====================================================
-- COMENTÁRIOS ADICIONAIS
-- =====================================================

COMMENT ON COLUMN tb_produto.anti_snipe_enabled IS 'Se habilitado, estende o tempo do leilão quando há lances nos últimos minutos';
COMMENT ON COLUMN tb_produto.anti_snipe_extension IS 'Tempo em segundos para estender o leilão quando anti-snipe é ativado';
COMMENT ON COLUMN tb_lance.proxy_bid IS 'Indica se é um lance automático (proxy bid)';
COMMENT ON COLUMN tb_lance.max_bid_value IS 'Valor máximo para lances automáticos';
COMMENT ON COLUMN tb_pre_autorizacao.payment_method_id IS 'ID do método de pagamento no gateway (Stripe, PagSeguro, etc.)';

-- =====================================================
-- FIM DO SCRIPT
-- =====================================================