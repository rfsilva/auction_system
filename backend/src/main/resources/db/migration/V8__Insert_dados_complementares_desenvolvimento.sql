-- =====================================================
-- Sistema de Leilão Eletrônico - Massa de Dados V8
-- Entidade: DADOS COMPLEMENTARES
-- Versão: 8.0 - Lances, Favoritos, Notificações e Auditoria
-- Data: 2024-12-19
-- Descrição: Dados complementares para simular atividade real da plataforma
-- =====================================================

-- =====================================================
-- LANCES NOS PRODUTOS ATIVOS
-- =====================================================

-- Lances no Samsung Galaxy S23 Ultra (produto mais disputado)
INSERT INTO tb_lance (id, produto_id, user_id, value, timestamp, ip_address, user_agent, proxy_bid, is_winning) VALUES
('bb1e8400-e29b-41d4-a716-446655440001', '990e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440020', 2850.00, DATE_SUB(NOW(), INTERVAL 4 DAY), '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', FALSE, FALSE),
('bb1e8400-e29b-41d4-a716-446655440002', '990e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440021', 2900.00, DATE_SUB(NOW(), INTERVAL 4 DAY), '192.168.1.101', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)', FALSE, FALSE),
('bb1e8400-e29b-41d4-a716-446655440003', '990e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440023', 3000.00, DATE_SUB(NOW(), INTERVAL 3 DAY), '192.168.1.102', 'Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X)', FALSE, FALSE),
('bb1e8400-e29b-41d4-a716-446655440004', '990e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440020', 3100.00, DATE_SUB(NOW(), INTERVAL 2 DAY), '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', FALSE, FALSE),
('bb1e8400-e29b-41d4-a716-446655440005', '990e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440040', 3250.00, DATE_SUB(NOW(), INTERVAL 1 DAY), '192.168.1.103', 'Mozilla/5.0 (Android 13; Mobile)', FALSE, TRUE);

-- Lances no Vaso de Cerâmica
INSERT INTO tb_lance (id, produto_id, user_id, value, timestamp, ip_address, user_agent, proxy_bid, is_winning) VALUES
('bb1e8400-e29b-41d4-a716-446655440006', '990e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440021', 85.00, DATE_SUB(NOW(), INTERVAL 2 DAY), '192.168.1.101', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)', FALSE, FALSE),
('bb1e8400-e29b-41d4-a716-446655440007', '990e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440030', 95.00, DATE_SUB(NOW(), INTERVAL 1 DAY), '192.168.1.104', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)', FALSE, FALSE),
('bb1e8400-e29b-41d4-a716-446655440008', '990e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440031', 125.00, DATE_SUB(NOW(), INTERVAL 12 HOUR), '192.168.1.105', 'Mozilla/5.0 (Linux; Android 12)', FALSE, TRUE);

-- Lances no Dom Casmurro (livro raro)
INSERT INTO tb_lance (id, produto_id, user_id, value, timestamp, ip_address, user_agent, proxy_bid, max_bid_value, is_winning) VALUES
('bb1e8400-e29b-41d4-a716-446655440009', '990e8400-e29b-41d4-a716-446655440006', '550e8400-e29b-41d4-a716-446655440023', 2600.00, DATE_SUB(NOW(), INTERVAL 3 DAY), '192.168.1.102', 'Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X)', FALSE, NULL, FALSE),
('bb1e8400-e29b-41d4-a716-446655440010', '990e8400-e29b-41d4-a716-446655440006', '550e8400-e29b-41d4-a716-446655440020', 3000.00, DATE_SUB(NOW(), INTERVAL 2 DAY), '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)', TRUE, 4000.00, FALSE),
('bb1e8400-e29b-41d4-a716-446655440011', '990e8400-e29b-41d4-a716-446655440006', '550e8400-e29b-41d4-a716-446655440023', 3200.00, DATE_SUB(NOW(), INTERVAL 1 DAY), '192.168.1.102', 'Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X)', FALSE, NULL, FALSE),
('bb1e8400-e29b-41d4-a716-446655440012', '990e8400-e29b-41d4-a716-446655440006', '550e8400-e29b-41d4-a716-446655440020', 3300.00, DATE_SUB(NOW(), INTERVAL 18 HOUR), '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)', TRUE, 4000.00, FALSE),
('bb1e8400-e29b-41d4-a716-446655440013', '990e8400-e29b-41d4-a716-446655440006', '550e8400-e29b-41d4-a716-446655440023', 3800.00, DATE_SUB(NOW(), INTERVAL 6 HOUR), '192.168.1.102', 'Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X)', FALSE, NULL, TRUE);

