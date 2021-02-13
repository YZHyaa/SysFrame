package com.mdt.util.dbcenter.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClientUtils {
	/*private String host;//ip
	private int port;//端口
	private String msg="socket client test";//数据
	
	public SocketClientUtils(String host,int port){
		this.host=host;
		this.port=port;
	}
	*//**
	 * socket 测试
	 * @return 返回测试字符串
	 *//*
	public String connectStocket(){
		return connectStocket(this.msg);
	}
	*//**
	 * socket测试
	 * @param msg
	 * @return 返回测试字符串
	 *//*
	public String connectStocket(String msg){
		String serverReturn=null;
		try{
			if(msg==null||msg==""){
				msg=this.msg;
			}
			Socket socket = new Socket(host, port);
			// 由系统标准输入设备构造BufferedReader对象
			PrintWriter os = new PrintWriter(socket.getOutputStream());
			// 由Socket对象得到输出流，并构造PrintWriter对象
			BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			os.println(msg);
			// 将从系统标准输入读入的字符串输出到Server
			os.flush();
			serverReturn=is.readLine();
			os.close(); // 关闭Socket输出流
			is.close(); // 关闭Socket输入流
			socket.close(); // 关闭Socket
		} catch (Exception e) {
			System.out.println("Error" + e); // 出错，则打印出错信息
		}
		return serverReturn;
	}*/
	
	public static boolean connectStocket(String host,int port,String msg){
		boolean serverReturn=false;
		try{
			if(msg==null||msg==""){
				msg="socket client test";
			}
			Socket socket = new Socket(host, port);
			// 由系统标准输入设备构造BufferedReader对象
			PrintWriter os = new PrintWriter(socket.getOutputStream());
			// 由Socket对象得到输出流，并构造PrintWriter对象
			BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			os.println(msg);
			// 将从系统标准输入读入的字符串输出到Server
			os.flush();
			String retStr=is.readLine();
			
			os.close(); // 关闭Socket输出流
			is.close(); // 关闭Socket输入流
			socket.close(); // 关闭Socket
			if(retStr.indexOf(msg)>=0){
				return true;
			}
		} catch (Exception e) {
			System.out.println("Error" + e); // 出错，则打印出错信息
			return false;
		}
		return serverReturn;
	}
}
