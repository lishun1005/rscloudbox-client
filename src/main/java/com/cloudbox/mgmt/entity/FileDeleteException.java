package com.cloudbox.mgmt.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
@Table(name = "file_delete_exception")
public class FileDeleteException{
	private String id;
	private String downloadPath;
	private String imageName;
	private Integer status;
	private Date createDt;
	private Date updateDt;

	@Id
	public String getId(){
		return id; 
	}
	public  void setId(String id){
		this.id=id; 
	}

	@Column(name = "download_path")
	public String getDownloadPath(){
		return downloadPath; 
	}
	public  void setDownloadPath(String downloadPath){
		this.downloadPath=downloadPath; 
	}

	@Column(name = "image_name")
	public String getImageName(){
		return imageName; 
	}
	public  void setImageName(String imageName){
		this.imageName=imageName; 
	}

	@Column(name = "status")
	public Integer getStatus(){
		return status; 
	}
	public  void setStatus(Integer status){
		this.status=status; 
	}

	@Column(name = "create_dt")
	public Date getCreateDt(){
		return createDt; 
	}
	public  void setCreateDt(Date createDt){
		this.createDt=createDt; 
	}

	@Column(name = "update_dt")
	public Date getUpdateDt(){
		return updateDt; 
	}
	public  void setUpdateDt(Date updateDt){
		this.updateDt=updateDt; 
	}

}
