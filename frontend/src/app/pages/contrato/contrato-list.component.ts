import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Subject, takeUntil, debounceTime, distinctUntilChanged } from 'rxjs';

import { ContratoService } from '../../core/services/contrato.service';
import { AuthService } from '../../core/services/auth.service';
import { 
  Contrato, 
  ContratoFiltro, 
  PaginatedResponse, 
  ContractStatus 
} from '../../core/models/contrato.model';

@Component({
  selector: 'app-contrato-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './contrato-list.component.html',
  styleUrl: './contrato-list.component.scss'
})
export class ContratoListComponent implements OnInit, OnDestroy {
  
  private destroy$ = new Subject<void>();
  private searchSubject = new Subject<string>();

  // Dados
  contratos: Contrato[] = [];
  paginacao: PaginatedResponse<Contrato> | null = null;
  categorias: string[] = [];
  
  // Estados
  loading = false;
  error: string | null = null;
  
  // Filtros
  filtros: ContratoFiltro = {
    page: 0,
    size: 20,
    sort: 'createdAt',
    direction: 'desc'
  };
  
  // UI
  mostrarFiltros = false;
  expandedContratos = new Set<string>();
  
  // Enums para template
  ContractStatus = ContractStatus;
  
  // Math para template
  Math = Math;

