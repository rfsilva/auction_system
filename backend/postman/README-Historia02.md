# História 02 - Catálogo de Lotes - Testes Postman

## 📋 Visão Geral

Esta collection testa os endpoints da **História 02: Transformação do Catálogo em Catálogo de Lotes** da Sprint S2.3 V2.

## 🚀 Como Usar

### 1. Importar no Postman

1. Abra o Postman
2. Clique em **Import**
3. Importe os arquivos:
   - `Historia02-Lotes-Environment.postman_environment.json`
   - `Historia02-Catalogo-Lotes.postman_collection.json`

### 2. Configurar Environment

1. Selecione o environment **"História 02 - Catálogo de Lotes"**
2. Verifique se a `baseUrl` está correta: `http://localhost:8080`
3. Os demais valores são baseados na massa de dados do projeto

### 3. Executar Testes

#### Ordem Recomendada:

1. **Auth > Login Admin** - Para obter token de autenticação
2. **Catálogo Público de Lotes** - Testar todos os cenários
3. **Lotes em Destaque** - Testar lotes encerrando em 1 semana
4. **Detalhes do Lote** - Testar busca por ID
5. **Testes de Validação** - Casos extremos
6. **Compatibilidade** - Endpoints antigos

## 📊 Endpoints Testados

### 🔓 Públicos (sem autenticação)

| Endpoint | Método | Descrição |
|----------|--------|-----------|
| `/api/lotes/catalogo-publico` | GET | Catálogo público de lotes |
| `/api/lotes/destaque` | GET | Lotes encerrando em 1 semana |
| `/api/lotes/{id}` | GET | Detalhes de um lote |

### 🔒 Autenticados

| Endpoint | Método | Descrição |
|----------|--------|-----------|
| `/api/auth/login` | POST | Login para obter token |

## 🧪 Cenários de Teste

### 1. Catálogo Público
- ✅ Listagem básica com paginação padrão (10 itens)
- ✅ Filtro por categoria
- ✅ Busca textual no título/descrição
- ✅ Ordenação por recentes
- ✅ Paginação configurável (50 por página)

### 2. Lotes em Destaque
- ✅ Lotes encerrando em 1 semana
- ✅ Validação de tempo restante ≤ 7 dias
- ✅ Apenas lotes ativos com produtos válidos

### 3. Detalhes do Lote
- ✅ Busca por ID válido
- ✅ Tratamento de ID inexistente (404)

### 4. Validações
- ✅ Paginação inválida (page=-1, size=0)
- ✅ Ordenação inválida (usa padrão)

### 5. Performance
- ✅ Tempo de resposta < 2000ms
- ✅ Headers corretos (Content-Type: application/json)

## 📋 Massa de Dados Utilizada

### Usuários de Teste
- **Admin:** admin@leilao.com / admin123
- **Vendedor:** ana.santos@email.com / senha123
- **Comprador:** fernanda.costa@email.com / senha123

### Lotes de Teste
- **Lote Eletrônicos Premium:** `880e8400-e29b-41d4-a716-446655440001`
- **Lote Livros Clássicos:** `880e8400-e29b-41d4-a716-446655440002`
- **Lote Arte Contemporânea:** `880e8400-e29b-41d4-a716-446655440003`

### Produtos de Teste
- **Samsung Galaxy S23:** `990e8400-e29b-41d4-a716-446655440001`
- **iPhone 14 Pro:** `990e8400-e29b-41d4-a716-446655440002`
- **Dom Casmurro 1ª Ed:** `990e8400-e29b-41d4-a716-446655440006`

### Categorias
- Eletrônicos
- Livros
- Arte
- Colecionáveis
- Móveis
- Roupas

## 🔍 Regras de Negócio Testadas

### Visibilidade de Lotes
- ✅ Apenas lotes com status `ACTIVE`
- ✅ Apenas lotes com produtos válidos (`ACTIVE` ou `PUBLISHED`)
- ✅ Lotes sem produtos válidos não aparecem

### Paginação
- ✅ Padrão: 10 lotes por página
- ✅ Opções: 10, 20, 50 por página
- ✅ Validação de parâmetros inválidos

### Ordenação
- ✅ **proximidade_encerramento:** Por `loteEndDateTime ASC` (padrão)
- ✅ **recentes:** Por `createdAt DESC`
- ✅ **alfabetica:** Por `title ASC`

### Filtros
- ✅ **Categoria:** Filtra por categoria do contrato
- ✅ **Busca textual:** Busca no título e descrição do lote
- ✅ Combinação de filtros

### Lotes em Destaque
- ✅ Encerramento entre agora e 1 semana (7 dias)
- ✅ Apenas lotes ativos
- ✅ Ordenados por proximidade de encerramento

## 🚨 Possíveis Erros e Soluções

### 1. Erro 401 - Unauthorized
**Causa:** Token expirado ou inválido  
**Solução:** Execute "Login Admin" novamente

### 2. Erro 404 - Not Found
**Causa:** Endpoint não encontrado  
**Solução:** Verifique se o backend está rodando na porta 8080

### 3. Erro 500 - Internal Server Error
**Causa:** Erro no backend ou banco de dados  
**Solução:** Verifique logs do backend e se o banco está populado

### 4. Resposta vazia
**Causa:** Massa de dados não carregada  
**Solução:** Execute os scripts de migração (Flyway) para popular o banco

## 📈 Métricas Esperadas

### Performance
- ✅ Tempo de resposta < 2000ms
- ✅ Catálogo com 10 lotes: ~200-500ms
- ✅ Busca com filtros: ~300-800ms

### Dados
- ✅ Total de lotes ativos: ~10-15
- ✅ Lotes em destaque: ~3-5
- ✅ Produtos válidos por lote: 1-5

## 🔄 Execução Automatizada

Para executar todos os testes automaticamente:

1. Selecione a collection
2. Clique em **Run**
3. Configure:
   - Environment: "História 02 - Catálogo de Lotes"
   - Iterations: 1
   - Delay: 100ms
4. Clique em **Run História 02**

## 📝 Logs e Debug

Os testes incluem logs detalhados no console do Postman:
- URLs das requisições
- Tempos de resposta
- Dados retornados
- Validações executadas

Para ver os logs:
1. Abra o **Console** do Postman (View > Show Postman Console)
2. Execute os testes
3. Acompanhe os logs em tempo real

## 🎯 Critérios de Sucesso

A História 02 está implementada corretamente se:

- ✅ Todos os testes passam (status verde)
- ✅ Performance < 2000ms
- ✅ Apenas lotes com produtos válidos são retornados
- ✅ Paginação configurável funciona
- ✅ Filtros aplicam corretamente
- ✅ Lotes em destaque respeitam critério de 1 semana
- ✅ Tratamento adequado de erros (404, validações)

---

**Versão:** 1.0.0  
**Data:** 2024-01-15  
**Autor:** História 02 - Sprint S2.3 V2