import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-auction-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './auction-list.component.html',
  styleUrl: './auction-list.component.scss'
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