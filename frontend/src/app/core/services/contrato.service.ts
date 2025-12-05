import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

// ✅ CONSOLIDADO: Importa do arquivo centralizado
import { 
  Contrato, 
  ContratoCreateRequest, 
  ContratoUpdateRequest, 
  ContratoFiltro, 
  PaginatedResponse, 
  ApiResponse,
  ContractStatus,
  AtivarVendedorRequest
} from '../../shared/interfaces/api.interfaces';

export interface ContratoCreateFromUserRequest {
  usuarioId: string;
  feeRate: number;
  terms: string;
  validFrom?: string;
  validTo?: string;
  categoria?: string;
  ativarImediatamente?: boolean;
}

/**
 * Service para operações com contratos
 * FASE 2 - Reorganização de Services: Atualizado para nova estrutura de rotas
 * 
 * Conecta com os endpoints:
 * - /api/admin/contratos/** (operações administrativas)
 * - /api/vendedor/contratos/** (operações do vendedor)
 * 
 * Inclui funcionalidades da História 2: Processo de Contratação de Vendedores
 */
@Injectable({
  providedIn: 'root'
})
export class ContratoService {

  private readonly adminUrl = `${environment.apiUrl}/api/admin/contratos`;
  private readonly vendedorUrl = `${environment.apiUrl}/api/vendedor/contratos`;

  constructor(private http: HttpClient) {}

  // ========================================
  // OPERAÇÕES ADMINISTRATIVAS
  // ========================================

  /**
   * Cria um novo contrato a partir de usuário (novo fluxo)
   * Promove automaticamente o usuário a vendedor se necessário
   */
  criarContratoDeUsuario(contrato: ContratoCreateFromUserRequest): Observable<ApiResponse<Contrato>> {
    return this.http.post<ApiResponse<Contrato>>(`${this.adminUrl}/criar-de-usuario`, contrato);
  }

  /**
   * Cria um novo contrato (fluxo antigo - para vendedores existentes)
   */
  criarContrato(contrato: ContratoCreateRequest): Observable<ApiResponse<Contrato>> {
    return this.http.post<ApiResponse<Contrato>>(this.adminUrl, contrato);
  }

  /**
   * Ativa usuário como vendedor através de contrato
   * História 2: Processo de Contratação de Vendedores
   */
  ativarVendedor(request: AtivarVendedorRequest): Observable<ApiResponse<Contrato>> {
    return this.http.post<ApiResponse<Contrato>>(`${this.adminUrl}/ativar-vendedor`, request);
  }

  /**
   * Atualiza um contrato existente (admin)
   */
  atualizarContrato(contratoId: string, contrato: ContratoUpdateRequest): Observable<ApiResponse<Contrato>> {
    return this.http.put<ApiResponse<Contrato>>(`${this.adminUrl}/${contratoId}`, contrato);
  }

  /**
   * Busca contrato por ID (admin)
   */
  buscarContrato(contratoId: string): Observable<ApiResponse<Contrato>> {
    return this.http.get<ApiResponse<Contrato>>(`${this.adminUrl}/${contratoId}`);
  }

  /**
   * Lista contratos com filtros (admin)
   */
  listarContratos(filtros: ContratoFiltro = {}): Observable<ApiResponse<PaginatedResponse<Contrato>>> {
    let params = new HttpParams();

    if (filtros.sellerId) params = params.set('sellerId', filtros.sellerId);
    if (filtros.sellerName) params = params.set('sellerName', filtros.sellerName);
    if (filtros.status) params = params.set('status', filtros.status);
    if (filtros.categoria) params = params.set('categoria', filtros.categoria);
    if (filtros.active !== undefined) params = params.set('active', filtros.active.toString());
    if (filtros.feeRateMin !== undefined) params = params.set('feeRateMin', filtros.feeRateMin.toString());
    if (filtros.feeRateMax !== undefined) params = params.set('feeRateMax', filtros.feeRateMax.toString());
    if (filtros.termo) params = params.set('termo', filtros.termo);
    if (filtros.page !== undefined) params = params.set('page', filtros.page.toString());
    if (filtros.size !== undefined) params = params.set('size', filtros.size.toString());
    if (filtros.sort) params = params.set('sort', filtros.sort);
    if (filtros.direction) params = params.set('direction', filtros.direction);

    return this.http.get<ApiResponse<PaginatedResponse<Contrato>>>(this.adminUrl, { params });
  }

  /**
   * Lista contratos por vendedor (admin)
   */
  listarContratosPorVendedor(sellerId: string, page: number = 0, size: number = 20): Observable<ApiResponse<PaginatedResponse<Contrato>>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', 'createdAt')
      .set('direction', 'desc');

