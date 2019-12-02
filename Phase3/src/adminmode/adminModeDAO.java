package adminmode;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

import vehicle.VehicleDAO;

public class adminModeDAO {
	private String openVehicleQuery = "delete from order_list where registration_number = ? AND buyer_id ='admin'";
	private String notOpenVehicleQuery = "insert into order_list values(?, ?, 'admin', sysdate)";
	private static final String regExpCarNum = "^[0-9]{2}[A-Z]{2}[0-9]{4}$";
	private static final String regExpModelYear = "^(19[8-9][0-9]|20[0-1][0-9])-(0[0-9]|1[0-2])$";
	private static final String regExpLocation = "^[A-Z]{1}[a-z]{1,19}$";
	private static final String modifyVehicleQuery = "update vehicle set price=?,mileage=?,model_year=?,location=?,engine_displacement=?,detailed_model_name=?,color=?,transmission=?,fuel=? where registration_number=?";
	// private static final String insertVehicleQuery = "insert into vehicle
	// values(?,?,?,?,?,?,?,?,?,?,?)";
	private String url = "jdbc:oracle:thin:@localhost:1600:xe";
	private String user = "knu";
	private String pw = "comp322";
	private Connection con;
	private PreparedStatement pstmt;

	/*
	 * public static void main(String[] args) { adminModeDAO dao = new
	 * adminModeDAO();
	 * 
	 * //dao.insertVehicle("A"); dao.updateVehicle("A"); }
	 */
	public adminModeDAO() {
		connDB();
	}

