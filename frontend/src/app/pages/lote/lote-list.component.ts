import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { LoteService } from '../../core/services/lote.service';
import { AuthService } from '../../core/services/auth.service';
import { Lote, PaginatedResponse } from '../../core/models/lote.model';

@Component({
  selector: 'app-lote-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './lote-list.component.html',
  styleUrls: ['./lote-list.component.scss']
})
export class LoteListComponent implements OnInit {

  lotes: Lote[] = [];
  loading = false;
  error = '';
  success = '';
  
  // Paginação
  currentPage = 0;
  totalPages = 0;
  totalElements = 0;
  pageSize = 20;
  
  // Controle de expansão de detalhes
  expandedLoteId: string | null = null;
  
  constructor(
    private loteService: LoteService,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    this.carregarLotes();
  }

  carregarLotes(page: number = 0): void {
    this.loading = true;
    this.error = '';
    
    this.loteService.listarMeusLotes(page, this.pageSize).subscribe({
      next: (response) => {
        if (response.success) {
          this.lotes = response.data.content;
          this.currentPage = response.data.number;
          this.totalPages = response.data.totalPages;
          this.totalElements = response.data.totalElements;
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('Erro ao carregar lotes:', error);
        this.error = 'Erro ao carregar lotes';
        this.loading = false;
      }
    });
  }

  // Controle de expansão de detalhes
  toggleDetalhes(loteId: string): void {
    this.expandedLoteId = this.expandedLoteId === loteId ? null : loteId;
  }

  isExpanded(loteId: string): boolean {
    return this.expandedLoteId === loteId;
  }

  ativarLote(lote: Lote): void {
    if (!this.canActivate(lote)) return;
    
    if (!confirm(`Tem certeza que deseja ativar o lote "${lote.title}"?`)) {
      return;
    }
    
    this.loteService.ativarLote(lote.id).subscribe({
      next: (response) => {
        if (response.success) {
          this.success = 'Lote ativado com sucesso!';
          this.carregarLotes(this.currentPage);
          setTimeout(() => this.success = '', 3000);
        }
      },
      error: (error) => {
        console.error('Erro ao ativar lote:', error);
        this.error = error.error?.message || 'Erro ao ativar lote';
        setTimeout(() => this.error = '', 5000);
      }
    });
  }

  cancelarLote(lote: Lote): void {
    if (!this.canCancel(lote)) return;
    
    if (!confirm(`Tem certeza que deseja cancelar o lote "${lote.title}"?`)) {
      return;
    }
    
    this.loteService.cancelarLote(lote.id).subscribe({
      next: (response) => {
        if (response.success) {
          this.success = 'Lote cancelado com sucesso!';
          this.carregarLotes(this.currentPage);
          setTimeout(() => this.success = '', 3000);
        }
      },
      error: (error) => {
        console.error('Erro ao cancelar lote:', error);
        this.error = error.error?.message || 'Erro ao cancelar lote';
        setTimeout(() => this.error = '', 5000);
      }
    });
  }

  excluirLote(lote: Lote): void {
    if (!this.canDelete(lote)) return;
    
    if (!confirm(`Tem certeza que deseja excluir o lote "${lote.title}"? Esta ação não pode ser desfeita.`)) {
      return;
    }
    
    this.loteService.excluirLote(lote.id).subscribe({
      next: (response) => {
        if (response.success) {
          this.success = 'Lote excluído com sucesso!';
          this.carregarLotes(this.currentPage);
          setTimeout(() => this.success = '', 3000);
        }
      },
      error: (error) => {
        console.error('Erro ao excluir lote:', error);
        this.error = error.error?.message || 'Erro ao excluir lote';
        setTimeout(() => this.error = '', 5000);
      }
    });
  }

  // Paginação
  irParaPagina(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.carregarLotes(page);
    }
  }

  proximaPagina(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.carregarLotes(this.currentPage + 1);
    }
  }

  paginaAnterior(): void {
    if (this.currentPage > 0) {
      this.carregarLotes(this.currentPage - 1);
    }
  }

  getPaginationPages(): number[] {
    const pages: number[] = [];
    const maxPages = 5;
    let startPage = Math.max(0, this.currentPage - Math.floor(maxPages / 2));
    let endPage = Math.min(this.totalPages - 1, startPage + maxPages - 1);
    
    if (endPage - startPage < maxPages - 1) {
      startPage = Math.max(0, endPage - maxPages + 1);
    }
    
    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    
    return pages;
  }

  // Métodos de verificação de permissões
  canEdit(lote: Lote): boolean {
    return this.loteService.canPerformAction(lote, 'edit');
  }

  canActivate(lote: Lote): boolean {
    return this.loteService.canPerformAction(lote, 'activate');
  }

  canCancel(lote: Lote): boolean {
    return this.loteService.canPerformAction(lote, 'cancel');
  }

  canDelete(lote: Lote): boolean {
    return this.loteService.canPerformAction(lote, 'delete');
  }

  // Métodos de formatação
  formatarTempoRestante(segundos: number): string {
    return this.loteService.formatarTempoRestante(segundos);
  }

  getStatusText(status: string): string {
    return this.loteService.getStatusText(status);
  }

  getStatusClass(status: string): string {
    return this.loteService.getStatusClass(status);
  }

  formatarData(dataString: string): string {
    return this.loteService.formatarData(dataString);
  }
}