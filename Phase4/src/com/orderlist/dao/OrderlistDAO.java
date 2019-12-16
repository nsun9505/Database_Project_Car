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
			pstmt = conn.prepareStatement("select * from order_list where buyer_id != 'admin'");
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
}
