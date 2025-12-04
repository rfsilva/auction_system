-- =====================================================
-- Otimização de Performance - Índices Específicos
-- História 5: Integração e Otimização - Sprint S2.2
-- Versão: V3
-- Data: 2024-12-19
-- =====================================================

-- =====================================================
-- ÍNDICES PARA ESTATÍSTICAS DE CONTRATOS
-- =====================================================

-- Índice composto para consultas de contratos ativos por vendedor e período
CREATE INDEX idx_tb_contrato_active_seller_period ON tb_contrato(active, seller_id, valid_from, valid_to)
WHERE active = TRUE AND status = 'ACTIVE';

-- Índice para consultas de contratos vencendo (otimiza queries de vencimento)
CREATE INDEX idx_tb_contrato_vencimento ON tb_contrato(status, valid_to, active)
WHERE status = 'ACTIVE' AND active = TRUE AND valid_to IS NOT NULL;

-- Índice para estatísticas por status e período de criação
CREATE INDEX idx_tb_contrato_stats_status_created ON tb_contrato(status, created_at, active);

-- Índice para cálculo de taxa média de comissão
CREATE INDEX idx_tb_contrato_fee_rate_active ON tb_contrato(fee_rate, active, status)
WHERE active = TRUE AND status = 'ACTIVE';

-- Índice para consultas por categoria e status
CREATE INDEX idx_tb_contrato_categoria_status ON tb_contrato(categoria, status, active)
WHERE categoria IS NOT NULL;

-- =====================================================
-- ÍNDICES PARA RELATÓRIOS DE COMISSÕES
-- =====================================================

-- Índice composto para relatórios de comissões por período e vendedor
CREATE INDEX idx_tb_contrato_comissoes_periodo ON tb_contrato(created_at, seller_id, categoria, status, fee_rate);

-- Índice para consultas de contratos por vendedor no período
CREATE INDEX idx_tb_contrato_seller_period ON tb_contrato(seller_id, created_at, status);

-- =====================================================
-- ÍNDICES PARA VENDEDORES
-- =====================================================

-- Índice para consultas de vendedores ativos
CREATE INDEX idx_tb_vendedor_active_verified ON tb_vendedor(active, verificado, created_at);

-- Índice para busca por empresa
CREATE INDEX idx_tb_vendedor_company_name ON tb_vendedor(company_name)
WHERE company_name IS NOT NULL;

-- =====================================================
-- ÍNDICES PARA PRODUTOS (OTIMIZAÇÃO DE CATÁLOGO)
-- =====================================================

-- Índice composto para catálogo público com filtros
CREATE INDEX idx_tb_produto_catalogo_publico ON tb_produto(status, end_datetime, categoria, current_price, created_at)
WHERE status IN ('ACTIVE', 'SOLD', 'EXPIRED');

-- Índice para produtos ativos ordenados por preço
CREATE INDEX idx_tb_produto_active_price ON tb_produto(status, current_price, end_datetime)
WHERE status = 'ACTIVE';

-- Índice para produtos por vendedor e status
CREATE INDEX idx_tb_produto_seller_status_created ON tb_produto(seller_id, status, created_at);

-- Índice para busca por título (otimiza LIKE queries)
CREATE INDEX idx_tb_produto_title_search ON tb_produto(title, status);

-- =====================================================
-- ÍNDICES PARA LANCES (OTIMIZAÇÃO DE LEILÕES)
-- =====================================================

-- Índice composto para lances por produto ordenados por timestamp
CREATE INDEX idx_tb_lance_produto_timestamp_value ON tb_lance(produto_id, timestamp DESC, value DESC);

-- Índice para lance vencedor por produto
CREATE INDEX idx_tb_lance_winning_produto ON tb_lance(produto_id, is_winning, timestamp DESC)
WHERE is_winning = TRUE;

-- Índice para lances por usuário
CREATE INDEX idx_tb_lance_user_timestamp ON tb_lance(user_id, timestamp DESC);

-- =====================================================
-- ÍNDICES PARA AUDITORIA E LOGS
-- =====================================================

-- Índice composto para logs de auditoria por entidade e período
CREATE INDEX idx_tb_audit_log_entity_timestamp ON tb_audit_log(entity_type, entity_id, timestamp DESC);

