package com.lcg.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.lcg.dao.OrderDao;
import com.lcg.dao.ProdDao;
import com.lcg.dao.UserDao;
import com.lcg.domain.Order;
import com.lcg.domain.OrderItem;
import com.lcg.domain.OrderListForm;
import com.lcg.domain.Product;
import com.lcg.domain.SaleInfo;
import com.lcg.domain.User;
import com.lcg.factory.BasicFactory;

public class OrderServiceImpl implements OrderService {

	OrderDao orderDao = BasicFactory.getFactory().getDao(OrderDao.class);
	ProdDao prodDao = BasicFactory.getFactory().getDao(ProdDao.class);
	UserDao userDao = BasicFactory.getFactory().getDao(UserDao.class);
	public void addOrder(Order order) {
		try {
			//1.生成订单
			orderDao.addOrder(order);
			//2.生成订单项，以及扣除商品数量
			for(OrderItem item : order.getList()){
				orderDao.addOrderItem(item);
				prodDao.delPnum(item.getProduct_id(),item.getBuynum());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

	public List<OrderListForm> findOrders(int user_id) {
		try {
			List<OrderListForm> olfList = new ArrayList<OrderListForm>();
			//1.根据用户id查询这个id用户的所有订单
			List<Order> list = orderDao.findOrderByUserId(user_id);
			//2.遍历订单 生成orderListForm对象,存入List中，也就是存入orderListForm JavaBean里面
			for(Order order : list){
				//--设置订单信息
				OrderListForm olf = new OrderListForm();
				BeanUtils.copyProperties(olf, order);
				//找到Order中所有与OrderListForm所对应的属性，然后把这些属性存到OrderListForm中。用于两个bean之间的操作。
				//--设置用户名
				//通过在userDao中新创建一个方法findUserById来，把相应id的用户拿到。
				User user = userDao.findUserById(order.getUser_id());
				olf.setUsername(user.getUsername());
				//--设置商品信息
				//----查询当前订单所有订单项
				List<OrderItem> itemList = orderDao.findOrderItems(order.getId());
				//----遍历所有订单项,获取商品id,查找对应的商品,存入list
				Map<Product,Integer> prodMap = new HashMap<Product,Integer>();
				for(OrderItem item : itemList){
					String prod_id = item.getProduct_id();
					Product prod = prodDao.findProdById(prod_id);
					prodMap.put(prod, item.getBuynum());
				}
				olf.setProdMap(prodMap);
				
				olfList.add(olf);
			}
			return olfList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void delOrderByID(String id) {
		//1.根据id查询出所有订单项
		List<OrderItem> list = orderDao.findOrderItems(id);
		//2.遍历订单项,将对应prod_id的商品的库存加回去
		for(OrderItem item : list){
			prodDao.addPnum(item.getProduct_id(),item.getBuynum());
		}
		//3.删除订单项（对第三方数据表进行操作，把订单的外键删掉）
		orderDao.delOrderItem(id);
		//4.删除订单（对订单数据表进行操作）
		orderDao.delOrder(id);
	}

	public Order findOrderById(String order_id) {
		return orderDao.findOrderById(order_id);
	}

	public void changePayState(String order, int i) {
		orderDao.changePayState(order,i);		
	}

	public List<SaleInfo> saleList() {
		return orderDao.saleList();
	}

}
