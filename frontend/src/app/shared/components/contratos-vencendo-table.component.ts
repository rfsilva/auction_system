import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ContratoVencendo, UrgenciaEnum } from '../../core/models/dashboard-admin.model';

/**
 * Componente para tabela de contratos vencendo
 * História 4: Dashboard Administrativo de Contratos - Sprint S2.2
 */
@Component({
  selector: 'app-contratos-vencendo-table',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './contratos-vencendo-table.component.html',
  styleUrls: ['./contratos-vencendo-table.component.scss']
})
export class ContratosVencendoTableComponent {
  @Input() contratos: ContratoVencendo[] | null = null;
  
  @Output() renovarContrato = new EventEmitter<ContratoVencendo>();
  @Output() notificarContrato = new EventEmitter<ContratoVencendo>();

  // Enum para template
  UrgenciaEnum = UrgenciaEnum;

  /**
   * TrackBy function para otimizar *ngFor
   */
  trackByContratoId(index: number, contrato: ContratoVencendo): string {
    return contrato.id;
  }

  /**
   * Formata data para exibição
   */
  formatarData(dataString: string): string {
    return new Date(dataString).toLocaleDateString('pt-BR');
  }

  /**
   * Calcula tempo até vencimento
   */
  calcularTempoVencimento(diasRestantes: number): string {
    if (diasRestantes < 0) return 'Vencido';
    if (diasRestantes === 0) return 'Hoje';
    if (diasRestantes === 1) return '1 dia';
    return `${diasRestantes} dias`;
  }

  /**
   * Obtém classe CSS para urgência
   */
  getUrgenciaClass(urgencia: UrgenciaEnum): string {
    switch (urgencia) {
      case UrgenciaEnum.ALTA:
        return 'bg-urgencia-alta';
      case UrgenciaEnum.MEDIA:
        return 'bg-urgencia-media';
      case UrgenciaEnum.BAIXA:
        return 'bg-urgencia-baixa';
      default:
        return 'bg-secondary';
    }
  }

  /**
   * Obtém texto da urgência
   */
  getUrgenciaText(urgencia: UrgenciaEnum): string {
    switch (urgencia) {
      case UrgenciaEnum.ALTA:
        return 'Alta';
      case UrgenciaEnum.MEDIA:
        return 'Média';
      case UrgenciaEnum.BAIXA:
        return 'Baixa';
      default:
        return 'Indefinida';
    }
  }

  /**
   * Obtém classe CSS para tempo restante
   */
  getTempoRestanteClass(diasRestantes: number): string {
    if (diasRestantes <= 7) return 'bg-tempo-critico';
    if (diasRestantes <= 15) return 'bg-tempo-atencao';
    return 'bg-tempo-normal';
  }

  /**
   * Obtém classe CSS para status
   */
  getStatusClass(status: string): string {
    switch (status) {
      case 'ACTIVE':
        return 'bg-success';
      case 'DRAFT':
        return 'bg-warning text-dark';
      case 'EXPIRED':
        return 'bg-danger';
      case 'CANCELLED':
        return 'bg-secondary';
      case 'SUSPENDED':
        return 'bg-info';
      default:
        return 'bg-secondary';
    }
  }

  /**
   * Obtém texto do status
   */
  getStatusText(status: string): string {
    switch (status) {
      case 'ACTIVE':
        return 'Ativo';
      case 'DRAFT':
        return 'Rascunho';
      case 'EXPIRED':
        return 'Expirado';
      case 'CANCELLED':
        return 'Cancelado';
      case 'SUSPENDED':
        return 'Suspenso';
      default:
        return status;
    }
  }

  /**
   * Evento para renovar contrato
   */
  onRenovarContrato(contrato: ContratoVencendo): void {
    this.renovarContrato.emit(contrato);
  }

  /**
   * Evento para notificar contrato
   */
  onNotificarContrato(contrato: ContratoVencendo): void {
    this.notificarContrato.emit(contrato);
  }
}