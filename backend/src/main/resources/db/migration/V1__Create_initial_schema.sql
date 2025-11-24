-- =====================================================
-- Sistema de Leilão Eletrônico - Schema Inicial
-- Versão: 1.0
-- Data: 2024-11-24
-- =====================================================

-- Extensões necessárias
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- =====================================================
-- ENUMS
-- =====================================================

-- Status do usuário
CREATE TYPE user_status AS ENUM ('ACTIVE', 'INACTIVE', 'SUSPENDED', 'PENDING_VERIFICATION');

-- Roles do usuário
CREATE TYPE user_role AS ENUM ('VISITOR', 'PARTICIPANT', 'BUYER', 'SELLER', 'ADMIN');

-- Status KYC do comprador
CREATE TYPE kyc_status AS ENUM ('PENDING', 'APPROVED', 'REJECTED', 'EXPIRED');

-- Status do produto
CREATE TYPE product_status AS ENUM ('DRAFT', 'PENDING_APPROVAL', 'ACTIVE', 'SOLD', 'CANCELLED', 'EXPIRED');

-- Status do lote
CREATE TYPE lot_status AS ENUM ('DRAFT', 'ACTIVE', 'CLOSED', 'CANCELLED');

-- Status do pagamento
CREATE TYPE payment_status AS ENUM ('PENDING', 'AUTHORIZED', 'CAPTURED', 'FAILED', 'REFUNDED', 'CANCELLED');

-- Status da disputa
CREATE TYPE dispute_status AS ENUM ('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED');

-- Status da notificação
CREATE TYPE notification_status AS ENUM ('PENDING', 'SENT', 'READ', 'FAILED');

-- Status da pré-autorização
CREATE TYPE pre_auth_status AS ENUM ('PENDING', 'AUTHORIZED', 'EXPIRED', 'CANCELLED', 'USED');

