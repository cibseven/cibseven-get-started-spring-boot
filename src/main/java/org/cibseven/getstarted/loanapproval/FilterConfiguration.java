package org.cibseven.getstarted.loanapproval;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

	@Bean
	public FilterRegistrationBean<HttpHeaderSecurityFilter> cspFilter() {
	    FilterRegistrationBean<HttpHeaderSecurityFilter> bean = new FilterRegistrationBean<>();
	    bean.setFilter(new HttpHeaderSecurityFilter());
	    bean.addUrlPatterns("/*");
	    return bean;
	}
}