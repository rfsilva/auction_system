import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="home-container">
      <!-- Hero Section -->
      <section class="hero">
        <div class="hero-content">
          <h1 class="hero-title">Bem-vindo ao Leilão Online</h1>
          <p class="hero-subtitle">
            Participe de leilões em tempo real e encontre produtos únicos com os melhores preços.
          </p>
          <div class="hero-actions">
            <a routerLink="/auctions" class="btn btn-primary btn-large">
              Ver Leilões Ativos
            </a>
            <a routerLink="/auth/register" class="btn btn-outline btn-large">
              Cadastre-se Grátis
            </a>
          </div>
        </div>
        <div class="hero-image">
          <div class="placeholder-image">
            🔨 Leilão em Tempo Real
          </div>
        </div>
      </section>

      <!-- Features Section -->
      <section class="features">
        <h2 class="section-title">Por que escolher nosso sistema?</h2>
        <div class="features-grid">
          <div class="feature-card">
            <div class="feature-icon">⚡</div>
            <h3>Tempo Real</h3>
            <p>Lances atualizados instantaneamente com tecnologia WebSocket</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">🔒</div>
            <h3>Seguro</h3>
            <p>Transações protegidas e dados criptografados</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">📱</div>
            <h3>Responsivo</h3>
            <p>Acesse de qualquer dispositivo, a qualquer hora</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">💰</div>
            <h3>Sem Taxas Ocultas</h3>
            <p>Transparência total nos custos e comissões</p>
          </div>
        </div>
      </section>

      <!-- Stats Section -->
      <section class="stats">
        <div class="stats-grid">
          <div class="stat-item">
            <div class="stat-number">1,234</div>
            <div class="stat-label">Leilões Realizados</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">5,678</div>
            <div class="stat-label">Usuários Ativos</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">R$ 2.5M</div>
            <div class="stat-label">Volume Negociado</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">98%</div>
            <div class="stat-label">Satisfação</div>
          </div>
        </div>
      </section>

      <!-- CTA Section -->
      <section class="cta">
        <div class="cta-content">
          <h2>Pronto para começar?</h2>
          <p>Cadastre-se agora e participe do seu primeiro leilão!</p>
          <a routerLink="/auth/register" class="btn btn-primary btn-large">
            Criar Conta Gratuita
          </a>
        </div>
      </section>
    </div>
  `,
  styles: [`
    .home-container {
      max-width: 1200px;
      margin: 0 auto;
    }

    .hero {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 3rem;
      align-items: center;
      padding: 3rem 0;
      min-height: 500px;
    }

    .hero-content {
      padding: 2rem 0;
    }

    .hero-title {
      font-size: 3rem;
      font-weight: bold;
      color: #333;
      margin-bottom: 1rem;
      line-height: 1.2;
    }

    .hero-subtitle {
      font-size: 1.2rem;
      color: #666;
      margin-bottom: 2rem;
      line-height: 1.6;
    }

    .hero-actions {
      display: flex;
      gap: 1rem;
      flex-wrap: wrap;
    }

    .hero-image {
      display: flex;
      justify-content: center;
      align-items: center;
    }

    .placeholder-image {
      width: 300px;
      height: 300px;
      background: linear-gradient(135deg, #e74c3c, #c0392b);
      border-radius: 20px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: white;
      font-size: 1.5rem;
      font-weight: bold;
      text-align: center;
      box-shadow: 0 10px 30px rgba(231, 76, 60, 0.3);
    }

    .section-title {
      text-align: center;
      font-size: 2.5rem;
      color: #333;
      margin-bottom: 3rem;
    }

    .features {
      padding: 4rem 0;
      background: #f8f9fa;
      margin: 3rem -2rem;
      padding-left: 2rem;
      padding-right: 2rem;
    }

    .features-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 2rem;
    }

    .feature-card {
      background: white;
      padding: 2rem;
      border-radius: 12px;
      text-align: center;
      box-shadow: 0 4px 6px rgba(0,0,0,0.1);
      transition: transform 0.2s;
    }

    .feature-card:hover {
      transform: translateY(-5px);
    }

    .feature-icon {
      font-size: 3rem;
      margin-bottom: 1rem;
    }

    .feature-card h3 {
      color: #333;
      margin-bottom: 1rem;
      font-size: 1.3rem;
    }

    .feature-card p {
      color: #666;
      line-height: 1.6;
    }

    .stats {
      padding: 4rem 0;
    }

    .stats-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 2rem;
    }

    .stat-item {
      text-align: center;
    }

    .stat-number {
      font-size: 3rem;
      font-weight: bold;
      color: #e74c3c;
      margin-bottom: 0.5rem;
    }

    .stat-label {
      color: #666;
      font-size: 1.1rem;
    }

    .cta {
      background: linear-gradient(135deg, #e74c3c, #c0392b);
      color: white;
      padding: 4rem 2rem;
      margin: 3rem -2rem 0;
      text-align: center;
      border-radius: 12px;
    }

    .cta-content h2 {
      font-size: 2.5rem;
      margin-bottom: 1rem;
    }

    .cta-content p {
      font-size: 1.2rem;
      margin-bottom: 2rem;
      opacity: 0.9;
    }

    .btn {
      padding: 0.75rem 1.5rem;
      border: none;
      border-radius: 6px;
      font-weight: 600;
      text-decoration: none;
      cursor: pointer;
      transition: all 0.2s;
      display: inline-block;
      font-size: 1rem;
    }

    .btn-large {
      padding: 1rem 2rem;
      font-size: 1.1rem;
    }

    .btn-primary {
      background: #e74c3c;
      color: white;
    }

    .btn-primary:hover {
      background: #c0392b;
      transform: translateY(-2px);
    }

    .btn-outline {
      background: transparent;
      color: #e74c3c;
      border: 2px solid #e74c3c;
    }

    .btn-outline:hover {
      background: #e74c3c;
      color: white;
    }

    .cta .btn-primary {
      background: white;
      color: #e74c3c;
    }

    .cta .btn-primary:hover {
      background: #f8f9fa;
    }

    @media (max-width: 768px) {
      .hero {
        grid-template-columns: 1fr;
        text-align: center;
        gap: 2rem;
      }

      .hero-title {
        font-size: 2rem;
      }

      .hero-actions {
        justify-content: center;
      }

      .placeholder-image {
        width: 250px;
        height: 250px;
        font-size: 1.2rem;
      }

      .section-title {
        font-size: 2rem;
      }

      .features {
        margin: 2rem -1rem;
        padding-left: 1rem;
        padding-right: 1rem;
      }

      .cta {
        margin: 2rem -1rem 0;
      }

      .cta-content h2 {
        font-size: 2rem;
      }
    }
  `]
})
export class HomeComponent {}