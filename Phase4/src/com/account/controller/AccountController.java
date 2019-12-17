package com.account.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.account.service.AccountService;
import com.account.vo.AccountVO;

/**
 * Servlet implementation class AccountController
 */
@WebServlet("/account/*")
public class AccountController extends HttpServlet {     
	/**
	 * 
	 */
	AccountService accountService;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AccountController() {
        super();
        // TODO Auto-generated constructor stub
    }
    

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
		String nextPage ="";
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		
		String action = request.getPathInfo();
		
		System.out.println("action : " + action);
		System.out.println("request : " + request.getRequestURL());
		System.out.println(request.getHeaderNames());
		try {
			if(action.equals("/login.do")) {
				String user_id = request.getParameter("user_id");
				String user_pw = request.getParameter("user_pw");
				System.out.println(user_id + " " + user_pw);
				AccountVO userInfo = accountService.login(user_id, user_pw);
				
				if(userInfo != null) {
					HttpSession session = request.getSession();
					session.setAttribute("isLogon", "true");
					session.setAttribute("userInfo", userInfo);
				}
				nextPage = "/vehicle/list.do";
			} else if(action.equals("/modifyUserInfo.do")) {
				String id = (String)request.getParameter("id");
				String pw = (String) request.getParameter("password");
				String name = (String) request.getParameter("name");
				String phone = (String) request.getParameter("phoneNumber");
				String addr = (String) request.getParameter("address");
				String strDate = (String) request.getParameter("birthDate");
				String gender = (String) request.getParameter("gender");
				String job = (String) request.getParameter("job");
				String account_type = (String)request.getParameter("account_type");
				System.out.println(strDate);
				AccountVO updateUserInfo = accountService.modifyUserInfo(id, pw, name, phone, addr, strDate, gender, job, account_type);
				
				if(updateUserInfo != null) {
					nextPage = "/modify/modifyUserInfoOk.jsp";
					HttpSession session = request.getSession();
					session.removeAttribute("userInfo");
					session.setAttribute("userInfo", updateUserInfo);
				}
				else
					nextPage ="/modify/modifyUserInfoFail.jsp";
			} else if(action.equals("/idDupCheck.do")) {
				String user_id = request.getParameter("id");
				boolean ret = accountService.idDupCheck(user_id);
				if(ret == true) {
					request.setAttribute("checkOkId", user_id);
				} 
				nextPage = "/login/registerForm.jsp";
			} else if(action.equals("/register.do")) {
				String id = (String)request.getParameter("id");
				String pw = (String) request.getParameter("password");
				String fname = (String) request.getParameter("first_name");
				String lname = (String) request.getParameter("last_name");
				String phone = (String) request.getParameter("phoneNumber");
				String addr = (String) request.getParameter("address");
				String strDate = (String) request.getParameter("birthDate");
				if(strDate.length() == 0)
					strDate = null;
				String gender = (String) request.getParameter("gender");
				String job = (String) request.getParameter("job");
				String account_type = (String) request.getParameter("account_type");
				accountService.register(id, pw, fname+" "+lname, phone, addr, strDate, gender, job, account_type);
				nextPage = "/login/loginForm.jsp";
			} else if(action.equals("/withdrawalAccount.do")) {
				String user_id = (String)request.getParameter("id");
				String account_type = (String)request.getParameter("account_type");
				boolean ret = accountService.withdrawal(user_id, account_type);
			}
			RequestDispatcher dispatcher = request.getRequestDispatcher(nextPage);
			dispatcher.forward(request, response);
			return;
		}catch(Exception e) {
			System.out.println("[register.do] error : "+e.getMessage());
			e.printStackTrace();
		}
	}
}
