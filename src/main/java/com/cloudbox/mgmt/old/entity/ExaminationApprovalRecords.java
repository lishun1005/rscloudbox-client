package com.cloudbox.mgmt.old.entity;


import java.util.Date;

import javax.persistence.Column;
/**
 * 审批信息表
 * @author lishun
 *
 */
public class ExaminationApprovalRecords {
	private String id;
	private Integer purpose;
	private Integer examination_approval_status;
	private Integer applicant_submit;
	private String applicant_information_id;
	private String applicant_data_list_id;
	private Date examination_approval_time;
	private Date applicant_time;

	@Column(name = "id")
	public String getId(){
		return id; 
	}
	public  void setId(String id){
		this.id=id; 
	}

	@Column(name = "purpose")
	public Integer getPurpose(){
		return purpose; 
	}
	public  void setPurpose(Integer purpose){
		this.purpose=purpose; 
	}
	public Integer getExamination_approval_status() {
		return examination_approval_status;
	}
	public void setExamination_approval_status(Integer examination_approval_status) {
		this.examination_approval_status = examination_approval_status;
	}
	public Integer getApplicant_submit() {
		return applicant_submit;
	}
	public void setApplicant_submit(Integer applicant_submit) {
		this.applicant_submit = applicant_submit;
	}
	public String getApplicant_information_id() {
		return applicant_information_id;
	}
	public void setApplicant_information_id(String applicant_information_id) {
		this.applicant_information_id = applicant_information_id;
	}
	public String getApplicant_data_list_id() {
		return applicant_data_list_id;
	}
	public void setApplicant_data_list_id(String applicant_data_list_id) {
		this.applicant_data_list_id = applicant_data_list_id;
	}
	public Date getExamination_approval_time() {
		return examination_approval_time;
	}
	public void setExamination_approval_time(Date examination_approval_time) {
		this.examination_approval_time = examination_approval_time;
	}
	public Date getApplicant_time() {
		return applicant_time;
	}
	public void setApplicant_time(Date applicant_time) {
		this.applicant_time = applicant_time;
	}
	
}
