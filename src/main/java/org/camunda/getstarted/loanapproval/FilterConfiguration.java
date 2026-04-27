package org.camunda.getstarted.loanapproval;

import org.camunda.bpm.engine.rest.security.auth.ProcessEngineAuthenticationFilter;
import org.springframework.boot.autoconfigure.web.servlet.JerseyApplicationPath;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.camunda.community.process_test_coverage.junit4.platform7.rules.TestCoverageProcessEngineRuleBuilder;
import org.camunda.community.process_test_coverage.junit4.platform7.rules.TestCoverageProcessEngineRule;


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
            org.camunda.bpm.engine.rest.security.auth.impl.HttpBasicAuthenticationProvider.class.getName()
        );

        registrationBean.addUrlPatterns(urlPatterns);
        return registrationBean;
    }
    
    private String addUrl(String base, String extend) {
  	  return (base + extend).replaceFirst("^(\\/+|([^/]))", "/$2");
    }
}