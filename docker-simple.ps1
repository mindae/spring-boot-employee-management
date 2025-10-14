# Simple Docker setup for Spring Boot app connecting to existing Oracle database

Write-Host "🐳 Building and running Spring Boot application in Docker..." -ForegroundColor Green
Write-Host "📋 This will connect to your existing Oracle database" -ForegroundColor Yellow
Write-Host ""

# Build the Docker image
Write-Host "Building Docker image..." -ForegroundColor Cyan
docker build -t hello-world-app:latest .

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Docker build failed!" -ForegroundColor Red
    exit 1
}

Write-Host "✅ Docker image built successfully!" -ForegroundColor Green

# Stop any existing container
Write-Host "Stopping any existing container..." -ForegroundColor Cyan
docker stop hello-world-app 2>$null
docker rm hello-world-app 2>$null

# Run the container
Write-Host "Starting Spring Boot application..." -ForegroundColor Cyan
docker run -d `
    --name hello-world-app `
    --network host `
    -e SPRING_PROFILES_ACTIVE=docker `
    -e SPRING_DATASOURCE_URL="jdbc:oracle:thin:@localhost:1521/XEPDB1" `
    -e SPRING_DATASOURCE_USERNAME="hr" `
    -e SPRING_DATASOURCE_PASSWORD="hr" `
    -v "${PWD}/logs:/app/logs" `
    hello-world-app:latest

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Spring Boot application started successfully!" -ForegroundColor Green
    Write-Host ""
    Write-Host "📋 Application Information:" -ForegroundColor Yellow
    Write-Host "  • Application URL: http://localhost:8080" -ForegroundColor Cyan
    Write-Host "  • API Documentation: http://localhost:8080/swagger-ui.html" -ForegroundColor Cyan
    Write-Host "  • Health Check: http://localhost:8080/actuator/health" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "🔗 Database Connection:" -ForegroundColor Yellow
    Write-Host "  • Oracle Database: localhost:1521/XEPDB1" -ForegroundColor Cyan
    Write-Host "  • Username: hr" -ForegroundColor Cyan
    Write-Host "  • Password: hr" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "📊 Useful Commands:" -ForegroundColor Yellow
    Write-Host "  • View logs: docker logs -f hello-world-app" -ForegroundColor Cyan
    Write-Host "  • Stop app: docker stop hello-world-app" -ForegroundColor Cyan
    Write-Host "  • Remove container: docker rm hello-world-app" -ForegroundColor Cyan
} else {
    Write-Host "❌ Failed to start application!" -ForegroundColor Red
    Write-Host "Check logs with: docker logs hello-world-app" -ForegroundColor Yellow
}
