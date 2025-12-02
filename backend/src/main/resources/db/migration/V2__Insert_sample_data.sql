-- =====================================================
-- Sistema de Leilão Eletrônico - Dados de Teste
-- Versão: 2.0 - Massa de dados para testes
-- Data: 2024-12-19
-- Descrição: Dados representativos para testes das funcionalidades
-- =====================================================

-- =====================================================
-- USUÁRIOS DE TESTE
-- =====================================================

-- Vendedor 1 - João Silva
INSERT INTO tb_usuario (id, nome, email, senha_hash, status, email_verificado, telefone) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440001',
    'João Silva',
    'joao.silva@leilao.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    TRUE,
    '(11) 99999-0001'
);

INSERT INTO tb_usuario_role (usuario_id, role) VALUES 
    ('550e8400-e29b-41d4-a716-446655440001', 'PARTICIPANT'),
    ('550e8400-e29b-41d4-a716-446655440001', 'SELLER');

-- Vendedor 2 - Maria Santos
INSERT INTO tb_usuario (id, nome, email, senha_hash, status, email_verificado, telefone) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440002',
    'Maria Santos',
    'maria.santos@leilao.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    TRUE,
    '(11) 99999-0002'
);

INSERT INTO tb_usuario_role (usuario_id, role) VALUES 
    ('550e8400-e29b-41d4-a716-446655440002', 'PARTICIPANT'),
    ('550e8400-e29b-41d4-a716-446655440002', 'SELLER');

-- Comprador 1 - Carlos Oliveira
INSERT INTO tb_usuario (id, nome, email, senha_hash, status, email_verificado, telefone) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440003',
    'Carlos Oliveira',
    'carlos.oliveira@leilao.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    TRUE,
    '(11) 99999-0003'
);

INSERT INTO tb_usuario_role (usuario_id, role) VALUES 
    ('550e8400-e29b-41d4-a716-446655440003', 'PARTICIPANT'),
    ('550e8400-e29b-41d4-a716-446655440003', 'BUYER');

-- Comprador 2 - Ana Costa
INSERT INTO tb_usuario (id, nome, email, senha_hash, status, email_verificado, telefone) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440004',
    'Ana Costa',
    'ana.costa@leilao.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    TRUE,
    '(11) 99999-0004'
);

INSERT INTO tb_usuario_role (usuario_id, role) VALUES 
    ('550e8400-e29b-41d4-a716-446655440004', 'PARTICIPANT'),
    ('550e8400-e29b-41d4-a716-446655440004', 'BUYER');

-- Usuário apenas participante
INSERT INTO tb_usuario (id, nome, email, senha_hash, status, email_verificado, telefone) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440005',
    'Pedro Almeida',
    'pedro.almeida@leilao.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    TRUE,
    '(11) 99999-0005'
);

INSERT INTO tb_usuario_role (usuario_id, role) VALUES 
    ('550e8400-e29b-41d4-a716-446655440005', 'PARTICIPANT');

-- =====================================================
-- CONTRATOS DE TESTE
-- =====================================================

-- Contrato para João Silva - Eletrônicos
INSERT INTO tb_contrato (
    id, seller_id, fee_rate, terms, valid_from, valid_to, active, categoria, status, 
    created_by, approved_by, approved_at
) VALUES (
    '660e8400-e29b-41d4-a716-446655440001',
    '550e8400-e29b-41d4-a716-446655440001',
    0.05, -- 5% de comissão
    'Contrato para venda de produtos eletrônicos. Taxa de 5% sobre o valor final de venda. Válido por 1 ano.',
    CURRENT_TIMESTAMP,
    DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 1 YEAR),
    TRUE,
    'Eletrônicos',
    'ACTIVE',
    '550e8400-e29b-41d4-a716-446655440000', -- admin
    '550e8400-e29b-41d4-a716-446655440000', -- admin
    CURRENT_TIMESTAMP
);

-- Contrato para Maria Santos - Antiguidades
INSERT INTO tb_contrato (
    id, seller_id, fee_rate, terms, valid_from, valid_to, active, categoria, status,
    created_by, approved_by, approved_at
) VALUES (
    '660e8400-e29b-41d4-a716-446655440002',
    '550e8400-e29b-41d4-a716-446655440002',
    0.08, -- 8% de comissão
    'Contrato para venda de antiguidades e objetos de arte. Taxa de 8% sobre o valor final de venda. Válido por 2 anos.',
    CURRENT_TIMESTAMP,
    DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 2 YEAR),
    TRUE,
    'Antiguidades',
    'ACTIVE',
    '550e8400-e29b-41d4-a716-446655440000', -- admin
    '550e8400-e29b-41d4-a716-446655440000', -- admin
    CURRENT_TIMESTAMP
);

