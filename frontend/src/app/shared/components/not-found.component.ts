import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

/**
 * Componente para página não encontrada (404)
 * FASE 2 - Componentes Compartilhados
 */
@Component({
  selector: 'app-not-found',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="not-found-container">
      <div class="not-found-content">
        <div class="error-code">404</div>
        <h1>Página não encontrada</h1>
        <p>
          A página que você está procurando não existe ou foi movida.
        </p>
        <div class="actions">
          <a routerLink="/" class="btn btn-primary">
            <i class="icon-home"></i>
            Voltar ao Início
          </a>
          <a routerLink="/catalogo" class="btn btn-outline">
            <i class="icon-search"></i>
            Ver Catálogo
          </a>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .not-found-container {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 2rem;
      background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
    }

    .not-found-content {
      text-align: center;
      max-width: 500px;
    }

    .error-code {
      font-size: 8rem;
      font-weight: 900;
      color: #667eea;
      line-height: 1;
      margin-bottom: 1rem;
      text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
    }

    h1 {
      font-size: 2rem;
      color: #333;
      margin-bottom: 1rem;
      font-weight: 700;
    }

    p {
      font-size: 1.1rem;
      color: #666;
      margin-bottom: 2rem;
      line-height: 1.6;
    }

    .actions {
      display: flex;
      gap: 1rem;
      justify-content: center;
      flex-wrap: wrap;
    }

    .btn {
      display: inline-flex;
      align-items: center;
      gap: 0.5rem;
      padding: 0.75rem 1.5rem;
      border-radius: 8px;
      text-decoration: none;
      font-weight: 600;
      transition: all 0.3s ease;
      border: 2px solid transparent;
    }

    .btn-primary {
      background: #667eea;
      color: white;
      border-color: #667eea;
    }

    .btn-primary:hover {
      background: #5a6fd8;
      border-color: #5a6fd8;
      transform: translateY(-2px);
    }

    .btn-outline {
      background: transparent;
      color: #667eea;
      border-color: #667eea;
    }

    .btn-outline:hover {
      background: #667eea;
      color: white;
      transform: translateY(-2px);
    }

    @media (max-width: 768px) {
      .error-code {
        font-size: 6rem;
      }

      h1 {
        font-size: 1.5rem;
      }

      .actions {
        flex-direction: column;
        align-items: center;
      }

      .btn {
        width: 200px;
        justify-content: center;
      }
    }
  `]
})
export class NotFoundComponent {}