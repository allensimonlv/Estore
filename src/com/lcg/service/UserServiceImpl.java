package com.lcg.service;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.dbutils.DbUtils;

import com.lcg.dao.UserDao;
import com.lcg.domain.University;
import com.lcg.domain.User;
import com.lcg.factory.BasicFactory;

public class UserServiceImpl implements UserService {
	private UserDao dao = BasicFactory.getFactory().getDao(UserDao.class);
	public void regist(User user) {
		Connection conn = null;//这样做很不好，在Service层中使用了Dao层的对象，会造成耦合。暂时先这样写，先把功能做出来。
		try {
			//1.校验用户名是否已经存在
			if(dao.findUserByName(user.getUsername())!=null){
				throw new RuntimeException("用户名已经存在！");
			}
			
			//2.调用dao中的方法添加用户到数据库
			user.setRole("user");
			user.setState(0);
			user.setActivecode(UUID.randomUUID().toString());
			dao.addUser(user);
			
			//3.发送激活邮件（记得业务逻辑都在Service层中处理）
			Properties prop = new Properties();
			//找到邮局
			prop.setProperty("mail.transport.protocol", "smtp");//协议
			prop.setProperty("mail.smtp.host", "localhost");//主机名
			prop.setProperty("mail.smtp.auth", "true");//是否开启权限控制
			prop.setProperty("mail.debug", "true");//如果设置为true则在发送邮件时会打印发送的信息
			
			//创建程序到邮件服务器之间的一次会话
			Session session = Session.getInstance(prop);
			//获取邮件对象（买个信封）
			Message msg = new MimeMessage(session);//Message是抽象类，但它的子类MimeMessage不是抽象类
			msg.setFrom(new InternetAddress("aa@allensimon.com"));
			msg.setRecipient(RecipientType.TO, new InternetAddress(user.getEmail()));//邮箱不写死，注册时是什么邮箱这里就拿什么邮箱。
			msg.setSubject(user.getUsername()+",来自estore的激活邮件");
			msg.setText(user.getUsername()+",点击如下连接激活账户,如果不能点击请复制到浏览器地址栏访问:http://www.estore.com/ActiveServlet?activecode="+user.getActivecode());
			//找到邮递员
			Transport trans = session.getTransport();
			trans.connect("aa", "123");
			trans.sendMessage(msg, msg.getAllRecipients());
			DbUtils.commitAndCloseQuietly(conn);//提交事务并静悄悄地关闭连接。
		} catch (Exception e) {
			DbUtils.rollbackAndCloseQuietly(conn);//调用框架方法，很方便的关闭连接，以及回滚
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	public void activeUser(String activecode) {
		//1.调用dao对象，根据激活码到Dao层中查找用户
		User user = dao.findUserByActivecode(activecode);
		//2.如果找不到这个用户，提示激活码无效
		if(user == null){
			throw new RuntimeException("激活码无效，此用户不存在！");
		}
		//3.如果用户已经激活过，提示不要重复激活
		if(user.getState()==1){
			throw new RuntimeException("不要重复激活！");
		}
		//4.如果没有激活，但是激活码已经超时，则提示。并删除此用户
		if(System.currentTimeMillis()-user.getUpdatetime().getTime()>1000*3600*24){
			//如果系统当前时间减上用户注册时间大于24小时，则激活码超时，无效。
			dao.delUser(user.getId());
			throw new RuntimeException("激活码已经超时，请重新注册并在24小时内完成注册！");
		}
		//5.调用Dao层中修改用户激活状态的方法。
		dao.updateState(user.getId(), 1);
	}
	public User getUserByNameAndPsw(String username, String password) {
		return dao.findUserByNameAndPsw(username, password);
	}
	public boolean hasName(String username) {
		return dao.findUserByName(username)!=null;
	}
	public List<University> getUniversity() {
		// TODO Auto-generated method stub
		List<University> list = dao.findUniversity();
		return list;
	}

}
