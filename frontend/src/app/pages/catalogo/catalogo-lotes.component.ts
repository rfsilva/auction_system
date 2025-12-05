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
 */
@Component({
  selector: 'app-catalogo-lotes',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, LoteCardComponent],
  template: `
    <div class="catalogo-container">
      <!-- Header do catálogo -->
      <div class="catalogo-header">
        <div class="header-content">
          <h1>Catálogo de Lotes</h1>
          <p class="subtitle">
            Explore nossos lotes ativos com produtos selecionados
          </p>
        </div>

        <!-- Filtros e busca -->
        <div class="filtros-section">
          <div class="filtros-principais">
            <!-- Busca -->
            <div class="search-box">
              <input
                type="text"
                [(ngModel)]="filtros.termo"
                placeholder="Buscar lotes..."
                (keyup.enter)="aplicarFiltros()"
                class="search-input">
              <button 
                type="button" 
                (click)="aplicarFiltros()"
                class="search-btn">
                <i class="icon-search"></i>
              </button>
            </div>

            <!-- Categoria -->
            <select 
              [(ngModel)]="filtros.categoria"
              (change)="aplicarFiltros()"
              class="filter-select">
              <option value="">Todas as categorias</option>
              <option *ngFor="let categoria of categorias" [value]="categoria">
                {{ categoria }}
              </option>
            </select>

            <!-- Ordenação -->
            <select 
              [(ngModel)]="filtros.ordenacao"
              (change)="aplicarFiltros()"
              class="filter-select">
              <option value="proximidade_encerramento">Encerrando primeiro</option>
              <option value="recentes">Mais recentes</option>
              <option value="alfabetica">Ordem alfabética</option>
            </select>

            <!-- Botão de filtros avançados -->
            <button 
              type="button"
              (click)="toggleFiltros()"
              class="btn-filtros"
              [class.active]="filtrosExpandidos">
              <i class="icon-filter"></i>
              Filtros
            </button>
          </div>

          <!-- Filtros expandidos -->
          <div class="filtros-expandidos" *ngIf="filtrosExpandidos">
            <div class="filtros-grid">
              <div class="filter-group">
                <label>Itens por página:</label>
                <select 
                  [(ngModel)]="filtros.size"
                  (change)="aplicarFiltros()"
                  class="filter-select small">
                  <option value="10">10</option>
                  <option value="20">20</option>
                  <option value="50">50</option>
                </select>
              </div>
            </div>

            <div class="filtros-actions">
              <button 
                type="button"
                (click)="limparFiltros()"
                class="btn-limpar">
                Limpar Filtros
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Resultados -->
      <div class="catalogo-content">
        <!-- Loading -->
        <div *ngIf="loading" class="loading-container">
          <div class="loading-spinner"></div>
          <p>Carregando lotes...</p>
        </div>

        <!-- Erro -->
        <div *ngIf="error" class="error-container">
          <div class="error-message">
            <i class="icon-alert"></i>
            <p>{{ error }}</p>
            <button 
              type="button"
              (click)="buscarLotes()"
              class="btn-retry">
              Tentar Novamente
            </button>
          </div>
        </div>

        <!-- Resultados -->
        <div *ngIf="!loading && !error" class="resultados-container">
          <!-- Info dos resultados -->
          <div class="resultados-info">
            <p>
              <strong>{{ totalElements }}</strong> 
              {{ totalElements === 1 ? 'lote encontrado' : 'lotes encontrados' }}
              <span *ngIf="filtros.termo"> para "{{ filtros.termo }}"</span>
            </p>
            
            <div class="ordenacao-info">
              Ordenado por: {{ getOrdenacaoText() }}
            </div>
          </div>

          <!-- Grid de lotes -->
          <div class="lotes-grid" *ngIf="lotes.length > 0">
            <app-lote-card
              *ngFor="let lote of lotes; trackBy: trackByLoteId"
              [lote]="lote"
              [showFavoriteButton]="false"
              [isFavorito]="false">
            </app-lote-card>
          </div>

          <!-- Estado vazio -->
          <div *ngIf="lotes.length === 0" class="empty-state">
            <div class="empty-content">
              <i class="icon-search-empty"></i>
              <h3>Nenhum lote encontrado</h3>
              <p>
                Não encontramos lotes que correspondam aos seus critérios de busca.
              </p>
              <button 
                type="button"
                (click)="limparFiltros()"
                class="btn-limpar-filtros">
                Limpar Filtros
              </button>
            </div>
          </div>

          <!-- Paginação -->
          <div class="paginacao" *ngIf="totalPages > 1">
            <div class="paginacao-info">
              Página {{ currentPage + 1 }} de {{ totalPages }}
            </div>

            <div class="paginacao-controls">
              <button 
                type="button"
                (click)="paginaAnterior()"
                [disabled]="currentPage === 0"
                class="btn-paginacao">
                <i class="icon-chevron-left"></i>
                Anterior
              </button>

              <div class="paginacao-numeros">
                <button
                  *ngFor="let page of getPaginationPages()"
                  type="button"
                  (click)="irParaPagina(page)"
                  [class.active]="page === currentPage"
                  class="btn-pagina">
                  {{ page + 1 }}
                </button>
              </div>

              <button 
                type="button"
                (click)="proximaPagina()"
                [disabled]="currentPage >= totalPages - 1"
                class="btn-paginacao">
                Próxima
                <i class="icon-chevron-right"></i>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
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