package vehicle;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class DetailVehicleInfoDTO extends BasicVehicleInfoDTO{
	private String category;
	private int engine_displacement;
	private String transmission;
	private String car_number;
	private String model_name;
	private String country;
	private String sellerId;
	
	public DetailVehicleInfoDTO(String make, String country, String model_name, String category, String car_number,
			int regNum, int price, int mileage, Date model_year, String location, int engine_displacement, 
			String detailed_model_name, String color, String transmission, String fuel, String sellerId) {
		super(regNum, make, detailed_model_name, model_year, price, mileage, location, fuel, color);
		this.category = category;
		this.engine_displacement = engine_displacement;
		this.transmission = transmission;
		this.car_number = car_number;
		this.model_name = model_name;
		this.country = country;
		this.setSellerId(sellerId);
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

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public void printInfo() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM");
		String model_year = sdfDate.format(this.getModel_year());
		System.out.println("제조사 : "+this.getMake());
		System.out.println("모델/세부모델명 : "+this.getModel_name() + "/"+this.getDetailed_model_name());
		System.out.println("차종 : "+this.getCategory());
		System.out.println("차량번호 : " + this.getCar_number());
		System.out.println("연식 : " +model_year);
		System.out.println("주행거리 : "+this.getMileage()+"km");
		System.out.println("지역 : "+this.getLocation());
		System.out.println("배기량 : "+this.getEngine_displacement()+"cc(전기차의 경우 cc는 0입니다.)");
		System.out.println("연료 : "+this.getFuel());
		System.out.println("변속기 : "+this.getTransmission());
		System.out.println("색상 : " +this.getColor());
		System.out.println("가격 : "+this.getPrice()+" 원");
		System.out.println("판매자 : "+this.getSellerId());
	}
}
