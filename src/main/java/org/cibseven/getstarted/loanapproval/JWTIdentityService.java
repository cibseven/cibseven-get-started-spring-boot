package org.cibseven.getstarted.loanapproval;

import org.cibseven.bpm.engine.identity.NativeUserQuery;
import org.cibseven.bpm.engine.identity.User;
import org.cibseven.bpm.engine.identity.UserQuery;
import org.cibseven.bpm.engine.impl.identity.ReadOnlyIdentityProvider;
import org.cibseven.bpm.engine.impl.interceptor.CommandContext;


import org.cibseven.bpm.engine.identity.Group;
import org.cibseven.bpm.engine.identity.Tenant;
import org.cibseven.bpm.engine.identity.GroupQuery;
import org.cibseven.bpm.engine.identity.TenantQuery;

import org.cibseven.bpm.engine.impl.interceptor.Session;

// optional: internal implementation classes you may return where appropriate

import org.cibseven.bpm.engine.impl.TenantQueryImpl;
import org.cibseven.bpm.engine.impl.NativeUserQueryImpl;
import org.cibseven.bpm.engine.impl.Page;
import org.cibseven.bpm.engine.impl.persistence.entity.UserEntity;
import org.cibseven.bpm.engine.impl.persistence.entity.GroupEntity;
import org.cibseven.bpm.engine.impl.persistence.entity.TenantEntity;
import org.cibseven.bpm.engine.impl.context.Context;

public class JWTIdentityService implements ReadOnlyIdentityProvider  {

	  private final String restUrl;
	  private final String clientId;
	  private final String clientSecret;

	  public JWTIdentityService(String restUrl, String clientId, String clientSecret) {
	    this.restUrl = restUrl;
	    this.clientId = clientId;
	    this.clientSecret = clientSecret;
	  }

	  @Override
	  public User findUserById(String userId) {
	    return null;
	  }

	  @Override
	  public boolean checkPassword(String userId, String password) {
	    return true;
	  }

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public UserQuery createUserQuery() {
        return new JWTUserQuery();
	}

	@Override
	public UserQuery createUserQuery(CommandContext commandContext) {
        return new JWTUserQuery();
	}

	@Override
	public NativeUserQuery createNativeUserQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Group findGroupById(String groupId) {
		return null;
	}

	@Override
	public GroupQuery createGroupQuery() {
		return new JWTGroupQuery();
	}

	@Override
	public GroupQuery createGroupQuery(CommandContext commandContext) {
		return new JWTGroupQuery();
	}

	@Override
	public Tenant findTenantById(String tenantId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TenantQuery createTenantQuery() {
		return new JWTTenantQuery();
	}

	@Override
	public TenantQuery createTenantQuery(CommandContext commandContext) {
		return new JWTTenantQuery();
	}
}

