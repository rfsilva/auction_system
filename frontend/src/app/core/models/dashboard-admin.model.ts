/**
 * Modelos para o Dashboard Administrativo de Contratos
 * História 4: Dashboard Administrativo de Contratos - Sprint S2.2
 */

import { ContractStatus } from './contrato.model';

// Interfaces para Estatísticas de Contratos
export interface ContratoEstatisticas {
  totalContratos: number;
  contratosPorStatus: Record<ContractStatus, number>;
  vendedoresAtivos: number;
  receitaProjetadaMes: number;
  receitaRealizadaMes: number;
  taxaMediaComissao: number;
  contratosVencendo30Dias: number;
}

// Interfaces para Comissões
export interface PeriodoDto {
  inicio: string;
  fim: string;
}

export interface ComissaoResumo {
  totalComissoes: number;
  totalVendas: number;
  numeroTransacoes: number;
}

export interface ComissaoDto {
  contratoId: string;
  vendedorNome: string;
  categoria: string;
  taxaComissao: number;
  totalVendas: number;      // CORRIGIDO: era 'vendas'
  totalComissoes: number;   // CORRIGIDO: era 'comissoes'
  numeroTransacoes: number; // CORRIGIDO: era 'transacoes'
}

export interface ComissaoRelatorio {
  periodo: PeriodoDto;
  resumo: ComissaoResumo;
  porContrato: ComissaoDto[];
}

// Interfaces para Contratos Vencendo
export enum UrgenciaEnum {
  ALTA = 'ALTA',
  MEDIA = 'MEDIA', 
  BAIXA = 'BAIXA'
}

export interface ContratoVencendo {
  id: string;
  vendedorNome: string;
  categoria: string;
  validTo: string;
  diasRestantes: number;
  status: ContractStatus;
  urgencia: UrgenciaEnum;
  notificado: boolean;
}

export interface ContratoVencendoResumo {
  total: number;
  urgenciaAlta: number;
  urgenciaMedia: number;
  urgenciaBaixa: number;
}

export interface ContratoVencendoRelatorio {
  contratos: ContratoVencendo[];
  resumo: ContratoVencendoResumo;
}

// Interfaces para Filtros do Dashboard
export interface DashboardFiltros {
  periodo: 'dia' | 'semana' | 'mes' | 'trimestre' | 'ano';
  dataInicio?: string;
  dataFim?: string;
  vendedor?: string;
  categoria?: string;
  autoRefresh: boolean;
}

// Interface para Métricas do Dashboard
export interface MetricaCard {
  titulo: string;
  valor: number | string;
  icone: string;
  cor: 'primary' | 'success' | 'warning' | 'danger' | 'info';
  formato?: 'numero' | 'moeda' | 'percentual';
  tendencia?: {
    valor: number;
    tipo: 'alta' | 'baixa' | 'estavel';
  };
}

// Interface para Configuração de Gráficos
export interface GraficoConfig {
  tipo: 'bar' | 'line' | 'pie' | 'doughnut';
  dados: any;
  opcoes: any;
}

// Interface para Status de Conexão do Dashboard
export interface DashboardStatus {
  carregando: boolean;
  ultimaAtualizacao: string;
  erro?: string;
  autoRefreshAtivo: boolean;
  proximaAtualizacao?: string;
}

// Interface para Ações Rápidas
export interface AcaoRapida {
  id: string;
  titulo: string;
  descricao: string;
  icone: string;
  cor: string;
  acao: () => void;
  habilitada: boolean;
}

// Interface para Configurações do Dashboard
export interface DashboardConfig {
  intervalosAutoRefresh: number[];
  intervaloPadrao: number;
  formatoData: string;
  formatoMoeda: string;
  idioma: string;
  tema: 'claro' | 'escuro';
}

// Interface para Resposta da API
export interface ApiResponse<T> {
  success: boolean;
  data: T;
  message?: string;
  timestamp: string;
}