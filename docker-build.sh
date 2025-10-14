#!/bin/bash

# Docker build script for Spring Boot application

echo "Building Spring Boot application Docker image..."

# Build the Docker image
docker build -t hello-world-app:latest .

if [ $? -eq 0 ]; then
    echo "✅ Docker image built successfully!"
    echo "Image: hello-world-app:latest"
else
    echo "❌ Docker build failed!"
    exit 1
fi
