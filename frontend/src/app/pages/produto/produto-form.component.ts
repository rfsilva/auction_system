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
    if (this.produtoForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    this.loading = true;
    this.error = '';
    this.success = '';

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
          this.error = error.error?.message || 'Erro ao atualizar produto';
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
          this.error = error.error?.message || 'Erro ao criar produto';
          this.loading = false;
        }
      });
    }
  }

  private markFormGroupTouched() {
    Object.keys(this.produtoForm.controls).forEach(key => {
      const control = this.produtoForm.get(key);
      control?.markAsTouched();
    });
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.produtoForm.get(fieldName);
    return !!(field && field.invalid && field.touched);
  }

  getFieldError(fieldName: string): string {
    const field = this.produtoForm.get(fieldName);
    if (field?.errors) {
      if (field.errors['required']) return `${fieldName} é obrigatório`;
      if (field.errors['min']) return `Valor mínimo: ${field.errors['min'].min}`;
      if (field.errors['max']) return `Valor máximo: ${field.errors['max'].max}`;
      if (field.errors['maxlength']) return `Máximo ${field.errors['maxlength'].requiredLength} caracteres`;
    }
    return '';
  }

  cancelar() {
    this.router.navigate(['/produtos/meus-produtos']);
  }
}