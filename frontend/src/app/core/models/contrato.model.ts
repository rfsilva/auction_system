/**
 * Modelos para o sistema de contratos
 */

export interface Contrato {
  id: string;
  sellerId: string;
  sellerName?: string;
  sellerCompanyName?: string;
  feeRate: number;
  terms: string;
  validFrom: string;
  validTo?: string;
  active: boolean;
  categoria?: string;
  status: ContractStatus;
  createdBy?: string;
  createdByName?: string;
  approvedBy?: string;
  approvedByName?: string;
  approvedAt?: string;
  createdAt: string;
  updatedAt: string;
  
  // Campos calculados
  isActive: boolean;
  isExpired: boolean;
  canBeActivated: boolean;
  canBeCancelled: boolean;
  canBeEdited: boolean;
  daysUntilExpiration?: number;
  statusDisplayName: string;
}

export interface ContratoCreateRequest {
  sellerId: string;
  feeRate: number;
  terms: string;
  validFrom?: string;
  validTo?: string;
  categoria?: string;
}

export interface ContratoUpdateRequest {
  feeRate?: number;
  terms?: string;
  validFrom?: string;
  validTo?: string;
  categoria?: string;
}

export interface ContratoFiltro {
  sellerId?: string;
  sellerName?: string;
  status?: ContractStatus;
  categoria?: string;
  active?: boolean;
  feeRateMin?: number;
  feeRateMax?: number;
  termo?: string;
  page?: number;
  size?: number;
  sort?: string;
  direction?: string;
}

export enum ContractStatus {
  DRAFT = 'DRAFT',
  ACTIVE = 'ACTIVE',
  EXPIRED = 'EXPIRED',
  CANCELLED = 'CANCELLED',
  SUSPENDED = 'SUSPENDED'
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

export interface ApiResponse<T> {
  success: boolean;
  data: T;
  message?: string;
  timestamp: string;
}