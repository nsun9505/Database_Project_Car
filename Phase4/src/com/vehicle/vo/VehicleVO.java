package com.vehicle.vo;

import java.sql.Date;

public class VehicleVO {
	private int regNum;
	private String make;
	private String detailed_model_name;
	private String model_year;
	private int price;
	private int mileage;
	private String location;
	private String fuel;
	private String color;
	private String category;
	private int engine_displacement;
	private String transmission;
	private String car_number;
	private String model_name;
	private String country;
	private String sellerId;

	public VehicleVO(int regNum, String make, String detailed_model_name, String model_year, int price, int mileage,
			String location, String fuel, String color, String category, int engine_displacement, String transmission,
			String car_number, String model_name, String country, String sellerId) {
		this.regNum = regNum;
		this.make = make;
		this.detailed_model_name = detailed_model_name;
		this.model_year = model_year;
		this.price = price;
		this.mileage = mileage;
		this.location = location;
		this.fuel = fuel;
		this.color = color;
		this.category = category;
		this.engine_displacement = engine_displacement;
		this.transmission = transmission;
		this.car_number = car_number;
		this.model_name = model_name;
		this.country = country;
		this.sellerId = sellerId;
	}
	
	public VehicleVO(int regNum, String make, String detailed_model_name, String model_year, int price,
			int mileage, String location, String fuel, String color) {
		super();
		this.regNum = regNum;
		this.make = make;
		this.detailed_model_name = detailed_model_name;
		this.model_year = model_year;
		this.price = price;
		this.mileage = mileage;
		this.location = location;
		this.fuel = fuel;
		this.color = color;
	}
	
	public VehicleVO(String detailed_model_name, String model_year, int price, int mileage,
			String location, String fuel, String color, int engine_displacement, String transmission,
			String car_number, String sellerId) {
		this.detailed_model_name = detailed_model_name;
		this.model_year = model_year;
		this.price = price;
		this.mileage = mileage;
		this.location = location;
		this.fuel = fuel;
		this.color = color;
		this.engine_displacement = engine_displacement;
		this.transmission = transmission;
		this.car_number = car_number;
		this.sellerId = sellerId;
	}

	public int getRegNum() {
		return regNum;
	}

	public void setRegNum(int regNum) {
		this.regNum = regNum;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getDetailed_model_name() {
		return detailed_model_name;
	}

	public void setDetailed_model_name(String detailed_model_name) {
		this.detailed_model_name = detailed_model_name;
	}

	public String getModel_year() {
		return model_year;
	}

	public void setModel_year(String model_year) {
		this.model_year = model_year;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getMileage() {
		return mileage;
	}

	public void setMileage(int mileage) {
		this.mileage = mileage;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getFuel() {
		return fuel;
	}

	public void setFuel(String fuel) {
		this.fuel = fuel;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getEngine_displacement() {
		return engine_displacement;
	}

	public void setEngine_displacement(int engine_displacement) {
		this.engine_displacement = engine_displacement;
	}

	public String getTransmission() {
		return transmission;
	}

	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}

	public String getCar_number() {
		return car_number;
	}

	public void setCar_number(String car_number) {
		this.car_number = car_number;
	}

	public String getModel_name() {
		return model_name;
	}

	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	
	
}
