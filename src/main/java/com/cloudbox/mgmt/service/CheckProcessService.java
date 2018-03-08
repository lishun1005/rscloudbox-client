package com.cloudbox.mgmt.service;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.cloudbox.mgmt.old.entity.ApplicantDataListDto;
import com.cloudbox.mgmt.old.entity.ApplicantInformation;
import com.cloudbox.mgmt.old.entity.DownloadDataList;
import com.cloudbox.mgmt.old.entity.ExaminationApprovalRecords;

/**
 * 审核流程service
 * @author lishun
 *
 */
public interface CheckProcessService {
	/**
	 * 添加审批信息到客户端
	 * @param applicantInformation 审批人信息
	 * @param examinationApprovalRecords 审批记录表
	 * @param applicantDataListDto 数据清单List
	 * @param downloadDataList 数据下载列表
	 */
	public void addCheckProcessToClient(ApplicantInformation applicantInformation,
			ExaminationApprovalRecords examinationApprovalRecords,
			ApplicantDataListDto applicantDataListDto,
			List<DownloadDataList> downloadDataList);
	/**
	 * 查看云盒客户端所有用户的审批信息
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> listCheckProcess(String examinationApprovalStatus,
			String keyWord,Integer purpose);
	
	/**
	 * 获取影像信息汇总数据
	 * @param ids
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> filterAreaImage(String[] ids);
	/**
	* Description:获取单条审批记录的详细信息 
	* @param id 审批id
	* @return Map<String, Object>
	* @author lishun 
	* @date 2016-5-9 下午2:14:29
	 */
	public Map<String, Object> getCheckProcessDetailById(String id) throws Exception;
	/**
	* Description:当选择面积较小的数据时，默认选择面积较大的id(当多条数据的image_satellite_type和file_path相等时就选择面积较大未统计数据) 
	* @param recordId
	* @return  返回过滤后的ids 以,分隔
	*
	* @author lishun 
	* @date 2016-5-9 下午2:09:10
	 */
	String filterAreaImageIdsByRecordId(String recordId);
	/**
	* Description:修改审批状态 
	* @param id 审批id
	* @param status 审批状态
	* @param examinationApprovalTime 审批时间
	* @return  boolean
	* @author lishun 
	* @date 2016-5-9 下午2:33:59
	 */
	boolean updateExaminationApprovalRecordsById(String id, Integer status,String examinationApprovalTime);
	/**
	 * 当选择面积较小的数据时，默认选择面积较大的id(当多条数据的image_satellite_type和file_path相等时就选择面积较大未统计数据)
	 * @return
	 */
	String filterImageAreaId(String[] id);
	/**
	* Description:获取rscmAreaImage的详细信息 
	* @param id
	* @return  Map<String, Object>
	* @author lishun 
	* @date 2016-5-9 下午2:22:54
	 */
	Map<String, Object> getrscmAreaImageById(String id);
	/**
	* Description: 更新数据下载列表的状态
	* @param recordId id
	* @param status 状态
	* @return  boolean 
	* @author lishun 
	* @date 2016-5-9 下午2:23:43
	 */
	boolean updateDownloadDataById(String id, Integer status);
	/**
	* Description:获取审批记录的下载数据列表 
	* @param ExaminationApprovalRecordId 审批记录id
	* @return List
	* @author lishun 
	* @date 2016-5-9 下午2:26:36
	 */
	List<Map<String, Object>> findDownloadDataListByExaminationApprovalRecordId(
			String ExaminationApprovalRecordId);
	/**
	* Description:获取样例数据的时间限制和数量限制 
	* @return  
	* @author lishun 
	* @date 2016-5-9 下午2:13:34
	 */
	Map<String, Object> findExampleDataLimit();
	/**
	* Description:获取一个时间段的样例数据已经下载的景数
	* @param startDate 开始时间
	* @param endDate 结束时间
	* @return  int
	* @author lishun 
	* @date 2016-5-9 下午2:29:17
	 */
	int findDownloadDataListCountByDate(
			String startDate, String endDate);
	/**
	* Description: 通过recordId判断该条记录是否可以提交审批
	* @param recordId
	* @return Map code=1:可以提交审批；code=0:不可以提交审批[val=存在recordid数据的审批记录id]
	* @author lishun 
	* @date 2016-5-9 下午3:46:45
	 */
	Map<String,String> checkExaminationApprovalRecordsByRecordId(String recordId);
	/**
	 * 
	* Description: 添加审批信息到客户端及其后端
	* @param applicantInformation 审批人信息
	* @param examinationApprovalRecords 审批记录表
	* @param applicantDataListDto 数据清单List
	* @param contractScanningFile 合同扫描件
	* @param invoiceScanningFile 发票扫描件
	* @param downloadDataListIds 
	* @param applicantName 当前登录用户名
	* @throws Exception  
	* @author lishun 
	* @date 2016年12月9日 下午2:11:33
	 */
	void addCheckProcess(ApplicantInformation applicantInformation,
			ExaminationApprovalRecords examinationApprovalRecords,
			ApplicantDataListDto applicantDataListDto,
			File contractScanningFile, File invoiceScanningFile,
			String downloadDataListIds, String applicantName) throws Exception;
}
