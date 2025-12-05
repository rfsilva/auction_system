import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-vendedor-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="container-fluid">
      <div class="row">
        <div class="col-12">
          <div class="card">
            <div class="card-header">
              <h4 class="card-title mb-0">
                <i class="fas fa-store me-2"></i>
                Gestão de Vendedores
              </h4>
            </div>
            <div class="card-body">
              <div class="alert alert-info">
                <i class="fas fa-info-circle me-2"></i>
                Componente de gestão de vendedores em desenvolvimento.
              </div>
              
              <div class="text-center py-5">
                <i class="fas fa-tools fa-3x text-muted mb-3"></i>
                <h5 class="text-muted">Em Construção</h5>
                <p class="text-muted">
                  Este componente será implementado nas próximas iterações.
                </p>
                <button 
                  type="button" 
                  class="btn btn-primary"
                  routerLink="/admin"
                >
                  <i class="fas fa-arrow-left me-2"></i>
                  Voltar ao Dashboard
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .card {
      border: none;
      box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
    }
    
    .card-header {
      background-color: #f8f9fa;
      border-bottom: 1px solid #dee2e6;
    }
    
    .fa-tools {
      opacity: 0.3;
    }
  `]
})
export class VendedorListComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
    console.log('VendedorListComponent inicializado');
  }
}