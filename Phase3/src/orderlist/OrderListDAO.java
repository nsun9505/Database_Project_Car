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
	 * //ȸ�� ��� �ڽ��� �ŷ����� Ȯ�� //dao.printMyOrderlist("test48");
	 * 
	 * //������ ��� ��� �ŷ����� Ȯ�� //dao.printAllOrderlist();
	 * 
	 * //������ ��� ����� Ȯ�� dao.getCostSum();
	 * 
	 * }
	 */

	public void printAllOrderlist() {
		ArrayList<String> metadata = new ArrayList<String>();
		ArrayList<OrderListDTO> list = new ArrayList<OrderListDTO>();

		list = getAllOrderList(metadata);
		System.out.println("��� �ŷ����� Ȯ���ϱ�");
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
		System.out.println("�� �ŷ����� Ȯ���ϱ�");
		System.out.printf("%20s %20s %20s %20s\n", metadata.get(0), metadata.get(1), metadata.get(2), metadata.get(3));

		for (int i = 0; i < list.size(); i++) {
			OrderListDTO dto = list.get(i);
			System.out.printf("%20d %20s %20s %20s\n", dto.getRegistration_number(), dto.getSeller_id(),
					dto.getBuyer_id(), dto.getSell_date().toString());
		}
	}

	// commit �߰�
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

	// commit �߰�
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
			System.out.println("1.��,�� �� ����� Ȯ��\n2.�����纰 ����� Ȯ��\n3. ������");
			System.out.print("���� : ");
			String sel = sc.nextLine();
			String year = "";
			String make = "";
			String month = "";
			int tempmonth = 0;
			int tempyear = 0;
			if (sel.equals("1")) {
				try {
					System.out.println("������ �˻��� �� ����  0 �Է�");
					System.out.print("���� : ");
					tempyear = sc.nextInt();
					sc.nextLine();
					if (tempyear < 0) {
						System.out.println("�߸� �Է��ϼ̽��ϴ�.");
						continue;
					}
				} catch (InputMismatchException e) {
					System.out.println("������ 2019�� ���� ���ڸ� �Է����ּ���.");
					sc.nextLine();
					continue;
				}
				year = Integer.toString(tempyear);

				try {
					System.out.print("�� (1~12�Է�): ");
					tempmonth = sc.nextInt();
					sc.nextLine();
					// month = sc.nextLine();
					month = Integer.toString(tempmonth);
				} catch (InputMismatchException e) {
					System.out.println("���� ��ȿ�� ������ ���ڸ��� �Է����ּ���.");
					sc.nextLine();
					continue;
				}
				if (tempmonth > 0 && tempmonth < 10)
					month = "0" + month;

				if (month.equals("0") && year.equals("0")) {
					System.out.println("�߸� �Է��ϼ̽��ϴ�.");
					continue;
				} else if (month.equals("0")) {
					long sum = getCostSum(year, month, make, 1);
					System.out.println(year + "�� ����� : " + sum + "��");

				} else {
					long sum = getCostSum(year, month, make, 2);
					System.out.println(year + "�� " + month + "�� ����� : " + sum + "��");
				}
			} else if (sel.equals("2")) {
				try {
				System.out.println("������ ����");
				System.out.println("1. Hyundai");
				System.out.println("2. Kia");
				System.out.println("3. Renaultsamsung");
				System.out.println("4. Chevrolet");
				System.out.println("5. Mercedes_Benz");
				System.out.println("6. Audi");
				System.out.println("7. Bmw");
				System.out.println("8. Volkswagen");
				System.out.print("���� : ");
				
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
					System.out.println("��ȿ�� �ε��� ���� �Է� ��Ź�帳�ϴ�.");
					sc.nextLine();
					continue;
				}
				sc.nextLine();
				}catch(InputMismatchException e) {
					System.out.println("�ε��� ���� �Է� ��Ź�帳�ϴ�.");
					sc.nextLine();
					continue;
				}
				try {
				System.out.println("�����纰 �Ѹ���� �˻� �� ������ �� ��� 0�Է� \n������ �˻��� �� ���� 0 �Է�");
				System.out.print("���� : ");
				tempyear = sc.nextInt();
				sc.nextLine();
				}catch(InputMismatchException e) {
					System.out.println("���� ���� 2019�� ���� ���ڸ� �Է����ּ���.");
					sc.nextLine();
					continue;
				}
				if (tempyear < 0) {
					System.out.println("�߸� �Է��ϼ̽��ϴ�.");
					continue;
				}
				year = Integer.toString(tempyear);

				try {
				System.out.print("�� (1~12�Է�): ");
				tempmonth = sc.nextInt();
				sc.nextLine();
				}catch(InputMismatchException e) {
					System.out.println("���� 1~12 ������ ���� �Է����ּ���.");
					sc.nextLine();
					continue;
				}
				if(tempmonth < 0 || tempmonth > 12) {
					System.out.println("���� 1~12 ������ ���� �Է����ּ���.");
					continue;
				}
				// month = sc.nextLine();
				month = Integer.toString(tempmonth);

				if (tempmonth > 0 && tempmonth < 10)
					month = "0" + month;
				if (month.equals("0") && year.equals("0")) {
					long sum = getCostSum(year, month, make, 6);
					System.out.println("�� ����� : " + sum + "��");
				} else if (month.equals("0")) {
					long sum = getCostSum(year, month, make, 4);
					System.out.println(year + "�� ����� : " + sum + "��");
				} else {
					long sum = getCostSum(year, month, make, 5);
					System.out.println(year + "�� " + month + "�� ����� : " + sum + "��");
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
		if (flag == 1) {// ������ �Է�
			query = "select sum(v.price) " + "from order_list o, vehicle v, detailed_model dm, model m "
					+ "where TO_CHAR(o.sell_date,'YYYY') = '" + year + "' "
					+ "and o.registration_number = v.registration_number ";
		} else if (flag == 2) {// ����, �� �Է�
			query = "select sum(v.price) " + "from order_list o, vehicle v, detailed_model dm, model m "
					+ "where TO_CHAR(o.sell_date,'YYYY-MM') = '" + year + "-" + month + "' "
					+ "and o.registration_number = v.registration_number ";
		} else if (flag == 3) {// �� �����
			query = "select sum(v.price) " + "from order_list o, vehicle v "
					+ "where o.registration_number = v.registration_number ";
		} else if (flag == 4) {// ���� + ������ �Է�
			query = "select sum(v.price) " + "from order_list o, vehicle v, detailed_model dm, model m "
					+ "where TO_CHAR(o.sell_date,'YYYY') = '" + year + "' "
					+ "and o.registration_number = v.registration_number "
					+ "and v.detailed_model_name = dm.detailed_model_name " + "and dm.model_name = m.model_name "
					+ "and m.make = '" + make + "'";
		} else if (flag == 5) {// ����,�� + ������ �Է�
			query = "select sum(v.price) " + "from order_list o, vehicle v, detailed_model dm, model m "
					+ "where TO_CHAR(o.sell_date,'YYYY-MM') = '" + year + "-" + month + "' "
					+ "and o.registration_number = v.registration_number "
					+ "and v.detailed_model_name = dm.detailed_model_name " + "and dm.model_name = m.model_name "
					+ "and m.make = '" + make + "'";
		} else if (flag == 6) {// �����縸 �Է�
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
