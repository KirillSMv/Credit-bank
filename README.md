# Credit-bank

The **Credit Application** is a microservices-based platform designed to facilitate credit application 
processing, scoring, offer generation, and loan issuance. The system ensures an easy experience for users 
applying for loans. 

Loan statement request passes through the process of pre-scoring, scoring using custom FICO system, calculation of 
loan parameters and finalization stage. At each critical step such as statement acceptance, loan approval/rejection, 
initiating documents signing process and loan issuance client is receiving email messages with necessary information and proper instructions.

---
## Features

- **Microservices architecture** for modular and scalable processing
- **Request Routing**: Gateway microservice routes incoming client requests to the appropriate backend service
- **Error Handling**: Ensured robust error reporting and fallback mechanisms for downstream service issues
- **Accuracy**: Requests are validated  before forwarding them to backend services
- **Automatization**: Automated pre-scoring and full credit assessment
- **Email notifications**: at key stages of the loan statement processing

---
## Technologies Used

- **Java 21**: Programming language.
- **Spring Boot**: Framework for building microservices.
- **WebClient**: Client to perform HTTP requests to microservices.
- **Maven**: Dependency management and build tool.
- **OpenAPI (Swagger)**: API documentation and testing.
- **PostgreSQL**: Database.
- **Liquibase**: Library for tracking, managing and applying database schema changes.
- **Kafka**: Messages broker.
- **Docker**: Platform for containerisation.
- **Spring Data JPA**: ORM technology
- **Swagger**: Tool for documenting APIs using the OpenAPI Specification.

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
