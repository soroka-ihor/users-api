# Users API Aggregator

Spring Boot service that aggregates user data from multiple databases (PostgreSQL, Oracle, MySQL) in parallel and exposes a single REST endpoint returning the consolidated user list.

## Features

- **Declarative Configuration**: Define multiple data sources and column mappings in `application.yaml`.
- **Dynamic Database Support**: Supports PostgreSQL, Oracle, and MySQL (drivers included).
- **Asynchronous Data Fetching**: Queries multiple databases in parallel to improve performance.
- **OpenAPI/Swagger Documentation**: Interactive API documentation available at `/swagger-ui.html`.

## Requirements

- Java 17+
- Maven 3.8+
- Docker & Docker Compose

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

### Full Docker Setup (recommended)

1. Clone the repository:
   ```bash
   git clone https://github.com/soroka-ihor/users-api
   cd users-api
   ```
2. Start all services (databases + application):
   ```bash
   docker-compose up -d
   ```
3. Wait for the containers to initialize, then access:
   - **API**: `http://localhost:8887/users`
   - **Swagger UI**: `http://localhost:8887/swagger-ui.html`

### Locally (databases via Docker)

1. Clone the repository:
   ```bash
   git clone https://github.com/soroka-ihor/users-api
   cd users-api
   ```
2. Start the databases:
   ```bash
   docker-compose up -d postgres oracle mysql
   ```
3. Build the project:
   ```bash
   ./mvnw clean package -DskipTests
   ```
4. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
5. Access the application:
   - **API**: `http://localhost:8887/users`
   - **Swagger UI**: `http://localhost:8887/swagger-ui.html`
   
## Database Initialization

Initialization scripts are applied automatically on first container start:

- **PostgreSQL**: `docker/init/postgres/init.sql` (table `users`)
- **Oracle**: `docker/init/oracle/init.sql` (table `user_table`)
- **MySQL**: `docker/init/mysql/init.sql` (table `users`)

Each script creates the required table and inserts 5 sample users.

## API Endpoints

- `GET /users` — Returns a list of users aggregated from all configured databases.

## Testing

```bash
./mvnw test
```

Tests use test containers to simulate multiple data sources with different schemas.
