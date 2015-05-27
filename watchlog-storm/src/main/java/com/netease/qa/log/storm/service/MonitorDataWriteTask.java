package com.netease.qa.log.storm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class MonitorDataWriteTask implements Runnable{

	private static final int time_accuracy = 15000; 
	private static final Logger logger = LoggerFactory.getLogger(MonitorDataWriteTask.class);

	@Override
	public void run() {
		while(true){
			//TODO 采样时间为storm系统时间，和日志原始时间偏差较大，需要处理下
			MonitorDataService.writeExceptionData(System.currentTimeMillis()/1000); 
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
