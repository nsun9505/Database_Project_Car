package com.vehicle.service;

import java.sql.Date;
import java.util.*;

import com.vehicle.dao.VehicleDAO;
import com.vehicle.vo.VehicleVO;

public class VehicleService {
	VehicleDAO vehicleDAO;

	public VehicleService() {
		this.vehicleDAO = new VehicleDAO();
	}

	public VehicleVO carInfo(int regnum) {
		VehicleVO car = null;

		car = vehicleDAO.carInfo(regnum);

		return car;
	}

	public boolean modifyVehicle(int regnum, String detailed_model_name, String model_year, int price, int mileage,
			String location, String fuel, String color, int engine_displacement, String transmission, String car_number,
			String sellerId) {
		boolean ret = false;

		VehicleVO newVehicle = new VehicleVO(detailed_model_name, model_year, price, mileage, location, fuel, color,
				engine_displacement, transmission, car_number, sellerId);

		ret = vehicleDAO.modifyVehicle(newVehicle, regnum);

		if (ret == true)
			System.out.println("성공!!!!");

		return ret;
	}

	public boolean addVehicle(String detailed_model_name, String model_year, int price, int mileage, String location,
			String fuel, String color, int engine_displacement, String transmission, String car_number,
			String sellerId) {
		boolean ret = false;

		VehicleVO newVehicle = new VehicleVO(detailed_model_name, model_year, price, mileage, location, fuel, color,
				engine_displacement, transmission, car_number, sellerId);

		ret = vehicleDAO.insertVehicle(newVehicle);

		if (ret == true)
			System.out.println("성공!!!!");

		return ret;
	}

	public HashMap<String, ArrayList<String>> initCondition() {
		HashMap<String, ArrayList<String>> conditions = new HashMap<String, ArrayList<String>>();
		conditions.put("category", getCategoryList());
		conditions.put("make", getMakeList());
		conditions.put("location", getLocationList());
		conditions.put("color", getColorList());
		conditions.put("fuel", getFuelList());
		conditions.put("transmission", getTransList());
		// TODO Auto-generated method stub
		return conditions;
	}

	public ArrayList<String> getLocationList() {
		ArrayList<String> locationsList = vehicleDAO.getLocationList();
		return locationsList;
	}

	public ArrayList<String> getCategoryList() {
		ArrayList<String> A = vehicleDAO.getCategory();
		return A;
	}

	public ArrayList<String> getMakeList() {
		ArrayList<String> A = vehicleDAO.getMake();
		return A;
	}

	public ArrayList<String> getModelList(String make) {
		ArrayList<String> A = vehicleDAO.getModel(make);
		return A;
	}

	public ArrayList<String> getDetailelList(String model) {
		ArrayList<String> A = vehicleDAO.getDetaile(model);
		return A;
	}

	public ArrayList<String> getEngineList(String detaile) {
		ArrayList<String> A = vehicleDAO.getEngine(detaile);
		return A;
	}

	public ArrayList<String> getFuelList() {
		ArrayList<String> A = vehicleDAO.getFuel();
		return A;
	}

	public ArrayList<String> getColorList() {
		ArrayList<String> A = vehicleDAO.getColor();
		return A;
	}

	public ArrayList<String> getTransList() {
		ArrayList<String> A = vehicleDAO.getTrans();
		return A;
	}

	public String checkCarnumber(String carnum) {
		String A = vehicleDAO.getCarnumber(carnum, 0);
		return A;
	}

	private static boolean setColumnCondition(String column, HashMap<String, ArrayList<String>> conditions) {
		// 현재 설정된 조건 항목에 의해 선택된 항목을 출력하기 위해 where절 갱신
		// 갱신된 where절에 의해 필터링된 category 리스트를 가져옴.
		// 필터링된 category 중 선택할 항목을 선택한 후 conditions를 갱신
		VehicleDAO VDao = new VehicleDAO();
		String whereClusure = getWhereClusureByColumn(conditions, column);
		ArrayList<String> list = VDao.getConditionList(column, whereClusure);
		return setConditionForColumn(list, conditions, column);
	}

	private static boolean setConditionForColumn(ArrayList<String> list, HashMap<String, ArrayList<String>> conditions,
			String column) {
		Scanner sc = new Scanner(System.in);
		String sel = null;
		while (true) {
			for (int i = 0; i < list.size(); i++)
				System.out.println(i + 1 + " " + list.get(i));

			System.out.print(column + " 선택[exit입력 시 뒤로 돌아감] : ");
			sel = sc.nextLine();
			if (sel.equals(""))
				return false;

			if (sel.equals("exit"))
				return false;

			try {
				int columnSel = Integer.parseInt(sel);
				if (columnSel <= 0 || columnSel >= list.size() + 1) {
					System.out.println("다시 선택해 주세요.");
				} else {
					ArrayList<String> condition = null;

					if (conditions.get(column) == null) {
						condition = new ArrayList<String>();
						conditions.put(column, condition);
					}
					condition = conditions.get(column);

					if (condition.contains(list.get(columnSel - 1))) {
						System.out.println("해당 조건은 이미 선택되었습니다.");
						return false;
					} else {
						condition.add(list.get(columnSel - 1));
						conditions.replace(column, condition);
						return true;
					}
				}
			} catch (NumberFormatException e) {
				System.out.println("입력하신 값은 유효한 값이 아닙니다. 다시 입력해 주세요.");
			}
		}
	}

