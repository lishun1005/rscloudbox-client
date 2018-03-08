package com.cloudbox.Enum;

/**
 * @Description 波段与光谱信息的枚举函数
 * */
public enum ImgBandsEnum {

	productOriginal("1,2,3,4","多光谱", "QS")
	,productOriginal2("1","全色", "")
	,productOriginal3("2","全色", "")
	,productOriginal4("3","全色", "")
	,productOriginal5("4","全色", "")
	,productOriginal6("5","全色", "")
	,productOriginal7("5","全色", "")
	,productOriginal8("2,3,4","多光谱", "QS")
	,productOriginal9("1,2,3","多光谱", "QS");
	private ImgBandsEnum(String bands, String bandsType, String bandscode) {
		this.bandsType=bandsType;
		this.bands=bands;
		this.bandscode=bandscode;
	}
	 public static ImgBandsEnum gettableEnum(String bands) {
	    	for (ImgBandsEnum e1 : ImgBandsEnum .values()) { 
	    		if (e1.getBands().equals(bands)) {
	    			return e1;
				}
	    	}
			return null;
		}
	
	
	private String bandsType;
	private String bands;
	private String bandscode;
	
	
	public synchronized String getBandsType() {
		return bandsType;
	}
	public synchronized void setBandsType(String bandsType) {
		this.bandsType = bandsType;
	}
	public synchronized String getBands() {
		return bands;
	}
	public synchronized void setBands(String bands) {
		this.bands = bands;
	}
	public synchronized String getBandscode() {
		return bandscode;
	}
	public synchronized void setBandscode(String bandscode) {
		this.bandscode = bandscode;
	}
	public static void main(String[] args) {
		
		
	}
	
	
	
	
	
	
}
