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
	private static final String insertAccountQuery = "insert into account values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String isExistIdQuery = "select count(id) from account where id = ?";
	private static final String isExistAdminQeury = "select account_type from account where id = ? AND password = ?";
	private static final String getAccountInfoQuery = "select * from account where id = ? AND password = ?";
	private static final String isExistAccountQuery = "select name from account where id = ? AND password = ?";
	private static final String url = "jdbc:oracle:thin:@localhost:1600:xe";
	private static final String user = "knu";
	private static final String pw = "comp322";
	private static final String regExpId = "^[a-zA-Z]{1}[a-zA-Z0-9]{4,14}$";
	private static final String regExpPw = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,15}$";
	private static final String regExpName = "^[��-�R]{2,4}|[a-zA-Z]{2,10}\\s[a-zA-Z]{2,10}$";
	private static final String regExpPhoneNum = "^01(?:0|1[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";
	private static final String regExpDate = "^[0-9][0-9][0-9][0-9]\\-[0-9][0-9]\\-[0-9][0-9]$";
	private Connection con;
	private PreparedStatement pstmt;

	public static void main(String[] args) {
		String id = null, pw = null;
		AccountDAO dao = new AccountDAO();
//		dao.joinAccount();

		// ȸ�� ���� ���� �ʿ�
//		dao.modifyAccountInfo("admin");
		dao.login(id, pw);
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
		String pw;
		while((pw = getInputPasswd(sc)) == null) { }
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

			if (address == null || address.length() == 0)
				pstmt.setNull(5, Types.CHAR);
			else
				pstmt.setString(5, address);

			if (birth_date == null || birth_date.length() == 0)
				pstmt.setNull(6, Types.DATE);
			else
				pstmt.setDate(6, Date.valueOf(birth_date));

			if (sex == null || sex.length() == 0)
				pstmt.setNull(7, Types.CHAR);
			else
				pstmt.setString(7, sex);

			if (job == null || job.length() == 0)
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
		AccountDTO dto = getAccountInfoById(id);

		if (dto == null)
			return;
		while (true) {
			System.out.println("<<ȸ�� ���� ���� �׸� ����>>");
			System.out.println("1. ���̵�[�����Ұ�] : " + dto.getId());
			System.out.println("2. ��й�ȣ : " + dto.getPw());
			System.out.println("3. �̸� : " + dto.getName());
			System.out.println("4. �ڵ��� ��ȣ : " + dto.getPhone_num());
			System.out.println("5. �ּ� : " + dto.getAddress());
			System.out.println("6. ������� : " + dto.getBirth_date().toString());
			System.out.println("7. ���� : " + dto.getSex());
			System.out.println("8. ���� : " + dto.getJob());
			System.out.println("9. ����  ���� ���� �� ����");
			System.out.print("���� �׸� ���� : ");
		
			switch (Integer.parseInt(sc.nextLine())) {
			case 1:
				System.out.println("���̵�� ������ �� �����ϴ�.");
				break;
			case 2:
				System.out.print("������ ��й�ȣ �Է�  : ");
				break;
			case 3:
				System.out.print("������ �̸� �Է� : ");
				break;
			case 4:
				System.out.print("������ �ڵ��� ��ȣ �Է� : ");
				break;
			case 5:
				System.out.print("������ �ּ� �Է� : ");
				break;
			case 6:
				System.out.print("������ ������� �Է� : ");
				break;
			case 7:
				System.out.print("������ ���� �Է� : ");
				break;
			case 8:
				System.out.print("������ ���� �Է� : ");
				break;
			case 9:

				return;
			default:
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
			System.out.println(" " + (validCheck(id, regExpId) == false ? "false" : "true"));
			System.out.println(" " + (validCheck(pw, regExpPw) == false ? "false" : "true"));
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

	private String getInputPasswd(Scanner sc) {
		String pw = null;

		System.out.println("��й�ȣ�� ����, ����, Ư������[$@!%*#] 1�� �̻��� ������ 8~15�ڸ� �Դϴ�.");
		// ��й�ȣ ȭ��
		System.out.print("��й�ȣ �Է� : ");
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

	private String getInputName(Scanner sc) {
		String name = null;
		// �̸�
		System.out.println("�̸� ������� : (���� FirstName(2~10�ڸ�), LastName(2~10�ڸ�)");
		System.out.print("�̸� �Է� : ");
		name = sc.nextLine();
		if (validCheck(name, regExpName) == false) {
			System.out.println("�̸��� �ʼ� �׸��̸� ��������� �����ּ���.");
			return null;
		}
		return name;
	}

	private String getInputPhoneNumber(Scanner sc) {
		String phone_num = null;
		// �ڵ��� ��ȣ
		System.out.println("�ڵ��� ��ȣ ������� : (���� 010-1234-1234 or 010-123-1234)");
		System.out.print("�ڵ��� �Է� : ");
		phone_num = sc.nextLine();

		if (validCheck(phone_num, regExpPhoneNum) == false) {
			System.out.println("�ڵ��� ��ȣ�� �ʼ� �����Դϴ�. �Է� ��Ź�帳�ϴ�. ���� : 010-123-1234 or 010-1234-1234");
			return null;
		}

		return phone_num;
	}

	private String getInputAddress(Scanner sc) {
		System.out.print("�ּ� �Է�[�ʼ� �ƴ�]: ");
		String address = sc.nextLine();
		if (address == null || address.length() == 0)
			return null;
		return address;
	}

	private String getInputSex(Scanner sc) {
		String sex = null;
		System.out.println("���� (F:�� / M : ��)");
		while (true) {
			System.out.print("���� �Է� : ");
			sex = sc.nextLine().toUpperCase();
			if (sex == null || sex.length() == 0)
				return null;
			if (sex.equals("F") || sex.equals("M"))
				break;
			else
				System.out.println("������ �Է��Ͻ� ��� F/f(����), M/m(����)���� �Է� ��Ź�帳�ϴ�.");
		}
		return sex;
	}

	private String getInputJob(Scanner sc) {
		System.out.print("���� �Է�[�ʼ� �ƴ�] : ");
		String job = sc.nextLine();
		if (job == null || job.length() == 0)
			return null;
		return job;
	}

	private String getInputBirthDate(Scanner sc) {
		String bDate = null;

		System.out.println("���� �������[�ʼ� �ƴ�] : YYYY-MM-DD");
		while (true) {
			System.out.print("���� �Է� : ");
			bDate = sc.nextLine();

			if (bDate == null || bDate.length() == 0)
				return null;

			if (validCheck(bDate, regExpDate) == false) {
				System.out.println("�����(YYYY-MM-DD) ������ �����ֽʽÿ�.");
				continue;
			} else
				break;
		}
		return bDate;
	}

	private AccountDTO getAccountInfoById(String id) {
		Scanner sc = new Scanner(System.in);
		AccountDTO dto = null;
		try {
			System.out.println("<<<ȸ�� ���� ����>>>");
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
					if (sc.nextLine().toUpperCase().equals("N"))
						break;
				}
			}

		} catch (SQLException e) {
			System.err.println("sql error : " + e.getMessage());
			System.exit(1);
		}
		return dto;
	}

}
