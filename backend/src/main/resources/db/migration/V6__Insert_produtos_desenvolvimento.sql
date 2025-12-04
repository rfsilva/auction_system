-- =====================================================
-- Sistema de Leilão Eletrônico - Massa de Dados V6
-- Entidade: PRODUTOS
-- Versão: 6.0 - Produtos para Desenvolvimento
-- Data: 2024-12-19
-- Descrição: Massa de dados de produtos com diferentes categorias, status e cenários
-- =====================================================

-- =====================================================
-- PRODUTOS DA ABC IMPORTADORA (ELETRÔNICOS)
-- =====================================================

-- Produto 1: Smartphone Ativo
INSERT INTO tb_produto (
    id,
    seller_id,
    lote_id,
    title,
    description,
    images,
    weight,
    dimensions,
    initial_price,
    current_price,
    reserve_price,
    increment_min,
    end_datetime,
    status,
    moderated,
    moderator_id,
    moderated_at,
    anti_snipe_enabled,
    anti_snipe_extension,
    categoria,
    tags,
    created_at
) VALUES (
    '990e8400-e29b-41d4-a716-446655440001',
    '660e8400-e29b-41d4-a716-446655440010', -- ABC Importadora
    NULL, -- Será associado ao lote depois
    'Smartphone Samsung Galaxy S23 Ultra 256GB - Novo Lacrado',
    'Smartphone Samsung Galaxy S23 Ultra com 256GB de armazenamento, 12GB de RAM, câmera de 200MP, tela Dynamic AMOLED 2X de 6.8 polegadas. Produto novo, lacrado, com garantia de 1 ano. Acompanha carregador original, cabo USB-C e fones de ouvido. Cor: Phantom Black.',
    '["https://example.com/images/samsung-s23-ultra-1.jpg", "https://example.com/images/samsung-s23-ultra-2.jpg", "https://example.com/images/samsung-s23-ultra-3.jpg"]',
    0.234,
    '{"length": 16.3, "width": 7.8, "height": 0.89}',
    2800.00,
    3250.00,
    3000.00,
    50.00,
    DATE_ADD(NOW(), INTERVAL 3 DAY),
    'ACTIVE',
    TRUE,
    '550e8400-e29b-41d4-a716-446655440001', -- Maria admin
    DATE_SUB(NOW(), INTERVAL 2 DAY),
    TRUE,
    300,
    'Eletrônicos',
    'smartphone,samsung,galaxy,s23,ultra,256gb,novo,lacrado',
    DATE_SUB(NOW(), INTERVAL 5 DAY)
);

-- Produto 2: Notebook Draft
INSERT INTO tb_produto (
    id,
    seller_id,
    lote_id,
    title,
    description,
    images,
    weight,
    dimensions,
    initial_price,
    current_price,
    reserve_price,
    increment_min,
    end_datetime,
    status,
    moderated,
    moderator_id,
    moderated_at,
    anti_snipe_enabled,
    anti_snipe_extension,
    categoria,
    tags,
    created_at
) VALUES (
    '990e8400-e29b-41d4-a716-446655440002',
    '660e8400-e29b-41d4-a716-446655440010', -- ABC Importadora
    NULL,
    'Notebook Dell Inspiron 15 3000 - i5 8GB 256GB SSD',
    'Notebook Dell Inspiron 15 3000 com processador Intel Core i5 de 11ª geração, 8GB de RAM DDR4, SSD de 256GB, tela de 15.6 polegadas Full HD. Ideal para trabalho e estudos. Produto seminovo em excelente estado.',
    '["https://example.com/images/dell-inspiron-1.jpg", "https://example.com/images/dell-inspiron-2.jpg"]',
    2.100,
    '{"length": 35.8, "width": 24.2, "height": 1.99}',
    1800.00,
    1800.00,
    1600.00,
    25.00,
    DATE_ADD(NOW(), INTERVAL 7 DAY),
    'DRAFT',
    FALSE,
    NULL,
    NULL,
    TRUE,
    300,
    'Eletrônicos',
    'notebook,dell,inspiron,i5,8gb,256gb,ssd,seminovo',
    DATE_SUB(NOW(), INTERVAL 1 DAY)
);

