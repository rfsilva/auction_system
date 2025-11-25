import { Injectable, signal, computed } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface RealtimeEvent {
  name: string;
  data: any;
  timestamp: string;
}

export interface ConnectionStatus {
  sse: boolean;
  websocket: boolean;
  lastEvent?: string;
  eventCount: number;
}

// Definição simples do STOMP para evitar dependências externas
interface StompClient {
  connect(headers: any, connectCallback: () => void, errorCallback?: (error: any) => void): void;
  disconnect(disconnectCallback?: () => void): void;
  send(destination: string, headers: any, body: string): void;
  subscribe(destination: string, callback: (message: any) => void): any;
}

declare global {
  interface Window {
    Stomp: {
      over(ws: WebSocket): StompClient;
    };
  }
}

@Injectable({
  providedIn: 'root'
})
export class RealtimeService {
  private eventSource: EventSource | null = null;
  private websocket: WebSocket | null = null;
  private stompClient: StompClient | null = null;
  
  // URLs do environment
  private readonly sseUrl = environment.sseUrl;
  private readonly wsUrl = environment.wsUrl;
  private readonly apiUrl = environment.apiUrl;
  
  // Signals para estado reativo
  private _connectionStatus = signal<ConnectionStatus>({
    sse: false,
    websocket: false,
    eventCount: 0
  });
  
  private _events = signal<RealtimeEvent[]>([]);
  private _latencyStats = signal<{
    min: number;
    max: number;
    avg: number;
    count: number;
  }>({
    min: 0,
    max: 0,
    avg: 0,
    count: 0
  });

  // Subjects para observables
  private eventsSubject = new Subject<RealtimeEvent>();
  private connectionSubject = new BehaviorSubject<ConnectionStatus>({
    sse: false,
    websocket: false,
    eventCount: 0
  });

  // Getters para signals
  connectionStatus = this._connectionStatus.asReadonly();
  events = this._events.asReadonly();
  latencyStats = this._latencyStats.asReadonly();
  
  // Computed signals
  isConnected = computed(() => {
    const status = this._connectionStatus();
    return status.sse || status.websocket;
  });

  lastEvents = computed(() => {
    return this._events().slice(-10); // Últimos 10 eventos
  });

  // Observables para compatibilidade
  events$ = this.eventsSubject.asObservable();
  connectionStatus$ = this.connectionSubject.asObservable();

  private latencyMeasurements: number[] = [];

  constructor() {
    console.log('RealtimeService initialized');
    console.log('SSE URL:', this.sseUrl);
    console.log('WebSocket URL:', this.wsUrl);
    console.log('API URL:', this.apiUrl);
  }

  /**
   * Conecta ao SSE para receber eventos em tempo real
   */
  connectSSE(): void {
    if (this.eventSource) {
      console.log('SSE já está conectado');
      return;
    }

    try {
      console.log('Conectando ao SSE:', this.sseUrl);
      this.eventSource = new EventSource(this.sseUrl);
      
      this.eventSource.onopen = () => {
        console.log('SSE conectado com sucesso');
        this.updateConnectionStatus({ sse: true });
      };

      this.eventSource.onmessage = (event) => {
        console.log('SSE mensagem recebida:', event);
        this.handleSSEEvent('message', event.data);
      };

      this.eventSource.addEventListener('connected', (event) => {
        console.log('SSE evento connected:', event);
        this.handleSSEEvent('connected', (event as MessageEvent).data);
      });

      this.eventSource.addEventListener('test-event', (event) => {
        console.log('SSE evento test-event:', event);
        this.handleSSEEvent('test-event', (event as MessageEvent).data);
      });

      this.eventSource.addEventListener('simulation', (event) => {
        console.log('SSE evento simulation:', event);
        this.handleSSEEvent('simulation', (event as MessageEvent).data);
      });

      this.eventSource.onerror = (error) => {
        console.error('Erro no SSE:', error);
        console.error('SSE readyState:', this.eventSource?.readyState);
        this.updateConnectionStatus({ sse: false });
        
        // Reconexão automática após 3 segundos
        setTimeout(() => {
          if (this.eventSource?.readyState === EventSource.CLOSED) {
            console.log('Tentando reconectar SSE...');
            this.disconnectSSE();
            this.connectSSE();
          }
        }, 3000);
      };

    } catch (error) {
      console.error('Erro ao conectar SSE:', error);
      this.updateConnectionStatus({ sse: false });
    }
  }

