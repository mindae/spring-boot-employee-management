# PowerShell script to build Docker image

Write-Host "Building Spring Boot application Docker image..." -ForegroundColor Green

# Build the Docker image
docker build -t hello-world-app:latest .

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Docker image built successfully!" -ForegroundColor Green
    Write-Host "Image: hello-world-app:latest" -ForegroundColor Cyan
} else {
    Write-Host "❌ Docker build failed!" -ForegroundColor Red
    exit 1
}
