package com.lcg.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.lcg.domain.Order;
import com.lcg.domain.OrderItem;
import com.lcg.domain.Product;
import com.lcg.domain.User;
import com.lcg.factory.BasicFactory;
import com.lcg.service.OrderService;

public class AddOrderServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		OrderService service = BasicFactory.getFactory().getService(OrderService.class);
		try {
			//1.将订单信息存入Orderbean中
			Order order = new Order();
			//--订单编号
			order.setId(UUID.randomUUID().toString());
			//--支付状态
			order.setPaystate(0);
			//--收货地址
			BeanUtils.populate(order,request.getParameterMap());
			//request调用getParameterMap()这个方法，可以获取所有请求参数组成的集合，键是String类型，值是String[]类型
			//--金额->将订单信息存入order中，前台的计算金额其实是不太能相信的，因为有可能会有人修改浏览器设置。所以金额一定要后台校验。
			Map<Product, Integer> cartmap = (Map<Product, Integer>) request.getSession().getAttribute("cartmap");
			double money = 0;
			List<OrderItem> list = new ArrayList<OrderItem>();
			for(Map.Entry<Product, Integer> entry : cartmap.entrySet()){
				money += entry.getKey().getPrice() * entry.getValue();
				//entry对象拿到键之后，再获取Productbean中的单价参数，再乘以购买的商品数量。循环之后就得到了够买总商品所需的价格。
				OrderItem item = new OrderItem();
				item.setOrder_id(order.getId());//从订单bean中拿到订单编号，存入OrderItembean中。OrderItembean其实就是多对多关系中的一个第三方中介表。存储着两个表的键值以及相关联的一些信息。
				item.setProduct_id(entry.getKey().getId());
				item.setBuynum(entry.getValue());
				list.add(item);
			}
			order.setMoney(money);//开始存钱 
			order.setList(list);//把OrderItem中的信息也存入。
			//--客户编号
			User user = (User) request.getSession().getAttribute("user");
			order.setUser_id(user.getId());
			//2.调用Service中添加订单的方法
			service.addOrder(order);
			//3.清空购物车
			cartmap.clear();
			//4.回到购物车
			response.getWriter().write("订单生成成功，请去支付！");
			response.setHeader("Refresh", "3;url=/index.jsp");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
