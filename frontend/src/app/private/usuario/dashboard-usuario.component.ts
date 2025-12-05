import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

/**
 * Dashboard do Usuário
 * CORRIGIDO: HTML e CSS separados em arquivos
 */
@Component({
  selector: 'app-dashboard-usuario',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard-usuario.component.html',
  styleUrls: ['./dashboard-usuario.component.scss']
})
export class DashboardUsuarioComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
    console.log('DashboardUsuarioComponent inicializado');
  }
}