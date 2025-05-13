
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

CIB Seven Spring Boot Project Installation and Deployment Guide
<!--覃雄伟-->
1. Environment Preparation
1.1 System Requirements
Operating System: Windows 10+/Linux/macOS

Java: JDK 17 or later

Build Tools: Maven 3.6+ or Gradle 7.x

Database: MySQL 5.7+/PostgreSQL 12+ (depending on project configuration)

Optional Tool: Camunda Modeler (for editing BPMN files)

1.2 Install Required Software
Java Installation

# Linux (Ubuntu/Debian)
sudo apt update
sudo apt install openjdk-17-jdk

# Windows
# Download and install JDK 17 from https://adoptium.net/

# Verify installation
java -version

Maven Installation

# Linux (Ubuntu/Debian)
sudo apt install maven

# Windows
# Download and install from https://maven.apache.org/download.cgi

# Verify installation
mvn -v

<!--张佳-->
2. Obtain Project Code
2.1 Clone Repository

git clone https://github.com/your-repository/cibseven-springboot-project.git
cd cibseven-springboot-project

2.2 Check Branch

git branch -a
git checkout main  # or appropriate branch

3. Database Configuration
3.1 Create Database
Check application.yaml configuration:

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/seven_spring_db?useSSL=false&characterEncoding=utf8
    username: springuser
    password: springpass
    driver-class-name: com.mysql.cj.jdbc.Driver

Create Database (MySQL)

-- Execute via MySQL client
CREATE DATABASE IF NOT EXISTS seven_spring_db 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

CREATE USER 'springuser'@'%' IDENTIFIED BY 'springpass';
GRANT ALL PRIVILEGES ON seven_spring_db.* TO 'springuser'@'%';
FLUSH PRIVILEGES;

PostgreSQL Example:

CREATE DATABASE seven_spring_db 
ENCODING 'UTF8' 
LC_COLLATE 'en_US.UTF-8' 
LC_CTYPE 'en_US.UTF-8';

CREATE USER springuser WITH PASSWORD 'springpass';
GRANT ALL PRIVILEGES ON DATABASE seven_spring_db TO springuser;

4. Project Configuration
4.1 Modify Configuration File
Edit src/main/resources/application.yaml:

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/cibseven_db
    username: your_username
    password: your_password

cibseven:
  api:
    base-url: https://api.cibseven.org/v1
    api-key: your_api_key_here
    timeout: 5000

camunda:
  bpm:
    database:
      schema-update: true
    admin-user:
      id: admin
      password: admin

4.2 Environment Variables (Optional)
Set environment variables to override sensitive data:


export SPRING_DATASOURCE_PASSWORD=your_db_password
export CIBSVEN_API_KEY=your_api_key

5. Build the Project
5.1 Build with Maven

mvn clean install -DskipTests
5.2 Build Docker Image (Optional)
If the project includes a Dockerfile:


docker build -t cibseven-app:1.0 .

<!--覃雄伟-->
6. Deploy the Project
6.1 Run Locally

mvn spring-boot:run

# Or run the packaged JAR
java -jar target/cibseven-getstarted-1.0.0.jar
6.2 Production Deployment
Using Systemd (Linux)
Create a service file at /etc/systemd/system/cibseven.service:


[Unit]
Description=CIB Seven Spring Boot Application
After=syslog.target network.target

[Service]
User=appuser
WorkingDirectory=/opt/cibseven
ExecStart=/usr/bin/java -jar /opt/cibseven/cibseven-getstarted-1.0.0.jar
SuccessExitStatus=143
Restart=always

[Install]
WantedBy=multi-user.target
Enable and start the service:


sudo systemctl daemon-reload
sudo systemctl enable cibseven
sudo systemctl start cibseven
Using Docker Compose
Create docker-compose.yml:


version: '3.8'

services:
  cibseven-app:
    image: cibseven-app:1.0
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/cibseven_db
      - SPRING_DATASOURCE_USERNAME=root
      - SPRING_DATASOURCE_PASSWORD=dbpassword
    depends_on:
      - db

  db:
    image: mysql:5.7
    environment:
      - MYSQL_ROOT_PASSWORD=dbpassword
      - MYSQL_DATABASE=cibseven_db
    volumes:
      - db_data:/var/lib/mysql
    ports:
      - "3306:3306"

volumes:
  db_data:
Start services:


docker-compose up -d
6.3 Deploy BPMN Models
Steps to Deploy BPMN Models:

Design & Validate BPMN Model:

Use tools like Camunda Modeler to create/edit BPMN 2.0 files.

Validate syntax and export as .bpmn or .bpmn20.xml.

Deploy to Camunda Engine:

Via REST API:


