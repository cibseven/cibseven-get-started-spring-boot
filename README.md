Spring Boot quick start project template, a full-process toolchain that supports enterprise-level application development.

## Functional Features

### Core Module
- **Pre-configured Maven Build**
  - Built-in Spring Boot 3.1.5, Spring Web 6.0.13, Spring Data JPA 3.1.5
  - Integrated Lombok 1.18.28 to simplify POJO development
  - Automatically generated `.gitignore` and `HELP.md` files
- **Example REST API Interface**
  - `/api/v1/hello` (GET): Returns a welcome message in JSON format
  - `/api/v1/users` (POST): User registration API (see example request body below)
  - Global exception handling: supports status codes such as `404` (resource not found), `500` (server error), etc.
- **Security and Monitoring**
  - Integrated Spring Security 6.1.5 basic configuration
  - Actuator endpoints open `/health` and `/info`
### DevOps Integration
- **CI/CD Pipeline**
  - GitHub Actions Configuration:
  - `build.yml`: Automatically compiles and runs unit tests
  - `codeql-analysis.yml`: Code security scanning (triggered weekly)
  - SonarQube code quality inspection (needs to configure `sonar-project.properties` yourself)
- **Containerization Support**
  - Multi-stage build `Dockerfile` (optimized image size to <150MB)
  - `docker-compose.yml` template integrating PostgreSQL and Redis
### Development Tools
  - Pre-configured Swagger UI documentation (access path: `/swagger-ui.html`)
  - H2 in-memory database configuration (enabled by default in the development environment)
  - Flyway database migration script template (located in `src/main/resources/db/migration`)
## Environmental Requirements
### Necessary Tools
|   Tool Name    | Minimum Version |   Verification Command   |
|----------------|------------|---------------------------|  
| Java           | 17         | `java -version`           |  
| Maven          | 3.8.6      | `mvn -v`                  | 

### Optional Tools
| Tool Name | Recommended Version | Purpose Description |
|----------------|------------|---------------------------|  
| Docker         | 24.0.6     | Containerized deployment     |  
| PostgreSQL     | 15.3       | Production environment database  |  
| IntelliJ IDEA  | 2023.2     |IDE integrated support       |

## Installation and Quick Start
### Basic Deployment
### Basic Deployment
1. Clone the repository and enter the directory:
     ```bash
   git clone https://github.com/cibseven/cibseven-get-started-springboot.git
   cd cibseven-get-started-springboot
Compilation project:
bash
mvn clean install -DskipTests 

Start the application:
bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev

Verify running statusCheck health status:
bash
curl http://localhost:8080/actuator/health
Expected output: {"status":"UP"}

Test example interface:
bash
# Example of a GET request
curl http://localhost:8080/api/v1/hello
# POST request example (User registration)
curl -X POST -H "Content-Type: application/json" \
-d '{"name":"testUser", "email":"test@example.com"}' \
http://localhost:8080/api/v1/users
Advanced Configuration
Environment variable override
Variable name     default value      application scenario
SPRING_DATASOURCE_URL jdbc:h2:mem:test, replace with the PostgreSQL connection string for the production environment.
LOGGING_LEVEL_ROOT INFO can be set to DEBUG during debugging
Docker Production Deployment

Building the image:
bash
docker build --build-arg JAR_FILE=target/*.jar -t springboot-prod .

Running container (binding to external database):
bash
docker run -d -p 8080:8080 \
-e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/mydb \
springboot-prod

Multi-environment configuration
application-dev.properties: Development environment (default activated)
application-prod.properties: Production environment (requires activation through -Dspring.profiles.active=prod)
Contribution Guide
Development process
Branch Naming Conventions:
Feature development: feat/descriptive name (e.g., feat/add-payment-module)
Defect Fix: fix/issue number (e.g. fix/#123)
Submission message format:
* Detailed description
* Related issue number (e.g., closes #456)
Type labels: feat, fix, docs, refactor, test

Code review must be approved by at least one core member.

Use the Files changed tab on GitHub to make line comments.

Test requirementsNew features must include unit tests (JUnit 5 Mockito)

Integration testing needs to be configured in src/test/resources/application-test.properties.

Document resources Core references Resource type link
Complete Guide to Spring Boot Annotations https://docs.spring.io/spring-boot/docs/current/reference/html/
Best Practices for Docker https://docs.docker.com/develop/develop-images/dockerfile_best-practices/
Community resources: Stack Overflow tags: spring-boot, docker

Chinese Forum: Spring China Community: https://spring.io/community-china

Open Source China Spring Boot SectionTroubleshootingFrequently Asked QuestionsPort Conflict:
Modify SERVER_PORT or terminate the process occupying the port:
bash
lsof -i :8080 && kill -9 <PID>

Dependency download failed:
Configure Maven mirror source (edit ~/.m2/settings.xml):
  xml
  <mirror>
    <id>aliyun-maven</id>
    <mirrorOf>*</mirrorOf>
    <name>阿里云仓库</name>
    <url>https://maven.aliyun.com/repository/public</url>
  </mirror>
  .....