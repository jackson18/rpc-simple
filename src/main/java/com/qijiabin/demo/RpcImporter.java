package com.qijiabin.demo;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * ========================================================
 * 日 期：2016年4月14日 上午8:12:10
 * 作 者：jiabin.qi
 * 版 本：1.0.0
 * 类说明：客户端代理类
 * TODO
 * ========================================================
 * 修订日期     修订人    描述
 * @param <S>
 */
public class RpcImporter<S> {

	@SuppressWarnings("unchecked")
	public S importer(final Class<?> serviceClass, final InetSocketAddress addr) {
		return (S) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class<?>[] {serviceClass.getInterfaces()[0]}, 
				new InvocationHandler() {
					
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						Socket socket = null;
						ObjectOutputStream output = null;
						ObjectInputStream input = null;
						try {
							socket = new Socket();
							socket.connect(addr);
							output = new ObjectOutputStream(socket.getOutputStream());
							output.writeUTF(serviceClass.getName());
							output.writeUTF(method.getName());
							output.writeObject(method.getParameterTypes());
							output.writeObject(args);
							input = new ObjectInputStream(socket.getInputStream());
							return input.readObject();
						} finally {
							if (socket != null) {
								socket.close();
							}
							if (output != null) {
								output.close();
							}
							if (input != null) {
								input.close();
							}
						}
					}
				});
	}
	
}