  constructor(
    private contratoService: ContratoService,
    public authService: AuthService
  ) {
    // Setup do debounce para busca
    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      takeUntil(this.destroy$)
    ).subscribe(termo => {
      this.filtros.termo = termo;
      this.filtros.page = 0;
      this.carregarContratos();
    });
  }

  ngOnInit(): void {
    this.carregarContratos();
    this.carregarCategorias();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // Carregamento de dados

  carregarContratos(page: number = 0): void {
    this.loading = true;
    this.error = null;
    this.filtros.page = page;

    this.contratoService.listarContratos(this.filtros)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.contratos = response.data.content;
            this.paginacao = response.data;
          } else {
            this.error = response.message || 'Erro ao carregar contratos';
          }
          this.loading = false;
        },
        error: (error) => {
          console.error('Erro ao carregar contratos:', error);
          this.error = 'Erro ao carregar contratos. Tente novamente.';
          this.loading = false;
        }
      });
  }

  carregarCategorias(): void {
    this.contratoService.listarCategorias()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.categorias = response.data;
          }
        },
        error: (error) => {
          console.error('Erro ao carregar categorias:', error);
        }
      });
  }

  // Ações de contrato

  ativarContrato(contrato: Contrato): void {
    if (!confirm(`Tem certeza que deseja ativar o contrato de ${contrato.sellerName}?`)) {
      return;
    }

    this.contratoService.ativarContrato(contrato.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.carregarContratos(this.filtros.page);
          } else {
            alert(response.message || 'Erro ao ativar contrato');
          }
        },
        error: (error) => {
          console.error('Erro ao ativar contrato:', error);
          alert('Erro ao ativar contrato. Tente novamente.');
        }
      });
  }

  cancelarContrato(contrato: Contrato): void {
    if (!confirm(`Tem certeza que deseja cancelar o contrato de ${contrato.sellerName}?`)) {
      return;
    }

    this.contratoService.cancelarContrato(contrato.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.carregarContratos(this.filtros.page);
          } else {
            alert(response.message || 'Erro ao cancelar contrato');
          }
        },
        error: (error) => {
          console.error('Erro ao cancelar contrato:', error);
          alert('Erro ao cancelar contrato. Tente novamente.');
        }
      });
  }

  suspenderContrato(contrato: Contrato): void {
    if (!confirm(`Tem certeza que deseja suspender o contrato de ${contrato.sellerName}?`)) {
      return;
    }

    this.contratoService.suspenderContrato(contrato.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.carregarContratos(this.filtros.page);
          } else {
            alert(response.message || 'Erro ao suspender contrato');
          }
        },
        error: (error) => {
          console.error('Erro ao suspender contrato:', error);
          alert('Erro ao suspender contrato. Tente novamente.');
        }
      });
  }

  excluirContrato(contrato: Contrato): void {
    if (!confirm(`Tem certeza que deseja excluir o contrato de ${contrato.sellerName}? Esta ação não pode ser desfeita.`)) {
      return;
    }

    this.contratoService.excluirContrato(contrato.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.carregarContratos(this.filtros.page);
          } else {
            alert(response.message || 'Erro ao excluir contrato');
          }
        },
        error: (error) => {
          console.error('Erro ao excluir contrato:', error);
          alert('Erro ao excluir contrato. Tente novamente.');
        }
      });
  }

  // Filtros e busca

  aplicarFiltros(): void {
    this.filtros.page = 0;
    this.carregarContratos();
  }

  limparFiltros(): void {
    this.filtros = {
      page: 0,
      size: 20,
      sort: 'createdAt',
      direction: 'desc'
    };
    this.carregarContratos();
  }

  onBuscar(event: Event): void {
    const target = event.target as HTMLInputElement;
    this.searchSubject.next(target.value || '');
  }

  toggleFiltros(): void {
    this.mostrarFiltros = !this.mostrarFiltros;
  }

  // Paginação

  irParaPagina(page: number): void {
    if (page >= 0 && page < (this.paginacao?.totalPages || 0)) {
      this.carregarContratos(page);
    }
  }

  proximaPagina(): void {
    if (this.paginacao && !this.paginacao.last) {
      this.irParaPagina(this.paginacao.number + 1);
    }
  }

  paginaAnterior(): void {
    if (this.paginacao && !this.paginacao.first) {
      this.irParaPagina(this.paginacao.number - 1);
    }
  }

  getPaginationPages(): number[] {
    if (!this.paginacao) return [];
    
    const totalPages = this.paginacao.totalPages;
    const currentPage = this.paginacao.number;
    const pages: number[] = [];
    
    const startPage = Math.max(0, currentPage - 2);
    const endPage = Math.min(totalPages - 1, currentPage + 2);
    
    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    
    return pages;
  }

  // UI helpers

  toggleDetalhes(contratoId: string): void {
    if (this.expandedContratos.has(contratoId)) {
      this.expandedContratos.delete(contratoId);
    } else {
      this.expandedContratos.add(contratoId);
    }
  }

  isExpanded(contratoId: string): boolean {
    return this.expandedContratos.has(contratoId);
  }

  // Métodos de verificação de permissões

  canActivate(contrato: Contrato): boolean {
    return this.contratoService.canPerformAction(contrato, 'activate');
  }

  canCancel(contrato: Contrato): boolean {
    return this.contratoService.canPerformAction(contrato, 'cancel');
  }

  canSuspend(contrato: Contrato): boolean {
    return this.contratoService.canPerformAction(contrato, 'suspend');
  }

  canEdit(contrato: Contrato): boolean {
    return this.contratoService.canPerformAction(contrato, 'edit');
  }

  canDelete(contrato: Contrato): boolean {
    return this.contratoService.canPerformAction(contrato, 'delete');
  }

  // Métodos de formatação

  formatarTaxa(taxa: number): string {
    return this.contratoService.formatarTaxa(taxa);
  }

  formatarData(dataString: string): string {
    return this.contratoService.formatarData(dataString);
  }

  getStatusClass(status: ContractStatus): string {
    return this.contratoService.getStatusClass(status);
  }

  getStatusText(status: ContractStatus): string {
    return this.contratoService.getStatusText(status);
  }

  isProximoVencimento(contrato: Contrato): boolean {
    return this.contratoService.isProximoVencimento(contrato, 30);
  }

  // TrackBy function para otimizar *ngFor
  trackByContratoId(index: number, contrato: Contrato): string {
    return contrato.id;
  }
}