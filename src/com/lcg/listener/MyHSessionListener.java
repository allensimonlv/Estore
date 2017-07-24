package com.lcg.listener;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.lcg.domain.Product;

public class MyHSessionListener implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent se) {
		se.getSession().setAttribute("cartmap", new LinkedHashMap<Product, Integer>());
		//���ﳵcartmap�ڼ��������ʱ����Ѿ�����������Ʒ��Ϣ������������
	}

	public void sessionDestroyed(HttpSessionEvent se) {

	}

}
