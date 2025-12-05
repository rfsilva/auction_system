import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { UserRole } from '../models/user.model';

/**
 * Guards específicos para diferentes áreas da aplicação
 * FASE 2 - Reorganização de Guards: Separação por roles e áreas
 */

@Injectable({
  providedIn: 'root'
})
export class PublicGuard implements CanActivate {
  /**
   * Guard para área pública - sempre permite acesso
   */
  canActivate(): boolean {
    return true;
  }
}

@Injectable({
  providedIn: 'root'
})
export class PrivateGuard implements CanActivate {
  /**
   * Guard para área privada - requer autenticação
   */
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(): boolean {
    if (this.authService.isAuthenticated()) {
      return true;
    }
    
    this.router.navigate(['/auth/login']);
    return false;
  }
}

@Injectable({
  providedIn: 'root'
})
export class UsuarioGuard implements CanActivate {
  /**
   * Guard para área do usuário - requer role PARTICIPANT ou superior
   */
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(): boolean {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/auth/login']);
      return false;
    }

    const user = this.authService.currentUser();
    if (user && (user.roles.includes(UserRole.PARTICIPANT) || 
                 user.roles.includes(UserRole.BUYER) ||
                 user.roles.includes(UserRole.SELLER) ||
                 user.roles.includes(UserRole.ADMIN))) {
      return true;
    }

    // Se não tem role adequada, redireciona para área apropriada
    this.redirectToAppropriateArea();
    return false;
  }

  private redirectToAppropriateArea(): void {
    const user = this.authService.currentUser();
    if (!user) {
      this.router.navigate(['/auth/login']);
      return;
    }

    if (user.roles.includes(UserRole.ADMIN)) {
      this.router.navigate(['/admin']);
    } else if (user.roles.includes(UserRole.SELLER)) {
      this.router.navigate(['/vendedor']);
    } else {
      this.router.navigate(['/']);
    }
  }
}

@Injectable({
  providedIn: 'root'
})
export class VendedorGuard implements CanActivate {
  /**
   * Guard para área do vendedor - requer role SELLER
   */
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(): boolean {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/auth/login']);
      return false;
    }

    const user = this.authService.currentUser();
    if (user && user.roles.includes(UserRole.SELLER)) {
      return true;
    }

    // Se não tem role SELLER, redireciona para área apropriada
    this.redirectToAppropriateArea();
    return false;
  }

  private redirectToAppropriateArea(): void {
    const user = this.authService.currentUser();
    if (!user) {
      this.router.navigate(['/auth/login']);
      return;
    }

    if (user.roles.includes(UserRole.ADMIN)) {
      this.router.navigate(['/admin']);
    } else if (user.roles.includes(UserRole.PARTICIPANT) || user.roles.includes(UserRole.BUYER)) {
      this.router.navigate(['/app']);
    } else {
      this.router.navigate(['/']);
    }
  }
}

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {
  /**
   * Guard para área do admin - requer role ADMIN
   */
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(): boolean {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/auth/login']);
      return false;
    }

    const user = this.authService.currentUser();
    if (user && user.roles.includes(UserRole.ADMIN)) {
      return true;
    }

    // Se não tem role ADMIN, redireciona para área apropriada
    this.redirectToAppropriateArea();
    return false;
  }

  private redirectToAppropriateArea(): void {
    const user = this.authService.currentUser();
    if (!user) {
      this.router.navigate(['/auth/login']);
      return;
    }

    if (user.roles.includes(UserRole.SELLER)) {
      this.router.navigate(['/vendedor']);
    } else if (user.roles.includes(UserRole.PARTICIPANT) || user.roles.includes(UserRole.BUYER)) {
      this.router.navigate(['/app']);
    } else {
      this.router.navigate(['/']);
    }
  }
}