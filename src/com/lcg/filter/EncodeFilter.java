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
	private FilterConfig config = null;//FilterConfig:����web.xml�жԵ�ǰ��������������Ϣ
	private ServletContext context = null;//ServletContext:����ǰ��webӦ�á�
	private String encode = null;//����Ҫ�õ��ı��뷽ʽ�Ķ���
	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		//��Ӧ����Ľ��
		response.setCharacterEncoding(encode);//���÷������˵ı��뷽ʽ
		response.setContentType("text/html;charset="+encode);//֪ͨ������˵ı��뷽ʽ
		//����������룺����װ�����ģʽ�ı�request����ͻ�ȡ���������صķ������Ӷ������������⡣����˵request��GET�ύ��POST�ύ�ı������ò�һ������Ҫͨ��װ�����ģʽ�����Ǳ�Ϊ����ͨ�õĽ��������
		chain.doFilter(new MyHttpServletRequest((HttpServletRequest) request), response);
		//����һ��Ҫע�⣡����ʱ��request�������ڲ��ദ����Ķ��󣬵�����Ҫnewһ���ڲ��࣬�ٽ������ǿת�����⣡����������
		//��FilterChain���������doFilter����ʱ���ͱ�������������û�����⣬����Դ���е���һ���ڵ㡣
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.config = filterConfig;
		this.context = filterConfig.getServletContext();//��ȡServletContext����
		this.encode = filterConfig.getInitParameter("encode") == null ? "utf-8" : config.getInitParameter("encode");//��ȡ��ʼ���Ĳ�������Ϣ,���encode��������web.xml��������
		//ע�������85��һ����Ҳ�������ж�encode�Ƿ�Ϊnull����䡣������һΪ�յĻ�������ͻ��׳���ָ���쳣������������
	}
	
	private class MyHttpServletRequest extends HttpServletRequestWrapper{
		//д����ڲ������Ϊ�˽��request��GET��ʽ��POST�ύ��ʽ�������뷽����ͬ�����⡣
		private HttpServletRequest request = null;
		private boolean isNotEncode = true;
		
		public MyHttpServletRequest(HttpServletRequest request) {	
			super(request);//�������ķ���ֱ�ӴӸ���̳�
			this.request = request;
		}

		@Override
		//�����ķ����ٽ��и����޸ġ�
		public Map<String, String[]> getParameterMap() {
			try {
				if(request.getMethod().equalsIgnoreCase("POST")){//--�����post�ύ,һ�д�����post�ύ�����������
					request.setCharacterEncoding(encode);
					return request.getParameterMap();					
				}else if(request.getMethod().equalsIgnoreCase("GET")){
					//--�����get�ύ,��Ӧ���ֶ������������
					Map<String, String[]> map = request.getParameterMap();//��ȡ�������map
					if(isNotEncode){//ֻ���ڵ�һ�ν�����룬һ�ν���͹��ˡ�
						for(Map.Entry<String, String[]> entry : map.entrySet()){//����map,�������ֵ������
							//entrySet()����������ֵ��ӳ���ϵ��Ϊ����洢����Set�����У������ӳ���ϵ�����;���Map.Entry���͡�
							String [] vs = entry.getValue();
							for(int i=0; i<vs.length; i++){
								vs[i] = new String(vs[i].getBytes("iso8859-1"),encode);
							}
						}
						isNotEncode = false;//����Ϊfalse,�ڶ��ξͲ����ٽ�����������
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
			//��ȡ�ļ���String���͵ġ�������һ���жϣ���ֹ��ָ���쳣
			return getParameterValues(name) == null ? null : getParameterValues(name)[0];
		}

		@Override
		public String[] getParameterValues(String name) {
			//��ȡ��ֵ���ַ����������͵ġ�
			return getParameterMap().get(name);
		}
		
	}

}
