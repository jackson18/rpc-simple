package com.qijiabin.demo;

import java.net.InetSocketAddress;

import org.junit.Test;

/**
 * ========================================================
 * 日 期：2016年4月14日 上午8:11:19
 * 作 者：jiabin.qi
 * 版 本：1.0.0
 * 类说明：
 * TODO
 * ========================================================
 * 修订日期     修订人    描述
 */
public class RpcTest {

	@Test
	public void testRpc() {
		new Thread(new Runnable() {
			
			public void run() {
				try {
					RpcExporter.exporter("localhost", 8088);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		RpcImporter<EchoService> importer = new RpcImporter<EchoService>();
		EchoService echo = importer.importer(EchoServiceImpl.class, new InetSocketAddress("localhost", 8088));
		System.out.println(echo.echo("jack"));
	}
	
}
