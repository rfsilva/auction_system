import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, BehaviorSubject, timer, EMPTY } from 'rxjs';
import { catchError, switchMap, tap, shareReplay, map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import {
  ContratoEstatisticas,
  ComissaoRelatorio,
  ContratoVencendoRelatorio,
  DashboardFiltros,
  DashboardStatus,
  MetricaCard,
  ApiResponse,
  UrgenciaEnum
} from '../models/dashboard-admin.model';

/**
 * Service para Dashboard Administrativo de Contratos
 * História 4: Dashboard Administrativo de Contratos - Sprint S2.2
 * CORRIGIDO: URLs atualizadas para corresponder exatamente aos controllers do backend
 */
@Injectable({
  providedIn: 'root'
})
export class DashboardAdminService {
  private readonly baseUrl = `${environment.apiUrl}/api/admin/contratos`;
  
  // Estados do dashboard
  private dashboardStatusSubject = new BehaviorSubject<DashboardStatus>({
    carregando: false,
    ultimaAtualizacao: new Date().toISOString(),
    autoRefreshAtivo: false
  });
  
  private estatisticasSubject = new BehaviorSubject<ContratoEstatisticas | null>(null);
  private comissoesSubject = new BehaviorSubject<ComissaoRelatorio | null>(null);
  private contratosVencendoSubject = new BehaviorSubject<ContratoVencendoRelatorio | null>(null);
  
  // Observables públicos
  public dashboardStatus$ = this.dashboardStatusSubject.asObservable();
  public estatisticas$ = this.estatisticasSubject.asObservable();
  public comissoes$ = this.comissoesSubject.asObservable();
  public contratosVencendo$ = this.contratosVencendoSubject.asObservable();
  
  // Timer para auto-refresh
  private autoRefreshTimer: Observable<number> | null = null;
  private autoRefreshSubscription: any = null;

  constructor(private http: HttpClient) {}

  /**
   * Obtém estatísticas consolidadas de contratos
   * CORRIGIDO: Usa a rota correta /api/admin/contratos/estatisticas
   */
  obterEstatisticas(): Observable<ContratoEstatisticas> {
    this.updateStatus({ carregando: true });
    
    return this.http.get<ApiResponse<ContratoEstatisticas>>(`${this.baseUrl}/estatisticas`)
      .pipe(
        tap(response => {
          this.estatisticasSubject.next(response.data);
          this.updateStatus({ 
            carregando: false, 
            ultimaAtualizacao: new Date().toISOString(),
            erro: undefined
          });
        }),
        catchError(error => {
          this.updateStatus({ 
            carregando: false, 
            erro: 'Erro ao carregar estatísticas' 
          });
          throw error;
        }),
        map(response => response.data),
        shareReplay(1)
      );
  }

  /**
   * Obtém relatório de comissões por período
   * CORRIGIDO: Usa a rota correta /api/admin/contratos/estatisticas/comissoes
   */
  obterRelatorioComissoes(
    inicio: string,
    fim: string,
    vendedor?: string,
    categoria?: string,
    status?: string
  ): Observable<ComissaoRelatorio> {
    let params = new HttpParams()
      .set('inicio', inicio)
      .set('fim', fim);
    
    if (vendedor) params = params.set('vendedor', vendedor);
    if (categoria) params = params.set('categoria', categoria);
    if (status) params = params.set('status', status);

    return this.http.get<ApiResponse<ComissaoRelatorio>>(`${this.baseUrl}/estatisticas/comissoes`, { params })
      .pipe(
        tap(response => {
          this.comissoesSubject.next(response.data);
        }),
        catchError(error => {
          console.error('Erro ao obter relatório de comissões:', error);
          throw error;
        }),
        map(response => response.data)
      );
  }

  /**
   * Obtém contratos próximos ao vencimento
   * CORRIGIDO: Usa a rota correta /api/admin/contratos/vencimentos
   */
  obterContratosVencendo(
    dias: number = 30,
    incluirNotificados: boolean = true,
    vendedor?: string,
    categoria?: string,
    urgencia?: UrgenciaEnum
  ): Observable<ContratoVencendoRelatorio> {
    let params = new HttpParams()
      .set('dias', dias.toString())
      .set('incluirNotificados', incluirNotificados.toString());
    
    if (vendedor) params = params.set('vendedor', vendedor);
    if (categoria) params = params.set('categoria', categoria);
    if (urgencia) params = params.set('urgencia', urgencia);

    return this.http.get<ApiResponse<ContratoVencendoRelatorio>>(`${this.baseUrl}/vencimentos`, { params })
      .pipe(
        tap(response => {
          this.contratosVencendoSubject.next(response.data);
        }),
        catchError(error => {
          console.error('Erro ao obter contratos vencendo:', error);
          throw error;
        }),
        map(response => response.data)
      );
  }

  /**
   * Carrega todos os dados do dashboard
   */
  carregarDashboard(filtros: DashboardFiltros): Observable<any> {
    this.updateStatus({ carregando: true });
    
    const hoje = new Date();
    const inicio = this.calcularDataInicio(hoje, filtros.periodo);
    const fim = filtros.dataFim || hoje.toISOString().split('T')[0];
    
    // Carrega todos os dados em paralelo
    const estatisticas$ = this.obterEstatisticas();
    const comissoes$ = this.obterRelatorioComissoes(
      inicio, 
      fim, 
      filtros.vendedor, 
      filtros.categoria
    );
    const contratosVencendo$ = this.obterContratosVencendo(30, true, filtros.vendedor, filtros.categoria);
    
    return new Observable(observer => {
      Promise.all([
        estatisticas$.toPromise(),
        comissoes$.toPromise(),
        contratosVencendo$.toPromise()
      ]).then(([estatisticas, comissoes, contratosVencendo]) => {
        this.updateStatus({ 
          carregando: false, 
          ultimaAtualizacao: new Date().toISOString(),
          erro: undefined
        });
        
        observer.next({
          estatisticas,
          comissoes,
          contratosVencendo
        });
        observer.complete();
      }).catch(error => {
        this.updateStatus({ 
          carregando: false, 
          erro: 'Erro ao carregar dashboard' 
        });
        observer.error(error);
      });
    });
  }

  /**
   * Inicia auto-refresh do dashboard
   */
  iniciarAutoRefresh(intervalos: number = 30000, filtros: DashboardFiltros): void {
    this.pararAutoRefresh();
    
    this.autoRefreshTimer = timer(intervalos, intervalos);
    this.autoRefreshSubscription = this.autoRefreshTimer.pipe(
      switchMap(() => this.carregarDashboard(filtros))
    ).subscribe({
      next: () => {
        console.log('Dashboard atualizado automaticamente');
      },
      error: (error) => {
        console.error('Erro no auto-refresh:', error);
      }
    });
    
    this.updateStatus({ autoRefreshAtivo: true });
  }

  /**
   * Para o auto-refresh do dashboard
   */
  pararAutoRefresh(): void {
    if (this.autoRefreshSubscription) {
      this.autoRefreshSubscription.unsubscribe();
      this.autoRefreshSubscription = null;
    }
    this.updateStatus({ autoRefreshAtivo: false });
  }

  /**
   * Exporta relatório de contratos vencendo em CSV
   * CORRIGIDO: Usa a rota correta /api/admin/contratos/vencimentos/export/csv
   */
  exportarContratosVencendoCSV(
    dias: number = 30,
    incluirNotificados: boolean = true,
    vendedor?: string,
    categoria?: string,
    urgencia?: UrgenciaEnum
  ): Observable<Blob> {
    let params = new HttpParams()
      .set('dias', dias.toString())
      .set('incluirNotificados', incluirNotificados.toString());
    
    if (vendedor) params = params.set('vendedor', vendedor);
    if (categoria) params = params.set('categoria', categoria);
    if (urgencia) params = params.set('urgencia', urgencia);

    return this.http.get(`${this.baseUrl}/vencimentos/export/csv`, {
      params,
      responseType: 'blob'
    });
  }

  /**
   * Exporta relatório de contratos vencendo em PDF
   * CORRIGIDO: Usa a rota correta /api/admin/contratos/vencimentos/export/pdf
   */
  exportarContratosVencendoPDF(
    dias: number = 30,
    incluirNotificados: boolean = true,
    vendedor?: string,
    categoria?: string,
    urgencia?: UrgenciaEnum
  ): Observable<Blob> {
    let params = new HttpParams()
      .set('dias', dias.toString())
      .set('incluirNotificados', incluirNotificados.toString());
    
    if (vendedor) params = params.set('vendedor', vendedor);
    if (categoria) params = params.set('categoria', categoria);
    if (urgencia) params = params.set('urgencia', urgencia);

    return this.http.get(`${this.baseUrl}/vencimentos/export/pdf`, {
      params,
      responseType: 'blob'
    });
  }

  /**
   * Envia notificações de contratos vencendo
   * CORRIGIDO: Usa a rota correta /api/admin/contratos/vencimentos/notificar
   */
  enviarNotificacoesVencimento(): Observable<string> {
    return this.http.post<ApiResponse<string>>(`${this.baseUrl}/vencimentos/notificar`, {})
      .pipe(
        map(response => response.data)
      );
  }

  /**
   * Obtém cálculos avançados de comissões
   * CORRIGIDO: Usa a rota correta /api/admin/contratos/comissoes/detalhado
   */
  calcularComissoesDetalhado(
    inicio: string,
    fim: string,
    vendedor?: string,
    categoria?: string,
    status?: string
  ): Observable<ComissaoRelatorio> {
    let params = new HttpParams()
      .set('inicio', inicio)
      .set('fim', fim);
    
    if (vendedor) params = params.set('vendedor', vendedor);
    if (categoria) params = params.set('categoria', categoria);
    if (status) params = params.set('status', status);

    return this.http.get<ApiResponse<ComissaoRelatorio>>(`${this.baseUrl}/comissoes/detalhado`, { params })
      .pipe(
        map(response => response.data)
      );
  }

  /**
   * Obtém comissões agrupadas por vendedor
   * CORRIGIDO: Usa a rota correta /api/admin/contratos/comissoes/por-vendedor
   */
  calcularComissoesPorVendedor(
    inicio: string,
    fim: string,
    categoria?: string
  ): Observable<{ [key: string]: any }> {
    let params = new HttpParams()
      .set('inicio', inicio)
      .set('fim', fim);
    
    if (categoria) params = params.set('categoria', categoria);

    return this.http.get<ApiResponse<{ [key: string]: any }>>(`${this.baseUrl}/comissoes/por-vendedor`, { params })
      .pipe(
        map(response => response.data)
      );
  }

  /**
   * Calcula projeção de receita
   * CORRIGIDO: Usa a rota correta /api/admin/contratos/comissoes/projecao
   */
  calcularProjecaoReceita(
    meses: number = 3,
    categoria?: string
  ): Observable<number> {
    let params = new HttpParams()
      .set('meses', meses.toString());
    
    if (categoria) params = params.set('categoria', categoria);

    return this.http.get<ApiResponse<number>>(`${this.baseUrl}/comissoes/projecao`, { params })
      .pipe(
        map(response => response.data)
      );
  }

  /**
   * Obtém breakdown detalhado de um vendedor
   * CORRIGIDO: Usa a rota correta /api/admin/contratos/comissoes/breakdown/{vendedorId}
   */
  obterBreakdownVendedor(
    vendedorId: string,
    inicio: string,
    fim: string
  ): Observable<any[]> {
    let params = new HttpParams()
      .set('inicio', inicio)
      .set('fim', fim);

    return this.http.get<ApiResponse<any[]>>(`${this.baseUrl}/comissoes/breakdown/${vendedorId}`, { params })
      .pipe(
        map(response => response.data)
      );
  }

  /**
   * Compara performance entre períodos
   * CORRIGIDO: Usa a rota correta /api/admin/contratos/comissoes/comparacao
   */
  compararPeriodos(
    periodo1Inicio: string,
    periodo1Fim: string,
    periodo2Inicio: string,
    periodo2Fim: string,
    categoria?: string
  ): Observable<{ [key: string]: any }> {
    let params = new HttpParams()
      .set('periodo1Inicio', periodo1Inicio)
      .set('periodo1Fim', periodo1Fim)
      .set('periodo2Inicio', periodo2Inicio)
      .set('periodo2Fim', periodo2Fim);
    
    if (categoria) params = params.set('categoria', categoria);

    return this.http.get<ApiResponse<{ [key: string]: any }>>(`${this.baseUrl}/comissoes/comparacao`, { params })
      .pipe(
        map(response => response.data)
      );
  }

  /**
   * Converte estatísticas em métricas para cards
   */
  converterEstatisticasParaMetricas(estatisticas: ContratoEstatisticas): MetricaCard[] {
    return [
      {
        titulo: 'Contratos Ativos',
        valor: estatisticas.totalContratos,
        icone: 'fas fa-file-contract',
        cor: 'primary',
        formato: 'numero'
      },
      {
        titulo: 'Vendedores Ativos',
        valor: estatisticas.vendedoresAtivos,
        icone: 'fas fa-users',
        cor: 'info',
        formato: 'numero'
      },
      {
        titulo: 'Receita do Mês',
        valor: estatisticas.receitaRealizadaMes,
        icone: 'fas fa-dollar-sign',
        cor: 'success',
        formato: 'moeda'
      },
      {
        titulo: 'Receita Projetada',
        valor: estatisticas.receitaProjetadaMes,
        icone: 'fas fa-chart-line',
        cor: 'warning',
        formato: 'moeda'
      },
      {
        titulo: 'Taxa Média Comissão',
        valor: estatisticas.taxaMediaComissao * 100,
        icone: 'fas fa-percentage',
        cor: 'info',
        formato: 'percentual'
      },
      {
        titulo: 'Contratos Vencendo',
        valor: estatisticas.contratosVencendo30Dias,
        icone: 'fas fa-exclamation-triangle',
        cor: estatisticas.contratosVencendo30Dias > 10 ? 'danger' : 'warning',
        formato: 'numero'
      }
    ];
  }

  /**
   * Formata valor conforme o tipo
   */
  formatarValor(valor: number | string, formato?: string): string {
    if (typeof valor === 'string') return valor;
    
    switch (formato) {
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

  /**
   * Obtém classe CSS para urgência
   */
  getUrgenciaClass(urgencia: UrgenciaEnum): string {
    switch (urgencia) {
      case UrgenciaEnum.ALTA:
        return 'badge-danger';
      case UrgenciaEnum.MEDIA:
        return 'badge-warning';
      case UrgenciaEnum.BAIXA:
        return 'badge-info';
      default:
        return 'badge-secondary';
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

  // Métodos privados
  private updateStatus(updates: Partial<DashboardStatus>): void {
    const currentStatus = this.dashboardStatusSubject.value;
    this.dashboardStatusSubject.next({ ...currentStatus, ...updates });
  }

  private calcularDataInicio(dataFim: Date, periodo: string): string {
    const data = new Date(dataFim);
    
    switch (periodo) {
      case 'dia':
        data.setDate(data.getDate() - 1);
        break;
      case 'semana':
        data.setDate(data.getDate() - 7);
        break;
      case 'mes':
        data.setMonth(data.getMonth() - 1);
        break;
      case 'trimestre':
        data.setMonth(data.getMonth() - 3);
        break;
      case 'ano':
        data.setFullYear(data.getFullYear() - 1);
        break;
      default:
        data.setMonth(data.getMonth() - 1);
    }
    
    return data.toISOString().split('T')[0];
  }

  /**
   * Limpa todos os dados do dashboard
   */
  limparDados(): void {
    this.estatisticasSubject.next(null);
    this.comissoesSubject.next(null);
    this.contratosVencendoSubject.next(null);
    this.pararAutoRefresh();
  }

  /**
   * Obtém status atual do dashboard
   */
  getStatus(): DashboardStatus {
    return this.dashboardStatusSubject.value;
  }
}