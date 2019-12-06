package com.account.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import com.account.service.AccountService;
import com.account.vo.AccountVO;

/**
 * Servlet implementation class AccountController
 */
@WebServlet("/account/*")
public class AccountController extends HttpServlet {
	AccountService accountService;
	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		accountService = new AccountService();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doHandle(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doHandle(request, response);
	}

	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String nextPage ="";
		request.setCharacterEncoding("utf-8");
		String action = request.getPathInfo();
		System.out.println(action);
		try {
			if(action.equals("/login.do")) {
				String user_id = request.getParameter("user_id");
				String user_pw = request.getParameter("user_pw");
				System.out.println(user_id + " " + user_pw);
				AccountVO userInfo = accountService.login(user_id, user_pw);
				
				if(userInfo != null) {
					System.out.println("로그인 성공");
					HttpSession session = request.getSession();
					session.setAttribute("isLogon", "true");
					session.setAttribute("user_id", userInfo.getId());
					session.setAttribute("user_name", userInfo.getName());
					session.setAttribute("account_type", userInfo.getAccount_type());
				}
				nextPage = "/vehicle/list.do";
			} else if(action.equals("/modify.do")) {
				nextPage = "/vehicle/list.do";
			}
			RequestDispatcher dispatcher = request.getRequestDispatcher(nextPage);
			dispatcher.forward(request, response);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
