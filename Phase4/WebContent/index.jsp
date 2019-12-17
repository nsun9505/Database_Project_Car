<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String isLogon = (String) session.getAttribute("isLogon");
%>

<c:if test="${sessionScope.init eq null }">
	<jsp:forward page="/vehicle/list.do"></jsp:forward>
</c:if>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="userInfo" value="${sessionScope.userInfo}" />
<c:set var="category_list" value="${sessionScope.category_list}" />
<c:set var="make_list" value="${sessionScope.make_list}" />
<c:set var="model_List" value="${sessionScope.model_list}" />
<c:set var="detailed_list" value="${sessionScope.detailed_list}" />
<c:set var="min_model_year" value="${sessionScope.min_model_year}" />
<c:set var="max_model_year" value="${sessionScope.max_model_year}" />
<c:set var="min_mileage" value="${sessionScope.min_mileage}" />
<c:set var="max_mileage" value="${sessionScope.max_mileage}" />
<c:set var="min_price" value="${sessionScope.min_price}" />
<c:set var="max_price" value="${sessionScope.max_price}" />
<c:set var="location_list" value="${sessionScope.location_list}" />
<c:set var="color_list" value="${sessionScope.color_list}" />
<c:set var="fuel_list" value="${sessionScope.fuel_list}" />
<c:set var="transmission_list" value="${sessionScope.transmission_list}" />
<c:set var="vehicle_list" value="${sessionScope.vehicle_list}" />
<!doctype html>
<html>
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
html, body, .grid-container {
	height: 1200px;
	width: 1400px;
	margin: auto;
}

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
	grid-template-areas: "E1 E2 E2 E3" "E1 Header Main-Menu E3"
		"E1 Side-Menu Content E3";
}

.E1 {
	grid-area: E1;
}

.E2 {
	grid-area: E2;
}

.E3 {
	grid-area: E3;
}

.Header {
	grid-area: Header;
}

.Main-Menu {
	grid-area: Main-Menu;
}

.Side-Menu {
	grid-area: Side-Menu;
}

