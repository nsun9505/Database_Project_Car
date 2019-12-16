<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%
    	String checkId = (String)request.getAttribute("checkOkId");
    	String account_type = (String)session.getAttribute("account_type");
    	//String account_type = (String)session.getAttribute("account_type");
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
<script type="text/javascript" src="${contextPath }/modify/modifyValidCheck.js" charset="UTF-8"></script>
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
<c:choose>
<c:when test="${userInfo eq null }">
	<script>
		alert("먼저 로그인을 해주세요.");
	</script>
</c:when>
<c:otherwise>
<div class="signup-form">
	<form action="${contextPath}/account/modifyUserInfo.do" method="post" name="modifyForm">
		<h2>회원가입</h2>
		<p class="hint-text" style="font-size:10px;">아이디 : 5~15자의 영문 대소문자와 숫자로만 입력</p>
        <div class="form-group">
        	<label for="user_id">ID</label>
			<input type="text" class="form-control" id="user_id" name="id" value="${userInfo.id }" readonly>       	
        </div>
        <div class="form-group">
        	<label for="name">Name (FirstName LastName[각각  영어 10자 이내])</label>
        	<input type="text" class="form-control" id="user_name" name="name" value="${userInfo.name }" placeholder="Name(FirstName Lastname[각각  영어 10자 이내])" required="required" maxlength="20">
        </div>
		<div class="form-group">
			<label for="user_pw">Password</label>
            <input type="password" class="form-control" id="user_pw" name="password" value="${userInfo.pw }" placeholder="Password" required="required" maxlength="15">
        </div>
		<div class="form-group">
			<label for="user_pwCheck">Password Confirm</label>
            <input type="password" class="form-control" id="user_pwCheck" name="passwordCheck" value="${userInfo.pw }" placeholder="Password confirm" required="required" maxlength="15">
        </div>
        <div class="form-group">
        <label for="user_phone">Phone Number</label>
        <c:choose>
        	<c:when test="${userInfo.phone_num eq null }">
            	<input type="text" class="form-control" id="user_phone" name="phoneNumber" placeholder="Phone(Format : XXX-XXX-XXXX OR XXX-XXXX-XXXX)" maxlength="13" required="required">
			</c:when>
        	<c:otherwise>
            	<input type="text" class="form-control" id="user_phone" name="phoneNumber" value="${userInfo.phone_num }" placeholder="Phone(Format : XXX-XXX-XXXX OR XXX-XXXX-XXXX)" maxlength="13" required="required">
        	</c:otherwise>
        </c:choose>
        </div>
       	<div class="form-group">
       	<label for="user_address">Address</label>
        <c:choose>
        	<c:when test="${userInfo.address eq null}">
         		   <input type="text" class="form-control" id="user_address" name="address" placeholder="address" maxlength="50">
        	</c:when>
        	<c:otherwise>
            		<input type="text" class="form-control" id="user_address" name="address" value="${userInfo.address}" placeholder="address" maxlength="50">
        	</c:otherwise>
        </c:choose>
        </div>
        <div class="form-group">
        <label for="user_date">Birth Date</label>
        <c:choose>
        	<c:when test="${userInfo.birth_date eq null}">
        			<input type="date" class="form-control" id="user_birth" name="birthDate" min="1900-01-01" max="2000-12-31">  	
        	</c:when>
        	<c:otherwise>
        			<input type="date" class="form-control" id="user_birth" value="${userInfo.birth_date.toString()}" name="birthDate" min="1900-01-01" max="2000-12-31">  	
        	</c:otherwise>
        </c:choose>
    	</div>        
		<div class="form-group">
			<div class="row">
				<div class="col-xs-6">
					<input type="text" value="Gender" class="form-control" readOnly>
				</div>
				<div class="col-xs-6">
					<select name="gender" id="user_gender" class="form-control">
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
					<select name="job" id="user_job" class="form-control">
						<option value="None" selected>None</option>
						<option value="Student">Student</option>
						<option value="Profession">Profession</option>
						<option value="Self-employed">Self-employed</option>
						<option value="Other">Other</option>
					</select>
				</div>
			</div>
		</div>
        <input type="hidden" name="account_type" value="${userInfo.account_type}">
        <div class="form-group" style="text-align:right">
        	<input type="button" class="btn btn-default" value="회원 탈퇴" onclick="goodBye()">
        	<input type="button" class="btn btn-default" value="수정" onclick="validCheck()">
        	<input type="button" class="btn btn-default" value="취소" onclick="cancel()">
        </div>
    </form>
</div>
<script>
	var job = document.getElementById("user_job");
	for(var i=0; i<job.options.length; i++){
		if(job.options[i].value == '<c:out value="${userInfo.job}"/>'){
			job.options[i].selected = true;
			break;
		}
	}
	var gender = document.getElementById("user_gender");
	for(var i=0; i<gender.options.length; i++){
		if(gender.options[i].value == '<c:out value="${userInfo.sex}"/>'){
			gender.options[i].selected = true;
			break;
		}
	}
</script>
</c:otherwise>
</c:choose>
</body>
</html>