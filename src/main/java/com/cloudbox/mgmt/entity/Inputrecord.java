package com.cloudbox.mgmt.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
@Table(name="inputrecord")
public class Inputrecord {
	private Integer id;
	private Long downloadsize;
	private Integer opstatus;
	private Date starttime;
	private String recordid;
	private String message;
	private String dataname;
	private Date endtime;
	private Boolean isorthoimage;

	@Id
	public Integer getId(){
		return id; 
	}
	public  void setId(Integer id){
		this.id=id; 
	}

	@Column(name = "downloadsize")
	public Long getDownloadsize(){
		return downloadsize; 
	}
	public  void setDownloadsize(Long downloadsize){
		this.downloadsize=downloadsize; 
	}

	@Column(name = "opstatus")
	public Integer getOpstatus(){
		return opstatus; 
	}
	public  void setOpstatus(Integer opstatus){
		this.opstatus=opstatus; 
	}

	@Column(name = "starttime")
	public Date getStarttime(){
		return starttime; 
	}
	public  void setStarttime(Date starttime){
		this.starttime=starttime; 
	}

	@Column(name = "recordid")
	public String getRecordid(){
		return recordid; 
	}
	public  void setRecordid(String recordid){
		this.recordid=recordid; 
	}

	@Column(name = "message")
	public String getMessage(){
		return message; 
	}
	public  void setMessage(String message){
		this.message=message; 
	}

	@Column(name = "dataname")
	public String getDataname(){
		return dataname; 
	}
	public  void setDataname(String dataname){
		this.dataname=dataname; 
	}

	@Column(name = "endtime")
	public Date getEndtime(){
		return endtime; 
	}
	public  void setEndtime(Date endtime){
		this.endtime=endtime; 
	}

	@Column(name = "isorthoimage")
	public Boolean getIsorthoimage(){
		return isorthoimage; 
	}
	public  void setIsorthoimage(Boolean isorthoimage){
		this.isorthoimage=isorthoimage; 
	}

}
