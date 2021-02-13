package com.mdt.util.dbcenter.socket;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * socket server工具类
 * 
 * @author lh
 *
 */
public class SocketServerUtils {
	public static Map<String, ServerSocket> serverMap = new HashMap<String, ServerSocket>();
	public Map<String, Object> realizeMap = new HashMap<String, Object>();

	/**
	 * 创建socketserver 默认超时时间为30秒
	 * @param porn  端口
	 */
	public SocketServerUtils(int port) {
		(new Thread(new SocketServerThread(port, 30000, null))).start();
	}

	/**
	 * 创建socketserver
	 * @param porn 端口
	 * @param timeout 超生时间（毫秒）
	 */
	public SocketServerUtils(int port, int timeout) {
		(new Thread(new SocketServerThread(port, timeout, null))).start();
	}

	/**
	 * 创建socketserver
	 * 
	 * @param port 端口
	 * @param timeout 超生时间（毫秒）
	 * @param infomap  数据数理类信息
	 */
	public SocketServerUtils(int port, int timeout, Map infomap) {
		(new Thread(new SocketServerThread(port, timeout, infomap))).start();
	}

	/**
	 * 手动停止socketserver
	 * @param port
	 */
	public void stopSocketServer(int port) {
		if (serverMap.get(port + "") != null) {
			ServerSocket serverSocket = serverMap.get(port + "");
			try {
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
	}

	/**
	 * 
	 * @author lh
	 *
	 */
	class SocketServerThread implements Runnable {
		public int port;// 端口
		public int timeout = 3000;// 超时时间
		public Map infoMap;

		public SocketServerThread(int port, int timeout, Map infoMap) {
			this.port = port;
			this.timeout = timeout;
			this.infoMap = infoMap;
		}

		@Override
		public void run() {
			createServer(port);
		}

		public void createServer(int port) {
			// 服务端监听客户端请求的TCP连接
			ServerSocket server;
			try {
				server = new ServerSocket(port);
				//
				serverMap.put(port + "", server);
				Socket socket = null;
				// 通过调用Executors类的静态方法，创建一个ExecutorService实例
				// ExecutorService接口是Executor接口的子接口
				Executor service = Executors.newCachedThreadPool();
				boolean f = true;
				while (server != null && !server.isClosed()) {
					// 等待客户端的连接
					try {
						socket = server.accept();
						System.out.println("与客户端连接成功！");
						// 调用execute()方法时，如果必要，会创建一个新的线程来处理任务，但它首先会尝试使用已有的线程，
						// 如果一个线程空闲60秒以上，则将其移除线程池；
						// 另外，任务是在Executor的内部排队，而不是在网络中排队
						socket.setSoTimeout(timeout);// 超时时间
						service.execute(new SocketThread(socket, infoMap));
					} catch (IOException e) {
						e.printStackTrace();
						if (socket != null) {
							socket.close();
						}
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();

			}
		}
	}

	/**
	 * 多线程处理客户端发送的数据
	 * 
	 * @author lh
	 *
	 */
	class SocketThread implements Runnable {

		private Socket socket = null;
		private Map infoMap = null;

		public SocketThread(Socket socket, Map infoMap) {
			this.socket = socket;
			this.infoMap = infoMap;
		}

		// 处理通信细节的静态方法，这里主要是方便线程池服务器的调用
		public void execute(Socket socket) {
			BufferedReader buf = null;
			try {
				// 获取Socket的输出流，用来向客户端发送数据
				PrintStream out = new PrintStream(socket.getOutputStream());

				// 获取Socket的输入流，用来接收从客户端发送过来的数据
				buf = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				String info = null;
				while ((info = buf.readLine()) != null) {
					if (infoMap == null) {
						// 将接收到的字符串前面加上echo，发送到对应的客户端
						out.println("server return :::::" + info);
					} else {
						//通过反射调用数据接受类
						String classStr = infoMap.get("class") + "";
						String methodStr = infoMap.get("method") + "";
						if(StringUtils.isNotBlank(classStr)&&StringUtils.isNotBlank(methodStr)&&StringUtils.isNotBlank(info)){
							Class clazz=Class.forName(classStr);
							Object clazzObj=clazz.newInstance();
							Method method=clazz.getDeclaredMethod(methodStr, String.class);
							method.invoke(clazzObj,info);
						}
					}
					System.out.println((new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss:SSS")).format(new Date())
							+ ":::::"
							+ socket.getInetAddress()
							+ ":::::"
							+ socket.getPort() + "::::: 字符长度：" + info.length());
				}
				out.close();
				buf.close();
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					if (buf != null) {
						buf.close();
					}
					if (socket != null) {
						socket.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

		@Override
		public void run() {
			execute(socket);
		}
	}
}
