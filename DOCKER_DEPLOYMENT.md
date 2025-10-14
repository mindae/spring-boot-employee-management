# Docker Deployment Guide

This guide explains how to deploy the Spring Boot application in Docker while connecting to your existing Oracle database.

## Prerequisites

- Docker Desktop installed and running
- Your existing Oracle database running on localhost:1521/XEPDB1
- Port 8080 available on your system

## Quick Start

### Option 1: Simple PowerShell Script (Recommended)

```powershell
# Build and run the application
.\docker-simple.ps1
```

### Option 2: Using Docker Compose

```powershell
# Build and start the application
docker-compose -f docker-compose-simple.yml up -d --build
```

### Option 3: Manual Docker Commands

```bash
# Build the application image
docker build -t hello-world-app:latest .

# Run the container
docker run -d --name hello-world-app --network host -e SPRING_PROFILES_ACTIVE=docker -e SPRING_DATASOURCE_URL="jdbc:oracle:thin:@localhost:1521/XEPDB1" -e SPRING_DATASOURCE_USERNAME="hr" -e SPRING_DATASOURCE_PASSWORD="hr" -v "${PWD}/logs:/app/logs" hello-world-app:latest
```

## Services

### Spring Boot Application
- **URL**: http://localhost:8080
- **Container**: hello-world-app
- **Port**: 8080
- **Profile**: docker

### Your Existing Oracle Database
- **URL**: localhost:1521/XEPDB1
- **Username**: hr
- **Password**: hr

## Default Credentials

### Your Existing Database
- **HR User**: hr/hr (as configured in your application.properties)

### Application
- **Admin**: admin/admin
- **User**: user/user

## Useful Commands

### View Logs
```bash
# View all logs
docker-compose logs -f

# View application logs only
docker-compose logs -f spring-boot-app

# View database logs only
docker-compose logs -f oracle-db
```

### Stop Services
```bash
# Stop all services
docker-compose down

# Stop and remove volumes (WARNING: This will delete all data)
docker-compose down -v
```

### Restart Services
```bash
# Restart all services
docker-compose restart

# Restart specific service
docker-compose restart spring-boot-app
```

### Access Database
```bash
# Connect to Oracle database
docker exec -it oracle-xe sqlplus hr/hr@localhost:1521/XE

# Connect as system user
docker exec -it oracle-xe sqlplus system/Oracle123@localhost:1521/XE
```

### Access Application Container
```bash
# Access application container shell
docker exec -it hello-world-app bash

# View application logs inside container
docker exec -it hello-world-app tail -f /app/logs/application.log
```

## Configuration

### Environment Variables
The application uses the following environment variables (set in docker-compose.yml):

- `SPRING_PROFILES_ACTIVE=docker`
- `SPRING_DATASOURCE_URL=jdbc:oracle:thin:@oracle-db:1521/XE`
- `SPRING_DATASOURCE_USERNAME=hr`
- `SPRING_DATASOURCE_PASSWORD=hr`

### Custom Configuration
To modify database connection or other settings, edit the environment variables in `docker-compose.yml`.

## Troubleshooting

### Common Issues

1. **Port Already in Use**
   ```bash
   # Check what's using the port
   netstat -an | findstr :8080
   netstat -an | findstr :1521
   ```

2. **Database Connection Issues**
   ```bash
   # Check if Oracle is ready
   docker-compose logs oracle-db
   
   # Wait for database to be ready
   docker-compose logs -f oracle-db
   ```

3. **Application Won't Start**
   ```bash
   # Check application logs
   docker-compose logs spring-boot-app
   
   # Check if database is healthy
   docker-compose ps
   ```

4. **Out of Memory**
   - Increase Docker memory limit in Docker Desktop settings
   - Minimum 4GB RAM recommended

### Health Checks
```bash
# Check service status
docker-compose ps

# Check if application is responding
curl http://localhost:8080/actuator/health

# Check if database is responding
docker exec oracle-xe sqlplus -L hr/hr@localhost:1521/XE -c "SELECT 1 FROM DUAL;"
```

## Development

### Rebuilding After Code Changes
```bash
# Rebuild and restart application only
docker-compose up -d --build spring-boot-app

# Rebuild everything
docker-compose down
docker-compose up -d --build
```

### Database Persistence
Data is persisted in a Docker volume named `oracle-data`. To reset the database:

```bash
# Stop services and remove volume
docker-compose down -v

# Start services (will recreate database)
docker-compose up -d
```

## Production Considerations

1. **Security**: Change default passwords
2. **Resource Limits**: Set appropriate memory and CPU limits
3. **Backup**: Implement database backup strategy
4. **Monitoring**: Add health checks and monitoring
5. **SSL**: Configure SSL/TLS for production

## API Endpoints

Once running, you can access:

- **Application**: http://localhost:8080
- **API Documentation**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/actuator/health
- **Employees API**: http://localhost:8080/api/employees
