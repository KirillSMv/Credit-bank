# Credit-bank

The **Credit Application** System is a microservices-based platform designed to facilitate credit application 
processing, scoring, offer generation, and loan issuance. The system ensures an easy experience for users 
applying for loans.

---
## Features

- **Microservices architecture** for modular and scalable processing
- **Request Routing**: Routes incoming client requests to the appropriate backend service (Deal or Statement) based on
  the endpoint.
- **Error Handling**: Ensures robust error reporting and fallback mechanisms for downstream service issues.
- **Security**: Validates requests before forwarding them to backend services.
- **Automatization**: automated pre-scoring and full credit assessment
- **Email notifications**: at key stages of the loan process

---
## Technologies Used

- **Java 21**: Programming language.
- **Spring Boot**: Framework for building microservices.
- **WebClient**: Client to perform HTTP requests to microservices.
- **Maven**: Dependency management and build tool.
- **OpenAPI (Swagger)**: API documentation and testing.
- **PostgreSQL**: database
- **Kafka**: message broker
- **Docker**: platform for containerisation

---

## API Endpoints

| Method | Endpoint                                | Description                                |
|--------|-----------------------------------------|--------------------------------------------|
| POST   | `/statement/registration/{statementId}` | Calculate and save loan parameters         |
| POST   | `/document/{statementId}`               | Process send documents request             |
| POST   | `/document/{statementId}/sign`          | Process signing documents request          |
| POST   | `/document/{statementId}/sign/code`     | Process security code received from client |
| POST   | `/admin/statement/{statementId}`        | Request statement by id                    |
| POST   | `/admin/statement`                      | Request all statements by admin            |
| POST   | `/statement`                            | Process loan request statement             |
| POST   | `/statement/offer`                      | Process loan offer acceptance              |

---

## Contact

For questions or feedback, please contact [kirill551@yandex.ru].
