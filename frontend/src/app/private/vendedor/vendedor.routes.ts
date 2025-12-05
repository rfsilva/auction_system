import { Routes } from '@angular/router';

export const vendedorRoutes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./dashboard-vendedor.component').then(m => m.DashboardVendedorComponent)
  },
  
  // ========================================
  // GESTÃO DE PRODUTOS
  // ========================================
  {
    path: 'produtos',
    loadComponent: () => import('../../pages/produto/produto-list.component').then(m => m.ProdutoListComponent)
  },
  {
    path: 'produtos/novo',
    loadComponent: () => import('../../pages/produto/produto-form.component').then(m => m.ProdutoFormComponent)
  },
  {
    path: 'produtos/:id',
    loadComponent: () => import('../../pages/produto/produto-form.component').then(m => m.ProdutoFormComponent)
  },
  {
    path: 'produtos/:id/editar',
    loadComponent: () => import('../../pages/produto/produto-form.component').then(m => m.ProdutoFormComponent)
  },
  
  // ========================================
  // GESTÃO DE LOTES
  // ========================================
  {
    path: 'lotes',
    loadComponent: () => import('../../pages/lote/lote-list.component').then(m => m.LoteListComponent)
  },
  {
    path: 'lotes/novo',
    loadComponent: () => import('../../pages/lote/lote-form.component').then(m => m.LoteFormComponent)
  },
  {
    path: 'lotes/:id',
    loadComponent: () => import('../../pages/lote/lote-form.component').then(m => m.LoteFormComponent)
  },
  {
    path: 'lotes/:id/editar',
    loadComponent: () => import('../../pages/lote/lote-form.component').then(m => m.LoteFormComponent)
  },
  
  // ========================================
  // GESTÃO DE CONTRATOS
  // ========================================
  {
    path: 'contratos',
    loadComponent: () => import('../../pages/contrato/meus-contratos.component').then(m => m.MeusContratosComponent)
  }
];