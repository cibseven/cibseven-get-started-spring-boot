package org.cibseven.getstarted.loanapproval.config;

import javax.sql.DataSource;

import org.cibseven.persistence.CibsevenJpa;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wires the Camunda/CIB seven engine datasource (configured via
 * {@code camunda.bpm.datasource.*}) and points the CIB seven webclient tables (the
 * modeler's and the chat's) at it.
 *
 * <p>The webclient brings its own persistence unit and transaction manager, so this
 * application only has to say which datasource those tables live in: the bean named
 * {@link CibsevenJpa#DATA_SOURCE}. Without it the webclient would follow the
 * application's primary datasource ({@code spring.datasource.*}, see
 * {@link AppDataSourceConfig}), where those tables do not exist. The modeler and
 * the chat share the one unit, so both follow along.
 *
 * <p>The application's own {@code entityManagerFactory} and
 * {@code transactionManager} stay free for the application's persistence.
 */
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

  /** The modeler and chat tables live in the engine database. */
  @Bean(CibsevenJpa.DATA_SOURCE)
  public DataSource cibsevenDataSource(@Qualifier("camundaBpmDataSource") DataSource dataSource) {
    return dataSource;
  }
}
