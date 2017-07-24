<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  </head>
  	<font color="red"><h1>${prod.name }</h1><hr></font>
  	<table width="100%">
  		<tr>
  			<td><img src="/ImgServlet?imgurl=${prod.imgurl }" /></td>
  			<td>
  				商品名称:${prod.name }<br>
  				商品种类:${prod.category }<br>
  				商品库存:${prod.pnum }<br>
  				商品价格:${prod.price }<br>
  				商品描述:${prod.description }<br>
  				<a href="/AddCartServlet?id=${prod.id }"><img src="/img/buy.bmp"/></a><br>
  			</td>
  		</tr>
  	</table>
  <body>
    
  </body>
</html>
