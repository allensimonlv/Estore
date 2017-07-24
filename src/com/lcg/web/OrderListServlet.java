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
		//1.获取用户id
		User user = (User) request.getSession().getAttribute("user");
		int id = user.getId();
		//2.调用OrderService中根据用户id查询用户具有的订单的方法
		List<OrderListForm> list = service.findOrders(id);
		//3.存入request域带到页面显示
		request.setAttribute("list", list);
		request.getRequestDispatcher("/orderList.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
