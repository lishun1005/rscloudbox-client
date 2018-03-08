package com.cloudbox.mgmt.old.entity;


import java.io.Serializable;

/**
 * @author lzw 2015-12-12
 * 
 * @Description   记录几何对象实体类型
 * 			用于配合GeojsonMultiPolygonDto类解析几何实体对象的类
 * 
 * @version V1.2
 */
public class GeojsonDto implements Serializable {

	private static final long serialVersionUID = 1460735693731047889L;
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
