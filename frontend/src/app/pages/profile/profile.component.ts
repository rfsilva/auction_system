import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="profile-container">
      <div class="profile-header">
        <h1>Meu Perfil</h1>
        <p>Gerencie suas informações e atividades</p>
      </div>

      <div class="profile-content">
        <div class="profile-sidebar">
          <div class="profile-card">
            <div class="profile-avatar">
              <div class="avatar-placeholder">
                {{ getInitials() }}
              </div>
            </div>
            <div class="profile-info">
              <h3>{{ currentUser()?.name || 'Usuário' }}</h3>
              <p>{{ currentUser()?.email || 'email@exemplo.com' }}</p>
              <span class="profile-role">{{ getRoleText() }}</span>
            </div>
          </div>

          <nav class="profile-nav">
            <a href="#" class="nav-item active">
              <span class="nav-icon">👤</span>
              Informações Pessoais
            </a>
            <a href="#" class="nav-item">
              <span class="nav-icon">🔨</span>
              Meus Lances
            </a>
            <a href="#" class="nav-item">
              <span class="nav-icon">📦</span>
              Meus Leilões
            </a>
            <a href="#" class="nav-item">
              <span class="nav-icon">💳</span>
              Pagamentos
            </a>
            <a href="#" class="nav-item">
              <span class="nav-icon">⚙️</span>
              Configurações
            </a>
          </nav>
        </div>

        <div class="profile-main">
          <div class="section">
            <h2>Informações Pessoais</h2>
            <div class="info-grid">
              <div class="info-item">
                <label>Nome Completo</label>
                <div class="info-value">{{ currentUser()?.name || 'Não informado' }}</div>
              </div>
              <div class="info-item">
                <label>E-mail</label>
                <div class="info-value">{{ currentUser()?.email || 'Não informado' }}</div>
              </div>
              <div class="info-item">
                <label>Telefone</label>
                <div class="info-value">Não informado</div>
              </div>
              <div class="info-item">
                <label>Tipo de Conta</label>
                <div class="info-value">{{ getRoleText() }}</div>
              </div>
            </div>
            <button class="btn btn-primary">Editar Informações</button>
          </div>

          <div class="section">
            <h2>Estatísticas</h2>
            <div class="stats-grid">
              <div class="stat-card">
                <div class="stat-number">12</div>
                <div class="stat-label">Lances Dados</div>
              </div>
              <div class="stat-card">
                <div class="stat-number">3</div>
                <div class="stat-label">Leilões Ganhos</div>
              </div>
              <div class="stat-card">
                <div class="stat-number">R$ 15.500</div>
                <div class="stat-label">Total Gasto</div>
              </div>
              <div class="stat-card">
                <div class="stat-number">98%</div>
                <div class="stat-label">Taxa de Sucesso</div>
              </div>
            </div>
          </div>

          <div class="section">
            <h2>Atividade Recente</h2>
            <div class="activity-list">
              <div class="activity-item">
                <div class="activity-icon">🔨</div>
                <div class="activity-content">
                  <div class="activity-title">Lance de R$ 4.500 no iPhone 15 Pro Max</div>
                  <div class="activity-time">Há 2 horas</div>
                </div>
                <div class="activity-status success">Vencendo</div>
              </div>
              <div class="activity-item">
                <div class="activity-icon">📦</div>
                <div class="activity-content">
                  <div class="activity-title">Leilão finalizado - MacBook Air M2</div>
                  <div class="activity-time">Ontem</div>
                </div>
                <div class="activity-status neutral">Perdeu</div>
              </div>
              <div class="activity-item">
                <div class="activity-icon">💳</div>
                <div class="activity-content">
                  <div class="activity-title">Pagamento processado - R$ 2.300</div>
                  <div class="activity-time">3 dias atrás</div>
                </div>
                <div class="activity-status success">Concluído</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .profile-container {
      max-width: 1200px;
      margin: 0 auto;
    }

    .profile-header {
      text-align: center;
      margin-bottom: 3rem;
    }

    .profile-header h1 {
      font-size: 2.5rem;
      color: #333;
      margin-bottom: 0.5rem;
    }

    .profile-header p {
      color: #666;
      font-size: 1.1rem;
    }

    .profile-content {
      display: grid;
      grid-template-columns: 300px 1fr;
      gap: 3rem;
    }

    .profile-sidebar {
      display: flex;
      flex-direction: column;
      gap: 2rem;
    }

    .profile-card {
      background: white;
      padding: 2rem;
      border-radius: 12px;
      box-shadow: 0 4px 6px rgba(0,0,0,0.1);
      text-align: center;
    }

    .profile-avatar {
      margin-bottom: 1rem;
    }

    .avatar-placeholder {
      width: 80px;
      height: 80px;
      border-radius: 50%;
      background: linear-gradient(135deg, #667eea, #764ba2);
      display: flex;
      align-items: center;
      justify-content: center;
      color: white;
      font-size: 2rem;
      font-weight: bold;
      margin: 0 auto;
    }

    .profile-info h3 {
      color: #333;
      margin-bottom: 0.5rem;
    }

    .profile-info p {
      color: #666;
      margin-bottom: 1rem;
    }

    .profile-role {
      background: #e74c3c;
      color: white;
      padding: 0.25rem 0.75rem;
      border-radius: 20px;
      font-size: 0.8rem;
      font-weight: 600;
    }

    .profile-nav {
      background: white;
      border-radius: 12px;
      box-shadow: 0 4px 6px rgba(0,0,0,0.1);
      overflow: hidden;
    }

    .nav-item {
      display: flex;
      align-items: center;
      gap: 1rem;
      padding: 1rem 1.5rem;
      text-decoration: none;
      color: #666;
      transition: all 0.2s;
      border-bottom: 1px solid #f0f0f0;
    }

    .nav-item:last-child {
      border-bottom: none;
    }

    .nav-item:hover,
    .nav-item.active {
      background: #f8f9fa;
      color: #e74c3c;
    }

    .nav-icon {
      font-size: 1.2rem;
    }

    .profile-main {
      display: flex;
      flex-direction: column;
      gap: 2rem;
    }

    .section {
      background: white;
      padding: 2rem;
      border-radius: 12px;
      box-shadow: 0 4px 6px rgba(0,0,0,0.1);
    }

    .section h2 {
      color: #333;
      margin-bottom: 1.5rem;
      font-size: 1.5rem;
    }

    .info-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 1.5rem;
      margin-bottom: 2rem;
    }

    .info-item {
      display: flex;
      flex-direction: column;
      gap: 0.5rem;
    }

    .info-item label {
      font-weight: 500;
      color: #666;
      font-size: 0.9rem;
    }

    .info-value {
      color: #333;
      font-size: 1.1rem;
    }

    .stats-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 1.5rem;
    }

    .stat-card {
      text-align: center;
      padding: 1.5rem;
      background: #f8f9fa;
      border-radius: 8px;
    }

    .stat-number {
      font-size: 2rem;
      font-weight: bold;
      color: #e74c3c;
      margin-bottom: 0.5rem;
    }

    .stat-label {
      color: #666;
      font-size: 0.9rem;
    }

    .activity-list {
      display: flex;
      flex-direction: column;
      gap: 1rem;
    }

    .activity-item {
      display: flex;
      align-items: center;
      gap: 1rem;
      padding: 1rem;
      background: #f8f9fa;
      border-radius: 8px;
    }

    .activity-icon {
      font-size: 1.5rem;
    }

    .activity-content {
      flex: 1;
    }

    .activity-title {
      color: #333;
      font-weight: 500;
      margin-bottom: 0.25rem;
    }

    .activity-time {
      color: #666;
      font-size: 0.9rem;
    }

    .activity-status {
      padding: 0.25rem 0.75rem;
      border-radius: 20px;
      font-size: 0.8rem;
      font-weight: 600;
    }

    .activity-status.success {
      background: #d4edda;
      color: #155724;
    }

    .activity-status.neutral {
      background: #f8d7da;
      color: #721c24;
    }

    .btn {
      padding: 0.75rem 1.5rem;
      border: none;
      border-radius: 6px;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.2s;
    }

    .btn-primary {
      background: #e74c3c;
      color: white;
    }

    .btn-primary:hover {
      background: #c0392b;
    }

    @media (max-width: 768px) {
      .profile-content {
        grid-template-columns: 1fr;
        gap: 2rem;
      }

      .info-grid {
        grid-template-columns: 1fr;
      }

      .stats-grid {
        grid-template-columns: repeat(2, 1fr);
      }
    }
  `]
})
export class ProfileComponent {
  constructor(public authService: AuthService) {}

  get currentUser() {
    return this.authService.currentUser;
  }

  getInitials(): string {
    const user = this.currentUser();
    if (user?.name) {
      return user.name
        .split(' ')
        .map(n => n[0])
        .join('')
        .toUpperCase()
        .substring(0, 2);
    }
    return 'U';
  }

  getRoleText(): string {
    const user = this.currentUser();
    switch (user?.role) {
      case 'ADMIN': return 'Administrador';
      case 'SELLER': return 'Vendedor';
      case 'BUYER': return 'Comprador';
      case 'PARTICIPANT': return 'Participante';
      default: return 'Visitante';
    }
  }
}