import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { ProdutoService } from '../../core/services/produto.service';
import { AuthService } from '../../core/services/auth.service';
import { Produto, ProdutoCreateRequest, ProdutoUpdateRequest } from '../../core/models/produto.model';

@Component({
  selector: 'app-produto-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './produto-form.component.html',
  styleUrls: ['./produto-form.component.scss']
})
export class ProdutoFormComponent implements OnInit {
  produtoForm: FormGroup;
  isEditMode = false;
  produtoId?: string;
  loading = false;
  error = '';
  success = '';
  categorias: string[] = [];
  validationErrors: { [key: string]: string } = {};

  constructor(
    private fb: FormBuilder,
    private produtoService: ProdutoService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.produtoForm = this.createForm();
  }

  ngOnInit() {
    // Verificar se usuário pode vender
    if (!this.authService.canSell()) {
      this.router.navigate(['/']);
      return;
    }

    // Carregar categorias
    this.carregarCategorias();

    // Verificar se é modo de edição
    this.produtoId = this.route.snapshot.paramMap.get('id') || undefined;
    if (this.produtoId) {
      this.isEditMode = true;
      this.carregarProduto();
    }
  }

  private createForm(): FormGroup {
    return this.fb.group({
      title: ['', [Validators.required, Validators.maxLength(255)]],
      description: ['', [Validators.required, Validators.maxLength(5000)]],
      categoria: [''],
      initialPrice: ['', [Validators.required, Validators.min(0.01)]],
      reservePrice: ['', [Validators.min(0.01)]],
      incrementMin: [1, [Validators.required, Validators.min(0.01)]],
      endDateTime: ['', [Validators.required]],
      weight: ['', [Validators.min(0.001)]],
      dimensions: [''],
      antiSnipeEnabled: [true],
      antiSnipeExtension: [300, [Validators.min(60), Validators.max(3600)]],
      images: this.fb.array([]),
      tags: this.fb.array([])
    });
  }

  get imagesArray(): FormArray {
    return this.produtoForm.get('images') as FormArray;
  }

  get tagsArray(): FormArray {
    return this.produtoForm.get('tags') as FormArray;
  }

