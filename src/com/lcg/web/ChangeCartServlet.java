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

public class ChangeCartServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ProdService service = BasicFactory.getFactory().getService(ProdService.class);
		//1.��ȡҪɾ����id������id���ҳ���Ʒ
		String id = request.getParameter("id");
		Product prod = service.findProdById(id);
		//2.��ȡ���ﳵ���޸���Ʒ����
		Map<Product, Integer> cartmap = (Map<Product, Integer>) request.getSession().getAttribute("cartmap");
		cartmap.put(prod, Integer.parseInt(request.getParameter("buynum")));
		//3.�ض���ص����ﳵҳ��
		response.sendRedirect("/cart.jsp");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}