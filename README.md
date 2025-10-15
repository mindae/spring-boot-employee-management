# 🚀 Spring Boot Employee Management System

[![CI/CD Pipeline](https://github.com/mindae/spring-boot-employee-management/actions/workflows/ci.yml/badge.svg)](https://github.com/mindae/spring-boot-employee-management/actions/workflows/ci.yml)
[![Docker Build](https://github.com/mindae/spring-boot-employee-management/actions/workflows/docker-build.yml/badge.svg)](https://github.com/mindae/spring-boot-employee-management/actions/workflows/docker-build.yml)
[![Security Scan](https://github.com/mindae/spring-boot-employee-management/actions/workflows/security.yml/badge.svg)](https://github.com/mindae/spring-boot-employee-management/actions/workflows/security.yml)
[![Release](https://github.com/mindae/spring-boot-employee-management/actions/workflows/release.yml/badge.svg)](https://github.com/mindae/spring-boot-employee-management/actions/workflows/release.yml)
[![codecov](https://codecov.io/gh/mindae/spring-boot-employee-management/branch/master/graph/badge.svg)](https://codecov.io/gh/mindae/spring-boot-employee-management)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A comprehensive Spring Boot application with employee management, authentication, Docker support, and comprehensive testing frameworks.

## 📊 Project Status

| Status | Description |
|--------|-------------|
| 🟢 **Build** | All builds passing |
| 🟢 **Tests** | Comprehensive test coverage |
| 🟢 **Security** | Regular security scans |
| 🟢 **Quality** | Code quality checks enabled |
| 🟢 **Docker** | Containerized deployment ready |

## ✨ Features

- **🔐 Authentication & Security**: HTTP Basic Auth + Form-based login
- **👥 Employee Management**: Full CRUD operations for employees
- **📊 API Documentation**: Swagger UI with authentication support
- **🐳 Docker Support**: Containerized deployment with external Oracle DB
- **🧪 Testing**: BDD testing with comprehensive test coverage
- **📝 Logging**: Advanced logging with Logback configuration
- **🔧 AOP**: Aspect-Oriented Programming for cross-cutting concerns
- **🌐 REST APIs**: RESTful endpoints with proper HTTP status codes

## 🛠 Tech Stack

- **Backend**: Spring Boot 3.3.4, Spring Security, Spring Data JPA
- **Database**: Oracle Database (XEPDB1)
- **Authentication**: HTTP Basic Auth + Form-based login
- **Documentation**: OpenAPI 3.0 (Swagger UI)
- **Testing**: JUnit 5, AssertJ, WireMock, Jqwik
- **Containerization**: Docker, Docker Compose
- **Build Tool**: Maven
- **Java Version**: 17

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+
- Oracle Database (running on localhost:1521/XEPDB1)
- Docker Desktop (for containerized deployment)

### Local Development

1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd cursor-demo
   ```

2. **Configure Database**
   - Ensure Oracle Database is running on `localhost:1521/XEPDB1`
   - Create user `hr` with password `hr`
   - Grant necessary privileges

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - **Main App**: http://localhost:8080
   - **API Documentation**: http://localhost:8080/swagger-ui/index.html
   - **Health Check**: http://localhost:8080/actuator/health

### Docker Deployment

1. **One-command deployment**
   ```powershell
   .\docker-simple.ps1
   ```

2. **Or using Docker Compose**
   ```bash
   docker-compose -f docker-compose-simple.yml up -d --build
   ```

## 🔐 Authentication

### Available Users
| Username | Password | Roles | Description |
|----------|----------|-------|-------------|
| `admin` | `admin` | ROLE_USER, ROLE_ADMIN | Full access |
| `user` | `user` | ROLE_USER | Standard user |
| `bill` | `bill` | ROLE_ADMIN | Admin user |

### API Testing
```powershell
# Test with PowerShell
.\test-api.ps1

# Or manually with curl
curl -H "Authorization: Basic YWRtaW46YWRtaW4=" http://localhost:8080/api/employees
```

## 📋 API Endpoints

### Employee Management
- `GET /api/employees` - Get all employees
- `GET /api/employees/{id}` - Get employee by ID
- `POST /api/employees` - Create new employee
- `PUT /api/employees/{id}` - Update employee
- `DELETE /api/employees/{id}` - Delete employee

### Authentication Required
All API endpoints require authentication (HTTP Basic Auth or form login).

## 🧪 Testing

### Run Tests
```bash
# Run all tests
mvn test

# Run specific test classes
mvn test -Dtest=SimpleBddTest

# Run with coverage
mvn test jacoco:report
```

### Test Categories
- **Unit Tests**: Service and repository layer testing
- **Integration Tests**: Full application context testing
- **BDD Tests**: Behavior-driven development tests
- **API Tests**: REST endpoint testing

## 🐳 Docker Configuration

### Files Structure
```
├── Dockerfile                 # Multi-stage Docker build
├── docker-compose-simple.yml # Simple orchestration
├── docker-simple.ps1         # One-command deployment
├── application-docker.properties # Docker-specific config
└── .dockerignore             # Docker build context
```

### Environment Variables
- `SPRING_PROFILES_ACTIVE=docker`
- `SPRING_DATASOURCE_URL=jdbc:oracle:thin:@localhost:1521/XEPDB1`
- `SPRING_DATASOURCE_USERNAME=hr`
- `SPRING_DATASOURCE_PASSWORD=hr`

## 📊 Monitoring & Logging

### Log Files
- **Application Logs**: `logs/application.log`
- **Error Logs**: `logs/error.log`
- **Console Output**: Structured logging with timestamps

### Health Checks
- **Application Health**: `/actuator/health`
- **Database Connection**: Automatically checked on startup

## 🔧 Configuration

### Application Properties
- **Default Profile**: `application.properties`
- **Docker Profile**: `application-docker.properties`
- **Logging**: `logback-spring.xml`

### Database Configuration
```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521/XEPDB1
spring.datasource.username=hr
spring.datasource.password=hr
spring.jpa.hibernate.ddl-auto=update
```

## 📚 Documentation

- [API Testing Guide](API_TESTING_GUIDE.md)
- [Docker Deployment Guide](DOCKER_DEPLOYMENT.md)
- [Swagger Authentication Guide](SWAGGER_AUTH_GUIDE.md)
- [Logging Guide](LOGGING_GUIDE.md)
- [BDD Testing Guide](BDD_TESTING_GUIDE.md)
- [AOP Logging Guide](AOP_LOGGING_GUIDE.md)
- [Exception Handling Guide](AOP_EXCEPTION_HANDLING_GUIDE.md)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Mahendra Chaurasia**
- Email: mahendrachaurasia@gmail.com
- GitHub: [@mindae](https://github.com/mindae)

## 🙏 Acknowledgments

- Spring Boot team for the amazing framework
- Oracle for the database support
- Docker team for containerization
- Open source community for various libraries

---

⭐ **Star this repository if you found it helpful!**