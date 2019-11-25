package vehicle;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class VehicleDAO {
	private static final String getBasicVehicleInfoQuery = "select registration_number, make, detailed_model_name, model_year, price, mileage, location, fuel, color from ALL_VEHICLE_INFO where registration_number not in (select registration_number from order_list) order by registration_number desc";
	private static final String getTableColumnNamesQuery = "select cname from col where tname=?";
	private static final String getMakeListQuery = "select * from make";
	private static final String searchQuery = "select ? from selling_car";
	private static final String url = "jdbc:oracle:thin:@localhost:1600:xe";
	private static final String user = "knu";
	private static final String pw = "comp322";
	private static final String buyVehicleQuery = "insert into order_list values(?, 'admin', ?, sysdate)";
	private PreparedStatement pstmt;
	private Connection con;

	public VehicleDAO() {
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

	// 전체를 출력할 필요는 없음.
	// 즉 전체 정보를 DB에서 가져올 필요가 없고, 기본 정보만 출력하고
	// 전체 정보는 Registration_number로 DB에 요청했을 때만 전체 정보를 출력.
	// 기본 정보에 대한 정의 필요
	public ArrayList<BasicVehicleInfoDTO> getAllSellingVehicleInfo(ArrayList<String> metadata) {
		ArrayList<BasicVehicleInfoDTO> list = new ArrayList<BasicVehicleInfoDTO>();
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(getBasicVehicleInfoQuery);
			rs = pstmt.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			int cnt = rsmd.getColumnCount();
			for (int i = 1; i <= cnt; i++)
				metadata.add(rsmd.getColumnName(i));
			while (rs.next()) {
				list.add(new BasicVehicleInfoDTO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4),
						rs.getInt(5), rs.getInt(6), rs.getString(7), rs.getString(8), rs.getString(9)));
			}

		} catch (SQLException e) {
			System.err.println("[getSellingVehicleInfo()] sql error " + e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getSellingVehicleInfo()] sql error " + e.getMessage());
			}
		}

		return list;
	}

	public ArrayList<BasicVehicleInfoDTO> getBasicVehicleInfoByQuery(String whereClusure, ArrayList<String> metadata) {
		ArrayList<BasicVehicleInfoDTO> list = new ArrayList<BasicVehicleInfoDTO>();
		ResultSet rs = null;
		String query = "select registration_number, make, detailed_model_name, model_year, price, mileage, location, fuel, color "
				+ "from ALL_VEHICLE_INFO "
				+ "where registration_number not in (select registration_number from order_list) ";
		if (whereClusure != null)
			query += "AND " + whereClusure;
		System.out.println(query);
		try {
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int cnt = rsmd.getColumnCount();
			for(int i=1; i<=cnt; i++)
				metadata.add(rsmd.getColumnName(i));
			while (rs.next()) {
				list.add(new BasicVehicleInfoDTO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4),
						rs.getInt(5), rs.getInt(6), rs.getString(7), rs.getString(8), rs.getString(9)));
			}
		} catch (SQLException e) {
			System.err.println("[getBasicVehicleInfoByQuery] sql error : " + e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getSellingVehicleInfo()] sql error " + e.getMessage());
			}
		}

		return list;
	}

	public ArrayList<String> getMakeList() {
		ArrayList<String> makeList = new ArrayList<String>();
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(getMakeListQuery);
			rs = pstmt.executeQuery();
			while (rs.next())
				makeList.add(rs.getString(1));
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			System.err.println("[getMakeList] sql error : " + e.getMessage());
		}
		return makeList;
	}

	public ArrayList<String> getModelList(String make) {
		ArrayList<String> modelList = new ArrayList<String>();
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement("select model_name from model where make=?");
			pstmt.setString(1, make);
			rs = pstmt.executeQuery();
			while (rs.next())
				modelList.add(rs.getString(1));
		} catch (SQLException e) {
			System.err.println("[getModelList] sql error : " + e.getMessage());
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getModelList] sql error : " + e.getMessage());
			}
		}
		return modelList;
	}

	public ArrayList<String> getDetailedModelList(String model_name) {
		ArrayList<String> dModelList = new ArrayList<String>();
		ResultSet rs = null;
		String query = "select detailed_model_name from detailed_model";

		// detailed_model_name만 선택해서 검색하는 경우
		if (model_name == null)
			query += " where model_name=?";

		try {
			pstmt = con.prepareStatement(query);

			// detailed_model_name만 선택해서 검색하는 경우
			if (model_name != null)
				pstmt.setString(1, model_name);

			rs = pstmt.executeQuery();
			while (rs.next())
				dModelList.add(rs.getString(1));

		} catch (SQLException e) {
			System.err.println("[getDetailedModelList] sql error : " + e.getMessage());
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getDetailedModelList] sql error : " + e.getMessage());
			}
		}
		return dModelList;
	}

	public DetailVehicleInfoDTO getVehicleInfoByRegNum(int regNum) {
		DetailVehicleInfoDTO ret = null;
		String query = "select * from ALL_VEHICLE_INFO where registration_number=?";
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, regNum);
			rs = pstmt.executeQuery();

			if (rs.next())
				ret = new DetailVehicleInfoDTO(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getDate(9), rs.getString(10),
						rs.getInt(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15));
			else
				ret = null;
		} catch (SQLException e) {
			System.err.println("[getVehicleInfoByRegNum] sql error : " + e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getVehicleInfoByRegNum] sql error " + e.getMessage());
			}
		}
		return ret;
	}

	protected void finalize() throws Throwable {
		pstmt.close();
		con.close();
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

	public ArrayList<String> getConditionList(String columnNmae, String whereClusure) {
		ResultSet rs = null;
		ArrayList<String> list = new ArrayList<String>();
		String query = "select distinct " + columnNmae
				+ " from ALL_VEHICLE_INFO WHERE registration_number not in (select registration_number from order_list) ";
		if (whereClusure != null)
			query += "AND " + whereClusure;
		// System.out.println(query);
		try {
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(rs.getString(1));
		} catch (SQLException e) {
			System.err.println("[getConditionList] sql error : " + e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getConditionList] sql error : " + e.getMessage());
			}
		}
		return list;
	}

	public void buyVehicle(String buyerId, int regNum) {
		int ret = 0;
		try {
			pstmt = con.prepareStatement(buyVehicleQuery);
			pstmt.setInt(1, regNum);
			pstmt.setString(2, buyerId);
			ret = pstmt.executeUpdate();
			if(ret == 1)
				System.out.println("구매 성공!");
		}catch(SQLException e) {
			System.err.println("[buyVehicle] sql error : " + e.getMessage());
		}
	}
}
