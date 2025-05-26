# to-do-task

A simple Spring Boot RESTful API for managing to-do tasks.

## Features
- CRUD operations for tasks (Create, Read, Update, Delete)
- Each task has a title, description, and completion status
- In-memory H2 database for easy development and testing
- RESTful endpoints for integration with frontends or other services
- Unit and integration tests
- Test coverage reporting with JaCoCo (minimum 80% enforced)

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

### Get all tasks
```
GET /api/tasks
```
Response: 200 OK

### Get a task by ID
```
GET /api/tasks/{id}
```
Response: 200 OK (if found), 404 Not Found (if not found)

### Create a new task
```
POST /api/tasks
Content-Type: application/json

{
  "title": "Buy groceries",
  "description": "Milk, Bread, Eggs, and Fruits",
  "completed": false
}
```
Response: 201 Created, Location header: /api/tasks/{id}

### Update an existing task
```
PUT /api/tasks/{id}
Content-Type: application/json

{
  "title": "Buy groceries (updated)",
  "description": "Milk, Bread, Eggs, Fruits, and Cheese",
  "completed": true
}
```
Response: 200 OK (if found), 404 Not Found (if not found)

### Delete a task
```
DELETE /api/tasks/{id}
```
Response: 204 No Content (if found), 404 Not Found (if not found)

## Example Task JSON
```json
{
  "title": "Buy groceries",
  "description": "Milk, Bread, Eggs, and Fruits",
  "completed": false
}
```

## Database
- Uses H2 in-memory database (see `src/main/resources/application.yml`)
- Sample data loaded from `src/main/resources/data.sql`
- H2 Console available at `/h2-console` (if enabled)

## Testing

### Run Tests
```sh
./gradlew test
```

### Test Coverage
After running tests, open the coverage report:
```
build/jacocoHtml/index.html
```
- Build will fail if code coverage is below 80% (see `build.gradle.kts`)

## Project Structure
- `src/main/java`         : Application source code
- `src/test/java`         : Unit and integration tests
- `src/main/resources`    : Configuration and data files

## License
This project is for educational/demo purposes.