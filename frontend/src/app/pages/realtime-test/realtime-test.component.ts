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
  template: `
    <div class="realtime-test-container">
      <div class="header">
        <h1>🚀 Teste de Comunicação em Tempo Real</h1>
        <p>Prova de conceito SSE/WebSocket - História 5 Sprint 1</p>
      </div>

      <!-- Status das Conexões -->
      <div class="connection-status">
        <h2>📡 Status das Conexões</h2>
        <div class="status-grid">
          <div class="status-card" [class.connected]="realtimeService.connectionStatus().sse">
            <div class="status-indicator"></div>
            <div class="status-info">
              <h3>SSE (Server-Sent Events)</h3>
              <p>{{ realtimeService.connectionStatus().sse ? 'Conectado' : 'Desconectado' }}</p>
            </div>
            <div class="status-actions">
              <button 
                (click)="connectSSE()" 
                [disabled]="realtimeService.connectionStatus().sse"
                class="btn btn-primary">
                Conectar
              </button>
              <button 
                (click)="disconnectSSE()" 
                [disabled]="!realtimeService.connectionStatus().sse"
                class="btn btn-secondary">
                Desconectar
              </button>
            </div>
          </div>

          <div class="status-card" [class.connected]="realtimeService.connectionStatus().websocket">
            <div class="status-indicator"></div>
            <div class="status-info">
              <h3>WebSocket</h3>
              <p>{{ realtimeService.connectionStatus().websocket ? 'Conectado' : 'Desconectado' }}</p>
            </div>
            <div class="status-actions">
              <button 
                (click)="connectWebSocket()" 
                [disabled]="realtimeService.connectionStatus().websocket"
                class="btn btn-primary">
                Conectar
              </button>
              <button 
                (click)="disconnectWebSocket()" 
                [disabled]="!realtimeService.connectionStatus().websocket"
                class="btn btn-secondary">
                Desconectar
              </button>
            </div>
          </div>
        </div>

        <div class="connection-stats">
          <div class="stat">
            <span class="stat-label">Eventos Recebidos:</span>
            <span class="stat-value">{{ realtimeService.connectionStatus().eventCount }}</span>
          </div>
          <div class="stat">
            <span class="stat-label">Último Evento:</span>
            <span class="stat-value">{{ realtimeService.connectionStatus().lastEvent || 'Nenhum' }}</span>
          </div>
          <div class="stat">
            <span class="stat-label">Status Geral:</span>
            <span class="stat-value" [class.connected]="realtimeService.isConnected()">
              {{ realtimeService.isConnected() ? 'Online' : 'Offline' }}
            </span>
          </div>
        </div>
      </div>

      <!-- Testes de Funcionalidade -->
      <div class="test-section">
        <h2>🧪 Testes de Funcionalidade</h2>
        
        <div class="test-grid">
          <!-- Teste SSE -->
          <div class="test-card">
            <h3>📡 Teste SSE</h3>
            <p>Testa eventos Server-Sent Events</p>
            <div class="test-actions">
              <button (click)="triggerBroadcast()" class="btn btn-primary">
                Enviar Broadcast
              </button>
              <button (click)="startSimulation()" class="btn btn-secondary">
                Iniciar Simulação
              </button>
            </div>
          </div>

          <!-- Teste WebSocket -->
          <div class="test-card">
            <h3>🔄 Teste WebSocket</h3>
            <div class="websocket-test">
              <input 
                [(ngModel)]="testMessage" 
                placeholder="Digite uma mensagem de teste"
                class="form-input">
              <button 
                (click)="sendTestMessage()" 
                [disabled]="!realtimeService.connectionStatus().websocket"
                class="btn btn-primary">
                Enviar Mensagem
              </button>
            </div>
            <div class="websocket-test">
              <input 
                [(ngModel)]="bidAmount" 
                type="number" 
                placeholder="Valor do lance"
                class="form-input">
              <button 
                (click)="sendTestBid()" 
                [disabled]="!realtimeService.connectionStatus().websocket"
                class="btn btn-success">
                Simular Lance
              </button>
            </div>
          </div>

          <!-- Teste de Latência -->
          <div class="test-card">
            <h3>⚡ Teste de Latência</h3>
            <div class="latency-stats">
              <div class="latency-stat">
                <span>Mín:</span>
                <span>{{ realtimeService.latencyStats().min }}ms</span>
              </div>
              <div class="latency-stat">
                <span>Máx:</span>
                <span>{{ realtimeService.latencyStats().max }}ms</span>
              </div>
              <div class="latency-stat">
                <span>Média:</span>
                <span>{{ realtimeService.latencyStats().avg }}ms</span>
              </div>
              <div class="latency-stat">
                <span>Amostras:</span>
                <span>{{ realtimeService.latencyStats().count }}</span>
              </div>
            </div>
            <button (click)="testLatency()" class="btn btn-primary">
              Testar Latência
            </button>
          </div>

          <!-- Teste de Concorrência -->
          <div class="test-card">
            <h3>🏁 Teste de Concorrência</h3>
            <div class="concurrency-controls">
              <div class="form-group">
                <label>Número de mensagens:</label>
                <input [(ngModel)]="concurrencyCount" type="number" min="1" max="1000" class="form-input">
              </div>
              <div class="form-group">
                <label>Intervalo (ms):</label>
                <input [(ngModel)]="concurrencyInterval" type="number" min="10" max="5000" class="form-input">
              </div>
            </div>
            <button 
              (click)="startConcurrencyTest()" 
              [disabled]="!realtimeService.connectionStatus().websocket"
              class="btn btn-warning">
              Iniciar Teste
            </button>
          </div>
        </div>
      </div>

      <!-- Log de Eventos -->
      <div class="events-section">
        <div class="events-header">
          <h2>📋 Log de Eventos (Últimos 10)</h2>
          <button (click)="clearEvents()" class="btn btn-secondary">
            Limpar Log
          </button>
        </div>
        
        <div class="events-list">
          @if (realtimeService.lastEvents().length === 0) {
            <div class="no-events">
              <p>Nenhum evento recebido ainda. Conecte-se e teste as funcionalidades acima.</p>
            </div>
          } @else {
            @for (event of realtimeService.lastEvents(); track event.timestamp) {
              <div class="event-item" [attr.data-event-type]="event.name">
                <div class="event-header">
                  <span class="event-name">{{ event.name }}</span>
                  <span class="event-time">{{ formatTime(event.timestamp) }}</span>
                </div>
                <div class="event-data">
                  <pre>{{ formatEventData(event.data) }}</pre>
                </div>
              </div>
            }
          }
        </div>
      </div>

      <!-- Informações Técnicas -->
      <div class="tech-info">
        <h2>🔧 Informações Técnicas</h2>
        <div class="info-grid">
          <div class="info-card">
            <h3>SSE (Server-Sent Events)</h3>
            <ul>
              <li>Endpoint: <code>{{ sseUrl }}</code></li>
              <li>Unidirecional (servidor → cliente)</li>
              <li>Reconexão automática</li>
              <li>Ideal para espectadores</li>
            </ul>
          </div>
          <div class="info-card">
            <h3>WebSocket</h3>
            <ul>
              <li>Endpoint: <code>{{ wsUrl }}</code></li>
              <li>Bidirecional (cliente ↔ servidor)</li>
              <li>Baixa latência</li>
              <li>Ideal para participantes ativos</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  `,
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