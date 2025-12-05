import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

/**
 * Lista de Vendedores (Admin)
 * CORRIGIDO: HTML e CSS separados em arquivos
 */
@Component({
  selector: 'app-vendedor-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './vendedor-list.component.html',
  styleUrls: ['./vendedor-list.component.scss']
})
export class VendedorListComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
    console.log('VendedorListComponent inicializado');
  }
}