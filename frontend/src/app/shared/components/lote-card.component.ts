import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { LoteDto } from '../../core/services/public-catalogo.service';
import { LoteImageComponent } from './lote-image.component';

/**
 * Componente para exibir card de lote no catálogo
 * História 02: Transformação do Catálogo em Catálogo de Lotes
 * 
 * CORRIGIDO: Usar LoteDto ao invés de LoteCatalogo para compatibilidade
 */
@Component({
  selector: 'app-lote-card',
  standalone: true,
  imports: [CommonModule, RouterModule, LoteImageComponent],
  template: `
    <div class="lote-card" [class]="getCardClass()">
      <!-- Imagem do lote com componente dedicado -->
      <app-lote-image
        [src]="getImagemLote()"
        [alt]="lote.title"
        [height]="200"
        [showBadges]="true"
        [statusClass]="getStatusClass()"
        [statusText]="getStatusText()"
        [produtoCount]="getProdutoCount()">
      </app-lote-image>

      <!-- Conteúdo do card -->
      <div class="lote-content">
        <!-- Título -->
        <h3 class="lote-title" [title]="lote.title">
          {{ lote.title }}
        </h3>

        <!-- Descrição -->
        <p class="lote-description" [title]="lote.description">
          {{ getDescricaoResumida() }}
        </p>

        <!-- Informações do lote -->
        <div class="lote-info">
          <div class="info-item">
            <i class="fas fa-box me-1"></i>
            <span>{{ getProdutoCount() }} {{ getProdutoCount() === 1 ? 'produto' : 'produtos' }}</span>
          </div>
          
          <div class="info-item" *ngIf="lote.status">
            <i class="fas fa-info-circle me-1"></i>
            <span class="status-badge" [class]="getStatusClass()">{{ getStatusText() }}</span>
          </div>
        </div>

        <!-- Tempo restante -->
        <div class="tempo-restante" [class]="getTempoClass()">
          <i class="fas fa-clock me-1"></i>
          <span>{{ getTempoRestante() }}</span>
        </div>

        <!-- Data de encerramento -->
        <div class="data-encerramento">
          <small>Encerra em: {{ getDataEncerramento() }}</small>
        </div>
      </div>

      <!-- Ações do card -->
      <div class="lote-actions">
        <!-- Botão de favoritar (se habilitado) -->
        <button 
          *ngIf="showFavoriteButton"
          type="button"
          class="btn btn-sm btn-outline-danger"
          [class.btn-danger]="isFavorito"
          (click)="onFavoriteClick($event)"
          [title]="isFavorito ? 'Remover dos favoritos' : 'Adicionar aos favoritos'">
          <i [class]="getFavoriteIcon()"></i>
        </button>

        <!-- Botão de ver detalhes -->
        <a 
          [routerLink]="['/catalogo/lotes', lote.id]"
          class="btn btn-primary btn-sm">
          <i class="fas fa-eye me-1"></i>
          Ver Detalhes
        </a>
      </div>
    </div>
  `,
  styleUrls: ['./lote-card.component.scss']
})
export class LoteCardComponent {
  @Input() lote!: LoteDto; // CORRIGIDO: Usar LoteDto
  @Input() showFavoriteButton = false;
  @Input() isFavorito = false;
  
  @Output() favoritar = new EventEmitter<string>();
  @Output() desfavoritar = new EventEmitter<string>();

  /**
   * ✅ CORRIGIDO: Retorna null para usar placeholder CSS em vez de imagem física
   */
  getImagemLote(): string | null {
    // Se o lote tem uma imagem válida, usar ela
    if (this.lote.imagemDestaque && this.isValidImageUrl(this.lote.imagemDestaque)) {
      return this.lote.imagemDestaque;
    }
    
    // Retornar null para que o componente LoteImageComponent use o placeholder CSS
    return null;
  }

