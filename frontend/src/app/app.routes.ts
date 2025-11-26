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
        path: 'auctions',
        loadComponent: () => import('./pages/auction/auction-list.component').then(m => m.AuctionListComponent)
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
      }
    ]
  },
  {
    path: '**',
    redirectTo: ''
  }
];