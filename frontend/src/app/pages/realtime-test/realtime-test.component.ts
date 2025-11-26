import { Component, OnInit, OnDestroy, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RealtimeService, RealtimeEvent } from '../../core/services/realtime.service';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-realtime-test',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './realtime-test.component.html',
  styleUrls: ['./realtime-test.component.scss']
})
export class RealtimeTestComponent implements OnInit, OnDestroy {
  testMessage = 'Mensagem de teste';
  bidAmount = 100;
  concurrencyCount = 50;
  concurrencyInterval = 100;

  // URLs do environment para exibição
  readonly apiUrl = environment.apiUrl;
  readonly wsUrl = environment.wsUrl;
  readonly sseUrl = environment.sseUrl;

  constructor(
    public realtimeService: RealtimeService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    console.log('RealtimeTestComponent inicializado');
    console.log('Environment URLs:', {
      apiUrl: this.apiUrl,
      wsUrl: this.wsUrl,
      sseUrl: this.sseUrl
    });
  }

  ngOnDestroy(): void {
    this.realtimeService.disconnectAll();
  }

  // Métodos de conexão
  connectSSE(): void {
    this.realtimeService.connectSSE();
  }

  disconnectSSE(): void {
    this.realtimeService.disconnectSSE();
  }

  connectWebSocket(): void {
    this.realtimeService.connectWebSocket();
  }

  disconnectWebSocket(): void {
    this.realtimeService.disconnectWebSocket();
  }

  // Métodos de teste
  triggerBroadcast(): void {
    const message = `Teste broadcast - ${new Date().toLocaleTimeString()}`;
    const url = `${this.apiUrl}/realtime/broadcast`;
    
    console.log('Enviando broadcast para:', url);
    
    this.http.post(url, null, {
      params: { message }
    }).subscribe({
      next: (response) => {
        console.log('Broadcast enviado:', response);
      },
      error: (error) => {
        console.error('Erro ao enviar broadcast:', error);
      }
    });
  }

  startSimulation(): void {
    const url = `${this.apiUrl}/realtime/start-simulation`;
    
    console.log('Iniciando simulação em:', url);
    
    this.http.post(url, null).subscribe({
      next: (response) => {
        console.log('Simulação iniciada:', response);
      },
      error: (error) => {
        console.error('Erro ao iniciar simulação:', error);
      }
    });
  }

  sendTestMessage(): void {
    if (this.testMessage.trim()) {
      this.realtimeService.sendTestMessage(this.testMessage);
    }
  }

  sendTestBid(): void {
    if (this.bidAmount > 0) {
      this.realtimeService.sendBid('test-product-123', this.bidAmount);
    }
  }

  testLatency(): void {
    this.realtimeService.testLatency();
  }

  startConcurrencyTest(): void {
    this.realtimeService.startConcurrencyTest(this.concurrencyCount, this.concurrencyInterval);
  }

  clearEvents(): void {
    this.realtimeService.clearEvents();
  }

  // Métodos utilitários
  formatTime(timestamp: string): string {
    return new Date(timestamp).toLocaleTimeString();
  }

  formatEventData(data: any): string {
    if (typeof data === 'string') {
      return data;
    }
    return JSON.stringify(data, null, 2);
  }
}