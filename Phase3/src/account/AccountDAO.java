package account;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Scanner;

public class AccountDAO {
	private static final String insertAccountQuery = "insert into account values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String isExistIdQuery = "select count(id) from account where id = ?";
	private static final String isExistAdminQeury = "select account_type from account where id = ? AND password = ?";
	private static final String getAccountInfoQuery = "select * from account where id = ? AND password = ?";
	private static final String isExistAccountQuery = "select name from account where id = ? AND password = ?";
	private static final String getNumberOfAdmin = "select count(id) from account where account_type = 'A'";
	private static final String modifyAccountInfoQuery = "update account set password=?, name=?, phone_number=?, address=?, bDate=?, sex=?, job=? where id=?";
	private static final String modifyPasswordQuery = "update account set password=? where id=?";
	private static final String url = "jdbc:oracle:thin:@localhost:1600:xe";
	private static final String user = "knu";
	private static final String pw = "comp322";
	private static final String regExpId = "^[a-zA-Z]{1}[a-zA-Z0-9]{4,14}$";
	private static final String regExpPw = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,15}$";
	private static final String regExpName = "^[a-zA-Z]{2,10}\\s[a-zA-Z]{2,10}$";
	private static final String regExpPhoneNum = "^01(?:0|1[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";
	private static final String regExpDate = "^(19[0-9][0-9]|20\\d{2})-(0[0-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$";
	private Connection con;
	private PreparedStatement pstmt;
	private Statement stmt;

