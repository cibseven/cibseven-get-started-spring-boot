package org.cibseven.getstarted.loanapproval.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.cibseven.modeler.config.ElementTemplateProperties;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;

/**
 * Wires the Camunda/CIB seven engine datasource (configured via
 * {@code camunda.bpm.datasource.*}) and the modeler JPA infrastructure
 * (entities, repositories, REST controllers, service providers) on top of the
 * same datasource. Engine and modeler share one transaction manager.
 *
 * <p>The application's primary datasource ({@code spring.datasource.*}, see
 * {@link AppDataSourceConfig}) stays untouched and remains free for the
 * application's own persistence.
 */
@Configuration
@EnableConfigurationProperties(ElementTemplateProperties.class)
@EnableJpaRepositories(
    basePackages = "org.cibseven.modeler.repository",
    entityManagerFactoryRef = "modelerEntityManagerFactory",
    transactionManagerRef = "camundaBpmTransactionManager")
@ComponentScan({
    "org.cibseven.modeler.rest",
    "org.cibseven.modeler.provider",
    "org.cibseven.modeler.repository",
    "org.cibseven.modeler.util"
})
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

  @Bean
  public LocalContainerEntityManagerFactoryBean modelerEntityManagerFactory(
      @Qualifier("camundaBpmDataSource") DataSource dataSource) {
    // same naming strategies Spring Boot applies to its default persistence
    // unit, so the modeler tables keep their usual names
    Map<String, Object> jpaProperties = new HashMap<>();
    jpaProperties.put("hibernate.physical_naming_strategy",
        CamelCaseToUnderscoresNamingStrategy.class.getName());
    jpaProperties.put("hibernate.implicit_naming_strategy",
        SpringImplicitNamingStrategy.class.getName());
    jpaProperties.put("hibernate.hbm2ddl.auto", "none");

    LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
    emf.setDataSource(dataSource);
    emf.setPersistenceUnitName("modeler");
    emf.setPackagesToScan("org.cibseven.modeler.model");
    emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    emf.setJpaPropertyMap(jpaProperties);
    return emf;
  }

  /**
   * Shared transaction manager for the engine and the modeler. A
   * {@link JpaTransactionManager} is required for the modeler's JPA
   * repositories; it also exposes the underlying JDBC connection of
   * {@code camundaBpmDataSource}, so the engine participates in the same
   * transactions. Picked up by the CIB seven starter via its qualifier
   * (org.cibseven.bpm.spring.boot.starter.configuration.impl.DefaultDatasourceConfiguration),
   * and used by the modeler repositories via {@code transactionManagerRef}.
   */
  @Bean("camundaBpmTransactionManager")
  public PlatformTransactionManager camundaTransactionManager(
      @Qualifier("modelerEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }
}
