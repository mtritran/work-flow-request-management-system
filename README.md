# 📋 Workflow Request Management System

## 🚀 Overview

This project is a **Spring Boot Workflow Request Management System** that provides essential APIs for managing internal workflow requests with authentication & authorization. It supports multiple request types (Purchase, Account, IT Equipment, Document Approval) with approval/rejection workflow. The project is designed with **Dockerized deployment**, **role-based security**, and **monitoring via Spring Boot Actuator**.

## ✨ Features

* **Authentication & Authorization** with JWT and role-based access (`USER`, `ADMIN`)
* **User Management**: register, login, update profile, admin manages users
* **Request Management**: Create, view, approve, and reject internal requests
* **Multiple Request Types**: 
  - Purchase Request (Yêu cầu mua thiết bị)
  - Account Request (Yêu cầu cấp tài khoản)
  - IT Equipment Request (Yêu cầu xin thiết bị IT)
  - Document Approval Request (Yêu cầu phê duyệt công văn)
* **Request Status Workflow**: PENDING → APPROVED/REJECTED
* **Security**: Spring Security + JWT + Role-based access
* **Monitoring**: Spring Boot Actuator (health, metrics, env, info)

## 🛠️ Tech Stack

* **Backend**: Spring Boot 3, Spring Security, JWT, JPA/Hibernate
* **Database**: MySQL 8
* **Build Tool**: Maven
* **Containerization**: Docker & Docker Compose
* **Monitoring**: Spring Boot Actuator
* **API Documentation**: Swagger/OpenAPI

## 📂 Project Structure

```
src/main/java/com/mtritran/e_commerce_order_service/
├── configuration/     # Security, Swagger, JWT configs
├── controller/        # REST controllers
├── dto/              # Request/Response DTOs
├── entity/           # JPA entities (Request, User, Role, etc.)
├── enums/            # Enums (RequestStatus, RequestType, RoleEnum)
├── exception/        # Exception handling
├── mapper/           # MapStruct mappers
├── repository/       # JPA repositories
└── service/          # Business logic
```

## 🔑 API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout
- `POST /api/users` - User registration

### Requests (User)
- `POST /api/requests` - Create new request
- `GET /api/requests/my` - Get my requests
- `GET /api/requests/{id}` - Get request by ID

### Requests (Admin)
- `GET /api/requests` - Get all requests
- `PUT /api/requests/{id}/approve` - Approve request
- `PUT /api/requests/{id}/reject` - Reject request

### User Management (Admin)
- `GET /api/users` - Get all users
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

## 📊 Request Types

1. **PURCHASE** - Purchase equipment requests
2. **ACCOUNT** - Account creation requests
3. **IT_EQUIPMENT** - IT equipment requests
4. **DOCUMENT_APPROVAL** - Document approval requests

## 📈 Request Status Flow

```
PENDING → APPROVED (by Admin)
       → REJECTED (by Admin)
```

## ⚙️ Setup & Run

### 1️⃣ Prerequisites
- Java 21
- Maven 3.8+
- MySQL 8.0+
- Docker & Docker Compose (optional)

### 2️⃣ Database Setup
Create MySQL database:
```sql
CREATE DATABASE mydb;
```

### 3️⃣ Configuration
Update `src/main/resources/application.yaml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mydb
    username: your_username
    password: your_password
```

### 4️⃣ Run Application
```bash
# Build project
mvn clean install

# Run application
mvn spring-boot:run
```

### 5️⃣ Docker Setup (Optional)
```bash
# Build and run with Docker Compose
docker compose build
docker compose up -d
```

## 🔐 Security

- JWT-based authentication
- Role-based authorization (USER, ADMIN)
- Password encryption with BCrypt
- Protected endpoints require authentication

## 📝 Example Request Creation

```json
POST /api/requests
{
  "requestType": "PURCHASE",
  "title": "Request for new laptop",
  "description": "Need a new laptop for development work",
  "details": {
    "equipment": "Laptop",
    "quantity": "1",
    "estimatedCost": "15000000"
  }
}
```

## 🧪 Testing

Access Swagger UI at: `http://localhost:8080/api/swagger-ui/index.html`

## 📦 Deployment

The application can be deployed using Docker:
```bash
docker build -t workflow-request-service .
docker run -p 8080:8080 workflow-request-service
```

## 📄 License

This project is for demonstration purposes.
