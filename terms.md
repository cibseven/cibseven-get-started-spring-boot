#                                术语词汇表

## 集成（Integration）
定义：将 CIB Seven 平台与 Spring Boot 框架结合，实现技术架构的升级和优化。
目标：提升开发效率、系统性能和可维护性。

## 持久层框架(Persistence layer framework)
定义：用于数据访问和持久化的框架，如 Spring Data JPA 或 MyBatis。
作用：简化数据访问层的开发，提高数据操作效率。

## 安全框架(Security Framework)
定义：用于保护系统和数据的框架，如 Spring Security 或 Shiro。
功能：身份验证、授权、数据加密等。

## 缓存中间件(cache middleware)
定义：用于存储和快速检索数据的软件，如 Redis、Ehcache。
作用：提升系统性能，减少数据库访问压力。

## 模块化设计(Modular design)
定义：将系统划分为独立的模块，每个模块负责特定功能。
优势：提高系统的可维护性和可扩展性。

## 异步处理(asynchronously)
定义：在后台执行任务，不阻塞主线程。
应用：提升系统响应速度，改善用户体验。

## 技术创新
定义：引入新技术和新特性，推动平台升级。
目标：提升企业竞争力，满足业务需求。
## 总结
本术语词汇表聚焦于 CIB Seven 平台与 Spring Boot 集成项目中的关键概念和技术词汇，帮助理解项目的技术架构和实现目标。这些术语直接对应项目简介中提到的内容，确保与项目需求紧密相关。

建模
定义：建模是指为了某个特定目的，对现实世界中的对象、系统、过程等进行抽象、简化和结构化表达的过程。它通过建立模型，以数学、图形、文字等多种形式来描述和分析问题，帮助人们更好地理解、预测和控制所研究的对象。
流程配置
定义：流程配置是指根据业务需求和目标，对业务流程进行规划、设计、调整和优化的过程。它涉及到确定流程的各个环节、参与人员、执行顺序、输入输出等要素，以确保流程的高效运行和达成预期的业务目标。

# 术语表 (Terms Glossary)

| 英文术语                  | 中文翻译及说明                                                                 |
|---------------------------|-----------------------------------------------------------------------------|
| **Spring Security**       | Spring安全框架：企业级Java安全解决方案，提供认证/授权/攻击防护功能          |
| **OAuth 2.0**             | 开放授权2.0：行业标准的授权框架，支持第三方应用安全访问资源                  |
| **JWT (JSON Web Token)**  | JSON Web令牌：紧凑的URL安全令牌格式，用于声明信息交换（如用户身份）          |
| **Reactive Programming**  | 响应式编程：基于事件驱动的非阻塞编程范式，适合高并发场景（如WebFlux实现）    |
| **Backpressure**          | 背压机制：响应式流中控制数据生产速率以防止资源过载的策略                    |
| **Distributed Tracing**   | 分布式追踪：跨服务调用链的请求跟踪技术（Sleuth+Zipkin实现）                  |
| **Chaos Engineering**     | 混沌工程：通过主动故障注入验证系统容错能力的学科（Chaos Monkey工具）         |
| **Circuit Breaker**       | 熔断器模式：故障隔离机制，当服务异常时快速失败防止级联崩溃（Resilience4J）   |
| **Bulkhead Isolation**    | 舱壁隔离：将系统资源分组隔离，限制故障影响范围的安全设计模式                |
| **Adaptive Caching**      | 自适应缓存：根据运行时指标动态调整缓存策略的智能缓存机制                    |
| **Caffeine Cache**        | Caffeine缓存：高性能Java缓存库，支持过期策略和大小限制                      |
| **JVM Tuning**            | JVM调优：通过GC参数/内存配置优化Java虚拟机性能的技术实践                    |
=======

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




