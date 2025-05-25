# to-do-task

A simple Spring Boot RESTful API for managing to-do tasks.

## Features
- Create, read, update, and delete (CRUD) tasks
- Store tasks with title, description, and completion status
- In-memory H2 database for development and testing
- RESTful endpoints for integration with frontends or other services
- Unit and integration tests
- Test coverage reporting with JaCoCo

## Requirements
- Java 21
- Gradle (wrapper included)

## Getting Started

### Build and Run
```sh
./gradlew bootRun
```
The application will start on [http://localhost:8080](http://localhost:8080).

### API Endpoints
- `GET    /api/tasks`           : List all tasks
- `GET    /api/tasks/{id}`      : Get a task by ID
- `POST   /api/tasks`           : Create a new task
- `PUT    /api/tasks/{id}`      : Update an existing task
- `DELETE /api/tasks/{id}`      : Delete a task

### Example Task JSON
```json
{
  "title": "Buy groceries",
  "description": "Milk, Bread, Eggs, and Fruits",
  "completed": false
}
```

### Database
- Uses H2 in-memory database (see `src/main/resources/application.yml`)
- Sample data is loaded from `src/main/resources/data.sql`
- H2 Console available at `/h2-console` (if enabled)

### Running Tests
```sh
./gradlew test
```

### Test Coverage
After running tests, open the coverage report:
```
build/jacocoHtml/index.html
```

## Project Structure
- `src/main/java`    : Application source code
- `src/test/java`    : Unit and integration tests
- `src/main/resources` : Configuration and data files

## License
This project is for educational/demo purposes.