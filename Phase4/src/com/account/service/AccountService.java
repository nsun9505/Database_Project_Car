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
}
