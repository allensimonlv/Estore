package com.lcg.service;

import java.util.List;

import com.lcg.annotation.Tran;
import com.lcg.domain.Order;
import com.lcg.domain.OrderListForm;
import com.lcg.domain.SaleInfo;

public interface OrderService extends Service{

	/**
	 * ���Ӷ���
	 * @param order ����bean
	 */
	@Tran
	void addOrder(Order order);

	/**
	 * ��ѯָ���û����ж����ķ���
	 * @param user_id
	 * @return ���ҵ�������
	 */
	List<OrderListForm> findOrders(int user_id);

	/**
	 * ���ݶ������ɾ������
	 * @param id
	 */
	@Tran
	void delOrderByID(String id);

	/**
	 * ����id��ѯ����
	 * @param id
	 * @return
	 */
	Order findOrderById(String order);

	/**
	 * �޸�ָ��id������֧��״̬
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
