<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%
    	String checkId = (String)request.getAttribute("checkOkId");
    	//String account_type = (String)session.getAttribute("account_type");
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>회원가입</title>
<script type="text/javascript" src="<c:url value="/resource/js/jquery-3.4.1.js"/>"></script>
<script language="JavaScript" src="MemberCheckValid.js" charset="UTF-8"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<style type="text/css">
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
	<form action="/account/register.do" method="post" name="joinForm">
		<h2>회원가입</h2>
		<p class="hint-text" style="font-size:10px;">아이디, 비밀번호 : 5~15자의 영문 대소문자와 숫자로만 입력</p>
        <div class="form-group">
			<div class="row">
			<%if(checkId == null){ %>
				<div class="col-xs-6"><input type="text" class="form-control" id="user_id" name="id" placeholder="아이디" required="required" maxlength="14"></div>
				<div class="col-xs-6"><input type="button" class="form-control" onclick="return checkDupId()" value="중복확인"></div>
			<%} else{ %>
				<div class="col-xs-6"><input type="text" class="form-control" id="user_id" name="id" value="<%=checkId%>" placeholder="아이디" required="required" maxlength="14" readOnly></div>
				<div class="col-xs-6"><input type="button" class="form-control" onclick="return checkDupId()" value="중복확인" disabled="disabled"></div>
			<% } %>
			</div>        	
        </div>
        <div class="form-group">
        	<div class="row">
        		<div class="col-xs-6"><input type="text" class="form-control" id="fname" name="first_name" placeholder="First Name" required="required" maxlength="10"></div>
				<div class="col-xs-6"><input type="text" class="form-control" id="lname" name="last_name" placeholder="Last Name" required="required" maxlength="10"></div>
			</div>
        </div>
		<div class="form-group">
            <input type="password" class="form-control" id="user_pw" name="pw" placeholder="비밀번호" required="required" maxlength="15">
        </div>
		<div class="form-group">
            <input type="password" class="form-control" id="user_pwCheck" name="pwck" placeholder="비밀번호 확인" required="required" maxlength="15">
        </div>
        <div class="form-group">
            <input type="text" class="form-control" id="user_phone" name="phoneNumber" placeholder="핸드폰 번호(형식 : XXX-XXX-XXXX OR XXX-XXXX-XXXX)" maxlength="13" required="required">
        </div>
        <div class="form-group">
            <input type="text" class="form-control" id="user_pwCheck" name="pwck" placeholder="주소" maxlength="50">
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
						<option value="None" checked>None</option>
						<option value="F">Female</option>
						<option value="M">Male</option>
					</select>
				</div>
			</div>
		</div>
		<div class="form-group">
            <input type="button" class="btn btn-success btn-lg btn-block" value="회원가입" onclick="return checkInfo()">
        </div>
<!--        <input type="hidden" value="<%=account_type %>"> -->
    </form>
	<div class="text-center">이미 계정이 있나요? <a href="loginForm.html">로그인 창 가기</a></div>
</div>
</body>
</html>