package com.orderlist.service;

import com.orderlist.dao.OrderlistDAO;

public class OrderlistService {
	OrderlistDAO orderlistDao;
	
	public OrderlistService() {
		orderlistDao = new OrderlistDAO();
	}
}
