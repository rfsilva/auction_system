C4Component
title Backend — Componentes (Spring Boot, Java 21)

Container(api, "API Backend", "Spring Boot") {

  Component(controllerAuction, "AuctionController", "Exposição de endpoints REST/SSE/WebSocket")
  Component(serviceAuction, "AuctionService", "Regras de negócio de leilões")
  Component(repoAuction, "AuctionRepository", "Persistência JPA/Hibernate")

  Component(controllerBid, "BidController", "Endpoints de lances")
  Component(serviceBid, "BidService", "Registra, valida e ordena lances")
  Component(repoBid, "BidRepository", "Persistência dos lances")

  Component(controllerUser, "UserController", "Cadastro, login, perfis")
  Component(serviceUser, "UserService", "Validação, reputação, histórico")
  Component(repoUser, "UserRepository", "Persistência de usuários")

  Component(serviceNotification, "NotificationService", "Disparo de e-mails/push")
  Component(clientShipping, "ShippingClient", "Integração com API externa de frete")
  Component(clientPayment, "PaymentClient", "Integração com gateway de pagamentos")

  Component(securityLayer, "Security Layer", "JWT, RBAC, filtros de autenticação")
  Component(cacheLayer, "Cache Layer", "Redis para lock, cache e rate-limit")
  Component(audit, "Audit Module", "Registros de auditoria e trilhas")

}

User -> controllerAuction: Consultas
User -> controllerBid: Envio de lances
controllerAuction --> serviceAuction
controllerBid --> serviceBid
serviceBid --> repoBid
serviceAuction --> repoAuction
controllerUser --> serviceUser
serviceUser --> repoUser

serviceBid --> cacheLayer: Lock/Redis
serviceAuction --> cacheLayer: Cache de leitura

serviceAuction --> serviceNotification: Notificar outbid
serviceAuction --> clientShipping: Cálculo de frete
serviceBid --> clientPayment: Pagamentos (pós-arremate)

securityLayer --> all components
audit --> all components
