package org.cibseven.getstarted.loanapproval.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AppDataSourceConfig {

  @Bean(name = "appDataSourceProperties")
  @Primary
  @ConfigurationProperties("spring.datasource")
  public DataSourceProperties appDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean(name = "appDataSource")
  @Primary
  public DataSource appDataSource(
      @Qualifier("appDataSourceProperties") DataSourceProperties properties) {
    return properties.initializeDataSourceBuilder().build();
  }
}
