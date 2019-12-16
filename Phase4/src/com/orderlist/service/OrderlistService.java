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
}
