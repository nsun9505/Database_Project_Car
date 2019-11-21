package Main;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import page.Pagination;
import vehicle.BasicVehicleInfoDTO;
import vehicle.DetailVehicleInfoDTO;
import vehicle.VehicleDAO;

public class main {
	public static void main(String[] args) {
		VehicleDAO VDao = new VehicleDAO();
		/*
		 * ArrayList<String> metadata = VDao.getTableColumnMetaData("ALL_VEHICLE_INFO");
		 * for(int i=0; i<metadata.size(); i++) System.out.print(metadata.get(i)+ "\t");
		 * System.out.println("\n");
		 */
		ArrayList<String> columnNames = new ArrayList<String>();
		ArrayList<BasicVehicleInfoDTO> list = VDao.getAllSellingVehicleInfo(columnNames);

		System.out.printf("%-20s %-15s %-20s %-10s %-7s %-15s %-20s %-15s %-20s\n", columnNames.get(0),
				columnNames.get(1), columnNames.get(2), columnNames.get(3), columnNames.get(5), columnNames.get(6),
				columnNames.get(7), columnNames.get(8), columnNames.get(4));

		for (int i = 0; i < list.size(); i++) {
			BasicVehicleInfoDTO dto = list.get(i);
			System.out.printf("%-20d %-15s %-20s %-10s %-7d %-15s %-20s %-15s %-20d\n", dto.getRegNum(), dto.getMake(),
					dto.getDetailed_model_name(), dto.getModel_year().toString(), dto.getMileage(), dto.getLocation(),
					dto.getFuel(), dto.getColor(), dto.getPrice());
			if (i == 100)
				break;
		}

		System.out.println(list.size());
	}
}
