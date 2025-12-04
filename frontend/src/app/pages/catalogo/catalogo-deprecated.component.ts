import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

/**
 * Componente de aviso para catálogo depreciado
 * 
 * Exibe mensagem informativa sobre a migração para o novo sistema de lotes
 * e redireciona automaticamente após alguns segundos
 */
@Component({
  selector: 'app-catalogo-deprecated',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="deprecated-container">
      <div class="deprecated-card">
        <div class="deprecated-icon">
          ⚠️
        </div>
        
        <h2 class="deprecated-title">
          Catálogo Atualizado!
        </h2>
        
        <p class="deprecated-message">
          Melhoramos a experiência de navegação! Agora você encontra produtos organizados em <strong>lotes</strong>, 
          como em um leilão real.
        </p>
        
        <div class="deprecated-features">
          <div class="feature-item">
            <span class="feature-icon">📦</span>
            <span>Produtos organizados em lotes</span>
          </div>
          <div class="feature-item">
            <span class="feature-icon">⏰</span>
            <span>Tempo de encerramento por lote</span>
          </div>
          <div class="feature-item">
            <span class="feature-icon">🎯</span>
            <span>Experiência mais próxima de leilão real</span>
          </div>
        </div>
        
        <div class="deprecated-actions">
          <button 
            class="btn-primary" 
            (click)="irParaCatalogo()"
            [disabled]="redirecting">
            {{ redirecting ? 'Redirecionando...' : 'Ver Novo Catálogo' }}
          </button>
          
          <div class="redirect-info" *ngIf="countdown > 0">
            Redirecionamento automático em {{ countdown }}s
          </div>
        </div>
        
        <div class="deprecated-help">
          <p><strong>Precisa de ajuda?</strong></p>
          <p>O novo catálogo funciona da seguinte forma:</p>
          <ol>
            <li>Navegue pelos <strong>lotes disponíveis</strong></li>
            <li>Clique em um lote para ver os <strong>produtos que o compõem</strong></li>
            <li>Cada lote tem seu próprio <strong>tempo de encerramento</strong></li>
          </ol>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .deprecated-container {
      min-height: 80vh;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 2rem;
      background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
    }
    
    .deprecated-card {
      background: white;
      border-radius: 16px;
      padding: 3rem;
      max-width: 600px;
      text-align: center;
      box-shadow: 0 20px 40px rgba(0,0,0,0.1);
      border: 3px solid #ffd700;
    }
    
    .deprecated-icon {
      font-size: 4rem;
      margin-bottom: 1rem;
      animation: pulse 2s infinite;
    }
    
    @keyframes pulse {
      0% { transform: scale(1); }
      50% { transform: scale(1.1); }
      100% { transform: scale(1); }
    }
    
    .deprecated-title {
      color: #2c3e50;
      margin-bottom: 1rem;
      font-size: 2rem;
      font-weight: bold;
    }
    
    .deprecated-message {
      color: #5a6c7d;
      font-size: 1.1rem;
      line-height: 1.6;
      margin-bottom: 2rem;
    }
    
    .deprecated-features {
      display: flex;
      flex-direction: column;
      gap: 1rem;
      margin-bottom: 2rem;
      text-align: left;
    }
    
    .feature-item {
      display: flex;
      align-items: center;
      gap: 1rem;
      padding: 0.75rem;
      background: #f8f9fa;
      border-radius: 8px;
      border-left: 4px solid #28a745;
    }
    
    .feature-icon {
      font-size: 1.5rem;
    }
    
    .deprecated-actions {
      margin-bottom: 2rem;
    }
    
    .btn-primary {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      border: none;
      padding: 1rem 2rem;
      border-radius: 8px;
      font-size: 1.1rem;
      font-weight: bold;
      cursor: pointer;
      transition: all 0.3s ease;
      min-width: 200px;
    }
    
    .btn-primary:hover:not(:disabled) {
      transform: translateY(-2px);
      box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
    }
    
    .btn-primary:disabled {
      opacity: 0.7;
      cursor: not-allowed;
    }
    
    .redirect-info {
      margin-top: 1rem;
      color: #6c757d;
      font-style: italic;
    }
    
    .deprecated-help {
      background: #e3f2fd;
      border-radius: 8px;
      padding: 1.5rem;
      text-align: left;
      border-left: 4px solid #2196f3;
    }
    
    .deprecated-help p {
      margin-bottom: 0.5rem;
      color: #1565c0;
    }
    
    .deprecated-help ol {
      margin: 1rem 0 0 1rem;
      color: #1976d2;
    }
    
    .deprecated-help li {
      margin-bottom: 0.5rem;
    }
    
    @media (max-width: 768px) {
      .deprecated-container {
        padding: 1rem;
      }
      
      .deprecated-card {
        padding: 2rem;
      }
      
      .deprecated-title {
        font-size: 1.5rem;
      }
      
      .deprecated-features {
        text-align: center;
      }
      
      .feature-item {
        flex-direction: column;
        text-align: center;
      }
    }
  `]
})
export class CatalogoDeprecatedComponent implements OnInit {
  countdown = 10;
  redirecting = false;
  private countdownInterval?: number;

  constructor(private router: Router) {}

  ngOnInit() {
    // Iniciar countdown automático
    this.startCountdown();
  }

  ngOnDestroy() {
    if (this.countdownInterval) {
      clearInterval(this.countdownInterval);
    }
  }

  private startCountdown() {
    this.countdownInterval = window.setInterval(() => {
      this.countdown--;
      
      if (this.countdown <= 0) {
        this.irParaCatalogo();
      }
    }, 1000);
  }

  irParaCatalogo() {
    if (this.redirecting) return;
    
    this.redirecting = true;
    
    if (this.countdownInterval) {
      clearInterval(this.countdownInterval);
    }
    
    // Pequeno delay para feedback visual
    setTimeout(() => {
      this.router.navigate(['/catalogo']);
    }, 500);
  }
}