<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>매물 추가</title>
</head>
<body>
<form name="addVehicleForm" action="/Phase4/vehicle/addVehicle.do" method="post">
<div>

	<label for="mkList">Make</label>
	<select id="make_list" name = "make">
	<%
			ArrayList<String> mklist = (ArrayList<String>)session.getAttribute("makelist");
			for(int i=0; i<mklist.size();i++){
				if(session.getAttribute("selected_make") != null){
					String selected_make = (String)session.getAttribute("selected_make");
					if(selected_make.equals(mklist.get(i)))
						out.println("<option value='"+mklist.get(i)+"' selected>"+mklist.get(i)+"</option>");
					else
						out.println("<option value='"+mklist.get(i)+"'>"+mklist.get(i)+"</option>");
				} else {
					out.println("<option value='"+mklist.get(i)+"'>"+mklist.get(i)+"</option>");
				}
			}
	%>
	</select>
	</div>
	
	<div>
	<label for="mList">Model</label>
	<select id="model_list" name="model_name">
	<%
	if(session.getAttribute("modelList") != null){
	ArrayList<String> mdlist = (ArrayList<String>)session.getAttribute("modelList");
	for(int i=0; i<mdlist.size();i++)
		if(session.getAttribute("selected_model") != null){
			String selected_model = (String)session.getAttribute("selected_model");
			if(selected_model.equals(mdlist.get(i)))
				out.println("<option value='"+mdlist.get(i)+"' selected>"+mdlist.get(i)+"</option>");
			else
				out.println("<option value='"+mdlist.get(i)+"'>"+mdlist.get(i)+"</option>");
		} else {
			out.println("<option value='"+mdlist.get(i)+"'>"+mdlist.get(i)+"</option>");
		}
	}
	%>
	</select>
	
	</div>
	
	<div>
	<label for="dmList">Detailed Model Name</label>
	<select id="detailed_model_list" name="detailed_model_name">
		<%
	if(session.getAttribute("detaileList") != null){
	ArrayList<String> detailelist = (ArrayList<String>)session.getAttribute("detaileList");
	for(int i=0; i<detailelist.size();i++)
		if(session.getAttribute("selected_detaile") != null){
			String selected_detaile = (String)session.getAttribute("selected_detaile");
			if(selected_detaile.equals(detailelist.get(i)))
				out.println("<option value='"+detailelist.get(i)+"' selected>"+detailelist.get(i)+"</option>");
			else
				out.println("<option value='"+detailelist.get(i)+"'>"+detailelist.get(i)+"</option>");
		} else {
			out.println("<option value='"+detailelist.get(i)+"'>"+detailelist.get(i)+"</option>");
		}	
	}
	%>
	</select>
	</div>
	
	<div>
		<label for="engine">Engine Displacement</label>
		<select id="engine" name="engine_displacement">
			<%
	if(session.getAttribute("EngineList") != null){
	ArrayList<String> enginelist = (ArrayList<String>)session.getAttribute("EngineList");
	for(int i=0; i<enginelist.size();i++)
		if(session.getAttribute("selected_engine") != null){
			String selected_engine = (String)session.getAttribute("selected_engine");
			if(selected_engine.equals(enginelist.get(i)))
				out.println("<option value='"+enginelist.get(i)+"' selected>"+enginelist.get(i)+"</option>");
			else
				out.println("<option value='"+enginelist.get(i)+"'>"+enginelist.get(i)+"</option>");
		} else {
			out.println("<option value='"+enginelist.get(i)+"'>"+enginelist.get(i)+"</option>");
		}	
	}
	%>
		</select>
	</div>
	
	<div>
		<label for="model_year_id">model year</label>
		<select id="model_year_id" name="model_year">
		</select>
		<select id="model_month_id" name="model_month">
			<option value="1">1</option>
			<option value="2">2</option>
			<option value="3">3</option>
			<option value="4">4</option>
			<option value="5">5</option>
			<option value="6">6</option>
			<option value="7">7</option>
			<option value="8">8</option>
			<option value="9">9</option>
			<option value="10">10</option>
			<option value="11">11</option>
			<option value="12">12</option>			
		</select>
	</div>
	
	<div>
		<label for="fuel_id">Fuel</label>
		<select id="fuel_id" name="fuel">
			<%
			if(session.getAttribute("fuelList")!=null){
				ArrayList<String> fuel = (ArrayList<String>)session.getAttribute("fuelList");
				for(int i=0;i<fuel.size();i++)
					out.println("<option value='"+fuel.get(i)+"' selected>"+fuel.get(i)+"</option>");
			}
			%>
		</select>
	</div>
	
	<div>
		<label for="color_id">Color</label>
		<select id="color_id" name="color">
				<%
			if(session.getAttribute("fuelList")!=null){
				ArrayList<String> color = (ArrayList<String>)session.getAttribute("colorList");
				for(int i=0;i<color.size();i++)
					out.println("<option value='"+color.get(i)+"' selected>"+color.get(i)+"</option>");
			}
			%>
		</select>
	</div>
	
	<div>
		<label for="trans_id">Transmission</label>
		<select id="trans_id" name="transmission">
				<%
			if(session.getAttribute("fuelList")!=null){
				ArrayList<String> trans = (ArrayList<String>)session.getAttribute("transList");
				for(int i=0;i<trans.size();i++)
					out.println("<option value='"+trans.get(i)+"' selected>"+trans.get(i)+"</option>");
			}
			%>
		</select>
	</div>
	
	<div>
		<label for="price_id">Price</label>
		<input type="text" id="price_id" name="price">
	</div>
	
	<div>
		<label for="mileage_id">Mileage</label>
		<input type="text" id="mileage_id" name="mileage">
	</div>
	
	<div>
		<label for="location_id">Location</label>
		<input type="text" id="location_id" name="location">
	</div>
	
	<div>
		<label for="car_number_id">car_number</label>
		<input type="text" id="car_number_id" name="car_number">
	</div>
	
	<div>
		<input type="submit" value="매물 등록">
		<input type="reset" value="초기화">
		<input type="button" value="취소">
	</div>
	
	
	
</form>
<script>
var model_year = document.getElementById("model_year_id");

var make_list = document.getElementById("make_list");
make_list.addEventListener("change", changeMake);

var model_list = document.getElementById("model_list");
model_list.addEventListener("change",changeModel);

var detaile_list = document.getElementById("detailed_model_list");
detaile_list.addEventListener("change",changeDetaile);

var engine_list = document.getElementById("engine");
engine_list.addEventListener("change",changeEngine);

for(var i=1980; i<=2019; i++){
	model_year.innerHTML += "<option value='"+i+"'>"+i+"</option>";
}

function changeMake(){
	addVehicleForm.action = "/Phase4/vehicle/getModel.do";
	addVehicleForm.submit();
}

function changeModel(){
	addVehicleForm.action = "/Phase4/vehicle/getDetailed.do";
	addVehicleForm.submit();
}

function changeDetaile(){
	addVehicleForm.action = "/Phase4/vehicle/getEngine.do";
	addVehicleForm.submit();
}

function changeEngine(){
	addVehicleForm.action = "/Phase4/vehicle/getElse.do";
	addVehicleForm.submit();
}

</script>
</body>
</html>