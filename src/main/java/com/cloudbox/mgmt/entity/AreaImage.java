package com.cloudbox.mgmt.entity;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "rscm_area_image")
public class AreaImage{
	private String id;
	private String areaNo;
	private String imageRowCol;
	private String imageSatelliteType;
	private Double imageStartResolution;
	private String imageSpectrumType;
	private String beginTime;
	private String updateTime;
	private String gemo;
	private String range;
	private Integer num;
	private Integer isCover;
	private String dataId;
	private String imageProductType;
	private String relationNo;
	private String name;
	private Double imageCloudage;
	private String sensorId;
	private String productLevel;
	private String collectStartTime;
	private String filePath;
	private String productId;
	private Boolean iscorrect;
	private String jobid;
	private String areaNoArray;
	private String imgUrl;
	private String imageArea;
	private String recordId;
	private String collectEndTime;
	private Double imageEndResolution;
	private String tip;
	private String srid;
	private String areaDescription;
	private Long imageSize;
	private Integer autoTag;

	@Column(name = "id")
	public String getId(){
		return id; 
	}
	public  void setId(String id){
		this.id=id; 
	}

	@Column(name = "area_no")
	public String getAreaNo(){
		return areaNo; 
	}
	public  void setAreaNo(String areaNo){
		this.areaNo=areaNo; 
	}

	@Column(name = "image_row_col")
	public String getImageRowCol(){
		return imageRowCol; 
	}
	public  void setImageRowCol(String imageRowCol){
		this.imageRowCol=imageRowCol; 
	}

	@Column(name = "image_satellite_type")
	public String getImageSatelliteType(){
		return imageSatelliteType; 
	}
	public  void setImageSatelliteType(String imageSatelliteType){
		this.imageSatelliteType=imageSatelliteType; 
	}

	@Column(name = "image_start_resolution")
	public Double getImageStartResolution(){
		return imageStartResolution; 
	}
	public  void setImageStartResolution(Double imageStartResolution){
		this.imageStartResolution=imageStartResolution; 
	}

	@Column(name = "image_spectrum_type")
	public String getImageSpectrumType(){
		return imageSpectrumType; 
	}
	public  void setImageSpectrumType(String imageSpectrumType){
		this.imageSpectrumType=imageSpectrumType; 
	}

	@Column(name = "begin_time")
	public String getBeginTime(){
		return beginTime; 
	}
	public  void setBeginTime(String beginTime){
		this.beginTime=beginTime; 
	}

	@Column(name = "update_time")
	public String getUpdateTime(){
		return updateTime; 
	}
	public  void setUpdateTime(String updateTime){
		this.updateTime=updateTime; 
	}

	@Column(name = "gemo")
	public String getGemo(){
		return gemo; 
	}
	public  void setGemo(String gemo){
		this.gemo=gemo; 
	}

	@Column(name = "range")
	public String getRange(){
		return range; 
	}
	public  void setRange(String range){
		this.range=range; 
	}

	@Column(name = "num")
	public Integer getNum(){
		return num; 
	}
	public  void setNum(Integer num){
		this.num=num; 
	}

	@Column(name = "is_cover")
	public Integer getIsCover(){
		return isCover; 
	}
	public  void setIsCover(Integer isCover){
		this.isCover=isCover; 
	}

	@Column(name = "data_id")
	public String getDataId(){
		return dataId; 
	}
	public  void setDataId(String dataId){
		this.dataId=dataId; 
	}

	@Column(name = "image_product_type")
	public String getImageProductType(){
		return imageProductType; 
	}
	public  void setImageProductType(String imageProductType){
		this.imageProductType=imageProductType; 
	}

	@Column(name = "relation_no")
	public String getRelationNo(){
		return relationNo; 
	}
	public  void setRelationNo(String relationNo){
		this.relationNo=relationNo; 
	}

	@Column(name = "name")
	public String getName(){
		return name; 
	}
	public  void setName(String name){
		this.name=name; 
	}

	@Column(name = "image_cloudage")
	public Double getImageCloudage(){
		return imageCloudage; 
	}
	public  void setImageCloudage(Double imageCloudage){
		this.imageCloudage=imageCloudage; 
	}

	@Column(name = "sensor_id")
	public String getSensorId(){
		return sensorId; 
	}
	public  void setSensorId(String sensorId){
		this.sensorId=sensorId; 
	}

	@Column(name = "product_level")
	public String getProductLevel(){
		return productLevel; 
	}
	public  void setProductLevel(String productLevel){
		this.productLevel=productLevel; 
	}

	@Column(name = "collect_start_time")
	public String getCollectStartTime(){
		return collectStartTime; 
	}
	public  void setCollectStartTime(String collectStartTime){
		this.collectStartTime=collectStartTime; 
	}

	@Column(name = "file_path")
	public String getFilePath(){
		return filePath; 
	}
	public  void setFilePath(String filePath){
		this.filePath=filePath; 
	}

	@Column(name = "product_id")
	public String getProductId(){
		return productId; 
	}
	public  void setProductId(String productId){
		this.productId=productId; 
	}

	@Column(name = "iscorrect")
	public Boolean getIscorrect(){
		return iscorrect; 
	}
	public  void setIscorrect(Boolean iscorrect){
		this.iscorrect=iscorrect; 
	}

	@Column(name = "jobid")
	public String getJobid(){
		return jobid; 
	}
	public  void setJobid(String jobid){
		this.jobid=jobid; 
	}

	@Column(name = "area_no_array")
	public String getAreaNoArray(){
		return areaNoArray; 
	}
	public  void setAreaNoArray(String areaNoArray){
		this.areaNoArray=areaNoArray; 
	}

	@Column(name = "img_url")
	public String getImgUrl(){
		return imgUrl; 
	}
	public  void setImgUrl(String imgUrl){
		this.imgUrl=imgUrl; 
	}

	@Column(name = "image_area")
	public String getImageArea(){
		return imageArea; 
	}
	public  void setImageArea(String imageArea){
		this.imageArea=imageArea; 
	}

	@Column(name = "record_id")
	public String getRecordId(){
		return recordId; 
	}
	public  void setRecordId(String recordId){
		this.recordId=recordId; 
	}

	@Column(name = "collect_end_time")
	public String getCollectEndTime(){
		return collectEndTime; 
	}
	public  void setCollectEndTime(String collectEndTime){
		this.collectEndTime=collectEndTime; 
	}

	@Column(name = "image_end_resolution")
	public Double getImageEndResolution(){
		return imageEndResolution; 
	}
	public  void setImageEndResolution(Double imageEndResolution){
		this.imageEndResolution=imageEndResolution; 
	}

	@Column(name = "tip")
	public String getTip(){
		return tip; 
	}
	public  void setTip(String tip){
		this.tip=tip; 
	}

	@Column(name = "srid")
	public String getSrid(){
		return srid; 
	}
	public  void setSrid(String srid){
		this.srid=srid; 
	}

	@Column(name = "area_description")
	public String getAreaDescription(){
		return areaDescription; 
	}
	public  void setAreaDescription(String areaDescription){
		this.areaDescription=areaDescription; 
	}

	@Column(name = "image_size")
	public Long getImageSize(){
		return imageSize; 
	}
	public  void setImageSize(Long imageSize){
		this.imageSize=imageSize; 
	}

	@Column(name = "auto_tag")
	public Integer getAutoTag(){
		return autoTag; 
	}
	public  void setAutoTag(Integer autoTag){
		this.autoTag=autoTag; 
	}

}	