-- Contrato geral para João Silva (sem categoria específica)
INSERT INTO tb_contrato (
    id, seller_id, fee_rate, terms, valid_from, valid_to, active, categoria, status,
    created_by, approved_by, approved_at
) VALUES (
    '660e8400-e29b-41d4-a716-446655440003',
    '550e8400-e29b-41d4-a716-446655440001',
    0.06, -- 6% de comissão
    'Contrato geral para venda de diversos produtos. Taxa de 6% sobre o valor final de venda.',
    CURRENT_TIMESTAMP,
    NULL, -- sem data de expiração
    TRUE,
    NULL, -- sem categoria específica
    'ACTIVE',
    '550e8400-e29b-41d4-a716-446655440000', -- admin
    '550e8400-e29b-41d4-a716-446655440000', -- admin
    CURRENT_TIMESTAMP
);

-- =====================================================
-- VENDEDORES DE TESTE
-- =====================================================

-- João Silva - Vendedor de Eletrônicos
INSERT INTO tb_vendedor (
    id, usuario_id, company_name, tax_id, contact_email, contact_phone, description,
    contract_id, fee_rate, active, verificado
) VALUES (
    '770e8400-e29b-41d4-a716-446655440001',
    '550e8400-e29b-41d4-a716-446655440001',
    'Silva Tech Ltda',
    '12.345.678/0001-90',
    'contato@silvatech.com',
    '(11) 3333-1111',
    'Especializada em produtos eletrônicos novos e seminovos. Mais de 10 anos de experiência no mercado.',
    '660e8400-e29b-41d4-a716-446655440001',
    0.05,
    TRUE,
    TRUE
);

-- Maria Santos - Vendedora de Antiguidades
INSERT INTO tb_vendedor (
    id, usuario_id, company_name, tax_id, contact_email, contact_phone, description,
    contract_id, fee_rate, active, verificado
) VALUES (
    '770e8400-e29b-41d4-a716-446655440002',
    '550e8400-e29b-41d4-a716-446655440002',
    'Santos Antiguidades ME',
    '98.765.432/0001-10',
    'maria@santosantiguidades.com',
    '(11) 3333-2222',
    'Loja especializada em antiguidades, objetos de arte e peças de coleção. Certificados de autenticidade garantidos.',
    '660e8400-e29b-41d4-a716-446655440002',
    0.08,
    TRUE,
    TRUE
);

-- =====================================================
-- COMPRADORES DE TESTE
-- =====================================================

-- Carlos Oliveira - Comprador Premium
INSERT INTO tb_comprador (id, usuario_id, kyc_status, limite_credito, preferences) 
VALUES (
    '880e8400-e29b-41d4-a716-446655440001',
    '550e8400-e29b-41d4-a716-446655440003',
    'APPROVED',
    100000.00,
    '{"categorias_interesse": ["Eletrônicos", "Informática"], "notificacoes_email": true, "notificacoes_sms": true}'
);

-- Ana Costa - Compradora Regular
INSERT INTO tb_comprador (id, usuario_id, kyc_status, limite_credito, preferences) 
VALUES (
    '880e8400-e29b-41d4-a716-446655440002',
    '550e8400-e29b-41d4-a716-446655440004',
    'APPROVED',
    50000.00,
    '{"categorias_interesse": ["Antiguidades", "Arte"], "notificacoes_email": true, "notificacoes_sms": false}'
);

-- =====================================================
-- LOTES DE TESTE
-- =====================================================

-- Lote de Eletrônicos - João Silva
INSERT INTO tb_lote (
    id, seller_id, contract_id, title, description, lote_end_datetime, status
) VALUES (
    '990e8400-e29b-41d4-a716-446655440001',
    '770e8400-e29b-41d4-a716-446655440001',
    '660e8400-e29b-41d4-a716-446655440001',
    'Lote Premium de Eletrônicos',
    'Lote com produtos eletrônicos de alta qualidade, incluindo smartphones, notebooks e acessórios. Todos os produtos com garantia e em perfeito estado.',
    DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 5 DAY),
    'ACTIVE'
);

