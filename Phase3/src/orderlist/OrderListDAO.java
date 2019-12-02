package orderlist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class OrderListDAO {
	private String getRegNumbersByIdQuery = "select registration_number from order_list ";
	private String getMyOrderListQuery = "";
	private String getAllOrderListQuery = "select * from order_list where buyer_id != 'admin'";
	private static final String getTableColumnNamesQuery = "select cname from col where tname=?";
	private String url = "jdbc:oracle:thin:@localhost:1600:xe";
	private String user = "knu";
	private String pw = "comp322";
	private Connection con;
	private PreparedStatement pstmt;

	/*
	 * public static void main(String[] args) { OrderListDAO dao = new
	 * OrderListDAO();
	 * 
	 * //회원 모드 자신의 거래내역 확인 //dao.printMyOrderlist("test48");
	 * 
	 * //관리자 모드 모든 거래내역 확인 //dao.printAllOrderlist();
	 * 
	 * //관리자 모드 매출액 확인 dao.getCostSum();
	 * 
	 * }
	 */

	public void printAllOrderlist() {
		ArrayList<String> metadata = new ArrayList<String>();
		ArrayList<OrderListDTO> list = new ArrayList<OrderListDTO>();

		list = getAllOrderList(metadata);
		System.out.println("모든 거래내역 확인하기");
		System.out.printf("%20s %20s %20s %20s\n", metadata.get(0), metadata.get(1), metadata.get(2), metadata.get(3));

		for (int i = 0; i < list.size(); i++) {
			OrderListDTO dto = list.get(i);
			System.out.printf("%20d %20s %20s %20s\n", dto.getRegistration_number(), dto.getSeller_id(),
					dto.getBuyer_id(), dto.getSell_date().toString());
		}
	}

	public void printMyOrderlist(String id) {
		ArrayList<String> metadata = new ArrayList<String>();
		ArrayList<OrderListDTO> list = new ArrayList<OrderListDTO>();

		list = getMyOrderList(id, metadata);
		System.out.println("내 거래내역 확인하기");
		System.out.printf("%20s %20s %20s %20s\n", metadata.get(0), metadata.get(1), metadata.get(2), metadata.get(3));

		for (int i = 0; i < list.size(); i++) {
			OrderListDTO dto = list.get(i);
			System.out.printf("%20d %20s %20s %20s\n", dto.getRegistration_number(), dto.getSeller_id(),
					dto.getBuyer_id(), dto.getSell_date().toString());
		}
	}

	// commit 추가
	public void deleteOrderListByBuyerId(String buyer_id) {
		try {
			pstmt = con.prepareStatement("delete from order_list where buyer_id=?");
			pstmt.setString(1, buyer_id);
			int ret = pstmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			System.err.println("[deleteOrderListByRegNum] sql error : " + e.getMessage());
		}
	}

	public ArrayList<Integer> getRegNumsById(String id, String account_type) {
		ArrayList<Integer> regnums = new ArrayList<Integer>();
		ResultSet rs = null;
		if (account_type.equals("C"))
			getRegNumbersByIdQuery += " where buyer_id=?";
		else
			getRegNumbersByIdQuery += " where seller_id=?";
		try {
			pstmt = con.prepareStatement(getRegNumbersByIdQuery);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while (rs.next())
				regnums.add(rs.getInt(1));
		} catch (SQLException e) {
			System.err.println("[getRegNumsByBuyerId] sql error : " + e.getMessage());
		}
		return regnums;
	}

	// commit 추가
	public void updateSellerId(String id) {
		int ret = 0;
		try {
			pstmt = con.prepareStatement("update order_list set seller_id='admin' where seller_id=?");
			pstmt.setString(1, id);
			ret = pstmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			System.err.println("");
		}
	}

	public void getCostSum() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("1.연,월 별 매출액 확인\n2.제조사별 매출액 확인\n3. 나가기");
			System.out.print("선택 : ");
			String sel = sc.nextLine();
			String year = "";
			String make = "";
			String month = "";
			int tempmonth = 0;
			int tempyear = 0;
			if (sel.equals("1")) {
				try {
					System.out.println("연도만 검색할 시 월에  0 입력");
					System.out.print("연도 : ");
					tempyear = sc.nextInt();
					sc.nextLine();
					if (tempyear < 0) {
						System.out.println("잘못 입력하셨습니다.");
						continue;
					}
				} catch (InputMismatchException e) {
					System.out.println("연도는 2019와 같은 숫자만 입력해주세요.");
					sc.nextLine();
					continue;
				}
				year = Integer.toString(tempyear);

				try {
					System.out.print("월 (1~12입력): ");
					tempmonth = sc.nextInt();
					sc.nextLine();
					// month = sc.nextLine();
					month = Integer.toString(tempmonth);
				} catch (InputMismatchException e) {
					System.out.println("월은 유효한 범위의 숫자만을 입력해주세요.");
					sc.nextLine();
					continue;
				}
				if (tempmonth > 0 && tempmonth < 10)
					month = "0" + month;

				if (month.equals("0") && year.equals("0")) {
					System.out.println("잘못 입력하셨습니다.");
					continue;
				} else if (month.equals("0")) {
					long sum = getCostSum(year, month, make, 1);
					System.out.println(year + "년 매출액 : " + sum + "원");

				} else {
					long sum = getCostSum(year, month, make, 2);
					System.out.println(year + "년 " + month + "월 매출액 : " + sum + "원");
				}
			} else if (sel.equals("2")) {
				try {
				System.out.println("제조사 선택");
				System.out.println("1. Hyundai");
				System.out.println("2. Kia");
				System.out.println("3. Renaultsamsung");
				System.out.println("4. Chevrolet");
				System.out.println("5. Mercedes_Benz");
				System.out.println("6. Audi");
				System.out.println("7. Bmw");
				System.out.println("8. Volkswagen");
				System.out.print("선택 : ");
				
				int makenum = sc.nextInt();
				switch (makenum) {
				case 1:
					make = "Hyundai";
					break;
				case 2:
					make = "Kia";
					break;
				case 3:
					make = "Renaultsamsung";
					break;
				case 4:
					make = "Chevrolet";
					break;
				case 5:
					make = "Mercedes_Benz";
					break;
				case 6:
					make = "Audi";
					break;
				case 7:
					make = "Bmw";
					break;
				case 8:
					make = "Volkswagen";
					break;
				default :
					System.out.println("유효한 인덱스 값만 입력 부탁드립니다.");
					sc.nextLine();
					continue;
				}
				sc.nextLine();
				}catch(InputMismatchException e) {
					System.out.println("인덱스 값만 입력 부탁드립니다.");
					sc.nextLine();
					continue;
				}
				try {
				System.out.println("제조사별 총매출액 검색 시 연도와 월 모두 0입력 \n연도만 검색할 시 월에 0 입력");
				System.out.print("연도 : ");
				tempyear = sc.nextInt();
				sc.nextLine();
				}catch(InputMismatchException e) {
					System.out.println("연도 값은 2019와 같은 숫자만 입력해주세요.");
					sc.nextLine();
					continue;
				}
				if (tempyear < 0) {
					System.out.println("잘못 입력하셨습니다.");
					continue;
				}
				year = Integer.toString(tempyear);

				try {
				System.out.print("월 (1~12입력): ");
				tempmonth = sc.nextInt();
				sc.nextLine();
				}catch(InputMismatchException e) {
					System.out.println("월은 1~12 사이의 값을 입력해주세요.");
					sc.nextLine();
					continue;
				}
				if(tempmonth < 0 || tempmonth > 12) {
					System.out.println("월은 1~12 사이의 값을 입력해주세요.");
					continue;
				}
				// month = sc.nextLine();
				month = Integer.toString(tempmonth);

				if (tempmonth > 0 && tempmonth < 10)
					month = "0" + month;
				if (month.equals("0") && year.equals("0")) {
					long sum = getCostSum(year, month, make, 6);
					System.out.println("총 매출액 : " + sum + "원");
				} else if (month.equals("0")) {
					long sum = getCostSum(year, month, make, 4);
					System.out.println(year + "년 매출액 : " + sum + "원");
				} else {
					long sum = getCostSum(year, month, make, 5);
					System.out.println(year + "년 " + month + "월 매출액 : " + sum + "원");
				}
			} else {
				break;
			}
		}
	}

	public OrderListDAO() {
		connDB();
	}

	public ArrayList<String> getTableColumnMetaData(String table_name) {
		ArrayList<String> metadata = new ArrayList<String>();
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(getTableColumnNamesQuery);
			pstmt.setString(1, table_name);
			rs = pstmt.executeQuery();

			while (rs.next())
				metadata.add(rs.getString(1));

		} catch (SQLException e) {
			System.err.println("[getTableColumnMetaData()] sql error " + e.getMessage());
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getTableColumnMetaData()] sql error " + e.getMessage());
			}
		}
		return metadata;
	}

	public ArrayList<OrderListDTO> getMyOrderList(String id, ArrayList<String> metadata) {
		ArrayList<OrderListDTO> list = new ArrayList<OrderListDTO>();
		ResultSet rs = null;
		getMyOrderListQuery = "select * from order_list where buyer_id = '" + id + "'";
		try {
			pstmt = con.prepareStatement(getMyOrderListQuery);
			rs = pstmt.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			int cnt = rsmd.getColumnCount();
			for (int i = 1; i <= cnt; i++)
				metadata.add(rsmd.getColumnName(i));
			while (rs.next()) {
				list.add(new OrderListDTO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4)));
			}

		} catch (SQLException e) {
			System.err.println("[getMyOrderList]sql error : " + e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getMyOrderList]sql error : " + e.getMessage());
			}
		}

		return list;
	}

	public ArrayList<OrderListDTO> getAllOrderList(ArrayList<String> metadata) {
		ArrayList<OrderListDTO> list = new ArrayList<OrderListDTO>();
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(getAllOrderListQuery);
			rs = pstmt.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			int cnt = rsmd.getColumnCount();
			for (int i = 1; i <= cnt; i++)
				metadata.add(rsmd.getColumnName(i));
			while (rs.next()) {
				list.add(new OrderListDTO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4)));
			}

		} catch (SQLException e) {
			System.err.println("[getAllOrderList]sql error : " + e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getAllOrderList]sql error : " + e.getMessage());
			}
		}

		return list;
	}

	public long getCostSum(String year, String month, String make, int flag) {
		long sum = 0;

		ResultSet rs = null;
		String query = "";
		if (flag == 1) {// 연도만 입력
			query = "select sum(v.price) " + "from order_list o, vehicle v, detailed_model dm, model m "
					+ "where TO_CHAR(o.sell_date,'YYYY') = '" + year + "' "
					+ "and o.registration_number = v.registration_number ";
		} else if (flag == 2) {// 연도, 월 입력
			query = "select sum(v.price) " + "from order_list o, vehicle v, detailed_model dm, model m "
					+ "where TO_CHAR(o.sell_date,'YYYY-MM') = '" + year + "-" + month + "' "
					+ "and o.registration_number = v.registration_number ";
		} else if (flag == 3) {// 총 매출액
			query = "select sum(v.price) " + "from order_list o, vehicle v "
					+ "where o.registration_number = v.registration_number ";
		} else if (flag == 4) {// 연도 + 제조사 입력
			query = "select sum(v.price) " + "from order_list o, vehicle v, detailed_model dm, model m "
					+ "where TO_CHAR(o.sell_date,'YYYY') = '" + year + "' "
					+ "and o.registration_number = v.registration_number "
					+ "and v.detailed_model_name = dm.detailed_model_name " + "and dm.model_name = m.model_name "
					+ "and m.make = '" + make + "'";
		} else if (flag == 5) {// 연도,월 + 제조사 입력
			query = "select sum(v.price) " + "from order_list o, vehicle v, detailed_model dm, model m "
					+ "where TO_CHAR(o.sell_date,'YYYY-MM') = '" + year + "-" + month + "' "
					+ "and o.registration_number = v.registration_number "
					+ "and v.detailed_model_name = dm.detailed_model_name " + "and dm.model_name = m.model_name "
					+ "and m.make = '" + make + "'";
		} else if (flag == 6) {// 제조사만 입력
			query = "select sum(v.price) " + "from order_list o, vehicle v, detailed_model dm, model m "
					+ "where o.registration_number = v.registration_number "
					+ "and v.detailed_model_name = dm.detailed_model_name " + "and dm.model_name = m.model_name "
					+ "and m.make = '" + make + "'";
		} else {
			return -1;
		}
		try {
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next())
				sum = rs.getLong(1);

		} catch (SQLException e) {
			System.err.println("[getCostSum]sql error : " + e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getCostSum]sql error : " + e.getMessage());
			}
		}

		return sum;
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
