package com.lcg.service;

import java.util.List;

import com.lcg.domain.Product;

public interface ProdService extends Service{
	//注意接口和接口之间的继承关系还是用extends。
	/**
	 * 添加商品
	 * @param prod 商品信息bean
	 */
	void addProd(Product prod);

	/**
	 * 查询所有商品
	 * @return
	 */
	List<Product> findAllProd();

	/**
	 * 根据id查找商品信息
	 * @param id
	 * @return
	 */
	Product findProdById(String id);
}
