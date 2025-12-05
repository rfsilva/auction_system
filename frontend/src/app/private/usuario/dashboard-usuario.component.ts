import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-dashboard-usuario',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="container-fluid">
      <div class="row">
        <div class="col-12">
          <div class="welcome-header mb-4">
            <h2 class="mb-1">
              <i class="fas fa-user-circle me-2"></i>
              Bem-vindo ao seu Dashboard
            </h2>
            <p class="text-muted">Gerencie suas atividades e acompanhe leilões</p>
          </div>
        </div>
      </div>

      <div class="row">
        <!-- Card de Leilões Ativos -->
        <div class="col-md-6 col-lg-4 mb-4">
          <div class="card h-100">
            <div class="card-body text-center">
              <div class="icon-circle bg-primary mb-3">
                <i class="fas fa-gavel text-white"></i>
              </div>
              <h5 class="card-title">Leilões Ativos</h5>
              <p class="card-text text-muted">
                Acompanhe os leilões em andamento
              </p>
              <button class="btn btn-outline-primary" routerLink="/catalogo">
                <i class="fas fa-eye me-2"></i>
                Ver Catálogo
              </button>
            </div>
          </div>
        </div>

        <!-- Card de Favoritos -->
        <div class="col-md-6 col-lg-4 mb-4">
          <div class="card h-100">
            <div class="card-body text-center">
              <div class="icon-circle bg-warning mb-3">
                <i class="fas fa-heart text-white"></i>
              </div>
              <h5 class="card-title">Meus Favoritos</h5>
              <p class="card-text text-muted">
                Lotes que você marcou como favoritos
              </p>
              <button class="btn btn-outline-warning" disabled>
                <i class="fas fa-star me-2"></i>
                Em Breve
              </button>
            </div>
          </div>
        </div>

        <!-- Card de Histórico -->
        <div class="col-md-6 col-lg-4 mb-4">
          <div class="card h-100">
            <div class="card-body text-center">
              <div class="icon-circle bg-success mb-3">
                <i class="fas fa-history text-white"></i>
              </div>
              <h5 class="card-title">Meu Histórico</h5>
              <p class="card-text text-muted">
                Histórico de participações em leilões
              </p>
              <button class="btn btn-outline-success" disabled>
                <i class="fas fa-list me-2"></i>
                Em Breve
              </button>
            </div>
          </div>
        </div>
      </div>

      <div class="row">
        <div class="col-12">
          <div class="card">
            <div class="card-header">
              <h5 class="card-title mb-0">
                <i class="fas fa-info-circle me-2"></i>
                Informações
              </h5>
            </div>
            <div class="card-body">
              <div class="alert alert-info mb-0">
                <h6 class="alert-heading">Dashboard do Usuário</h6>
                <p class="mb-0">
                  Este é seu painel pessoal. Aqui você poderá acompanhar leilões, 
                  gerenciar favoritos e visualizar seu histórico de participações.
                </p>
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
  `]
})
export class DashboardUsuarioComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
    console.log('DashboardUsuarioComponent inicializado');
  }
}