# to-do-task

A simple Spring Boot RESTful API for managing to-do tasks.

## Table of Contents
- [Features](#features)
- [Requirements](#requirements)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Database](#database)
- [Exception Handling & Validation](#exception-handling--validation)
- [Testing & Coverage](#testing--coverage)
- [Project Structure](#project-structure)
- [API Documentation](#api-documentation)
- [License](#license)
- [Contributing](#contributing)
- [Author](#author)

## Features
- CRUD operations for tasks (Create, Read, Update, Delete)
- Each task has a title, description, and completion status
- RESTful endpoints for integration with frontends or other services
- Entity/DTO mapping handled by a dedicated `TaskMapper` class in the `mapper` package
- Builder pattern used for object creation in both `Task` and `TaskDTO`
- Unit and integration tests with 100% code coverage (JaCoCo enforced)

## Requirements
- Java 21
- Gradle (wrapper included)

## Getting Started

### Build and Run
```sh
./gradlew bootRun
```
The application will start at [http://localhost:8080](http://localhost:8080).

## API Endpoints
- `GET /api/tasks` — List all tasks
- `GET /api/tasks/{id}` — Get a task by ID
- `POST /api/tasks` — Create a new task
- `PUT /api/tasks/{id}` — Update an existing task
- `DELETE /api/tasks/{id}` — Delete a task

All endpoints accept/return JSON. Example request body for create/update:
```json
{
  "title": "Buy groceries",
  "description": "Milk, Bread, Eggs, and Fruits",
  "completed": false
}
```

## Database
- Uses H2 in-memory database for development and testing
- Sample data loaded from `src/main/resources/data.sql`

## Exception Handling & Validation
- All controller and service exceptions are handled using custom exceptions (`TaskNotFoundException`, `TaskAlreadyExistsException`, etc.) and a global exception handler (`GlobalExceptionHandler`).
- Validation is enforced on request bodies and path variables using Jakarta Bean Validation annotations (e.g., `@Valid`, `@Min(1)`).
- The controller is annotated with `@Validated` to enable validation of path variables, ensuring real `ConstraintViolationException` handling.
- All exception handlers are covered by real use cases and tested with unit/integration tests.

## Testing & Coverage
- Run all tests:
  ```sh
  ./gradlew test
  ```
- Generate coverage report:
  ```sh
  ./gradlew jacocoTestReport
  # Open build/jacocoHtml/index.html in your browser
  ```
- 100% code coverage enforced (including all exception handlers and validation branches)
- All exception scenarios (including `ConstraintViolationException` and `TaskAlreadyExistsException`) are covered by real code paths and tests.

## Project Structure
- `src/main/java` — Application source code
  - `entity/` — JPA entities
  - `model/` — DTOs
  - `mapper/` — Entity/DTO mappers
  - `service/` — Business logic
  - `controller/` — REST controllers
  - `repository/` — Spring Data JPA repositories
- `src/test/java` — Unit and integration tests
- `src/main/resources` — Configuration and data files

## API Documentation
- If you use Swagger/OpenAPI, documentation will be available at `/swagger-ui.html` (add dependency if needed).

## License
This project is for educational/demo purposes. No license is granted for production use.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## Author
[[Shubhajit Sahoo](https://github.com/shubhajit1992)]

---
This project is for educational/demo purposes.