<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015/11/13
  Time: 10:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>统一登录</title>
</head>
<body>
<form action="/auth/auth" method="post">
    <%--<input type="hidden" name="action" value="authuser">--%>
    <table>
        <tr>
            <td>name</td>
            <td><input type="text" name="username" value=""/></td>
        </tr>
        <tr>
            <td>password</td>
            <td><input type="password" name="password" value=""/></td>
        </tr>
        <tr>
            <td colspan="2"><input type="submit" value="submit"/></td>
        </tr>
    </table>
</form>
</body>
</html>
