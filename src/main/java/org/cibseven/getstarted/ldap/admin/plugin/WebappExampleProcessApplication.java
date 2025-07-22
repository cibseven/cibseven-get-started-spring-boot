package org.cibseven.getstarted.ldap.admin.plugin;

import org.cibseven.bpm.engine.RuntimeService;
import org.cibseven.bpm.engine.impl.plugin.AdministratorAuthorizationPlugin;
import org.cibseven.bpm.identity.impl.ldap.plugin.LdapIdentityProviderPlugin;
import org.cibseven.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.cibseven.bpm.spring.boot.starter.event.PostDeployEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@EnableProcessApplication
public class WebappExampleProcessApplication {

  /**
   * https://docs.cibseven.org/manual/latest/user-guide/process-engine/identity-service/#configuration-properties-of-the-ldap-plugin
   * https://docs.cibseven.org/manual/latest/user-guide/cibseven-run/#ldap-identity-service
   */
  
  @Value("${camunda.bpm.run.ldap.server-url:}")
  private String ldapServerUrl;
  
  @Value("${camunda.bpm.run.ldap.manager-dn:}")
  private String ldapManagerDn;
  
  @Value("${camunda.bpm.run.ldap.manager-password:}")
  private String ldapManagerPassword;
  
  @Value("${camunda.bpm.run.ldap.base-dn:}")
  private String ldapBaseDn;
  
  @Value("${camunda.bpm.run.ldap.user-search-base:}")
  private String ldapUserSearchBase;
  
  @Value("${camunda.bpm.run.ldap.user-search-filter:}")
  private String ldapUserSearchFilter;
  
  @Value("${camunda.bpm.run.ldap.user-id-attribute:}")
  private String ldapUserIdAttribute;
  
  @Value("${camunda.bpm.run.ldap.user-firstname-attribute:}")
  private String ldapUserFirstnameAttribute;
  
  @Value("${camunda.bpm.run.ldap.user-lastname-attribute:}")
  private String ldapUserLastnameAttribute;
  
  @Value("${camunda.bpm.run.ldap.user-email-attribute:}")
  private String ldapUserEmailAttribute;
  
  @Value("${camunda.bpm.run.ldap.user-password-attribute:}")
  private String ldapUserPasswordAttribute;
  
  @Value("${camunda.bpm.run.ldap.group-search-base:}")
  private String ldapGroupSearchBase;
  
  @Value("${camunda.bpm.run.ldap.group-search-filter:}")
  private String ldapGroupSearchFilter;
  
  @Value("${camunda.bpm.run.ldap.group-id-attribute:}")
  private String ldapGroupIdAttribute;
  
  @Value("${camunda.bpm.run.ldap.group-name-attribute:}")
  private String ldapGroupNameAttribute;
  
  @Value("${camunda.bpm.run.ldap.group-member-attribute:}")
  private String ldapGroupMemberAttribute;
  
  @Value("${camunda.bpm.run.ldap.sort-control-supported:false}")
  private boolean ldapSortControlSupported;
  
  @Value("${camunda.bpm.run.ldap.accept-untrusted-certificates:false}")
  private boolean ldapAcceptUntrustedCertificates;
  
  /**
   * https://docs.cibseven.org/manual/latest/user-guide/process-engine/authorization-service/#the-administrator-authorization-plugin
   * https://docs.cibseven.org/manual/latest/user-guide/cibseven-run/#ldap-administrator-authorization
   */
  @Value("${camunda.bpm.run.admin-auth.administrator-user-name:}")
  private String administratorUserName;
  
  @Value("${camunda.bpm.run.admin-auth.administrator-group-name:}")
  private String administratorGroupName;

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

  @Bean
  public AdministratorAuthorizationPlugin administratorAuthorizationPlugin() {
    AdministratorAuthorizationPlugin adminPlugin = new AdministratorAuthorizationPlugin();
    adminPlugin.setAdministratorGroupName(administratorGroupName);
    adminPlugin.setAdministratorUserName(administratorUserName);
    return adminPlugin;
  }

  @Bean
  public LdapIdentityProviderPlugin ldapIdentityProviderPlugin() {
    LdapIdentityProviderPlugin ldapPlugin = new LdapIdentityProviderPlugin();
    
    // Connection settings
    ldapPlugin.setServerUrl(ldapServerUrl);
    ldapPlugin.setManagerDn(ldapManagerDn);
    ldapPlugin.setManagerPassword(ldapManagerPassword);
    ldapPlugin.setBaseDn(ldapBaseDn);
    
    // User settings
    ldapPlugin.setUserSearchBase(ldapUserSearchBase);
    ldapPlugin.setUserSearchFilter(ldapUserSearchFilter);
    ldapPlugin.setUserIdAttribute(ldapUserIdAttribute);
    ldapPlugin.setUserFirstnameAttribute(ldapUserFirstnameAttribute);
    ldapPlugin.setUserLastnameAttribute(ldapUserLastnameAttribute);
    ldapPlugin.setUserEmailAttribute(ldapUserEmailAttribute);
    ldapPlugin.setUserPasswordAttribute(ldapUserPasswordAttribute);
    
    // Group settings
    ldapPlugin.setGroupSearchBase(ldapGroupSearchBase);
    ldapPlugin.setGroupSearchFilter(ldapGroupSearchFilter);
    ldapPlugin.setGroupIdAttribute(ldapGroupIdAttribute);
    ldapPlugin.setGroupNameAttribute(ldapGroupNameAttribute);
    ldapPlugin.setGroupMemberAttribute(ldapGroupMemberAttribute);
    
    ldapPlugin.setSortControlSupported(ldapSortControlSupported);
    
    // Security settings
    ldapPlugin.setUseSsl(false);
    ldapPlugin.setAcceptUntrustedCertificates(ldapAcceptUntrustedCertificates);
    
    return ldapPlugin;
  }

}