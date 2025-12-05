import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { catchError, switchMap } from 'rxjs/operators';
import { throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  // URLs que não precisam de autenticação
  const publicUrls = [
    '/auth/login',
    '/auth/register',
    '/auth/check-email',
    '/auth/health',
    '/public/catalogo',  // CORRIGIDO: Adicionado endpoints públicos do catálogo
    '/public/sobre',
    '/public/contato'
  ];

  // Verificar se é uma URL pública
  const isPublicUrl = publicUrls.some(url => req.url.includes(url));

  // Se for URL pública ou não tiver token, prosseguir sem modificar
  if (isPublicUrl || !token) {
    return next(req);
  }

  // Adicionar token no header Authorization
  const authReq = req.clone({
    headers: req.headers.set('Authorization', `Bearer ${token}`)
  });

  return next(authReq).pipe(
    catchError(error => {
      // Se receber 401 (Unauthorized), tentar renovar o token
      if (error.status === 401 && !req.url.includes('/auth/refresh')) {
        const refreshToken = authService.getRefreshToken();
        
        if (refreshToken) {
          // Tentar renovar o token
          return authService.refreshToken().pipe(
            switchMap(() => {
              // Repetir a requisição original com o novo token
              const newToken = authService.getToken();
              const retryReq = req.clone({
                headers: req.headers.set('Authorization', `Bearer ${newToken}`)
              });
              return next(retryReq);
            }),
            catchError(refreshError => {
              // Se falhar ao renovar, fazer logout
              console.error('Erro ao renovar token:', refreshError);
              // authService.logout(); // Não chamar aqui para evitar loop
              return throwError(() => error);
            })
          );
        }
      }

      return throwError(() => error);
    })
  );
};