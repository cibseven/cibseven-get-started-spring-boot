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
——————

    Introduction to the Integration Project of CIB Seven Platform and Spring Boot
Project objectives
Quickly build an integrated framework
Seamless integration between Spring Boot and CIB Seven platform enables process automation and financial business collaboration.
Unified process management
Use Camunda Modeler modeling tool to decouple and visualize business processes from CIB Seven's financial services module.
Standardized configuration and expansion
Reduce integration complexity and support customized development through Maven dependency management and Spring Boot automatic configuration.
Core implementation steps
Initialize Spring Boot project
Connect to CIB Seven platform
Camunda Process Modeling and Integration
Process design:
Design business processes (such as "loan approval") using Camunda Modeler and define the following key nodes:
Service task: Call the credit evaluation interface of CIB Seven.
User task: Manual review process.
Gateway: Determine process branches based on credit scores.
Process deployment:
Place the generated BPMN file (such as loan approval. bpmn) in the src/main/resources/processes directory, and it will be automatically deployed when Spring Boot starts.