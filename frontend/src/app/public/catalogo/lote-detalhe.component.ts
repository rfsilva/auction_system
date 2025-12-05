import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Subject, takeUntil, finalize } from 'rxjs';
import { PublicCatalogoService, LoteDto, ProdutoDto, ApiResponse, Page } from '../../core/services/public-catalogo.service';

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
 * ADICIONADO: Formatação de dimensões JSON para exibição legível
 * CORRIGIDO: Removida importação desnecessária do ProdutoImageComponent
 * CORRIGIDO: Adicionado tratamento seguro para erro de imagem (evita loop infinito)
 * CORRIGIDO: Adicionado tratamento para imagem do cabeçalho do lote
 * CORRIGIDO: Filtro de URLs de exemplo para evitar warnings de sanitização do Angular
 */
@Component({
  selector: 'app-lote-detalhe',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
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

  // NOVO: Controle de imagens com erro para evitar loop infinito
  private imageErrorMap = new Set<string>();

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
  // NOVO: Detecção e filtro de URLs de exemplo
  // ========================================

  /**
   * Verifica se uma URL é de exemplo (que não deve ser carregada)
   */
  private isExampleUrl(url: string): boolean {
    if (!url || url.trim() === '') {
      return false;
    }

    const exampleDomains = [
      'example.com',
      'example.org',
      'example.net',
      'placeholder.com',
      'via.placeholder.com',
      'picsum.photos',
      'lorem.picsum',
      'dummyimage.com',
      'fakeimg.pl',
      'placehold.it',
      'placehold.co',
      'placekitten.com',
      'fillmurray.com',
      'placecage.com'
    ];

    const lowerUrl = url.toLowerCase();
    
    return exampleDomains.some(domain => 
      lowerUrl.includes(domain) || 
      lowerUrl.startsWith(`http://${domain}`) || 
      lowerUrl.startsWith(`https://${domain}`)
    );
  }

  /**
   * Verifica se uma URL é válida e segura para uso
   */
  private isValidAndSafeUrl(url: string): boolean {
    if (!url || url.trim() === '') {
      return false;
    }

    // Primeiro, verificar se é URL de exemplo
    if (this.isExampleUrl(url)) {
      console.log('URL de exemplo detectada e filtrada:', url);
      return false;
    }

    // Verificar se é uma URL válida
    try {
      const urlObj = new URL(url);
      // Aceitar apenas HTTP e HTTPS
      return urlObj.protocol === 'http:' || urlObj.protocol === 'https:';
    } catch {
      // Se não for URL absoluta, verificar se é relativa válida
      return url.startsWith('/') || url.startsWith('./') || url.startsWith('../');
    }
  }

  // ========================================
  // NOVO: Tratamento seguro de erro de imagem
  // ========================================

  /**
   * Trata erro de imagem de forma segura, evitando loop infinito
   */
  onImageError(event: Event, produtoId: string): void {
    const img = event.target as HTMLImageElement;
    const imageKey = `${produtoId}_${img.src}`;
    
    // Se já tentamos carregar esta imagem e deu erro, não tenta novamente
    if (this.imageErrorMap.has(imageKey)) {
      // Remove a imagem completamente para evitar loop
      img.style.display = 'none';
      return;
    }
    
    // Marca esta imagem como tendo erro
    this.imageErrorMap.add(imageKey);
    
    // Tenta usar um placeholder inline (data URL)
    img.src = this.getPlaceholderImage();
    img.alt = 'Imagem não disponível';
  }

  /**
   * NOVO: Trata erro de imagem do lote de forma segura
   */
  onLoteImageError(event: Event): void {
    const img = event.target as HTMLImageElement;
    const imageKey = `lote_${this.loteId}_${img.src}`;
    
    // Se já tentamos carregar esta imagem e deu erro, não tenta novamente
    if (this.imageErrorMap.has(imageKey)) {
      // Remove a imagem completamente para evitar loop
      img.style.display = 'none';
      return;
    }
    
    // Marca esta imagem como tendo erro
    this.imageErrorMap.add(imageKey);
    
    // Tenta usar um placeholder inline (data URL)
    img.src = this.getLotePlaceholderImage();
    img.alt = 'Imagem do lote não disponível';
  }

  /**
   * Retorna uma imagem placeholder como data URL (não precisa de arquivo)
   */
  private getPlaceholderImage(): string {
    // SVG simples como data URL - não precisa de arquivo externo
    const svg = `
      <svg width="200" height="150" xmlns="http://www.w3.org/2000/svg">
        <rect width="200" height="150" fill="#f0f0f0" stroke="#ddd" stroke-width="1"/>
        <text x="100" y="75" text-anchor="middle" dy=".3em" font-family="Arial, sans-serif" font-size="14" fill="#999">
          Sem imagem
        </text>
      </svg>
    `;
    return `data:image/svg+xml;base64,${btoa(svg)}`;
  }

  /**
   * NOVO: Retorna uma imagem placeholder para lote como data URL
   */
  private getLotePlaceholderImage(): string {
    // SVG simples como data URL - não precisa de arquivo externo
    const svg = `
      <svg width="300" height="200" xmlns="http://www.w3.org/2000/svg">
        <rect width="300" height="200" fill="#f8f9fa" stroke="#dee2e6" stroke-width="2"/>
        <text x="150" y="90" text-anchor="middle" dy=".3em" font-family="Arial, sans-serif" font-size="16" fill="#6c757d">
          📦 Lote
        </text>
        <text x="150" y="120" text-anchor="middle" dy=".3em" font-family="Arial, sans-serif" font-size="14" fill="#adb5bd">
          ${this.lote?.title || 'Sem título'}
        </text>
      </svg>
    `;
    return `data:image/svg+xml;base64,${btoa(svg)}`;
  }

  /**
   * Obtém a URL da imagem do produto de forma segura
   */
  getImageUrl(produto: ProdutoDto): string {
    const primeiraImagem = this.obterPrimeiraImagem(produto.images);
    
    // Se não tem imagem, usa placeholder
    if (!primeiraImagem) {
      return this.getPlaceholderImage();
    }

    // Verificar se é URL de exemplo ou inválida
    if (!this.isValidAndSafeUrl(primeiraImagem)) {
      return this.getPlaceholderImage();
    }
    
    // Se já deu erro antes, usa placeholder
    if (this.imageErrorMap.has(`${produto.id}_${primeiraImagem}`)) {
      return this.getPlaceholderImage();
    }
    
    return primeiraImagem;
  }

  /**
   * NOVO: Obtém a URL da imagem do lote de forma segura
   */
  getLoteImageUrl(imageUrl: string): string {
    // Verificar se é URL de exemplo ou inválida
    if (!this.isValidAndSafeUrl(imageUrl)) {
      return this.getLotePlaceholderImage();
    }

    const imageKey = `lote_${this.loteId}_${imageUrl}`;
    
    // Se já deu erro antes, usa placeholder
    if (this.imageErrorMap.has(imageKey)) {
      return this.getLotePlaceholderImage();
    }
    
    return imageUrl;
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
  // NOVO: Formatação de dimensões
  // ========================================

  /**
   * Formata as dimensões do produto de JSON para texto legível
   * Suporta diferentes formatos de entrada
   */
  formatarDimensoes(dimensions: string | undefined): string {
    if (!dimensions || dimensions.trim() === '') {
      return '';
    }

    try {
      // Tentar fazer parse do JSON
      const dimensionsObj = JSON.parse(dimensions);
      
      // Verificar se tem as propriedades esperadas
      if (dimensionsObj && typeof dimensionsObj === 'object') {
        const parts: string[] = [];
        
        // Mapear diferentes nomes de propriedades possíveis
        const length = dimensionsObj.length || dimensionsObj.comprimento || dimensionsObj.l;
        const width = dimensionsObj.width || dimensionsObj.largura || dimensionsObj.w;
        const height = dimensionsObj.height || dimensionsObj.altura || dimensionsObj.h;
        const depth = dimensionsObj.depth || dimensionsObj.profundidade || dimensionsObj.d;
        
        if (length) parts.push(`C: ${this.formatarMedida(length)}`);
        if (width) parts.push(`L: ${this.formatarMedida(width)}`);
        if (height) parts.push(`A: ${this.formatarMedida(height)}`);
        if (depth && !height) parts.push(`P: ${this.formatarMedida(depth)}`);
        
        if (parts.length > 0) {
          return parts.join(' × ');
        }
      }
    } catch (error) {
      // Se não for JSON válido, tentar outros formatos
      console.debug('Dimensões não são JSON válido, tentando outros formatos:', dimensions);
    }

    // Tentar formatos alternativos
    // Formato: "16.3 x 7.8 x 0.89" ou "16.3x7.8x0.89"
    const dimensionPattern = /(\d+(?:\.\d+)?)\s*[x×]\s*(\d+(?:\.\d+)?)\s*[x×]\s*(\d+(?:\.\d+)?)/i;
    const match = dimensions.match(dimensionPattern);
    
    if (match) {
      const [, length, width, height] = match;
      return `C: ${this.formatarMedida(length)} × L: ${this.formatarMedida(width)} × A: ${this.formatarMedida(height)}`;
    }

    // Formato: "16.3 x 7.8" (apenas 2 dimensões)
    const dimension2DPattern = /(\d+(?:\.\d+)?)\s*[x×]\s*(\d+(?:\.\d+)?)/i;
    const match2D = dimensions.match(dimension2DPattern);
    
    if (match2D) {
      const [, length, width] = match2D;
      return `C: ${this.formatarMedida(length)} × L: ${this.formatarMedida(width)}`;
    }

    // Se nada funcionar, retornar o valor original limpo
    return dimensions.replace(/[{}"\[\]]/g, '').trim();
  }

  /**
   * Formata uma medida individual, adicionando unidade se necessário
   */
  private formatarMedida(medida: string | number): string {
    const valor = typeof medida === 'string' ? parseFloat(medida) : medida;
    
    if (isNaN(valor)) {
      return medida.toString();
    }

    // Se o valor for muito pequeno (< 1), assumir que está em metros e converter para cm
    if (valor < 1 && valor > 0) {
      return `${(valor * 100).toFixed(1)}cm`;
    }
    
    // Se o valor for razoável (1-500), assumir que já está em cm
    if (valor >= 1 && valor <= 500) {
      return `${valor}cm`;
    }
    
    // Se o valor for muito grande (> 500), assumir que está em mm e converter para cm
    if (valor > 500) {
      return `${(valor / 10).toFixed(1)}cm`;
    }
    
    return `${valor}cm`;
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

  // CORRIGIDO: Método que obtém primeira imagem sem dependência do ProdutoImageComponent
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