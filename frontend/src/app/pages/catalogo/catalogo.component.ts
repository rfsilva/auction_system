import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ProdutoService } from '../../core/services/produto.service';
import { Produto, CatalogoFiltro, PaginatedResponse } from '../../core/models/produto.model';

@Component({
  selector: 'app-catalogo',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './catalogo.component.html',
  styleUrls: ['./catalogo.component.scss']
})
export class CatalogoComponent implements OnInit {
  produtos: Produto[] = [];
  categorias: string[] = [];
  loading = false;
  error = '';
  
  // Filtros
  filtros: CatalogoFiltro = {
    categoria: '',
    precoMin: undefined,
    precoMax: undefined,
    titulo: '',
    ordenacao: 'recentes',
    page: 0,
    size: 20
  };

  // Paginação
  currentPage = 0;
  totalPages = 0;
  totalElements = 0;

  // UI
  filtrosExpandidos = false;

  constructor(private produtoService: ProdutoService) {}

  ngOnInit() {
    this.carregarCategorias();
    this.buscarProdutos();
  }

  carregarCategorias() {
    this.produtoService.listarCategorias().subscribe({
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

  buscarProdutos(page: number = 0) {
    this.loading = true;
    this.error = '';
    
    const filtrosComPagina = { ...this.filtros, page };

    this.produtoService.buscarCatalogo(filtrosComPagina).subscribe({
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

  aplicarFiltros() {
    this.filtros.page = 0;
    this.buscarProdutos(0);
  }

  limparFiltros() {
    this.filtros = {
      categoria: '',
      precoMin: undefined,
      precoMax: undefined,
      titulo: '',
      ordenacao: 'recentes',
      page: 0,
      size: 20
    };
    this.buscarProdutos(0);
  }

  alterarOrdenacao(ordenacao: string) {
    this.filtros.ordenacao = ordenacao as any;
    this.aplicarFiltros();
  }

  toggleFiltros() {
    this.filtrosExpandidos = !this.filtrosExpandidos;
  }

  proximaPagina() {
    if (this.currentPage < this.totalPages - 1) {
      this.buscarProdutos(this.currentPage + 1);
    }
  }

  paginaAnterior() {
    if (this.currentPage > 0) {
      this.buscarProdutos(this.currentPage - 1);
    }
  }

  irParaPagina(page: number) {
    if (page >= 0 && page < this.totalPages) {
      this.buscarProdutos(page);
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

  getOrdenacaoText(ordenacao: string): string {
    const ordenacaoMap: { [key: string]: string } = {
      'recentes': 'Mais Recentes',
      'preco_asc': 'Menor Preço',
      'preco_desc': 'Maior Preço',
      'terminando': 'Terminando Primeiro'
    };
    return ordenacaoMap[ordenacao] || ordenacao;
  }
}