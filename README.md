# 🛒 Ecommerce API

API RESTful de e-commerce desenvolvida com Java 17 e Spring Boot 3, seguindo boas práticas de arquitetura backend.

## 🚀 Tecnologias

- Java 17
- Spring Boot 3
- Spring Security + JWT
- Spring Data JPA
- MySQL 8
- Docker + Docker Compose
- Maven

## 🏗️ Arquitetura

```
Controller → Service → Repository → Database
                ↓
             Mapper
                ↓
               DTO
```

Separação clara de responsabilidades com DTO pattern, tratamento global de exceções e validação de dados.

## 🔐 Autenticação

A API utiliza autenticação via JWT. Endpoints protegidos exigem o token no header:

```
Authorization: Bearer <token>
```

### Permissões por endpoint

| Endpoint | Método | Acesso |
|---|---|---|
| `/auth/login` | POST | Público |
| `/users` | POST | Público |
| `/products` | GET | Público |
| `/products/{id}` | GET | Público |
| `/products` | POST | ADMIN |
| `/products/{id}` | PUT | ADMIN |
| `/products/{id}` | DELETE | ADMIN |
| `/users` | GET | ADMIN |
| `/users/{id}` | DELETE | ADMIN |
| `/users/{id}` | GET | Autenticado |
| `/users/{id}` | PUT | Autenticado |

## ⚙️ Como rodar o projeto

### Pré-requisitos
- Docker
- Docker Compose

### Passo a passo

**1. Clone o repositório**
```bash
git clone https://github.com/seu-usuario/ecommerce-api.git
cd ecommerce-api
```

**2. Configure as variáveis de ambiente**
```bash
cp .env.example .env
```
Edite o `.env` com suas credenciais.

**3. Suba o ambiente**
```bash
docker-compose up --build
```

A API estará disponível em `http://localhost:8080`.

## 📋 Exemplos de uso

### Criar usuário
```http
POST /users
Content-Type: application/json

{
  "name": "Eduardo",
  "email": "edu@email.com",
  "password": "123456",
  "role": "ROLE_ADMIN"
}
```

### Login
```http
POST /auth/login
Content-Type: application/json

{
  "email": "edu@email.com",
  "password": "123456"
}
```

Resposta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### Criar produto (requer token ADMIN)
```http
POST /products
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Teclado Mecânico",
  "price": 299.90,
  "quantity": 50
}
```

## 📁 Estrutura do projeto

```
src/main/java/com/eduardo/ecommerce/
├── config/          # SecurityConfig
├── controller/      # Controllers REST
├── dto/             # DTOs de entrada e saída
├── exception/       # Exceções e handler global
├── mapper/          # Conversão Entity ↔ DTO
├── model/           # Entidades JPA
├── repository/      # Interfaces JPA
├── security/        # JWT Filter, JwtService, UserDetailsService
└── service/         # Regras de negócio
```

## 🔜 Próximos passos

- [ ] Testes unitários com JUnit 5 e Mockito
- [ ] Módulo de Pedidos (Order)
- [ ] Refresh Token