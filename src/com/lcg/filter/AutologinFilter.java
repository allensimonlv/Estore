package com.lcg.filter;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lcg.domain.User;
import com.lcg.factory.BasicFactory;
import com.lcg.service.UserService;

public class AutologinFilter implements Filter {

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		//只有未登陆的用户才自动登陆
		if(req.getSession(false)==null || req.getSession().getAttribute("user")==null){
			//2.只有带了自动登录cookie的用户才自动登录。
			Cookie [] cs = req.getCookies();
			Cookie findC = null;
			if(cs!=null){
				for(Cookie c : cs){
					if("autologin".equals(c.getName())){
						findC = c;
						break;//找到了这个用户，就跳出循环
					}
				}
			}
			if(findC!=null){
				//3.只有自动登录cookie中的用户名密码都正确才做自动登录操作
				String v = URLDecoder.decode(findC.getValue(),"utf-8");//把cookie里面的值用utf-8进行解码。
				String username = v.split(":")[0];
				String password = v.split(":")[1];//这些内容和LoginServlet的第58行相对应。一个建立cookie编码，一个拿到此cookie解码。
				UserService service = BasicFactory.getFactory().getService(UserService.class);
				User user = service.getUserByNameAndPsw(username, password);
				if(user!=null){
					req.getSession().setAttribute("user", user);
				}
			}
		}
		//4.无论是否登录都要放行
		chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

}
