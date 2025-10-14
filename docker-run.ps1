# PowerShell script to run the complete application stack

Write-Host "Starting Oracle database and Spring Boot application..." -ForegroundColor Green

# Start all services using docker-compose
docker-compose up -d

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Services started successfully!" -ForegroundColor Green
    Write-Host ""
    Write-Host "📋 Service Information:" -ForegroundColor Yellow
    Write-Host "  • Spring Boot App: http://localhost:8080" -ForegroundColor Cyan
    Write-Host "  • Oracle Database: localhost:1521/XE" -ForegroundColor Cyan
    Write-Host "  • Oracle EM Express: http://localhost:5500/em" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "🔐 Default Credentials:" -ForegroundColor Yellow
    Write-Host "  • Database (system): system/Oracle123" -ForegroundColor Cyan
    Write-Host "  • Database (hr): hr/hr" -ForegroundColor Cyan
    Write-Host "  • Application (admin): admin/admin" -ForegroundColor Cyan
    Write-Host "  • Application (user): user/user" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "📊 To view logs:" -ForegroundColor Yellow
    Write-Host "  docker-compose logs -f spring-boot-app" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "🛑 To stop services:" -ForegroundColor Yellow
    Write-Host "  docker-compose down" -ForegroundColor Cyan
} else {
    Write-Host "❌ Failed to start services!" -ForegroundColor Red
    exit 1
}
