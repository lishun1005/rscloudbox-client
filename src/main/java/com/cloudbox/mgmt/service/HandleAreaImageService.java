package com.cloudbox.mgmt.service;

import java.util.List;
import java.util.Map;

import com.cloudbox.mgmt.old.entity.RsImageMetaData;

/**
 * 处理影像数据
 * @author lishun
 *
 */
public interface HandleAreaImageService {

	List<RsImageMetaData> getEdgeDataInfomationByImageid(String imageId,
			String FileAbsolutePath);

	Map<String, String> checkDataInfomationIsSell(String imageId);
	
}
