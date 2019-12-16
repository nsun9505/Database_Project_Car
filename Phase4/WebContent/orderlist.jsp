<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
      <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <c:set var="contextPath" value="${pageContext.request.contextPath}"/>
    <c:set var="orderlist" value="${requestScope.orderlist }"/>
    <c:set var="isLogon" value="${sessionScope.isLogon }"/>
    <c:if test="${isLogon eq null}">
    	<jsp:forward page="/invalidPage.html"></jsp:forward>
    </c:if>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Order List</title>
</head>
<body>

</body>
</html>