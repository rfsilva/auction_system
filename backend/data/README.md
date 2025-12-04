# 📊 Massa de Dados para Desenvolvimento

Este diretório contém a documentação da massa de dados criada para desenvolvimento e testes do Sistema de Leilão Eletrônico.

## 🗂️ Estrutura das Migrações

As migrações foram organizadas por entidade para facilitar manutenção e compreensão:

| Versão | Entidade | Descrição | Registros |
|--------|----------|-----------|-----------|
| **V1** | Schema Base | Estrutura completa + Admin Root | 1 usuário |
| **V2** | Usuários | Diferentes perfis e roles | 15 usuários |
| **V3** | Vendedores | Empresas e perfis comerciais | 7 vendedores |
| **V4** | Contratos | Diferentes status e cenários | 10 contratos |
| **V5** | Compradores | Perfis KYC e limites variados | 10 compradores |
| **V6** | Produtos | Catálogo com diferentes categorias | 12 produtos |
| **V7** | Lotes | Agrupamentos e cenários temporais | 10 lotes |
| **V8** | Complementares | Lances, favoritos, notificações | 50+ registros |

## 🎯 Cenários de Teste Cobertos

### ✅ **Funcionalidades Principais**
- Sistema completo de usuários e permissões
- Gestão de contratos com diferentes taxas e status
- Catálogo de produtos com moderação
- Sistema de lotes e agrupamentos
- Lances em tempo real com disputas
- Favoritos e sistema de notificações
- Auditoria completa de ações

### ✅ **Perfis de Usuário**
- **Administradores**: Root, secundários, suporte
- **Vendedores**: Empresas grandes, pequenas, individuais
- **Compradores**: VIP, regulares, estudantes, corporativos
- **Participantes**: Visitantes, curiosos

### ✅ **Cenários Especiais**
- Usuários bloqueados e suspensos
- Contratos vencendo e expirados
- Produtos em diferentes status
- Lances com disputas acirradas
- Sistema de pré-autorizações

## 🗺️ Mapa Visual dos Dados

