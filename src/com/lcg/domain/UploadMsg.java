package com.lcg.domain;

public class UploadMsg {
	private String per;//�ϴ��ٷֱ�
	private String speed;//�ϴ��ٶ�
	private String ltime;//�ϴ�ʣ��ʱ��
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
