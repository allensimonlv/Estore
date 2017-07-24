package com.lcg.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lcg.factory.BasicFactory;
import com.lcg.service.UserService;

public class ValiNameServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserService service = BasicFactory.getFactory().getService(UserService.class);
		String username = request.getParameter("username");
		String msg = null;
		if(service.hasName(username)){
			msg = "{msg:'用户名已经存在!',stat:1}";//json格式的msg对象是用大括号包裹起来的。一般格式为：{名称1:值,名称2:值2}
		}else{
			msg = "{msg:'用户名可以使用',stat:0}";
		}
		response.getWriter().write(msg);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
