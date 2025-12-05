/**
 * Interfaces comuns da API - Arquivo consolidado
 * Centraliza todas as interfaces compartilhadas entre módulos
 */

// ===== INTERFACES DE RESPOSTA DA API =====

export interface ApiResponse<T> {
  success: boolean;
  data: T;
  message?: string;
  timestamp: string;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

// ===== ENUMS COMPARTILHADOS =====

export enum UserStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  SUSPENDED = 'SUSPENDED',
  PENDING_VERIFICATION = 'PENDING_VERIFICATION'
}

export enum UserRole {
  VISITOR = 'VISITOR',
  BUYER = 'BUYER',
  SELLER = 'SELLER',
  ADMIN = 'ADMIN',
  PARTICIPANT = 'PARTICIPANT'
}

export enum ContractStatus {
  DRAFT = 'DRAFT',
  ACTIVE = 'ACTIVE',
  EXPIRED = 'EXPIRED',
  CANCELLED = 'CANCELLED',
  SUSPENDED = 'SUSPENDED'
}

export enum ProdutoStatus {
  DRAFT = 'DRAFT',
  ACTIVE = 'ACTIVE',
  SOLD = 'SOLD',
  EXPIRED = 'EXPIRED',
  CANCELLED = 'CANCELLED',
  PENDING_APPROVAL = 'PENDING_APPROVAL'
}

export enum LoteStatus {
  DRAFT = 'DRAFT',
  ACTIVE = 'ACTIVE',
  CLOSED = 'CLOSED',
  CANCELLED = 'CANCELLED'
}

// ===== INTERFACES BASE PARA FILTROS =====

export interface BaseFilter {
  page?: number;
  size?: number;
  sort?: string;
  direction?: string;
}

export interface SearchFilter extends BaseFilter {
  termo?: string;
}

// ===== INTERFACES DE USUÁRIO =====

export interface User {
  id: string;
  name: string;
  email: string;
  phone?: string;
  status: UserStatus;
  roles: UserRole[];
  emailVerificado: boolean;
  telefoneVerificado: boolean;
  ultimoLogin?: string;
}

export interface UsuarioSugestaoDto {
  id: string;
  nome: string;
  email: string;
  telefone?: string;
  isVendedor: boolean;
  temContratoAtivo?: boolean;
}

// ===== INTERFACES DE AUTENTICAÇÃO =====

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
  phone?: string;
}

export interface AuthResponse {
  success: boolean;
  data: {
    token: string;
    refreshToken: string;
    user: User;
  };
  message?: string;
}

export interface RefreshTokenRequest {
  refreshToken: string;
}

// ===== INTERFACES DE CONTRATO =====

export interface Contrato {
  id: string;
  sellerId: string;
  sellerName?: string;
  sellerCompanyName?: string;
  feeRate: number;
  terms: string;
  validFrom: string;
  validTo?: string;
  active: boolean;
  categoria?: string;
  status: ContractStatus;
  createdBy?: string;
  createdByName?: string;
  approvedBy?: string;
  approvedByName?: string;
  approvedAt?: string;
  createdAt: string;
  updatedAt: string;
  
  // Campos calculados
  isActive: boolean;
  isExpired: boolean;
  canBeActivated: boolean;
  canBeCancelled: boolean;
  canBeEdited: boolean;
  daysUntilExpiration?: number;
  statusDisplayName: string;
}

export interface ContratoCreateRequest {
  sellerId: string;
  feeRate: number;
  terms: string;
  validFrom?: string;
  validTo?: string;
  categoria?: string;
}

export interface ContratoUpdateRequest {
  feeRate?: number;
  terms?: string;
  validFrom?: string;
  validTo?: string;
  categoria?: string;
}

export interface AtivarVendedorRequest {
  usuarioId: string;
  feeRate: number;
  terms: string;
  validFrom?: string;
  validTo?: string;
  categoria?: string;
  ativarImediatamente?: boolean;
}

// ===== INTERFACES DE PRODUTO =====

export interface Produto {
  id: string;
  sellerId: string;
  loteId?: string;
  title: string;
  description: string;
  images: string[];
  weight?: number;
  dimensions?: string;
  initialPrice: number;
  currentPrice: number;
  reservePrice?: number;
  incrementMin: number;
  endDateTime: string;
  status: ProdutoStatus;
  moderated: boolean;
  moderatorId?: string;
  moderatedAt?: string;
  antiSnipeEnabled: boolean;
  antiSnipeExtension: number;
  categoria?: string;
  tags: string[];
  createdAt: string;
  updatedAt: string;
  
