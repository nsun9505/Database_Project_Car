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
	private static final String regExpName = "^[°¡-ÆR]{2,4}|[a-zA-Z]{2,10}\\s[a-zA-Z]{2,10}$";
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
		dao.withdrawalAccount("nsun9505", "³²»óÀ±", "A");
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
		String pw, name, phone_num;
		while(true) {
			pw = getInputPasswd(sc, "ºñ¹Ğ¹øÈ£ ÀÔ·Â  : ");
			if(pw != null)
				break;
		}
		while(true) {
			name = getInputName(sc, "ÀÌ¸§ ÀÔ·Â[ÇÊ¼öÁ¤º¸] : ");
			if(name != null)
				break;
		}
		while(true) {
			phone_num = getInputPhoneNumber(sc, "ÇÚµåÆù ¹øÈ£ ÀÔ·Â[ÇÊ¼öÁ¤º¸] : ");
			if(phone_num != null)
				break;
		}
		System.out.println("¾Æ·¡ Á¤º¸µéÀº ÇÊ¼ö Á¤º¸°¡ ¾Æ´Õ´Ï´Ù. EnterKey¸¦ ´­·¯ ½ºÅµÇÒ ¼ö ÀÖ½À´Ï´Ù.");
 		String address = getInputAddress(sc, "ÁÖ¼Ò ÀÔ·Á[ÇÊ¼ö¾Æ´Ô] : ");
		String birth_date = getInputBirthDate(sc, "»ı³â¿ùÀÏ ÀÔ·Á[ÇÊ¼ö¾Æ´Ô] : ");
		String sex = getInputSex(sc, "¼ºº° ÀÔ·Á[ÇÊ¼ö¾Æ´Ô] : ");
		String job = getInputJob(sc, "Á÷¾÷ ÀÔ·Á[ÇÊ¼ö¾Æ´Ô] : ");

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
				System.out.println("È¸¿ø °¡ÀÔ ¼º°ø!");
			con.commit();
		} catch (SQLException e) {
			System.err.println("sql error : " + e.getMessage());
		}
	}

	// È¸¿ø Á¤º¸ ¼öÁ¤
	public void modifyAccountInfo(String id) {
		Scanner sc = new Scanner(System.in);
		boolean flag = false;
		int ret;

		System.out.println("<<<È¸¿ø Á¤º¸ ¼öÁ¤>>>");
		AccountDTO dto = getAccountInfoById(id);
		if (dto == null) {
			System.out.println("ºñ¹Ğ ¹øÈ£°¡ Æ²¸³´Ï´Ù. È¸¿ø Á¤º¸ ¼öÁ¤À» Á¾·áÇÕ´Ï´Ù.");
			return;	
		}

		while (true) {
			System.out.println("<<È¸¿ø Á¤º¸ ¼öÁ¤ Ç×¸ñ ¼±ÅÃ>>");
			System.out.println("1. ¾ÆÀÌµğ[¼öÁ¤ºÒ°¡] : " + dto.getId());
			System.out.println("2. ºñ¹Ğ¹øÈ£ : " + dto.getPw());
			System.out.println("3. ÀÌ¸§ : " + dto.getName());
			System.out.println("4. ÇÚµåÆù ¹øÈ£ : " + dto.getPhone_num());
			System.out.println("5. ÁÖ¼Ò : " + (dto.getAddress() == null ? "[ÀÔ·ÂÇÏÁö ¾ÊÀ½]" : dto.getAddress()));
			System.out.println("6. »ı³â¿ùÀÏ : " + (dto.getBirth_date() == null ? "[ÀÔ·ÂÇÏÁö ¾ÊÀ½]" : dto.getBirth_date().toString()));
			System.out.println("7. ¼ºº° : " + (dto.getSex() == null ? "[ÀÔ·ÂÇÏÁö ¾ÊÀ½]" : dto.getSex()));
			System.out.println("8. Á÷¾÷ : " + (dto.getJob() == null ? "[ÀÔ·ÂÇÏÁö ¾ÊÀ½]" : dto.getJob()));
			System.out.println("9. ¼öÁ¤  ³»¿ë ÀúÀå ÈÄ Á¾·á");
			System.out.println("0. ¼öÁ¤ ³»¿ë ÀúÀåÇÏÁö ¾Ê°í Á¾·á");
			System.out.print("¼öÁ¤ Ç×¸ñ ¼±ÅÃ : ");

			switch (sc.nextLine()) {
			case "1":
				System.out.println("¾ÆÀÌµğ´Â ¼öÁ¤ÇÒ ¼ö ¾ø½À´Ï´Ù.");
				break;
			case "2":
				String pw = getInputPasswd(sc, "º¯°æÇÒ ºñ¹Ğ¹øÈ£ ÀÔ·Â  : ");
				if (pw != null) {
					flag = true;
					dto.setPw(pw);
				}
				break;
			case "3":
				String name = getInputName(sc, "º¯°æÇÒ ÀÌ¸§ ÀÔ·Â : ");
				if (name != null) {
					flag = true;
					dto.setName(name);
				}
				break;
			case "4":
				String phone_num = getInputPhoneNumber(sc, "º¯°æÇÒ ÇÚµåÆù ¹øÈ£ ÀÔ·Â : ");
				if (phone_num != null) {
					flag = true;
					dto.setPhone_num(phone_num);
				}
				break;
			case "5":
				String address = getInputAddress(sc, "º¯°æÇÒ ÁÖ¼Ò ÀÔ·Â : ");
				if (address != null) {
					flag = true;
					dto.setAddress(address);
				} else {
					flag = decisionNullInput("address", dto, sc);
				}
				break;
			case "6":
				String bDate = getInputBirthDate(sc, "º¯°æÇÒ »ı³â¿ùÀÏ ÀÔ·Â : ");
				if (bDate != null) {
					flag = true;
					dto.setBirth_date(Date.valueOf(bDate));
				} else {
					flag = decisionNullInput("birth_date", dto, sc);
				}
				break;
			case "7":
				String sex = getInputSex(sc, "º¯°æÇÒ ¼ºº± ÀÔ·Â : ");
				if (sex != null) {
					flag = true;
					dto.setSex(sex);
				} else {
					flag = decisionNullInput("sex", dto, sc);
				}
				break;
			case "8":
				String job = getInputJob(sc, "º¯°æÇÒ Á÷¾÷ ÀÔ·Â : ");
				if (job != null) {
					flag = true;
					dto.setJob(job);
				} else {
					flag = decisionNullInput("job", dto, sc);
				}
				break;
			case "9":
				if (flag == false) {
					System.out.println("¼öÁ¤ÇÑ ³»¿ëÀÌ ¾ø½À´Ï´Ù. È¸¿ø Á¤º¸ ¼öÁ¤À» Á¾·áÇÕ´Ï´Ù.");
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
							System.out.println("¼öÁ¤À» ¿Ï·áÇß½À´Ï´Ù. È¸¿ø Á¤º¸ ¼öÁ¤À» Á¾·áÇÕ´Ï´Ù.");
						con.commit();
					} catch (SQLException e) {
						System.err.println("[modifyAccount method] sql error : " + e.getMessage());
						return;
					}
				}
				return;
			case "0":
				System.out.println("È¸¿ø Á¤º¸¸¦ ¼öÁ¤ÇÏÁö ¾Ê°í Á¾·áÇÕ´Ï´Ù.");
				break;
			default:
				System.out.println("ÇöÀç ÀÔ·ÂÀº À¯È¿ÇÏÁö ¾ÊÀº ÀÔ·ÂÀÔ´Ï´Ù.");
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
	
	// È¸¿øÅ»Åğ ±â´É ±¸Çö ¿Ï·á
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
					System.out.println("°ü¸®ÀÚ °èÁ¤Àº ÃÖ¼Ò 1°³ ÀÌ»ó ÀÖ¾î¾ß ÇÏ¹Ç·Î  ÇØ´ç °ü¸®ÀÚ °èÁ¤("+id+")Àº Å»ÅğÇÒ ¼ö ¾ø½À´Ï´Ù.");
					return;
				}
			}
			
			System.out.println("<<<È¸¿ø Å»Åğ>>>");
			System.out.println("È¸¿ø Å»Åğ¸¦ À§ÇØ ºñ¹Ğ¹øÈ£¸¦ ÀÔ·ÂÇØÁÖ¼¼¿ä.");
			System.out.print("ºñ¹Ğ¹øÈ£ ÀÔ·Â : ");
			String pw = sc.nextLine();
			
			if(validCheck(pw, regExpPw) == false) {
				System.out.println("ºñ“A¹øÈ£°¡ Æ²·È½À´Ï´Ù. ÀÌÀü È­¸éÀ¸·Î µ¹¾Æ°©´Ï´Ù.");
				return;
			}
			
			pstmt = con.prepareStatement(isExistAccountQuery);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				System.out.println(rs.getString(1)+"´Ô Á¤¸»·Î È¸¿øÅ»Åğ¸¦ ÇÏ½Ã°Ú½À´Ï±î?(Y/N)");
				if(sc.nextLine().toUpperCase().equals("Y")) {
					pstmt = con.prepareStatement("delete from account where id = ?");
					pstmt.setString(1, id);
					int ret = pstmt.executeUpdate();
					if(ret == 1)
						System.out.println("È¸¿øÅ»Åğ ¿Ï·á!\n°Å·¡³»¿ª Á¤º¸´Â  3³â°£ À¯Áö°¡ µÈ ÈÄ¿¡ »èÁ¦°¡ µÇ´Â Á¡ À¯ÀÇ¹Ù¶ø´Ï´Ù.");
					con.commit();
				}
				else {
					System.out.println("È¸¿øÅ»Åğ Ãë¼Ò");
				}
			}else {
				System.out.println("ºñ¹Ğ¹øÈ£°¡ Æ²·È½À´Ï´Ù. ÀÌÀü È­¸éÀ¸·Î µ¹¾Æ°©´Ï´Ù.");
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

	private String getInputPasswd(Scanner sc, String message) {
		String pw = null;

		System.out.println("ºñ¹Ğ¹øÈ£´Â ¿µ¾î, ¼ıÀÚ, Æ¯¼ö¹®ÀÚ[$@!%*#] 1°³ ÀÌ»óÀ» Á¶ÇÕÇÑ 8~15ÀÚ¸® ÀÔ´Ï´Ù.");
		// ºñ¹Ğ¹øÈ£ È­ÀÎ
		System.out.print(message);
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

	private String getInputName(Scanner sc, String message) {
		String name = null;
		// ÀÌ¸§
		System.out.println("ÀÌ¸§ Á¦¾à»çÇ× : (¿µ¾î FirstName(2~10ÀÚ¸®), LastName(2~10ÀÚ¸®)");
		System.out.print(message);
		name = sc.nextLine();
		if (validCheck(name, regExpName) == false) {
			System.out.println("ÀÌ¸§ Á¦¾à»çÇ×À» ÁöÄÑÁÖ¼¼¿ä.");
			return null;
		}
		return name;
	}

	private String getInputPhoneNumber(Scanner sc, String meesage) {
		String phone_num = null;
		// ÇÚµåÆù ¹øÈ£
		System.out.println("ÇÚµåÆù ¹øÈ£ Á¦¾à»çÇ× : (¿¹½Ã 010-1234-1234 or 010-123-1234)");
		System.out.print(meesage);
		phone_num = sc.nextLine();

		if (validCheck(phone_num, regExpPhoneNum) == false) {
			System.out.println("À¯È¿ÇÏÁö ¾ÊÀº Çü½Ä ÀÔ´Ï´Ù. : 010-123-1234 or 010-1234-1234");
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
		System.out.println("¼ºº° (F:¿© / M : ³²)");
		while (true) {
			System.out.print(message);
			sex = sc.nextLine().toUpperCase();
			if (sex == null || sex.length() == 0)
				return null;
			if (sex.equals("F") || sex.equals("M"))
				break;
			else {
				System.out.println("¼ºº°À» ÀÔ·ÂÇÏ½Ç °æ¿ì F/f(¿©ÀÚ), M/m(³²ÀÚ)À¸·Î ÀÔ·Â ºÎÅ¹µå¸³´Ï´Ù.");
				System.out.print("´Ù½Ã ÀÔ·ÂÇÏ½Ã°Ú½À´Ï±î?(Y/N) : ");
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

		System.out.println("»ıÀÏ Á¦¾à»çÇ×[ÇÊ¼ö ¾Æ´Ô] : YYYY-MM-DD");
		while (true) {
			System.out.print(message);
			bDate = sc.nextLine();

			if (bDate == null || bDate.length() == 0)
				return null;

			if (validCheck(bDate, regExpDate) == false) {
				System.out.println("³â¿ùÀÏ(YYYY-MM-DD) Çü½ÄÀ» ÁöÄÑÁÖ½Ê½Ã¿À.");
				System.out.print("´Ù½Ã ÀÔ·ÂÇÏ½Ã°Ú½À´Ï±î?(Y/N) : ");
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
	
	// ¼öÁ¤ ÇÊ¿ä
	private boolean decisionNullInput(String param, AccountDTO dto, Scanner sc) {
		boolean ret = false;
		while (true) {
			System.out.println("ÀÔ·Â °ªÀÌ ¾ø½À´Ï´Ù. ÀÌ´ë·Î Àû¿ëÇÏ½Ã°Ú½À´Ï±î?(Y/N) : ");
			String input = sc.nextLine().toUpperCase();
			if (input.equals("Y")) {
				if(param.equals("address")) dto.setAddress(null); 
				else if(param.equals("birth_date")) dto.setBirth_date(null);
				else if(param.equals("job")) dto.setJob(null);
				else if(param.equals("sex")) dto.setSex(null);
				ret = true;
				break;
			} else if(input.equals("N")) {
				// ¼öÁ¤ ÇÊ¿ä
				break;
			} else {
				System.out.println("N ¶Ç´Â Y¸¦ ÀÔ·ÂÇÏ¼¼¿ä.");
			}
		}
		return ret;
	}
}
