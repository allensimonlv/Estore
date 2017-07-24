package com.lcg.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtils {
	private IOUtils() {
	}
	
	public static void In2Out(InputStream in,OutputStream out) throws IOException{
		//把输入到输出的通用代码封装成一个IO工具类中的一个方法
		byte [] bs = new byte[1024];
		int i = 0;
		while((i=in.read(bs))!=-1){
			out.write(bs,0,i);
		}
	}
	
	public static void close(InputStream in,OutputStream out){
		//把通用的关闭操作封装成一个方法，到时直接调用即可。不用再重复写。
		if(in!=null){
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				in = null;
			}
		}
		if(out!=null){
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				out = null;
			}
		}
	}
}
