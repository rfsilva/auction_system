import { Routes } from '@angular/router';

export const usuarioRoutes: Routes = [
  {
    path: '',
    redirectTo: 'profile',
    pathMatch: 'full'
  },
  {
    path: 'profile',
    loadComponent: () => import('../../pages/profile/profile.component').then(m => m.ProfileComponent)
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./dashboard-usuario.component').then(m => m.DashboardUsuarioComponent)
  }
];