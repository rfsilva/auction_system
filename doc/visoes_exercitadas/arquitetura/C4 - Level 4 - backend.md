
---

# 🧩 **C4 — Nível 4 (Código / Design Interno)**  
*Backend + Frontend separados*

---

## 🟦 **C4 — Nível 4 (Backend / Spring Boot)**  
Foco nos módulos internos, classes principais e responsabilidades.

```markdown
# C4 — Nível 4 — Backend (Spring Boot)

```mermaid
classDiagram
    class AuctionController {
        +getAuction(id)
        +listAuctions()
        +createAuction()
        +subscribeRealtime()
    }

    class AuctionService {
        +createAuction()
        +closeAuction()
        +getAuction()
        +calculateShipping()
    }

    class AuctionRepository {
        +save()
        +findById()
        +findActive()
    }

    class BidController {
        +placeBid()
        +getBidHistory()
    }

    class BidService {
        +validateBid()
        +registerBid()
        +notifyOutbid()
        +applyLock()
    }

    class BidRepository {
        +save()
        +findByAuction()
    }

    class UserController {
        +register()
        +login()
        +profile()
    }

    class UserService {
        +validateUser()
        +updateReputation()
    }

    class UserRepository {
        +save()
        +findByEmail()
    }

    class NotificationService {
        +sendEmail()
        +sendPush()
        +generateTemplate()
    }

    class ShippingClient {
        +calculate()
    }

    class PaymentClient {
        +authorize()
        +capture()
    }

    class RedisCache {
        +lock()
        +unlock()
        +cache()
    }

    class SecurityLayer {
        +authenticate()
        +authorize()
    }

    AuctionController --> AuctionService
    AuctionService --> AuctionRepository
    BidController --> BidService
    BidService --> BidRepository
    UserController --> UserService
    UserService --> UserRepository
    AuctionService --> ShippingClient
    BidService --> PaymentClient
    BidService --> RedisCache
    AuctionService --> RedisCache
    BidService --> NotificationService
    SecurityLayer --> AuctionController
    SecurityLayer --> BidController
    SecurityLayer --> UserController
