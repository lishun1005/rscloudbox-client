package com.cloudbox.mgmt.service;

import java.util.List;
import java.util.Map;

import com.chinarsgeo.rscloudmart.web.dao.entity.DownloadRecord;


public interface DownloadRecordService {
	/**
	 * Description 插入入库状态记录
	 * @param   recordid 自动入库状态记录id
	 * @param   recordtime 插入记录的时间
	 * @param   userordertype 用户订单对应的类型
	 * 
	 **/
	abstract void insertRecordStutas(String recordid, String recordtime,String userordertype
			,String name,String downloadFilepath,String imageProductId,Integer downloadstatus,
			Long downloadsize,String taskendtime);
	/**
	 * Description 更新自动入库状态记录状态
	 * @param   downloadstatus 入库的状态，
	 * @param   recordid   自动入库状态记录id
	 * 
	 **/
	abstract int updateRecordStutasFordownloadstatus(Integer downloadstatus,String name);
	/**
	 * Description 更新自动入库状态记录下载文件大小
	 * @param   downloadsize 下载入库文件大小
	 * @param   recordid   自动入库状态记录id
	 * 
	 **/
	abstract int updateRecordStutasFordownloadsize(Long downloadsize,String name);
	/**
	 * Description 查询所有自动入库状态记录
	 * 
	 **/
	abstract Map<String, Object> queryRecordStutas();
	/**
	 * Description 根据id查询自动入库状态记录状态
	 * @param   recordid   自动入库状态记录id
	 **/
	Map<String, Object> queryrecordstutasfordownloadstatus(String recordid);
	/**
	 * @Description 查询该id下的自动入库状态记录是否存在
	 * @param  recordid  自动入库状态记录id
	 * 
	 **/
	int qureyRecordbyrecordid(String recordid);
	/**
	 * Description 根据id更新自动入库状态记录的数据下载完成时间
	 * @param  recordid  自动入库状态记录id
	 * 
	 **/
	void updateDownloadEndTime(String downloadendtime, String name);
	/**
	 * Description 根据id更新自动入库状态记录的数据解压完成时间
	 * @param  recordid  自动入库状态记录id
	 * 
	 **/
	void updateTargzEndTime(String targzendtime, String name);
	/**
	 * Description 根据id更新自动入库状态记录的数据切割完成时间
	 * @param  recordid  自动入库状态记录id
	 * 
	 **/
	void updateCutEndTime(String cutendtime, String recordid);
	/**
	 * Description 根据id更新自动入库状态记录的数据入库完成时间
	 * @param  recordid  自动入库状态记录id
	 * 
	 **/
	void updateInputstorageEndTime(String inputstorageendtime, String name);
	/**
	 * Description 根据id查询自动入库状态记录
	 * @param   recordid   自动入库状态记录id
	 **/
	DownloadRecord queryRecordStutasByRecordid(String recordid);
	/**
	 * Description 根据id更新自动入库状态记录的自动入库任务完成时间
	 * @param  recordid  自动入库状态记录id
	 * 
	 **/
	void updateTaskEndTime(String taskendtime, String name);
	/**
	 * @Description 根据recordId查询自动入库推送状态和该记录的在客户端的下载状态
	 * @param   recordid   自动入库状态记录id
	 * @author lishun
	 **/
	Map<String, Object> getRecordstutasAndOpStatus(String recordId);
	/**
	 * 根据下载状态获取全部下载记录
	 * @param downloadstatus
	 * @author lishun
	 */
	List<Map<String, Object>> getDownloadrecordByDownloadStatus(
			Integer downloadstatus);
	/**
	* Description: 根据name和status判断是否存在记录
	* @param name 
	* @return  Boolean true:存在记录；false：不存在
	* @author lishun 
	* @date 2016-5-10 下午4:08:49
	 */
	Boolean checkIsDownloadByName(String name, Integer status);
	/**
	* Description:根据name获取下载记录 
	* @param name eq:GF2_PMS2_E113.9_N23.8_20151219_L1A0001253966
	* @return  List
	* @author lishun 
	* @date 2016-5-11 上午10:09:35
	 */
	List<Map<String, Object>> queryDownrecordByName(String name);
	/**
	* Description: 根据name更新推送状态
	* @param downloadstatus  
	* @param name name
	* @return  int
	*
	* @author lishun 
	* @date 2016-5-12 上午10:32:24
	 */
	int updateRecordStutasByName(Integer downloadstatus, String name);
	/**
	* Description: 根据name分组来推送数据(相同的name=GF2_PMS2_E113.9_N23.8_20151219_L1A0001253966对应的是同一个压缩包)
	* @return  List
	* @author lishun 
	* @date 2016-5-17 下午3:18:38
	 */
	List<Map<String, Object>> getDownloadrecordGroupByName(Integer downloadstatus);
	/**
	 * 
	* Description:删除记录 
	* @param recordId  
	* @return void<br>
	*
	* @author lishun 
	* @date 2016-7-28 上午9:21:14
	 */
	void deleteByName(String name);
	/**
	 * 
	* Description:删除记录 
	* @param recordId  
	* @return void<br>
	*
	* @author lishun 
	* @date 2016-7-28 上午9:21:14
	 */
	void deleteById(String id);
	/**
	* Description:添加删除中转服务器文件失败记录 
	* @param downloadPath 中转服务器下载路径
	* @param imageName 影像名称
	* @param status 删除状态
	* @author lishun 
	* @date 2016-11-17 下午3:24:45
	 */
	public void addFileDeleteException(String downloadPath, String imageName,Integer status);
}
