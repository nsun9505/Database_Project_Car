<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="userInfo" value="${sessionScope.userInfo }" />
<c:set var="order_list" value="${sessionScope.order_list}" />
<c:set var="make_list" value="${sessionScope.make_list}"/>
<c:set var="intakeResult" value="${requestScope.intakeResult }"/>
<c:set var="resultString" value="${requestScope.resultString }"/>
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
						<h1>${userInfo.name }님의거래내역입니다.</h1>
					</c:if>
					<c:if test="${userInfo.account_type eq 'A' }">
						<h1>전체 거래내역(관리자용)</h1>
						<hr>
						<form>
							<div class="form-group">
								<label for="inTake_id"></label>
								<select class="form-control" id="inTake_id" name="typeOfIntake" onchange="changes(this);">
									<option value="NONE" selected>선택</option>
									<option value="makeTotal">제조사 총 매출액</option>
									<option value="makeYear">제조사 연도 총 매출액</option>
									<option value="makeMonth">제조사 월 매출액</option>
									<option value="yearTotal">연도별 총 매출액</option>
									<option value="monthTotal">월별 총 매출액</option>
									<option value="systemTotal">총 매출액</option>
								</select>
							</div>
						</form>
						<form action="${contextPath}/orderlist/getTotalIntake.do">
							<input type="hidden" value="" id="selected_intake_id" name="selected_intake">
							<div class="form-group" id="selected_output">
							<c:if test="${intakeResult ne null }">
								<h1>${resultString}</h1>
								<h1>${intakeResult}원</h1>
							</c:if>
							</div>
						</form>
					</c:if>
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
							<c:forEach items="${sessionScope.order_list}" var="order">
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
			<div style="text-align: center">
				<input type="button" value="닫기" onclick="self.close()">
			</div>
		</c:otherwise>
	</c:choose>
</body>
<script>
	function changes(selected_obj){
		var selected_idx = selected_obj.selectedIndex;
		var intake_type = selected_obj.options[selected_idx].value;
		if(intake_type != "NONE"){
			var myDiv = document.getElementById("selected_output");
			var type = document.getElementById("selected_intake_id");
			type.value= intake_type;
			myDiv.innerHTML = "";
			switch(intake_type){
			case "makeTotal":
				printMake();
				break;
			case "makeYear":
				printMake();
				printYear();
				break;
			case "makeMonth":
				printMake();
				printYear();
				printMonth();
				break;
			case "yearTotal":
				printYear();
				break;
			case "monthTotal":
				printYear();
				printMonth();
				break;
			case "systemTotal":
				break;
			}
			myDiv.innerHTML += "<input type='submit' value='검색' class='btn btn-default'>"
		}
	}
	
	function printMonth(){
		var myDiv = document.getElementById("selected_output");
		var myMonth ="";
		myDiv.innerHTML += "<select name='selected_month' id='selected_month_id' class='form-control'>"
		myDiv.innerHTML +="</select>";
		for(var i=1; i<=12; i++){
			myMonth += "<option value='"+i+"'>"+i+"</option>";
		}
		document.getElementById("selected_month_id").innerHTML = myMonth;
	}
	
	function printYear(){
		var myDiv = document.getElementById("selected_output");
		var myYear ="";
		myDiv.innerHTML += "<select name='selected_year' id='selected_year_id' class='form-control'>"
		myDiv.innerHTML +="</select>";
		for(var i=1980; i<=2019; i++){
			myYear += "<option value='"+i+"'>"+i+"</option>";
		}
		document.getElementById("selected_year_id").innerHTML = myYear;
	}
	
	function printMake(){
		var list = new Array();
		var myMake = "";
		<c:forEach items="${make_list}" var="make">
			list.push("${make}");
		</c:forEach>
		var myDiv = document.getElementById("selected_output");
		myDiv.innerHTML += "<select name='selected_make' id='selected_make_id' class='form-control'>"
		myDiv.innerHTML += "</select>";
		for(var i=0; i<list.length; i++){
			myMake += "<option value='"+list[i]+"'>"+list[i]+"</option>"
		}
		document.getElementById("selected_make_id").innerHTML = myMake;
	}
</script>
</html>