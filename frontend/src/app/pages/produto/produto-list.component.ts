import { Component, OnInit, OnDestroy, computed, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ProdutoService } from '../../core/services/produto.service';
import { AuthService } from '../../core/services/auth.service';
import { Produto, PaginatedResponse } from '../../core/models/produto.model';
import { ProdutoImageComponent } from '../../shared/components/produto-image.component';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-produto-list',
  standalone: true,
  imports: [CommonModule, RouterModule, ProdutoImageComponent],
  templateUrl: './produto-list.component.html',
  styleUrls: ['./produto-list.component.scss']
})
export class ProdutoListComponent implements OnInit, OnDestroy {
  produtos: Produto[] = [];
  loading = false;
  error = '';
  
  // Paginação
  currentPage = 0;
  totalPages = 0;
  totalElements = 0;
  pageSize = 20;

  private subscriptions: Subscription[] = [];

  // Computed para verificar se pode vender (evita re-renderizações)
  canSellComputed = computed(() => {
    const user = this.authService.currentUser();
    const isAuth = this.authService.isAuthenticated();
    return isAuth && user && this.authService.canSell();
  });

  constructor(
    private produtoService: ProdutoService,
    public authService: AuthService
  ) {
    // Effect para reagir a mudanças de autenticação
    effect(() => {
      const canSell = this.canSellComputed();
      if (canSell && this.produtos.length === 0 && !this.loading) {
        // Só carrega se ainda não carregou e pode vender
        this.carregarProdutos();
      } else if (!canSell) {
        this.error = 'Você não tem permissão para acessar esta página';
        this.produtos = [];
      }
    });
  }

  ngOnInit() {
    // Verificação inicial - só carrega se já estiver autenticado
    if (this.canSellComputed()) {
      this.carregarProdutos();
    }
  }

  ngOnDestroy() {
    // Limpar subscriptions para evitar memory leaks
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  // TrackBy function para otimizar *ngFor
  trackByProdutoId(index: number, produto: Produto): string {
    return produto.id;
  }

  // Função para obter a primeira imagem válida
  getFirstImage(images: string[] | undefined): string | undefined {
    if (!images || images.length === 0) {
      return undefined;
    }
    
    // Retornar a primeira imagem que não seja vazia
    const firstValidImage = images.find(img => img && img.trim() !== '');
    return firstValidImage;
  }

  carregarProdutos(page: number = 0) {
    // Evitar múltiplas chamadas simultâneas
    if (this.loading) {
      return;
    }

    this.loading = true;
    this.error = '';

    const subscription = this.produtoService.listarMeusProdutos(page, this.pageSize).subscribe({
      next: (response) => {
        if (response.success) {
          const paginatedData = response.data;
          this.produtos = paginatedData.content;
          this.currentPage = paginatedData.number;
          this.totalPages = paginatedData.totalPages;
          this.totalElements = paginatedData.totalElements;
          
          console.log('Produtos carregados:', this.produtos.length);
          // Log das imagens para debug
          this.produtos.forEach(produto => {
            console.log(`Produto ${produto.title}:`, produto.images);
          });
        }
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Erro ao carregar produtos';
        this.loading = false;
        console.error('Erro ao carregar produtos:', error);
      }
    });

    this.subscriptions.push(subscription);
  }

  excluirProduto(produto: Produto) {
    if (!confirm(`Tem certeza que deseja excluir o produto "${produto.title}"?`)) {
      return;
    }

    const subscription = this.produtoService.excluirProduto(produto.id).subscribe({
      next: (response) => {
        if (response.success) {
          // Recarregar lista
          this.carregarProdutos(this.currentPage);
        }
      },
      error: (error) => {
        alert('Erro ao excluir produto: ' + (error.error?.message || 'Erro desconhecido'));
        console.error('Erro ao excluir produto:', error);
      }
    });

    this.subscriptions.push(subscription);
  }

  publicarProduto(produto: Produto) {
    if (!confirm(`Tem certeza que deseja publicar o produto "${produto.title}"?`)) {
      return;
    }

    const subscription = this.produtoService.publicarProduto(produto.id).subscribe({
      next: (response) => {
        if (response.success) {
          // Recarregar lista
          this.carregarProdutos(this.currentPage);
        }
      },
      error: (error) => {
        alert('Erro ao publicar produto: ' + (error.error?.message || 'Erro desconhecido'));
        console.error('Erro ao publicar produto:', error);
      }
    });

    this.subscriptions.push(subscription);
  }

  proximaPagina() {
    if (this.currentPage < this.totalPages - 1) {
      this.carregarProdutos(this.currentPage + 1);
    }
  }

  paginaAnterior() {
    if (this.currentPage > 0) {
      this.carregarProdutos(this.currentPage - 1);
    }
  }

  irParaPagina(page: number) {
    if (page >= 0 && page < this.totalPages) {
      this.carregarProdutos(page);
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

  formatarPreco(preco: number): string {
    return this.produtoService.formatarPreco(preco);
  }

  formatarTempoRestante(segundos: number): string {
    return this.produtoService.formatarTempoRestante(segundos);
  }

  getStatusText(status: string): string {
    return this.produtoService.getStatusText(status);
  }

  getStatusClass(status: string): string {
    return this.produtoService.getStatusClass(status);
  }

  canEdit(produto: Produto): boolean {
    return produto.status === 'DRAFT' || 
           (produto.status === 'ACTIVE' && produto.currentPrice === produto.initialPrice);
  }

  canDelete(produto: Produto): boolean {
    return produto.status === 'DRAFT' || 
           (produto.status === 'ACTIVE' && produto.currentPrice === produto.initialPrice);
  }

  canPublish(produto: Produto): boolean {
    return produto.status === 'DRAFT';
  }
}