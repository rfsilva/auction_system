/**
 * Modelos específicos para o catálogo público de lotes
 * História 02: Transformação do Catálogo em Catálogo de Lotes
 * 
 * CORRIGIDO: Mapeamento correto dos campos do backend
 */

import { LoteStatus } from './lote.model';

export interface LoteCatalogo {
  id: string;
  title: string;
  description: string;
  loteEndDateTime: string;
  status: LoteStatus;
  createdAt: string;
  
  // CORRIGIDO: Campos calculados com nomes corretos do backend
  active: boolean;        // Backend envia 'active', não 'isActive'
  expired: boolean;       // Backend envia 'expired', não 'isExpired'
  timeRemaining: number;  // em segundos
  
  // Aliases para compatibilidade (getters calculados)
  isActive?: boolean;     // Será calculado a partir de 'active'
  isExpired?: boolean;    // Será calculado a partir de 'expired'
  
  // Informações dos produtos válidos
  totalProdutos: number;
  quantidadeProdutosValidos?: number; // Alias para compatibilidade
  imagemDestaque?: string; // Primeira imagem do primeiro produto válido
  
  // Informações do vendedor (para exibição)
  sellerName?: string;
  sellerCompanyName?: string;
  categoria?: string;
}

export interface LoteCatalogoFiltro {
  termo?: string; // Busca por título ou descrição
  categoria?: string; // Filtro por categoria
  ordenacao?: 'proximidade_encerramento' | 'recentes' | 'alfabetica';
  
  // Paginação
  page?: number;
  size?: number; // 10, 20, 50
}

export interface PaginacaoConfig {
  opcoesPorPagina: number[];
  padraoLotes: number;
  padraoProdutos: number;
}

// Configuração padrão da paginação
export const PAGINACAO_CONFIG: PaginacaoConfig = {
  opcoesPorPagina: [10, 20, 50],
  padraoLotes: 10,
  padraoProdutos: 20
};

export interface LoteDestaque {
  id: string;
  title: string;
  description: string;
  loteEndDateTime: string;
  timeRemaining: number;
  
  // CORRIGIDO: Usar campos corretos do backend
  active: boolean;
  totalProdutos: number;
  quantidadeProdutosValidos?: number; // Alias para compatibilidade
  
  imagemDestaque?: string;
  sellerName?: string;
  categoria?: string;
}