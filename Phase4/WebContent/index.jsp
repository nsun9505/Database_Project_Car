<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
	<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<%
		String isLogon = (String)session.getAttribute("isLogon");
		HashMap<String, ArrayList<String>> conditions = (HashMap<String, ArrayList<String>>) session.getAttribute("conditions");
	%>
	
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="userInfo" value="${sessionScope.userInfo}"/>
<c:set var="category_list" value="${sessionScope.category_list}"/>
<c:set var="make_list" value="${sessionScope.make_list}"/>
<c:set var="model_List" value="${sessionScope.model_list}"/>
<c:set var="detailed_list" value="${sessionScope.detailed_list}"/>
<c:set var="min_model_year" value="${sessionScope.min_model_year}"/>
<c:set var="min_model_month" value="${sessionScope.min_model_month}"/>
<c:set var="max_model_year" value="${sessionScope.max_model_year}"/>
<c:set var="max_model_month" value="${sessionScope.max_model_month}"/>
<c:set var="min_mileage" value="${sessionScope.min_mileage}"/>
<c:set var="userImax_mileagenfo" value="${sessionScope.max_mileage}"/>
<c:set var="min_price" value="${sessionScope.min_price}"/>
<c:set var="max_price" value="${sessionScope.max_price}"/>
<c:set var="location_list" value="${sessionScope.location_list}"/>
<c:set var="color_list" value="${sessionScope.color_list}"/>
<c:set var="fuel_list" value="${sessionScope.fuel_list}"/>
<c:set var="transmission_list" value="${sessionScope.transmission_list}"/>
<!doctype html>
<html>
<head>
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport"content="width=device-width, initial-scale=1, shrink-to-fit=no">
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
  				<c:choose>
  					<c:when test="${userInfo == null}">
 				 		<li class="nav-item">
    						<a class="nav-link" style="height:100%" href="${contextPath}/login/loginForm.jsp">Login</a>
  						</li>
  					</c:when>
  					<c:otherwise>
  						<li class="nav-item">
  							<button class="nav-link" style="height:100%; background-color:white; border:none;">${userInfo.name} 님 반갑습니다.</button>
  						</li>
  						<li class="nav-item">
	  						<button class="nav-link" style="height:100%; background-color:white; border:none;" onclick="orderListPopup()">
  							<c:choose>
	  							<c:when test="${userInfo.account_type eq 'C'}">${userInfo.name }님의 거래내역</c:when>
  								<c:otherwise>모든 거래내역</c:otherwise>
  							</c:choose>
  							</button>
  						</li>
  						<li class="nav-item">
  							<button class="nav-link" style="height:100%; background-color:white; border:none;" onclick="modifyPopup()">회원정보수정</button>
  						</li>
  						<li class="nav-item">
    						<a class="nav-link" style="height:100%" href="${contextPath}/login/logout.jsp">Logout</a>
  						</li>
  					</c:otherwise>
  				</c:choose>
			</ul>
  		</div>
 		<div class="Side-Menu">
 		<div>
 			<c:if test="${category_list ne null }">
 			<div><label>Category</label></div>
 			<div>
 				<c:forEach items="${category_list }" var="category">
 					<div>
		 				<label><input type="checkbox" value="${category}" name="category_check">${category }</label>
		 			</div>
 				</c:forEach>
 			</div>
 			</c:if>
 		</div>
 		<c:choose>
 			<c:when test="${modelList eq null && detailedList eq null }">
 			<div>
 				<label for="select_make_id">Make</label>
 				<select id="select_make" onchange="changeMake();">
 					<option value="">Make</option>
 					<c:forEach items="${make_list }" var="make">
 						<option value="${make }">${make }</option>
 					</c:forEach>
 				</select>
 			</div>
 			</c:when>
 			<c:when test="${model_list ne null && detailed_list eq null }">
 				<div>
 				<label for="select_model_id">Model</label>
 				<select id="select_model" onchange="changeModel();">
 					<option value="">Model</option>
 					<c:forEach items="${model_list }" var="model">
 						<option value="${model}">${model}</option>
 					</c:forEach>
 				</select>
 				</div>
 			</c:when>
 			<c:when test="${make_list ne null && model_list ne null }">
 				<div>
 				<label for="select_detailed_id">Model</label>
 				<select id="select_detailed_id" onchange="changedetailed();">
 					<option value="">Model</option>
 					<c:forEach items="${detailed_list }" var="detailed">
 						<option value="${detailed}">${detailed}</option>
 					</c:forEach>
 				</select>
 				</div>
 			</c:when>
 		</c:choose>
  		
  		<label for="model_year">Model Year</label>
  		<div id="model_year">
  			<c:choose>
  			<c:when test="${min_model_year eq null && max_model_year eq null}">
  				<select name="min_model_year" onchange="change_min_year(this)">
  					<option value="">년</option>
  					<c:forEach var="i" begin="1980" end="2019" step="1">
  						<option value="${i}">${i}년</option>
  					</c:forEach>
  				</select>
  				<select name="min_model_month" onchange="change_min_month(this)">
  					<option value="">월</option>
  					<c:forEach var="i" begin="1" end="12" step="1">
  						<option value="${i}">${i}월</option>
  					</c:forEach>
  				</select>부터<br>
  				<select name="max_model_year" onchange="change_max_year(this)">
  					<option value="">년</option>
  					<c:forEach var="i" begin="1980" end="2019" step="1">
  						<option value="${i}">${i}년</option>
  					</c:forEach>
  				</select>
  				<select name="max_model_month" onchange="change_max_month(this)">
  					<option value="">월</option>
  					<c:forEach var="i" begin="1" end="12" step="1">
  						<option value="${i}">${i}월</option>
  					</c:forEach>
  				</select>까지
  			</c:when>
  			<!-- min model year ok -->
  			<c:when test="${min_model_year ne null && max_model_year eq null}">
  				<select name="min_model_year" onchange="change_min_year(this)">
  					<option value="">년</option>
  					<c:forEach var="i" begin="${min_model_year }" end="2019" step="1">
  						<option value="${i}">${i}년</option>
  					</c:forEach>
  				</select>부터<br>
  				<select name="max_model_year" onchange="change_max_year(this)">
  					<option value="">년</option>
  					<c:forEach var="i" begin="1980" end="2019" step="1">
  						<option value="${i}">${i}년</option>
  					</c:forEach>
  				</select>까지
  			</c:when>
  			<!-- max model year ok -->
  			<c:when test="${min_model_year eq null && max_model_year eq null}">
  				<select name="min_model_year" onchange="change_min_year(this)">
  					<option value="">년</option>
  					<c:forEach var="i" begin="1980" end="2019" step="1">
  						<option value="${i}">${i}년</option>
  					</c:forEach>
  				</select>부터<br>
  				<select name="max_model_year" onchange="change_max_year(this)">
  					<option value="">년</option>
  					<c:forEach var="i" begin="1980" end="2019" step="1">
  						<option value="${i}">${i}년</option>
  					</c:forEach>
  				</select>까지
  			</c:when>
  			<!-- min and max model ok -->
  			<c:when test="${min_model_year eq null && max_model_year eq null}">
  				<select name="min_model_year" onchange="change_min_year(this)">
  					<option value="">년</option>
  					<c:forEach var="i" begin="1980" end="2019" step="1">
  						<option value="${i}">${i}년</option>
  					</c:forEach>
  				</select>부터<br>
  				<select name="max_model_year" onchange="change_max_year(this)">
  					<option value="">년</option>
  					<c:forEach var="i" begin="1980" end="2019" step="1">
  						<option value="${i}">${i}년</option>
  					</c:forEach>
  				</select>까지
  			</c:when>
  			</c:choose>
  		</div>
  		
  		</div>
  		
  		<div class="Content">
  
  		</div>
	</div>
</body>
<script>
function modifyPopup(){
	var url = "/Phase4/modify/modifyInfoForm.jsp";
	var name = "_blank";
	var specs = "";
	window.open(url, name, specs);
}

function orderListPopup(){
	var url = "/Phase4/orderlist/myOrderList.do";
	var name = "_blank";
	var specs = "";
	window.open(url, name, specs);
}
</script>
</html>