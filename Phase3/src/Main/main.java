package Main;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import page.Pagination;
import vehicle.BasicVehicleInfoDTO;
import vehicle.DetailVehicleInfoDTO;
import vehicle.VehicleDAO;

public class main {
	public static void main(String[] args) {
		VehicleDAO VDao = new VehicleDAO();
		/*
		 * ArrayList<String> metadata = VDao.getTableColumnMetaData("ALL_VEHICLE_INFO");
		 * for(int i=0; i<metadata.size(); i++) System.out.print(metadata.get(i)+ "\t");
		 * System.out.println("\n");
		 */
//		ArrayList<String> columnNames = new ArrayList<String>();
//		ArrayList<BasicVehicleInfoDTO> list = VDao.getAllSellingVehicleInfo(columnNames);

		// 전체 매물 출력 기능 완료
		/*
		System.out.printf("%-20s %-15s %-20s %-10s %-7s %-15s %-20s %-15s %-20s\n", columnNames.get(0),
				columnNames.get(1), columnNames.get(2), columnNames.get(3), columnNames.get(5), columnNames.get(6),
				columnNames.get(7), columnNames.get(8), columnNames.get(4));

		for (int i = 0; i < list.size(); i++) {
			BasicVehicleInfoDTO dto = list.get(i);
			System.out.printf("%-20d %-15s %-20s %-10s %-7d %-15s %-20s %-15s %-20d\n", dto.getRegNum(), dto.getMake(),
					dto.getDetailed_model_name(), dto.getModel_year().toString(), dto.getMileage(), dto.getLocation(),
					dto.getFuel(), dto.getColor(), dto.getPrice());
		}
		*/
		// registration number를 통한 매물 상세 정보 가져오기 기능 ok
		// DetailVehicleInfoDTO ret = VDao.getVehicleInfoByRegNum(1500);
		
		Scanner sc = new Scanner(System.in);
		HashMap<String, ArrayList<String>> conditions = new HashMap<String, ArrayList<String>>();
		while(true) {
			System.out.println("1. 차종 선택");
			System.out.println("2. 제조사/모델/세부모델 선택");
			System.out.println("3. 연식 선택");
			System.out.println("4. 주행거리 선택");
			System.out.println("5. 가격 선택");
			System.out.println("6. 지역 선택");
			System.out.println("7. 색상 선택");
			System.out.println("8. 연료 선택");
			System.out.println("9. 변속기 선택");
			System.out.println("10. 모델 또는 세부 모델명으로만 검색하기");
			System.out.println("11. 등록번호(regNum)로 매물 세부 정보 보기");
			System.out.println("0. 조건 빼기");
			System.out.println("나가기 : exit");
			System.out.println("현재 선택한 옵션 내용 : ");
			System.out.println("선택 : ");
			String sel = sc.nextLine();
			
			if(sel.equals("1")){
				
				// 차종 조건 추가
				if(conditions.get("category") == null) 
					conditions.put("category", new ArrayList<String>());
			} else if(sel.equals("2")) {
				// 제조사/모델/세부모델 조건 추가
				if(conditions.get("make") == null)
					conditions.put("make", new ArrayList<String>());
			} else if(sel.equals("3")) {
				// 연식 조건 추가
				if(conditions.get("model_year") == null)
					conditions.put("model_year", new ArrayList<String>());
			} else if(sel.equals("4")) {
				// 주행거리 조건 추가
				if(conditions.get("model_year") == null)
					conditions.put("model_year", new ArrayList<String>());
			} else if(sel.equals("5")) {
				// 가격 조건 추가
				if(conditions.get("model_year") == null)
					conditions.put("model_year", new ArrayList<String>());
			} else if(sel.equals("6")) {
				// 지역 조건 추가
				if(conditions.get("model_year") == null)
					conditions.put("model_year", new ArrayList<String>());
			} else if(sel.equals("7")) {
				// 색상 조건 추가
				if(conditions.get("model_year") == null)
					conditions.put("model_year", new ArrayList<String>());
			} else if(sel.equals("8")) {
				// 연로 조건 추가
				if(conditions.get("model_year") == null)
					conditions.put("model_year", new ArrayList<String>());
			} else if(sel.equals("9")) {
				// 변속기 조건 추가
				if(conditions.get("model_year") == null)
					conditions.put("model_year", new ArrayList<String>());
			} else if(sel.equals("10")) {
				while(true) {
					// 모델명 혹은 세부 모델명으로 검색
					System.out.println("1. 모델이름으로 검색하기  \t 2. 세부모델명으로 검색하기 \t 3. 나가기");
					System.out.print("선택 : ");
					String secondSel = sc.nextLine();
					if(secondSel.equals("1")) {
					
					} else if(secondSel.equals("2")) {
					
					} else if(secondSel.equals("3")) {
						System.out.println("3번 선택, 모델명 또는 세부모델명으로 검색하기를 종료합니다.");
						break;
					} else {
						System.out.println("유효하지 않은 값입니다. 다시 입력해주세요.");
					}
				}
			} else if(sel.equals("11")) {
				// 등록번호로 검색된 차량 세부 정보 보기
				String regNum = null;
				boolean flag = false;
				while(true) {
					System.out.print("등록번호(registration number) 입력 [나가기 : exit] : ");
					regNum = sc.nextLine();
					
					if(regNum.equals("exit"))
						break;
					
					if(regNum.matches("[0-9]*") == false) {
						System.out.println("입력한 값은 숫자가 아닙니다. 오로지 숫자만 입력하세요.");
						continue;
					}
					else {
						flag = true;
						break;
					}
				}
				if(flag) {
					DetailVehicleInfoDTO info = VDao.getVehicleInfoByRegNum(Integer.parseInt(regNum));
					// 구매하기 기능 필요.
					// 구매한 경우는 true를 리턴하여 main 화면으로 나가고, 구매하지 않고 정보만 본 경우 false를 리턴하여 조건 검색을 계속 실행
					flag = showDetailVehicleInfo(info);
				}
			} else if(sel.equals("0")) {
				// 조건 빼기
			} else if(sel.equals("exit")) {
				// 조건검색 나가기
				System.out.println("조건 검색으로 종료합니다.");
				break;
			} else {
				// 그 이외의 값을 입력한 경우 에러 메시지 출력
				System.out.println("현재 입력한 "+sel+"값은 유요한 값이 아닙니다. 다시 입력해주세요.");
			}
		}
	}

	private static boolean showDetailVehicleInfo(DetailVehicleInfoDTO info) {
		boolean isBuy = false;
		Scanner sc = new Scanner(System.in);
		System.out.println("선택하신 차량의 세부 정보입니다.");
		info.printInfo();
		while (true) {
			System.out.println("1. 구매하기\t2.뒤로가기");
			String sel = sc.nextLine();
			if(sel.equals("1")) {
				while (true) {
					System.out.print("정말로 구매하시겠습니까?(Y/N) : ");
					sel = sc.nextLine().toUpperCase();
					if(sel.equals("Y")) {
						isBuy = true;
						break;
					}else if(sel.equals("N")) {
						System.out.println("구매를 취소합니다.");
						break;
					}else {
						System.out.println("Y(y) 또는 N(n) 값 중 하나만 입력해주세요.");
					}
				}
			}
			else if(sel.equals("2")) {
				System.out.println("뒤로 돌아갑니다.");
				break;
			}else {
				System.out.println("1 또는 2 둘 중 하나의 값만 입력해주세요.");
			}
		}
		return isBuy;
	}
}
