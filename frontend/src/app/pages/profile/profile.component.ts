import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service';
import { UserRole, UserStatus } from '../../core/models/user.model';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
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

  getRoleDescription(role?: UserRole): string {
    if (role) {
      return this.authService.getRoleDisplayName(role);
    }
    
    const user = this.currentUser();
    if (user?.roles && user.roles.length > 0) {
      // Mostrar o role mais importante
      const roleHierarchy = [UserRole.ADMIN, UserRole.SELLER, UserRole.BUYER, UserRole.PARTICIPANT, UserRole.VISITOR];
      for (const hierarchyRole of roleHierarchy) {
        if (user.roles.includes(hierarchyRole)) {
          return this.authService.getRoleDisplayName(hierarchyRole);
        }
      }
    }
    return 'Visitante';
  }

  getStatusDescription(): string {
    const user = this.currentUser();
    return user?.status ? this.authService.getStatusDisplayName(user.status) : 'Desconhecido';
  }

  getRoleClass(role?: UserRole): string {
    if (role) {
      return `role-${role.toLowerCase()}`;
    }
    
    const user = this.currentUser();
    if (user?.roles && user.roles.length > 0) {
      const roleHierarchy = [UserRole.ADMIN, UserRole.SELLER, UserRole.BUYER, UserRole.PARTICIPANT, UserRole.VISITOR];
      for (const hierarchyRole of roleHierarchy) {
        if (user.roles.includes(hierarchyRole)) {
          return `role-${hierarchyRole.toLowerCase()}`;
        }
      }
    }
    return 'role-visitor';
  }

  getStatusClass(): string {
    const user = this.currentUser();
    if (user?.status) {
      return `status-${user.status.toLowerCase().replace('_', '-')}`;
    }
    return 'status-unknown';
  }

  isVisitorOnly(): boolean {
    const user = this.currentUser();
    return user?.roles?.length === 1 && user.roles.includes(UserRole.VISITOR);
  }

  getRolePermissions(role: UserRole): string[] {
    const permissions: Record<UserRole, string[]> = {
      [UserRole.VISITOR]: ['Visualizar leilões', 'Criar conta'],
      [UserRole.PARTICIPANT]: ['Visualizar leilões', 'Favoritar produtos', 'Receber notificações'],
      [UserRole.BUYER]: ['Dar lances', 'Participar de leilões', 'Comprar produtos'],
      [UserRole.SELLER]: ['Criar produtos', 'Gerenciar leilões', 'Receber pagamentos'],
      [UserRole.ADMIN]: ['Gerenciar usuários', 'Moderar conteúdo', 'Configurar sistema']
    };
    return permissions[role] || [];
  }

  formatDate(dateString?: string): string {
    if (!dateString) return 'Nunca';
    
    const date = new Date(dateString);
    return date.toLocaleString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}