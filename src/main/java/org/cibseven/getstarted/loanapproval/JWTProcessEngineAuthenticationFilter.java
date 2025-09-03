package org.cibseven.getstarted.loanapproval;

import java.io.IOException;

import org.cibseven.bpm.engine.rest.security.auth.AuthenticationProvider;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Response.Status;

public class JWTProcessEngineAuthenticationFilter implements Filter{

	public static final String AUTHENTICATION_PROVIDER_PARAM = "authentication-provider";
	public static final String JWT_SECRET_PATH_PARAM = "jwt-secret-path";
	public static final String UNSECURE_PATTERN_PARAM = "unsecure-pattern";
	public static final String BASIC_PATTERN_PARAM = "basic-pattern";
	private static String jwtSecretPath;
	private static String jwtValidator;
	private static String unsecurePattern;
	private static String basicPattern;
	private static Class<?> jwtValidatorClass;
	
	protected AuthenticationProvider /*JWTAuthenticationProvider*/ authenticationProvider;
	
	public void init(FilterConfig filterConfig) throws ServletException {
		String authenticationProviderClassName = filterConfig.getInitParameter(AUTHENTICATION_PROVIDER_PARAM);
		
		if (jwtSecretPath == null) {
			jwtSecretPath = filterConfig.getInitParameter(JWT_SECRET_PATH_PARAM);
		}
		if (unsecurePattern == null) {
			unsecurePattern = filterConfig.getInitParameter(UNSECURE_PATTERN_PARAM);
		}
		if (basicPattern == null) {
			basicPattern = filterConfig.getInitParameter(BASIC_PATTERN_PARAM);
		}
		
		if (authenticationProviderClassName == null) {
	      throw new ServletException("Cannot instantiate authentication filter: no authentication provider set. init-param " + AUTHENTICATION_PROVIDER_PARAM + " missing");
	    }

	    try {
	      Class<?> authenticationProviderClass = Class.forName(authenticationProviderClassName);
	      authenticationProvider = (AuthenticationProvider) authenticationProviderClass.newInstance();
	    } catch (ClassNotFoundException e) {
	      throw new ServletException("Cannot instantiate authentication filter: authentication provider not found", e);
	    } catch (InstantiationException e) {
	      throw new ServletException("Cannot instantiate authentication filter: cannot instantiate authentication provider", e);
	    } catch (IllegalAccessException e) {
	      throw new ServletException("Cannot instantiate authentication filter: constructor not accessible", e);
	    } catch (ClassCastException e) {
	      throw new ServletException("Cannot instantiate authentication filter: authentication provider does not implement interface " +
	          AuthenticationProvider.class.getName(), e);
	    }		
		
		//authenticationProvider.init(jwtSecretPath);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

	    HttpServletResponse resp = (HttpServletResponse) response;
	    
		//resp.setStatus(Status.UNAUTHORIZED.getStatusCode());
	}
	
}
