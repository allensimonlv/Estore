package com.lcg.web;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lcg.domain.SaleInfo;
import com.lcg.factory.BasicFactory;
import com.lcg.service.OrderService;

public class SaleListServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//1.����Service�в�ѯ���۰񵥵ķ���
		OrderService service = BasicFactory.getFactory().getService(OrderService.class);
		List<SaleInfo> list = service.saleList();
		//2.�����۰���Ϣ��֯��csv��ʽ������
		StringBuffer buffer = new StringBuffer();
		buffer.append("��Ʒ��ţ���Ʒ���ƣ���������\r\n");
		for(SaleInfo si : list){
			buffer.append(si.getProd_id()+","+si.getProd_name()+","+si.getSale_num()+"\r\n");
		}
		String data = buffer.toString();
		//3.�ṩ����
		String filename = "Estore���۰�_"+new Date().toLocaleString()+".csv";
		response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(filename,"utf-8"));
		response.getWriter().write(data);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
