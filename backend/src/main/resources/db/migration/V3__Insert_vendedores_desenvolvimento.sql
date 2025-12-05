-- =====================================================
-- Sistema de Leilão Eletrônico - Massa de Dados V3
-- Entidade: VENDEDORES
-- Versão: 3.0 - Vendedores para Desenvolvimento
-- Data: 2024-12-19
-- Descrição: Massa de dados de vendedores com diferentes perfis empresariais
-- =====================================================

-- =====================================================
-- USUÁRIOS ADICIONAIS PARA VENDEDORES EXTRAS
-- =====================================================

-- Usuário para Sebo do João
INSERT INTO tb_usuario (id, nome, email, senha_hash, status, email_verificado, telefone_verificado, telefone, ultimo_login) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440060',
    'João Silva Livreiro',
    'joao@sebodojoao.com.br',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    TRUE,
    TRUE,
    '(11) 91234-5678',
    DATE_SUB(NOW(), INTERVAL 3 DAY)
);

INSERT INTO tb_usuario_role (usuario_id, role) VALUES 
    ('550e8400-e29b-41d4-a716-446655440060', 'SELLER'),
    ('550e8400-e29b-41d4-a716-446655440060', 'PARTICIPANT');

-- Usuário para Harmonia Musical
INSERT INTO tb_usuario (id, nome, email, senha_hash, status, email_verificado, telefone_verificado, telefone, ultimo_login) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440061',
    'Marcos Harmonia Musical',
    'marcos@harmoniamusical.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    TRUE,
    TRUE,
    '(11) 92345-6789',
    DATE_SUB(NOW(), INTERVAL 1 DAY)
);

INSERT INTO tb_usuario_role (usuario_id, role) VALUES 
    ('550e8400-e29b-41d4-a716-446655440061', 'SELLER'),
    ('550e8400-e29b-41d4-a716-446655440061', 'PARTICIPANT');

-- =====================================================
-- VENDEDOR 1: EMPRESA GRANDE - VERIFICADO
-- =====================================================
INSERT INTO tb_vendedor (
    id, 
    usuario_id, 
    company_name, 
    tax_id, 
    contact_email, 
    contact_phone, 
    description, 
    active, 
    verificado,
    created_at
) VALUES (
    '660e8400-e29b-41d4-a716-446655440010',
    '550e8400-e29b-41d4-a716-446655440010', -- Carlos Eduardo Ferreira
    'ABC Importadora e Exportadora Ltda',
    '12.345.678/0001-90',
    'vendas@empresaabc.com',
    '(11) 3456-7890',
    'Empresa especializada em importação e exportação de produtos eletrônicos, eletrodomésticos e equipamentos industriais. Atuamos no mercado há mais de 15 anos, oferecendo produtos de alta qualidade com garantia e suporte técnico especializado.',
    TRUE,
    TRUE,
    DATE_SUB(NOW(), INTERVAL 180 DAY)
);

-- =====================================================
-- VENDEDOR 2: LOJA PEQUENA - VERIFICADO
-- =====================================================
INSERT INTO tb_vendedor (
    id, 
    usuario_id, 
    company_name, 
    tax_id, 
    contact_email, 
    contact_phone, 
    description, 
    active, 
    verificado,
    created_at
) VALUES (
    '660e8400-e29b-41d4-a716-446655440011',
    '550e8400-e29b-41d4-a716-446655440011', -- Ana Paula Costa
    'Loja da Ana - Artesanatos e Decoração',
    '987.654.321-00',
    'contato@lojadaana.com.br',
    '(11) 98765-1234',
    'Loja especializada em artesanatos únicos, peças de decoração artesanais e produtos personalizados. Trabalhamos com artesãos locais e priorizamos a sustentabilidade em nossos produtos. Cada peça é cuidadosamente selecionada.',
    TRUE,
    TRUE,
    DATE_SUB(NOW(), INTERVAL 90 DAY)
);

-- =====================================================
-- VENDEDOR 3: ANTIQUÁRIO - NÃO VERIFICADO
-- =====================================================
INSERT INTO tb_vendedor (
    id, 
    usuario_id, 
    company_name, 
    tax_id, 
    contact_email, 
    contact_phone, 
    description, 
    active, 
    verificado,
    created_at
) VALUES (
    '660e8400-e29b-41d4-a716-446655440012',
    '550e8400-e29b-41d4-a716-446655440012', -- Roberto Silva Antunes
    'Antiquário Silva - Peças Raras e Colecionáveis',
    '456.789.123-45',
    'roberto@antiqualhas.net',
    '(11) 97531-8642',
    'Antiquário especializado em peças raras, móveis antigos, objetos de arte e colecionáveis. Possuímos um acervo diversificado com peças dos séculos XVIII, XIX e XX. Avaliamos e compramos coleções particulares.',
    TRUE,
    FALSE,
    DATE_SUB(NOW(), INTERVAL 15 DAY)
);