-- Lances na Fender Stratocaster (item mais valioso)
INSERT INTO tb_lance (id, produto_id, user_id, value, timestamp, ip_address, user_agent, proxy_bid, is_winning) VALUES
('bb1e8400-e29b-41d4-a716-446655440014', '990e8400-e29b-41d4-a716-446655440008', '550e8400-e29b-41d4-a716-446655440023', 8500.00, DATE_SUB(NOW(), INTERVAL 6 DAY), '192.168.1.102', 'Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X)', FALSE, FALSE),
('bb1e8400-e29b-41d4-a716-446655440015', '990e8400-e29b-41d4-a716-446655440008', '550e8400-e29b-41d4-a716-446655440020', 9000.00, DATE_SUB(NOW(), INTERVAL 5 DAY), '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)', FALSE, FALSE),
('bb1e8400-e29b-41d4-a716-446655440016', '990e8400-e29b-41d4-a716-446655440008', '550e8400-e29b-41d4-a716-446655440030', 10000.00, DATE_SUB(NOW(), INTERVAL 4 DAY), '192.168.1.104', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)', FALSE, FALSE),
('bb1e8400-e29b-41d4-a716-446655440017', '990e8400-e29b-41d4-a716-446655440008', '550e8400-e29b-41d4-a716-446655440023', 11000.00, DATE_SUB(NOW(), INTERVAL 2 DAY), '192.168.1.102', 'Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X)', FALSE, FALSE),
('bb1e8400-e29b-41d4-a716-446655440018', '990e8400-e29b-41d4-a716-446655440008', '550e8400-e29b-41d4-a716-446655440020', 12500.00, DATE_SUB(NOW(), INTERVAL 1 DAY), '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)', FALSE, TRUE);

-- Lances no iPhone 13 Pro
INSERT INTO tb_lance (id, produto_id, user_id, value, timestamp, ip_address, user_agent, proxy_bid, is_winning) VALUES
('bb1e8400-e29b-41d4-a716-446655440019', '990e8400-e29b-41d4-a716-446655440009', '550e8400-e29b-41d4-a716-446655440021', 2250.00, DATE_SUB(NOW(), INTERVAL 1 DAY), '192.168.1.101', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)', FALSE, FALSE),
('bb1e8400-e29b-41d4-a716-446655440020', '990e8400-e29b-41d4-a716-446655440009', '550e8400-e29b-41d4-a716-446655440031', 2350.00, DATE_SUB(NOW(), INTERVAL 18 HOUR), '192.168.1.105', 'Mozilla/5.0 (Linux; Android 12)', FALSE, FALSE),
('bb1e8400-e29b-41d4-a716-446655440021', '990e8400-e29b-41d4-a716-446655440009', '550e8400-e29b-41d4-a716-446655440021', 2450.00, DATE_SUB(NOW(), INTERVAL 8 HOUR), '192.168.1.101', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)', FALSE, TRUE);

-- =====================================================
-- PRODUTOS FAVORITOS
-- =====================================================

-- Favoritos da Fernanda (compradora VIP)
INSERT INTO tb_favorito (id, user_id, produto_id, created_at) VALUES
('cc1e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440020', '990e8400-e29b-41d4-a716-446655440001', DATE_SUB(NOW(), INTERVAL 5 DAY)),
('cc1e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440020', '990e8400-e29b-41d4-a716-446655440008', DATE_SUB(NOW(), INTERVAL 7 DAY)),
('cc1e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440020', '990e8400-e29b-41d4-a716-446655440004', DATE_SUB(NOW(), INTERVAL 3 DAY));

