# Sistema de Leilão Eletrônico - Frontend

Frontend Angular 18+ com standalone components para o Sistema de Leilão Eletrônico.

## 🚀 Tecnologias

- **Angular 18+** com Standalone Components
- **TypeScript 5.9+**
- **SCSS** para estilização
- **RxJS** para programação reativa
- **HttpClient** com Fetch API
- **Angular Router** para roteamento
- **ESLint** para linting
- **Prettier** para formatação

## 🏗️ Arquitetura

O projeto segue uma arquitetura modular e escalável:

```
src/app/
├── core/                    # Funcionalidades centrais
│   ├── guards/             # Guards de rota
│   ├── interceptors/       # Interceptors HTTP
│   └── services/           # Serviços principais
├── shared/                 # Componentes compartilhados
│   ├── components/         # Componentes reutilizáveis
│   └── models/             # Interfaces e tipos
├── pages/                  # Páginas da aplicação
│   ├── auth/              # Autenticação (login/registro)
│   ├── home/              # Página inicial
│   ├── auction/           # Leilões
│   └── profile/           # Perfil do usuário
├── layouts/               # Layouts da aplicação
└── environments/          # Configurações de ambiente
```

## 🛠️ Setup de Desenvolvimento

### Pré-requisitos

- Node.js 18+ ou 20+
- npm 9+
- Angular CLI 18+

### Instalação

```bash
# Navegar para o diretório do frontend
cd frontend/leilao-frontend

# Instalar dependências
npm install

# Executar em modo de desenvolvimento
npm start

# Ou com configuração específica
npm run start:dev
```

A aplicação estará disponível em: `http://localhost:4200`

## 📋 Scripts Disponíveis

### Desenvolvimento
```bash
npm start              # Servidor de desenvolvimento
npm run start:dev      # Desenvolvimento com configuração específica
npm run start:prod     # Desenvolvimento com configuração de produção
```

### Build
```bash
npm run build          # Build de produção
npm run build:dev      # Build de desenvolvimento
npm run build:prod     # Build de produção otimizado
```

### Testes e Qualidade
```bash
npm test               # Executar testes unitários
npm run test:ci        # Testes para CI (sem watch)
npm run lint           # Executar linter
npm run lint:fix       # Corrigir problemas de lint automaticamente
```

### Análise
```bash
npm run analyze        # Analisar bundle size
```

## 🔧 Configuração

### Ambientes

O projeto suporta múltiplos ambientes:

- **Development** (`environment.ts`)
- **Production** (`environment.prod.ts`)

### Proxy para Backend

Durante o desenvolvimento, as requisições para `/api/*` e `/ws/*` são automaticamente redirecionadas para o backend local (`http://localhost:8080`).

Configuração em `proxy.conf.json`:
```json
{
  "/api/*": {
    "target": "http://localhost:8080",
    "secure": false,
    "changeOrigin": true
  }
}
```

## 🎨 Funcionalidades Implementadas

### ✅ Autenticação
- [x] Login com email/senha
- [x] Registro de usuário
- [x] JWT Token management
- [x] Refresh token automático
- [x] Guards de rota
- [x] Interceptor de autenticação

### ✅ Layout e Navegação
- [x] Layout responsivo
- [x] Header com navegação
- [x] Footer informativo
- [x] Roteamento lazy loading
- [x] Design system consistente

### ✅ Páginas Principais
- [x] **Home** - Landing page com informações do sistema
- [x] **Login** - Formulário de autenticação
- [x] **Registro** - Formulário de cadastro
- [x] **Leilões** - Lista de leilões ativos/futuros/finalizados
- [x] **Perfil** - Informações do usuário e estatísticas

### ✅ Componentes Reutilizáveis
- [x] Formulários reativos
- [x] Botões com estados (loading, disabled)
- [x] Cards de leilão
- [x] Alertas e notificações
- [x] Loading spinners

## 🔄 Comunicação com Backend

