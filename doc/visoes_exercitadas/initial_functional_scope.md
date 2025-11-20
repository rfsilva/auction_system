# **Especificação Funcional Completa – Sistema de Leilão Eletrônico**

---

# **1. Visão Geral do Sistema**

O Sistema de Leilão Eletrônico é uma plataforma web destinada à realização de leilões online envolvendo vendedores profissionais, compradores cadastrados e a administração da plataforma. O sistema oferece funcionalidades de cadastro de produtos, agrupamento em lotes, controle de horários de encerramento, mecanismo de lances, arremate, cálculo de frete via API externa, cobrança de taxa de leiloeiro definida contratualmente, emissão de documentos e notificações.

A solução deve ser responsiva, segura, escalável e compatível com altos volumes de acessos durante períodos de encerramento de leilões.

---

# **2. Atores do Sistema**

### **2.1. Comprador**

* Pessoa física ou jurídica interessada em participar dos leilões.
* Deve realizar cadastro com dupla validação (e-mail + telefone/SMS).
* Pode visualizar lotes/produtos, enviar lances, acompanhar arremates e solicitar retirada/envio.

### **2.2. Vendedor**

* Pessoa física ou jurídica que utiliza o sistema para vender produtos.
* Possui cadastro próprio e um **contrato digital** associando regras comerciais, incluindo **taxa de leiloeiro**.
* Cadastra produtos e solicita criação de lotes.

### **2.3. Administrador da Plataforma**

* Responsável pelo gerenciamento do sistema, controle de usuários, aprovação de produtos, configuração de taxas, monitoramento de leilões, resolução de disputas, emissão de relatórios.

### **2.4. Sistema Externo de Frete (API)**

* Serviço contratado pela plataforma.
* Recebe informações de origem, destino, peso, dimensões e modalidade.
* Retorna prazos e valores.

---

# **3. Entidades Principais**

## **3.1. Usuário**

* ID
* Nome completo
* E-mail (validação obrigatória)
* Telefone (validação via SMS)
* CPF/CNPJ
* Endereço principal
* Data de cadastro
* Status (Ativo, Bloqueado, Aguardando Validação)
* Perfis: Comprador / Vendedor / Administrador

## **3.2. Vendedor**

* ID do vendedor (FK usuário)
* Razão social / nome
* CNPJ/CPF
* Contrato associado (FK)
* Taxa de leiloeiro (definida no contrato)
* Dados bancários

## **3.3. Contrato**

* ID do contrato
* Vendedor (FK)
* Taxa de leiloeiro (%)
* Taxa mínima (R$)
* Validade
* Modalidade (percentual por arremate, taxa fixa, híbrida)

## **3.4. Produto**

* ID do produto
* Vendedor (FK)
* Título
* Descrição completa
* Categoria
* Fotos
* Peso e dimensões para cálculo de frete
* Lance mínimo
* Incremento mínimo
* Data/hora **individual** de encerramento
* Status: Ativo, Aguardando Lote, Em Leilão, Vendido, Não Vendido

## **3.5. Lote**

* ID do lote
* Vendedor (FK)
* Título
* Descrição
* Lista de produtos (1:N)
* **Data/hora de encerramento geral do lote**
* Status (Aberto, Encerrado Parcialmente, Encerrado Totalmente)

**Regras:**

* Cada produto pode ter **data/hora de encerramento mais curta** do que a do lote.
* O lote só é considerado encerrado quando **todos os produtos estiverem encerrados**.

## **3.6. Lance**

* ID do lance
* ID do comprador
* ID do produto
* Valor
* Data/hora
* Via (Web, App)
* Status (Aceito, Rejeitado)

## **3.7. Arremate**

* ID do arremate
* Produto
* Comprador
* Valor final
* Taxa do leiloeiro aplicada
* Valor líquido do vendedor
* Data/hora

## **3.8. Documento**

* Nota de arremate
* Recibo
* Contrato de venda
* Comprovante de pagamento

## **3.9. Notificação**

* E-mail
* SMS
* Push
* Eventos automatizados

---

# **4. Fluxos Principais do Sistema**

## **4.1. Cadastro de Usuário com Dupla Validação**

1. Usuário envia dados de cadastro.
2. Sistema envia e-mail com link de confirmação.
3. Usuário confirma e-mail.
4. Sistema envia código SMS.
5. Usuário valida telefone.
6. Conta é ativada.

## **4.2. Cadastro de Vendedor e Assinatura de Contrato**

1. Usuário solicita habilitação como vendedor.
2. Plataforma valida documentos.
3. Um contrato é gerado (PDF) com taxa do leiloeiro.
4. Vendedor assina digitalmente.
5. Conta é habilitada para cadastrar produtos.

## **4.3. Cadastro de Produto**

1. Vendedor cria produto.
2. Define título, descrição, fotos.
3. Preenche peso e dimensões.
4. Define lance mínimo e incremento.
5. Define **data/hora de encerramento individual**.
6. Admin revisa e aprova.

## **4.4. Criação de Lote**

1. Vendedor solicita lote.
2. Admin agrupa produtos.
3. Define data/hora geral de encerramento.
4. Produtos recebem status "Em Leilão".

## **4.5. Fluxo de Lances**

1. Comprador envia lance.
2. Sistema verifica:

   * Incremento mínimo
   * Horário de encerramento do produto
   * Se o comprador está habilitado
