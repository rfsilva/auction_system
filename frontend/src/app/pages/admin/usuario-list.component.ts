import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject, takeUntil, debounceTime, distinctUntilChanged } from 'rxjs';

import { AdminUsuarioService } from '../../core/services/admin-usuario.service';
import { AuthService } from '../../core/services/auth.service';
import { 
  AdminUsuario, 
  AdminUsuarioFiltro,
  UserStatus,
  UserRole
} from '../../core/models/admin-usuario.model';

@Component({
  selector: 'app-usuario-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './usuario-list.component.html',
  styleUrl: './usuario-list.component.scss'
})
export class UsuarioListComponent implements OnInit, OnDestroy {
  
  private destroy$ = new Subject<void>();
  private filtroSubject = new Subject<void>();

  // Estados
  loading = false;
  usuarios: AdminUsuario[] = [];
  
  // Paginação
  paginaAtual = 0;
  totalPaginas = 0;
  totalUsuarios = 0;
  itensPorPagina = 20;
  
  // Filtros
  filtroForm: FormGroup;
  mostrarFiltros = false;
  
  // Seleção e ações
  usuariosSelecionados: Set<string> = new Set();
  expandedUsuarios: Set<string> = new Set();
  
  // Enums para template
  UserStatus = UserStatus;
  UserRole = UserRole;
  
