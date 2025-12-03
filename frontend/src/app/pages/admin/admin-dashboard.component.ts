import { Component, OnInit, OnDestroy, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Chart, ChartConfiguration, ChartType, registerables } from 'chart.js';
import { Subscription } from 'rxjs';

import { DashboardAdminService } from '../../core/services/dashboard-admin.service';
import { AuthService } from '../../core/services/auth.service';
import { ContratoService } from '../../core/services/contrato.service';
import { VendedorService } from '../../core/services/vendedor.service';

import { MetricCardComponent } from '../../shared/components/metric-card.component';
import { ContratosVencendoTableComponent } from '../../shared/components/contratos-vencendo-table.component';

import {
  ContratoEstatisticas,
  ComissaoRelatorio,
  ContratoVencendoRelatorio,
  DashboardFiltros,
  DashboardStatus,
  MetricaCard,
  UrgenciaEnum,
  ContratoVencendo
} from '../../core/models/dashboard-admin.model';

/**
 * Dashboard Administrativo de Contratos
 * História 4: Dashboard Administrativo de Contratos - Sprint S2.2
 */
@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [
    CommonModule, 
    FormsModule, 
    ReactiveFormsModule, 
    RouterModule,
    MetricCardComponent,
    ContratosVencendoTableComponent
  ],
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent implements OnInit, OnDestroy, AfterViewInit {
  
  // ViewChild para gráficos
  @ViewChild('chartContratosPorStatus', { static: false }) chartContratosRef!: ElementRef;
  @ViewChild('chartComissoesPorMes', { static: false }) chartComissoesRef!: ElementRef;
  @ViewChild('chartReceitaProjetada', { static: false }) chartReceitaRef!: ElementRef;

  // Dados do dashboard
  estatisticas: ContratoEstatisticas | null = null;
  comissoes: ComissaoRelatorio | null = null;
  contratosVencendo: ContratoVencendoRelatorio | null = null;
  metricas: MetricaCard[] = [];
  
  // Status e controle
  dashboardStatus: DashboardStatus = {
    carregando: false,
    ultimaAtualizacao: new Date().toISOString(),
    autoRefreshAtivo: false
  };
  
  // Formulário de filtros
  filtrosForm: FormGroup;
  filtros: DashboardFiltros = {
    periodo: 'mes',
    autoRefresh: true
  };
  
  // Dados para seleção
  categorias: string[] = [];
  vendedores: any[] = [];
  
  // Gráficos
  chartContratosPorStatus: Chart | null = null;
  chartComissoesPorMes: Chart | null = null;
  chartReceitaProjetada: Chart | null = null;
  
  // Subscriptions
  private subscriptions: Subscription[] = [];
  
  // Configurações
  autoRefreshInterval = 30000; // 30 segundos
  showFiltros = false;
  
  // Enum para template
  UrgenciaEnum = UrgenciaEnum;

  constructor(
    private dashboardService: DashboardAdminService,
    public authService: AuthService,
    private contratoService: ContratoService,
    private vendedorService: VendedorService,
    private fb: FormBuilder
  ) {
    // Registrar Chart.js
    Chart.register(...registerables);
    
    // Criar formulário de filtros
    this.filtrosForm = this.fb.group({
      periodo: ['mes'],
      dataInicio: [''],
      dataFim: [''],
      vendedor: [''],
      categoria: [''],
      autoRefresh: [true]
    });
  }

  ngOnInit(): void {
    this.inicializarDashboard();
    this.carregarDadosIniciais();
    this.configurarSubscriptions();
  }

  ngAfterViewInit(): void {
    // Aguarda um pouco para garantir que os elementos estão renderizados
    setTimeout(() => {
      this.inicializarGraficos();
    }, 100);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
    this.dashboardService.pararAutoRefresh();
    this.destruirGraficos();
  }

  /**
   * Inicializa o dashboard
   */
  private inicializarDashboard(): void {
    // Configurar filtros iniciais
    this.filtros = {
      periodo: 'mes',
      autoRefresh: true
    };
    
    this.filtrosForm.patchValue(this.filtros);
  }

  /**
   * Carrega dados iniciais
   */
  private carregarDadosIniciais(): void {
    // Carregar categorias
    this.contratoService.listarCategorias().subscribe({
      next: (response) => {
        this.categorias = response.data;
      },
      error: (error) => {
        console.error('Erro ao carregar categorias:', error);
      }
    });
    
    // Carregar vendedores
    this.vendedorService.listarAtivos().subscribe({
      next: (response) => {
        this.vendedores = response.data;
      },
      error: (error) => {
        console.error('Erro ao carregar vendedores:', error);
      }
    });
    
    // Carregar dados do dashboard
    this.carregarDashboard();
  }

  /**
   * Configura subscriptions para observables
   */
  private configurarSubscriptions(): void {
    // Status do dashboard
    const statusSub = this.dashboardService.dashboardStatus$.subscribe(status => {
      this.dashboardStatus = status;
    });
    this.subscriptions.push(statusSub);
    
    // Estatísticas
    const estatisticasSub = this.dashboardService.estatisticas$.subscribe(estatisticas => {
      if (estatisticas) {
        this.estatisticas = estatisticas;
        this.metricas = this.dashboardService.converterEstatisticasParaMetricas(estatisticas);
        this.atualizarGraficoContratosPorStatus();
      }
    });
    this.subscriptions.push(estatisticasSub);
    
    // Comissões
    const comissoesSub = this.dashboardService.comissoes$.subscribe(comissoes => {
      if (comissoes) {
        this.comissoes = comissoes;
        this.atualizarGraficoComissoes();
      }
    });
    this.subscriptions.push(comissoesSub);
    
    // Contratos vencendo
    const contratosVencendoSub = this.dashboardService.contratosVencendo$.subscribe(contratos => {
      if (contratos) {
        this.contratosVencendo = contratos;
      }
    });
    this.subscriptions.push(contratosVencendoSub);
  }

  /**
   * Carrega todos os dados do dashboard
   */
  carregarDashboard(): void {
    const filtrosAtivos = this.filtrosForm.value;
    
    this.dashboardService.carregarDashboard(filtrosAtivos).subscribe({
      next: (dados) => {
        console.log('Dashboard carregado com sucesso');
        
        // Atualizar gráficos após carregar dados
        setTimeout(() => {
          this.atualizarTodosGraficos();
        }, 100);
      },
      error: (error) => {
        console.error('Erro ao carregar dashboard:', error);
      }
    });
  }

  /**
   * Aplica filtros e recarrega dashboard
   */
  aplicarFiltros(): void {
    this.filtros = { ...this.filtrosForm.value };
    this.carregarDashboard();
    
    // Configurar auto-refresh se habilitado
    if (this.filtros.autoRefresh) {
      this.iniciarAutoRefresh();
    } else {
      this.pararAutoRefresh();
    }
  }

  /**
   * Limpa filtros e recarrega
   */
  limparFiltros(): void {
    this.filtrosForm.reset({
      periodo: 'mes',
      autoRefresh: true
    });
    this.aplicarFiltros();
  }

  /**
   * Toggle do painel de filtros
   */
  toggleFiltros(): void {
    this.showFiltros = !this.showFiltros;
  }

  /**
   * Inicia auto-refresh
   */
  iniciarAutoRefresh(): void {
    this.dashboardService.iniciarAutoRefresh(this.autoRefreshInterval, this.filtros);
  }

  /**
   * Para auto-refresh
   */
  pararAutoRefresh(): void {
    this.dashboardService.pararAutoRefresh();
  }

  /**
   * Força atualização manual
   */
  atualizarManual(): void {
    this.carregarDashboard();
  }

  // Métodos para gráficos
  
  /**
   * Inicializa todos os gráficos
   */
  private inicializarGraficos(): void {
    this.criarGraficoContratosPorStatus();
    this.criarGraficoComissoes();
    this.criarGraficoReceitaProjetada();
  }

  /**
   * Cria gráfico de contratos por status
   */
  private criarGraficoContratosPorStatus(): void {
    if (!this.chartContratosRef?.nativeElement) return;
    
    const ctx = this.chartContratosRef.nativeElement.getContext('2d');
    
    this.chartContratosPorStatus = new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels: [],
        datasets: [{
          data: [],
          backgroundColor: [
            '#28a745', // ACTIVE - Verde
            '#ffc107', // DRAFT - Amarelo
            '#dc3545', // EXPIRED - Vermelho
            '#6c757d', // CANCELLED - Cinza
            '#17a2b8'  // SUSPENDED - Azul
          ],
          borderWidth: 2,
          borderColor: '#fff'
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'bottom'
          },
          title: {
            display: true,
            text: 'Contratos por Status'
          }
        }
      }
    });
  }

  /**
   * Cria gráfico de comissões
   */
  private criarGraficoComissoes(): void {
    if (!this.chartComissoesRef?.nativeElement) return;
    
    const ctx = this.chartComissoesRef.nativeElement.getContext('2d');
    
    this.chartComissoesPorMes = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: [],
        datasets: [{
          label: 'Comissões (R$)',
          data: [],
          backgroundColor: '#007bff',
          borderColor: '#0056b3',
          borderWidth: 1
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          title: {
            display: true,
            text: 'Comissões por Contrato'
          }
        },
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              callback: function(value) {
                return 'R$ ' + new Intl.NumberFormat('pt-BR').format(value as number);
              }
            }
          }
        }
      }
    });
  }

  /**
   * Cria gráfico de receita projetada
   */
  private criarGraficoReceitaProjetada(): void {
    if (!this.chartReceitaRef?.nativeElement) return;
    
    const ctx = this.chartReceitaRef.nativeElement.getContext('2d');
    
    this.chartReceitaProjetada = new Chart(ctx, {
      type: 'line',
      data: {
        labels: ['Realizada', 'Projetada'],
        datasets: [{
          label: 'Receita (R$)',
          data: [],
          borderColor: '#28a745',
          backgroundColor: 'rgba(40, 167, 69, 0.1)',
          borderWidth: 2,
          fill: true
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          title: {
            display: true,
            text: 'Receita Realizada vs Projetada'
          }
        },
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              callback: function(value) {
                return 'R$ ' + new Intl.NumberFormat('pt-BR').format(value as number);
              }
            }
          }
        }
      }
    });
  }

  /**
   * Atualiza gráfico de contratos por status
   */
  private atualizarGraficoContratosPorStatus(): void {
    if (!this.chartContratosPorStatus || !this.estatisticas) return;
    
    const labels = Object.keys(this.estatisticas.contratosPorStatus);
    const data = Object.values(this.estatisticas.contratosPorStatus);
    
    this.chartContratosPorStatus.data.labels = labels;
    this.chartContratosPorStatus.data.datasets[0].data = data;
    this.chartContratosPorStatus.update();
  }

  /**
   * Atualiza gráfico de comissões
   * CORRIGIDO: Usa os nomes corretos dos campos do DTO
   */
  private atualizarGraficoComissoes(): void {
    if (!this.chartComissoesPorMes || !this.comissoes) return;
    
    const labels = this.comissoes.porContrato.map(c => c.vendedorNome);
    const data = this.comissoes.porContrato.map(c => c.totalComissoes); // CORRIGIDO: era c.comissoes
    
    this.chartComissoesPorMes.data.labels = labels;
    this.chartComissoesPorMes.data.datasets[0].data = data;
    this.chartComissoesPorMes.update();
  }

  /**
   * Atualiza todos os gráficos
   */
  private atualizarTodosGraficos(): void {
    this.atualizarGraficoContratosPorStatus();
    this.atualizarGraficoComissoes();
    
    if (this.chartReceitaProjetada && this.estatisticas) {
      this.chartReceitaProjetada.data.datasets[0].data = [
        this.estatisticas.receitaRealizadaMes,
        this.estatisticas.receitaProjetadaMes
      ];
      this.chartReceitaProjetada.update();
    }
  }

  /**
   * Destrói todos os gráficos
   */
  private destruirGraficos(): void {
    if (this.chartContratosPorStatus) {
      this.chartContratosPorStatus.destroy();
    }
    if (this.chartComissoesPorMes) {
      this.chartComissoesPorMes.destroy();
    }
    if (this.chartReceitaProjetada) {
      this.chartReceitaProjetada.destroy();
    }
  }

  // Métodos para ações

  /**
   * Exporta contratos vencendo em CSV
   * CORRIGIDO: Previne comportamento padrão do link e redirecionamento
   */
  exportarCSV(event?: Event): void {
    if (event) {
      event.preventDefault();
      event.stopPropagation();
    }

    console.log('Iniciando exportação CSV...');
    
    this.dashboardService.exportarContratosVencendoCSV().subscribe({
      next: (blob) => {
        console.log('CSV recebido, iniciando download...');
        this.downloadFile(blob, 'csv');
      },
      error: (error) => {
        console.error('Erro ao exportar CSV:', error);
        // Aqui você pode adicionar uma notificação de erro para o usuário
      }
    });
  }

  /**
   * Exporta contratos vencendo em PDF
   * CORRIGIDO: Previne comportamento padrão do link e redirecionamento
   */
  exportarPDF(event?: Event): void {
    if (event) {
      event.preventDefault();
      event.stopPropagation();
    }

    console.log('Iniciando exportação PDF...');
    
    this.dashboardService.exportarContratosVencendoPDF().subscribe({
      next: (blob) => {
        console.log('PDF recebido, iniciando download...');
        this.downloadFile(blob, 'pdf');
      },
      error: (error) => {
        console.error('Erro ao exportar PDF:', error);
        // Aqui você pode adicionar uma notificação de erro para o usuário
      }
    });
  }

  /**
   * NOVO: Método utilitário para fazer download de arquivos
   */
  private downloadFile(blob: Blob, tipo: 'csv' | 'pdf'): void {
    try {
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      
      const dataAtual = new Date().toISOString().split('T')[0];
      const extensao = tipo === 'csv' ? 'csv' : 'pdf';
      link.download = `contratos-vencendo-${dataAtual}.${extensao}`;
      
      // Adicionar ao DOM temporariamente para funcionar em todos os navegadores
      document.body.appendChild(link);
      link.click();
      
      // Limpar
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
      
      console.log(`Download do arquivo ${tipo.toUpperCase()} iniciado com sucesso`);
    } catch (error) {
      console.error(`Erro ao fazer download do arquivo ${tipo.toUpperCase()}:`, error);
    }
  }

  /**
   * Envia notificações de vencimento
   */
  enviarNotificacoes(): void {
    this.dashboardService.enviarNotificacoesVencimento().subscribe({
      next: (message) => {
        console.log('Notificações enviadas:', message);
        // Recarregar dados após enviar notificações
        this.carregarDashboard();
      },
      error: (error) => {
        console.error('Erro ao enviar notificações:', error);
      }
    });
  }

  // Eventos dos componentes auxiliares

  /**
   * Evento para renovar contrato
   */
  onRenovarContrato(contrato: ContratoVencendo): void {
    console.log('Renovar contrato:', contrato);
    // Implementar lógica de renovação
    // Por exemplo, navegar para formulário de edição
  }

  /**
   * Evento para notificar contrato
   */
  onNotificarContrato(contrato: ContratoVencendo): void {
    console.log('Notificar contrato:', contrato);
    // Implementar lógica de notificação individual
    // Por exemplo, chamar API específica para este contrato
  }

  // Métodos utilitários

  /**
   * Formata valor conforme tipo
   */
  formatarValor(valor: number | string, formato?: string): string {
    return this.dashboardService.formatarValor(valor, formato);
  }

  /**
   * Obtém classe CSS para urgência
   */
  getUrgenciaClass(urgencia: UrgenciaEnum): string {
    return this.dashboardService.getUrgenciaClass(urgencia);
  }

  /**
   * Obtém texto da urgência
   */
  getUrgenciaText(urgencia: UrgenciaEnum): string {
    return this.dashboardService.getUrgenciaText(urgencia);
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
   * Obtém classe CSS para métrica
   */
  getMetricaClass(cor: string): string {
    return `metric-card metric-${cor}`;
  }

  /**
   * Verifica se há dados para exibir
   */
  temDados(): boolean {
    return !!(this.estatisticas || this.comissoes || this.contratosVencendo);
  }

  /**
   * Obtém próxima atualização formatada
   */
  getProximaAtualizacao(): string {
    if (!this.dashboardStatus.autoRefreshAtivo) return '';
    
    const proxima = new Date(Date.now() + this.autoRefreshInterval);
    return proxima.toLocaleTimeString('pt-BR');
  }
}