package vehicle;

import java.sql.Date;

public class VehicleDTO {
	private String carNum;
	private int regNum;
	private int price;
	private int mileage;
	private Date model_yeay;
	private String location;
	private int engine_displacement;
	private String color;
	private String transmission;
	private String fuel;
	public VehicleDTO(String carNum, int regNum, int price, int mileage, Date model_yeay, String location,
			int engine_displacement, String color, String transmission, String fuel) {
		super();
		this.carNum = carNum;
		this.regNum = regNum;
		this.price = price;
		this.mileage = mileage;
		this.model_yeay = model_yeay;
		this.location = location;
		this.engine_displacement = engine_displacement;
		this.color = color;
		this.transmission = transmission;
		this.fuel = fuel;
	}
	public String getCarNum() {
		return carNum;
	}
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
	public int getRegNum() {
		return regNum;
	}
	public void setRegNum(int regNum) {
		this.regNum = regNum;
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
	public Date getModel_yeay() {
		return model_yeay;
	}
	public void setModel_yeay(Date model_yeay) {
		this.model_yeay = model_yeay;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getEngine_displacement() {
		return engine_displacement;
	}
	public void setEngine_displacement(int engine_displacement) {
		this.engine_displacement = engine_displacement;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getTransmission() {
		return transmission;
	}
	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}
	public String getFuel() {
		return fuel;
	}
	public void setFuel(String fuel) {
		this.fuel = fuel;
	}
}
