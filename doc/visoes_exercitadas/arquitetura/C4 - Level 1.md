# 🧱 C4 Model — Sistema de Leilão Eletrônico

---

# 📘 Nível 1 — Diagrama de Contexto (C4-1)

```mermaid
C4Context
title Sistema de Leilão — Diagrama de Contexto

Person(buyer, "Comprador", "Usuário que participa dos leilões e registra lances.")
Person(seller, "Vendedor", "Usuário que cria lotes e produtos para leilão.")
Person(admin, "Administrador", "Responsável por auditoria, controle e suporte.")

System(auction, "Plataforma de Leilão Online", "Permite cadastrar produtos, formar lotes, receber lances e finalizar leilões.")

buyer --> auction: Visualiza produtos, dá lances
seller --> auction: Cadastra produtos e lotes
admin --> auction: Gerencia usuários e auditorias