-- Lote de Antiguidades - Maria Santos
INSERT INTO tb_lote (
    id, seller_id, contract_id, title, description, lote_end_datetime, status
) VALUES (
    '990e8400-e29b-41d4-a716-446655440002',
    '770e8400-e29b-41d4-a716-446655440002',
    '660e8400-e29b-41d4-a716-446655440002',
    'Coleção de Antiguidades Europeias',
    'Lote especial com peças antigas europeias dos séculos XVIII e XIX. Inclui móveis, objetos decorativos e peças de arte.',
    DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 7 DAY),
    'ACTIVE'
);

-- =====================================================
-- PRODUTOS DE TESTE
-- =====================================================

-- iPhone 15 Pro Max - No lote de eletrônicos
INSERT INTO tb_produto (
    id, seller_id, lote_id, title, description, images, weight, dimensions,
    initial_price, current_price, reserve_price, increment_min, end_datetime, 
    status, moderated, categoria, tags
) VALUES (
    'aa0e8400-e29b-41d4-a716-446655440001',
    '770e8400-e29b-41d4-a716-446655440001',
    '990e8400-e29b-41d4-a716-446655440001',
    'iPhone 15 Pro Max 256GB Titânio Natural',
    'Smartphone Apple iPhone 15 Pro Max com 256GB de armazenamento, cor Titânio Natural. Estado: Seminovo (9/10). Acompanha: caixa original, carregador USB-C, cabo, documentação. Sem riscos na tela, pequenos sinais de uso na traseira. Bateria com 98% de capacidade.',
    '["https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=400", "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=400"]',
    0.221,
    '{"length": 15.99, "width": 7.69, "height": 0.83}',
    3500.00,
    4200.00,
    4000.00,
    50.00,
    DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 2 DAY) + INTERVAL 14 HOUR,
    'ACTIVE',
    TRUE,
    'Eletrônicos',
    'smartphone,apple,iphone,premium,seminovo'
);

-- MacBook Air M2 - No lote de eletrônicos
INSERT INTO tb_produto (
    id, seller_id, lote_id, title, description, images, weight, dimensions,
    initial_price, current_price, reserve_price, increment_min, end_datetime,
    status, moderated, categoria, tags
) VALUES (
    'aa0e8400-e29b-41d4-a716-446655440002',
    '770e8400-e29b-41d4-a716-446655440001',
    '990e8400-e29b-41d4-a716-446655440001',
    'MacBook Air M2 2023 8GB 256GB Prata',
    'Notebook Apple MacBook Air com chip M2, 8GB de RAM unificada, 256GB de SSD, tela Liquid Retina de 13.6 polegadas, cor Prata. Estado: Usado (8/10). Usado por apenas 8 meses, em excelente estado de funcionamento. Acompanha carregador original MagSafe 3.',
    '["https://images.unsplash.com/photo-1541807084-5c52b6b3adef?w=400", "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=400"]',
    1.24,
    '{"length": 30.41, "width": 21.24, "height": 1.13}',
    6000.00,
    6800.00,
    6500.00,
    100.00,
    DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 3 DAY) + INTERVAL 10 HOUR,
    'ACTIVE',
    TRUE,
    'Eletrônicos',
    'notebook,apple,macbook,laptop,m2'
);

-- Relógio Vintage - No lote de antiguidades
INSERT INTO tb_produto (
    id, seller_id, lote_id, title, description, images, weight, dimensions,
    initial_price, current_price, reserve_price, increment_min, end_datetime,
    status, moderated, categoria, tags
) VALUES (
    'aa0e8400-e29b-41d4-a716-446655440003',
    '770e8400-e29b-41d4-a716-446655440002',
    '990e8400-e29b-41d4-a716-446655440002',
    'Relógio de Mesa Francês Séc. XIX',
    'Belíssimo relógio de mesa francês do século XIX, em bronze dourado com detalhes em mármore. Movimento mecânico original funcionando perfeitamente. Peça de coleção com certificado de autenticidade. Altura: 35cm. Assinado pelo relojoeiro parisiense Henri Leroy.',
    '["https://images.unsplash.com/photo-1594736797933-d0401ba2fe65?w=400", "https://images.unsplash.com/photo-1563861826100-9cb868fdbe1c?w=400"]',
    2.8,
    '{"height": 35, "width": 25, "depth": 15}',
    8000.00,
    8000.00,
    12000.00,
    200.00,
    DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 4 DAY) + INTERVAL 16 HOUR,
    'ACTIVE',
    TRUE,
    'Antiguidades',
    'relogio,antigo,frances,seculo-xix,bronze,colecionavel'
);

