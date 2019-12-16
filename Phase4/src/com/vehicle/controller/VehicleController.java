package com.vehicle.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
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
import com.vehicle.dao.VehicleDAO;
import com.vehicle.service.VehicleService;

/**
 * Servlet implementation class VehicleController
 */
@SuppressWarnings("serial")
@WebServlet("/vehicle/*")
public class VehicleController extends HttpServlet {
	VehicleService vehicleService;

	public VehicleController() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		vehicleService = new VehicleService();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doHandle(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doHandle(request, response);
	}

	protected void doHandle(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		String nextPage = "";
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		String action = request.getPathInfo();
		System.out.println(action);
		try {
			if (action.equals("/list.do")) {
				nextPage = "/index.jsp";
			} else if (action.equals("/addVehicle.do")) {
				// 매물 등록 작업
				String car_number = (String) request.getParameter("car_number");
				car_number = vehicleService.checkCarnumber(car_number);
				if (car_number == null) {
					nextPage = "/Vehicle/addVehicleFail.jsp";
				} else {
					String make = (String) request.getParameter("make");
					String model = (String) request.getParameter("model_name");
		
					String detailed_model = (String) request.getParameter("detailed_model_name");
					int engine = Integer.parseInt((String) request.getParameter("engine_displacement"));
					String year = (String) request.getParameter("model_year");
					String month = (String) request.getParameter("model_month");
					String fuel = (String) request.getParameter("fuel");
					String color = (String) request.getParameter("color");
					String transmission = (String) request.getParameter("transmission");
					int price = Integer.parseInt((String) request.getParameter("price"));
					int mileage = Integer.parseInt((String) request.getParameter("mileage"));
					String location = (String) request.getParameter("location");
					String sellerId;

					HttpSession session = request.getSession();
					AccountVO user = (AccountVO) session.getAttribute("userInfo");
					sellerId = user.getId();

					if(Integer.parseInt(month)<10)
						month="0"+month;
					String model_year = year+"-"+month+"-"+"01";
					
					System.out.println(detailed_model + model_year+ price+ mileage+ location+ fuel+ color+
							engine+ transmission+ car_number+ sellerId);
					vehicleService.addVehicle(detailed_model, model_year, price, mileage, location, fuel, color,
							engine, transmission, car_number, sellerId);
					nextPage = "/index.jsp";
				}
			} else if (action.equals("/getMake.do")) {
				ArrayList<String> makelist = vehicleService.getMakeList();
				HttpSession session = request.getSession();

				session.setAttribute("makelist", makelist);

				nextPage = "/Vehicle/addVehicle.jsp";
			} else if (action.equals("/getModel.do")) {
				String make = (String) request.getParameter("make");
				ArrayList<String> modelList = vehicleService.getModelList(make);
				HttpSession session = request.getSession();

				session.setAttribute("modelList", modelList);
				session.setAttribute("selected_make", make);

				nextPage = "/Vehicle/addVehicle.jsp";
			} else if (action.equals("/getDetailed.do")) {
				String model = (String) request.getParameter("model_name");
				ArrayList<String> detaileList = vehicleService.getDetailelList(model);
				HttpSession session = request.getSession();

				session.setAttribute("detaileList", detaileList);
				session.setAttribute("selected_model", model);

				nextPage = "/Vehicle/addVehicle.jsp";
			} else if (action.equals("/getEngine.do")) {
				String detaile = (String) request.getParameter("detailed_model_name");
				ArrayList<String> EngineList = vehicleService.getEngineList(detaile);
				HttpSession session = request.getSession();

				session.setAttribute("EngineList", EngineList);
				session.setAttribute("selected_detaile", detaile);

				nextPage = "/Vehicle/addVehicle.jsp";
			} else if (action.equals("/getElse.do")) {
				String engine = (String) request.getParameter("engine_displacement");
				HttpSession session = request.getSession();
				ArrayList<String> fuelList = vehicleService.getFuelList();
				ArrayList<String> transList = vehicleService.getTransList();
				ArrayList<String> colorList = vehicleService.getColorList();

				session.setAttribute("selected_engine", engine);
				session.setAttribute("fuelList", fuelList);
				session.setAttribute("colorList", colorList);
				session.setAttribute("transList", transList);

				nextPage = "/Vehicle/addVehicle.jsp";

			}
			RequestDispatcher dispatcher = request.getRequestDispatcher(nextPage);
			dispatcher.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
