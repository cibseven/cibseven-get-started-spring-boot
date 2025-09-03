package org.cibseven.getstarted.loanapproval;

import org.cibseven.bpm.engine.impl.identity.ReadOnlyIdentityProvider;
import org.cibseven.bpm.engine.impl.interceptor.Session;
import org.cibseven.bpm.engine.impl.interceptor.SessionFactory;

public class JWTIdentityProviderFactory implements SessionFactory {

	  private final String restUrl;
	  private final String clientId;
	  private final String clientSecret;

	  public JWTIdentityProviderFactory(String restUrl, String clientId, String clientSecret) {
	    this.restUrl = restUrl;
	    this.clientId = clientId;
	    this.clientSecret = clientSecret;
	  }

	  @Override
	  public Class<?> getSessionType() {
	    return ReadOnlyIdentityProvider.class;
	  }

	  @Override
	  public Session openSession() {
	    return new JWTIdentityService(restUrl, clientId, clientSecret);
	  }
	}


