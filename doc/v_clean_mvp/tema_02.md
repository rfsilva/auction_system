##🟪 TEMA 2 — Gestão de Contas e Perfis

| Tema | Épico | História / Enabler | Tipo | Descrição | Critérios de Aceite |
|------|-------|---------------------|------|-----------|----------------------|
| Gestão de Contas | Autenticação | Cadastro de usuário | História | Criar conta | Dados obrigatórios, validação email |
| Gestão de Contas | Autenticação | Login/Logout | História | Autenticar usuário | JWT válido, refresh token |
| Gestão de Contas | Autenticação | Recuperação de senha | História | Reset password | Token seguro, expiração |
| Gestão de Contas | Perfis e Papéis | Atualizar perfil | História | Editar perfil básico | Campos validados |
| Gestão de Contas | Perfis e Papéis | Upgrade de Visitante → Participante | História | Atribuição de papel | Permissões liberadas |
| Gestão de Contas | Perfis e Papéis | Validação documental | História | Upload documentos | Aprovado/reprovado |
| Gestão de Contas | Enablers | EN-07 JWT compartilhado | Enabler | Token único entre camadas | Válido nos 3 serviços |
| Gestão de Contas | Enablers | EN-08 RBAC middleware | Enabler | Controle de acesso central | Checagem por endpoint |
| Gestão de Contas | Enablers | EN-09 Refresh token | Enabler | Renovação segura | Armazenamento HttpOnly |
