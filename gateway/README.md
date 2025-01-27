# Gateway Microservice

The **Gateway** microservice is a core component of the bank loan processing application. It implements the **Gateway Pattern** to direct user requests to the appropriate backend services: **Deal** and **Statement**.

---

## Features
- **Request Routing**: Routes incoming client requests to the appropriate backend service (Deal or Statement) based on the endpoint.
- **Error Handling**: Ensures robust error reporting and fallback mechanisms for downstream service issues.
- **Security**: Validates requests before forwarding them to backend services.

---

## Technologies Used

- **Java 17**: Programming language.
- **Spring Boot**: Framework for building microservices.
- **WebClient**: Client to perform HTTP requests to microservices.
- **Maven**: Dependency management and build tool.
- **OpenAPI (Swagger)**: API documentation and testing.

---

## API Endpoints

### Gateway Endpoints

| Method | Endpoint                                | Description                                |
|--------|-----------------------------------------|--------------------------------------------|
| POST   | `/statement/registration/{statementId}` | Calculate and save loan parameters         |
| POST   | `/document/{statementId}`               | Process send documents request             |
| POST   | `/document/{statementId}/sign`          | Process signing documents request          |
| POST   | `/document/{statementId}/sign/code`     | Process security code received from client |
| POST   | `/admin/statement/{statementId}`        | Request statement by id                    |
| POST   | `/admin/statement`                      | Request all statements                     |
| POST   | `/statement`                            | Process loan request statement             |
| POST   | `/statement/offer`                      | Process loan offer acceptance              |

---

## Contact

For questions or feedback, please contact [kirill551@yandex.ru].
