package com.lcg.web;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lcg.domain.User;
import com.lcg.factory.BasicFactory;
import com.lcg.service.UserService;
import com.lcg.util.MD5Utils;

public class LoginServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserService service = BasicFactory.getFactory().getService(UserService.class);
		//1.��ȡ�û�������
		String username = request.getParameter("username");
		String password = MD5Utils.md5(request.getParameter("password"));
		//2.����Service�еĸ����û�������������û��ķ���
		User user = service.getUserByNameAndPsw(username, password);
		if(user == null){
			request.setAttribute("msg", "�û������벻��ȷ��û�д��û���");
			request.getRequestDispatcher("/login.jsp").forward(request, response);//����ת��ʱʹ�õ�������·�����õ��Ǿ���·��������/��ͷ��
			return;//���ﷵ��һ�£�ʡ������ת������ٱ��õ���
		}
		//3.����û�����״̬
		if(user.getState() == 0){
			request.setAttribute("msg", "�û���δ����뵽�����н��м��");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			return;
		}
		//4.��¼�û��ض�����ҳ
		request.getSession().setAttribute("user", user);
		//--�����ס�û���������ֻ����cookieȥ���ڱ����û�������Ϊsession������ʱ���Ǻ̵ܶġ�session����������Ĭ�Ͻ�Ϊ30���ӡ�
		if("true".equals(request.getParameter("remname"))){
			//if�е�����д��������Ҫ�ο�login.jsp�ļ��жԼ�ס�û�����һģ��Ĳ����趨��
			Cookie remnameC = new Cookie("remname",URLEncoder.encode(user.getUsername(),"utf-8"));//��URL������û��������������û���������utf-8�ַ�����ʾ��
			remnameC.setPath("/");
			remnameC.setMaxAge(3600*24*30);//���ñ���ʱ��Ϊһ����
			response.addCookie(remnameC);//��cookie��Ϣ�洢��response��Ϣ����ȥ���ٴ�ظ��������
		}else{
			//���û�й�ѡ��ס�û�����һ��Ͳ������û�����cookie��Ϣ����Ϊ�ա�
			//���û�й�ѡ���else�в�����Ҳ���á�������cookie��MaxAge�Ļ������cookie����һ���Ự�����cookie������ʱ�����ڴ򿪵�������ڴ��С�����Ϊ0��ͬ��ͬpath��cookie�ͻᱻɾ����
			Cookie remnameC = new Cookie("remname","");
			remnameC.setPath("/");
			remnameC.setMaxAge(0);
			response.addCookie(remnameC);
		}
		
		//--����30�����Զ���¼
		if("true".equals(request.getParameter("autologin"))){
			Cookie autologinC = new Cookie("autologin",URLEncoder.encode(user.getUsername()+":"+user.getPassword(), "utf-8"));
			//�Զ���¼��Ҫ�û��������룬����cookie��Ҫ�õ���Щ���ݣ����ڿͻ��˽��д���
			autologinC.setPath("/");
			autologinC.setMaxAge(3600*24*30);
			response.addCookie(autologinC);
		}
		
		response.sendRedirect("/index.jsp");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
