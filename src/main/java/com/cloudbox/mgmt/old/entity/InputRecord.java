package com.cloudbox.mgmt.old.entity;


import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * @Description 入库记录表
 */
public class InputRecord implements Serializable {
	private static final long serialVersionUID = -945846212358480084L;
	private String id;
	private Long downloadsize;
	private int opstatus;
	private Timestamp starttime;
	private String recordid;
	private String message;
	private String dataname;
	private Timestamp endtime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getDownloadsize() {
		return downloadsize;
	}

	public void setDownloadsize(Long downloadsize) {
		this.downloadsize = downloadsize;
	}

	public int getOpstatus() {
		return opstatus;
	}

	public void setOpstatus(int opstatus) {
		this.opstatus = opstatus;
	}

	public Timestamp getStarttime() {
		return starttime;
	}

	public void setStarttime(Timestamp starttime) {
		this.starttime = starttime;
	}

	public String getRecordid() {
		return recordid;
	}

	public void setRecordid(String recordid) {
		this.recordid = recordid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDataname() {
		return dataname;
	}

	public void setDataname(String dataname) {
		this.dataname = dataname;
	}

	public Timestamp getEndtime() {
		return endtime;
	}

	public void setEndtime(Timestamp endtime) {
		this.endtime = endtime;
	}
}
