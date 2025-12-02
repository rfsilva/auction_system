import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { LoteService } from '../../core/services/lote.service';
import { ProdutoService } from '../../core/services/produto.service';
import { ContratoService } from '../../core/services/contrato.service';
import { AuthService } from '../../core/services/auth.service';
import { Lote, LoteCreateRequest, LoteUpdateRequest } from '../../core/models/lote.model';
import { Produto } from '../../core/models/produto.model';
import { Contrato } from '../../core/models/contrato.model';

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
  
  // Contratos ativos do vendedor
  contratosAtivos: Contrato[] = [];
  loadingContratos = false;
  
  // Categorias disponíveis
  categorias: string[] = [];
  loadingCategorias = false;

  constructor(
    private fb: FormBuilder,
    private loteService: LoteService,
    private produtoService: ProdutoService,
    public contratoService: ContratoService, // Tornado público para uso no template
    public authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.createForm();
  }

  ngOnInit(): void {
    this.loteId = this.route.snapshot.paramMap.get('id') || undefined;
    this.isEditMode = !!this.loteId;
    
    this.carregarContratosAtivos();
    this.carregarCategorias();
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
      contractId: ['', [Validators.required]], // Campo obrigatório para seleção do contrato
      categoria: [''], // Categoria para filtrar contratos (opcional)
      produtoIds: this.fb.array([])
    });
  }

  get produtoIdsArray(): FormArray {
    return this.loteForm.get('produtoIds') as FormArray;
  }

  private carregarContratosAtivos(): void {
    this.loadingContratos = true;
    
    this.contratoService.listarMeusContratosAtivos().subscribe({
      next: (response) => {
        if (response.success) {
          this.contratosAtivos = response.data;
          
          // Se há apenas um contrato ativo, selecionar automaticamente
          if (this.contratosAtivos.length === 1 && !this.isEditMode) {
            this.loteForm.patchValue({
              contractId: this.contratosAtivos[0].id
            });
          }
        }
        this.loadingContratos = false;
      },
      error: (error) => {
        console.error('Erro ao carregar contratos ativos:', error);
        this.error = 'Erro ao carregar contratos ativos. Verifique se você possui contratos ativos.';
        this.loadingContratos = false;
      }
    });
  }

  private carregarCategorias(): void {
    this.loadingCategorias = true;
    
    this.contratoService.listarCategorias().subscribe({
      next: (response) => {
        if (response.success) {
          this.categorias = response.data;
        }
        this.loadingCategorias = false;
      },
      error: (error) => {
        console.error('Erro ao carregar categorias:', error);
        this.loadingCategorias = false;
      }
    });
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
      loteEndDateTime: formattedDateTime,
      contractId: lote.contractId,
      categoria: lote.categoria || ''
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
      produtoIds: this.produtoIdsArray.value,
      contractId: formValue.contractId, // Incluir o ID do contrato selecionado
      categoria: formValue.categoria || undefined // Não enviar string vazia
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

  // Filtrar contratos por categoria selecionada
  getContratosFiltrados(): Contrato[] {
    const categoriaSelecionada = this.loteForm.get('categoria')?.value;
    
    if (!categoriaSelecionada) {
      return this.contratosAtivos;
    }
    
    return this.contratosAtivos.filter(contrato => 
      contrato.categoria === categoriaSelecionada || !contrato.categoria
    );
  }

  // Obter contrato selecionado
  getContratoSelecionado(): Contrato | null {
    const contractId = this.loteForm.get('contractId')?.value;
    return this.contratosAtivos.find(c => c.id === contractId) || null;
  }

  // Formatar contrato para exibição
  formatarContrato(contrato: Contrato): string {
    return this.contratoService.formatarContratoParaSelecao(contrato);
  }

  // Métodos públicos para uso no template
  formatarTaxa(taxa: number): string {
    return this.contratoService.formatarTaxa(taxa);
  }

  formatarData(dataString: string): string {
    return this.contratoService.formatarData(dataString);
  }

  getStatusText(status: any): string {
    return this.contratoService.getStatusText(status);
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
      'loteEndDateTime': 'Data de encerramento',
      'contractId': 'Contrato',
      'categoria': 'Categoria'
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