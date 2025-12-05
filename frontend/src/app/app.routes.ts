import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layouts/main-layout.component';
import { PrivateGuard, UsuarioGuard, VendedorGuard, AdminGuard } from './core/guards/area.guards';

export const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      // ========================================
      // 🌐 ÁREA PÚBLICA (Sem Guards)
      // ========================================
      { 
        path: '', 
        loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent)
      },
      { 
        path: 'catalogo', 
        loadChildren: () => import('./public/catalogo/catalogo.routes').then(m => m.catalogoRoutes)
      },
      { 
        path: 'sobre', 
        loadComponent: () => import('./public/pages/sobre.component').then(m => m.SobreComponent)
      },
      { 
        path: 'contato', 
        loadComponent: () => import('./public/pages/contato.component').then(m => m.ContatoComponent)
      },
      
      // ========================================
      // 🔐 AUTENTICAÇÃO
      // ========================================
      { 
        path: 'auth', 
        loadChildren: () => import('./auth/auth.routes').then(m => m.authRoutes)
      },
      
      // ========================================
      // 👤 ÁREA PRIVADA - USUÁRIO
      // ========================================
      { 
        path: 'app', 
        canActivate: [UsuarioGuard],
        loadChildren: () => import('./private/usuario/usuario.routes').then(m => m.usuarioRoutes)
      },
      
      // ========================================
      // 🏪 ÁREA PRIVADA - VENDEDOR
      // ========================================
      { 
        path: 'vendedor', 
        canActivate: [VendedorGuard],
        loadChildren: () => import('./private/vendedor/vendedor.routes').then(m => m.vendedorRoutes)
      },
      
      // ========================================
      // 👑 ÁREA PRIVADA - ADMIN
      // ========================================
      { 
        path: 'admin', 
        canActivate: [AdminGuard],
        loadChildren: () => import('./private/admin/admin.routes').then(m => m.adminRoutes)
      },

      // ========================================
      // 🧪 ÁREA DE TESTES (Temporário)
      // ========================================
      {
        path: 'realtime-test',
        loadComponent: () => import('./pages/realtime-test/realtime-test.component').then(m => m.RealtimeTestComponent)
      },

      // ========================================
      // 🔄 REDIRECTS DE COMPATIBILIDADE
      // ========================================
      
      // Redirects do sistema antigo para nova estrutura
      { path: 'produtos/novo', redirectTo: '/vendedor/produtos/novo', pathMatch: 'full' },
      { path: 'produtos/meus-produtos', redirectTo: '/vendedor/produtos', pathMatch: 'full' },
      { path: 'produtos/:id/editar', redirectTo: '/vendedor/produtos/:id/editar', pathMatch: 'full' },
      { path: 'lotes', redirectTo: '/vendedor/lotes', pathMatch: 'full' },
      { path: 'lotes/novo', redirectTo: '/vendedor/lotes/novo', pathMatch: 'full' },
      { path: 'lotes/:id/editar', redirectTo: '/vendedor/lotes/:id/editar', pathMatch: 'full' },
      { path: 'contratos/meus-contratos', redirectTo: '/vendedor/contratos', pathMatch: 'full' },
      { path: 'admin/contratos', redirectTo: '/admin/contratos', pathMatch: 'full' },
      { path: 'admin/usuarios', redirectTo: '/admin/usuarios', pathMatch: 'full' },
      { path: 'admin/ativar-vendedor', redirectTo: '/admin/contratos/ativar-vendedor', pathMatch: 'full' },
      
      // Redirect do catálogo antigo de produtos
      { path: 'catalogo-produtos', redirectTo: '/catalogo', pathMatch: 'full' },
      
      // Profile redirect
      { path: 'profile', redirectTo: '/app/profile', pathMatch: 'full' }
    ]
  },
  { 
    path: '**', 
    loadComponent: () => import('./shared/components/not-found.component').then(m => m.NotFoundComponent)
  }
];