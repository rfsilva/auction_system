-- =====================================================
-- Sistema de Leilão Eletrônico - Schema Inicial MySQL
-- Versão: 1.4 - UUID como STRING
-- Data: 2024-11-26
-- =====================================================

-- =====================================================
-- TABELA: TB_USUARIO
-- =====================================================
CREATE TABLE tb_usuario (
    id VARCHAR(36) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    telefone VARCHAR(20),
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED', 'PENDING_VERIFICATION') NOT NULL DEFAULT 'PENDING_VERIFICATION',
    email_verificado BOOLEAN NOT NULL DEFAULT FALSE,
    telefone_verificado BOOLEAN NOT NULL DEFAULT FALSE,
    ultimo_login TIMESTAMP NULL,
    tentativas_login INTEGER NOT NULL DEFAULT 0,
    bloqueado_ate TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Índices para tb_usuario
CREATE INDEX idx_tb_usuario_email ON tb_usuario(email);
CREATE INDEX idx_tb_usuario_status ON tb_usuario(status);
CREATE INDEX idx_tb_usuario_created_at ON tb_usuario(created_at);

-- =====================================================
-- TABELA: TB_USUARIO_ROLE (para JPA @ElementCollection)
-- =====================================================
CREATE TABLE tb_usuario_role (
    usuario_id VARCHAR(36) NOT NULL,
    role ENUM('VISITOR', 'PARTICIPANT', 'BUYER', 'SELLER', 'ADMIN') NOT NULL,
    PRIMARY KEY (usuario_id, role),
    FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id) ON DELETE CASCADE
);

-- Índices para tb_usuario_role
CREATE INDEX idx_tb_usuario_role_usuario_id ON tb_usuario_role(usuario_id);
CREATE INDEX idx_tb_usuario_role_role ON tb_usuario_role(role);

-- =====================================================
-- TABELA: TB_CONTRATO
-- =====================================================
CREATE TABLE tb_contrato (
    id VARCHAR(36) PRIMARY KEY,
    seller_id VARCHAR(36) NOT NULL,
    fee_rate DECIMAL(5,4) NOT NULL,
    terms TEXT NOT NULL,
    valid_from TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    valid_to TIMESTAMP NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Índices para tb_contrato
CREATE INDEX idx_tb_contrato_seller_id ON tb_contrato(seller_id);
CREATE INDEX idx_tb_contrato_active ON tb_contrato(active);
CREATE INDEX idx_tb_contrato_valid_period ON tb_contrato(valid_from, valid_to);

-- =====================================================
-- TABELA: TB_VENDEDOR
-- =====================================================
CREATE TABLE tb_vendedor (
    id VARCHAR(36) PRIMARY KEY,
    usuario_id VARCHAR(36) NOT NULL UNIQUE,
    company_name VARCHAR(255),
    tax_id VARCHAR(50) UNIQUE,
    contract_id VARCHAR(36),
    fee_rate DECIMAL(5,4) NOT NULL DEFAULT 0.05,
    documents TEXT, -- JSON como TEXT para compatibilidade
    verificado BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (contract_id) REFERENCES tb_contrato(id)
);

-- Índices para tb_vendedor
CREATE INDEX idx_tb_vendedor_usuario_id ON tb_vendedor(usuario_id);
CREATE INDEX idx_tb_vendedor_contract_id ON tb_vendedor(contract_id);
CREATE INDEX idx_tb_vendedor_verificado ON tb_vendedor(verificado);

-- =====================================================
-- TABELA: TB_COMPRADOR
-- =====================================================
CREATE TABLE tb_comprador (
    id VARCHAR(36) PRIMARY KEY,
    usuario_id VARCHAR(36) NOT NULL UNIQUE,
    kyc_status ENUM('PENDING', 'APPROVED', 'REJECTED', 'EXPIRED') NOT NULL DEFAULT 'PENDING',
    preferences TEXT, -- JSON como TEXT para compatibilidade
    limite_credito DECIMAL(15,2),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id) ON DELETE CASCADE
);

-- Índices para tb_comprador
CREATE INDEX idx_tb_comprador_usuario_id ON tb_comprador(usuario_id);
CREATE INDEX idx_tb_comprador_kyc_status ON tb_comprador(kyc_status);

-- =====================================================
-- TABELA: TB_LOTE
-- =====================================================
CREATE TABLE tb_lote (
    id VARCHAR(36) PRIMARY KEY,
    seller_id VARCHAR(36) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    lote_end_datetime TIMESTAMP NOT NULL,
    status ENUM('DRAFT', 'ACTIVE', 'CLOSED', 'CANCELLED') NOT NULL DEFAULT 'DRAFT',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (seller_id) REFERENCES tb_vendedor(id) ON DELETE CASCADE
);

