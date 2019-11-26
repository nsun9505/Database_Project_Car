package Main;

import java.sql.Date;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import account.AccountDAO;
import account.AccountDTO;
import adminmode.adminModeDAO;
import orderlist.OrderListDAO;
import page.Pagination;
import vehicle.BasicVehicleInfoDTO;
import vehicle.DetailVehicleInfoDTO;
import vehicle.VehicleDAO;

public class main {
	public static void main(String[] args) {
		String pw = "";
		String id = "test100";
		String sel;
		Scanner sc = new Scanner(System.in);
		AccountDTO user = null;
		boolean isLoginOk = false;
		while(true) {
			System.out.println("1.로그인\n2.회원가입\n3.나가기");
			System.out.print("선택 : ");
			sel = sc.nextLine();
			if(sel.equals("1")) {
				user = login();
				if(user != null) {
					if(user.getAccount_type().equals("C")) {
						CustomerMenu(user.getId(), user.getName(), user.getAccount_type());
					} else {
						adminMenu(user.getId(), user.getName(), user.getAccount_type());
					}
				}
			}
			else if(sel.equals("2"))
				join();
			else if(sel.equals("3")) {
				System.out.println("프로그램 종료");
				break;
			}
			VehicleDAO.clearScreen();
		}
		// 조건 검색 기능 추가 중 19.11.22
		// conditionSearch(new VehicleDAO(), id);
		
	}
	
	
	private static void adminMenu(String id, String name, String type) {
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.println("1. 회원 정보 수정");
			System.out.println("2. 차량검색");
			System.out.println("3. 거래내역");
			System.out.println("4. 매물 등록 및 수정");
			System.out.println("5. 로그아웃");
			System.out.print("선택 : ");
			String sel = sc.nextLine();
			
			if(sel.equals("1")) {
				if(modifyAccountInfo(id, name, type) == false)
					return;
			}else if(sel.equals("2")) {
				searchVehicle(id);
			}else if(sel.equals("3")) {
				seeAllOrderList();
			}else if(sel.equals("4")) {
				RegisterAndModifyVehicle();
			}else if(sel.equals("5")) {
				System.out.println(id+"님 안녕히가세요.");
				break;
			}else {
				System.out.println("올바르지 않은 입력입니다. 다시 입력 부탁드립니다.");
			}
		}
	}


	private static void seeAllOrderList() {
		OrderListDAO dao = new OrderListDAO();
		dao.printAllOrderlist();
	}

	private static void RegisterAndModifyVehicle() {
		adminModeDAO dao = new adminModeDAO();
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.println("1. 매물 등록");
			System.out.println("2. 매물 수정");
			System.out.println("3. 매물 공개/비공개 설정");
			System.out.println("4. 나가기");
			System.out.print("선택 : ");
			String sel = sc.nextLine();
			
			if(sel.equals("1")){
				dao.insertVehicle();
			} else if(sel.equals("2")) {
				dao.updateVehicle();
			} else if(sel.equals("3")) {
				//
			} else if(sel.equals("4")) {
				break;
			} else{
				System.out.println("올바르지 않은 입력입니다. 다시 입력 부탁드립니다.");
			}
		}
	}

	private static void CustomerMenu(String id, String name, String type) {
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.println("1. 회원 정보 수정");
			System.out.println("2. 차량검색");
			System.out.println("3. 거래내역");
			System.out.println("4. 로그아웃");
			System.out.print("선택 : ");
			String sel = sc.nextLine();
			
			if(sel.equals("1")) {
				if(modifyAccountInfo(id, name, type) == false)
					return;
			}else if(sel.equals("2")) {
				searchVehicle(id);
			}else if(sel.equals("3")) {
				seeMyOrderList(id);
			}else if(sel.equals("4")) {
				System.out.println(id+"님 안녕히가세요.");
				break;
			}else {
				System.out.println("올바르지 않은 입력입니다. 다시 입력 부탁드립니다.");
			}
		}
	}


	private static void seeMyOrderList(String id) {
		OrderListDAO dao = new OrderListDAO();
		dao.printMyOrderlist(id);
	}

	private static void searchVehicle(String id) {
		VehicleDAO.conditionSearch(id);
	}

	private static boolean modifyAccountInfo(String id, String name, String type) {
		String sel ="";
		boolean isExist = true;
		Scanner sc = new Scanner(System.in);
		AccountDAO dao = new AccountDAO();
		while(true) {
			System.out.println("1. 회원 정보 수정");
			System.out.println("2. 회원 탈퇴");
			System.out.println("3. 나가기");
			System.out.print("선택 : ");
			sel = sc.nextLine();
			
			if(sel.equals("1"))
				dao.modifyAccountInfo(id);
			else if(sel.equals("2")) {
				isExist = dao.withdrawalAccount(id, name, type);
				if(isExist == false)
					return isExist;
			}
			else if(sel.equals("3"))
				break;
			else 
				System.out.println("올바르지 않은 입력입니다. 다시 입력 부탁드립니다.");
		}
		return isExist;
	}


	private static void join() {
		AccountDAO dao = new AccountDAO();
		dao.joinAccount();
	}
	private static AccountDTO login() {
		AccountDAO accountDao = new AccountDAO();
		AccountDTO ret = null;
		ret = accountDao.login();
		return ret;
	}
}
// 테스트용 코드 주석
/*
 * ArrayList<String> metadata = VDao.getTableColumnMetaData("ALL_VEHICLE_INFO");
 * for(int i=0; i<metadata.size(); i++) System.out.print(metadata.get(i)+ "\t");
 * System.out.println("\n");

ArrayList<String> columnNames = new ArrayList<String>();
ArrayList<BasicVehicleInfoDTO> list = VDao.getAllSellingVehicleInfo(columnNames);

// 전체 매물 출력 기능 완료 필요없음...

 * System.out.printf("%-20s %-15s %-20s %-10s %-7s %-15s %-20s %-15s %-20s\n",
 * columnNames.get(0), columnNames.get(1), columnNames.get(2),
 * columnNames.get(3), columnNames.get(5), columnNames.get(6),
 * columnNames.get(7), columnNames.get(8), columnNames.get(4));
 * 
 * for (int i = 0; i < list.size(); i++) { BasicVehicleInfoDTO dto =
 * list.get(i);
 * System.out.printf("%-20d %-15s %-20s %-10s %-7d %-15s %-20s %-15s %-20d\n",
 * dto.getRegNum(), dto.getMake(), dto.getDetailed_model_name(),
 * dto.getModel_year().toString(), dto.getMileage(), dto.getLocation(),
 * dto.getFuel(), dto.getColor(), dto.getPrice()); }
 
 registration number를 통한 매물 상세 정보 가져오기 기능 ok
 DetailVehicleInfoDTO ret = VDao.getVehicleInfoByRegNum(1500);
*/
