import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute } from '@angular/router';

/**
 * Detalhes do Lote (Público)
 * CORRIGIDO: HTML e CSS separados em arquivos
 */
@Component({
  selector: 'app-lote-detalhe',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './lote-detalhe.component.html',
  styleUrls: ['./lote-detalhe.component.scss']
})
export class LoteDetalheComponent implements OnInit {
  loteId: string | null = null;

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.loteId = this.route.snapshot.paramMap.get('id');
    console.log('LoteDetalheComponent inicializado para lote:', this.loteId);
  }
}