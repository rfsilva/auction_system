-- =====================================================
-- Sistema de Leilão Eletrônico - Massa de Dados V4
-- Entidade: CONTRATOS
-- Versão: 4.0 - Contratos para Desenvolvimento
-- Data: 2024-12-19
-- Descrição: Massa de dados de contratos com diferentes cenários e status
-- =====================================================

-- =====================================================
-- CONTRATO 1: ABC IMPORTADORA - ATIVO GERAL
-- =====================================================
INSERT INTO tb_contrato (
    id,
    seller_id,
    fee_rate,
    terms,
    valid_from,
    valid_to,
    active,
    categoria,
    status,
    created_by,
    approved_by,
    approved_at,
    created_at
) VALUES (
    '770e8400-e29b-41d4-a716-446655440010',
    '660e8400-e29b-41d4-a716-446655440010', -- ABC Importadora
    0.0350, -- 3.5%
    'Contrato geral para venda de produtos eletrônicos e eletrodomésticos. Taxa de comissão de 3,5% sobre o valor final de venda. Vendedor responsável por descrições precisas e garantia dos produtos. Prazo de entrega máximo de 15 dias úteis.',
    DATE_SUB(NOW(), INTERVAL 180 DAY),
    DATE_ADD(NOW(), INTERVAL 185 DAY), -- Válido por 1 ano
    TRUE,
    NULL, -- Contrato geral (todas as categorias)
    'ACTIVE',
    '550e8400-e29b-41d4-a716-446655440000', -- Admin root
    '550e8400-e29b-41d4-a716-446655440001', -- Maria admin
    DATE_SUB(NOW(), INTERVAL 179 DAY),
    DATE_SUB(NOW(), INTERVAL 180 DAY)
);

-- =====================================================
-- CONTRATO 2: LOJA DA ANA - ATIVO ESPECÍFICO
-- =====================================================
INSERT INTO tb_contrato (
    id,
    seller_id,
    fee_rate,
    terms,
    valid_from,
    valid_to,
    active,
    categoria,
    status,
    created_by,
    approved_by,
    approved_at,
    created_at
) VALUES (
    '770e8400-e29b-41d4-a716-446655440011',
    '660e8400-e29b-41d4-a716-446655440011', -- Loja da Ana
    0.0250, -- 2.5%
    'Contrato específico para artesanatos e decoração. Taxa promocional de 2,5% para incentivar pequenos artesãos. Produtos devem ser únicos ou em pequenas quantidades. Foco em sustentabilidade e produção local.',
    DATE_SUB(NOW(), INTERVAL 90 DAY),
    DATE_ADD(NOW(), INTERVAL 275 DAY), -- Válido por 1 ano
    TRUE,
    'Artesanatos',
    'ACTIVE',
    '550e8400-e29b-41d4-a716-446655440001', -- Maria admin
    '550e8400-e29b-41d4-a716-446655440001', -- Maria admin
    DATE_SUB(NOW(), INTERVAL 89 DAY),
    DATE_SUB(NOW(), INTERVAL 90 DAY)
);

-- =====================================================
-- CONTRATO 3: PATRICIA - ATIVO MÚLTIPLAS CATEGORIAS
-- =====================================================
INSERT INTO tb_contrato (
    id,
    seller_id,
    fee_rate,
    terms,
    valid_from,
    valid_to,
    active,
    categoria,
    status,
    created_by,
    approved_by,
    approved_at,
    created_at
) VALUES (
    '770e8400-e29b-41d4-a716-446655440040',
    '660e8400-e29b-41d4-a716-446655440040', -- Patricia Almeida
    0.0400, -- 4.0%
    'Contrato para comerciante híbrida. Taxa de 4% aplicável a produtos diversos. Vendedora experiente com histórico de transações confiáveis. Especialização em eletrônicos usados, livros e colecionáveis.',
    DATE_SUB(NOW(), INTERVAL 120 DAY),
    DATE_ADD(NOW(), INTERVAL 245 DAY), -- Válido por 1 ano
    TRUE,
    NULL, -- Múltiplas categorias
    'ACTIVE',
    '550e8400-e29b-41d4-a716-446655440000', -- Admin root
    '550e8400-e29b-41d4-a716-446655440000', -- Admin root
    DATE_SUB(NOW(), INTERVAL 119 DAY),
    DATE_SUB(NOW(), INTERVAL 120 DAY)
);

