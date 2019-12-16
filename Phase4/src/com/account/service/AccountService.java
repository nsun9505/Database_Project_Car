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
		if(strDate == null)
			bDate = bDate.valueOf(strDate);
		
		AccountVO newAccount = new AccountVO(user_id, user_pw, name, phone_num, address, bDate, sex, job, type);
		
		ret = accountDAO.joinAccount(newAccount);
		
		if(ret == true)
			System.out.println("성공!!!!");
		
		return ret;
	}
	
	public boolean idDupCheck(String user_id) {
		return accountDAO.idDupCheck(user_id);
	}
}
