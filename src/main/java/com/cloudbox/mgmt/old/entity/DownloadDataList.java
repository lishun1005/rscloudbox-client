package com.cloudbox.mgmt.old.entity;


/**
 * 申请人信息表
 * @author lishun
 *
 */
public class DownloadDataList {

	private String id;
	private String examination_approval_record_id;
	private String file_name;
	private String image_product_type;
	private Integer op_status;
	private Long downloadsize;
	private String range;
	private String record_id;
	private Integer auto_tag;
	public Integer getAuto_tag() {
		return auto_tag;
	}
	public void setAuto_tag(Integer auto_tag) {
		this.auto_tag = auto_tag;
	}
	public String getRecord_id() {
		return record_id;
	}
	public void setRecord_id(String record_id) {
		this.record_id = record_id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImage_product_type() {
		return image_product_type;
	}
	public void setImage_product_type(String image_product_type) {
		this.image_product_type = image_product_type;
	}
	public String getExamination_approval_record_id() {
		return examination_approval_record_id;
	}
	public void setExamination_approval_record_id(
			String examination_approval_record_id) {
		this.examination_approval_record_id = examination_approval_record_id;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public Integer getOp_status() {
		return op_status;
	}
	public void setOp_status(Integer op_status) {
		this.op_status = op_status;
	}
	public Long getDownloadsize() {
		return downloadsize;
	}
	public void setDownloadsize(Long downloadsize) {
		this.downloadsize = downloadsize;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getFile_directory() {
		return file_directory;
	}
	public void setFile_directory(String file_directory) {
		this.file_directory = file_directory;
	}
	private String file_directory;

}