.Content {
	grid-area: Content;
}
</style>
<title>중고차 판매 사이트</title>
</head>
<body>
	<div class="grid-container">
		<div class="E1">
			<form name="transferForm" method="post">
				<input type="text" value="" name="condition" id="setCondition">
			</form>
		</div>
		<div class="E2"></div>
		<div class="E3"></div>
		<div class="Header">
			<ul class="nav justify-content-center" style="height: 100%">
				<li class="nav-item"><a class="nav-link" href="/Phase4/vehicle/init.do">중고차
						판매 사이트</a></li>
			</ul>
		</div>
		<div class="Main-Menu">
			<ul class="nav justify-content-end" style="height: 100%">
				<c:choose>
					<c:when test="${userInfo == null}">
						<li class="nav-item"><a class="nav-link" style="height: 100%"
							href="${contextPath}/login/loginForm.jsp">Login</a></li>
					</c:when>
					<c:otherwise>
						<li class="nav-item">
							<button class="nav-link"
								style="height: 100%; background-color: white; border: none;">${userInfo.name}
								님 반갑습니다.</button>
						</li>
						<li class="nav-item">
							<button class="nav-link"
								style="height: 100%; background-color: white; border: none;"
								onclick="orderListPopup()">
								<c:choose>
									<c:when test="${userInfo.account_type eq 'C'}">${userInfo.name }님의 거래내역</c:when>
									<c:otherwise>모든 거래내역</c:otherwise>
								</c:choose>
							</button>
						</li>
						<c:if test="${userInfo ne null && userInfo.account_type eq 'A' }">
							<li class="nav-item"><button class="nav-link" style="height:100%; background-color:white; border:none">비공개 리스트</button></li>
						</c:if>
						<li class="nav-item">
							<button class="nav-link"
								style="height: 100%; background-color: white; border: none;"
								onclick="modifyPopup()">회원정보수정</button>
						</li>
						<li class="nav-item"><a class="nav-link" style="height: 100%"
							href="${contextPath}/login/logout.jsp">Logout</a></li>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
		<div class="Side-Menu">
			<div>
				<c:if test="${category_list ne null }">
					<div>
						<label>Category</label>
					</div>
					<div>
						<c:forEach items="${category_list }" var="category">
							<div>
								<label><input type="checkbox" value="${category}"
									name="change_category" onclick="change_category(this)">${category }</label>
							</div>
						</c:forEach>
					</div>
				</c:if>
			</div>
			<c:choose>
				<c:when test="${modelList eq null && detailedList eq null }">
					<div>
						<label for="select_make_id">Make</label> <select id="select_make"
							onchange="change_make(this)">
							<option value="X">선택</option>
							<c:forEach items="${make_list }" var="make">
								<option value="${make }">${make }</option>
							</c:forEach>
						</select>
					</div>
				</c:when>
				<c:when test="${model_list ne null && detailed_list eq null }">
					<div>
						<label for="select_model_id">Model</label> <select
							id="select_model" onchange="change_model(this);">
							<option value="X">선택</option>
							<c:forEach items="${model_list }" var="model">
								<option value="${model}">${model}</option>
							</c:forEach>
						</select>
					</div>
				</c:when>
				<c:when test="${make_list ne null && model_list ne null }">
					<div>
						<label for="select_detailed_id">Model</label> <select
							id="select_detailed_id" onchange="change_detailed(this);">
							<option value="X">선택</option>
							<c:forEach items="${detailed_list }" var="detailed">
								<option value="${detailed}">${detailed}</option>
							</c:forEach>
						</select>
					</div>
				</c:when>
			</c:choose>
			<br> <label for="model_year">Model Year</label>
			<div id="model_year">
				<select name="min_model_year" onchange="change_min_year(this)">
					<option value="X">년</option>
					<c:forEach var="i" begin="${min_model_year}"
						end="${max_model_year }" step="1">
						<option value="${i}">${i}년</option>
					</c:forEach>
				</select>부터<br> <select name="max_model_year"
					onchange="change_max_year(this)">
					<option value="X">년</option>
					<c:forEach var="i" begin="${min_model_year}"
						end="${max_model_year }" step="1">
						<option value="${i}">${i}년</option>
					</c:forEach>
				</select>까지
			</div><br>
			<label for="min_mileage_id">Mileage</label>
			<div id="min_mileage_id">
				<select name="min_mileage" onchange="change_min_mileage(this)">
					<option value="X">선택</option>
					<c:forEach var="i" begin="${min_mileage }" end="${max_mileage }" step="10000">
						<option value="${i}">${i}km</option>
					</c:forEach>
				</select>부터<br>
				<select name="min_mileage" onchange="change_max_mileage(this)">
					<option value="X">선택</option>
					<c:forEach var="i" begin="${min_mileage }" end="${max_mileage }" step="10000">
						<option value="${i}">${i}km</option>
					</c:forEach>
				</select>까지
			</div>
			<br> <label for="price_div">Price</label>
			<div id="price_div">
				<select name="min_price" onchange="change_min_price(this)">
					<option value="X">선택</option>
					<c:forEach var="i" begin="${min_price }" end="${max_price }"
						step="500">
						<option value="${i}">${i}만원</option>
					</c:forEach>
				</select>부터<br> <select name="max_price"
					onchange="change_max_price(this)">
					<option value="X">선택</option>
					<c:forEach var="i" begin="${min_price }" end="${max_price }"
						step="500">
						<option value="${i}">${i}만원</option>
					</c:forEach>
				</select>까지
			</div>
			<br> <label for="location_div">Location</label>
			<div id="location_div">
				<c:forEach items="${location_list }" var="loca">
					<div>
						<label><input type="checkbox" value="${loca}"
							name="change_location" onclick="change_location(this)">${loca }</label>
					</div>
				</c:forEach>
			</div>

			<label for="color_div">Color</label>
			<div id="color_div">
				<c:forEach items="${color_list }" var="color">
					<div>
						<label><input type="checkbox" value="${color}"
							name="change_color" onclick="change_color(this)">${color }</label>
					</div>
				</c:forEach>
			</div>

			<label for="fuel_div">Fuel</label>
			<div id="fuel_div">
				<c:forEach items="${fuel_list }" var="fuel">
					<div>
						<label><input type="checkbox" value="${fuel}"
							name="change_fuel" onclick="change_fuel(this)">${fuel }</label>
					</div>
				</c:forEach>
			</div>

			<label for="transmission_div">Transmission</label>
			<div id="transmission_div">
				<c:forEach items="${transmission_list }" var="trans">
					<div>
						<label><input type="checkbox" value="${trans}"
							name="change_transmission" onclick="change_transmission(this)">${trans }</label>
					</div>
				</c:forEach>
			</div>
		</div>
		<div class="Content">
		
		<table class="table table-hover">
  		<thead>
   			<tr>
    	  	<th scope="col">Regnum</th>
      		<th scope="col">First</th>
      		<th scope="col">Last</th>
      		<th scope="col">Handle</th>
    		</tr>
  		</thead>
  		<tbody>
    	<tr>
      	<th scope="row">1</th>
      	<td>Mark</td>
      	<td>Otto</td>
      	<td>@mdo</td>
    	</tr>
    	<tr>
      <th scope="row">2</th>
      <td>Jacob</td>
      <td>Thornton</td>
      <td>@fat</td>
    </tr>
    <tr>
      <th scope="row">3</th>
      <td colspan="2">Larry the Bird</td>
      <td>@twitter</td>
    </tr>
  </tbody>
