package com.lcg.service;

import java.util.List;

import com.lcg.domain.Product;

public interface ProdService extends Service{
	//ע��ӿںͽӿ�֮��ļ̳й�ϵ������extends��
	/**
	 * �����Ʒ
	 * @param prod ��Ʒ��Ϣbean
	 */
	void addProd(Product prod);

	/**
	 * ��ѯ������Ʒ
	 * @return
	 */
	List<Product> findAllProd();

	/**
	 * ����id������Ʒ��Ϣ
	 * @param id
	 * @return
	 */
	Product findProdById(String id);
}
