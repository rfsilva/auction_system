import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, AbstractControl } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { RegisterRequest } from '../../core/models/user.model';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  registerForm: FormGroup;
  isLoading = false;
  isCheckingEmail = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(255)]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(255)]],
      phone: ['', [Validators.maxLength(20)]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(100)]],
      confirmPassword: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });

    // Verificar disponibilidade do email
    this.setupEmailAvailabilityCheck();
  }

  passwordMatchValidator(form: AbstractControl) {
    const password = form.get('password');
    const confirmPassword = form.get('confirmPassword');
    
    if (password && confirmPassword && password.value !== confirmPassword.value) {
      return { passwordMismatch: true };
    }
    return null;
  }

  private setupEmailAvailabilityCheck(): void {
    const emailControl = this.registerForm.get('email');
    if (emailControl) {
      emailControl.valueChanges.pipe(
        debounceTime(500),
        distinctUntilChanged()
      ).subscribe(email => {
        if (email && emailControl.valid && !emailControl.errors?.['email']) {
          this.checkEmailAvailability(email);
        }
      });
    }
  }

  checkEmailAvailability(email: string): void {
    this.isCheckingEmail = true;
    
    this.authService.checkEmailExists(email).subscribe({
      next: (response) => {
        this.isCheckingEmail = false;
        if (response.success && response.data) {
          // Email já existe
          this.registerForm.get('email')?.setErrors({ emailExists: true });
        }
      },
      error: (error) => {
        this.isCheckingEmail = false;
        console.error('Erro ao verificar email:', error);
      }
    });
  }

  onSubmit(): void {
    if (this.registerForm.valid && !this.isCheckingEmail) {
      this.isLoading = true;
      this.errorMessage = '';
      this.successMessage = '';

      const { confirmPassword, ...formData } = this.registerForm.value;
      
      const registerRequest: RegisterRequest = {
        name: formData.name,
        email: formData.email,
        password: formData.password,
        phone: formData.phone || undefined
      };

      this.authService.register(registerRequest).subscribe({
        next: (response) => {
          this.isLoading = false;
          if (response.success) {
            this.successMessage = 'Conta criada com sucesso! Redirecionando...';
            console.log('Usuário registrado:', response.data.user);
            setTimeout(() => {
              this.router.navigate(['/']);
            }, 2000);
          } else {
            this.errorMessage = response.message || 'Erro ao criar conta';
          }
        },
        error: (error) => {
          this.isLoading = false;
          console.error('Erro no registro:', error);
          
          if (error.error?.data) {
            this.errorMessage = error.error.data;
          } else if (error.error?.message) {
            this.errorMessage = error.error.message;
          } else {
            this.errorMessage = 'Erro ao conectar com o servidor';
          }
        }
      });
    } else {
      // Marcar todos os campos como touched para mostrar erros
      Object.keys(this.registerForm.controls).forEach(key => {
        this.registerForm.get(key)?.markAsTouched();
      });
    }
  }
}