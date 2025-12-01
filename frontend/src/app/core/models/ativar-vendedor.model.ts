/**
 * Modelos para ativação de vendedores
 * História 2: Processo de Contratação de Vendedores
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