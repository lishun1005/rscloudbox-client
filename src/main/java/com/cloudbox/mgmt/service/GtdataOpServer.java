package com.cloudbox.mgmt.service;

import java.util.List;
import java.util.Map;

import com.cloudbox.mgmt.old.entity.RsImageMetaData;

public interface GtdataOpServer {

	public abstract Map<String, Object> correctPic(
			RsImageMetaData RsImageMetaData);

	public abstract Map<String, Object> goldtowersectionCutting(String inPath,
			Integer max_layers, String mapname, double max_resolution,
			double min_resolution, boolean watermark);

	public abstract void recordCorrectResult(String jobId, String status);

	public abstract Map<String, Object> goldtowersectionCuttingbydefaultresolution(
			String inPath, Integer max_layers, String mapname, boolean watermark);

	public Map<String, Object> querygoldtowersectionCuttingProgress(String jobid);
	/**
	 * Description：边部数据请求后调用的入库程序
	 * @param  list 元数据列表
	 * */
	void InPutInstorage(List list, String record, String fileAbsolutePath);

}