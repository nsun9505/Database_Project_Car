package com.orderlist.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.orderlist.vo.OrderlistVO;

public class OrderlistDAO {
	private DataSource dataSrc;
	Connection conn;
	PreparedStatement pstmt;

	public OrderlistDAO() {
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
	
	public boolean secretVehicle(int regnum, String id) {
		int ret = 0;

		String notOpenVehicleQuery = "insert into order_list values(?, ?, 'admin', sysdate)";
		try {
			conn = dataSrc.getConnection();
			pstmt = conn.prepareStatement(notOpenVehicleQuery);
			pstmt.setInt(1, regnum);
			pstmt.setString(2, id);
			ret = pstmt.executeUpdate();
			if (ret > 0) {
				conn.commit();
				return true;
			}
		} catch (SQLException e) {
			System.err.println("[notopenVehicle] sql error : " + e.getMessage());
		}
		return false;
	}
	
	public boolean openVehicle(int regnum) {
		int ret = 0;
		String openVehicleQuery = "delete from order_list where registration_number = ? AND buyer_id ='admin'";
	
		try {
			conn = dataSrc.getConnection();
			pstmt = conn.prepareStatement(openVehicleQuery);
			pstmt.setInt(1, regnum);
			ret = pstmt.executeUpdate();
			if (ret > 0) {
				
				conn.commit();
				return true;
			}
		} catch (SQLException e) {
			System.err.println("[openVehicle] sql error : " + e.getMessage());
		}
		return false;
	}

	public ArrayList<OrderlistVO> getCustomerOrderList(String id) {
		ArrayList<OrderlistVO> list = new ArrayList<OrderlistVO>();
		ResultSet rs = null;
		try {
			conn = dataSrc.getConnection();
			pstmt = conn.prepareStatement("select * from order_list where buyer_id=?");
			pstmt.setString(1,id);
			rs = pstmt.executeQuery();

			while (rs.next())
				list.add(new OrderlistVO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4)));

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

	public ArrayList<OrderlistVO> getAdminOrderList(String id) {
		ArrayList<OrderlistVO> list = new ArrayList<OrderlistVO>();
		ResultSet rs = null;
		
		try {
			conn = dataSrc.getConnection();
			pstmt = conn.prepareStatement("select * from order_list where buyer_id != 'admin' order by sell_date");
			rs = pstmt.executeQuery();
			while (rs.next()) 
				list.add(new OrderlistVO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4)));
			
		} catch (SQLException e) {
			System.err.println("[getAdminOrderList]sql error : " + e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getAdminOrderList]sql error : " + e.getMessage());
			}
		}
		return list;
	}

	public long getIntakeByFlag(String year, String month, String make, int flag) {
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
			conn = dataSrc.getConnection();
			pstmt = conn.prepareStatement(query);
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
}
