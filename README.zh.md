# 测试 CIB seven 集成 <!-- by 刘仁炽 -->
## 测试目的
验证 CIB seven 集成是否正常工作，包括流程定义的部署以及流程实例的启动等关键功能，确保系统在集成 CIB seven 后能够按照预期运行，各组件之间的交互正常。
## 测试环境
- 操作系统：[Windows 10  ]
- 开发工具：[IntelliJ IDEA 2022.3.2]
- 运行环境：[JDK 11、Spring Boot 2.6.7、Camunda BPM 7.15.0]
- 数据库：[H2 数据库 2.1.210]
- 验证 CIB seven 集成是否正常工作，包括流程定义的部署以及流程实例的启动等关键功能，确保系统在集成 CIB seven 后能够按照预期运行，各组件之间的交互正常。
## 测试步骤
1. 创建测试类：在 src/main/java/org/cibseven/getstarted/loanapproval/ 路径下创建 LoanApprovalIntegrationTest.java 文件，用于编写具体的集成测试代码。
2. 编写测试代码：
```bash
package org.cibseven.getstarted.loanapproval;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PostDeployIntegrationTest {

    @Autowired
    private RuntimeService runtimeService;

    @Test
    public void shouldStartProcessAfterDeployment() throws Exception {
        // 等待应用完全启动并触发 PostDeployEvent
        Thread.sleep(5000); // 简单等待，实际测试中可使用更优雅的等待机制
        
        // 验证是否已启动流程实例
        List<ProcessInstance> instances = runtimeService
                .createProcessInstanceQuery()
                .processDefinitionKey("loanApproval")
                .list();
        
        assertThat(instances).isNotEmpty();
        assertThat(instances.size()).isEqualTo(1);
    }
}
```
3. 在pom.xml添加依赖
```bash
<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-engine</artifactId>
    <scope>test</scope>
</dependency>
```

4. 配置测试环境：在 src/test/resources 目录下的 application.yaml 文件中添加以下配置信息：
```bash
spring.datasource.url=jdbc:h2:mem:test;DB_CLOSE_DELAY=-1
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
camunda.bpm.auto-deployment-enabled=false
logging.level.org.camunda.bpm=DEBUG
```
5. 执行测试：使用 IDE 的测试运行器运行 LoanApprovalIntegrationTest 类中的测试方法；或者使用项目构建工具（如 Maven 的 mvn test 命令来执行测试。

## 预期结果
- shouldDeployProcessDefinition 测试方法中，能够获取到键为 "loanApproval" 的流程定义，断言验证流程定义不为空，测试通过。
- shouldStartProcessInstance 测试方法中，能够成功启动键为 "loanApproval" 的流程实例，断言验证流程实例不为空且流程定义 ID 不为空，测试通过。