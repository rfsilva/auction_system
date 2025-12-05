import { Routes } from '@angular/router';

export const catalogoRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('../../pages/catalogo/catalogo-lotes.component').then(m => m.CatalogoLotesComponent)
  },
  {
    path: 'lotes',
    loadComponent: () => import('../../pages/catalogo/catalogo-lotes.component').then(m => m.CatalogoLotesComponent)
  },
  {
    path: 'lotes/:id',
    loadComponent: () => import('./lote-detalhe.component').then(m => m.LoteDetalheComponent)
  },
  {
    path: 'lotes/:loteId/produtos/:produtoId',
    loadComponent: () => import('./produto-detalhe.component').then(m => m.ProdutoDetalheComponent)
  }
];