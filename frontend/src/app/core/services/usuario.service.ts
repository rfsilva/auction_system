import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface UsuarioSugestaoDto {
  id: string;
  nome: string;
  email: string;
  isVendedor: boolean;
  temContratoAtivo?: boolean;
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
 * Service para operações com usuários (para seleção em contratos)
 */
@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private readonly baseUrl = `${environment.apiUrl}/admin/usuarios`;

  constructor(private http: HttpClient) {}

  /**
   * Lista todos os usuários com paginação
   */
  listarUsuarios(
    page: number = 0, 
    size: number = 20, 
    termo?: string,
    tipoFiltro?: string
  ): Observable<ApiResponse<PaginatedResponse<UsuarioSugestaoDto>>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (termo && termo.trim()) {
      params = params.set('termo', termo.trim());
    }

    if (tipoFiltro && tipoFiltro !== '') {
      params = params.set('tipo', tipoFiltro);
    }

    return this.http.get<ApiResponse<PaginatedResponse<UsuarioSugestaoDto>>>(`${this.baseUrl}/listar`, { params });
  }

  /**
   * Busca usuários para seleção em contratos (método antigo - manter compatibilidade)
   */
  buscarUsuarios(termo: string, limit: number = 10): Observable<ApiResponse<UsuarioSugestaoDto[]>> {
    let params = new HttpParams()
      .set('termo', termo)
      .set('limit', limit.toString());

    return this.http.get<ApiResponse<UsuarioSugestaoDto[]>>(`${this.baseUrl}/buscar`, { params });
  }

  /**
   * Verifica se usuário pode ser vendedor
   */
  podeSerVendedor(usuarioId: string): Observable<ApiResponse<boolean>> {
    return this.http.get<ApiResponse<boolean>>(`${this.baseUrl}/${usuarioId}/pode-ser-vendedor`);
  }

  // Métodos utilitários

  /**
   * Formata nome do usuário para exibição
   */
  formatarNomeUsuario(usuario: UsuarioSugestaoDto): string {
    let nome = usuario.nome;
    
    if (usuario.isVendedor) {
      nome += ' (Vendedor)';
    }
    
    if (usuario.temContratoAtivo) {
      nome += ' - Ativo';
    }
    
    return nome;
  }

  /**
   * Obtém classe CSS do status do usuário
   */
  getStatusClass(usuario: UsuarioSugestaoDto): string {
    if (usuario.temContratoAtivo) return 'text-success';
    if (usuario.isVendedor) return 'text-warning';
    return 'text-muted';
  }

  /**
   * Obtém ícone do status do usuário
   */
  getStatusIcon(usuario: UsuarioSugestaoDto): string {
    if (usuario.temContratoAtivo) return 'fas fa-check-circle';
    if (usuario.isVendedor) return 'fas fa-user-tie';
    return 'fas fa-user';
  }
}