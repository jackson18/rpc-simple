package com.qijiabin.demo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * ========================================================
 * 日 期：2016年4月14日 上午8:12:02
 * 作 者：jiabin.qi
 * 版 本：1.0.0
 * 类说明：服务端实现类
 * TODO
 * ========================================================
 * 修订日期     修订人    描述
 */
public class RpcExporter {

	private static Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	
	@SuppressWarnings("resource")
	public static void exporter(String hostName, int port) throws Exception {
		ServerSocket server = new ServerSocket();
		server.bind(new InetSocketAddress(hostName, port));
		while (true) {
			executor.execute(new ExporterTask(server.accept()));
		}
	}
	
	private static class ExporterTask implements Runnable {
		Socket client = null;

		public ExporterTask(Socket client) {
			super();
			this.client = client;
		}
		
		public void run() {
			ObjectInputStream input = null;
			ObjectOutputStream output = null;
			try {
				input = new ObjectInputStream(client.getInputStream());
				String interfaceName = input.readUTF();
				String methodName = input.readUTF();
				Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
				Object[] arguments = (Object[]) input.readObject();
				Class<?> service = Class.forName(interfaceName);
				Method method = service.getMethod(methodName, parameterTypes);
				Object result = method.invoke(service.newInstance(), arguments);
				output = new ObjectOutputStream(client.getOutputStream());
				output.writeObject(result);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} finally {
				if (output != null) {
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (client != null) {
					try {
						client.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
	}
	
}
