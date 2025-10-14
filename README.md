## Simple Spring Boot "Hello World" (Maven)

### Prerequisites
- Java 17+
- Maven 3.9+

### Build
```bash
mvn clean package
```

### Run
```bash
mvn spring-boot:run
# or
java -jar target/hello-world-0.0.1-SNAPSHOT.jar
```

### Test the endpoint
```bash
# without auth (401 after enabling security)
curl -i http://localhost:8080/api/hello

# with HTTP Basic auth
curl -i -u admin:admin123 http://localhost:8080/api/hello
# -> 200 OK and body: Hello, World!
```

### H2 Console
- Visit `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- User: `sa`, Password: (leave blank)

### Employee CRUD (JSON)
Base path: `/api/employees`

Create
```bash
curl -i -u admin:admin123 \
  -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Doe","email":"john.doe@example.com"}' \
  http://localhost:8080/api/employees
```

List
```bash
curl -i -u admin:admin123 http://localhost:8080/api/employees
```

Get by id
```bash
curl -i -u admin:admin123 http://localhost:8080/api/employees/1
```

Update
```bash
curl -i -u admin:admin123 -X PUT \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Jane","lastName":"Doe","email":"jane.doe@example.com"}' \
  http://localhost:8080/api/employees/1
```

Delete
```bash
curl -i -u admin:admin123 -X DELETE http://localhost:8080/api/employees/1
```

### Project structure
```
.
├── pom.xml
├── src
│   └── main
│       ├── java
│       │   └── com
│       │       └── example
│       │           └── hello
│       │               ├── HelloWorldApplication.java
│       │               ├── config
│       │               │   └── SecurityConfig.java
│       │               └── controller
│       │                   └── HelloController.java
│       │               └── model
│       │               │   └── Employee.java
│       │               └── repository
│       │                   └── EmployeeRepository.java
│       └── resources
│           └── application.properties
└── README.md
```