-- Produto 3: TV Pendente Aprovação
INSERT INTO tb_produto (
    id,
    seller_id,
    lote_id,
    title,
    description,
    images,
    weight,
    dimensions,
    initial_price,
    current_price,
    reserve_price,
    increment_min,
    end_datetime,
    status,
    moderated,
    moderator_id,
    moderated_at,
    anti_snipe_enabled,
    anti_snipe_extension,
    categoria,
    tags,
    created_at
) VALUES (
    '990e8400-e29b-41d4-a716-446655440003',
    '660e8400-e29b-41d4-a716-446655440010', -- ABC Importadora
    NULL,
    'Smart TV LG 55" 4K UHD OLED C3 - Nova na Caixa',
    'Smart TV LG OLED C3 de 55 polegadas com resolução 4K UHD, HDR10 Pro, Dolby Vision IQ, webOS 23, processador α9 Gen6 AI. Produto novo na caixa lacrada, com garantia de 2 anos. Acompanha controle Magic Remote e suporte para parede.',
    '["https://example.com/images/lg-oled-c3-1.jpg", "https://example.com/images/lg-oled-c3-2.jpg", "https://example.com/images/lg-oled-c3-3.jpg", "https://example.com/images/lg-oled-c3-4.jpg"]',
    18.900,
    '{"length": 122.8, "width": 70.4, "height": 4.6}',
    4500.00,
    4500.00,
    NULL,
    100.00,
    DATE_ADD(NOW(), INTERVAL 5 DAY),
    'PENDING_APPROVAL',
    FALSE,
    NULL,
    NULL,
    TRUE,
    300,
    'Eletrônicos',
    'smart,tv,lg,55,4k,uhd,oled,c3,nova,caixa',
    NOW()
);

-- =====================================================
-- PRODUTOS DA LOJA DA ANA (ARTESANATOS)
-- =====================================================

-- Produto 4: Vaso Artesanal Ativo
INSERT INTO tb_produto (
    id,
    seller_id,
    lote_id,
    title,
    description,
    images,
    weight,
    dimensions,
    initial_price,
    current_price,
    reserve_price,
    increment_min,
    end_datetime,
    status,
    moderated,
    moderator_id,
    moderated_at,
    anti_snipe_enabled,
    anti_snipe_extension,
    categoria,
    tags,
    created_at
) VALUES (
    '990e8400-e29b-41d4-a716-446655440004',
    '660e8400-e29b-41d4-a716-446655440011', -- Loja da Ana
    NULL,
    'Vaso Decorativo Artesanal em Cerâmica - Peça Única',
    'Lindo vaso decorativo feito à mão em cerâmica, com técnica de esmaltação especial que cria um efeito degradê único. Peça exclusiva criada por artesã local. Ideal para plantas ou como objeto decorativo. Dimensões perfeitas para ambientes internos.',
    '["https://example.com/images/vaso-ceramica-1.jpg", "https://example.com/images/vaso-ceramica-2.jpg", "https://example.com/images/vaso-ceramica-3.jpg"]',
    1.200,
    '{"length": 20, "width": 20, "height": 25}',
    80.00,
    125.00,
    90.00,
    5.00,
    DATE_ADD(NOW(), INTERVAL 2 DAY),
    'ACTIVE',
    TRUE,
    '550e8400-e29b-41d4-a716-446655440002', -- João suporte
    DATE_SUB(NOW(), INTERVAL 1 DAY),
    TRUE,
    300,
    'Artesanatos',
    'vaso,decorativo,artesanal,ceramica,unico,feito,mao',
    DATE_SUB(NOW(), INTERVAL 3 DAY)
);