curl -X POST \
  "http://localhost:8080/engine-rest/deployment/create" \
  -H "Content-Type: multipart/form-data" \
  -F "deployment-name=my-process" \
  -F "deployment-source=local" \
  -F "deploy-changed-only=true" \
  -F "my-process.bpmn=@/path/to/your/model.bpmn"
Via Java Code:


@Autowired
private RepositoryService repositoryService;

public void deployProcess() {
  repositoryService.createDeployment()
    .addClasspathResource("processes/my-process.bpmn")
    .name("My Process Deployment")
    .deploy();
}

<!--张佳-->
7. Verify Deployment
7.1 Check Application Status

curl http://localhost:8080/actuator/health
# Expected output: {"status":"UP"}
7.2 Access Interfaces
Swagger UI (if configured): http://localhost:8080/swagger-ui.html

Camunda Tasklist: http://localhost:8080/camunda/app/tasklist

8. Troubleshooting
8.1 Database Connection Issues
Error: Cannot create connection to database server

Solutions:

Verify database service is running.

Check JDBC URL, username, and password in application.yaml.

8.2 CIB Seven API Authentication Failure
Error: 401 Unauthorized

Solutions:

Confirm API key and authentication headers are correct.

Check API key permissions and expiration.

8.3 BPMN Deployment Failure
Error: Failed to deploy BPMN model

Solutions:

Validate BPMN file syntax using Camunda Modeler.

Ensure database user has sufficient privileges.

<!--覃雄伟-->
9. Upgrade Guide
Pull the latest code:


git pull origin main
Rebuild the project:


mvn clean install
Restart the service:


sudo systemctl restart cibseven
# Or
docker-compose up -d --build

10. Backup & Recovery
10.1 Database Backup

mysqldump -u username -p cibseven_db > cibseven_backup_$(date +%F).sql
10.2 Database Restore

mysql -u username -p cibseven_db < cibseven_backup_2023-01-01.sql


License: The source files in this repository are made available under the [Apache License Version 2.0](./LICENSE).
——————
#韩辉
    Introduction to the Integration Project of CIB Seven Platform and Spring Boot
project background
With the acceleration of enterprise digital transformation, the demand for efficient, flexible, and scalable technology architecture is increasing day by day. As an important business support system within the enterprise, the CIB Seven platform needs to be deeply integrated with modern development frameworks and technologies to improve development efficiency, system performance, and maintainability. Spring Boot, as a popular Java development framework, has become the preferred choice for enterprise application development due to its simple configuration, rich ecosystem, and powerful performance.

Project objectives
This project aims to deeply integrate the CIB Seven platform with the Spring Boot framework to achieve the following goals:

Improve development efficiency: Utilize the automatic configuration and rapid development features of Spring Boot to reduce developers' configuration and coding work, and accelerate project delivery.
Enhance system performance: Improve system response speed and throughput through Spring Boot's performance optimization and asynchronous processing mechanism.
Improve maintainability: With the modular design and rich ecosystem tools of Spring Boot, simplify the system maintenance and upgrade process.
Promote technological innovation: Introduce new technologies and features of Spring Boot to drive technological and business innovation on the CIB Seven platform.
Integrated Content
This project mainly covers the integration of the following aspects:

Basic framework integration: Spring Boot is used as the basic development framework for the CIB Seven platform, replacing the original development framework to achieve rapid development and efficient operation and maintenance.
Data Access Integration: Utilizing persistence layer frameworks such as Spring Data JPA or MyBatis to simplify the development of the data access layer and improve data access efficiency.
Security Framework Integration: Integrate security frameworks such as Spring Security or Shiro to enhance system security and protect enterprise data and user information.
Message queue integration: Introducing message queue middleware such as RabbitMQ and Kafka to achieve asynchronous communication and decoupling between systems, improving system scalability and reliability.
Cache integration: Integrate Redis, Ehcache and other caching middleware to improve system performance and response speed, and reduce database access pressure.
Log management integration: Utilize Spring Boot's log management framework, such as Logback or Log4j2, to achieve unified management and analysis of logs.
expected results
Through the implementation of this project, it is expected to achieve the following results:

Significant improvement in development efficiency: Developers can complete business function development and testing more quickly, shortening project delivery cycles.
The system performance has been significantly enhanced: the response speed and throughput of the system have been significantly improved, meeting the high concurrency and high load business needs of enterprises.
Significant improvement in maintainability: The modularity and configurability of the system have been enhanced, reducing maintenance costs and improving the scalability and reusability of the system.
Continuous promotion of technological innovation: Introducing new technologies and features to drive technological and business innovation on the CIB Seven platform, enhancing the competitiveness of enterprises.
summarize
The integration project between CIB Seven platform and Spring Boot is an important step in the digital transformation of enterprises. Through deep integration, the technical advantages of Spring Boot will be fully utilized to enhance the development efficiency, system performance, and maintainability of the CIB Seven platform, providing strong technical support for the sustainable development of enterprises.


