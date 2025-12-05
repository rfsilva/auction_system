# 🎯 PLANO DE EXECUÇÃO - REORGANIZAÇÃO DE ROTAS

## 📋 OBJETIVO
Reorganizar rotas do sistema (front+back) com foco na separação público/não-público de forma enxuta e objetiva.

## ⏱️ CRONOGRAMA: 7 DIAS ÚTEIS

---

## 🔧 FASE 1: BACKEND (3 dias)

### **DIA 1 - Criação da Estrutura Base**

#### ✅ **Tarefa 1.1: Criar PublicController**
```bash
# Arquivo: backend/src/main/java/com/leilao/modules/public/controller/PublicController.java
```

**Implementação:**
```java
@RestController
@RequestMapping("/public")
@CrossOrigin(origins = "*")
public class PublicController {
    
    @Autowired
    private LoteService loteService;
    
    // Catálogo público de lotes
    @GetMapping("/catalogo/lotes")
    public ResponseEntity<ApiResponse<Page<LoteDto>>> buscarLotes(
            @RequestParam(required = false) String termo,
            @PageableDefault(size = 20) Pageable pageable) {
        // Implementação
    }
    
    @GetMapping("/catalogo/lotes/{id}")
    public ResponseEntity<ApiResponse<LoteDto>> buscarLote(@PathVariable String id) {
        // Implementação
    }
    
    // Páginas institucionais
    @GetMapping("/sobre")
    public ResponseEntity<ApiResponse<Map<String, String>>> sobre() {
        // Implementação
    }
    
    @GetMapping("/contato")
    public ResponseEntity<ApiResponse<Map<String, String>>> contato() {
        // Implementação
    }
}
```

**Tempo estimado:** 4h

#### ✅ **Tarefa 1.2: Atualizar SecurityConfig**
```java
@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(authz -> authz
                // Público - SEM autenticação
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/auth/**").permitAll()
                
                // Privado - COM autenticação por role
                .requestMatchers("/api/usuario/**").hasRole("USER")
                .requestMatchers("/api/vendedor/**").hasRole("SELLER") 
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                
                // Qualquer outra requisição precisa de autenticação
                .anyRequest().authenticated()
            )
            .build();
    }
}
```

**Tempo estimado:** 2h

---

### **DIA 2 - Reorganização dos Controllers Existentes**

#### ✅ **Tarefa 2.1: Reorganizar LoteController**
- **Manter:** `/api/vendedor/lotes/**` (operações do vendedor)
- **Migrar para PublicController:** endpoints de catálogo público
- **Remover:** duplicações

**Arquivos modificados:**
- `LoteController.java`
- `CatalogoLoteController.java` (remover se existir)

**Tempo estimado:** 3h

#### ✅ **Tarefa 2.2: Reorganizar ContratoController**
- **Mover para:** `/api/admin/contratos/**`
- **Manter:** estrutura atual, apenas alterar `@RequestMapping`

**Tempo estimado:** 2h

#### ✅ **Tarefa 2.3: Reorganizar ProdutoController**
- **Manter:** `/api/vendedor/produtos/**`
- **Verificar:** se há endpoints públicos para migrar

**Tempo estimado:** 1h

---

### **DIA 3 - Testes e Ajustes Backend**

#### ✅ **Tarefa 3.1: Testes de Endpoints**
- Testar todos os endpoints públicos sem autenticação
- Testar endpoints privados com autenticação
- Verificar SecurityConfig funcionando

**Ferramentas:** Postman/Insomnia

**Tempo estimado:** 4h

#### ✅ **Tarefa 3.2: Ajustes e Correções**
- Corrigir problemas encontrados nos testes
- Documentar mudanças

**Tempo estimado:** 2h

---

## 🎨 FASE 2: FRONTEND (3 dias)

### **DIA 4 - Reestruturação de Rotas**

#### ✅ **Tarefa 4.1: Criar Nova Estrutura de Rotas**

**Arquivo:** `frontend/src/app/app.routes.ts`
```typescript
export const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      // ========================================
      // 🌐 ÁREA PÚBLICA (Sem Guards)
      // ========================================
      { path: '', component: HomeComponent },
      { path: 'catalogo', loadChildren: () => import('./public/catalogo/catalogo.routes') },
      { path: 'sobre', component: SobreComponent },
      { path: 'contato', component: ContatoComponent },
      
      // ========================================
      // 🔐 AUTENTICAÇÃO
      // ========================================
      { path: 'auth', loadChildren: () => import('./auth/auth.routes') },
      
      // ========================================
      // 👤 ÁREA PRIVADA - USUÁRIO
      // ========================================
      { 
        path: 'app', 
        canActivate: [authGuard],
        loadChildren: () => import('./private/usuario/usuario.routes')
      },
      
      // ========================================
      // 🏪 ÁREA PRIVADA - VENDEDOR
      // ========================================
      { 
        path: 'vendedor', 
        canActivate: [authGuard, sellerGuard],
        loadChildren: () => import('./private/vendedor/vendedor.routes')
      },
      
      // ========================================
      // 👑 ÁREA PRIVADA - ADMIN
      // ========================================
      { 
        path: 'admin', 
        canActivate: [authGuard, adminGuard],
        loadChildren: () => import('./private/admin/admin.routes')
      }
    ]
  },
  { path: '**', component: NotFoundComponent }
];
```

