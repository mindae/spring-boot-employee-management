# PlantUML Diagram Generator Script
# This script generates PNG images from PlantUML files

Write-Host "PlantUML Diagram Generator" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan

# Check if PlantUML is installed
try {
    $plantumlVersion = plantuml -version 2>$null
    if ($LASTEXITCODE -eq 0) {
        Write-Host "PlantUML is installed: $plantumlVersion" -ForegroundColor Green
    } else {
        throw "PlantUML not found"
    }
} catch {
    Write-Host "PlantUML is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install PlantUML from: https://plantuml.com/download" -ForegroundColor Yellow
    Write-Host "Or use the online version at: http://www.plantuml.com/plantuml/uml/" -ForegroundColor Yellow
    exit 1
}

# Create output directory
$outputDir = "generated"
if (!(Test-Path $outputDir)) {
    New-Item -ItemType Directory -Path $outputDir | Out-Null
    Write-Host "Created output directory: $outputDir" -ForegroundColor Green
}

# Get all PlantUML files
$pumlFiles = Get-ChildItem -Path "." -Filter "*.puml" -Recurse

if ($pumlFiles.Count -eq 0) {
    Write-Host "No PlantUML files found" -ForegroundColor Red
    exit 1
}

Write-Host "Found $($pumlFiles.Count) PlantUML files:" -ForegroundColor Cyan
foreach ($file in $pumlFiles) {
    Write-Host "  - $($file.Name)" -ForegroundColor White
}

Write-Host "`nGenerating diagrams..." -ForegroundColor Cyan

# Generate diagrams
$successCount = 0
$errorCount = 0

foreach ($file in $pumlFiles) {
    try {
        Write-Host "Processing: $($file.Name)" -ForegroundColor Yellow
        
        # Generate PNG
        $outputFile = Join-Path $outputDir "$($file.BaseName).png"
        plantuml -tpng -o $outputDir $file.FullName
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "  Generated: $outputFile" -ForegroundColor Green
            $successCount++
        } else {
            Write-Host "  Failed to generate: $($file.Name)" -ForegroundColor Red
            $errorCount++
        }
    } catch {
        Write-Host "  Error processing $($file.Name): $($_.Exception.Message)" -ForegroundColor Red
        $errorCount++
    }
}

# Generate SVG versions
Write-Host "`nGenerating SVG versions..." -ForegroundColor Cyan
foreach ($file in $pumlFiles) {
    try {
        $outputFile = Join-Path $outputDir "$($file.BaseName).svg"
        plantuml -tsvg -o $outputDir $file.FullName
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "  Generated SVG: $outputFile" -ForegroundColor Green
        }
    } catch {
        Write-Host "  Failed to generate SVG: $($file.Name)" -ForegroundColor Red
    }
}

# Summary
Write-Host "`nGeneration Summary:" -ForegroundColor Cyan
Write-Host "  Successful: $successCount" -ForegroundColor Green
Write-Host "  Failed: $errorCount" -ForegroundColor Red
Write-Host "  Output directory: $outputDir" -ForegroundColor Cyan

if ($successCount -gt 0) {
    Write-Host "`nDiagrams generated successfully!" -ForegroundColor Green
    Write-Host "Check the '$outputDir' directory for generated images." -ForegroundColor Cyan
} else {
    Write-Host "`nNo diagrams were generated successfully." -ForegroundColor Red
}

# Open output directory
if (Test-Path $outputDir) {
    $files = Get-ChildItem -Path $outputDir
    if ($files.Count -gt 0) {
        Write-Host "`nGenerated files:" -ForegroundColor Cyan
        foreach ($file in $files) {
            Write-Host "  - $($file.Name)" -ForegroundColor White
        }
        
        # Ask if user wants to open the directory
        $openDir = Read-Host "`nWould you like to open the output directory? (y/n)"
        if ($openDir -eq "y" -or $openDir -eq "Y") {
            Start-Process $outputDir
        }
    }
}
