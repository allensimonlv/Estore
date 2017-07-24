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
			//传入了一个Connection，那这几条sql语句就在一个事务中了。因为在一个事务中，所以不需要再把数据源new进来了，直接在update或query方法中添加conn对象即可。
			//如果没有conn这个对象，QueryRunner是需要把数据源new进来的。
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
			//非常注意：之所以这前两个方法都引入了conn对象，而后面的方法没有引入。是因为在注册用户的时候用到了事务。后面没有用到事务。
			return runner.query(sql, new BeanHandler<User>(User.class),username);
			//BeanHandler对象：将结果集中的每一行数据封装到一个对应的JavaBean实例当中。
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
