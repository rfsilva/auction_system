import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { LoteCatalogo } from '../../core/models/lote-catalogo.model';
import { LoteCatalogoService } from '../../core/services/lote-catalogo.service';
import { LoteImageComponent } from './lote-image.component';

/**
 * Componente para exibir card de lote no catálogo
 * História 02: Transformação do Catálogo em Catálogo de Lotes
 * 
 * CORRIGIDO: Usar campo 'active' ao invés de 'isActive' com tratamento de tipos
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

        <!-- Informações do vendedor -->
        <div class="seller-info" *ngIf="lote.sellerName || lote.sellerCompanyName">
          <i class="fas fa-user me-1"></i>
          <span>{{ getVendedorDisplay() }}</span>
        </div>

        <!-- Categoria -->
        <div class="categoria-info" *ngIf="lote.categoria">
          <i class="fas fa-tag me-1"></i>
          <span>{{ lote.categoria }}</span>
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
          [routerLink]="['/lotes', lote.id]"
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
  @Input() lote!: LoteCatalogo;
  @Input() showFavoriteButton = false;
  @Input() isFavorito = false;
  
  @Output() favoritar = new EventEmitter<string>();
  @Output() desfavoritar = new EventEmitter<string>();

  constructor(private loteCatalogoService: LoteCatalogoService) {}

  /**
   * Helper para obter o valor booleano de isActive com fallback
   */
  private getIsActive(): boolean {
    // Usar 'active' (campo real do backend) ou 'isActive' (fallback), padrão false
    return this.lote.active !== undefined ? this.lote.active : (this.lote.isActive || false);
  }

  getImagemLote(): string {
    return this.loteCatalogoService.getImagemOuPlaceholder(this.lote);
  }

  getCardClass(): string {
    const classes = ['card', 'h-100'];
    
    if (this.loteCatalogoService.isProximoEncerramento(this.lote)) {
      classes.push('ending-soon');
    }
    
    // CORRIGIDO: Usar helper para evitar erros de tipo
    const isActive = this.getIsActive();
    if (!isActive) {
      classes.push('inactive');
    }
    
    return classes.join(' ');
  }

  getStatusClass(): string {
    return this.loteCatalogoService.getStatusClass(this.lote);
  }

  getStatusText(): string {
    return this.loteCatalogoService.getStatusText(this.lote);
  }

  // Método para obter contagem de produtos com fallback
  getProdutoCount(): number {
    return this.lote.totalProdutos || this.lote.quantidadeProdutosValidos || 0;
  }

  getDescricaoResumida(): string {
    if (!this.lote.description) return '';
    
    const maxLength = 100;
    if (this.lote.description.length <= maxLength) {
      return this.lote.description;
    }
    
    return this.lote.description.substring(0, maxLength) + '...';
  }

  getVendedorDisplay(): string {
    if (this.lote.sellerCompanyName) {
      return this.lote.sellerCompanyName;
    }
    
    if (this.lote.sellerName) {
      return this.lote.sellerName;
    }
    
    return 'Vendedor não informado';
  }

  getTempoRestante(): string {
    return this.loteCatalogoService.formatarTempoRestante(this.lote.timeRemaining);
  }

  getTempoClass(): string {
    // CORRIGIDO: Usar helper para evitar erros de tipo
    const isActive = this.getIsActive();
    
    if (!isActive) {
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
    return this.loteCatalogoService.formatarData(this.lote.loteEndDateTime);
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
}