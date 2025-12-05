import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { 
  User, 
  UserRole, 
  UserStatus, 
  LoginRequest, 
  RegisterRequest, 
  AuthResponse, 
  RefreshTokenRequest 
} from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = environment.apiUrl;
  private readonly TOKEN_KEY = 'auth_token';
  private readonly REFRESH_TOKEN_KEY = 'refresh_token';
  private readonly USER_KEY = 'current_user';
  
  // Signal para estado reativo
  private isAuthenticatedSignal = signal<boolean>(false);
  private currentUserSignal = signal<User | null>(null);
  
  // BehaviorSubject para compatibilidade com RxJS
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  
  constructor(private http: HttpClient) {
    this.checkAuthStatus();
  }

  // Getters para signals (Angular 17+)
  get isAuthenticated() {
    return this.isAuthenticatedSignal.asReadonly();
  }

  get currentUser() {
    return this.currentUserSignal.asReadonly();
  }

  // Observable para compatibilidade
  get isAuthenticated$(): Observable<boolean> {
    return this.isAuthenticatedSubject.asObservable();
  }

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/auth/login`, credentials)
      .pipe(
        tap(response => {
          if (response.success) {
            this.setTokens(response.data.token, response.data.refreshToken);
            this.setUser(response.data.user);
          }
        }),
        catchError(error => {
          console.error('Erro no login:', error);
          throw error;
        })
      );
  }

  register(userData: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/auth/register`, userData)
      .pipe(
        tap(response => {
          if (response.success) {
            this.setTokens(response.data.token, response.data.refreshToken);
            this.setUser(response.data.user);
          }
        }),
        catchError(error => {
          console.error('Erro no registro:', error);
          throw error;
        })
      );
  }

  logout(): Observable<any> {
    return this.http.post(`${this.API_URL}/auth/logout`, {})
      .pipe(
        tap(() => {
          this.clearAuth();
        }),
        catchError(error => {
          // Mesmo com erro, limpar dados locais
          this.clearAuth();
          throw error;
        })
      );
  }

  refreshToken(): Observable<AuthResponse> {
    const refreshToken = this.getRefreshToken();
    if (!refreshToken) {
      throw new Error('Refresh token não encontrado');
    }

    const request: RefreshTokenRequest = { refreshToken };
    return this.http.post<AuthResponse>(`${this.API_URL}/auth/refresh`, request)
      .pipe(
        tap(response => {
          if (response.success) {
            this.setTokens(response.data.token, response.data.refreshToken);
            this.setUser(response.data.user);
          }
        }),
        catchError(error => {
          console.error('Erro ao renovar token:', error);
          this.clearAuth();
          throw error;
        })
      );
  }

  checkEmailExists(email: string): Observable<{ success: boolean; data: boolean }> {
    return this.http.get<{ success: boolean; data: boolean }>(`${this.API_URL}/auth/check-email?email=${email}`);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  getRefreshToken(): string | null {
    return localStorage.getItem(this.REFRESH_TOKEN_KEY);
  }

  private setTokens(token: string, refreshToken: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
    localStorage.setItem(this.REFRESH_TOKEN_KEY, refreshToken);
    this.updateAuthState(true);
  }

  private setUser(user: User): void {
    // Salvar usuário no localStorage para persistir entre reloads
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
    this.currentUserSignal.set(user);
  }

  private clearAuth(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.REFRESH_TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    this.updateAuthState(false);
    this.currentUserSignal.set(null);
  }

  private updateAuthState(isAuthenticated: boolean): void {
    // Atualizar ambos os estados de uma vez para evitar múltiplas re-renderizações
    this.isAuthenticatedSignal.set(isAuthenticated);
    this.isAuthenticatedSubject.next(isAuthenticated);
  }

  private checkAuthStatus(): void {
    const token = this.getToken();
    const userJson = localStorage.getItem(this.USER_KEY);
    
    if (token && userJson) {
      try {
        const user: User = JSON.parse(userJson);
        // Validar se o objeto user tem as propriedades necessárias
        if (user && user.id && user.email) {
          this.currentUserSignal.set(user);
          this.updateAuthState(true);
          console.log('Usuário recuperado do localStorage:', user.email);
        } else {
          console.warn('Dados do usuário inválidos no localStorage');
          this.clearAuth();
        }
      } catch (error) {
        console.error('Erro ao parsear dados do usuário:', error);
        this.clearAuth();
      }
    } else {
      this.updateAuthState(false);
    }
  }

  // Métodos para verificar roles
  hasRole(role: UserRole): boolean {
    const user = this.currentUser();
    return user?.roles?.includes(role) || false;
  }

  hasAnyRole(roles: UserRole[]): boolean {
    const user = this.currentUser();
    return roles.some(role => user?.roles?.includes(role)) || false;
  }

  canBid(): boolean {
    return this.hasRole(UserRole.BUYER) && this.isAccountActive();
  }

  canSell(): boolean {
    return this.hasRole(UserRole.SELLER) && this.isAccountActive();
  }

  isAdmin(): boolean {
    return this.hasRole(UserRole.ADMIN);
  }

  isAccountActive(): boolean {
    const user = this.currentUser();
    return user?.status === UserStatus.ACTIVE;
  }

  isEmailVerified(): boolean {
    const user = this.currentUser();
    return user?.emailVerificado || false;
  }

  // Métodos utilitários
  getRoleDisplayName(role: UserRole): string {
    const roleNames: Record<UserRole, string> = {
      [UserRole.VISITOR]: 'Visitante',
      [UserRole.PARTICIPANT]: 'Participante',
      [UserRole.BUYER]: 'Comprador',
      [UserRole.SELLER]: 'Vendedor',
      [UserRole.ADMIN]: 'Administrador'
    };
    return roleNames[role] || role;
  }

  getStatusDisplayName(status: UserStatus): string {
    const statusNames: Record<UserStatus, string> = {
      [UserStatus.PENDING_VERIFICATION]: 'Aguardando Verificação',
      [UserStatus.ACTIVE]: 'Ativo',
      [UserStatus.INACTIVE]: 'Inativo',
      [UserStatus.SUSPENDED]: 'Suspenso'
    };
    return statusNames[status] || status;
  }
}