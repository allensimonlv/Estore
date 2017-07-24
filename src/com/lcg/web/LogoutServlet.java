package com.lcg.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//1.杀死session，先检查一下到底有没有session对象。
		if(request.getSession(false)!=null){
			//getSession(boolean)这个方法是检查有没有session，有，就返回；没有，就返回null；并不新建一个session
			request.getSession().invalidate();
			//删除自动登录的cookie。一般注销后就不用自动登录了。
			Cookie autologinC = new Cookie("autologin","");
			autologinC.setPath("/");
			autologinC.setMaxAge(0);
			response.addCookie(autologinC);
		}
		//2.重定向到主页
		response.sendRedirect("/index.jsp");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
