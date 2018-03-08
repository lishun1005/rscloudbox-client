package com.cloudbox.utils;


import java.util.Map;

/**
 * @Description  xml模板引擎，通过在spring配置文件中配置相应的bean，以此较容易的获取xml对应的classname。也可以通过枚举类来实现
 * */
public class XmlModelMap {
    private Map<String, String>  xmlModelMap;

	public XmlModelMap(Map<String, String>  xmlModelMap) {
		this.xmlModelMap = xmlModelMap;
	}

	public Map getXmlModelMap() {
		return xmlModelMap;
	}

	public void setXmlModelMap(Map<String, String>  xmlModelMap) {
		this.xmlModelMap = xmlModelMap;
	}
    
	public String getXmlModel(String XmlModelKey) {
		return this.xmlModelMap.get(XmlModelKey);
	}
}
