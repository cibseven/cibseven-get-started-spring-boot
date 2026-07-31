/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cibseven.getstarted.loanapproval;

import java.util.concurrent.atomic.AtomicInteger;

import org.cibseven.bpm.engine.delegate.DelegateExecution;
import org.cibseven.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Fails on purpose for the first {@link #FAILURES_BEFORE_SUCCESS} executions so the retry
 * behaviour of an asynchronous service task can be observed: the job executor rolls the
 * transaction back, decrements the retries and re-runs the delegate according to the
 * failedJobRetryTimeCycle configured on the task.
 */
@Component("failingDelegate")
public class FailingDelegate implements JavaDelegate {

  private static final Logger LOGGER = LoggerFactory.getLogger(FailingDelegate.class);

  private static final int FAILURES_BEFORE_SUCCESS = 2;

  private final AtomicInteger attempts = new AtomicInteger();

  @Override
  public void execute(DelegateExecution execution) throws Exception {
    int attempt = attempts.incrementAndGet();

    if (attempt <= FAILURES_BEFORE_SUCCESS) {
      LOGGER.warn("Attempt {} on activity {} fails on purpose", attempt, execution.getCurrentActivityId());
      throw new IllegalStateException("Deliberate failure on attempt " + attempt);
    }

    LOGGER.info("Attempt {} succeeded for process instance {}", attempt, execution.getProcessInstanceId());
  }

}
