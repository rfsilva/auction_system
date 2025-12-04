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
  @Input() src: string | undefined = '';
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

  ngOnInit() {
    this.setImageSrc();
  }

  private setImageSrc() {
    // Verificar se é uma URL de exemplo (que sabemos que não funciona)
    if (this.src && this.isExampleDomain(this.src)) {
      this.isExampleUrl = true;
      this.imageError = true;
      console.log('URL de exemplo detectada, usando placeholder:', this.src);
      return;
    }

    // Verificar se a URL é válida
    if (this.src && this.isValidUrl(this.src)) {
      this.imageSrc = this.src;
    } else {
      this.imageError = true;
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
    return Boolean(this.imageSrc) && !this.imageError && !this.isExampleUrl;
  }

  onImageLoad() {
    this.imageLoaded = true;
    this.imageError = false;
    console.log('Imagem do lote carregada com sucesso:', this.imageSrc);
  }

  onImageError() {
    this.imageError = true;
    this.imageLoaded = false;
    console.log('Erro ao carregar imagem do lote:', this.imageSrc);
  }

  getPlaceholderIcon(): string {
    if (this.isExampleUrl) {
      return '📦';
    } else if (this.imageError) {
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
    } else {
      return 'Carregando...';
    }
  }
}