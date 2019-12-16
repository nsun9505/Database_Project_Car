<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%
    	String checkId = (String)request.getAttribute("checkOkId");
    %>
    
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <c:set var="contextPath" value="${pageContext.request.contextPath}"/>
    <c:set var="userInfo" value="${sessionScope.userInfo }"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>회원가입</title>
<script type="text/javascript" src="<c:url value="/resource/js/jquery-3.4.1.js"/>"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${contextPath }/login/registerValidCheck.js" charset="UTF-8"></script>
<style  type="text/css">
	body{
		color: black;
		background: white;
		font-family: 'Roboto', sans-serif;
	}
    .form-control{
		height: 40px;
		box-shadow: none;
		color: #969fa4;
	}
	.form-control:focus{
		border-color: #5cb85c;
	}
    .form-control, .btn{        
        border-radius: 3px;
    }
	.signup-form{
		width: 500px;
		margin: 0 auto;
		padding: 30px 0;
	}
	.signup-form h2{
		color: #636363;
        margin: 0 0 15px;
		position: relative;
		text-align: center;
    }
	.signup-form h2:before, .signup-form h2:after{
		content: "";
		height: 2px;
		width: 30%;
		background: #d4d4d4;
		position: absolute;
		top: 50%;
		z-index: 2;
	}	
	.signup-form h2:before{
		left: 0;
	}
	.signup-form h2:after{
		right: 0;
	}
    .signup-form .hint-text{
		color: #999;
		margin-bottom: 30px;
		text-align: center;
	}
    .signup-form form{
		color: #999;
		border-radius: 3px;
    	margin-bottom: 15px;
        background: #f2f3f7;
        box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
        padding: 30px;
    }
	.signup-form .form-group{
		margin-bottom: 20px;
	}
	.signup-form input[type="checkbox"]{
		margin-top: 3px;
	}
	.signup-form .btn{        
        font-size: 16px;
        font-weight: bold;		
		min-width: 140px;
        outline: none !important;
    }
	.signup-form .row div:first-child{
		padding-right: 10px;
	}
	.signup-form .row div:last-child{
		padding-left: 10px;
	}    	
    .signup-form a{
		color: black;
		text-decoration: underline;
	}
    .signup-form a:hover{
		text-decoration: none;
	}
	.signup-form form a{
		color: #5cb85c;
		text-decoration: none;
	}	
	.signup-form form a:hover{
		text-decoration: underline;
	}  
</style>
</head>
<body>
<div class="signup-form">
	<form action="${contextPath}/account/register.do" method="post" name="joinForm">
		<h2>회원가입</h2>
		<p class="hint-text" style="font-size:10px;">아이디 : 5~15자의 영문 대소문자와 숫자로만 입력</p>
        <div class="form-group">
			<div class="row">
			<%if(checkId == null){ %>
				<div class="col-xs-6"><input type="text" class="form-control" id="user_id" name="id" value="" placeholder="ID" required="required" maxlength="14"></div>
				<div class="col-xs-6"><input type="button" class="form-control" onclick="return idDuplicateCheck()" value="중복확인"></div>
			<%} else{ %>
				<div class="col-xs-6"><input type="text" class="form-control" id="user_id" name="id" value="<%=checkId%>" placeholder="아이디" required="required" maxlength="14" readOnly></div>
				<div class="col-xs-6"><input type="button" class="form-control" onclick="return idDuplicateCheck()" value="중복확인" disabled="disabled"></div>
			<% } %>
			</div>        	
        </div>
        <div class="form-group">
        		<input type="text" class="form-control" id="fname" name="first_name" placeholder="FirstName (4~10)" required="required" maxlength="10">
        </div>
        <div class="form-group">
			<input type="text" class="form-control" id="lname" name="last_name" placeholder="LastName (4~10)" required="required" maxlength="10">
        </div>
		<div class="form-group">
            <input type="password" class="form-control" id="user_pw" name="password" placeholder="Password" required="required" maxlength="15">
        </div>
		<div class="form-group">
            <input type="password" class="form-control" id="user_pwCheck" name="passwordCheck" placeholder="Password confirm" required="required" maxlength="15">
        </div>
        <div class="form-group">
            <input type="text" class="form-control" id="user_phone" name="phoneNumber" placeholder="Phone(Format : XXX-XXX-XXXX OR XXX-XXXX-XXXX)" maxlength="13" required="required">
        </div>
        <div class="form-group">
            <input type="text" class="form-control" id="user_address" name="address" placeholder="address" maxlength="50">
        </div>
        <div class="form-group">
        	<input type="date" class="form-control" id="user_birth" name="birthDate" min="1900-01-01" max="2000-12-31">  	
        </div>        
		<div class="form-group">
			<div class="row">
				<div class="col-xs-6">
					<input type="text" value="Gender" class="form-control" readOnly>
				</div>
				<div class="col-xs-6">
					<select name="gender" class="form-control">
						<option value="" selected>None</option>
						<option value="F">Female</option>
						<option value="M">Male</option>
					</select>
				</div>
			</div>
		</div>
		<div class="form-group">
			<div class="row">
				<div class="col-xs-6">
					<input type="text" value="JOB" class="form-control" readOnly>
				</div>
				<div class="col-xs-6">
					<select name="job" class="form-control">
						<option value="None" selected>None</option>
						<option value="Student">Student</option>
						<option value="Profession">Profession</option>
						<option value="Self-employed">Self-employed</option>
						<option value="Other">Other</option>
					</select>
				</div>
			</div>
		</div>
		<div class="form-group">
            <input type="button" class="btn btn-success btn-lg btn-block" value="register" onclick="return checkInfo()">
        </div>
       	<c:choose>
       		<c:when test="${userInfo.account_type == null || userInfo.account_type eq 'C'}">
       			<input type="hidden" name="account_type" value="C">
       		</c:when>
       		<c:otherwise>
       			<input type="hidden" name="account_type" value="A">
       		</c:otherwise>
       	</c:choose>
    </form>
	<div class="text-center">already account : <a href="${contextPath}/index.jsp">login window</a></div>
</div>
</body>
</html>