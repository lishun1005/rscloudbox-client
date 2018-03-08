package com.cloudbox.mgmt.service.impl;


import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudbox.mgmt.dao.CheckProcessRespository;
import com.cloudbox.mgmt.dao.ProductInformationRespository;
import com.cloudbox.mgmt.old.entity.ApplicantDataList;
import com.cloudbox.mgmt.old.entity.ApplicantDataListDto;
import com.cloudbox.mgmt.old.entity.ApplicantInformation;
import com.cloudbox.mgmt.old.entity.DownloadDataList;
import com.cloudbox.mgmt.old.entity.ExaminationApprovalRecords;
import com.cloudbox.mgmt.service.CheckProcessService;
import com.cloudbox.utils.Base64;
import com.cloudbox.utils.RSAEncrypt;
import com.cloudbox.utils.StringTools;
@Service("checkProcessService")
@Transactional
public class CheckProcessServiceImpl implements CheckProcessService {
	private static Logger logger = LoggerFactory.getLogger(CheckProcessServiceImpl.class);
	@Autowired
	private CheckProcessRespository checkProcessManagement;
	@Autowired
	private ProductInformationRespository productInformationManagement;
	
	

	@Value("#{bboxSystemProperties[yunHeBackManagerUrl]}")
	private String yunHeBackManagerUrl;

	@Value("#{bboxSystemProperties[yunHeLoginUrl]}")
	private String yunHeLoginUrl;

	@Value("#{bboxSystemProperties[yunHeLoginUser]}")
	private String yunHeLoginUser;

	@Value("#{bboxSystemProperties[yunHeLoginPassword]}")
	private String yunHeLoginPassword;
	
