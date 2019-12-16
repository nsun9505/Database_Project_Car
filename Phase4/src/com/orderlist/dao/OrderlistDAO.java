package com.orderlist.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

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
}
