# CIB seven - Getting Started with CIB seven and Spring Boot

This Repository contains the example Spring Boot application for the guide at [docs.cibseven.org](https://docs.cibseven.org/get-started/spring-boot/).

This project requires Java 17.

Every step of the tutorial was tagged in this repository. You can jump to the final state of each step
by the following command:

```
git checkout -f Step-X
```

If you want to follow the tutorial along please clone this repository and checkout the `Start` tag.

```
git clone https://github.com/cibseven/cibseven-get-started-spring-boot.git
git checkout -f Start
```

License: The source files in this repository are made available under the [Apache License Version 2.0](./LICENSE).

Installation and Deployment Guide
<!--覃雄伟-->
1. Environment Preparation
1.1 System Requirements
Operating System: Windows 10/11, macOS 10.15+, or Linux (Ubuntu 20.04+ recommended)

Java Development Environment: JDK 17+

Build Tool: Maven 3.8+

Version Control: Git 2.30+

Database: MySQL 8.0 or PostgreSQL 13+ (Optional, depending on actual configuration)

1.2 Development Tools Installation
Install JDK 17:

Download from Oracle JDK

Set the JAVA_HOME environment variable.

Install Maven:

# Linux/macOS  
sudo apt install maven  
# Or download binaries from the official website  

# Windows  
# Download from https://maven.apache.org/download.cgi and configure PATH  
Install Git:

# Linux  
sudo apt install git  

# macOS  
brew install git  

# Windows  
# Download from https://git-scm.com/download/win  

<!--张佳-->
2. Obtain Project Code
2.1 Clone Repository


git clone https://github.com/your-organization/your-repository.git  
cd your-repository  
2.2 Check Branch


git checkout main  # or specified branch  
3. Project Configuration
3.1 Modify Configuration File
Edit src/main/resources/application.yaml:


server:  
  port: 8080  # Modify port as needed  

spring:  
  datasource:  
    url: jdbc:mysql://localhost:3306/your_db  
    username: your_username  
    password: your_password  
    driver-class-name: com.mysql.cj.jdbc.Driver  

cibseven:  
  api:  
    base-url: https://api.cibseven.org/v1  
    key: your-api-key  
3.2 Database Setup (If Required)
Create the database:


CREATE DATABASE your_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;  
Run initialization scripts (if provided).

<!--覃雄伟-->
4. Build the Project
4.1 Build with Maven


mvn clean install  
4.2 Resolve Dependency Issues
If dependency downloads fail:



mvn clean install -U  
5. Run the Project
5.1 Run in Development Mode


mvn spring-boot:run  
5.2 Deploy to Production
Package the application:



mvn clean package -DskipTests  
Run the JAR file:


java -jar target/your-application.jar  
Optional PM2 Management:


npm install pm2 -g  
pm2 start "java -jar target/your-application.jar" --name your-app  
6. Verify Installation
6.1 Check Runtime Status
Access: http://localhost:8080/actuator/health (default port)
Expected response:


{"status":"UP"}  
6.2 Test API Endpoints
Use Postman or curl:


curl http://localhost:8080/api/v1/process/start  

<!--张佳-->
7. Deploy to Server
7.1 Docker Deployment (Recommended)
Build the Docker image:


docker build -t your-app .  
Run the container:


docker run -p 8080:8080 -d your-app  
7.2 Traditional Server Deployment
Copy the JAR file to the server.

Create a systemd service (/etc/systemd/system/your-app.service):


[Unit]  
Description=Your Spring Boot App  
After=syslog.target  

[Service]  
User=appuser  
ExecStart=/usr/bin/java -jar /path/to/your-application.jar  
SuccessExitStatus=143  

[Install]  
WantedBy=multi-user.target  
Start the service:


sudo systemctl daemon-reload  
sudo systemctl enable your-app  
sudo systemctl start your-app  