-- =====================================================
-- CONTRATO 4: SEBO DO JOÃO - ATIVO LIVROS
-- =====================================================
INSERT INTO tb_contrato (
    id,
    seller_id,
    fee_rate,
    terms,
    valid_from,
    valid_to,
    active,
    categoria,
    status,
    created_by,
    approved_by,
    approved_at,
    created_at
) VALUES (
    '770e8400-e29b-41d4-a716-446655440060',
    '660e8400-e29b-41d4-a716-446655440060', -- Sebo do João
    0.0300, -- 3.0%
    'Contrato específico para livros e literatura. Taxa de 3% para incentivar o mercado de livros usados e raros. Vendedor deve fornecer informações detalhadas sobre estado de conservação, edição e raridade.',
    DATE_SUB(NOW(), INTERVAL 200 DAY),
    DATE_ADD(NOW(), INTERVAL 165 DAY), -- Válido por 1 ano
    TRUE,
    'Livros',
    'ACTIVE',
    '550e8400-e29b-41d4-a716-446655440002', -- João suporte
    '550e8400-e29b-41d4-a716-446655440001', -- Maria admin
    DATE_SUB(NOW(), INTERVAL 199 DAY),
    DATE_SUB(NOW(), INTERVAL 200 DAY)
);

-- =====================================================
-- CONTRATO 5: HARMONIA MUSICAL - ATIVO INSTRUMENTOS
-- =====================================================
INSERT INTO tb_contrato (
    id,
    seller_id,
    fee_rate,
    terms,
    valid_from,
    valid_to,
    active,
    categoria,
    status,
    created_by,
    approved_by,
    approved_at,
    created_at
) VALUES (
    '770e8400-e29b-41d4-a716-446655440061',
    '660e8400-e29b-41d4-a716-446655440061', -- Harmonia Musical
    0.0450, -- 4.5%
    'Contrato para instrumentos musicais vintage e raros. Taxa de 4,5% devido à especialização e valor agregado dos produtos. Todos os instrumentos devem passar por revisão técnica antes da venda.',
    DATE_SUB(NOW(), INTERVAL 150 DAY),
    DATE_ADD(NOW(), INTERVAL 215 DAY), -- Válido por 1 ano
    TRUE,
    'Instrumentos Musicais',
    'ACTIVE',
    '550e8400-e29b-41d4-a716-446655440001', -- Maria admin
    '550e8400-e29b-41d4-a716-446655440000', -- Admin root
    DATE_SUB(NOW(), INTERVAL 149 DAY),
    DATE_SUB(NOW(), INTERVAL 150 DAY)
);

-- =====================================================
-- CONTRATO 6: ANTIQUÁRIO - DRAFT (AGUARDANDO APROVAÇÃO)
-- =====================================================
INSERT INTO tb_contrato (
    id,
    seller_id,
    fee_rate,
    terms,
    valid_from,
    valid_to,
    active,
    categoria,
    status,
    created_by,
    approved_by,
    approved_at,
    created_at
) VALUES (
    '770e8400-e29b-41d4-a716-446655440012',
    '660e8400-e29b-41d4-a716-446655440012', -- Antiquário Silva
    0.0500, -- 5.0%
    'Proposta de contrato para antiguidades e colecionáveis. Taxa de 5% devido à natureza especializada dos produtos. Vendedor compromete-se a fornecer certificados de autenticidade quando aplicável.',
    NOW(),
    DATE_ADD(NOW(), INTERVAL 365 DAY),
    FALSE,
    'Antiguidades',
    'DRAFT',
    '550e8400-e29b-41d4-a716-446655440002', -- João suporte
    NULL,
    NULL,
    DATE_SUB(NOW(), INTERVAL 5 DAY)
);

