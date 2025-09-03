package org.cibseven.getstarted.loanapproval;

import java.util.List;

import org.cibseven.bpm.engine.identity.Group;
import org.cibseven.bpm.engine.impl.GroupQueryImpl;
import org.cibseven.bpm.engine.impl.Page;
import org.cibseven.bpm.engine.impl.interceptor.CommandContext;
import org.cibseven.bpm.engine.impl.persistence.entity.GroupEntity;

public class JWTGroupQuery extends GroupQueryImpl {

	private static final long serialVersionUID = 1342L;

	@Override
	public long executeCount(CommandContext commandContext) {
		return 1L; // pretend there’s always 1 group
	}

	@Override
	public List<Group> executeList(CommandContext commandContext, Page page) {
		GroupEntity demo = new GroupEntity();		
		demo.setId("managers");
		demo.setName("Managers");
		demo.setType("security-role");
		return List.of(demo);	
	}

}
