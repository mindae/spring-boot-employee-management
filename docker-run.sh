#!/bin/bash

# Docker run script for the complete application stack

echo "Starting Oracle database and Spring Boot application..."

# Start all services using docker-compose
docker-compose up -d

if [ $? -eq 0 ]; then
    echo "✅ Services started successfully!"
    echo ""
    echo "📋 Service Information:"
    echo "  • Spring Boot App: http://localhost:8080"
    echo "  • Oracle Database: localhost:1521/XE"
    echo "  • Oracle EM Express: http://localhost:5500/em"
    echo ""
    echo "🔐 Default Credentials:"
    echo "  • Database (system): system/Oracle123"
    echo "  • Database (hr): hr/hr"
    echo "  • Application (admin): admin/admin"
    echo "  • Application (user): user/user"
    echo ""
    echo "📊 To view logs:"
    echo "  docker-compose logs -f spring-boot-app"
    echo ""
    echo "🛑 To stop services:"
    echo "  docker-compose down"
else
    echo "❌ Failed to start services!"
    exit 1
fi
