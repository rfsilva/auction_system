import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

/**
 * Dashboard do Vendedor
 * CORRIGIDO: HTML e CSS separados em arquivos
 */
@Component({
  selector: 'app-dashboard-vendedor',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard-vendedor.component.html',
  styleUrls: ['./dashboard-vendedor.component.scss']
})
export class DashboardVendedorComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
    console.log('DashboardVendedorComponent inicializado');
  }
}