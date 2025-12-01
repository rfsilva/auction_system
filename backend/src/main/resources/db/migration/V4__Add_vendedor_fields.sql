-- =====================================================
-- Adicionar campos extras para vendedores - Sprint 2.1
-- História 2: Processo de Contratação de Vendedores
-- =====================================================

-- Adicionar campos de contato e descrição para vendedores
ALTER TABLE tb_vendedor 
ADD COLUMN contact_email VARCHAR(255) NULL COMMENT 'Email de contato do vendedor',
ADD COLUMN contact_phone VARCHAR(20) NULL COMMENT 'Telefone de contato do vendedor',
ADD COLUMN description TEXT NULL COMMENT 'Descrição do vendedor/empresa',
ADD COLUMN active BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Indica se o vendedor está ativo';

-- Adicionar índices para os novos campos
CREATE INDEX idx_tb_vendedor_contact_email ON tb_vendedor(contact_email);
CREATE INDEX idx_tb_vendedor_active ON tb_vendedor(active);

-- Comentários nas colunas existentes para documentação
ALTER TABLE tb_vendedor 
MODIFY COLUMN company_name VARCHAR(255) NULL COMMENT 'Nome da empresa/razão social',
MODIFY COLUMN tax_id VARCHAR(50) NULL COMMENT 'CNPJ ou CPF do vendedor',
MODIFY COLUMN contract_id VARCHAR(36) NULL COMMENT 'ID do contrato ativo (pode ser NULL)',
MODIFY COLUMN fee_rate DECIMAL(5,4) NOT NULL DEFAULT 0.05 COMMENT 'Taxa de comissão padrão (deprecated - usar contrato)',
MODIFY COLUMN documents TEXT NULL COMMENT 'Documentos do vendedor em formato JSON',
MODIFY COLUMN verificado BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Indica se o vendedor foi verificado';

-- Atualizar vendedores existentes com dados padrão
UPDATE tb_vendedor v
INNER JOIN tb_usuario u ON v.usuario_id = u.id
SET 
    v.contact_email = u.email,
    v.contact_phone = u.telefone,
    v.description = CONCAT('Vendedor: ', u.nome),
    v.active = TRUE
WHERE v.contact_email IS NULL;