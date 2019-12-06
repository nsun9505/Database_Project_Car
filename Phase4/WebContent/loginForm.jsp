<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%
		
    %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script> 
<style type="text/css">
	body{
		color: black;
		background: white;
	}
	.login-form {
		width: 340px;
    	margin: 50px auto;
	}
    .login-form form {
    	margin-bottom: 15px;
        background: #f7f7f7;
        box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
        padding: 30px;
    }
    .login-form h2 {
        margin: 0 0 15px;
    }
    .form-control, .btn {
        min-height: 38px;
        border-radius: 2px;
    }
    .btn {        
        font-size: 15px;
        font-weight: bold;
    }
</style>
<title>로그인</title>
</head>
<body>
<div class="container">
	<br><br><br>
	<h1 style="text-align:center; font: 50px arial">중고차 매매 사이트 로그인</h1>
	<div class="login-form">
    <form action="${contextPath}/account/login.do" method="post">
        <h2 class="text-center" style="color:#63738a">Log in</h2>       
        <div class="form-group">
            <input type="text" class="form-control" name="user_id" placeholder="ID" maxlength="20" required="required">
        </div>
        <div class="form-group">
            <input type="password" class="form-control" name="user_pw" placeholder="PASSWORD" maxlength="20" required="required">
        </div>
        <div class="form-group">
            <input type="submit" class="btn btn-primary btn-block" value="Login">
        </div>        
    </form>
    <p class="text-center"><a href="registerForm.jsp">회원가입</a></p>
	</div>
</div>
</body>
</html>