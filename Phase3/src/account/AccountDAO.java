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
	private static final String regExpName = "^[��-�R]{2,4}|[a-zA-Z]{2,10}\\s[a-zA-Z]{2,10}$";
	private static final String regExpPhoneNum = "^01(?:0|1[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";
	private static final String regExpDate = "^(19[0-9][0-9]|20\\d{2})-(0[0-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$";
	private Connection con;
	private PreparedStatement pstmt;
	private Statement stmt;

	public static void main(String[] args) {
		String id = null, pw = null;
		AccountDAO dao = new AccountDAO();
//		dao.joinAccount();
//		dao.login(id, pw);
		dao.modifyAccountInfo("nsun9505");
		dao.withdrawalAccount("nsun9505", "������", "A");
	}

	public AccountDAO() {
		connDB();
	}

	// �Ϲ� �� ȸ�� ����
	public void joinAccount() {
		Scanner sc = new Scanner(System.in);
		String account_type = "C";
		String sel;

		System.out.println("<<<ȸ������>>>");
		while (true) {
			System.out.println("1. �� ���� \t2. ������ ����");
			System.out.print("���� : ");
			sel = sc.nextLine();
			if (sel.equals("1") || sel.equals("2"))
				break;
			else
				System.out.println("1 �Ǵ� 2�� �Է��Ͻʽÿ�.");
		}

		if (sel.equals("2")) {
			while (true) {
				System.out.println("<<<������ ������ ���� ������ ����>>>");
				System.out.print("������ ID : ");
				String adminId = sc.nextLine();
				System.out.print("������ PW : ");
				String adminPw = sc.nextLine();

				try {
					pstmt = con.prepareStatement(isExistAdminQeury);
					pstmt.setString(1, adminId);
					pstmt.setString(2, adminPw);
					ResultSet rs = pstmt.executeQuery();
					if (rs.next()) {
						String type = rs.getString(1);
						if (type.equals("A") == false) {
							System.out.print("������ ���� ���� ����. �ٽ� �����Ͻðڽ��ϱ�?");
							if (sc.nextLine().toUpperCase().equals("N"))
								return;
						}
						System.out.println("������ ���� ����!");
						account_type = "A";
						break;
					} else {
						System.out.print("������ ���� ���� ����. �ٽ� �����Ͻðڽ��ϱ�?");
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
			pw = getInputPasswd(sc, "��й�ȣ �Է�  : ");
			if(pw != null)
				break;
		}
		while(true) {
			name = getInputName(sc, "�̸� �Է�[�ʼ�����] : ");
			if(name != null)
				break;
		}
		while(true) {
			phone_num = getInputPhoneNumber(sc, "�ڵ��� ��ȣ �Է�[�ʼ�����] : ");
			if(phone_num != null)
				break;
		}
		System.out.println("�Ʒ� �������� �ʼ� ������ �ƴմϴ�. EnterKey�� ���� ��ŵ�� �� �ֽ��ϴ�.");
 		String address = getInputAddress(sc, "�ּ� �Է�[�ʼ��ƴ�] : ");
		String birth_date = getInputBirthDate(sc, "������� �Է�[�ʼ��ƴ�] : ");
		String sex = getInputSex(sc, "���� �Է�[�ʼ��ƴ�] : ");
		String job = getInputJob(sc, "���� �Է�[�ʼ��ƴ�] : ");

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
				System.out.println("ȸ�� ���� ����!");
			con.commit();
		} catch (SQLException e) {
			System.err.println("sql error : " + e.getMessage());
		}
	}

	// ȸ�� ���� ����
	public void modifyAccountInfo(String id) {
		Scanner sc = new Scanner(System.in);
		boolean flag = false;
		int ret;

		System.out.println("<<<ȸ�� ���� ����>>>");
		AccountDTO dto = getAccountInfoById(id);
		if (dto == null) {
			System.out.println("��� ��ȣ�� Ʋ���ϴ�. ȸ�� ���� ������ �����մϴ�.");
			return;	
		}

		while (true) {
			System.out.println("<<ȸ�� ���� ���� �׸� ����>>");
			System.out.println("1. ���̵�[�����Ұ�] : " + dto.getId());
			System.out.println("2. ��й�ȣ : " + dto.getPw());
			System.out.println("3. �̸� : " + dto.getName());
			System.out.println("4. �ڵ��� ��ȣ : " + dto.getPhone_num());
			System.out.println("5. �ּ� : " + (dto.getAddress() == null ? "[�Է����� ����]" : dto.getAddress()));
			System.out.println("6. ������� : " + (dto.getBirth_date() == null ? "[�Է����� ����]" : dto.getBirth_date().toString()));
			System.out.println("7. ���� : " + (dto.getSex() == null ? "[�Է����� ����]" : dto.getSex()));
			System.out.println("8. ���� : " + (dto.getJob() == null ? "[�Է����� ����]" : dto.getJob()));
			System.out.println("9. ����  ���� ���� �� ����");
			System.out.println("0. ���� ���� �������� �ʰ� ����");
			System.out.print("���� �׸� ���� : ");

			switch (sc.nextLine()) {
			case "1":
				System.out.println("���̵�� ������ �� �����ϴ�.");
				break;
			case "2":
				String pw = getInputPasswd(sc, "������ ��й�ȣ �Է�  : ");
				if (pw != null) {
					flag = true;
					dto.setPw(pw);
				}
				break;
			case "3":
				String name = getInputName(sc, "������ �̸� �Է� : ");
				if (name != null) {
					flag = true;
					dto.setName(name);
				}
				break;
			case "4":
				String phone_num = getInputPhoneNumber(sc, "������ �ڵ��� ��ȣ �Է� : ");
				if (phone_num != null) {
					flag = true;
					dto.setPhone_num(phone_num);
				}
				break;
			case "5":
				String address = getInputAddress(sc, "������ �ּ� �Է� : ");
				if (address != null) {
					flag = true;
					dto.setAddress(address);
				} else {
					flag = decisionNullInput("address", dto, sc);
				}
				break;
			case "6":
				String bDate = getInputBirthDate(sc, "������ ������� �Է� : ");
				if (bDate != null) {
					flag = true;
					dto.setBirth_date(Date.valueOf(bDate));
				} else {
					flag = decisionNullInput("birth_date", dto, sc);
				}
				break;
			case "7":
				String sex = getInputSex(sc, "������ ���� �Է� : ");
				if (sex != null) {
					flag = true;
					dto.setSex(sex);
				} else {
					flag = decisionNullInput("sex", dto, sc);
				}
				break;
			case "8":
				String job = getInputJob(sc, "������ ���� �Է� : ");
				if (job != null) {
					flag = true;
					dto.setJob(job);
				} else {
					flag = decisionNullInput("job", dto, sc);
				}
				break;
			case "9":
				if (flag == false) {
					System.out.println("������ ������ �����ϴ�. ȸ�� ���� ������ �����մϴ�.");
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
							System.out.println("������ �Ϸ��߽��ϴ�. ȸ�� ���� ������ �����մϴ�.");
						con.commit();
					} catch (SQLException e) {
						System.err.println("[modifyAccount method] sql error : " + e.getMessage());
						return;
					}
				}
				return;
			case "0":
				System.out.println("ȸ�� ������ �������� �ʰ� �����մϴ�.");
				break;
			default:
				System.out.println("���� �Է��� ��ȿ���� ���� �Է��Դϴ�.");
				break;
			}
		}
	}
	
	// �α��� ��� ����
	public void login(String id, String pw) {
		Scanner sc = new Scanner(System.in);
		try {
			System.out.println("<<<�α���>>>");
			System.out.print("���̵� : ");
			id = sc.nextLine();
			System.out.print("��й�ȣ : ");
			pw = sc.nextLine();
			if(validCheck(id, regExpId) == false || validCheck(pw, regExpPw) == false) {
				System.out.println("[�α��� ����] �������� ���� ���̵��̰ų�, �߸��� ��й�ȣ�Դϴ�.");
				return;
			}
			
			pstmt = con.prepareStatement(isExistAccountQuery);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				String name = rs.getString(1);
				System.out.println("[�α��� ����] "+ name + "�� ȯ���մϴ�!");
			}
			else {
				System.out.println("[�α��� ����] �������� ���� ���̵��̰ų�, �߸��� ��й�ȣ�Դϴ�.");
			}
		}catch(SQLException e) {
			System.err.println("sql error : " + e.getMessage());
		}
	}
	
	// ȸ��Ż�� ��� ���� �Ϸ�
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
					System.out.println("������ ������ �ּ� 1�� �̻� �־�� �ϹǷ�  �ش� ������ ����("+id+")�� Ż���� �� �����ϴ�.");
					return;
				}
			}
			
			System.out.println("<<<ȸ�� Ż��>>>");
			System.out.println("ȸ�� Ż�� ���� ��й�ȣ�� �Է����ּ���.");
			System.out.print("��й�ȣ �Է� : ");
			String pw = sc.nextLine();
			
			if(validCheck(pw, regExpPw) == false) {
				System.out.println("��A��ȣ�� Ʋ�Ƚ��ϴ�. ���� ȭ������ ���ư��ϴ�.");
				return;
			}
			
			pstmt = con.prepareStatement(isExistAccountQuery);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				System.out.println(rs.getString(1)+"�� ������ ȸ��Ż�� �Ͻðڽ��ϱ�?(Y/N)");
				if(sc.nextLine().toUpperCase().equals("Y")) {
					pstmt = con.prepareStatement("delete from account where id = ?");
					pstmt.setString(1, id);
					int ret = pstmt.executeUpdate();
					if(ret == 1)
						System.out.println("ȸ��Ż�� �Ϸ�!\n�ŷ����� ������  3�Ⱓ ������ �� �Ŀ� ������ �Ǵ� �� ���ǹٶ��ϴ�.");
					con.commit();
				}
				else {
					System.out.println("ȸ��Ż�� ���");
				}
			}else {
				System.out.println("��й�ȣ�� Ʋ�Ƚ��ϴ�. ���� ȭ������ ���ư��ϴ�.");
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
		// ���̵�
		System.out.print("���̵�(5~15�ڸ�, Ư������ X, ���� ��ҹ��ڿ� ���� ����)");
		while (true) {
			System.out.print("���̵� �Է� : ");
			id = sc.nextLine();

			if (validCheck(id, regExpId) == false) {
				System.out.println("���̵�� 5�ڸ� �̻� 15�ڸ� ������ ����, ���� �����Դϴ�. Ư�����ڸ� ����� �� ������, ������ �������θ� �����մϴ�.");
				continue;
			}
			try {
				pstmt = con.prepareStatement(isExistIdQuery);
				pstmt.setString(1, id);
				ResultSet rs = pstmt.executeQuery();
				rs.next();

				if (rs.getInt(1) == 1) {
					System.out.println("���̵� �̹� �����մϴ�. �ٸ� ���̵� ����Ͻʽÿ�.");
					continue;
				} else {
					System.out.println("���̵� �ߺ�üũ �Ϸ�");
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

		System.out.println("��й�ȣ�� ����, ����, Ư������[$@!%*#] 1�� �̻��� ������ 8~15�ڸ� �Դϴ�.");
		// ��й�ȣ ȭ��
		System.out.print(message);
		pw = sc.nextLine();

		// ��й�ȣ �˻� : ����, ����, Ư�����ڸ� �����ؾ� ��.
		if (validCheck(pw, regExpPw) == false) {
			System.out.println("��й�ȣ�� ����, ����, Ư������[$@!%*#] 1�� �̻��� �����ؾ� �մϴ�.");
			return null;
		}

		System.out.print("��й�ȣ Ȯ�� : ");
		String pwConfirm = sc.nextLine();

		// �Է��� ��й�ȣ�� �´��� Ȯ��
		if (pw.equals(pwConfirm) == false) {
			System.out.println("��й�ȣ, ��й�ȣ Ȯ���� �ٸ��ϴ�. �ٽ� �Է��ϼ���.");
			return null;
		}
		return pw;
	}

	private String getInputName(Scanner sc, String message) {
		String name = null;
		// �̸�
		System.out.println("�̸� ������� : (���� FirstName(2~10�ڸ�), LastName(2~10�ڸ�)");
		System.out.print(message);
		name = sc.nextLine();
		if (validCheck(name, regExpName) == false) {
			System.out.println("�̸� ��������� �����ּ���.");
			return null;
		}
		return name;
	}

	private String getInputPhoneNumber(Scanner sc, String meesage) {
		String phone_num = null;
		// �ڵ��� ��ȣ
		System.out.println("�ڵ��� ��ȣ ������� : (���� 010-1234-1234 or 010-123-1234)");
		System.out.print(meesage);
		phone_num = sc.nextLine();

		if (validCheck(phone_num, regExpPhoneNum) == false) {
			System.out.println("��ȿ���� ���� ���� �Դϴ�. : 010-123-1234 or 010-1234-1234");
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
		System.out.println("���� (F:�� / M : ��)");
		while (true) {
			System.out.print(message);
			sex = sc.nextLine().toUpperCase();
			if (sex == null || sex.length() == 0)
				return null;
			if (sex.equals("F") || sex.equals("M"))
				break;
			else {
				System.out.println("������ �Է��Ͻ� ��� F/f(����), M/m(����)���� �Է� ��Ź�帳�ϴ�.");
				System.out.print("�ٽ� �Է��Ͻðڽ��ϱ�?(Y/N) : ");
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

		System.out.println("���� �������[�ʼ� �ƴ�] : YYYY-MM-DD");
		while (true) {
			System.out.print(message);
			bDate = sc.nextLine();

			if (bDate == null || bDate.length() == 0)
				return null;

			if (validCheck(bDate, regExpDate) == false) {
				System.out.println("�����(YYYY-MM-DD) ������ �����ֽʽÿ�.");
				System.out.print("�ٽ� �Է��Ͻðڽ��ϱ�?(Y/N) : ");
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
			System.out.println("ȸ�� ���� ������ ���Ͽ� ��й�ȣ�� �Է����ּ���.");
			while (true) {
				System.out.print("��й�ȣ �Է� : ");
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
					System.out.print("��й�ȣ�� Ʋ���ϴ�. �ٽ� �Է��Ͻðڽ��ϱ�?(Y/N)");
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
	
	// ���� �ʿ�
	private boolean decisionNullInput(String param, AccountDTO dto, Scanner sc) {
		boolean ret = false;
		while (true) {
			System.out.println("�Է� ���� �����ϴ�. �̴�� �����Ͻðڽ��ϱ�?(Y/N) : ");
			String input = sc.nextLine().toUpperCase();
			if (input.equals("Y")) {
				if(param.equals("address")) dto.setAddress(null); 
				else if(param.equals("birth_date")) dto.setBirth_date(null);
				else if(param.equals("job")) dto.setJob(null);
				else if(param.equals("sex")) dto.setSex(null);
				ret = true;
				break;
			} else if(input.equals("N")) {
				// ���� �ʿ�
				break;
			} else {
				System.out.println("N �Ǵ� Y�� �Է��ϼ���.");
			}
		}
		return ret;
	}
}
