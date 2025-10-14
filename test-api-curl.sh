#!/bin/bash

# Bash script to test API endpoints with Basic Authentication using curl

echo "🧪 Testing Employee Management API with Basic Authentication"
echo ""

BASE_URL="http://localhost:8080"
USERNAME="admin"
PASSWORD="admin"

# Create Basic Auth header
AUTH_HEADER=$(echo -n "${USERNAME}:${PASSWORD}" | base64)

echo "📋 Testing API Endpoints:"
echo ""

# Test 1: Get all employees
echo "1️⃣ Testing GET /api/employees"
echo "Command: curl -H \"Authorization: Basic $AUTH_HEADER\" $BASE_URL/api/employees"
echo ""

response=$(curl -s -w "\n%{http_code}" -H "Authorization: Basic $AUTH_HEADER" "$BASE_URL/api/employees")
http_code=$(echo "$response" | tail -n1)
body=$(echo "$response" | head -n -1)

if [ "$http_code" -eq 200 ]; then
    echo "✅ Success! HTTP $http_code"
    echo "$body" | jq '.' 2>/dev/null || echo "$body"
else
    echo "❌ Failed! HTTP $http_code"
    echo "$body"
fi

echo ""

# Test 2: Get specific employee
echo "2️⃣ Testing GET /api/employees/1"
echo "Command: curl -H \"Authorization: Basic $AUTH_HEADER\" $BASE_URL/api/employees/1"
echo ""

response=$(curl -s -w "\n%{http_code}" -H "Authorization: Basic $AUTH_HEADER" "$BASE_URL/api/employees/1")
http_code=$(echo "$response" | tail -n1)
body=$(echo "$response" | head -n -1)

if [ "$http_code" -eq 200 ]; then
    echo "✅ Success! HTTP $http_code"
    echo "$body" | jq '.' 2>/dev/null || echo "$body"
elif [ "$http_code" -eq 404 ]; then
    echo "ℹ️  No employee found with ID 1 (HTTP $http_code)"
else
    echo "❌ Failed! HTTP $http_code"
    echo "$body"
fi

echo ""

# Test 3: Create a new employee
echo "3️⃣ Testing POST /api/employees"
echo "Command: curl -X POST -H \"Authorization: Basic $AUTH_HEADER\" -H \"Content-Type: application/json\" -d '...' $BASE_URL/api/employees"
echo ""

NEW_EMPLOYEE='{
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
}'

response=$(curl -s -w "\n%{http_code}" -X POST -H "Authorization: Basic $AUTH_HEADER" -H "Content-Type: application/json" -d "$NEW_EMPLOYEE" "$BASE_URL/api/employees")
http_code=$(echo "$response" | tail -n1)
body=$(echo "$response" | head -n -1)

if [ "$http_code" -eq 201 ]; then
    echo "✅ Success! HTTP $http_code - Employee created"
    echo "$body" | jq '.' 2>/dev/null || echo "$body"
else
    echo "❌ Failed! HTTP $http_code"
    echo "$body"
fi

echo ""

# Test 4: Test without authentication (should fail)
echo "4️⃣ Testing GET /api/employees without authentication (should fail)"
echo "Command: curl $BASE_URL/api/employees"
echo ""

response=$(curl -s -w "\n%{http_code}" "$BASE_URL/api/employees")
http_code=$(echo "$response" | tail -n1)
body=$(echo "$response" | head -n -1)

if [ "$http_code" -eq 401 ]; then
    echo "✅ Expected failure! HTTP $http_code - Authentication required"
else
    echo "❌ Unexpected response! HTTP $http_code"
    echo "$body"
fi

echo ""
echo "🎉 API Testing Complete!"
echo ""
echo "📝 Available Credentials:"
echo "  • admin/admin (ROLE_USER, ROLE_ADMIN)"
echo "  • user/user (ROLE_USER)"
echo "  • bill/bill (ROLE_ADMIN)"
echo ""
echo "🔗 API Documentation: http://localhost:8080/swagger-ui/index.html"
