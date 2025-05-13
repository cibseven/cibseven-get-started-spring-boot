安装部署说明
<!--覃雄伟-->
1. 环境准备
1.1 系统要求
操作系统: Windows 10/11, macOS 10.15+, 或 Linux (Ubuntu 20.04+推荐)

Java 开发环境: JDK 17+

构建工具: Maven 3.8+

版本控制: Git 2.30+

数据库: MySQL 8.0 或 PostgreSQL 13+ (可选，根据实际配置)

1.2 开发工具安装
安装JDK 17:

从Oracle官网下载

设置JAVA_HOME环境变量

安装Maven:


# Linux/macOS
sudo apt install maven
# 或从官网下载二进制包

# Windows
# 从https://maven.apache.org/download.cgi下载并配置PATH
安装Git:


# Linux
sudo apt install git

# macOS
brew install git

# Windows
# 从https://git-scm.com/download/win下载安装

<!--张佳-->
2. 获取项目代码
2.1 克隆仓库

git clone https://github.com/your-organization/your-repository.git
cd your-repository
2.2 检查分支

git checkout main  # 或指定分支
3. 项目配置
3.1 配置文件修改
编辑src/main/resources/application.yaml:


server:
  port: 8080  # 根据需要修改端口

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
3.2 数据库设置 (如需要)
创建数据库:


CREATE DATABASE your_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
运行初始化脚本 (如有)

<!--覃雄伟-->
4. 构建项目
4.1 使用Maven构建

mvn clean install
4.2 解决依赖问题
如遇到依赖下载失败:

mvn clean install -U
5. 运行项目
5.1 开发模式运行

mvn spring-boot:run
5.2 生产环境部署
打包应用:


mvn clean package -DskipTests
运行JAR文件:


java -jar target/your-application.jar
使用PM2管理 (可选):


npm install pm2 -g
pm2 start "java -jar target/your-application.jar" --name your-app


6. 验证安装
6.1 检查运行状态
访问: http://localhost:8080/actuator/health (默认端口)

应返回:


{"status":"UP"}
6.2 测试API端点
使用Postman或curl测试API:


curl http://localhost:8080/api/v1/process/start

<!--张佳-->
7. 部署到服务器
7.1 Docker部署 (推荐)
构建Docker镜像:

docker build -t your-app .
运行容器:


docker run -p 8080:8080 -d your-app
7.2 传统服务器部署
复制JAR文件到服务器

创建systemd服务:


[Unit]
Description=Your Spring Boot App
After=syslog.target

[Service]
User=appuser
ExecStart=/usr/bin/java -jar /path/to/your-application.jar
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
启动服务:


sudo systemctl daemon-reload
sudo systemctl enable your-app
sudo systemctl start your-app