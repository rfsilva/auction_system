import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { PublicCatalogoService, LoteDto, LoteFiltro } from '../../core/services/public-catalogo.service';
import { LoteCardComponent } from '../../shared/components/lote-card.component';

/**
 * Componente do catálogo de lotes
 * FASE 2 - Atualização de Componentes: Migrado para PublicCatalogoService
 * 
 * História 02: Transformação do Catálogo em Catálogo de Lotes
 * REFATORADO: Template movido para arquivo HTML externo
 */
@Component({
  selector: 'app-catalogo-lotes',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, LoteCardComponent],
  templateUrl: './catalogo-lotes.component.html',
  styleUrls: ['./catalogo-lotes.component.scss']
})
export class CatalogoLotesComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  lotes: LoteDto[] = [];
  categorias: string[] = [];
  loading = false;
  error = '';
  
  // Filtros
  filtros: LoteFiltro = {
    termo: '',
    categoria: '',
    ordenacao: 'proximidade_encerramento',
    page: 0,
    size: 20
  };

  // Paginação
  currentPage = 0;
  totalPages = 0;
  totalElements = 0;

  // UI
  filtrosExpandidos = false;

  constructor(private publicCatalogoService: PublicCatalogoService) {}

  ngOnInit(): void {
    this.carregarCategorias();
    this.buscarLotes();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // TrackBy function para otimizar *ngFor
  trackByLoteId(index: number, lote: LoteDto): string {
    return lote.id;
  }

  carregarCategorias(): void {
    this.publicCatalogoService.listarCategorias()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.categorias = response.data;
          }
        },
        error: (error) => {
          console.error('Erro ao carregar categorias:', error);
          // Fallback para categorias fixas
          this.categorias = [
            'Eletrônicos',
            'Livros',
            'Arte',
            'Colecionáveis',
            'Móveis',
            'Roupas'
          ];
        }
      });
  }

  buscarLotes(page?: number): void {
    this.loading = true;
    this.error = '';
    
    const filtrosComPagina = { 
      ...this.filtros, 
      page: page !== undefined ? page : this.filtros.page 
    };

    this.publicCatalogoService.buscarLotes(filtrosComPagina)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.success) {
            const paginatedData = response.data;
            this.lotes = paginatedData.content;
            this.currentPage = paginatedData.number;
            this.totalPages = paginatedData.totalPages;
            this.totalElements = paginatedData.totalElements;
            
            console.log('Lotes do catálogo carregados:', this.lotes.length);
          }
          this.loading = false;
        },
        error: (error) => {
          this.error = 'Erro ao carregar lotes. Tente novamente.';
          this.loading = false;
          console.error('Erro ao carregar lotes:', error);
        }
      });
  }

  aplicarFiltros(): void {
    this.filtros.page = 0;
    this.buscarLotes(0);
  }

  limparFiltros(): void {
    this.filtros = {
      termo: '',
      categoria: '',
      ordenacao: 'proximidade_encerramento',
      page: 0,
      size: 20
    };
    this.buscarLotes(0);
  }

  toggleFiltros(): void {
    this.filtrosExpandidos = !this.filtrosExpandidos;
  }

  proximaPagina(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.buscarLotes(this.currentPage + 1);
    }
  }

  paginaAnterior(): void {
    if (this.currentPage > 0) {
      this.buscarLotes(this.currentPage - 1);
    }
  }

  irParaPagina(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.buscarLotes(page);
    }
  }

  getPaginationPages(): number[] {
    const pages: number[] = [];
    const start = Math.max(0, this.currentPage - 2);
    const end = Math.min(this.totalPages - 1, this.currentPage + 2);
    
    for (let i = start; i <= end; i++) {
      pages.push(i);
    }
    
    return pages;
  }

  getOrdenacaoText(): string {
    const ordenacaoMap: { [key: string]: string } = {
      'proximidade_encerramento': 'Encerrando primeiro',
      'recentes': 'Mais recentes',
      'alfabetica': 'Ordem alfabética'
    };
    return ordenacaoMap[this.filtros.ordenacao || 'proximidade_encerramento'] || 'Padrão';
  }
}