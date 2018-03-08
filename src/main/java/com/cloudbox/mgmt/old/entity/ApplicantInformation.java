package com.cloudbox.mgmt.old.entity;


import javax.persistence.Column;
/**
 * 申请人信息表
 * @author lishun
 *
 */
public class ApplicantInformation {
	private String applicant_name;
	private String customer_name;
	private String customer_contacter;
	private String contract_sales_area;
	private String customer_contact;
	private String contract_scanning_path;
	private String invoice_scanning_path;
	private String id;
	private String bbox;
	private Integer price_type;
	private String bbox_agent;
	
	public String getBbox() {
		return bbox;
	}
	public void setBbox(String bbox) {
		this.bbox = bbox;
	}
	public String getApplicant_name() {
		return applicant_name;
	}
	public void setApplicant_name(String applicant_name) {
		this.applicant_name = applicant_name;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getCustomer_contacter() {
		return customer_contacter;
	}
	public void setCustomer_contacter(String customer_contacter) {
		this.customer_contacter = customer_contacter;
	}
	public String getContract_sales_area() {
		return contract_sales_area;
	}
	public void setContract_sales_area(String contract_sales_area) {
		this.contract_sales_area = contract_sales_area;
	}
	public String getCustomer_contact() {
		return customer_contact;
	}
	public void setCustomer_contact(String customer_contact) {
		this.customer_contact = customer_contact;
	}
	public String getContract_scanning_path() {
		return contract_scanning_path;
	}
	public void setContract_scanning_path(String contract_scanning_path) {
		this.contract_scanning_path = contract_scanning_path;
	}
	public String getInvoice_scanning_path() {
		return invoice_scanning_path;
	}
	public void setInvoice_scanning_path(String invoice_scanning_path) {
		this.invoice_scanning_path = invoice_scanning_path;
	}
	@Column(name = "id")
	public String getId(){
		return id; 
	}
	public  void setId(String id){
		this.id=id; 
	}
	public Integer getPrice_type() {
		return price_type;
	}
	public void setPrice_type(Integer price_type) {
		this.price_type = price_type;
	}
	public String getBbox_agent() {
		return bbox_agent;
	}
	public void setBbox_agent(String bbox_agent) {
		this.bbox_agent = bbox_agent;
	}

}
