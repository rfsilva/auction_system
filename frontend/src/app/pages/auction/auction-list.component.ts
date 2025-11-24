import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-auction-list',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="auction-list-container">
      <div class="page-header">
        <h1>Leilões Ativos</h1>
        <p>Participe dos leilões em tempo real</p>
      </div>

      <div class="filters">
        <div class="filter-group">
          <label>Categoria:</label>
          <select class="form-control">
            <option value="">Todas as categorias</option>
            <option value="electronics">Eletrônicos</option>
            <option value="vehicles">Veículos</option>
            <option value="art">Arte</option>
            <option value="collectibles">Colecionáveis</option>
          </select>
        </div>
        <div class="filter-group">
          <label>Status:</label>
          <select class="form-control">
            <option value="active">Ativos</option>
            <option value="upcoming">Próximos</option>
            <option value="ended">Finalizados</option>
          </select>
        </div>
      </div>

      <div class="auction-grid">
        @for (auction of mockAuctions; track auction.id) {
          <div class="auction-card">
            <div class="auction-image">
              <div class="placeholder-image">📷</div>
              <div class="auction-status" [class]="auction.status">
                {{ getStatusText(auction.status) }}
              </div>
            </div>
            <div class="auction-content">
              <h3 class="auction-title">{{ auction.title }}</h3>
              <p class="auction-description">{{ auction.description }}</p>
              
              <div class="auction-info">
                <div class="price-info">
                  <span class="current-price">R$ {{ auction.currentPrice | number:'1.2-2' }}</span>
                  <span class="bid-count">{{ auction.bidCount }} lances</span>
                </div>
                <div class="time-info">
                  <span class="time-left">{{ auction.timeLeft }}</span>
                </div>
              </div>

              <div class="auction-actions">
                <button class="btn btn-primary" [disabled]="auction.status !== 'active'">
                  @if (auction.status === 'active') {
                    Dar Lance
                  } @else if (auction.status === 'upcoming') {
                    Em Breve
                  } @else {
                    Finalizado
                  }
                </button>
                <button class="btn btn-outline">Ver Detalhes</button>
              </div>
            </div>
          </div>
        }
      </div>

      @if (mockAuctions.length === 0) {
        <div class="empty-state">
          <div class="empty-icon">🔨</div>
          <h3>Nenhum leilão encontrado</h3>
          <p>Não há leilões ativos no momento. Volte em breve!</p>
        </div>
      }
    </div>
  `,
  styles: [`
    .auction-list-container {
      max-width: 1200px;
      margin: 0 auto;
    }

    .page-header {
      text-align: center;
      margin-bottom: 3rem;
    }

    .page-header h1 {
      font-size: 2.5rem;
      color: #333;
      margin-bottom: 0.5rem;
    }

    .page-header p {
      color: #666;
      font-size: 1.1rem;
    }

    .filters {
      display: flex;
      gap: 2rem;
      margin-bottom: 2rem;
      padding: 1.5rem;
      background: #f8f9fa;
      border-radius: 8px;
    }

    .filter-group {
      display: flex;
      flex-direction: column;
      gap: 0.5rem;
    }

    .filter-group label {
      font-weight: 500;
      color: #333;
    }

    .form-control {
      padding: 0.5rem;
      border: 1px solid #ddd;
      border-radius: 4px;
      font-size: 0.9rem;
    }

    .auction-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
      gap: 2rem;
      margin-bottom: 3rem;
    }

    .auction-card {
      background: white;
      border-radius: 12px;
      box-shadow: 0 4px 6px rgba(0,0,0,0.1);
      overflow: hidden;
      transition: transform 0.2s, box-shadow 0.2s;
    }

    .auction-card:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 25px rgba(0,0,0,0.15);
    }

    .auction-image {
      position: relative;
      height: 200px;
      background: linear-gradient(135deg, #667eea, #764ba2);
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .placeholder-image {
      font-size: 3rem;
      color: white;
    }

    .auction-status {
      position: absolute;
      top: 1rem;
      right: 1rem;
      padding: 0.25rem 0.75rem;
      border-radius: 20px;
      font-size: 0.8rem;
      font-weight: 600;
      text-transform: uppercase;
    }

    .auction-status.active {
      background: #27ae60;
      color: white;
    }

    .auction-status.upcoming {
      background: #f39c12;
      color: white;
    }

    .auction-status.ended {
      background: #95a5a6;
      color: white;
    }

    .auction-content {
      padding: 1.5rem;
    }

    .auction-title {
      font-size: 1.3rem;
      color: #333;
      margin-bottom: 0.5rem;
      font-weight: 600;
    }

    .auction-description {
      color: #666;
      margin-bottom: 1rem;
      line-height: 1.5;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }

    .auction-info {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 1.5rem;
      padding: 1rem;
      background: #f8f9fa;
      border-radius: 8px;
    }

    .price-info {
      display: flex;
      flex-direction: column;
      gap: 0.25rem;
    }

    .current-price {
      font-size: 1.5rem;
      font-weight: bold;
      color: #e74c3c;
    }

    .bid-count {
      font-size: 0.9rem;
      color: #666;
    }

    .time-info {
      text-align: right;
    }

    .time-left {
      font-size: 0.9rem;
      color: #e74c3c;
      font-weight: 500;
    }

    .auction-actions {
      display: flex;
      gap: 1rem;
    }

    .btn {
      padding: 0.75rem 1.5rem;
      border: none;
      border-radius: 6px;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.2s;
      font-size: 0.9rem;
      flex: 1;
    }

    .btn-primary {
      background: #e74c3c;
      color: white;
    }

    .btn-primary:hover:not(:disabled) {
      background: #c0392b;
    }

    .btn-primary:disabled {
      background: #bdc3c7;
      cursor: not-allowed;
    }

    .btn-outline {
      background: transparent;
      color: #666;
      border: 1px solid #ddd;
    }

    .btn-outline:hover {
      background: #f8f9fa;
    }

    .empty-state {
      text-align: center;
      padding: 4rem 2rem;
    }

    .empty-icon {
      font-size: 4rem;
      margin-bottom: 1rem;
    }

    .empty-state h3 {
      color: #333;
      margin-bottom: 1rem;
    }

    .empty-state p {
      color: #666;
    }

    @media (max-width: 768px) {
      .filters {
        flex-direction: column;
        gap: 1rem;
      }

      .auction-grid {
        grid-template-columns: 1fr;
      }

      .auction-actions {
        flex-direction: column;
      }
    }
  `]
})
export class AuctionListComponent {
  mockAuctions = [
    {
      id: 1,
      title: 'iPhone 15 Pro Max 256GB',
      description: 'Smartphone Apple iPhone 15 Pro Max com 256GB de armazenamento, cor Titânio Natural, em perfeito estado.',
      currentPrice: 4500.00,
      bidCount: 23,
      timeLeft: '2h 15min',
      status: 'active'
    },
    {
      id: 2,
      title: 'MacBook Air M2 2023',
      description: 'Notebook Apple MacBook Air com chip M2, 8GB RAM, 256GB SSD, tela de 13 polegadas.',
      currentPrice: 7200.00,
      bidCount: 45,
      timeLeft: '5h 30min',
      status: 'active'
    },
    {
      id: 3,
      title: 'Honda Civic 2020',
      description: 'Honda Civic Sedan 2020, motor 2.0, automático, cor prata, 45.000km rodados.',
      currentPrice: 85000.00,
      bidCount: 12,
      timeLeft: '1 dia 3h',
      status: 'active'
    },
    {
      id: 4,
      title: 'Quadro Vintage Abstrato',
      description: 'Quadro abstrato vintage, óleo sobre tela, 80x60cm, assinado pelo artista.',
      currentPrice: 1200.00,
      bidCount: 8,
      timeLeft: 'Inicia em 2h',
      status: 'upcoming'
    },
    {
      id: 5,
      title: 'Relógio Rolex Submariner',
      description: 'Relógio Rolex Submariner, modelo clássico, aço inoxidável, com certificado de autenticidade.',
      currentPrice: 45000.00,
      bidCount: 67,
      timeLeft: 'Finalizado',
      status: 'ended'
    }
  ];

  getStatusText(status: string): string {
    switch (status) {
      case 'active': return 'Ativo';
      case 'upcoming': return 'Em Breve';
      case 'ended': return 'Finalizado';
      default: return status;
    }
  }
}