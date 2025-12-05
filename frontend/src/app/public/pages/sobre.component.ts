import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { PublicCatalogoService } from '../../core/services/public-catalogo.service';

/**
 * Componente da página Sobre
 * FASE 2 - Componentes Públicos: Página institucional
 * CORRIGIDO: HTML e CSS separados em arquivos
 */
@Component({
  selector: 'app-sobre',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sobre.component.html',
  styleUrls: ['./sobre.component.scss']
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