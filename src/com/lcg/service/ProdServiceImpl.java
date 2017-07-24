package com.lcg.service;

import java.util.List;
import java.util.UUID;

import com.lcg.dao.ProdDao;
import com.lcg.domain.Product;
import com.lcg.factory.BasicFactory;

public class ProdServiceImpl implements ProdService {
	ProdDao dao = BasicFactory.getFactory().getDao(ProdDao.class);
	public void addProd(Product prod) {
		prod.setId(UUID.randomUUID().toString());//ʹ��UUID������������֤�Ƕ�һ�޶��ġ�����ע�����ݱ��е�id��String���͵ģ��������ҪtoStringһ�¡�
		dao.addProd(prod);
	}

	public List<Product> findAllProd() {
		return dao.findAllProd();
	}

	public Product findProdById(String id) {
		return dao.findProdById(id);
	}

}
