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
		//1.获取用户名密码
		String username = request.getParameter("username");
		String password = MD5Utils.md5(request.getParameter("password"));
		//2.调用Service中的根据用户名和密码查找用户的方法
		User user = service.getUserByNameAndPsw(username, password);
		if(user == null){
			request.setAttribute("msg", "用户名密码不正确，没有此用户！");
			request.getRequestDispatcher("/login.jsp").forward(request, response);//请求转发时使用的是虚拟路径，用的是绝对路径，即以/开头。
			return;//这里返回一下，省得请求转发语句再被用到。
		}
		//3.检查用户激活状态
		if(user.getState() == 0){
			request.setAttribute("msg", "用户尚未激活，请到邮箱中进行激活！");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			return;
		}
		//4.登录用户重定向到主页
		request.getSession().setAttribute("user", user);
		//--处理记住用户名，这里只能用cookie去长期保存用户名。因为session的留存时间是很短的。session的生命周期默认仅为30分钟。
		if("true".equals(request.getParameter("remname"))){
			//if中的条件写成这样，要参考login.jsp文件中对记住用户名这一模块的参数设定。
			Cookie remnameC = new Cookie("remname",URLEncoder.encode(user.getUsername(),"utf-8"));//用URL编码把用户名包起来，把用户名换成用utf-8字符集表示。
			remnameC.setPath("/");
			remnameC.setMaxAge(3600*24*30);//设置保存时间为一个月
			response.addCookie(remnameC);//把cookie信息存储到response信息里面去。再打回给浏览器。
		}else{
			//如果没有勾选记住用户名这一项，就不保存用户名，cookie信息设置为空。
			//如果没有勾选此项，else中不处理也不好。不设置cookie的MaxAge的话，这个cookie就是一个会话级别的cookie。会临时保存在打开的浏览器内存中。设置为0，同名同path的cookie就会被删除。
			Cookie remnameC = new Cookie("remname","");
			remnameC.setPath("/");
			remnameC.setMaxAge(0);
			response.addCookie(remnameC);
		}
		
		//--处理30天内自动登录
		if("true".equals(request.getParameter("autologin"))){
			Cookie autologinC = new Cookie("autologin",URLEncoder.encode(user.getUsername()+":"+user.getPassword(), "utf-8"));
			//自动登录需要用户名和密码，所以cookie需要拿到这些内容，再在客户端进行处理。
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
