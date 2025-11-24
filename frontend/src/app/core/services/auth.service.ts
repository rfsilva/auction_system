import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
  phone?: string;
}

export interface AuthResponse {
  success: boolean;
  data: {
    token: string;
    refreshToken: string;
    user: User;
  };
  message?: string;
}

export interface User {
  id: string;
  name: string;
  email: string;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = environment.apiUrl;
  private readonly TOKEN_KEY = 'auth_token';
  private readonly REFRESH_TOKEN_KEY = 'refresh_token';
  
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
        })
      );
  }

  register(userData: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/auth/register`, userData);
  }

  logout(): Observable<any> {
    return this.http.post(`${this.API_URL}/auth/logout`, {})
      .pipe(
        tap(() => {
          this.clearAuth();
        })
      );
  }

  refreshToken(): Observable<AuthResponse> {
    const refreshToken = this.getRefreshToken();
    return this.http.post<AuthResponse>(`${this.API_URL}/auth/refresh`, { refreshToken })
      .pipe(
        tap(response => {
          if (response.success) {
            this.setTokens(response.data.token, response.data.refreshToken);
          }
        })
      );
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
    this.isAuthenticatedSignal.set(true);
    this.isAuthenticatedSubject.next(true);
  }

  private setUser(user: User): void {
    this.currentUserSignal.set(user);
  }

  private clearAuth(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.REFRESH_TOKEN_KEY);
    this.isAuthenticatedSignal.set(false);
    this.currentUserSignal.set(null);
    this.isAuthenticatedSubject.next(false);
  }

  private checkAuthStatus(): void {
    const token = this.getToken();
    if (token) {
      // TODO: Validar token com o backend
      this.isAuthenticatedSignal.set(true);
      this.isAuthenticatedSubject.next(true);
    }
  }
}