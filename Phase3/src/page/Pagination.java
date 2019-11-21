package page;

import java.util.ArrayList;

import vehicle.BasicVehicleInfoDTO;

public class Pagination {
	private int totalPage;
	private int totalList;
	private int startIdx;
	private int lastIdx;
	private int pagesize = 100;
	
	public Pagination(int list_size) {
		this.totalPage = (int)Math.ceil(list_size*1.0/pagesize);
		this.totalList = list_size;
		this.startIdx = 1;
		this.lastIdx = this.getTotalPage();
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalList() {
		return totalList;
	}

	public void setTotalList(int totalList) {
		this.totalList = totalList;
	}

	public int getStartIdx() {
		return startIdx;
	}

	public void setStartIdx(int startIdx) {
		this.startIdx = startIdx;
	}

	public int getLastIdx() {
		return lastIdx;
	}

	public void setLastIdx(int lastIdx) {
		this.lastIdx = lastIdx;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	
}
