package com.cloudbox.mgmt.controller;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cloudbox.mgmt.entity.User;
import com.cloudbox.mgmt.old.entity.ApplicantDataListDto;
import com.cloudbox.mgmt.old.entity.ApplicantInformation;
import com.cloudbox.mgmt.old.entity.DownloadDataList;
import com.cloudbox.mgmt.old.entity.ExaminationApprovalRecords;
import com.cloudbox.mgmt.service.CheckProcessService;
import com.cloudbox.mgmt.service.ProductInformationService;
import com.cloudbox.utils.Base64;
import com.cloudbox.utils.DateTools;
import com.cloudbox.utils.RSAEncrypt;
import com.cloudbox.utils.StringTools;
import com.cloudbox.utils.file.FileTools;

@Controller
public class CheckProcessController extends BaseController {
	private static Logger loggger=LoggerFactory.getLogger(CheckProcessController.class);
	@Autowired
	private CheckProcessService checkProcessService;
	@Autowired
	private ProductInformationService productInformationserver;
	
	@Value("#{bboxSystemProperties[FileTempPath]}")
	private String localSavedFolder;
	
	@Value("#{bboxSystemProperties[decryptFilePath]}")
	private String decryptFilePath;
	
	
	
	private static Logger logger = LoggerFactory.getLogger(CheckProcessService.class);
	@ResponseBody
	@RequestMapping(value = "/addCheckProcessNoFile", method = RequestMethod.POST)
	public Map<String,Object> addCheckProcessNoFile(ApplicantInformation applicantInformation,
			 ExaminationApprovalRecords examinationApprovalRecords,
			ApplicantDataListDto applicantDataListDto,
			@RequestParam(value="downloadDataListIds",required=false) String downloadDataListIds,
			String backUrl) throws IOException{
		return addCheckProcess(applicantInformation, examinationApprovalRecords, applicantDataListDto, null, null, downloadDataListIds,backUrl);
	}
	@ResponseBody
	@RequestMapping(value = "/addCheckProcessContractScanningFile", method = RequestMethod.POST)
	public Map<String,Object> addCheckProcessContractScanningFile(ApplicantInformation applicantInformation,
			 ExaminationApprovalRecords examinationApprovalRecords,
			ApplicantDataListDto applicantDataListDto,
			@RequestParam(value="contractScanning",required=false) MultipartFile contractScanning,
			@RequestParam(value="downloadDataListIds",required=false) String downloadDataListIds,
			String backUrl) throws IOException{
		return addCheckProcess(applicantInformation, examinationApprovalRecords, applicantDataListDto, contractScanning, null, downloadDataListIds,backUrl);
	}
	@ResponseBody
	@RequestMapping(value = "/addCheckProcessInvoiceScanningFile", method = RequestMethod.POST)
	public Map<String,Object> addCheckProcessContractInvoiceScanningFile(ApplicantInformation applicantInformation,
			 ExaminationApprovalRecords examinationApprovalRecords,
			ApplicantDataListDto applicantDataListDto,
			@RequestParam(value="invoiceScanning",required=false) MultipartFile invoiceScanning,
			@RequestParam(value="downloadDataListIds",required=false) String downloadDataListIds,
			String backUrl) throws IOException{
		return addCheckProcess(applicantInformation, examinationApprovalRecords, applicantDataListDto, null, invoiceScanning,downloadDataListIds, backUrl);
	}
	/**
	 * 添加审批信息
	 * 
	 * @param applicantInformation
	 *            审批人信息
	 * @param examinationApprovalRecords
	 *            审批记录表
	 * @param applicantDataListDto
	 *            数据清单List
	 * @param contractScanning
	 *            合同扫描件
	 * @param invoiceScanning
	 *            发票扫描件
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/addCheckProcess", method = RequestMethod.POST)
	public Map<String,Object> addCheckProcess(ApplicantInformation applicantInformation,
			 ExaminationApprovalRecords examinationApprovalRecords,
			ApplicantDataListDto applicantDataListDto,
			@RequestParam(value="contractScanning",required=false) MultipartFile contractScanning,
			@RequestParam(value="invoiceScanning",required=false) MultipartFile invoiceScanning,
			@RequestParam(value="downloadDataListIds",required=false) String downloadDataListIds,
			String backUrl) {
		Map<String,Object> map=new HashMap<String, Object>();
		HttpServletRequest request = getRequest();
		HttpSession session = request.getSession();
		Object userNameObj=session.getAttribute("user");
		String applicantName="";
		if(userNameObj!=null){//获取申请人姓名(当前登录用户)
			User user=(User)userNameObj;
			applicantName=user.getUsername();
		}else{
			map.put("code", 0);
			map.put("message", "用户没有登录");
			logger.info("用户没有登录");
			return map;
		}
		String contractScanningFileName = "";
		String invoiceScanningFileName = "";
		File contractScanningFile = null;
		File invoiceScanningFile = null;
		try {
			if(contractScanning!=null){
				if (contractScanning.getSize() > 0) {
					contractScanningFileName = UUID.randomUUID().toString() + "_"
							+ contractScanning.getOriginalFilename();
					// 保存文件
					contractScanningFile = new File(localSavedFolder,contractScanningFileName);
					FileUtils.copyInputStreamToFile(contractScanning.getInputStream(),contractScanningFile);
					applicantInformation.setContract_scanning_path(localSavedFolder+ File.separator + contractScanningFileName);
				}
			}
			if(invoiceScanning!=null){
				if (invoiceScanning.getSize() > 0) {
					invoiceScanningFileName = UUID.randomUUID().toString() + "_"
							+ invoiceScanning.getOriginalFilename();
					
					invoiceScanningFile = new File(localSavedFolder,
							invoiceScanningFileName);

					FileUtils.copyInputStreamToFile(invoiceScanning.getInputStream(),
							invoiceScanningFile);

					applicantInformation.setInvoice_scanning_path(localSavedFolder
							+ File.separator + invoiceScanningFileName);
				}
			}
			checkProcessService.addCheckProcess(applicantInformation, examinationApprovalRecords, 
					applicantDataListDto, contractScanningFile, invoiceScanningFile, 
					downloadDataListIds, applicantName);
			map.put("code", 1);
		} catch (Exception e) {
			e.printStackTrace();
			if (!"".equals(contractScanningFileName)) {// 添加失败，删除上传过的文件
				FileTools.delete(new File(localSavedFolder + File.separator
						+ contractScanningFileName));
			}
			if (!"".equals(invoiceScanningFileName)) {
				FileTools.delete(new File(localSavedFolder + File.separator
						+ invoiceScanningFileName));
			}
			logger.info("添加审批失败" + e.getMessage());
			map.put("code", 0);
			map.put("message", "添加审批失败" + e.getMessage());
		}
		return map;
	}

	/**
	 * 审批页面
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/checkProcessPage", method = RequestMethod.POST)
	public String checkProcessPage(String ids, Model model) throws ParseException {
		if (ids != null && !"".equals(ids)) {
			String idsString = checkProcessService.filterImageAreaId(ids.split(","));
			model.addAttribute("list",
					checkProcessService.filterAreaImage(idsString.split(",")));// 获取数据清单
			Map<String,Object> map=checkProcessService.findExampleDataLimit();
			if(map!=null){
				Integer dateLimit=(Integer)map.get("date_limit");
				Integer numLimit=(Integer)map.get("num_limit");
				String endDate="";
				String startDate="";
				if(dateLimit>1){//存在大于1个月的时间间隔
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
					String startUseDateStr=StringTools.getValueFromProperties("install.properties", "startUseDateStr");
					Map<String,Object> resultMap=
							getCountDate(dateLimit,sdf.parse(startUseDateStr),new Date());
					startDate=resultMap.get("first").toString();
					endDate=resultMap.get("last").toString();
				}else{
					Map<String,String> mapDate=DateTools.getFirstdayLastday(new Date());
					startDate=mapDate.get("first").toString();
					endDate=mapDate.get("last").toString();
				}
				System.out.println(startDate+endDate);
				int count=checkProcessService
						.findDownloadDataListCountByDate(startDate, endDate);
				model.addAttribute("count",count);
				model.addAttribute("numLimit",numLimit);
			}
		}
		
		return "/checkProcess/checkProcessPage";
	}
	/**
	* Description:按月获取一个时间段 
	* @param dateLimit 间隔月数 
	* @param startUseDate 开始使用时间
	* @param currentDate  当前时间
	*</br>eq:开始使用日期：2015-02-01；时间间隔 5；当前时间 2016-06-01；</br>返回值 2016-05-01~2016-09-31
	* @author lishun 
	* @date 2016年6月12日 下午5:18:12
	 */
	public  Map<String,Object> getCountDate(int dateLimit,Date startUseDate
			,Date currentDate){
		Map<String,Object> resultMap=new HashMap<String, Object>();
		int startYear=DateTools.getDate(startUseDate, DateTools.YEAR);
		int currentYear=DateTools.getDate(currentDate, DateTools.YEAR);
		int yearSub=currentYear-startYear;
		if(yearSub>0){//跨年的日期
			int monthSub=12*yearSub+DateTools.getDate(currentDate, DateTools.MONTH)-
					DateTools.getDate(startUseDate, DateTools.MONTH);//计算间隔多少个月
			Calendar tempCa=Calendar.getInstance();
			int startMonth=DateTools.getDate(currentDate, DateTools.MONTH)-monthSub%dateLimit-1;
			if(startMonth<0){
				tempCa.set(Calendar.YEAR, DateTools.getDate(currentDate, DateTools.YEAR)-1);
				tempCa.set(Calendar.MONTH, 12+startMonth);
			}else{
				tempCa.set(Calendar.YEAR, DateTools.getDate(currentDate, DateTools.YEAR));
				tempCa.set(Calendar.MONTH, startMonth);
			}
			resultMap.put("first", DateTools.getFirstdayLastday(tempCa.getTime()).get("first"));
			tempCa.add(Calendar.MONTH, dateLimit-1);
			resultMap.put("last", DateTools.getFirstdayLastday(tempCa.getTime()).get("last"));
		}else{
			int monthSub=DateTools.getDate(currentDate, DateTools.MONTH)-
					DateTools.getDate(startUseDate, DateTools.MONTH);//计算间隔多少个月
			int startMonth=DateTools.getDate(currentDate, DateTools.MONTH)
					-monthSub%dateLimit;//计算开始统计时间
			Calendar tempCa=Calendar.getInstance();
			tempCa.setTime(currentDate);
			tempCa.set(Calendar.MONTH, startMonth-1);
			resultMap.put("first", DateTools.getFirstdayLastday(tempCa.getTime()).get("first"));
			tempCa.add(Calendar.MONTH, dateLimit-1);
			resultMap.put("last", DateTools.getFirstdayLastday(tempCa.getTime()).get("last"));
		}
		return resultMap;
	}
	/**
	 * 审批页面
	 */
	@RequestMapping(value = "/checkProcessPageByRecordId/{recordId}/{name}", method = RequestMethod.GET)
	public String checkProcessPageByRecordId(@PathVariable("recordId") String recordId,@PathVariable("name")String name,Model model) throws ParseException {
		String ids=checkProcessService.filterAreaImageIdsByRecordId(recordId);
		if("".equals(ids)){
			recordId=productInformationserver.queryRecordIdByName(name);
			if(!"".equals(recordId)){
				ids=checkProcessService.filterAreaImageIdsByRecordId(recordId);
			}else{
				logger.info("未找到name="+name+"的影像数据");
			}
		}
		if (ids != null && !"".equals(ids)) {
			String idsString = checkProcessService.filterImageAreaId(ids.split(","));
			model.addAttribute("list",
					checkProcessService.filterAreaImage(idsString.split(",")));
			Map<String,Object> map=checkProcessService.findExampleDataLimit();//审批数量和时间限制
			if(map!=null){
				Integer dateLimit=(Integer)map.get("date_limit");
				Integer numLimit=(Integer)map.get("num_limit");
				String endDate="";
				String startDate="";
				if(dateLimit>1){//存在大于1个月的时间间隔
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
					String startUseDateStr=StringTools.getValueFromProperties("install", "startUseDateStr");
					Map<String,Object> resultMap=
							getCountDate(dateLimit,sdf.parse(startUseDateStr),new Date());
					startDate=resultMap.get("first").toString();
					endDate=resultMap.get("last").toString();
				}else{
					Map<String,String> mapDate=DateTools.getFirstdayLastday(new Date());
					startDate=mapDate.get("first").toString();
					endDate=mapDate.get("last").toString();
				}
				int count=checkProcessService
						.findDownloadDataListCountByDate(startDate, endDate);
				model.addAttribute("count",count);
				model.addAttribute("numLimit",numLimit);
			}
		}
		return "/checkProcess/checkProcessPage";
	}

