package com.lcg.service;

import java.util.List;
import java.util.UUID;

import com.lcg.dao.ProdDao;
import com.lcg.domain.Product;
import com.lcg.factory.BasicFactory;

public class ProdServiceImpl implements ProdService {
	ProdDao dao = BasicFactory.getFactory().getDao(ProdDao.class);
	public void addProd(Product prod) {
		prod.setId(UUID.randomUUID().toString());//使用UUID设置主键，保证是独一无二的。这里注意数据表中的id是String类型的，所以最后还要toString一下。
		dao.addProd(prod);
	}

	public List<Product> findAllProd() {
		return dao.findAllProd();
	}

	public Product findProdById(String id) {
		return dao.findProdById(id);
	}

}
