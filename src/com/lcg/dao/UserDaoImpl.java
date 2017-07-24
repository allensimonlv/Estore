package com.lcg.dao;

import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.lcg.domain.University;
import com.lcg.domain.User;
import com.lcg.util.TransactionManager;

public class UserDaoImpl implements UserDao {

	public void addUser(User user) {
		String sql = "insert into users values(null,?,?,?,?,?,?,?,null,?)";
		try {
			//������һ��Connection�����⼸��sql������һ���������ˡ���Ϊ��һ�������У����Բ���Ҫ�ٰ�����Դnew�����ˣ�ֱ����update��query���������conn���󼴿ɡ�
			//���û��conn�������QueryRunner����Ҫ������Դnew�����ġ�
			QueryRunner runner = new QueryRunner(TransactionManager.getSource());
			runner.update(sql,user.getUsername(),user.getPassword(),user.getNickname(),
				user.getEmail(),user.getRole(),user.getState(),user.getActivecode(),user.getUniversity());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

	public User findUserByName(String username) {
		String sql = "select * from users where username=?";
		try {
			QueryRunner runner = new QueryRunner(TransactionManager.getSource());
			//�ǳ�ע�⣺֮������ǰ����������������conn���󣬶�����ķ���û�����롣����Ϊ��ע���û���ʱ���õ������񡣺���û���õ�����
			return runner.query(sql, new BeanHandler<User>(User.class),username);
			//BeanHandler���󣺽�������е�ÿһ�����ݷ�װ��һ����Ӧ��JavaBeanʵ�����С�
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void delUser(int id) {
		String sql = "delete from users where id = ?";
		try {
			QueryRunner runner = new QueryRunner(TransactionManager.getSource());
			runner.update(sql, id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public User findUserByActivecode(String activecode) {
		String sql = "select * from users where activecode = ?";
		try {
			QueryRunner runner = new QueryRunner(TransactionManager.getSource());			
			return runner.query(sql, new BeanHandler<User>(User.class),activecode);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public User findUserByNameAndPsw(String username, String password) {
		String sql = "select * from users where username=? and password=?";
		try {
			QueryRunner runner = new QueryRunner(TransactionManager.getSource());
			return runner.query(sql, new BeanHandler<User>(User.class),username, password);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void updateState(int id, int state) {
		String sql = "update users set state = ? where id = ?";
		try {
			QueryRunner runner = new QueryRunner(TransactionManager.getSource());
			runner.update(sql, state, id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

	public User findUserById(int user_id) {
		String sql = "select * from users where id = ?";
		try {
			QueryRunner runner = new QueryRunner(TransactionManager.getSource());
			return runner.query(sql, new BeanHandler<User>(User.class),user_id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<University> findUniversity() {
		String sql = "select * from university";
		try {
			QueryRunner runner = new QueryRunner(TransactionManager.getSource());
			return runner.query(sql, new BeanListHandler<University>(University.class));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
