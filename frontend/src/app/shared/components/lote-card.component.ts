import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { LoteDto } from '../../core/services/public-catalogo.service';
import { LoteImageComponent } from './lote-image.component';

/**
 * Componente para exibir card de lote no catálogo
 * História 02: Transformação do Catálogo em Catálogo de Lotes
 * 
 * CORRIGIDO: Calcular status baseado nos dados reais ao invés de confiar no backend
 * REFATORADO: Template movido para arquivo HTML externo
 */
@Component({
  selector: 'app-lote-card',
  standalone: true,
  imports: [CommonModule, RouterModule, LoteImageComponent],
  templateUrl: './lote-card.component.html',
  styleUrls: ['./lote-card.component.scss']
})
export class LoteCardComponent {
  @Input() lote!: LoteDto;
  @Input() showFavoriteButton = false;
  @Input() isFavorito = false;
  
  @Output() favoritar = new EventEmitter<string>();
  @Output() desfavoritar = new EventEmitter<string>();

  /**
   * CORRIGIDO: Calcula se o lote está ativo baseado na data de encerramento
   * Não confia no campo isActive do backend
   */
  private calculateIsActive(): boolean {
    if (!this.lote.loteEndDateTime) {
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
    if (!this.lote.loteEndDateTime) {
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
    if (!this.lote.loteEndDateTime) {
      return 0;
    }
    
    const now = new Date();
    const endDate = new Date(this.lote.loteEndDateTime);
    const diffMs = endDate.getTime() - now.getTime();
    
    return Math.max(0, Math.floor(diffMs / 1000));
  }

  getImagemLote(): string | null {
    if (this.lote.imagemDestaque && this.isValidImageUrl(this.lote.imagemDestaque)) {
      return this.lote.imagemDestaque;
    }
    return null;
  }

  private isValidImageUrl(url: string): boolean {
    if (!url || url.trim() === '') {
      return false;
    }
    
    if (this.isExampleDomain(url)) {
      return false;
    }
    
    try {
      new URL(url);
      return true;
    } catch {
      return url.startsWith('/') || url.startsWith('./') || !url.includes('://');
    }
  }

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
    
    // CORRIGIDO: Usar cálculo próprio
    const isActive = this.calculateIsActive();
    
    if (this.isProximoEncerramento()) {
      classes.push('ending-soon');
    }
    
    if (!isActive) {
      classes.push('inactive');
    }
    
    return classes.join(' ');
  }

  getStatusClass(): string {
    // CORRIGIDO: Usar cálculo próprio
    const isActive = this.calculateIsActive();
    const isExpired = this.calculateIsExpired();
    
    if (isExpired) {
      return 'status-expired';
    }
    
    if (!isActive) {
      return 'status-inactive';
    }
    
    if (this.isProximoEncerramento()) {
      return 'status-ending-soon';
    }
    
    return 'status-active';
  }

  getStatusText(): string {
    // CORRIGIDO: Usar cálculo próprio
    const isActive = this.calculateIsActive();
    const isExpired = this.calculateIsExpired();
    
    if (isExpired) {
      return 'Encerrado';
    }
    
    if (!isActive) {
      return 'Inativo';
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
    // CORRIGIDO: Usar cálculo próprio
    const isActive = this.calculateIsActive();
    const isExpired = this.calculateIsExpired();
    
    if (!isActive || isExpired) {
      return 'Encerrado';
    }
    
    const timeRemaining = this.calculateTimeRemaining();
    return this.formatarTempoRestante(timeRemaining);
  }

  getTempoClass(): string {
    // CORRIGIDO: Usar cálculo próprio
    const isActive = this.calculateIsActive();
    const isExpired = this.calculateIsExpired();
    
    if (!isActive || isExpired) {
      return 'tempo-encerrado text-muted';
    }
    
    const timeRemaining = this.calculateTimeRemaining();
    
    if (timeRemaining <= 3600) { // 1 hora
      return 'tempo-critico text-danger fw-bold';
    }
    
    if (timeRemaining <= 86400) { // 1 dia
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
    // CORRIGIDO: Usar cálculo próprio
    const isActive = this.calculateIsActive();
    const isExpired = this.calculateIsExpired();
    const timeRemaining = this.calculateTimeRemaining();
    
    return isActive && !isExpired && timeRemaining <= 86400; // 24 horas
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