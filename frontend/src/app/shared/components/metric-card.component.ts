import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MetricaCard } from '../../core/models/dashboard-admin.model';

/**
 * Componente para exibir cards de métricas no dashboard
 * História 4: Dashboard Administrativo de Contratos - Sprint S2.2
 */
@Component({
  selector: 'app-metric-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './metric-card.component.html',
  styleUrls: ['./metric-card.component.scss']
})
export class MetricCardComponent {
  @Input() metrica!: MetricaCard;

  getCardClass(): string {
    return `metric-${this.metrica.cor}`;
  }

  formatarValor(): string {
    const valor = this.metrica.valor;
    
    if (typeof valor === 'string') {
      return valor;
    }
    
    switch (this.metrica.formato) {
      case 'moeda':
        return new Intl.NumberFormat('pt-BR', {
          style: 'currency',
          currency: 'BRL'
        }).format(valor);
      
      case 'percentual':
        return new Intl.NumberFormat('pt-BR', {
          style: 'percent',
          minimumFractionDigits: 2,
          maximumFractionDigits: 2
        }).format(valor / 100);
      
      case 'numero':
      default:
        return new Intl.NumberFormat('pt-BR').format(valor);
    }
  }
}