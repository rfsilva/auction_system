import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { LoteCatalogo } from '../../core/models/lote-catalogo.model';
import { LoteCatalogoService } from '../../core/services/lote-catalogo.service';

/**
 * Componente para exibir card de lote no catálogo
 * História 02: Transformação do Catálogo em Catálogo de Lotes
 */
@Component({
  selector: 'app-lote-card',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="lote-card" [class]="getCardClass()">
      <!-- Imagem do lote -->
      <div class="lote-image">
        <img 
          [src]="getImagemLote()" 
          [alt]="lote.title"
          (error)="onImageError($event)"
          loading="lazy">
        
        <!-- Badge de status -->
        <div class="status-badge" [class]="getStatusClass()">
          {{ getStatusText() }}
        </div>
        
        <!-- Badge de produtos -->
        <div class="produtos-badge">
          {{ lote.quantidadeProdutosValidos }} 
          {{ lote.quantidadeProdutosValidos === 1 ? 'produto' : 'produtos' }}
        </div>
      </div>

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
          <i class="icon-user"></i>
          <span>{{ getVendedorDisplay() }}</span>
        </div>

        <!-- Categoria -->
        <div class="categoria-info" *ngIf="lote.categoria">
          <i class="icon-tag"></i>
          <span>{{ lote.categoria }}</span>
        </div>

        <!-- Tempo restante -->
        <div class="tempo-restante" [class]="getTempoClass()">
          <i class="icon-clock"></i>
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
          class="btn-favorite"
          [class.favorited]="isFavorito"
          (click)="onFavoriteClick($event)"
          [title]="isFavorito ? 'Remover dos favoritos' : 'Adicionar aos favoritos'">
          <i [class]="getFavoriteIcon()"></i>
        </button>

        <!-- Botão de ver detalhes -->
        <a 
          [routerLink]="['/lotes', lote.id]"
          class="btn-details">
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

  getImagemLote(): string {
    return this.loteCatalogoService.getImagemOuPlaceholder(this.lote);
  }

  onImageError(event: any): void {
    event.target.src = '/assets/images/lote-placeholder.jpg';
  }

  getCardClass(): string {
    const classes = ['card'];
    
    if (this.loteCatalogoService.isProximoEncerramento(this.lote)) {
      classes.push('ending-soon');
    }
    
    if (!this.lote.isActive) {
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
    if (!this.lote.isActive) {
      return 'tempo-encerrado';
    }
    
    if (this.lote.timeRemaining <= 3600) { // 1 hora
      return 'tempo-critico';
    }
    
    if (this.lote.timeRemaining <= 86400) { // 1 dia
      return 'tempo-urgente';
    }
    
    return 'tempo-normal';
  }

  getDataEncerramento(): string {
    return this.loteCatalogoService.formatarData(this.lote.loteEndDateTime);
  }

  getFavoriteIcon(): string {
    return this.isFavorito ? 'icon-heart-filled' : 'icon-heart';
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