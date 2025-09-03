package org.cibseven.getstarted.loanapproval;

import java.util.Collections;
import java.util.List;

import org.cibseven.bpm.engine.identity.User;
import org.cibseven.bpm.engine.impl.Page;
import org.cibseven.bpm.engine.impl.UserQueryImpl;
import org.cibseven.bpm.engine.impl.context.Context;
import org.cibseven.bpm.engine.impl.interceptor.CommandContext;
import org.cibseven.bpm.engine.impl.persistence.entity.UserEntity;

public class JWTUserQuery extends UserQueryImpl {

	private static final long serialVersionUID = 1L;

	public JWTUserQuery() {
        super(Context.getProcessEngineConfiguration().getCommandExecutorTxRequired());
    }

    @Override
    public long executeCount(CommandContext commandContext) {
        return 1L; // pretend there’s always 1 user
    }

    @Override
    public List<User> executeList(CommandContext commandContext, Page page) {
        UserEntity demo = new UserEntity();
        demo.setId("demo");
        demo.setFirstName("JWT");
        demo.setLastName("User");
        demo.setEmail("demo@example.com");

        return Collections.singletonList(demo);
    }
}