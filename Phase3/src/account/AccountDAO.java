package account;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.Scanner;

public class AccountDAO {
	private String insertAccountQuery = "insert into account values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private String isExistIdQuery = "select count(id) from account where id = ?";
	private String url = "jdbc:oracle:thin:@localhost:1600:xe";
	private String user = "knu";
	private String pw = "comp322";
	private Connection con;
	private PreparedStatement pstmt;
	
	public static void main(String[] args) {
		AccountDAO dao = new AccountDAO();
		dao.joinAccount();
	}

	public AccountDAO() {
		connDB();
	}

	// 일반 고객 회원 가입
	public void joinAccount() {
		Scanner sc = new Scanner(System.in);
		System.out.println("<<<회원가입>>>");

		String id = getInputId(sc);
		String pw = getInputPasswd(sc);
		String name = getInputName(sc);
		String phone_num = getInputPhoneNumber(sc);
		String address = getInputAddress(sc);
		String birth_date = getInputBirthDate(sc);
		String sex = getInputSex(sc);
		String job = getInputJob(sc);
		
		try {
			pstmt = con.prepareStatement(insertAccountQuery);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.setString(3, name);
			pstmt.setString(4, phone_num);
			
			if(address == null || address.length() == 0)
				pstmt.setNull(5, Types.CHAR);
			else 
				pstmt.setString(5, address);
			
			if(birth_date == null || birth_date.length() == 0)
				pstmt.setNull(6, Types.DATE);
			else 
				pstmt.setDate(6, Date.valueOf(birth_date));
			
			if(sex == null || sex.length() == 0)
				pstmt.setNull(7, Types.CHAR);
			else
				pstmt.setString(7, sex);
			
			if(job == null || job.length() == 0)
				pstmt.setNull(8, Types.CHAR);
			else
				pstmt.setString(8, job);
			
			pstmt.setString(9, "C");
			
			int res = pstmt.executeUpdate();
			if(res == 1)
				System.out.println("회원 가입 성공!");
			con.commit();
		}catch(SQLException e) {
			System.err.println("sql error : "+e.getMessage());
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
		String regExp = "^[a-zA-Z]{1}[a-zA-Z0-9]{4,14}$";
		String id = null;
		// 아이디
		System.out.print("아이디(5~15자리, 특수문자 X, 영어 대소문자와 숫자 결합)");
		while (true) {
			System.out.print("아이디 입력 : ");
			id = sc.nextLine();

			if (id.matches(regExp) == false) {
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
	
	private String getInputPasswd(Scanner sc) {
		String pw = null;
		String regExp ="^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,15}$";
		
		System.out.println("비밀번호는 영어, 숫자, 특수문자[$@!%*#] 1개 이상을 조합한 8~15자리 입니다.");
		// 비밀번호 화인
		while (true) {
			System.out.print("비밀번호 입력 : ");
			pw = sc.nextLine();
			
			// 비밀번호 검사 : 영어, 숫자, 특수문자를 포함해야 함.
			if(pw.matches(regExp) == false) {
				System.out.println("비밀번호는 영어, 숫자, 특수문자[$@!%*#] 1개 이상을 포함해야 합니다.");
				continue;
			}
			
			System.out.print("비밀번호 확인 : ");
			String pwConfirm = sc.nextLine();

			// 입력한 비밀번호가 맞는지 확인
			if (pw.equals(pwConfirm) == false) {
				System.out.println("비밀번호, 비밀번호 확인이 다릅니다. 다시 입력하세요.");
				continue;
			} else 
				break;
		}
		
		return pw;
	}

	private String getInputName(Scanner sc) {
		String regExp = "^[가-힣]{2,4}|[a-zA-Z]{2,10}\\s[a-zA-Z]{2,10}$";
		String name = null;
		// 이름
		while (true) {
			System.out.println("이름 제약사항 : (영어 FirstName(2~10자리), LastName(2~10자리)");
			System.out.print("이름 입력 : ");
			name = sc.nextLine();
			if (name.matches(regExp) == false) {
				System.out.println("이름은 필수 항목이며 제약사항을 지켜주세요.");
				continue;
			}
			else
				break;
		}
		return name;
	}

	private String getInputPhoneNumber(Scanner sc) {
		String phone_num = null;
		String regExp = "^01(?:0|1|[6-9])[-]?(\\d{3}|\\d{4})[-]?(\\d{4})$";
		// 핸드폰 번호
		System.out.println("핸드폰 번호 제약사항 : (예시 01012341234 or 010-1234-1234)");
		while (true) {
			System.out.print("핸드폰 입력 : ");
			phone_num = sc.nextLine();
			
			if (phone_num.matches(regExp) == false) {
				System.out.println("핸드폰 번호는 필수 정보입니다. 입력 부탁드립니다. 형식 : 01012341234 or 010-1234-1234");
				continue;
			}
			break;
		}
		return phone_num;
	}

	private String getInputAddress(Scanner sc) {
		System.out.print("주소 입력[필수 아님]: ");
		String address = sc.nextLine();
		if(address == null || address.length() == 0)
			return null;
		return address;
	}
	
	private String getInputSex(Scanner sc) {
		String sex = null;
		System.out.println("성별 (F:여 / M : 남)");
		while(true) {
			System.out.print("성별 입력 : ");
			sex = sc.nextLine().toUpperCase();
			if(sex == null || sex.length() == 0)
				return null;
			if(sex.equals("F") || sex.equals("M"))
				break;
			else
				System.out.println("성별을 입력하실 경우 F/f(여자), M/m(남자)으로 입력 부탁드립니다.");
		}
		return sex;
	}

	private String getInputJob(Scanner sc) {
		System.out.print("직업 입력[필수 아님] : ");
		String job = sc.nextLine();
		if(job == null || job.length() == 0)
			return null;
		return job;
	}

	private String getInputBirthDate(Scanner sc){
		String regExp = "^[0-9][0-9][0-9][0-9]\\-[0-9][0-9]\\-[0-9][0-9]$";
		String bDate = null;
		
		System.out.println("생일 제약사항[필수 아님] : YYYY-MM-DD");
		while (true) {
			System.out.print("생일 입력 : ");
			bDate = sc.nextLine();
			
			if(bDate == null || bDate.length() == 0)
				return null;
			
			if (bDate.matches(regExp) == false) {
				System.out.println("년월일(YYYY-MM-DD) 형식을 지켜주십시오.");
				continue;
			}
			else 
				break;
		}
		return bDate;
	}
	
}
