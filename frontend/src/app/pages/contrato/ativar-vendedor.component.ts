import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ContratoService } from '../../core/services/contrato.service';
import { AuthService } from '../../core/services/auth.service';
// ✅ CORRIGIDO: Import da interface consolidada
import { AtivarVendedorRequest } from '../../core/models/contrato.model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable, debounceTime, distinctUntilChanged, switchMap, of } from 'rxjs';

interface UsuarioSugestao {
  id: string;
  nome: string;
  email: string;
  telefone?: string;
  isVendedor: boolean;
  temContratoAtivo: boolean;
}

interface ApiResponse<T> {
  success: boolean;
  data: T;
  message?: string;
  timestamp: string;
}

/**
 * Componente para ativar vendedores através de contratos
 * História 2: Processo de Contratação de Vendedores
 */
@Component({
  selector: 'app-ativar-vendedor',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './ativar-vendedor.component.html',
  styleUrls: ['./ativar-vendedor.component.scss']
})
export class AtivarVendedorComponent implements OnInit {

  form!: FormGroup;
  loading = false;
  errorMessage = '';
  successMessage = '';
  
  // Busca de usuários
  usuarios: UsuarioSugestao[] = [];
  usuarioSelecionado: UsuarioSugestao | null = null;
  buscandoUsuarios = false;
  
  // Categorias disponíveis
  categorias: string[] = [];
  
  // Validações
  validationErrors: string[] = [];

  constructor(
    private fb: FormBuilder,
    private contratoService: ContratoService,
    private authService: AuthService,
    private http: HttpClient,
    private router: Router
  ) {
    this.createForm();
  }

  ngOnInit(): void {
    // Verificar se é admin
    if (!this.authService.isAdmin()) {
      this.router.navigate(['/']);
      return;
    }

    this.carregarCategorias();
    this.setupUsuarioBusca();
  }

