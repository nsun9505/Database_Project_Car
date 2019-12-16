package com.orderlist.controller;

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

import com.account.vo.AccountVO;
import com.orderlist.service.OrderlistService;

/**
 * Servlet implementation class OrderlistController
 */
@WebServlet("/orderlist/*")
public class OrderlistController extends HttpServlet {
	OrderlistService orderlistService;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderlistController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		orderlistService = new OrderlistService();
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

	private void doHandle(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String nextPage ="";
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		
		String action = request.getPathInfo();
		
		System.out.println("action : " + action);
		System.out.println("request : " + request.getRequestURL());
		System.out.println(request.getHeaderNames());
		try {
			if(action.equals("/myOrderList")) {
				HttpSession session = request.getSession();
				AccountVO user = (AccountVO)session.getAttribute("userInfo");
				// commit test
			}
			RequestDispatcher dispatcher = request.getRequestDispatcher(nextPage);
			dispatcher.forward(request, response);
			return;
		}catch(Exception e) {
			System.out.println("[Orderlist Controller ERROR] : "+e.getMessage());
		}
	}


}
