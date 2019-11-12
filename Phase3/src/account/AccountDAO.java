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
	private static final String regExpName = "^[°¡-ÆR]{2,4}|[a-zA-Z]{2,10}\\s[a-zA-Z]{2,10}$";
	private static final String regExpPhoneNum = "^01(?:0|1[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";
	private static final String regExpDate = "^[0-9][0-9][0-9][0-9]\\-[0-9][0-9]\\-[0-9][0-9]$";
	private Connection con;
	private PreparedStatement pstmt;

	public static void main(String[] args) {
		String id = null, pw = null;
		AccountDAO dao = new AccountDAO();
//		dao.joinAccount();

		// È¸¿ø Á¤º¸ ¼öÁ¤ ÇÊ¿ä
//		dao.modifyAccountInfo("admin");
		dao.login(id, pw);
	}

	public AccountDAO() {
		connDB();
	}

	// ÀÏ¹İ °í°´ È¸¿ø °¡ÀÔ
	public void joinAccount() {
		Scanner sc = new Scanner(System.in);
		String account_type = "C";
		String sel;

		System.out.println("<<<È¸¿ø°¡ÀÔ>>>");
		while (true) {
			System.out.println("1. °í°´ °¡ÀÔ \t2. °ü¸®ÀÚ °¡ÀÔ");
			System.out.print("¼±ÅÃ : ");
			sel = sc.nextLine();
			if (sel.equals("1") || sel.equals("2"))
				break;
			else
				System.out.println("1 ¶Ç´Â 2¸¦ ÀÔ·ÂÇÏ½Ê½Ã¿À.");
		}

		if (sel.equals("2")) {
			while (true) {
				System.out.println("<<<°ü¸®ÀÚ °¡ÀÔÀ» À§ÇÑ °ü¸®ÀÚ ÀÎÁõ>>>");
				System.out.print("°ü¸®ÀÚ ID : ");
				String adminId = sc.nextLine();
				System.out.print("°ü¸®ÀÚ PW : ");
				String adminPw = sc.nextLine();

				try {
					pstmt = con.prepareStatement(isExistAdminQeury);
					pstmt.setString(1, adminId);
					pstmt.setString(2, adminPw);
					ResultSet rs = pstmt.executeQuery();
					if (rs.next()) {
						String type = rs.getString(1);
						if (type.equals("A") == false) {
							System.out.print("°ü¸®ÀÚ °èÁ¤ ÀÎÁõ ½ÇÆĞ. ´Ù½Ã ÀÎÁõÇÏ½Ã°Ú½À´Ï±î?");
							if (sc.nextLine().toUpperCase().equals("N"))
								return;
						}
						System.out.println("°ü¸®ÀÚ ÀÎÁõ ¼º°ø!");
						account_type = "A";
						break;
					} else {
						System.out.print("°ü¸®ÀÚ °èÁ¤ ÀÎÁõ ½ÇÆĞ. ´Ù½Ã ÀÎÁõÇÏ½Ã°Ú½À´Ï±î?");
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
				System.out.println("È¸¿ø °¡ÀÔ ¼º°ø!");
			con.commit();
		} catch (SQLException e) {
			System.err.println("sql error : " + e.getMessage());
		}
	}

	// È¸¿ø Á¤º¸ ¼öÁ¤
	public void modifyAccountInfo(String id) {
		Scanner sc = new Scanner(System.in);
		AccountDTO dto = getAccountInfoById(id);

		if (dto == null)
			return;
		while (true) {
			System.out.println("<<È¸¿ø Á¤º¸ ¼öÁ¤ Ç×¸ñ ¼±ÅÃ>>");
			System.out.println("1. ¾ÆÀÌµğ[¼öÁ¤ºÒ°¡] : " + dto.getId());
			System.out.println("2. ºñ¹Ğ¹øÈ£ : " + dto.getPw());
			System.out.println("3. ÀÌ¸§ : " + dto.getName());
			System.out.println("4. ÇÚµåÆù ¹øÈ£ : " + dto.getPhone_num());
			System.out.println("5. ÁÖ¼Ò : " + dto.getAddress());
			System.out.println("6. »ı³â¿ùÀÏ : " + dto.getBirth_date().toString());
			System.out.println("7. ¼ºº° : " + dto.getSex());
			System.out.println("8. Á÷¾÷ : " + dto.getJob());
			System.out.println("9. ¼öÁ¤  ³»¿ë ÀúÀå ÈÄ Á¾·á");
			System.out.print("¼öÁ¤ Ç×¸ñ ¼±ÅÃ : ");
		
			switch (Integer.parseInt(sc.nextLine())) {
			case 1:
				System.out.println("¾ÆÀÌµğ´Â ¼öÁ¤ÇÒ ¼ö ¾ø½À´Ï´Ù.");
				break;
			case 2:
				System.out.print("º¯°æÇÒ ºñ¹Ğ¹øÈ£ ÀÔ·Â  : ");
				break;
			case 3:
				System.out.print("º¯°æÇÒ ÀÌ¸§ ÀÔ·Â : ");
				break;
			case 4:
				System.out.print("º¯°æÇÒ ÇÚµåÆù ¹øÈ£ ÀÔ·Â : ");
				break;
			case 5:
				System.out.print("º¯°æÇÒ ÁÖ¼Ò ÀÔ·Â : ");
				break;
			case 6:
				System.out.print("º¯°æÇÒ »ı³â¿ùÀÏ ÀÔ·Â : ");
				break;
			case 7:
				System.out.print("º¯°æÇÒ ¼ºº° ÀÔ·Â : ");
				break;
			case 8:
				System.out.print("º¯°æÇÒ Á÷¾÷ ÀÔ·Â : ");
				break;
			case 9:

				return;
			default:
				break;
			}
		}
	}
	
	// ·Î±×ÀÎ ±â´É ±¸Çö
	public void login(String id, String pw) {
		Scanner sc = new Scanner(System.in);
		try {
			System.out.println("<<<·Î±×ÀÎ>>>");
			System.out.print("¾ÆÀÌµğ : ");
			id = sc.nextLine();
			System.out.print("ºñ¹Ğ¹øÈ£ : ");
			pw = sc.nextLine();
			System.out.println(" " + (validCheck(id, regExpId) == false ? "false" : "true"));
			System.out.println(" " + (validCheck(pw, regExpPw) == false ? "false" : "true"));
			if(validCheck(id, regExpId) == false || validCheck(pw, regExpPw) == false) {
				System.out.println("[·Î±×ÀÎ ½ÇÆĞ] °¡ÀÔÇÏÁö ¾ÊÀº ¾ÆÀÌµğÀÌ°Å³ª, Àß¸øµÈ ºñ¹Ğ¹øÈ£ÀÔ´Ï´Ù.");
				return;
			}
			
			pstmt = con.prepareStatement(isExistAccountQuery);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				String name = rs.getString(1);
				System.out.println("[·Î±×ÀÎ ¼º°ø] "+ name + "´Ô È¯¿µÇÕ´Ï´Ù!");
			}
			else {
				System.out.println("[·Î±×ÀÎ ½ÇÆĞ] °¡ÀÔÇÏÁö ¾ÊÀº ¾ÆÀÌµğÀÌ°Å³ª, Àß¸øµÈ ºñ¹Ğ¹øÈ£ÀÔ´Ï´Ù.");
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
		// ¾ÆÀÌµğ
		System.out.print("¾ÆÀÌµğ(5~15ÀÚ¸®, Æ¯¼ö¹®ÀÚ X, ¿µ¾î ´ë¼Ò¹®ÀÚ¿Í ¼ıÀÚ °áÇÕ)");
		while (true) {
			System.out.print("¾ÆÀÌµğ ÀÔ·Â : ");
			id = sc.nextLine();

			if (validCheck(id, regExpId) == false) {
				System.out.println("¾ÆÀÌµğ´Â 5ÀÚ¸® ÀÌ»ó 15ÀÚ¸® ÀÌÇÏÀÇ ¿µ¾î, ¼ıÀÚ Á¶ÇÕÀÔ´Ï´Ù. Æ¯¼ö¹®ÀÚ¸¦ »ç¿ëÇÒ ¼ö ¾øÀ¸¸ç, ½ÃÀÛÀº ¿µ¹®À¸·Î¸¸ °¡´ÉÇÕ´Ï´Ù.");
				continue;
			}
			try {
				pstmt = con.prepareStatement(isExistIdQuery);
				pstmt.setString(1, id);
				ResultSet rs = pstmt.executeQuery();
				rs.next();

				if (rs.getInt(1) == 1) {
					System.out.println("¾ÆÀÌµğ°¡ ÀÌ¹Ì Á¸ÀçÇÕ´Ï´Ù. ´Ù¸¥ ¾ÆÀÌµğ¸¦ »ç¿ëÇÏ½Ê½Ã¿À.");
					continue;
				} else {
					System.out.println("¾ÆÀÌµğ Áßº¹Ã¼Å© ¿Ï·á");
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

		System.out.println("ºñ¹Ğ¹øÈ£´Â ¿µ¾î, ¼ıÀÚ, Æ¯¼ö¹®ÀÚ[$@!%*#] 1°³ ÀÌ»óÀ» Á¶ÇÕÇÑ 8~15ÀÚ¸® ÀÔ´Ï´Ù.");
		// ºñ¹Ğ¹øÈ£ È­ÀÎ
		System.out.print("ºñ¹Ğ¹øÈ£ ÀÔ·Â : ");
		pw = sc.nextLine();

		// ºñ¹Ğ¹øÈ£ °Ë»ç : ¿µ¾î, ¼ıÀÚ, Æ¯¼ö¹®ÀÚ¸¦ Æ÷ÇÔÇØ¾ß ÇÔ.
		if (validCheck(pw, regExpPw) == false) {
			System.out.println("ºñ¹Ğ¹øÈ£´Â ¿µ¾î, ¼ıÀÚ, Æ¯¼ö¹®ÀÚ[$@!%*#] 1°³ ÀÌ»óÀ» Æ÷ÇÔÇØ¾ß ÇÕ´Ï´Ù.");
			return null;
		}

		System.out.print("ºñ¹Ğ¹øÈ£ È®ÀÎ : ");
		String pwConfirm = sc.nextLine();

		// ÀÔ·ÂÇÑ ºñ¹Ğ¹øÈ£°¡ ¸Â´ÂÁö È®ÀÎ
		if (pw.equals(pwConfirm) == false) {
			System.out.println("ºñ¹Ğ¹øÈ£, ºñ¹Ğ¹øÈ£ È®ÀÎÀÌ ´Ù¸¨´Ï´Ù. ´Ù½Ã ÀÔ·ÂÇÏ¼¼¿ä.");
			return null;
		}
		return pw;
	}

	private String getInputName(Scanner sc) {
		String name = null;
		// ÀÌ¸§
		System.out.println("ÀÌ¸§ Á¦¾à»çÇ× : (¿µ¾î FirstName(2~10ÀÚ¸®), LastName(2~10ÀÚ¸®)");
		System.out.print("ÀÌ¸§ ÀÔ·Â : ");
		name = sc.nextLine();
		if (validCheck(name, regExpName) == false) {
			System.out.println("ÀÌ¸§Àº ÇÊ¼ö Ç×¸ñÀÌ¸ç Á¦¾à»çÇ×À» ÁöÄÑÁÖ¼¼¿ä.");
			return null;
		}
		return name;
	}

	private String getInputPhoneNumber(Scanner sc) {
		String phone_num = null;
		// ÇÚµåÆù ¹øÈ£
		System.out.println("ÇÚµåÆù ¹øÈ£ Á¦¾à»çÇ× : (¿¹½Ã 010-1234-1234 or 010-123-1234)");
		System.out.print("ÇÚµåÆù ÀÔ·Â : ");
		phone_num = sc.nextLine();

		if (validCheck(phone_num, regExpPhoneNum) == false) {
			System.out.println("ÇÚµåÆù ¹øÈ£´Â ÇÊ¼ö Á¤º¸ÀÔ´Ï´Ù. ÀÔ·Â ºÎÅ¹µå¸³´Ï´Ù. Çü½Ä : 010-123-1234 or 010-1234-1234");
			return null;
		}

		return phone_num;
	}

	private String getInputAddress(Scanner sc) {
		System.out.print("ÁÖ¼Ò ÀÔ·Â[ÇÊ¼ö ¾Æ´Ô]: ");
		String address = sc.nextLine();
		if (address == null || address.length() == 0)
			return null;
		return address;
	}

	private String getInputSex(Scanner sc) {
		String sex = null;
		System.out.println("¼ºº° (F:¿© / M : ³²)");
		while (true) {
			System.out.print("¼ºº° ÀÔ·Â : ");
			sex = sc.nextLine().toUpperCase();
			if (sex == null || sex.length() == 0)
				return null;
			if (sex.equals("F") || sex.equals("M"))
				break;
			else
				System.out.println("¼ºº°À» ÀÔ·ÂÇÏ½Ç °æ¿ì F/f(¿©ÀÚ), M/m(³²ÀÚ)À¸·Î ÀÔ·Â ºÎÅ¹µå¸³´Ï´Ù.");
		}
		return sex;
	}

	private String getInputJob(Scanner sc) {
		System.out.print("Á÷¾÷ ÀÔ·Â[ÇÊ¼ö ¾Æ´Ô] : ");
		String job = sc.nextLine();
		if (job == null || job.length() == 0)
			return null;
		return job;
	}

	private String getInputBirthDate(Scanner sc) {
		String bDate = null;

		System.out.println("»ıÀÏ Á¦¾à»çÇ×[ÇÊ¼ö ¾Æ´Ô] : YYYY-MM-DD");
		while (true) {
			System.out.print("»ıÀÏ ÀÔ·Â : ");
			bDate = sc.nextLine();

			if (bDate == null || bDate.length() == 0)
				return null;

			if (validCheck(bDate, regExpDate) == false) {
				System.out.println("³â¿ùÀÏ(YYYY-MM-DD) Çü½ÄÀ» ÁöÄÑÁÖ½Ê½Ã¿À.");
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
			System.out.println("<<<È¸¿ø Á¤º¸ ¼öÁ¤>>>");
			System.out.println("È¸¿ø Á¤º¸ ¼öÁ¤À» À§ÇÏ¿© ºñ¹Ğ¹øÈ£¸¦ ÀÔ·ÂÇØÁÖ¼¼¿ä.");
			while (true) {
				System.out.print("ºñ¹Ğ¹øÈ£ ÀÔ·Â : ");
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
					System.out.print("ºñ¹Ğ¹øÈ£°¡ Æ²¸³´Ï´Ù. ´Ù½Ã ÀÔ·ÂÇÏ½Ã°Ú½À´Ï±î?(Y/N)");
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
