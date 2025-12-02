/**
 * Enums e interfaces para Lote
 * Atualizado para usar contractId ao invés de sellerId
 */

export enum LoteStatus {
  DRAFT = 'DRAFT',
  ACTIVE = 'ACTIVE', 
  CLOSED = 'CLOSED',
  CANCELLED = 'CANCELLED'
}

export interface Lote {
  id: string;
  contractId: string;
  title: string;
  description: string;
  loteEndDateTime: string;
  status: LoteStatus;
  createdAt: string;
  updatedAt: string;
  
  // Campos calculados
  isActive: boolean;
  isExpired: boolean;
  canBeEdited: boolean;
  canBeActivated: boolean;
  canBeCancelled: boolean;
  timeRemaining: number; // em segundos
  
  // Lista de produtos associados
  produtoIds: string[];
  totalProdutos: number;
  
  // Informações do contrato e vendedor (para exibição)
  sellerId?: string; // Obtido através do contrato
  sellerName?: string; // Nome do vendedor
  sellerCompanyName?: string; // Nome da empresa do vendedor
  contractStatus?: string; // Status do contrato
  categoria?: string; // Categoria do contrato
}

export interface LoteCreateRequest {
  title: string;
  description: string;
  loteEndDateTime: string;
  produtoIds?: string[];
  categoria?: string; // Para buscar o contrato correto
}

export interface LoteUpdateRequest {
  title?: string;
  description?: string;
  loteEndDateTime?: string;
  produtoIds?: string[];
}

export interface LoteFiltro {
  termo?: string;
  categoria?: string;
  contractId?: string;
  sellerId?: string; // Para compatibilidade
  page?: number;
  size?: number;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}