# Changelog

All notable changes and improvements to this project.

## [2.0.0] - 2025-01-28

### 🔒 Security Improvements

- **Environment-Based Configuration**: Moved all sensitive credentials to environment variables
  - JWT secret now configurable via `JWT_SECRET`
  - Database credentials via `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`
  - CORS origins configurable via `CORS_ALLOWED_ORIGINS`
- **Password Security**: Maintained BCrypt password encryption with DelegatingPasswordEncoder
- **JWT Expiration**: Made configurable via `JWT_EXPIRATION` environment variable

### 🗄️ Database Improvements

- **Schema Fixes**: Added missing `created_by_manager_id` column migration
- **Hibernate Configuration**: Changed from `ddl-auto=update` to `validate` to prevent conflicts with Flyway
- **Migration Cleanup**: Removed dangerous `drop_table_users.sql` migration
- **Index Optimization**: Added index on `created_by_manager_id` for better query performance

### 🏗️ Architecture Improvements

- **Authentication Consistency**: Standardized to use email as principal identifier across the system
  - Updated `AppUserDetails.getUsername()` to return email
  - Added `getActualUsername()` method for display purposes
  - Updated all UserService methods to use email consistently
- **Entity Cleanup**: Removed redundant task relationship lists from User entity
  - Kept only `createdTasks` and `assignedTasks`
  - Removed duplicate mappings (completedTasks, canceledTasks, etc.)

### ⚡ Performance Improvements

- **Pagination Support**: Added pagination to all list endpoints
  - Tasks endpoint: `/api/v1/tasks?page=0&size=10&sortBy=createdAt&sortDirection=DESC`
  - Users endpoint: `/api/v1/users?page=0&size=10`
  - Users by role: `/api/v1/users/by-role?role=USER&page=0&size=10`
- **Repository Enhancement**: Added paginated query methods to TaskRepository
- **Efficient Data Transfer**: Used Spring Data's `Page` for efficient pagination

### 📝 Logging & Monitoring

- **SLF4J Logging**: Replaced `System.out.println` with proper logging
  - Added `@Slf4j` annotation to TaskServiceImpl
  - Implemented INFO level logs for successful operations
  - Implemented WARN level logs for access denied attempts
  - Added context to log messages (userId, taskId, etc.)

### 🧹 Code Quality Improvements

- **Removed Debug Code**: Cleaned up all debug print statements
- **Deleted Backup Files**: Removed all `.java~` and `.md~` backup files
- **Removed Unused Code**:
  - Deleted `assignTaskToUser()` method (never used)
  - Removed redundant `PublicEndpointFilter`
- **Consistent Response Messages**: Standardized API response messages across all endpoints

### 🌐 CORS Configuration

- **Centralized CORS**: Created `CorsConfig` class for global CORS configuration
- **Environment-Based**: CORS origins now configurable via environment variable
- **Removed Annotations**: Removed `@CrossOrigin` annotations from individual controllers

### 📚 Documentation

- **Comprehensive README**: Added detailed README.md with:
  - Full API documentation
  - Installation instructions
  - Environment variable setup
  - Database schema documentation
  - Error handling guidelines
  - Deployment instructions
- **CHANGELOG**: Added this changelog to track project evolution
- **Git Ignore**: Added comprehensive `.gitignore` file

### 🔧 Configuration Files

- **application.properties**: Complete refactoring with environment variables
- **CorsConfig.java**: New centralized CORS configuration
- **.gitignore**: Added comprehensive gitignore rules

### 🐛 Bug Fixes

- Fixed database schema mismatch (missing column)
- Fixed authentication inconsistency between email and username
- Fixed Hibernate and Flyway conflict
- Fixed hardcoded CORS origins
- Fixed missing JWT expiration configuration

### 🎨 API Response Improvements

- Standardized success messages for POST, PUT, DELETE operations
- Improved error response structure
- Added proper HTTP status codes (201 for created resources)
- Consistent response format across all endpoints

### 📦 Dependencies

No new dependencies added - used existing Spring Boot capabilities more effectively.

### 🚀 Ready for Production

The application is now production-ready with:

- ✅ Environment-based configuration
- ✅ Proper logging
- ✅ Pagination for scalability
- ✅ Security best practices
- ✅ Clean code architecture
- ✅ Comprehensive documentation
- ✅ Database migrations
- ✅ Error handling

---

## [1.0.0] - Initial Release

- Basic task management functionality
- User authentication with JWT
- Role-based access control (ADMIN, MANAGER, USER)
- CRUD operations for tasks and users
- PostgreSQL database integration
- Flyway migrations
- Spring Security implementation
