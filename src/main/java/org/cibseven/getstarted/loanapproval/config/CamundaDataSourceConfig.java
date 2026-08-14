package org.cibseven.getstarted.loanapproval.config;

import javax.sql.DataSource;

import org.cibseven.modeler.config.ModelerJpa;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wires the Camunda/CIB seven engine datasource (configured via
 * {@code camunda.bpm.datasource.*}) and points the CIB seven JPA modules (modeler
 * and chat) at it.
 *
 * <p>The modeler brings its own persistence unit and transaction manager, so this
 * application only has to say which datasource its tables live in: the bean named
 * {@link ModelerJpa#DATA_SOURCE}. Without it the modeler would follow the
 * application's primary datasource ({@code spring.datasource.*}, see
 * {@link AppDataSourceConfig}), where the modeler tables do not exist. The chat
 * shares the modeler's unit, so it follows along.
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
  @Bean(ModelerJpa.DATA_SOURCE)
  public DataSource modelerDataSource(@Qualifier("camundaBpmDataSource") DataSource dataSource) {
    return dataSource;
  }
}
