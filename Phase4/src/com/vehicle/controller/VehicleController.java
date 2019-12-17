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
import com.vehicle.vo.VehicleVO;

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
			if (action.equals("/init.do")) {
				if (request.getSession().isNew()) {
					HttpSession session = request.getSession();
					session.setAttribute("init", true);
					session.setAttribute("make_list", vehicleService.getMakeList());
					session.setAttribute("category_list", vehicleService.getCategoryList());
					session.setAttribute("transmission_list", vehicleService.getTransList());
					session.setAttribute("fuel_list", vehicleService.getFuelList());
					session.setAttribute("location_list", vehicleService.getLocationList());
					session.setAttribute("color_list", vehicleService.getColorList());
					session.setAttribute("min_model_year", 1980);
					session.setAttribute("max_model_year", 2020);
					session.setAttribute("min_mileage", 10000);
					session.setAttribute("max_mileage", 200000);
					session.setAttribute("min_price", 500);
					session.setAttribute("max_price", 10000);
					session.setAttribute("vehicle_list", vehicleService.getInitList());
					session.setAttribute("conditions", new HashMap<String, ArrayList<String>>());
				}
				nextPage = "/index.jsp";
			} else if (action.equals("/combinationQuery.do")) {
				HttpSession session = request.getSession();
				HashMap<String, ArrayList<String>> conditions = (HashMap<String, ArrayList<String>>) session
						.getAttribute("conditions");
				session.setAttribute("vehicle_list", vehicleService.getVehicleListByQuery(conditions));
				nextPage = "/index.jsp";
			} else if (action.equals("/list.do")) {
				nextPage = "/index.jsp";
			} else if (action.equals("/selectCategory.do")) {
				ArrayList<String> list = null;
				String condition = (String) request.getParameter("condition");
				HttpSession session = request.getSession();
				HashMap<String, ArrayList<String>> conditions = (HashMap<String, ArrayList<String>>) session
						.getAttribute("conditions");
				if (conditions.containsKey("make") == false) {
					list = new ArrayList<String>();
					list.add(condition);
				} else {
					boolean flag = false;
					int idx = 0;
					list = conditions.get("make");
					for (idx = 0; idx < list.size(); idx++) {
						if (list.get(idx).equals(condition)) {
							flag = true;
							break;
						}
					}
					if (flag == true)
						list.remove(idx);
					else
						list.add(condition);
				}
				conditions.put("make", list);
				session.setAttribute("conditions", conditions);
				session.setAttribute("vehicle_list", vehicleService.getVehicleListByQuery(conditions));
				nextPage = "/index.jsp";
			} else if (action.equals("/selectMake.do")) {
				HttpSession session = request.getSession();
				HashMap<String, ArrayList<String>> conditions = (HashMap<String, ArrayList<String>>) session
						.getAttribute("conditions");
				String condition = (String) request.getParameter("condition");
				ArrayList<String> list = null;
				if (conditions.containsKey("make") == false) {
					list = new ArrayList<String>();
					list.add(condition);
				} else {
					boolean flag = false;
					int idx = 0;
					list = conditions.get("make");
					for (idx = 0; idx < list.size(); idx++) {
						if (list.get(idx).equals(condition)) {
							flag = true;
							break;
						}
					}
					if (flag == true)
						list.remove(idx);
					else
						list.add(condition);
				}
				conditions.put("make", list);
				session.setAttribute("conditions", conditions);
				session.setAttribute("model_list", vehicleService.getModelList(condition));
				session.setAttribute("vehicle_list", vehicleService.getVehicleListByQuery(conditions));
				nextPage = "/index.jsp";
			} else if (action.equals("/selectModel.do")) {
				HttpSession session = request.getSession();
				HashMap<String, ArrayList<String>> conditions = (HashMap<String, ArrayList<String>>) session
						.getAttribute("conditions");
				String condition = (String) request.getParameter("condition");
				ArrayList<String> list = new ArrayList<String>();
				list.add(condition);
				conditions.put("model_name", list);
				session.setAttribute("conditions", conditions);
				session.setAttribute("detailed_list", vehicleService.getDetailelList(condition));
				session.setAttribute("vehicle_list", vehicleService.getVehicleListByQuery(conditions));
				nextPage = "/index.jsp";
			} else if (action.equals("/selectDetailed.do")) {
				HttpSession session = request.getSession();
				HashMap<String, ArrayList<String>> conditions = (HashMap<String, ArrayList<String>>) session
						.getAttribute("conditions");
				String condition = (String) request.getParameter("condition");
				ArrayList<String> list = new ArrayList<String>();
				list.add(condition);
				conditions.put("detailed_model_name", list);
				session.setAttribute("conditions", conditions);
				session.setAttribute("vehicle_list", vehicleService.getVehicleListByQuery(conditions));
				nextPage = "/index.jsp";
			} else if (action.equals("/selectMinModelYear.do")) {
				HttpSession session = request.getSession();
				HashMap<String, ArrayList<String>> conditions = (HashMap<String, ArrayList<String>>) session.getAttribute("conditions");
				String condition = (String) request.getParameter("condition");
				ArrayList<String> min_list = new ArrayList<String>();
				if (conditions.containsKey("max_model_year")) {
					int max_year = Integer.parseInt(conditions.get("max_model_year").get(0));
					int min_year = Integer.parseInt(condition);
					if (min_year > max_year) {
						ArrayList<String> max_list = conditions.get("max_model_year");
						max_list.remove(0);
						max_list.add(String.valueOf(min_year));
						min_list.add(String.valueOf(max_year));
						conditions.put("max_model_year", max_list);
						conditions.put("min_model_year", min_list);
					}
				} else {
					min_list.add(condition);
					conditions.put("min_model_year", min_list);
				}
				session.setAttribute("conditions", conditions);
				session.setAttribute("vehicle_list", vehicleService.getVehicleListByQuery(conditions));
				nextPage = "/index.jsp";
			} else if (action.equals("/selectMaxModelYear.do")) {
				HttpSession session = request.getSession();
				HashMap<String, ArrayList<String>> conditions = (HashMap<String, ArrayList<String>>) session
						.getAttribute("conditions");
				String condition = (String) request.getParameter("condition");
				ArrayList<String> max_list = new ArrayList<String>();
				if (conditions.containsKey("min_model_year")) {
					int min_year = Integer.parseInt(conditions.get("min_model_year").get(0));
					int max_year = Integer.parseInt(condition);
					if (min_year > max_year) {
						ArrayList<String> min_list = conditions.get("min_model_year");
						min_list.remove(0);
						max_list.add(String.valueOf(min_year));
						min_list.add(String.valueOf(max_year));
						conditions.put("max_model_year", max_list);
						conditions.put("min_model_year", min_list);
					}
				} else {
					max_list.add(condition);
					conditions.put("max_model_year", max_list);
				}
				session.setAttribute("conditions", conditions);
				session.setAttribute("vehicle_list", vehicleService.getVehicleListByQuery(conditions));
				nextPage = "/index.jsp";
			} else if (action.equals("/selectMinMileage.do")) {
				HttpSession session = request.getSession();
				HashMap<String, ArrayList<String>> conditions = (HashMap<String, ArrayList<String>>) session.getAttribute("conditions");
				String condition = (String) request.getParameter("condition");
				ArrayList<String> min_list = new ArrayList<String>();
				if (conditions.containsKey("max_mileage")) {
					int max_mileage = Integer.parseInt(conditions.get("max_mileage").get(0));
					int min_mileage = Integer.parseInt(condition);
					if (min_mileage > max_mileage) {
						ArrayList<String> max_list = conditions.get("max_mileage");
						max_list.remove(0);
						max_list.add(String.valueOf(min_mileage));
						min_list.add(String.valueOf(max_mileage));
						conditions.put("max_mileage", max_list);
						conditions.put("min_mileage", min_list);
					}
				} else {
					min_list.add(condition);
					conditions.put("min_mileage", min_list);
				}
				session.setAttribute("conditions", conditions);
				session.setAttribute("vehicle_list", vehicleService.getVehicleListByQuery(conditions));
				nextPage = "/index.jsp";
			} else if (action.equals("/selectMaxMileage.do")) {
				HttpSession session = request.getSession();
				HashMap<String, ArrayList<String>> conditions = (HashMap<String, ArrayList<String>>) session.getAttribute("conditions");
				String condition = (String) request.getParameter("condition");
				ArrayList<String> max_list = new ArrayList<String>();
				if (conditions.containsKey("min_mileage")) {
					int min_mileage = Integer.parseInt(conditions.get("min_mileage").get(0));
					int max_mileage = Integer.parseInt(condition);
					if (min_mileage > max_mileage) {
						ArrayList<String> min_list = conditions.get("min_mileage");
						min_list.remove(0);
						min_list.add(String.valueOf(max_mileage));
						max_list.add(String.valueOf(min_mileage));
						conditions.put("max_mileage", max_list);
						conditions.put("min_mileage", min_list);
					}
				} else {
					max_list.add(condition);
					conditions.put("max_mileage", max_list);
				}
				session.setAttribute("conditions", conditions);
				session.setAttribute("vehicle_list", vehicleService.getVehicleListByQuery(conditions));
				nextPage = "/index.jsp";
			} else if (action.equals("/selectMinPrice.do")) {
				HttpSession session = request.getSession();
				HashMap<String, ArrayList<String>> conditions = (HashMap<String, ArrayList<String>>) session.getAttribute("conditions");
				String condition = (String) request.getParameter("condition");
				ArrayList<String> min_list = new ArrayList<String>();
				if (conditions.containsKey("max_price")) {
					int max_price = Integer.parseInt(conditions.get("max_price").get(0));
					int min_price = Integer.parseInt(condition);
					if (min_price > max_price) {
						ArrayList<String> max_list = conditions.get("max_price");
						max_list.remove(0);
						min_list.add(String.valueOf(max_price));
						max_list.add(String.valueOf(min_price));
						conditions.put("max_price", max_list);
						conditions.put("min_price", min_list);
					}
				} else {
					min_list.add(condition);
					conditions.put("min_price", min_list);
				}
				session.setAttribute("conditions", conditions);
				session.setAttribute("vehicle_list", vehicleService.getVehicleListByQuery(conditions));
				nextPage = "/index.jsp";
			} else if (action.equals("/selectMaxPrice.do")) {
				HttpSession session = request.getSession();
				HashMap<String, ArrayList<String>> conditions = (HashMap<String, ArrayList<String>>) session.getAttribute("conditions");
				String condition = (String) request.getParameter("condition");
				ArrayList<String> max_list = new ArrayList<String>();
				if (conditions.containsKey("min_price")) {
					int min_price = Integer.parseInt(conditions.get("min_price").get(0));
					int max_price = Integer.parseInt(condition);
					if (min_price > max_price) {
						ArrayList<String> min_list = conditions.get("min_price");
						min_list.remove(0);
						min_list.add(String.valueOf(max_price));
						max_list.add(String.valueOf(min_price));
						conditions.put("max_price", max_list);
						conditions.put("min_price", min_list);
					}
				} else {
					max_list.add(condition);
					conditions.put("max_price", max_list);
				}
				session.setAttribute("conditions", conditions);
				session.setAttribute("vehicle_list", vehicleService.getVehicleListByQuery(conditions));
				nextPage = "/index.jsp";
			} else if (action.equals("/selectLocation.do")) {
				HttpSession session = request.getSession();
				HashMap<String, ArrayList<String>> conditions = (HashMap<String, ArrayList<String>>) session.getAttribute("conditions");
				String condition = (String)request.getParameter("condition");
				boolean flag = false; int idx=0;
				ArrayList<String> location_list = null;
				if(conditions.containsKey("location")) {
					location_list = conditions.get("location");
					for(idx=0; idx<location_list.size(); idx++)
						if(location_list.get(idx).equals(condition)) {
							flag = true;
							break;
						}
					if(flag == true)
						location_list.remove(idx);
					else
						location_list.add(condition);
				} else {
					ArrayList<String> list = new ArrayList<String>();
					list.add(condition);
				}
				conditions.put("location", location_list);
				session.setAttribute("conditions", conditions);
				session.setAttribute("vehicle_list", vehicleService.getVehicleListByQuery(conditions));
				nextPage = "/index.jsp";
			} else if (action.equals("/selectColor.do")) {
				HttpSession session = request.getSession();
				HashMap<String, ArrayList<String>> conditions = (HashMap<String, ArrayList<String>>) session.getAttribute("conditions");
				String condition = (String)request.getParameter("condition");
				boolean flag = false; int idx=0;
				ArrayList<String> list = null;
				if(conditions.containsKey("color")) {
					list = conditions.get("color");
					for(idx = 0; idx < list.size(); idx++)
						if(list.get(idx).equals(condition)) {
							flag = true;
							break;
						}
					if(flag == true)
						list.remove(idx);
					else
						list.add(condition);
				}
				else {
					list = new ArrayList<String>();
					list.add(condition);
				}
				session.setAttribute("color", list);
				session.setAttribute("conditions", conditions);
				session.setAttribute("vehicle_list", vehicleService.getVehicleListByQuery(conditions));
				nextPage = "/index.jsp";
			} else if (action.equals("/selectFuel.do")) {
				HttpSession session = request.getSession();
				HashMap<String, ArrayList<String>> conditions = (HashMap<String, ArrayList<String>>) session.getAttribute("conditions");
				String condition = (String)request.getParameter("condition");
				boolean flag = false; int idx=0;
				ArrayList<String> list = null;
				if(conditions.containsKey("fuel")) {
					list = conditions.get("fuel");
					for(idx = 0; idx < list.size(); idx++)
						if(list.get(idx).equals(condition)) {
							flag = true;
							break;
						}
					if(flag == true)
						list.remove(idx);
					else
						list.add(condition);
				}
				else {
					list = new ArrayList<String>();
					list.add(condition);
				}
				session.setAttribute("fuel", list);
				session.setAttribute("conditions", conditions);
				session.setAttribute("vehicle_list", vehicleService.getVehicleListByQuery(conditions));
				nextPage = "/index.jsp";
			} else if (action.equals("/selectTransmission.do")) {
				HttpSession session = request.getSession();
				HashMap<String, ArrayList<String>> conditions = (HashMap<String, ArrayList<String>>) session.getAttribute("conditions");
				String condition = (String)request.getParameter("condition");
				boolean flag = false; int idx=0;
				ArrayList<String> list = null;
				if(conditions.containsKey("transmission")) {
					list = conditions.get("transmission");
					for(idx = 0; idx < list.size(); idx++)
						if(list.get(idx).equals(condition)) {
							flag = true;
							break;
						}
					if(flag == true)
						list.remove(idx);
					else
						list.add(condition);
				}
				else {
					list = new ArrayList<String>();
					list.add(condition);
				}				
				session.setAttribute("transmission", list);
				session.setAttribute("conditions", conditions);
				session.setAttribute("vehicle_list", vehicleService.getVehicleListByQuery(conditions));
				nextPage = "/index.jsp";
			} else if (action.equals("/addVehicle.do")) {
				// 매물 등록 작업
				String car_number = (String) request.getParameter("car_number");
				car_number = vehicleService.checkCarnumber(car_number);
				if (car_number == null) {
					nextPage = "/Vehicle/addVehicleFail.jsp";
					System.out.println("차량등록실패");
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

					if (Integer.parseInt(month) < 10)
						month = "0" + month;
					String model_year = year + "-" + month + "-" + "01";

					System.out.println(detailed_model + model_year + price + mileage + location + fuel + color + engine
							+ transmission + car_number + sellerId);
					vehicleService.addVehicle(detailed_model, model_year, price, mileage, location, fuel, color, engine,
							transmission, car_number, sellerId);
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
			} else if (action.equals("/modifyVehicle.do")) {
				String car_number = (String) request.getParameter("car_number");
				String detailed_model = (String) request.getParameter("detailed_model_name");
				int engine = Integer.parseInt((String) request.getParameter("engine_displacement"));
				String model_year = (String) request.getParameter("model_year");
				String fuel = (String) request.getParameter("fuel");
				String color = (String) request.getParameter("color");
				String transmission = (String) request.getParameter("transmission");
				int price = Integer.parseInt((String) request.getParameter("price"));
				int mileage = Integer.parseInt((String) request.getParameter("mileage"));
				String location = (String) request.getParameter("location");
				String sellerId = (String) request.getParameter("sellerID");

				// 456은 임의로 정해둔 regnum
				vehicleService.modifyVehicle(456, detailed_model, model_year, price, mileage, location, fuel, color,
						engine, transmission, car_number, sellerId);

				nextPage = "/index.jsp";
			} else if (action.equals("/clickModify.do")) {
				HttpSession session = request.getSession();
				VehicleVO carInfo = vehicleService.carInfo(456);// 임의로 정해둠
				session.setAttribute("carInfo", carInfo);

				ArrayList<String> fuelList = vehicleService.getFuelList();
				ArrayList<String> transList = vehicleService.getTransList();
				ArrayList<String> colorList = vehicleService.getColorList();

				session.setAttribute("fuel_list", fuelList);
				session.setAttribute("color_list", colorList);
				session.setAttribute("trans_list", transList);

				nextPage = "/Vehicle/modifyVehicle.jsp";
			}
			RequestDispatcher dispatcher = request.getRequestDispatcher(nextPage);
			dispatcher.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
