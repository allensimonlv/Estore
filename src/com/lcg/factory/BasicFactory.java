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
	//由工厂类去把配置文件读出来，如果涉及到Dao层和Service层的数据交互时，通过工厂类生成的对象进行。不同的层之间尽量不会直接联系。
	static{
		//在静态代码块中把配置文件的信息读出来。
		try {
			prop = new Properties();
			String path = BasicFactory.class.getClassLoader().getResource("config.properties").getPath();
			/*
			 * getResource()方法返回的是一个URL对象，这个对象再调用URL中的getPath方法，可以获得URL的路径部分。
			 * 如果配置文件在src目录下，getResource("...")里的字符串就直接是配置文件的名称。
			 * 如果配置文件在某个包里，getResource方法中要加上包的路径名：com/..../config.properties
			 * 如果配置文件直接放在WEB-INF目录下，根据tomcat中的应用目录结构，它是classes目录的上一级，这时getResource里应该是../config.properties。代表返回配置文件的上一级。
			 * 
			 * */
			path = URLDecoder.decode(path,"utf-8");
			FileInputStream in = new FileInputStream(path);
			prop.load(in);
			//在非Servlet环境下读取资源文件（配置文件），就要使用类加载器查找带有给定名称的资源，再调用getPath方法获得资源的真实路径。
			//而且这样做没有更新延迟的问题，不像类加载器调用getResourceAsStream("...")时，会有更新延迟的问题；以及如果文件太大，会占用过多内存。
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static BasicFactory getFactory(){
		//谁要用到工厂类的对象，都要调用getFactory这个方法返回对象。
		return factory;
	}
	
	/**
	 * 获取Service的方法
	 */
	/*天下大事分久必合合久必分，因为涉及到面向切面编程的动态代理。所以要把web层和Service层获取下层对象的方法分离开来。
	 * 重新分开之后，现在的问题主要是Service层要处理业务逻辑，需要涉及到事务的处理，但是又不是所有的方法都需要处理事务，
	 * 所以用动态代理的方案，要进行事务处理的，专门处理。不进行事务处理的，直接代理回去即可。
	 * 由于在Dao层不需要处理业务逻辑，仅是连接数据库，所以拿dao对象比较简单。
	 * 
	 * 这里要注意，getService/Dao方法时方法头使用了上边界泛型。这样指定了方法中的泛型必须是继承上边界的类型。
	 * 以免调用Service层的对象和调用Dao层的对象混淆。
	 * */
	@SuppressWarnings("unchecked")//压一下警告，否则下面警告太多了。
	public <T extends Service> T getService(Class<T> clazz){
		try {
			//根据配置文件创建具体的Service
			String infName = clazz.getSimpleName();//Class类中的这个方法返回clazz对象所对应的接口的名称。
			String implName = prop.getProperty(infName);//返回接口对应的类实现的名字。
			final T service = (T) Class.forName(implName).newInstance();//生成实现类的对象并返回。newInstance这个方法代表创建出这个Class对象所表示的一个新的实例。
			
			//--为了实现AOP,生成service代理,根据注解确定在Service方法执行之前和之后做一些操作,比如:事务管理/记录日志/细粒度权限控制....
			//newProxyInstance()这个方法返回一个指定接口的代理类实例，该接口可以将方法调用指派到指定的调用程序处理。
			T proxyService = (T)Proxy.newProxyInstance(service.getClass().getClassLoader(),service.getClass().getInterfaces(),
					new InvocationHandler(){

						//根据注解控制事务
						public Object invoke(Object proxy, Method method,Object[] args) throws Throwable {
							if(method.isAnnotationPresent(Tran.class)){//如果有注解，则管理事务。
								try {
									TransactionManager.startTran();//--开启事务
									Object obj = method.invoke(service, args);//--真正执行方法
									TransactionManager.commit();//--提交事务
									return obj;
								} catch (InvocationTargetException e) {
									TransactionManager.rollback();//--回滚事务
									throw new RuntimeException(e.getTargetException());
								} catch (Exception e) {
									TransactionManager.rollback();//--回滚事务
									throw new RuntimeException(e);
								}finally{
									TransactionManager.release();//--释放资源，关闭连接
								}
							}else{//如果没有注解，则不管理事务，直接执行方法
								return method.invoke(service, args);
								//注意，内部类调用外部类的对象，外部类对象service必须是final类型的。
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
	 * 获取dao的方法
	 */
	public <T extends Dao> T getDao(Class<T> clazz){
		try {
			String infName = clazz.getSimpleName();//Class类中的这个方法返回clazz对象所对应的接口的名称。
			String implName = prop.getProperty(infName);//返回接口对应的类实现的名字。
			//上面的这些语句都要用到配置文件
			return (T) Class.forName(implName).newInstance();//生成实现类的对象并返回。newInstance这个方法代表创建出这个Class对象所表示的一个新的实例。
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	//天下大事分久必合合久必分，因为涉及到面向切面编程的动态代理。所以要把web层和Service层获取下层对象的方法分离开来。
//	public <T> T getInstance(Class<T> clazz){
//		try {
//			String infName = clazz.getSimpleName();//Class类中的这个方法返回clazz对象所对应的接口的名称。
//			String implName = prop.getProperty(infName);//返回接口对应的类实现的名字。
//			return (T) Class.forName(implName).newInstance();//生成实现类的对象并返回。newInstance这个方法代表创建出这个Class对象所表示的一个新的实例。
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//	}
}
