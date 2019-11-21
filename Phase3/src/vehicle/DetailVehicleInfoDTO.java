package vehicle;

import java.sql.Date;

public class DetailVehicleInfoDTO extends BasicVehicleInfoDTO{
	private String category;
	private int engine_displacement;
	private String transmission;
	private String car_number;
	private String model_name;
	private String country;
	
	public DetailVehicleInfoDTO(String make, String country, String model_name, String category, String car_number,
			int regNum, int price, int mileage, Date model_year, String location, int engine_displacement, 
			String detailed_model_name, String color, String transmission, String fuel) {
		super(regNum, make, detailed_model_name, model_year, price, mileage, location, fuel, color);
		this.category = category;
		this.engine_displacement = engine_displacement;
		this.transmission = transmission;
		this.car_number = car_number;
		this.model_name = model_name;
		this.country = country;
	}

	public String getCategory() {
		return category;
	}

	public int getEngine_displacement() {
		return engine_displacement;
	}

	public String getTransmission() {
		return transmission;
	}

	public String getCar_number() {
		return car_number;
	}

	public String getModel_name() {
		return model_name;
	}

	public String getCountry() {
		return country;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setEngine_displacement(int engine_displacement) {
		this.engine_displacement = engine_displacement;
	}

	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}

	public void setCar_number(String car_number) {
		this.car_number = car_number;
	}

	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
