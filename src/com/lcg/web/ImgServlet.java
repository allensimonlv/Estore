package com.lcg.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lcg.domain.Product;
import com.lcg.factory.BasicFactory;
import com.lcg.service.ProdService;

public class ImgServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//1.根据id查找出商品
//		ProdService service = BasicFactory.getFactory().getInstance(ProdService.class);
//		Product prod = service.findProdById(request.getParameter("id"));
		//2.获取商品url，输出图片
		String imgurl = request.getParameter("imgurl");
		request.getRequestDispatcher(imgurl).forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
