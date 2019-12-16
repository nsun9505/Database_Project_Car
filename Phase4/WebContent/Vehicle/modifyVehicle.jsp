<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
     <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
    <c:set var="carInfo" value="${sessionScope.carInfo}"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>매물 추가</title>
</head>
<body>
<form name="modifyVehicleForm" action="${contextPath }/vehicle/modifyVehicle.do" method="post">
<div>
	<label for="detailed_model">detailed model name</label>
	<input type="text" id="detailed_model" name="detailed_model_name" value="${carInfo.detailed_model_name }" readonly>
	</div>
	
	<div>
	<label for="engine_displacement">engine displacement</label>
	<input type="text" id="engine_displacement" name="engine_displacement" value="${carInfo.engine_displacement }" readonly>
	</div>
	
	<div>
	<label for="model_year">model year</label>
	<input type="text" id="model_year" name="model_year" value="${carInfo.model_year }" readonly>
	</div>
	
	<div>
	<label for="car_number">model year</label>
	<input type="text" id="car_number" name="car_number" value="${carInfo.car_number }" readonly>
	</div>
	
	<div>
	<label for="sellerId">seller ID</label>
	<input type="text" id="sellerId" name="sellerId" value="${carInfo.sellerId }" readonly>
	</div>
	
	
	<div>
		<label for="color_id">Color</label>
		<select id="color_id" name="color">
		<c:forEach items="${sessionScope.color_list}" var="color">
			<c:if test="${color eq carInfo.color }">
				<option value="${color }" selected>${color }</option>
			</c:if>
			<c:if test="${color ne carInfo.color }">
				<option value="${color }">${color }</option>
			</c:if>
		</c:forEach>
		</select>
	</div>
	
	<div>
		<label for="fuel_id">Fuel</label>
		<select id="fuel_id" name="fuel">
		<c:forEach items="${sessionScope.fuel_list}" var="fuel">
			<c:if test="${fuel eq carInfo.fuel }">
				<option value="${fuel }" selected>${fuel }</option>
			</c:if>
			<c:if test="${fuel ne carInfo.fuel }">
				<option value="${fuel }">${fuel }</option>
			</c:if>
		</c:forEach>
		</select>
	</div>
	
	<div>
		<label for="trans_id">Transmission</label>
		<select id="trans_id" name="transmission">
		<c:forEach items="${sessionScope.trans_list}" var="trans">
			<c:if test="${trans eq carInfo.transmission }">
				<option value="${trans }" selected>${trans }</option>
			</c:if>
			<c:if test="${trans ne carInfo.transmission }">
				<option value="${trans }">${trans }</option>
			</c:if>
		</c:forEach>
		</select>
	</div>
	
	<div>
		<label for="price_id">Price</label>
		<input type="text" id="price_id" name="price" value="${carInfo.price }">
	</div>
	
	<div>
		<label for="mileage_id">Mileage</label>
		<input type="text" id="mileage_id" name="mileage" value="${carInfo.mileage }">
	</div>
	
	<div>
		<label for="location_id">Location</label>
		<input type="text" id="location_id" name="location" value="${carInfo.location }">
	</div>
	
	
	<div>
		<input type="submit" value="수정">
		<input type="reset" value="초기화">
		<input type="button" value="취소">
	</div>
	
	
	
</form>
</body>
</html>