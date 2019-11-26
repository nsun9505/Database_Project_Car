package vehicle;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import page.Pagination;

public class VehicleDAO {
	private static final String getBasicVehicleInfoQuery = "select registration_number, make, detailed_model_name, model_year, price, mileage, location, fuel, color from ALL_VEHICLE_INFO where registration_number not in (select registration_number from order_list) order by registration_number desc";
	private static final String getTableColumnNamesQuery = "select cname from col where tname=?";
	private static final String getMakeListQuery = "select * from make";
	private static final String searchQuery = "select ? from selling_car";
	private static final String url = "jdbc:oracle:thin:@localhost:1600:xe";
	private static final String user = "knu";
	private static final String pw = "comp322";
	private static final String buyVehicleQuery = "insert into order_list values(?, ?, ?, sysdate)";
	private PreparedStatement pstmt;
	private Connection con;

	public VehicleDAO() {
		connDB();
	}
	
	// commit 추가
	public void updateSellerId(String id) {
		int ret = 0;
		try {
			pstmt = con.prepareStatement("update vehicle set seller_id='admin' where seller_id=?");
			pstmt.setString(1, id);
			ret = pstmt.executeUpdate();
			con.commit();
		}catch(SQLException e) {
			System.err.println("");
		}
	}

	public ArrayList<String> getTableColumnMetaData(String table_name) {
		ArrayList<String> metadata = new ArrayList<String>();
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(getTableColumnNamesQuery);
			pstmt.setString(1, table_name);
			rs = pstmt.executeQuery();

			while (rs.next())
				metadata.add(rs.getString(1));

		} catch (SQLException e) {
			System.err.println("[getTableColumnMetaData()] sql error " + e.getMessage());
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getTableColumnMetaData()] sql error " + e.getMessage());
			}
		}
		return metadata;
	}
	
	// commit 추가 OK
	public void deleteVehicle(int regNum) {
		try {
			pstmt = con.prepareStatement("delete from vehicle where registration_number=?");
			pstmt.setInt(1, regNum);
			int ret = pstmt.executeUpdate();
			con.commit();
		}catch(SQLException e) {
			System.err.println("[deleteVehicle] sql error : "+e.getMessage());
		}
	}

	// 전체를 출력할 필요는 없음.
	// 즉 전체 정보를 DB에서 가져올 필요가 없고, 기본 정보만 출력하고
	// 전체 정보는 Registration_number로 DB에 요청했을 때만 전체 정보를 출력.
	// 기본 정보에 대한 정의 필요
	public ArrayList<BasicVehicleInfoDTO> getAllSellingVehicleInfo(ArrayList<String> metadata) {
		ArrayList<BasicVehicleInfoDTO> list = new ArrayList<BasicVehicleInfoDTO>();
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(getBasicVehicleInfoQuery);
			rs = pstmt.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			int cnt = rsmd.getColumnCount();
			for (int i = 1; i <= cnt; i++)
				metadata.add(rsmd.getColumnName(i));
			while (rs.next()) {
				list.add(new BasicVehicleInfoDTO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4),
						rs.getInt(5), rs.getInt(6), rs.getString(7), rs.getString(8), rs.getString(9)));
			}

		} catch (SQLException e) {
			System.err.println("[getSellingVehicleInfo()] sql error " + e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getSellingVehicleInfo()] sql error " + e.getMessage());
			}
		}

		return list;
	}

	public ArrayList<BasicVehicleInfoDTO> getBasicVehicleInfoByQuery(String whereClusure, ArrayList<String> metadata) {
		ArrayList<BasicVehicleInfoDTO> list = new ArrayList<BasicVehicleInfoDTO>();
		ResultSet rs = null;
		String query = "select registration_number, make, detailed_model_name, model_year, price, mileage, location, fuel, color "
				+ "from ALL_VEHICLE_INFO "
				+ "where registration_number not in (select registration_number from order_list) ";
		if (whereClusure != null)
			query += "AND " + whereClusure;
		query += " order by registration_number desc";
//		System.out.println(query);
		try {
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int cnt = rsmd.getColumnCount();
			for(int i=1; i<=cnt; i++)
				metadata.add(rsmd.getColumnName(i));
			while (rs.next()) {
				list.add(new BasicVehicleInfoDTO(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4),
						rs.getInt(5), rs.getInt(6), rs.getString(7), rs.getString(8), rs.getString(9)));
			}
		} catch (SQLException e) {
			System.err.println("[getBasicVehicleInfoByQuery] sql error : " + e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getSellingVehicleInfo()] sql error " + e.getMessage());
			}
		}

		return list;
	}
	
	public HashMap<String, String> isExistModelName(String model_name) {
		ResultSet rs = null;
		HashMap<String, String> map = null;
		try {
			pstmt = con.prepareStatement("select distinct make, model_name from ALL_VEHICLE_INFO where registration_number not in (select registration_number from ORDER_LIST) AND model_name = ?");
			pstmt.setString(1, model_name);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				map = new HashMap<String, String>();
				map.put("make", rs.getString(1));
				map.put("model_name", rs.getString(2));
				return map;
			}
			else 
				return null;
		}catch(SQLException e) {
			System.err.println("[isExistModelName] sql error : "+e.getMessage());
			System.exit(1);
		}
		return null;
	}
	
	public HashMap<String, String> isExistDetailedModelName(String detailed_model_name){
		ResultSet rs = null;
		HashMap<String, String> map = null;
		try {
			pstmt = con.prepareStatement("select distinct make, model_name, detailed_model_name from ALL_VEHICLE_INFO where registration_number not in (select registration_number from ORDER_LIST) AND detailed_model_name = ?");
			pstmt.setString(1, detailed_model_name);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				map = new HashMap<String, String>();
				map.put("make", rs.getString(1));
				map.put("model_name", rs.getString(2));
				map.put("detailed_model_name", rs.getString(3));
				return map;
			}
			else 
				return null;
		}catch(SQLException e) {
			System.err.println("[isExistDetailedModelName] sql error "+e.getMessage());
		}
		return null;
	}

	public ArrayList<String> getMakeList() {
		ArrayList<String> makeList = new ArrayList<String>();
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(getMakeListQuery);
			rs = pstmt.executeQuery();
			while (rs.next())
				makeList.add(rs.getString(1));
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			System.err.println("[getMakeList] sql error : " + e.getMessage());
		}
		return makeList;
	}

	public ArrayList<String> getModelList(String make) {
		ArrayList<String> modelList = new ArrayList<String>();
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement("select model_name from model where make=?");
			pstmt.setString(1, make);
			rs = pstmt.executeQuery();
			while (rs.next())
				modelList.add(rs.getString(1));
		} catch (SQLException e) {
			System.err.println("[getModelList] sql error : " + e.getMessage());
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getModelList] sql error : " + e.getMessage());
			}
		}
		return modelList;
	}

	public ArrayList<String> getDetailedModelList(String model_name) {
		ArrayList<String> dModelList = new ArrayList<String>();
		ResultSet rs = null;
		String query = "select detailed_model_name from detailed_model";

		// detailed_model_name만 선택해서 검색하는 경우
		if (model_name == null)
			query += " where model_name=?";

		try {
			pstmt = con.prepareStatement(query);

			// detailed_model_name만 선택해서 검색하는 경우
			if (model_name != null)
				pstmt.setString(1, model_name);

			rs = pstmt.executeQuery();
			while (rs.next())
				dModelList.add(rs.getString(1));

		} catch (SQLException e) {
			System.err.println("[getDetailedModelList] sql error : " + e.getMessage());
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getDetailedModelList] sql error : " + e.getMessage());
			}
		}
		return dModelList;
	}

	public DetailVehicleInfoDTO getVehicleInfoByRegNum(int regNum) {
		DetailVehicleInfoDTO ret = null;
		String query = "select * from ALL_VEHICLE_INFO where registration_number=? AND registration_number not in (select registration_number from order_list)";
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, regNum);
			rs = pstmt.executeQuery();

			if (rs.next())
				ret = new DetailVehicleInfoDTO(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getDate(9), rs.getString(10),
						rs.getInt(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15), rs.getString("seller_id"));
			else
				ret = null;
		} catch (SQLException e) {
			System.err.println("[getVehicleInfoByRegNum] sql error : " + e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getVehicleInfoByRegNum] sql error " + e.getMessage());
			}
		}
		return ret;
	}

	protected void finalize() throws Throwable {
		pstmt.close();
		con.close();
	}

	private void connDB() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(url, user, pw);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("sql error : " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> getConditionList(String columnNmae, String whereClusure) {
		ResultSet rs = null;
		ArrayList<String> list = new ArrayList<String>();
		String query = "select distinct " + columnNmae
				+ " from ALL_VEHICLE_INFO WHERE registration_number not in (select registration_number from order_list) ";
		if (whereClusure != null)
			query += "AND " + whereClusure;
		// System.out.println(query);
		try {
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(rs.getString(1));
		} catch (SQLException e) {
			System.err.println("[getConditionList] sql error : " + e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				System.err.println("[getConditionList] sql error : " + e.getMessage());
			}
		}
		return list;
	}

	// commit 완료
	public void buyVehicle(String buyerId, int regNum, String seller_id) {
		int ret = 0;
		try {
			pstmt = con.prepareStatement(buyVehicleQuery);
			pstmt.setInt(1, regNum);
			pstmt.setString(2, seller_id);
			pstmt.setString(3, buyerId);
			ret = pstmt.executeUpdate();
			if(ret == 1)
				System.out.println("구매 성공!");
			con.commit();
		}catch(SQLException e) {
			System.err.println("[buyVehicle] sql error : " + e.getMessage());
		}
	}
	
	public static void conditionSearch(String id, String account_type) {
		boolean isUpdateCondition = true;
		Scanner sc = new Scanner(System.in);
		VehicleDAO VDao = new VehicleDAO();
		
		HashMap<String, ArrayList<String>> conditions = new HashMap<String, ArrayList<String>>();
		ArrayList<String> columnNames = new ArrayList<String>();
		ArrayList<BasicVehicleInfoDTO> vehicleList = null;
		Pagination page = null;
		while (true) {
			if(isUpdateCondition) {
				String whereClusure = getWhereClusureByColumn(conditions, "all");
				vehicleList = VDao.getBasicVehicleInfoByQuery(whereClusure, columnNames);
				page = new Pagination(vehicleList.size());
			}
			System.out.print("검색된 매물 : "+vehicleList.size()+"개\t");
			System.out.println("("+page.getCurPage()+"/"+page.getTotalPage()+")");
			System.out.printf("%-20s %-15s %-20s %-10s %-7s %-15s %-20s %-15s %-20s\n",
					 columnNames.get(0), columnNames.get(1), columnNames.get(2),
					 columnNames.get(3), columnNames.get(5), columnNames.get(6),
					 columnNames.get(7), columnNames.get(8), columnNames.get(4));
			if (vehicleList.size() > 0) {
				for (int i = page.getStartIdx(); i < page.getLastIdx(); i++) {
					vehicleList.get(i).printBasicVehicleInfo();
				}
			}else
				System.out.println("해당 조건에 맞는 매물이 없습니다.");
			
			System.out.print("1. 차종 선택\t\t\t");
			System.out.print("2. 제조사/모델/세부모델 선택\t\t");
			System.out.print("3. 연식 선택\t\t\t");
			System.out.println("4. 주행거리 선택");
			System.out.print("5. 가격 선택\t\t\t");
			System.out.print("6. 지역 선택\t\t\t");
			System.out.print("7. 색상 선택\t\t\t");
			System.out.println("8. 연료 선택");
			System.out.print("9. 변속기 선택\t\t\t");
			System.out.print("10. 모델 또는 세부 모델명으로만 검색하기\t");
			System.out.println("11. 등록번호(regNum)로 매물 세부 정보 보기\t");
			System.out.print("0. 조건 빼기\t\t\t");
			System.out.println("exit. 나가기\t\t\t>.다음페이지\t\t\t<.이전 페이지");
			System.out.println("[현재 선택된 조건]");
			printCurrentCondition(conditions);
			System.out.print("선택 : ");
			String sel = sc.nextLine();
			
			if (sel.equals("1")) {
				isUpdateCondition = setColumnCondition("category", conditions);
			} else if (sel.equals("2")) {
				// 제조사/모델/세부모델 조건 추가 
				String column = "";
				if(conditions.containsKey("make") && conditions.containsKey("model_name")) {
					column = "detailed_model_name";
					System.out.println("선택한 제조사(make) : "+ conditions.get("make").toString());
					System.out.println("선택한 모델(model_name) : "+ conditions.get("model_name").toString());
				} else if(conditions.containsKey("make") && conditions.containsKey("detailed_model") == false) {
					column = "model_name";
					System.out.println("선택한 제조사(make) : " +conditions.get("make").toString());
				} else if(conditions.containsKey("detailed_model_name") == false && conditions.containsKey("model_name") == false) {
					column = "make";
				}
				isUpdateCondition = setColumnCondition(column, conditions);
			} else if(sel.equals("3")) { 
				// 연식 조건 추가 
				isUpdateCondition = setModelYearCondition(conditions);
			} else if(sel.equals("4")) {
				// 주행거리 조건 추가
				isUpdateCondition = setMileageCondition(conditions);
			}else if(sel.equals("5")) { 
				//최소, 최대 가격 조건 추가 
				isUpdateCondition = setPriceCondition(conditions);
			} else if(sel.equals("6")) { 
				// 지역 조건 추가 
				isUpdateCondition = setColumnCondition("location",conditions);
			} else if(sel.equals("7")) { 
				// 색상 조건 추가  
				isUpdateCondition = setColumnCondition("color", conditions);
			} else if(sel.equals("8")) { 
				// 연료 조건 추가 
				isUpdateCondition = setColumnCondition("fuel", conditions);
			} else if(sel.equals("9")) { 
				// 변속기 조건 추가 
				isUpdateCondition = setColumnCondition("transmission", conditions);
			} else if(sel.equals("10")) {
				String select = getSelectCondition("모델이름으로 검색하기", "세부모델명으로 검색하기", "model_name", "detailed_model_name");
				if(select.equals("detailed_model_name"))
					isUpdateCondition = setConditionByDetailedModelName(conditions);
				else if(select.equals("model_name"))
					isUpdateCondition = setConditionsByModelName(conditions);
				else
					isUpdateCondition = false;
			} else if(sel.equals("11")) { 
				// 등록번호로 검색된 차량 세부 정보 보기
				DetailVehicleInfoDTO dto = searchVehicleByRegNum(VDao);
				if (dto != null) {
					isUpdateCondition = decidePurchase(dto, VDao, id, account_type);
					if(isUpdateCondition == true) {
						ArrayList<String> keys = new ArrayList<String>(); 
						for(String key : conditions.keySet())
							keys.add(key);
						for(String key : keys)
							conditions.remove(key);
					}
				}
				else
					isUpdateCondition = false;
			} else if(sel.equals("0")) {
				isUpdateCondition = cancelCondition(conditions);
			} else if(sel.equals(">")) {
				if(page.nextPage() == false)
					System.out.println("마지막 페이지 입니다.");
				isUpdateCondition = false;
			} else if(sel.equals("<")) {
				if(page.prevPage() == false)
					System.out.println("첫 페이지입니다.");
				isUpdateCondition = false;
			} else if(sel.equals("exit")) { 
				// 조건검색 나가기
				System.out.println("조건 검색을 종료합니다."); 
				break;
			} else { 
				// 그 이외의 값을 입력한 경우 에러 메지시 출력
				System.out.println("현재 입력한 "+sel+"값은 유요한 값이 아닙니다. 다시 입력해주세요."); 
				isUpdateCondition = false;
			}
		}
		
	}
	private static void printCurrentCondition(HashMap<String, ArrayList<String>> conditions) {
		if(conditions.isEmpty())
			return;
		
		for (String key : conditions.keySet())
			System.out.println(key + " : "+conditions.get(key).toString()+ " ");	
	}
	private static boolean decidePurchase(DetailVehicleInfoDTO dto, VehicleDAO VDao, String buyerId, String user_type) {
		Scanner sc = new Scanner(System.in);
		boolean isUpdateCondition = false;
		while (true) {
			dto.printInfo();
			System.out.println("1.구매하기\t2.뒤로가기");
			System.out.print("선택 : ");
			String sel = sc.nextLine();
			if (sel.equals("1")) {
				if(user_type.equals("A")) {
					System.out.println("관리자 계정으로는 구매할 수 없습니다.");
					return false;
				}
				if(getAnswerYesOrNo("정말로 구매하시겠습니까?(Y/N)", sc) == true) {
					VDao.buyVehicle(buyerId, dto.getRegNum(), dto.getSellerId());
					isUpdateCondition = true;
					break;
				}
				else {
					System.out.println("차량을 구매하지 않습니다.");
					isUpdateCondition = false;
					break;
				}
			}else if (sel.equals("2")) {
				isUpdateCondition = false;
				break;
			} else {
				System.out.println("유효한 범위의 값이 아닙니다. 다시 입력 부탁드립니다.");
			}
		}
		return isUpdateCondition;
	}
	
	private static boolean getAnswerYesOrNo(String message, Scanner sc) {
		while(true) {
			System.out.println(message);
			String answer = sc.nextLine().toUpperCase();
			if(answer.equals("Y"))
				return true;
			else if(answer.equals("N"))
				return false;
			else
				System.out.println("Y,y 또는 N,n만 입력해주세요. 다시 입력 부탁드립니다.");
		}
	}
	private static DetailVehicleInfoDTO searchVehicleByRegNum(VehicleDAO VDao) {
		DetailVehicleInfoDTO dto = null;
		Scanner sc = new Scanner(System.in);
		String selRegNum = "";
		int regNum;
		while (true) {
			try {
				System.out.print("등록번호(registration number) 입력 [취소 : -1] : ");
				selRegNum = sc.nextLine();
				if (selRegNum.equals("-1")) {
					System.out.println("등록번호로 차량 정보 보기 종료");
					break;
				}
				regNum = Integer.parseInt(selRegNum);
				dto = VDao.getVehicleInfoByRegNum(regNum);
				if (dto == null)
					System.out.println("해당 매물이 없습니다.");
				else
					return dto;
			} catch (NumberFormatException e) {
				System.err.println("숫자만 입력해주세요.");
			}
		}
		return null;
	}
	private static boolean cancelCondition(HashMap<String, ArrayList<String>> conditions) {
		String sel = "";
		int selIdx;
		ArrayList<String> list = null;
		Scanner sc = new Scanner(System.in);
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> condition = null;
		String removeKey = null;
		int idx = 1;
		
		if(conditions.isEmpty()) {
			System.out.println("삭제할 조건이 없습니다.");
			return false;
		}
		
		while (true) {
			idx = 1;
			System.out.println("0. 선택 조건 초기화");
			for (String key : conditions.keySet()) {
				System.out.println(idx++ + ". " + key + " : " + conditions.get(key).toString());
				keys.add(key);
			}
			System.out.print("삭제할 조건 선택 [취소 : -1] : ");
			sel = sc.nextLine();
			if (sel.equals("-1"))
				return false;
			
			try {
				selIdx = Integer.parseInt(sel);
				if(selIdx < 0 || selIdx >= idx) {
					System.out.println("유효한 범위 값이 아닙니다. 다시 입력 부탁드립니다.");
					continue;
				}
				if(selIdx == 0) {
					for(String key : keys)
						conditions.remove(key);
					System.out.println("모든 조건을 초기화하였습니다.");
					return true;
				}else {
					removeKey = keys.get(selIdx-1);
					condition = conditions.get(removeKey);
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("숫자(인덱스)만 입력 부탁드립니다.");
			}
		}
		
		while(true) {
			for(int i=0; i<condition.size(); i++)
				System.out.println(i+1 + ". " + condition.get(i));
			System.out.print("삭제할 조건 데이터 선택[취소 : -1] : ");
			sel = sc.nextLine();
			if(sel.equals("-1"))
				return false;
			
			selIdx = Integer.parseInt(sel);
			if(selIdx < 1 || selIdx > condition.size()) {
				System.out.println("유효한 범위 값이 아닙니다.");
			}
			
			if(getAnswerYesOrNo("정말로 삭제하시겠습니까?(Y/N)", sc)) {
				condition.remove(selIdx-1);
				if(condition.size() == 0) {
					conditions.remove(removeKey);
					// 제조사를 제거할 경우 밑에 연관된 조건들도 다 없애버림
					// 모델를 조건에서 뺼 경우 해당 모델과 관련된 세부모델도 다 없애버림 
					if(removeKey.equals("model_name") && conditions.containsKey("detailed_model_name")) {
						conditions.remove("detailed_model_name");
					} else if(removeKey.equals("make")) {
						if(conditions.containsKey("model_name"))
							conditions.remove("model_name");
						if(conditions.containsKey("detailed_model_name"))
							conditions.remove("detailed_model_name");
					}
				}
				else
					conditions.replace(removeKey, condition);
				return true;
			} else {
				System.out.println("삭제를 취소합니다.");
				return false;
			}
		}
	}
	private static boolean setConditionsByModelName(HashMap<String, ArrayList<String>> conditions) {
		VehicleDAO dao = new VehicleDAO();
		HashMap<String, String> map = null;
		Scanner sc = new Scanner(System.in);
		ArrayList<String> list = null;
		boolean isUpdate = false;
		System.out.println("<<<모델 이름 검색>>>");
		System.out.println("모델 이름 : ");
		String model_name = sc.nextLine();
		if(model_name.equals(""))
			return false;
		
		map = dao.isExistModelName(model_name);
		
		if(map != null) {
			if(conditions.isEmpty() == false) {
				list = new ArrayList<String>();
				for(String key : conditions.keySet())
					list.add(key);
				for(String key : list) 
					conditions.remove(key);
			}
			
			list = new ArrayList<String>();
			list.add(map.get("make"));
			conditions.put("make", list);
				
			list = new ArrayList<String>();
			list.add(map.get("model_name"));
			conditions.put("model_name",list);
			isUpdate = true;
		}else {
			System.out.println("입력한 "+model_name+"은 존재하지 않습니다.");
		}
		return isUpdate;
	}
	private static boolean setConditionByDetailedModelName(HashMap<String, ArrayList<String>> conditions) {
		VehicleDAO dao = new VehicleDAO();
		HashMap<String, String> map = null;
		Scanner sc = new Scanner(System.in);
		ArrayList<String> list = null;
		boolean isUpdate = false;
		System.out.println("<<세부모델 이름 검색>>>");
		System.out.print("세부모델 이름(첫문자는 대문자) : ");
		String detailed_model_name = sc.nextLine();
		if(detailed_model_name.equals(""))
			return false;
		
		map = dao.isExistDetailedModelName(detailed_model_name);
		
		if(map != null) {
			if(conditions.isEmpty() == false) {
				list = new ArrayList<String>();
				for(String key : conditions.keySet())
					list.add(key);
				for(String key : list) 
					conditions.remove(key);
			}
			
			list = new ArrayList<String>();
			list.add(map.get("make"));
			conditions.put("make", list);
				
			list = new ArrayList<String>();
			list.add(map.get("model_name"));
			conditions.put("model_name",list);
			
			list = new ArrayList<String>();
			list.add(map.get("detailed_model_name"));
			conditions.put("detailed_model_name", list);
			isUpdate = true;
		}else {
			System.out.println("입력한 "+detailed_model_name+"은 존재하지 않습니다.");
		}
		return isUpdate;
	}
	private static boolean setModelYearCondition(HashMap<String, ArrayList<String>> conditions) {
		String model_year = "";
		String modelYearSel = "";
		String year ="", month="";
		ArrayList<String> list = null;
		int startYearIdx = 1980, lastYearIdx = 2019;
		model_year = getSelectCondition("최소 연식 선택", "최대 연식 선택","min_model_year", "max_model_year");
		if(model_year.equals("exit"))
			return false;
		
		if(model_year.equals("min_model_year")) {
			if(conditions.containsKey("max_model_year")) {
				list = conditions.get("max_model_year");
				lastYearIdx = Integer.parseInt(list.get(0).substring(0, 4));
			}
			
			year = String.valueOf(getModelYear(startYearIdx, lastYearIdx));
			if(year.equals("-1"))
				return false;
			
			month = String.valueOf(getModelMonth(true, false));
			if(month.length() == 1) 
				month = "0"+month;
			list = new ArrayList<String>();
			list.add(year+"-"+month);
			conditions.put("min_model_year", list);
		} else if(model_year.equals("max_model_year")) {
			if(conditions.containsKey("min_model_year")) {
				list = conditions.get("min_model_year");
				startYearIdx = Integer.parseInt(list.get(0).substring(0, 4));
			}
			
			year = String.valueOf(getModelYear(startYearIdx, lastYearIdx));
			if(year.equals("-1"))
				return false;
			
			month = String.valueOf(getModelMonth(false, true));
			if(month.length() == 1) month = "0"+month;
			list = new ArrayList<String>();
			list.add(year+"-"+month);
			conditions.put("max_model_year", list);
		}
		// 만약 최소 연식 조건의 년도와 최대 연식 조건의 년도가 같으면서 월은 최대 연식 조건이 작은 경우  SWAP
		if(conditions.containsKey("min_model_year") && conditions.containsKey("max_model_year")) {
			list = conditions.get("min_model_year");
			int min_model_year = Integer.parseInt(list.get(0).substring(0, 4));
			int min_model_month = Integer.parseInt(list.get(0).substring(5));
			
			list = conditions.get("max_model_year");
			int max_model_year = Integer.parseInt(list.get(0).substring(0, 4));
			int max_model_month = Integer.parseInt(list.get(0).substring(5));
			
			if(min_model_year == max_model_year && min_model_month > max_model_month) {
				ArrayList<String> temp = conditions.get("min_model_year");
				conditions.put("min_model_year", conditions.get("max_model_year"));
				conditions.put("max_model_year", temp);
				System.out.println("min_model_month : "+conditions.get("min_model_year").toString());
				System.out.println("max_model_month : "+conditions.get("max_model_year").toString());
			}
		}
		return true;		
	}
	private static boolean setColumnCondition(String column, HashMap<String, ArrayList<String>> conditions) {
		// 현재 설정된 조건 항목에 의해 선택된 항목을 출력하기 위해 where절 갱신
		// 갱신된 where절에 의해 필터링된 category 리스트를 가져옴.
		// 필터링된 category 중 선택할 항목을 선택한 후 conditions를 갱신
		VehicleDAO VDao = new VehicleDAO();
		String whereClusure = getWhereClusureByColumn(conditions, column);
		ArrayList<String> list = VDao.getConditionList(column, whereClusure);
		return setConditionForColumn(list, conditions, column);		
	}
	private static String getSelectCondition(String condition1, String condition2,String min, String max) {
		String sel = "";
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.println("1."+condition1+"\t2."+condition2+"\t3.나가기");
			System.out.print("선택 : ");
			sel = sc.nextLine();
			if(sel.equals("3"))
				return "exit";
			else if(sel.equals("1"))
				return min;
			else if(sel.equals("2"))
				return max;
			else {
				System.out.println("유효하지 않은 입력 값입니다. 다시 입력 부탁드립니다.");
				continue;
			}
		}
	}
	
	private static boolean setMileageCondition(HashMap<String, ArrayList<String>> conditions) {
		String mileage = "";
		int lastIdx = 20;
		int startIdx = 1;
		ArrayList<String> list = null;
		mileage = getSelectCondition("최소 주행거리 선택", "최대 주행거리 선택","min_mileage", "max_mileage");
		if(mileage.equals("exit"))
			return false;
		
		// 1번 선택, 즉 최소 주행거리를 선택한 경우(주행거리는 10000단위로 10000부터 200000까지 설정가능)
		if(mileage.equals("min_mileage")) {
			// 이미 최대 주행거리가 있는경우
			// 최소 주행거리의 범위는 10000 - 최대주행거리 - 10000
			if(conditions.containsKey("max_mileage")) {
				list = conditions.get("max_mileage");
				lastIdx = (Integer.parseInt(list.get(0)) / 10000) - 1;
			}
			
			list = getMileage("min_mileage", startIdx, lastIdx);
			if(list != null)
				conditions.put("min_mileage", list);
		}
		else if(mileage.equals("max_mileage")) {
			if (conditions.containsKey("min_mileage")) {
				list = conditions.get("min_mileage");
				startIdx = (Integer.parseInt(list.get(0)) / 10000);
				if(startIdx == 100) {
					System.out.println("최소 주행거리가 200,000이므로 최대 주행거리를 선택할 수 없습니다.");
					return false;
				}
			}
			list = getMileage("max_mileage", startIdx, lastIdx);
			if(list != null)
				conditions.put("max_mileage", list);
		}
		return true;
	}
	
	private static boolean setPriceCondition(HashMap<String, ArrayList<String>> conditions) {
		String price = "";
		String priceSel = "";
		price = getSelectCondition("최소 가격 선택", "최대 가격 선택","min_price", "max_price");
		ArrayList<String> list = null;

		if(price.equals("exit"))
			return false;
		
		if(price.equals("min_price")) {
			int lastIdx = 100;
			int startIdx = 1;
			if(conditions.containsKey("max_price")) {
				list = conditions.get("max_price");
				lastIdx = Integer.parseInt(list.get(0)) / 1000000;
				lastIdx = lastIdx > 20 ? lastIdx - 10 : lastIdx - 1;
			}
			list = getPrice("min price", startIdx, lastIdx);
			if(list != null)
				conditions.put("min_price", list);
			else
				return false;
		}
		// 사용자 입력을 while(true)로 나가는 입력(-1) 또는 유효 범위의 값(min price ~ 1억원)을
		// 입력할 때까지 사용자 입력을 받으며 잘못된 범위의 값이나 문자 값을 입력했을 때는 오류를 출력하고 다시 입력 받음.
		else if(price.equals("max_price")) {
			int startIdx = 1;
			int lastIdx = 100;
			if (conditions.containsKey("min_price")) {
				list = conditions.get("min_price");
				startIdx = (Integer.parseInt(list.get(0)) / 1000000);
				// 시작 index가 최소 가격을 백만으로 나누었을 때
				// 20 이상이면 +10, 20보다 작으면 +1을 하여 최소 가격보다 100만 또는 1000만원 이상부터 선택할 수 있음. 
				startIdx = startIdx >= 20 ? startIdx + 10 : startIdx + 1;
				if(startIdx > 100) {
					System.out.println("설정된 최소 가격이 1억원이므로 최대 가격을 선택할 수 없습니다.");
					return false;
				}	
			}
			list = getPrice("max price", startIdx, lastIdx);
			if(list != null)
				conditions.put("max_price", list);
			 else
				return false;
		}
		return true;
	}

	private static boolean setConditionForColumn(ArrayList<String> list, HashMap<String, ArrayList<String>> conditions, String column) {
		Scanner sc = new Scanner(System.in);
		String sel = null;
		while (true) {
			for (int i = 0; i < list.size(); i++)
				System.out.println(i + 1 + " " + list.get(i));

			System.out.print(column +" 선택[exit입력 시 뒤로 돌아감] : ");
			sel = sc.nextLine();
			if(sel.equals(""))
				return false;	

			if(sel.equals("exit"))
				return false;	

			try {
				int columnSel = Integer.parseInt(sel);
				if (columnSel <= 0 && columnSel >= list.size() + 1) {
					System.out.println("다시 선택해 주세요.");
				} else {
					ArrayList<String> condition = null;
					
					if (conditions.get(column) == null) {
						condition = new ArrayList<String>();
						conditions.put(column, condition);
					}
					condition = conditions.get(column);

					if (condition.contains(list.get(columnSel - 1))) {
						System.out.println("해당 조건은 이미 선택되었습니다.");
						return false;
					}
					else {
						condition.add(list.get(columnSel - 1));
						conditions.replace(column, condition);
						return true;
					}
				}
			} catch (NumberFormatException e) {
				System.out.println("입력하신 값은 유효한 값이 아닙니다. 다시 입력해 주세요.");
			}
		}
	}

	/*
	 	카테고리 
 	 	 - 모델/제조사(모델을 정하는 순간 카테고리는 정할 수 있는 것이 제한된다.)
 	 	 - 연식, 주행거리, 가격, 지역, 색상, 연료, 변속기
		제조사(검색에서 오로지 한 제조사만 선택 가능) : 차종, 연식, 주행거리, 가격, 지역, 색상, 연료, 변속기
		모델 : 제조사, 연식, 주행거리, 가격, 지역, 색상, 연료, 변속기
		세부모델 : 모델, 연식, 주행거리, 가격, 지역, 색상, 연료, 변속기
		연식, 가격, 주행거리 : 다른 조건들에게 영향을 받지 않음. 하지만 다른 조건들에게 영향을 줌.
		지역 : 카테고리, (제조사, 모델, 세부모델), 연식, 주행거리, 가격, 색상, 연료, 변속기
		색상 : 카테고리, (제조사, 모델, 세부모델), 연식, 주행거리, 가격, 연료, 변속기
		연료 : 카테고리, (제조사, 모델, 세부모델), 연식, 주행거리, 가격, 변속기
		변속기 : 카테고리, (제조사, 모델, 세부모델), 연식, 주행거리, 가격, 연료
	 */
	private static String getWhereClusureByColumn(HashMap<String, ArrayList<String>> conditions, String column) {
		ArrayList<String> list = null;
		ArrayList<String> query = new ArrayList<String>();
		boolean flag = false;

		if (column.equals("category") == false && conditions.containsKey("category"))
			addCategoryCondition(conditions.get("category"), query);

		// 제조사 및 모델 조건 추가
		if (column.equals("detailed_model_name") == false && conditions.containsKey("detailed_model_name"))
			flag = addDetailedModelNameCondition(conditions.get("detailed_model_name"), query);
		else if (column.equals("model_name") == false && conditions.containsKey("model_name"))
			flag = addModelCondition(conditions.get("model_name"), query);
		else if (column.equals("make") == false && conditions.containsKey("make"))
			addMakeCondition(conditions.get("make"), query);

		// 연식 최소 추가
		if (column.equals("min_model_year") == false && conditions.containsKey("min_model_year"))
			addMinModelYearCondition(conditions.get("min_model_year"), query);

		// 연식 최대 추가
		if (column.equals("max_model_year") == false && conditions.containsKey("max_model_year"))
			addMaxModelYearCondition(conditions.get("max_model_year"), query);

		// 최소 주행거리 추가
		if (column.equals("min_mileage") == false && conditions.containsKey("min_mileage"))
			addMinMileageCondition(conditions.get("min_mileage"), query);

		// 최대 주행거리 추가
		if (column.equals("max_mileage") == false && conditions.containsKey("max_mileage"))
			addMaxMileageCondition(conditions.get("max_mileage"), query);

		// 최소 가격 추가
		if (column.equals("min_pirce") == false && conditions.containsKey("min_price"))
			addMinPriceCondition(conditions.get("min_price"), query);
		// 최대 가격 추가
		if (column.equals("max_price") == false && conditions.containsKey("max_price"))
			addMaxPriceCondition(conditions.get("max_price"), query);

		// 색상 추가
		if (column.equals("color") == false && conditions.containsKey("color"))
			addColorCondition(conditions.get("color"), query);

		// 지역 추가
		if (column.equals("location") == false && conditions.containsKey("location"))
			addLocationCondition(conditions.get("location"), query);

		// 변속기 옵션 추가
		if (column.equals("transmission") == false && conditions.containsKey("transmission"))
			addTransmissionCondition(conditions.get("transmission"), query);

		// 연료 조건 추가
		if (column.equals("fuel") == false && conditions.containsKey("fuel"))
			addFuelCondition(conditions.get("fuel"), query);

		return combinationConditions(query);
	}

	private static void addCategoryCondition(ArrayList<String> list, ArrayList<String> query) {
		String category = "";
		for (int i = 0; i < list.size(); i++)
			category += "category='" + list.get(i) + "' " + (i == list.size() - 1 ? "" : " OR ");
		if(list.size() > 1)
			category = "("+category+")";
		if (category.equals("") == false)
			query.add(category);
	}

	private static boolean addDetailedModelNameCondition(ArrayList<String> list, ArrayList<String> query) {
		String detailed_model_name = "";
		for (int i = 0; i < list.size(); i++)
			detailed_model_name += "detailed_model_name='" + list.get(i) + "' " + (i == list.size() - 1 ? "" : " OR ");
		if(list.size() > 1)
			detailed_model_name = "("+detailed_model_name+")";
		if (detailed_model_name.equals("") == false) {
			query.add(detailed_model_name);
			return true;
		}
		return false;
	}

	private static boolean addModelCondition(ArrayList<String> list, ArrayList<String> query) {
		String model = "";
		for (int i = 0; i < list.size(); i++)
			model += "model_name='" + list.get(i) + "' " + (i == list.size() - 1 ? "" : " OR ");
		if(list.size()>1)
			model = "("+model+")";
		if (model.equals("") == false) {
			query.add(model);
			return true;
		}
		return false;
	}

	private static boolean addMakeCondition(ArrayList<String> list, ArrayList<String> query) {
		String make = "";
		for (int i = 0; i < list.size(); i++)
			make += "make='" + list.get(i) + "' " + (i == list.size() - 1 ? "" : " OR ");

		if (make.equals("") == false) {
			query.add(make);
			return true;
		}
		return false;
	}

	private static void addMinModelYearCondition(ArrayList<String> list, ArrayList<String> query) {
		query.add("model_year >= TO_DATE('" + list.get(0) + "', ('YYYY-MM'))");
	}

	private static void addMaxModelYearCondition(ArrayList<String> list, ArrayList<String> query) {
		query.add("model_year <= TO_DATE('" + list.get(0) + "', ('YYYY-MM'))");
	}

	private static void addMinMileageCondition(ArrayList<String> list, ArrayList<String> query) {
		query.add("mileage >=" + list.get(0));
	}

	private static void addMaxMileageCondition(ArrayList<String> list, ArrayList<String> query) {
		query.add("mileage <=" + list.get(0));
	}

	private static void addMinPriceCondition(ArrayList<String> list, ArrayList<String> query) {
		query.add("price >=" + list.get(0));
	}

	private static void addMaxPriceCondition(ArrayList<String> list, ArrayList<String> query) {
		query.add("price <=" + list.get(0));
	}

	private static void addColorCondition(ArrayList<String> list, ArrayList<String> query) {
		String color = "";
		for (int i = 0; i < list.size(); i++)
			color += "color='" + list.get(i) + "' " + (i == list.size() - 1 ? "" : " OR ");
		if(list.size() > 1)
			color = "(" + color + ")";
		if (color.equals("") == false)
			query.add(color);
	}

	private static void addLocationCondition(ArrayList<String> list, ArrayList<String> query) {
		String location = "";
		for (int i = 0; i < list.size(); i++)
			location += "location='" + list.get(i) + "' " + (i == list.size() - 1 ? "" : " OR ");
		if(list.size() > 1)
			location = "(" + location + ")";
		if (location.equals("") == false)
			query.add(location);
	}

	private static void addTransmissionCondition(ArrayList<String> list, ArrayList<String> query) {
		String transmission = "";
		for (int i = 0; i < list.size(); i++)
			transmission += "transmission='" + list.get(i) + "' " + (i == list.size() - 1 ? "" : " OR ");
		if(list.size() > 1)
			transmission = "(" + transmission + ")";
		if (transmission.equals("") == false)
			query.add(transmission);
	}

	private static void addFuelCondition(ArrayList<String> list, ArrayList<String> query) {
		String fuel = "";
		for (int i = 0; i < list.size(); i++)
			fuel += "fuel='" + list.get(i) + "' " + (i == list.size() - 1 ? "" : "OR ");
		if(list.size() > 1)
			fuel = "(" + fuel + ")";
		if (fuel.equals("") == false)
			query.add(fuel);
	}

	private static String combinationConditions(ArrayList<String> query) {
		if(query.isEmpty())
			return null;
		String ret = "";
		for (int i = 0; i < query.size(); i++)
			ret += query.get(i) + (i == query.size() - 1 ? " " : " AND ");

		return ret;
	}
	
	private static int getModelYear(int startYearIdx, int lastYearIdx) {
		Scanner sc = new Scanner(System.in);
		int model_year = -1;
		while (true) {
			System.out.printf("%-5s : 년도\n", "index");
			for (int year = startYearIdx, i = 0; year <= lastYearIdx; year++, i++)
				System.out.printf("%-5d : %d년\n", i + 1, year);
			System.out.println("index 선택[선택 취소 : -1] : ");
			String selYear = sc.nextLine();
			if(selYear.equals(""))
				continue;
			
			try {
				if (selYear.equals("-1"))
					return -1;

				int yearIdx = Integer.parseInt(selYear);
				if (yearIdx < 1 || yearIdx > (lastYearIdx-startYearIdx+1)) {
					System.out.println("범위를 벗어나는 값입니다. 다시 입력 부탁드립니다.");
					continue;
				}
				model_year = yearIdx + startYearIdx-1;
			} catch (NumberFormatException e) {
				System.err.println("숫자(인덱스)만 입력해주세요.");
			}
			break;
		}
		return model_year;
	}
	
	private static int getModelMonth(boolean isMinMonth, boolean isMaxMonth) {
		Scanner sc = new Scanner(System.in);
		int model_month = -1;
		String monthSel = null;
		while(true) {
			System.out.printf("%5s : %5s\n", "index", "month");
			for(int monthIdx = 1; monthIdx <= 12; monthIdx++) 
				System.out.printf("%-5d : %-5d월\n", monthIdx, monthIdx);
			System.out.print("index 선택[선택취소 : -1] : ");
			monthSel = sc.nextLine();
			
			if(monthSel.equals(""))
				continue;
			// 선택 취소 시 default 값은 isMinMonth == true이면 month = 1
			// 					  isMaxMonth == true이면 month = 12
			if(monthSel.equals("-1") && isMinMonth)
				return 1;
			else if(monthSel.equals("-1") && isMaxMonth)
				return 12;
			try {
			model_month = Integer.parseInt(monthSel);
			
			if(model_month < 1 || model_month > 12) {
				System.out.println("범위를 벗어나는 값입니다. 다시 입력 부탁드립니다.");
				continue;
			}
			
			}catch(NumberFormatException e) {
				System.err.println("숫자(인덱스)만 입력해주세요.");
			} 
			break;
		}
		return model_month;
	}
	
	private static ArrayList<String> getMileage(String condition, int startIdx, int lastIdx){
		String mileageSel ="";
		Scanner sc = new Scanner(System.in);
		ArrayList<String> ret = null;
		
		// 사용자 입력을 while(true)로 나가는 입력(-1) 또는 제대로 된 입력(10000~max mileage-10000)을
		// 입력할 떄까지는 입력을 받으며 잘못된 범위의 값이나 문자 값을 입력했을 때는 오류를 출력하고 다시 입력을 받음.
		while (true) {
			System.out.println(condition+" 선택");
			for (int i = startIdx; i <= lastIdx; i++)
				System.out.println(i + ". " + i * 10000);
			System.out.print("선택 [-1 : 선택취소]: ");
			mileageSel = sc.nextLine();
			if(mileageSel.equals(""))
				continue;
			if(mileageSel.equals("-1"))
				break;

			try {
				int mileage = Integer.parseInt(mileageSel);
				if (mileage < startIdx || mileage > lastIdx) {
					System.out.println("범위를 벗어나는 값입니다. 다시 선택 부탁드립니다.");
					continue;
				}
				ret = new ArrayList<String>();
				ret.add(String.valueOf(mileage * 10000));
				break;
			} catch (NumberFormatException e) {
				System.out.println("다시 선택해주세요.");
			}
		}
		return ret;
	}
	
	private static ArrayList<String> getPrice(String condition, int startIdx, int lastIdx) {
		String priceSel = "";
		Scanner sc = new Scanner(System.in);
		ArrayList<String> ret = null;
		while (true) {
			System.out.println(condition + " 선택");
			for (int i = startIdx; i <= lastIdx;) {
				System.out.println(i + ". " + i * 100);
				if(i < 20) i++;
				else if(i >= 20 && i <=100) i += 10;
			}
			System.out.print("선택 [-1 : 선택취소]: ");
			priceSel = sc.nextLine();
			if(priceSel.equals(""))
				continue;		
			if(priceSel.equals("-1"))
				break;

			try {
				int price = Integer.parseInt(priceSel);
				if (price < startIdx || price > 100) {
					System.out.println("범위를 벗어나는 값입니다. 다시 선택 부탁드립니다.");
					continue;
				} else if(price > 20 && price%10 != 0) {
					System.out.println("2000만원 이상 선택 시 10단위로 입력 부탁드립니다.");
					continue;
				}
				ret = new ArrayList<String>();
				ret.add(String.valueOf(price * 1000000));
				break;
			} catch (NumberFormatException e) {
				System.out.println("다시 선택해주세요.");
			}
		}
		return ret;
	}

	private static boolean showDetailVehicleInfo(DetailVehicleInfoDTO info) {
		boolean isBuy = false;
		Scanner sc = new Scanner(System.in);
		System.out.println("선택하신 차량의 세부 정보입니다.");
		info.printInfo();
		while (true) {
			System.out.println("1. 구매하기\t2.뒤로가기");
			String sel = sc.nextLine();
			if (sel.equals("1")) {
				while (true) {
					System.out.print("정말로 구매하시겠습니까?(Y/N) : ");
					sel = sc.nextLine().toUpperCase();
					if (sel.equals("Y")) {
						isBuy = true;
						break;
					} else if (sel.equals("N")) {
						System.out.println("구매를 취소합니다.");
						break;
					} else {
						System.out.println("Y(y) 또는 N(n) 값 중 하나만 입력해주세요.");
					}
				}
			} else if (sel.equals("2")) {
				System.out.println("뒤로 돌아갑니다.");
				break;
			} else {
				System.out.println("1 또는 2 둘 중 하나의 값만 입력해주세요.");
			}
		}
		return isBuy;
	}

	public static void clearScreen() {
		for (int i = 0; i < 80; i++)
			System.out.println();
	}
}
