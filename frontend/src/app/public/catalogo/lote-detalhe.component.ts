import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Subject, takeUntil, finalize } from 'rxjs';
import { PublicCatalogoService, LoteDto, ProdutoDto, ApiResponse, Page } from '../../core/services/public-catalogo.service';
import { ProdutoImageComponent } from '../../shared/components/produto-image.component';

/**
 * HISTÓRIA 03: Página de Detalhes do Lote e Lista de Produtos Válidos
 * 
 * Componente que exibe:
 * - Detalhes completos do lote
 * - Lista paginada de produtos válidos do lote
 * - Navegação entre produtos com paginação configurável (10, 20, 50 por página)
 * - Informações do lote (tempo restante, descrição, regras)
 * - Interface responsiva
 * 
 * CORRIGIDO: Aplicada a mesma solução do lote-card.component.ts para calcular status próprio
 */
@Component({
  selector: 'app-lote-detalhe',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, ProdutoImageComponent],
  templateUrl: './lote-detalhe.component.html',
  styleUrls: ['./lote-detalhe.component.scss']
})
export class LoteDetalheComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  // Dados principais
  lote: LoteDto | null = null;
  produtos: ProdutoDto[] = [];
  loteId: string | null = null;

  // Estados de carregamento
  loadingLote = false;
  loadingProdutos = false;
  error = '';
  errorProdutos = '';

  // Paginação de produtos
  currentPage = 0;
  totalPages = 0;
  totalElements = 0;
  pageSize = 20; // Padrão: 20 produtos por página
  pageSizeOptions = [10, 20, 50]; // Opções configuráveis

  // UI
  Math = Math; // Para usar Math.min, Math.max no template

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private publicCatalogoService: PublicCatalogoService
  ) {}

  ngOnInit(): void {
    this.loteId = this.route.snapshot.paramMap.get('id');
    
    if (!this.loteId) {
      this.error = 'ID do lote não fornecido';
      return;
    }

    this.carregarLote();
    this.carregarProdutos();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // ========================================
  // CORRIGIDO: Métodos de cálculo próprio (mesma solução do lote-card.component.ts)
  // ========================================

  /**
   * CORRIGIDO: Calcula se o lote está ativo baseado na data de encerramento
   * Não confia no campo isActive do backend
   */
  private calculateIsActive(): boolean {
    if (!this.lote?.loteEndDateTime) {
      return false;
    }
    
    const now = new Date();
    const endDate = new Date(this.lote.loteEndDateTime);
    
    // Lote está ativo se:
    // 1. Status é ACTIVE
    // 2. Data de encerramento é no futuro
    // 3. Tem produtos
    return this.lote.status === 'ACTIVE' && 
           endDate > now && 
           (this.lote.totalProdutos > 0);
  }

  /**
   * CORRIGIDO: Calcula se o lote está expirado baseado na data de encerramento
   * Não confia no campo isExpired do backend
   */
  private calculateIsExpired(): boolean {
    if (!this.lote?.loteEndDateTime) {
      return false;
    }
    
    const now = new Date();
    const endDate = new Date(this.lote.loteEndDateTime);
    
    return endDate <= now;
  }

  /**
   * CORRIGIDO: Calcula o tempo restante em segundos
   * Não confia no campo timeRemaining do backend
   */
  private calculateTimeRemaining(): number {
    if (!this.lote?.loteEndDateTime) {
      return 0;
    }
    
    const now = new Date();
    const endDate = new Date(this.lote.loteEndDateTime);
    const diffMs = endDate.getTime() - now.getTime();
    
    return Math.max(0, Math.floor(diffMs / 1000));
  }

  // ========================================
  // Carregamento de dados
  // ========================================

  carregarLote(): void {
    if (!this.loteId) return;

    this.loadingLote = true;
    this.error = '';

    this.publicCatalogoService.buscarLote(this.loteId)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.loadingLote = false)
      )
      .subscribe({
        next: (response: ApiResponse<LoteDto>) => {
          if (response.success) {
            this.lote = response.data;
            
            // CORRIGIDO: Usar cálculo próprio ao invés de confiar nos campos do backend
            // Removida a validação problemática das linhas 94-96
            // const isActive = this.calculateIsActive();
            // const isExpired = this.calculateIsExpired();
            
            // Log para debug
            console.log('Lote carregado:', {
              lote: this.lote,
              isActiveCalculado: this.calculateIsActive(),
              isExpiredCalculado: this.calculateIsExpired(),
              timeRemainingCalculado: this.calculateTimeRemaining()
            });

            // REMOVIDO: Validação que estava bloqueando a visualização
            // if (!this.lote.isActive || this.lote.isExpired) {
            //   this.error = 'Este lote não está disponível para visualização pública.';
            //   return;
            // }

          } else {
            this.error = response.message || 'Erro ao carregar detalhes do lote';
          }
        },
        error: (error) => {
          console.error('Erro ao carregar lote:', error);
          this.error = 'Lote não encontrado ou não está disponível.';
        }
      });
  }

  carregarProdutos(page: number = 0): void {
    if (!this.loteId) return;

    this.loadingProdutos = true;
    this.errorProdutos = '';

    this.publicCatalogoService.listarProdutosDoLote(this.loteId, page, this.pageSize)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.loadingProdutos = false)
      )
      .subscribe({
        next: (response: ApiResponse<Page<ProdutoDto>>) => {
          if (response.success) {
            const paginatedData = response.data;
            this.produtos = paginatedData.content;
            this.currentPage = paginatedData.number;
            this.totalPages = paginatedData.totalPages;
            this.totalElements = paginatedData.totalElements;

            console.log(`Produtos carregados: ${this.produtos.length} de ${this.totalElements} total`);
          } else {
            this.errorProdutos = response.message || 'Erro ao carregar produtos do lote';
          }
        },
        error: (error) => {
          console.error('Erro ao carregar produtos:', error);
          this.errorProdutos = 'Erro ao carregar produtos do lote.';
        }
      });
  }

  // ========================================
  // Paginação
  // ========================================

  proximaPagina(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.carregarProdutos(this.currentPage + 1);
    }
  }

  paginaAnterior(): void {
    if (this.currentPage > 0) {
      this.carregarProdutos(this.currentPage - 1);
    }
  }

  irParaPagina(page: number): void {
    if (page >= 0 && page < this.totalPages && page !== this.currentPage) {
      this.carregarProdutos(page);
    }
  }

  // CORRIGIDO: Método para alterar tamanho da página com type safety
  alterarTamanhoPagina(event: Event): void {
    const target = event.target as HTMLSelectElement;
    if (target && target.value) {
      const novoTamanho = parseInt(target.value, 10);
      if (this.pageSizeOptions.includes(novoTamanho) && novoTamanho !== this.pageSize) {
        this.pageSize = novoTamanho;
        this.carregarProdutos(0); // Voltar para primeira página
      }
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

  // ========================================
  // Formatação e utilitários
  // ========================================

  formatarTempoRestante(segundos: number): string {
    return this.publicCatalogoService.formatarTempoRestante(segundos);
  }

  formatarPreco(preco: number): string {
    return this.publicCatalogoService.formatarPreco(preco);
  }

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

  // CORRIGIDO: Método que converte null para undefined para compatibilidade com ProdutoImageComponent
  obterPrimeiraImagem(images: string[]): string | undefined {
    const imagem = this.publicCatalogoService.obterPrimeiraImagem(images);
    return imagem ?? undefined; // Converte null para undefined
  }

  getStatusClass(status: string): string {
    const statusMap: { [key: string]: string } = {
      'ACTIVE': 'status-active',
      'DRAFT': 'status-draft',
      'EXPIRED': 'status-expired',
      'SOLD': 'status-sold',
      'CANCELLED': 'status-cancelled'
    };
    return statusMap[status] || 'status-default';
  }

  getStatusText(status: string): string {
    const statusMap: { [key: string]: string } = {
      'ACTIVE': 'Ativo',
      'DRAFT': 'Rascunho',
      'EXPIRED': 'Expirado',
      'SOLD': 'Vendido',
      'CANCELLED': 'Cancelado'
    };
    return statusMap[status] || status;
  }

  // ========================================
  // Navegação
  // ========================================

  voltarParaCatalogo(): void {
    this.router.navigate(['/catalogo']);
  }

  // ========================================
  // TrackBy functions para otimização
  // ========================================

  trackByProdutoId(index: number, produto: ProdutoDto): string {
    return produto.id;
  }

  trackByPageNumber(index: number, page: number): number {
    return page;
  }

  // ========================================
  // Getters para template (CORRIGIDOS para usar cálculo próprio)
  // ========================================

  get temProdutos(): boolean {
    return this.produtos && this.produtos.length > 0;
  }

  get loteAtivo(): boolean {
    if (!this.lote) return false;
    
    // CORRIGIDO: Usar cálculo próprio ao invés de confiar nos campos do backend
    return this.calculateIsActive() && !this.calculateIsExpired();
  }

  get temPaginacao(): boolean {
    return this.totalPages > 1;
  }

  get paginaInfo(): string {
    if (this.totalElements === 0) {
      return 'Nenhum produto encontrado';
    }
    
    const inicio = (this.currentPage * this.pageSize) + 1;
    const fim = Math.min((this.currentPage + 1) * this.pageSize, this.totalElements);
    
    return `Exibindo ${inicio} a ${fim} de ${this.totalElements} produtos`;
  }

  // ========================================
  // Métodos auxiliares para o template (usando cálculo próprio)
  // ========================================

  /**
   * Retorna o tempo restante calculado localmente
   */
  getTempoRestanteCalculado(): number {
    return this.calculateTimeRemaining();
  }

  /**
   * Verifica se o lote está próximo do encerramento (24 horas)
   */
  isProximoEncerramento(): boolean {
    const isActive = this.calculateIsActive();
    const isExpired = this.calculateIsExpired();
    const timeRemaining = this.calculateTimeRemaining();
    
    return isActive && !isExpired && timeRemaining <= 86400; // 24 horas
  }
}