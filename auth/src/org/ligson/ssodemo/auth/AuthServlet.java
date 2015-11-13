package org.ligson.ssodemo.auth;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by ligson on 2015/11/13.
 */
public class AuthServlet extends javax.servlet.http.HttpServlet {
    private static ConcurrentMap<String, String> users = new ConcurrentHashMap<String, String>();
    private static ConcurrentMap<String, String> ssoIds = new ConcurrentHashMap<String, String>();

    @Override
    public void init() throws ServletException {
        users.put("ligson1", "password");
        users.put("ligson2", "password");
    }

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
        String result = "failed";
        if (action == null) {
            handlerFromLogin(request, response);
        } else if (action.equals("authcookie")) {
            String myCookie = request.getParameter("cookiename");
            if (myCookie != null) result = authCookie(myCookie);
            out.print(result);
            out.close();
        } else if (action.equals("authuser")) {
            result = authNameAndPasswd(request, response);
            out.print(result);
            out.close();
        } else if (action.equals("logout")) {
            String myCookie = request.getParameter("cookiename");
            logout(myCookie);
            out.close();
        }
    }

    private void handlerFromLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String pass = (String) users.get(username);
        if ((pass == null) || (!pass.equals(password)))
            getServletContext().getRequestDispatcher("/failed.html").forward(request, response);
        else {
            String gotoURL = request.getParameter("goto");
            String newID = UUID.randomUUID().toString();
            ssoIds.put(newID, username);
            Cookie wangyu = new Cookie("sankaikey", newID);
            wangyu.setDomain(".kanglaohui.com");
            wangyu.setMaxAge(60000);
            wangyu.setValue(newID);
            wangyu.setPath("/");
            response.addCookie(wangyu);
            System.out.println("login success, goto back url:" + gotoURL);
            if (gotoURL != null) {
                PrintWriter out = response.getWriter();
                response.sendRedirect(gotoURL);
                out.close();
            }
        }
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doPost(request, response);
    }


    public static String authCookie(String value) {
        String result = (String) ssoIds.get(value);
        if (result == null) {
            result = "failed";
            System.out.println("Authentication failed!");
        } else {
            System.out.println("Authentication success!");
        }
        return result;
    }

    public static String authUserAndPass(String username, String password) {
        String pass = (String) users.get(username);
        if ((pass == null) || (!pass.equals(password))) {
            return "failed";
        }
        String newID = UUID.randomUUID().toString();
        ssoIds.put(newID, username);
        return username;
    }

    protected String authNameAndPasswd(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String pass = (String) users.get(username);
        if ((pass == null) || (!pass.equals(password))) {
            return "failed";
        }
        String newID = UUID.randomUUID().toString();
        ssoIds.put(newID, username);
        return newID;
    }

    private void logout(String UID) {
        System.out.println("Logout for " + UID);
        ssoIds.remove(UID);
    }

}
