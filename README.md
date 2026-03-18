# 🏥 Patient Management System — Microservices Architecture

A production-grade **Patient Management System** built on a microservices architecture using Spring Boot. The system demonstrates enterprise-level distributed system patterns: API Gateway with JWT validation, inter-service communication via gRPC, event-driven analytics with Apache Kafka, Protocol Buffers for serialization, infrastructure-as-code with AWS CDK on LocalStack, and integration testing.

## ✨ Features

- **JWT-secured API Gateway** — all traffic routes through Spring Cloud Gateway; a custom `JwtValidationGatewayFilterFactory` validates Bearer tokens before forwarding requests
- **Patient service (CRUD)** — full REST API for patient management with `UUID`-based IDs, bean validation (including custom `CreatePatientValidationGroup`), duplicate email detection, and Swagger/OpenAPI documentation
- **Auth service** — handles login (username + password) and returns a signed JWT; validates tokens via a dedicated `/validate` endpoint
- **Billing service (gRPC)** — `BillingService.CreateBillingAccount` RPC called synchronously by the patient service whenever a new patient is created; defined via Protocol Buffers
- **Kafka event streaming** — patient creation triggers a `PatientEvent` (Protobuf-serialized) published to the `patient` Kafka topic
- **Analytics service** — consumes `patient` topic events via Kafka and processes them for analytics/reporting
- **Infrastructure as Code** — AWS CDK (Java) defines the cloud infrastructure; LocalStack used for local AWS emulation during development
- **Integration tests** — dedicated `integration-tests` module tests auth and patient workflows end-to-end

## 🛠️ Tech Stack

| Component | Technology |
|-----------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 3 |
| API Gateway | Spring Cloud Gateway |
| Security | Spring Security + JWT |
| Inter-service RPC | gRPC + Protocol Buffers |
| Event Streaming | Apache Kafka |
| Serialization | Protocol Buffers (proto3) |
| Persistence | Spring Data JPA + PostgreSQL |
| API Documentation | Swagger / OpenAPI 3 (springdoc) |
| Infrastructure | AWS CDK (Java) + LocalStack |
| Build | Maven (multi-module) |
| Containerization | Docker (per-service Dockerfiles) |

## 🏗️ Architecture

```
Client
  │
  ▼
[API Gateway] ──JWT Validation──► auth-service (/login, /validate)
  │
  ├──► patient-service (CRUD)
  │         │
  │         ├──► [gRPC] ──► billing-service (CreateBillingAccount)
  │         │
  │         └──► [Kafka: patient topic] ──► analytics-service
  │
  └──► (future services)
```

### Services

| Service | Port | Responsibility |
|---------|------|---------------|
| `api-gateway` | 4004 | JWT validation, routing |
| `auth-service` | 4005 | Login, token issuance/validation |
| `patient-service` | 4000 | Patient CRUD, gRPC client, Kafka producer |
| `billing-service` | 9001 | gRPC billing account creation |
| `analytics-service` | — | Kafka consumer for patient events |

## 🚀 Setup & Installation

**Prerequisites:** Java 17+, Maven, Docker, LocalStack (for infrastructure)

```bash
# 1. Clone the repository
git clone https://github.com/moksh555/Patient-Management-System.git
cd Patient-Management-System

# 2. Start infrastructure (Kafka, PostgreSQL) using LocalStack
cd Infrastructure
./localstack-deploy.sh

# 3. Build all services
mvn clean package -DskipTests

# 4. Run each service (or use Docker Compose)
# Each service has its own Dockerfile
docker build -t patient-service ./patient-service
docker build -t auth-service ./auth-service
# ... repeat for each service
```

## 📡 API Reference

### Auth Service
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/login` | Authenticate and receive JWT |
| `GET` | `/validate` | Validate `Authorization: Bearer <token>` |

### Patient Service (via API Gateway)
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/patients` | List all patients |
| `POST` | `/patients` | Create patient (triggers gRPC + Kafka) |
| `PUT` | `/patients/{id}` | Update patient |
| `DELETE` | `/patients/{id}` | Delete patient |

See `api-requests/` for ready-to-run `.http` request files.

## 🔌 gRPC Contract

```protobuf
service BillingService {
  rpc CreateBillingAccount (BillingRequest) returns (BillingResponse);
}

message BillingRequest {
  string patientId = 1;
  string name = 2;
  string email = 3;
}
```

## 🧪 Running Integration Tests

```bash
cd integration-tests
mvn test
```

Tests cover: user login → JWT issuance → authenticated patient creation → error cases (duplicate email).
