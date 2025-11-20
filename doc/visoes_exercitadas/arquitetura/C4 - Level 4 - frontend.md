
---

## 🟩 **C4 — Nível 4 (Frontend / Angular MFE)**  
Foco em módulos, serviços e componentes.

```markdown
# C4 — Nível 4 — Frontend (Angular MFE)

```mermaid
classDiagram

    class ShellModule {
        +loadMFEs()
        +configureRoutes()
    }

    class AuctionModule {
        +AuctionListComponent
        +AuctionDetailComponent
        +AuctionService
    }

    class BidModule {
        +BidPanelComponent
        +BidHistoryComponent
        +BidService
        +RealtimeService
    }

    class UserModule {
        +RegisterComponent
        +LoginComponent
        +ProfileComponent
        +UserService
    }

    class AdminModule {
        +DashboardComponent
        +AdminService
    }

    class AuctionService {
        +list()
        +get()
        +create()
    }

    class BidService {
        +place()
        +listHistory()
    }

    class RealtimeService {
        +connectWebSocket()
        +subscribe()
        +disconnect()
    }

    class UserService {
        +login()
        +register()
        +getProfile()
    }

    AuctionModule --> AuctionService
    BidModule --> BidService
    BidModule --> RealtimeService
    UserModule --> UserService
    ShellModule --> AuctionModule
    ShellModule --> BidModule
    ShellModule --> UserModule
    ShellModule --> AdminModule
