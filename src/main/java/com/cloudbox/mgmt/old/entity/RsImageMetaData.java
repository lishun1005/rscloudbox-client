package com.cloudbox.mgmt.old.entity;



/**
 * @author lzw 2015-12-12
 * 
 * @Description   影像信息类
 * 
 * @version V1.0
 */
public class RsImageMetaData {
	private String  id ; //标识id
	private String  area_no ; // 地区编号
	private String  image_row_col ; // 行列号
	private String  image_satellite_type ;// 卫星
	private double  image_start_resolution ; // 开始分辨率
	private double  image_end_resolution ; // 结束分辨率
	private String  image_spectrum_type ; // 光谱
	private String  begin_time ;
	private String  update_time ; // 更新时间
	private String  gemo ; // 切图范围
	private String  range ; // 数据范围
	private Integer num ; // 影像数量
	private Integer is_cover ; // 是否全覆盖
	private String  image_area ; // 面积
	private String  data_id ; // 影像id
	private String  image_product_type ; // 数据类型（0101:原始数据;0102:正射影像）
	private String  relation_no ; // 组合数据id
	private String  name ; // 名称
	private double  image_cloudage ; // 云量
	private String  sensor_id;//传感器类型
	private String  product_level;// 影像级别
	private String  collect_start_time;//采集时间
	private String  collect_end_time;//采集时间
	private String  Product_id;//产品序列号
	private String  file_path;//XML路径
	private String  area_no_array;
	private String  jobId;
	private boolean iscorrect;
	private String  img_url;
	private String  srid;
	private String  record_id;
	private String area_description;
	private String tip;
	
	private Integer auto_tag;
	public Integer getAuto_tag() {
		return auto_tag;
	}
	public void setAuto_tag(Integer auto_tag) {
		this.auto_tag = auto_tag;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getArea_no() {
		return area_no;
	}
	public void setArea_no(String area_no) {
		this.area_no = area_no;
	}
	public String getImage_row_col() {
		return image_row_col;
	}
	public void setImage_row_col(String image_row_col) {
		this.image_row_col = image_row_col;
	}
	public String getImage_satellite_type() {
		return image_satellite_type;
	}
	public void setImage_satellite_type(String image_satellite_type) {
		this.image_satellite_type = image_satellite_type;
	}
	
	public String getImage_spectrum_type() {
		return image_spectrum_type;
	}
	public void setImage_spectrum_type(String image_spectrum_type) {
		this.image_spectrum_type = image_spectrum_type;
	}
	public String getBegin_time() {
		return begin_time;
	}
	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}
	
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getGemo() {
		return gemo;
	}
	public void setGemo(String gemo) {
		this.gemo = gemo;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getIs_cover() {
		return is_cover;
	}
	public void setIs_cover(Integer is_cover) {
		this.is_cover = is_cover;
	}
	
	public synchronized String getImage_area() {
		return image_area;
	}
	public synchronized void setImage_area(String image_area) {
		this.image_area = image_area;
	}
	public String getData_id() {
		return data_id;
	}
	public void setData_id(String data_id) {
		this.data_id = data_id;
	}
	public String getImage_product_type() {
		return image_product_type;
	}
	public void setImage_product_type(String image_product_type) {
		this.image_product_type = image_product_type;
	}
	public String getRelation_no() {
		return relation_no;
	}
	public void setRelation_no(String relation_no) {
		this.relation_no = relation_no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public  double getImage_cloudage() {
		return image_cloudage;
	}
	public  void setImage_cloudage(double image_cloudage) {
		this.image_cloudage = image_cloudage;
	}
	public String getSensor_id() {
		return sensor_id;
	}
	public void setSensor_id(String sensor_id) {
		this.sensor_id = sensor_id;
	}
	public String getProduct_level() {
		return product_level;
	}
	public void setProduct_level(String product_level) {
		this.product_level = product_level;
	}
	public String getCollect_start_time() {
		return collect_start_time;
	}
	public void setCollect_start_time(String collect_start_time) {
		this.collect_start_time = collect_start_time;
	}
	public String getCollect_end_time() {
		return collect_end_time;
	}
	public void setCollect_end_time(String collect_end_time) {
		this.collect_end_time = collect_end_time;
	}
	public String getProduct_id() {
		return Product_id;
	}
	public void setProduct_id(String product_id) {
		Product_id = product_id;
	}
	public String getFile_path() {
		return file_path;
	}
	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}
	public double getImage_start_resolution() {
		return image_start_resolution;
	}
	public void setImage_start_resolution(double image_start_resolution) {
		this.image_start_resolution = image_start_resolution;
	}
	public double getImage_end_resolution() {
		return image_end_resolution;
	}
	public void setImage_end_resolution(double image_end_resolution) {
		this.image_end_resolution = image_end_resolution;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public  String getArea_no_array() {
		return area_no_array;
	}
	public  void setArea_no_array(String area_no_array) {
		this.area_no_array = area_no_array;
	}
	public synchronized boolean isIscorrect() {
		return iscorrect;
	}
	public synchronized void setIscorrect(boolean iscorrect) {
		this.iscorrect = iscorrect;
	}
	public synchronized String getImg_url() {
		return img_url;
	}
	public synchronized void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	public String getRecord_id() {
		return record_id;
	}
	public void setRecord_id(String record_id) {
		this.record_id = record_id;
	}
	public String getSrid() {
		return srid;
	}
	public void setSrid(String srid) {
		this.srid = srid;
	}
	public String getArea_description() {
		return area_description;
	}
	public void setArea_description(String area_description) {
		this.area_description = area_description;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
}
