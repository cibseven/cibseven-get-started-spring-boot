package org.cibseven.getstarted.loanapproval;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnProperty(prefix = "cibseven.mcp", name = "restapi-mcp", havingValue = "true")
@Import(org.cibseven.mcp.restapi.RESTAPIMcpToolsConfig.class)
public class RestApiMcpConfig {
}