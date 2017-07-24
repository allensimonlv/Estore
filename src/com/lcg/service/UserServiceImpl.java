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
		Connection conn = null;//�������ܲ��ã���Service����ʹ����Dao��Ķ��󣬻������ϡ���ʱ������д���Ȱѹ�����������
		try {
			//1.У���û����Ƿ��Ѿ�����
			if(dao.findUserByName(user.getUsername())!=null){
				throw new RuntimeException("�û����Ѿ����ڣ�");
			}
			
			//2.����dao�еķ�������û������ݿ�
			user.setRole("user");
			user.setState(0);
			user.setActivecode(UUID.randomUUID().toString());
			dao.addUser(user);
			
			//3.���ͼ����ʼ����ǵ�ҵ���߼�����Service���д���
			Properties prop = new Properties();
			//�ҵ��ʾ�
			prop.setProperty("mail.transport.protocol", "smtp");//Э��
			prop.setProperty("mail.smtp.host", "localhost");//������
			prop.setProperty("mail.smtp.auth", "true");//�Ƿ���Ȩ�޿���
			prop.setProperty("mail.debug", "true");//�������Ϊtrue���ڷ����ʼ�ʱ���ӡ���͵���Ϣ
			
			//���������ʼ�������֮���һ�λỰ
			Session session = Session.getInstance(prop);
			//��ȡ�ʼ���������ŷ⣩
			Message msg = new MimeMessage(session);//Message�ǳ����࣬����������MimeMessage���ǳ�����
			msg.setFrom(new InternetAddress("aa@allensimon.com"));
			msg.setRecipient(RecipientType.TO, new InternetAddress(user.getEmail()));//���䲻д����ע��ʱ��ʲô�����������ʲô���䡣
			msg.setSubject(user.getUsername()+",����estore�ļ����ʼ�");
			msg.setText(user.getUsername()+",����������Ӽ����˻�,������ܵ���븴�Ƶ��������ַ������:http://www.estore.com/ActiveServlet?activecode="+user.getActivecode());
			//�ҵ��ʵ�Ա
			Transport trans = session.getTransport();
			trans.connect("aa", "123");
			trans.sendMessage(msg, msg.getAllRecipients());
			DbUtils.commitAndCloseQuietly(conn);//�ύ���񲢾����ĵعر����ӡ�
		} catch (Exception e) {
			DbUtils.rollbackAndCloseQuietly(conn);//���ÿ�ܷ������ܷ���Ĺر����ӣ��Լ��ع�
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	public void activeUser(String activecode) {
		//1.����dao���󣬸��ݼ����뵽Dao���в����û�
		User user = dao.findUserByActivecode(activecode);
		//2.����Ҳ�������û�����ʾ��������Ч
		if(user == null){
			throw new RuntimeException("��������Ч�����û������ڣ�");
		}
		//3.����û��Ѿ����������ʾ��Ҫ�ظ�����
		if(user.getState()==1){
			throw new RuntimeException("��Ҫ�ظ����");
		}
		//4.���û�м�����Ǽ������Ѿ���ʱ������ʾ����ɾ�����û�
		if(System.currentTimeMillis()-user.getUpdatetime().getTime()>1000*3600*24){
			//���ϵͳ��ǰʱ������û�ע��ʱ�����24Сʱ���򼤻��볬ʱ����Ч��
			dao.delUser(user.getId());
			throw new RuntimeException("�������Ѿ���ʱ��������ע�Ტ��24Сʱ�����ע�ᣡ");
		}
		//5.����Dao�����޸��û�����״̬�ķ�����
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
