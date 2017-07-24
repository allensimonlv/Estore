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
			String encode = this.getServletContext().getInitParameter("encode");//��webӦ�������õĳ�ʼencode�������ó�����
			Map<String, String> paramMap = new HashMap<String, String>();
			//1.�ϴ�ͼƬ
			//�������������ڴ滺�����Ĵ�С����ʱ�ļ��е�λ�á�
			//��Apacheд�������ļ��ϴ����ص�jar�� commons-fileupload�Ĵ����ļ���������������ע�⣬Apache�Ĺ����඼��Ҫnew���С�
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(1024*1024);//�����ڴ滺�����Ĵ�СΪ1M
			factory.setRepository(new File(this.getServletContext().getRealPath("WEB-INF/temp")));//������ʱ�ļ��У����������ʱ�ļ��е���ʵ·������������
			//һͨ����֮�󣬻�ȡ�ļ��ϴ��ĺ����࣬����ļ����������⣬�������ļ��Ĵ�С�������ޡ�
			ServletFileUpload fileUpload = new ServletFileUpload(factory);
			fileUpload.setHeaderEncoding(encode);
//			fileUpload.setFileSizeMax(1024*1024*10);
//			fileUpload.setSizeMax(1024*1024*20);
			
			fileUpload.setProgressListener(new ProgressListener(){
				Long beginTime = System.currentTimeMillis();
				public void update(long bytesRead, long contentLength, int items) {
					try {
						Thread.sleep(100);//֮����������˯һ������Ϊ�뿴һ�½������ϴ�����Ч����������ȫ����˯��
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					UploadMsg umsg = new UploadMsg();
					BigDecimal br = new BigDecimal(bytesRead).divide(new BigDecimal(1024),2,BigDecimal.ROUND_HALF_UP);
					BigDecimal cl = new BigDecimal(contentLength).divide(new BigDecimal(1024),2,BigDecimal.ROUND_HALF_UP);
					//ʣ���ֽ���
					BigDecimal ll = cl.subtract(br);
					//�ϴ��ٷֱ�
					BigDecimal per = br.multiply(new BigDecimal(100)).divide(cl,2,BigDecimal.ROUND_HALF_UP);
					umsg.setPer(per.toString());
					//�ϴ���ʱ
					Long nowTime = System.currentTimeMillis();
					Long useTime = (nowTime - beginTime)/1000;
					//�ϴ��ٶ�
					BigDecimal speed = new BigDecimal(0);
					if(useTime!=0){
						speed = br.divide(new BigDecimal(useTime),2,BigDecimal.ROUND_HALF_UP);
					}
					umsg.setSpeed(speed.toString());
					//����ʣ��ʱ��
					BigDecimal ltime = new BigDecimal(0);
					if(!speed.equals(new BigDecimal(0))){
						ltime = ll.divide(speed,0,BigDecimal.ROUND_HALF_UP);
					}
					umsg.setLtime(ltime.toString());
					request.getSession().setAttribute("umsg", umsg);
				}		
			});
			
			//����Ƿ�����ȷ���ļ��ϴ���
			if(!fileUpload.isMultipartContent(request)){
				throw new RuntimeException("��ʹ����ȷ�ı������ϴ���");
			}
			//����request����request�е����ݷŵ�list������ȥ��
			List<FileItem> list = fileUpload.parseRequest(request);
			for(FileItem item : list){
				//�������list���ϣ��ҵ���������Ҫ���ļ��ϴ��
				if(item.isFormField()){
					//��ͨ�ֶ�
					String name = item.getFieldName();//��Ϊ����ͨ�ֶΣ������������ȡ�ֶ����Ʒ���
					String value = item.getString(encode);
					paramMap.put(name, value);
				}else{
					//���������ͨ�ֶ���Ǿ�Ӧ����һ���ļ��ϴ������ʹ��UUID���uuidname�������Ҫ��ֹ�ļ����ظ���
					String realname = item.getName();
					String uuidname = UUID.randomUUID().toString()+"_"+realname;//ʹ��UUID�Ļ�����ʽ����������
					//--��Ŀ¼�洢��ֹһ���ļ������ļ�����
					String hash = Integer.toHexString(uuidname.hashCode());//����һ��16���Ƶ�8λ��ϣֵ��
					String upload = this.getServletContext().getRealPath("WEB-INF/upload");//ָ����Ʒ�ϴ����ڵ���ʵĿ¼��
					String imgurl = "/WEB-INF/upload";
					for(char c : hash.toCharArray()){
						upload+="/"+c;
						imgurl+="/"+c;//hashֵ��ÿһλ����һ��Ŀ¼��
					}
					imgurl+="/"+uuidname;
					paramMap.put("imgurl", imgurl);//����ʹ��pmap���Map���͵Ķ�����д洢���ݡ���ʱimgurl���·�������ˡ�
					File uploadFile = new File(upload);
					if(!uploadFile.exists())
						uploadFile.mkdirs();//��������ڣ��򴴽��ļ��С������ظ����������⡣
					//��ȡ������
					InputStream in = item.getInputStream();
					//��ȡ�����
					OutputStream out = new FileOutputStream(new File(upload,uuidname));
					//--���Խ��ϴ���ֱ�ӵ��ù����ࡣ
					IOUtils.In2Out(in, out);
					IOUtils.close(in, out);					
					//--ɾ����ʱ�ļ�
					item.delete();

					//��������ͼ����ѹ��ͼƬ�Ĺ����ർ�������õ�ͼƬ��·�������ù������еķ������е�����
					PicUtils picu = new PicUtils(this.getServletContext().getRealPath(imgurl));
					picu.resizeByHeight(140);
				}
			}
			//2.����Service���ṩ�ķ����������ݿ��������Ʒ
			Product prod = new Product();
			BeanUtils.populate(prod, paramMap);
			service.addProd(prod);
			//3.��ʾ�ɹ����ص���ҳ
			response.getWriter().write("��ӳɹ���3��ص���ҳ......");
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
