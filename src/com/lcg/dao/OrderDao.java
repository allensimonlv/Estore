package com.lcg.dao;

import java.sql.SQLException;
import java.util.List;

import com.lcg.domain.Order;
import com.lcg.domain.OrderItem;
import com.lcg.domain.SaleInfo;

public interface OrderDao extends Dao{

	/**
	 * �ڶ������в����¼
	 * @param order
	 * @throws SQLException 
	 */
	void addOrder(Order order) throws SQLException;

	/**
	 * �ڶ�������в����¼
	 * @param item
	 * @throws SQLException 
	 */
	void addOrderItem(OrderItem item) throws SQLException;

	/**
	 * ��ѯָ���û������ж���
	 * @param user_id Ҫ��ѯ���û�id
	 * @return ��������û�������Ϣ��ɵļ���
	 */
	List<Order> findOrderByUserId(int user_id);

	/**
	 * ��ѯָ�����������ж�����
	 * @param id
	 * @return
	 */
	List<OrderItem> findOrderItems(String id);

	/**
	 * ɾ��ָ��id���������������ж�����
	 * @param order_id ����id
	 */
	void delOrderItem(String order_id);

	/**
	 * ɾ��ָ��id �Ķ���
	 * @param id
	 */
	void delOrder(String id);

	/**
	 * ����id��ѯ����
	 * @param order_id
	 * @return
	 */
	Order findOrderById(String order_id);

	/**
	 * �޸Ķ���֧��״̬
	 * @param order
	 * @param i
	 */
	void changePayState(String order, int i);

	/**
	 * ��ѯ���۰�
	 * @return
	 */
	List<SaleInfo> saleList();

}