	private static String getWhereClusureByColumn(HashMap<String, ArrayList<String>> conditions, String column) {
		ArrayList<String> list = null;
		ArrayList<String> query = new ArrayList<String>();
		boolean flag = false;

		if (column.equals("category") == false && conditions.containsKey("category"))
			addCategoryCondition(conditions.get("category"), query);

		// 제조사 및 모델 조건 추가
		if (column.equals("detailed_model_name") == false && conditions.containsKey("detailed_model_name"))
			flag = addDetailedModelNameCondition(conditions.get("detailed_model_name"), query);
		else if (column.equals("model_name") == false && conditions.containsKey("model_name"))
			flag = addModelCondition(conditions.get("model_name"), query);
		else if (column.equals("make") == false && conditions.containsKey("make"))
			addMakeCondition(conditions.get("make"), query);

		// 연식 최소 추가
		if (column.equals("min_model_year") == false && conditions.containsKey("min_model_year"))
			addMinModelYearCondition(conditions.get("min_model_year"), query);

		// 연식 최대 추가
		if (column.equals("max_model_year") == false && conditions.containsKey("max_model_year"))
			addMaxModelYearCondition(conditions.get("max_model_year"), query);

		// 최소 주행거리 추가
		if (column.equals("min_mileage") == false && conditions.containsKey("min_mileage"))
			addMinMileageCondition(conditions.get("min_mileage"), query);

		// 최대 주행거리 추가
		if (column.equals("max_mileage") == false && conditions.containsKey("max_mileage"))
			addMaxMileageCondition(conditions.get("max_mileage"), query);

		// 최소 가격 추가
		if (column.equals("min_pirce") == false && conditions.containsKey("min_price"))
			addMinPriceCondition(conditions.get("min_price"), query);
		// 최대 가격 추가
		if (column.equals("max_price") == false && conditions.containsKey("max_price"))
			addMaxPriceCondition(conditions.get("max_price"), query);

		// 색상 추가
		if (column.equals("color") == false && conditions.containsKey("color"))
			addColorCondition(conditions.get("color"), query);

		// 지역 추가
		if (column.equals("location") == false && conditions.containsKey("location"))
			addLocationCondition(conditions.get("location"), query);

		// 변속기 옵션 추가
		if (column.equals("transmission") == false && conditions.containsKey("transmission"))
			addTransmissionCondition(conditions.get("transmission"), query);

		// 연료 조건 추가
		if (column.equals("fuel") == false && conditions.containsKey("fuel"))
			addFuelCondition(conditions.get("fuel"), query);

		return combinationConditions(query);
	}

	private static void addCategoryCondition(ArrayList<String> list, ArrayList<String> query) {
		String category = "";
		for (int i = 0; i < list.size(); i++)
			category += "category='" + list.get(i) + "' " + (i == list.size() - 1 ? "" : " OR ");
		if (list.size() > 1)
			category = "(" + category + ")";
		if (category.equals("") == false)
			query.add(category);
	}

	private static boolean addDetailedModelNameCondition(ArrayList<String> list, ArrayList<String> query) {
		String detailed_model_name = "";
		for (int i = 0; i < list.size(); i++)
			detailed_model_name += "detailed_model_name='" + list.get(i) + "' " + (i == list.size() - 1 ? "" : " OR ");
		if (list.size() > 1)
			detailed_model_name = "(" + detailed_model_name + ")";
		if (detailed_model_name.equals("") == false) {
			query.add(detailed_model_name);
			return true;
		}
		return false;
	}

	private static boolean addModelCondition(ArrayList<String> list, ArrayList<String> query) {
		String model = "";
		for (int i = 0; i < list.size(); i++)
			model += "model_name='" + list.get(i) + "' " + (i == list.size() - 1 ? "" : " OR ");
		if (list.size() > 1)
			model = "(" + model + ")";
		if (model.equals("") == false) {
			query.add(model);
			return true;
		}
		return false;
	}

	private static boolean addMakeCondition(ArrayList<String> list, ArrayList<String> query) {
		String make = "";
		for (int i = 0; i < list.size(); i++)
			make += "make='" + list.get(i) + "' " + (i == list.size() - 1 ? "" : " OR ");

		if (make.equals("") == false) {
			query.add(make);
			return true;
		}
		return false;
	}

	private static void addMinModelYearCondition(ArrayList<String> list, ArrayList<String> query) {
		query.add("model_year >= TO_DATE('" + list.get(0) + "', ('YYYY'))");
	}

	private static void addMaxModelYearCondition(ArrayList<String> list, ArrayList<String> query) {
		query.add("model_year <= TO_DATE('" + list.get(0) + "', ('YYYY'))");
	}