-- Índices para tb_lote
CREATE INDEX idx_tb_lote_seller_id ON tb_lote(seller_id);
CREATE INDEX idx_tb_lote_status ON tb_lote(status);
CREATE INDEX idx_tb_lote_end_datetime ON tb_lote(lote_end_datetime);

-- =====================================================
-- TABELA: TB_PRODUTO
-- =====================================================
CREATE TABLE tb_produto (
    id VARCHAR(36) PRIMARY KEY,
    seller_id VARCHAR(36) NOT NULL,
    lote_id VARCHAR(36),
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    images TEXT, -- JSON como TEXT para compatibilidade
    weight DECIMAL(10,3), -- Peso em kg
    dimensions TEXT, -- JSON como TEXT: {length, width, height} em cm
    initial_price DECIMAL(15,2) NOT NULL,
    current_price DECIMAL(15,2) NOT NULL,
    reserve_price DECIMAL(15,2), -- Preço de reserva (opcional)
    increment_min DECIMAL(15,2) NOT NULL DEFAULT 1.00,
    end_datetime TIMESTAMP NOT NULL,
    status ENUM('DRAFT', 'PENDING_APPROVAL', 'ACTIVE', 'SOLD', 'CANCELLED', 'EXPIRED') NOT NULL DEFAULT 'DRAFT',
    moderated BOOLEAN NOT NULL DEFAULT FALSE,
    moderator_id VARCHAR(36),
    moderated_at TIMESTAMP NULL,
    anti_snipe_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    anti_snipe_extension INTEGER NOT NULL DEFAULT 300, -- segundos
    categoria VARCHAR(100),
    tags TEXT, -- Array como TEXT separado por vírgulas
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (seller_id) REFERENCES tb_vendedor(id) ON DELETE CASCADE,
    FOREIGN KEY (lote_id) REFERENCES tb_lote(id) ON DELETE SET NULL,
    FOREIGN KEY (moderator_id) REFERENCES tb_usuario(id)
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
    id VARCHAR(36) PRIMARY KEY,
    produto_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    value DECIMAL(15,2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'BRL',
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45), -- IPv6 compatible
    user_agent TEXT,
    sequence_number BIGINT NOT NULL AUTO_INCREMENT UNIQUE,
    proxy_bid BOOLEAN NOT NULL DEFAULT FALSE,
    max_bid_value DECIMAL(15,2), -- Para lances automáticos
    is_winning BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (produto_id) REFERENCES tb_produto(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES tb_usuario(id) ON DELETE CASCADE
);

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
    id VARCHAR(36) PRIMARY KEY,
    produto_id VARCHAR(36) NOT NULL UNIQUE,
    winning_bid_id VARCHAR(36) NOT NULL UNIQUE,
    buyer_id VARCHAR(36) NOT NULL,
    final_price DECIMAL(15,2) NOT NULL,
    fee_amount DECIMAL(15,2) NOT NULL,
    freight_estimate DECIMAL(15,2),
    status_payment ENUM('PENDING', 'AUTHORIZED', 'CAPTURED', 'FAILED', 'REFUNDED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    payment_due_date TIMESTAMP NULL,
    paid_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (produto_id) REFERENCES tb_produto(id) ON DELETE CASCADE,
    FOREIGN KEY (winning_bid_id) REFERENCES tb_lance(id),
    FOREIGN KEY (buyer_id) REFERENCES tb_comprador(id)
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
    id VARCHAR(36) PRIMARY KEY,
    entity_id VARCHAR(36) NOT NULL,
    entity_type VARCHAR(50) NOT NULL, -- 'PRODUTO', 'ARREMATE', 'USUARIO', etc.
    type VARCHAR(50) NOT NULL, -- 'INVOICE', 'RECEIPT', 'CONTRACT', etc.
    filename VARCHAR(255) NOT NULL,
    storage_url TEXT NOT NULL,
    file_hash VARCHAR(64) NOT NULL, -- SHA-256
    file_size BIGINT NOT NULL,
    mime_type VARCHAR(100),
    generated_by VARCHAR(36),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NULL,
    
    FOREIGN KEY (generated_by) REFERENCES tb_usuario(id)
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
    id VARCHAR(36) PRIMARY KEY,
    entity_type VARCHAR(50) NOT NULL,
    entity_id VARCHAR(36) NOT NULL,
    action VARCHAR(50) NOT NULL, -- 'CREATE', 'UPDATE', 'DELETE', 'LOGIN', etc.
    performed_by VARCHAR(36),
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45), -- IPv6 compatible
    user_agent TEXT,
    old_values TEXT, -- JSON como TEXT
    new_values TEXT, -- JSON como TEXT
    metadata TEXT, -- JSON como TEXT
    
    FOREIGN KEY (performed_by) REFERENCES tb_usuario(id)
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
    id VARCHAR(36) PRIMARY KEY,
    arremate_id VARCHAR(36) NOT NULL UNIQUE,
    buyer_id VARCHAR(36) NOT NULL,
    seller_id VARCHAR(36) NOT NULL,
    reason TEXT NOT NULL,
    status ENUM('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED') NOT NULL DEFAULT 'OPEN',
    evidence TEXT, -- JSON como TEXT: Array de evidências
    resolution TEXT,
    resolved_by VARCHAR(36),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP NULL,
    
    FOREIGN KEY (arremate_id) REFERENCES tb_arremate(id) ON DELETE CASCADE,
    FOREIGN KEY (buyer_id) REFERENCES tb_comprador(id),
    FOREIGN KEY (seller_id) REFERENCES tb_vendedor(id),
    FOREIGN KEY (resolved_by) REFERENCES tb_usuario(id)
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
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    produto_id VARCHAR(36) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE KEY uk_tb_favorito_user_produto (user_id, produto_id),
    FOREIGN KEY (user_id) REFERENCES tb_usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (produto_id) REFERENCES tb_produto(id) ON DELETE CASCADE
);

