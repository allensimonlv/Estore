<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  </head>
  
  <body>
    <h1>Estore_商品列表</h1><br>
    <a href="/index.jsp">回到主页面</a><br><br>
    <table width="100%" style="text-align:center;">
    	<c:forEach items="${requestScope.list }" var="prod">
    		<tr>
    			<td width="20%">
    				<a href="/ProdInfoServlet?id=${prod.id }"><img src="/ImgServlet?imgurl=${prod.imgurls }" style="cursor:pointer;" /></a>
    			</td>   				
    			<td width="40%">
    				${prod.name }<br>
    				${prod.price }<br>
    				${prod.category }<br>
    			</td>
    			<td width="40%">
    				<c:if test="${prod.pnum>0}">
    					<font color="blue">有货</font>
    				</c:if>
    				<c:if test="${prod.pnum<=0}">
    					<font color="red">缺货</font>
    				</c:if>
    			</td>
    		</tr>
    		<tr>
    			<td colspan="3"><hr></td>
    		</tr>
    	</c:forEach>
    </table>
    
  </body>
</html>
