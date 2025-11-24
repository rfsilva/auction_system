# Configuração de Email - Sistema de Leilão Eletrônico

## 🐛 Problema Resolvido

**Erro Original:**
```
Mail health check failed
jakarta.mail.AuthenticationFailedException: failed to connect, no password specified?
```

**Causa:** Spring Boot tentando verificar conectividade de email sem credenciais configuradas.

## ✅ Solução Implementada

### 1. Configuração Condicional de Email

O sistema agora suporta três modos de operação:

#### Modo 1: Email Desabilitado (Padrão para Desenvolvimento)
```yaml
app:
  email:
    enabled: false
    mock: true

management:
  health:
    mail:
      enabled: false
```

#### Modo 2: Email Mock (Para Testes)
```yaml
app:
  email:
    enabled: true
    mock: true
```

#### Modo 3: Email Real (Para Produção)
```yaml
app:
  email:
    enabled: true
    mock: false

spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

### 2. Arquivos Criados/Modificados

#### Configurações
- **`application.yml`** - Configurações padrão com email desabilitado
- **`application-dev.yml`** - Email mock para desenvolvimento
- **`application-test.yml`** - Email desabilitado para testes

#### Classes Java
- **`EmailConfig.java`** - Configuração condicional do JavaMailSender
- **`EmailService.java`** - Serviço de email com suporte a mock
- **`CustomMailHealthIndicator.java`** - Health check customizado

## 🔧 Como Usar

### Desenvolvimento (Padrão)

O email está **desabilitado por padrão** em desenvolvimento:

```bash
# Iniciar aplicação (email desabilitado)
mvn spring-boot:run

# Logs mostrarão:
# EmailService inicializado - Enabled: false, Mock: true
# MOCK EMAIL - Para: user@example.com, Assunto: Welcome, Texto: ...
```

### Habilitar Email Mock

Para testar funcionalidades de email sem servidor real:

```yaml
# application-dev.yml
app:
  email:
    enabled: true
    mock: true
```

### Configurar Email Real

Para usar servidor de email real (produção):

#### Gmail/Google Workspace
```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}  # App Password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true

app:
  email:
    enabled: true
    mock: false

management:
  health:
    mail:
      enabled: true
```

#### Outros Provedores
```yaml
# Outlook/Hotmail
spring:
  mail:
    host: smtp-mail.outlook.com
    port: 587

# SendGrid
spring:
  mail:
    host: smtp.sendgrid.net
    port: 587
    username: apikey
    password: ${SENDGRID_API_KEY}

# Amazon SES
spring:
  mail:
    host: email-smtp.us-east-1.amazonaws.com
    port: 587
    username: ${AWS_SES_USERNAME}
    password: ${AWS_SES_PASSWORD}
```

## 📧 Funcionalidades de Email

### Emails Implementados

1. **Email de Boas-vindas**
   ```java
   emailService.sendWelcomeEmail("user@example.com", "João Silva");
   ```

2. **Notificação de Lance Superado**
   ```java
   emailService.sendBidOutbidNotification("user@example.com", "João", "iPhone 15", "R$ 4.500");
   ```

3. **Notificação de Leilão Terminando**
   ```java
   emailService.sendAuctionEndingNotification("user@example.com", "João", "iPhone 15", "30 minutos");
   ```

### Exemplo de Uso no Código

```java
@Service
public class UserService {
    
    private final EmailService emailService;
    
    public void registerUser(User user) {
        // Salvar usuário no banco
        userRepository.save(user);
        
        // Enviar email de boas-vindas
        emailService.sendWelcomeEmail(user.getEmail(), user.getName());
    }
}
```

## 🧪 Testando Email

### Modo Mock (Desenvolvimento)

```bash
# Logs mostrarão:
MOCK EMAIL - Enviando email simples:
Para: user@example.com
Assunto: Bem-vindo ao Sistema de Leilão Eletrônico
Texto: Olá João Silva, Bem-vindo ao nosso sistema...
```

### Modo Real (Produção)

```bash
# Configurar variáveis de ambiente
export MAIL_USERNAME=your-email@gmail.com
export MAIL_PASSWORD=your-app-password

# Iniciar aplicação
mvn spring-boot:run -Dspring.profiles.active=prod
```

### Teste via Endpoint

```bash
# Testar conectividade (se habilitado)
curl http://localhost:8080/api/actuator/health

# Resposta esperada:
{
  "status": "UP",
  "components": {
    "db": {"status": "UP"},
    "mail": {"status": "UP"}  // Se habilitado
  }
}
```

## 🔍 Troubleshooting

### Email Mock Não Aparece nos Logs

**Verificar configuração:**
```yaml
app:
  email:
    enabled: true
    mock: true

logging:
  level:
    com.leilao.shared.service.EmailService: DEBUG
```

### Erro de Autenticação Gmail

**Usar App Password:**
1. Habilitar 2FA na conta Google
2. Gerar App Password: https://myaccount.google.com/apppasswords
3. Usar App Password no lugar da senha normal

### Health Check Falhando

**Desabilitar temporariamente:**
```yaml
management:
  health:
    mail:
      enabled: false
```

### Emails Não Sendo Enviados

**Verificar configurações:**
```java
// Adicionar logs para debug
@Value("${app.email.enabled}")
private boolean emailEnabled;

@Value("${app.email.mock}")
private boolean emailMock;

logger.info("Email config - Enabled: {}, Mock: {}", emailEnabled, emailMock);
```

## 🚀 Configuração para Produção

### Variáveis de Ambiente

```bash
# Email
export EMAIL_ENABLED=true
export EMAIL_MOCK=false
export MAIL_USERNAME=noreply@yourdomain.com
export MAIL_PASSWORD=your-secure-password

# Health checks
export MANAGEMENT_HEALTH_MAIL_ENABLED=true
```

### Docker Compose

```yaml
services:
  backend:
    environment:
      - EMAIL_ENABLED=true
      - EMAIL_MOCK=false
      - MAIL_USERNAME=${MAIL_USERNAME}
      - MAIL_PASSWORD=${MAIL_PASSWORD}
```

### Kubernetes

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: email-credentials
data:
  username: <base64-encoded-username>
  password: <base64-encoded-password>

---
apiVersion: apps/v1
kind: Deployment
spec:
  template:
    spec:
      containers:
      - name: backend
        env:
        - name: EMAIL_ENABLED
          value: "true"
        - name: MAIL_USERNAME
          valueFrom:
            secretKeyRef:
              name: email-credentials
              key: username
```

## ✅ Checklist de Validação

- [ ] Aplicação inicia sem erros de email
- [ ] Health check não falha por causa de email
- [ ] Logs mostram "EmailService inicializado"
- [ ] Modo mock funciona (logs aparecem)
- [ ] Modo real funciona (emails enviados)
- [ ] Health check de email OK (se habilitado)

---

**🎉 Email Configurado!** O sistema agora funciona corretamente em todos os ambientes, com ou sem servidor de email configurado.