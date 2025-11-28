import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { LoteService } from '../../core/services/lote.service';
import { ProdutoService } from '../../core/services/produto.service';
import { AuthService } from '../../core/services/auth.service';
import { Lote, LoteCreateRequest, LoteUpdateRequest } from '../../core/models/lote.model';
import { Produto } from '../../core/models/produto.model';

@Component({
  selector: 'app-lote-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './lote-form.component.html',
  styleUrls: ['./lote-form.component.scss']
})
export class LoteFormComponent implements OnInit {
  
  loteForm!: FormGroup;
  isEditMode = false;
  loteId?: string;
  loading = false;
  error = '';
  success = '';
  
  // Produtos disponíveis para associação
  produtosDisponiveis: Produto[] = [];
  produtosSelecionados: Produto[] = [];
  loadingProdutos = false;

  constructor(
    private fb: FormBuilder,
    private loteService: LoteService,
    private produtoService: ProdutoService,
    public authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.createForm();
  }

  ngOnInit(): void {
    this.loteId = this.route.snapshot.paramMap.get('id') || undefined;
    this.isEditMode = !!this.loteId;
    
    this.carregarProdutosDisponiveis();
    
    if (this.isEditMode && this.loteId) {
      this.carregarLote();
    }
  }

  private createForm(): void {
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    tomorrow.setHours(18, 0, 0, 0); // 18:00 do dia seguinte por padrão
    
    this.loteForm = this.fb.group({
      title: ['', [Validators.required, Validators.maxLength(255)]],
      description: ['', [Validators.maxLength(5000)]],
      loteEndDateTime: [tomorrow.toISOString().slice(0, 16), [Validators.required]],
      produtoIds: this.fb.array([])
    });
  }

  get produtoIdsArray(): FormArray {
    return this.loteForm.get('produtoIds') as FormArray;
  }

  private carregarProdutosDisponiveis(): void {
    this.loadingProdutos = true;
    
    this.produtoService.listarMeusProdutos(0, 100).subscribe({
      next: (response) => {
        if (response.success) {
          // Filtrar apenas produtos em DRAFT que não estão em lotes
          this.produtosDisponiveis = response.data.content.filter(
            produto => produto.status === 'DRAFT' && !produto.loteId
          );
        }
        this.loadingProdutos = false;
      },
      error: (error) => {
        console.error('Erro ao carregar produtos:', error);
        this.error = 'Erro ao carregar produtos disponíveis';
        this.loadingProdutos = false;
      }
    });
  }

  private carregarLote(): void {
    if (!this.loteId) return;
    
    this.loading = true;
    
    this.loteService.buscarLote(this.loteId).subscribe({
      next: (response) => {
        if (response.success) {
          this.preencherFormulario(response.data);
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('Erro ao carregar lote:', error);
        this.error = 'Erro ao carregar lote';
        this.loading = false;
      }
    });
  }

  private preencherFormulario(lote: Lote): void {
    // Converter data para formato do input datetime-local
    const endDateTime = new Date(lote.loteEndDateTime);
    const formattedDateTime = endDateTime.toISOString().slice(0, 16);
    
    this.loteForm.patchValue({
      title: lote.title,
      description: lote.description,
      loteEndDateTime: formattedDateTime
    });

    // Preencher produtos selecionados
    this.produtoIdsArray.clear();
    lote.produtoIds.forEach(produtoId => {
      this.produtoIdsArray.push(this.fb.control(produtoId));
    });
    
    this.atualizarProdutosSelecionados();
  }

  onSubmit(): void {
    if (this.loteForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    this.loading = true;
    this.error = '';
    this.success = '';

    const formValue = this.loteForm.value;
    
    // Converter data para ISO string
    const endDateTime = new Date(formValue.loteEndDateTime).toISOString();
    
    const loteData = {
      ...formValue,
      loteEndDateTime: endDateTime,
      produtoIds: this.produtoIdsArray.value
    };

    if (this.isEditMode && this.loteId) {
      this.atualizarLote(loteData);
    } else {
      this.criarLote(loteData);
    }
  }

  private criarLote(loteData: LoteCreateRequest): void {
    this.loteService.criarLote(loteData).subscribe({
      next: (response) => {
        if (response.success) {
          this.success = 'Lote criado com sucesso!';
          setTimeout(() => {
            this.router.navigate(['/lotes']);
          }, 2000);
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('Erro ao criar lote:', error);
        this.error = error.error?.message || 'Erro ao criar lote';
        this.loading = false;
      }
    });
  }

  private atualizarLote(loteData: LoteUpdateRequest): void {
    if (!this.loteId) return;
    
    this.loteService.atualizarLote(this.loteId, loteData).subscribe({
      next: (response) => {
        if (response.success) {
          this.success = 'Lote atualizado com sucesso!';
          setTimeout(() => {
            this.router.navigate(['/lotes']);
          }, 2000);
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('Erro ao atualizar lote:', error);
        this.error = error.error?.message || 'Erro ao atualizar lote';
        this.loading = false;
      }
    });
  }

  onProdutoToggle(produto: Produto, event: any): void {
    const isChecked = event.target.checked;
    
    if (isChecked) {
      // Adicionar produto
      this.produtoIdsArray.push(this.fb.control(produto.id));
    } else {
      // Remover produto
      const index = this.produtoIdsArray.controls.findIndex(
        control => control.value === produto.id
      );
      if (index >= 0) {
        this.produtoIdsArray.removeAt(index);
      }
    }
    
    this.atualizarProdutosSelecionados();
  }

  private atualizarProdutosSelecionados(): void {
    const selectedIds = this.produtoIdsArray.value;
    this.produtosSelecionados = this.produtosDisponiveis.filter(
      produto => selectedIds.includes(produto.id)
    );
  }

  isProdutoSelecionado(produtoId: string): boolean {
    return this.produtoIdsArray.value.includes(produtoId);
  }

  cancelar(): void {
    this.router.navigate(['/lotes']);
  }

  // Métodos de validação
  isFieldInvalid(fieldName: string): boolean {
    const field = this.loteForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }

  getFieldError(fieldName: string): string {
    const field = this.loteForm.get(fieldName);
    if (field && field.errors) {
      if (field.errors['required']) {
        return `${this.getFieldLabel(fieldName)} é obrigatório`;
      }
      if (field.errors['maxlength']) {
        return `${this.getFieldLabel(fieldName)} deve ter no máximo ${field.errors['maxlength'].requiredLength} caracteres`;
      }
    }
    return '';
  }

  private getFieldLabel(fieldName: string): string {
    const labels: { [key: string]: string } = {
      'title': 'Título',
      'description': 'Descrição',
      'loteEndDateTime': 'Data de encerramento'
    };
    return labels[fieldName] || fieldName;
  }

  private markFormGroupTouched(): void {
    Object.keys(this.loteForm.controls).forEach(key => {
      const control = this.loteForm.get(key);
      control?.markAsTouched();
    });
  }
}