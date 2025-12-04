-- =====================================================
-- Sistema de Leilão Eletrônico - Massa de Dados V7
-- Entidade: LOTES
-- Versão: 7.0 - Lotes para Desenvolvimento
-- Data: 2024-12-19
-- Descrição: Massa de dados de lotes com diferentes cenários e produtos associados
-- =====================================================

-- =====================================================
-- LOTE 1: ABC IMPORTADORA - ELETRÔNICOS PREMIUM
-- =====================================================
INSERT INTO tb_lote (
    id,
    seller_id,
    contract_id,
    title,
    description,
    lote_end_datetime,
    status,
    created_at
) VALUES (
    'aa0e8400-e29b-41d4-a716-446655440001',
    '660e8400-e29b-41d4-a716-446655440010', -- ABC Importadora
    '770e8400-e29b-41d4-a716-446655440010', -- Contrato ABC
    'Lote Premium Eletrônicos - Smartphones e Tablets',
    'Lote especial com produtos eletrônicos de alta qualidade da ABC Importadora. Inclui smartphones Samsung e outros dispositivos móveis. Todos os produtos são novos, lacrados e com garantia. Ideal para revendedores ou colecionadores de tecnologia.',
    DATE_ADD(NOW(), INTERVAL 5 DAY),
    'ACTIVE',
    DATE_SUB(NOW(), INTERVAL 3 DAY)
);

-- =====================================================
-- LOTE 2: LOJA DA ANA - COLEÇÃO ARTESANAL
-- =====================================================
INSERT INTO tb_lote (
    id,
    seller_id,
    contract_id,
    title,
    description,
    lote_end_datetime,
    status,
    created_at
) VALUES (
    'aa0e8400-e29b-41d4-a716-446655440002',
    '660e8400-e29b-41d4-a716-446655440011', -- Loja da Ana
    '770e8400-e29b-41d4-a716-446655440011', -- Contrato Ana
    'Coleção Artesanal Sustentável - Peças Únicas',
    'Lote exclusivo com peças artesanais únicas da Loja da Ana. Inclui vasos decorativos, quadros em madeira e outros objetos de decoração. Todas as peças são feitas à mão por artesãos locais, priorizando materiais sustentáveis e técnicas tradicionais.',
    DATE_ADD(NOW(), INTERVAL 4 DAY),
    'ACTIVE',
    DATE_SUB(NOW(), INTERVAL 5 DAY)
);

-- =====================================================
-- LOTE 3: SEBO DO JOÃO - LITERATURA CLÁSSICA
-- =====================================================
INSERT INTO tb_lote (
    id,
    seller_id,
    contract_id,
    title,
    description,
    lote_end_datetime,
    status,
    created_at
) VALUES (
    'aa0e8400-e29b-41d4-a716-446655440003',
    '660e8400-e29b-41d4-a716-446655440060', -- Sebo do João
    '770e8400-e29b-41d4-a716-446655440060', -- Contrato Sebo
    'Biblioteca Clássica - Primeiras Edições e Coleções',
    'Lote especial para bibliófilos e colecionadores. Inclui primeira edição de Dom Casmurro e coleção completa de Agatha Christie. Livros em excelente estado de conservação, alguns com valor histórico significativo. Oportunidade única para adquirir obras raras.',
    DATE_ADD(NOW(), INTERVAL 8 DAY),
    'ACTIVE',
    DATE_SUB(NOW(), INTERVAL 2 DAY)
);

-- =====================================================
-- LOTE 4: HARMONIA MUSICAL - INSTRUMENTOS VINTAGE
-- =====================================================
INSERT INTO tb_lote (
    id,
    seller_id,
    contract_id,
    title,
    description,
    lote_end_datetime,
    status,
    created_at
) VALUES (
    'aa0e8400-e29b-41d4-a716-446655440004',
    '660e8400-e29b-41d4-a716-446655440061', -- Harmonia Musical
    '770e8400-e29b-41d4-a716-446655440061', -- Contrato Harmonia
    'Coleção Vintage - Instrumentos dos Anos 70 e 80',
    'Lote exclusivo com instrumentos musicais vintage da Harmonia Musical. Destaque para guitarra Fender Stratocaster de 1978 e outros instrumentos de época. Todos os instrumentos foram revisados por luthiers especializados e estão em perfeito estado de funcionamento.',
    DATE_ADD(NOW(), INTERVAL 12 DAY),
    'ACTIVE',
    DATE_SUB(NOW(), INTERVAL 6 DAY)
);

