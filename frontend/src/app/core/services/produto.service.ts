import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { 
  Produto, 
  ProdutoCreateRequest, 
  ProdutoUpdateRequest, 
  CatalogoFiltro, 
  PaginatedResponse 
} from '../models/produto.model';

interface ApiResponse<T> {
  success: boolean;
  data: T;
  message?: string;
  timestamp: string;
}

/**
 * Serviço para gerenciamento de produtos
 * 
 * ⚠️ ATENÇÃO: Métodos de catálogo público foram depreciados
 * Use LoteCatalogoService para navegação pública
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

  // ===== CATÁLOGO PÚBLICO - DEPRECIADO =====

  /**
   * ⚠️ DEPRECIADO: Busca produtos no catálogo público
   * 
   * @deprecated Use LoteCatalogoService.buscarCatalogoPublico() para navegação por lotes
   * Este método será removido em versão futura.
   * 
   * Migração sugerida:
   * ```typescript
   * // Antigo
   * produtoService.buscarCatalogo(filtros)
   * 
   * // Novo
   * loteCatalogoService.buscarCatalogoPublico(filtros)
   * ```
   */
  @Deprecated('Use LoteCatalogoService.buscarCatalogoPublico()')
  buscarCatalogo(filtros: CatalogoFiltro = {}): Observable<ApiResponse<PaginatedResponse<Produto>>> {
    console.warn('⚠️ MÉTODO DEPRECIADO: buscarCatalogo() foi chamado. Use LoteCatalogoService.buscarCatalogoPublico()');
    
    // Retornar erro informativo
    return throwError(() => ({
      error: {
        success: false,
        message: 'Método depreciado. Use LoteCatalogoService para navegação por lotes.',
        data: null,
        timestamp: new Date().toISOString()
      }
    }));
  }

  /**
   * ⚠️ DEPRECIADO: Busca produto específico no catálogo (público)
   * 
   * @deprecated Produtos agora são acessados através de lotes
   * Use LoteService.buscarLote() e navegue pelos produtos do lote
   * 
   * Migração sugerida:
   * ```typescript
   * // Antigo
   * produtoService.buscarProdutoCatalogo(produtoId)
   * 
   * // Novo
   * loteService.buscarLote(loteId) // e encontrar o produto na lista
   * ```
   */
  @Deprecated('Use LoteService.buscarLote() e navegue pelos produtos')
  buscarProdutoCatalogo(produtoId: string): Observable<ApiResponse<Produto>> {
    console.warn('⚠️ MÉTODO DEPRECIADO: buscarProdutoCatalogo() foi chamado. Use navegação por lotes.');
    
    // Retornar erro informativo
    return throwError(() => ({
      error: {
        success: false,
        message: 'Método depreciado. Produtos são acessados através de lotes.',
        data: null,
        timestamp: new Date().toISOString()
      }
    }));
  }

  /**
   * ✅ MANTIDO: Lista categorias disponíveis
   * 
   * Este método continua ativo pois é usado tanto por lotes quanto produtos.
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