-- =====================================================
-- TABELA: USUARIOS
-- =====================================================
CREATE TABLE usuarios (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    telefone VARCHAR(20),
    status user_status NOT NULL DEFAULT 'PENDING_VERIFICATION',
    roles user_role[] NOT NULL DEFAULT ARRAY['VISITOR'::user_role],
    email_verificado BOOLEAN NOT NULL DEFAULT FALSE,
    telefone_verificado BOOLEAN NOT NULL DEFAULT FALSE,
    ultimo_login TIMESTAMP WITH TIME ZONE,
    tentativas_login INTEGER NOT NULL DEFAULT 0,
    bloqueado_ate TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para usuarios
CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_usuarios_status ON usuarios(status);
CREATE INDEX idx_usuarios_roles ON usuarios USING GIN(roles);
CREATE INDEX idx_usuarios_created_at ON usuarios(created_at);

-- =====================================================
-- TABELA: CONTRATOS
-- =====================================================
CREATE TABLE contratos (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    seller_id UUID NOT NULL,
    fee_rate DECIMAL(5,4) NOT NULL CHECK (fee_rate >= 0 AND fee_rate <= 1),
    terms TEXT NOT NULL,
    valid_from TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    valid_to TIMESTAMP WITH TIME ZONE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para contratos
CREATE INDEX idx_contratos_seller_id ON contratos(seller_id);
CREATE INDEX idx_contratos_active ON contratos(active);
CREATE INDEX idx_contratos_valid_period ON contratos(valid_from, valid_to);

-- =====================================================
-- TABELA: VENDEDORES
-- =====================================================
CREATE TABLE vendedores (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    usuario_id UUID NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    company_name VARCHAR(255),
    tax_id VARCHAR(50),
    contract_id UUID REFERENCES contratos(id),
    fee_rate DECIMAL(5,4) NOT NULL DEFAULT 0.05 CHECK (fee_rate >= 0 AND fee_rate <= 1),
    documents JSONB,
    verificado BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT uk_vendedores_usuario_id UNIQUE (usuario_id),
    CONSTRAINT uk_vendedores_tax_id UNIQUE (tax_id)
);

-- Índices para vendedores
CREATE INDEX idx_vendedores_usuario_id ON vendedores(usuario_id);
CREATE INDEX idx_vendedores_contract_id ON vendedores(contract_id);
CREATE INDEX idx_vendedores_verificado ON vendedores(verificado);

-- =====================================================
-- TABELA: COMPRADORES
-- =====================================================
CREATE TABLE compradores (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    usuario_id UUID NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    kyc_status kyc_status NOT NULL DEFAULT 'PENDING',
    preferences JSONB,
    limite_credito DECIMAL(15,2),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT uk_compradores_usuario_id UNIQUE (usuario_id)
);

-- Índices para compradores
CREATE INDEX idx_compradores_usuario_id ON compradores(usuario_id);
CREATE INDEX idx_compradores_kyc_status ON compradores(kyc_status);

-- =====================================================
-- TABELA: LOTES
-- =====================================================
CREATE TABLE lotes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    seller_id UUID NOT NULL REFERENCES vendedores(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    lote_end_datetime TIMESTAMP WITH TIME ZONE NOT NULL,
    status lot_status NOT NULL DEFAULT 'DRAFT',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para lotes
CREATE INDEX idx_lotes_seller_id ON lotes(seller_id);
CREATE INDEX idx_lotes_status ON lotes(status);
CREATE INDEX idx_lotes_end_datetime ON lotes(lote_end_datetime);

-- =====================================================
-- TABELA: PRODUTOS
-- =====================================================
CREATE TABLE produtos (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    seller_id UUID NOT NULL REFERENCES vendedores(id) ON DELETE CASCADE,
    lote_id UUID REFERENCES lotes(id) ON DELETE SET NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    images JSONB, -- Array de URLs das imagens
    weight DECIMAL(10,3), -- Peso em kg
    dimensions JSONB, -- {length, width, height} em cm
    initial_price DECIMAL(15,2) NOT NULL CHECK (initial_price > 0),
    current_price DECIMAL(15,2) NOT NULL CHECK (current_price >= initial_price),
    reserve_price DECIMAL(15,2), -- Preço de reserva (opcional)
    increment_min DECIMAL(15,2) NOT NULL DEFAULT 1.00 CHECK (increment_min > 0),
    end_datetime TIMESTAMP WITH TIME ZONE NOT NULL,
    status product_status NOT NULL DEFAULT 'DRAFT',
    moderated BOOLEAN NOT NULL DEFAULT FALSE,
    moderator_id UUID REFERENCES usuarios(id),
    moderated_at TIMESTAMP WITH TIME ZONE,
    anti_snipe_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    anti_snipe_extension INTEGER NOT NULL DEFAULT 300, -- segundos
    categoria VARCHAR(100),
    tags TEXT[],
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_produtos_end_datetime CHECK (end_datetime > created_at)
);

-- Índices para produtos
CREATE INDEX idx_produtos_seller_id ON produtos(seller_id);
CREATE INDEX idx_produtos_lote_id ON produtos(lote_id);
CREATE INDEX idx_produtos_status ON produtos(status);
CREATE INDEX idx_produtos_end_datetime ON produtos(end_datetime);
CREATE INDEX idx_produtos_categoria ON produtos(categoria);
CREATE INDEX idx_produtos_current_price ON produtos(current_price);
CREATE INDEX idx_produtos_tags ON produtos USING GIN(tags);
CREATE INDEX idx_produtos_created_at ON produtos(created_at);

-- =====================================================
-- TABELA: LANCES
-- =====================================================
CREATE TABLE lances (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    produto_id UUID NOT NULL REFERENCES produtos(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    value DECIMAL(15,2) NOT NULL CHECK (value > 0),
    currency VARCHAR(3) NOT NULL DEFAULT 'BRL',
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address INET,
    user_agent TEXT,
    sequence_number BIGSERIAL,
    proxy_bid BOOLEAN NOT NULL DEFAULT FALSE,
    max_bid_value DECIMAL(15,2), -- Para lances automáticos
    is_winning BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para lances
CREATE INDEX idx_lances_produto_id ON lances(produto_id);
CREATE INDEX idx_lances_user_id ON lances(user_id);
CREATE INDEX idx_lances_timestamp ON lances(timestamp);
CREATE INDEX idx_lances_sequence_number ON lances(sequence_number);
CREATE INDEX idx_lances_is_winning ON lances(is_winning);
CREATE INDEX idx_lances_produto_timestamp ON lances(produto_id, timestamp DESC);

-- =====================================================
-- TABELA: ARREMATES
-- =====================================================
CREATE TABLE arremates (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    produto_id UUID NOT NULL REFERENCES produtos(id) ON DELETE CASCADE,
    winning_bid_id UUID NOT NULL REFERENCES lances(id),
    buyer_id UUID NOT NULL REFERENCES compradores(id),
    final_price DECIMAL(15,2) NOT NULL CHECK (final_price > 0),
    fee_amount DECIMAL(15,2) NOT NULL CHECK (fee_amount >= 0),
    freight_estimate DECIMAL(15,2),
    status_payment payment_status NOT NULL DEFAULT 'PENDING',
    payment_due_date TIMESTAMP WITH TIME ZONE,
    paid_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT uk_arremates_produto_id UNIQUE (produto_id),
    CONSTRAINT uk_arremates_winning_bid_id UNIQUE (winning_bid_id)
);

-- Índices para arremates
CREATE INDEX idx_arremates_produto_id ON arremates(produto_id);
CREATE INDEX idx_arremates_buyer_id ON arremates(buyer_id);
CREATE INDEX idx_arremates_status_payment ON arremates(status_payment);
CREATE INDEX idx_arremates_created_at ON arremates(created_at);

-- =====================================================
-- TABELA: DOCUMENTOS
-- =====================================================
CREATE TABLE documentos (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    entity_id UUID NOT NULL,
    entity_type VARCHAR(50) NOT NULL, -- 'PRODUTO', 'ARREMATE', 'USUARIO', etc.
    type VARCHAR(50) NOT NULL, -- 'INVOICE', 'RECEIPT', 'CONTRACT', etc.
    filename VARCHAR(255) NOT NULL,
    storage_url TEXT NOT NULL,
    file_hash VARCHAR(64) NOT NULL, -- SHA-256
    file_size BIGINT NOT NULL,
    mime_type VARCHAR(100),
    generated_by UUID REFERENCES usuarios(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP WITH TIME ZONE
);

-- Índices para documentos
CREATE INDEX idx_documentos_entity ON documentos(entity_type, entity_id);
CREATE INDEX idx_documentos_type ON documentos(type);
CREATE INDEX idx_documentos_created_at ON documentos(created_at);
CREATE INDEX idx_documentos_expires_at ON documentos(expires_at);

-- =====================================================
-- TABELA: AUDIT_LOG
-- =====================================================
CREATE TABLE audit_log (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    entity_type VARCHAR(50) NOT NULL,
    entity_id UUID NOT NULL,
    action VARCHAR(50) NOT NULL, -- 'CREATE', 'UPDATE', 'DELETE', 'LOGIN', etc.
    performed_by UUID REFERENCES usuarios(id),
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address INET,
    user_agent TEXT,
    old_values JSONB,
    new_values JSONB,
    metadata JSONB
);

-- Índices para audit_log
CREATE INDEX idx_audit_log_entity ON audit_log(entity_type, entity_id);
CREATE INDEX idx_audit_log_performed_by ON audit_log(performed_by);
CREATE INDEX idx_audit_log_timestamp ON audit_log(timestamp);
CREATE INDEX idx_audit_log_action ON audit_log(action);

-- =====================================================
-- TABELA: DISPUTAS
-- =====================================================
CREATE TABLE disputas (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    arremate_id UUID NOT NULL REFERENCES arremates(id) ON DELETE CASCADE,
    buyer_id UUID NOT NULL REFERENCES compradores(id),
    seller_id UUID NOT NULL REFERENCES vendedores(id),
    reason TEXT NOT NULL,
    status dispute_status NOT NULL DEFAULT 'OPEN',
    evidence JSONB, -- Array de evidências (URLs, textos, etc.)
    resolution TEXT,
    resolved_by UUID REFERENCES usuarios(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP WITH TIME ZONE,
    
    CONSTRAINT uk_disputas_arremate_id UNIQUE (arremate_id)
);

-- Índices para disputas
CREATE INDEX idx_disputas_arremate_id ON disputas(arremate_id);
CREATE INDEX idx_disputas_buyer_id ON disputas(buyer_id);
CREATE INDEX idx_disputas_seller_id ON disputas(seller_id);
CREATE INDEX idx_disputas_status ON disputas(status);

-- =====================================================
-- TABELA: FAVORITOS
-- =====================================================
CREATE TABLE favoritos (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    produto_id UUID NOT NULL REFERENCES produtos(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT uk_favoritos_user_produto UNIQUE (user_id, produto_id)
);

-- Índices para favoritos
CREATE INDEX idx_favoritos_user_id ON favoritos(user_id);
CREATE INDEX idx_favoritos_produto_id ON favoritos(produto_id);

-- =====================================================
-- TABELA: NOTIFICACOES
-- =====================================================
CREATE TABLE notificacoes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    type VARCHAR(50) NOT NULL, -- 'BID_OUTBID', 'AUCTION_ENDING', 'PAYMENT_DUE', etc.
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    status notification_status NOT NULL DEFAULT 'PENDING',
    data JSONB, -- Dados adicionais específicos do tipo de notificação
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sent_at TIMESTAMP WITH TIME ZONE,
    read_at TIMESTAMP WITH TIME ZONE
);

-- Índices para notificacoes
CREATE INDEX idx_notificacoes_user_id ON notificacoes(user_id);
CREATE INDEX idx_notificacoes_status ON notificacoes(status);
CREATE INDEX idx_notificacoes_type ON notificacoes(type);
CREATE INDEX idx_notificacoes_created_at ON notificacoes(created_at);

-- =====================================================
-- TABELA: PRE_AUTORIZACOES
-- =====================================================
CREATE TABLE pre_autorizacoes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    buyer_id UUID NOT NULL REFERENCES compradores(id) ON DELETE CASCADE,
    produto_id UUID NOT NULL REFERENCES produtos(id) ON DELETE CASCADE,
    amount DECIMAL(15,2) NOT NULL CHECK (amount > 0),
    status pre_auth_status NOT NULL DEFAULT 'PENDING',
    payment_method_id VARCHAR(255), -- ID do método de pagamento no gateway
    authorization_code VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    used_at TIMESTAMP WITH TIME ZONE,
    
    CONSTRAINT uk_pre_auth_buyer_produto UNIQUE (buyer_id, produto_id)
);

-- Índices para pre_autorizacoes
CREATE INDEX idx_pre_auth_buyer_id ON pre_autorizacoes(buyer_id);
CREATE INDEX idx_pre_auth_produto_id ON pre_autorizacoes(produto_id);
CREATE INDEX idx_pre_auth_status ON pre_autorizacoes(status);
CREATE INDEX idx_pre_auth_expires_at ON pre_autorizacoes(expires_at);

-- =====================================================
-- TABELA: HISTORICO_PRODUTOS
-- =====================================================
CREATE TABLE historico_produtos (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    produto_id UUID NOT NULL REFERENCES produtos(id) ON DELETE CASCADE,
    action VARCHAR(50) NOT NULL, -- 'PRICE_UPDATE', 'STATUS_CHANGE', 'DESCRIPTION_UPDATE', etc.
    performed_by UUID NOT NULL REFERENCES usuarios(id),
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    old_values JSONB,
    new_values JSONB,
    metadata JSONB
);

-- Índices para historico_produtos
CREATE INDEX idx_historico_produtos_produto_id ON historico_produtos(produto_id);
CREATE INDEX idx_historico_produtos_performed_by ON historico_produtos(performed_by);
CREATE INDEX idx_historico_produtos_timestamp ON historico_produtos(timestamp);

-- =====================================================
-- TRIGGERS PARA UPDATED_AT
-- =====================================================

-- Função para atualizar updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Triggers para updated_at
CREATE TRIGGER update_usuarios_updated_at BEFORE UPDATE ON usuarios FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_contratos_updated_at BEFORE UPDATE ON contratos FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_vendedores_updated_at BEFORE UPDATE ON vendedores FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_compradores_updated_at BEFORE UPDATE ON compradores FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_lotes_updated_at BEFORE UPDATE ON lotes FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_produtos_updated_at BEFORE UPDATE ON produtos FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_arremates_updated_at BEFORE UPDATE ON arremates FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- =====================================================
-- COMENTÁRIOS NAS TABELAS
-- =====================================================

COMMENT ON TABLE usuarios IS 'Tabela principal de usuários do sistema';
COMMENT ON TABLE vendedores IS 'Perfil de vendedor associado a um usuário';
COMMENT ON TABLE compradores IS 'Perfil de comprador associado a um usuário';
COMMENT ON TABLE contratos IS 'Contratos de comissão com vendedores';
COMMENT ON TABLE lotes IS 'Agrupamento de produtos para leilão';
COMMENT ON TABLE produtos IS 'Produtos disponíveis para leilão';
COMMENT ON TABLE lances IS 'Lances dados em produtos';
COMMENT ON TABLE arremates IS 'Produtos arrematados e informações de pagamento';
COMMENT ON TABLE documentos IS 'Documentos gerados pelo sistema (notas, contratos, etc.)';
COMMENT ON TABLE audit_log IS 'Log de auditoria de todas as ações do sistema';
COMMENT ON TABLE disputas IS 'Disputas relacionadas a arremates';
COMMENT ON TABLE favoritos IS 'Produtos favoritados pelos usuários';
COMMENT ON TABLE notificacoes IS 'Notificações enviadas aos usuários';
COMMENT ON TABLE pre_autorizacoes IS 'Pré-autorizações de pagamento para lances';
COMMENT ON TABLE historico_produtos IS 'Histórico de alterações em produtos';

-- =====================================================
-- DADOS INICIAIS (SEEDS)
-- =====================================================

-- Usuário administrador padrão
INSERT INTO usuarios (id, nome, email, senha_hash, status, roles, email_verificado) 
VALUES (
    uuid_generate_v4(),
    'Administrador do Sistema',
    'admin@leilao.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password
    'ACTIVE',
    ARRAY['ADMIN'::user_role],
    TRUE
);

-- Inserir alguns tipos de notificação padrão
COMMENT ON COLUMN notificacoes.type IS 'Tipos: BID_PLACED, BID_OUTBID, AUCTION_ENDING, AUCTION_WON, PAYMENT_DUE, PAYMENT_CONFIRMED, DISPUTE_OPENED, etc.';

-- =====================================================
-- FIM DO SCRIPT
-- =====================================================