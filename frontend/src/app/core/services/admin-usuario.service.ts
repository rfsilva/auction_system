import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { 
  AdminUsuario, 
  AdminUsuarioFiltro, 
  AdminUsuarioUpdateRequest,
  PaginatedResponse, 
  ApiResponse,
  UserStatus,
  UserRole,
  UsuarioSugestaoDto
} from '../models/admin-usuario.model';

/**
 * Service para gestão administrativa de usuários
 */
@Injectable({
  providedIn: 'root'
})
export class AdminUsuarioService {

  private readonly baseUrl = `${environment.apiUrl}/api/admin/usuarios`;

  constructor(private http: HttpClient) {}

  /**
   * Lista todos os usuários com paginação e filtros
   */
  listarUsuarios(filtros: AdminUsuarioFiltro = {}): Observable<ApiResponse<PaginatedResponse<AdminUsuario>>> {
    let params = new HttpParams()
      .set('page', (filtros.page || 0).toString())
      .set('size', (filtros.size || 20).toString())
      .set('sort', filtros.sort || 'nome')
      .set('direction', filtros.direction || 'asc');

    if (filtros.nome) {
      params = params.set('nome', filtros.nome);
    }
    if (filtros.email) {
      params = params.set('email', filtros.email);
    }
    if (filtros.status) {
      params = params.set('status', filtros.status);
    }
    if (filtros.role) {
      params = params.set('role', filtros.role);
    }
    if (filtros.emailVerificado !== undefined) {
      params = params.set('emailVerificado', filtros.emailVerificado.toString());
    }
    if (filtros.telefoneVerificado !== undefined) {
      params = params.set('telefoneVerificado', filtros.telefoneVerificado.toString());
    }
    if (filtros.isVendedor !== undefined) {
      params = params.set('isVendedor', filtros.isVendedor.toString());
    }
    if (filtros.temContratoAtivo !== undefined) {
      params = params.set('temContratoAtivo', filtros.temContratoAtivo.toString());
    }

    return this.http.get<ApiResponse<PaginatedResponse<AdminUsuario>>>(`${this.baseUrl}/admin-list`, { params });
  }

  /**
   * Busca usuários por termo (nome ou email) para seleção em formulários
   * Usado principalmente para ativação de vendedores
   */
  buscarUsuarios(termo: string, limit: number = 10): Observable<ApiResponse<UsuarioSugestaoDto[]>> {
    let params = new HttpParams()
      .set('termo', termo)
      .set('limit', limit.toString());

    return this.http.get<ApiResponse<UsuarioSugestaoDto[]>>(`${this.baseUrl}/buscar`, { params });
  }

  /**
   * Busca usuário por ID
   */
  buscarUsuario(usuarioId: string): Observable<ApiResponse<AdminUsuario>> {
    return this.http.get<ApiResponse<AdminUsuario>>(`${this.baseUrl}/${usuarioId}`);
  }

  /**
   * Atualiza dados do usuário
   */
  atualizarUsuario(usuarioId: string, dados: AdminUsuarioUpdateRequest): Observable<ApiResponse<AdminUsuario>> {
    return this.http.put<ApiResponse<AdminUsuario>>(`${this.baseUrl}/${usuarioId}`, dados);
  }

  /**
   * Ativa usuário
   */
  ativarUsuario(usuarioId: string): Observable<ApiResponse<AdminUsuario>> {
    return this.http.patch<ApiResponse<AdminUsuario>>(`${this.baseUrl}/${usuarioId}/ativar`, {});
  }

  /**
   * Desativa usuário
   */
  desativarUsuario(usuarioId: string): Observable<ApiResponse<AdminUsuario>> {
    return this.http.patch<ApiResponse<AdminUsuario>>(`${this.baseUrl}/${usuarioId}/desativar`, {});
  }

  /**
   * Suspende usuário (equivalente ao "bloquear")
   */
  bloquearUsuario(usuarioId: string, motivo?: string): Observable<ApiResponse<AdminUsuario>> {
    const body = motivo ? { motivo } : {};
    return this.http.patch<ApiResponse<AdminUsuario>>(`${this.baseUrl}/${usuarioId}/bloquear`, body);
  }

  /**
   * Remove suspensão do usuário (equivalente ao "desbloquear")
   */
  desbloquearUsuario(usuarioId: string): Observable<ApiResponse<AdminUsuario>> {
    return this.http.patch<ApiResponse<AdminUsuario>>(`${this.baseUrl}/${usuarioId}/desbloquear`, {});
  }

  /**
   * Verifica email do usuário
   */
  verificarEmail(usuarioId: string): Observable<ApiResponse<AdminUsuario>> {
    return this.http.patch<ApiResponse<AdminUsuario>>(`${this.baseUrl}/${usuarioId}/verificar-email`, {});
  }

  /**
   * Verifica telefone do usuário
   */
  verificarTelefone(usuarioId: string): Observable<ApiResponse<AdminUsuario>> {
    return this.http.patch<ApiResponse<AdminUsuario>>(`${this.baseUrl}/${usuarioId}/verificar-telefone`, {});
  }

  /**
   * Adiciona role ao usuário
   */
  adicionarRole(usuarioId: string, role: UserRole): Observable<ApiResponse<AdminUsuario>> {
    return this.http.patch<ApiResponse<AdminUsuario>>(`${this.baseUrl}/${usuarioId}/roles/adicionar`, { role });
  }

