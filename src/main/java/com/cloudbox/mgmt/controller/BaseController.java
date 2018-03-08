package com.cloudbox.mgmt.controller;



import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.ModelAttribute;
/**
 * @author yingl 2014-3-4
 * 
 * @Description Spring MVC 的 controller的抽象类，包含了获取和设置request和context的方法，
 *              其他的controller都要继承自该类。
 * @version V1.0
 */

public abstract class BaseController {


    public final Log logger = LogFactory.getLog(getClass());
    
    public HttpServletRequest request;

    public HttpServletResponse response;

    public HttpSession session;

    public ServletContext sc;

    public ApplicationContext applicationContext;

    @SuppressWarnings("unused")
    private String ip;

    
    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request,
            HttpServletResponse response) {
        this.request = request;
        this.response = response;
        if (request != null)
            this.session = request.getSession();
        if (session != null)
            this.sc = session.getServletContext();
    }

    public String getIp() {
        String ip = getRequest().getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "127.0.0.1";
        }
        return ip;
    }
    
    public JSONObject strToJsonobject(String str){
    	//保证传入参数不为空
    	if(!str.equals(null)||str.length()>0){
    		JSONObject jsonObj = JSONObject.fromObject(str);
    		return jsonObj;
    	}else {
			return null;
		}
    }
    
	@SuppressWarnings("unchecked")
	public String getParameter(HttpServletRequest httpRequest) {
		StringBuffer sb = new StringBuffer();
		Map<String, String[]> map = httpRequest.getParameterMap();

		if (map != null) {
			try {
				for (Entry<String, String[]> entry : map.entrySet()) {
					String value;
					value = entry.getValue()[0];
					String key = entry.getKey();
					if (!key.equals("prevUrl")) {
						if (sb.length() > 0)
							sb.append("&");

						sb.append(key);
						sb.append("=");
						sb.append(URLEncoder.encode(value, "UTF-8"));
					}
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

    
    public void setIp(String ip) {
        this.ip = ip;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public ServletContext getSc() {
        return sc;
    }

    public void setSc(ServletContext sc) {
        this.sc = sc;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
