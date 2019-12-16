package com.vehicle.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.account.vo.AccountVO;
import com.vehicle.vo.VehicleVO;



public class VehicleDAO {

	private static final String getBasicVehicleInfoQuery = "select registration_number, make, detailed_model_name, model_year, price, mileage, location, fuel, color from ALL_VEHICLE_INFO where registration_number not in (select registration_number from order_list) order by registration_number desc";
	private static final String getTableColumnNamesQuery = "select cname from col where tname=?";
	private static final String getMakeListQuery = "select * from make";
	private static final String searchQuery = "select ? from selling_car";
	private static final String url = "jdbc:oracle:thin:@localhost:1600:xe";
	private static final String user = "knu";
	private static final String pw = "comp322";
	private static final String buyVehicleQuery = "insert into order_list values(?, ?, ?, sysdate)";
	private static final String regExpCarNum = "^[0-9]{2}[A-Z]{2}[0-9]{4}$";
	private PreparedStatement pstmt;
	private Connection conn;
	private DataSource dataSrc;

	public VehicleDAO() {
		try {
			Context context = new InitialContext();
			dataSrc = (DataSource) context.lookup("java:comp/env/jdbc/Oracle");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public VehicleVO carInfo(int regnum) {
		VehicleVO dto = null;
		try {
			conn = dataSrc.getConnection();
			pstmt = conn.prepareStatement("select * from vehicle where registration_number = '"+regnum+"'");
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				dto = new VehicleVO(rs.getString(8),rs.getString(5),rs.getInt(3),rs.getInt(4),rs.getString(6),rs.getString(11),rs.getString(9),rs.getInt(7),rs.getString(10),rs.getString(1),rs.getString(12));
			else
				dto = null;
		} catch (SQLException e) {
			System.err.println("sql error : " + e.getMessage());
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				System.err.println("[joinAccount] sql error : " + e.getMessage());
			}
		}
		return dto;
	}

	public boolean modifyVehicle(VehicleVO newVehicle,int regnum) {
		try {
			String modifyVehicleQuery = "update vehicle set price=?,mileage=?,location=?,color=?,transmission=?,fuel=? where registration_number=?";

			conn = dataSrc.getConnection();
			pstmt = conn.prepareStatement(modifyVehicleQuery);

			pstmt = conn.prepareStatement(modifyVehicleQuery);
			pstmt.setInt(1, newVehicle.getPrice());
			pstmt.setInt(2, newVehicle.getMileage());
			pstmt.setString(3, newVehicle.getLocation());
			pstmt.setString(4, newVehicle.getColor());
			pstmt.setString(5, newVehicle.getTransmission());
			pstmt.setString(6, newVehicle.getFuel());
			pstmt.setInt(7, regnum);

			int res = pstmt.executeUpdate();
			if (res == 1) {
				conn.commit();
				return true;
			}
		} catch (SQLException e) {
			System.err.println("sql error : " + e.getMessage());
		}
		return false;
	}

	public boolean insertVehicle(VehicleVO newVehicle) {
		try {

			String addVehicleInsertQuery = "insert into vehicle values('" + newVehicle.getCar_number()
					+ "',registration_seq.nextVal," + newVehicle.getPrice() + "," + newVehicle.getMileage() + ","
					+ "TO_DATE('" + newVehicle.getModel_year() + "','yyyy-mm-dd')" + ",'" + newVehicle.getLocation()
					+ "'," + newVehicle.getEngine_displacement() + ",'" + newVehicle.getDetailed_model_name() + "','"
					+ newVehicle.getColor() + "','" + newVehicle.getTransmission() + "','" + newVehicle.getFuel()
					+ "','" + newVehicle.getSellerId() + "')";

			conn = dataSrc.getConnection();
			pstmt = conn.prepareStatement(addVehicleInsertQuery);

			/*
			 * pstmt.setString(1, newVehicle.getCar_number());
			 * pstmt.setString(2,"registration_seq.nextVal"); pstmt.setInt(3,
			 * newVehicle.getPrice()); pstmt.setInt(4,newVehicle.getMileage());
			 * pstmt.setDate(5, java.sql.Date.valueOf(newVehicle.getModel_year()));
			 * pstmt.setString(6, newVehicle.getLocation());
			 * pstmt.setInt(7,newVehicle.getEngine_displacement());
			 * pstmt.setString(8,newVehicle.getDetailed_model_name()); pstmt.setString(9,
			 * newVehicle.getColor()); pstmt.setString(10, newVehicle.getTransmission());
			 * pstmt.setString(11, newVehicle.getFuel()); pstmt.setString(12,
			 * newVehicle.getSellerId());
			 */

			int res = pstmt.executeUpdate();
			if (res == 1) {
				conn.commit();
				return true;
			}
		} catch (SQLException e) {
			System.err.println("sql error : " + e.getMessage());
		}
		return false;
	}

	public ArrayList<String> getMake() {

		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;

		try {
			conn = dataSrc.getConnection();
			pstmt = conn.prepareStatement("select make from make");
			rs = pstmt.executeQuery();

			while (rs.next())
				list.add(rs.getString(1));

		} catch (SQLException e) {
			System.err.println("[getMake] sql error " + e.getMessage());
		}

		return list;
	}

	public ArrayList<String> getModel(String make) {
		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;

		try {
			conn = dataSrc.getConnection();
			pstmt = conn.prepareStatement("select model_name from model where make = '" + make + "'");
			rs = pstmt.executeQuery();

			while (rs.next())
				list.add(rs.getString(1));

		} catch (SQLException e) {
			System.err.println("[getModel] sql error " + e.getMessage());
		}

		return list;
	}

	public ArrayList<String> getDetaile(String model) {
		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;

		try {
			conn = dataSrc.getConnection();
			pstmt = conn.prepareStatement(
					"select detailed_model_name from detailed_model where model_name = '" + model + "'");
			rs = pstmt.executeQuery();

			while (rs.next())
				list.add(rs.getString(1));

		} catch (SQLException e) {
			System.err.println("[getDetaile] sql error " + e.getMessage());
		}

		return list;
	}

	public ArrayList<String> getEngine(String detaile) {
		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;

		try {
			conn = dataSrc.getConnection();
			pstmt = conn.prepareStatement(
					"select engine_displacement from engine_displacement where detailed_model_name = '" + detaile
							+ "'");
			rs = pstmt.executeQuery();

			while (rs.next())
				list.add(rs.getString(1));

		} catch (SQLException e) {
			System.err.println("[getEngine] sql error " + e.getMessage());
		}

		return list;
	}

	public ArrayList<String> getFuel() {
		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;

		try {
			conn = dataSrc.getConnection();
			pstmt = conn.prepareStatement("select fuel from fuel");
			rs = pstmt.executeQuery();

			while (rs.next())
				list.add(rs.getString(1));

		} catch (SQLException e) {
			System.err.println("[getFuel] sql error " + e.getMessage());
		}

		return list;
	}

	public ArrayList<String> getColor() {
		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;

		try {
			conn = dataSrc.getConnection();
			pstmt = conn.prepareStatement("select color from color");
			rs = pstmt.executeQuery();

			while (rs.next())
				list.add(rs.getString(1));

		} catch (SQLException e) {
			System.err.println("[getColor] sql error " + e.getMessage());
		}

		return list;
	}

	public ArrayList<String> getTrans() {
		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;

		try {
			conn = dataSrc.getConnection();
			pstmt = conn.prepareStatement("select transmission from transmission");
			rs = pstmt.executeQuery();

			while (rs.next())
				list.add(rs.getString(1));

		} catch (SQLException e) {
			System.err.println("[getTrans] sql error " + e.getMessage());
		}

		return list;
	}

	public String getCarnumber(String carnum, int flag) {
		ArrayList<String> list = new ArrayList<String>();
		ResultSet rs = null;

		if (validCheck(carnum, regExpCarNum) == false) {
			System.out.println("차량번호형식안맞음");
			return null;
		}

		try {
			pstmt = conn.prepareStatement("select Car_number from vehicle");
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
			return null;
		}
	}

	public boolean validCheck(String src, String regExp) {
		return src.matches(regExp);
	}

}
