package com.account.service;

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
	
	public boolean join(String user_id, String user_pw, String name, String phone_num, String address, String bDate, String sex, String job) {
		boolean ret = false;
//		AccountVO newAccount = new AccountVO(user_id, user_pw, name, phone_num, address, bDate, sex, job);
		
		return ret;
	}
}
