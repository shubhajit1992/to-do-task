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
- Swagger UI is available at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- OpenAPI specification is available at [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## License
This project is for educational/demo purposes. No license is granted for production use.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## Author
[[Shubhajit Sahoo](https://github.com/shubhajit1992)]

## Running with Docker and PostgreSQL

### Prerequisites
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running
- [Docker Compose](https://docs.docker.com/compose/) (comes with Docker Desktop)

### Step-by-Step Guide

#### 1. Build the Application JAR

```
./gradlew clean build -x test
```
This creates `build/libs/todotask-0.0.1-SNAPSHOT.jar` for Docker.

#### 2. Start the Database and Application Containers

```
docker-compose up --build
```
- This will build your app image and start both the PostgreSQL database and your Spring Boot app.
- The database will be available at `localhost:5433` (host) and `db:5432` (inside Docker).
- The app will be available at `http://localhost:8080`.

#### 3. Verify Containers Are Running

```
docker-compose ps
```
You should see both `db` and `app` services as "Up".

#### 4. Access PostgreSQL Database (Optional)

**A. From your host (if you have `psql` installed):**
```
psql -h localhost -p 5433 -U postgres -d todotask
```
Password: `postgres`

**B. Or from inside the running container:**
```
docker-compose exec db psql -U postgres -d todotask
```

**C. List tables and query data:**
```
\dt
SELECT * FROM task;
```

#### 5. How Schema and Data Are Loaded
- On startup, Spring Boot will execute `src/main/resources/schema.sql` to create the `task` table, then `data.sql` to insert sample data.
- If you want to change the schema or initial data, edit these files and restart the containers.

#### 6. Test APIs in Insomnia (or Postman)
- Open Insomnia.
- Set your API base URL to: `http://localhost:8080`
- Use the appropriate endpoints (e.g., `GET /api/tasks`, `POST /api/tasks`, etc.).
- Send requests and view responses.

#### 7. Stopping and Cleaning Up
- To stop the containers:
  ```
  docker-compose down
  ```
- To remove all data and start fresh:
  ```
  docker-compose down -v
  ```

#### 8. Manually Executing SQL Queries in Docker (If Schema/Data Does Not Load Automatically)
If your tables or data do not appear after starting the containers, you can manually execute your SQL scripts inside the running PostgreSQL container:

**A. Copy SQL files into the database container:**
```sh
docker cp src/main/resources/schema.sql $(docker-compose ps -q db):/tmp/schema.sql
docker cp src/main/resources/data.sql $(docker-compose ps -q db):/tmp/data.sql
```

**B. Connect to the database container:**
```sh
docker-compose exec db psql -U postgres -d todotask
```

**C. Run the SQL scripts inside the psql prompt:**
```sql
\i /tmp/schema.sql
\i /tmp/data.sql
```

**D. Verify table and data:**
```sql
\dt
SELECT * FROM task;
```

If you prefer, you can also copy-paste the contents of your SQL files directly into the `psql` prompt.

---

**Troubleshooting:**
- If tables/data are missing, ensure `schema.sql` and `data.sql` are correct and in `src/main/resources`.
- If you change the schema/data, use `docker-compose down -v` to reset the database volume.
- For logs:
  - App: `docker-compose logs app`
  - DB: `docker-compose logs db`

---

> **Note:**
> 
> When you run `docker-compose down -v`, **all database data is erased** because the Docker volume is deleted. This is standard Docker behavior. To preserve your data, use only `docker-compose down` (without `-v`).
>
> - Use `docker-compose down` to stop and remove containers but keep your database data.
> - Use `docker-compose down -v` only if you want to reset the database and lose all data.
>
> For production or persistent development data, always rely on the Docker volume and regular database backups, not on SQL files.

## Security: Managing Secrets and Passwords

**Never hardcode secrets or passwords in your Dockerfile or docker-compose.yml.**

### Recommended Approach: Use a `.env` File

1. **Create a `.env` file in your project root:**
   ```env
   POSTGRES_PASSWORD=your_strong_password
   SPRING_DATASOURCE_PASSWORD=your_strong_password
   POSTGRES_USER=postgres
   POSTGRES_DB=todotask
   SPRING_DATASOURCE_USERNAME=postgres
   SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/todotask
   ```
   - Do **not** commit this file to version control. Add `.env` to your `.gitignore`.

2. **Reference environment variables in `docker-compose.yml`:**
   ```yaml
   environment:
     POSTGRES_DB: ${POSTGRES_DB}
     POSTGRES_USER: ${POSTGRES_USER}
     POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
     # ...
     SPRING_DATASOURCE_URL: ${SPRING_DATASOURCE_URL}
     SPRING_DATASOURCE_USERNAME: ${SPRING_DATASOURCE_USERNAME}
     SPRING_DATASOURCE_PASSWORD: ${SPRING_DATASOURCE_PASSWORD}
   ```

3. **Reference environment variables in `application.yml`:**
   ```yaml
   spring:
     datasource:
       url: ${SPRING_DATASOURCE_URL}
       username: ${SPRING_DATASOURCE_USERNAME}
       password: ${SPRING_DATASOURCE_PASSWORD}
   ```

4. **Add `.env` to `.gitignore`:**
   ```
   .env
   ```

### Why?
- Keeps secrets out of source code and version control.
- Makes it easy to change passwords and other secrets without editing code or config files.
- Follows industry best practices for local and team development.

For production, consider using Docker secrets, Kubernetes secrets, or a dedicated secrets manager.

---
This project is for educational/demo purposes.