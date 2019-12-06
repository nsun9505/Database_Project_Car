package com.account.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
			dataSrc = (DataSource) context.lookup("java:comp/env/jdbc/Oracle");
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

	public boolean joinAccount(AccountVO newAccount) {
		try {
			conn = dataSrc.getConnection();
			pstmt = conn.prepareStatement(insertAccountQuery);
			pstmt.setString(1, newAccount.getId());
			pstmt.setString(2, newAccount.getPw());
			pstmt.setString(3, newAccount.getName());
			pstmt.setString(4, newAccount.getPhone_num());

			if (newAccount.getAddress() == null)
				pstmt.setNull(5, Types.CHAR);
			else
				pstmt.setString(5, newAccount.getAddress());

			if (newAccount.getBirth_date() == null)
				pstmt.setNull(6, Types.DATE);
			else
				pstmt.setDate(6, newAccount.getBirth_date());

			if (newAccount.getSex() == null)
				pstmt.setNull(7, Types.CHAR);
			else
				pstmt.setString(7, newAccount.getSex());

			if (newAccount.getJob() == null)
				pstmt.setNull(8, Types.CHAR);
			else
				pstmt.setString(8, newAccount.getJob());

			pstmt.setString(9, newAccount.getAccount_type());

			int res = pstmt.executeUpdate();
			if (res == 1) {
				conn.commit();
				return true;	
			}
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
		return false;
	}
}
