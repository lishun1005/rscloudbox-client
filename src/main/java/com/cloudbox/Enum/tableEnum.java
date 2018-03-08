package com.cloudbox.Enum;


/**
 * @Description 数据类型与表的枚举函数
 * */
public enum tableEnum {
	productOriginal("0101","原始影像","rscm_area_image", "originalimageresultList"),productOriginal2("0102","正射影像","rscm_area_image", "orthoimageresultList");
	private tableEnum(String productsourceTypeCode,String productsourceType, String tablename, String HtmltableInit) {
		this.productsourceType=productsourceType;
		this.tablename=tablename;
		this.HtmltableInit=HtmltableInit;
		this.productsourceTypeCode=productsourceTypeCode;
	}
    public static tableEnum gettableEnum(String productsourceType) {
    	for (tableEnum e1 : tableEnum .values()) { 
    		if (e1.getProductsourceType().equals(productsourceType)) {
    			return e1;
			}
    	}
		return null;
	}
	private String productsourceType;
	private String tablename;
	private String HtmltableInit;
	private String productsourceTypeCode;

	public String getProductsourceType() {
		return productsourceType;
	}

	public void setProductsourceType(String productsourceType) {
		this.productsourceType = productsourceType;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getHtmltableInit() {
		return HtmltableInit;
	}

	public void setHtmltableInit(String htmltableInit) {
		HtmltableInit = htmltableInit;
	}
	
	public synchronized String getProductsourceTypeCode() {
		return productsourceTypeCode;
	}
	
	public synchronized void setProductsourceTypeCode(String productsourceTypeCode) {
		this.productsourceTypeCode = productsourceTypeCode;
	}
	
	public static void main(String[] args) {
		tableEnum aa = tableEnum.gettableEnum("原始影像");
		System.out.println(aa.getProductsourceType());
		System.out.println(aa.getHtmltableInit());
		
	}

}
