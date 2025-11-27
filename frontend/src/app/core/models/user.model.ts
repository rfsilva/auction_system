export enum UserRole {
  VISITOR = 'VISITOR',
  PARTICIPANT = 'PARTICIPANT',
  BUYER = 'BUYER',
  SELLER = 'SELLER',
  ADMIN = 'ADMIN'
}

export enum UserStatus {
  PENDING_VERIFICATION = 'PENDING_VERIFICATION',
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  SUSPENDED = 'SUSPENDED'
}

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