  private carregarCategorias() {
    this.produtoService.listarCategorias().subscribe({
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

  private carregarProduto() {
    if (!this.produtoId) return;

    this.loading = true;
    this.produtoService.buscarProduto(this.produtoId).subscribe({
      next: (response) => {
        if (response.success) {
          this.preencherFormulario(response.data);
        }
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Erro ao carregar produto';
        this.loading = false;
        console.error('Erro ao carregar produto:', error);
      }
    });
  }

  private preencherFormulario(produto: Produto) {
    // Converter data para formato do input datetime-local
    const endDateTime = new Date(produto.endDateTime);
    const endDateTimeLocal = endDateTime.toISOString().slice(0, 16);

    this.produtoForm.patchValue({
      title: produto.title,
      description: produto.description,
      categoria: produto.categoria,
      initialPrice: produto.initialPrice,
      reservePrice: produto.reservePrice,
      incrementMin: produto.incrementMin,
      endDateTime: endDateTimeLocal,
      weight: produto.weight,
      dimensions: produto.dimensions,
      antiSnipeEnabled: produto.antiSnipeEnabled,
      antiSnipeExtension: produto.antiSnipeExtension
    });

    // Preencher imagens
    this.imagesArray.clear();
    produto.images.forEach(image => {
      this.imagesArray.push(this.fb.control(image));
    });

    // Preencher tags
    this.tagsArray.clear();
    produto.tags.forEach(tag => {
      this.tagsArray.push(this.fb.control(tag));
    });
  }

  adicionarImagem() {
    this.imagesArray.push(this.fb.control('', [Validators.required]));
  }

  removerImagem(index: number) {
    this.imagesArray.removeAt(index);
  }

  adicionarTag() {
    this.tagsArray.push(this.fb.control('', [Validators.required]));
  }

  removerTag(index: number) {
    this.tagsArray.removeAt(index);
  }

  onSubmit() {
    // Limpar erros anteriores
    this.error = '';
    this.success = '';
    this.validationErrors = {};

    if (this.produtoForm.invalid) {
      this.markFormGroupTouched();
      this.collectValidationErrors();
      this.error = 'Por favor, corrija os erros no formulário antes de continuar.';
      return;
    }

    // Validações customizadas
    const customValidationErrors = this.validateCustomRules();
    if (Object.keys(customValidationErrors).length > 0) {
      this.validationErrors = customValidationErrors;
      this.error = 'Por favor, corrija os erros indicados nos campos.';
      return;
    }

    this.loading = true;

    const formData = this.produtoForm.value;
    
    // Converter data para ISO string
    const endDateTime = new Date(formData.endDateTime).toISOString();
    
    // Filtrar arrays vazios
    const images = formData.images.filter((img: string) => img.trim() !== '');
    const tags = formData.tags.filter((tag: string) => tag.trim() !== '');

    if (this.isEditMode && this.produtoId) {
      const updateRequest: ProdutoUpdateRequest = {
        ...formData,
        endDateTime,
        images,
        tags
      };

      this.produtoService.atualizarProduto(this.produtoId, updateRequest).subscribe({
        next: (response) => {
          if (response.success) {
            this.success = 'Produto atualizado com sucesso!';
            setTimeout(() => {
              this.router.navigate(['/produtos/meus-produtos']);
            }, 2000);
          }
          this.loading = false;
        },
        error: (error) => {
          this.handleServerError(error);
          this.loading = false;
        }
      });
    } else {
      const createRequest: ProdutoCreateRequest = {
        ...formData,
        endDateTime,
        images,
        tags
      };

      this.produtoService.criarProduto(createRequest).subscribe({
        next: (response) => {
          if (response.success) {
            this.success = 'Produto criado com sucesso!';
            setTimeout(() => {
              this.router.navigate(['/produtos/meus-produtos']);
            }, 2000);
          }
          this.loading = false;
        },
        error: (error) => {
          this.handleServerError(error);
          this.loading = false;
        }
      });
    }
  }

  private validateCustomRules(): { [key: string]: string } {
    const errors: { [key: string]: string } = {};
    const formValue = this.produtoForm.value;

    // Validar data de encerramento
    if (formValue.endDateTime) {
      const endDate = new Date(formValue.endDateTime);
      const now = new Date();
      const oneHourFromNow = new Date(now.getTime() + 60 * 60 * 1000);

      if (endDate <= oneHourFromNow) {
        errors['endDateTime'] = 'A data de encerramento deve ser pelo menos 1 hora no futuro';
      }
    }

    // Validar preço de reserva
    if (formValue.reservePrice && formValue.initialPrice) {
      if (formValue.reservePrice < formValue.initialPrice) {
        errors['reservePrice'] = 'O preço de reserva não pode ser menor que o preço inicial';
      }
    }

    // Validar dimensões JSON
    if (formValue.dimensions && formValue.dimensions.trim() !== '') {
      try {
        JSON.parse(formValue.dimensions);
      } catch {
        errors['dimensions'] = 'Formato JSON inválido. Use: {"length": 10, "width": 5, "height": 3}';
      }
    }

    // Validar URLs das imagens
    formValue.images.forEach((img: string, index: number) => {
      if (img && img.trim() !== '') {
        try {
          new URL(img);
        } catch {
          errors[`images.${index}`] = 'URL da imagem inválida';
        }
      }
    });

    return errors;
  }

  private collectValidationErrors() {
    this.validationErrors = {};
    
    Object.keys(this.produtoForm.controls).forEach(key => {
      const control = this.produtoForm.get(key);
      if (control && control.invalid && control.touched) {
        this.validationErrors[key] = this.getFieldError(key);
      }
    });

    // Validar arrays
    this.imagesArray.controls.forEach((control, index) => {
      if (control.invalid && control.touched) {
        this.validationErrors[`images.${index}`] = 'URL da imagem é obrigatória';
      }
    });

    this.tagsArray.controls.forEach((control, index) => {
      if (control.invalid && control.touched) {
        this.validationErrors[`tags.${index}`] = 'Tag é obrigatória';
      }
    });
  }

  private handleServerError(error: any) {
    console.error('Erro do servidor:', error);
    
    if (error.status === 400 && error.error?.data) {
      // Tratar erros de validação do backend
      const serverErrors = error.error.data;
      this.validationErrors = {};
      
      Object.keys(serverErrors).forEach(field => {
        this.validationErrors[field] = serverErrors[field];
      });
      
      this.error = 'Dados inválidos. Verifique os campos destacados.';
    } else if (error.error?.message) {
      this.error = error.error.message;
    } else {
      this.error = 'Erro interno do servidor. Tente novamente.';
    }
  }

  private markFormGroupTouched() {
    Object.keys(this.produtoForm.controls).forEach(key => {
      const control = this.produtoForm.get(key);
      control?.markAsTouched();
      
      if (control instanceof FormArray) {
        control.controls.forEach(arrayControl => {
          arrayControl.markAsTouched();
        });
      }
    });
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.produtoForm.get(fieldName);
    const hasValidationError = this.validationErrors[fieldName];
    return !!(field && field.invalid && field.touched) || !!hasValidationError;
  }

  getFieldError(fieldName: string): string {
    // Verificar se há erro customizado primeiro
    if (this.validationErrors[fieldName]) {
      return this.validationErrors[fieldName];
    }

    const field = this.produtoForm.get(fieldName);
    if (field?.errors) {
      if (field.errors['required']) {
        return this.getFieldDisplayName(fieldName) + ' é obrigatório';
      }
      if (field.errors['min']) {
        return `Valor mínimo: ${field.errors['min'].min}`;
      }
      if (field.errors['max']) {
        return `Valor máximo: ${field.errors['max'].max}`;
      }
      if (field.errors['maxlength']) {
        return `Máximo ${field.errors['maxlength'].requiredLength} caracteres`;
      }
    }
    return '';
  }

  private getFieldDisplayName(fieldName: string): string {
    const displayNames: { [key: string]: string } = {
      'title': 'Título',
      'description': 'Descrição',
      'categoria': 'Categoria',
      'initialPrice': 'Preço inicial',
      'reservePrice': 'Preço de reserva',
      'incrementMin': 'Incremento mínimo',
      'endDateTime': 'Data de encerramento',
      'weight': 'Peso',
      'dimensions': 'Dimensões',
      'antiSnipeExtension': 'Extensão anti-snipe'
    };
    return displayNames[fieldName] || fieldName;
  }

  hasValidationErrors(): boolean {
    return Object.keys(this.validationErrors).length > 0;
  }

  getValidationErrorsList(): string[] {
    return Object.values(this.validationErrors);
  }

  cancelar() {
    this.router.navigate(['/produtos/meus-produtos']);
  }
}