# Task Management System - Spring Boot

A comprehensive REST API for managing tasks with role-based access control (RBAC). Built with Spring Boot 3, PostgreSQL, JWT authentication, and modern security practices.

## 🚀 Features

- **Authentication & Authorization**: JWT-based authentication with role-based access control
- **User Management**: Support for three user roles (ADMIN, MANAGER, USER)
- **Task Management**: Full CRUD operations with status tracking
- **Pagination**: Efficient data retrieval with pagination support
- **Security**: Environment-based configuration, password encryption, and secure endpoints
- **Database Migrations**: Flyway for version-controlled database schema
- **Logging**: SLF4J logging for monitoring and debugging

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 13+
- Docker (optional, for PostgreSQL)

## 🛠️ Technology Stack

- **Backend Framework**: Spring Boot 3.4.4
- **Database**: PostgreSQL
- **Security**: Spring Security + JWT
- **ORM**: JPA/Hibernate
- **Migration**: Flyway
- **Mapping**: MapStruct
- **Build Tool**: Maven
- **Logging**: SLF4J + Logback

## 🔧 Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd task-menagment-system-springboot1
```

### 2. Configure Environment Variables

Create a `.env` file or set environment variables:

```bash
# Server Configuration
SERVER_PORT=8081

# Database Configuration
DATABASE_URL=jdbc:postgresql://127.0.0.1:5432/task-db
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=secret

# JWT Configuration (CHANGE THIS IN PRODUCTION!)
JWT_SECRET=your-secret-key-here-change-in-production
JWT_EXPIRATION=86400000

# CORS Configuration
CORS_ALLOWED_ORIGINS=http://localhost:5173,http://localhost:3000

# JPA Configuration
JPA_SHOW_SQL=true
```

### 3. Start PostgreSQL Database

**Using Docker Compose:**

```bash
docker-compose up -d
```

**Or manually:**

```bash
createdb task-db
```

### 4. Build and Run

```bash
mvn clean install
mvn spring-boot:run
```

The application will start at `http://localhost:8081`

## 👥 User Roles

The system supports three user roles with different permissions:

| Role        | Permissions                                    |
| ----------- | ---------------------------------------------- |
| **ADMIN**   | Full system access, manage all users and tasks |
| **MANAGER** | Create users, create and manage own tasks      |
| **USER**    | View assigned tasks, update task status        |

## 🔐 Default Users

The system seeds the following test users on startup:

| Username   | Email                | Password | Role    |
| ---------- | -------------------- | -------- | ------- |
| superadmin | superadmin@gmail.com | password | ADMIN   |
| manager    | manager@example.com  | password | MANAGER |
| user       | user@example.com     | password | USER    |

## 📚 API Documentation

### Base URL

```
http://localhost:8081/api/v1
```

### Authentication Endpoints

#### Login

```http
POST /auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 86400000
}
```

### User Endpoints

#### Get Current User Profile

```http
GET /users/me
Authorization: Bearer {token}
```

#### Get All Users (Paginated)

```http
GET /users?page=0&size=10
Authorization: Bearer {token}
```

#### Get User by ID

```http
GET /users/{id}
Authorization: Bearer {token}
```

#### Register Manager (ADMIN only)

```http
POST /users/register/manager
Authorization: Bearer {token}
Content-Type: application/json

{
  "username": "newmanager",
  "email": "manager@example.com",
  "password": "password123",
  "role": "MANAGER"
}
```

#### Register User (ADMIN/MANAGER)

```http
POST /users/register/user
Authorization: Bearer {token}
Content-Type: application/json

{
  "username": "newuser",
  "email": "user@example.com",
  "password": "password123",
  "role": "USER"
}
```

#### Update User

```http
PUT /users/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "username": "updateduser",
  "email": "updated@example.com",
  "password": "newpassword123"
}
```

#### Delete User

```http
DELETE /users/{id}
Authorization: Bearer {token}
```

#### Get Users by Role

```http
GET /users/by-role?role=USER&page=0&size=10
Authorization: Bearer {token}
```

