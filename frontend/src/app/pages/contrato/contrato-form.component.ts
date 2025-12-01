import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { Subject, takeUntil, debounceTime, distinctUntilChanged } from 'rxjs';

import { ContratoService, ContratoCreateFromUserRequest } from '../../core/services/contrato.service';
import { UsuarioService, UsuarioSugestaoDto } from '../../core/services/usuario.service';
import { AuthService } from '../../core/services/auth.service';
import { 
  Contrato, 
  ContratoCreateRequest, 
  ContratoUpdateRequest 
} from '../../core/models/contrato.model';

@Component({
  selector: 'app-contrato-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './contrato-form.component.html',
  styleUrl: './contrato-form.component.scss'
})
export class ContratoFormComponent implements OnInit, OnDestroy {
  
  private destroy$ = new Subject<void>();
  private searchSubject = new Subject<string>();

  // Estados
  loading = false;
  saving = false;
  isEditMode = false;
  contratoId: string | null = null;
  
  // Dados
  contrato: Contrato | null = null;
  categorias: string[] = [];
  usuarios: UsuarioSugestaoDto[] = [];
  usuarioSelecionado: UsuarioSugestaoDto | null = null;
  
  // Busca de usuários
  buscandoUsuarios = false;
  termoBusca = '';
  
  // Formulário
  contratoForm: FormGroup;
  
  // Erros
  errors: { [key: string]: string } = {};
  serverError: string | null = null;

