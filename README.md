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
## Example

*We configure the server to be named as `cibseven-mcp`. *

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