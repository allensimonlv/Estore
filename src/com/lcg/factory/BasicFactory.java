package com.lcg.factory;

import java.io.FileInputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URLDecoder;
import java.util.Properties;

import com.lcg.annotation.Tran;
import com.lcg.dao.Dao;
import com.lcg.service.Service;
import com.lcg.util.TransactionManager;

public class BasicFactory {
	private static BasicFactory factory = new BasicFactory();
	private static Properties prop = null;
	private BasicFactory(){}
	//�ɹ�����ȥ�������ļ�������������漰��Dao���Service������ݽ���ʱ��ͨ�����������ɵĶ�����С���ͬ�Ĳ�֮�価������ֱ����ϵ��
	static{
		//�ھ�̬������а������ļ�����Ϣ��������
		try {
			prop = new Properties();
			String path = BasicFactory.class.getClassLoader().getResource("config.properties").getPath();
			/*
			 * getResource()�������ص���һ��URL������������ٵ���URL�е�getPath���������Ի��URL��·�����֡�
			 * ��������ļ���srcĿ¼�£�getResource("...")����ַ�����ֱ���������ļ������ơ�
			 * ��������ļ���ĳ�����getResource������Ҫ���ϰ���·������com/..../config.properties
			 * ��������ļ�ֱ�ӷ���WEB-INFĿ¼�£�����tomcat�е�Ӧ��Ŀ¼�ṹ������classesĿ¼����һ������ʱgetResource��Ӧ����../config.properties�������������ļ�����һ����
			 * 
			 * */
			path = URLDecoder.decode(path,"utf-8");
			FileInputStream in = new FileInputStream(path);
			prop.load(in);
			//�ڷ�Servlet�����¶�ȡ��Դ�ļ��������ļ�������Ҫʹ������������Ҵ��и������Ƶ���Դ���ٵ���getPath���������Դ����ʵ·����
			//����������û�и����ӳٵ����⣬���������������getResourceAsStream("...")ʱ�����и����ӳٵ����⣻�Լ�����ļ�̫�󣬻�ռ�ù����ڴ档
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static BasicFactory getFactory(){
		//˭Ҫ�õ�������Ķ��󣬶�Ҫ����getFactory����������ض���
		return factory;
	}
	
	/**
	 * ��ȡService�ķ���
	 */
	/*���´��·־ñغϺϾñط֣���Ϊ�漰�����������̵Ķ�̬��������Ҫ��web���Service���ȡ�²����ķ������뿪����
	 * ���·ֿ�֮�����ڵ�������Ҫ��Service��Ҫ����ҵ���߼�����Ҫ�漰������Ĵ��������ֲ������еķ�������Ҫ��������
	 * �����ö�̬����ķ�����Ҫ����������ģ�ר�Ŵ���������������ģ�ֱ�Ӵ����ȥ���ɡ�
	 * ������Dao�㲻��Ҫ����ҵ���߼��������������ݿ⣬������dao����Ƚϼ򵥡�
	 * 
	 * ����Ҫע�⣬getService/Dao����ʱ����ͷʹ�����ϱ߽緺�͡�����ָ���˷����еķ��ͱ����Ǽ̳��ϱ߽�����͡�
	 * �������Service��Ķ���͵���Dao��Ķ��������
	 * */
	@SuppressWarnings("unchecked")//ѹһ�¾��棬�������澯��̫���ˡ�
	public <T extends Service> T getService(Class<T> clazz){
		try {
			//���������ļ����������Service
			String infName = clazz.getSimpleName();//Class���е������������clazz��������Ӧ�Ľӿڵ����ơ�
			String implName = prop.getProperty(infName);//���ؽӿڶ�Ӧ����ʵ�ֵ����֡�
			final T service = (T) Class.forName(implName).newInstance();//����ʵ����Ķ��󲢷��ء�newInstance������������������Class��������ʾ��һ���µ�ʵ����
			
			//--Ϊ��ʵ��AOP,����service����,����ע��ȷ����Service����ִ��֮ǰ��֮����һЩ����,����:�������/��¼��־/ϸ����Ȩ�޿���....
			//newProxyInstance()�����������һ��ָ���ӿڵĴ�����ʵ�����ýӿڿ��Խ���������ָ�ɵ�ָ���ĵ��ó�����
			T proxyService = (T)Proxy.newProxyInstance(service.getClass().getClassLoader(),service.getClass().getInterfaces(),
					new InvocationHandler(){

						//����ע���������
						public Object invoke(Object proxy, Method method,Object[] args) throws Throwable {
							if(method.isAnnotationPresent(Tran.class)){//�����ע�⣬���������
								try {
									TransactionManager.startTran();//--��������
									Object obj = method.invoke(service, args);//--����ִ�з���
									TransactionManager.commit();//--�ύ����
									return obj;
								} catch (InvocationTargetException e) {
									TransactionManager.rollback();//--�ع�����
									throw new RuntimeException(e.getTargetException());
								} catch (Exception e) {
									TransactionManager.rollback();//--�ع�����
									throw new RuntimeException(e);
								}finally{
									TransactionManager.release();//--�ͷ���Դ���ر�����
								}
							}else{//���û��ע�⣬�򲻹�������ֱ��ִ�з���
								return method.invoke(service, args);
								//ע�⣬�ڲ�������ⲿ��Ķ����ⲿ�����service������final���͵ġ�
							}
						}
				
			});
			return proxyService;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}		
	}
	
	/**
	 * ��ȡdao�ķ���
	 */
	public <T extends Dao> T getDao(Class<T> clazz){
		try {
			String infName = clazz.getSimpleName();//Class���е������������clazz��������Ӧ�Ľӿڵ����ơ�
			String implName = prop.getProperty(infName);//���ؽӿڶ�Ӧ����ʵ�ֵ����֡�
			//�������Щ��䶼Ҫ�õ������ļ�
			return (T) Class.forName(implName).newInstance();//����ʵ����Ķ��󲢷��ء�newInstance������������������Class��������ʾ��һ���µ�ʵ����
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	//���´��·־ñغϺϾñط֣���Ϊ�漰�����������̵Ķ�̬��������Ҫ��web���Service���ȡ�²����ķ������뿪����
//	public <T> T getInstance(Class<T> clazz){
//		try {
//			String infName = clazz.getSimpleName();//Class���е������������clazz��������Ӧ�Ľӿڵ����ơ�
//			String implName = prop.getProperty(infName);//���ؽӿڶ�Ӧ����ʵ�ֵ����֡�
//			return (T) Class.forName(implName).newInstance();//����ʵ����Ķ��󲢷��ء�newInstance������������������Class��������ʾ��һ���µ�ʵ����
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//	}
}