### Task Endpoints

#### Get All Tasks (Paginated & Filtered by Role)

```http
GET /tasks?page=0&size=10&sortBy=createdAt&sortDirection=DESC
Authorization: Bearer {token}
```

Response includes:

- **ADMIN**: All tasks
- **MANAGER**: Tasks created by the manager
- **USER**: Tasks assigned to the user

#### Get Task by ID

```http
GET /tasks/{id}
Authorization: Bearer {token}
```

#### Create Task

```http
POST /tasks/create
Authorization: Bearer {token}
Content-Type: application/json

{
  "title": "Implement login feature",
  "description": "Create JWT-based authentication",
  "priority": "HIGH",
  "deadline": "2025-02-01",
  "assignedToId": 2
}
```

#### Update Task

```http
PUT /tasks/{id}/update
Authorization: Bearer {token}
Content-Type: application/json

{
  "title": "Updated title",
  "description": "Updated description",
  "priority": "MEDIUM",
  "deadline": "2025-02-15"
}
```

#### Update Task Status

```http
PATCH /tasks/{id}/status
Authorization: Bearer {token}
Content-Type: application/json

{
  "status": "IN_PROGRESS"
}
```

**Valid Status Transitions:**

- `PENDING` → `IN_PROGRESS`, `COMPLETED`, `CANCELED`
- `IN_PROGRESS` → `COMPLETED`, `CANCELED`
- `COMPLETED` → (No transitions allowed)
- `CANCELED` → (No transitions allowed)

#### Delete Task

```http
DELETE /tasks/{id}
Authorization: Bearer {token}
```

## 🎯 Task Priorities

- `LOW`
- `MEDIUM`
- `HIGH`

## 📊 Task Status Flow

```
PENDING
  ├─→ IN_PROGRESS
  ├─→ COMPLETED
  └─→ CANCELED

IN_PROGRESS
  ├─→ COMPLETED
  └─→ CANCELED
```

## 🔒 Security Features

- JWT token-based authentication
- Password encryption using BCrypt
- Role-based access control (RBAC)
- Environment-based secret management
- CORS configuration
- Stateless session management

## 🗄️ Database Schema

### Users Table

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(50) NOT NULL,
    created_by_manager_id BIGINT REFERENCES users(id) ON DELETE SET NULL
);
```

### Tasks Table

```sql
CREATE TABLE tasks (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    status VARCHAR(50) NOT NULL,
    priority VARCHAR(50) NOT NULL,
    deadline DATE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    assigned_to_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_by_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);
```

## 🧪 Testing

Run tests with:

```bash
mvn test
```

## 📝 Error Handling

The API returns consistent error responses:

```json
{
  "message": "Error description",
  "status": 404,
  "errors": {
    "field": "error message"
  }
}
```

### Common HTTP Status Codes

- `200 OK` - Successful GET/PUT/PATCH
- `201 Created` - Successful POST
- `204 No Content` - Successful DELETE
- `400 Bad Request` - Validation errors
- `401 Unauthorized` - Missing/invalid token
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `409 Conflict` - Business logic error
- `500 Internal Server Error` - Server error

## 🚀 Deployment

### Docker Deployment

1. Build the application:

```bash
mvn clean package -DskipTests
```

2. Build Docker image:

```bash
docker build -t task-management-api .
```

3. Run with Docker Compose:

```bash
docker-compose up
```

### Environment Variables for Production

Make sure to set these in production:

```bash
JWT_SECRET=<strong-random-secret-512-bits>
DATABASE_PASSWORD=<secure-database-password>
CORS_ALLOWED_ORIGINS=https://your-frontend-domain.com
JPA_SHOW_SQL=false
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 👨‍💻 Author

Arvanit Telaku

## 🙏 Acknowledgments

- Spring Boot Team
- PostgreSQL Community
- MapStruct Project
- JWT.io

---

**Note**: This is a portfolio project demonstrating Spring Boot best practices, REST API design, and secure authentication implementation.
