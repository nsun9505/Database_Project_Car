package account;

import java.sql.Date;

public class AccountDTO {
	private String id;					// 필수
	private String pw;					// 필수
	private String name;				// 필수
	private String phone_num;			// 필수
	private String address;
	private Date birth_date;
	private String sex;
	private String job;
	private String account_type;
	
	public AccountDTO(String id, String pw, String name, String phone_num, String addr, Date bDate, String sex, String job, String type) {
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.phone_num = phone_num;
		this.address = addr;
		this.birth_date = bDate;
		this.sex = sex;
		this.job = job;
		this.account_type = type;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone_num() {
		return phone_num;
	}
	public void setPhone_num(String phone_num) {
		this.phone_num = phone_num;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getBirth_date() {
		return birth_date;
	}
	public void setBirth_date(Date birth_date) {
		this.birth_date = birth_date;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getAccount_type() {
		return account_type;
	}
	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}
}
