package com.cloudbox.table;


import java.io.IOException;
import java.util.Properties;

//import java.io.IOException;
//import java.util.Properties;
//
//import zkyg.util.MailHelper;

/**
 * 系统常用常量参数，本类存放静态不可修改的常量；如果是随环境影响经常更改的常量，请放在system.propertis中
 * 命名规范：全部大写，单词间用下划线隔开。
 * 
 * @author gongzk
 * 
 */
public final class Constant {
	/** *********** 云盒数据管理系统基础数据表 ******************* */
	public static String rsclouds_area_table = "admin123_simple_all";
	
	public static String rsclouds_producttype_table = "producttype";

	public static String rsclouds_rscm_area_image_table = "rscm_area_image";
	
	public static String applicant_information = "applicant_information";
	public static String download_data_list = "download_data_list";
	public static String examination_approval_records = "examination_approval_records";
	
	public static String applicant_data_list = "applicant_data_list";
	/** *********** 系统基础数据 ******************* */
	// 前端普通用户在session中的名称
	public static String Subscription = "subscriptions";

	public static String ImageData = "image_data";

	public static String SubsImageDataRelationship = "subs_image_relationship";

	// 前端普通用户在session中的对象
	public static String SESSION_USERID = "session_userid";

	public static String SESSION_USERNAME = "session_username";

	public static String SESSION_ID = "session_id";

	public static String SESSION_Token = "session_Token ";

	public static String SESSION_MessageCode = "MessageCode";

	public static String imageAttention_table = "\"imageAttention\"";

	public static String rsmarketnews_table = "\"rsmarketnews\"";
	
	public static String ChinaRSServerURL = "";
	
	public static String DownLoadRecord = "downloadrecord";
	
	public static String InputRecord = "inputrecord";
	

}