	// commit OK
	public void updateVehicle() {
		Scanner sc = new Scanner(System.in);
		HashMap<String, Boolean> flag = new HashMap<String, Boolean>();
		int regNum = 0;
		String make = "";
		String detailed_model_name = "";
		String model_year = "";
		int price = 0;
		int mileage = 0;
		String location = "";
		String fuel = "";
		String color = "";
		String category = "";
		int engine_displacement = 0;
		String transmission = "";
		String car_number = "";
		String model_name = "";
		int ret;
		Date modelyear = null;

		/*
		 * if(account_type.equals("C")) { System.out.println("������ ������ ������ �� �����ϴ�.");
		 * return; }
		 */

		flag.put("price", false);
		flag.put("mileage", false);
		flag.put("model_year", false);
		flag.put("location", false);
		flag.put("engine", false);
		flag.put("detailed_model", false);
		flag.put("color", false);
		flag.put("transmission", false);
		flag.put("fuel", false);
		String sel;
		ResultSet rs;

		while (true) {
			System.out.println("1.������ ������ ��Ϲ�ȣ �Է�");
			System.out.println("2.������ ������ ������ȣ �Է�");
			System.out.println("3.������");
			System.out.print("���� : ");
			sel = sc.nextLine();
			if (sel.equals("1")) {
				regNum = getRegNum(sc, "��Ϲ�ȣ �Է�: ");
				if (regNum == 0)
					continue;
				else
					break;
			} else if (sel.equals("2")) {
				car_number = getCarnumber(sc, "������ȣ �Է�: ", 1);
				if (car_number == null)
					continue;
				else
					break;
			} else if (sel.equals("3"))
				return;
			else
				System.out.println("1 ~ 3 �� ���ڸ� �Է��ϼ���.");
		}

		try {
			if (sel.equals("1")) {
				pstmt = con.prepareStatement("select * from vehicle where registration_number = " + regNum);
			} else {
				pstmt = con.prepareStatement("select * from vehicle where car_number = '" + car_number + "'");
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				car_number = rs.getString(1);
				regNum = rs.getInt(2);
				price = rs.getInt(3);
				mileage = rs.getInt(4);
				// model_year = rs.getString(5);
				modelyear = rs.getDate(5);
				location = rs.getString(6);
				engine_displacement = rs.getInt(7);
				detailed_model_name = rs.getString(8);
				color = rs.getString(9);
				transmission = rs.getString(10);
				fuel = rs.getString(11);
			}

			rs.close();
		} catch (SQLException e) {
			System.err.println("sql error : " + e.getMessage());
			System.exit(1);
		}

		while (true) {
			System.out.println("<<���� ���� ���� �׸� ����>>");
			System.out.println("1. ���� : " + price);
			System.out.println("2. ����Ÿ�: " + mileage);
			if (flag.get("model_year"))
				System.out.println("3. ����: " + model_year);
			else
				System.out.println("3. ����: " + modelyear);
			System.out.println("4. ����: " + location);
			System.out.println("5. ��ⷮ: " + engine_displacement);
			System.out.println("6. ���θ�: " + detailed_model_name);
			System.out.println("7. ����: " + color);
			System.out.println("8. ���ӱ�: " + transmission);
			System.out.println("9. ����: " + fuel);
			System.out.println("10. �������� ���� �� ����");
			System.out.println("11. �������� ���������ʰ� ����");

			System.out.print("�׸� ����: ");

			switch (sc.nextLine()) {
			case "1":
				price = getPrice(sc, "�ǸŰ��� �Է�: ");
				flag.replace("price", true);
				break;
			case "2":
				mileage = getMileage(sc, "����Ÿ� �Է�: ");
				flag.replace("mileage", true);
				break;
			case "3":
				String temp_model_year = getModelyear(sc, "���� �Է�: ");
				if(temp_model_year != null) {
					model_year = temp_model_year + "-01";
					flag.replace("model_year", true);
				}
				break;
			case "4":
				String temp_location = getLocation(sc, "���� �Է� : ");
				if(temp_location != null) {
					location = temp_location;
					flag.replace("location", true);
				}
				break;
			case "5":
				engine_displacement = getEngine(sc, "��ⷮ �Է�: ", detailed_model_name);
				flag.replace("engine", true);
				break;
			case "6":
				make = getMake(sc, "������ �Է�: ");
				model_name = getModelName(sc, "�� �Է�: ", make);
				detailed_model_name = getDetailedModel(sc, "���θ� �Է�: ", model_name);
				engine_displacement = getEngine(sc, "��ⷮ �Է�: ", detailed_model_name);
				flag.replace("detailed_model", true);
				break;
			case "7":
				color = getColor(sc, "���� �Է�: ");
				flag.replace("color", true);
				break;
			case "8":
				transmission = getTransmission(sc, "���ӱ� �Է�: ");
				flag.replace("transmission", true);
				break;
			case "9":
				fuel = getFuel(sc, "���� �Է�: ");
				flag.replace("fuel", true);
				break;
			case "10":
				try {
					pstmt = con.prepareStatement(modifyVehicleQuery);
					pstmt.setInt(1, price);
					pstmt.setInt(2, mileage);
					if (flag.get("model_year"))
						pstmt.setDate(3, Date.valueOf(model_year));
					else
						pstmt.setDate(3, modelyear);
					pstmt.setString(4, location);
					pstmt.setInt(5, engine_displacement);
					pstmt.setString(6, detailed_model_name);
					pstmt.setString(7, color);
					pstmt.setString(8, transmission);
					pstmt.setString(9, fuel);
					pstmt.setInt(10, regNum);
					ret = pstmt.executeUpdate();
					if (ret == 1)
						System.out.println("������ �Ϸ��߽��ϴ�.");
					con.commit();
				} catch (SQLException e) {
					System.err.println("[updateVehicle method] sql error : " + e.getMessage());
					return;
				}
				return;
			case "11":
				System.out.println("���� ������ �������� �ʰ� �����մϴ�.");
				return;
			default:
				System.out.println("1 ~ 11 �� ���ڸ� �Է��ϼ���");
				break;
			}
		}

	}