```mermaid
graph TB
    %% Usuários (V2)
    subgraph "👥 USUÁRIOS (V2 - 15 total)"
        U1[🔑 Admin Root<br/>admin@leilao.com]
        U2[👨‍💼 Maria Admin<br/>maria.admin@leilao.com]
        U3[🛠️ João Suporte<br/>joao.suporte@leilao.com]
        
        U10[🏢 Carlos Vendedor<br/>ABC Importadora]
        U11[🎨 Ana Vendedor<br/>Loja da Ana]
        U12[🏺 Roberto Vendedor<br/>Antiquário Silva]
        U13[⚠️ Pedro Suspenso<br/>Vendas Rápidas]
        U40[🔄 Patricia Híbrida<br/>Compra e Vende]
        
        U20[💎 Fernanda VIP<br/>R$ 15k limite]
        U21[👤 Marcos Regular<br/>R$ 5k limite]
        U22[⏳ Juliana Pendente<br/>Verificação KYC]
        U23[📚 Ricardo Colecionador<br/>R$ 25k limite]
        
        U30[👀 Luiza Curiosa<br/>Participante]
        U31[🎓 Gabriel Estudante<br/>R$ 500 limite]
        U50[🚫 Bruno Bloqueado<br/>Tentativas excessivas]
    end

    %% Vendedores (V3)
    subgraph "🏪 VENDEDORES (V3 - 7 total)"
        V10[🏢 ABC Importadora<br/>CNPJ: 12.345.678/0001-90<br/>✅ Verificado]
        V11[🎨 Loja da Ana<br/>CPF: 987.654.321-00<br/>✅ Verificado]
        V12[🏺 Antiquário Silva<br/>CPF: 456.789.123-45<br/>❌ Não Verificado]
        V13[⚠️ Vendas Rápidas<br/>CPF: 789.123.456-78<br/>🚫 Suspenso]
        V40[🔄 Patricia Almeida<br/>CPF: 321.654.987-12<br/>✅ Verificado]
        V60[📖 Sebo do João<br/>CNPJ: 147.258.369-01<br/>✅ Verificado]
        V61[🎸 Harmonia Musical<br/>CNPJ: 258.369.147-02<br/>✅ Verificado]
    end

    %% Contratos (V4)
    subgraph "📋 CONTRATOS (V4 - 10 total)"
        C10[📱 ABC - Geral<br/>3.5% | ✅ ATIVO<br/>Válido +185 dias]
        C11[🎨 Ana - Artesanatos<br/>2.5% | ✅ ATIVO<br/>Válido +275 dias]
        C40[🔄 Patricia - Geral<br/>4.0% | ✅ ATIVO<br/>Válido +245 dias]
        C60[📚 Sebo - Livros<br/>3.0% | ✅ ATIVO<br/>Válido +165 dias]
        C61[🎸 Harmonia - Instrumentos<br/>4.5% | ✅ ATIVO<br/>Válido +215 dias]
        C82[⏰ Patricia - Eletrônicos<br/>3.5% | ⚠️ VENCE EM 15 DIAS]
        
        C12[📋 Antiquário - DRAFT<br/>5.0% | ⏳ Aguardando]
        C13[❌ Vendas Rápidas<br/>6.0% | 🚫 CANCELADO]
        C80[📱 ABC - Histórico<br/>4.0% | ⏰ EXPIRADO]
        C81[🎨 Ana - Decoração<br/>3.0% | ⏸️ SUSPENSO]
    end

    %% Compradores (V5)
    subgraph "🛒 COMPRADORES (V5 - 10 total)"
        B20[💎 Fernanda VIP<br/>R$ 15.000 | ✅ APROVADO<br/>Eletrônicos, Decoração]
        B21[👤 Marcos Regular<br/>R$ 5.000 | ✅ APROVADO<br/>Eletrônicos, Música]
        B22[⏳ Juliana Nova<br/>Sem limite | ⏳ PENDENTE<br/>Artesanatos]
        B23[📚 Ricardo Colecionador<br/>R$ 25.000 | ✅ APROVADO<br/>Antiguidades, Livros]
        B40[🔄 Patricia Híbrida<br/>R$ 8.000 | ✅ APROVADO<br/>Revenda]
        B30[🏢 Luiza Corporativa<br/>R$ 50.000 | ✅ APROVADO<br/>Equipamentos]
        B31[🎓 Gabriel Estudante<br/>R$ 500 | ✅ APROVADO<br/>Livros, Eletrônicos]
        B50[🚫 Bruno Rejeitado<br/>Sem limite | ❌ REJEITADO]
        B51[⏰ Carla Expirada<br/>R$ 1.000 | ⏰ EXPIRADO]
        B99[🌍 Carlos Internacional<br/>R$ 100.000 | ✅ APROVADO<br/>Arte, Antiguidades]
    end

    %% Produtos (V6)
    subgraph "📦 PRODUTOS (V6 - 12 total)"
        P1[📱 Samsung S23 Ultra<br/>R$ 2.800→3.250 | ✅ ATIVO<br/>5 lances | Termina em 3 dias]
        P2[💻 Notebook Dell<br/>R$ 1.800 | 📝 DRAFT<br/>Aguardando publicação]
        P3[📺 Smart TV LG<br/>R$ 4.500 | ⏳ PENDENTE<br/>Aguardando moderação]
        
        P4[🏺 Vaso Cerâmica<br/>R$ 80→125 | ✅ ATIVO<br/>3 lances | Termina em 2 dias]
        P5[🖼️ Quadro Mandala<br/>R$ 120→145 | ✅ ATIVO<br/>Termina em 4 dias]
        P10[💡 Luminária Bambu<br/>R$ 150→280 | ✅ VENDIDA<br/>Histórico]
        
        P6[📖 Dom Casmurro 1899<br/>R$ 2.500→3.800 | ✅ ATIVO<br/>5 lances | Termina em 6 dias]
        P7[📚 Coleção Agatha<br/>R$ 800→950 | ✅ ATIVO<br/>Termina em 8 dias]
        
        P8[🎸 Fender Strat 1978<br/>R$ 8.000→12.500 | ✅ ATIVO<br/>5 lances | Termina em 10 dias]
        
        P9[📱 iPhone 13 Pro<br/>R$ 2.200→2.450 | ✅ ATIVO<br/>3 lances | Termina em 1 dia]
        
        P11[📱 Tablet Samsung<br/>R$ 500 | ⏰ EXPIRADO<br/>Sem lances]
        P12[❌ Produto Cancelado<br/>R$ 300 | 🚫 CANCELADO<br/>Irregularidades]
    end

    %% Lotes (V7)
    subgraph "📦 LOTES (V7 - 10 total)"
        L1[📱 Eletrônicos Premium<br/>✅ ATIVO | 5 dias<br/>2 produtos]
        L2[🎨 Coleção Artesanal<br/>✅ ATIVO | 4 dias<br/>3 produtos]
        L3[📚 Literatura Clássica<br/>✅ ATIVO | 8 dias<br/>2 produtos]
        L4[🎸 Instrumentos Vintage<br/>✅ ATIVO | 12 dias<br/>1 produto]
        L5[🔄 Liquidação Mista<br/>✅ ATIVO | 2 dias<br/>1 produto]
        L10[⚡ Lote Express<br/>✅ ATIVO | 6 horas<br/>0 produtos]
        
        L6[🏠 Eletrodomésticos<br/>📝 DRAFT | 15 dias<br/>Em preparação]
        L7[🎄 Decoração Natalina<br/>✅ FECHADO | Encerrado<br/>Todos vendidos]
        L8[❌ Produtos Diversos<br/>🚫 CANCELADO<br/>Irregularidades]
        L9[🌍 Literatura Internacional<br/>📝 FUTURO | 30 dias<br/>Agendado]
    end

    %% Dados Complementares (V8)
    subgraph "📊 ATIVIDADE (V8)"
        subgraph "💰 LANCES (21 total)"
            LA1[📱 Samsung: 5 lances<br/>R$ 2.850→3.250]
            LA2[🏺 Vaso: 3 lances<br/>R$ 85→125]
            LA3[📖 Dom Casmurro: 5 lances<br/>R$ 2.600→3.800]
            LA4[🎸 Fender: 5 lances<br/>R$ 8.500→12.500]
            LA5[📱 iPhone: 3 lances<br/>R$ 2.250→2.450]
        end
        
        subgraph "⭐ FAVORITOS (10 total)"
            F1[💎 Fernanda: 3 favoritos<br/>Samsung, Fender, Vaso]
            F2[📚 Ricardo: 3 favoritos<br/>Dom Casmurro, Agatha, Fender]
            F3[👤 Marcos: 2 favoritos<br/>Samsung, iPhone]
            F4[🎓 Gabriel: 2 favoritos<br/>Livros]
        end
        
        subgraph "🔔 NOTIFICAÇÕES (9 total)"
            N1[📢 Lance superado: 3<br/>Fernanda, Marcos, Ricardo]
            N2[⏰ Terminando: 2<br/>Samsung, iPhone]
            N3[🆕 Novos produtos: 2<br/>Por categoria]
            N4[⚙️ Sistema: 2<br/>Verificações]
        end
        
        subgraph "💳 PRÉ-AUTORIZAÇÕES (3)"
            PA1[💎 Fernanda: R$ 5.000<br/>Samsung autorizado]
            PA2[📚 Ricardo: R$ 5.000<br/>Dom Casmurro autorizado]
            PA3[👤 Marcos: R$ 3.000<br/>iPhone autorizado]
        end
    end

    %% Relações principais
    U10 --> V10
    U11 --> V11
    U12 --> V12
    U13 --> V13
    U40 --> V40
    U10 --> V60
    U11 --> V61

    V10 --> C10
    V11 --> C11
    V40 --> C40
    V60 --> C60
    V61 --> C61
    V40 --> C82
    V12 --> C12
    V13 --> C13

    U20 --> B20
    U21 --> B21
    U22 --> B22
    U23 --> B23
    U40 --> B40
    U30 --> B30
    U31 --> B31

    V10 --> P1
    V10 --> P2
    V10 --> P3
    V11 --> P4
    V11 --> P5
    V60 --> P6
    V60 --> P7
    V61 --> P8
    V40 --> P9

    V10 --> L1
    V11 --> L2
    V60 --> L3
    V61 --> L4
    V40 --> L5

    P1 --> L1
    P4 --> L2
    P5 --> L2
    P6 --> L3
    P7 --> L3
    P8 --> L4
    P9 --> L5

    %% Lances
    B20 --> LA1
    B21 --> LA1
    B23 --> LA1
    B40 --> LA1

    B21 --> LA2
    B30 --> LA2
    B31 --> LA2

    B23 --> LA3
    B20 --> LA3

    B23 --> LA4
    B20 --> LA4

    B21 --> LA5
    B31 --> LA5

    %% Favoritos
    B20 --> F1
    B23 --> F2
    B21 --> F3
    B31 --> F4

    %% Estilo
    classDef admin fill:#ff6b6b,stroke:#d63031,color:#fff
    classDef vendedor fill:#74b9ff,stroke:#0984e3,color:#fff
    classDef comprador fill:#55a3ff,stroke:#2d3436,color:#fff
    classDef produto fill:#00b894,stroke:#00a085,color:#fff
    classDef lote fill:#fdcb6e,stroke:#e17055,color:#000
    classDef ativo fill:#00b894,stroke:#00a085,color:#fff
    classDef inativo fill:#ddd,stroke:#999,color:#000
    classDef problema fill:#ff7675,stroke:#d63031,color:#fff

    class U1,U2,U3 admin
    class U10,U11,U12,U40,V10,V11,V12,V40,V60,V61 vendedor
    class U20,U21,U22,U23,U30,U31,B20,B21,B22,B23,B30,B31,B40 comprador
    class P1,P4,P5,P6,P7,P8,P9,C10,C11,C40,C60,C61,C82 ativo
    class P2,P3,P11,C12,C80,C81,L6,L7,L9 inativo
    class U13,U50,V13,P12,C13,L8,B50,B51 problema
    class P1,P2,P3,P4,P5,P6,P7,P8,P9,P10,P11,P12 produto
    class L1,L2,L3,L4,L5,L6,L7,L8,L9,L10 lote
```

