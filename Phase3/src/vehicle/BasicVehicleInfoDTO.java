package vehicle;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class BasicVehicleInfoDTO {
	private int regNum;
	private String make;
	private String detailed_model_name;
	private Date model_year;
	private int price;
	private int mileage;
	private String location;
	private String fuel;
	private String color;
	
	public BasicVehicleInfoDTO(int regNum, String make, String detailed_model_name, Date model_year, int price,
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

	public int getRegNum() {
		return regNum;
	}

	public String getMake() {
		return make;
	}

	public String getDetailed_model_name() {
		return detailed_model_name;
	}

	public Date getModel_year() {
		return model_year;
	}

	public int getPrice() {
		return price;
	}

	public int getMileage() {
		return mileage;
	}

	public String getLocation() {
		return location;
	}

	public String getFuel() {
		return fuel;
	}
	public String getColor() {
		return color;
	}

	public void setRegNum(int regNum) {
		this.regNum = regNum;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public void setDetailed_model_name(String detailed_model_name) {
		this.detailed_model_name = detailed_model_name;
	}

	public void setModel_year(Date model_year) {
		this.model_year = model_year;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public void setMileage(int mileage) {
		this.mileage = mileage;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setFuel(String fuel) {
		this.fuel = fuel;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	public void printBasicVehicleInfo() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM");
		String model_year = sdfDate.format(this.getModel_year());
		System.out.printf("%-20d %-15s %-20s %-10s %-7d %-15s %-20s %-15s %-20d\n",
				 this.getRegNum(), this.getMake(), this.getDetailed_model_name(),
				 model_year, this.getMileage(), this.getLocation(),
				 this.getFuel(), this.getColor(), this.getPrice());
	}
}
