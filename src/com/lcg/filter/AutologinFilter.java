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
		//ֻ��δ��½���û����Զ���½
		if(req.getSession(false)==null || req.getSession().getAttribute("user")==null){
			//2.ֻ�д����Զ���¼cookie���û����Զ���¼��
			Cookie [] cs = req.getCookies();
			Cookie findC = null;
			if(cs!=null){
				for(Cookie c : cs){
					if("autologin".equals(c.getName())){
						findC = c;
						break;//�ҵ�������û���������ѭ��
					}
				}
			}
			if(findC!=null){
				//3.ֻ���Զ���¼cookie�е��û������붼��ȷ�����Զ���¼����
				String v = URLDecoder.decode(findC.getValue(),"utf-8");//��cookie�����ֵ��utf-8���н��롣
				String username = v.split(":")[0];
				String password = v.split(":")[1];//��Щ���ݺ�LoginServlet�ĵ�58�����Ӧ��һ������cookie���룬һ���õ���cookie���롣
				UserService service = BasicFactory.getFactory().getService(UserService.class);
				User user = service.getUserByNameAndPsw(username, password);
				if(user!=null){
					req.getSession().setAttribute("user", user);
				}
			}
		}
		//4.�����Ƿ��¼��Ҫ����
		chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

}
