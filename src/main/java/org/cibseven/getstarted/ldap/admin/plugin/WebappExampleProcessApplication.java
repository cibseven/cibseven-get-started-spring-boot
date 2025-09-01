package org.cibseven.getstarted.ldap.admin.plugin;

import org.cibseven.bpm.engine.RuntimeService;
import org.cibseven.bpm.engine.impl.plugin.AdministratorAuthorizationPlugin;
import org.cibseven.bpm.identity.impl.ldap.plugin.LdapIdentityProviderPlugin;
import org.cibseven.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.cibseven.bpm.spring.boot.starter.event.PostDeployEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@EnableProcessApplication
public class WebappExampleProcessApplication {
  
  private static final String LDAP_PREFIX = "camunda.bpm.run.ldap";
  private static final String ADMIN_PREFIX = "camunda.bpm.run.admin-auth";

  @Bean
  @ConditionalOnProperty(name = "enabled", havingValue = "true", prefix = ADMIN_PREFIX)
  @ConfigurationProperties(prefix = ADMIN_PREFIX)
  public AdministratorAuthorizationPlugin administratorAuthorizationPlugin() {
    return new AdministratorAuthorizationPlugin();
  }

  @Bean
  @ConfigurationProperties(prefix = LDAP_PREFIX)
  @ConditionalOnProperty(name = "enabled", havingValue = "true", prefix = LDAP_PREFIX)
  public LdapIdentityProviderPlugin ldapIdentityProviderPlugin() {
    return new LdapIdentityProviderPlugin();
  }
  
  /**
   * The dependencies of some of the beans in the application context form a cycle:
┌─────┐
|  webappExampleProcessApplication (field private org.cibseven.bpm.engine.RuntimeService org.cibseven.getstarted.ldap.admin.plugin.WebappExampleProcessApplication.runtimeService)
↑     ↓
|  org.cibseven.bpm.engine.spring.SpringProcessEngineServicesConfiguration (field private org.cibseven.bpm.engine.ProcessEngine org.cibseven.bpm.engine.spring.SpringProcessEngineServicesConfiguration.processEngine)
↑     ↓
|  org.cibseven.bpm.spring.boot.starter.CamundaBpmAutoConfiguration$ProcessEngineConfigurationImplDependingConfiguration (field protected org.cibseven.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl org.cibseven.bpm.spring.boot.starter.CamundaBpmAutoConfiguration$ProcessEngineConfigurationImplDependingConfiguration.processEngineConfigurationImpl)
↑     ↓
|  processEngineConfigurationImpl defined in class path resource [org/cibseven/bpm/spring/boot/starter/CamundaBpmConfiguration.class]
└─────┘
   */
//  @Autowired
//  private RuntimeService runtimeService;

  public static void main(String... args) {
    SpringApplication.run(WebappExampleProcessApplication.class, args);
  }
  
  @EventListener
  public void processPostDeploy(PostDeployEvent event) {
    RuntimeService runtimeService = event.getProcessEngine().getRuntimeService();
    runtimeService.startProcessInstanceByKey("loanApproval");
  }

}