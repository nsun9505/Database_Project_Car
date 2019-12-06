<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
	<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<%
		String isLogon = (String)session.getAttribute("isLogon");
		String user_id = (String)session.getAttribute("user_id");
	%>
	
    <c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html lang="en">
<head>
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">


<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
	integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
	crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
	integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
	crossorigin="anonymous"></script>
<script
	src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
	integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
	crossorigin="anonymous"></script>
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
	integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
	crossorigin="anonymous"></script>
<style>
html, body, .grid-container { height: 1200px; width:1400px; margin: auto; }

.grid-container * { 
 border: 1px solid red;
 position: relative;
}

.grid-container *:after { 
 position: absolute;
 top: 0;
 left: 0;
}

.grid-container {
  display: grid;
  grid-template-columns: 50px 250px 1050px 50px;
  grid-template-rows: 50px 50px 1000px;
  grid-template-areas: "E1 E2 E2 E3" "E1 Header Main-Menu E3" "E1 Side-Menu Content E3";
}

.E1 { grid-area: E1; }

.E2 { grid-area: E2; }

.E3 { grid-area: E3; }

.Header { 
	grid-area: Header; 
}

.Main-Menu { grid-area: Main-Menu; }

.Side-Menu { grid-area: Side-Menu; }

.Content { grid-area: Content; }

</style>
<title>중고차 판매 사이트</title>
</head>
<body>
	<div class="grid-container">
  		<div class="E1"></div>
  		<div class="E2"></div>
  		<div class="E3"></div>
  		<div class="Header">
  			<ul class="nav justify-content-center" style="height:100%">
  				<li class="nav-item">
  					<a class="nav-link" href="index.jsp">중고차 판매 사이트</a>
  				</li>
  			</ul>
  		</div>
  		<div class="Main-Menu">
  			<ul class="nav justify-content-end" style="height:100%">
  				<%if(isLogon == null){ %>
 				 <li class="nav-item">
    				<a class="nav-link" style="height:100%" href="${contextPath}/loginForm.jsp">Login</a>
  				</li>
  				<%} else { %>
  				<li class="nav-item"><a class="nav-link"><%=user_id %>님 반갑습니다.</a></li>
  				<li class="nav-item">
  					<a class="nav-link" style="height:100%" href="${contextPath}/account/modify.do">회원정보수정</a>
  				</li>
  				<li class="nav-item">
    				<a class="nav-link" style="height:100%" href="${contextPath}/logout.jsp">Logout</a>
  				</li>
  				<%} %>
			</ul>
  		</div>
 		<div class="Side-Menu">
 		
  		</div>
  		<div class="Content">
  
  		</div>
	</div>
</body>
</html>