import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { Subject, takeUntil, finalize } from 'rxjs';
import { PublicCatalogoService, LoteDto, ProdutoDto, ApiResponse } from '../../core/services/public-catalogo.service';

/**
 * HISTÓRIA 04: Página de Detalhes do Produto Válido (público)
 * 
 * Componente que exibe:
 * - Detalhes completos do produto válido de um lote
 * - Carrossel de imagens do produto
 * - Informações do produto (lance atual, tempo restante, descrição)
 * - Validação de que o produto pertence ao lote e está ativo
 * - Interface responsiva
 * 
 * CORRIGIDO: Adicionado tratamento seguro para erro de imagem (evita loop infinito)
 * CORRIGIDO: Filtro de URLs de exemplo para evitar warnings de sanitização do Angular
 * 
 * Preparação para o core do sistema: lances e arremates
 */
@Component({
  selector: 'app-produto-detalhe',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './produto-detalhe.component.html',
  styleUrls: ['./produto-detalhe.component.scss']
})
export class ProdutoDetalheComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  // Dados principais
  produto: ProdutoDto | null = null;
  lote: LoteDto | null = null;
  loteId: string | null = null;
  produtoId: string | null = null;

  // Estados de carregamento
  loadingProduto = false;
  loadingLote = false;
  error = '';

  // Carrossel de imagens
  imagemAtualIndex = 0;
  imagensValidas: string[] = [];

  // NOVO: Controle de imagens com erro para evitar loop infinito
  private imageErrorMap = new Set<string>();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private publicCatalogoService: PublicCatalogoService
  ) {}

  ngOnInit(): void {
    this.loteId = this.route.snapshot.paramMap.get('loteId');
    this.produtoId = this.route.snapshot.paramMap.get('produtoId');
    
    if (!this.loteId || !this.produtoId) {
      this.error = 'IDs do lote e produto são obrigatórios';
      return;
    }

    this.carregarProduto();
    this.carregarLote();
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
  onImageError(event: Event, imageIndex?: number): void {
    const img = event.target as HTMLImageElement;
    const imageKey = `${this.produtoId}_${img.src}_${imageIndex || 'main'}`;
    
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
   * Retorna uma imagem placeholder como data URL (não precisa de arquivo)
   */
  private getPlaceholderImage(): string {
    // SVG simples como data URL - não precisa de arquivo externo
    const svg = `
      <svg width="400" height="300" xmlns="http://www.w3.org/2000/svg">
        <rect width="400" height="300" fill="#f8f9fa" stroke="#dee2e6" stroke-width="2"/>
        <text x="200" y="140" text-anchor="middle" dy=".3em" font-family="Arial, sans-serif" font-size="18" fill="#6c757d">
          Imagem não disponível
        </text>
        <text x="200" y="170" text-anchor="middle" dy=".3em" font-family="Arial, sans-serif" font-size="14" fill="#adb5bd">
          Produto: ${this.produto?.title || 'Sem título'}
        </text>
      </svg>
    `;
    return `data:image/svg+xml;base64,${btoa(svg)}`;
  }

  /**
   * Obtém a URL da imagem de forma segura
   */
  getImageUrl(imageUrl: string, imageIndex?: number): string {
    // Primeiro, verificar se é URL de exemplo ou inválida
    if (!this.isValidAndSafeUrl(imageUrl)) {
      return this.getPlaceholderImage();
    }

    const imageKey = `${this.produtoId}_${imageUrl}_${imageIndex || 'main'}`;
    
    // Se já deu erro antes, usa placeholder
    if (this.imageErrorMap.has(imageKey)) {
      return this.getPlaceholderImage();
    }
    
    return imageUrl;
  }

  // ========================================
  // Carregamento de dados
  // ========================================

  carregarProduto(): void {
    if (!this.loteId || !this.produtoId) return;

    this.loadingProduto = true;
    this.error = '';

    this.publicCatalogoService.buscarProdutoDoLote(this.loteId, this.produtoId)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.loadingProduto = false)
      )
      .subscribe({
        next: (response: ApiResponse<ProdutoDto>) => {
          if (response.success) {
            this.produto = response.data;
            this.processarImagens();
            
            console.log('Produto carregado:', this.produto);
          } else {
            this.error = response.message || 'Erro ao carregar detalhes do produto';
          }
        },
        error: (error) => {
          console.error('Erro ao carregar produto:', error);
          this.error = 'Produto não encontrado ou não está disponível neste lote.';
        }
      });
  }

  carregarLote(): void {
    if (!this.loteId) return;

    this.loadingLote = true;

    this.publicCatalogoService.buscarLote(this.loteId)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.loadingLote = false)
      )
      .subscribe({
        next: (response: ApiResponse<LoteDto>) => {
          if (response.success) {
            this.lote = response.data;
            console.log('Lote carregado:', this.lote);
          } else {
            console.warn('Erro ao carregar lote:', response.message);
          }
        },
        error: (error) => {
          console.error('Erro ao carregar lote:', error);
        }
      });
  }

  // ========================================
  // Processamento de imagens
  // ========================================

  private processarImagens(): void {
    if (!this.produto?.images) {
      this.imagensValidas = [];
      return;
    }

    // CORRIGIDO: Filtrar URLs válidas E seguras (sem URLs de exemplo)
    this.imagensValidas = this.produto.images.filter(img => 
      img && img.trim() !== '' && this.isValidAndSafeUrl(img)
    );

    // Log para debug
    const totalImagens = this.produto.images.length;
    const imagensFiltradasCount = totalImagens - this.imagensValidas.length;
    
    if (imagensFiltradasCount > 0) {
      console.log(`Filtradas ${imagensFiltradasCount} imagens de exemplo de ${totalImagens} total`);
    }

    // Resetar índice se necessário
    if (this.imagemAtualIndex >= this.imagensValidas.length) {
      this.imagemAtualIndex = 0;
    }
  }

  // ========================================
  // Carrossel de imagens
  // ========================================

  proximaImagem(): void {
    if (this.imagensValidas.length > 1) {
      this.imagemAtualIndex = (this.imagemAtualIndex + 1) % this.imagensValidas.length;
    }
  }

  imagemAnterior(): void {
    if (this.imagensValidas.length > 1) {
      this.imagemAtualIndex = this.imagemAtualIndex === 0 
        ? this.imagensValidas.length - 1 
        : this.imagemAtualIndex - 1;
    }
  }

  selecionarImagem(index: number): void {
    if (index >= 0 && index < this.imagensValidas.length) {
      this.imagemAtualIndex = index;
    }
  }

  // ========================================
  // Cálculos próprios (não confiar no backend)
  // ========================================

  private calculateIsActive(): boolean {
    if (!this.produto?.endDateTime) {
      return false;
    }
    
    const now = new Date();
    const endDate = new Date(this.produto.endDateTime);
    
    return this.produto.status === 'ACTIVE' && endDate > now;
  }

  private calculateIsExpired(): boolean {
    if (!this.produto?.endDateTime) {
      return false;
    }
    
    const now = new Date();
    const endDate = new Date(this.produto.endDateTime);
    
    return endDate <= now;
  }

  private calculateTimeRemaining(): number {
    if (!this.produto?.endDateTime) {
      return 0;
    }
    
    const now = new Date();
    const endDate = new Date(this.produto.endDateTime);
    const diffMs = endDate.getTime() - now.getTime();
    
    return Math.max(0, Math.floor(diffMs / 1000));
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

  formatarPeso(peso: number | undefined): string {
    if (!peso) return '';
    
    if (peso < 1) {
      return `${(peso * 1000).toFixed(0)}g`;
    } else {
      return `${peso.toFixed(2)}kg`;
    }
  }

  formatarDimensoes(dimensions: string | undefined): string {
    if (!dimensions || dimensions.trim() === '') {
      return '';
    }

    try {
      // Tentar fazer parse do JSON
      const dimensionsObj = JSON.parse(dimensions);
      
      if (dimensionsObj && typeof dimensionsObj === 'object') {
        const parts: string[] = [];
        
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
      console.debug('Dimensões não são JSON válido, tentando outros formatos:', dimensions);
    }

    // Tentar formatos alternativos
    const dimensionPattern = /(\d+(?:\.\d+)?)\s*[x×]\s*(\d+(?:\.\d+)?)\s*[x×]\s*(\d+(?:\.\d+)?)/i;
    const match = dimensions.match(dimensionPattern);
    
    if (match) {
      const [, length, width, height] = match;
      return `C: ${this.formatarMedida(length)} × L: ${this.formatarMedida(width)} × A: ${this.formatarMedida(height)}`;
    }

    const dimension2DPattern = /(\d+(?:\.\d+)?)\s*[x×]\s*(\d+(?:\.\d+)?)/i;
    const match2D = dimensions.match(dimension2DPattern);
    
    if (match2D) {
      const [, length, width] = match2D;
      return `C: ${this.formatarMedida(length)} × L: ${this.formatarMedida(width)}`;
    }

    return dimensions.replace(/[{}"\[\]]/g, '').trim();
  }

  private formatarMedida(medida: string | number): string {
    const valor = typeof medida === 'string' ? parseFloat(medida) : medida;
    
    if (isNaN(valor)) {
      return medida.toString();
    }

    if (valor < 1 && valor > 0) {
      return `${(valor * 100).toFixed(1)}cm`;
    }
    
    if (valor >= 1 && valor <= 500) {
      return `${valor}cm`;
    }
    
    if (valor > 500) {
      return `${(valor / 10).toFixed(1)}cm`;
    }
    
    return `${valor}cm`;
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

  voltarParaLote(): void {
    if (this.loteId) {
      this.router.navigate(['/catalogo/lotes', this.loteId]);
    } else {
      this.router.navigate(['/catalogo']);
    }
  }

  voltarParaCatalogo(): void {
    this.router.navigate(['/catalogo']);
  }

  // ========================================
  // Getters para template
  // ========================================

  get produtoAtivo(): boolean {
    if (!this.produto) return false;
    return this.calculateIsActive() && !this.calculateIsExpired();
  }

  get temImagens(): boolean {
    return this.imagensValidas && this.imagensValidas.length > 0;
  }

  get imagemAtual(): string | undefined {
    if (!this.temImagens) return undefined;
    return this.imagensValidas[this.imagemAtualIndex];
  }

  get imagemAtualSegura(): string {
    const imagemAtual = this.imagemAtual;
    if (!imagemAtual) {
      return this.getPlaceholderImage();
    }
    return this.getImageUrl(imagemAtual, this.imagemAtualIndex);
  }

  get temMultiplasImagens(): boolean {
    return this.imagensValidas && this.imagensValidas.length > 1;
  }

  get tempoRestanteCalculado(): number {
    return this.calculateTimeRemaining();
  }

  get isProximoEncerramento(): boolean {
    const isActive = this.calculateIsActive();
    const isExpired = this.calculateIsExpired();
    const timeRemaining = this.calculateTimeRemaining();
    
    return isActive && !isExpired && timeRemaining <= 86400; // 24 horas
  }

  get podeReceberLances(): boolean {
    return this.produtoAtivo && !this.isProximoEncerramento;
  }

  get temTags(): boolean {
    return !!(this.produto?.tags && this.produto.tags.length > 0);
  }

  get temInformacoesAdicionais(): boolean {
    return !!(this.produto?.weight || this.produto?.dimensions || this.produto?.categoria);
  }
}