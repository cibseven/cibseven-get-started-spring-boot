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

