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
		//1.ɱ��session���ȼ��һ�µ�����û��session����
		if(request.getSession(false)!=null){
			//getSession(boolean)��������Ǽ����û��session���У��ͷ��أ�û�У��ͷ���null�������½�һ��session
			request.getSession().invalidate();
			//ɾ���Զ���¼��cookie��һ��ע����Ͳ����Զ���¼�ˡ�
			Cookie autologinC = new Cookie("autologin","");
			autologinC.setPath("/");
			autologinC.setMaxAge(0);
			response.addCookie(autologinC);
		}
		//2.�ض�����ҳ
		response.sendRedirect("/index.jsp");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