  /**
   * Valida se a URL da imagem é válida
   */
  private isValidImageUrl(url: string): boolean {
    if (!url || url.trim() === '') {
      return false;
    }
    
    // Verificar se não é uma URL de exemplo
    if (this.isExampleDomain(url)) {
      return false;
    }
    
    // Verificar se é uma URL válida
    try {
      new URL(url);
      return true;
    } catch {
      // Se não for URL absoluta, assumir que é relativa e válida se não estiver vazia
      return url.startsWith('/') || url.startsWith('./') || !url.includes('://');
    }
  }

  /**
   * Verifica se é um domínio de exemplo
   */
  private isExampleDomain(url: string): boolean {
    return url.includes('example.com') || 
           url.includes('placeholder.com') || 
           url.includes('via.placeholder.com') ||
           url.includes('picsum.photos') ||
           url.startsWith('http://example') ||
           url.startsWith('https://example');
  }

  getCardClass(): string {
    const classes = ['card', 'h-100'];
    
    if (this.isProximoEncerramento()) {
      classes.push('ending-soon');
    }
    
    if (!this.lote.isActive) {
      classes.push('inactive');
    }
    
    return classes.join(' ');
  }

  getStatusClass(): string {
    if (!this.lote.isActive) {
      return 'status-inactive';
    }
    
    if (this.lote.isExpired) {
      return 'status-expired';
    }
    
    if (this.isProximoEncerramento()) {
      return 'status-ending-soon';
    }
    
    return 'status-active';
  }

  getStatusText(): string {
    if (!this.lote.isActive) {
      return 'Inativo';
    }
    
    if (this.lote.isExpired) {
      return 'Encerrado';
    }
    
    if (this.isProximoEncerramento()) {
      return 'Encerrando';
    }
    
    return 'Ativo';
  }

  getProdutoCount(): number {
    return this.lote.totalProdutos || this.lote.produtoIds?.length || 0;
  }

  getDescricaoResumida(): string {
    if (!this.lote.description) return '';
    
    const maxLength = 100;
    if (this.lote.description.length <= maxLength) {
      return this.lote.description;
    }
    
    return this.lote.description.substring(0, maxLength) + '...';
  }

  getTempoRestante(): string {
    if (!this.lote.isActive || this.lote.isExpired) {
      return 'Encerrado';
    }
    
    return this.formatarTempoRestante(this.lote.timeRemaining);
  }

  getTempoClass(): string {
    if (!this.lote.isActive || this.lote.isExpired) {
      return 'tempo-encerrado text-muted';
    }
    
    if (this.lote.timeRemaining <= 3600) { // 1 hora
      return 'tempo-critico text-danger fw-bold';
    }
    
    if (this.lote.timeRemaining <= 86400) { // 1 dia
      return 'tempo-urgente text-warning fw-bold';
    }
    
    return 'tempo-normal text-success';
  }

  getDataEncerramento(): string {
    return this.formatarData(this.lote.loteEndDateTime);
  }

  getFavoriteIcon(): string {
    return this.isFavorito ? 'fas fa-heart' : 'far fa-heart';
  }

  onFavoriteClick(event: Event): void {
    event.preventDefault();
    event.stopPropagation();
    
    if (this.isFavorito) {
      this.desfavoritar.emit(this.lote.id);
    } else {
      this.favoritar.emit(this.lote.id);
    }
  }

  private isProximoEncerramento(): boolean {
    return this.lote.isActive && !this.lote.isExpired && this.lote.timeRemaining <= 86400; // 24 horas
  }

  private formatarTempoRestante(segundos: number): string {
    if (segundos <= 0) {
      return 'Encerrado';
    }

    const dias = Math.floor(segundos / 86400);
    const horas = Math.floor((segundos % 86400) / 3600);
    const minutos = Math.floor((segundos % 3600) / 60);

    if (dias > 0) {
      return `${dias}d ${horas}h`;
    } else if (horas > 0) {
      return `${horas}h ${minutos}m`;
    } else {
      return `${minutos}m`;
    }
  }

  private formatarData(dataString: string): string {
    try {
      const data = new Date(dataString);
      return data.toLocaleDateString('pt-BR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    } catch (error) {
      return 'Data inválida';
    }
  }
}