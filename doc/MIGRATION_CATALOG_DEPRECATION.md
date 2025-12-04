# 📋 Guia de Migração - Depreciação do Catálogo de Produtos

## 🎯 **Resumo da Mudança**

**Data de Implementação:** 19 de Dezembro de 2024  
**Versão:** Sprint S2.3  
**Impacto:** Alto - Mudança na navegação pública  

O sistema migrou de um **catálogo direto de produtos** para um **catálogo baseado em lotes**, proporcionando uma experiência mais próxima de leilões reais.

---

## ⚠️ **Endpoints Depreciados**

### Backend API

| Endpoint Antigo | Status | Novo Endpoint | Ação |
|----------------|--------|---------------|-------|
| `GET /api/catalogo/produtos` | ❌ Depreciado | `GET /api/lotes/catalogo-publico` | Redirect 301 |
| `GET /api/catalogo/produtos/{id}` | ❌ Depreciado | `GET /api/lotes/{loteId}` | Redirect/410 |
| `GET /api/catalogo/categorias` | ✅ Mantido | `GET /api/catalogo/categorias` | Sem mudança |

### Frontend Routes

| Rota Antiga | Status | Nova Rota | Ação |
|-------------|--------|-----------|-------|
| `/catalogo-produtos` | ❌ Depreciado | `/catalogo` | Redirect automático |
| `/catalogo` | ✅ Atualizado | `/catalogo` | Agora mostra lotes |

---

## 🔄 **Como Migrar**

### Para Desenvolvedores Frontend

#### ❌ **Código Antigo**
```typescript
// Não use mais
this.produtoService.buscarCatalogo(filtros).subscribe(...)
this.produtoService.buscarProdutoCatalogo(produtoId).subscribe(...)
```

#### ✅ **Código Novo**
```typescript
// Use o novo serviço de lotes
this.loteCatalogoService.buscarCatalogoPublico(filtros).subscribe(...)

// Para acessar produtos, navegue pelo lote
this.loteService.buscarLote(loteId).subscribe(lote => {
  // Produtos estão em lote.produtos
})
```

### Para Desenvolvedores Backend/API

#### ❌ **Chamadas Antigas**
```bash
# Depreciado - retorna redirect
GET /api/catalogo/produtos?page=0&size=20

# Depreciado - retorna redirect ou 410
GET /api/catalogo/produtos/123e4567-e89b-12d3-a456-426614174000
```

#### ✅ **Chamadas Novas**
```bash
# Novo padrão - catálogo de lotes
GET /api/lotes/catalogo-publico?page=0&size=10

# Detalhes do lote (inclui produtos)
GET /api/lotes/123e4567-e89b-12d3-a456-426614174000

# Categorias (sem mudança)
GET /api/catalogo/categorias
```

---

## 🏗️ **Nova Arquitetura**

### Fluxo de Navegação Antigo
```
Home → Catálogo de Produtos → Produto Individual
```

### Fluxo de Navegação Novo
```
Home → Catálogo de Lotes → Lote → Produtos do Lote
```

### Estrutura de Dados

#### Lote (Novo)
```json
{
  "id": "lote-uuid",
  "title": "Lote de Eletrônicos Premium",
  "description": "Smartphones e tablets de última geração",
  "loteEndDateTime": "2024-12-25T20:00:00Z",
  "status": "ACTIVE",
  "totalProdutos": 5,
  "produtos": [
    {
      "id": "produto-uuid",
      "title": "iPhone 15 Pro",
      "currentPrice": 4500.00,
      "status": "ACTIVE"
    }
  ]
}
```

---

## 🔧 **Regras de Negócio Atualizadas**

### Visibilidade de Produtos
1. **Produtos sem lote**: Não aparecem no catálogo público
2. **Produtos com lote**: Visíveis apenas através do lote
3. **Status válidos**: Apenas `ACTIVE` e `PUBLISHED` são exibidos

### Sistema de Navegação
1. **Catálogo público**: Mostra apenas lotes ativos
2. **Detalhes do lote**: Lista produtos válidos do lote
3. **Paginação**: Configurável (10, 20, 50 itens por página)

---

## 🚨 **Comportamento dos Endpoints Depreciados**

### `GET /api/catalogo/produtos`
```http
HTTP/1.1 301 Moved Permanently
Location: /api/lotes/catalogo-publico
Content-Type: application/json

{
  "success": false,
  "message": "Endpoint depreciado. Use /api/lotes/catalogo-publico",
  "error": "DEPRECATED_ENDPOINT",
  "timestamp": "2024-12-19T10:00:00Z"
}
```

### `GET /api/catalogo/produtos/{id}`
```http
# Se produto tem lote
HTTP/1.1 301 Moved Permanently
Location: /api/lotes/{loteId}

# Se produto não tem lote
HTTP/1.1 410 Gone
{
  "success": false,
  "message": "Produto não está mais disponível publicamente. Apenas produtos em lotes são exibidos.",
  "error": "PRODUCT_NOT_PUBLIC"
}
```

---

## 📊 **Cronograma de Remoção**

| Fase | Data | Ação |
|------|------|------|
| **Fase 1** | 19/12/2024 | ✅ Implementação de redirects |
| **Fase 2** | 26/12/2024 | Monitoramento e ajustes |
| **Fase 3** | 02/01/2025 | Remoção completa dos endpoints |
| **Fase 4** | 09/01/2025 | Limpeza final do código |

---

## 🧪 **Como Testar**

### Teste 1: Redirect de Catálogo
```bash
curl -I "http://localhost:8080/api/catalogo/produtos"
# Esperado: HTTP/1.1 301 Moved Permanently
```

### Teste 2: Novo Catálogo de Lotes
```bash
curl "http://localhost:8080/api/lotes/catalogo-publico?page=0&size=10"
# Esperado: HTTP/1.1 200 OK com lista de lotes
```

### Teste 3: Frontend Redirect
```bash
# Acesse: http://localhost:4200/catalogo-produtos
# Esperado: Redirect automático para /catalogo
```

---

## 🆘 **Suporte e Dúvidas**

### Problemas Comuns

#### 1. **Erro 404 ao acessar produto diretamente**
**Causa:** Produto não está em um lote ativo  
**Solução:** Navegue pelo catálogo de lotes

#### 2. **Links antigos quebrados**
**Causa:** Bookmarks ou links externos  
**Solução:** Atualize para usar `/catalogo` (lotes)

#### 3. **API retorna redirect inesperado**
**Causa:** Usando endpoint depreciado  
**Solução:** Migre para `/api/lotes/catalogo-publico`

### Contato para Suporte
- **Equipe de Desenvolvimento**: dev-team@leilao.com
- **Documentação**: [Link para docs internas]
- **Issues**: [Link para sistema de tickets]

---

## 📚 **Recursos Adicionais**

### Documentação
- [Guia do Novo Sistema de Lotes](./sprint_02.03_backlog_v2.md)
- [API Reference - Lotes](./api-reference-lotes.md)
- [Frontend Components Guide](./frontend-components.md)

### Collections Postman
- [04-Produtos-Updated.postman_collection.json](../backend/postman/04-Produtos-Updated.postman_collection.json)
- [Historia02-Catalogo-Lotes.postman_collection.json](../backend/postman/Historia02-Catalogo-Lotes.postman_collection.json)

### Exemplos de Código
- [Exemplo de Migração Frontend](./examples/frontend-migration.md)
- [Exemplo de Migração API](./examples/api-migration.md)

---

**Documento atualizado em:** 19 de Dezembro de 2024  
**Versão:** 1.0  
**Próxima revisão:** 26 de Dezembro de 2024