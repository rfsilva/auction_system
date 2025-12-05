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

export interface ProdutoDto {
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
  status: string;
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
  timeRemaining: number;
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
 * HISTÓRIA 03: Adicionado método para listar produtos válidos de um lote
 * HISTÓRIA 04: Adicionado método para buscar produto específico válido de um lote
 * 
 * Conecta com os endpoints do PublicoController no backend:
 * - GET /public/catalogo/lotes
 * - GET /public/catalogo/lotes/{id}
 * - GET /public/catalogo/lotes/{id}/produtos (História 03)
 * - GET /public/catalogo/lotes/{loteId}/produtos/{produtoId} (História 04)
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
   * HISTÓRIA 03: Lista produtos válidos de um lote específico com paginação
   * Endpoint: GET /public/catalogo/lotes/{id}/produtos
   */
  listarProdutosDoLote(loteId: string, page: number = 0, size: number = 20): Observable<ApiResponse<Page<ProdutoDto>>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', 'createdAt');

    return this.http.get<ApiResponse<Page<ProdutoDto>>>(`${this.baseUrl}/catalogo/lotes/${loteId}/produtos`, { params });
  }

  /**
   * HISTÓRIA 04: Busca produto específico válido de um lote
   * Endpoint: GET /public/catalogo/lotes/{loteId}/produtos/{produtoId}
   */
  buscarProdutoDoLote(loteId: string, produtoId: string): Observable<ApiResponse<ProdutoDto>> {
    return this.http.get<ApiResponse<ProdutoDto>>(`${this.baseUrl}/catalogo/lotes/${loteId}/produtos/${produtoId}`);
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

  // ========================================
  // Métodos auxiliares para formatação
  // ========================================

  /**
   * Formata tempo restante em formato legível
   */
  formatarTempoRestante(segundos: number): string {
    if (segundos <= 0) {
      return 'Expirado';
    }

    const dias = Math.floor(segundos / (24 * 3600));
    const horas = Math.floor((segundos % (24 * 3600)) / 3600);
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
   * Formata preço em formato brasileiro
   */
  formatarPreco(preco: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(preco);
  }

  /**
   * Obtém primeira imagem válida de uma lista
   */
  obterPrimeiraImagem(images: string[]): string | null {
    if (!images || images.length === 0) {
      return null;
    }
    
    // Filtrar apenas URLs válidas
    const imagensValidas = images.filter(img => 
      img && img.trim() !== '' && this.isValidUrl(img)
    );
    
    return imagensValidas.length > 0 ? imagensValidas[0] : null;
  }

  /**
   * Verifica se uma string é uma URL válida
   */
  private isValidUrl(url: string): boolean {
    try {
      new URL(url);
      return true;
    } catch {
      return false;
    }
  }
}