-- Índices para tb_favorito
CREATE INDEX idx_tb_favorito_user_id ON tb_favorito(user_id);
CREATE INDEX idx_tb_favorito_produto_id ON tb_favorito(produto_id);

-- =====================================================
-- TABELA: TB_NOTIFICACAO
-- =====================================================
CREATE TABLE tb_notificacao (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    type VARCHAR(50) NOT NULL, -- 'BID_OUTBID', 'AUCTION_ENDING', 'PAYMENT_DUE', etc.
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    status ENUM('PENDING', 'SENT', 'READ', 'FAILED') NOT NULL DEFAULT 'PENDING',
    data TEXT, -- JSON como TEXT: Dados adicionais específicos do tipo
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sent_at TIMESTAMP NULL,
    read_at TIMESTAMP NULL,
    
    FOREIGN KEY (user_id) REFERENCES tb_usuario(id) ON DELETE CASCADE
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
    id VARCHAR(36) PRIMARY KEY,
    buyer_id VARCHAR(36) NOT NULL,
    produto_id VARCHAR(36) NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    status ENUM('PENDING', 'AUTHORIZED', 'EXPIRED', 'CANCELLED', 'USED') NOT NULL DEFAULT 'PENDING',
    payment_method_id VARCHAR(255), -- ID do método de pagamento no gateway
    authorization_code VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    used_at TIMESTAMP NULL,
    
    UNIQUE KEY uk_tb_pre_autorizacao_buyer_produto (buyer_id, produto_id),
    FOREIGN KEY (buyer_id) REFERENCES tb_comprador(id) ON DELETE CASCADE,
    FOREIGN KEY (produto_id) REFERENCES tb_produto(id) ON DELETE CASCADE
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
    id VARCHAR(36) PRIMARY KEY,
    produto_id VARCHAR(36) NOT NULL,
    action VARCHAR(50) NOT NULL, -- 'PRICE_UPDATE', 'STATUS_CHANGE', 'DESCRIPTION_UPDATE', etc.
    performed_by VARCHAR(36) NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    old_values TEXT, -- JSON como TEXT
    new_values TEXT, -- JSON como TEXT
    metadata TEXT, -- JSON como TEXT
    
    FOREIGN KEY (produto_id) REFERENCES tb_produto(id) ON DELETE CASCADE,
    FOREIGN KEY (performed_by) REFERENCES tb_usuario(id)
);

-- Índices para tb_historico_produto
CREATE INDEX idx_tb_historico_produto_produto_id ON tb_historico_produto(produto_id);
CREATE INDEX idx_tb_historico_produto_performed_by ON tb_historico_produto(performed_by);
CREATE INDEX idx_tb_historico_produto_timestamp ON tb_historico_produto(timestamp);

-- =====================================================
-- FIM DO SCRIPT
-- =====================================================