	// commit OK
	public void insertVehicle(String id) {
		int regNum;
		String make;
		String detailed_model_name;
		String model_year;
		int price;
		int mileage;
		String location;
		String fuel;
		String color;
		String category;
		int engine_displacement;
		String transmission;
		String car_number;
		String model_name;
		Scanner sc = new Scanner(System.in);
		/*
		 * if(account_type.equals("C")) { System.out.println("������ ������ �Ź��� ����� �� �����ϴ�.");
		 * return; }
		 */
		System.out.println("���ο� �Ź� ���");
		while (true) {
			make = getMake(sc, "������ �Է�: ");
			if (make != null)
				break;
		}
		while (true) {
			model_name = getModelName(sc, "�� �Է�: ", make);
			if (model_name != null)
				break;
		}
		while (true) {
			category = getCategory(model_name);
			if (category != null)
				break;
		}
		while (true) {
			detailed_model_name = getDetailedModel(sc, "���θ� �Է�: ", model_name);
			if (detailed_model_name != null)
				break;
		}
		while (true) {
			engine_displacement = getEngine(sc, "��ⷮ �Է�: ", detailed_model_name);
			if (engine_displacement != 0)
				break;
		}
		while (true) {
			color = getColor(sc, "���� �Է�: ");
			if (color != null)
				break;
		}
		while (true) {
			transmission = getTransmission(sc, "���ӱ� �Է�: ");
			if (transmission != null)
				break;
		}
		while (true) {
			fuel = getFuel(sc, "���� �Է�: ");
			if (fuel != null)
				break;
		}
		while (true) {
			car_number = getCarnumber(sc, "������ȣ �Է�: ", 0);
			if (car_number != null)
				break;
		}
		while (true) {
			price = getPrice(sc, "�ǸŰ��� �Է�: ");
			if (price != 0)
				break;
		}
		while (true) {
			mileage = getMileage(sc, "����Ÿ� �Է�: ");
			if (mileage != 0)
				break;
		}
		while (true) {
			model_year = getModelyear(sc, "���� �Է�: ");
			if (model_year != null) {
				break;
			}
		}
		while (true) {
			location = getLocation(sc, "���� �Է�: ");
			if (location != null)
				break;
		}

		try {
			String insertVehicleQuery = "insert into vehicle values('" + car_number + "',registration_seq.nextVal,"
					+ price + "," + mileage + "," + "TO_DATE('" + model_year + "','yyyy-mm')" + ",'" + location + "',"
					+ engine_displacement + ",'" + detailed_model_name + "','" + color + "','" + transmission + "','"
					+ fuel + "','" + id + "')";

			pstmt = con.prepareStatement(insertVehicleQuery);
			/*
			 * pstmt.setString(1, car_number); pstmt.setString(2,
			 * "registration_seq.nextVal"); pstmt.setInt(3, price); pstmt.setInt(4,mileage);
			 * pstmt.setString(5, model_year); pstmt.setString(6, location); pstmt.setInt(7,
			 * engine_displacement); pstmt.setString(8,detailed_model_name);
			 * pstmt.setString(9, color); pstmt.setString(10, transmission);
			 * pstmt.setString(11, fuel);
			 */

			int res = pstmt.executeUpdate();
			if (res == 1)
				System.out.println("�Ź� ��� ����");
			con.commit();
		} catch (SQLException e) {
			System.err.println("sql error : " + e.getMessage());
		}
	}

