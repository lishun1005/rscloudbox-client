package com.cloudbox.filter;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		//login.html，css,js不进行过滤;
		String requestUrlString=((HttpServletRequest)request).getRequestURI();
		if(requestUrlString.indexOf("login.html")!=-1
				||requestUrlString.indexOf(".js")!=-1||requestUrlString.indexOf(".css")!=-1
				||requestUrlString.indexOf(".png")!=-1||requestUrlString.indexOf(".jpg")!=-1
				||requestUrlString.indexOf(".gif")!=-1
				||requestUrlString.indexOf("loginAction")!=-1){
			chain.doFilter(request, response);
		}else {
			HttpSession session = ((HttpServletRequest)request).getSession();
			Object userObject=session.getAttribute("user");
			if(userObject!=null){
				chain.doFilter(request, response);
			}else{
				((HttpServletResponse)response).sendRedirect((((HttpServletRequest)request)).getContextPath()+"/login.html");
			}
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
