# Clean up log files and ensure they're not tracked by Git
Write-Host "🧹 Cleaning up log files..." -ForegroundColor Yellow

# Remove logs directory from Git tracking (if it exists)
git rm -r --cached logs/ 2>$null

# Remove any existing log files
if (Test-Path "logs") {
    Remove-Item -Path "logs" -Recurse -Force
    Write-Host "✅ Removed logs directory" -ForegroundColor Green
}

# Create empty logs directory (will be ignored by Git)
New-Item -ItemType Directory -Path "logs" -Force | Out-Null
Write-Host "✅ Created empty logs directory (ignored by Git)" -ForegroundColor Green

Write-Host "🎉 Log cleanup complete!" -ForegroundColor Green
Write-Host "📝 Log files will be created at runtime but won't be committed to Git" -ForegroundColor Cyan
