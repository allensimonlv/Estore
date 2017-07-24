<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <link rel="stylesheet" type="text/css" href="/css/register.css" />
  	<script type="text/javascript" src="/js/jquery-1.4.2.js"></script>
  	<script type="text/javascript" src="/ajax/myajax.js"></script>
	<script type="text/javascript">
		function changeImg(img){
			img.src = img.src+"?time="+new Date().getTime();
		}
		
		function checkForm(){
			var canSub = true;
			//1.非空校验
			canSub = checkNull("username","用户名不能为空！") && canSub;
			canSub = checkNull("password","密码不能为空！") && canSub;
			canSub = checkNull("password2","确认密码不能为空！") && canSub;
			canSub = checkNull("nickname","昵称不能为空！") && canSub;
			canSub = checkNull("email","邮箱不能为空！") && canSub;
			canSub = checkNull("valistr","验证码不能为空！") && canSub;
			
			//2.两次密码是否一致的校验
			var psw1 = document.getElementByName("password")[0].value;
			var psw2 = document.getElementByName("password2")[0].value;
			if(psw1 != psw2){
				document.getElementById("password2_msg").innerHTML = "<font color='red'>两次密码不一致！</font>";
				canSub = false;
			}
			
			//3.邮箱格式校验:xxxx@xxx.xxx.com
			var email = document.getElementsByName("email")[0].value;
			if(email != null && email != "" && !/^\w+@\w+(\.\w+)+$/.test(email)){
				document.getElementById("email_msg").innerHTML = "<font color='red'>邮箱格式不正确！</font>";
				canSub = false;
			}
			return canSub;//最后再把经过业务处理后的canSub的值给返回。
		}
		
		function checkNull(name,msg){
		//各个input中对于输入为空的情况处理方法基本是相同的，所以抽取成一个方法。
			document.getElementById(name+"_msg").innerHTML = "";//让内容先置为空，有消息就往里面填，没消息就显示不能为空
			var objValue = document.getElementsByName(name)[0].value;
			if(objValue == null || objValue == ""){
				document.getElementById(name+"_msg").innerHTML = "<font color='red'>"+msg+"</font>";
				return false;
			}
			return true;
		}
		window.onload=function(){
			//blur函数的意思是触发每一个匹配元素的blur事件。例如：$("p").blur(); blur事件会在元素失去焦点时触发，既可以是鼠标行为，也可以是按tab键离开。
			$("input[type='text'][name='username']").blur(function(){
				var username = $(this).val();
				//$.get(url,[data],[callback],[type])是通过get请求载入信息，是jQuery对AJAX的简单封装。请求成功时可调用回调函数。返回值是XMLHttpRequest类型的。
				$.get("/ValiNameServlet",{username:username},function(data){
					var json = eval("("+data+")");
					if(json.stat==1){
						$("#username_msg").html("<font color='red'>"+json.msg+"</font>");
					}else if(json.stat==0){
						$("#username_msg").html("<font color='green'>"+json.msg+"</font>");
					}
				});
			});
		}
	</script>
  </head> 
  <body>
    <div align="center">
    	<h1>Estore注册</h1><hr>
    	<form action="/RegistServlet" method="POST" onsubmit="return checkForm()" >
    		<table>
    			<tr>
    				<td>用户名</td>
    				<td><input type="text" name="username" value="${param.username }" /></td>
    				<td id="username_msg"></td>
    			</tr>
    			<tr>
    				<td>密码</td>
    				<td><input type="password" name="password" /></td>
    				<td id="password_msg"></td>
    			</tr>
    			<tr>
    				<td>确认密码</td>
    				<td><input type="password" name="password2" /></td>
    				<td id="password2_msg"></td>
    			</tr>
    			<tr>
    				<td>昵称</td>
    				<td><input type="text" name="nickname" value="${param.nickname }" /></td>
    				<td id="nickname_msg"></td>
    			</tr>
    			<tr>
    				<td>大学</td>
    				<td>
    				<!-- 隐藏一个大学的id,注册一个用户，应当以大学编号为准，而不能以大学的名称为准 -->
   						<input type="hidden" name="university" id="uuniversity" />
   						<input id="university" onclick="showUniTbl();" type="text" name=""/>
   					</td>
    				<td id="university_msg"></td>
    			</tr>
    			<tr>
    				<td>邮箱</td>
    				<td><input type="text" name="email" value="${param.email }" /></td>
    				<td id="email_msg"></td>
    			</tr>
    			<tr>
    				<td>验证码</td>
    				<td><input type="text" name="valistr" /></td>
    				<td id="valistr_msg">${msg }</td>
    			</tr>
    			<tr>
    				<td><input type="submit" value="注册用户" /></td>
    				<td><img src="/ValiImg" onclick="changeImg(this)" style="cursor:pointer;" /></td>
    			</tr>
    		</table>
    	</form>
    	<a href="/index.jsp">回到主页面</a>
    </div>
    
    <!-- 准备显示的大学表格 -->
  	<div class="divSch" id="uniDiv" style="display: none; position: absolute; top: 50px; left: 750px">
  		<table border="1" align="center" height="380px" width="500px" bordercolor="#3B5888">
  			<tr>
  				<td valign="top" bordercolor="#C3C3C3">
  					<div style="width: 550px; height: 280px; overflow: auto" id="uniTbl">
						<table width="100%">
							<tr>
								<c:forEach var="university" items="${unilist}" varStatus="vs">
									<td>
										<li>
										<a onclick='showMyUni(this)' href="javascript:void(0);" class="xh" id="${university.id }">${university.name }</a>
										<!-- 通过showMyUni函数把对应的大学名称放入input框中 -->
										</li>
									</td>
									<!-- 通过标签控制三个大学一行 -->
									<c:if test="${vs.count%3==0}">
									</tr><tr></c:if>
								</c:forEach>
							</tr>						
						</table>
					</div>
  				</td>
  			</tr>
  			<tr>
				<td align="right" bordercolor="#FFFFFF">
					<input type="button" value="关闭窗口" onclick="closeUniTbl()" style="color: white; background-color: #3B5888; padding: 3px;" />
					<br />
					<br />
					<br />
					<br />
				</td>
			</tr>
  		</table>
  	</div>
  </body>
</html>
