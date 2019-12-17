package com.vehicle.service;

import java.sql.Date;
import java.util.*;

import com.vehicle.dao.VehicleDAO;
import com.vehicle.vo.VehicleVO;

public class VehicleService {
	VehicleDAO vehicleDAO;
	
	public VehicleService() {
		this.vehicleDAO = new VehicleDAO();
	}
	
	public VehicleVO carInfo(int regnum) {
		VehicleVO car = null;
		
		car = vehicleDAO.carInfo(regnum);
		
		return car;
	}
	
	public boolean modifyVehicle(int regnum,String detailed_model_name,String model_year,int price,int mileage,String location,String fuel,String color,int engine_displacement,String transmission,String car_number,String sellerId) {		
		boolean ret = false;
		
		VehicleVO newVehicle = new VehicleVO(detailed_model_name,model_year, price, mileage, location, fuel, color,engine_displacement, transmission, car_number, sellerId);
		
		ret = vehicleDAO.modifyVehicle(newVehicle,regnum);
		
		if(ret == true)
			System.out.println("성공!!!!");
		
		return ret;
	}
	
	public boolean addVehicle(String detailed_model_name,String model_year,int price,int mileage,String location,String fuel,String color,int engine_displacement,String transmission,String car_number,String sellerId) {		
		boolean ret = false;
		
		VehicleVO newVehicle = new VehicleVO(detailed_model_name,model_year, price, mileage, location, fuel, color,engine_displacement, transmission, car_number, sellerId);
		
		ret = vehicleDAO.insertVehicle(newVehicle);
		
		if(ret == true)
			System.out.println("성공!!!!");
		
		return ret;
	}

	public ArrayList<String> getMakeList() {
		ArrayList<String> A=vehicleDAO.getMake();
		return A;
	}
	
	public ArrayList<String> getModelList(String make){
		ArrayList<String> A = vehicleDAO.getModel(make);
		return A;
	}

	public ArrayList<String> getDetailelList(String model){
		ArrayList<String> A = vehicleDAO.getDetaile(model);
		return A;
	}

	public ArrayList<String> getEngineList(String detaile){
		ArrayList<String> A = vehicleDAO.getEngine(detaile);
		return A;
	}
	
	public ArrayList<String> getFuelList(){
		ArrayList<String> A = vehicleDAO.getFuel();
		return A;
	}
	
	public ArrayList<String> getColorList(){
		ArrayList<String> A = vehicleDAO.getColor();
		return A;
	}
	
	public ArrayList<String> getTransList(){
		ArrayList<String> A = vehicleDAO.getTrans();
		return A;
	}
	
	public String checkCarnumber(String carnum) {
		String A = vehicleDAO.getCarnumber(carnum, 0);
		return A;
	}
}
