package com.qijiabin.demo;

/**
 * ========================================================
 * 日 期：2016年4月14日 上午8:11:46
 * 作 者：jiabin.qi
 * 版 本：1.0.0
 * 类说明：服务实现类
 * TODO
 * ========================================================
 * 修订日期     修订人    描述
 */
public class EchoServiceImpl implements EchoService {

	public String echo(String msg) {
		return "hello: " + msg;
	}

}