/*	//Test Ok
	public static void main(String[] args) {

		String id = null, pw = null;
		AccountDAO dao = new AccountDAO();
		dao.joinAccount();									// 회원가입
		dao.login(id, pw);									// 로그인
		dao.modifyAccountInfo("nsun9505");					// 회원 정보 수정 + 비밀번호 변경
		dao.withdrawalAccount("nsun9505", "남상윤", "A");		// 회원 탈퇴 
	}
*/
	public AccountDAO() {
		connDB();
	}

	// 일반 고객 회원 가입 & 관리자 인증 후 관리자 가입 가능
	public void joinAccount() {
		Scanner sc = new Scanner(System.in);
		String account_type = "C";
		String sel;

		System.out.println("<<<회원가입>>>");
		while (true) {
			System.out.println("1. 고객 가입 \t2. 관리자 가입");
			System.out.print("선택 : ");
			sel = sc.nextLine();
			if (sel.equals("1") || sel.equals("2"))
				break;
			else
				System.out.println("1 또는 2를 입력하십시오.");
		}

		if (sel.equals("2")) {
			while (true) {
				System.out.println("<<<관리자 가입을 위한 관리자 인증>>>");
				System.out.print("관리자 ID : ");
				String adminId = sc.nextLine();
				System.out.print("관리자 PW : ");
				String adminPw = sc.nextLine();

				try {
					pstmt = con.prepareStatement(isExistAdminQeury);
					pstmt.setString(1, adminId);
					pstmt.setString(2, adminPw);
					ResultSet rs = pstmt.executeQuery();
					if (rs.next()) {
						String type = rs.getString(1);
						if (type.equals("A") == false) {
							System.out.print("관리자 계정 인증 실패. 다시 인증하시겠습니까?");
							if (sc.nextLine().toUpperCase().equals("N"))
								return;
						}
						System.out.println("관리자 인증 성공!");
						account_type = "A";
						break;
					} else {
						System.out.print("관리자 계정 인증 실패. 다시 인증하시겠습니까?");
						if (sc.nextLine().toUpperCase().equals("N"))
							return;
					}
				} catch (SQLException e) {
					System.err.println("sql error : " + e.getMessage());
					System.exit(1);
				}
			}
		}

		String id = getInputId(sc);
		String pw, name, phone_num;
		while(true) {
			pw = getInputPasswd(sc, "비밀번호 입력  : ");
			if(pw != null)
				break;
		}
		while(true) {
			name = getInputName(sc, "이름 입력[필수정보] : ");
			if(name != null)
				break;
		}
		while(true) {
			phone_num = getInputPhoneNumber(sc, "핸드폰 번호 입력[필수정보] : ");
			if(phone_num != null)
				break;
		}
		System.out.println("아래 정보들은 필수 정보가 아닙니다. EnterKey를 눌러 스킵할 수 있습니다.");
 		String address = getInputAddress(sc, "주소 입려[필수아님] : ");
		String birth_date = getInputBirthDate(sc, "생년월일 입려[필수아님] : ");
		String sex = getInputSex(sc, "성별 입려[필수아님] : ");
		String job = getInputJob(sc, "직업 입려[필수아님] : ");

		try {
			pstmt = con.prepareStatement(insertAccountQuery);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.setString(3, name);
			pstmt.setString(4, phone_num);

			if (address == null)
				pstmt.setNull(5, Types.CHAR);
			else
				pstmt.setString(5, address);

			if (birth_date == null)
				pstmt.setNull(6, Types.DATE);
			else
				pstmt.setDate(6, Date.valueOf(birth_date));

			if (sex == null)
				pstmt.setNull(7, Types.CHAR);
			else
				pstmt.setString(7, sex);

			if (job == null)
				pstmt.setNull(8, Types.CHAR);
			else
				pstmt.setString(8, job);

			pstmt.setString(9, account_type);

			int res = pstmt.executeUpdate();
			if (res == 1)
				System.out.println("회원 가입 성공!");
			con.commit();
		} catch (SQLException e) {
			System.err.println("sql error : " + e.getMessage());
		}
	}

	// 회원 정보 수정
	public void modifyAccountInfo(String id) {
		Scanner sc = new Scanner(System.in);
		HashMap<String, Boolean> flags = new HashMap<String, Boolean>();
		boolean flag = false;
		int ret;
		System.out.println("<<<회원 정보 수정>>>");
		AccountDTO dto = getAccountInfoById(id);
		if (dto == null) {
			System.out.println("비밀 번호가 틀립니다. 회원 정보 수정을 종료합니다.");
			return;	
		}
		
		flags.put("password", false);
		flags.put("name", false);
		flags.put("phone_number", false);
		flags.put("address", false);
		flags.put("birth_date", false);
		flags.put("sex", false);
		flags.put("job", false);
		
		while (true) {
			System.out.println("<<회원 정보 수정 항목 선택>>");
			System.out.println("1. 아이디[수정불가] : " + dto.getId());
			System.out.println("2. 비밀번호 : " + dto.getPw());
			System.out.println("3. 이름 : " + dto.getName());
			System.out.println("4. 핸드폰 번호 : " + dto.getPhone_num());
			System.out.println("5. 주소 : " + (dto.getAddress() == null ? "[입력하지 않음]" : dto.getAddress()));
			System.out.println("6. 생년월일 : " + (dto.getBirth_date() == null ? "[입력하지 않음]" : dto.getBirth_date().toString()));
			System.out.println("7. 성별 : " + (dto.getSex() == null ? "[입력하지 않음]" : dto.getSex()));
			System.out.println("8. 직업 : " + (dto.getJob() == null ? "[입력하지 않음]" : dto.getJob()));
			System.out.println("9. 수정  내용 저장 후 종료");
			System.out.println("0. 수정 내용 저장하지 않고 종료");
			System.out.print("수정 항목 선택 : ");

			switch (sc.nextLine()) {
			case "1":
				System.out.println("아이디는 수정할 수 없습니다.");
				break;
			case "2":
				String pw = getInputPasswd(sc, "변경할 비밀번호 입력  : ");
				if (pw != null) {
					flags.replace("password", true);
					dto.setPw(pw);
				}
				break;
			case "3":
				String name = getInputName(sc, "변경할 이름 입력 : ");
				if (name != null) {
					flags.replace("name", true);
					dto.setName(name);
				}
				break;
			case "4":
				String phone_num = getInputPhoneNumber(sc, "변경할 핸드폰 번호 입력 : ");
				if (phone_num != null) {
					flags.replace("phone_number", true);
					dto.setPhone_num(phone_num);
				}
				break;
			case "5":
				String address = getInputAddress(sc, "변경할 주소 입력 : ");
				if (address != null) {
					flags.replace("address", true);
					dto.setAddress(address);
				} else {
					flag = decisionNullInput("address", dto, sc);
					if(flag && flag != flags.get("address"))
						flags.replace("address", flag);
				}
				break;
			case "6":
				String bDate = getInputBirthDate(sc, "변경할 생년월일 입력 : ");
				if (bDate != null) {
					flags.put("birth_date", true);
					dto.setBirth_date(Date.valueOf(bDate));
				} else {
					flag = decisionNullInput("birth_date", dto, sc);
					if(flag && flag != flags.get("birth_date"))
						flags.replace("birth_date", flag);
				}
				break;
			case "7":
				String sex = getInputSex(sc, "변경할 성볍 입력 : ");
				if (sex != null) {
					flags.put("sex", true);
					dto.setSex(sex);
				} else {
					flag = decisionNullInput("sex", dto, sc);
					if(flag && flag != flags.get("sex"))
						flags.replace("sex", flag);
				}
				break;
			case "8":
				String job = getInputJob(sc, "변경할 직업 입력 : ");
				if (job != null) {
					flags.put("job", true);
					dto.setJob(job);
				} else {
					flag = decisionNullInput("job", dto, sc);
					if(flag && flag != flags.get("job"))
						flags.replace("job", flag);

				}
				break;
			case "9":
				if (flag == false) {
					System.out.println("수정한 내용이 없습니다. 회원 정보 수정을 종료합니다.");
				} else {
					try {
						pstmt = con.prepareStatement(modifyAccountInfoQuery);
						pstmt.setString(1, dto.getPw());
						pstmt.setString(2, dto.getName());
						pstmt.setString(3, dto.getPhone_num());
						if (dto.getAddress() == null)
							pstmt.setNull(4, Types.VARCHAR);
						else
							pstmt.setString(4, dto.getAddress());
						if (dto.getBirth_date() == null)
							pstmt.setNull(5, Types.DATE);
						else
							pstmt.setDate(5, dto.getBirth_date());
						if (dto.getSex() == null)
							pstmt.setNull(6, Types.VARCHAR);
						else
							pstmt.setString(6, dto.getSex());
						if (dto.getJob() == null)
							pstmt.setNull(7, Types.VARCHAR);
						else
							pstmt.setString(7, dto.getJob());
						pstmt.setString(8, id);
						ret = pstmt.executeUpdate();
						if (ret == 1)
							System.out.println("수정을 완료했습니다. 회원 정보 수정을 종료합니다.");
						con.commit();
					} catch (SQLException e) {
						System.err.println("[modifyAccount method] sql error : " + e.getMessage());
						return;
					}
				}
				return;
			case "0":
				System.out.println("회원 정보를 수정하지 않고 종료합니다.");
				break;
			default:
				System.out.println("현재 입력은 유효하지 않은 입력입니다.");
				break;
			}
		}
	}
	
	// 로그인 기능 구현
	public void login(String id, String pw) {
		Scanner sc = new Scanner(System.in);
		try {
			System.out.println("<<<로그인>>>");
			System.out.print("아이디 : ");
			id = sc.nextLine();
			System.out.print("비밀번호 : ");
			pw = sc.nextLine();
			if(validCheck(id, regExpId) == false || validCheck(pw, regExpPw) == false) {
				System.out.println("[로그인 실패] 가입하지 않은 아이디이거나, 잘못된 비밀번호입니다.");
				return;
			}
			
			pstmt = con.prepareStatement(isExistAccountQuery);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				String name = rs.getString(1);
				System.out.println("[로그인 성공] "+ name + "님 환영합니다!");
			}
			else {
				System.out.println("[로그인 실패] 가입하지 않은 아이디이거나, 잘못된 비밀번호입니다.");
			}
		}catch(SQLException e) {
			System.err.println("sql error : " + e.getMessage());
		}
	}
	
	// 회원탈퇴 기능 구현 완료
	public void withdrawalAccount(String id, String name, String account_type) {
		Scanner sc = new Scanner(System.in);
		try {
			if(account_type.equals("A")) {
				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("select count(*) from account where account_type='A'");
				rs.next();
				int numOfAdmin = rs.getInt(1);
				
				rs.close();
				stmt.close();
				if(numOfAdmin <= 1) {
					System.out.println("관리자 계정은 최소 1개 이상 있어야 하므로  해당 관리자 계정("+id+")은 탈퇴할 수 없습니다.");
					return;
				}
			}
			
			System.out.println("<<<회원 탈퇴>>>");
			System.out.println("회원 탈퇴를 위해 비밀번호를 입력해주세요.");
			System.out.print("비밀번호 입력 : ");
			String pw = sc.nextLine();
			
			if(validCheck(pw, regExpPw) == false) {
				System.out.println("비밃번호가 틀렸습니다. 이전 화면으로 돌아갑니다.");
				return;
			}
			
			pstmt = con.prepareStatement(isExistAccountQuery);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				System.out.println(rs.getString(1)+"님 정말로 회원탈퇴를 하시겠습니까?(Y/N)");
				if(sc.nextLine().toUpperCase().equals("Y")) {
					pstmt = con.prepareStatement("delete from account where id = ?");
					pstmt.setString(1, id);
					int ret = pstmt.executeUpdate();
					if(ret == 1)
						System.out.println("회원탈퇴 완료!\n거래내역 정보는  3년간 유지가 된 후에 삭제가 되는 점 유의바랍니다.");
					con.commit();
				}
				else {
					System.out.println("회원탈퇴 취소");
				}
			}else {
				System.out.println("비밀번호가 틀렸습니다. 이전 화면으로 돌아갑니다.");
				return;
			}
			
		}catch(SQLException e) {
			System.err.println("sql error : " + e.getMessage());
			System.exit(1);
		}
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

	private String getInputId(Scanner sc) {
		String id = null;
		// 아이디
		System.out.print("아이디(5~15자리, 특수문자 X, 영어 대소문자와 숫자 결합)");
		while (true) {
			System.out.print("아이디 입력 : ");
			id = sc.nextLine();

			if (validCheck(id, regExpId) == false) {
				System.out.println("아이디는 5자리 이상 15자리 이하의 영어, 숫자 조합입니다. 특수문자를 사용할 수 없으며, 시작은 영문으로만 가능합니다.");
				continue;
			}
			try {
				pstmt = con.prepareStatement(isExistIdQuery);
				pstmt.setString(1, id);
				ResultSet rs = pstmt.executeQuery();
				rs.next();

				if (rs.getInt(1) == 1) {
					System.out.println("아이디가 이미 존재합니다. 다른 아이디를 사용하십시오.");
					continue;
				} else {
					System.out.println("아이디 중복체크 완료");
					break;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return id;
	}

	private boolean validCheck(String src, String regExp) {
		return src.matches(regExp);
	}

	private String getInputPasswd(Scanner sc, String message) {
		String pw = null;

		System.out.println("비밀번호는 영어, 숫자, 특수문자[$@!%*#] 1개 이상을 조합한 8~15자리 입니다.");
		// 비밀번호 화인
		System.out.print(message);
		pw = sc.nextLine();

		// 비밀번호 검사 : 영어, 숫자, 특수문자를 포함해야 함.
		if (validCheck(pw, regExpPw) == false) {
			System.out.println("비밀번호는 영어, 숫자, 특수문자[$@!%*#] 1개 이상을 포함해야 합니다.");
			return null;
		}

		System.out.print("비밀번호 확인 : ");
		String pwConfirm = sc.nextLine();

		// 입력한 비밀번호가 맞는지 확인
		if (pw.equals(pwConfirm) == false) {
			System.out.println("비밀번호, 비밀번호 확인이 다릅니다. 다시 입력하세요.");
			return null;
		}
		return pw;
	}

	private String getInputName(Scanner sc, String message) {
		String name = null;
		// 이름
		System.out.println("이름 제약사항 : (영어 FirstName(2~10자리), LastName(2~10자리)");
		System.out.print(message);
		name = sc.nextLine();
		if (validCheck(name, regExpName) == false) {
			System.out.println("이름 제약사항을 지켜주세요.");
			return null;
		}
		return name;
	}

	private String getInputPhoneNumber(Scanner sc, String meesage) {
		String phone_num = null;
		// 핸드폰 번호
		System.out.println("핸드폰 번호 제약사항 : (예시 010-1234-1234 or 010-123-1234)");
		System.out.print(meesage);
		phone_num = sc.nextLine();

		if (validCheck(phone_num, regExpPhoneNum) == false) {
			System.out.println("유효하지 않은 형식 입니다. : 010-123-1234 or 010-1234-1234");
			return null;
		}

		return phone_num;
	}

	private String getInputAddress(Scanner sc, String message) {
		System.out.print(message);
		String address = sc.nextLine();
		if (address == null || address.length() == 0)
			return null;
		return address;
	}

	private String getInputSex(Scanner sc, String message) {
		String sex = null;
		System.out.println("성별 (F:여 / M : 남)");
		while (true) {
			System.out.print(message);
			sex = sc.nextLine().toUpperCase();
			if (sex == null || sex.length() == 0)
				return null;
			if (sex.equals("F") || sex.equals("M"))
				break;
			else {
				System.out.println("성별을 입력하실 경우 F/f(여자), M/m(남자)으로 입력 부탁드립니다.");
				System.out.print("다시 입력하시겠습니까?(Y/N) : ");
				if(sc.nextLine().toUpperCase().equals("Y"))
					continue;
				else
					return null;
			}
		}
		return sex;
	}

	private String getInputJob(Scanner sc, String message) {
		System.out.print(message);
		String job = sc.nextLine();
		if (job == null || job.length() == 0)
			return null;
		return job;
	}

	private String getInputBirthDate(Scanner sc, String message) {
		String bDate = null;

		System.out.println("생일 제약사항[필수 아님] : YYYY-MM-DD");
		while (true) {
			System.out.print(message);
			bDate = sc.nextLine();

			if (bDate == null || bDate.length() == 0)
				return null;

			if (validCheck(bDate, regExpDate) == false) {
				System.out.println("년월일(YYYY-MM-DD) 형식을 지켜주십시오.");
				System.out.print("다시 입력하시겠습니까?(Y/N) : ");
				if(sc.nextLine().toUpperCase().equals("Y"))
					continue;
				else
					return null;
			} else
				break;
		}
		return bDate;
	}

	private AccountDTO getAccountInfoById(String id) {
		Scanner sc = new Scanner(System.in);
		AccountDTO dto = null;
		try {
			System.out.println("회원 정보 수정을 위하여 비밀번호를 입력해주세요.");
			while (true) {
				System.out.print("비밀번호 입력 : ");
				String pw = sc.nextLine();
				pstmt = con.prepareStatement(getAccountInfoQuery);
				pstmt.setString(1, id);
				pstmt.setString(2, pw);
				ResultSet rs = pstmt.executeQuery();

				if (rs.next()) {
					dto = new AccountDTO(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
							rs.getString(5), rs.getDate(6), rs.getString(7), rs.getString(8), rs.getString(9));
					rs.close();
					break;
				} else {
					System.out.print("비밀번호가 틀립니다. 다시 입력하시겠습니까?(Y/N)");
					if (sc.nextLine().toUpperCase().equals("Y"))
						continue;
					else
						break;
				}
			}

		} catch (SQLException e) {
			System.err.println("sql error : " + e.getMessage());
			System.exit(1);
		}
		return dto;
	}
	
	private boolean decisionNullInput(String param, AccountDTO dto, Scanner sc) {
		boolean ret = false;
		while (true) {
			System.out.println("입력 값이 없습니다. 이대로 적용하시겠습니까?(Y/N) : ");
			String input = sc.nextLine().toUpperCase();
			if (input.equals("Y")) {
				if(param.equals("address")) dto.setAddress(null); 
				else if(param.equals("birth_date")) dto.setBirth_date(null);
				else if(param.equals("job")) dto.setJob(null);
				else if(param.equals("sex")) dto.setSex(null);
				ret = true;
				break;
			} else if(input.equals("N")) {
				break;
			} else {
				System.out.println("N 또는 Y를 입력하세요.");
			}
		}
		return ret;
	}
}
