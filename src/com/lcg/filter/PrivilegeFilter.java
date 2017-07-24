package com.lcg.filter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lcg.domain.User;

public class PrivilegeFilter implements Filter {
	private List<String> admin_list = new ArrayList<String>();
	private List<String> user_list = new ArrayList<String>();
	public void destroy() {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;//向下转型，使用更为具体的对象。
		HttpServletResponse resp = (HttpServletResponse) response;
		String uri = req.getRequestURI();//拿到请求参数的资源路径
		if(admin_list.contains(uri) || user_list.contains(uri)){
			//说明当前资源需要权限
			if(req.getSession(false) == null || req.getSession().getAttribute("user")=="null"){
				response.getWriter().write("当前资源需要权限,请先登录!");
				return;
			}
			User user = (User) req.getSession().getAttribute("user");
			if(admin_list.contains(uri) && "admin".equals(user.getRole())){
				chain.doFilter(request, response);
			}else if(user_list.contains(uri) && "user".equals(user.getRole())){
				chain.doFilter(request, response);
			}else{
				resp.getWriter().write("您不具有对应的权限!!!");
				resp.setHeader("Refresh", "3;url=/index.jsp");
			}
		}else{
			//不需要权限
			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		ServletContext context = filterConfig.getServletContext();
		try {
			BufferedReader adminReader = new BufferedReader(new FileReader(context.getRealPath("WEB-INF/admin.txt")));
			String line = null;
			while((line=adminReader.readLine())!=null){
				admin_list.add(line);
			}
			BufferedReader userReader = new BufferedReader(new FileReader(context.getRealPath("WEB-INF/user.txt")));
			line = null;
			while((line=userReader.readLine())!=null){
				user_list.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