## 🔐 Usuários de Teste

### **Administradores**
| Email | Senha | Perfil |
|-------|-------|--------|
| `admin@leilao.com` | `password` | Admin Root |
| `maria.admin@leilao.com` | `password` | Admin Secundário |
| `joao.suporte@leilao.com` | `password` | Admin Suporte |

### **Vendedores**
| Email | Senha | Empresa | Status |
|-------|-------|---------|--------|
| `carlos.vendedor@empresaabc.com` | `password` | ABC Importadora | ✅ Ativo |
| `ana@lojadaana.com.br` | `password` | Loja da Ana | ✅ Ativo |
| `roberto@antiqualhas.net` | `password` | Antiquário Silva | ⏳ Não Verificado |
| `patricia@compraevende.com` | `password` | Patricia Almeida | ✅ Híbrido |

### **Compradores**
| Email | Senha | Perfil | Limite |
|-------|-------|--------|--------|
| `fernanda.lima@email.com` | `password` | VIP | R$ 15.000 |
| `marcos.pereira@gmail.com` | `password` | Regular | R$ 5.000 |
| `ricardo.colecoes@yahoo.com` | `password` | Colecionador | R$ 25.000 |
| `gabriel.estudante@usp.br` | `password` | Estudante | R$ 500 |

## 📊 Estatísticas dos Dados