  /**
   * Desconecta do SSE
   */
  disconnectSSE(): void {
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = null;
      this.updateConnectionStatus({ sse: false });
      console.log('SSE desconectado');
    }
  }

  /**
   * Conecta ao WebSocket usando implementação nativa (sem STOMP por enquanto)
   */
  connectWebSocket(): void {
    if (this.websocket) {
      console.log('WebSocket já está conectado');
      return;
    }

    try {
      console.log('Conectando ao WebSocket:', this.wsUrl);
      this.websocket = new WebSocket(this.wsUrl);

      this.websocket.onopen = () => {
        console.log('WebSocket conectado com sucesso');
        this.updateConnectionStatus({ websocket: true });
        
        // Simular conexão STOMP básica
        this.simulateStompConnection();
      };

      this.websocket.onmessage = (event) => {
        console.log('WebSocket mensagem recebida:', event);
        this.handleWebSocketEvent('message', event.data);
      };

      this.websocket.onerror = (error) => {
        console.error('Erro no WebSocket:', error);
        this.updateConnectionStatus({ websocket: false });
      };

      this.websocket.onclose = () => {
        console.log('WebSocket desconectado');
        this.updateConnectionStatus({ websocket: false });
        this.websocket = null;
        
        // Reconexão automática após 3 segundos
        setTimeout(() => {
          console.log('Tentando reconectar WebSocket...');
          this.connectWebSocket();
        }, 3000);
      };

    } catch (error) {
      console.error('Erro ao conectar WebSocket:', error);
      this.updateConnectionStatus({ websocket: false });
    }
  }

  /**
   * Simula uma conexão STOMP básica para testes
   */
  private simulateStompConnection(): void {
    if (!this.websocket) return;

    // Enviar comando CONNECT do STOMP
    const connectFrame = 'CONNECT\naccept-version:1.0,1.1,2.0\n\n\x00';
    this.websocket.send(connectFrame);

    // Simular subscrições
    setTimeout(() => {
      this.subscribeToTopic('/topic/test');
      this.subscribeToTopic('/topic/bids');
      this.subscribeToTopic('/topic/broadcast');
      this.subscribeToTopic('/topic/simulation');
    }, 100);
  }

  /**
   * Simula subscrição a um tópico STOMP
   */
  private subscribeToTopic(destination: string): void {
    if (!this.websocket) return;

    const subscribeFrame = `SUBSCRIBE\ndestination:${destination}\nid:sub-${Date.now()}\n\n\x00`;
    this.websocket.send(subscribeFrame);
    console.log(`Subscrito ao tópico: ${destination}`);
  }

  /**
   * Desconecta do WebSocket
   */
  disconnectWebSocket(): void {
    if (this.websocket) {
      // Enviar comando DISCONNECT do STOMP
      const disconnectFrame = 'DISCONNECT\n\n\x00';
      this.websocket.send(disconnectFrame);
      
      setTimeout(() => {
        if (this.websocket) {
          this.websocket.close();
          this.websocket = null;
        }
      }, 100);
      
      this.updateConnectionStatus({ websocket: false });
      console.log('WebSocket desconectado');
    }
  }

  /**
   * Envia mensagem via WebSocket usando protocolo STOMP
   */
  sendWebSocketMessage(destination: string, message: any): void {
    if (!this.websocket || this.websocket.readyState !== WebSocket.OPEN) {
      console.error('WebSocket não está conectado');
      return;
    }

    const body = JSON.stringify({
      ...message,
      timestamp: Date.now()
    });

    const sendFrame = `SEND\ndestination:${destination}\ncontent-type:application/json\n\n${body}\x00`;
    this.websocket.send(sendFrame);
    console.log('Mensagem enviada via WebSocket:', { destination, message });
  }

  /**
   * Envia mensagem de teste via WebSocket
   */
  sendTestMessage(message: string): void {
    this.sendWebSocketMessage('/app/test', { 
      message, 
      type: 'test',
      clientId: 'test-client'
    });
  }

  /**
   * Simula um lance via WebSocket
   */
  sendBid(productId: string, amount: number): void {
    this.sendWebSocketMessage('/app/bid', { 
      productId, 
      amount,
      type: 'bid',
      clientId: 'test-client'
    });
  }

  /**
   * Testa latência enviando ping
   */
  testLatency(): void {
    const startTime = Date.now();
    this.sendWebSocketMessage('/app/ping', { 
      type: 'ping', 
      timestamp: startTime,
      clientId: 'test-client'
    });
  }

  /**
   * Inicia teste de concorrência
   */
  startConcurrencyTest(messageCount: number = 100, intervalMs: number = 100): void {
    console.log(`Iniciando teste de concorrência: ${messageCount} mensagens a cada ${intervalMs}ms`);
    
    let count = 0;
    const interval = setInterval(() => {
      if (count >= messageCount) {
        clearInterval(interval);
        console.log('Teste de concorrência finalizado');
        return;
      }

      this.sendTestMessage(`Teste concorrência #${count + 1} - ${Date.now()}`);
      count++;
    }, intervalMs);
  }

  /**
   * Limpa histórico de eventos
   */
  clearEvents(): void {
    this._events.set([]);
    this.latencyMeasurements = [];
    this.updateLatencyStats();
    console.log('Histórico de eventos limpo');
  }

  /**
   * Desconecta todos os canais
   */
  disconnectAll(): void {
    this.disconnectSSE();
    this.disconnectWebSocket();
  }

  private handleSSEEvent(eventName: string, data: string): void {
    const event: RealtimeEvent = {
      name: eventName,
      data: this.tryParseJSON(data),
      timestamp: new Date().toISOString()
    };

    this.addEvent(event);
    this.measureLatency(event);
  }

  private handleWebSocketEvent(eventName: string, data: string): void {
    // Processar frames STOMP
    if (data.startsWith('CONNECTED')) {
      console.log('STOMP conectado');
      return;
    }

    if (data.startsWith('MESSAGE')) {
      // Parsear frame MESSAGE do STOMP
      const lines = data.split('\n');
      let body = '';
      let inBody = false;
      
      for (const line of lines) {
        if (inBody) {
          body += line;
        } else if (line === '') {
          inBody = true;
        }
      }

      if (body) {
        const event: RealtimeEvent = {
          name: 'websocket-message',
          data: this.tryParseJSON(body.replace('\x00', '')),
          timestamp: new Date().toISOString()
        };

        this.addEvent(event);
        this.measureLatency(event);
      }
      return;
    }

    // Fallback para mensagens não-STOMP
    const event: RealtimeEvent = {
      name: eventName,
      data: this.tryParseJSON(data),
      timestamp: new Date().toISOString()
    };

    this.addEvent(event);
    this.measureLatency(event);
  }

  private addEvent(event: RealtimeEvent): void {
    const currentEvents = this._events();
    const newEvents = [...currentEvents, event];
    
    // Manter apenas os últimos 100 eventos
    if (newEvents.length > 100) {
      newEvents.splice(0, newEvents.length - 100);
    }

    this._events.set(newEvents);
    this.eventsSubject.next(event);
    
    // Atualizar contador de eventos
    const currentStatus = this._connectionStatus();
    this.updateConnectionStatus({ 
      eventCount: currentStatus.eventCount + 1,
      lastEvent: event.name 
    });
  }

  private measureLatency(event: RealtimeEvent): void {
    // Tentar extrair timestamp do evento para calcular latência
    if (event.data && typeof event.data === 'object') {
      let eventTime: number | null = null;
      
      // Tentar diferentes campos de timestamp
      if (event.data.timestamp) {
        eventTime = typeof event.data.timestamp === 'number' 
          ? event.data.timestamp 
          : new Date(event.data.timestamp).getTime();
      } else if (event.data.serverTimestamp) {
        eventTime = typeof event.data.serverTimestamp === 'number'
          ? event.data.serverTimestamp
          : new Date(event.data.serverTimestamp).getTime();
      } else if (event.data.clientTimestamp) {
        eventTime = typeof event.data.clientTimestamp === 'number'
          ? event.data.clientTimestamp
          : new Date(event.data.clientTimestamp).getTime();
      }
      
      if (eventTime) {
        const now = Date.now();
        const latency = now - eventTime;
        
        if (latency > 0 && latency < 10000) { // Latência válida (< 10s)
          this.latencyMeasurements.push(latency);
          
          // Manter apenas as últimas 50 medições
          if (this.latencyMeasurements.length > 50) {
            this.latencyMeasurements.splice(0, this.latencyMeasurements.length - 50);
          }
          
          this.updateLatencyStats();
        }
      }
    }
  }

  private updateLatencyStats(): void {
    if (this.latencyMeasurements.length === 0) {
      this._latencyStats.set({ min: 0, max: 0, avg: 0, count: 0 });
      return;
    }

    const min = Math.min(...this.latencyMeasurements);
    const max = Math.max(...this.latencyMeasurements);
    const avg = this.latencyMeasurements.reduce((a, b) => a + b, 0) / this.latencyMeasurements.length;
    const count = this.latencyMeasurements.length;

    this._latencyStats.set({ min, max, avg: Math.round(avg), count });
  }

  private updateConnectionStatus(updates: Partial<ConnectionStatus>): void {
    const current = this._connectionStatus();
    const newStatus = { ...current, ...updates };
    this._connectionStatus.set(newStatus);
    this.connectionSubject.next(newStatus);
  }

  private tryParseJSON(data: string): any {
    try {
      return JSON.parse(data);
    } catch {
      return data;
    }
  }
}