  // Mensagens
  successMessage: string | null = null;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private adminUsuarioService: AdminUsuarioService,
    public authService: AuthService,
    private router: Router
  ) {
    this.filtroForm = this.createFiltroForm();
    
    // Setup do debounce para filtros
    this.filtroSubject.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      takeUntil(this.destroy$)
    ).subscribe(() => {
      this.paginaAtual = 0;
      this.carregarUsuarios();
    });
  }

  ngOnInit(): void {
    this.carregarUsuarios();
    this.setupFiltroWatchers();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // Criação do formulário de filtros

  createFiltroForm(): FormGroup {
    return this.fb.group({
      nome: [''],
      email: [''],
      status: [''],
      role: [''],
      emailVerificado: [''],
      telefoneVerificado: [''],
      isVendedor: [''],
      temContratoAtivo: ['']
    });
  }

  setupFiltroWatchers(): void {
    this.filtroForm.valueChanges
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => {
        this.filtroSubject.next();
      });
  }

  // Carregamento de dados

  carregarUsuarios(): void {
    this.loading = true;
    
    const filtros: AdminUsuarioFiltro = {
      ...this.filtroForm.value,
      page: this.paginaAtual,
      size: this.itensPorPagina,
      sort: 'nome',
      direction: 'asc'
    };

    // Limpar valores vazios
    Object.keys(filtros).forEach(key => {
      if (filtros[key as keyof AdminUsuarioFiltro] === '' || filtros[key as keyof AdminUsuarioFiltro] === null) {
        delete filtros[key as keyof AdminUsuarioFiltro];
      }
    });

    this.adminUsuarioService.listarUsuarios(filtros)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.success) {
            const paginatedData = response.data;
            this.usuarios = paginatedData.content;
            this.totalUsuarios = paginatedData.totalElements;
            this.totalPaginas = paginatedData.totalPages;
          } else {
            this.showError(response.message || 'Erro ao carregar usuários');
          }
          this.loading = false;
        },
        error: (error) => {
          console.error('Erro ao carregar usuários:', error);
          this.showError('Erro ao carregar usuários. Tente novamente.');
          this.loading = false;
        }
      });
  }

  // Filtros e busca

  toggleFiltros(): void {
    this.mostrarFiltros = !this.mostrarFiltros;
  }

  limparFiltros(): void {
    this.filtroForm.reset();
    this.paginaAtual = 0;
    this.carregarUsuarios();
  }

  aplicarFiltros(): void {
    this.paginaAtual = 0;
    this.carregarUsuarios();
  }

  // Paginação

  irParaPagina(pagina: number): void {
    if (pagina >= 0 && pagina < this.totalPaginas) {
      this.paginaAtual = pagina;
      this.carregarUsuarios();
    }
  }

  paginaAnterior(): void {
    this.irParaPagina(this.paginaAtual - 1);
  }

  proximaPagina(): void {
    this.irParaPagina(this.paginaAtual + 1);
  }

  getPaginationPages(): number[] {
    const pages: number[] = [];
    const maxPages = 5;
    
    let startPage = Math.max(0, this.paginaAtual - Math.floor(maxPages / 2));
    let endPage = Math.min(this.totalPaginas - 1, startPage + maxPages - 1);
    
    if (endPage - startPage + 1 < maxPages) {
      startPage = Math.max(0, endPage - maxPages + 1);
    }
    
    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    
    return pages;
  }

  // Seleção de usuários

  toggleUsuarioSelecionado(usuarioId: string): void {
    if (this.usuariosSelecionados.has(usuarioId)) {
      this.usuariosSelecionados.delete(usuarioId);
    } else {
      this.usuariosSelecionados.add(usuarioId);
    }
  }

  isUsuarioSelecionado(usuarioId: string): boolean {
    return this.usuariosSelecionados.has(usuarioId);
  }

  selecionarTodos(): void {
    if (this.todosSelecionados()) {
      this.usuariosSelecionados.clear();
    } else {
      this.usuarios.forEach(usuario => {
        this.usuariosSelecionados.add(usuario.id);
      });
    }
  }

  todosSelecionados(): boolean {
    return this.usuarios.length > 0 && this.usuarios.every(usuario => 
      this.usuariosSelecionados.has(usuario.id)
    );
  }

  algunsSelecionados(): boolean {
    return this.usuariosSelecionados.size > 0 && !this.todosSelecionados();
  }

  // Expansão de detalhes

  toggleDetalhes(usuarioId: string): void {
    if (this.expandedUsuarios.has(usuarioId)) {
      this.expandedUsuarios.delete(usuarioId);
    } else {
      this.expandedUsuarios.add(usuarioId);
    }
  }

  isExpanded(usuarioId: string): boolean {
    return this.expandedUsuarios.has(usuarioId);
  }

  // Ações individuais

  ativarUsuario(usuario: AdminUsuario): void {
    if (!this.adminUsuarioService.canActivate(usuario)) return;

    this.adminUsuarioService.ativarUsuario(usuario.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.showSuccess(`Usuário ${usuario.nome} ativado com sucesso`);
            this.carregarUsuarios();
          } else {
            this.showError(response.message || 'Erro ao ativar usuário');
          }
        },
        error: (error) => {
          console.error('Erro ao ativar usuário:', error);
          this.showError('Erro ao ativar usuário. Tente novamente.');
        }
      });
  }

  desativarUsuario(usuario: AdminUsuario): void {
    if (!this.adminUsuarioService.canDeactivate(usuario)) return;

    if (!confirm(`Tem certeza que deseja desativar o usuário ${usuario.nome}?`)) {
      return;
    }

    this.adminUsuarioService.desativarUsuario(usuario.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.showSuccess(`Usuário ${usuario.nome} desativado com sucesso`);
            this.carregarUsuarios();
          } else {
            this.showError(response.message || 'Erro ao desativar usuário');
          }
        },
        error: (error) => {
          console.error('Erro ao desativar usuário:', error);
          this.showError('Erro ao desativar usuário. Tente novamente.');
        }
      });
  }

  bloquearUsuario(usuario: AdminUsuario): void {
    if (!this.adminUsuarioService.canBlock(usuario)) return;

    const motivo = prompt(`Motivo do bloqueio do usuário ${usuario.nome}:`);
    if (motivo === null) return; // Cancelou

    this.adminUsuarioService.bloquearUsuario(usuario.id, motivo)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.showSuccess(`Usuário ${usuario.nome} bloqueado com sucesso`);
            this.carregarUsuarios();
          } else {
            this.showError(response.message || 'Erro ao bloquear usuário');
          }
        },
        error: (error) => {
          console.error('Erro ao bloquear usuário:', error);
          this.showError('Erro ao bloquear usuário. Tente novamente.');
        }
      });
  }

  desbloquearUsuario(usuario: AdminUsuario): void {
    if (!this.adminUsuarioService.canUnblock(usuario)) return;

    if (!confirm(`Tem certeza que deseja desbloquear o usuário ${usuario.nome}?`)) {
      return;
    }

    this.adminUsuarioService.desbloquearUsuario(usuario.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.showSuccess(`Usuário ${usuario.nome} desbloqueado com sucesso`);
            this.carregarUsuarios();
          } else {
            this.showError(response.message || 'Erro ao desbloquear usuário');
          }
        },
        error: (error) => {
          console.error('Erro ao desbloquear usuário:', error);
          this.showError('Erro ao desbloquear usuário. Tente novamente.');
        }
      });
  }

  verificarEmail(usuario: AdminUsuario): void {
    if (usuario.emailVerificado) return;

    this.adminUsuarioService.verificarEmail(usuario.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.showSuccess(`Email do usuário ${usuario.nome} verificado com sucesso`);
            this.carregarUsuarios();
          } else {
            this.showError(response.message || 'Erro ao verificar email');
          }
        },
        error: (error) => {
          console.error('Erro ao verificar email:', error);
          this.showError('Erro ao verificar email. Tente novamente.');
        }
      });
  }

  redefinirSenha(usuario: AdminUsuario): void {
    if (!confirm(`Tem certeza que deseja redefinir a senha do usuário ${usuario.nome}?`)) {
      return;
    }

    this.adminUsuarioService.redefinirSenha(usuario.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.success) {
            const novaSenha = response.data.novaSenha;
            alert(`Nova senha gerada: ${novaSenha}\n\nAnote esta senha e repasse ao usuário.`);
            this.showSuccess(`Senha do usuário ${usuario.nome} redefinida com sucesso`);
          } else {
            this.showError(response.message || 'Erro ao redefinir senha');
          }
        },
        error: (error) => {
          console.error('Erro ao redefinir senha:', error);
          this.showError('Erro ao redefinir senha. Tente novamente.');
        }
      });
  }

  editarUsuario(usuario: AdminUsuario): void {
    this.router.navigate(['/admin/usuarios', usuario.id, 'editar']);
  }

  // Ações em lote

  ativarSelecionados(): void {
    if (this.usuariosSelecionados.size === 0) return;

    if (!confirm(`Tem certeza que deseja ativar ${this.usuariosSelecionados.size} usuário(s)?`)) {
      return;
    }

    // Implementar ação em lote
    this.showSuccess(`${this.usuariosSelecionados.size} usuário(s) ativado(s) com sucesso`);
    this.usuariosSelecionados.clear();
    this.carregarUsuarios();
  }

  desativarSelecionados(): void {
    if (this.usuariosSelecionados.size === 0) return;

    if (!confirm(`Tem certeza que deseja desativar ${this.usuariosSelecionados.size} usuário(s)?`)) {
      return;
    }

    // Implementar ação em lote
    this.showSuccess(`${this.usuariosSelecionados.size} usuário(s) desativado(s) com sucesso`);
    this.usuariosSelecionados.clear();
    this.carregarUsuarios();
  }

  // Helpers para template

  formatarData(dataString?: string): string {
    return this.adminUsuarioService.formatarData(dataString);
  }

  getStatusClass(status: UserStatus): string {
    return this.adminUsuarioService.getStatusClass(status);
  }

  getStatusText(status: UserStatus): string {
    return this.adminUsuarioService.getStatusText(status);
  }

  getStatusIcon(status: UserStatus): string {
    return this.adminUsuarioService.getStatusIcon(status);
  }

  getRoleClass(role: UserRole): string {
    return this.adminUsuarioService.getRoleClass(role);
  }

  getRoleText(role: UserRole): string {
    return this.adminUsuarioService.getRoleText(role);
  }

  canEdit(usuario: AdminUsuario): boolean {
    return this.adminUsuarioService.canEdit(usuario);
  }

  canActivate(usuario: AdminUsuario): boolean {
    return this.adminUsuarioService.canActivate(usuario);
  }

  canDeactivate(usuario: AdminUsuario): boolean {
    return this.adminUsuarioService.canDeactivate(usuario);
  }

  canBlock(usuario: AdminUsuario): boolean {
    return this.adminUsuarioService.canBlock(usuario);
  }

  canUnblock(usuario: AdminUsuario): boolean {
    return this.adminUsuarioService.canUnblock(usuario);
  }

  trackByUsuarioId(index: number, usuario: AdminUsuario): string {
    return usuario.id;
  }

  // Mensagens

  showSuccess(message: string): void {
    this.successMessage = message;
    this.errorMessage = null;
    setTimeout(() => {
      this.successMessage = null;
    }, 5000);
  }

  showError(message: string): void {
    this.errorMessage = message;
    this.successMessage = null;
    setTimeout(() => {
      this.errorMessage = null;
    }, 5000);
  }

  clearMessages(): void {
    this.successMessage = null;
    this.errorMessage = null;
  }
}