  constructor(
    private fb: FormBuilder,
    private contratoService: ContratoService,
    private usuarioService: UsuarioService,
    public authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.contratoForm = this.createForm();
    
    // Setup do debounce para busca de usuários
    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      takeUntil(this.destroy$)
    ).subscribe(termo => {
      if (termo.length >= 2) {
        this.buscarUsuarios(termo);
      } else {
        this.usuarios = [];
      }
    });
  }

  ngOnInit(): void {
    this.contratoId = this.route.snapshot.paramMap.get('id');
    this.isEditMode = !!this.contratoId;
    
    this.carregarCategorias();
    
    if (this.isEditMode && this.contratoId) {
      this.carregarContrato();
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // Criação do formulário

  createForm(): FormGroup {
    return this.fb.group({
      usuarioId: ['', [Validators.required]], // Mudança: usuarioId em vez de sellerId
      feeRate: ['', [
        Validators.required,
        Validators.min(0.0001),
        Validators.max(0.5000)
      ]],
      terms: ['', [
        Validators.required,
        Validators.minLength(50),
        Validators.maxLength(10000)
      ]],
      validFrom: [''],
      validTo: [''],
      categoria: ['']
    }, {
      validators: [this.dateRangeValidator, this.minimumPeriodValidator]
    });
  }

  // Validadores customizados

  dateRangeValidator(form: AbstractControl) {
    const validFrom = form.get('validFrom')?.value;
    const validTo = form.get('validTo')?.value;
    
    if (validFrom && validTo && new Date(validTo) <= new Date(validFrom)) {
      return { dateRange: true };
    }
    return null;
  }

  minimumPeriodValidator(form: AbstractControl) {
    const validFrom = form.get('validFrom')?.value;
    const validTo = form.get('validTo')?.value;
    
    if (validFrom && validTo) {
      const start = new Date(validFrom);
      const end = new Date(validTo);
      const diffDays = (end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24);
      
      if (diffDays < 30) {
        return { minimumPeriod: true };
      }
    }
    return null;
  }

  // Carregamento de dados

  carregarContrato(): void {
    if (!this.contratoId) return;
    
    this.loading = true;
    this.contratoService.buscarContrato(this.contratoId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.contrato = response.data;
            this.preencherFormulario(response.data);
          } else {
            this.serverError = response.message || 'Erro ao carregar contrato';
          }
          this.loading = false;
        },
        error: (error) => {
          console.error('Erro ao carregar contrato:', error);
          this.serverError = 'Erro ao carregar contrato. Tente novamente.';
          this.loading = false;
        }
      });
  }

  carregarCategorias(): void {
    this.contratoService.listarCategorias()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
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

  buscarUsuarios(termo: string): void {
    this.buscandoUsuarios = true;
    this.usuarioService.buscarUsuarios(termo)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.usuarios = response.data;
          } else {
            console.error('Erro ao buscar usuários:', response.message);
            this.usuarios = [];
          }
          this.buscandoUsuarios = false;
        },
        error: (error) => {
          console.error('Erro ao buscar usuários:', error);
          this.usuarios = [];
          this.buscandoUsuarios = false;
        }
      });
  }

  preencherFormulario(contrato: Contrato): void {
    // Converter datas para formato de input datetime-local
    const validFrom = contrato.validFrom ? 
      new Date(contrato.validFrom).toISOString().slice(0, 16) : '';
    const validTo = contrato.validTo ? 
      new Date(contrato.validTo).toISOString().slice(0, 16) : '';

    this.contratoForm.patchValue({
      usuarioId: contrato.sellerId, // No backend, ainda usamos sellerId
      feeRate: contrato.feeRate * 100, // Converter para porcentagem
      terms: contrato.terms,
      validFrom: validFrom,
      validTo: validTo,
      categoria: contrato.categoria || ''
    });

    // Se estamos editando, buscar o usuário selecionado
    if (contrato.sellerName) {
      this.usuarioSelecionado = {
        id: contrato.sellerId,
        nome: contrato.sellerName,
        email: '', // Não temos o email no contrato
        isVendedor: true,
        temContratoAtivo: true
      };
      this.termoBusca = contrato.sellerName;
    }
  }

  // Busca de usuários

  onBuscarUsuario(event: Event): void {
    const target = event.target as HTMLInputElement;
    const termo = target.value || '';
    this.termoBusca = termo;
    this.searchSubject.next(termo);
  }

  selecionarUsuario(usuario: UsuarioSugestaoDto): void {
    this.usuarioSelecionado = usuario;
    this.termoBusca = usuario.nome;
    this.contratoForm.patchValue({ usuarioId: usuario.id });
    this.usuarios = []; // Limpar lista após seleção
  }

  limparSelecao(): void {
    this.usuarioSelecionado = null;
    this.termoBusca = '';
    this.contratoForm.patchValue({ usuarioId: '' });
    this.usuarios = [];
  }

  // Submissão do formulário

  onSubmit(): void {
    if (this.contratoForm.invalid) {
      this.markFormGroupTouched();
      this.collectValidationErrors();
      return;
    }

    this.saving = true;
    this.serverError = null;
    this.errors = {};

    const formData = this.contratoForm.value;
    
    if (this.isEditMode && this.contratoId) {
      // Para edição, usar o método antigo
      const contratoData: ContratoUpdateRequest = {
        feeRate: formData.feeRate / 100, // Converter de porcentagem para decimal
        terms: formData.terms,
        validFrom: formData.validFrom || undefined,
        validTo: formData.validTo || undefined,
        categoria: formData.categoria || undefined
      };
      this.atualizarContrato(contratoData);
    } else {
      // Para criação, usar o novo método
      const contratoData: ContratoCreateFromUserRequest = {
        usuarioId: formData.usuarioId,
        feeRate: formData.feeRate / 100, // Converter de porcentagem para decimal
        terms: formData.terms,
        validFrom: formData.validFrom || undefined,
        validTo: formData.validTo || undefined,
        categoria: formData.categoria || undefined,
        ativarImediatamente: false // Por padrão, criar em rascunho
      };
      this.criarContrato(contratoData);
    }
  }

  criarContrato(contratoData: ContratoCreateFromUserRequest): void {
    this.contratoService.criarContratoDeUsuario(contratoData)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.router.navigate(['/admin/contratos'], {
              queryParams: { created: 'true' }
            });
          } else {
            this.handleServerError(response);
          }
          this.saving = false;
        },
        error: (error) => {
          this.handleServerError(error);
          this.saving = false;
        }
      });
  }

  atualizarContrato(contratoData: ContratoUpdateRequest): void {
    if (!this.contratoId) return;
    
    this.contratoService.atualizarContrato(this.contratoId, contratoData)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.router.navigate(['/admin/contratos'], {
              queryParams: { updated: 'true' }
            });
          } else {
            this.handleServerError(response);
          }
          this.saving = false;
        },
        error: (error) => {
          this.handleServerError(error);
          this.saving = false;
        }
      });
  }

  // Navegação

  cancelar(): void {
    this.router.navigate(['/admin/contratos']);
  }

  // Validação e erros

  markFormGroupTouched(): void {
    Object.keys(this.contratoForm.controls).forEach(key => {
      const control = this.contratoForm.get(key);
      control?.markAsTouched();
    });
  }

  collectValidationErrors(): void {
    this.errors = {};
    
    Object.keys(this.contratoForm.controls).forEach(key => {
      const control = this.contratoForm.get(key);
      if (control && control.invalid && control.touched) {
        this.errors[key] = this.getFieldError(key);
      }
    });

    // Erros do formulário como um todo
    if (this.contratoForm.errors?.['dateRange']) {
      this.errors['dateRange'] = 'Data de fim deve ser posterior à data de início';
    }
    if (this.contratoForm.errors?.['minimumPeriod']) {
      this.errors['minimumPeriod'] = 'Contrato deve ter duração mínima de 30 dias';
    }
  }

  getFieldError(fieldName: string): string {
    const control = this.contratoForm.get(fieldName);
    if (!control || !control.errors) return '';

    const errors = control.errors;
    
    if (errors['required']) return `${this.getFieldLabel(fieldName)} é obrigatório`;
    if (errors['min']) return `${this.getFieldLabel(fieldName)} deve ser maior que ${errors['min'].min}`;
    if (errors['max']) return `${this.getFieldLabel(fieldName)} deve ser menor que ${errors['max'].max}`;
    if (errors['minlength']) return `${this.getFieldLabel(fieldName)} deve ter pelo menos ${errors['minlength'].requiredLength} caracteres`;
    if (errors['maxlength']) return `${this.getFieldLabel(fieldName)} deve ter no máximo ${errors['maxlength'].requiredLength} caracteres`;
    
    return 'Campo inválido';
  }

  getFieldLabel(fieldName: string): string {
    const labels: { [key: string]: string } = {
      usuarioId: 'Usuário',
      feeRate: 'Taxa de comissão',
      terms: 'Termos do contrato',
      validFrom: 'Data de início',
      validTo: 'Data de fim',
      categoria: 'Categoria'
    };
    return labels[fieldName] || fieldName;
  }

  isFieldInvalid(fieldName: string): boolean {
    const control = this.contratoForm.get(fieldName);
    return !!(control && control.invalid && control.touched);
  }

  hasValidationErrors(): boolean {
    return Object.keys(this.errors).length > 0;
  }

  getValidationErrorsList(): string[] {
    return Object.values(this.errors);
  }

  handleServerError(error: any): void {
    if (error.error && error.error.message) {
      this.serverError = error.error.message;
    } else if (error.message) {
      this.serverError = error.message;
    } else {
      this.serverError = 'Erro interno do servidor. Tente novamente.';
    }
  }

  // Helpers para template

  formatarTaxa(taxa: number): string {
    return this.contratoService.formatarTaxa(taxa / 100);
  }

  formatarNomeUsuario(usuario: UsuarioSugestaoDto): string {
    return this.usuarioService.formatarNomeUsuario(usuario);
  }

  getStatusClass(usuario: UsuarioSugestaoDto): string {
    return this.usuarioService.getStatusClass(usuario);
  }

  getStatusIcon(usuario: UsuarioSugestaoDto): string {
    return this.usuarioService.getStatusIcon(usuario);
  }
}