3. Lance aceito → vira maior lance.
4. Contagem regressiva exibida.
5. Se o produto estiver nos minutos finais, regras de anti-sniping podem ser aplicadas.

## **4.6. Encerramento do Produto**

1. Ao chegar no horário individual do produto → sistema encerra somente o produto.
2. Geração do arremate.
3. Cálculo da taxa de leiloeiro.
4. Emissão de documento.
5. Notificação ao comprador e vendedor.

## **4.7. Encerramento do Lote**

* O lote só encerra quando **todos** os produtos estiverem encerrados.

## **4.8. Cálculo de Frete (API Externa)**

1. Arremate ocorre.
2. Sistema envia requisição à API de frete contendo:

   * Origem (endereço do vendedor)
   * Destino (endereço do comprador)
   * Peso
   * Dimensões
3. API retorna preço e prazo.
4. Sistema exibe opções de frete.

## **4.9. Pagamento e Liquidação**

1. Comprador paga o valor final.

   * Valor do arremate
   * Frete
   * Taxas (se houver)
2. Plataforma retém taxa de leiloeiro.
3. Plataforma liquida o valor líquido para o vendedor.

---

# **5. Regras de Negócio**

### **RN01 – Vendedor é entidade separada**

Um usuário só pode operar como vendedor após aprovação e contrato.

### **RN02 – Produto possui encerramento individual**

Cada produto possui sua própria data/hora de encerramento.

### **RN03 – Encerramento do lote**

O lote só encerra quando todos os produtos estiverem encerrados.

### **RN04 – Taxa de leiloeiro**

* Definida no **contrato do vendedor**.
* Pode ser percentual, fixa ou híbrida.

### **RN05 – Lances válidos**

* Devem respeitar incremento mínimo.
* Devem ser maiores do que o lance atual.

### **RN06 – Anti-sniping**

* Encerramento é prorrogado em X segundos se houver lance nos últimos Y segundos.

### **RN07 – Integração com API externa de frete**

* Obrigatório cálculo automático após arremate.

### **RN08 – Notificações obrigatórias**

* Abertura de lote
* Lance superado
* Produto encerrado
* Produto arrematado
* Pagamento aprovado

---

# **6. Estrutura de Documentos Gerados**

### **6.1. Nota de Arremate**

* Dados do comprador
* Dados do vendedor
* Produto
* Valor do arremate
* Taxa do leiloeiro
* Valor total
* Informações de frete

### **6.2. Contrato do vendedor**

* Dados pessoais
* Taxa do leiloeiro
* Vigência
* Regras de operação

### **6.3. Relatórios Administrativos**

* Relatório de vendas por vendedor
* Arrecadação da taxa de leiloeiro
* Relatório de produtos não vendidos

---

# **7. Notificações Automáticas**

### **E-mail**

* Confirmação de cadastro
* Lance recebido
* Lance superado
* Produto arrematado
* Documentos disponíveis

### **SMS**

* Código para validação
* Avisos opcionais de encerramento

### **Push**

* Lances
* Avisos de última hora

---

# **8. APIs (Modelo de Referência)**

## **8.1. Autenticação**

```
POST /auth/signup
POST /auth/confirm-email
POST /auth/confirm-phone
POST /auth/login
```

## **8.2. Vendedores**

```
POST /vendors
GET /vendors/{id}
POST /vendors/{id}/contract
```

## **8.3. Produtos**

```
POST /products
GET /products/{id}
GET /products?lote={id}&status=...
PATCH /products/{id}
```

## **8.4. Lotes**

```
POST /lots
GET /lots/{id}
GET /lots?status=...
```

## **8.5. Lances**

```
POST /products/{id}/bid
GET /products/{id}/bids
```

## **8.6. Frete (API Externa)**

```
POST /frete/cotar
```

Payload:

```
{
  "origem": {...},
  "destino": {...},
  "peso": 1.5,
  "dimensoes": { "a": 10, "l": 20, "p": 30 }
}
```

## **8.7. Arremate**

```
GET /products/{id}/winner
POST /checkout/{id}
```

---

# **9. Requisitos Não Funcionais**

### **Performance**

* Capacidade de suportar picos de 10k lances por segundo.

### **Segurança**

* 2FA obrigatório
* Autorização RBAC (Admin / Vendedor / Comprador)
* Criptografia de dados sensíveis

### **Escalabilidade**

* Serviços desacoplados (produtos, lances, pagamentos)

### **Auditabilidade**

* Log completo de lances, acessos, alterações

---

# **10. Roadmap Evolutivo**

### **MVP**

* Cadastro de usuários com dupla validação
* Cadastro de vendedor e contrato básico
* Cadastro de produtos com encerramento individual
* Criação de lotes
* Lances e arremate
* Cálculo de frete via API
* Notificações básicas

### **Versão 2.0**

* Dashboard do vendedor
* Mecanismo avançado de anti-sniping
* Pagamentos integrados
* Relatórios avançados

### **Versão 3.0**

* App mobile completo
* Live streaming do leilão
* Chat em tempo real

---

# **11. Conclusão**

Este documento consolida todos os requisitos funcionais e de negócio para o sistema de leilão eletrônico, considerando a separação de entidades (vendedor), encerramentos independentes por produto, cálculo automático de frete via API externa e aplicação de taxa de leiloeiro definida em contrato. A estrutura permite escalabilidade, governança clara e suporte para múltiplos modelos de leilão.

---