-- Favoritos do Ricardo (colecionador)
INSERT INTO tb_favorito (id, user_id, produto_id, created_at) VALUES
('cc1e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440023', '990e8400-e29b-41d4-a716-446655440006', DATE_SUB(NOW(), INTERVAL 4 DAY)),
('cc1e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440023', '990e8400-e29b-41d4-a716-446655440007', DATE_SUB(NOW(), INTERVAL 6 DAY)),
('cc1e8400-e29b-41d4-a716-446655440006', '550e8400-e29b-41d4-a716-446655440023', '990e8400-e29b-41d4-a716-446655440008', DATE_SUB(NOW(), INTERVAL 8 DAY));

-- Favoritos do Marcos (comprador regular)
INSERT INTO tb_favorito (id, user_id, produto_id, created_at) VALUES
('cc1e8400-e29b-41d4-a716-446655440007', '550e8400-e29b-41d4-a716-446655440021', '990e8400-e29b-41d4-a716-446655440001', DATE_SUB(NOW(), INTERVAL 2 DAY)),
('cc1e8400-e29b-41d4-a716-446655440008', '550e8400-e29b-41d4-a716-446655440021', '990e8400-e29b-41d4-a716-446655440009', DATE_SUB(NOW(), INTERVAL 1 DAY));

-- Favoritos do Gabriel (estudante)
INSERT INTO tb_favorito (id, user_id, produto_id, created_at) VALUES
('cc1e8400-e29b-41d4-a716-446655440009', '550e8400-e29b-41d4-a716-446655440031', '990e8400-e29b-41d4-a716-446655440006', DATE_SUB(NOW(), INTERVAL 3 DAY)),
('cc1e8400-e29b-41d4-a716-446655440010', '550e8400-e29b-41d4-a716-446655440031', '990e8400-e29b-41d4-a716-446655440007', DATE_SUB(NOW(), INTERVAL 5 DAY));

-- =====================================================
-- NOTIFICAÇÕES
-- =====================================================

-- Notificações de lance superado
INSERT INTO tb_notificacao (id, user_id, type, title, message, status, data, created_at, sent_at) VALUES
('dd1e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440020', 'BID_OUTBID', 'Seu lance foi superado!', 'Seu lance de R$ 3.100,00 no produto "Smartphone Samsung Galaxy S23 Ultra" foi superado. O lance atual é R$ 3.250,00.', 'SENT', '{"produto_id": "990e8400-e29b-41d4-a716-446655440001", "lance_anterior": 3100.00, "lance_atual": 3250.00}', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 23 HOUR)),

('dd1e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440021', 'BID_OUTBID', 'Seu lance foi superado!', 'Seu lance de R$ 2.350,00 no produto "iPhone 13 Pro 128GB" foi superado. O lance atual é R$ 2.450,00.', 'SENT', '{"produto_id": "990e8400-e29b-41d4-a716-446655440009", "lance_anterior": 2350.00, "lance_atual": 2450.00}', DATE_SUB(NOW(), INTERVAL 8 HOUR), DATE_SUB(NOW(), INTERVAL 7 HOUR)),

('dd1e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440023', 'BID_OUTBID', 'Seu lance foi superado!', 'Seu lance de R$ 11.000,00 no produto "Guitarra Fender Stratocaster 1978" foi superado. O lance atual é R$ 12.500,00.', 'READ', '{"produto_id": "990e8400-e29b-41d4-a716-446655440008", "lance_anterior": 11000.00, "lance_atual": 12500.00}', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 23 HOUR));

-- Notificações de leilão terminando
INSERT INTO tb_notificacao (id, user_id, type, title, message, status, data, created_at, sent_at) VALUES
('dd1e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440020', 'AUCTION_ENDING', 'Leilão terminando em breve!', 'O leilão do produto "Smartphone Samsung Galaxy S23 Ultra" termina em 24 horas. Você está vencendo com R$ 3.250,00!', 'SENT', '{"produto_id": "990e8400-e29b-41d4-a716-446655440001", "tempo_restante": "24 horas", "posicao": "vencendo"}', DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 1 HOUR)),

