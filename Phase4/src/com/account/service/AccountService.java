package com.account.service;

import java.sql.Date;

import com.account.dao.AccountDAO;
import com.account.vo.AccountVO;

public class AccountService {
	AccountDAO accountDAO;
	
	public AccountService() {
		this.accountDAO = new AccountDAO();
	}
	
	public AccountVO login(String user_id, String user_pw) {
		AccountVO userInfo = accountDAO.login(user_id, user_pw);
		return userInfo;
	}
	
	public boolean register(String user_id, String user_pw, String name, String phone_num, String address, String strDate, String sex, String job, String type) {
		boolean ret = false;
		Date bDate = null;
		if(strDate != null)
			bDate = Date.valueOf(strDate);
		
		AccountVO newAccount = new AccountVO(user_id, user_pw, name, phone_num, address, bDate, sex, job, type);
		
		ret = accountDAO.joinAccount(newAccount);
		
		if(ret == true)
			System.out.println("성공!!!!");
		
		return ret;
	}
	
	public boolean idDupCheck(String user_id) {
		return accountDAO.idDupCheck(user_id);
	}

	public AccountVO modifyUserInfo(String id, String pw, String name, String phone, String addr, String strDate, String gender, String job, String account_type) {
		boolean ret = false;
		Date bDate = null;
		if(strDate != null)
			bDate = Date.valueOf(strDate);
		
		AccountVO newAccount = new AccountVO(id, pw, name, phone, addr, bDate, gender, job, account_type);
		
		ret = accountDAO.modifyUserInfo(newAccount);
		if(ret == true)
			return newAccount;
		else
			return null;
	}
}
