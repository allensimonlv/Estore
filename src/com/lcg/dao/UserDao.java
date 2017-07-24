package com.lcg.dao;

import java.util.List;

import com.lcg.domain.University;
import com.lcg.domain.User;

public interface UserDao extends Dao{

	/**
	 * �����û��������û�
	 * @param username �û���
	 * @param conn 
	 * @return ���ҵ����û�,����Ҳ�������null
 	 */
	User findUserByName(String username);

	/**
	 * ����û�
	 * @param user ��װ���û���Ϣ��bean
	 * @param conn 
	 */
	void addUser(User user);

	/**
	 * ���ݼ���������û�
	 * @param activecode ������
	 * @return �ҵ����û�,��������ڷ���null
	 */
	User findUserByActivecode(String activecode);

	/**
	 * ����idɾ���û�
	 * @param id Ҫɾ�����û�id
	 */
	void delUser(int id);

	/**
	 * �޸�ָ��id�ͻ���״̬
	 * @param id �ͻ�id
	 * @param i Ҫ���µĿͻ�״̬
	 */
	void updateState(int id, int state);
	
	/**
	 * �����û�����������û�
	 * @param username �û���
	 * @param password ����
	 * @return �ҵ����û�bean
	 */
	User findUserByNameAndPsw(String username, String password);

	/**
	 * ����id�����û�
	 * @param user_id
	 * @return
	 */
	User findUserById(int user_id);

	/**
	 * ȡ����ѧ��Ϣ
	 * */
	List<University> findUniversity();
}