('dd1e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440021', 'AUCTION_ENDING', 'Leilão terminando em breve!', 'O leilão do produto "iPhone 13 Pro 128GB" termina em 6 horas. Você está vencendo com R$ 2.450,00!', 'PENDING', '{"produto_id": "990e8400-e29b-41d4-a716-446655440009", "tempo_restante": "6 horas", "posicao": "vencendo"}', NOW(), NULL);

-- Notificações de novos produtos favoritos
INSERT INTO tb_notificacao (id, user_id, type, title, message, status, data, created_at, sent_at) VALUES
('dd1e8400-e29b-41d4-a716-446655440006', '550e8400-e29b-41d4-a716-446655440023', 'NEW_PRODUCT_CATEGORY', 'Novo produto na sua categoria favorita!', 'Um novo produto foi adicionado na categoria "Livros": Dom Casmurro - Machado de Assis - 1ª Edição 1899', 'SENT', '{"produto_id": "990e8400-e29b-41d4-a716-446655440006", "categoria": "Livros"}', DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY)),

('dd1e8400-e29b-41d4-a716-446655440007', '550e8400-e29b-41d4-a716-446655440020', 'NEW_PRODUCT_CATEGORY', 'Novo produto na sua categoria favorita!', 'Um novo produto foi adicionado na categoria "Eletrônicos": Smartphone Samsung Galaxy S23 Ultra 256GB', 'READ', '{"produto_id": "990e8400-e29b-41d4-a716-446655440001", "categoria": "Eletrônicos"}', DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY));

-- Notificações de sistema
INSERT INTO tb_notificacao (id, user_id, type, title, message, status, data, created_at) VALUES
('dd1e8400-e29b-41d4-a716-446655440008', '550e8400-e29b-41d4-a716-446655440022', 'ACCOUNT_VERIFICATION', 'Verificação de conta pendente', 'Sua conta está aguardando verificação. Por favor, envie os documentos necessários para completar o processo.', 'PENDING', '{"tipo_verificacao": "KYC", "documentos_pendentes": ["RG", "CPF", "Comprovante de residência"]}', DATE_SUB(NOW(), INTERVAL 7 DAY)),

('dd1e8400-e29b-41d4-a716-446655440009', '550e8400-e29b-41d4-a716-446655440012', 'SELLER_VERIFICATION', 'Verificação de vendedor aprovada!', 'Parabéns! Sua verificação como vendedor foi aprovada. Agora você pode criar e publicar produtos.', 'SENT', '{"status_anterior": "pendente", "status_atual": "aprovado"}', DATE_SUB(NOW(), INTERVAL 10 DAY));

-- =====================================================
-- AUDITORIA (LOGS DE AÇÕES IMPORTANTES)
-- =====================================================

-- Logs de criação de contratos
INSERT INTO tb_audit_log (id, entity_type, entity_id, action, performed_by, timestamp, ip_address, user_agent, new_values, metadata) VALUES
('ee1e8400-e29b-41d4-a716-446655440001', 'CONTRATO', '770e8400-e29b-41d4-a716-446655440010', 'CREATE', '550e8400-e29b-41d4-a716-446655440000', DATE_SUB(NOW(), INTERVAL 180 DAY), '10.0.0.1', 'Mozilla/5.0 (Admin Panel)', '{"seller_id": "660e8400-e29b-41d4-a716-446655440010", "fee_rate": 0.035, "status": "DRAFT"}', '{"motivo": "Novo contrato para ABC Importadora"}'),

('ee1e8400-e29b-41d4-a716-446655440002', 'CONTRATO', '770e8400-e29b-41d4-a716-446655440010', 'APPROVE', '550e8400-e29b-41d4-a716-446655440001', DATE_SUB(NOW(), INTERVAL 179 DAY), '10.0.0.2', 'Mozilla/5.0 (Admin Panel)', '{"status": "ACTIVE", "approved_by": "550e8400-e29b-41d4-a716-446655440001"}', '{"motivo": "Contrato aprovado após análise"}');