	private int getRegNum(Scanner sc, String message) {
		int regnum;

		System.out.print(message);
		try {
		regnum = sc.nextInt();
		sc.nextLine();
		}catch(InputMismatchException e) {
			System.out.println("��Ϲ�ȣ�� ���ڸ� �Է��� �� �ֽ��ϴ�.");
			sc.nextLine();
			return 0;
		}
		ArrayList<Integer> list = new ArrayList<Integer>();
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement("select registration_number from vehicle");
			rs = pstmt.executeQuery();

			while (rs.next())
				list.add(rs.getInt(1));

		} catch (SQLException e) {
			System.err.println("[getRegNum] sql error " + e.getMessage());
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getRegNum] sql error " + e.getMessage());
			}
		}

		for (int i = 0; i < list.size(); i++) {
			if (regnum == list.get(i)) {
				return regnum;
			}

		}

		System.out.println("��ϵǾ� ���� ���� ��ȣ�Դϴ�.");
		return 0;
	}

	private String getLocation(Scanner sc, String message) {
		String location = "";

		System.out.println("���� �Է� ����: ����� �Է� (2~20����,ù���ڴ� �빮��)");

		System.out.print(message);

		location = sc.nextLine();
		if (validCheck(location, regExpLocation) == false) {
			System.out.println("��ȿ���� ���� ���� �Դϴ�. : ex)Daegu");
			return null;
		}

		return location;
	}

	private String getModelyear(Scanner sc, String message) {

		String modelyear;

		System.out.println("���� �Է����� : YYYY-MM (1980�� ���ĸ� ��ϰ���)");
		System.out.print(message);
		modelyear = sc.nextLine();

		if (validCheck(modelyear, regExpModelYear) == false) {
			System.out.println("��ȿ���� ���� ���� �Դϴ�. : ex)YYYY-MM");
			return null;
		}

		return modelyear;
	}

	private int getMileage(Scanner sc, String message) {
		int mileage = 0;

		while (true) {
			try {
			System.out.print(message);
			mileage = sc.nextInt();
			sc.nextLine();
			if (mileage < 0)
				System.out.println("����Ÿ��� ������ �� �� �����ϴ�. �ٽ� �Է� ��Ź�帳�ϴ�.");
			else
				break;
			} catch (InputMismatchException e) {
				System.out.println("����Ÿ� ������ ����� �Է��ؾ� �մϴ�.");
				sc.nextLine();
			}
		}
		return mileage;
	}

	private int getPrice(Scanner sc, String message) {
		int price = 0;

		while (true) {
			try {
				System.out.print(message);
				price = sc.nextInt();
				sc.nextLine();
				if (price < 0)
					System.out.println("������ ������ �� �� �����ϴ�. �ٽ� �Է� ��Ź�帳�ϴ�.");
				else
					break;
			} catch (InputMismatchException e) {
				System.out.println("���� ������ ����� �Է��ؾ� �մϴ�.");
				sc.nextLine();
			}
		}
		return price;
	}

	private String getCarnumber(Scanner sc, String message, int flag) {
		String carnum = "";

		System.out.println("������ȣ �Է� ���� : 12GA5839");
		System.out.print(message);

		carnum = sc.nextLine();
		if (validCheck(carnum, regExpCarNum) == false) {
			System.out.println("��ȿ���� ���� ���� �Դϴ�. : ex)12GA5839");
			return null;
		}

		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement("select Car_number from vehicle");
			rs = pstmt.executeQuery();

			while (rs.next())
				list.add(rs.getString(1));

		} catch (SQLException e) {
			System.err.println("[getCarnumber] sql error " + e.getMessage());
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getCarnumber] sql error " + e.getMessage());
			}
		}

		for (int i = 0; i < list.size(); i++) {
			if (flag == 0) {
				if (carnum.equals(list.get(i))) {
					System.out.println("�̹� ��ϵ� ������ȣ�Դϴ�.");
					return null;
				}
			} else if (flag == 1) {
				if (carnum.equals(list.get(i))) {
					return carnum;
				}
			}
		}

		if (flag == 0)
			return carnum;
		else {
			System.out.println("��ϵǾ� ���� ���� ������ȣ�Դϴ�.");
			return null;
		}
	}

	private String getMake(Scanner sc, String message) {
		String make = "";
		int num;

		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement("select make from make");
			rs = pstmt.executeQuery();

			while (rs.next())
				list.add(rs.getString(1));

		} catch (SQLException e) {
			System.err.println("[getMake] sql error " + e.getMessage());
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getMake] sql error " + e.getMessage());
			}
		}

		while (true) {
			try {
				System.out.println("������ ��� : ");
				for (int i = 0; i < list.size(); i++) {
					System.out.println((i + 1) + "." + list.get(i));
				}
				System.out.print(message);
				num = sc.nextInt();
				sc.nextLine();
				if (num < 1 || num > list.size())
					System.out.println("��ȿ�� ���� ������ �ƴմϴ�. �ٽ� �Է� ��Ź�帳�ϴ�.");
				else
					break;
			} catch (InputMismatchException e) {
				System.out.println("�ε��� ���� �Է� ��Ź�帳�ϴ�.");
				sc.nextLine();
			}
		}

		make = list.get(num - 1);
		return make;
	}

	private String getModelName(Scanner sc, String message, String make) {
		String modelname = "";
		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement("select model_name from model where make = '" + make + "'");
			rs = pstmt.executeQuery();

			while (rs.next())
				list.add(rs.getString(1));

		} catch (SQLException e) {
			System.err.println("[getModelName] sql error " + e.getMessage());
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getModelName] sql error " + e.getMessage());
			}
		}

		int num = 0;
		while (true) {
			try {
				System.out.println("�� ��� : ");
				for (int i = 0; i < list.size(); i++) {
					System.out.println((i + 1) + "." + list.get(i));
				}
				
				System.out.print(message);
				num = sc.nextInt();
				sc.nextLine();
				if (num < 1 || num > list.size())
					System.out.println("��ȿ�� ���� ������ �ƴմϴ�. �ٽ� �Է� ��Ź�帳�ϴ�.");
				else
					break;
			} catch (InputMismatchException e) {
				System.out.println("�ε��� ���� �Է� ��Ź�帳�ϴ�.");
				sc.nextLine();
			}
		}

		modelname = list.get(num - 1);

		return modelname;
	}

	private String getCategory(String modelname) {
		String category = "";
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement("select category from model where model_name = '" + modelname + "'");
			rs = pstmt.executeQuery();

			while (rs.next())
				category = rs.getString(1);

		} catch (SQLException e) {
			System.err.println("[getCategory] sql error " + e.getMessage());
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getCategory] sql error " + e.getMessage());
			}
		}

		return category;
	}

	private String getDetailedModel(Scanner sc, String message, String modelname) {
		String detailedmodel = "";

		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(
					"select detailed_model_name from detailed_model where model_name = '" + modelname + "'");
			rs = pstmt.executeQuery();

			while (rs.next())
				list.add(rs.getString(1));

		} catch (SQLException e) {
			System.err.println("[getDetailedModel] sql error " + e.getMessage());
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getDetailedModel] sql error " + e.getMessage());
			}
		}

		int num = 0;
		while (true) {
			try {
				System.out.println("���θ� ��� : ");
				for (int i = 0; i < list.size(); i++) {
					System.out.println((i + 1) + "." + list.get(i));
				}
				
				System.out.print(message);
				num = sc.nextInt();
				sc.nextLine();
				if (num < 1 || num > list.size())
					System.out.println("��ȿ�� ���� ������ �ƴմϴ�. �ٽ� �Է� ��Ź�帳�ϴ�.");
				else
					break;
			}  catch (InputMismatchException e) {
				System.out.println("�ε��� ���� �Է� ��Ź�帳�ϴ�.");
				sc.nextLine();
			}
		}

		detailedmodel = list.get(num - 1);

		return detailedmodel;
	}

	private int getEngine(Scanner sc, String message, String detailedmodel) {
		int engine = 0;

		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(
					"select engine_displacement from engine_displacement where detailed_model_name = '" + detailedmodel
							+ "'");
			rs = pstmt.executeQuery();

			while (rs.next())
				list.add(rs.getString(1));

		} catch (SQLException e) {
			System.err.println("[getEngine] sql error " + e.getMessage());
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getEngine] sql error " + e.getMessage());
			}
		}

		int num = 0;
		while (true) {
			try {
				System.out.println("��ⷮ ��� : ");
				for (int i = 0; i < list.size(); i++) {
					System.out.println((i + 1) + "." + list.get(i));
				}
				System.out.print(message);
				num = sc.nextInt();
				sc.nextLine();
				if (num < 1 || num > list.size())
					System.out.println("��ȿ�� ���� ������ �ƴմϴ�. �ٽ� �Է� ��Ź�帳�ϴ�.");
				else
					break;
			}  catch (InputMismatchException e) {
				System.out.println("�ε��� ���� �Է� ��Ź�帳�ϴ�.");
				sc.nextLine();
			}
		}

		engine = Integer.parseInt(list.get(num - 1));

		return engine;
	}

	private String getColor(Scanner sc, String message) {
		String color = "";

		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement("select color from color");
			rs = pstmt.executeQuery();

			while (rs.next())
				list.add(rs.getString(1));

		} catch (SQLException e) {
			System.err.println("[getColor] sql error " + e.getMessage());
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getColor] sql error " + e.getMessage());
			}
		}

		int num = 0;
		while (true) {
			try {
				System.out.println("���� ��� : ");
				for (int i = 0; i < list.size(); i++) {
					System.out.println((i + 1) + "." + list.get(i));
				}
				
				System.out.print(message);
				num = sc.nextInt();
				sc.nextLine();
				if (num < 1 || num > list.size())
					System.out.println("��ȿ�� ���� ������ �ƴմϴ�. �ٽ� �Է� ��Ź�帳�ϴ�.");
				else
					break;
			}  catch (InputMismatchException e) {
				System.out.println("�ε��� ���� �Է� ��Ź�帳�ϴ�.");
				sc.nextLine();
			}
		}

		color = list.get(num - 1);

		return color;
	}

	private String getTransmission(Scanner sc, String message) {
		String trans = "";

		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement("select transmission from transmission");
			rs = pstmt.executeQuery();

			while (rs.next())
				list.add(rs.getString(1));

		} catch (SQLException e) {
			System.err.println("[getTransmission] sql error " + e.getMessage());
		} finally {
			try { 
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getTransmission] sql error " + e.getMessage());
			}
		}

		int num = 0;
		while (true) {
			try {
				System.out.println("���ӱ� ��� : ");
				for (int i = 0; i < list.size(); i++) {
					System.out.println((i + 1) + "." + list.get(i));
				}

				System.out.print(message);
				num = sc.nextInt();
				sc.nextLine();
				if (num < 1 || num > list.size())
					System.out.println("��ȿ�� ���� ������ �ƴմϴ�. �ٽ� �Է� ��Ź�帳�ϴ�.");
				else
					break;
			}   catch (InputMismatchException e) {
				System.out.println("�ε��� ���� �Է� ��Ź�帳�ϴ�.");
				sc.nextLine();
			}
		}
		trans = list.get(num - 1);

		return trans;
	}

	private String getFuel(Scanner sc, String message) {
		String fuel = "";

		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement("select fuel from fuel");
			rs = pstmt.executeQuery();

			while (rs.next())
				list.add(rs.getString(1));

		} catch (SQLException e) {
			System.err.println("[getFuel] sql error " + e.getMessage());
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getFuel] sql error " + e.getMessage());
			}
		}

		int num = 0;
		while (true) {
			try {
				System.out.println("���� ��� : ");
				for (int i = 0; i < list.size(); i++) {
					System.out.println((i + 1) + "." + list.get(i));
				}

				System.out.print(message);
				num = sc.nextInt();
				sc.nextLine();
				if (num < 1 || num > list.size())
					System.out.println("��ȿ�� ���� ������ �ƴմϴ�. �ٽ� �Է� ��Ź�帳�ϴ�.");
				else
					break;

			}   catch (InputMismatchException e) {
				System.out.println("�ε��� ���� �Է� ��Ź�帳�ϴ�.");
				sc.nextLine();
			}
		}
		fuel = list.get(num - 1);

		return fuel;
	}

	// commit �߰� OK
	public void openVehicle() {
		int ret = 0;
		int regNum;
		while (true) {
			try {
				Scanner sc = new Scanner(System.in);
				System.out.print("���� ó���� ��Ϲ�ȣ �Է� : ");
				String sel = sc.nextLine();
				regNum = Integer.parseInt(sel);
				if (regNum > 0)
					break;
				else
					System.out.println("����� �Է����ּ���.");
			} catch (NumberFormatException e) {
				System.out.println("���ڸ� �Է����ּ���.");
			}
		}
		try {
			pstmt = con.prepareStatement(openVehicleQuery);
			pstmt.setInt(1, regNum);
			ret = pstmt.executeUpdate();
			if (ret > 0)
				System.out.println(regNum + "���� ���� ó�� �Ǿ����ϴ�.");
			else if (ret == 0)
				System.out.println("���� ó�� ����");
			con.commit();
		} catch (SQLException e) {
			System.err.println("[openVehicle] sql error : " + e.getMessage());
		}
	}

	// commit �߰� OK
	public void notOpenVehicle(String id) {
		int ret = 0;
		int regNum;
		ResultSet rs = null;
		while (true) {
			try {
				Scanner sc = new Scanner(System.in);
				System.out.print("����� ó���� ��Ϲ�ȣ �Է� : ");
				String sel = sc.nextLine();
				regNum = Integer.parseInt(sel);
				if (regNum > 0)
					break;
				else
					System.out.println("����� �Է����ּ���.");
			} catch (NumberFormatException e) {
				System.out.println("���ڸ� �Է����ּ���.");
			}
		}

		VehicleDAO VDao = new VehicleDAO();
		if (VDao.getVehicleInfoByRegNum(regNum) == null) {
			System.out.println("���� �Ǹ����� ������ �ƴմϴ�.");
			return;
		}

		try {
			pstmt = con.prepareStatement(notOpenVehicleQuery);
			pstmt.setInt(1, regNum);
			pstmt.setString(2, id);
			ret = pstmt.executeUpdate();
			if (ret > 0)
				System.out.println(regNum + "���� ����� ó�� �Ǿ����ϴ�.");
			con.commit();
		} catch (SQLException e) {
			System.err.println("[openVehicle] sql error : " + e.getMessage());
		}
	}

	private boolean validCheck(String src, String regExp) {
		return src.matches(regExp);
	}

	private void connDB() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(url, user, pw);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("sql error : " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void finalize() throws Throwable {
		pstmt.close();
		con.close();
	}
}