-- =====================================================
-- CONTRATO 7: VENDAS RÁPIDAS - CANCELADO
-- =====================================================
INSERT INTO tb_contrato (
    id,
    seller_id,
    fee_rate,
    terms,
    valid_from,
    valid_to,
    active,
    categoria,
    status,
    created_by,
    approved_by,
    approved_at,
    created_at
) VALUES (
    '770e8400-e29b-41d4-a716-446655440013',
    '660e8400-e29b-41d4-a716-446655440013', -- Vendas Rápidas (suspenso)
    0.0600, -- 6.0%
    'Contrato cancelado devido a irregularidades identificadas nas práticas comerciais do vendedor. Taxa original era de 6% para produtos diversos.',
    DATE_SUB(NOW(), INTERVAL 60 DAY),
    DATE_ADD(NOW(), INTERVAL 305 DAY),
    FALSE,
    NULL,
    'CANCELLED',
    '550e8400-e29b-41d4-a716-446655440000', -- Admin root
    '550e8400-e29b-41d4-a716-446655440000', -- Admin root (aprovado antes do cancelamento)
    DATE_SUB(NOW(), INTERVAL 59 DAY),
    DATE_SUB(NOW(), INTERVAL 60 DAY)
);

-- =====================================================
-- CONTRATO 8: ABC IMPORTADORA - EXPIRADO (HISTÓRICO)
-- =====================================================
INSERT INTO tb_contrato (
    id,
    seller_id,
    fee_rate,
    terms,
    valid_from,
    valid_to,
    active,
    categoria,
    status,
    created_by,
    approved_by,
    approved_at,
    created_at
) VALUES (
    '770e8400-e29b-41d4-a716-446655440080',
    '660e8400-e29b-41d4-a716-446655440010', -- ABC Importadora
    0.0400, -- 4.0%
    'Contrato anterior da ABC Importadora. Taxa de 4% que foi renegociada para 3,5% no contrato atual. Histórico de bom relacionamento comercial.',
    DATE_SUB(NOW(), INTERVAL 550 DAY),
    DATE_SUB(NOW(), INTERVAL 185 DAY), -- Expirado há 6 meses
    FALSE,
    NULL,
    'EXPIRED',
    '550e8400-e29b-41d4-a716-446655440000', -- Admin root
    '550e8400-e29b-41d4-a716-446655440000', -- Admin root
    DATE_SUB(NOW(), INTERVAL 549 DAY),
    DATE_SUB(NOW(), INTERVAL 550 DAY)
);

-- =====================================================
-- CONTRATO 9: LOJA DA ANA - SUSPENSO TEMPORARIAMENTE
-- =====================================================
INSERT INTO tb_contrato (
    id,
    seller_id,
    fee_rate,
    terms,
    valid_from,
    valid_to,
    active,
    categoria,
    status,
    created_by,
    approved_by,
    approved_at,
    created_at
) VALUES (
    '770e8400-e29b-41d4-a716-446655440081',
    '660e8400-e29b-41d4-a716-446655440011', -- Loja da Ana
    0.0300, -- 3.0%
    'Contrato suspenso temporariamente para revisão de termos. Vendedora solicitou alteração nas condições de entrega. Suspensão administrativa.',
    DATE_SUB(NOW(), INTERVAL 30 DAY),
    DATE_ADD(NOW(), INTERVAL 335 DAY),
    FALSE,
    'Decoração',
    'SUSPENDED',
    '550e8400-e29b-41d4-a716-446655440001', -- Maria admin
    '550e8400-e29b-41d4-a716-446655440001', -- Maria admin
    DATE_SUB(NOW(), INTERVAL 29 DAY),
    DATE_SUB(NOW(), INTERVAL 30 DAY)
);

