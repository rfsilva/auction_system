import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PublicCatalogoService } from '../../core/services/public-catalogo.service';

/**
 * Componente da página Contato
 * FASE 2 - Componentes Públicos: Página institucional
 * CORRIGIDO: HTML e CSS separados em arquivos
 */
@Component({
  selector: 'app-contato',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './contato.component.html',
  styleUrls: ['./contato.component.scss']
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