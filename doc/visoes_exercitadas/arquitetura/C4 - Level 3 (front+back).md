# 📙 C4 — Nível 3 (Frontend + Backend + Integrações)

```mermaid
C4Component
title C4 — Nível 3 — Frontend + Backend

Person(buyer, "Comprador")
Person(seller, "Vendedor")
Person(admin, "Administrador")

Container_Boundary(frontend, "Frontend MFE (Angular)") {
    Component(fe_shell, "Shell MFE", "Angular", "Carrega micro-frontends, roteamento, layout")
    Component(fe_auction, "MFE Leilões", "Angular", "Listagem e detalhes dos leilões")
    Component(fe_bid, "MFE Lances", "Angular", "Tela de lances e realtime")
    Component(fe_user, "MFE Usuários", "Angular", "Cadastro, login e perfil")
    Component(fe_admin, "MFE Admin", "Angular", "Gestão interna")
}

Container_Boundary(backend, "Backend (Spring Boot)") {

    Component(controllerAuction, "AuctionController", "REST/WebSocket/SSE")
    Component(serviceAuction, "AuctionService", "Regras de negócio dos leilões")
    Component(repoAuction, "AuctionRepository", "JPA")

    Component(controllerBid, "BidController", "REST/WebSocket")
    Component(serviceBid, "BidService", "Validação e registro de lances")
    Component(repoBid, "BidRepository", "JPA")

    Component(controllerUser, "UserController", "REST")
    Component(serviceUser, "UserService", "Autenticação, perfil")
    Component(repoUser, "UserRepository", "JPA")

    Component(serviceNotification, "NotificationService", "E-mail, push, templates")
    Component(clientShipping, "ShippingClient", "Frete externo")
    Component(clientPayment, "PaymentClient", "Gateway pagamento")

    Component(cacheLayer, "Cache Layer", "Redis")
    Component(security, "Security Layer", "JWT / RBAC")
    Component(audit, "Audit Trail", "Auditoria")
}

ContainerDb(db, "PostgreSQL", "Database")
Container(redis, "Redis", "Cache e locks")
Container(ext_ship, "Serviço de Frete", "API Externa")
Container(ext_pay, "Gateway de Pagamento", "API Externa")

buyer --> fe_auction
fe_auction --> controllerAuction
fe_bid --> controllerBid
fe_user --> controllerUser
fe_admin --> controllerAuction

controllerAuction --> serviceAuction
serviceAuction --> repoAuction
serviceAuction --> cacheLayer

controllerBid --> serviceBid
serviceBid --> repoBid
serviceBid --> cacheLayer

controllerUser --> serviceUser
serviceUser --> repoUser

serviceAuction --> serviceNotification
serviceAuction --> clientShipping
serviceBid --> clientPayment

serviceNotification --> buyer

repoAuction --> db
repoBid --> db
repoUser --> db

cacheLayer --> redis
clientShipping --> ext_ship
clientPayment --> ext_pay

security --> all components
audit --> all components
