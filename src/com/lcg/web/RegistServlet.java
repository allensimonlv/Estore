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
			//1.校验验证码
			String valistr1 = request.getParameter("valistr");//把JSP页面中提交来的验证码数据拿到
			String valistr2 = (String) request.getSession().getAttribute("valistr");//把Servlet中生成的原始验证码数据拿到。因为它是存到了session域里，所以建立session域再拿过来。
			if(valistr1 == null || valistr2 == null || !valistr1.equals(valistr2)){
				//然后比较这两个验证码图片的内容是否相同，不同就说明不正确
				request.setAttribute("msg", "<font color='red'>验证码不正确！</font>");//这个数据是要转发到注册页面上的。
				request.getRequestDispatcher("/regist.jsp").forward(request, response);
				return;
			}
			//2.封装数据*校验数据
			User user = new User();
			BeanUtils.populate(user,request.getParameterMap());//调用处理JavaBean的jar包把JavaBean中的数据封装起来。user中的数据是以Map集合的形式保存的。
			user.setPassword(MD5Utils.md5(user.getPassword()));//对user进行MD5算法加密。
			//3.调用Service层中的方法注册用户
			service.regist(user); 
			//4.回到主页
			response.getWriter().write("注册成功，请到邮箱中进行激活......");
			response.setHeader("Refresh", "3;url=/index.jsp");//设置响应头，控制定时刷新，跳转页面。
			
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
