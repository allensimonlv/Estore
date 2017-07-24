package com.lcg.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lcg.domain.OrderListForm;
import com.lcg.domain.User;
import com.lcg.factory.BasicFactory;
import com.lcg.service.OrderService;

public class OrderListServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		OrderService service = BasicFactory.getFactory().getService(OrderService.class);
		//1.��ȡ�û�id
		User user = (User) request.getSession().getAttribute("user");
		int id = user.getId();
		//2.����OrderService�и����û�id��ѯ�û����еĶ����ķ���
		List<OrderListForm> list = service.findOrders(id);
		//3.����request�����ҳ����ʾ
		request.setAttribute("list", list);
		request.getRequestDispatcher("/orderList.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
