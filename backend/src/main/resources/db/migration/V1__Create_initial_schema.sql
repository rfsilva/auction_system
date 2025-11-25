-- =====================================================
-- Sistema de Leilão Eletrônico - Schema Inicial
-- Versão: 1.2 - Padronização tb_ + singular
-- Data: 2024-11-25
-- =====================================================

-- =====================================================
-- TABELA: TB_USUARIO
-- =====================================================
CREATE TABLE tb_usuario (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    telefone VARCHAR(20),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING_VERIFICATION'
        CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED', 'PENDING_VERIFICATION')),
    email_verificado BOOLEAN NOT NULL DEFAULT FALSE,
    telefone_verificado BOOLEAN NOT NULL DEFAULT FALSE,
    ultimo_login TIMESTAMP,
    tentativas_login INTEGER NOT NULL DEFAULT 0,
    bloqueado_ate TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para tb_usuario
CREATE INDEX idx_tb_usuario_email ON tb_usuario(email);
CREATE INDEX idx_tb_usuario_status ON tb_usuario(status);
CREATE INDEX idx_tb_usuario_created_at ON tb_usuario(created_at);

-- =====================================================
-- TABELA: TB_USUARIO_ROLE (para JPA @ElementCollection)
-- =====================================================
CREATE TABLE tb_usuario_role (
    usuario_id UUID NOT NULL REFERENCES tb_usuario(id) ON DELETE CASCADE,
    role VARCHAR(50) NOT NULL
        CHECK (role IN ('VISITOR', 'PARTICIPANT', 'BUYER', 'SELLER', 'ADMIN')),
    PRIMARY KEY (usuario_id, role)
);

-- Índices para tb_usuario_role
CREATE INDEX idx_tb_usuario_role_usuario_id ON tb_usuario_role(usuario_id);
CREATE INDEX idx_tb_usuario_role_role ON tb_usuario_role(role);

-- =====================================================
-- TABELA: TB_CONTRATO
-- =====================================================
CREATE TABLE tb_contrato (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    seller_id UUID NOT NULL,
    fee_rate DECIMAL(5,4) NOT NULL CHECK (fee_rate >= 0 AND fee_rate <= 1),
    terms TEXT NOT NULL,
    valid_from TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    valid_to TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para tb_contrato
CREATE INDEX idx_tb_contrato_seller_id ON tb_contrato(seller_id);
CREATE INDEX idx_tb_contrato_active ON tb_contrato(active);
CREATE INDEX idx_tb_contrato_valid_period ON tb_contrato(valid_from, valid_to);

-- =====================================================
-- TABELA: TB_VENDEDOR
-- =====================================================
CREATE TABLE tb_vendedor (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id UUID NOT NULL REFERENCES tb_usuario(id) ON DELETE CASCADE,
    company_name VARCHAR(255),
    tax_id VARCHAR(50),
    contract_id UUID REFERENCES tb_contrato(id),
    fee_rate DECIMAL(5,4) NOT NULL DEFAULT 0.05 CHECK (fee_rate >= 0 AND fee_rate <= 1),
    documents TEXT, -- JSON como TEXT para compatibilidade
    verificado BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT uk_tb_vendedor_usuario_id UNIQUE (usuario_id),
    CONSTRAINT uk_tb_vendedor_tax_id UNIQUE (tax_id)
);

-- Índices para tb_vendedor
CREATE INDEX idx_tb_vendedor_usuario_id ON tb_vendedor(usuario_id);
CREATE INDEX idx_tb_vendedor_contract_id ON tb_vendedor(contract_id);
CREATE INDEX idx_tb_vendedor_verificado ON tb_vendedor(verificado);

-- =====================================================
-- TABELA: TB_COMPRADOR
-- =====================================================
CREATE TABLE tb_comprador (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id UUID NOT NULL REFERENCES tb_usuario(id) ON DELETE CASCADE,
    kyc_status VARCHAR(50) NOT NULL DEFAULT 'PENDING'
        CHECK (kyc_status IN ('PENDING', 'APPROVED', 'REJECTED', 'EXPIRED')),
    preferences TEXT, -- JSON como TEXT para compatibilidade
    limite_credito DECIMAL(15,2),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT uk_tb_comprador_usuario_id UNIQUE (usuario_id)
);

-- Índices para tb_comprador
CREATE INDEX idx_tb_comprador_usuario_id ON tb_comprador(usuario_id);
CREATE INDEX idx_tb_comprador_kyc_status ON tb_comprador(kyc_status);

-- =====================================================
-- TABELA: TB_LOTE
-- =====================================================
CREATE TABLE tb_lote (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    seller_id UUID NOT NULL REFERENCES tb_vendedor(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    lote_end_datetime TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'DRAFT'
        CHECK (status IN ('DRAFT', 'ACTIVE', 'CLOSED', 'CANCELLED')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para tb_lote
CREATE INDEX idx_tb_lote_seller_id ON tb_lote(seller_id);
CREATE INDEX idx_tb_lote_status ON tb_lote(status);
CREATE INDEX idx_tb_lote_end_datetime ON tb_lote(lote_end_datetime);

-- =====================================================
-- TABELA: TB_PRODUTO
-- =====================================================
CREATE TABLE tb_produto (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    seller_id UUID NOT NULL REFERENCES tb_vendedor(id) ON DELETE CASCADE,
    lote_id UUID REFERENCES tb_lote(id) ON DELETE SET NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    images TEXT, -- JSON como TEXT para compatibilidade
    weight DECIMAL(10,3), -- Peso em kg
    dimensions TEXT, -- JSON como TEXT: {length, width, height} em cm
    initial_price DECIMAL(15,2) NOT NULL CHECK (initial_price > 0),
    current_price DECIMAL(15,2) NOT NULL CHECK (current_price >= initial_price),
    reserve_price DECIMAL(15,2), -- Preço de reserva (opcional)
    increment_min DECIMAL(15,2) NOT NULL DEFAULT 1.00 CHECK (increment_min > 0),
    end_datetime TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'DRAFT'
        CHECK (status IN ('DRAFT', 'PENDING_APPROVAL', 'ACTIVE', 'SOLD', 'CANCELLED', 'EXPIRED')),
    moderated BOOLEAN NOT NULL DEFAULT FALSE,
    moderator_id UUID REFERENCES tb_usuario(id),
    moderated_at TIMESTAMP,
    anti_snipe_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    anti_snipe_extension INTEGER NOT NULL DEFAULT 300, -- segundos
    categoria VARCHAR(100),
    tags TEXT, -- Array como TEXT separado por vírgulas
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_tb_produto_end_datetime CHECK (end_datetime > created_at)
);

-- Índices para tb_produto
CREATE INDEX idx_tb_produto_seller_id ON tb_produto(seller_id);
CREATE INDEX idx_tb_produto_lote_id ON tb_produto(lote_id);
CREATE INDEX idx_tb_produto_status ON tb_produto(status);
CREATE INDEX idx_tb_produto_end_datetime ON tb_produto(end_datetime);
CREATE INDEX idx_tb_produto_categoria ON tb_produto(categoria);
CREATE INDEX idx_tb_produto_current_price ON tb_produto(current_price);
CREATE INDEX idx_tb_produto_created_at ON tb_produto(created_at);

-- =====================================================
-- TABELA: TB_LANCE
-- =====================================================
CREATE TABLE tb_lance (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    produto_id UUID NOT NULL REFERENCES tb_produto(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES tb_usuario(id) ON DELETE CASCADE,
    value DECIMAL(15,2) NOT NULL CHECK (value > 0),
    currency VARCHAR(3) NOT NULL DEFAULT 'BRL',
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45), -- IPv6 compatible
    user_agent TEXT,
    sequence_number BIGINT NOT NULL,
    proxy_bid BOOLEAN NOT NULL DEFAULT FALSE,
    max_bid_value DECIMAL(15,2), -- Para lances automáticos
    is_winning BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Sequência para lances (compatível com múltiplos bancos)
CREATE SEQUENCE tb_lance_sequence_number_seq START WITH 1 INCREMENT BY 1;

-- Índices para tb_lance
CREATE INDEX idx_tb_lance_produto_id ON tb_lance(produto_id);
CREATE INDEX idx_tb_lance_user_id ON tb_lance(user_id);
CREATE INDEX idx_tb_lance_timestamp ON tb_lance(timestamp);
CREATE INDEX idx_tb_lance_sequence_number ON tb_lance(sequence_number);
CREATE INDEX idx_tb_lance_is_winning ON tb_lance(is_winning);
CREATE INDEX idx_tb_lance_produto_timestamp ON tb_lance(produto_id, timestamp DESC);

-- =====================================================
-- TABELA: TB_ARREMATE
-- =====================================================
CREATE TABLE tb_arremate (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    produto_id UUID NOT NULL REFERENCES tb_produto(id) ON DELETE CASCADE,
    winning_bid_id UUID NOT NULL REFERENCES tb_lance(id),
    buyer_id UUID NOT NULL REFERENCES tb_comprador(id),
    final_price DECIMAL(15,2) NOT NULL CHECK (final_price > 0),
    fee_amount DECIMAL(15,2) NOT NULL CHECK (fee_amount >= 0),
    freight_estimate DECIMAL(15,2),
    status_payment VARCHAR(50) NOT NULL DEFAULT 'PENDING'
        CHECK (status_payment IN ('PENDING', 'AUTHORIZED', 'CAPTURED', 'FAILED', 'REFUNDED', 'CANCELLED')),
    payment_due_date TIMESTAMP,
    paid_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT uk_tb_arremate_produto_id UNIQUE (produto_id),
    CONSTRAINT uk_tb_arremate_winning_bid_id UNIQUE (winning_bid_id)
);

-- Índices para tb_arremate
CREATE INDEX idx_tb_arremate_produto_id ON tb_arremate(produto_id);
CREATE INDEX idx_tb_arremate_buyer_id ON tb_arremate(buyer_id);
CREATE INDEX idx_tb_arremate_status_payment ON tb_arremate(status_payment);
CREATE INDEX idx_tb_arremate_created_at ON tb_arremate(created_at);

-- =====================================================
-- TABELA: TB_DOCUMENTO
-- =====================================================
CREATE TABLE tb_documento (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    entity_id UUID NOT NULL,
    entity_type VARCHAR(50) NOT NULL, -- 'PRODUTO', 'ARREMATE', 'USUARIO', etc.
    type VARCHAR(50) NOT NULL, -- 'INVOICE', 'RECEIPT', 'CONTRACT', etc.
    filename VARCHAR(255) NOT NULL,
    storage_url TEXT NOT NULL,
    file_hash VARCHAR(64) NOT NULL, -- SHA-256
    file_size BIGINT NOT NULL,
    mime_type VARCHAR(100),
    generated_by UUID REFERENCES tb_usuario(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP
);

-- Índices para tb_documento
CREATE INDEX idx_tb_documento_entity ON tb_documento(entity_type, entity_id);
CREATE INDEX idx_tb_documento_type ON tb_documento(type);
CREATE INDEX idx_tb_documento_created_at ON tb_documento(created_at);
CREATE INDEX idx_tb_documento_expires_at ON tb_documento(expires_at);

-- =====================================================
-- TABELA: TB_AUDIT_LOG
-- =====================================================
CREATE TABLE tb_audit_log (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    entity_type VARCHAR(50) NOT NULL,
    entity_id UUID NOT NULL,
    action VARCHAR(50) NOT NULL, -- 'CREATE', 'UPDATE', 'DELETE', 'LOGIN', etc.
    performed_by UUID REFERENCES tb_usuario(id),
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45), -- IPv6 compatible
    user_agent TEXT,
    old_values TEXT, -- JSON como TEXT
    new_values TEXT, -- JSON como TEXT
    metadata TEXT -- JSON como TEXT
);

-- Índices para tb_audit_log
CREATE INDEX idx_tb_audit_log_entity ON tb_audit_log(entity_type, entity_id);
CREATE INDEX idx_tb_audit_log_performed_by ON tb_audit_log(performed_by);
CREATE INDEX idx_tb_audit_log_timestamp ON tb_audit_log(timestamp);
CREATE INDEX idx_tb_audit_log_action ON tb_audit_log(action);

-- =====================================================
-- TABELA: TB_DISPUTA
-- =====================================================
CREATE TABLE tb_disputa (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    arremate_id UUID NOT NULL REFERENCES tb_arremate(id) ON DELETE CASCADE,
    buyer_id UUID NOT NULL REFERENCES tb_comprador(id),
    seller_id UUID NOT NULL REFERENCES tb_vendedor(id),
    reason TEXT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'OPEN'
        CHECK (status IN ('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED')),
    evidence TEXT, -- JSON como TEXT: Array de evidências
    resolution TEXT,
    resolved_by UUID REFERENCES tb_usuario(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP,
    
    CONSTRAINT uk_tb_disputa_arremate_id UNIQUE (arremate_id)
);

-- Índices para tb_disputa
CREATE INDEX idx_tb_disputa_arremate_id ON tb_disputa(arremate_id);
CREATE INDEX idx_tb_disputa_buyer_id ON tb_disputa(buyer_id);
CREATE INDEX idx_tb_disputa_seller_id ON tb_disputa(seller_id);
CREATE INDEX idx_tb_disputa_status ON tb_disputa(status);

-- =====================================================
-- TABELA: TB_FAVORITO
-- =====================================================
CREATE TABLE tb_favorito (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES tb_usuario(id) ON DELETE CASCADE,
    produto_id UUID NOT NULL REFERENCES tb_produto(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT uk_tb_favorito_user_produto UNIQUE (user_id, produto_id)
);

-- Índices para tb_favorito
CREATE INDEX idx_tb_favorito_user_id ON tb_favorito(user_id);
CREATE INDEX idx_tb_favorito_produto_id ON tb_favorito(produto_id);

-- =====================================================
-- TABELA: TB_NOTIFICACAO
-- =====================================================
CREATE TABLE tb_notificacao (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES tb_usuario(id) ON DELETE CASCADE,
    type VARCHAR(50) NOT NULL, -- 'BID_OUTBID', 'AUCTION_ENDING', 'PAYMENT_DUE', etc.
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING'
        CHECK (status IN ('PENDING', 'SENT', 'READ', 'FAILED')),
    data TEXT, -- JSON como TEXT: Dados adicionais específicos do tipo
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sent_at TIMESTAMP,
    read_at TIMESTAMP
);

-- Índices para tb_notificacao
CREATE INDEX idx_tb_notificacao_user_id ON tb_notificacao(user_id);
CREATE INDEX idx_tb_notificacao_status ON tb_notificacao(status);
CREATE INDEX idx_tb_notificacao_type ON tb_notificacao(type);
CREATE INDEX idx_tb_notificacao_created_at ON tb_notificacao(created_at);

-- =====================================================
-- TABELA: TB_PRE_AUTORIZACAO
-- =====================================================
CREATE TABLE tb_pre_autorizacao (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    buyer_id UUID NOT NULL REFERENCES tb_comprador(id) ON DELETE CASCADE,
    produto_id UUID NOT NULL REFERENCES tb_produto(id) ON DELETE CASCADE,
    amount DECIMAL(15,2) NOT NULL CHECK (amount > 0),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING'
        CHECK (status IN ('PENDING', 'AUTHORIZED', 'EXPIRED', 'CANCELLED', 'USED')),
    payment_method_id VARCHAR(255), -- ID do método de pagamento no gateway
    authorization_code VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    used_at TIMESTAMP,
    
    CONSTRAINT uk_tb_pre_autorizacao_buyer_produto UNIQUE (buyer_id, produto_id)
);

-- Índices para tb_pre_autorizacao
CREATE INDEX idx_tb_pre_autorizacao_buyer_id ON tb_pre_autorizacao(buyer_id);
CREATE INDEX idx_tb_pre_autorizacao_produto_id ON tb_pre_autorizacao(produto_id);
CREATE INDEX idx_tb_pre_autorizacao_status ON tb_pre_autorizacao(status);
CREATE INDEX idx_tb_pre_autorizacao_expires_at ON tb_pre_autorizacao(expires_at);

-- =====================================================
-- TABELA: TB_HISTORICO_PRODUTO
-- =====================================================
CREATE TABLE tb_historico_produto (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    produto_id UUID NOT NULL REFERENCES tb_produto(id) ON DELETE CASCADE,
    action VARCHAR(50) NOT NULL, -- 'PRICE_UPDATE', 'STATUS_CHANGE', 'DESCRIPTION_UPDATE', etc.
    performed_by UUID NOT NULL REFERENCES tb_usuario(id),
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    old_values TEXT, -- JSON como TEXT
    new_values TEXT, -- JSON como TEXT
    metadata TEXT -- JSON como TEXT
);

-- Índices para tb_historico_produto
CREATE INDEX idx_tb_historico_produto_produto_id ON tb_historico_produto(produto_id);
CREATE INDEX idx_tb_historico_produto_performed_by ON tb_historico_produto(performed_by);
CREATE INDEX idx_tb_historico_produto_timestamp ON tb_historico_produto(timestamp);

-- =====================================================
-- COMENTÁRIOS NAS TABELAS
-- =====================================================

COMMENT ON TABLE tb_usuario IS 'Tabela principal de usuários do sistema';
COMMENT ON TABLE tb_usuario_role IS 'Roles associadas aos usuários (relacionamento many-to-many)';
COMMENT ON TABLE tb_vendedor IS 'Perfil de vendedor associado a um usuário';
COMMENT ON TABLE tb_comprador IS 'Perfil de comprador associado a um usuário';
COMMENT ON TABLE tb_contrato IS 'Contratos de comissão com vendedores';
COMMENT ON TABLE tb_lote IS 'Agrupamento de produtos para leilão';
COMMENT ON TABLE tb_produto IS 'Produtos disponíveis para leilão';
COMMENT ON TABLE tb_lance IS 'Lances dados em produtos';
COMMENT ON TABLE tb_arremate IS 'Produtos arrematados e informações de pagamento';
COMMENT ON TABLE tb_documento IS 'Documentos gerados pelo sistema (notas, contratos, etc.)';
COMMENT ON TABLE tb_audit_log IS 'Log de auditoria de todas as ações do sistema';
COMMENT ON TABLE tb_disputa IS 'Disputas relacionadas a arremates';
COMMENT ON TABLE tb_favorito IS 'Produtos favoritados pelos usuários';
COMMENT ON TABLE tb_notificacao IS 'Notificações enviadas aos usuários';
COMMENT ON TABLE tb_pre_autorizacao IS 'Pré-autorizações de pagamento para lances';
COMMENT ON TABLE tb_historico_produto IS 'Histórico de alterações em produtos';

-- =====================================================
-- DADOS INICIAIS (SEEDS)
-- =====================================================

-- Usuário administrador padrão
INSERT INTO tb_usuario (id, nome, email, senha_hash, status, email_verificado) 
VALUES (
    gen_random_uuid(),
    'Administrador do Sistema',
    'admin@leilao.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    TRUE
);

-- Inserir role de admin para o usuário administrador
INSERT INTO tb_usuario_role (usuario_id, role)
SELECT id, 'ADMIN' FROM tb_usuario WHERE email = 'admin@leilao.com';

-- =====================================================
-- FIM DO SCRIPT
-- =====================================================