-- =====================================================
-- LOTE 5: PATRICIA - LIQUIDAÇÃO MISTA
-- =====================================================
INSERT INTO tb_lote (
    id,
    seller_id,
    contract_id,
    title,
    description,
    lote_end_datetime,
    status,
    created_at
) VALUES (
    'aa0e8400-e29b-41d4-a716-446655440005',
    '660e8400-e29b-41d4-a716-446655440040', -- Patricia Almeida
    '770e8400-e29b-41d4-a716-446655440040', -- Contrato Patricia
    'Liquidação Especial - Eletrônicos Seminovos',
    'Lote de liquidação com produtos eletrônicos seminovos em ótimo estado. Inclui smartphones, tablets e outros dispositivos. Todos os produtos foram testados e estão funcionando perfeitamente. Oportunidade para adquirir tecnologia com preços acessíveis.',
    DATE_ADD(NOW(), INTERVAL 2 DAY),
    'ACTIVE',
    DATE_SUB(NOW(), INTERVAL 1 DAY)
);

-- =====================================================
-- LOTES COM DIFERENTES STATUS (CENÁRIOS DE TESTE)
-- =====================================================

-- Lote 6: Draft (Aguardando Publicação)
INSERT INTO tb_lote (
    id,
    seller_id,
    contract_id,
    title,
    description,
    lote_end_datetime,
    status,
    created_at
) VALUES (
    'aa0e8400-e29b-41d4-a716-446655440006',
    '660e8400-e29b-41d4-a716-446655440010', -- ABC Importadora
    '770e8400-e29b-41d4-a716-446655440010', -- Contrato ABC
    'Lote Eletrodomésticos - Em Preparação',
    'Lote em preparação com eletrodomésticos diversos. Aguardando finalização da seleção de produtos e aprovação para publicação.',
    DATE_ADD(NOW(), INTERVAL 15 DAY),
    'DRAFT',
    NOW()
);

-- Lote 7: Fechado (Encerrado)
INSERT INTO tb_lote (
    id,
    seller_id,
    contract_id,
    title,
    description,
    lote_end_datetime,
    status,
    created_at
) VALUES (
    'aa0e8400-e29b-41d4-a716-446655440007',
    '660e8400-e29b-41d4-a716-446655440011', -- Loja da Ana
    '770e8400-e29b-41d4-a716-446655440011', -- Contrato Ana
    'Lote Decoração Natalina - ENCERRADO',
    'Lote temático com decorações natalinas artesanais. Lote já encerrado com sucesso, todos os produtos foram vendidos.',
    DATE_SUB(NOW(), INTERVAL 5 DAY),
    'CLOSED',
    DATE_SUB(NOW(), INTERVAL 20 DAY)
);

-- Lote 8: Cancelado
INSERT INTO tb_lote (
    id,
    seller_id,
    contract_id,
    title,
    description,
    lote_end_datetime,
    status,
    created_at
) VALUES (
    'aa0e8400-e29b-41d4-a716-446655440008',
    '660e8400-e29b-41d4-a716-446655440013', -- Vendas Rápidas (suspenso)
    '770e8400-e29b-41d4-a716-446655440013', -- Contrato cancelado
    'Lote Produtos Diversos - CANCELADO',
    'Lote cancelado devido a irregularidades identificadas com o vendedor. Produtos foram removidos da plataforma.',
    DATE_ADD(NOW(), INTERVAL 10 DAY),
    'CANCELLED',
    DATE_SUB(NOW(), INTERVAL 30 DAY)
);

-- =====================================================
-- ASSOCIAÇÃO DE PRODUTOS AOS LOTES
-- =====================================================

-- Associar produtos ao Lote 1 (Eletrônicos Premium)
UPDATE tb_produto 
SET lote_id = 'aa0e8400-e29b-41d4-a716-446655440001'
WHERE id IN (
    '990e8400-e29b-41d4-a716-446655440001', -- Samsung Galaxy S23 Ultra
    '990e8400-e29b-41d4-a716-446655440011'  -- Tablet Samsung (expirado)
);

-- Associar produtos ao Lote 2 (Coleção Artesanal)
UPDATE tb_produto 
SET lote_id = 'aa0e8400-e29b-41d4-a716-446655440002'
WHERE id IN (
    '990e8400-e29b-41d4-a716-446655440004', -- Vaso Cerâmica
    '990e8400-e29b-41d4-a716-446655440005', -- Quadro Mandala
    '990e8400-e29b-41d4-a716-446655440010'  -- Luminária Bambu (vendida)
);

-- Associar produtos ao Lote 3 (Literatura Clássica)
UPDATE tb_produto 
SET lote_id = 'aa0e8400-e29b-41d4-a716-446655440003'
WHERE id IN (
    '990e8400-e29b-41d4-a716-446655440006', -- Dom Casmurro 1ª Ed.
    '990e8400-e29b-41d4-a716-446655440007'  -- Coleção Agatha Christie
);

