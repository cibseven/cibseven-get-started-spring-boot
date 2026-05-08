package org.cibseven.getstarted.loanapproval.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class CamundaDataSourceConfig {

  @Bean(name = "camundaDataSourceProperties")
  @ConfigurationProperties("camunda.bpm.datasource")
  public DataSourceProperties camundaDataSourceProperties() {
    return new DataSourceProperties();
  }

  //Will be picked up by org.cibseven.bpm.spring.boot.starter.configuration.impl.DefaultDatasourceConfiguration
  @Bean("camundaBpmDataSource")
  public DataSource camundaDataSource(
      @Qualifier("camundaDataSourceProperties") DataSourceProperties properties) {
    return properties.initializeDataSourceBuilder().build();
  }

  //Will be picked up by org.cibseven.bpm.spring.boot.starter.configuration.impl.DefaultDatasourceConfiguration
  @Bean("camundaBpmTransactionManager")
  public PlatformTransactionManager camundaTransactionManager(
      @Qualifier("camundaDataSource") DataSource camundaDataSource) {
    return new DataSourceTransactionManager(camundaDataSource);
  }
}
