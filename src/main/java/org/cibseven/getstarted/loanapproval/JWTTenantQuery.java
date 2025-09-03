package org.cibseven.getstarted.loanapproval;

import java.util.List;

import org.cibseven.bpm.engine.identity.Tenant;
import org.cibseven.bpm.engine.impl.Page;
import org.cibseven.bpm.engine.impl.TenantQueryImpl;
import org.cibseven.bpm.engine.impl.interceptor.CommandContext;
import org.cibseven.bpm.engine.impl.persistence.entity.TenantEntity;

public class JWTTenantQuery extends TenantQueryImpl {

	private static final long serialVersionUID = 11232L;

	@Override
	public long executeCount(CommandContext commandContext) {
		return 0;
	}

	@Override
	public List<Tenant> executeList(CommandContext commandContext, Page page) {
		TenantEntity demo = new TenantEntity();
		demo.setId("tenant1");
		demo.setName("Tenant 1");
		return List.of(demo);
	}

}
