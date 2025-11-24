-- =====================================================
-- Sistema de Leilão Eletrônico - Dados de Exemplo
-- Versão: 2.0
-- Data: 2024-11-24
-- =====================================================

-- =====================================================
-- USUÁRIOS DE EXEMPLO
-- =====================================================

-- Usuário vendedor
INSERT INTO usuarios (id, nome, email, senha_hash, status, roles, email_verificado, telefone) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440001',
    'João Silva Vendedor',
    'vendedor@leilao.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    ARRAY['PARTICIPANT'::user_role, 'SELLER'::user_role],
    TRUE,
    '(11) 99999-0001'
);

-- Usuário comprador
INSERT INTO usuarios (id, nome, email, senha_hash, status, roles, email_verificado, telefone) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440002',
    'Maria Santos Compradora',
    'comprador@leilao.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    ARRAY['PARTICIPANT'::user_role, 'BUYER'::user_role],
    TRUE,
    '(11) 99999-0002'
);

-- Usuário participante
INSERT INTO usuarios (id, nome, email, senha_hash, status, roles, email_verificado, telefone) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440003',
    'Carlos Oliveira Participante',
    'participante@leilao.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    ARRAY['PARTICIPANT'::user_role],
    TRUE,
    '(11) 99999-0003'
);

-- =====================================================
-- CONTRATOS DE EXEMPLO
-- =====================================================

INSERT INTO contratos (id, seller_id, fee_rate, terms, valid_from, active) 
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

INSERT INTO vendedores (id, usuario_id, company_name, tax_id, contract_id, fee_rate, verificado) 
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

INSERT INTO compradores (id, usuario_id, kyc_status, limite_credito) 
VALUES (
    '880e8400-e29b-41d4-a716-446655440001',
    '550e8400-e29b-41d4-a716-446655440002',
    'APPROVED',
    50000.00
);

INSERT INTO compradores (id, usuario_id, kyc_status, limite_credito) 
VALUES (
    '880e8400-e29b-41d4-a716-446655440002',
    '550e8400-e29b-41d4-a716-446655440003',
    'APPROVED',
    25000.00
);

-- =====================================================
-- LOTES DE EXEMPLO
-- =====================================================

INSERT INTO lotes (id, seller_id, title, description, lote_end_datetime, status) 
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
INSERT INTO produtos (
    id, seller_id, lote_id, title, description, images, weight, dimensions,
    initial_price, current_price, increment_min, end_datetime, status, moderated, categoria, tags
) VALUES (
    'aa0e8400-e29b-41d4-a716-446655440001',
    '770e8400-e29b-41d4-a716-446655440001',
    '990e8400-e29b-41d4-a716-446655440001',
    'iPhone 15 Pro Max 256GB Titânio Natural',
    'Smartphone Apple iPhone 15 Pro Max com 256GB de armazenamento, cor Titânio Natural, em perfeito estado de conservação. Acompanha caixa original, carregador e fones.',
    '["https://example.com/iphone1.jpg", "https://example.com/iphone2.jpg"]'::jsonb,
    0.221,
    '{"length": 15.99, "width": 7.69, "height": 0.83}'::jsonb,
    3500.00,
    4500.00,
    50.00,
    CURRENT_TIMESTAMP + INTERVAL '2 days 3 hours',
    'ACTIVE',
    TRUE,
    'Eletrônicos',
    ARRAY['smartphone', 'apple', 'iphone', 'premium']
);

-- MacBook Air M2
INSERT INTO produtos (
    id, seller_id, lote_id, title, description, images, weight, dimensions,
    initial_price, current_price, increment_min, end_datetime, status, moderated, categoria, tags
) VALUES (
    'aa0e8400-e29b-41d4-a716-446655440002',
    '770e8400-e29b-41d4-a716-446655440001',
    '990e8400-e29b-41d4-a716-446655440001',
    'MacBook Air M2 2023 8GB 256GB',
    'Notebook Apple MacBook Air com chip M2, 8GB de RAM unificada, 256GB de SSD, tela Liquid Retina de 13 polegadas. Usado por apenas 6 meses, em excelente estado.',
    '["https://example.com/macbook1.jpg", "https://example.com/macbook2.jpg"]'::jsonb,
    1.24,
    '{"length": 30.41, "width": 21.24, "height": 1.13}'::jsonb,
    6000.00,
    7200.00,
    100.00,
    CURRENT_TIMESTAMP + INTERVAL '1 day 5 hours',
    'ACTIVE',
    TRUE,
    'Eletrônicos',
    ARRAY['notebook', 'apple', 'macbook', 'laptop']
);

-- Produto futuro (ainda não iniciado)
INSERT INTO produtos (
    id, seller_id, title, description, images, weight, dimensions,
    initial_price, current_price, increment_min, end_datetime, status, moderated, categoria, tags
) VALUES (
    'aa0e8400-e29b-41d4-a716-446655440003',
    '770e8400-e29b-41d4-a716-446655440001',
    'Relógio Rolex Submariner Vintage',
    'Relógio Rolex Submariner modelo clássico dos anos 80, aço inoxidável, movimento automático. Acompanha certificado de autenticidade e caixa original.',
    '["https://example.com/rolex1.jpg", "https://example.com/rolex2.jpg"]'::jsonb,
    0.15,
    '{"diameter": 4.0, "thickness": 1.25}'::jsonb,
    35000.00,
    35000.00,
    500.00,
    CURRENT_TIMESTAMP + INTERVAL '5 days',
    'PENDING_APPROVAL',
    FALSE,
    'Relógios',
    ARRAY['rolex', 'submariner', 'vintage', 'luxo']
);

-- =====================================================
-- LANCES DE EXEMPLO
-- =====================================================

