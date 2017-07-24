package com.lcg.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class EncodeFilter implements Filter {
	private FilterConfig config = null;//FilterConfig:代表web.xml中对当前过滤器的配置信息
	private ServletContext context = null;//ServletContext:代表当前的web应用。
	private String encode = null;//下面要用到的编码方式的对象
	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		//响应乱码的解决
		response.setCharacterEncoding(encode);//设置服务器端的编码方式
		response.setContentType("text/html;charset="+encode);//通知浏览器端的编码方式
		//解决请求乱码：利用装饰设计模式改变request对象和获取请求参数相关的方法，从而解决乱码的问题。就是说request的GET提交和POST提交的编码设置不一样，需要通过装饰设计模式把它们变为可以通用的解决方案。
		chain.doFilter(new MyHttpServletRequest((HttpServletRequest) request), response);
		//这里一定要注意！！此时的request对象是内部类处理过的对象，调用它要new一下内部类，再解决类型强转的问题！！！！！！
		//当FilterChain对象调用了doFilter方法时，就表明过滤器环节没有问题，把资源放行到下一个节点。
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.config = filterConfig;
		this.context = filterConfig.getServletContext();//获取ServletContext对象
		this.encode = filterConfig.getInitParameter("encode") == null ? "utf-8" : config.getInitParameter("encode");//获取初始化的参数的信息,这个encode的内容在web.xml中已配置
		//注意这里和85行一样，也必须有判断encode是否为null的语句。否则万一为空的话，程序就会抛出空指针异常！！！！！！
	}
	
	private class MyHttpServletRequest extends HttpServletRequestWrapper{
		//写这个内部类就是为了解决request中GET方式和POST提交方式处理乱码方法不同的问题。
		private HttpServletRequest request = null;
		private boolean isNotEncode = true;
		
		public MyHttpServletRequest(HttpServletRequest request) {	
			super(request);//不想改造的方法直接从父类继承
			this.request = request;
		}

		@Override
		//想改造的方法再进行覆盖修改。
		public Map<String, String[]> getParameterMap() {
			try {
				if(request.getMethod().equalsIgnoreCase("POST")){//--如果是post提交,一行代码解决post提交请求参数乱码
					request.setCharacterEncoding(encode);
					return request.getParameterMap();					
				}else if(request.getMethod().equalsIgnoreCase("GET")){
					//--如果是get提交,则应该手动编解码解决乱码
					Map<String, String[]> map = request.getParameterMap();//获取有乱码的map
					if(isNotEncode){//只能在第一次解决乱码，一次解决就够了。
						for(Map.Entry<String, String[]> entry : map.entrySet()){//遍历map,解决所有值的乱码
							//entrySet()方法将键和值的映射关系作为对象存储到了Set集合中，而这个映射关系的类型就是Map.Entry类型。
							String [] vs = entry.getValue();
							for(int i=0; i<vs.length; i++){
								vs[i] = new String(vs[i].getBytes("iso8859-1"),encode);
							}
						}
						isNotEncode = false;//设置为false,第二次就不会再进这个代码块了
					}
					return map;
				}else{
					return request.getParameterMap();
				}				
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		@Override
		public String getParameter(String name) {
			//获取的键是String类型的。这里做一个判断，防止空指针异常
			return getParameterValues(name) == null ? null : getParameterValues(name)[0];
		}

		@Override
		public String[] getParameterValues(String name) {
			//获取的值是字符串数组类型的。
			return getParameterMap().get(name);
		}
		
	}

}
