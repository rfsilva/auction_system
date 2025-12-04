import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { 
  Produto, 
  ProdutoCreateRequest, 
  ProdutoUpdateRequest, 
  PaginatedResponse 
} from '../models/produto.model';

interface ApiResponse<T> {
  success: boolean;
  data: T;
  message?: string;
  timestamp: string;
}

/**
 * Serviço para gerenciamento de produtos (área privada - vendedores)
 * 
 * Nota: Catálogo público foi movido para LoteCatalogoService
 * Produtos são acessados publicamente apenas através de lotes
 */
@Injectable({
  providedIn: 'root'
})
export class ProdutoService {
  private readonly apiUrl = `${environment.apiUrl}`;

  constructor(private http: HttpClient) {}

  // ===== CRUD para Vendedores (ÁREA PRIVADA) =====

  /**
   * Cria um novo produto
   */
  criarProduto(produto: ProdutoCreateRequest): Observable<ApiResponse<Produto>> {
    return this.http.post<ApiResponse<Produto>>(`${this.apiUrl}/produtos`, produto);
  }

  /**
   * Atualiza um produto existente
   */
  atualizarProduto(produtoId: string, produto: ProdutoUpdateRequest): Observable<ApiResponse<Produto>> {
    return this.http.put<ApiResponse<Produto>>(`${this.apiUrl}/produtos/${produtoId}`, produto);
  }

  /**
   * Busca produto por ID (vendedor)
   */
  buscarProduto(produtoId: string): Observable<ApiResponse<Produto>> {
    return this.http.get<ApiResponse<Produto>>(`${this.apiUrl}/produtos/${produtoId}`);
  }

  /**
   * Lista produtos do vendedor logado
   */
  listarMeusProdutos(page: number = 0, size: number = 20): Observable<ApiResponse<PaginatedResponse<Produto>>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<ApiResponse<PaginatedResponse<Produto>>>(`${this.apiUrl}/produtos/meus-produtos`, { params });
  }

  /**
   * Exclui um produto
   */
  excluirProduto(produtoId: string): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/produtos/${produtoId}`);
  }

  /**
   * Publica um produto
   */
  publicarProduto(produtoId: string): Observable<ApiResponse<Produto>> {
    return this.http.post<ApiResponse<Produto>>(`${this.apiUrl}/produtos/${produtoId}/publicar`, {});
  }

  /**
   * Lista categorias disponíveis (usado por lotes e produtos)
   * Endpoint: GET /api/catalogo/categorias
   */
  listarCategorias(): Observable<ApiResponse<string[]>> {
    return this.http.get<ApiResponse<string[]>>(`${this.apiUrl}/catalogo/categorias`);
  }

  // ===== Métodos Utilitários (MANTIDOS) =====

  /**
   * Formata preço para exibição
   */
  formatarPreco(preco: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(preco);
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
      'PENDING_APPROVAL': 'Aguardando Aprovação',
      'ACTIVE': 'Ativo',
      'SOLD': 'Vendido',
      'CANCELLED': 'Cancelado',
      'EXPIRED': 'Expirado'
    };
    return statusMap[status] || status;
  }

  /**
   * Obtém classe CSS do status
   */
  getStatusClass(status: string): string {
    const classMap: { [key: string]: string } = {
      'DRAFT': 'status-draft',
      'PENDING_APPROVAL': 'status-pending',
      'ACTIVE': 'status-active',
      'SOLD': 'status-sold',
      'CANCELLED': 'status-cancelled',
      'EXPIRED': 'status-expired'
    };
    return classMap[status] || 'status-default';
  }
}