-- Produto 5: Quadro Decorativo
INSERT INTO tb_produto (
    id,
    seller_id,
    lote_id,
    title,
    description,
    images,
    weight,
    dimensions,
    initial_price,
    current_price,
    reserve_price,
    increment_min,
    end_datetime,
    status,
    moderated,
    moderator_id,
    moderated_at,
    anti_snipe_enabled,
    anti_snipe_extension,
    categoria,
    tags,
    created_at
) VALUES (
    '990e8400-e29b-41d4-a716-446655440005',
    '660e8400-e29b-41d4-a716-446655440011', -- Loja da Ana
    NULL,
    'Quadro Decorativo Mandala em Madeira - Arte Sustentável',
    'Quadro decorativo com mandala entalhada em madeira de reflorestamento. Trabalho artesanal detalhado com acabamento em verniz natural. Peça sustentável que combina arte e consciência ambiental. Pronto para pendurar.',
    '["https://example.com/images/quadro-mandala-1.jpg", "https://example.com/images/quadro-mandala-2.jpg"]',
    0.800,
    '{"length": 40, "width": 40, "height": 2}',
    120.00,
    145.00,
    NULL,
    10.00,
    DATE_ADD(NOW(), INTERVAL 4 DAY),
    'ACTIVE',
    TRUE,
    '550e8400-e29b-41d4-a716-446655440001', -- Maria admin
    DATE_SUB(NOW(), INTERVAL 3 DAY),
    TRUE,
    300,
    'Artesanatos',
    'quadro,decorativo,mandala,madeira,sustentavel,artesanal',
    DATE_SUB(NOW(), INTERVAL 6 DAY)
);

-- =====================================================
-- PRODUTOS DO SEBO DO JOÃO (LIVROS)
-- =====================================================

-- Produto 6: Livro Raro Ativo
INSERT INTO tb_produto (
    id,
    seller_id,
    lote_id,
    title,
    description,
    images,
    weight,
    dimensions,
    initial_price,
    current_price,
    reserve_price,
    increment_min,
    end_datetime,
    status,
    moderated,
    moderator_id,
    moderated_at,
    anti_snipe_enabled,
    anti_snipe_extension,
    categoria,
    tags,
    created_at
) VALUES (
    '990e8400-e29b-41d4-a716-446655440006',
    '660e8400-e29b-41d4-a716-446655440060', -- Sebo do João
    NULL,
    'Dom Casmurro - Machado de Assis - 1ª Edição 1899',
    'Exemplar raro da primeira edição de "Dom Casmurro" de Machado de Assis, publicado em 1899 pela H. Garnier. Livro em estado de conservação muito bom, considerando a idade. Algumas páginas com amarelamento natural do tempo, mas texto perfeitamente legível. Capa original preservada. Peça de grande valor histórico e literário.',
    '["https://example.com/images/dom-casmurro-1899-1.jpg", "https://example.com/images/dom-casmurro-1899-2.jpg", "https://example.com/images/dom-casmurro-1899-3.jpg"]',
    0.350,
    '{"length": 19, "width": 12, "height": 2.5}',
    2500.00,
    3800.00,
    3000.00,
    100.00,
    DATE_ADD(NOW(), INTERVAL 6 DAY),
    'ACTIVE',
    TRUE,
    '550e8400-e29b-41d4-a716-446655440000', -- Admin root
    DATE_SUB(NOW(), INTERVAL 1 DAY),
    TRUE,
    300,
    'Livros',
    'dom,casmurro,machado,assis,primeira,edicao,1899,raro',
    DATE_SUB(NOW(), INTERVAL 4 DAY)
);

