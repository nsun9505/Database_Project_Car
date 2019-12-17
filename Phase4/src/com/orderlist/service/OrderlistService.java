package com.orderlist.service;

import java.util.*;

import com.orderlist.dao.OrderlistDAO;
import com.orderlist.vo.OrderlistVO;

public class OrderlistService {
	OrderlistDAO orderlistDao;
	
	public OrderlistService() {
		orderlistDao = new OrderlistDAO();
	}
	
	public boolean openVehicle(int regnum) {
		boolean ret = false;
		
		ret = orderlistDao.openVehicle(regnum);
		if(ret == true)
			System.out.println("성공!");
		
		return ret;
	}
	
	public boolean secretVehicle(int regnum,String id) {
		boolean ret = false;
		
		ret = orderlistDao.secretVehicle(regnum,id);
		
		if(ret == true)
			System.out.println("성공!");
		
		return ret;
	}

	public ArrayList<OrderlistVO> getCustomerOrderList(String id) {
		ArrayList<OrderlistVO> customerOrderList = null;
		customerOrderList = orderlistDao.getCustomerOrderList(id);
		if(customerOrderList.size() > 0)
			return customerOrderList;
		else
			return null;
	}

	public ArrayList<OrderlistVO> getAdminOrderList(String id) {
		ArrayList<OrderlistVO> adminOrderList = null;
		adminOrderList = orderlistDao.getAdminOrderList(id);
		if(adminOrderList.size() > 0)
			return adminOrderList;
		else 
			return null;
	}

	public long getIntake(String make, String year, String month, String type) {
		int flag = getFlag(type);
		long ret = -1;
		if(flag != -1)
			ret = orderlistDao.getIntakeByFlag(year, month, make, flag);
		return ret;
	}
	
	public static int getFlag(String type) {
		switch(type) {
		case "yearTotal":
			return 1;
		case "monthTotal":
			return 2;
		case "systemTotal":
			return 3;
		case "makeYear":
			return 4;
		case "makeMonth":
			return 5;
		case "makeTotal":
			return 6;
		}
		return -1;
	}
	
	public String getIntakeString(String type, String make, String year, String month) {
		switch(type) {
		case "yearTotal":
			return year + "년도 총 매출액";
		case "monthTotal":
			return year +"년 "+month+"월 총 매출액";
		case "systemTotal":
			return "총 매출액";
		case "makeYear":
			return make + "의 "+year+"년도 총 매출액";
		case "makeMonth":
			return make+"의 "+year+"년 "+month+"월 총 매출액";
		case "makeTotal":
			return make + " 총 매출액";
		}
		return null;
	}
}
