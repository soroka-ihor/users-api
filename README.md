# Users API Aggregator

This Spring Boot application aggregates user data from multiple databases and provides a single REST endpoint to retrieve the consolidated list.

## Features
- **Declarative Configuration**: Define multiple data sources and column mappings in `application.properties` or `application.yml`.
- **Dynamic Database Support**: Supports PostgreSQL, Oracle, and MySQL (drivers included).
- **Asynchronous Data Fetching**: Queries multiple databases in parallel to improve performance.
- **OpenAPI/Swagger Documentation**: Interactive API documentation at `/swagger-ui.html`.

## Requirements
- Java 17+
- Maven 3.8+
- Docker (optional, for running with Docker Compose)

## Configuration
Data sources are configured under the `app` prefix in `src/main/resources/application.yaml`.

Example:
```yaml
app:
  data-sources:
    - name: postgres-db
      strategy: postgres
      url: jdbc:postgresql://localhost:5432/users_db
      table: users
      user: testuser
      password: testpass
      mapping:
        id: user_id
        username: login
        name: first_name
        surname: last_name
```

## Running the Application
### Locally
1. Clone the repository.
2. Start the databases using Docker Compose (required for the default configuration):
   ```bash
   docker-compose up -d
   ```
3. Build the project:
   ```bash
   ./mvnw clean package
   ```
4. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
The API will be available at `http://localhost:8080/users`.
Swagger UI: `http://localhost:8080/swagger-ui.html`

### Using Docker Compose
A `docker-compose.yml` file is provided to set up a PostgreSQL and an Oracle database with pre-populated data.

1. Start the containers:
   ```bash
   docker-compose up -d
   ```
2. The databases are automatically initialized with 5 users each using scripts in `docker/init/`.
3. Run the application locally as described above, pointing to these databases.

## Database Initialization
The project includes initialization scripts for:
- **PostgreSQL**: `docker/init/postgres/init.sql` (Table `users`)
- **Oracle**: `docker/init/oracle/init.sql` (Table `user_table`)

Each script creates the necessary table and inserts 5 sample users.

## API Endpoints
- `GET /users`: Returns a list of users from all configured databases.

## Testing
Run unit and integration tests:
```bash
./mvnw test
```
The tests use an H2 in-memory database to simulate multiple data sources with different schemas.