-- Logs de criação de produtos
INSERT INTO tb_audit_log (id, entity_type, entity_id, action, performed_by, timestamp, ip_address, user_agent, new_values, metadata) VALUES
('ee1e8400-e29b-41d4-a716-446655440003', 'PRODUTO', '990e8400-e29b-41d4-a716-446655440001', 'CREATE', '550e8400-e29b-41d4-a716-446655440010', DATE_SUB(NOW(), INTERVAL 5 DAY), '192.168.1.200', 'Mozilla/5.0 (Seller Panel)', '{"title": "Smartphone Samsung Galaxy S23 Ultra", "initial_price": 2800.00, "status": "DRAFT"}', '{"categoria": "Eletrônicos", "vendedor": "ABC Importadora"}'),

('ee1e8400-e29b-41d4-a716-446655440004', 'PRODUTO', '990e8400-e29b-41d4-a716-446655440001', 'MODERATE', '550e8400-e29b-41d4-a716-446655440001', DATE_SUB(NOW(), INTERVAL 2 DAY), '10.0.0.2', 'Mozilla/5.0 (Admin Panel)', '{"moderated": true, "status": "ACTIVE"}', '{"moderador": "Maria Silva Santos", "aprovado": true}');

-- Logs de lances importantes
INSERT INTO tb_audit_log (id, entity_type, entity_id, action, performed_by, timestamp, ip_address, user_agent, new_values, metadata) VALUES
('ee1e8400-e29b-41d4-a716-446655440005', 'LANCE', 'bb1e8400-e29b-41d4-a716-446655440005', 'CREATE', '550e8400-e29b-41d4-a716-446655440040', DATE_SUB(NOW(), INTERVAL 1 DAY), '192.168.1.103', 'Mozilla/5.0 (Android 13; Mobile)', '{"produto_id": "990e8400-e29b-41d4-a716-446655440001", "value": 3250.00, "is_winning": true}', '{"lance_anterior": 3100.00, "incremento": 150.00}'),

('ee1e8400-e29b-41d4-a716-446655440006', 'LANCE', 'bb1e8400-e29b-41d4-a716-446655440018', 'CREATE', '550e8400-e29b-41d4-a716-446655440020', DATE_SUB(NOW(), INTERVAL 1 DAY), '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)', '{"produto_id": "990e8400-e29b-41d4-a716-446655440008", "value": 12500.00, "is_winning": true}', '{"lance_anterior": 11000.00, "incremento": 1500.00, "item_valioso": true}');

-- Logs de ações administrativas
INSERT INTO tb_audit_log (id, entity_type, entity_id, action, performed_by, timestamp, ip_address, user_agent, old_values, new_values, metadata) VALUES
('ee1e8400-e29b-41d4-a716-446655440007', 'USUARIO', '550e8400-e29b-41d4-a716-446655440013', 'SUSPEND', '550e8400-e29b-41d4-a716-446655440000', DATE_SUB(NOW(), INTERVAL 7 DAY), '10.0.0.1', 'Mozilla/5.0 (Admin Panel)', '{"status": "ACTIVE"}', '{"status": "SUSPENDED"}', '{"motivo": "Atividades suspeitas detectadas", "admin": "Administrador Root"}'),

('ee1e8400-e29b-41d4-a716-446655440008', 'CONTRATO', '770e8400-e29b-41d4-a716-446655440013', 'CANCEL', '550e8400-e29b-41d4-a716-446655440000', DATE_SUB(NOW(), INTERVAL 7 DAY), '10.0.0.1', 'Mozilla/5.0 (Admin Panel)', '{"status": "ACTIVE"}', '{"status": "CANCELLED"}', '{"motivo": "Cancelamento devido à suspensão do vendedor", "relacionado": "Suspensão do usuário 550e8400-e29b-41d4-a716-446655440013"}');

