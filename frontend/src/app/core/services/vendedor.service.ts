import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface VendedorDto {
  id: string;
  usuarioId: string;
  usuarioNome?: string;
  companyName?: string;
  taxId?: string;
  contactEmail?: string;
  contactPhone?: string;
  description?: string;
  active: boolean;
  verificado: boolean;
  createdAt: string;
  updatedAt: string;
  
  // Campos calculados
  temContratoAtivo?: boolean;
  totalContratos?: number;
}

export interface ApiResponse<T> {
  success: boolean;
  data: T;
  message?: string;
  timestamp: string;
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

/**
 * Service para operações com vendedores
 * CORRIGIDO: Atualizado para usar as rotas reorganizadas do admin
 */
@Injectable({
  providedIn: 'root'
})
export class VendedorService {

  // CORRIGIDO: URL atualizada para a nova estrutura de rotas
  private readonly baseUrl = `${environment.apiUrl}/api/admin/vendedores`;

  constructor(private http: HttpClient) {}

  /**
   * Lista todos os vendedores com paginação
   */
  listarTodos(page: number = 0, size: number = 50, sort: string = 'companyName', direction: string = 'asc'): Observable<ApiResponse<PaginatedResponse<VendedorDto>>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort)
      .set('direction', direction);

    return this.http.get<ApiResponse<PaginatedResponse<VendedorDto>>>(this.baseUrl, { params });
  }

  /**
   * Lista apenas vendedores ativos (para seleção em formulários)
   * CORRIGIDO: Agora usa a rota correta /api/admin/vendedores/ativos
   */
  listarAtivos(): Observable<ApiResponse<VendedorDto[]>> {
    return this.http.get<ApiResponse<VendedorDto[]>>(`${this.baseUrl}/ativos`);
  }

  /**
   * Busca vendedor por ID
   */
  buscarPorId(vendedorId: string): Observable<ApiResponse<VendedorDto>> {
    return this.http.get<ApiResponse<VendedorDto>>(`${this.baseUrl}/${vendedorId}`);
  }

  // Métodos utilitários

  /**
   * Formata nome do vendedor para exibição
   */
  formatarNomeVendedor(vendedor: VendedorDto): string {
    if (vendedor.companyName && vendedor.usuarioNome) {
      return `${vendedor.usuarioNome} - ${vendedor.companyName}`;
    } else if (vendedor.companyName) {
      return vendedor.companyName;
    } else if (vendedor.usuarioNome) {
      return vendedor.usuarioNome;
    } else {
      return `Vendedor ${vendedor.id.substring(0, 8)}`;
    }
  }

  /**
   * Obtém classe CSS do status
   */
  getStatusClass(vendedor: VendedorDto): string {
    if (!vendedor.active) return 'badge-secondary';
    if (vendedor.temContratoAtivo) return 'badge-success';
    if (vendedor.verificado) return 'badge-warning';
    return 'badge-secondary';
  }

  /**
   * Obtém texto do status
   */
  getStatusText(vendedor: VendedorDto): string {
    if (!vendedor.active) return 'Inativo';
    if (vendedor.temContratoAtivo) return 'Ativo';
    if (vendedor.verificado) return 'Verificado';
    return 'Pendente';
  }
}