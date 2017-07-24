package com.lcg.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.lcg.domain.User;
import com.lcg.factory.BasicFactory;
import com.lcg.service.UserService;
import com.lcg.util.MD5Utils;

public class RegistServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserService service = BasicFactory.getFactory().getService(UserService.class);
		try {
			//1.У����֤��
			String valistr1 = request.getParameter("valistr");//��JSPҳ�����ύ������֤�������õ�
			String valistr2 = (String) request.getSession().getAttribute("valistr");//��Servlet�����ɵ�ԭʼ��֤�������õ�����Ϊ���Ǵ浽��session������Խ���session�����ù�����
			if(valistr1 == null || valistr2 == null || !valistr1.equals(valistr2)){
				//Ȼ��Ƚ���������֤��ͼƬ�������Ƿ���ͬ����ͬ��˵������ȷ
				request.setAttribute("msg", "<font color='red'>��֤�벻��ȷ��</font>");//���������Ҫת����ע��ҳ���ϵġ�
				request.getRequestDispatcher("/regist.jsp").forward(request, response);
				return;
			}
			//2.��װ����*У������
			User user = new User();
			BeanUtils.populate(user,request.getParameterMap());//���ô���JavaBean��jar����JavaBean�е����ݷ�װ������user�е���������Map���ϵ���ʽ����ġ�
			user.setPassword(MD5Utils.md5(user.getPassword()));//��user����MD5�㷨���ܡ�
			//3.����Service���еķ���ע���û�
			service.regist(user); 
			//4.�ص���ҳ
			response.getWriter().write("ע��ɹ����뵽�����н��м���......");
			response.setHeader("Refresh", "3;url=/index.jsp");//������Ӧͷ�����ƶ�ʱˢ�£���תҳ�档
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
