# PowerShell script to test API endpoints with Basic Authentication

Write-Host "🧪 Testing Employee Management API with Basic Authentication" -ForegroundColor Green
Write-Host ""

$baseUrl = "http://localhost:8080"
$username = "admin"
$password = "admin"

# Create Basic Auth header
$credentials = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("${username}:${password}"))
$headers = @{
    "Authorization" = "Basic $credentials"
    "Content-Type" = "application/json"
}

Write-Host "📋 Testing API Endpoints:" -ForegroundColor Yellow
Write-Host ""

# Test 1: Get all employees
Write-Host "1️⃣ Testing GET /api/employees" -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/employees" -Method GET -Headers $headers
    Write-Host "✅ Success! Found $($response.Count) employees" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "❌ Failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 2: Get specific employee (if any exist)
Write-Host "2️⃣ Testing GET /api/employees/1" -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/employees/1" -Method GET -Headers $headers
    Write-Host "✅ Success! Employee details:" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "❌ Failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 3: Create a new employee
Write-Host "3️⃣ Testing POST /api/employees" -ForegroundColor Cyan
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

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/employees" -Method POST -Headers $headers -Body $newEmployee
    Write-Host "✅ Success! Created employee with ID: $($response.id)" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "❌ Failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 4: Test without authentication (should fail)
Write-Host "4️⃣ Testing GET /api/employees without authentication (should fail)" -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/employees" -Method GET
    Write-Host "❌ Unexpected success! This should have failed." -ForegroundColor Red
} catch {
    Write-Host "✅ Expected failure: $($_.Exception.Message)" -ForegroundColor Green
}

Write-Host ""
Write-Host "🎉 API Testing Complete!" -ForegroundColor Green
Write-Host ""
Write-Host "📝 Available Credentials:" -ForegroundColor Yellow
Write-Host "  • admin/admin (ROLE_USER, ROLE_ADMIN)" -ForegroundColor Cyan
Write-Host "  • user/user (ROLE_USER)" -ForegroundColor Cyan
Write-Host "  • bill/bill (ROLE_ADMIN)" -ForegroundColor Cyan
Write-Host ""
Write-Host "🔗 API Documentation: http://localhost:8080/swagger-ui/index.html" -ForegroundColor Yellow
