package orderlist;

import java.sql.Date;

public class OrderListDTO {
	private int registration_number;
	private String seller_id;
	private String buyer_id;
	private Date sell_date;
	
	public OrderListDTO(int registration_number, String seller_id, String buyer_id, Date sell_date) {
		this.registration_number = registration_number;
		this.seller_id = seller_id;
		this.buyer_id = buyer_id;
		this.sell_date = sell_date;
	}

	public int getRegistration_number() {
		return registration_number;
	}

	public void setRegistration_number(int registration_number) {
		this.registration_number = registration_number;
	}

	public String getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}

	public String getBuyer_id() {
		return buyer_id;
	}

	public void setBuyer_id(String buyer_id) {
		this.buyer_id = buyer_id;
	}

	public Date getSell_date() {
		return sell_date;
	}

	public void setSell_date(Date sell_date) {
		this.sell_date = sell_date;
	}
	
	
}
