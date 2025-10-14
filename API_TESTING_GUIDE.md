# API Testing Guide with Basic Authentication

This guide explains how to test your Employee Management API using HTTP Basic Authentication.

## 🔐 Authentication Setup

Your API now supports both:
- **Form-based authentication** for web interface (login page)
- **HTTP Basic Authentication** for API endpoints

## 👥 Available Test Users

| Username | Password | Roles | Description |
|----------|----------|-------|-------------|
| `admin` | `admin` | ROLE_USER, ROLE_ADMIN | Full access |
| `user` | `user` | ROLE_USER | Standard user |
| `bill` | `bill` | ROLE_ADMIN | Admin user |

## 🧪 Testing Methods

### Method 1: PowerShell Script (Recommended)

```powershell
# Run the automated test script
.\test-api.ps1
```

### Method 2: Manual PowerShell Testing

```powershell
# Set up credentials
$username = "admin"
$password = "admin"
$baseUrl = "http://localhost:8080"

# Create Basic Auth header
$credentials = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("${username}:${password}"))
$headers = @{
    "Authorization" = "Basic $credentials"
    "Content-Type" = "application/json"
}

# Test GET /api/employees
Invoke-RestMethod -Uri "$baseUrl/api/employees" -Method GET -Headers $headers

# Test GET /api/employees/1
Invoke-RestMethod -Uri "$baseUrl/api/employees/1" -Method GET -Headers $headers

# Test POST /api/employees
$newEmployee = @{
    firstName = "John"
    lastName = "Doe"
    email = "john.doe@example.com"
    phoneNumber = "123-456-7890"
    hireDate = "2025-01-01"
    jobId = "IT_PROG"
    salary = 50000
    commissionPct = 0.1
    managerId = 100
    departmentId = 10
} | ConvertTo-Json

Invoke-RestMethod -Uri "$baseUrl/api/employees" -Method POST -Headers $headers -Body $newEmployee
```

### Method 3: Using curl

```bash
# Set up credentials
USERNAME="admin"
PASSWORD="admin"
BASE_URL="http://localhost:8080"

# Create Basic Auth header
AUTH_HEADER=$(echo -n "${USERNAME}:${PASSWORD}" | base64)

# Test GET /api/employees
curl -H "Authorization: Basic $AUTH_HEADER" "$BASE_URL/api/employees"

# Test GET /api/employees/1
curl -H "Authorization: Basic $AUTH_HEADER" "$BASE_URL/api/employees/1"

# Test POST /api/employees
curl -X POST \
  -H "Authorization: Basic $AUTH_HEADER" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phoneNumber": "123-456-7890",
    "hireDate": "2025-01-01",
    "jobId": "IT_PROG",
    "salary": 50000,
    "commissionPct": 0.1,
    "managerId": 100,
    "departmentId": 10
  }' \
  "$BASE_URL/api/employees"
```

### Method 4: Using Postman

1. **Set Authorization Type**: Basic Auth
2. **Username**: `admin`
3. **Password**: `admin`
4. **URL**: `http://localhost:8080/api/employees`

### Method 5: Using Browser Developer Tools

1. Open browser developer tools (F12)
2. Go to Network tab
3. Make a request to `http://localhost:8080/api/employees`
4. You'll see a 401 Unauthorized response
5. The response will include a `WWW-Authenticate: Basic realm="Employee Management API"` header

## 📋 API Endpoints

### GET /api/employees
- **Description**: Get all employees
- **Authentication**: Required
- **Response**: Array of employee objects

### GET /api/employees/{id}
- **Description**: Get employee by ID
- **Authentication**: Required
- **Response**: Employee object or 404

### POST /api/employees
- **Description**: Create new employee
- **Authentication**: Required
- **Request Body**: Employee object
- **Response**: Created employee object

### PUT /api/employees/{id}
- **Description**: Update employee
- **Authentication**: Required
- **Request Body**: Employee object
- **Response**: Updated employee object

### DELETE /api/employees/{id}
- **Description**: Delete employee
- **Authentication**: Required
- **Response**: 204 No Content or 404

## 🔍 Troubleshooting

### Issue: Getting HTML login page instead of JSON
**Solution**: Add Basic Authentication header to your requests

### Issue: 401 Unauthorized
**Solution**: Check your credentials and ensure the Authorization header is properly formatted

### Issue: 403 Forbidden
**Solution**: User doesn't have sufficient permissions (try admin/admin)

### Issue: Connection refused
**Solution**: Ensure the application is running on port 8080

## 🛠 Development Tools

### Swagger UI
- **URL**: http://localhost:8080/swagger-ui/index.html
- **Authentication**: Use the "Authorize" button and enter credentials

### Health Check
- **URL**: http://localhost:8080/actuator/health
- **Authentication**: Not required

## 📝 Example Responses

### Successful GET /api/employees
```json
[
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phoneNumber": "123-456-7890",
    "hireDate": "2025-01-01",
    "jobId": "IT_PROG",
    "salary": 50000,
    "commissionPct": 0.1,
    "managerId": 100,
    "departmentId": 10
  }
]
```

### Successful POST /api/employees
```json
{
  "id": 2,
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@example.com",
  "phoneNumber": "987-654-3210",
  "hireDate": "2025-01-15",
  "jobId": "IT_PROG",
  "salary": 55000,
  "commissionPct": 0.15,
  "managerId": 100,
  "departmentId": 10
}
```

### Error Response (401 Unauthorized)
```json
{
  "timestamp": "2025-01-14T13:30:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/employees"
}
```

## 🚀 Quick Start

1. **Start your application**:
   ```bash
   mvn spring-boot:run
   ```

2. **Test with PowerShell**:
   ```powershell
   .\test-api.ps1
   ```

3. **Or test manually**:
   ```powershell
   $headers = @{ "Authorization" = "Basic " + [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("admin:admin")) }
   Invoke-RestMethod -Uri "http://localhost:8080/api/employees" -Headers $headers
   ```

Your API is now ready for programmatic access with proper authentication! 🎉
