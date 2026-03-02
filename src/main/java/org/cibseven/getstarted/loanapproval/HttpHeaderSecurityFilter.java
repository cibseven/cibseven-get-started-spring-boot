package org.cibseven.getstarted.loanapproval;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
 
public class HttpHeaderSecurityFilter implements Filter {

   @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse res = (HttpServletResponse) response;

        res.setHeader("Content-Security-Policy",
            "base-uri 'none';"
            + "font-src 'self' data:;"
            + "script-src 'self' 'unsafe-eval';"
            + "style-src 'self' 'unsafe-inline';"
            + "default-src 'self';"
            + "img-src 'self' data: blob:;"
            + "connect-src 'self' https://sso-test.cib.de;"
            + "worker-src 'self';"
            + "form-action 'self' https://sso-test.cib.de;"
            + "frame-ancestors 'self';"
            + "object-src 'none';"
        );

        chain.doFilter(request, response);
    }

}
