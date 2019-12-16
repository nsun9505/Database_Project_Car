package com.orderlist.service;

import java.util.ArrayList;

import com.orderlist.dao.OrderlistDAO;
import com.orderlist.vo.OrderlistVO;

public class OrderlistService {
	OrderlistDAO orderlistDao;
	
	public OrderlistService() {
		orderlistDao = new OrderlistDAO();
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