-- =====================================================
-- VENDEDOR 4: EMPRESA SUSPEITA - SUSPENSO
-- =====================================================
INSERT INTO tb_vendedor (
    id, 
    usuario_id, 
    company_name, 
    tax_id, 
    contact_email, 
    contact_phone, 
    description, 
    active, 
    verificado,
    created_at
) VALUES (
    '660e8400-e29b-41d4-a716-446655440013',
    '550e8400-e29b-41d4-a716-446655440013', -- Pedro Henrique Alves
    'Vendas Rápidas Online',
    '789.123.456-78',
    'contato@vendasrapidas.com',
    '(11) 96420-7531',
    'Empresa de vendas online com foco em liquidação de estoques e produtos com preços competitivos. Trabalhamos com diversas categorias de produtos.',
    FALSE,
    FALSE,
    DATE_SUB(NOW(), INTERVAL 60 DAY)
);

-- =====================================================
-- VENDEDOR 5: COMERCIANTE HÍBRIDO - VERIFICADO
-- =====================================================
INSERT INTO tb_vendedor (
    id, 
    usuario_id, 
    company_name, 
    tax_id, 
    contact_email, 
    contact_phone, 
    description, 
    active, 
    verificado,
    created_at
) VALUES (
    '660e8400-e29b-41d4-a716-446655440040',
    '550e8400-e29b-41d4-a716-446655440040', -- Patricia Almeida Comerciante
    'Patricia Almeida - Compra e Venda',
    '321.654.987-12',
    'patricia@compraevende.com',
    '(11) 94678-9012',
    'Comerciante autônoma especializada em compra e venda de produtos diversos. Atuo tanto como compradora em leilões para revenda quanto como vendedora de produtos selecionados. Foco em eletrônicos, livros e objetos de coleção.',
    TRUE,
    TRUE,
    DATE_SUB(NOW(), INTERVAL 120 DAY)
);

-- =====================================================
-- VENDEDOR 6: LOJA DE LIVROS USADOS
-- =====================================================
INSERT INTO tb_vendedor (
    id, 
    usuario_id, 
    company_name, 
    tax_id, 
    contact_email, 
    contact_phone, 
    description, 
    active, 
    verificado,
    created_at
) VALUES (
    '660e8400-e29b-41d4-a716-446655440060',
    '550e8400-e29b-41d4-a716-446655440060', -- João Silva Livreiro
    'Sebo do João - Livros Raros',
    '147.258.369-01',
    'joao@sebodojoao.com.br',
    '(11) 91234-5678',
    'Sebo especializado em livros raros, primeiras edições e obras esgotadas. Compramos e vendemos bibliotecas particulares. Temos expertise em literatura brasileira e estrangeira.',
    TRUE,
    TRUE,
    DATE_SUB(NOW(), INTERVAL 200 DAY)
);

-- =====================================================
-- VENDEDOR 7: LOJA DE INSTRUMENTOS MUSICAIS
-- =====================================================
INSERT INTO tb_vendedor (
    id, 
    usuario_id, 
    company_name, 
    tax_id, 
    contact_email, 
    contact_phone, 
    description, 
    active, 
    verificado,
    created_at
) VALUES (
    '660e8400-e29b-41d4-a716-446655440061',
    '550e8400-e29b-41d4-a716-446655440061', -- Marcos Harmonia Musical
    'Harmonia Musical - Instrumentos Vintage',
    '258.369.147-02',
    'vendas@harmoniamusical.com',
    '(11) 92345-6789',
    'Loja especializada em instrumentos musicais vintage e raros. Trabalhamos com guitarras, baixos, baterias e instrumentos acústicos de época. Todos os instrumentos passam por revisão técnica.',
    TRUE,
    TRUE,
    DATE_SUB(NOW(), INTERVAL 150 DAY)
);

-- =====================================================
-- COMENTÁRIOS FINAIS
-- =====================================================
/*
VENDEDORES CRIADOS PARA DESENVOLVIMENTO:

VENDEDORES ATIVOS E VERIFICADOS (5 total):
1. ABC Importadora (Carlos) - Empresa grande, eletrônicos
2. Loja da Ana (Ana) - Artesanatos, pequena empresa
3. Patricia Almeida - Comerciante híbrida
4. Sebo do João (João) - Livros raros
5. Harmonia Musical (Marcos) - Instrumentos vintage

VENDEDORES NÃO VERIFICADOS (1 total):
1. Antiquário Silva (Roberto) - Aguardando verificação

VENDEDORES SUSPENSOS (1 total):
1. Vendas Rápidas (Pedro) - Empresa suspensa

USUÁRIOS ADICIONAIS CRIADOS:
- João Silva Livreiro (joao@sebodojoao.com.br)
- Marcos Harmonia Musical (marcos@harmoniamusical.com)

CATEGORIAS REPRESENTADAS:
- Eletrônicos e Eletrodomésticos
- Artesanatos e Decoração
- Antiguidades e Colecionáveis
- Livros e Literatura
- Instrumentos Musicais
- Produtos Diversos

CENÁRIOS DE TESTE:
- Vendedor verificado com histórico longo
- Vendedor novo aguardando verificação
- Vendedor suspenso por problemas
- Vendedor híbrido (compra e vende)
- Diferentes portes de empresa
- Diferentes especialidades

TOTAL DE VENDEDORES: 7
TOTAL DE USUÁRIOS ADICIONAIS: 2
*/