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

export interface AdminUsuarioFiltro {
  nome?: string;
  email?: string;
  status?: UserStatus;
  role?: UserRole;
  emailVerificado?: boolean;
  telefoneVerificado?: boolean;
  isVendedor?: boolean;
  temContratoAtivo?: boolean;
  page?: number;
  size?: number;
  sort?: string;
  direction?: string;
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

export interface UsuarioSugestaoDto {
  id: string;
  nome: string;
  email: string;
  telefone?: string;
  isVendedor: boolean;
  temContratoAtivo?: boolean;
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

export interface ApiResponse<T> {
  success: boolean;
  data: T;
  message?: string;
  timestamp: string;
}

export enum UserStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  SUSPENDED = 'SUSPENDED',  // Mudança: SUSPENDED em vez de BLOCKED
  PENDING_VERIFICATION = 'PENDING_VERIFICATION'
}

export enum UserRole {
  VISITOR = 'VISITOR',
  BUYER = 'BUYER',    // Mudança: BUYER em vez de BIDDER
  SELLER = 'SELLER',
  ADMIN = 'ADMIN',
  PARTICIPANT = 'PARTICIPANT'
}