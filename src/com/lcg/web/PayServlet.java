package com.lcg.web;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lcg.domain.Order;
import com.lcg.factory.BasicFactory;
import com.lcg.service.OrderService;
import com.lcg.util.PaymentUtil;

public class PayServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ResourceBundle bundle = ResourceBundle.getBundle("merchantInfo");
		//用到国际化中ResourceBundle类，用资源包中的getBundle方法读取文件，比用Properties要简单一些。
		String p0_Cmd = "Buy";//代表业务类型，固定值buy
		String p1_MerId = bundle.getString("p1_MerId");//商户编号，就是用易宝时的账户
		//在配置文件里存入账户的信息，当Estore要用到易宝时，读取的是这个账户。到时用户转钱就到这个账户上，这个是易宝提供的测试账户。
		String p2_Order = request.getParameter("id");//商户购买商品的订单编号。从请求参数中把id拿过来。
		
		OrderService service = BasicFactory.getFactory().getService(OrderService.class);
		Order order= service.findOrderById(p2_Order);
		//String p3_Amt = order.getMoney()+"";
		String p3_Amt = "0.01";//代表支付金额，不能直接从前台拿，要拿着订单号从数据库中查一下。
		String p4_Cur = "CNY";//币种
		String p5_Pid = "";//
		String p6_Pcat = "";//
		String p7_Pdesc = "";//
		String p8_Url = "http://www.estore.com/Callback";// 商户接收支付成功数据的地址
		String p9_SAF = "0";//送货地址
		String pa_MP  = "";//
		String pd_FrpId = request.getParameter("pd_FrpId");//支付通道密码，从易宝连到各个银行
		String pr_NeedResponse = "1";//应答机制，固定值为1
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, bundle.getString("keyValue"));	
		//签名数据。为了保证在网络上传输的时候，数据不被篡改。易宝为每个商户在配置文件中提供了一个KeyValue，也就是MD5的秘钥。通过这个秘钥进行验证数据是否正确。
		// 生成url --- url?
		request.setAttribute("pd_FrpId", pd_FrpId);
		request.setAttribute("p0_Cmd", p0_Cmd);
		request.setAttribute("p1_MerId", p1_MerId);
		request.setAttribute("p2_Order", p2_Order);
		request.setAttribute("p3_Amt", p3_Amt);
		request.setAttribute("p4_Cur", p4_Cur);
		request.setAttribute("p5_Pid", p5_Pid);
		request.setAttribute("p6_Pcat", p6_Pcat);
		request.setAttribute("p7_Pdesc", p7_Pdesc);
		request.setAttribute("p8_Url", p8_Url);
		request.setAttribute("p9_SAF", p9_SAF);
		request.setAttribute("pa_MP", pa_MP);
		request.setAttribute("pr_NeedResponse", pr_NeedResponse);
		request.setAttribute("hmac", hmac);

		request.getRequestDispatcher("/confirm.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
