package page;

import java.util.ArrayList;

import vehicle.BasicVehicleInfoDTO;

public class Pagination {
	private int totalPage;
	private int totalList;
	private int startIdx;
	private int lastIdx;
	private int curPage;
	private int pageSize = 50;
	
	public Pagination(int list_size) {
		this.totalPage = (int)Math.ceil(list_size*1.0/pageSize);
		this.totalList = list_size;
		this.curPage = 1;
		this.startIdx = 0;
		if(this.totalPage == 1)
			this.lastIdx = list_size;
		else
			this.lastIdx = this.pageSize;
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

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pagesize) {
		this.pageSize = pagesize;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
	public boolean nextPage() {
		if(this.curPage == this.totalPage)
			return false;
		
		this.setCurPage(this.getCurPage() + 1);
		this.setStartIdx(this.getStartIdx() + this.getPageSize());
		
		if(this.getCurPage() == this.getTotalPage()) {
			this.setLastIdx(this.getTotalList());
		} else
			this.setLastIdx(this.getLastIdx() + this.getPageSize());
		return true;
	}
	public boolean prevPage() {
		if(this.curPage == 1)
			return false;
		
		
		if(this.getCurPage() == this.getTotalPage()) {
			this.setLastIdx(this.getStartIdx());
		} else {
			this.setLastIdx(this.getLastIdx() - this.getPageSize());
		}
		
		this.setCurPage(this.getCurPage() - 1);
		
		if(this.getCurPage() == 1) {
			this.setStartIdx(0);
		} else {
			this.setStartIdx(this.getStartIdx() - this.getPageSize());
		}
		return true;
	}
}
