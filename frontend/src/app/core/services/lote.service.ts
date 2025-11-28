import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { 
  Lote, 
  LoteCreateRequest, 
  LoteUpdateRequest, 
  LoteFiltro, 
  PaginatedResponse,
  LoteStatus 
} from '../models/lote.model';

interface ApiResponse<T> {
  success: boolean;
  data: T;
  message?: string;
  timestamp: string;
}

/**
 * Service para operações de Lote
 */
@Injectable({
  providedIn: 'root'
})
export class LoteService {

  private readonly apiUrl = `${environment.apiUrl}/lotes`;
  private readonly catalogoUrl = `${environment.apiUrl}/catalogo/lotes`;

  constructor(private http: HttpClient) {}

  /**
   * Cria um novo lote
   */
  criarLote(lote: LoteCreateRequest): Observable<ApiResponse<Lote>> {
    return this.http.post<ApiResponse<Lote>>(this.apiUrl, lote);
  }

  /**
   * Atualiza um lote existente
   */
  atualizarLote(loteId: string, lote: LoteUpdateRequest): Observable<ApiResponse<Lote>> {
    return this.http.put<ApiResponse<Lote>>(`${this.apiUrl}/${loteId}`, lote);
  }

  /**
   * Busca lote por ID
   */
  buscarLote(loteId: string): Observable<ApiResponse<Lote>> {
    return this.http.get<ApiResponse<Lote>>(`${this.apiUrl}/${loteId}`);
  }

  /**
   * Busca lote específico no catálogo (público)
   */
  buscarLoteCatalogo(loteId: string): Observable<ApiResponse<Lote>> {
    return this.http.get<ApiResponse<Lote>>(`${this.catalogoUrl}/${loteId}`);
  }

  /**
   * Lista lotes do vendedor logado
   */
  listarMeusLotes(page: number = 0, size: number = 20): Observable<ApiResponse<PaginatedResponse<Lote>>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<ApiResponse<PaginatedResponse<Lote>>>(`${this.apiUrl}/meus-lotes`, { params });
  }

  /**
   * Busca lotes no catálogo público
   */
  buscarCatalogoLotes(filtros: LoteFiltro = {}): Observable<ApiResponse<PaginatedResponse<Lote>>> {
    let params = new HttpParams();
    
    if (filtros.termo) {
      params = params.set('termo', filtros.termo);
    }
    if (filtros.page !== undefined) {
      params = params.set('page', filtros.page.toString());
    }
    if (filtros.size !== undefined) {
      params = params.set('size', filtros.size.toString());
    }

    return this.http.get<ApiResponse<PaginatedResponse<Lote>>>(this.catalogoUrl, { params });
  }

  /**
   * Ativa um lote
   */
  ativarLote(loteId: string): Observable<ApiResponse<Lote>> {
    return this.http.post<ApiResponse<Lote>>(`${this.apiUrl}/${loteId}/ativar`, {});
  }

  /**
   * Cancela um lote
   */
  cancelarLote(loteId: string): Observable<ApiResponse<Lote>> {
    return this.http.post<ApiResponse<Lote>>(`${this.apiUrl}/${loteId}/cancelar`, {});
  }

  /**
   * Exclui um lote
   */
  excluirLote(loteId: string): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/${loteId}`);
  }

  /**
   * Formata tempo restante
   */
  formatarTempoRestante(segundos: number): string {
    if (segundos <= 0) {
      return 'Encerrado';
    }

    const dias = Math.floor(segundos / 86400);
    const horas = Math.floor((segundos % 86400) / 3600);
    const minutos = Math.floor((segundos % 3600) / 60);

    if (dias > 0) {
      return `${dias}d ${horas}h ${minutos}m`;
    } else if (horas > 0) {
      return `${horas}h ${minutos}m`;
    } else {
      return `${minutos}m`;
    }
  }

  /**
   * Obtém texto do status
   */
  getStatusText(status: string): string {
    const statusMap: { [key: string]: string } = {
      'DRAFT': 'Rascunho',
      'ACTIVE': 'Ativo',
      'CLOSED': 'Fechado',
      'CANCELLED': 'Cancelado'
    };
    return statusMap[status] || status;
  }

  /**
   * Obtém classe CSS do status
   */
  getStatusClass(status: string): string {
    const classMap: { [key: string]: string } = {
      'DRAFT': 'badge-secondary',
      'ACTIVE': 'badge-success',
      'CLOSED': 'badge-info',
      'CANCELLED': 'badge-danger'
    };
    return classMap[status] || 'badge-secondary';
  }

  /**
   * Formata data para exibição
   */
  formatarData(dataString: string): string {
    const data = new Date(dataString);
    return data.toLocaleString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  /**
   * Verifica se o lote pode receber ações
   */
  canPerformAction(lote: Lote, action: 'edit' | 'activate' | 'cancel' | 'delete'): boolean {
    switch (action) {
      case 'edit':
        return lote.canBeEdited;
      case 'activate':
        return lote.canBeActivated;
      case 'cancel':
        return lote.canBeCancelled;
      case 'delete':
        return lote.status === LoteStatus.DRAFT;
      default:
        return false;
    }
  }
}