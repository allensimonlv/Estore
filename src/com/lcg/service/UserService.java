package com.lcg.service;

import java.util.List;

import com.lcg.annotation.Tran;
import com.lcg.domain.University;
import com.lcg.domain.User;

public interface UserService extends Service{

	/**
	 * ע���û�
	 * @param user ��װ���û����ݵ�userbean
	 */
	@Tran
	void regist(User user);//ע���û���ʱ��Ҫ�õ����񣬵���Tran�İ������Խ��з���ע��

	/**
	 * �����û��ķ���
	 * @param activecode ������
	 */
	void activeUser(String activecode);

	/**
	 * �����û�����������û�
	 * @param username
	 * @param password
	 */
	User getUserByNameAndPsw(String username, String password);

	/**
	 * �����û����Ƿ��Ѿ�����
	 * @param username
	 * @return
	 */
	boolean hasName(String username);

	/**
	 * �Ѵ�ѧ��Ϣ�ó���
	 * */
	List<University> getUniversity(); 
}