-- Produto 7: Coleção de Livros
INSERT INTO tb_produto (
    id,
    seller_id,
    lote_id,
    title,
    description,
    images,
    weight,
    dimensions,
    initial_price,
    current_price,
    reserve_price,
    increment_min,
    end_datetime,
    status,
    moderated,
    moderator_id,
    moderated_at,
    anti_snipe_enabled,
    anti_snipe_extension,
    categoria,
    tags,
    created_at
) VALUES (
    '990e8400-e29b-41d4-a716-446655440007',
    '660e8400-e29b-41d4-a716-446655440060', -- Sebo do João
    NULL,
    'Coleção Completa Agatha Christie - 66 Volumes Encadernados',
    'Coleção completa das obras de Agatha Christie em 66 volumes encadernados em couro sintético. Edição especial da Editora Nova Cultural dos anos 1980. Todos os volumes em excelente estado de conservação, com páginas sem amarelamento. Inclui todos os clássicos: Hercule Poirot, Miss Marple e outros mistérios.',
    '["https://example.com/images/colecao-agatha-1.jpg", "https://example.com/images/colecao-agatha-2.jpg", "https://example.com/images/colecao-agatha-3.jpg"]',
    15.800,
    '{"length": 50, "width": 30, "height": 40}',
    800.00,
    950.00,
    NULL,
    25.00,
    DATE_ADD(NOW(), INTERVAL 8 DAY),
    'ACTIVE',
    TRUE,
    '550e8400-e29b-41d4-a716-446655440002', -- João suporte
    DATE_SUB(NOW(), INTERVAL 2 DAY),
    TRUE,
    300,
    'Livros',
    'colecao,agatha,christie,66,volumes,encadernados,completa',
    DATE_SUB(NOW(), INTERVAL 7 DAY)
);

-- =====================================================
-- PRODUTOS DA HARMONIA MUSICAL (INSTRUMENTOS)
-- =====================================================

-- Produto 8: Guitarra Vintage Ativa
INSERT INTO tb_produto (
    id,
    seller_id,
    lote_id,
    title,
    description,
    images,
    weight,
    dimensions,
    initial_price,
    current_price,
    reserve_price,
    increment_min,
    end_datetime,
    status,
    moderated,
    moderator_id,
    moderated_at,
    anti_snipe_enabled,
    anti_snipe_extension,
    categoria,
    tags,
    created_at
) VALUES (
    '990e8400-e29b-41d4-a716-446655440008',
    '660e8400-e29b-41d4-a716-446655440061', -- Harmonia Musical
    NULL,
    'Guitarra Fender Stratocaster 1978 - Sunburst Original',
    'Guitarra Fender Stratocaster de 1978 em acabamento Sunburst original. Instrumento em excelente estado de conservação, com todas as peças originais. Captadores single-coil originais, ponte tremolo funcionando perfeitamente. Acompanha case rígido original da época. Instrumento revisado por luthier especializado.',
    '["https://example.com/images/fender-strat-1978-1.jpg", "https://example.com/images/fender-strat-1978-2.jpg", "https://example.com/images/fender-strat-1978-3.jpg", "https://example.com/images/fender-strat-1978-4.jpg"]',
    3.200,
    '{"length": 100, "width": 35, "height": 5}',
    8000.00,
    12500.00,
    10000.00,
    250.00,
    DATE_ADD(NOW(), INTERVAL 10 DAY),
    'ACTIVE',
    TRUE,
    '550e8400-e29b-41d4-a716-446655440001', -- Maria admin
    DATE_SUB(NOW(), INTERVAL 3 DAY),
    TRUE,
    600, -- Anti-snipe estendido para item valioso
    'Instrumentos Musicais',
    'guitarra,fender,stratocaster,1978,sunburst,vintage,original',
    DATE_SUB(NOW(), INTERVAL 8 DAY)
);

-- =====================================================
-- PRODUTOS DA PATRICIA (DIVERSOS)
-- =====================================================

