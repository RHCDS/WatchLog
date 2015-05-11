package com.netease.qa.log.util;

import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.UnknownHostException;
import java.util.Random;

public class MyLogProducer {

	public void produce() throws ConnectException, UnknownHostException, NoRouteToHostException {
		LogProducer lp = new LogProducer();
		lp.produce();
	}
}


class LogProducer {
	private static Random random = new Random();

	public void produce() throws ConnectException, UnknownHostException, NoRouteToHostException {

		int roll = random.nextInt(100);
		if (roll < 20) {
			throw new ConnectException("this is a ConnectException");
		}
		else if (roll < 50) {
			throw new NoRouteToHostException("this is a NoRouteToHostException");
		}
		else {
			throw new UnknownHostException("this is a UnknownHostException");
		}
	}

}
