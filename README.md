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

# Test CIB seven Integration <!-- by 刘仁炽 -->
## Test Objectives
Verify whether the CIB seven integration works properly, including key functions such as the deployment of process definitions and the start of process instances. Ensure that the system can operate as expected after integrating CIB seven, and the interaction between various components is normal.
## Test Environment
- Operating System: [Windows 10]
- Development Tool: [IntelliJ IDEA 2022.3.2]
- Runtime Environment: [JDK 17, Spring Boot 2.6.7, Camunda BPM 7.15.0]
- Database: [H2 Database 2.1.210]
## Test Steps
1. Create a Test Class: Create the LoanApprovalIntegrationTest.java file under the path src/main/java/org/cibseven/getstarted/loanapproval/ to write specific integration test code.
2. Write Test Code:
```bash
package org.cibseven.getstarted.loanapproval;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PostDeployIntegrationTest {

    @Autowired
    private RuntimeService runtimeService;

    @Test
    public void shouldStartProcessAfterDeployment() throws Exception {
        // Wait for the application to fully start and trigger the PostDeployEvent
        Thread.sleep(5000); // Simple waiting, a more elegant waiting mechanism can be used in actual tests

        // Verify whether the process instance has been started
        var instances = runtimeService
               .createProcessInstanceQuery()
               .processDefinitionKey("loanApproval")
               .list();

        assertThat(instances).isNotEmpty();
        assertThat(instances.size()).isEqualTo(1);
    }
}
```
3. Add the dependency in the pom.xml.
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
4. Configure the Test Environment: Add the following configuration information to the application.yaml file in the src/test/resources directory:
```bash
spring:
  datasource:
    url: jdbc:h2:mem:test;DB_CLOSE_DELAY=-1
    driver-class-name: org.h2.Driver
    username: sa
    password: 
camunda:
  bpm:
    auto-deployment-enabled: false
logging:
  level:
    org.camunda.bpm: DEBUG
```
5. Execute the Test: Use the test runner of the IDE to run the test methods in the LoanApprovalIntegrationTest class; or use the project build tool (such as the mvn test command of Maven) to execute the test.
## Expected Results
- In the shouldDeployProcessDefinition test method, the process definition with the key "loanApproval" can be obtained, and the assertion verifies that the process definition is not null, and the test passes.
- In the shouldStartProcessInstance test method, the process instance with the key "loanApproval" can be successfully started, and the assertion verifies that the process instance is not null and the process definition ID is not null, and the test passes.