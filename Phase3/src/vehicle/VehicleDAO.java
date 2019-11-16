package vehicle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class VehicleDAO {
	private static final String getSellingVehicleInfoQuery = "select * from (VEHICLE V JOIN ODRER_LIST O ON O.resistration_number != V.registration_number)";
	private static final String getMakeListQuery = "select * from make";
	private static final String searchQuery = "select ? from selling_car";
	private static final String url = "jdbc:oracle:thin:@localhost:1600:xe";
	private static final String user = "knu";
	private static final String pw = "comp322";
	private PreparedStatement pstmt;
	private Connection con;
	
	public VehicleDAO() {
		connDB();
	}
	
	public ArrayList<SellingVehicleDTO> getSellingVehicleInfo(){
		ArrayList<SellingVehicleDTO> list = new ArrayList<SellingVehicleDTO>();
		ResultSet rs = null;
		
		try {
			pstmt = con.prepareStatement(getSellingVehicleInfoQuery);
			
		}catch(SQLException e) {
			System.err.println("[getSellingVehicleInfo()] sql error " +e.getMessage());
		} finally {
			try {
				rs.close();
				pstmt.close();
			}catch(SQLException e) {
				System.err.println("[getSellingVehicleInfo()] sql error " +e.getMessage());
			}
		}
		
		return list;
	}
	
	public ArrayList<String> getMakeList(){
		ArrayList<String> makeList = new ArrayList<String>();
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(getMakeListQuery);
			rs = pstmt.executeQuery();
			while(rs.next())
				makeList.add(rs.getString(1));
			rs.close();
			pstmt.close();
		}catch(SQLException e) {
			System.err.println("[getMakeList] sql error : "+e.getMessage());
		}
		return makeList;
	}
	
	public ArrayList<String> getModelList(String make){
		ArrayList<String> modelList = new ArrayList<String>();
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement("select model_name from model where make=?");
			pstmt.setString(1, make);
			rs = pstmt.executeQuery();
			while(rs.next())
				modelList.add(rs.getString(1));
		}catch(SQLException e) {
			System.err.println("[getModelList] sql error : "+e.getMessage());
		} finally {
			try {
			rs.close();
			pstmt.close();
			}catch(SQLException e) {
				System.err.println("[getModelList] sql error : "+e.getMessage());
			}
		}		
		return modelList;
	}
	
	public ArrayList<String> getDetailedModelList(String model_name){
		ArrayList<String> dModelList = new ArrayList<String>();
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement("select detailed_model_name from detailed_model where model_name=?");
			pstmt.setString(1, model_name);
			rs = pstmt.executeQuery();
			while(rs.next())
				dModelList.add(rs.getString(1));
			
		}catch(SQLException e) {
			System.err.println("[getDetailedModelList] sql error : "+e.getMessage());
		}finally {
			try {
				rs.close();
				pstmt.close();
			}catch(SQLException e) {
				System.err.println("[getDetailedModelList] sql error : "+e.getMessage());
			}
		}
		return dModelList;	
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
}
