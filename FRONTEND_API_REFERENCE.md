# Task Management System - API Reference for Frontend

**Backend Base URL:** `http://localhost:8081/api/v1`

---

## 🔐 Authentication Endpoints

### 1. Register New User
```
POST /auth/register
Content-Type: application/json

Body:
{
  "username": "string",
  "email": "string",
  "password": "string"
}

Response 201:
{
  "message": "Registration successful! You can now login.",
  "email": "string"
}
```

### 2. Login
```
POST /auth/login
Content-Type: application/json

Body:
{
  "email": "string",
  "password": "string"
}

Response 200:
{
  "token": "string (JWT)",
  "expiresIn": 86400000
}
```

---

## 👤 User/Profile Endpoints

**Note:** All endpoints below require `Authorization: Bearer {token}` header

### 3. Get Current User Profile
```
GET /users/profile
Authorization: Bearer {token}

Response 200:
{
  "id": number,
  "username": "string",
  "email": "string",
  "role": "USER" | "MANAGER" | "ADMIN"
}
```

### 4. Update Profile
```
PUT /users/profile
Authorization: Bearer {token}
Content-Type: application/json

Body:
{
  "username": "string",
  "email": "string"
}

Response 200: "Profile updated successfully"
```

### 5. Change Password
```
PUT /users/profile/password
Authorization: Bearer {token}
Content-Type: application/json

Body:
{
  "currentPassword": "string",
  "newPassword": "string",
  "confirmPassword": "string"
}

Response 200: "Password changed successfully"
```

### 6. Get All Users (Paginated)
```
GET /users?page=0&size=10
Authorization: Bearer {token}

Response 200:
{
  "content": [UserViewDto],
  "totalElements": number,
  "totalPages": number,
  "size": number,
  "number": number
}
```

---

## 📋 Task Endpoints

### 7. Get All Tasks (Paginated)
```
GET /tasks?page=0&size=10&sortBy=createdAt&sortDirection=DESC
Authorization: Bearer {token}

Response 200:
{
  "content": [
    {
      "id": number,
      "title": "string",
      "description": "string",
      "status": "PENDING" | "IN_PROGRESS" | "COMPLETED" | "CANCELED",
      "priority": "LOW" | "MEDIUM" | "HIGH",
      "deadline": "2025-12-31",
      "createdAt": "2025-01-15",
      "createdBy": {
        "id": number,
        "username": "string",
        "email": "string",
        "role": "string"
      },
      "assignedTo": {
        "id": number,
        "username": "string",
        "email": "string",
        "role": "string"
      }
    }
  ],
  "totalElements": number,
  "totalPages": number,
  "size": number,
  "number": number
}
```

### 8. Get Task by ID
```
GET /tasks/{id}
Authorization: Bearer {token}

Response 200: ViewTaskDto (same as above)
```

### 9. Create Task
```
POST /tasks/create
Authorization: Bearer {token}
Content-Type: application/json

Body:
{
  "title": "string" (required, min 5 chars),
  "description": "string" (required, min 10 chars),
  "priority": "LOW" | "MEDIUM" | "HIGH",
  "deadline": "2025-12-31",
  "assignedToId": number (optional - if not provided, assigned to creator)
}

Response 201: ViewTaskDto
```

### 10. Update Task
```
PUT /tasks/{id}/update
Authorization: Bearer {token}
Content-Type: application/json

Body:
{
  "title": "string",
  "description": "string",
  "status": "PENDING" | "IN_PROGRESS" | "COMPLETED" | "CANCELED",
  "priority": "LOW" | "MEDIUM" | "HIGH",
  "deadline": "2025-12-31"
}

Response 200: ViewTaskDto
```

### 11. Delete Task
```
DELETE /tasks/{id}
Authorization: Bearer {token}

Response 200: "Task deleted successfully"
```

### 12. Update Task Status
```
PATCH /tasks/{id}/status
Authorization: Bearer {token}
Content-Type: application/json

Body:
{
  "status": "PENDING" | "IN_PROGRESS" | "COMPLETED" | "CANCELED"
}

Response 200:
{
  "message": "Task status updated successfully"
}
```

---

## 📊 Dashboard & Statistics

### 13. Get Dashboard Statistics
```
GET /tasks/dashboard
Authorization: Bearer {token}

Response 200:
{
  "totalTasks": number,
  "tasksCreated": number,
  "tasksAssigned": number,
  "pendingTasks": number,
  "inProgressTasks": number,
  "completedTasks": number,
  "canceledTasks": number,
  "highPriorityTasks": number,
  "mediumPriorityTasks": number,
  "lowPriorityTasks": number,
  "completionRate": number (percentage),
  "overdueTasks": number,
  "todayTasks": number
}
```

