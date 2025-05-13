使用教程-配置杨文华

1. 基础环境配置
在 application.yaml 中配置Camunda管理用户和流程过滤器：

yaml
camunda.bpm:
  admin-user:
    id: demo               # 管理账号用户名（生产环境需修改）
    password: demo         # 管理账号密码（生产环境需修改）
    firstName: Demo        # 显示名称
  filter:
    create: All tasks      # 默认任务过滤器
2. CIB Seven 平台连接配置
在 application.yaml 中添加以下参数以对接CIB Seven金融服务：

yaml
cibseven:
  api:
    base-url: https://api.cibseven.com/v1  # 平台API地址
    auth:
      client-id: your-client-id            # 客户端ID（从平台获取）
      secret-key: your-secret-key          # 密钥（从平台获取）
    timeout: 5000                          # 接口超时时间（毫秒）
3. Camunda流程引擎配置
自动部署流程：
将BPMN文件（如loan-approval.bpmn）放入 src/main/resources/processes 目录，Spring Boot启动时自动加载并部署。

自定义流程引擎参数：

yaml
camunda.bpm:
  database:
    schema-update: true     # 自动更新数据库表结构
  history-level: full       # 流程历史记录级别
  job-execution:
    enabled: true          # 启用异步任务执行
4. 多环境配置
通过Profile区分环境（示例）：

yaml
# application-dev.yaml（开发环境）
cibseven:
  api:
    base-url: http://dev-api.cibseven.com/v1

# application-prod.yaml（生产环境）
cibseven:
  api:
    base-url: https://api.cibseven.com/v1
5. 安全配置建议
Camunda管理账号：生产环境中必须修改默认账号密码

CIB Seven密钥：通过Vault或环境变量注入敏感信息，避免明文存储

HTTPS加密：确保所有API通信启用TLS加密

验证配置是否生效
启动Spring Boot应用

访问 http://localhost:8080/camunda 登录控制台

检查流程定义列表是否包含部署的BPMN流程

调用CIB Seven接口时，通过日志确认连接参数正确性