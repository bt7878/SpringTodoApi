# Todo API

A RESTful API for managing todo items built with Spring Boot.

## Technologies Used

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven
- Docker

## Project Description

This Todo API provides a simple and efficient way to manage todo items. It supports creating, reading, updating, and
deleting todo items through a RESTful interface.

## API Endpoints

| Method | Endpoint        | Description                    |
|--------|-----------------|--------------------------------|
| GET    | /api/todos      | Get all todo items             |
| GET    | /api/todos/{id} | Get a specific todo item by ID |
| POST   | /api/todos      | Create a new todo item         |
| PUT    | /api/todos/{id} | Update an existing todo item   |
| DELETE | /api/todos/{id} | Delete a todo item             |

### Request/Response Examples

#### Create a Todo Item

```
POST /api/todos
Content-Type: application/json

{
  "title": "Buy groceries",
  "completed": false
}
```

Response:

```json
{
  "id": 1,
  "title": "Buy groceries",
  "completed": false
}
```

## Setup and Installation

### Prerequisites

- Java 21 or higher
- Maven
- PostgreSQL (for development)
- Docker (optional, for containerization)

### Development Setup

1. Clone the repository
2. Configure your PostgreSQL database:
    - Create a database named `todoapi`
    - Create a user `dev` with password `dev` (or update the application-dev.properties file with your credentials)

3. Run the application:

```bash
./mvnw spring-boot:run
```

The application will be available at http://localhost:8080

### Running with Docker

1. Build the Docker image:

```bash
docker build -t todoapi .
```

2. Run the container:

```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://your-db-host:5432/todoapi \
  -e SPRING_DATASOURCE_USERNAME=your-username \
  -e SPRING_DATASOURCE_PASSWORD=your-password \
  todoapi
```

## Configuration

The application has different configuration profiles:

- `dev`: Used for development (default)
- `prod`: Used for production

### Development Configuration

The development profile uses a local PostgreSQL database with the following configuration:

- URL: jdbc:postgresql://localhost:5432/todoapi
- Username: dev
- Password: dev
- Hibernate DDL Auto: create (automatically creates database schema)

### Production Configuration

The production profile expects the following environment variables:

- SPRING_DATASOURCE_URL
- SPRING_DATASOURCE_USERNAME
- SPRING_DATASOURCE_PASSWORD

In production, Hibernate is set to validate the schema rather than create it.

## Testing

Run the tests using Maven:

```bash
./mvnw test
```