  // Campos calculados
  isActive: boolean;
  isExpired: boolean;
  canReceiveBids: boolean;
  timeRemaining: number; // em segundos
}

export interface ProdutoCreateRequest {
  title: string;
  description: string;
  images?: string[];
  weight?: number;
  dimensions?: string;
  initialPrice: number;
  reservePrice?: number;
  incrementMin?: number;
  endDateTime: string;
  categoria?: string;
  tags?: string[];
  antiSnipeEnabled?: boolean;
  antiSnipeExtension?: number;
}

export interface ProdutoUpdateRequest {
  title?: string;
  description?: string;
  images?: string[];
  weight?: number;
  dimensions?: string;
  initialPrice?: number;
  reservePrice?: number;
  incrementMin?: number;
  endDateTime?: string;
  categoria?: string;
  tags?: string[];
  antiSnipeEnabled?: boolean;
  antiSnipeExtension?: number;
}

// ===== INTERFACES DE LOTE =====

export interface Lote {
  id: string;
  sellerId: string;
  title: string;
  description: string;
  loteEndDateTime: string;
  status: LoteStatus;
  createdAt: string;
  updatedAt: string;
  
  // Campos calculados
  isActive: boolean;
  isExpired: boolean;
  canBeEdited: boolean;
  canBeActivated: boolean;
  canBeCancelled: boolean;
  timeRemaining: number; // em segundos
  
  // Lista de produtos associados
  produtoIds: string[];
  totalProdutos: number;
}

export interface LoteCreateRequest {
  title: string;
  description: string;
  loteEndDateTime: string;
  produtoIds?: string[];
}

export interface LoteUpdateRequest {
  title?: string;
  description?: string;
  loteEndDateTime?: string;
  produtoIds?: string[];
}

// ===== INTERFACES DE FILTROS ESPECÍFICOS =====

export interface ContratoFiltro extends BaseFilter {
  sellerId?: string;
  sellerName?: string;
  status?: ContractStatus;
  categoria?: string;
  active?: boolean;
  feeRateMin?: number;
  feeRateMax?: number;
  termo?: string;
}

export interface CatalogoFiltro extends BaseFilter {
  categoria?: string;
  precoMin?: number;
  precoMax?: number;
  titulo?: string;
  ordenacao?: 'recentes' | 'preco_asc' | 'preco_desc' | 'terminando';
}

export interface LoteFiltro extends SearchFilter {
  // Herda de SearchFilter (termo, page, size, sort, direction)
}

export interface AdminUsuarioFiltro extends BaseFilter {
  nome?: string;
  email?: string;
  status?: UserStatus;
  role?: UserRole;
  emailVerificado?: boolean;
  telefoneVerificado?: boolean;
  isVendedor?: boolean;
  temContratoAtivo?: boolean;
}

// ===== INTERFACES DE VENDEDOR =====

export interface VendedorDto {
  id: string;
  usuarioId: string;
  usuarioNome?: string;
  companyName?: string;
  taxId?: string;
  contactEmail?: string;
  contactPhone?: string;
  description?: string;
  active: boolean;
  verificado: boolean;
  createdAt: string;
  updatedAt: string;
  
  // Campos calculados
  temContratoAtivo?: boolean;
  totalContratos?: number;
}

// ===== INTERFACES DE ADMIN =====

export interface AdminUsuario {
  id: string;
  nome: string;
  email: string;
  telefone?: string;
  status: UserStatus;
  roles: UserRole[];
  emailVerificado: boolean;
  telefoneVerificado: boolean;
  ultimoLogin?: string;
  createdAt: string;
  updatedAt: string;
  
  // Campos calculados
  isActive: boolean;
  isBlocked: boolean;
  isVendedor: boolean;
  temContratoAtivo?: boolean;
  totalContratos?: number;
}

export interface AdminUsuarioUpdateRequest {
  nome?: string;
  email?: string;
  telefone?: string;
  status?: UserStatus;
  roles?: UserRole[];
  emailVerificado?: boolean;
  telefoneVerificado?: boolean;
}

// ===== INTERFACES DE REALTIME =====

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