	/**
	 * 查询全部审批数据
	 */
	@RequestMapping(value = "/listCheckProcess", method = RequestMethod.GET)
	public String listCheckProcess(Model model,
			String examinationApprovalStatus, String keyWord, Integer purpose) {
		HttpServletRequest request = getRequest();
		HttpSession session = request.getSession();
		Object userNameObj=session.getAttribute("user");
		if(userNameObj!=null){//获当前登录用户
			User user=(User)userNameObj;
			model.addAttribute("list", checkProcessService.listCheckProcess(
					examinationApprovalStatus, user.getUsername(), purpose));
			return "/checkProcess/list";
		}else{
			logger.info("用户没有登录");
			return "redirect:/login.html";
		}
		
	}

	/**
	 * 获取单条审批记录的详细信息
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/getCheckProcessDetail/{id}", method = RequestMethod.GET)
	public String getCheckProcessDetailById(@PathVariable("id") String id, Model model)
			throws Exception {
		Map<String, Object> mapMsg = checkProcessService
				.getCheckProcessDetailById(id);
		model.addAttribute("map", mapMsg);
		Map map=checkProcessService.findExampleDataLimit();
		if(map!=null){
			Integer dateLimit=(Integer)map.get("date_limit");
			Integer numLimit=(Integer)map.get("num_limit");
			String endDate="";
			String startDate="";
			if(dateLimit>1){//存在大于1个月的时间间隔
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				String startUseDateStr=StringTools.getValueFromProperties("install", "startUseDateStr");
				Map<String,Object> resultMap=
						getCountDate(dateLimit,sdf.parse(startUseDateStr),new Date());
				startDate=resultMap.get("first").toString();
				endDate=resultMap.get("last").toString();
			}else{
				Map<String,String> mapDate=DateTools.getFirstdayLastday(new Date());
				startDate=mapDate.get("first").toString();
				endDate=mapDate.get("last").toString();
			}
			int count=checkProcessService
					.findDownloadDataListCountByDate(startDate, endDate);
			model.addAttribute("count",count);
			model.addAttribute("numLimit",numLimit);
		}
		return "/checkProcess/detail";
	}
	/**
	 * 获取rscmAreaImage的详细信息
	 */
	@ResponseBody
	@RequestMapping(value = "/getrscmAreaImageById", method = RequestMethod.GET)
	public Map<String,Object>  getrscmAreaImageById(String id){
		Map<String,Object> map=checkProcessService.getrscmAreaImageById(id);
		return map;
	}
	
	
	/**
	 * Description: 重新审批页面
	 * 
	 * @param recordIds
	 *            多条推送数据id
	 * @param downloadDataListIds
	 *            多条数据下载清单id
	 * @param names
	 *            数据名称
	 * @param customerName
	 *            客户名称
	 * @param customerContacter
	 *            联系人
	 * @param customerContact
	 *            联系电话
	 * @author huangxj
	 * 
	 * @date 2016-7-19 上午10:23:03
	 */
	@RequestMapping(value = "/checkProcessPageReapply", method = RequestMethod.POST)
	public String checkProcessPageReapply(String recordIds,
			String downloadDataListIds, String names, String customerName,
			String customerContacter, String customerContact, Model model)
			throws ParseException {

		//将客户信息提交给前台
		model.addAttribute("customerName", customerName);
		model.addAttribute("customerContacter", customerContacter);
		model.addAttribute("customerContact", customerContact);
		
		//downloadDataListIds提交给前台的主要目是为了单用户提交重新申请审批时可以将相应记录设为已重新下载
		model.addAttribute("downloadDataListIds", downloadDataListIds);
		
		String[] recordIdArr = recordIds.split(",");
		String[] nameArr = names.split(",");

		// 将路径名转化成不带后缀的文件名
		for (int i = 0; i < nameArr.length; i++) {
			
			//去掉路径
			int lastSeparatorIndex = nameArr[i].lastIndexOf(File.separator);
			if (lastSeparatorIndex != -1
					&& lastSeparatorIndex < (nameArr[i].length() - 1)) {

				nameArr[i] = nameArr[i].substring(lastSeparatorIndex + 1);
			}
			
			//去掉后缀名
			int suffixIndex = nameArr[i].indexOf(".tar.gz");
			if (suffixIndex != -1) {
				nameArr[i] = nameArr[i].substring(0, suffixIndex);
			}
			System.out.println(nameArr[i]);
		}
		
		//拼接rscm_area_image_id
		String ids = "";
		for (int i = 0; i < recordIdArr.length; i++) {
			String recordId = recordIdArr[i];
			String temIds=checkProcessService.filterAreaImageIdsByRecordId(recordId);
			if("".equals(temIds)){
				recordId=productInformationserver.queryRecordIdByName(nameArr[i]);
				if(!"".equals(recordId)){
					temIds=checkProcessService.filterAreaImageIdsByRecordId(recordId);
				}else{
					logger.info("未找到name="+nameArr[i]+"的影像数据");
				}
			}
			if(!"".equals(temIds)){
				ids = ids + "," + temIds;
			}
		}
		if(ids.length() > 1){
			
			ids = ids.substring(1);
		}
		
		//获取要申请的影像数据信息，并返回给前台
		if (ids != null && !"".equals(ids)) {
			String idsString = checkProcessService.filterImageAreaId(ids.split(","));
			model.addAttribute("list",
					checkProcessService.filterAreaImage(idsString.split(",")));
			Map<String,Object> map=checkProcessService.findExampleDataLimit();//审批数量和时间限制
			if(map!=null){
				Integer dateLimit=(Integer)map.get("date_limit");
				Integer numLimit=(Integer)map.get("num_limit");
				String endDate="";
				String startDate="";
				if(dateLimit>1){//存在大于1个月的时间间隔
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
					String startUseDateStr=StringTools.getValueFromProperties("install", "startUseDateStr");
					Map<String,Object> resultMap=
							getCountDate(dateLimit,sdf.parse(startUseDateStr),new Date());
					startDate=resultMap.get("first").toString();
					endDate=resultMap.get("last").toString();
				}else{
					Map<String,String> mapDate=DateTools.getFirstdayLastday(new Date());
					startDate=mapDate.get("first").toString();
					endDate=mapDate.get("last").toString();
				}
				int count=checkProcessService
						.findDownloadDataListCountByDate(startDate, endDate);
				model.addAttribute("count",count);
				model.addAttribute("numLimit",numLimit);
			}
		}

		return "/checkProcess/checkProcessPageReapply";
	}
}
