package com.cloudbox.mgmt.service;

import java.util.Map;

public interface AreaService {
	public abstract Map<String, Object> AreaqueryforProvince();
	public abstract Map<String, Object> AreaqueryforCity(String province);
	public abstract Map<String, Object> AreaqueryforCounty(String city,String cityid);
	public Map<String, Object> AreaqueryByareaId(String areaId);
	public abstract String AreaqueryForNameByIds(String areaIdarray);
}