### HTTP Client
- Configurado com Fetch API (`withFetch()`)
- Interceptor automático para JWT
- Tratamento de erros centralizado
- Tipagem TypeScript completa

### Serviços Principais

#### AuthService
```typescript
// Login
authService.login(credentials).subscribe(response => {
  // Handle response
});

// Verificar autenticação (Signal)
const isAuthenticated = authService.isAuthenticated();

// Usuário atual (Signal)
const currentUser = authService.currentUser();
```

## 🎯 Próximas Implementações

### 🔄 Em Desenvolvimento
- [ ] Integração WebSocket/SSE para tempo real
- [ ] Componente de leilão ao vivo
- [ ] Sistema de notificações
- [ ] Upload de imagens

### 📋 Backlog
- [ ] PWA (Progressive Web App)
- [ ] Internacionalização (i18n)
- [ ] Testes E2E
- [ ] Acessibilidade (a11y)
- [ ] Dark mode

## 🧪 Testes

### Testes Unitários
```bash
# Executar testes
npm test

# Testes com coverage
npm run test:ci
```

### Estrutura de Testes
- Testes de componentes
- Testes de serviços
- Testes de guards e interceptors
- Coverage reports

## 📦 Build e Deploy

### Build de Produção
```bash
npm run build:prod
```

### Otimizações Incluídas
- Tree shaking
- Minificação
- Compressão
- Lazy loading
- Bundle splitting
- Cache busting

### CI/CD
Pipeline automatizado com GitHub Actions:
- Lint e testes em múltiplas versões do Node.js
- Build de produção
- Upload de artefatos
- Deploy automático (configurável)

## 🔒 Segurança

### Implementações de Segurança
- JWT token storage seguro
- Interceptor de autenticação
- Guards de rota
- Sanitização de inputs
- HTTPS em produção
- CSP headers (configurável)

## 📱 Responsividade

### Breakpoints
- **Mobile:** < 768px
- **Tablet:** 768px - 1024px
- **Desktop:** > 1024px

### Features Responsivas
- Layout adaptativo
- Navegação mobile-friendly
- Formulários otimizados para touch
- Imagens responsivas

## 🎨 Design System

### Cores Principais
- **Primary:** #e74c3c (Vermelho)
- **Secondary:** #667eea (Azul)
- **Success:** #28a745 (Verde)
- **Warning:** #ffc107 (Amarelo)
- **Danger:** #dc3545 (Vermelho escuro)

### Tipografia
- **Font Family:** System fonts (-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto)
- **Sizes:** h1-h6 com escala harmônica
- **Weights:** 400 (normal), 500 (medium), 600 (semibold), 700 (bold)

## 🤝 Contribuição

### Padrões de Código
1. Usar standalone components
2. Seguir convenções Angular
3. Tipagem TypeScript rigorosa
4. Testes para novas funcionalidades
5. Documentação atualizada

### Commit Convention
```
feat: adicionar nova funcionalidade
fix: corrigir bug
docs: atualizar documentação
style: mudanças de estilo/formatação
refactor: refatoração de código
test: adicionar/atualizar testes
```

## 📄 Licença

Este projeto está sob licença MIT. Veja o arquivo [LICENSE](../../LICENSE) para detalhes.

---

## 🚀 Status da História 2

### ✅ Tasks Concluídas
- [x] **Task 1:** Pasta frontend criada
- [x] **Task 2:** Projeto Angular 18+ com standalone components
- [x] **Task 3:** HttpClient configurado com withFetch()
- [x] **Task 4:** Layout base e roteamento inicial completos
- [x] **Task 5:** CI/CD básico configurado

### 📊 Métricas
- **Componentes:** 8 componentes standalone
- **Páginas:** 5 páginas principais
- **Serviços:** 1 serviço principal (AuthService)
- **Guards:** 1 guard de autenticação
- **Interceptors:** 1 interceptor HTTP
- **Build Size:** ~1.55MB (development)

A História 2 está **100% implementada** e pronta para integração com o backend! 🎉