-- Lances no iPhone
INSERT INTO lances (id, produto_id, user_id, value, timestamp, sequence_number, is_winning) 
VALUES (
    'bb0e8400-e29b-41d4-a716-446655440001',
    'aa0e8400-e29b-41d4-a716-446655440001',
    '550e8400-e29b-41d4-a716-446655440002',
    3550.00,
    CURRENT_TIMESTAMP - INTERVAL '2 hours',
    1,
    FALSE
);

INSERT INTO lances (id, produto_id, user_id, value, timestamp, sequence_number, is_winning) 
VALUES (
    'bb0e8400-e29b-41d4-a716-446655440002',
    'aa0e8400-e29b-41d4-a716-446655440001',
    '550e8400-e29b-41d4-a716-446655440003',
    3600.00,
    CURRENT_TIMESTAMP - INTERVAL '1 hour 30 minutes',
    2,
    FALSE
);

INSERT INTO lances (id, produto_id, user_id, value, timestamp, sequence_number, is_winning) 
VALUES (
    'bb0e8400-e29b-41d4-a716-446655440003',
    'aa0e8400-e29b-41d4-a716-446655440001',
    '550e8400-e29b-41d4-a716-446655440002',
    4500.00,
    CURRENT_TIMESTAMP - INTERVAL '15 minutes',
    3,
    TRUE
);

-- Lances no MacBook
INSERT INTO lances (id, produto_id, user_id, value, timestamp, sequence_number, is_winning) 
VALUES (
    'bb0e8400-e29b-41d4-a716-446655440004',
    'aa0e8400-e29b-41d4-a716-446655440002',
    '550e8400-e29b-41d4-a716-446655440002',
    6100.00,
    CURRENT_TIMESTAMP - INTERVAL '3 hours',
    1,
    FALSE
);

INSERT INTO lances (id, produto_id, user_id, value, timestamp, sequence_number, is_winning) 
VALUES (
    'bb0e8400-e29b-41d4-a716-446655440005',
    'aa0e8400-e29b-41d4-a716-446655440002',
    '550e8400-e29b-41d4-a716-446655440003',
    7200.00,
    CURRENT_TIMESTAMP - INTERVAL '45 minutes',
    2,
    TRUE
);

-- =====================================================
-- FAVORITOS DE EXEMPLO
-- =====================================================

INSERT INTO favoritos (user_id, produto_id) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440002',
    'aa0e8400-e29b-41d4-a716-446655440003'
);

INSERT INTO favoritos (user_id, produto_id) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440003',
    'aa0e8400-e29b-41d4-a716-446655440001'
);

-- =====================================================
-- NOTIFICAÇÕES DE EXEMPLO
-- =====================================================

INSERT INTO notificacoes (user_id, type, title, message, status, data) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440003',
    'BID_OUTBID',
    'Você foi superado!',
    'Seu lance no iPhone 15 Pro Max foi superado. O lance atual é R$ 4.500,00.',
    'SENT',
    '{"produto_id": "aa0e8400-e29b-41d4-a716-446655440001", "current_bid": 4500.00}'::jsonb
);

INSERT INTO notificacoes (user_id, type, title, message, status, data) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440002',
    'AUCTION_ENDING',
    'Leilão terminando em breve!',
    'O leilão do MacBook Air M2 termina em 1 hora. Você está vencendo com R$ 7.200,00.',
    'SENT',
    '{"produto_id": "aa0e8400-e29b-41d4-a716-446655440002", "time_left": "1 hour"}'::jsonb
);

-- =====================================================
-- AUDIT LOG DE EXEMPLO
-- =====================================================

INSERT INTO audit_log (entity_type, entity_id, action, performed_by, old_values, new_values, metadata) 
VALUES (
    'PRODUTO',
    'aa0e8400-e29b-41d4-a716-446655440001',
    'PRICE_UPDATE',
    '550e8400-e29b-41d4-a716-446655440002',
    '{"current_price": 3600.00}'::jsonb,
    '{"current_price": 4500.00}'::jsonb,
    '{"bid_id": "bb0e8400-e29b-41d4-a716-446655440003", "increment": 900.00}'::jsonb
);

INSERT INTO audit_log (entity_type, entity_id, action, performed_by, metadata) 
VALUES (
    'USUARIO',
    '550e8400-e29b-41d4-a716-446655440002',
    'LOGIN',
    '550e8400-e29b-41d4-a716-446655440002',
    '{"ip_address": "192.168.1.100", "user_agent": "Mozilla/5.0..."}'::jsonb
);

-- =====================================================
-- ATUALIZAR SEQUÊNCIAS
-- =====================================================

-- Atualizar a sequência de lances para continuar a partir do último ID usado
SELECT setval('lances_sequence_number_seq', (SELECT COALESCE(MAX(sequence_number), 0) FROM lances));

-- =====================================================
-- COMENTÁRIOS ADICIONAIS
-- =====================================================

COMMENT ON COLUMN produtos.anti_snipe_enabled IS 'Se habilitado, estende o tempo do leilão quando há lances nos últimos minutos';
COMMENT ON COLUMN produtos.anti_snipe_extension IS 'Tempo em segundos para estender o leilão quando anti-snipe é ativado';
COMMENT ON COLUMN lances.proxy_bid IS 'Indica se é um lance automático (proxy bid)';
COMMENT ON COLUMN lances.max_bid_value IS 'Valor máximo para lances automáticos';
COMMENT ON COLUMN pre_autorizacoes.payment_method_id IS 'ID do método de pagamento no gateway (Stripe, PagSeguro, etc.)';

-- =====================================================
-- FIM DO SCRIPT
-- =====================================================