  private createForm(): void {
    this.form = this.fb.group({
      // Busca de usuário
      usuarioBusca: ['', [Validators.required]],
      usuarioSelecionadoId: ['', [Validators.required]],
      
      // Dados do contrato
      feeRate: [0.05, [
        Validators.required, 
        Validators.min(0.0001), 
        Validators.max(0.5)
      ]],
      terms: ['', [Validators.required, Validators.minLength(10)]],
      validFrom: ['', [Validators.required]],
      validTo: [''],
      categoria: [''],
      
      // Opções
      ativarImediatamente: [true]
    });

    // Definir data padrão como hoje
    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);
    this.form.patchValue({
      validFrom: hoje.toISOString().split('T')[0]
    });
  }

  private setupUsuarioBusca(): void {
    this.form.get('usuarioBusca')?.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(termo => {
        if (!termo || termo.length < 2) {
          return of([]);
        }
        return this.buscarUsuarios(termo);
      })
    ).subscribe({
      next: (usuarios) => {
        this.usuarios = usuarios;
        this.buscandoUsuarios = false;
      },
      error: (error) => {
        console.error('Erro ao buscar usuários:', error);
        this.usuarios = [];
        this.buscandoUsuarios = false;
      }
    });
  }

  private buscarUsuarios(termo: string): Observable<UsuarioSugestao[]> {
    this.buscandoUsuarios = true;
    
    return this.http.get<ApiResponse<UsuarioSugestao[]>>(
      `${environment.apiUrl}/admin/usuarios/buscar?termo=${encodeURIComponent(termo)}`
    ).pipe(
      switchMap(response => of(response.data || []))
    );
  }

  private carregarCategorias(): void {
    this.contratoService.listarCategorias().subscribe({
      next: (response) => {
        if (response.success) {
          this.categorias = response.data;
        }
      },
      error: (error) => {
        console.error('Erro ao carregar categorias:', error);
      }
    });
  }

  selecionarUsuario(usuario: UsuarioSugestao): void {
    this.usuarioSelecionado = usuario;
    this.form.patchValue({
      usuarioBusca: `${usuario.nome} (${usuario.email})`,
      usuarioSelecionadoId: usuario.id
    });
    this.usuarios = [];

    // Limpar mensagens
    this.errorMessage = '';
    this.successMessage = '';

    // Verificar se já é vendedor
    if (usuario.isVendedor && usuario.temContratoAtivo) {
      this.errorMessage = 'Este usuário já é um vendedor ativo com contrato vigente.';
    }
  }

  limparSelecao(): void {
    this.usuarioSelecionado = null;
    this.form.patchValue({
      usuarioBusca: '',
      usuarioSelecionadoId: ''
    });
    this.usuarios = [];
    this.errorMessage = '';
    this.successMessage = '';
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.markFormGroupTouched();
      this.collectValidationErrors();
      return;
    }

    if (!this.usuarioSelecionado) {
      this.errorMessage = 'Selecione um usuário válido.';
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';
    this.validationErrors = [];

    const formData = this.form.value;
    
    // Criar request para ativar vendedor
    const ativarVendedorRequest: AtivarVendedorRequest = {
      usuarioId: this.usuarioSelecionado.id,
      feeRate: formData.feeRate,
      terms: formData.terms,
      validFrom: formData.validFrom ? new Date(formData.validFrom).toISOString() : undefined,
      validTo: formData.validTo ? new Date(formData.validTo).toISOString() : undefined,
      categoria: formData.categoria || undefined,
      ativarImediatamente: formData.ativarImediatamente
    };

    this.ativarVendedor(ativarVendedorRequest);
  }

  private ativarVendedor(request: AtivarVendedorRequest): void {
    // Usar o método ativarVendedor do ContratoService
    this.contratoService.ativarVendedor(request).subscribe({
      next: (response) => {
        if (response.success) {
          const contrato = response.data;
          
          if (request.ativarImediatamente) {
            this.successMessage = `${this.usuarioSelecionado?.nome} foi transformado em vendedor ativo com sucesso! Contrato criado e ativado.`;
          } else {
            this.successMessage = `Contrato criado em rascunho para ${this.usuarioSelecionado?.nome}. Ative quando necessário.`;
          }
          
          this.enviarNotificacaoVendedor();
          this.resetForm();
        } else {
          this.errorMessage = response.message || 'Erro ao ativar vendedor';
        }
        this.loading = false;
      },
      error: (error) => {
        this.handleServerError(error);
        this.loading = false;
      }
    });
  }

  private enviarNotificacaoVendedor(): void {
    if (!this.usuarioSelecionado) return;

    // Enviar notificação para o usuário que virou vendedor
    const notificacao = {
      usuarioId: this.usuarioSelecionado.id,
      tipo: 'VENDEDOR_ATIVADO',
      titulo: 'Parabéns! Você agora é um vendedor',
      mensagem: `Seu contrato foi aprovado e você agora pode vender produtos na plataforma. Taxa de comissão: ${this.formatarTaxa(this.form.value.feeRate)}.`,
      link: '/vendedor/contratos'
    };

    this.http.post(`${environment.apiUrl}/notificacoes`, notificacao).subscribe({
      next: () => {
        console.log('Notificação enviada com sucesso');
      },
      error: (error) => {
        console.error('Erro ao enviar notificação:', error);
      }
    });
  }

  private resetForm(): void {
    this.form.reset();
    this.usuarioSelecionado = null;
    this.usuarios = [];
    
    // Redefinir valores padrão
    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);
    this.form.patchValue({
      feeRate: 0.05,
      validFrom: hoje.toISOString().split('T')[0],
      ativarImediatamente: true
    });
  }

  cancelar(): void {
    this.router.navigate(['/admin/contratos']);
  }

  // Métodos de validação e utilitários

  private markFormGroupTouched(): void {
    Object.keys(this.form.controls).forEach(key => {
      const control = this.form.get(key);
      control?.markAsTouched();
    });
  }

  private collectValidationErrors(): void {
    this.validationErrors = [];
    
    Object.keys(this.form.controls).forEach(key => {
      const control = this.form.get(key);
      if (control && control.invalid && control.touched) {
        const fieldName = this.getFieldDisplayName(key);
        const error = this.getFieldError(key);
        if (error) {
          this.validationErrors.push(`${fieldName}: ${error}`);
        }
      }
    });
  }

  private handleServerError(error: any): void {
    console.error('Erro do servidor:', error);
    
    if (error.error?.message) {
      this.errorMessage = error.error.message;
    } else if (error.message) {
      this.errorMessage = error.message;
    } else {
      this.errorMessage = 'Erro interno do servidor. Tente novamente.';
    }

    // Tratar erros de validação específicos
    if (error.status === 400 && error.error?.data) {
      const validationErrors = error.error.data;
      this.validationErrors = Object.values(validationErrors).flat() as string[];
    }
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.form.get(fieldName);
    return !!(field && field.invalid && field.touched);
  }

  getFieldError(fieldName: string): string | null {
    const field = this.form.get(fieldName);
    if (field && field.errors && field.touched) {
      const errors = field.errors;
      
      if (errors['required']) return 'Campo obrigatório';
      if (errors['email']) return 'Email inválido';
      if (errors['min']) return `Valor mínimo: ${errors['min'].min}`;
      if (errors['max']) return `Valor máximo: ${errors['max'].max}`;
      if (errors['minlength']) return `Mínimo ${errors['minlength'].requiredLength} caracteres`;
      if (errors['maxlength']) return `Máximo ${errors['maxlength'].requiredLength} caracteres`;
    }
    
    return null;
  }

  private getFieldDisplayName(fieldName: string): string {
    const names: Record<string, string> = {
      usuarioBusca: 'Usuário',
      usuarioSelecionadoId: 'Usuário selecionado',
      feeRate: 'Taxa de comissão',
      terms: 'Termos do contrato',
      validFrom: 'Data de início',
      validTo: 'Data de fim',
      categoria: 'Categoria'
    };
    return names[fieldName] || fieldName;
  }

  hasValidationErrors(): boolean {
    return this.validationErrors.length > 0;
  }

  formatarTaxa(taxa: number): string {
    return `${(taxa * 100).toFixed(2)}%`;
  }

  formatarData(dataString: string): string {
    if (!dataString) return '';
    
    const data = new Date(dataString);
    return data.toLocaleDateString('pt-BR');
  }
}