-- Produto 9: iPhone Usado
INSERT INTO tb_produto (
    id,
    seller_id,
    lote_id,
    title,
    description,
    images,
    weight,
    dimensions,
    initial_price,
    current_price,
    reserve_price,
    increment_min,
    end_datetime,
    status,
    moderated,
    moderator_id,
    moderated_at,
    anti_snipe_enabled,
    anti_snipe_extension,
    categoria,
    tags,
    created_at
) VALUES (
    '990e8400-e29b-41d4-a716-446655440009',
    '660e8400-e29b-41d4-a716-446655440040', -- Patricia Almeida
    NULL,
    'iPhone 13 Pro 128GB - Usado em Ótimo Estado',
    'iPhone 13 Pro de 128GB em ótimo estado de conservação. Aparelho usado por apenas 8 meses, sempre com película e capinha. Bateria com 94% de capacidade. Acompanha carregador original, caixa e documentos. Sem riscos na tela ou no corpo.',
    '["https://example.com/images/iphone-13-pro-1.jpg", "https://example.com/images/iphone-13-pro-2.jpg"]',
    0.204,
    '{"length": 14.67, "width": 7.15, "height": 0.76}',
    2200.00,
    2450.00,
    NULL,
    50.00,
    DATE_ADD(NOW(), INTERVAL 1 DAY),
    'ACTIVE',
    TRUE,
    '550e8400-e29b-41d4-a716-446655440002', -- João suporte
    DATE_SUB(NOW(), INTERVAL 1 DAY),
    TRUE,
    300,
    'Eletrônicos',
    'iphone,13,pro,128gb,usado,otimo,estado,apple',
    DATE_SUB(NOW(), INTERVAL 2 DAY)
);

-- =====================================================
-- PRODUTOS COM DIFERENTES STATUS (CENÁRIOS DE TESTE)
-- =====================================================

-- Produto 10: Produto Vendido
INSERT INTO tb_produto (
    id,
    seller_id,
    lote_id,
    title,
    description,
    images,
    weight,
    dimensions,
    initial_price,
    current_price,
    reserve_price,
    increment_min,
    end_datetime,
    status,
    moderated,
    moderator_id,
    moderated_at,
    anti_snipe_enabled,
    anti_snipe_extension,
    categoria,
    tags,
    created_at
) VALUES (
    '990e8400-e29b-41d4-a716-446655440010',
    '660e8400-e29b-41d4-a716-446655440011', -- Loja da Ana
    NULL,
    'Luminária Artesanal em Bambu - VENDIDA',
    'Luminária artesanal feita em bambu natural com design moderno. Produto já vendido para demonstrar histórico de vendas.',
    '["https://example.com/images/luminaria-bambu-1.jpg"]',
    0.600,
    '{"length": 25, "width": 25, "height": 35}',
    150.00,
    280.00,
    NULL,
    10.00,
    DATE_SUB(NOW(), INTERVAL 2 DAY),
    'SOLD',
    TRUE,
    '550e8400-e29b-41d4-a716-446655440001', -- Maria admin
    DATE_SUB(NOW(), INTERVAL 5 DAY),
    TRUE,
    300,
    'Artesanatos',
    'luminaria,artesanal,bambu,vendida',
    DATE_SUB(NOW(), INTERVAL 10 DAY)
);

-- Produto 11: Produto Expirado
INSERT INTO tb_produto (
    id,
    seller_id,
    lote_id,
    title,
    description,
    images,
    weight,
    dimensions,
    initial_price,
    current_price,
    reserve_price,
    increment_min,
    end_datetime,
    status,
    moderated,
    moderator_id,
    moderated_at,
    anti_snipe_enabled,
    anti_snipe_extension,
    categoria,
    tags,
    created_at
) VALUES (
    '990e8400-e29b-41d4-a716-446655440011',
    '660e8400-e29b-41d4-a716-446655440010', -- ABC Importadora
    NULL,
    'Tablet Samsung Galaxy Tab A8 - EXPIRADO',
    'Tablet Samsung que não recebeu lances suficientes e expirou.',
    '["https://example.com/images/tablet-samsung-1.jpg"]',
    0.500,
    '{"length": 24.6, "width": 16.1, "height": 0.69}',
    500.00,
    500.00,
    NULL,
    25.00,
    DATE_SUB(NOW(), INTERVAL 1 DAY),
    'EXPIRED',
    TRUE,
    '550e8400-e29b-41d4-a716-446655440001', -- Maria admin
    DATE_SUB(NOW(), INTERVAL 7 DAY),
    TRUE,
    300,
    'Eletrônicos',
    'tablet,samsung,galaxy,tab,a8,expirado',
    DATE_SUB(NOW(), INTERVAL 8 DAY)
);

