# API Quick Start Guide

Quick reference for testing the Task Management System API.

## 🚀 Getting Started

### 1. Start the Application

```bash
# Start PostgreSQL
docker-compose up -d

# Run the application
mvn spring-boot:run
```

Base URL: `http://localhost:8081/api/v1`

## 🔐 Step 1: Login

```bash
curl -X POST http://localhost:8081/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "superadmin@gmail.com",
    "password": "password"
  }'
```

**Response:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 86400000
}
```

**Save the token** - you'll need it for all other requests!

## 📋 Step 2: Get Current User

```bash
curl -X GET http://localhost:8081/api/v1/users/me \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## 👥 Step 3: Create a User

### As ADMIN - Create a Manager:

```bash
curl -X POST http://localhost:8081/api/v1/users/register/manager \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johnmanager",
    "email": "john@manager.com",
    "password": "password123",
    "role": "MANAGER"
  }'
```

### As MANAGER - Create a User:

```bash
curl -X POST http://localhost:8081/api/v1/users/register/user \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "janeuser",
    "email": "jane@user.com",
    "password": "password123",
    "role": "USER"
  }'
```

## 📝 Step 4: Create a Task

```bash
curl -X POST http://localhost:8081/api/v1/tasks/create \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Implement new feature",
    "description": "Add user profile management feature",
    "priority": "HIGH",
    "deadline": "2025-02-15",
    "assignedToId": 2
  }'
```

**Response:**

```json
{
  "id": 1,
  "title": "Implement new feature",
  "description": "Add user profile management feature",
  "status": "PENDING",
  "priority": "HIGH",
  "deadline": "2025-02-15",
  "createdAt": "2025-01-28",
  "createdBy": {
    "id": 1,
    "username": "superadmin",
    "email": "superadmin@gmail.com",
    "role": "ADMIN"
  },
  "assignedTo": {
    "id": 2,
    "username": "user",
    "email": "user@example.com",
    "role": "USER"
  }
}
```

## 📊 Step 5: Get Tasks (Paginated)

```bash
# Get first page (10 items)
curl -X GET "http://localhost:8081/api/v1/tasks?page=0&size=10&sortBy=createdAt&sortDirection=DESC" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

**Response:**

```json
{
  "content": [
    {
      "id": 1,
      "title": "Implement new feature",
      "status": "PENDING",
      ...
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 1,
  "totalPages": 1,
  "last": true,
  "first": true
}
```

## 🔄 Step 6: Update Task Status

```bash
curl -X PATCH http://localhost:8081/api/v1/tasks/1/status \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "status": "IN_PROGRESS"
  }'
```

## ✏️ Step 7: Update Task

```bash
curl -X PUT http://localhost:8081/api/v1/tasks/1/update \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Updated title",
    "description": "Updated description",
    "priority": "MEDIUM"
  }'
```

## 🗑️ Step 8: Delete Task

```bash
curl -X DELETE http://localhost:8081/api/v1/tasks/1 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## 🔍 Additional Queries

### Get Users by Role (Paginated)

```bash
curl -X GET "http://localhost:8081/api/v1/users/by-role?role=USER&page=0&size=10" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### Get All Managers (ADMIN only)

```bash
curl -X GET http://localhost:8081/api/v1/users/managers \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### Get User by ID

```bash
curl -X GET http://localhost:8081/api/v1/users/2 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### Get Task by ID

```bash
curl -X GET http://localhost:8081/api/v1/tasks/1 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## 🎯 Testing Different Roles

### Test as ADMIN:

```bash
# Login as admin
curl -X POST http://localhost:8081/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "superadmin@gmail.com", "password": "password"}'
```

### Test as MANAGER:

```bash
# Login as manager
curl -X POST http://localhost:8081/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "manager@example.com", "password": "password"}'
```

### Test as USER:

```bash
# Login as user
curl -X POST http://localhost:8081/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "user@example.com", "password": "password"}'
```

## 📝 Task Status Transitions

Valid transitions:

- `PENDING` → `IN_PROGRESS` ✅
- `PENDING` → `COMPLETED` ✅
- `PENDING` → `CANCELED` ✅
- `IN_PROGRESS` → `COMPLETED` ✅
- `IN_PROGRESS` → `CANCELED` ✅
- `COMPLETED` → (none) ❌
- `CANCELED` → (none) ❌

## 🎨 Priority Levels

- `LOW`
- `MEDIUM`
- `HIGH`

## ⚠️ Common Error Responses

### 401 Unauthorized

```json
{
  "message": "Invalid email or password",
  "status": 401,
  "errors": null
}
```

### 403 Forbidden

```json
{
  "message": "You are not allowed to delete this task",
  "status": 403,
  "errors": null
}
```

### 404 Not Found

```json
{
  "message": "Task not found with id: 999",
  "status": 404,
  "errors": null
}
```

### 400 Validation Error

```json
{
  "message": "Validation failed",
  "status": 400,
  "errors": {
    "email": "must be a well-formed email address",
    "password": "Password must be at least 8 characters long"
  }
}
```

## 🛠️ Postman Collection

Import these into Postman for easier testing:

1. Create new collection: "Task Management API"
2. Add environment variables:
   - `baseUrl`: `http://localhost:8081/api/v1`
   - `token`: (will be set after login)
3. Add login request and save token to environment
4. Use `{{baseUrl}}` and `{{token}}` in subsequent requests

---

**Happy Testing!** 🚀
