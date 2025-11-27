import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ProdutoService } from '../../core/services/produto.service';
import { AuthService } from '../../core/services/auth.service';
import { Produto, PaginatedResponse } from '../../core/models/produto.model';

@Component({
  selector: 'app-produto-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './produto-list.component.html',
  styleUrls: ['./produto-list.component.scss']
})
export class ProdutoListComponent implements OnInit {
  produtos: Produto[] = [];
  loading = false;
  error = '';
  
  // Paginação
  currentPage = 0;
  totalPages = 0;
  totalElements = 0;
  pageSize = 20;

  constructor(
    private produtoService: ProdutoService,
    public authService: AuthService
  ) {}

  ngOnInit() {
    // Verificar se usuário pode vender
    if (!this.authService.canSell()) {
      this.error = 'Você não tem permissão para acessar esta página';
      return;
    }

    this.carregarProdutos();
  }

  carregarProdutos(page: number = 0) {
    this.loading = true;
    this.error = '';

    this.produtoService.listarMeusProdutos(page, this.pageSize).subscribe({
      next: (response) => {
        if (response.success) {
          const paginatedData = response.data;
          this.produtos = paginatedData.content;
          this.currentPage = paginatedData.number;
          this.totalPages = paginatedData.totalPages;
          this.totalElements = paginatedData.totalElements;
        }
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Erro ao carregar produtos';
        this.loading = false;
        console.error('Erro ao carregar produtos:', error);
      }
    });
  }

  excluirProduto(produto: Produto) {
    if (!confirm(`Tem certeza que deseja excluir o produto "${produto.title}"?`)) {
      return;
    }

    this.produtoService.excluirProduto(produto.id).subscribe({
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
  }

  publicarProduto(produto: Produto) {
    if (!confirm(`Tem certeza que deseja publicar o produto "${produto.title}"?`)) {
      return;
    }

    this.produtoService.publicarProduto(produto.id).subscribe({
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