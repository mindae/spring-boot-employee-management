#!/bin/bash

# PlantUML Diagram Generator Script
# This script generates PNG images from PlantUML files

echo "🎨 PlantUML Diagram Generator"
echo "================================"

# Check if PlantUML is installed
if ! command -v plantuml &> /dev/null; then
    echo "❌ PlantUML is not installed or not in PATH"
    echo "Please install PlantUML from: https://plantuml.com/download"
    echo "Or use the online version at: http://www.plantuml.com/plantuml/uml/"
    exit 1
fi

echo "✅ PlantUML is installed: $(plantuml -version)"

# Create output directory
output_dir="generated"
if [ ! -d "$output_dir" ]; then
    mkdir -p "$output_dir"
    echo "📁 Created output directory: $output_dir"
fi

# Get all PlantUML files
puml_files=$(find . -name "*.puml" -type f)

if [ -z "$puml_files" ]; then
    echo "❌ No PlantUML files found"
    exit 1
fi

echo "📊 Found PlantUML files:"
echo "$puml_files" | while read -r file; do
    echo "  - $(basename "$file")"
done

echo ""
echo "🔄 Generating diagrams..."

# Generate diagrams
success_count=0
error_count=0

echo "$puml_files" | while read -r file; do
    echo "Processing: $(basename "$file")"
    
    # Generate PNG
    if plantuml -tpng -o "$output_dir" "$file"; then
        echo "  ✅ Generated: $output_dir/$(basename "$file" .puml).png"
        ((success_count++))
    else
        echo "  ❌ Failed to generate: $(basename "$file")"
        ((error_count++))
    fi
done

# Generate SVG versions
echo ""
echo "🔄 Generating SVG versions..."
echo "$puml_files" | while read -r file; do
    if plantuml -tsvg -o "$output_dir" "$file"; then
        echo "  ✅ Generated SVG: $output_dir/$(basename "$file" .puml).svg"
    else
        echo "  ❌ Failed to generate SVG: $(basename "$file")"
    fi
done

# Summary
echo ""
echo "📈 Generation Summary:"
echo "  ✅ Successful: $success_count"
echo "  ❌ Failed: $error_count"
echo "  📁 Output directory: $output_dir"

if [ $success_count -gt 0 ]; then
    echo ""
    echo "🎉 Diagrams generated successfully!"
    echo "Check the '$output_dir' directory for generated images."
else
    echo ""
    echo "❌ No diagrams were generated successfully."
fi

# List generated files
if [ -d "$output_dir" ]; then
    files=$(ls "$output_dir" 2>/dev/null)
    if [ -n "$files" ]; then
        echo ""
        echo "📂 Generated files:"
        echo "$files" | while read -r file; do
            echo "  - $file"
        done
    fi
fi
