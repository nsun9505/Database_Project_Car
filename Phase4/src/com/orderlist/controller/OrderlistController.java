package com.orderlist.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

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
import com.orderlist.vo.OrderlistVO;
import com.vehicle.dao.VehicleDAO;

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
			if(action.equals("/myOrderList.do")) {
				HttpSession session = request.getSession();
				AccountVO user = (AccountVO)session.getAttribute("userInfo");
				ArrayList<OrderlistVO> orderlist = null;
				if(user.getAccount_type().equals("C")) {
					orderlist = orderlistService.getCustomerOrderList(user.getId());
				}else {
					VehicleDAO dao = new VehicleDAO(); 
					ArrayList<String> makelist = dao.getMake();
					session.setAttribute("make_list", makelist);
					orderlist = orderlistService.getAdminOrderList(user.getId());
				}
				
				if(orderlist != null) {
					session.setAttribute("order_list", orderlist);
					System.out.println("개수 : "+orderlist.size());
				}
				nextPage = "/Orderlist/orderlist.jsp";
			} else if(action.equals("/secretVehicle.do")) {
				int regnum = 457;//regnum 임의로 정해둠
				String sellerId;
				boolean res=false;

				HttpSession session = request.getSession();
				AccountVO user = (AccountVO) session.getAttribute("userInfo");
				sellerId = user.getId();
				
				res = orderlistService.secretVehicle(regnum, sellerId);
				if(res==true)
					System.out.println(regnum+" 비공개 성공");
				nextPage = "/index.jsp";
			}else if(action.equals("/openVehicle.do")) {
				int regnum = 457; //regnum 임의로 정해둠
				boolean res = false;
				
				res = orderlistService.openVehicle(regnum);
				if(res==true)
					System.out.println(regnum+" 공개 성공");
				nextPage="/index.jsp";
			} else if(action.equals("/getTotalIntake.do")) {
				String make = (String)request.getParameter("selected_make");
				String year = (String)request.getParameter("selected_year");
				String month = (String)request.getParameter("selected_month");
				String type = (String)request.getParameter("selected_intake");
				
				long ret = orderlistService.getIntake(make, year, month, type);
				if(ret != -1) {
					request.setAttribute("intakeResult", ret);
					request.setAttribute("resultString", orderlistService.getIntakeString(type, make, year, month));
				}
				nextPage = "/Orderlist/orderlist.jsp";
			}else if(action.equals("/recommendVehicle.do")) {
				HttpSession session = request.getSession();
				ArrayList<String> recommendedCar = null;
				AccountVO user = (AccountVO) session.getAttribute("userInfo");
				
				recommendedCar = orderlistService.recommendCar(user.getSex(), user.getBirth_date());

				session.setAttribute("recommend", recommendedCar);
				//asf
				nextPage = "/Orderlist/recommendedVehicleForm.jsp";
			}else {
				nextPage = "/vehicle/list.do";
			}
			RequestDispatcher dispatcher = request.getRequestDispatcher(nextPage);
			dispatcher.forward(request, response);
			return;
		}catch(Exception e) {
			System.out.println("[Orderlist Controller ERROR] : "+e.getMessage());
		}
	}
}
