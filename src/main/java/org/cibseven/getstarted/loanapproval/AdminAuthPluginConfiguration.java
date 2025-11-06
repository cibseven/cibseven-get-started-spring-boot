package org.cibseven.getstarted.loanapproval;

import org.cibseven.bpm.engine.impl.plugin.AdministratorAuthorizationPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminAuthPluginConfiguration {

  @Bean
  public AdministratorAuthorizationPlugin administratorAuthorizationPlugin() {
    AdministratorAuthorizationPlugin plugin = new AdministratorAuthorizationPlugin();
    plugin.setAdministratorUserName("demo");
    //plugin.setAdministratorGroupName("camunda-admin");
    return plugin;
  }
}