    return this.http.get<ApiResponse<PaginatedResponse<Contrato>>>(`${this.adminUrl}/vendedor/${sellerId}`, { params });
  }

  /**
   * Ativa um contrato (admin)
   */
  ativarContrato(contratoId: string): Observable<ApiResponse<Contrato>> {
    return this.http.post<ApiResponse<Contrato>>(`${this.adminUrl}/${contratoId}/ativar`, {});
  }

  /**
   * Cancela um contrato (admin)
   */
  cancelarContrato(contratoId: string): Observable<ApiResponse<Contrato>> {
    return this.http.post<ApiResponse<Contrato>>(`${this.adminUrl}/${contratoId}/cancelar`, {});
  }

  /**
   * Suspende um contrato (admin)
   */
  suspenderContrato(contratoId: string): Observable<ApiResponse<Contrato>> {
    return this.http.post<ApiResponse<Contrato>>(`${this.adminUrl}/${contratoId}/suspender`, {});
  }

  /**
   * Exclui um contrato (admin)
   */
  excluirContrato(contratoId: string): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.adminUrl}/${contratoId}`);
  }

  /**
   * Busca contrato ativo para vendedor e categoria (admin)
   */
  buscarContratoAtivo(sellerId: string, categoria?: string): Observable<ApiResponse<Contrato>> {
    let params = new HttpParams();
    if (categoria) params = params.set('categoria', categoria);

    return this.http.get<ApiResponse<Contrato>>(`${this.adminUrl}/vendedor/${sellerId}/ativo`, { params });
  }

  /**
   * Lista categorias disponíveis
   */
  listarCategorias(): Observable<ApiResponse<string[]>> {
    return this.http.get<ApiResponse<string[]>>(`${this.adminUrl}/categorias`);
  }

  /**
   * Verifica se vendedor tem contrato ativo (admin)
   */
  vendedorTemContratoAtivo(sellerId: string): Observable<ApiResponse<boolean>> {
    return this.http.get<ApiResponse<boolean>>(`${this.adminUrl}/vendedor/${sellerId}/tem-ativo`);
  }

  // ========================================
  // OPERAÇÕES DO VENDEDOR
  // ========================================

  /**
   * Lista contratos ativos do vendedor atual (para seleção em lotes)
   */
  listarMeusContratosAtivos(): Observable<ApiResponse<Contrato[]>> {
    return this.http.get<ApiResponse<Contrato[]>>(`${this.vendedorUrl}/meus-ativos`);
  }

  /**
   * Busca contrato ativo do vendedor para uma categoria específica
   */
  buscarMeuContratoAtivo(categoria?: string): Observable<ApiResponse<Contrato>> {
    let params = new HttpParams();
    if (categoria) params = params.set('categoria', categoria);

    return this.http.get<ApiResponse<Contrato>>(`${this.vendedorUrl}/meu-ativo`, { params });
  }

  /**
   * Verifica se o vendedor tem contrato ativo
   */
  tenhoContratoAtivo(): Observable<ApiResponse<boolean>> {
    return this.http.get<ApiResponse<boolean>>(`${this.vendedorUrl}/tenho-ativo`);
  }

  // ========================================
  // MÉTODOS UTILITÁRIOS
  // ========================================

  /**
   * Formata taxa de comissão para exibição
   */
  formatarTaxa(taxa: number): string {
    return `${(taxa * 100).toFixed(2)}%`;
  }

  /**
   * Formata data para exibição
   */
  formatarData(dataString: string): string {
    if (!dataString) return '';
    
    const data = new Date(dataString);
    return data.toLocaleDateString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  /**
   * Obtém classe CSS do status
   */
  getStatusClass(status: ContractStatus): string {
    const classes: Record<ContractStatus, string> = {
      [ContractStatus.DRAFT]: 'badge-secondary',
      [ContractStatus.ACTIVE]: 'badge-success',
      [ContractStatus.EXPIRED]: 'badge-warning',
      [ContractStatus.CANCELLED]: 'badge-danger',
      [ContractStatus.SUSPENDED]: 'badge-warning'
    };
    return classes[status] || 'badge-secondary';
  }

  /**
   * Obtém texto do status
   */
  getStatusText(status: ContractStatus): string {
    const texts: Record<ContractStatus, string> = {
      [ContractStatus.DRAFT]: 'Rascunho',
      [ContractStatus.ACTIVE]: 'Ativo',
      [ContractStatus.EXPIRED]: 'Expirado',
      [ContractStatus.CANCELLED]: 'Cancelado',
      [ContractStatus.SUSPENDED]: 'Suspenso'
    };
    return texts[status] || status;
  }

  /**
   * Verifica se o contrato pode receber ações
   */
  canPerformAction(contrato: Contrato, action: 'edit' | 'activate' | 'cancel' | 'suspend' | 'delete'): boolean {
    switch (action) {
      case 'edit':
        return contrato.canBeEdited;
      case 'activate':
        return contrato.canBeActivated;
      case 'cancel':
        return contrato.canBeCancelled;
      case 'suspend':
        return contrato.status === ContractStatus.ACTIVE;
      case 'delete':
        return contrato.status === ContractStatus.DRAFT;
      default:
        return false;
    }
  }

  /**
   * Calcula dias até expiração
   */
  calcularDiasAteExpiracao(validTo?: string): number | null {
    if (!validTo) return null;
    
    const hoje = new Date();
    const dataExpiracao = new Date(validTo);
    const diffTime = dataExpiracao.getTime() - hoje.getTime();
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    
    return Math.max(0, diffDays);
  }

  /**
   * Verifica se contrato está próximo do vencimento
   */
  isProximoVencimento(contrato: Contrato, dias: number = 30): boolean {
    if (!contrato.validTo) return false;
    
    const diasRestantes = this.calcularDiasAteExpiracao(contrato.validTo);
    return diasRestantes !== null && diasRestantes <= dias && diasRestantes > 0;
  }

  /**
   * Formata informações do contrato para exibição
   */
  formatarContratoParaSelecao(contrato: Contrato): string {
    const categoria = contrato.categoria ? ` - ${contrato.categoria}` : ' - Geral';
    const taxa = this.formatarTaxa(contrato.feeRate);
    const validTo = contrato.validTo ? ` (válido até ${this.formatarData(contrato.validTo)})` : '';
    
    return `${categoria} - Taxa: ${taxa}${validTo}`;
  }
}