**Tempo estimado:** 3h

#### ✅ **Tarefa 4.2: Criar Arquivos de Rotas Modulares**

**Criar arquivos:**
- `frontend/src/app/public/catalogo/catalogo.routes.ts`
- `frontend/src/app/private/usuario/usuario.routes.ts`
- `frontend/src/app/private/vendedor/vendedor.routes.ts`
- `frontend/src/app/private/admin/admin.routes.ts`

**Tempo estimado:** 2h

---

### **DIA 5 - Reorganização de Services**

#### ✅ **Tarefa 5.1: Criar PublicCatalogoService**

**Arquivo:** `frontend/src/app/core/services/public-catalogo.service.ts`
```typescript
@Injectable({
  providedIn: 'root'
})
export class PublicCatalogoService {
  private baseUrl = '/public/catalogo';
  
  constructor(private http: HttpClient) {}
  
  buscarLotes(filtros: any = {}) {
    return this.http.get<ApiResponse<Page<LoteDto>>>(`${this.baseUrl}/lotes`, {
      params: filtros
    });
  }
  
  buscarLote(id: string) {
    return this.http.get<ApiResponse<LoteDto>>(`${this.baseUrl}/lotes/${id}`);
  }
}
```

**Tempo estimado:** 2h

#### ✅ **Tarefa 5.2: Atualizar Services Existentes**
- **LoteService:** remover métodos públicos, manter apenas privados
- **ContratoService:** atualizar URLs para `/api/admin/`
- **ProdutoService:** verificar e ajustar URLs

**Tempo estimado:** 3h

#### ✅ **Tarefa 5.3: Criar Guards Específicos**

**Arquivo:** `frontend/src/app/core/guards/area.guards.ts`
```typescript
@Injectable()
export class PublicGuard implements CanActivate {
  canActivate(): boolean {
    return true; // Sempre permite
  }
}

@Injectable() 
export class PrivateGuard implements CanActivate {
  constructor(private authService: AuthService) {}
  
  canActivate(): boolean {
    return this.authService.isAuthenticated();
  }
}
```

**Tempo estimado:** 1h

---

### **DIA 6 - Atualização de Componentes**

#### ✅ **Tarefa 6.1: Atualizar Componentes Públicos**
- **HomeComponent:** usar PublicCatalogoService
- **CatalogoLotesComponent:** usar PublicCatalogoService
- Remover dependências de autenticação

**Tempo estimado:** 3h

#### ✅ **Tarefa 6.2: Atualizar Componentes Privados**
- Verificar imports de services
- Ajustar rotas internas
- Testar navegação

**Tempo estimado:** 3h

---

## 🧪 FASE 3: TESTES E VALIDAÇÃO (1 dia)

### **DIA 7 - Testes Integrados**

#### ✅ **Tarefa 7.1: Testes Funcionais**
- **Área Pública:** acessível sem login
- **Área Privada:** requer autenticação
- **Navegação:** entre áreas funcionando
- **Guards:** bloqueando acesso adequadamente

**Tempo estimado:** 4h

#### ✅ **Tarefa 7.2: Testes de Performance**
- **Lazy Loading:** funcionando corretamente
- **Bundle Size:** verificar se não aumentou significativamente
- **Loading:** tempos de carregamento

**Tempo estimado:** 2h

---

## 📋 CHECKLIST DE ENTREGA

### **Backend ✅**
- [ ] PublicController criado e funcionando
- [ ] SecurityConfig atualizado
- [ ] Controllers reorganizados por área
- [ ] Endpoints públicos sem autenticação
- [ ] Endpoints privados com autenticação correta
- [ ] Testes passando

### **Frontend ✅**
- [ ] Rotas reorganizadas com lazy loading
- [ ] Guards funcionando corretamente
- [ ] Services separados por contexto
- [ ] Componentes atualizados
- [ ] Navegação funcionando
- [ ] Build sem erros

### **Integração ✅**
- [ ] Área pública acessível sem login
- [ ] Área privada requer autenticação
- [ ] Transição entre áreas suave
- [ ] Performance mantida
- [ ] Sem quebras funcionais

---

## 🚨 RISCOS E MITIGAÇÕES

| Risco | Probabilidade | Impacto | Mitigação |
|-------|---------------|---------|-----------|
| Quebra de funcionalidades existentes | Média | Alto | Testes extensivos em cada fase |
| Performance degradada | Baixa | Médio | Monitoramento de bundle size |
| Problemas de autenticação | Média | Alto | Testes específicos de guards |
| Conflitos de merge | Baixa | Baixo | Commits frequentes e comunicação |

---

## 📊 MÉTRICAS DE SUCESSO

1. **Funcional:** 100% das funcionalidades mantidas
2. **Segurança:** Área pública sem autenticação, privada protegida
3. **Performance:** Bundle size não aumentar >10%
4. **Manutenibilidade:** Estrutura clara e organizada
5. **Prazo:** Entrega em 7 dias úteis

---

## 👥 RESPONSABILIDADES

- **Backend Developer:** Fases 1 e 3 (backend)
- **Frontend Developer:** Fases 2 e 3 (frontend)  
- **Tech Lead:** Revisão e aprovação de cada fase
- **QA:** Testes de validação final

---

**Data de Criação:** $(date)  
**Versão:** 1.0  
**Status:** Aprovado para execução