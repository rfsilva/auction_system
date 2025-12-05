import { Routes } from '@angular/router';

export const adminRoutes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  },
  {
    path: 'dashboard',
    loadComponent: () => import('../../pages/admin/admin-dashboard.component').then(m => m.AdminDashboardComponent)
  },
  
  // ========================================
  // GESTÃO DE CONTRATOS
  // ========================================
  {
    path: 'contratos',
    loadComponent: () => import('../../pages/contrato/contrato-list.component').then(m => m.ContratoListComponent)
  },
  {
    path: 'contratos/novo',
    loadComponent: () => import('../../pages/contrato/contrato-form.component').then(m => m.ContratoFormComponent)
  },
  {
    path: 'contratos/ativar-vendedor',
    loadComponent: () => import('../../pages/contrato/ativar-vendedor.component').then(m => m.AtivarVendedorComponent)
  },
  {
    path: 'contratos/:id',
    loadComponent: () => import('../../pages/contrato/contrato-form.component').then(m => m.ContratoFormComponent)
  },
  {
    path: 'contratos/:id/editar',
    loadComponent: () => import('../../pages/contrato/contrato-form.component').then(m => m.ContratoFormComponent)
  },
  
  // ========================================
  // GESTÃO DE USUÁRIOS
  // ========================================
  {
    path: 'usuarios',
    loadComponent: () => import('../../pages/admin/usuario-list.component').then(m => m.UsuarioListComponent)
  },
  
  // ========================================
  // GESTÃO DE VENDEDORES
  // ========================================
  {
    path: 'vendedores',
    loadComponent: () => import('./vendedor-list.component').then(m => m.VendedorListComponent)
  }
];