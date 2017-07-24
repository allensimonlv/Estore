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
			msg = "{msg:'�û����Ѿ�����!',stat:1}";//json��ʽ��msg�������ô����Ű��������ġ�һ���ʽΪ��{����1:ֵ,����2:ֵ2}
		}else{
			msg = "{msg:'�û�������ʹ��',stat:0}";
		}
		response.getWriter().write(msg);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
