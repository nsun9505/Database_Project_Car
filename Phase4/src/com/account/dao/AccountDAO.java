package com.account.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.account.vo.AccountVO;

public class AccountDAO {
	private static final String insertAccountQuery = "insert into account values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String isExistIdQuery = "select count(id) from account where id = ?";
	private static final String isExistAdminQeury = "select account_type from account where id = ? AND password = ?";
	private static final String getAccountInfoQuery = "select * from account where id = ? AND password = ?";
	private static final String isExistAccountQuery = "select id, name, account_type from account where id = ? AND password = ?";
	private static final String getNumberOfAdmin = "select count(id) from account where account_type = 'A'";
	private static final String modifyAccountInfoQuery = "update account set password=?, name=?, phone_number=?, address=?, bDate=?, sex=?, job=? where id=?";
	private static final String modifyPasswordQuery = "update account set password=? where id=?";
	private DataSource dataSrc;
	Connection conn;
	PreparedStatement pstmt;
	
	public AccountDAO() {
		try {
			Context context = new InitialContext();
			dataSrc = (DataSource)context.lookup("java:comp/env/jdbc/Oracle");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public AccountVO login(String user_id, String user_pw) {
		AccountVO dto = null;
		try {
			conn = dataSrc.getConnection();
			pstmt = conn.prepareStatement(isExistAccountQuery);
			pstmt.setString(1, user_id);
			pstmt.setString(2, user_pw);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				dto = new AccountVO(rs.getString(1), rs.getString(2), rs.getString(3));
			else
				dto = null;
		} catch (SQLException e) {
			System.err.println("sql error : " + e.getMessage());
		}
		return dto;
	}
}
