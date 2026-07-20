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
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;

/**
 * Wires the Camunda/CIB seven engine datasource (configured via
 * {@code camunda.bpm.datasource.*}) and the CIB seven JPA modules (modeler and
 * chat) on top of the same datasource. Engine and JPA modules share one
 * transaction manager.
 *
 * <p>The JPA {@code entityManagerFactory}/{@code transactionManager} are exposed
 * under their default bean names so that both the modeler repositories (wired
 * here) and the EE chat repositories (wired by the auto-configured
 * {@code ChatJpaConfiguration}, which uses the default names) bind to them.
 *
 * <p>The application's primary datasource ({@code spring.datasource.*}, see
 * {@link AppDataSourceConfig}) stays untouched and remains free for the
 * application's own persistence.
 */
@Configuration
@EnableConfigurationProperties(ElementTemplateProperties.class)
@EnableJpaRepositories(basePackages = "org.cibseven.modeler.repository")
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

  /**
   * Shared entity manager factory for the modeler and chat modules, on the engine
   * datasource. Exposed under the default bean name {@code entityManagerFactory} so
   * the EE chat repositories (which use the default) bind to it as well.
   */
  @Bean
  @Primary
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(
      @Qualifier("camundaBpmDataSource") DataSource dataSource) {
    // same naming strategies Spring Boot applies to its default persistence unit
    Map<String, Object> jpaProperties = new HashMap<>();
    jpaProperties.put("hibernate.physical_naming_strategy",
        CamelCaseToUnderscoresNamingStrategy.class.getName());
    jpaProperties.put("hibernate.implicit_naming_strategy",
        SpringImplicitNamingStrategy.class.getName());
    // schema is managed externally (matches real deployments); the modeler/chat
    // tables must already exist in the datasource
    jpaProperties.put("hibernate.hbm2ddl.auto", "none");

    LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
    emf.setDataSource(dataSource);
    emf.setPersistenceUnitName("cibseven");
    emf.setPackagesToScan("org.cibseven.modeler.model", "org.cibseven.chat.model");
    emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    emf.setJpaPropertyMap(jpaProperties);
    return emf;
  }

  /**
   * Shared transaction manager for the engine, modeler and chat. Exposed under the
   * default name {@code transactionManager} (used by the chat and modeler
   * repositories) and {@code camundaBpmTransactionManager} (picked up by the CIB
   * seven starter for the engine), so all participate in the same transactions.
   */
  @Bean(name = {"transactionManager", "camundaBpmTransactionManager"})
  @Primary
  public PlatformTransactionManager transactionManager(
      @Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }
}
