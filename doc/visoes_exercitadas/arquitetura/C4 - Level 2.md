C4Container
title Sistema de Leilão — Containers

Person(buyer, "Comprador")
Person(seller, "Vendedor")
Person(admin, "Administrador")

System_Boundary(auction, "Plataforma de Leilão") {

  Container(frontend, "Frontend MFE (Angular)", "Angular 18+", "Interface web com micro-frontends")
  Container(api, "API Backend (Spring Boot)", "Java 21", "Serviços REST, WebSockets, SSE, lógica de domínio")
  ContainerDb(db, "Banco de Dados", "PostgreSQL", "Armazena entidades de domínio")
  Container(redis, "Cache / Mensageria", "Redis", "Gerencia locks, sessões e filas leves")
  Container(notify, "Serviço de Notificações", "Spring Boot ou Node", "E-mails, push, templates")
  Container(ext_ship, "API de Frete Externa", "HTTP API", "Cálculo de frete")
  Container(ext_pay, "Gateway de Pagamento", "HTTP API", "Processamento de pagamentos")

}

buyer --> frontend: Usa navegador/webapp
seller --> frontend: Usa navegador/webapp
admin --> frontend: Usa dashboard

frontend --> api: REST/HTTPS + WebSocket/SSE
api --> db: JDBC/SQL
api --> redis: Cache, locks
api --> notify: Eventos de notificação
api --> ext_ship: Cálculo de frete
api --> ext_pay: Pagamentos
notify --> buyer: E-mail/push