-- Índice para logs por usuário e período
CREATE INDEX idx_tb_audit_log_user_timestamp ON tb_audit_log(performed_by, timestamp DESC);

-- Índice para logs por ação
CREATE INDEX idx_tb_audit_log_action_timestamp ON tb_audit_log(action, timestamp DESC);

-- =====================================================
-- ÍNDICES PARA NOTIFICAÇÕES
-- =====================================================

-- Índice composto para notificações por usuário e status
CREATE INDEX idx_tb_notificacao_user_status_created ON tb_notificacao(user_id, status, created_at DESC);

-- Índice para notificações pendentes
CREATE INDEX idx_tb_notificacao_pending ON tb_notificacao(status, created_at)
WHERE status = 'PENDING';

-- =====================================================
-- ÍNDICES PARA DOCUMENTOS
-- =====================================================

-- Índice composto para documentos por entidade
CREATE INDEX idx_tb_documento_entity_type_created ON tb_documento(entity_type, entity_id, created_at DESC);

-- Índice para documentos expirados
CREATE INDEX idx_tb_documento_expires_at ON tb_documento(expires_at)
WHERE expires_at IS NOT NULL;

-- =====================================================
-- ÍNDICES PARA ARREMATES
-- =====================================================

-- Índice composto para arremates por comprador e status
CREATE INDEX idx_tb_arremate_buyer_status_created ON tb_arremate(buyer_id, status_payment, created_at DESC);

-- Índice para arremates por período
CREATE INDEX idx_tb_arremate_created_status ON tb_arremate(created_at, status_payment);

-- =====================================================
-- ÍNDICES PARA FAVORITOS
-- =====================================================

-- Índice para favoritos por usuário ordenados por data
CREATE INDEX idx_tb_favorito_user_created ON tb_favorito(user_id, created_at DESC);

-- =====================================================
-- ÍNDICES PARA PRÉ-AUTORIZAÇÕES
-- =====================================================

-- Índice composto para pré-autorizações por comprador e status
CREATE INDEX idx_tb_pre_autorizacao_buyer_status ON tb_pre_autorizacao(buyer_id, status, expires_at);

-- Índice para pré-autorizações expiradas
CREATE INDEX idx_tb_pre_autorizacao_expired ON tb_pre_autorizacao(expires_at, status)
WHERE status IN ('PENDING', 'AUTHORIZED');

-- =====================================================
-- ÍNDICES PARA DISPUTAS
-- =====================================================

-- Índice composto para disputas por status e data
CREATE INDEX idx_tb_disputa_status_created ON tb_disputa(status, created_at DESC);

-- Índice para disputas por comprador
CREATE INDEX idx_tb_disputa_buyer_status ON tb_disputa(buyer_id, status);

-- =====================================================
-- ÍNDICES PARA HISTÓRICO DE PRODUTOS
-- =====================================================

-- Índice composto para histórico por produto e data
CREATE INDEX idx_tb_historico_produto_produto_timestamp ON tb_historico_produto(produto_id, timestamp DESC);

-- Índice para histórico por usuário
CREATE INDEX idx_tb_historico_produto_user_timestamp ON tb_historico_produto(performed_by, timestamp DESC);

-- =====================================================
-- COMENTÁRIOS SOBRE OTIMIZAÇÃO
-- =====================================================

/*
ÍNDICES CRIADOS PARA OTIMIZAÇÃO:

1. CONTRATOS:
   - Consultas de contratos ativos por vendedor
   - Contratos vencendo nos próximos N dias
   - Estatísticas por status e período
   - Cálculo de taxa média de comissão

2. PRODUTOS:
   - Catálogo público com filtros múltiplos
   - Busca por título (LIKE otimizado)
   - Produtos por vendedor e status

3. LANCES:
   - Lances por produto ordenados por timestamp
   - Lance vencedor por produto
   - Histórico de lances por usuário

4. AUDITORIA:
   - Logs por entidade e período
   - Logs por usuário e ação

5. PERFORMANCE ESPERADA:
   - Consultas de estatísticas: < 200ms
   - Relatórios de comissões: < 500ms
   - Catálogo público: < 300ms
   - Busca de produtos: < 200ms

MONITORAMENTO:
- Use EXPLAIN ANALYZE para verificar uso dos índices
- Monitore slow query log
- Ajuste índices baseado em padrões de uso real
*/