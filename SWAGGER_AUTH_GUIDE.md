# Swagger UI Authentication Guide

This guide explains how to use Swagger UI with Basic Authentication for your Employee Management API.

## 🔐 How to Use Authentication in Swagger UI

### Step 1: Access Swagger UI
1. Start your application: `mvn spring-boot:run`
2. Open your browser and go to: http://localhost:8080/swagger-ui/index.html

### Step 2: Authenticate in Swagger UI
1. **Look for the "Authorize" button** at the top right of the Swagger UI page
2. **Click the "Authorize" button**
3. **Enter your credentials**:
   - **Username**: `admin`
   - **Password**: `admin`
4. **Click "Authorize"**
5. **Click "Close"**

### Step 3: Test API Endpoints
Now you can test any API endpoint:
1. **Expand any endpoint** (e.g., GET /api/employees)
2. **Click "Try it out"**
3. **Click "Execute"**
4. **You should see a successful response** instead of 401 Unauthorized

## 🎯 Available Test Credentials

| Username | Password | Roles | Description |
|----------|----------|-------|-------------|
| `admin` | `admin` | ROLE_USER, ROLE_ADMIN | Full access to all endpoints |
| `user` | `user` | ROLE_USER | Standard user access |
| `bill` | `bill` | ROLE_ADMIN | Admin user access |

## 🔧 Troubleshooting

### Issue: "Authorize" button not visible
**Solution**: 
- Make sure you're accessing the correct URL: http://localhost:8080/swagger-ui/index.html
- Check that the application is running
- Clear browser cache and refresh

### Issue: Still getting 401 Unauthorized after authentication
**Solution**:
1. Make sure you clicked "Authorize" and "Close" after entering credentials
2. Check that the lock icon next to endpoints shows as "locked" (authenticated)
3. Try logging out and logging back in

### Issue: Authentication not persisting
**Solution**:
- Swagger UI authentication is session-based
- If you refresh the page, you may need to re-authenticate
- Make sure you're not in incognito/private browsing mode

## 📋 What You'll See in Swagger UI

### Before Authentication
- All endpoints will show a lock icon 🔒
- Trying to execute any endpoint will return 401 Unauthorized
- You'll see the "Authorize" button at the top

### After Authentication
- Lock icons will show as "unlocked" 🔓
- You can execute all endpoints successfully
- You'll see your username in the top right corner

## 🧪 Testing Different Endpoints

### GET /api/employees
- **Description**: Get all employees
- **Authentication**: Required
- **Expected Response**: Array of employee objects

### GET /api/employees/{id}
- **Description**: Get employee by ID
- **Authentication**: Required
- **Parameters**: 
  - `id` (path): Employee ID (e.g., 1)
- **Expected Response**: Employee object or 404

### POST /api/employees
- **Description**: Create new employee
- **Authentication**: Required
- **Request Body**: Employee object
- **Expected Response**: Created employee object (201)

### PUT /api/employees/{id}
- **Description**: Update employee
- **Authentication**: Required
- **Parameters**: 
  - `id` (path): Employee ID
- **Request Body**: Updated employee object
- **Expected Response**: Updated employee object (200)

### DELETE /api/employees/{id}
- **Description**: Delete employee
- **Authentication**: Required
- **Parameters**: 
  - `id` (path): Employee ID
- **Expected Response**: 204 No Content

## 🎉 Success Indicators

When everything is working correctly, you should see:

1. **Green checkmarks** ✅ next to successful API calls
2. **HTTP 200/201/204** status codes instead of 401
3. **Actual JSON data** in responses instead of error messages
4. **Lock icons showing as unlocked** 🔓 for all endpoints

## 🔄 Alternative Testing Methods

If Swagger UI authentication doesn't work, you can also test using:

### PowerShell
```powershell
.\test-api.ps1
```

### curl
```bash
curl -H "Authorization: Basic YWRtaW46YWRtaW4=" http://localhost:8080/api/employees
```

### Postman
- Set Authorization Type to "Basic Auth"
- Username: `admin`
- Password: `admin`

Your Swagger UI is now fully configured with Basic Authentication support! 🎉
