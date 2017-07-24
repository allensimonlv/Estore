package com.lcg.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lcg.domain.Product;
import com.lcg.factory.BasicFactory;
import com.lcg.service.ProdService;

public class AddCartServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ProdService service = BasicFactory.getFactory().getService(ProdService.class);
		//1.根据id查找出要购买的商品
		String id = request.getParameter("id");
		Product prod = service.findProdById(id);
		//2.向cartmap中添加这个商品,如果之前没有这个商品,则添加并将数量设置为1,如果已经有过这个商品,数量+1
		if(prod == null){
			throw new RuntimeException("找不到该商品！");
		}else{
			Map<Product, Integer> cartmap = (Map<Product, Integer>) request.getSession().getAttribute("cartmap");//把商品的map形式的键值对给拿出来。
			cartmap.put(prod, cartmap.containsKey(prod)?cartmap.get(prod)+1 : 1);
			//在把商品的map给存起来时，还要加一个判断，就是Map中有没有这个商品的已知信息。如果有，把这个商品的值拿过来直接加一即可；如果没有，就是1。
		}
		//3.重定向到购物车页面进行展示
		response.sendRedirect("/cart.jsp");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
