flowchart LR
  actor User["Usuário (Browser)"]
  actor Seller["Vendedor (Browser)"]
  actor Admin["Administrador (Browser)"]

  subgraph System["Sistema de Leilão Eletrônico"]
    App["Aplicação de Leilão (Frontend + Backend)"]
  end

  Payment["Gateway de Pagamento (externo)"]
  Freight["API de Frete (externa)"]
  Storage["Storage (S3)"]
  Notif["Serviço de Notificações (Email/Push)"]
  AuthProvider["Provedor de Identidade (opcional)"]

  User --> App
  Seller --> App
  Admin --> App

  App --> Payment
  App --> Freight
  App --> Storage
  App --> Notif
  App --> AuthProvider
