package Main;

import java.util.ArrayList;
import java.util.HashMap;

import vehicle.BasicVehicleInfoDTO;
import vehicle.DetailVehicleInfoDTO;
import vehicle.VehicleDAO;

public class main {
	public static void main(String[] args) {
		VehicleDAO VDao = new VehicleDAO();
		ArrayList<String> metadata = VDao.getTableColumnMetaData("ALL_VEHICLE_INFO");
		for(int i=0; i<metadata.size(); i++)
			System.out.print(metadata.get(i)+ "\t");
		System.out.println("\n");
		
		ArrayList<BasicVehicleInfoDTO> list = VDao.getAllSellingVehicleInfo();
		for(int i=0; i<list.size(); i++) {
			BasicVehicleInfoDTO dto = list.get(i);
			System.out.printf("%-5d %-15s %-20s %-10s %-7d %-15s %-20s %-15s %-20d\n", 
					dto.getRegNum(), dto.getMake(), dto.getDetailed_model_name(), dto.getModel_year().toString(), 
					dto.getMileage(), dto.getLocation(), dto.getFuel(), dto.getColor(), dto.getPrice());
		}
		System.out.println(list.size());
	}
}
