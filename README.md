# CIB seven - Getting Started with CIB seven and Spring Boot

This Repository contains the example Spring Boot application for the guide at [docs.cibseven.org](https://docs.cibseven.org/get-started/spring-boot/).

This project requires Java 17 and currently builds against CIB seven 2.2.0 and Spring Boot 4.0.7
(see the `cibseven.version` and `spring-boot.version` properties in the [pom.xml](./pom.xml)).

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

## Java delegates on asynchronous service tasks

Beyond the tutorial, the `loanApproval` process contains two service tasks that are wired to Spring
beans via delegate expression. Both are marked `asyncBefore`, so they are executed by the job
executor rather than by the thread that starts the process instance:

```
Start → Say hello (async) → Fail on purpose (async) → Check the request (user task) → End
```

| Activity | Bean | Purpose |
| --- | --- | --- |
| `Task_HelloWorld` | [`HelloWorldDelegate`](./src/main/java/org/cibseven/getstarted/loanapproval/HelloWorldDelegate.java) | Logs a greeting together with the process instance id. |
| `Task_Failing` | [`FailingDelegate`](./src/main/java/org/cibseven/getstarted/loanapproval/FailingDelegate.java) | Throws on its first two executions and succeeds on the third, so the retry behaviour of an asynchronous task can be observed. |

Both tasks declare `<camunda:failedJobRetryTimeCycle>R3/PT10S</camunda:failedJobRetryTimeCycle>`.
`FailingDelegate` counts its attempts in an in-memory `AtomicInteger` on purpose: the counter lives
outside the transaction, so it survives the rollback the engine performs on failure and the process
instance eventually recovers instead of leaving a stuck incident behind. Increase the failure count
beyond the number of retries if you want to see the incident instead.

### Why these were added

They reproduce the setup described in
[cibseven discussion #51 — *Delegate Expression works synchronously but fails with Async Before*](https://github.com/orgs/cibseven/discussions/51),
where a `${...}` delegate expression fails with `Unknown property used in expression` as soon as
`asyncBefore` is enabled. Running this project on CIB seven 2.2.0 with Spring Boot 4.0.7 does **not**
show that behaviour — the beans are resolved on every attempt, each on a different job executor
thread:

```
[aTaskExecutor-1] HelloWorldDelegate : Hello World from process instance 6102ffb8-…
[aTaskExecutor-1] FailingDelegate    : Attempt 1 on activity Task_Failing fails on purpose
[aTaskExecutor-2] FailingDelegate    : Attempt 2 on activity Task_Failing fails on purpose
[aTaskExecutor-3] FailingDelegate    : Attempt 3 succeeded for process instance 6102ffb8-…
```

The exception surfaced by the retried job is the one thrown inside the delegate, never a failure to
resolve the bean. Note that this application is annotated with `@EnableProcessApplication`, which
activates the `ApplicationContextClassloaderSwitchPlugin` — a likely difference to the setup in the
discussion.

License: The source files in this repository are made available under the [Apache License Version 2.0](./LICENSE).
