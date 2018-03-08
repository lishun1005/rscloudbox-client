package com.cloudbox.mgmt.entity;


import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 地区信息类
 * */
@Table(name = "admin123_simple_all")
public class Area{
	
	private String admincode;
	private String py;
	private String adminname;
	private String proname;
	private String cityadminname;
	private String population;
	private String pointGeom;
	private String lineGeom;
	private String geom;
	private String classs;

	@Column(name = "admincode")
	public String getAdmincode(){
		return admincode; 
	}
	public  void setAdmincode(String admincode){
		this.admincode=admincode; 
	}

	@Column(name = "py")
	public String getPy(){
		return py; 
	}
	public  void setPy(String py){
		this.py=py; 
	}

	@Column(name = "adminname")
	public String getAdminname(){
		return adminname; 
	}
	public  void setAdminname(String adminname){
		this.adminname=adminname; 
	}

	@Column(name = "proname")
	public String getProname(){
		return proname; 
	}
	public  void setProname(String proname){
		this.proname=proname; 
	}

	@Column(name = "cityadminname")
	public String getCityadminname(){
		return cityadminname; 
	}
	public  void setCityadminname(String cityadminname){
		this.cityadminname=cityadminname; 
	}

	@Column(name = "population")
	public String getPopulation(){
		return population; 
	}
	public  void setPopulation(String population){
		this.population=population; 
	}

	@Column(name = "point_geom")
	public String getPointGeom(){
		return pointGeom; 
	}
	public  void setPointGeom(String pointGeom){
		this.pointGeom=pointGeom; 
	}

	@Column(name = "line_geom")
	public String getLineGeom(){
		return lineGeom; 
	}
	public  void setLineGeom(String lineGeom){
		this.lineGeom=lineGeom; 
	}

	@Column(name = "geom")
	public String getGeom(){
		return geom; 
	}
	public  void setGeom(String geom){
		this.geom=geom; 
	}

	@Column(name = "class")
	public String getClasss(){
		return classs; 
	}
	public  void setClass(String classs){
		this.classs=classs; 
	}

}
