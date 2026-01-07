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

import org.cibseven.bpm.engine.rest.security.auth.ProcessEngineAuthenticationFilter;
import org.springframework.boot.autoconfigure.web.servlet.JerseyApplicationPath;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class FilterConfiguration {

    @Bean
    // Composite Authentication Filter with Jwt Token and Http Basic
    public FilterRegistrationBean<ProcessEngineAuthenticationFilter> AuthenticationFilter(JerseyApplicationPath applicationPath) {
    	FilterRegistrationBean<ProcessEngineAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setName("cibseven-composite-auth");
    	registrationBean.setFilter(new ProcessEngineAuthenticationFilter());
        registrationBean.setOrder(10);// Order of execution if multiple filters

        String restApiPathPattern = applicationPath.getPath();
        
        // Apply to all URLs - whitelist logic in the filter handlers exclusions
        String[] urlPatterns = new String[] { addUrl(restApiPathPattern, "/*") };

    	// Enable async support
        registrationBean.setAsyncSupported(true);
        // Init parameters
        registrationBean.addInitParameter(
            "authentication-provider",
            org.cibseven.bpm.engine.rest.security.auth.impl.CompositeAuthenticationProvider.class.getName()
        );

        registrationBean.addUrlPatterns(urlPatterns);
        return registrationBean;
    }
    
    private String addUrl(String base, String extend) {
  	  return (base + extend).replaceFirst("^(\\/+|([^/]))", "/$2");
    }
}