</table>
		</div>
	</div>
</body>
<script>
	function modifyPopup() {
		var url = "/Phase4/modify/modifyInfoForm.jsp";
		var name = "_blank";
		var specs = "";
		window.open(url, name, specs);
	}

	function orderListPopup() {
		var url = "/Phase4/orderlist/myOrderList.do";
		var name = "_blank";
		var specs = "";
		window.open(url, name, specs);
	}

	function change_category(checked) {
		if(selected.value != "X"){
			transferForm.condition.value = selected.options[selected.selectedIndex].value;
			transferForm.action = "/Phase4/vehicle/selectCategory.do"
				transferFrom.submit();
		}
	}
	function change_make(selected) {
		if(selected.value != "X"){
			transferForm.condition.value = selected.options[selected.selectedIndex].value;
			transferForm.action = "/Phase4/vehicle/selectMake.do"
				transferFrom.submit();
		}
	}

	function change_model(selected) {
		if(selected.value != "X"){
			transferForm.condition.value = selected.options[selected.selectedIndex].value;
			transferForm.action = "/Phase4/vehicle/selectModel.do"
			transferFrom.submit();
		}
	}
	
	function change_detailed(selected) {
		if(selected.value != "X"){
			transferForm.condition.value = selected.options[selected.selectedIndex].value;
			transferForm.action = "/Phase4/vehicle/selectDetailed.do"
				transferFrom.submit();
		}
	}
	function change_max_year(selected){
		if(selected.value != "X"){
			transferForm.condition.value = selected.options[selected.selectedIndex].value;
			transferForm.action = "/Phase4/vehicle/selectMaxModelYear.do"
				transferFrom.submit();
		}
	}
	function change_min_year(selected){
		if(selected.value != "X"){
			transferForm.condition.value = selected.options[selected.selectedIndex].value;
			transferForm.action = "/Phase4/vehicle/selectMinModelYear.do"
				transferFrom.submit();
		}
	}
	
	function change_max_mileage(selected){
		if(selected.value != "X"){
			transferForm.condition.value = selected.options[selected.selectedIndex].value;
			transferForm.action = "/Phase4/vehicle/selectMaxMileage.do"
				transferFrom.submit();
		}
	}
	function change_min_mileage(selected){
		if(selected.value != "X"){
			transferForm.condition.value = selected.options[selected.selectedIndex].value;
			transferForm.action = "/Phase4/vehicle/selectMinMileage.do"
				transferFrom.submit();
		}
	}
	
	function change_min_price(selected){
		if(selected.value != "X"){
			transferForm.condition.value = selected.options[selected.selectedIndex].value;
			transferForm.action = "/Phase4/vehicle/selectMinPrice.do"
				transferFrom.submit();
		}
	}
	
	function change_max_price(selected){
		if(selected.value != "X"){
			transferForm.condition.value = selected.options[selected.selectedIndex].value;
			transferForm.action = "/Phase4/vehicle/selectMaxPrice.do"
				transferFrom.submit();
		}
	}
	
	function change_location(checked){
			transferForm.condition.value = selected.options[selected.selectedIndex].value;
			transferForm.action = "/Phase4/vehicle/selectLocation.do"
				transferFrom.submit();
	}
	function change_color(checked){
			transferForm.condition.value = selected.options[selected.selectedIndex].value;
			transferForm.action= "/Phase4/vehicle/selectColor.do"
			transferFrom.submit();
	}
	
	function change_fuel(checked){
			transferForm.condition.value = checked.value;
			alert(ceheck.value);
			transferForm.action = "/Phase4/vehicle/selectFuel.do"
			transferFrom.submit();
	}
	
	function change_transmission(checked){
			transferForm.condition.value = checked.value;
			transferForm.action = "/Phase4/vehicle/selectTransmission.do"
			transferFrom.submit();
	}
</script>
</html>
