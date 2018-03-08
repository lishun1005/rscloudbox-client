package com.cloudbox.mgmt.old.entity;


import javax.persistence.Column;
/**
 * 数据清单表
 * @author lishun
 *
 */
public class ApplicantDataList {
	private String id;
	private String data_type;
	private Long num;
	private String area;
	private Double unit_price;
	private String image_ids_list;
	private Integer op_status;
	private String sales_area;
	
	public Integer getOp_status() {
		return op_status;
	}
	public void setOp_status(Integer op_status) {
		this.op_status = op_status;
	}
	@Column(name = "id")
	public String getId(){
		return id; 
	}
	public  void setId(String id){
		this.id=id; 
	}
	public String getData_type() {
		return data_type;
	}
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}
	public Long getNum() {
		return num;
	}
	public void setNum(Long num) {
		this.num = num;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public Double getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(Double unit_price) {
		this.unit_price = unit_price;
	}
	public String getImage_ids_list() {
		return image_ids_list;
	}
	public void setImage_ids_list(String image_ids_list) {
		this.image_ids_list = image_ids_list;
	}
	public String getSales_area() {
		return sales_area;
	}
	public void setSales_area(String sales_area) {
		this.sales_area = sales_area;
	}

	
}