-- =====================================================
-- PRÉ-AUTORIZAÇÕES (PARA COMPRADORES ATIVOS)
-- =====================================================

-- Pré-autorização da Fernanda para o Samsung
INSERT INTO tb_pre_autorizacao (id, buyer_id, produto_id, amount, status, payment_method_id, authorization_code, created_at, expires_at) VALUES
('ff1e8400-e29b-41d4-a716-446655440001', '880e8400-e29b-41d4-a716-446655440020', '990e8400-e29b-41d4-a716-446655440001', 5000.00, 'AUTHORIZED', 'card_1234567890', 'AUTH_ABC123XYZ', DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_ADD(NOW(), INTERVAL 5 DAY));

-- Pré-autorização do Ricardo para o Dom Casmurro
INSERT INTO tb_pre_autorizacao (id, buyer_id, produto_id, amount, status, payment_method_id, authorization_code, created_at, expires_at) VALUES
('ff1e8400-e29b-41d4-a716-446655440002', '880e8400-e29b-41d4-a716-446655440023', '990e8400-e29b-41d4-a716-446655440006', 5000.00, 'AUTHORIZED', 'card_0987654321', 'AUTH_DEF456UVW', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 6 DAY));

-- Pré-autorização do Marcos para o iPhone
INSERT INTO tb_pre_autorizacao (id, buyer_id, produto_id, amount, status, payment_method_id, authorization_code, created_at, expires_at) VALUES
('ff1e8400-e29b-41d4-a716-446655440003', '880e8400-e29b-41d4-a716-446655440021', '990e8400-e29b-41d4-a716-446655440009', 3000.00, 'AUTHORIZED', 'pix_1122334455', 'AUTH_GHI789RST', DATE_SUB(NOW(), INTERVAL 6 HOUR), DATE_ADD(NOW(), INTERVAL 1 DAY));

-- =====================================================
-- COMENTÁRIOS FINAIS
-- =====================================================
/*
DADOS COMPLEMENTARES CRIADOS:

LANCES (21 total):
- Samsung Galaxy S23 Ultra: 5 lances (R$ 2.850 → R$ 3.250)
- Vaso Cerâmica: 3 lances (R$ 85 → R$ 125)
- Dom Casmurro: 5 lances (R$ 2.600 → R$ 3.800)
- Fender Stratocaster: 5 lances (R$ 8.500 → R$ 12.500)
- iPhone 13 Pro: 3 lances (R$ 2.250 → R$ 2.450)

FAVORITOS (10 total):
- Fernanda: 3 favoritos (Samsung, Fender, Vaso)
- Ricardo: 3 favoritos (Dom Casmurro, Agatha Christie, Fender)
- Marcos: 2 favoritos (Samsung, iPhone)
- Gabriel: 2 favoritos (Dom Casmurro, Agatha Christie)

NOTIFICAÇÕES (9 total):
- Lance superado: 3 notificações
- Leilão terminando: 2 notificações
- Novos produtos: 2 notificações
- Sistema: 2 notificações

AUDITORIA (8 logs):
- Criação/aprovação de contratos: 2 logs
- Criação/moderação de produtos: 2 logs
- Lances importantes: 2 logs
- Ações administrativas: 2 logs

PRÉ-AUTORIZAÇÕES (3 total):
- Fernanda: R$ 5.000 (Samsung)
- Ricardo: R$ 5.000 (Dom Casmurro)
- Marcos: R$ 3.000 (iPhone)

CENÁRIOS SIMULADOS:
- Disputa acirrada por produtos valiosos
- Diferentes tipos de compradores ativos
- Histórico de atividades administrativas
- Sistema de notificações funcionando
- Pré-autorizações para grandes compras
- Auditoria completa de ações importantes

ESTATÍSTICAS GERADAS:
- Taxa de conversão de favoritos em lances
- Padrões de comportamento por tipo de usuário
- Histórico de preços e disputas
- Atividade por categoria de produto
- Efetividade do sistema de notificações
*/