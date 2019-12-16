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
	<input type="text" id="mkList" list="make_list" name="make">
	<datalist id="make_list">
		<option value="Audi">Audi</option>
		<option value="Kia">Kia</option>
	</datalist>
	</div>
	
	<div>
	<label for="mList">Model</label>
	<input type="text" id="mList" list="model_list" name="model_name">
	<datalist id="model_list">
		<option value="R8">R8</option>
		<option value="K7">K7</option>
	</datalist>
	
	</div>
		
	<div>
		<label for="category_id">Category</label>
		<input type="text" name="category" id="category_id">
	</div>
	
	<div>
	<label for="dmList">Detailed Model Name</label>
	<input type="text" id="mkList" list="detailed_model_list" name="detailed_model_name">
	<datalist id="detailed_model_list">
		<option value="R8_RWS">R8_RWS</option>
		<option value="K7">K7</option>
	</datalist>
	</div>
	
	<div>
		<label for="engine">Engine Displacement</label>
		<input type="text" id="engin" name="engine_displacement">
	</div>
	
	<div>
		<label for="model_year_id"></label>
		<select id="model_year_id" name="model_year">
		</select>
	</div>
	
	<div>
		<label for="model_year_id"></label>
		<select id="model_year_id" name="model_year">
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
			<option value="LPG">LPG</option>
			<option value="Diesel">Diesel</option>
		</select>
	</div>
	
	<div>
		<label for="color_id">Color</label>
		<select id="color_id" name="color">
			<option value="Black">Black</option>
			<option value="White">White</option>
		</select>
	</div>
	
	<div>
		<label for="trans_id">Transmission</label>
		<select id="trans_id" name="transmission">
			<option value="Auto">Auto</option>
			<option value="Manual">Manual</option>
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
		<input type="submit" value="매물 등록">
		<input type="reset" value="초기화">
		<input type="button" value="취소">
	</div>
	
</form>
<script>
var model_year = document.getElementById("model_year_id");
for(var i=1980; i<=2019; i++){
	model_year.innerHTML += "<option value='"+i+"'>"+i+"</option>"
}
</script>
</body>
</html>