-- Associar produtos ao Lote 4 (Instrumentos Vintage)
UPDATE tb_produto 
SET lote_id = 'aa0e8400-e29b-41d4-a716-446655440004'
WHERE id = '990e8400-e29b-41d4-a716-446655440008'; -- Fender Stratocaster 1978

-- Associar produtos ao Lote 5 (Liquidação Mista)
UPDATE tb_produto 
SET lote_id = 'aa0e8400-e29b-41d4-a716-446655440005'
WHERE id = '990e8400-e29b-41d4-a716-446655440009'; -- iPhone 13 Pro

-- =====================================================
-- LOTES ADICIONAIS PARA CENÁRIOS ESPECÍFICOS
-- =====================================================

-- Lote 9: Lote Futuro (Para Testes de Agendamento)
INSERT INTO tb_lote (
    id,
    seller_id,
    contract_id,
    title,
    description,
    lote_end_datetime,
    status,
    created_at
) VALUES (
    'aa0e8400-e29b-41d4-a716-446655440009',
    '660e8400-e29b-41d4-a716-446655440060', -- Sebo do João
    '770e8400-e29b-41d4-a716-446655440060', -- Contrato Sebo
    'Lote Futuro - Literatura Internacional',
    'Lote agendado para o futuro com obras de literatura internacional. Será ativado automaticamente na data programada.',
    DATE_ADD(NOW(), INTERVAL 30 DAY),
    'DRAFT',
    NOW()
);

-- Lote 10: Lote Express (Encerra Hoje)
INSERT INTO tb_lote (
    id,
    seller_id,
    contract_id,
    title,
    description,
    lote_end_datetime,
    status,
    created_at
) VALUES (
    'aa0e8400-e29b-41d4-a716-446655440010',
    '660e8400-e29b-41d4-a716-446655440040', -- Patricia Almeida
    '770e8400-e29b-41d4-a716-446655440040', -- Contrato Patricia
    'Lote Express - Oportunidades Relâmpago',
    'Lote com duração reduzida para oportunidades relâmpago. Produtos com preços especiais por tempo limitado.',
    DATE_ADD(NOW(), INTERVAL 6 HOUR),
    'ACTIVE',
    DATE_SUB(NOW(), INTERVAL 2 HOUR)
);

-- =====================================================
-- COMENTÁRIOS FINAIS
-- =====================================================
/*
LOTES CRIADOS PARA DESENVOLVIMENTO:

LOTES ATIVOS (6 total):
1. Eletrônicos Premium - ABC Importadora (5 dias) - 2 produtos
2. Coleção Artesanal - Loja da Ana (4 dias) - 3 produtos
3. Literatura Clássica - Sebo João (8 dias) - 2 produtos
4. Instrumentos Vintage - Harmonia Musical (12 dias) - 1 produto
5. Liquidação Mista - Patricia (2 dias) - 1 produto
6. Lote Express - Patricia (6 horas) - 0 produtos

LOTES NÃO ATIVOS (4 total):
1. Eletrodomésticos - DRAFT (aguardando publicação)
2. Decoração Natalina - CLOSED (encerrado com sucesso)
3. Produtos Diversos - CANCELLED (cancelado por irregularidades)
4. Literatura Internacional - DRAFT (agendado para futuro)

DISTRIBUIÇÃO POR VENDEDOR:
- ABC Importadora: 2 lotes (1 ativo, 1 draft)
- Loja da Ana: 2 lotes (1 ativo, 1 fechado)
- Sebo do João: 2 lotes (1 ativo, 1 futuro)
- Harmonia Musical: 1 lote (ativo)
- Patricia Almeida: 2 lotes (2 ativos)
- Vendas Rápidas: 1 lote (cancelado)

PRODUTOS ASSOCIADOS:
- Total de produtos em lotes: 9
- Produtos sem lote: 3
- Lotes com múltiplos produtos: 3
- Lotes com produto único: 2

CENÁRIOS DE TESTE:
- Lotes com diferentes durações (6h a 30 dias)
- Lotes com produtos de diferentes status
- Lotes encerrados (histórico)
- Lotes cancelados (problemas)
- Lotes futuros (agendamento)
- Lotes express (urgência)

CATEGORIAS POR LOTE:
- Eletrônicos: 2 lotes
- Artesanatos: 1 lote
- Livros: 2 lotes
- Instrumentos: 1 lote
- Misto: 4 lotes

TOTAL DE LOTES: 10
LOTES ATIVOS: 6
PRODUTOS EM LOTES: 9
VENDEDORES COM LOTES: 5
*/