---

## 🔍 Advanced Features

### 14. Filter Tasks
```
GET /tasks/filter?status=PENDING&priority=HIGH&fromDate=2025-01-01&toDate=2025-12-31&page=0&size=10&sortBy=deadline&sortDirection=ASC
Authorization: Bearer {token}

Query Parameters (all optional):
- status: PENDING | IN_PROGRESS | COMPLETED | CANCELED
- priority: LOW | MEDIUM | HIGH
- fromDate: yyyy-MM-dd
- toDate: yyyy-MM-dd
- page: number (default: 0)
- size: number (default: 10)
- sortBy: string (default: createdAt)
- sortDirection: ASC | DESC (default: DESC)

Response 200: Paginated ViewTaskDto
```

### 15. Search Tasks
```
GET /tasks/search?query=urgent&page=0&size=10
Authorization: Bearer {token}

Query Parameters:
- query: string (required - searches in title and description)
- page: number (default: 0)
- size: number (default: 10)
- sortBy: string (default: createdAt)
- sortDirection: ASC | DESC (default: DESC)

Response 200: Paginated ViewTaskDto
```

---

## 🔑 Default Users for Testing

| Email                 | Password | Role    |
|-----------------------|----------|---------|
| superadmin@gmail.com  | password | ADMIN   |
| manager@example.com   | password | MANAGER |
| user@example.com      | password | USER    |

---

## 🎨 Task Status Flow

```
PENDING → IN_PROGRESS → COMPLETED
       ↘              ↗
         CANCELED ←--
```

**Valid Transitions:**
- PENDING → IN_PROGRESS, COMPLETED, CANCELED
- IN_PROGRESS → COMPLETED, CANCELED
- COMPLETED → (no transitions allowed)
- CANCELED → (no transitions allowed)

---

## 🎯 Task Priorities

- **LOW** - Low priority
- **MEDIUM** - Medium priority
- **HIGH** - High priority

---

## 🔒 Role Permissions

### USER
- ✅ Create personal tasks
- ✅ View own tasks (created or assigned)
- ✅ Update own tasks
- ✅ Delete own tasks
- ✅ Update status of assigned tasks
- ✅ View own dashboard
- ✅ Search & filter own tasks
- ✅ Manage own profile

### MANAGER (inherits USER permissions)
- ✅ Create tasks for team members
- ✅ View tasks created by them
- ✅ Assign tasks to users
- ✅ Create new users

### ADMIN (full access)
- ✅ All MANAGER permissions
- ✅ View all tasks
- ✅ Manage all users
- ✅ Create managers
- ✅ Delete any task

---

## 🚀 Quick Start for Vue Frontend

### 1. Install Axios
```bash
npm install axios
```

### 2. Create API Service (`src/services/api.js`)
```javascript
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8081/api/v1';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Add token to requests
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
```

### 3. Example API Calls
```javascript
// Login
const login = async (email, password) => {
  const response = await api.post('/auth/login', { email, password });
  localStorage.setItem('token', response.data.token);
  return response.data;
};

// Get Tasks
const getTasks = async (page = 0, size = 10) => {
  const response = await api.get(`/tasks?page=${page}&size=${size}`);
  return response.data;
};

// Create Task
const createTask = async (taskData) => {
  const response = await api.post('/tasks/create', taskData);
  return response.data;
};

// Get Dashboard
const getDashboard = async () => {
  const response = await api.get('/tasks/dashboard');
  return response.data;
};
```

---

## 📝 Notes for Frontend Development

1. **CORS is configured** - Backend accepts requests from `http://localhost:5173` (Vite default)
2. **JWT tokens expire** in 24 hours (86400000ms)
3. **Pagination** - All list endpoints support pagination
4. **Date format** - Use `yyyy-MM-dd` format for dates
5. **Error handling** - Backend returns appropriate HTTP status codes
6. **Token storage** - Store JWT in localStorage or Vuex/Pinia store

---

## ⚠️ Common HTTP Status Codes

- **200** - Success
- **201** - Created
- **400** - Bad Request (validation error)
- **401** - Unauthorized (invalid/missing token)
- **403** - Forbidden (insufficient permissions)
- **404** - Not Found
- **409** - Conflict (username/email already exists)

---

**Backend Repository:** https://github.com/arvanittelaku/task-menagment-system-springboot1

**Last Updated:** October 28, 2025

