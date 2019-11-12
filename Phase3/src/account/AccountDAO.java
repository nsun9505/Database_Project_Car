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

	// ÀÏ¹İ °í°´ È¸¿ø °¡ÀÔ
	public void joinAccount() {
		Scanner sc = new Scanner(System.in);
		System.out.println("<<<È¸¿ø°¡ÀÔ>>>");

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
				System.out.println("È¸¿ø °¡ÀÔ ¼º°ø!");
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
		// ¾ÆÀÌµğ
		System.out.print("¾ÆÀÌµğ(5~15ÀÚ¸®, Æ¯¼ö¹®ÀÚ X, ¿µ¾î ´ë¼Ò¹®ÀÚ¿Í ¼ıÀÚ °áÇÕ)");
		while (true) {
			System.out.print("¾ÆÀÌµğ ÀÔ·Â : ");
			id = sc.nextLine();

			if (id.matches(regExp) == false) {
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
	
	private String getInputPasswd(Scanner sc) {
		String pw = null;
		String regExp ="^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,15}$";
		
		System.out.println("ºñ¹Ğ¹øÈ£´Â ¿µ¾î, ¼ıÀÚ, Æ¯¼ö¹®ÀÚ[$@!%*#] 1°³ ÀÌ»óÀ» Á¶ÇÕÇÑ 8~15ÀÚ¸® ÀÔ´Ï´Ù.");
		// ºñ¹Ğ¹øÈ£ È­ÀÎ
		while (true) {
			System.out.print("ºñ¹Ğ¹øÈ£ ÀÔ·Â : ");
			pw = sc.nextLine();
			
			// ºñ¹Ğ¹øÈ£ °Ë»ç : ¿µ¾î, ¼ıÀÚ, Æ¯¼ö¹®ÀÚ¸¦ Æ÷ÇÔÇØ¾ß ÇÔ.
			if(pw.matches(regExp) == false) {
				System.out.println("ºñ¹Ğ¹øÈ£´Â ¿µ¾î, ¼ıÀÚ, Æ¯¼ö¹®ÀÚ[$@!%*#] 1°³ ÀÌ»óÀ» Æ÷ÇÔÇØ¾ß ÇÕ´Ï´Ù.");
				continue;
			}
			
			System.out.print("ºñ¹Ğ¹øÈ£ È®ÀÎ : ");
			String pwConfirm = sc.nextLine();

			// ÀÔ·ÂÇÑ ºñ¹Ğ¹øÈ£°¡ ¸Â´ÂÁö È®ÀÎ
			if (pw.equals(pwConfirm) == false) {
				System.out.println("ºñ¹Ğ¹øÈ£, ºñ¹Ğ¹øÈ£ È®ÀÎÀÌ ´Ù¸¨´Ï´Ù. ´Ù½Ã ÀÔ·ÂÇÏ¼¼¿ä.");
				continue;
			} else 
				break;
		}
		
		return pw;
	}

	private String getInputName(Scanner sc) {
		String regExp = "^[°¡-ÆR]{2,4}|[a-zA-Z]{2,10}\\s[a-zA-Z]{2,10}$";
		String name = null;
		// ÀÌ¸§
		while (true) {
			System.out.println("ÀÌ¸§ Á¦¾à»çÇ× : (¿µ¾î FirstName(2~10ÀÚ¸®), LastName(2~10ÀÚ¸®)");
			System.out.print("ÀÌ¸§ ÀÔ·Â : ");
			name = sc.nextLine();
			if (name.matches(regExp) == false) {
				System.out.println("ÀÌ¸§Àº ÇÊ¼ö Ç×¸ñÀÌ¸ç Á¦¾à»çÇ×À» ÁöÄÑÁÖ¼¼¿ä.");
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
		// ÇÚµåÆù ¹øÈ£
		System.out.println("ÇÚµåÆù ¹øÈ£ Á¦¾à»çÇ× : (¿¹½Ã 01012341234 or 010-1234-1234)");
		while (true) {
			System.out.print("ÇÚµåÆù ÀÔ·Â : ");
			phone_num = sc.nextLine();
			
			if (phone_num.matches(regExp) == false) {
				System.out.println("ÇÚµåÆù ¹øÈ£´Â ÇÊ¼ö Á¤º¸ÀÔ´Ï´Ù. ÀÔ·Â ºÎÅ¹µå¸³´Ï´Ù. Çü½Ä : 01012341234 or 010-1234-1234");
				continue;
			}
			break;
		}
		return phone_num;
	}

	private String getInputAddress(Scanner sc) {
		System.out.print("ÁÖ¼Ò ÀÔ·Â[ÇÊ¼ö ¾Æ´Ô]: ");
		String address = sc.nextLine();
		if(address == null || address.length() == 0)
			return null;
		return address;
	}
	
	private String getInputSex(Scanner sc) {
		String sex = null;
		System.out.println("¼ºº° (F:¿© / M : ³²)");
		while(true) {
			System.out.print("¼ºº° ÀÔ·Â : ");
			sex = sc.nextLine().toUpperCase();
			if(sex == null || sex.length() == 0)
				return null;
			if(sex.equals("F") || sex.equals("M"))
				break;
			else
				System.out.println("¼ºº°À» ÀÔ·ÂÇÏ½Ç °æ¿ì F/f(¿©ÀÚ), M/m(³²ÀÚ)À¸·Î ÀÔ·Â ºÎÅ¹µå¸³´Ï´Ù.");
		}
		return sex;
	}

	private String getInputJob(Scanner sc) {
		System.out.print("Á÷¾÷ ÀÔ·Â[ÇÊ¼ö ¾Æ´Ô] : ");
		String job = sc.nextLine();
		if(job == null || job.length() == 0)
			return null;
		return job;
	}

	private String getInputBirthDate(Scanner sc){
		String regExp = "^[0-9][0-9][0-9][0-9]\\-[0-9][0-9]\\-[0-9][0-9]$";
		String bDate = null;
		
		System.out.println("»ıÀÏ Á¦¾à»çÇ×[ÇÊ¼ö ¾Æ´Ô] : YYYY-MM-DD");
		while (true) {
			System.out.print("»ıÀÏ ÀÔ·Â : ");
			bDate = sc.nextLine();
			
			if(bDate == null || bDate.length() == 0)
				return null;
			
			if (bDate.matches(regExp) == false) {
				System.out.println("³â¿ùÀÏ(YYYY-MM-DD) Çü½ÄÀ» ÁöÄÑÁÖ½Ê½Ã¿À.");
				continue;
			}
			else 
				break;
		}
		return bDate;
	}
	
}
