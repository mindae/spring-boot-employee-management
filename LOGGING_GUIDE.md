# Logging Guide for Hello World Application

## Overview
This application implements comprehensive logging to help with production issue diagnosis while maintaining performance and avoiding log noise.

## Logging Configuration

### Log Levels
- **ERROR**: Critical issues that require immediate attention
- **WARN**: Potential issues or slow operations that should be monitored
- **INFO**: Important business operations and system events
- **DEBUG**: Detailed information for development and troubleshooting
- **TRACE**: Very detailed information (use sparingly)

### Log Files
- `logs/application.log` - Main application log file
- `logs/error.log` - Error-only log file for critical issues
- Log files are rotated daily with a maximum of 30 days retention
- Maximum file size: 10MB per file
- Total size cap: 1GB for application logs, 500MB for error logs

## Logging by Layer

### 1. Controller Layer
**Purpose**: Track HTTP requests and responses
**Log Level**: INFO for requests, WARN for errors
**Key Information**:
- HTTP method and endpoint
- Request parameters
- Response status codes
- Processing time
- Error details

**Example**:
```
2024-01-15 10:30:45.123 [http-nio-8080-exec-1] INFO  [EmployeeController] - GET /api/employees/123 - Retrieving employee
2024-01-15 10:30:45.156 [http-nio-8080-exec-1] INFO  [EmployeeController] - GET /api/employees/123 - Employee found: John Doe
```

### 2. Service Layer
**Purpose**: Track business logic operations
**Log Level**: INFO for operations, DEBUG for details, ERROR for failures
**Key Information**:
- Business operation start/completion
- Data processing results
- Error handling
- Performance metrics

**Example**:
```
2024-01-15 10:30:45.124 [http-nio-8080-exec-1] INFO  [EmployeeService] - Retrieving employee with ID: 123
2024-01-15 10:30:45.155 [http-nio-8080-exec-1] INFO  [EmployeeService] - Employee found: John Doe
```

### 3. Repository Layer
**Purpose**: Track database operations
**Log Level**: DEBUG for queries, WARN for slow operations
**Key Information**:
- SQL query execution
- Query parameters
- Execution time
- Result set sizes
- Database errors

**Example**:
```
2024-01-15 10:30:45.125 [http-nio-8080-exec-1] DEBUG [EmployeeRepositoryImpl] - Executing findById query for employee with ID: 123
2024-01-15 10:30:45.154 [http-nio-8080-exec-1] DEBUG [EmployeeRepositoryImpl] - findById query completed in 29ms, employee found: John Doe
```

### 4. Configuration Layer
**Purpose**: Track application startup and configuration
**Log Level**: INFO for initialization, WARN for issues
**Key Information**:
- Component initialization
- Configuration loading
- Security setup
- Database initialization

**Example**:
```
2024-01-15 10:30:40.000 [main] INFO  [SecurityConfig] - Initializing SecurityConfig with UserDetailsService
2024-01-15 10:30:40.050 [main] INFO  [DatabaseInitializer] - Starting database initialization
```

## Production Logging Best Practices

### 1. Log Levels by Environment
- **Development**: DEBUG level for detailed troubleshooting
- **Production**: INFO level for business operations, WARN/ERROR for issues
- **Staging**: INFO level with some DEBUG for testing

### 2. Performance Considerations
- Use async logging to avoid blocking application threads
- Log files are rotated to prevent disk space issues
- Error logs are separate to quickly identify critical issues

### 3. Security Considerations
- Never log sensitive data (passwords, tokens, PII)
- Use parameterized logging to avoid log injection
- Sanitize user input in log messages

### 4. Troubleshooting Production Issues

#### Common Issue Patterns to Look For:
1. **Slow Operations**: Look for WARN messages about slow database queries or HTTP requests
2. **Authentication Issues**: Check SecurityConfig and authentication logs
3. **Database Issues**: Look for database connection errors or slow queries
4. **External API Issues**: Check PostService logs for external API failures

#### Log Analysis Commands:
```bash
# Find all errors in the last hour
grep "ERROR" logs/application.log | grep "$(date -d '1 hour ago' '+%Y-%m-%d %H')"

# Find slow database operations
grep "Slow database operation" logs/application.log

# Find failed HTTP requests
grep "HTTP Error" logs/application.log

# Find authentication failures
grep "Authentication" logs/application.log
```

### 5. Log Monitoring
- Set up alerts for ERROR level logs
- Monitor log file sizes and rotation
- Monitor slow operations
- Track application performance metrics from logs

### 6. Correlation IDs
The application uses correlation IDs to track requests across multiple log entries:
```
2024-01-15 10:30:45.123 [http-nio-8080-exec-1] INFO  [EmployeeController] - [correlationId: a1b2c3d4] GET /api/employees/123 - Retrieving employee
2024-01-15 10:30:45.124 [http-nio-8080-exec-1] INFO  [EmployeeService] - [correlationId: a1b2c3d4] Retrieving employee with ID: 123
```

## Configuration Files

### application.properties
Contains basic logging configuration:
- Log levels for different packages
- File appender settings
- Console output patterns

### logback-spring.xml
Advanced logging configuration:
- Multiple appenders (console, file, error file)
- Async logging for performance
- Profile-specific configurations
- Log rotation policies

## Monitoring and Alerting

### Key Metrics to Monitor:
1. **Error Rate**: Number of ERROR logs per minute
2. **Response Time**: Track slow operations from logs
3. **Database Performance**: Monitor slow database queries
4. **External API Health**: Track external service calls

### Recommended Alerts:
- ERROR logs > 10 per minute
- Slow operations > 5 seconds
- Database connection failures
- External API failures

This logging setup provides comprehensive visibility into the application while maintaining good performance and avoiding log noise in production.
