import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { PublicCatalogoService } from '../../core/services/public-catalogo.service';

/**
 * Componente da página Sobre
 * FASE 2 - Componentes Públicos: Página institucional
 * CORRIGIDO: Problemas de tipagem com index signatures
 */
@Component({
  selector: 'app-sobre',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="sobre-container">
      <div class="hero-section">
        <div class="hero-content">
          <h1>{{ getInformacaoField('empresa') || 'Sistema de Leilão Online' }}</h1>
          <p class="hero-subtitle">
            {{ getInformacaoField('descricao') || 'Plataforma moderna para leilões online com segurança e transparência' }}
          </p>
        </div>
      </div>

      <div class="content-section">
        <div class="container">
          <div class="info-grid">
            <div class="info-card">
              <div class="info-icon">
                <i class="icon-target"></i>
              </div>
              <h3>Nossa Missão</h3>
              <p>{{ getInformacaoField('missao') || 'Conectar vendedores e compradores através de leilões justos e transparentes' }}</p>
            </div>

            <div class="info-card">
              <div class="info-icon">
                <i class="icon-eye"></i>
              </div>
              <h3>Nossa Visão</h3>
              <p>{{ getInformacaoField('visao') || 'Ser a principal plataforma de leilões online do mercado' }}</p>
            </div>

            <div class="info-card">
              <div class="info-icon">
                <i class="icon-heart"></i>
              </div>
              <h3>Nossos Valores</h3>
              <p>{{ getInformacaoField('valores') || 'Transparência, Segurança, Inovação e Excelência no Atendimento' }}</p>
            </div>
          </div>

          <div class="details-section">
            <div class="details-grid">
              <div class="detail-item">
                <strong>Fundação:</strong>
                <span>{{ getInformacaoField('fundacao') || '2024' }}</span>
              </div>
              <div class="detail-item">
                <strong>Sede:</strong>
                <span>{{ getInformacaoField('sede') || 'Brasil' }}</span>
              </div>
            </div>
          </div>

          <div class="cta-section">
            <h2>Pronto para começar?</h2>
            <p>Explore nosso catálogo de lotes ou cadastre-se para vender seus produtos.</p>
            <div class="cta-buttons">
              <a routerLink="/catalogo" class="btn btn-primary">
                Ver Catálogo
              </a>
              <a routerLink="/auth/register" class="btn btn-outline">
                Cadastrar-se
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .sobre-container {
      min-height: 100vh;
    }

    .hero-section {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      padding: 4rem 2rem;
      text-align: center;
    }

    .hero-content h1 {
      font-size: 3rem;
      margin-bottom: 1rem;
      font-weight: 700;
    }

    .hero-subtitle {
      font-size: 1.25rem;
      opacity: 0.9;
      max-width: 600px;
      margin: 0 auto;
    }

    .content-section {
      padding: 4rem 2rem;
    }

    .container {
      max-width: 1200px;
      margin: 0 auto;
    }

    .info-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
      gap: 2rem;
      margin-bottom: 4rem;
    }

    .info-card {
      text-align: center;
      padding: 2rem;
      border-radius: 12px;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
      background: white;
    }

    .info-icon {
      width: 60px;
      height: 60px;
      background: #667eea;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0 auto 1rem;
      color: white;
      font-size: 1.5rem;
    }

    .info-card h3 {
      margin-bottom: 1rem;
      color: #333;
    }

    .info-card p {
      color: #666;
      line-height: 1.6;
    }

    .details-section {
      background: #f8f9fa;
      padding: 2rem;
      border-radius: 12px;
      margin-bottom: 4rem;
    }

    .details-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 1rem;
    }

    .detail-item {
      display: flex;
      justify-content: space-between;
      padding: 0.5rem 0;
      border-bottom: 1px solid #e9ecef;
    }

    .detail-item strong {
      color: #333;
    }

    .detail-item span {
      color: #666;
    }

    .cta-section {
      text-align: center;
      padding: 3rem 2rem;
      background: #f8f9fa;
      border-radius: 12px;
    }

    .cta-section h2 {
      margin-bottom: 1rem;
      color: #333;
    }

    .cta-section p {
      margin-bottom: 2rem;
      color: #666;
      font-size: 1.1rem;
    }

    .cta-buttons {
      display: flex;
      gap: 1rem;
      justify-content: center;
      flex-wrap: wrap;
    }

    .btn {
      padding: 0.75rem 2rem;
      border-radius: 8px;
      text-decoration: none;
      font-weight: 600;
      transition: all 0.3s ease;
    }

    .btn-primary {
      background: #667eea;
      color: white;
      border: 2px solid #667eea;
    }

    .btn-primary:hover {
      background: #5a6fd8;
      border-color: #5a6fd8;
    }

    .btn-outline {
      background: transparent;
      color: #667eea;
      border: 2px solid #667eea;
    }

    .btn-outline:hover {
      background: #667eea;
      color: white;
    }

    @media (max-width: 768px) {
      .hero-content h1 {
        font-size: 2rem;
      }

      .hero-subtitle {
        font-size: 1.1rem;
      }

      .info-grid {
        grid-template-columns: 1fr;
      }

      .cta-buttons {
        flex-direction: column;
        align-items: center;
      }

      .btn {
        width: 200px;
      }
    }
  `]
})
export class SobreComponent implements OnInit {
  informacoes: {[key: string]: string} | null = null;

  constructor(private publicCatalogoService: PublicCatalogoService) {}

  ngOnInit(): void {
    this.carregarInformacoes();
  }

  // CORRIGIDO: Método helper para acessar campos do objeto informacoes
  getInformacaoField(field: string): string | null {
    return this.informacoes ? this.informacoes[field] || null : null;
  }

  private carregarInformacoes(): void {
    this.publicCatalogoService.obterInformacoesInstitucionais()
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.informacoes = response.data;
          }
        },
        error: (error) => {
          console.error('Erro ao carregar informações institucionais:', error);
          // Fallback com dados padrão
          this.informacoes = {
            empresa: 'Sistema de Leilão Online',
            descricao: 'Plataforma moderna para leilões online com segurança e transparência',
            missao: 'Conectar vendedores e compradores através de leilões justos e transparentes',
            visao: 'Ser a principal plataforma de leilões online do mercado',
            valores: 'Transparência, Segurança, Inovação e Excelência no Atendimento',
            fundacao: '2024',
            sede: 'Brasil'
          };
        }
      });
  }
}