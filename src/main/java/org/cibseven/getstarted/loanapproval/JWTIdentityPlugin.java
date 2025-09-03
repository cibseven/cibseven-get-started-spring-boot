package org.cibseven.getstarted.loanapproval;

import org.cibseven.bpm.engine.ProcessEngine;
import org.cibseven.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.cibseven.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JWTIdentityPlugin implements ProcessEnginePlugin {

	  private String restUrl;
	  private String clientId;
	  private String clientSecret;
	  
	  @Override
	  public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
	    JWTIdentityProviderFactory factory = new JWTIdentityProviderFactory(restUrl, clientId, clientSecret);
	    processEngineConfiguration.setIdentityProviderSessionFactory(factory);
	  }

	  @Override
	  public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {}

	  @Override
	  public void postProcessEngineBuild(ProcessEngine processEngine) {}
	  
	  public void setRestUrl(String restUrl) {
	      this.restUrl = restUrl;
	  }
	
	  public void setClientId(String clientId) {
	      this.clientId = clientId;
	  }
	
	  public void setClientSecret(String clientSecret) {
		  this.clientSecret = clientSecret;
	  }
}