	private static void addMinMileageCondition(ArrayList<String> list, ArrayList<String> query) {
		query.add("mileage >=" + list.get(0));
	}

	private static void addMaxMileageCondition(ArrayList<String> list, ArrayList<String> query) {
		query.add("mileage <=" + list.get(0));
	}

	private static void addMinPriceCondition(ArrayList<String> list, ArrayList<String> query) {
		query.add("price >=" + list.get(0));
	}

	private static void addMaxPriceCondition(ArrayList<String> list, ArrayList<String> query) {
		query.add("price <=" + list.get(0));
	}

	private static void addColorCondition(ArrayList<String> list, ArrayList<String> query) {
		String color = "";
		for (int i = 0; i < list.size(); i++)
			color += "color='" + list.get(i) + "' " + (i == list.size() - 1 ? "" : " OR ");
		if (list.size() > 1)
			color = "(" + color + ")";
		if (color.equals("") == false)
			query.add(color);
	}

	private static void addLocationCondition(ArrayList<String> list, ArrayList<String> query) {
		String location = "";
		for (int i = 0; i < list.size(); i++)
			location += "location='" + list.get(i) + "' " + (i == list.size() - 1 ? "" : " OR ");
		if (list.size() > 1)
			location = "(" + location + ")";
		if (location.equals("") == false)
			query.add(location);
	}

	private static void addTransmissionCondition(ArrayList<String> list, ArrayList<String> query) {
		String transmission = "";
		for (int i = 0; i < list.size(); i++)
			transmission += "transmission='" + list.get(i) + "' " + (i == list.size() - 1 ? "" : " OR ");
		if (list.size() > 1)
			transmission = "(" + transmission + ")";
		if (transmission.equals("") == false)
			query.add(transmission);
	}

	private static void addFuelCondition(ArrayList<String> list, ArrayList<String> query) {
		String fuel = "";
		for (int i = 0; i < list.size(); i++)
			fuel += "fuel='" + list.get(i) + "' " + (i == list.size() - 1 ? "" : "OR ");
		if (list.size() > 1)
			fuel = "(" + fuel + ")";
		if (fuel.equals("") == false)
			query.add(fuel);
	}

	private static String combinationConditions(ArrayList<String> query) {
		if (query.isEmpty())
			return null;
		String ret = "";
		for (int i = 0; i < query.size(); i++)
			ret += query.get(i) + (i == query.size() - 1 ? " " : " AND ");
		return ret;
	}

	public ArrayList<VehicleVO> getInitList() {
		ArrayList<VehicleVO> ret = vehicleDAO.getInitList();
		return null;
	}

	public ArrayList<VehicleVO> getVehicleListByQuery(HashMap<String, ArrayList<String>> conditions) {
		ArrayList<String> query = new ArrayList<String>();
		if (conditions.containsKey("category")) {
			ArrayList<String> list = conditions.get("category");
			addCategoryCondition(list, query);
		}
		if (conditions.containsKey("make")) {
			ArrayList<String> list = conditions.get("category");
			addMakeCondition(list, query);
		}
		if (conditions.containsKey("model_name")) {
			ArrayList<String> list = conditions.get("model_name");
			addModelCondition(list, query);
		}
		if (conditions.containsKey("detailed_model_name")) {
			ArrayList<String> list = conditions.get("detailed_model_name");
			addDetailedModelNameCondition(list, query);
		}
		if (conditions.containsKey("min_model_year")) {
			ArrayList<String> list = conditions.get("min_model_year");
			addMinModelYearCondition(list, query);
		}
		if (conditions.containsKey("max_model_year")) {
			ArrayList<String> list = conditions.get("max_model_year");
			addMaxModelYearCondition(list, query);
		}
		if (conditions.containsKey("min_mileage")) {
			ArrayList<String> list = conditions.get("min_mileage");
			addMinMileageCondition(list, query);
		}
		if (conditions.containsKey("max_mileage")) {
			ArrayList<String> list = conditions.get("max_mileage");
			addMaxMileageCondition(list, query);
		}
		if (conditions.containsKey("min_price")) {
			ArrayList<String> list = conditions.get("min_price");
			addMinPriceCondition(list, query);
		}
		if (conditions.containsKey("max_price")) {
			ArrayList<String> list = conditions.get("max_price");
			addMaxPriceCondition(list, query);
		}
		if (conditions.containsKey("location")) {
			ArrayList<String> list = conditions.get("location");
			addLocationCondition(list, query);
		}
		if (conditions.containsKey("color")) {
			ArrayList<String> list = conditions.get("color");
			addColorCondition(list, query);
		}
		if (conditions.containsKey("fuel")) {
			ArrayList<String> list = conditions.get("fuel");
			addFuelCondition(list, query);
		}
		if (conditions.containsKey("transmission")) {
			ArrayList<String> list = conditions.get("transmission");
			addTransmissionCondition(list, query);
		}
		String ret = combinationConditions(query);
		ArrayList<VehicleVO> vehicle_list = vehicleDAO.getVehicleListByQuery(ret);
		return vehicle_list;
	}
}