  /**
   * Remove role do usuário
   */
  removerRole(usuarioId: string, role: UserRole): Observable<ApiResponse<AdminUsuario>> {
    return this.http.patch<ApiResponse<AdminUsuario>>(`${this.baseUrl}/${usuarioId}/roles/remover`, { role });
  }

  /**
   * Redefine senha do usuário
   */
  redefinirSenha(usuarioId: string): Observable<ApiResponse<{ novaSenha: string }>> {
    return this.http.patch<ApiResponse<{ novaSenha: string }>>(`${this.baseUrl}/${usuarioId}/redefinir-senha`, {});
  }

  /**
   * Obtém estatísticas de usuários
   */
  obterEstatisticas(): Observable<ApiResponse<any>> {
    return this.http.get<ApiResponse<any>>(`${this.baseUrl}/estatisticas`);
  }

  // Métodos utilitários

  /**
   * Formata nome do usuário para exibição
   */
  formatarNomeUsuario(usuario: AdminUsuario): string {
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
   * Formata nome do usuário sugestão para exibição
   */
  formatarNomeUsuarioSugestao(usuario: UsuarioSugestaoDto): string {
    let nome = `${usuario.nome} (${usuario.email})`;
    
    if (usuario.isVendedor) {
      nome += ' - Vendedor';
    }
    
    if (usuario.temContratoAtivo) {
      nome += ' Ativo';
    }
    
    return nome;
  }

  /**
   * Obtém classe CSS do status do usuário
   */
  getStatusClass(status: UserStatus): string {
    switch (status) {
      case UserStatus.ACTIVE:
        return 'badge bg-success';
      case UserStatus.INACTIVE:
        return 'badge bg-secondary';
      case UserStatus.SUSPENDED:  // Mudança: SUSPENDED em vez de BLOCKED
        return 'badge bg-danger';
      case UserStatus.PENDING_VERIFICATION:
        return 'badge bg-warning';
      default:
        return 'badge bg-secondary';
    }
  }

  /**
   * Obtém texto do status do usuário
   */
  getStatusText(status: UserStatus): string {
    switch (status) {
      case UserStatus.ACTIVE:
        return 'Ativo';
      case UserStatus.INACTIVE:
        return 'Inativo';
      case UserStatus.SUSPENDED:  // Mudança: SUSPENDED em vez de BLOCKED
        return 'Suspenso';
      case UserStatus.PENDING_VERIFICATION:
        return 'Pendente';
      default:
        return 'Desconhecido';
    }
  }

  /**
   * Obtém ícone do status do usuário
   */
  getStatusIcon(status: UserStatus): string {
    switch (status) {
      case UserStatus.ACTIVE:
        return 'fas fa-check-circle';
      case UserStatus.INACTIVE:
        return 'fas fa-pause-circle';
      case UserStatus.SUSPENDED:  // Mudança: SUSPENDED em vez de BLOCKED
        return 'fas fa-ban';
      case UserStatus.PENDING_VERIFICATION:
        return 'fas fa-clock';
      default:
        return 'fas fa-question-circle';
    }
  }

  /**
   * Obtém classe CSS da role
   */
  getRoleClass(role: UserRole): string {
    switch (role) {
      case UserRole.ADMIN:
        return 'badge bg-danger';
      case UserRole.SELLER:
        return 'badge bg-info';
      case UserRole.BUYER:  // Mudança: BUYER em vez de BIDDER
        return 'badge bg-primary';
      case UserRole.VISITOR:
        return 'badge bg-secondary';
      case UserRole.PARTICIPANT:
        return 'badge bg-warning';
      default:
        return 'badge bg-secondary';
    }
  }

  /**
   * Obtém texto da role
   */
  getRoleText(role: UserRole): string {
    switch (role) {
      case UserRole.ADMIN:
        return 'Administrador';
      case UserRole.SELLER:
        return 'Vendedor';
      case UserRole.BUYER:  // Mudança: BUYER em vez de BIDDER
        return 'Comprador';
      case UserRole.VISITOR:
        return 'Visitante';
      case UserRole.PARTICIPANT:
        return 'Participante';
      default:
        return 'Desconhecido';
    }
  }

  /**
   * Formata data para exibição
   */
  formatarData(dataString?: string): string {
    if (!dataString) return 'Nunca';
    
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
   * Verifica se usuário pode ser editado
   */
  canEdit(usuario: AdminUsuario): boolean {
    return usuario.status !== UserStatus.SUSPENDED;  // Mudança: SUSPENDED em vez de BLOCKED
  }

  /**
   * Verifica se usuário pode ser ativado
   */
  canActivate(usuario: AdminUsuario): boolean {
    return usuario.status === UserStatus.INACTIVE || usuario.status === UserStatus.PENDING_VERIFICATION;
  }

  /**
   * Verifica se usuário pode ser desativado
   */
  canDeactivate(usuario: AdminUsuario): boolean {
    return usuario.status === UserStatus.ACTIVE;
  }

  /**
   * Verifica se usuário pode ser suspenso
   */
  canBlock(usuario: AdminUsuario): boolean {
    return usuario.status !== UserStatus.SUSPENDED;  // Mudança: SUSPENDED em vez de BLOCKED
  }

  /**
   * Verifica se usuário pode ter suspensão removida
   */
  canUnblock(usuario: AdminUsuario): boolean {
    return usuario.status === UserStatus.SUSPENDED;  // Mudança: SUSPENDED em vez de BLOCKED
  }
}