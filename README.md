# Saga-Shard-Wallet 🏦

**Final Year Project - Distributed Wallet System**

A distributed wallet system built using Spring Boot that implements advanced distributed computing patterns including SAGA orchestration, database sharding, and real-time consistency handling across distributed services.

## 📋 Project Overview

We'll be working on a Distributed Wallet System built using Spring Boot.

Our project aims to implement key features such as user balance management, transactions, and real-time consistency handling across distributed services. Through this project, we hope to gain a deeper understanding of distributed transactions, integrate consistency-aware databases, and apply scalability techniques like SAGA pattern, sharding, and choreography vs orchestration.

## ✨ Key Features

- **User Management**: Secure user registration and authentication with JWT tokens
- **Wallet Operations**: Create, manage, and monitor digital wallets
- **Transaction Processing**: Credit, debit, and transfer operations with ACID properties
- **SAGA Pattern Implementation**: Distributed transaction management with compensation logic
- **Database Sharding**: Horizontal scaling using Apache ShardingSphere
- **Real-time Consistency**: Ensuring data consistency across distributed services
- **Security**: JWT-based authentication and Spring Security integration
- **API Documentation**: Interactive API documentation with Swagger/OpenAPI

## 🏗️ Architecture

### Core Components

- **Controllers**: REST API endpoints for user, wallet, and transaction operations
- **Services**: Business logic implementation with SAGA orchestration
- **Repositories**: Data access layer with sharding support
- **Entities**: JPA entities representing domain models
- **DTOs**: Data transfer objects for API communication
- **Security**: JWT-based authentication and authorization

### Distributed Patterns

- **SAGA Orchestrator**: Manages distributed transactions with compensation
- **Database Sharding**: Horizontal partitioning for scalability
- **Choreography vs Orchestration**: Event-driven vs centralized coordination

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.5.5
- **Language**: Java 17
- **Database**: MySQL with ShardingSphere
- **Security**: Spring Security + JWT
- **Documentation**: SpringDoc OpenAPI
- **Build Tool**: Gradle
- **ORM**: Spring Data JPA with Hibernate

## 📦 Dependencies

- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Apache ShardingSphere JDBC 5.5.2
- JWT (jjwt) 0.12.5
- SpringDoc OpenAPI 2.8.9
- MySQL Connector
- Lombok

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- MySQL 8.0+
- Gradle 8.14.3+

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Saga-Shard-Wallet
   ```

2. **Configure Database**
   - Create MySQL databases as per sharding configuration
   - Update `sharding.yml` with your database credentials

3. **Build the project**
   ```bash
   ./gradlew build
   ```

4. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

The application will start on `http://localhost:8080`

### Configuration

Update the following configuration files:

- `application.properties`: Main application configuration
- `sharding.yml`: Database sharding configuration
- JWT secret key in application properties

## 📚 API Documentation

Once the application is running, access the interactive API documentation at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## 🔐 Authentication

The system uses JWT-based authentication:

1. Register a new user via `/api/public/register`
2. Login to get JWT token via `/api/public/login`
3. Include the token in Authorization header: `Bearer <token>`

## 💳 Core Operations

### User Management
- User registration and authentication
- Profile management

### Wallet Operations
- Create wallet
- Check balance
- Wallet history

### Transactions
- Credit wallet
- Debit wallet
- Transfer between wallets
- Transaction history with SAGA status tracking

## 🔄 SAGA Implementation

The system implements the SAGA pattern for distributed transactions:

- **SagaOrchestrator**: Coordinates transaction steps
- **SagaSteps**: Individual transaction operations
- **Compensation**: Rollback logic for failed transactions
- **Status Tracking**: Monitor transaction progress

### SAGA Steps
1. **DebitSourceWalletStep**: Debit amount from source wallet
2. **CreditDestinationWalletStep**: Credit amount to destination wallet
3. **UpdateTransactionStatus**: Update transaction status
4. **Compensation Logic**: Rollback on failures

## 🗄️ Database Sharding

The project uses Apache ShardingSphere for:
- Horizontal sharding of wallet and transaction data
- Load balancing across multiple database instances
- Transparent sharding for application layer

## 🧪 Testing

Run tests using:
```bash
./gradlew test
```

## 🤝 Contributing

This is a final year project. For any questions or suggestions, please contact the project team.

## 📄 License

This project is developed as part of academic coursework.

---

**Final Year Project** - Distributed Systems & Database Sharding Implementation
