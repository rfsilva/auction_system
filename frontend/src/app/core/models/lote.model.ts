/**
 * Enums e interfaces para Lote
 */

export enum LoteStatus {
  DRAFT = 'DRAFT',
  ACTIVE = 'ACTIVE', 
  CLOSED = 'CLOSED',
  CANCELLED = 'CANCELLED'
}

export interface Lote {
  id: string;
  sellerId: string;
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
}

export interface LoteCreateRequest {
  title: string;
  description: string;
  loteEndDateTime: string;
  produtoIds?: string[];
}

export interface LoteUpdateRequest {
  title?: string;
  description?: string;
  loteEndDateTime?: string;
  produtoIds?: string[];
}

export interface LoteFiltro {
  termo?: string;
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