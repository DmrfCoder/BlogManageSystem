<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/5/12
  Time: 0:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>登录</title>
</head>
<link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="css/user/user_normal.css">
<link rel="stylesheet" href="css/button.css">
<%

%>

<body>
<div class="container">
    <form:form action="admin/loginP" class="form-login">
        <div class="panel panel-default">
            <div class="panel-heading">
                <div class="row">
                    <div class="col-md-5"><h2>登录</h2></div>
                    <div class="col-md-5"></div>
                    <div class="col-md-2"><br>
                        <a  href="/admin/signupP" type="button" class="button button-primary button-circle" >SignUp</a>
                    </div>
                </div>
            </div>
            <div class="panel-body">
                <label for="username">用户名</label>
                <input type="text" id="username" class="form-control" placeholder="Username" name="username" required autofocus>
                <br>
                <label for="password">密码</label>
                <input type="password" id="password" class="form-control" placeholder="Password" name="password" required>
                <br>
                <div class="checkbox">
                    <label>
                        <input type="checkbox" value="remember-me" id="remb"> Remember Password
                    </label>
                </div>
                <br>
                <button  type="submit"  class="btn btn-lg btn-primary btn-block" id="loginBtn">LogIn</button>
            </div>
        </div>
    </form:form>

</div>

</body>
</html>
