
- 断言：Assertions 
- 过程实例启动：Start of Process Instances
- 数据库配置：Database Configuration
- 依赖：Dependency
- CIB seven 集成测试：CIB seven Integration Test


# 项目术语说明  

#### Camunda BPM  
流程自动化引擎，用于设计、执行和监控业务流程。  

#### BPMN (Business Process Model and Notation)  
标准化业务流程建模语言，通过图形化界面定义流程节点和逻辑。  

#### CIB Seven 平台  
金融业务协同平台，提供信用评估、交易处理等标准化API接口。  

#### Spring Boot Profile  
多环境配置机制，通过`application-{env}.yaml`文件区分开发、测试、生产环境参数。  

#### TLS 加密  
传输层安全协议，用于保障API通信数据的机密性和完整性。  

#### 流程历史记录级别  
Camunda参数（如`history-level: full`），控制流程实例运行数据的存储粒度。  

#### 异步任务执行  
Camunda后台任务处理模式，通过`job-execution.enabled`开关控制。  

# Project Terminology Glossary

#### Camunda BPM
Process automation engine for designing, executing, and monitoring business workflows.

#### BPMN (Business Process Model and Notation)
Standardized graphical notation for defining business process logic and workflow structures.

#### CIB Seven Platform
Financial business collaboration platform offering standardized APIs for credit evaluation, transaction processing, and compliance services.

#### Spring Boot Profile
Multi-environment configuration mechanism enabling parameter isolation via application-{env}.yaml files (e.g., dev/test/prod).

#### TLS Encryption
Transport Layer Security protocol ensuring confidentiality and integrity of API communication data.

#### Process History Level
Camunda configuration parameter (e.g., history-level: full) controlling the granularity of process instance data persistence.

#### Asynchronous Task Execution
Camunda's background task processing mode governed by the job-execution.enabled configuration toggle.


中文术语                 | 英文术语
REST API 接口	         |  REST API
JSON 格式	             |JSON Format
POJO 简化开发	         |POJO (Plain Old Java Object)
H2 内存数据库	         |H2 In-Memory Database
Flyway 数据库迁移工具	  |Flyway Database Migration
Swagger UI 文档	Swagger |UI Documentation
JUnit 5 单元测试框架	 |JUnit 5
Mockito 测试框架	    |Mockito
GitHub Actions 配置     |	GitHub Actions Configuration
CodeQL 代码安全扫描	    |CodeQL Security Scanning
SonarQube 代码质量检测	 |SonarQube Code Quality Inspection
多阶段构建	            |Multi-stage Build
docker-compose.yml 模板	 |docker-compose.yml Template
Redis 数据库 	        |Redis Database
Actuator 端点	        |Actuator Endpoints
环境变量覆盖	        |Environment Variable Overrides
多环境配置文件	        |Multi-Environment Configuration Files
Spring Profiles 配置	|Spring Profiles
Git 分支命名规范	    |Git Branch Naming Conventions
提交消息格式	        |Commit Message Format
代码审查流程	        |Code Review Process
单元测试	            |Unit Testing
集成测试	            |Integration Testing
Spring Boot 注解指南	|Spring Boot Annotations Guide
Docker 最佳实践	        |Docker Best Practices
Stack Overflow 标签	    |Stack Overflow Tags
Spring 中国社区	        |Spring China Community
端口冲突解决	        |Port Conflict Resolution
Maven 镜像配置	        |Maven Mirror Configuration
Maven 编译命令	        |Maven Build Command
Docker 生产镜像构建	    |Docker Production Image Build
Actuator 端点, 多环境配置	|Actuator Endpoints, Multi-Environment Config
Git 分支规范, 代码审查	 |Git Branch Conventions, Code Review
Maven 镜像配置	        |Maven Mirror Configuration

.

CIB Seven Spring Boot 项目术语词汇表（中英文对照）
中文术语	英文术语/缩写
应用配置文件	application.yaml Configuration File
数据源配置	Datasource Configuration
API 密钥	API Key
业务流程模型与标注	BPMN (Business Process Model and Notation)
流程实例	Process Instance
Docker 容器编排	Docker Compose
系统服务单元	Systemd Service Unit
数据库备份与恢复	Database Backup & Recovery
流程定义键	Process Definition Key
环境变量注入	Environment Variables Injection
关键术语说明
BPMN

中文：业务流程模型与标注

用途：标准化的业务流程建模语言，用于设计 Camunda 工作流。

Process Instance

中文：流程实例

用途：表示一个具体业务流程的运行实例（如订单审批、请假申请）。

API Key

中文：API 密钥

用途：用于身份验证，确保对 CIB Seven 平台 API 的安全访问。

Docker Compose

中文：Docker 容器编排工具

用途：通过 YAML 文件定义多容器应用的服务依赖与部署配置。

Systemd Service Unit

中文：系统服务单元

用途：在 Linux 系统中管理 Spring Boot 应用的后台服务（启动、监控、重启）。



