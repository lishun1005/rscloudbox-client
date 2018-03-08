package com.cloudbox.mgmt.dao;


import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
/**
 * @author lzw 2015-07
 * 
 * @Description   数据库连接映射类
 * 				用于映射到不同的数据库，相当于数据库池				 
 * @version V1.0
 */
public class JdbcTemplateMap {
	
	private Map<String, JdbcTemplate> JdbcTemplatemap;

	public JdbcTemplateMap(Map<String, JdbcTemplate> JdbcTemplatemap) {
		this.JdbcTemplatemap = JdbcTemplatemap;
	}

	public Map<String, JdbcTemplate> getJdbcTemplatemap() {
		return JdbcTemplatemap;
	}

	public void setJdbcTemplatemap(Map<String, JdbcTemplate> jdbcTemplatemap) {
		JdbcTemplatemap = jdbcTemplatemap;
	}

	public JdbcTemplate getJdbcTemplate(String JdbcTemplateKey) {
		return  this.JdbcTemplatemap.get(JdbcTemplateKey);
	}
}
