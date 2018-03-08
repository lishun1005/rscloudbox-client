package com.cloudbox.mgmt.controller;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinarsgeo.rscloudmart.web.dao.entity.ReturnBean;
import com.chinarsgeo.rscloudmart.web.webservice.IYunheLoginServer;
import com.cloudbox.mgmt.entity.User;
import com.cloudbox.utils.Base64;
import com.cloudbox.utils.RSAEncrypt;
import com.cloudbox.utils.StringTools;
/**
 * @Description  登陆controller
 * */
@Controller
public class UserCenterControler extends BaseController{
	@Autowired
	private IYunheLoginServer yunheLoginServer;
	private static String bboxName;
	static{
		try {
			bboxName = new String(RSAEncrypt.
						decrypt(RSAEncrypt.loadPublicKeyByStrDefault(), 
								Base64.decode(StringTools.getValueFromProperties("Rsmartgtcloudos", "groupUserName"))));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@RequestMapping("/loginAction")
	@ResponseBody
	public Map<String, Object> login(String username,String password){
		Map<String, Object> map=new HashMap<String, Object>();
		HttpServletRequest request = getRequest();
		HttpSession session = request.getSession();
		User user=new User();
		user.setUsername(username);
		user.setPassword(password);
		ReturnBean result=yunheLoginServer.login(bboxName, username, password);//使用云盒后端接口进行登录
		if ("1".equals(result.getCode())) {
			session.setAttribute("user", user);
			map.put("code", "1");
			map.put("message", "登陆成功!");
		}else {
			map.put("code", "0");
			map.put("message", result.getMessage());
		}
		return map;
	}
	/**
	 * 退出登录
	 * @param username
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(){
		HttpServletRequest request = getRequest();
		HttpSession session = request.getSession();
		session.removeAttribute("user");
		return "redirect:login.html";
	}
	@RequestMapping("/")
	public String Toindex(){
		return "redirect:cloudBox.html";
	}
}
