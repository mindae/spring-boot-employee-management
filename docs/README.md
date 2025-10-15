# Employee Management System - PlantUML Diagrams

This directory contains comprehensive PlantUML diagrams for the Spring Boot Employee Management System.

## 📊 Available Diagrams

### 1. **Class Diagram** (`class-diagram.puml`)
- **Purpose**: Shows the object-oriented structure of the system
- **Components**: 
  - Entity classes (User, Authority, Employee)
  - Repository interfaces
  - Service classes
  - Controller classes
  - Configuration classes
- **Relationships**: JPA associations, dependency injection, inheritance

### 2. **Sequence Diagrams**

#### **Login Flow** (`sequence-login.puml`)
- **Purpose**: Illustrates the authentication process
- **Flow**: User → Browser → LoginController → SecurityConfig → UserDetailsService → Database
- **Features**: Form-based authentication, CSRF protection, session management

#### **Employee Operations** (`sequence-employee.puml`)
- **Purpose**: Shows CRUD operations for employee management
- **Operations**: GET, POST, PUT, DELETE
- **Flow**: Client → EmployeeController → EmployeeService → EmployeeRepository → Database
- **Features**: Basic authentication, transaction management

### 3. **Activity Diagrams**

#### **User Registration** (`activity-user-registration.puml`)
- **Purpose**: Shows the complete user registration process
- **Flow**: Form validation → Username check → Password encryption → Database save → Authority assignment
- **Features**: Input validation, password encryption, email confirmation, transaction rollback

#### **Employee CRUD Operations** (`activity-employee-crud.puml`)
- **Purpose**: Detailed flow for all employee management operations
- **Operations**: Create, Read, Update, Delete, List, Search
- **Features**: Role-based access, input validation, dependency checking, audit logging

#### **Authentication Process** (`activity-authentication.puml`)
- **Purpose**: Complete authentication and authorization flow
- **Flow**: Credential validation → Password verification → Authority checking → Session management
- **Features**: Account lockout, failed login tracking, session timeout, logout handling

#### **System Startup** (`activity-system-startup.puml`)
- **Purpose**: Application initialization and startup process
- **Flow**: Configuration loading → Database connection → Security setup → Default data creation
- **Features**: Database initialization, default user creation, logging setup, graceful shutdown

#### **Error Handling** (`activity-error-handling.puml`)
- **Purpose**: Comprehensive error handling and recovery process
- **Flow**: Exception detection → Error classification → Response generation → User notification
- **Features**: Exception mapping, error logging, administrator alerts, retry mechanisms

### 4. **Component Diagram** (`component-diagram.puml`)
- **Purpose**: High-level system architecture
- **Layers**: Frontend, Presentation, Business, Data Access, Database, Infrastructure
- **Components**: Controllers, Services, Repositories, Database tables
- **Connections**: Dependencies and data flow between components

### 5. **Architecture Overview** (`architecture-overview.puml`)
- **Purpose**: System-wide architecture view
- **Layers**: Client, Application, Database, Infrastructure
- **Deployment**: Docker containerization, Oracle database integration
- **Security**: Authentication and authorization flows

### 6. **Security Flow** (`security-flow.puml`)
- **Purpose**: Security process flow
- **Types**: Web interface authentication, API authentication
- **Features**: Basic auth, form auth, CSRF protection, role-based access

## 🚀 How to Use These Diagrams

### **Online Viewing**
1. Copy the content of any `.puml` file
2. Visit [PlantUML Online Server](http://www.plantuml.com/plantuml/uml/)
3. Paste the content and click "Submit"

### **Local Rendering**
1. Install PlantUML locally
2. Use command: `plantuml -tpng docs/*.puml`
3. Generated images will be in the same directory

### **IDE Integration**
- **IntelliJ IDEA**: Install PlantUML plugin
- **VS Code**: Install PlantUML extension
- **Eclipse**: Install PlantUML plugin

## 📋 Diagram Features

### **Class Diagram Features**
- ✅ Entity relationships (OneToMany, ManyToOne)
- ✅ Repository patterns
- ✅ Service layer architecture
- ✅ Controller endpoints
- ✅ Security configuration

### **Sequence Diagram Features**
- ✅ Complete authentication flow
- ✅ CRUD operations with error handling
- ✅ Database interactions
- ✅ Security validation steps
- ✅ Response handling

### **Component Diagram Features**
- ✅ Layered architecture
- ✅ Database schema representation
- ✅ Infrastructure components
- ✅ External system connections
- ✅ Security flow annotations

## 🔧 Customization

### **Themes**
- Change `!theme plain` to other themes:
  - `!theme aws-orange`
  - `!theme carbon-gray`
  - `!theme cerulean`

### **Colors**
- Modify `skinparam` settings for custom colors
- Use `skinparam backgroundColor` for background colors
- Use `skinparam BorderColor` for border colors

### **Styling**
- Adjust `skinparam roundcorner` for rounded corners
- Modify `skinparam sequenceArrowThickness` for arrow thickness
- Use `skinparam maxmessagesize` for message length

## 📚 Related Documentation

- [API Testing Guide](../API_TESTING_GUIDE.md)
- [Swagger Authentication Guide](../SWAGGER_AUTH_GUIDE.md)
- [Docker Deployment Guide](../DOCKER_DEPLOYMENT.md)
- [GitHub Setup Guide](../GITHUB_SETUP_GUIDE.md)

## 🎯 Use Cases

### **For Developers**
- Understanding system architecture
- Onboarding new team members
- Code review and documentation
- System design discussions

### **For Stakeholders**
- System overview presentations
- Architecture documentation
- Security flow explanation
- Database schema understanding

### **For DevOps**
- Deployment architecture
- Infrastructure requirements
- Security configuration
- Monitoring and logging

---

**Generated for**: Spring Boot Employee Management System  
**Version**: 1.0.0  
**Author**: Mahendra Chaurasia  
**Email**: mahendrachaurasia@gmail.com  
**GitHub**: [@mindae](https://github.com/mindae)
