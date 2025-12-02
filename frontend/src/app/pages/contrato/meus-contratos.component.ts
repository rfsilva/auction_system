import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';

import { ContratoService } from '../../core/services/contrato.service';
import { AuthService } from '../../core/services/auth.service';
import { Contrato, ContractStatus } from '../../core/models/contrato.model';

@Component({
  selector: 'app-meus-contratos',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './meus-contratos.component.html',
  styleUrls: ['./meus-contratos.component.scss']
})
export class MeusContratosComponent implements OnInit, OnDestroy {
  
  private destroy$ = new Subject<void>();

  // Dados
  contratosAtivos: Contrato[] = [];
  
  // Estados
  loading = false;
  error: string | null = null;
  
  // UI
  expandedContratos = new Set<string>();
  
  // Enums para template
  ContractStatus = ContractStatus;

  constructor(
    private contratoService: ContratoService,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    this.carregarContratosAtivos();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // Carregamento de dados

  carregarContratosAtivos(): void {
    this.loading = true;
    this.error = null;

    this.contratoService.listarMeusContratosAtivos()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.contratosAtivos = response.data;
          } else {
            this.error = response.message || 'Erro ao carregar contratos';
          }
          this.loading = false;
        },
        error: (error) => {
          console.error('Erro ao carregar contratos:', error);
          this.error = 'Erro ao carregar seus contratos ativos. Tente novamente.';
          this.loading = false;
        }
      });
  }

  // UI helpers

  toggleDetalhes(contratoId: string): void {
    if (this.expandedContratos.has(contratoId)) {
      this.expandedContratos.delete(contratoId);
    } else {
      this.expandedContratos.add(contratoId);
    }
  }

  isExpanded(contratoId: string): boolean {
    return this.expandedContratos.has(contratoId);
  }

  // Métodos de formatação

  formatarTaxa(taxa: number): string {
    return this.contratoService.formatarTaxa(taxa);
  }

  formatarData(dataString: string): string {
    return this.contratoService.formatarData(dataString);
  }

  getStatusClass(status: ContractStatus): string {
    return this.contratoService.getStatusClass(status);
  }

  getStatusText(status: ContractStatus): string {
    return this.contratoService.getStatusText(status);
  }

  isProximoVencimento(contrato: Contrato): boolean {
    return this.contratoService.isProximoVencimento(contrato, 30);
  }

  calcularDiasRestantes(validTo?: string): number | null {
    return this.contratoService.calcularDiasAteExpiracao(validTo);
  }

  // TrackBy function para otimizar *ngFor
  trackByContratoId(index: number, contrato: Contrato): string {
    return contrato.id;
  }

  // Método para recarregar dados
  recarregar(): void {
    this.carregarContratosAtivos();
  }
}