-- Produto individual (sem lote) - Câmera Vintage
INSERT INTO tb_produto (
    id, seller_id, lote_id, title, description, images, weight, dimensions,
    initial_price, current_price, reserve_price, increment_min, end_datetime,
    status, moderated, categoria, tags
) VALUES (
    'aa0e8400-e29b-41d4-a716-446655440004',
    '770e8400-e29b-41d4-a716-446655440002',
    NULL, -- produto avulso
    'Câmera Leica IIIf 1952 com Lente Summitar',
    'Câmera Leica IIIf de 1952 em excelente estado de conservação. Acompanha lente Summitar 50mm f/2. Mecanismo funcionando perfeitamente, sem fungos nas lentes. Peça de coleção para fotógrafos e colecionadores. Número de série: 525789.',
    '["https://images.unsplash.com/photo-1606983340126-99ab4feaa64a?w=400", "https://images.unsplash.com/photo-1502920917128-1aa500764cbd?w=400"]',
    0.6,
    '{"length": 13.2, "width": 7.7, "height": 3.2}',
    15000.00,
    15000.00,
    18000.00,
    500.00,
    DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 6 DAY) + INTERVAL 20 HOUR,
    'ACTIVE',
    TRUE,
    'Antiguidades',
    'camera,leica,vintage,1952,colecionavel,fotografia'
);

-- Produto em rascunho
INSERT INTO tb_produto (
    id, seller_id, lote_id, title, description, images, weight, dimensions,
    initial_price, current_price, increment_min, end_datetime,
    status, moderated, categoria, tags
) VALUES (
    'aa0e8400-e29b-41d4-a716-446655440005',
    '770e8400-e29b-41d4-a716-446655440001',
    NULL,
    'iPad Pro 12.9" M2 2022 128GB',
    'Tablet Apple iPad Pro de 12.9 polegadas com chip M2, 128GB de armazenamento, Wi-Fi. Cor Cinza Espacial. Estado: Novo na caixa, lacrado.',
    '["https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=400"]',
    0.682,
    '{"length": 28.06, "width": 21.49, "height": 0.64}',
    4500.00,
    4500.00,
    100.00,
    DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 10 DAY),
    'DRAFT',
    FALSE,
    'Eletrônicos',
    'tablet,apple,ipad,pro,m2,novo'
);

-- =====================================================
-- LANCES DE TESTE
-- =====================================================

-- Lances no iPhone 15 Pro Max
INSERT INTO tb_lance (id, produto_id, user_id, value, timestamp, proxy_bid, is_winning) 
VALUES (
    'bb0e8400-e29b-41d4-a716-446655440001',
    'aa0e8400-e29b-41d4-a716-446655440001',
    '550e8400-e29b-41d4-a716-446655440003', -- Carlos
    3550.00,
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 HOUR),
    FALSE,
    FALSE
);

INSERT INTO tb_lance (id, produto_id, user_id, value, timestamp, proxy_bid, is_winning) 
VALUES (
    'bb0e8400-e29b-41d4-a716-446655440002',
    'aa0e8400-e29b-41d4-a716-446655440001',
    '550e8400-e29b-41d4-a716-446655440004', -- Ana
    3800.00,
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 HOUR),
    FALSE,
    FALSE
);

INSERT INTO tb_lance (id, produto_id, user_id, value, timestamp, proxy_bid, is_winning) 
VALUES (
    'bb0e8400-e29b-41d4-a716-446655440003',
    'aa0e8400-e29b-41d4-a716-446655440001',
    '550e8400-e29b-41d4-a716-446655440003', -- Carlos
    4200.00,
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 MINUTE),
    FALSE,
    TRUE
);

-- Lances no MacBook Air M2
INSERT INTO tb_lance (id, produto_id, user_id, value, timestamp, proxy_bid, is_winning) 
VALUES (
    'bb0e8400-e29b-41d4-a716-446655440004',
    'aa0e8400-e29b-41d4-a716-446655440002',
    '550e8400-e29b-41d4-a716-446655440004', -- Ana
    6100.00,
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 HOUR),
    FALSE,
    FALSE
);

INSERT INTO tb_lance (id, produto_id, user_id, value, timestamp, proxy_bid, is_winning) 
VALUES (
    'bb0e8400-e29b-41d4-a716-446655440005',
    'aa0e8400-e29b-41d4-a716-446655440002',
    '550e8400-e29b-41d4-a716-446655440003', -- Carlos
    6800.00,
    DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 HOUR),
    FALSE,
    TRUE
);

-- =====================================================
-- FAVORITOS DE TESTE
-- =====================================================

-- Carlos favoritou a câmera Leica
INSERT INTO tb_favorito (id, user_id, produto_id) 
VALUES (
    'ff0e8400-e29b-41d4-a716-446655440001',
    '550e8400-e29b-41d4-a716-446655440003',
    'aa0e8400-e29b-41d4-a716-446655440004'
);

