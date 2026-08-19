# CIB seven - Getting Started with CIB seven and Spring Boot

This Repository contains the example Spring Boot application for the guide at [docs.cibseven.org](https://docs.cibseven.org/get-started/spring-boot/).

This project is set up to work with Spring Boot 4 and Java 21.

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

## Still using Spring Boot 3?

No problem! You can still set up CIB seven with Spring Boot 3 using [this configuration](https://github.com/cibseven/cibseven-get-started-spring-boot/tree/spring-boot-3).

## CIB seven AI Agent Connector

Bring agentic AI to CIB seven BPMN workflows. The AI Agent Connector lets AI Agent service tasks interact with LLMs, use tools and knowledge, maintain context, and return results directly to your process. Want to know more? Check out the [documentation](https://docs.cibseven.org/manual/latest/reference/connect/ai-agent-connector/).

To use this feature, you need to add the following dependencies to the `<dependencies>` section of the pom file.

```xml
    <dependency>
       <groupId>org.cibseven.connect</groupId>
       <artifactId>cibseven-connect-agent-connector</artifactId>
       <version>${cibseven.version}</version>
    </dependency>
    <dependency>
       <groupId>org.cibseven.connect</groupId>
       <artifactId>cibseven-connect-connectors-all</artifactId>
       <version>${cibseven.version}</version>
    </dependency>
    <dependency>
      <groupId>org.cibseven.bpm</groupId>
      <artifactId>cibseven-engine-plugin-connect</artifactId>
      <version>${cibseven.version}</version>
    </dependency>
```