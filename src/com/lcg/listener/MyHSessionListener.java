package com.lcg.listener;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.lcg.domain.Product;

public class MyHSessionListener implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent se) {
		se.getSession().setAttribute("cartmap", new LinkedHashMap<Product, Integer>());
		//购物车cartmap在监听器这个时候就已经创建，把商品信息给保存起来。
	}

	public void sessionDestroyed(HttpSessionEvent se) {

	}

}
