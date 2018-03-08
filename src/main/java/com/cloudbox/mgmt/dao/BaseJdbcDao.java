package com.cloudbox.mgmt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

/**
 * @author wyq 2012-07
 * 
 * @Description   数据库基本操作封装实现类
 * 				提供对于数据库所有操作的封装
 * @version V1.0
 */
@Repository("basejdbcdao")
public class BaseJdbcDao<T extends java.io.Serializable>{
    protected final Log logger = LogFactory.getLog(getClass());
    private JdbcTemplateMap jdbcTemplateMap;
    
    public JdbcTemplateMap getJdbcTemplateMap() {
		return jdbcTemplateMap;
	}
    @Autowired
	public void setJdbcTemplateMap(JdbcTemplateMap jdbcTemplateMap) {
		this.jdbcTemplateMap = jdbcTemplateMap;
		//this.jdbcTemplate=this.jdbcTemplateMap.getJdbcTemplate("jdbctemplate");
	}

	public List<Map<String, Object>> queryForList(String sql,String jdbcTemplateKey) {
        if (logger.isDebugEnabled())
            logger.debug(sql);
        return this.jdbcTemplateMap.getJdbcTemplate(jdbcTemplateKey).queryForList(sql);
    }

    public int queryForInt(String sql,String jdbcTemplateKey) {
        if (logger.isDebugEnabled())
            logger.debug(sql);
        return this.jdbcTemplateMap.getJdbcTemplate(jdbcTemplateKey).queryForInt(sql);
    }

    public SqlRowSet queryForRowSet(String sql,String jdbcTemplateKey) {
        if (logger.isDebugEnabled())
            logger.debug(sql);
        return this.jdbcTemplateMap.getJdbcTemplate(jdbcTemplateKey).queryForRowSet(sql);
    }

    public int update(String sql,String jdbcTemplateKey) {
        if (logger.isDebugEnabled())
            logger.debug(sql);
        return this.jdbcTemplateMap.getJdbcTemplate(jdbcTemplateKey).update(sql);
    }

    public int[] batchUpdate(String[] sql,String jdbcTemplateKey) {
        if (sql.length > 0)
            return this.jdbcTemplateMap.getJdbcTemplate(jdbcTemplateKey).batchUpdate(sql);
        return new int[0];
    }

    public Map<String, Object> queryForMap(String sql,String jdbcTemplateKey) {
        if (logger.isDebugEnabled())
            logger.debug(sql);
        return this.jdbcTemplateMap.getJdbcTemplate(jdbcTemplateKey).queryForMap(sql);
    }

    public void delete(String sql,String jdbcTemplateKey) {
        if (logger.isDebugEnabled())
            logger.debug(sql);
        this.jdbcTemplateMap.getJdbcTemplate(jdbcTemplateKey).execute(sql);
    }

    public void insert(String sql,String jdbcTemplateKey) {
        if (logger.isDebugEnabled())
            logger.debug(sql);
        this.jdbcTemplateMap.getJdbcTemplate(jdbcTemplateKey).execute(sql);
    }

    public int insertwithreturn(String sql,String jdbcTemplateKey) {
        if (logger.isDebugEnabled())
            logger.debug(sql);
        final String newsql = sql;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplateMap.getJdbcTemplate(jdbcTemplateKey).update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(newsql,
                        new String[] { "id" });

                return ps;
            }
        }, keyHolder);
        return (Integer) keyHolder.getKey();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<T> queryForBeanPropertyRowMapper(String sql,String jdbcTemplateKey,
            Class<T> entityClazz) {
        if (logger.isDebugEnabled())
            logger.debug(sql);
        List<T> list = this.jdbcTemplateMap.getJdbcTemplate(jdbcTemplateKey).query(sql,
                new BeanPropertyRowMapper(entityClazz));
        return list;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<T> queryForBeanPropertyRowMapperByArgs(String sql,String jdbcTemplateKey,
            Object[] args, Class<T> entityClazz) {
        // TODO Auto-generated method stub
        if (logger.isDebugEnabled())
            logger.debug(sql + ";" + getObjectArrayString(args));
        List<T> list = this.jdbcTemplateMap.getJdbcTemplate(jdbcTemplateKey).query(sql, args,
                new BeanPropertyRowMapper(entityClazz));
        return list;
    }

    public int updateByArgs(String sql,String jdbcTemplateKey, Object[] args) {
        // TODO Auto-generated method stub
        if (logger.isDebugEnabled())
            logger.debug(sql + ";" + getObjectArrayString(args));
        return this.jdbcTemplateMap.getJdbcTemplate(jdbcTemplateKey).update(sql, args);
    }

    public int insertByArgs(String sql,String jdbcTemplateKey, Object[] args) {
        // TODO Auto-generated method stub
        if (logger.isDebugEnabled())
            logger.debug(sql + ";" + getObjectArrayString(args));
        return this.jdbcTemplateMap.getJdbcTemplate(jdbcTemplateKey).update(sql, args);
    }

    public List<Map<String, Object>> queryForMapByArgs(String sql,String jdbcTemplateKey, Object[] args) {
        // TODO Auto-generated method stub
        if (logger.isDebugEnabled())
            logger.debug(sql + ";" + getObjectArrayString(args));
        return this.jdbcTemplateMap.getJdbcTemplate(jdbcTemplateKey).queryForList(sql, args);
    }

    private String getObjectArrayString(Object[] args) {
        StringBuffer sb = new StringBuffer("");
        try {
            int i = 0;
            for (Object obj : args) {
                if (i > 0)
                    sb.append(",");
                if (obj == null)
                    obj = "null";

                sb.append(obj.toString());
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return sb.toString();
    }

    public SqlRowSet queryForRowSet(String sql,String jdbcTemplateKey, Object[] args) {
        // TODO Auto-generated method stub
        if (logger.isDebugEnabled())
            logger.debug(sql + ";" + getObjectArrayString(args));
        return this.jdbcTemplateMap.getJdbcTemplate(jdbcTemplateKey).queryForRowSet(sql, args);
    }

    public int deleteByArgs(String sql,String jdbcTemplateKey, Object[] args) {
        // TODO Auto-generated method stub
        if (logger.isDebugEnabled())
            logger.debug(sql + ";" + getObjectArrayString(args));
        return this.jdbcTemplateMap.getJdbcTemplate(jdbcTemplateKey).update(sql, args);
    }

    public int queryForInt(String sql,String jdbcTemplateKey, Object[] args) {
        // TODO Auto-generated method stub
        if (logger.isDebugEnabled())
            logger.debug(sql + ";" + getObjectArrayString(args));
        return this.jdbcTemplateMap.getJdbcTemplate(jdbcTemplateKey).queryForInt(sql, args);
    }
}
