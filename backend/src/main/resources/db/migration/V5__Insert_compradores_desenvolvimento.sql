-- =====================================================
-- Sistema de Leilão Eletrônico - Massa de Dados V5
-- Entidade: COMPRADORES
-- Versão: 5.0 - Compradores para Desenvolvimento
-- Data: 2024-12-19
-- Descrição: Massa de dados de compradores com diferentes perfis e status KYC
-- =====================================================

-- =====================================================
-- COMPRADOR 1: FERNANDA - VIP APROVADO
-- =====================================================
INSERT INTO tb_comprador (
    id,
    usuario_id,
    kyc_status,
    preferences,
    limite_credito,
    created_at
) VALUES (
    '880e8400-e29b-41d4-a716-446655440020',
    '550e8400-e29b-41d4-a716-446655440020', -- Fernanda Rodrigues Lima
    'APPROVED',
    '{"categorias_interesse": ["Eletrônicos", "Decoração", "Livros"], "faixa_preco_preferida": {"min": 100, "max": 5000}, "notificacoes": {"email": true, "sms": true, "push": true}, "metodo_pagamento_preferido": "cartao_credito", "endereco_entrega_padrao": {"cep": "01310-100", "cidade": "São Paulo", "estado": "SP"}}',
    15000.00,
    DATE_SUB(NOW(), INTERVAL 240 DAY)
);

-- =====================================================
-- COMPRADOR 2: MARCOS - REGULAR APROVADO
-- =====================================================
INSERT INTO tb_comprador (
    id,
    usuario_id,
    kyc_status,
    preferences,
    limite_credito,
    created_at
) VALUES (
    '880e8400-e29b-41d4-a716-446655440021',
    '550e8400-e29b-41d4-a716-446655440021', -- Marcos Antonio Pereira
    'APPROVED',
    '{"categorias_interesse": ["Eletrônicos", "Instrumentos Musicais"], "faixa_preco_preferida": {"min": 50, "max": 2000}, "notificacoes": {"email": true, "sms": false, "push": true}, "metodo_pagamento_preferido": "pix", "endereco_entrega_padrao": {"cep": "04567-890", "cidade": "São Paulo", "estado": "SP"}}',
    5000.00,
    DATE_SUB(NOW(), INTERVAL 180 DAY)
);

-- =====================================================
-- COMPRADOR 3: JULIANA - PENDENTE VERIFICAÇÃO
-- =====================================================
INSERT INTO tb_comprador (
    id,
    usuario_id,
    kyc_status,
    preferences,
    limite_credito,
    created_at
) VALUES (
    '880e8400-e29b-41d4-a716-446655440022',
    '550e8400-e29b-41d4-a716-446655440022', -- Juliana Santos Oliveira
    'PENDING',
    '{"categorias_interesse": ["Artesanatos", "Decoração"], "faixa_preco_preferida": {"min": 20, "max": 500}, "notificacoes": {"email": true, "sms": false, "push": false}, "metodo_pagamento_preferido": "boleto"}',
    NULL, -- Sem limite até aprovação
    DATE_SUB(NOW(), INTERVAL 7 DAY)
);

-- =====================================================
-- COMPRADOR 4: RICARDO - COLECIONADOR VIP
-- =====================================================
INSERT INTO tb_comprador (
    id,
    usuario_id,
    kyc_status,
    preferences,
    limite_credito,
    created_at
) VALUES (
    '880e8400-e29b-41d4-a716-446655440023',
    '550e8400-e29b-41d4-a716-446655440023', -- Ricardo Mendes Colecionador
    'APPROVED',
    '{"categorias_interesse": ["Antiguidades", "Livros", "Instrumentos Musicais"], "faixa_preco_preferida": {"min": 500, "max": 20000}, "notificacoes": {"email": true, "sms": true, "push": true}, "metodo_pagamento_preferido": "transferencia_bancaria", "especialidades": ["primeiras_edicoes", "instrumentos_vintage", "pecas_raras"], "endereco_entrega_padrao": {"cep": "22071-900", "cidade": "Rio de Janeiro", "estado": "RJ"}}',
    25000.00,
    DATE_SUB(NOW(), INTERVAL 300 DAY)
);

-- =====================================================
-- COMPRADOR 5: PATRICIA - HÍBRIDO (COMPRA E VENDE)
-- =====================================================
INSERT INTO tb_comprador (
    id,
    usuario_id,
    kyc_status,
    preferences,
    limite_credito,
    created_at
) VALUES (
    '880e8400-e29b-41d4-a716-446655440040',
    '550e8400-e29b-41d4-a716-446655440040', -- Patricia Almeida Comerciante
    'APPROVED',
    '{"categorias_interesse": ["Eletrônicos", "Livros", "Colecionáveis"], "faixa_preco_preferida": {"min": 100, "max": 3000}, "notificacoes": {"email": true, "sms": true, "push": false}, "metodo_pagamento_preferido": "cartao_credito", "finalidade": "revenda", "endereco_entrega_padrao": {"cep": "03456-789", "cidade": "São Paulo", "estado": "SP"}}',
    8000.00,
    DATE_SUB(NOW(), INTERVAL 120 DAY)
);

-- =====================================================
-- COMPRADORES ADICIONAIS PARA CENÁRIOS DE TESTE
-- =====================================================

-- Comprador 6: KYC Rejeitado
INSERT INTO tb_comprador (
    id,
    usuario_id,
    kyc_status,
    preferences,
    limite_credito,
    created_at
) VALUES (
    '880e8400-e29b-41d4-a716-446655440050',
    '550e8400-e29b-41d4-a716-446655440050', -- Bruno (usuário bloqueado)
    'REJECTED',
    '{"categorias_interesse": ["Eletrônicos"], "motivo_rejeicao": "documentos_invalidos", "data_rejeicao": "2024-12-10"}',
    NULL,
    DATE_SUB(NOW(), INTERVAL 45 DAY)
);

