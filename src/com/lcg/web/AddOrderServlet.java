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
			//1.��������Ϣ����Orderbean��
			Order order = new Order();
			//--�������
			order.setId(UUID.randomUUID().toString());
			//--֧��״̬
			order.setPaystate(0);
			//--�ջ���ַ
			BeanUtils.populate(order,request.getParameterMap());
			//request����getParameterMap()������������Ի�ȡ�������������ɵļ��ϣ�����String���ͣ�ֵ��String[]����
			//--���->��������Ϣ����order�У�ǰ̨�ļ�������ʵ�ǲ�̫�����ŵģ���Ϊ�п��ܻ������޸���������á����Խ��һ��Ҫ��̨У�顣
			Map<Product, Integer> cartmap = (Map<Product, Integer>) request.getSession().getAttribute("cartmap");
			double money = 0;
			List<OrderItem> list = new ArrayList<OrderItem>();
			for(Map.Entry<Product, Integer> entry : cartmap.entrySet()){
				money += entry.getKey().getPrice() * entry.getValue();
				//entry�����õ���֮���ٻ�ȡProductbean�еĵ��۲������ٳ��Թ������Ʒ������ѭ��֮��͵õ��˹�������Ʒ����ļ۸�
				OrderItem item = new OrderItem();
				item.setOrder_id(order.getId());//�Ӷ���bean���õ�������ţ�����OrderItembean�С�OrderItembean��ʵ���Ƕ�Զ��ϵ�е�һ���������н���洢��������ļ�ֵ�Լ��������һЩ��Ϣ��
				item.setProduct_id(entry.getKey().getId());
				item.setBuynum(entry.getValue());
				list.add(item);
			}
			order.setMoney(money);//��ʼ��Ǯ 
			order.setList(list);//��OrderItem�е���ϢҲ���롣
			//--�ͻ����
			User user = (User) request.getSession().getAttribute("user");
			order.setUser_id(user.getId());
			//2.����Service����Ӷ����ķ���
			service.addOrder(order);
			//3.��չ��ﳵ
			cartmap.clear();
			//4.�ص����ﳵ
			response.getWriter().write("�������ɳɹ�����ȥ֧����");
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