-- Produto 12: Produto Cancelado
INSERT INTO tb_produto (
    id,
    seller_id,
    lote_id,
    title,
    description,
    images,
    weight,
    dimensions,
    initial_price,
    current_price,
    reserve_price,
    increment_min,
    end_datetime,
    status,
    moderated,
    moderator_id,
    moderated_at,
    anti_snipe_enabled,
    anti_snipe_extension,
    categoria,
    tags,
    created_at
) VALUES (
    '990e8400-e29b-41d4-a716-446655440012',
    '660e8400-e29b-41d4-a716-446655440013', -- Vendas Rápidas (suspenso)
    NULL,
    'Produto Cancelado por Irregularidades',
    'Produto cancelado devido a problemas com o vendedor.',
    '["https://example.com/images/produto-cancelado.jpg"]',
    1.000,
    '{"length": 30, "width": 20, "height": 10}',
    300.00,
    300.00,
    NULL,
    15.00,
    DATE_ADD(NOW(), INTERVAL 5 DAY),
    'CANCELLED',
    FALSE,
    NULL,
    NULL,
    TRUE,
    300,
    'Diversos',
    'cancelado,irregularidades',
    DATE_SUB(NOW(), INTERVAL 15 DAY)
);

-- =====================================================
-- COMENTÁRIOS FINAIS
-- =====================================================
/*
PRODUTOS CRIADOS PARA DESENVOLVIMENTO:

PRODUTOS ATIVOS (7 total):
1. Samsung Galaxy S23 Ultra - ABC Importadora (R$ 3.250 atual)
2. Vaso Cerâmica - Loja da Ana (R$ 125 atual)
3. Quadro Mandala - Loja da Ana (R$ 145 atual)
4. Dom Casmurro 1ª Ed. - Sebo João (R$ 3.800 atual)
5. Coleção Agatha Christie - Sebo João (R$ 950 atual)
6. Fender Stratocaster 1978 - Harmonia Musical (R$ 12.500 atual)
7. iPhone 13 Pro - Patricia (R$ 2.450 atual)

PRODUTOS NÃO ATIVOS (5 total):
1. Notebook Dell - DRAFT (aguardando publicação)
2. Smart TV LG - PENDING_APPROVAL (aguardando moderação)
3. Luminária Bambu - SOLD (vendida)
4. Tablet Samsung - EXPIRED (expirado)
5. Produto Irregularidades - CANCELLED (cancelado)

CATEGORIAS REPRESENTADAS:
- Eletrônicos (5 produtos)
- Artesanatos (3 produtos)
- Livros (2 produtos)
- Instrumentos Musicais (1 produto)
- Diversos (1 produto)

FAIXAS DE PREÇO:
- Baixo: R$ 80 - R$ 500
- Médio: R$ 800 - R$ 3.000
- Alto: R$ 3.000 - R$ 15.000

CENÁRIOS DE TESTE:
- Produtos com lances (preço atual > inicial)
- Produtos sem lances (preço atual = inicial)
- Diferentes status de moderação
- Anti-snipe habilitado/desabilitado
- Produtos com/sem preço de reserva
- Diferentes prazos de encerramento

TOTAL DE PRODUTOS: 12
PRODUTOS ATIVOS: 7
PRODUTOS MODERADOS: 9
VENDEDORES COM PRODUTOS: 5
*/