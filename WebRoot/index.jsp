<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  </head> 
  <body>
    <h1>Estore</h1><hr>
    <c:if test="${sessionScope.user == null}">
    	欢迎光临，游客
    	<a href="/RegistPreServlet">注册</a>
    	<a href="/login.jsp">登录</a>
    </c:if>
    <c:if test="${sessionScope.user!= null}">
    	欢迎回来，${sessionScope.user.username }
    	<a href="/addProd.jsp">添加商品</a>
    	<a href="/ProdListServlet">商品列表</a>
    	<a href="/cart.jsp">查看购物车</a>
    	<a href="/OrderListServlet">订单查询</a>
    	<a href="/SaleListServlet">销售榜单下载</a>
    	<a href="/LogoutServlet">注销</a>
    </c:if>
  </body>
</html>
