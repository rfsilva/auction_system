import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-produto-image',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './produto-image.component.html',
  styleUrls: ['./produto-image.component.scss']
})
export class ProdutoImageComponent implements OnInit {
  @Input() src: string | undefined = '';
  @Input() alt: string = '';
  @Input() height: number = 200;

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
    try {
      const urlObj = new URL(url);
      return urlObj.protocol === 'http:' || urlObj.protocol === 'https:';
    } catch {
      return false;
    }
  }

  shouldShowImage(): boolean {
    // Conversão explícita para boolean para evitar erro de tipo
    return Boolean(this.imageSrc) && !this.imageError && !this.isExampleUrl;
  }

  onImageLoad() {
    this.imageLoaded = true;
    this.imageError = false;
    console.log('Imagem carregada com sucesso:', this.imageSrc);
  }

  onImageError() {
    this.imageError = true;
    this.imageLoaded = false;
    console.log('Erro ao carregar imagem:', this.imageSrc);
  }

  getPlaceholderIcon(): string {
    if (this.isExampleUrl) {
      return '🖼️';
    } else if (this.imageError) {
      return '❌';
    } else {
      return '⏳';
    }
  }

  getPlaceholderText(): string {
    if (this.isExampleUrl) {
      return 'Imagem de Exemplo';
    } else if (this.imageError) {
      return 'Imagem Indisponível';
    } else {
      return 'Carregando...';
    }
  }
}