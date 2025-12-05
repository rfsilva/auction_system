import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface ApiResponse<T> {
  success: boolean;
  data: T;
  message?: string;
  timestamp: string;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

export interface LoteDto {
  id: string;
  sellerId: string;
  title: string;
  description: string;
  loteEndDateTime: string;
  status: string;
  createdAt: string;
  updatedAt: string;
  isActive: boolean;
  isExpired: boolean;
  canBeEdited: boolean;
  canBeActivated: boolean;
  canBeCancelled: boolean;
  timeRemaining: number;
  produtoIds: string[];
  totalProdutos: number;
  
  // ✅ ADICIONADO: Campo para imagem de destaque
  imagemDestaque?: string; // Primeira imagem do primeiro produto válido ou imagem definida pelo vendedor
  
  // Informações adicionais do catálogo
  categoria?: string; // Categoria do contrato/lote
  sellerName?: string; // Nome do vendedor
  sellerCompanyName?: string; // Nome da empresa do vendedor
}

export interface LoteFiltro {
  termo?: string;
  categoria?: string;
  ordenacao?: string;
  page?: number;
  size?: number;
}

/**
 * Service público para catálogo de lotes
 * FASE 2 - Reorganização de Services: Endpoints públicos sem autenticação
 * 
 * Conecta com os endpoints do PublicoController no backend:
 * - GET /public/catalogo/lotes
 * - GET /public/catalogo/lotes/{id}
 * - GET /public/catalogo/lotes/destaque
 * - GET /public/catalogo/categorias
 * - GET /public/sobre
 * - GET /public/contato
 */
@Injectable({
  providedIn: 'root'
})
export class PublicCatalogoService {
  private baseUrl = `${environment.apiUrl}/public`;

  constructor(private http: HttpClient) {}

  /**
   * Lista lotes disponíveis no catálogo público
   */
  buscarLotes(filtros: LoteFiltro = {}): Observable<ApiResponse<Page<LoteDto>>> {
    let params = new HttpParams();
    
    if (filtros.termo) {
      params = params.set('termo', filtros.termo);
    }
    if (filtros.categoria) {
      params = params.set('categoria', filtros.categoria);
    }
    if (filtros.ordenacao) {
      params = params.set('ordenacao', filtros.ordenacao);
    }
    if (filtros.page !== undefined) {
      params = params.set('page', filtros.page.toString());
    }
    if (filtros.size !== undefined) {
      params = params.set('size', filtros.size.toString());
    }

    return this.http.get<ApiResponse<Page<LoteDto>>>(`${this.baseUrl}/catalogo/lotes`, { params });
  }

  /**
   * Busca lote específico no catálogo público
   */
  buscarLote(id: string): Observable<ApiResponse<LoteDto>> {
    return this.http.get<ApiResponse<LoteDto>>(`${this.baseUrl}/catalogo/lotes/${id}`);
  }

  /**
   * Busca lotes em destaque (encerrando em breve)
   */
  buscarLotesDestaque(): Observable<ApiResponse<LoteDto[]>> {
    return this.http.get<ApiResponse<LoteDto[]>>(`${this.baseUrl}/catalogo/lotes/destaque`);
  }

  /**
   * Lista categorias disponíveis no catálogo
   */
  listarCategorias(): Observable<ApiResponse<string[]>> {
    return this.http.get<ApiResponse<string[]>>(`${this.baseUrl}/catalogo/categorias`);
  }

  /**
   * Retorna informações institucionais da plataforma
   */
  obterInformacoesInstitucionais(): Observable<ApiResponse<{[key: string]: string}>> {
    return this.http.get<ApiResponse<{[key: string]: string}>>(`${this.baseUrl}/sobre`);
  }

  /**
   * Retorna informações de contato da plataforma
   */
  obterInformacoesContato(): Observable<ApiResponse<{[key: string]: string}>> {
    return this.http.get<ApiResponse<{[key: string]: string}>>(`${this.baseUrl}/contato`);
  }
}