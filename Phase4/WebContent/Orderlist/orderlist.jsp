<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="userInfo" value="${sessionScope.userInfo }" />
<c:set var="order_list" value="${requestScope.order_list}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Order List</title>
<script type="text/javascript"
	src="<c:url value="/resource/js/jquery-3.4.1.js"/>"></script>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
</head>
<body>
	<c:choose>
		<c:when test="${userInfo eq null }">
			<script>
				alert("로그인이 필요한 화면입니다.")
			</script>
		</c:when>
		<c:when test="${order_list eq null }">
			<script>
				alert("거래 내역이 없습니다. 해당 페이지를 닫습니다.");
				self.close();
			</script>
		</c:when>
		<c:otherwise>
			<div class="container">
				<div>
					<c:if test="${userInfo.account_type eq 'C' }">
						<h1>${userInfo.name }님의 거래내역입니다.</h1>	
					</c:if>
					<c:if test="${userInfo.account_type eq 'A' }">
						<h1>전체 거래내역(관리자용)</h1>
						<hr>
						<form action="${contextPath}/orderlist/getTotalIntake.do">
							<div>
								<label for=""></label>
								<select>
							
								</select>
							</div>
						</form>
					</c:if>
					<h1></h1>
				</div>
				<hr>
				<div>
					<table class="table">
						<thead>
							<tr>
								<th scope="col">registration_number</th>
								<th scope="col">Seller Id</th>
								<th scope="col">Buyer Id</th>
								<th scope="col">Sell Date</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${requestScope.order_list}" var="order">
								<tr>
									<th scope="row">${order.registration_number }</th>
									<th>${order.seller_id}</th>
									<th>${order.buyer_id}</th>
									<th>${order.sell_date.toString()}</th>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			<div style="text-align:center">
				<input type="button" value="닫기" onclick="self.close()">
			</div>
		</c:otherwise>
	</c:choose>
</body>
</html>