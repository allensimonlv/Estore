package com.lcg.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lcg.domain.UploadMsg;

public class UploadMsgServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UploadMsg umsg = (UploadMsg) request.getSession().getAttribute("umsg");
		if(umsg!=null)//这里要判断umsg不为空，否则窗口会报空指针异常。
			response.getWriter().write(umsg.toString());
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
