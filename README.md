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

## Our MCP Servers 

CIB seven provides a set of MCP servers that enable programmatic interaction with the CIB seven platform through Large Language Models (LLMs).

To integrate them, you only need to add them as dependency:
```xml
<dependency>
  <groupId>org.cibseven.mcp</groupId>
  <artifactId>cibseven-mcp-restapi</artifactId>
  <version>1.1.0-SNAPSHOT</version>
</dependency>    
<dependency>
  <groupId>org.cibseven.mcp</groupId>
  <artifactId>cibseven-mcp-process-executor</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```
and enable them in the configuration:

```yaml
cibseven:
  mcp:
    restapi-mcp: true
    processes-mcp: true
```
### Authentication

### Servlet filter

Our MCP servers support JWT authentication. They are ready to be used with our composite filter (`FilterConfiguration`) if enabled.

For this setup, you will need to properly configure the MCP server in your MCP client. For example:
```json
"cibseven-mcp": {
            "url": "http://localhost:8080/mcp",
            "type": "http",
            "headers": {"Authorization": "Bearer ..."}
}
```

### OAuth2

In case you want to enable OAuth2 in Spring Boot, our MCP servers will configure their security filters to work with OAuth2 as well. To do this, you only need to follow [the documentation steps](https://docs.cibseven.org/manual/latest/user-guide/spring-boot-integration/spring-security/#securing-engine-rest) with the only difference that you should set `issuer-uri` instead of `jwk-set-uri`:
```yaml
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: <issuerUri>
```
*Please remember to disable your servlet filter to avoid interference with Spring Security.*

Regarding the MCP client configuration:
```json
"cibseven-mcp": {
          "url": "http://localhost:8080/mcp",
          "type": "http",
          "auth": {
              "CLIENT_ID": <clientId>,
              "CLIENT_SECRET": <clientSecret>
          }
}
```

### Example

*We configure the server to be named as `cibseven-mcp`.*

**User:** Which business tools are exposed by the `cibseven-mcp` server?

**LLM likely reply:**
- `mcp_cibseven-mcp_businessTool_add_comment_to_krank_jira_task_v1` — adds a comment to a Krank Jira task
- `mcp_cibseven-mcp_businessTool_refresh_process_definitions` — refreshes process definitions

---

**User:**  
Do I need any required input variable for the add-comment-to-Krank-Jira-task tool?

**LLM likely reply:**

The tool has only one required input variable:

- `commentBody` — the text of the comment you want to add to Jira issue `KRANK-5735`

It is passed under the `variables` object like this:

```json
{
  "variables": {
    "commentBody": {
      "value": "Your comment text here"
    }
  }
}
```

The target Jira issue (`KRANK-5735`) is hardcoded into the process, so you do not need to specify it.

Just provide the comment body and the tool will handle the rest, including an optional review step before submission.

---

**User:**  
Execute the add-comment tool with comment `"I'm not feeling well today"`.

**LLM:**  
The comment was successfully submitted.

The process returned a status of `"sick leave"` for the comment `"I'm not feeling well today"`.