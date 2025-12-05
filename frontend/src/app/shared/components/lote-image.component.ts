import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-lote-image',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './lote-image.component.html',
  styleUrls: ['./lote-image.component.scss']
})
export class LoteImageComponent implements OnInit {
  @Input() src: string | null | undefined = null;
  @Input() alt: string = '';
  @Input() height: number = 200;
  @Input() showBadges: boolean = true;
  @Input() statusClass: string = '';
  @Input() statusText: string = '';
  @Input() produtoCount: number = 0;

  imageSrc: string = '';
  imageLoaded: boolean = false;
  imageError: boolean = false;
  isExampleUrl: boolean = false;
  showPlaceholder: boolean = true;

  ngOnInit() {
    this.setImageSrc();
  }

  private setImageSrc() {
    // Se não há src ou é null/undefined, usar placeholder
    if (!this.src || this.src.trim() === '') {
      this.showPlaceholder = true;
      this.imageError = false;
      this.isExampleUrl = false;
      return;
    }

    // Verificar se é uma URL de exemplo (que sabemos que não funciona)
    if (this.isExampleDomain(this.src)) {
      this.isExampleUrl = true;
      this.imageError = true;
      this.showPlaceholder = true;
      console.log('URL de exemplo detectada, usando placeholder:', this.src);
      return;
    }

    // Verificar se a URL é válida
    if (this.isValidUrl(this.src)) {
      this.imageSrc = this.src;
      this.showPlaceholder = false;
    } else {
      this.imageError = true;
      this.showPlaceholder = true;
    }
  }

  private isExampleDomain(url: string): boolean {
    return url.includes('example.com') || 
           url.includes('placeholder.com') || 
           url.includes('via.placeholder.com') ||
           url.includes('picsum.photos') ||
           url.startsWith('http://example') ||
           url.startsWith('https://example');
  }

  private isValidUrl(url: string): boolean {
    if (!url || url.trim() === '') {
      return false;
    }
    
    try {
      const urlObj = new URL(url);
      return urlObj.protocol === 'http:' || urlObj.protocol === 'https:';
    } catch {
      // Se não for URL absoluta, assumir que é relativa e válida se não estiver vazia
      return url.startsWith('/') || url.startsWith('./') || !url.includes('://');
    }
  }

  shouldShowImage(): boolean {
    return Boolean(this.imageSrc) && !this.imageError && !this.isExampleUrl && !this.showPlaceholder;
  }

  shouldShowPlaceholder(): boolean {
    return this.showPlaceholder || this.imageError || this.isExampleUrl || !this.imageSrc;
  }

  onImageLoad() {
    this.imageLoaded = true;
    this.imageError = false;
    this.showPlaceholder = false;
    console.log('Imagem do lote carregada com sucesso:', this.imageSrc);
  }

  onImageError() {
    this.imageError = true;
    this.imageLoaded = false;
    this.showPlaceholder = true;
    console.log('Erro ao carregar imagem do lote:', this.imageSrc);
  }

  getPlaceholderIcon(): string {
    if (this.isExampleUrl) {
      return '📦';
    } else if (this.imageError) {
      return '📦';
    } else if (!this.src) {
      return '📦';
    } else {
      return '⏳';
    }
  }

  getPlaceholderText(): string {
    if (this.isExampleUrl) {
      return 'Lote de Exemplo';
    } else if (this.imageError) {
      return 'Sem Imagem';
    } else if (!this.src) {
      return 'Lote';
    } else {
      return 'Carregando...';
    }
  }
}