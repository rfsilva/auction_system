export interface Produto {
  id: string;
  sellerId: string;
  loteId?: string;
  title: string;
  description: string;
  images: string[];
  weight?: number;
  dimensions?: string;
  initialPrice: number;
  currentPrice: number;
  reservePrice?: number;
  incrementMin: number;
  endDateTime: string;
  status: ProdutoStatus;
  moderated: boolean;
  moderatorId?: string;
  moderatedAt?: string;
  antiSnipeEnabled: boolean;
  antiSnipeExtension: number;
  categoria?: string;
  tags: string[];
  createdAt: string;
  updatedAt: string;
  
  // Campos calculados
  isActive: boolean;
  isExpired: boolean;
  canReceiveBids: boolean;
  timeRemaining: number; // em segundos
}

export enum ProdutoStatus {
  DRAFT = 'DRAFT',
  PENDING_APPROVAL = 'PENDING_APPROVAL',
  ACTIVE = 'ACTIVE',
  SOLD = 'SOLD',
  CANCELLED = 'CANCELLED',
  EXPIRED = 'EXPIRED'
}

export interface ProdutoCreateRequest {
  title: string;
  description: string;
  images?: string[];
  weight?: number;
  dimensions?: string;
  initialPrice: number;
  reservePrice?: number;
  incrementMin?: number;
  endDateTime: string;
  categoria?: string;
  tags?: string[];
  antiSnipeEnabled?: boolean;
  antiSnipeExtension?: number;
}

export interface ProdutoUpdateRequest {
  title?: string;
  description?: string;
  images?: string[];
  weight?: number;
  dimensions?: string;
  initialPrice?: number;
  reservePrice?: number;
  incrementMin?: number;
  endDateTime?: string;
  categoria?: string;
  tags?: string[];
  antiSnipeEnabled?: boolean;
  antiSnipeExtension?: number;
}

export interface CatalogoFiltro {
  categoria?: string;
  precoMin?: number;
  precoMax?: number;
  titulo?: string;
  ordenacao?: 'recentes' | 'preco_asc' | 'preco_desc' | 'terminando';
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