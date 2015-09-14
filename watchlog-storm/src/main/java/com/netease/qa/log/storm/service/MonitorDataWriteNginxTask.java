package com.netease.qa.log.storm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitorDataWriteNginxTask implements Runnable{
	private static final int time_accuracy = 10000; 
	private static final Logger logger = LoggerFactory.getLogger(MonitorDataWriteTask.class);
	
	@Override
	public void run() {
		while(true){
			MonitorDataService.writeNginxAccessData();
			try {
				//每30s执行一次写入操作
				Thread.sleep(time_accuracy);
			}
			catch (InterruptedException e) {
	        	logger.error("error", e);
			}
		}
	}

}
