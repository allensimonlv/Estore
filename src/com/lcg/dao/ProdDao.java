package com.lcg.dao;

import java.sql.SQLException;
import java.util.List;

import com.lcg.domain.Product;

public interface ProdDao extends Dao {
	/**
	 * �����Ʒ
	 * @param prod
	 */
	void addProd(Product prod);

	/**
	 * ��ѯ����
	 * @return
	 */
	List<Product> findAllProd();

	/**
	 * ����id������Ʒ
	 * @param id
	 * @return
	 */
	Product findProdById(String id);

	/**
	 * �۳���Ʒ�������
	 * @param product_id
	 * @param buynum
	 * @throws SQLException 
	 */
	void delPnum(String product_id, int buynum);


	void addPnum(String product_id, int buynum);
}
