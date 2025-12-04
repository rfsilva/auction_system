import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { 
  LoteCatalogo, 
  LoteCatalogoFiltro, 
  LoteDestaque,
  PAGINACAO_CONFIG 
} from '../models/lote-catalogo.model';
import { ApiResponse } from '../../shared/interfaces/api.interfaces';
import { PaginatedResponse } from '../models/lote.model';

/**
 * Service para catálogo público de lotes
 * História 02: Transformação do Catálogo em Catálogo de Lotes
 */
@Injectable({
  providedIn: 'root'
})
export class LoteCatalogoService {

  private readonly apiUrl = `${environment.apiUrl}/api/lotes`;

  constructor(private http: HttpClient) {}

  /**
   * Busca lotes no catálogo público
   */
  buscarCatalogoPublico(filtros: LoteCatalogoFiltro = {}): Observable<ApiResponse<PaginatedResponse<LoteCatalogo>>> {
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
    
    params = params.set('page', (filtros.page || 0).toString());
    params = params.set('size', (filtros.size || PAGINACAO_CONFIG.padraoLotes).toString());
    
    return this.http.get<ApiResponse<PaginatedResponse<LoteCatalogo>>>(
      `${this.apiUrl}/catalogo-publico`,
      { params }
    );
  }

  /**
   * Busca lotes em destaque (encerrando em 1 semana)
   */
  buscarLotesDestaque(): Observable<ApiResponse<LoteDestaque[]>> {
    return this.http.get<ApiResponse<LoteDestaque[]>>(`${this.apiUrl}/destaque`);
  }

  /**
   * Busca lote específico no catálogo
   */
  buscarLoteCatalogo(loteId: string): Observable<ApiResponse<LoteCatalogo>> {
    return this.http.get<ApiResponse<LoteCatalogo>>(`${this.apiUrl}/${loteId}`);
  }

  // Métodos utilitários

  /**
   * Formata tempo restante para exibição
   */
  formatarTempoRestante(segundos: number): string {
    if (segundos <= 0) {
      return 'Encerrado';
    }

    const dias = Math.floor(segundos / (24 * 3600));
    const horas = Math.floor((segundos % (24 * 3600)) / 3600);
    const minutos = Math.floor((segundos % 3600) / 60);

    if (dias > 0) {
      return `${dias}d ${horas}h`;
    } else if (horas > 0) {
      return `${horas}h ${minutos}m`;
    } else {
      return `${minutos}m`;
    }
  }

  /**
   * Formata data para exibição
   */
  formatarData(dataString: string): string {
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
   * Obtém texto da ordenação
   */
  getOrdenacaoText(ordenacao: string): string {
    switch (ordenacao) {
      case 'proximidade_encerramento':
        return 'Encerrando primeiro';
      case 'recentes':
        return 'Mais recentes';
      case 'alfabetica':
        return 'Ordem alfabética';
      default:
        return 'Encerrando primeiro';
    }
  }

  /**
   * Obtém classe CSS do status
   */
  getStatusClass(lote: LoteCatalogo): string {
    if (!lote.isActive) {
      return 'status-inactive';
    }
    
    if (lote.timeRemaining <= 3600) { // 1 hora
      return 'status-ending-soon';
    }
    
    if (lote.timeRemaining <= 86400) { // 1 dia
      return 'status-ending-today';
    }
    
    return 'status-active';
  }

  /**
   * Obtém texto do status
   */
  getStatusText(lote: LoteCatalogo): string {
    if (!lote.isActive) {
      return 'Encerrado';
    }
    
    if (lote.timeRemaining <= 3600) { // 1 hora
      return 'Encerrando em breve';
    }
    
    if (lote.timeRemaining <= 86400) { // 1 dia
      return 'Encerra hoje';
    }
    
    return 'Ativo';
  }

  /**
   * Verifica se o lote está próximo do encerramento
   */
  isProximoEncerramento(lote: LoteCatalogo): boolean {
    return lote.isActive && lote.timeRemaining <= 86400; // 24 horas
  }

  /**
   * Obtém configuração de paginação
   */
  getPaginacaoConfig() {
    return PAGINACAO_CONFIG;
  }

  /**
   * Valida tamanho de página
   */
  isValidPageSize(size: number): boolean {
    return PAGINACAO_CONFIG.opcoesPorPagina.includes(size);
  }

  /**
   * Obtém primeira imagem válida ou placeholder
   */
  getImagemOuPlaceholder(lote: LoteCatalogo): string {
    if (lote.imagemDestaque && this.isValidImageUrl(lote.imagemDestaque)) {
      return lote.imagemDestaque;
    }
    return '/assets/images/lote-placeholder.jpg';
  }

  /**
   * Valida se URL da imagem é válida
   */
  private isValidImageUrl(url: string): boolean {
    if (!url || url.trim() === '') {
      return false;
    }
    
    // Verificar se é uma URL válida
    try {
      new URL(url);
      return true;
    } catch {
      // Se não for URL absoluta, assumir que é relativa e válida
      return url.startsWith('/') || url.startsWith('./') || !url.includes('://');
    }
  }
}