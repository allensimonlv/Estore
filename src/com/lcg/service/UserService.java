package com.lcg.service;

import java.util.List;

import com.lcg.annotation.Tran;
import com.lcg.domain.University;
import com.lcg.domain.User;

public interface UserService extends Service{

	/**
	 * 注册用户
	 * @param user 封装了用户数据的userbean
	 */
	@Tran
	void regist(User user);//注册用户的时候要用到事务，导入Tran的包，可以进行反射注解

	/**
	 * 激活用户的方法
	 * @param activecode 激活码
	 */
	void activeUser(String activecode);

	/**
	 * 根据用户名密码查找用户
	 * @param username
	 * @param password
	 */
	User getUserByNameAndPsw(String username, String password);

	/**
	 * 检验用户名是否已经存在
	 * @param username
	 * @return
	 */
	boolean hasName(String username);

	/**
	 * 把大学信息拿出来
	 * */
	List<University> getUniversity(); 
}
