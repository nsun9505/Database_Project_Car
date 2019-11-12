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

	// �Ϲ� �� ȸ�� ����
	public void joinAccount() {
		Scanner sc = new Scanner(System.in);
		System.out.println("<<<ȸ������>>>");

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
				System.out.println("ȸ�� ���� ����!");
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
		// ���̵�
		System.out.print("���̵�(5~15�ڸ�, Ư������ X, ���� ��ҹ��ڿ� ���� ����)");
		while (true) {
			System.out.print("���̵� �Է� : ");
			id = sc.nextLine();

			if (id.matches(regExp) == false) {
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
	
	private String getInputPasswd(Scanner sc) {
		String pw = null;
		String regExp ="^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,15}$";
		
		System.out.println("��й�ȣ�� ����, ����, Ư������[$@!%*#] 1�� �̻��� ������ 8~15�ڸ� �Դϴ�.");
		// ��й�ȣ ȭ��
		while (true) {
			System.out.print("��й�ȣ �Է� : ");
			pw = sc.nextLine();
			
			// ��й�ȣ �˻� : ����, ����, Ư�����ڸ� �����ؾ� ��.
			if(pw.matches(regExp) == false) {
				System.out.println("��й�ȣ�� ����, ����, Ư������[$@!%*#] 1�� �̻��� �����ؾ� �մϴ�.");
				continue;
			}
			
			System.out.print("��й�ȣ Ȯ�� : ");
			String pwConfirm = sc.nextLine();

			// �Է��� ��й�ȣ�� �´��� Ȯ��
			if (pw.equals(pwConfirm) == false) {
				System.out.println("��й�ȣ, ��й�ȣ Ȯ���� �ٸ��ϴ�. �ٽ� �Է��ϼ���.");
				continue;
			} else 
				break;
		}
		
		return pw;
	}

	private String getInputName(Scanner sc) {
		String regExp = "^[��-�R]{2,4}|[a-zA-Z]{2,10}\\s[a-zA-Z]{2,10}$";
		String name = null;
		// �̸�
		while (true) {
			System.out.println("�̸� ������� : (���� FirstName(2~10�ڸ�), LastName(2~10�ڸ�)");
			System.out.print("�̸� �Է� : ");
			name = sc.nextLine();
			if (name.matches(regExp) == false) {
				System.out.println("�̸��� �ʼ� �׸��̸� ��������� �����ּ���.");
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
		// �ڵ��� ��ȣ
		System.out.println("�ڵ��� ��ȣ ������� : (���� 01012341234 or 010-1234-1234)");
		while (true) {
			System.out.print("�ڵ��� �Է� : ");
			phone_num = sc.nextLine();
			
			if (phone_num.matches(regExp) == false) {
				System.out.println("�ڵ��� ��ȣ�� �ʼ� �����Դϴ�. �Է� ��Ź�帳�ϴ�. ���� : 01012341234 or 010-1234-1234");
				continue;
			}
			break;
		}
		return phone_num;
	}

	private String getInputAddress(Scanner sc) {
		System.out.print("�ּ� �Է�[�ʼ� �ƴ�]: ");
		String address = sc.nextLine();
		if(address == null || address.length() == 0)
			return null;
		return address;
	}
	
	private String getInputSex(Scanner sc) {
		String sex = null;
		System.out.println("���� (F:�� / M : ��)");
		while(true) {
			System.out.print("���� �Է� : ");
			sex = sc.nextLine().toUpperCase();
			if(sex == null || sex.length() == 0)
				return null;
			if(sex.equals("F") || sex.equals("M"))
				break;
			else
				System.out.println("������ �Է��Ͻ� ��� F/f(����), M/m(����)���� �Է� ��Ź�帳�ϴ�.");
		}
		return sex;
	}

	private String getInputJob(Scanner sc) {
		System.out.print("���� �Է�[�ʼ� �ƴ�] : ");
		String job = sc.nextLine();
		if(job == null || job.length() == 0)
			return null;
		return job;
	}

	private String getInputBirthDate(Scanner sc){
		String regExp = "^[0-9][0-9][0-9][0-9]\\-[0-9][0-9]\\-[0-9][0-9]$";
		String bDate = null;
		
		System.out.println("���� �������[�ʼ� �ƴ�] : YYYY-MM-DD");
		while (true) {
			System.out.print("���� �Է� : ");
			bDate = sc.nextLine();
			
			if(bDate == null || bDate.length() == 0)
				return null;
			
			if (bDate.matches(regExp) == false) {
				System.out.println("�����(YYYY-MM-DD) ������ �����ֽʽÿ�.");
				continue;
			}
			else 
				break;
		}
		return bDate;
	}
	
}
