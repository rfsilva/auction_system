flowchart TB
  %% FRONTEND COMPONENTS
  subgraph FRONT["Frontend Angular 18 (Single App)"]
    FE_Home["Page: Home / Catalog"]
    FE_Product["Page: Product Details"]
    FE_Auction["Page: Auction (live)"]
    FE_MyBids["Page: My Bids / Purchases"]
    FE_SellerDash["Page: Seller Dashboard"]
    FE_Admin["Page: Admin Panel"]

    FE_AuthSvc["Service: AuthService (login, token)"]
    FE_ApiSvc["Service: ApiService (REST client withFetch)"]
    FE_RealSvc["Service: RealtimeService (SSE/WS)"]
    FE_UiCmp["UI Components (Header, Timer, BidForm, Feed)"]

    FE_Home --> FE_ApiSvc
    FE_Product --> FE_ApiSvc
    FE_Auction --> FE_ApiSvc
    FE_MyBids --> FE_ApiSvc
    FE_SellerDash --> FE_ApiSvc
    FE_Admin --> FE_ApiSvc

    FE_Auction --> FE_RealSvc
    FE_AuthSvc --> FE_ApiSvc
    FE_UiCmp --> FE_RealSvc
  end

  %% BACKEND COMPONENTS
  subgraph BACK["Backend Monolito Modular (Spring Boot)"]
    BE_Rest["REST API Layer (Controllers)"]
    BE_AuthCtrl["AuthController"]
    BE_ProductCtrl["ProductController"]
    BE_LotCtrl["LotController"]
    BE_BidCtrl["BidController"]
    BE_PaymentCtrl["PaymentController"]
    BE_DocCtrl["DocumentController"]
    BE_AdminCtrl["AdminController"]

    BE_SvcAuth["Service: AuthService"]
    BE_SvcUser["Service: UserService"]
    BE_SvcProd["Service: ProductService"]
    BE_SvcLot["Service: LotService"]
    BE_SvcAuction["Service: AuctionEngine / AuctionService"]
    BE_SvcBid["Service: BidService"]
    BE_SvcPayment["Service: PaymentService"]
    BE_SvcDocs["Service: DocumentService"]
    BE_SvcNotif["Service: NotificationService"]
    BE_SvcAudit["Service: AuditService"]
    BE_Scheduler["SchedulerService (closing jobs)"]
    BE_Repo["Repositories (JPA)"]
    BE_PDF["PDF Generator / Doc Worker"]
    BE_EventBus["Internal Event Bus / Redis PubSub Adapter"]
  end

  %% DATABASE & INFRA
  DB["PostgreSQL"]
  REDIS["Redis (Pub/Sub & Locks)"]
  S3["S3 Storage"]
  PAYMENT["Payment Gateway"]
  FRETE["Frete API"]
  NOTIF["Notification Service"]

  %% FRONT -> BACK connections
  FE_ApiSvc -->|HTTP/JSON| BE_Rest
  FE_AuthSvc -->|HTTP/JSON| BE_AuthCtrl
  FE_RealSvc -->|WS/SSE| BE_SvcAuction

  %% BACK internal wiring
  BE_Rest --> BE_AuthCtrl
  BE_Rest --> BE_ProductCtrl
  BE_Rest --> BE_LotCtrl
  BE_Rest --> BE_BidCtrl
  BE_Rest --> BE_PaymentCtrl
  BE_Rest --> BE_DocCtrl
  BE_Rest --> BE_AdminCtrl

  BE_AuthCtrl --> BE_SvcAuth
  BE_ProductCtrl --> BE_SvcProd
  BE_LotCtrl --> BE_SvcLot
  BE_BidCtrl --> BE_SvcBid
  BE_PaymentCtrl --> BE_SvcPayment
  BE_DocCtrl --> BE_SvcDocs
  BE_AdminCtrl --> BE_SvcAudit

  BE_SvcBid --> BE_SvcAuction
  BE_SvcAuction --> BE_EventBus
  BE_EventBus --> REDIS
  BE_SvcAuction --> BE_SvcNotif
  BE_SvcNotif --> NOTIF

  BE_SvcProd --> BE_Repo
  BE_SvcLot --> BE_Repo
  BE_SvcUser --> BE_Repo
  BE_SvcBid --> BE_Repo
  BE_SvcAuction --> BE_Repo

  BE_SvcDocs --> BE_PDF
  BE_PDF --> S3
  BE_SvcDocs --> S3

  BE_SvcPayment --> PAYMENT
  BE_SvcAuction --> FRETE

  BE_SvcAudit --> DB
  BE_Repo --> DB
  BE_Scheduler --> BE_SvcAuction
  BE_Scheduler --> BE_EventBus

  %% Notes for scaling and reliability (non-edge connections)
  classDef infra stroke:#444,stroke-dasharray: 5 3;
  class DB,REDIS,S3,PAYMENT,FRETE,NOTIF infra;