-- =====================================================
-- CONTRATO 10: PATRICIA - VENCENDO EM BREVE
-- =====================================================
INSERT INTO tb_contrato (
    id,
    seller_id,
    fee_rate,
    terms,
    valid_from,
    valid_to,
    active,
    categoria,
    status,
    created_by,
    approved_by,
    approved_at,
    created_at
) VALUES (
    '770e8400-e29b-41d4-a716-446655440082',
    '660e8400-e29b-41d4-a716-446655440040', -- Patricia Almeida
    0.0350, -- 3.5%
    'Contrato específico para eletrônicos que vence em breve. Necessária renovação para continuidade das vendas nesta categoria.',
    DATE_SUB(NOW(), INTERVAL 350 DAY),
    DATE_ADD(NOW(), INTERVAL 15 DAY), -- Vence em 15 dias
    TRUE,
    'Eletrônicos',
    'ACTIVE',
    '550e8400-e29b-41d4-a716-446655440002', -- João suporte
    '550e8400-e29b-41d4-a716-446655440001', -- Maria admin
    DATE_SUB(NOW(), INTERVAL 349 DAY),
    DATE_SUB(NOW(), INTERVAL 350 DAY)
);

-- =====================================================
-- ATUALIZAÇÃO DOS VENDEDORES COM CONTRATOS ATIVOS
-- =====================================================

-- Atualizar ABC Importadora com contrato ativo
UPDATE tb_vendedor 
SET contract_id = '770e8400-e29b-41d4-a716-446655440010'
WHERE id = '660e8400-e29b-41d4-a716-446655440010';

-- Atualizar Loja da Ana com contrato ativo
UPDATE tb_vendedor 
SET contract_id = '770e8400-e29b-41d4-a716-446655440011'
WHERE id = '660e8400-e29b-41d4-a716-446655440011';

-- Atualizar Patricia com contrato ativo
UPDATE tb_vendedor 
SET contract_id = '770e8400-e29b-41d4-a716-446655440040'
WHERE id = '660e8400-e29b-41d4-a716-446655440040';

-- Atualizar Sebo do João com contrato ativo
UPDATE tb_vendedor 
SET contract_id = '770e8400-e29b-41d4-a716-446655440060'
WHERE id = '660e8400-e29b-41d4-a716-446655440060';

-- Atualizar Harmonia Musical com contrato ativo
UPDATE tb_vendedor 
SET contract_id = '770e8400-e29b-41d4-a716-446655440061'
WHERE id = '660e8400-e29b-41d4-a716-446655440061';

-- =====================================================
-- COMENTÁRIOS FINAIS
-- =====================================================
/*
CONTRATOS CRIADOS PARA DESENVOLVIMENTO:

CONTRATOS ATIVOS (6 total):
1. ABC Importadora - Geral (3.5%) - Válido até +185 dias
2. Loja da Ana - Artesanatos (2.5%) - Válido até +275 dias
3. Patricia - Geral (4.0%) - Válido até +245 dias
4. Sebo do João - Livros (3.0%) - Válido até +165 dias
5. Harmonia Musical - Instrumentos (4.5%) - Válido até +215 dias
6. Patricia - Eletrônicos (3.5%) - VENCE EM 15 DIAS

CONTRATOS NÃO ATIVOS (4 total):
1. Antiquário Silva - DRAFT (aguardando aprovação)
2. Vendas Rápidas - CANCELLED (empresa suspensa)
3. ABC Importadora - EXPIRED (contrato anterior)
4. Loja da Ana - SUSPENDED (revisão de termos)

CENÁRIOS DE TESTE:
- Contratos com diferentes taxas (2.5% a 6.0%)
- Contratos gerais e específicos por categoria
- Contratos vencendo em breve (renovação)
- Contratos históricos (expirados)
- Contratos suspensos e cancelados
- Múltiplos contratos por vendedor
- Diferentes administradores criando/aprovando

CATEGORIAS COBERTAS:
- Geral (sem categoria específica)
- Artesanatos
- Livros
- Instrumentos Musicais
- Antiguidades
- Decoração
- Eletrônicos

TOTAL DE CONTRATOS: 10
CONTRATOS ATIVOS: 6
VENDEDORES COM CONTRATO ATIVO: 5
*/