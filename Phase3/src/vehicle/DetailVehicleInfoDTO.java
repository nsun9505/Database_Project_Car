package vehicle;

import java.sql.Date;

public class DetailVehicleInfoDTO extends BasicVehicleInfoDTO{
	
	public DetailVehicleInfoDTO(int regNum, String make, String detailed_model_name, Date model_year, int price,
			int mileage, String location, String fuel, String color) {
		super(regNum, make, detailed_model_name, model_year, price, mileage, location, fuel, color);
		
	}
	
}