-- Ana favoritou o relógio francês
INSERT INTO tb_favorito (id, user_id, produto_id) 
VALUES (
    'ff0e8400-e29b-41d4-a716-446655440002',
    '550e8400-e29b-41d4-a716-446655440004',
    'aa0e8400-e29b-41d4-a716-446655440003'
);

-- Pedro favoritou o iPhone
INSERT INTO tb_favorito (id, user_id, produto_id) 
VALUES (
    'ff0e8400-e29b-41d4-a716-446655440003',
    '550e8400-e29b-41d4-a716-446655440005',
    'aa0e8400-e29b-41d4-a716-446655440001'
);

-- =====================================================
-- NOTIFICAÇÕES DE TESTE
-- =====================================================

-- Notificação para Ana - foi superada no iPhone
INSERT INTO tb_notificacao (id, user_id, type, title, message, status, data) 
VALUES (
    'nn0e8400-e29b-41d4-a716-446655440001',
    '550e8400-e29b-41d4-a716-446655440004',
    'BID_OUTBID',
    'Você foi superada!',
    'Seu lance no iPhone 15 Pro Max foi superado. O lance atual é R$ 4.200,00.',
    'SENT',
    '{"produto_id": "aa0e8400-e29b-41d4-a716-446655440001", "current_bid": 4200.00, "your_bid": 3800.00}'
);

-- Notificação para Carlos - leilão terminando
INSERT INTO tb_notificacao (id, user_id, type, title, message, status, data) 
VALUES (
    'nn0e8400-e29b-41d4-a716-446655440002',
    '550e8400-e29b-41d4-a716-446655440003',
    'AUCTION_ENDING',
    'Leilão terminando em breve!',
    'O leilão do MacBook Air M2 termina em menos de 2 horas. Você está vencendo com R$ 6.800,00.',
    'SENT',
    '{"produto_id": "aa0e8400-e29b-41d4-a716-446655440002", "time_left_hours": 2, "current_bid": 6800.00}'
);

-- Notificação para Ana - novo produto favorito
INSERT INTO tb_notificacao (id, user_id, type, title, message, status, data) 
VALUES (
    'nn0e8400-e29b-41d4-a716-446655440003',
    '550e8400-e29b-41d4-a716-446655440004',
    'FAVORITE_STARTING',
    'Produto favorito iniciando!',
    'O leilão do Relógio de Mesa Francês Séc. XIX que você favoritou está ativo. Não perca!',
    'PENDING',
    '{"produto_id": "aa0e8400-e29b-41d4-a716-446655440003", "start_price": 8000.00}'
);

-- =====================================================
-- AUDIT LOG DE TESTE
-- =====================================================

-- Log de criação de produto
INSERT INTO tb_audit_log (id, entity_type, entity_id, action, performed_by, new_values, metadata) 
VALUES (
    'al0e8400-e29b-41d4-a716-446655440001',
    'PRODUTO',
    'aa0e8400-e29b-41d4-a716-446655440001',
    'CREATE',
    '550e8400-e29b-41d4-a716-446655440001',
    '{"title": "iPhone 15 Pro Max 256GB Titânio Natural", "initial_price": 3500.00, "status": "DRAFT"}',
    '{"ip_address": "192.168.1.100", "user_agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"}'
);

-- Log de lance
INSERT INTO tb_audit_log (id, entity_type, entity_id, action, performed_by, new_values, metadata) 
VALUES (
    'al0e8400-e29b-41d4-a716-446655440002',
    'LANCE',
    'bb0e8400-e29b-41d4-a716-446655440003',
    'CREATE',
    '550e8400-e29b-41d4-a716-446655440003',
    '{"produto_id": "aa0e8400-e29b-41d4-a716-446655440001", "value": 4200.00, "is_winning": true}',
    '{"ip_address": "192.168.1.101", "bid_increment": 400.00}'
);

-- Log de login
INSERT INTO tb_audit_log (id, entity_type, entity_id, action, performed_by, metadata) 
VALUES (
    'al0e8400-e29b-41d4-a716-446655440003',
    'USUARIO',
    '550e8400-e29b-41d4-a716-446655440003',
    'LOGIN',
    '550e8400-e29b-41d4-a716-446655440003',
    '{"ip_address": "192.168.1.101", "user_agent": "Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X)", "login_method": "email"}'
);

-- =====================================================
-- FIM DO SCRIPT V2
-- =====================================================