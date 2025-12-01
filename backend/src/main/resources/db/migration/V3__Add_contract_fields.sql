-- =====================================================
-- Adicionar campos extras para contratos - Sprint 2.1
-- =====================================================

-- Adicionar campos de categoria e status para contratos
ALTER TABLE tb_contrato 
ADD COLUMN categoria VARCHAR(100) NULL COMMENT 'Categoria específica do contrato (opcional)',
ADD COLUMN status ENUM('DRAFT', 'ACTIVE', 'EXPIRED', 'CANCELLED', 'SUSPENDED') NOT NULL DEFAULT 'DRAFT' COMMENT 'Status do contrato',
ADD COLUMN created_by VARCHAR(36) NULL COMMENT 'Admin que criou o contrato',
ADD COLUMN approved_by VARCHAR(36) NULL COMMENT 'Admin que aprovou o contrato',
ADD COLUMN approved_at TIMESTAMP NULL COMMENT 'Data de aprovação do contrato';

-- Adicionar índices para os novos campos
CREATE INDEX idx_tb_contrato_categoria ON tb_contrato(categoria);
CREATE INDEX idx_tb_contrato_status ON tb_contrato(status);
CREATE INDEX idx_tb_contrato_created_by ON tb_contrato(created_by);
CREATE INDEX idx_tb_contrato_approved_by ON tb_contrato(approved_by);

-- Adicionar foreign keys para os campos de auditoria
ALTER TABLE tb_contrato 
ADD CONSTRAINT fk_tb_contrato_created_by FOREIGN KEY (created_by) REFERENCES tb_usuario(id),
ADD CONSTRAINT fk_tb_contrato_approved_by FOREIGN KEY (approved_by) REFERENCES tb_usuario(id);

-- Atualizar contratos existentes para status ACTIVE se estiverem ativos
UPDATE tb_contrato 
SET status = 'ACTIVE' 
WHERE active = TRUE;

-- Atualizar contratos expirados
UPDATE tb_contrato 
SET status = 'EXPIRED', active = FALSE 
WHERE valid_to IS NOT NULL AND valid_to < NOW();

-- Comentários nas colunas existentes para documentação
ALTER TABLE tb_contrato 
MODIFY COLUMN fee_rate DECIMAL(5,4) NOT NULL COMMENT 'Taxa de comissão (0.0001 = 0.01% até 0.5000 = 50%)',
MODIFY COLUMN terms TEXT NOT NULL COMMENT 'Termos e condições do contrato',
MODIFY COLUMN valid_from TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de início da vigência',
MODIFY COLUMN valid_to TIMESTAMP NULL COMMENT 'Data de fim da vigência (NULL = sem expiração)',
MODIFY COLUMN active BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Indica se o contrato está ativo (usado junto com status)';