import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <div class="auth-container">
      <div class="auth-card">
        <div class="auth-header">
          <h1>Criar Conta</h1>
          <p>Cadastre-se para participar dos leilões</p>
        </div>

        <form [formGroup]="registerForm" (ngSubmit)="onSubmit()" class="auth-form">
          <div class="form-group">
            <label for="name">Nome Completo</label>
            <input
              type="text"
              id="name"
              formControlName="name"
              class="form-control"
              [class.error]="registerForm.get('name')?.invalid && registerForm.get('name')?.touched"
              placeholder="Seu nome completo"
            >
            @if (registerForm.get('name')?.invalid && registerForm.get('name')?.touched) {
              <div class="error-message">
                Nome é obrigatório
              </div>
            }
          </div>

          <div class="form-group">
            <label for="email">E-mail</label>
            <input
              type="email"
              id="email"
              formControlName="email"
              class="form-control"
              [class.error]="registerForm.get('email')?.invalid && registerForm.get('email')?.touched"
              placeholder="seu@email.com"
            >
            @if (registerForm.get('email')?.invalid && registerForm.get('email')?.touched) {
              <div class="error-message">
                @if (registerForm.get('email')?.errors?.['required']) {
                  E-mail é obrigatório
                }
                @if (registerForm.get('email')?.errors?.['email']) {
                  E-mail inválido
                }
              </div>
            }
          </div>

          <div class="form-group">
            <label for="phone">Telefone (opcional)</label>
            <input
              type="tel"
              id="phone"
              formControlName="phone"
              class="form-control"
              placeholder="(11) 99999-9999"
            >
          </div>

          <div class="form-group">
            <label for="password">Senha</label>
            <input
              type="password"
              id="password"
              formControlName="password"
              class="form-control"
              [class.error]="registerForm.get('password')?.invalid && registerForm.get('password')?.touched"
              placeholder="Mínimo 6 caracteres"
            >
            @if (registerForm.get('password')?.invalid && registerForm.get('password')?.touched) {
              <div class="error-message">
                @if (registerForm.get('password')?.errors?.['required']) {
                  Senha é obrigatória
                }
                @if (registerForm.get('password')?.errors?.['minlength']) {
                  Senha deve ter pelo menos 6 caracteres
                }
              </div>
            }
          </div>

          <div class="form-group">
            <label for="confirmPassword">Confirmar Senha</label>
            <input
              type="password"
              id="confirmPassword"
              formControlName="confirmPassword"
              class="form-control"
              [class.error]="registerForm.get('confirmPassword')?.invalid && registerForm.get('confirmPassword')?.touched"
              placeholder="Confirme sua senha"
            >
            @if (registerForm.get('confirmPassword')?.invalid && registerForm.get('confirmPassword')?.touched) {
              <div class="error-message">
                @if (registerForm.get('confirmPassword')?.errors?.['required']) {
                  Confirmação de senha é obrigatória
                }
                @if (registerForm.errors?.['passwordMismatch']) {
                  Senhas não coincidem
                }
              </div>
            }
          </div>

          @if (errorMessage) {
            <div class="alert alert-error">
              {{ errorMessage }}
            </div>
          }

          @if (successMessage) {
            <div class="alert alert-success">
              {{ successMessage }}
            </div>
          }

          <button
            type="submit"
            class="btn btn-primary btn-full"
            [disabled]="registerForm.invalid || isLoading"
          >
            @if (isLoading) {
              <span class="loading-spinner"></span>
              Criando conta...
            } @else {
              Criar Conta
            }
          </button>
        </form>

        <div class="auth-footer">
          <p>
            Já tem uma conta?
            <a routerLink="/auth/login" class="link">Faça login</a>
          </p>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .auth-container {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      padding: 2rem;
    }

    .auth-card {
      background: white;
      border-radius: 12px;
      padding: 3rem;
      width: 100%;
      max-width: 450px;
      box-shadow: 0 20px 40px rgba(0,0,0,0.1);
    }

    .auth-header {
      text-align: center;
      margin-bottom: 2rem;
    }

    .auth-header h1 {
      color: #333;
      margin-bottom: 0.5rem;
      font-size: 2rem;
    }

    .auth-header p {
      color: #666;
      margin: 0;
    }

    .auth-form {
      margin-bottom: 2rem;
    }

    .form-group {
      margin-bottom: 1.5rem;
    }

    .form-group label {
      display: block;
      margin-bottom: 0.5rem;
      color: #333;
      font-weight: 500;
    }

    .form-control {
      width: 100%;
      padding: 0.75rem;
      border: 2px solid #e1e5e9;
      border-radius: 6px;
      font-size: 1rem;
      transition: border-color 0.2s;
      box-sizing: border-box;
    }

    .form-control:focus {
      outline: none;
      border-color: #667eea;
    }

    .form-control.error {
      border-color: #e74c3c;
    }

    .error-message {
      color: #e74c3c;
      font-size: 0.875rem;
      margin-top: 0.25rem;
    }

    .alert {
      padding: 0.75rem;
      border-radius: 6px;
      margin-bottom: 1rem;
    }

    .alert-error {
      background: #fee;
      color: #c33;
      border: 1px solid #fcc;
    }

    .alert-success {
      background: #efe;
      color: #3c3;
      border: 1px solid #cfc;
    }

    .btn {
      padding: 0.75rem 1.5rem;
      border: none;
      border-radius: 6px;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.2s;
      font-size: 1rem;
      display: inline-flex;
      align-items: center;
      justify-content: center;
      gap: 0.5rem;
    }

    .btn-primary {
      background: #667eea;
      color: white;
    }

    .btn-primary:hover:not(:disabled) {
      background: #5a6fd8;
      transform: translateY(-1px);
    }

    .btn-primary:disabled {
      background: #ccc;
      cursor: not-allowed;
      transform: none;
    }

    .btn-full {
      width: 100%;
    }

    .loading-spinner {
      width: 16px;
      height: 16px;
      border: 2px solid transparent;
      border-top: 2px solid currentColor;
      border-radius: 50%;
      animation: spin 1s linear infinite;
    }

    @keyframes spin {
      to {
        transform: rotate(360deg);
      }
    }

    .auth-footer {
      text-align: center;
    }

    .auth-footer p {
      margin: 0;
      color: #666;
    }

    .link {
      color: #667eea;
      text-decoration: none;
      font-weight: 500;
    }

    .link:hover {
      text-decoration: underline;
    }

    @media (max-width: 480px) {
      .auth-container {
        padding: 1rem;
      }

      .auth-card {
        padding: 2rem;
      }

      .auth-header h1 {
        font-size: 1.5rem;
      }
    }
  `]
})
export class RegisterComponent {
  registerForm: FormGroup;
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      name: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      phone: [''],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });
  }

  passwordMatchValidator(form: FormGroup) {
    const password = form.get('password');
    const confirmPassword = form.get('confirmPassword');
    
    if (password && confirmPassword && password.value !== confirmPassword.value) {
      return { passwordMismatch: true };
    }
    return null;
  }

  onSubmit(): void {
    if (this.registerForm.valid) {
      this.isLoading = true;
      this.errorMessage = '';
      this.successMessage = '';

      const { confirmPassword, ...userData } = this.registerForm.value;

      this.authService.register(userData).subscribe({
        next: (response) => {
          this.isLoading = false;
          if (response.success) {
            this.successMessage = 'Conta criada com sucesso! Redirecionando para login...';
            setTimeout(() => {
              this.router.navigate(['/auth/login']);
            }, 2000);
          } else {
            this.errorMessage = response.message || 'Erro ao criar conta';
          }
        },
        error: (error) => {
          this.isLoading = false;
          this.errorMessage = error.error?.message || 'Erro ao conectar com o servidor';
        }
      });
    }
  }
}