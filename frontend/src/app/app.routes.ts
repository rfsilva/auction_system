import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layouts/main-layout.component';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      {
        path: '',
        loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent)
      },
      {
        path: 'auth/login',
        loadComponent: () => import('./pages/auth/login.component').then(m => m.LoginComponent)
      },
      {
        path: 'auth/register',
        loadComponent: () => import('./pages/auth/register.component').then(m => m.RegisterComponent)
      },
      {
        path: 'profile',
        loadComponent: () => import('./pages/profile/profile.component').then(m => m.ProfileComponent),
        canActivate: [authGuard]
      },
      {
        path: 'realtime-test',
        loadComponent: () => import('./pages/realtime-test/realtime-test.component').then(m => m.RealtimeTestComponent)
      },
      // Rotas do catálogo público
      {
        path: 'catalogo',
        loadComponent: () => import('./pages/catalogo/catalogo.component').then(m => m.CatalogoComponent)
      },
      // Rotas de produtos para vendedores
      {
        path: 'produtos/novo',
        loadComponent: () => import('./pages/produto/produto-form.component').then(m => m.ProdutoFormComponent),
        canActivate: [authGuard]
      },
      {
        path: 'produtos/meus-produtos',
        loadComponent: () => import('./pages/produto/produto-list.component').then(m => m.ProdutoListComponent),
        canActivate: [authGuard]
      },
      {
        path: 'produtos/:id/editar',
        loadComponent: () => import('./pages/produto/produto-form.component').then(m => m.ProdutoFormComponent),
        canActivate: [authGuard]
      },
      {
        path: 'produtos/:id',
        loadComponent: () => import('./pages/produto/produto-form.component').then(m => m.ProdutoFormComponent),
        canActivate: [authGuard]
      },
      // Rotas de lotes para vendedores
      {
        path: 'lotes',
        loadComponent: () => import('./pages/lote/lote-list.component').then(m => m.LoteListComponent),
        canActivate: [authGuard]
      },
      {
        path: 'lotes/novo',
        loadComponent: () => import('./pages/lote/lote-form.component').then(m => m.LoteFormComponent),
        canActivate: [authGuard]
      },
      {
        path: 'lotes/:id',
        loadComponent: () => import('./pages/lote/lote-form.component').then(m => m.LoteFormComponent),
        canActivate: [authGuard]
      },
      {
        path: 'lotes/:id/editar',
        loadComponent: () => import('./pages/lote/lote-form.component').then(m => m.LoteFormComponent),
        canActivate: [authGuard]
      },
      // Rotas de contratos para vendedores
      {
        path: 'contratos/meus-contratos',
        loadComponent: () => import('./pages/contrato/meus-contratos.component').then(m => m.MeusContratosComponent),
        canActivate: [authGuard]
      },
      // Rotas administrativas
      {
        path: 'admin/dashboard',
        loadComponent: () => import('./pages/admin/admin-dashboard.component').then(m => m.AdminDashboardComponent),
        canActivate: [authGuard]
      },
      {
        path: 'admin/contratos',
        loadComponent: () => import('./pages/contrato/contrato-list.component').then(m => m.ContratoListComponent),
        canActivate: [authGuard]
      },
      {
        path: 'admin/contratos/novo',
        loadComponent: () => import('./pages/contrato/contrato-form.component').then(m => m.ContratoFormComponent),
        canActivate: [authGuard]
      },
      {
        path: 'admin/contratos/:id',
        loadComponent: () => import('./pages/contrato/contrato-form.component').then(m => m.ContratoFormComponent),
        canActivate: [authGuard]
      },
      {
        path: 'admin/contratos/:id/editar',
        loadComponent: () => import('./pages/contrato/contrato-form.component').then(m => m.ContratoFormComponent),
        canActivate: [authGuard]
      },
      // Rotas de usuários para administradores
      {
        path: 'admin/usuarios',
        loadComponent: () => import('./pages/admin/usuario-list.component').then(m => m.UsuarioListComponent),
        canActivate: [authGuard]
      },
      // Rota para ativar vendedor
      {
        path: 'admin/ativar-vendedor',
        loadComponent: () => import('./pages/contrato/ativar-vendedor.component').then(m => m.AtivarVendedorComponent),
        canActivate: [authGuard]
      }
    ]
  },
  {
    path: '**',
    redirectTo: ''
  }
];