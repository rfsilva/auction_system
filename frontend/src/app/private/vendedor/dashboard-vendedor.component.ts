import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-dashboard-vendedor',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="container-fluid">
      <div class="row">
        <div class="col-12">
          <div class="welcome-header mb-4">
            <h2 class="mb-1">
              <i class="fas fa-store me-2"></i>
              Dashboard do Vendedor
            </h2>
            <p class="text-muted">Gerencie seus produtos e lotes</p>
          </div>
        </div>
      </div>

      <div class="row">
        <!-- Card de Produtos -->
        <div class="col-md-6 col-lg-3 mb-4">
          <div class="card h-100">
            <div class="card-body text-center">
              <div class="icon-circle bg-primary mb-3">
                <i class="fas fa-box text-white"></i>
              </div>
              <h5 class="card-title">Meus Produtos</h5>
              <p class="card-text text-muted">
                Gerencie seu catálogo de produtos
              </p>
              <button class="btn btn-primary" routerLink="/vendedor/produtos">
                <i class="fas fa-plus me-2"></i>
                Gerenciar
              </button>
            </div>
          </div>
        </div>

        <!-- Card de Lotes -->
        <div class="col-md-6 col-lg-3 mb-4">
          <div class="card h-100">
            <div class="card-body text-center">
              <div class="icon-circle bg-success mb-3">
                <i class="fas fa-gavel text-white"></i>
              </div>
              <h5 class="card-title">Meus Lotes</h5>
              <p class="card-text text-muted">
                Crie e gerencie seus lotes
              </p>
              <button class="btn btn-success" routerLink="/vendedor/lotes">
                <i class="fas fa-list me-2"></i>
                Ver Lotes
              </button>
            </div>
          </div>
        </div>

        <!-- Card de Contratos -->
        <div class="col-md-6 col-lg-3 mb-4">
          <div class="card h-100">
            <div class="card-body text-center">
              <div class="icon-circle bg-warning mb-3">
                <i class="fas fa-file-contract text-white"></i>
              </div>
              <h5 class="card-title">Meus Contratos</h5>
              <p class="card-text text-muted">
                Visualize seus contratos ativos
              </p>
              <button class="btn btn-warning" routerLink="/vendedor/contratos">
                <i class="fas fa-eye me-2"></i>
                Ver Contratos
              </button>
            </div>
          </div>
        </div>

        <!-- Card de Relatórios -->
        <div class="col-md-6 col-lg-3 mb-4">
          <div class="card h-100">
            <div class="card-body text-center">
              <div class="icon-circle bg-info mb-3">
                <i class="fas fa-chart-bar text-white"></i>
              </div>
              <h5 class="card-title">Relatórios</h5>
              <p class="card-text text-muted">
                Acompanhe suas vendas
              </p>
              <button class="btn btn-outline-info" disabled>
                <i class="fas fa-chart-line me-2"></i>
                Em Breve
              </button>
            </div>
          </div>
        </div>
      </div>

      <div class="row">
        <!-- Resumo Rápido -->
        <div class="col-md-8">
          <div class="card">
            <div class="card-header">
              <h5 class="card-title mb-0">
                <i class="fas fa-tachometer-alt me-2"></i>
                Resumo Rápido
              </h5>
            </div>
            <div class="card-body">
              <div class="row text-center">
                <div class="col-md-4">
                  <div class="stat-item">
                    <h3 class="text-primary">0</h3>
                    <p class="text-muted mb-0">Produtos Ativos</p>
                  </div>
                </div>
                <div class="col-md-4">
                  <div class="stat-item">
                    <h3 class="text-success">0</h3>
                    <p class="text-muted mb-0">Lotes Publicados</p>
                  </div>
                </div>
                <div class="col-md-4">
                  <div class="stat-item">
                    <h3 class="text-warning">0</h3>
                    <p class="text-muted mb-0">Contratos Ativos</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Ações Rápidas -->
        <div class="col-md-4">
          <div class="card">
            <div class="card-header">
              <h5 class="card-title mb-0">
                <i class="fas fa-bolt me-2"></i>
                Ações Rápidas
              </h5>
            </div>
            <div class="card-body">
              <div class="d-grid gap-2">
                <button class="btn btn-outline-primary" routerLink="/vendedor/produtos/novo">
                  <i class="fas fa-plus me-2"></i>
                  Novo Produto
                </button>
                <button class="btn btn-outline-success" routerLink="/vendedor/lotes/novo">
                  <i class="fas fa-gavel me-2"></i>
                  Novo Lote
                </button>
                <button class="btn btn-outline-info" routerLink="/catalogo">
                  <i class="fas fa-eye me-2"></i>
                  Ver Catálogo
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .welcome-header {
      padding: 1rem 0;
      border-bottom: 1px solid #dee2e6;
    }
    
    .card {
      border: none;
      box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
      transition: transform 0.2s ease-in-out;
    }
    
    .card:hover {
      transform: translateY(-2px);
    }
    
    .icon-circle {
      width: 60px;
      height: 60px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0 auto;
    }
    
    .icon-circle i {
      font-size: 1.5rem;
    }
    
    .card-header {
      background-color: #f8f9fa;
      border-bottom: 1px solid #dee2e6;
    }
    
    .stat-item {
      padding: 1rem 0;
    }
    
    .stat-item h3 {
      font-weight: bold;
      margin-bottom: 0.5rem;
    }
  `]
})
export class DashboardVendedorComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
    console.log('DashboardVendedorComponent inicializado');
  }
}