Spring Boot 快速启动项目模板，支持企业级应用开发的全流程工具链。  

---

## 功能特性  

### 核心模块  
- **预配置 Maven 构建**  
  - 内置 Spring Boot 3.1.5、Spring Web 6.0.13、Spring Data JPA 3.1.5  
  - 集成 Lombok 1.18.28 简化 POJO 开发  
  - 自动生成 `.gitignore` 和 `HELP.md` 文件  
- **示例 REST API 接口**  
  - `/api/v1/hello` (GET): 返回 JSON 格式欢迎消息  
  - `/api/v1/users` (POST): 用户注册接口（示例请求体见下文）  
  - 全局异常处理：支持 `404`（资源未找到）、`500`（服务器错误）等状态码  
- **安全与监控**  
  - 集成 Spring Security 6.1.5 基础配置  
  - Actuator 端点开放 `/health` 和 `/info`  

### DevOps 集成  
- **CI/CD 流水线**  
  - GitHub Actions 配置：  
    - `build.yml`: 自动编译并运行单元测试  
    - `codeql-analysis.yml`: 代码安全扫描（每周定时触发）  
  - SonarQube 代码质量检测（需自行配置 `sonar-project.properties`）  
- **容器化支持**  
  - 多阶段构建 `Dockerfile`（优化镜像体积至 <150MB）  
  - `docker-compose.yml` 模板整合 PostgreSQL 和 Redis  

### 开发工具  
- 预置 Swagger UI 文档（访问路径：`/swagger-ui.html`）  
- H2 内存数据库配置（开发环境默认启用）  
- Flyway 数据库迁移脚本模板（位于 `src/main/resources/db/migration`）  

## 环境要求  

### 必需工具  
| 工具名称       | 最低版本   | 验证命令                  |  
|----------------|------------|---------------------------|  
| Java           | 17         | `java -version`           |  
| Maven          | 3.8.6      | `mvn -v`                  |  

### 可选工具  
| 工具名称       | 推荐版本   | 用途说明                  |  
|----------------|------------|---------------------------|  
| Docker         | 24.0.6     | 容器化部署                |  
| PostgreSQL     | 15.3       | 生产环境数据库            |  
| IntelliJ IDEA  | 2023.2     | IDE 集成支持              |  

## 安装与快速启动  

### 基础部署  
1. 克隆仓库并进入目录：  
   ```bash
   git clone https://github.com/cibseven/cibseven-get-started-springboot.git
   cd cibseven-get-started-springboot
编译项目：

bash
mvn clean install -DskipTests  # 跳过测试以加速构建
启动应用：

bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
验证运行状态
检查健康状态：

bash
curl http://localhost:8080/actuator/health
预期输出：{"status":"UP"}

测试示例接口：

bash
# GET 请求示例
curl http://localhost:8080/api/v1/hello

# POST 请求示例（用户注册）
curl -X POST -H "Content-Type: application/json" \
-d '{"name":"testUser", "email":"test@example.com"}' \
http://localhost:8080/api/v1/users
高级配置
环境变量覆盖
变量名	默认值	应用场景
SPRING_DATASOURCE_URL	jdbc:h2:mem:test	生产环境需替换为 PostgreSQL 连接字符串
LOGGING_LEVEL_ROOT	INFO	调试时可设置为 DEBUG
Docker 生产部署
构建镜像：

bash
docker build --build-arg JAR_FILE=target/*.jar -t springboot-prod .
运行容器（绑定外部数据库）：

bash
docker run -d -p 8080:8080 \
-e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/mydb \
springboot-prod
多环境配置
application-dev.properties: 开发环境（默认激活）

application-prod.properties: 生产环境（需通过 -Dspring.profiles.active=prod 激活）

贡献指南
开发流程
分支命名规范：

功能开发：feat/描述性名称（如 feat/add-payment-module）

缺陷修复：fix/issue编号（如 fix/#123）

提交消息格式：


* 详细说明
* 关联的 Issue 编号（如 closes #456）
类型标签：feat、fix、docs、refactor、test

代码审查
必须通过至少 1 名核心成员的 Review

使用 GitHub 的 Files changed 标签进行行级评论

测试要求
新增功能需包含单元测试（JUnit 5 + Mockito）

集成测试需在 src/test/resources/application-test.properties 中配置
文档资源
核心参考
资源类型	链接
Spring Boot 注解大全	https://docs.spring.io/spring-boot/docs/current/reference/html/
Docker 最佳实践	https://docs.docker.com/develop/develop-images/dockerfile_best-practices/
社区资源
Stack Overflow 标签：spring-boot、docker

中文论坛：

Spring 中国社区：https://spring.io/community-china

开源中国 Spring Boot 专区
故障排除
常见问题
端口冲突：

修改 SERVER_PORT 或终止占用端口的进程：

bash
lsof -i :8080 && kill -9 <PID>
依赖下载失败：

配置 Maven 镜像源（编辑 ~/.m2/settings.xml）：

xml
<mirror>
  <id>aliyun-maven</id>
  <mirrorOf>*</mirrorOf>
  <name>阿里云仓库</name>
  <url>https://maven.aliyun.com/repository/public</url>
</mirror>
获取支持
....