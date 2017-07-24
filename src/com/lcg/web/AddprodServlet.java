package com.lcg.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.lcg.domain.Product;
import com.lcg.domain.UploadMsg;
import com.lcg.factory.BasicFactory;
import com.lcg.service.ProdService;
import com.lcg.util.IOUtils;
import com.lcg.util.PicUtils;

public class AddprodServlet extends HttpServlet {

	public void doGet(final HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ProdService service = BasicFactory.getFactory().getService(ProdService.class);
		try {
			String encode = this.getServletContext().getInitParameter("encode");//把web应用中配置的初始encode参数给拿出来。
			Map<String, String> paramMap = new HashMap<String, String>();
			//1.上传图片
			//创建工厂设置内存缓冲区的大小和临时文件夹的位置。
			//把Apache写的用于文件上传下载的jar包 commons-fileupload的磁盘文件项工厂类给导进来。注意，Apache的工厂类都需要new才行。
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(1024*1024);//设置内存缓冲区的大小为1M
			factory.setRepository(new File(this.getServletContext().getRealPath("WEB-INF/temp")));//设置临时文件夹，并把这个临时文件夹的真实路径给传进来。
			//一通设置之后，获取文件上传的核心类，解决文件名乱码问题，并设置文件的大小容量上限。
			ServletFileUpload fileUpload = new ServletFileUpload(factory);
			fileUpload.setHeaderEncoding(encode);
//			fileUpload.setFileSizeMax(1024*1024*10);
//			fileUpload.setSizeMax(1024*1024*20);
			
			fileUpload.setProgressListener(new ProgressListener(){
				Long beginTime = System.currentTimeMillis();
				public void update(long bytesRead, long contentLength, int items) {
					try {
						Thread.sleep(100);//之所以在这里睡一会是因为想看一下进度条上传进度效果。这里完全不用睡。
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					UploadMsg umsg = new UploadMsg();
					BigDecimal br = new BigDecimal(bytesRead).divide(new BigDecimal(1024),2,BigDecimal.ROUND_HALF_UP);
					BigDecimal cl = new BigDecimal(contentLength).divide(new BigDecimal(1024),2,BigDecimal.ROUND_HALF_UP);
					//剩余字节数
					BigDecimal ll = cl.subtract(br);
					//上传百分比
					BigDecimal per = br.multiply(new BigDecimal(100)).divide(cl,2,BigDecimal.ROUND_HALF_UP);
					umsg.setPer(per.toString());
					//上传用时
					Long nowTime = System.currentTimeMillis();
					Long useTime = (nowTime - beginTime)/1000;
					//上传速度
					BigDecimal speed = new BigDecimal(0);
					if(useTime!=0){
						speed = br.divide(new BigDecimal(useTime),2,BigDecimal.ROUND_HALF_UP);
					}
					umsg.setSpeed(speed.toString());
					//大致剩余时间
					BigDecimal ltime = new BigDecimal(0);
					if(!speed.equals(new BigDecimal(0))){
						ltime = ll.divide(speed,0,BigDecimal.ROUND_HALF_UP);
					}
					umsg.setLtime(ltime.toString());
					request.getSession().setAttribute("umsg", umsg);
				}		
			});
			
			//检查是否是正确的文件上传表单
			if(!fileUpload.isMultipartContent(request)){
				throw new RuntimeException("请使用正确的表单进行上传！");
			}
			//解析request，把request中的内容放到list集合中去。
			List<FileItem> list = fileUpload.parseRequest(request);
			for(FileItem item : list){
				//遍历这个list集合，找到我们所需要的文件上传项。
				if(item.isFormField()){
					//普通字段
					String name = item.getFieldName();//因为是普通字段，所以用这个获取字段名称方法
					String value = item.getString(encode);
					paramMap.put(name, value);
				}else{
					//如果不是普通字段项，那就应该是一个文件上传项。这里使用UUID类的uuidname对象就是要防止文件名重复。
					String realname = item.getName();
					String uuidname = UUID.randomUUID().toString()+"_"+realname;//使用UUID的基本格式就是这样。
					//--分目录存储防止一个文件夹中文件过多
					String hash = Integer.toHexString(uuidname.hashCode());//生成一个16进制的8位哈希值。
					String upload = this.getServletContext().getRealPath("WEB-INF/upload");//指定商品上传所在的真实目录。
					String imgurl = "/WEB-INF/upload";
					for(char c : hash.toCharArray()){
						upload+="/"+c;
						imgurl+="/"+c;//hash值的每一位就是一级目录。
					}
					imgurl+="/"+uuidname;
					paramMap.put("imgurl", imgurl);//这里使用pmap这个Map类型的对象进行存储数据。这时imgurl这个路径就有了。
					File uploadFile = new File(upload);
					if(!uploadFile.exists())
						uploadFile.mkdirs();//如果不存在，则创建文件夹。避免重复创建的问题。
					//获取输入流
					InputStream in = item.getInputStream();
					//获取输出流
					OutputStream out = new FileOutputStream(new File(upload,uuidname));
					//--流对接上传，直接调用工具类。
					IOUtils.In2Out(in, out);
					IOUtils.close(in, out);					
					//--删除临时文件
					item.delete();

					//生成缩略图，把压缩图片的工具类导进来，拿到图片的路径后再用工具类中的方法进行调整。
					PicUtils picu = new PicUtils(this.getServletContext().getRealPath(imgurl));
					picu.resizeByHeight(140);
				}
			}
			//2.调用Service中提供的方法，在数据库中添加商品
			Product prod = new Product();
			BeanUtils.populate(prod, paramMap);
			service.addProd(prod);
			//3.提示成功，回到主页
			response.getWriter().write("添加成功，3秒回到主页......");
			response.setHeader("Refresh", "3;url=/index.jsp");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
