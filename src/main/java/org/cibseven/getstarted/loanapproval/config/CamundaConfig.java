package org.cibseven.getstarted.loanapproval.config;

import java.util.List;

import javax.sql.DataSource;

import org.cibseven.bpm.engine.impl.cfg.CompositeProcessEnginePlugin;
import org.cibseven.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.cibseven.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.cibseven.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.cibseven.bpm.spring.boot.starter.util.CamundaSpringBootUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class CamundaConfig {

  // Mirrors org.cibseven.bpm.spring.boot.starter.CamundaBpmConfiguration#processEngineConfigurationImpl.
  // Using `new SpringProcessEngineConfiguration()` here instead of the helper, or omitting the
  // CompositeProcessEnginePlugin line, would skip property binding (camunda.bpm.*) and the
  // job executor would not start despite camunda.bpm.job-execution.enabled=true.
  @Bean
  public ProcessEngineConfigurationImpl processEngineConfiguration(
      @Qualifier("camundaDataSource") DataSource camundaDataSource,
      @Qualifier("camundaTransactionManager") PlatformTransactionManager camundaTransactionManager,
      List<ProcessEnginePlugin> processEnginePlugins) {

    SpringProcessEngineConfiguration configuration =
        CamundaSpringBootUtil.springProcessEngineConfiguration();

    configuration.setDataSource(camundaDataSource);
    configuration.setTransactionManager(camundaTransactionManager);
    configuration.getProcessEnginePlugins().add(new CompositeProcessEnginePlugin(processEnginePlugins));

    return configuration;
  }
}
