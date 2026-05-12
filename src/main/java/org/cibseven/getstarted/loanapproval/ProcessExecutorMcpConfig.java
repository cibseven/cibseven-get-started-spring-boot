package org.cibseven.getstarted.loanapproval;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnProperty(prefix = "cibseven.mcp", name = "processes-mcp", havingValue = "true")
@Import(org.cibseven.mcp.processexecutor.ProcessesMcpToolsConfig.class)
public class ProcessExecutorMcpConfig {
}