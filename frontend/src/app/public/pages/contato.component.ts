import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PublicCatalogoService } from '../../core/services/public-catalogo.service';

/**
 * Componente da página Contato
 * FASE 2 - Componentes Públicos: Página institucional
 * CORRIGIDO: Problemas de tipagem com index signatures
 */
@Component({
  selector: 'app-contato',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="contato-container">
      <div class="hero-section">
        <div class="hero-content">
          <h1>Entre em Contato</h1>
          <p class="hero-subtitle">
            Estamos aqui para ajudar. Entre em contato conosco através dos canais abaixo.
          </p>
        </div>
      </div>

      <div class="content-section">
        <div class="container">
          <div class="contato-grid">
            <!-- Informações de contato -->
            <div class="contato-info">
              <h2>Informações de Contato</h2>
              
              <div class="info-item" *ngIf="getContatoField('email')">
                <div class="info-icon">
                  <i class="icon-mail"></i>
                </div>
                <div class="info-content">
                  <h4>Email</h4>
                  <p>{{ getContatoField('email') }}</p>
                </div>
              </div>

              <div class="info-item" *ngIf="getContatoField('telefone')">
                <div class="info-icon">
                  <i class="icon-phone"></i>
                </div>
                <div class="info-content">
                  <h4>Telefone</h4>
                  <p>{{ getContatoField('telefone') }}</p>
                </div>
              </div>

              <div class="info-item" *ngIf="getContatoField('whatsapp')">
                <div class="info-icon">
                  <i class="icon-message-circle"></i>
                </div>
                <div class="info-content">
                  <h4>WhatsApp</h4>
                  <p>{{ getContatoField('whatsapp') }}</p>
                </div>
              </div>

              <div class="info-item" *ngIf="getContatoField('endereco')">
                <div class="info-icon">
                  <i class="icon-map-pin"></i>
                </div>
                <div class="info-content">
                  <h4>Endereço</h4>
                  <p>{{ getContatoField('endereco') }}</p>
                  <p *ngIf="getContatoField('cep')">CEP: {{ getContatoField('cep') }}</p>
                </div>
              </div>

              <div class="info-item" *ngIf="getContatoField('horarioAtendimento')">
                <div class="info-icon">
                  <i class="icon-clock"></i>
                </div>
                <div class="info-content">
                  <h4>Horário de Atendimento</h4>
                  <p>{{ getContatoField('horarioAtendimento') }}</p>
                </div>
              </div>

              <!-- Contatos específicos -->
              <div class="contatos-especificos" *ngIf="getContatoField('suporte') || getContatoField('comercial')">
                <h3>Contatos Específicos</h3>
                
                <div class="contato-especifico" *ngIf="getContatoField('suporte')">
                  <strong>Suporte Técnico:</strong>
                  <span>{{ getContatoField('suporte') }}</span>
                </div>
                
                <div class="contato-especifico" *ngIf="getContatoField('comercial')">
                  <strong>Comercial:</strong>
                  <span>{{ getContatoField('comercial') }}</span>
                </div>
              </div>
            </div>

            <!-- Formulário de contato -->
            <div class="contato-form">
              <h2>Envie uma Mensagem</h2>
              
              <form (ngSubmit)="enviarMensagem()" #contatoForm="ngForm">
                <div class="form-group">
                  <label for="nome">Nome *</label>
                  <input
                    type="text"
                    id="nome"
                    name="nome"
                    [(ngModel)]="formulario.nome"
                    required
                    class="form-control"
                    placeholder="Seu nome completo">
                </div>

                <div class="form-group">
                  <label for="email">Email *</label>
                  <input
                    type="email"
                    id="email"
                    name="email"
                    [(ngModel)]="formulario.email"
                    required
                    class="form-control"
                    placeholder="seu@email.com">
                </div>

                <div class="form-group">
                  <label for="telefone">Telefone</label>
                  <input
                    type="tel"
                    id="telefone"
                    name="telefone"
                    [(ngModel)]="formulario.telefone"
                    class="form-control"
                    placeholder="(11) 99999-9999">
                </div>

                <div class="form-group">
                  <label for="assunto">Assunto *</label>
                  <select
                    id="assunto"
                    name="assunto"
                    [(ngModel)]="formulario.assunto"
                    required
                    class="form-control">
                    <option value="">Selecione um assunto</option>
                    <option value="suporte">Suporte Técnico</option>
                    <option value="comercial">Informações Comerciais</option>
                    <option value="vendedor">Quero ser Vendedor</option>
                    <option value="duvida">Dúvida Geral</option>
                    <option value="sugestao">Sugestão</option>
                    <option value="reclamacao">Reclamação</option>
                  </select>
                </div>

                <div class="form-group">
                  <label for="mensagem">Mensagem *</label>
                  <textarea
                    id="mensagem"
                    name="mensagem"
                    [(ngModel)]="formulario.mensagem"
                    required
                    rows="5"
                    class="form-control"
                    placeholder="Descreva sua mensagem..."></textarea>
                </div>

                <div class="form-actions">
                  <button
                    type="submit"
                    [disabled]="!contatoForm.valid || enviando"
                    class="btn btn-primary">
                    <span *ngIf="enviando">Enviando...</span>
                    <span *ngIf="!enviando">Enviar Mensagem</span>
                  </button>
                </div>

                <!-- Mensagem de sucesso/erro -->
                <div *ngIf="mensagemEnvio" class="alert" [class.alert-success]="envioSucesso" [class.alert-error]="!envioSucesso">
                  {{ mensagemEnvio }}
                </div>
              </form>
            </div>
          </div>

          <!-- FAQ rápido -->
          <div class="faq-section">
            <h2>Perguntas Frequentes</h2>
            <div class="faq-grid">
              <div class="faq-item">
                <h4>Como posso me cadastrar como vendedor?</h4>
                <p>Acesse a página de cadastro e selecione a opção "Quero Vender". Nossa equipe entrará em contato para ativar sua conta.</p>
              </div>
              <div class="faq-item">
                <h4>Como funciona o processo de leilão?</h4>
                <p>Os lotes ficam disponíveis por um período determinado. Os interessados fazem lances e o maior lance ao final do período vence.</p>
              </div>
              <div class="faq-item">
                <h4>Qual a taxa cobrada?</h4>
                <p>As taxas variam conforme o tipo de produto e contrato. Entre em contato para mais informações.</p>
              </div>
              <div class="faq-item">
                <h4>Como recebo o pagamento?</h4>
                <p>O pagamento é processado após a confirmação da venda e transferido conforme o prazo estabelecido no contrato.</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .contato-container {
      min-height: 100vh;
    }

    .hero-section {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      padding: 3rem 2rem;
      text-align: center;
    }

    .hero-content h1 {
      font-size: 2.5rem;
      margin-bottom: 1rem;
      font-weight: 700;
    }

    .hero-subtitle {
      font-size: 1.1rem;
      opacity: 0.9;
      max-width: 600px;
      margin: 0 auto;
    }

    .content-section {
      padding: 3rem 2rem;
    }

    .container {
      max-width: 1200px;
      margin: 0 auto;
    }

    .contato-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 3rem;
      margin-bottom: 4rem;
    }

    .contato-info h2,
    .contato-form h2 {
      margin-bottom: 2rem;
      color: #333;
    }

    .info-item {
      display: flex;
      align-items: flex-start;
      margin-bottom: 2rem;
      padding: 1rem;
      background: #f8f9fa;
      border-radius: 8px;
    }

    .info-icon {
      width: 40px;
      height: 40px;
      background: #667eea;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      color: white;
      margin-right: 1rem;
      flex-shrink: 0;
    }

    .info-content h4 {
      margin: 0 0 0.5rem 0;
      color: #333;
    }

    .info-content p {
      margin: 0;
      color: #666;
    }

    .contatos-especificos {
      margin-top: 2rem;
      padding: 1rem;
      background: #e9ecef;
      border-radius: 8px;
    }

    .contatos-especificos h3 {
      margin-bottom: 1rem;
      color: #333;
    }

    .contato-especifico {
      display: flex;
      justify-content: space-between;
      margin-bottom: 0.5rem;
    }

    .contato-especifico strong {
      color: #333;
    }

    .contato-especifico span {
      color: #666;
    }

    .form-group {
      margin-bottom: 1.5rem;
    }

    .form-group label {
      display: block;
      margin-bottom: 0.5rem;
      font-weight: 600;
      color: #333;
    }

    .form-control {
      width: 100%;
      padding: 0.75rem;
      border: 2px solid #e9ecef;
      border-radius: 8px;
      font-size: 1rem;
      transition: border-color 0.3s ease;
    }

    .form-control:focus {
      outline: none;
      border-color: #667eea;
    }

    .form-actions {
      margin-top: 2rem;
    }

    .btn {
      padding: 0.75rem 2rem;
      border: none;
      border-radius: 8px;
      font-size: 1rem;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.3s ease;
    }

    .btn-primary {
      background: #667eea;
      color: white;
    }

    .btn-primary:hover:not(:disabled) {
      background: #5a6fd8;
    }

    .btn:disabled {
      opacity: 0.6;
      cursor: not-allowed;
    }

    .alert {
      margin-top: 1rem;
      padding: 1rem;
      border-radius: 8px;
      font-weight: 500;
    }

    .alert-success {
      background: #d4edda;
      color: #155724;
      border: 1px solid #c3e6cb;
    }

    .alert-error {
      background: #f8d7da;
      color: #721c24;
      border: 1px solid #f5c6cb;
    }

    .faq-section {
      margin-top: 4rem;
    }

    .faq-section h2 {
      text-align: center;
      margin-bottom: 2rem;
      color: #333;
    }

    .faq-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
      gap: 2rem;
    }

    .faq-item {
      padding: 1.5rem;
      background: #f8f9fa;
      border-radius: 8px;
    }

    .faq-item h4 {
      margin-bottom: 1rem;
      color: #333;
    }

    .faq-item p {
      color: #666;
      line-height: 1.6;
      margin: 0;
    }

    @media (max-width: 768px) {
      .contato-grid {
        grid-template-columns: 1fr;
        gap: 2rem;
      }

      .hero-content h1 {
        font-size: 2rem;
      }

      .faq-grid {
        grid-template-columns: 1fr;
      }
    }
  `]
})
export class ContatoComponent implements OnInit {
  contato: {[key: string]: string} | null = null;
  
  formulario = {
    nome: '',
    email: '',
    telefone: '',
    assunto: '',
    mensagem: ''
  };

  enviando = false;
  mensagemEnvio = '';
  envioSucesso = false;

  constructor(private publicCatalogoService: PublicCatalogoService) {}

  ngOnInit(): void {
    this.carregarInformacoesContato();
  }

  // CORRIGIDO: Método helper para acessar campos do objeto contato
  getContatoField(field: string): string | null {
    return this.contato ? this.contato[field] || null : null;
  }

  private carregarInformacoesContato(): void {
    this.publicCatalogoService.obterInformacoesContato()
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.contato = response.data;
          }
        },
        error: (error) => {
          console.error('Erro ao carregar informações de contato:', error);
          // Fallback com dados padrão
          this.contato = {
            email: 'contato@leilao.com',
            telefone: '(11) 3000-0000',
            whatsapp: '(11) 99999-9999',
            endereco: 'São Paulo, SP',
            horarioAtendimento: 'Segunda a Sexta, 9h às 18h',
            suporte: 'suporte@leilao.com',
            comercial: 'comercial@leilao.com'
          };
        }
      });
  }

  enviarMensagem(): void {
    this.enviando = true;
    this.mensagemEnvio = '';

    // Simular envio (em produção, seria uma chamada para o backend)
    setTimeout(() => {
      this.enviando = false;
      this.envioSucesso = true;
      this.mensagemEnvio = 'Mensagem enviada com sucesso! Entraremos em contato em breve.';
      
      // Limpar formulário
      this.formulario = {
        nome: '',
        email: '',
        telefone: '',
        assunto: '',
        mensagem: ''
      };
    }, 2000);
  }
}