/**
 * Modelos para o sistema de contratos
 * Consolidado com interfaces de ativação de vendedores
 */

// Importa interfaces comuns do arquivo centralizado
import type { 
  ApiResponse, 
  PaginatedResponse, 
  ContratoFiltro as BaseContratoFiltro
} from '../../shared/interfaces/api.interfaces';

// ✅ CORRIGIDO: ContractStatus importado como valor (enum) não como tipo
import { ContractStatus } from '../../shared/interfaces/api.interfaces';

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

/**
 * Interface consolidada para ativação de vendedores
 * Movida de ativar-vendedor.model.ts para consolidação
 */
export interface AtivarVendedorRequest {
  usuarioId: string;
  feeRate: number;
  terms: string;
  validFrom?: string;
  validTo?: string;
  categoria?: string;
  ativarImediatamente?: boolean;
}

// ✅ CORRIGIDO: Re-exporta enum como valor e tipos como type
export { ContractStatus };
export type { ApiResponse, PaginatedResponse };

// Alias para manter compatibilidade
export type ContratoFiltro = BaseContratoFiltro;