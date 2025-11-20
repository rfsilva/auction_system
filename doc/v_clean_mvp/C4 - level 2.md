flowchart TB
  subgraph Client["Client Side"]
    Browser["Angular 18 Single App\n(HTTP(S) + WebSocket/SSE)"]
  end

  subgraph Backend["Backend Monolito Modular\n(Spring Boot 3 - Java 21)"]
    AuthMod["Module: Identity / Auth"]
    CatalogMod["Module: Catalog (Products / Lots)"]
    AuctionMod["Module: Auction Engine / Bids"]
    PaymentMod["Module: Payments"]
    DocsMod["Module: Documents / PDF"]
    RealtimeMod["Module: Realtime (WS / SSE)"]
    AuditMod["Module: Audit / Logging"]
  end

  DB["PostgreSQL (RDS)"]
  REDIS["Redis (Pub/Sub, Locks)"]
  S3["S3 (Documentos / Mídia)"]
  PAYMENT["Gateway Pagamento (externo)"]
  FRETE["API Frete (externa)"]
  NOTIF["Notification Service (Email/Push)"]

  Browser -->|REST / JSON| AuthMod
  Browser -->|REST / JSON| CatalogMod
  Browser -->|REST / JSON| AuctionMod
  Browser -->|REST / JSON| PaymentMod
  Browser -->|WS / SSE| RealtimeMod

  AuthMod --> DB
  CatalogMod --> DB
  AuctionMod --> DB
  PaymentMod --> DB
  DocsMod --> S3
  RealtimeMod --> REDIS
  AuctionMod --> REDIS
  AuctionMod --> PaymentMod
  PaymentMod --> PAYMENT
  AuctionMod --> FRETE
  AuthMod --> NOTIF
  AuctionMod --> NOTIF
  AuditMod --> DB

  Backend -.-> S3
  Backend -.-> REDIS
