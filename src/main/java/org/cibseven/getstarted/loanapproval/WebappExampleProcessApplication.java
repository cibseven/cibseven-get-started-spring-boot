/*
 * Copyright CIB software GmbH and/or licensed to CIB software GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. CIB software licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.cibseven.getstarted.loanapproval;

import static org.cibseven.bpm.engine.authorization.Resources.APPLICATION;
import static org.cibseven.bpm.engine.authorization.Resources.AUTHORIZATION;
import static org.cibseven.bpm.engine.authorization.Resources.DEPLOYMENT;
import static org.cibseven.bpm.engine.authorization.Resources.FILTER;
import static org.cibseven.bpm.engine.authorization.Resources.GROUP;
import static org.cibseven.bpm.engine.authorization.Resources.PROCESS_DEFINITION;
import static org.cibseven.bpm.engine.authorization.Resources.PROCESS_INSTANCE;
import static org.cibseven.bpm.engine.authorization.Resources.DECISION_DEFINITION;
import static org.cibseven.bpm.engine.authorization.Resources.TASK;
import static org.cibseven.bpm.engine.authorization.Resources.USER;

import org.cibseven.bpm.engine.ProcessEngine;
import org.cibseven.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.cibseven.bpm.spring.boot.starter.event.PostDeployEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;

@SpringBootApplication(exclude = { org.cibseven.bpm.spring.boot.starter.CamundaBpmAutoConfiguration.class })
@EnableProcessApplication
public class WebappExampleProcessApplication {

	@Autowired
	@Qualifier("processEngineDefault")
	private ProcessEngine processEngineDefault;

	@Autowired
	@Qualifier("processEngineTenant1")
	private ProcessEngine processEngine1;

	@Autowired
	@Qualifier("processEngineTenant2")
	private ProcessEngine processEngine2;

	@Autowired
	@Qualifier("processEngineTenant3")
	private ProcessEngine processEngine3;

	@EventListener
	public void processPostDeploy(PostDeployEvent event) {
		
		createDemoUser(processEngineDefault);
		createDemoUser(processEngine1);
		createDemoUser(processEngine2);
		createDemoUser(processEngine3);
		
		processEngineDefault.getRuntimeService().startProcessInstanceByKey("loanApproval");
//		processEngine1.getRuntimeService().startProcessInstanceByKey("loanApproval");
//		processEngine2.getRuntimeService().startProcessInstanceByKey("loanApproval");
//		processEngine3.getRuntimeService().startProcessInstanceByKey("loanApproval");
		
	}

	private void createDemoUser(ProcessEngine processEngine) {
		
		var identityService = processEngine.getIdentityService();
		var authorizationService = processEngine.getAuthorizationService();
		var filterService = processEngine.getFilterService();

		// Check if user already exists
		if (identityService.createUserQuery().userId("demo").count() == 0) {
			var user = identityService.newUser("demo");
			user.setPassword("demo");
			user.setFirstName("Demo");
			user.setLastName("Demo");
			identityService.saveUser(user);
		}

		// Create camunda-admin group if it doesn't exist
		if (identityService.createGroupQuery().groupId("camunda-admin").count() == 0) {
			var group = identityService.newGroup("camunda-admin");
			group.setName("camunda BPM Administrators");
			group.setType("SYSTEM");
			identityService.saveGroup(group);
		}

		// Add user to camunda-admin group
		if (identityService.createUserQuery().userId("demo").memberOfGroup("camunda-admin").count() == 0) {
			identityService.createMembership("demo", "camunda-admin");
		}
		// Create global authorizations for camunda-admin group
		createGroupAuthorization(authorizationService, APPLICATION);
		createGroupAuthorization(authorizationService, USER);
		createGroupAuthorization(authorizationService, GROUP);
		createGroupAuthorization(authorizationService, AUTHORIZATION);
		createGroupAuthorization(authorizationService, FILTER);
		createGroupAuthorization(authorizationService, PROCESS_DEFINITION);
		createGroupAuthorization(authorizationService, PROCESS_INSTANCE);
		createGroupAuthorization(authorizationService, DECISION_DEFINITION);
		createGroupAuthorization(authorizationService, TASK);
		createGroupAuthorization(authorizationService, DEPLOYMENT);
		
		// Create "All tasks" filter
	    if (filterService.createFilterQuery().filterName("All tasks").count() == 0) {
	        var filter = filterService.newTaskFilter()
	            .setName("All tasks")
	            .setOwner("demo")
	            .setQuery(processEngine.getTaskService().createTaskQuery());
	        filterService.saveFilter(filter);
	    }
	}

	private void createGroupAuthorization(org.cibseven.bpm.engine.AuthorizationService authorizationService,
			org.cibseven.bpm.engine.authorization.Resource resource) {
		var existingAuth = authorizationService.createAuthorizationQuery().groupIdIn("camunda-admin")
				.resourceType(resource).resourceId(org.cibseven.bpm.engine.authorization.Authorization.ANY)
				.singleResult();

		if (existingAuth == null) {
			var auth = authorizationService
					.createNewAuthorization(org.cibseven.bpm.engine.authorization.Authorization.AUTH_TYPE_GRANT);
			auth.setGroupId("camunda-admin");
			auth.setResource(resource);
			auth.setResourceId(org.cibseven.bpm.engine.authorization.Authorization.ANY);
			auth.addPermission(org.cibseven.bpm.engine.authorization.Permissions.ALL);
			authorizationService.saveAuthorization(auth);
		}
	}

	public static void main(String... args) {
		SpringApplication.run(WebappExampleProcessApplication.class, args);
	}

}