### **Distribuição por Categoria**
- **Eletrônicos**: 5 produtos (mais popular)
- **Artesanatos**: 3 produtos
- **Livros**: 2 produtos
- **Instrumentos Musicais**: 1 produto
- **Diversos**: 1 produto

### **Faixas de Preço**
- **Baixo** (R$ 80 - R$ 500): 3 produtos
- **Médio** (R$ 800 - R$ 3.000): 5 produtos
- **Alto** (R$ 3.000 - R$ 15.000): 4 produtos

### **Status dos Produtos**
- **Ativos**: 7 produtos (58%)
- **Draft/Pendente**: 2 produtos (17%)
- **Finalizados**: 3 produtos (25%)

### **Atividade de Lances**
- **Total de lances**: 21
- **Produtos com disputa**: 5
- **Lance mais alto**: R$ 12.500 (Fender Stratocaster)
- **Maior incremento**: R$ 1.500

## 🎯 Como Usar os Dados

### **1. Para Desenvolvimento**
```bash
# Execute as migrações em ordem
mvn flyway:migrate
```

### **2. Para Testes Automatizados**
- Use os IDs fixos dos dados para testes consistentes
- Produtos ativos para testar lances
- Usuários com diferentes permissões

### **3. Para Demonstrações**
- Login com usuários de diferentes perfis
- Produtos com lances ativos para mostrar disputas
- Diferentes cenários de contrato

### **4. Para Validação de Funcionalidades**
- Teste sistema de favoritos
- Validação de notificações
- Fluxo completo de compra
- Gestão de contratos

## 🔄 Atualizações dos Dados

Os dados são **estáticos** e **consistentes** entre execuções. Para resetar:

```bash
# Limpar e recriar
mvn flyway:clean
mvn flyway:migrate
```

## 📝 Notas Importantes

- **Senhas**: Todas as senhas são `password` (hash bcrypt)
- **Datas**: Relativas ao momento da execução
- **IDs**: UUIDs fixos para consistência
- **Dados**: Realistas e brasileiros
- **Relações**: Todas as FKs são válidas

---

**💡 Dica**: Use este README como referência rápida durante desenvolvimento e testes!