	private static String bboxName;
	static{
		try {
			bboxName = new String(RSAEncrypt.
						decrypt(RSAEncrypt.loadPublicKeyByStrDefault(), 
								Base64.decode(StringTools.getValueFromProperties("Rsmartgtcloudos", "groupUserName"))));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void addCheckProcess(ApplicantInformation applicantInformation,
			 ExaminationApprovalRecords examinationApprovalRecords,
			 ApplicantDataListDto applicantDataListDto,File contractScanningFile,
			File invoiceScanningFile,String downloadDataListIds,String applicantName) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(yunHeLoginUrl);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", yunHeLoginUser));
		params.add(new BasicNameValuePair("password", yunHeLoginPassword));

		httppost.setEntity(new UrlEncodedFormEntity(params));

		HttpResponse responsed = httpclient.execute(httppost);
		HttpEntity entity = responsed.getEntity();
		String postResult = EntityUtils.toString(entity, "UTF-8");
		CookieStore cookieStore = ((AbstractHttpClient) httpclient).getCookieStore();
		List<Cookie> cookies = ((AbstractHttpClient) httpclient)
				.getCookieStore().getCookies();
		if(cookies.size()>0){//cookie大小为0，表示登陆失败
			/*
			 *  downloadDataListIds不为空时，
			 *  则修改表download_data_list对应记录的数据状态op_status，改为4：已重新申请下载
			 *  
			 *  将examination_approval_status的值设置为4，表示该条申请是重新申请的
			 */
			if (downloadDataListIds != null && !"".equals(downloadDataListIds)) {

				String[] downloadDataListIdArr = downloadDataListIds.split(",");
				for (int i = 0; i < downloadDataListIdArr.length; i++) {

					String downloadDataListId = downloadDataListIdArr[i];
					updateDownloadDataById(downloadDataListId,4);

				}
				// 标志位设置为重新提交申请
				examinationApprovalRecords.setExamination_approval_status(4);
			} else {
				// 标志位设置为待审批
				examinationApprovalRecords.setExamination_approval_status(0);
			}
			// 添加审批信息到当前云盒客户端
			if (applicantInformation.getId() == null
					|| applicantInformation.getId().equals("")) {
				applicantInformation.setId(UUID.randomUUID().toString());
			}
			if (examinationApprovalRecords.getId() == null
					|| examinationApprovalRecords.getId().equals("")) {
				examinationApprovalRecords.setId(UUID.randomUUID().toString());
			}
			
			applicantInformation.setApplicant_name(applicantName);//设置申请人名称
			applicantInformation.setBbox(bboxName);//设置云盒名称
			examinationApprovalRecords.setApplicant_time(new Date());		
			examinationApprovalRecords
					.setApplicant_information_id(applicantInformation.getId());
			List<DownloadDataList> downloadDataList =new ArrayList<DownloadDataList>();
			addCheckProcessToClient(applicantInformation,
					examinationApprovalRecords, applicantDataListDto,downloadDataList);
			//提交审批信息到服务端
			FileBody contractScanningBody = null;
			FileBody invoiceScanningBody = null;

			if (contractScanningFile != null) {
				contractScanningBody = new FileBody(contractScanningFile);
			}
			if (invoiceScanningFile != null) {
				invoiceScanningBody = new FileBody(invoiceScanningFile);
			}
			MultipartEntity reqEntity = new MultipartEntity();
			
			JSONObject applicantDataListDtoJson = JSONObject
					.fromObject(applicantDataListDto);
			JSONArray downloadDataListJson=JSONArray.fromObject(downloadDataList);
			JSONObject applicantInformationJson = JSONObject
					.fromObject(applicantInformation);
			JSONObject examinationApprovalRecordsJson = JSONObject
					.fromObject(examinationApprovalRecords);
			httppost = new HttpPost(yunHeBackManagerUrl);
			// 添加文件
			if (contractScanningBody!=null) {
				reqEntity.addPart("contractScanning", contractScanningBody);
			}
			if (invoiceScanningBody!=null) {
				reqEntity.addPart("invoiceScanning", invoiceScanningBody);
			}
			// 添加Json串
			reqEntity.addPart(
					"applicantDataListDtoJson",
					new StringBody(applicantDataListDtoJson.toString(), Charset
							.forName("UTF-8")));
			reqEntity.addPart(
					"applicantInformationJson",
					new StringBody(applicantInformationJson.toString(), Charset
							.forName("UTF-8")));
			reqEntity.addPart("examinationApprovalRecordsJson",
					new StringBody(examinationApprovalRecordsJson.toString(),
							Charset.forName("UTF-8")));
			reqEntity.addPart(
					"downloadDataListJson",
					new StringBody(downloadDataListJson.toString(), Charset
							.forName("UTF-8")));
			httppost.setEntity(reqEntity);
			responsed = httpclient.execute(httppost);
			logger.info("提交审批信息到服务端信息"+ responsed.getEntity().getContent());
			responsed.getEntity().consumeContent();
		}else{
			String error=String.format("登陆云盒后端的用户名或密码错误{system.properties};username:%s,pwd:%s", yunHeLoginUser,yunHeLoginPassword);
			throw new Exception(error);
		}
		
	}
	@Override
	public boolean updateExaminationApprovalRecordsById(String id,Integer status,String examinationApprovalTime){
		return checkProcessManagement.updateExaminationApprovalRecordsById(id, status,examinationApprovalTime);
	}
	@Override
	public void addCheckProcessToClient(ApplicantInformation applicantInformation,
			ExaminationApprovalRecords examinationApprovalRecords,
			ApplicantDataListDto applicantDataListDto,List<DownloadDataList> downloadDataList) {
		String image_ids_list="";
		String applicant_data_list_id="{";
		for (ApplicantDataList applicantDataList : applicantDataListDto.getListDto()) {//添加数据清单
			if(applicantDataList.getId()==null||applicantDataList.getId().equals("")){
				applicantDataList.setId(UUID.randomUUID().toString());
			}
			String imageIdsList=applicantDataList.getImage_ids_list();
			image_ids_list=image_ids_list+","+imageIdsList.substring(1,imageIdsList.length()-1);//获取所有的影像信息id
			applicant_data_list_id=applicant_data_list_id+applicantDataList.getId()+",";
			checkProcessManagement.addApplicantDataList(applicantDataList);
		}
		
		String[] image_ids_listArr =image_ids_list.substring(1).split(",");
		for (String string : image_ids_listArr) {
			Map<String,Object> map=productInformationManagement.queryproductInformationById(string);
			if(map!=null){
				DownloadDataList d=new DownloadDataList();
				if(map.get("downloadsize")!=null){
					d.setDownloadsize((Long)map.get("downloadsize"));
				}
				d.setRecord_id((String)map.get("record_id"));
				d.setExamination_approval_record_id(examinationApprovalRecords.getId());
				d.setFile_directory((String)map.get("file_path"));
				d.setFile_name(map.get("downloadFilepath")+File.separator+map.get("dlName")+".tar.gz");
				d.setId(UUID.randomUUID().toString());
				d.setOp_status(0);
				d.setRange((String)map.get("range"));
				d.setAuto_tag((Integer)map.get("auto_tag"));
				d.setImage_product_type((String)map.get("image_product_type"));
				checkProcessManagement.addDownloadDataList(d);
				downloadDataList.add(d);
			}else{
				logger.info("未找到id="+string+"的rscm_area_image信息");
			}
		}
		applicant_data_list_id=applicant_data_list_id.substring(0, applicant_data_list_id.length()-1)+"}";
		examinationApprovalRecords.setApplicant_submit(0);
//		examinationApprovalRecords.setExamination_approval_status(0);
		examinationApprovalRecords.setApplicant_data_list_id(applicant_data_list_id);
		checkProcessManagement//添加添加申请人信息
			.addApplicantInformation(applicantInformation);
		checkProcessManagement//添加审批记录
			.addExaminationApprovalRecords(examinationApprovalRecords);
	}
	public List<Map<String,Object>> listCheckProcess(String examinationApprovalStatus,
			String keyWord,Integer purpose){
		List<Map<String,Object>> list=checkProcessManagement
				.listCheckProcess(null,examinationApprovalStatus,keyWord,purpose);
		for(int i=0;i<list.size();i++){//查询一个用户数据清单总数量
			String applicantDataListIds=String.valueOf(list.get(i).get("applicant_data_list_id"));
			Integer countNum=0;
			if(applicantDataListIds.length()>0){
				applicantDataListIds=applicantDataListIds.substring(1, applicantDataListIds.length()-1);
				countNum=checkProcessManagement.getapplicantAllNum(applicantDataListIds.split(","));
			}
			list.get(i).put("countNum", countNum);
		}
		return list;
	}
	
	@Override
	public List<Map<String, Object>> filterAreaImage(String[] ids) {
		List<Map<String, Object>> list=checkProcessManagement.filterAreaImage(ids);
		if(list.size()<1){//获取影像类型的id
			for (String id : ids) {
				logger.info("未找到ids为："+id.toString()+"的影像信息");
			}
		}
		for(int i=0;i<list.size();i++){
			String satelliteType=(String)list.get(i).get("image_satellite_type");
			List<Map<String, Object>> listIds=checkProcessManagement
					.getIdsBySatelliteType(ids, satelliteType);
			String imageIds="";
			for (Map<String, Object> map : listIds) {
				imageIds=imageIds+map.get("id")+",";
			}
			if(!"".equals(imageIds)){
				imageIds=imageIds.substring(0,imageIds.length()-1);
			}
			imageIds="{"+imageIds+"}";
			list.get(i).put("image_ids_list", imageIds);
		}
		return list;
	}
	@Override
	public String filterAreaImageIdsByRecordId(String recordId) {
		String ids="";
		List<Map<String, Object>> idsList=checkProcessManagement.findAreaImageByRecordId(recordId);
		for (Map<String, Object> map : idsList) {
			ids=ids+","+map.get("id");
		}
		if(ids.length()>0){
			return ids.substring(1);
		}else{
			return ids;
		}
		
	}
	public Map<String, Object> getCheckProcessDetailById(String id) throws Exception{
		List<Map<String, Object>> list=checkProcessManagement.listCheckProcess(id,null,null,null);
		List<Map<String, Object>> listApplicantData=null;
		List<Map<String, Object>> listAreaImage=null;
		if(list.size()>1){
			throw new Exception("该id："+id+"存在多条记录");
		}else if(list.size()==1){
			String applicantDataListId=String.valueOf(list.get(0).get("applicant_data_list_id"));
			if(applicantDataListId!=null&&!"".equals(applicantDataListId)){//添加数据清单信息
				applicantDataListId=applicantDataListId.substring(1,applicantDataListId.length()-1);
				listApplicantData=
						checkProcessManagement.getapplicantDataListByIds(applicantDataListId.split(","));
				list.get(0).put("listApplicantData", listApplicantData);
				listAreaImage=findDownloadDataListByExaminationApprovalRecordId(id);
				list.get(0).put("listAreaImage", listAreaImage);
			}
			return list.get(0);
		}else{
			return null;
		}
		
	}
	@Override
	public List<Map<String, Object>> findDownloadDataListByExaminationApprovalRecordId(String ExaminationApprovalRecordId){
		return checkProcessManagement.findDownloadDataListByExaminationApprovalRecordId(ExaminationApprovalRecordId);
	}
	public String filterImageAreaId(String[] id) {
		String idsString="";
		for (int i = 0; i < id.length; i++) {
			String idStringTemp=checkProcessManagement.getMaxImageArea(id[i]);
			if(!"".equals(idStringTemp)){
				idsString=","+idStringTemp+idsString;
			}
		}
		if(idsString.length()>0){
			return idsString.substring(1);
		}else{
			return idsString;
		}
	}
	@Override
	public Map<String,Object> getrscmAreaImageById(String id){
		return checkProcessManagement.getrscmAreaImageById(id);
	}
	@Override
	public boolean updateDownloadDataById(String id,Integer status){
		return checkProcessManagement.updateDownloadDataById(id, status);
	}
	@Override
	public Map<String, Object> findExampleDataLimit(){
		return checkProcessManagement.findExampleDataLimit();
	}
	@Override
	public int findDownloadDataListCountByDate(String startDate,String endDate){
		return checkProcessManagement.findDownloadDataListCountByDate(startDate, endDate);
	}
	@Override
	public Map<String,String> checkExaminationApprovalRecordsByRecordId(String recordId){
		return checkProcessManagement.checkExaminationApprovalRecordsByRecordId(recordId);
	}
}
