package org.ligson.ssodemo.demo1;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.rmi.server.ExportException;

/**
 * Created by ligson on 2015/11/13.
 */
public class LoginFilter implements javax.servlet.Filter {
    private FilterConfig filterConfig = null;
    private String cookieName = "WangYuDesktopSSOID";
    private String SSOServiceURL = "http://auth.kanglaohui.com/auth/auth";
    private String SSOLoginPage = "http://auth.kanglaohui.com/auth/login.jsp";
    private static final boolean debug = true;

    public void destroy() {
    }

    public void doFilter(javax.servlet.ServletRequest req, javax.servlet.ServletResponse resp, javax.servlet.FilterChain chain) throws javax.servlet.ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String result = "failed";
        String url = request.getRequestURL().toString();
        String qstring = request.getQueryString();
        if (qstring == null) {
            qstring = "";
        }
        String cookieValue = "";
        Cookie[] diskCookies = request.getCookies();
        if (diskCookies != null) {
            for (int i = 0; i < diskCookies.length; i++) {
                if (diskCookies[i].getName().equals("sankaikey")) {
                    cookieValue = diskCookies[i].getValue();
                    result = SSOService(cookieValue);
                }
            }
        }
        if (result.equals("failed")) {
            response.sendRedirect(this.SSOLoginPage + "?goto=" + url);
        } else if (qstring.indexOf("logout") > 1) {
            //logoutService(cookieValue);
            response.sendRedirect(this.SSOLoginPage + "?goto=" + url);
        } else {
            request.setAttribute("SSOUser", result);
            Throwable problem = null;
            try {
                chain.doFilter(req, resp);
            } catch (Throwable t) {
                problem = t;
                t.printStackTrace();
            }
            if (problem != null) {
                if ((problem instanceof ServletException)) {
                    throw ((ServletException) problem);
                }
                if ((problem instanceof IOException)) {
                    throw ((IOException) problem);
                }
                //sendProcessingError(problem, resp);
            }
        }
    }

    public void init(javax.servlet.FilterConfig config) throws javax.servlet.ServletException {

    }

    private String SSOService(String cookievalue)
            throws IOException {
        String authAction = "?action=authcookie&cookiename=";
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(this.SSOServiceURL + authAction + cookievalue);

        try {

            HttpResponse result = httpclient.execute(get);
            return EntityUtils.toString(result.getEntity());
        } catch (ExportException e) {
            e.printStackTrace();
        }
        return null;
    }


}
