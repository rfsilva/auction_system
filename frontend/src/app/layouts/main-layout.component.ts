import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, Router } from '@angular/router';
import { AuthService } from '../core/services/auth.service';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink],
  template: `
    <div class="main-layout">
      <!-- Header -->
      <header class="header">
        <nav class="navbar">
          <div class="nav-brand">
            <a routerLink="/" class="brand-link">
              <h1>🔨 Leilão Online</h1>
            </a>
          </div>
          
          <div class="nav-menu">
            <a routerLink="/" class="nav-link">Home</a>
            <a routerLink="/auctions" class="nav-link">Leilões</a>
            
            @if (authService.isAuthenticated()) {
              <a routerLink="/profile" class="nav-link">Perfil</a>
              <button (click)="logout()" class="btn btn-outline">Sair</button>
            } @else {
              <a routerLink="/auth/login" class="nav-link">Login</a>
              <a routerLink="/auth/register" class="btn btn-primary">Cadastrar</a>
            }
          </div>
        </nav>
      </header>

      <!-- Main Content -->
      <main class="main-content">
        <router-outlet></router-outlet>
      </main>

      <!-- Footer -->
      <footer class="footer">
        <div class="footer-content">
          <p>&copy; 2024 Sistema de Leilão Eletrônico. Todos os direitos reservados.</p>
          <div class="footer-links">
            <a href="#" class="footer-link">Termos de Uso</a>
            <a href="#" class="footer-link">Privacidade</a>
            <a href="#" class="footer-link">Suporte</a>
          </div>
        </div>
      </footer>
    </div>
  `,
  styles: [`
    .main-layout {
      min-height: 100vh;
      display: flex;
      flex-direction: column;
    }

    .header {
      background: #fff;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
      position: sticky;
      top: 0;
      z-index: 100;
    }

    .navbar {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 1rem 2rem;
      max-width: 1200px;
      margin: 0 auto;
    }

    .nav-brand .brand-link {
      text-decoration: none;
      color: #333;
    }

    .nav-brand h1 {
      margin: 0;
      font-size: 1.5rem;
      color: #e74c3c;
    }

    .nav-menu {
      display: flex;
      align-items: center;
      gap: 1rem;
    }

    .nav-link {
      text-decoration: none;
      color: #666;
      font-weight: 500;
      padding: 0.5rem 1rem;
      border-radius: 4px;
      transition: color 0.2s;
    }

    .nav-link:hover {
      color: #e74c3c;
      background: #f8f9fa;
    }

    .btn {
      padding: 0.5rem 1rem;
      border: none;
      border-radius: 4px;
      font-weight: 500;
      text-decoration: none;
      cursor: pointer;
      transition: all 0.2s;
      display: inline-block;
    }

    .btn-primary {
      background: #e74c3c;
      color: white;
    }

    .btn-primary:hover {
      background: #c0392b;
    }

    .btn-outline {
      background: transparent;
      color: #666;
      border: 1px solid #ddd;
    }

    .btn-outline:hover {
      background: #f8f9fa;
    }

    .main-content {
      flex: 1;
      padding: 2rem;
      max-width: 1200px;
      margin: 0 auto;
      width: 100%;
    }

    .footer {
      background: #333;
      color: white;
      padding: 2rem;
      margin-top: auto;
    }

    .footer-content {
      max-width: 1200px;
      margin: 0 auto;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .footer-links {
      display: flex;
      gap: 1rem;
    }

    .footer-link {
      color: #ccc;
      text-decoration: none;
    }

    .footer-link:hover {
      color: white;
    }

    @media (max-width: 768px) {
      .navbar {
        flex-direction: column;
        gap: 1rem;
      }

      .nav-menu {
        flex-wrap: wrap;
        justify-content: center;
      }

      .footer-content {
        flex-direction: column;
        gap: 1rem;
        text-align: center;
      }

      .main-content {
        padding: 1rem;
      }
    }
  `]
})
export class MainLayoutComponent {
  constructor(
    public authService: AuthService,
    private router: Router
  ) {}

  logout(): void {
    this.authService.logout().subscribe({
      next: () => {
        this.router.navigate(['/']);
      },
      error: (error) => {
        console.error('Erro ao fazer logout:', error);
        // Mesmo com erro, limpar dados locais
        this.router.navigate(['/']);
      }
    });
  }
}