-- Comprador 7: KYC Expirado
INSERT INTO tb_comprador (
    id,
    usuario_id,
    kyc_status,
    preferences,
    limite_credito,
    created_at
) VALUES (
    '880e8400-e29b-41d4-a716-446655440051',
    '550e8400-e29b-41d4-a716-446655440051', -- Carla (usuária inativa)
    'EXPIRED',
    '{"categorias_interesse": ["Decoração", "Artesanatos"], "data_expiracao": "2024-11-15", "necessita_renovacao": true}',
    1000.00, -- Limite reduzido por expiração
    DATE_SUB(NOW(), INTERVAL 400 DAY)
);

-- Comprador 8: Comprador Corporativo
INSERT INTO tb_comprador (
    id,
    usuario_id,
    kyc_status,
    preferences,
    limite_credito,
    created_at
) VALUES (
    '880e8400-e29b-41d4-a716-446655440030',
    '550e8400-e29b-41d4-a716-446655440030', -- Luiza (participante curiosa)
    'APPROVED',
    '{"categorias_interesse": ["Eletrônicos", "Equipamentos"], "tipo_comprador": "corporativo", "faixa_preco_preferida": {"min": 1000, "max": 50000}, "notificacoes": {"email": true, "sms": false, "push": false}, "metodo_pagamento_preferido": "transferencia_bancaria", "endereco_entrega_padrao": {"cep": "01234-567", "cidade": "São Paulo", "estado": "SP"}}',
    50000.00,
    DATE_SUB(NOW(), INTERVAL 90 DAY)
);

-- Comprador 9: Estudante com Limite Baixo
INSERT INTO tb_comprador (
    id,
    usuario_id,
    kyc_status,
    preferences,
    limite_credito,
    created_at
) VALUES (
    '880e8400-e29b-41d4-a716-446655440031',
    '550e8400-e29b-41d4-a716-446655440031', -- Gabriel (estudante)
    'APPROVED',
    '{"categorias_interesse": ["Livros", "Eletrônicos"], "tipo_comprador": "estudante", "faixa_preco_preferida": {"min": 10, "max": 300}, "notificacoes": {"email": true, "sms": false, "push": true}, "metodo_pagamento_preferido": "pix", "desconto_estudante": true}',
    500.00,
    DATE_SUB(NOW(), INTERVAL 60 DAY)
);

-- =====================================================
-- COMPRADOR 10: COMPRADOR INTERNACIONAL
-- =====================================================
INSERT INTO tb_comprador (
    id,
    usuario_id,
    kyc_status,
    preferences,
    limite_credito,
    created_at
) VALUES (
    '880e8400-e29b-41d4-a716-446655440099',
    '550e8400-e29b-41d4-a716-446655440010', -- Reutilizando usuário Carlos para teste
    'APPROVED',
    '{"categorias_interesse": ["Antiguidades", "Arte"], "tipo_comprador": "internacional", "faixa_preco_preferida": {"min": 2000, "max": 100000}, "notificacoes": {"email": true, "sms": false, "push": false}, "metodo_pagamento_preferido": "cartao_credito_internacional", "moeda_preferida": "USD", "endereco_entrega_padrao": {"pais": "Estados Unidos", "cidade": "Miami", "estado": "FL"}}',
    100000.00,
    DATE_SUB(NOW(), INTERVAL 150 DAY)
);

-- =====================================================
-- COMENTÁRIOS FINAIS
-- =====================================================
/*
COMPRADORES CRIADOS PARA DESENVOLVIMENTO:

COMPRADORES APROVADOS (7 total):
1. Fernanda Lima - VIP (R$ 15.000 limite)
2. Marcos Pereira - Regular (R$ 5.000 limite)
3. Ricardo Colecionador - VIP (R$ 25.000 limite)
4. Patricia Comerciante - Híbrida (R$ 8.000 limite)
5. Luiza Corporativa - Corporativo (R$ 50.000 limite)
6. Gabriel Estudante - Estudante (R$ 500 limite)
7. Carlos Internacional - Internacional (R$ 100.000 limite)

COMPRADORES PENDENTES (1 total):
1. Juliana Santos - Aguardando verificação KYC

COMPRADORES COM PROBLEMAS (2 total):
1. Bruno - KYC Rejeitado (documentos inválidos)
2. Carla - KYC Expirado (necessita renovação)

PERFIS DE COMPRADOR:
- VIP: Alto limite, múltiplas categorias
- Regular: Limite médio, categorias específicas
- Estudante: Limite baixo, foco em livros/eletrônicos
- Corporativo: Limite alto, equipamentos
- Internacional: Limite muito alto, arte/antiguidades
- Híbrido: Compra para revenda
- Colecionador: Especialista em itens raros

CATEGORIAS DE INTERESSE:
- Eletrônicos (mais popular)
- Livros e Literatura
- Artesanatos e Decoração
- Instrumentos Musicais
- Antiguidades e Arte
- Equipamentos Corporativos

MÉTODOS DE PAGAMENTO:
- Cartão de Crédito
- PIX
- Transferência Bancária
- Boleto
- Cartão Internacional

LIMITES DE CRÉDITO:
- Mínimo: R$ 500 (estudante)
- Máximo: R$ 100.000 (internacional)
- Média: R$ 23.000

TOTAL DE COMPRADORES: 10
COMPRADORES ATIVOS: 7
COMPRADORES VERIFICADOS: 7
*/