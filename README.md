# 🛒 E-commerce API - Spring Boot

API RESTful desenvolvida com **Spring Boot** para gerenciamento de produtos de um e-commerce.

Este projeto foi criado com foco em boas práticas de arquitetura, separação de responsabilidades e uso de tecnologias modernas do ecossistema Java.

---

## 🚀 Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- MySQL
- Maven
- Docker & Docker Compose
- Bean Validation (Jakarta Validation)

---

## 📌 Funcionalidades

- ✅ Criar produto
- ✅ Listar todos os produtos
- ✅ Buscar produto por ID
- ✅ Atualizar produto
- ✅ Deletar produto
- ✅ Tratamento global de exceções
- ✅ Validação de dados de entrada
- ✅ Containerização com Docker

---

## 🏗️ Arquitetura do Projeto

O projeto segue uma arquitetura em camadas:

```
controller → service → repository → database
```

### 📂 Estrutura

```
com.eduardo.ecommerce
│
├── controller        # Camada responsável pelos endpoints (API)
├── service           # Regras de negócio
├── repository        # Comunicação com o banco de dados
├── model             # Entidades JPA
├── dto               # Objetos de transferência de dados
├── mapper            # Conversão entre Entity e DTO
└── exception         # Tratamento global de erros
```

---

## 🔗 Endpoints da API

### ➕ Criar Produto
```
POST /products
```

Body:
```json
{
  "name": "Notebook",
  "price": 3500.0,
  "quantity": 10
}
```

---

### 📋 Listar Produtos
```
GET /products
```

---

### 🔍 Buscar Produto por ID
```
GET /products/{id}
```

---

### ✏ Atualizar Produto
```
PUT /products/{id}
```

Body:
```json
{
  "name": "Notebook Gamer",
  "price": 4500.0,
  "quantity": 5
}
```

---

### ❌ Deletar Produto
```
DELETE /products/{id}
```

---

## 💾 Como Rodar Localmente (Sem Docker)

### 1️⃣ Clonar o repositório

```bash
git clone https://github.com/seu-usuario/ecommerce-api.git
cd ecommerce-api
```

### 2️⃣ Configurar o banco MySQL

Crie um banco:

```sql
CREATE DATABASE ecommerce;
```

Configure no `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### 3️⃣ Rodar o projeto

```bash
mvn spring-boot:run
```

A API ficará disponível em:

```
http://localhost:8080
```

---

## 🐳 Como Rodar com Docker

### 1️⃣ Gerar o JAR

```bash
mvn clean package -DskipTests
```

### 2️⃣ Subir os containers

```bash
docker compose up --build
```

Isso irá subir:

- 📦 Container da API
- 🐬 Container do MySQL

A aplicação ficará disponível em:

```
http://localhost:8080
```

Para parar:

```bash
docker compose down
```

---

## 🧪 Testes

Os endpoints podem ser testados com:

- Postman
- Insomnia
- Thunder Client (VS Code)

---

## 🛠️ Conceitos Aplicados

- API REST
- Arquitetura em camadas
- DTO Pattern
- Mapper Pattern
- Tratamento global de exceções
- Validação de dados
- Containerização
- Separação de ambientes

---

## 👨‍💻 Autor

Desenvolvido por **Eduardo Mutinelli**  
📍 Campinas - SP  
🔗 LinkedIn: https://www.linkedin.com/in/eduardomutinelli

---

## 📄 Licença

Este projeto foi desenvolvido para fins de estudo e portfólio.
