package com.lcg.domain;

public class UploadMsg {
	private String per;//上传百分比
	private String speed;//上传速度
	private String ltime;//上传剩余时间
	public String getPer() {
		return per;
	}
	public void setPer(String per) {
		this.per = per;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public String getLtime() {
		return ltime;
	}
	public void setLtime(String ltime) {
		this.ltime = ltime;
	}
	@Override
	public String toString() {
		return "{per:"+per+",speed:"+speed+",ltime:"+ltime+"}";
	}
}
