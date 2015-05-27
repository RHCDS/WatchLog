package com.netease.qa.log.storm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ConfigDataLoadTask implements Runnable{

	private static final Logger logger = LoggerFactory.getLogger(ConfigDataLoadTask.class);

	private static final int config_load_inteval = 15000;

	@Override
	public void run() {
		while(true){
			ConfigDataService.loadConfig();
			try {
				Thread.sleep(config_load_inteval);
			}
			catch (InterruptedException e) {
	        	logger.error("error", e);
			}
		}
	}

}
	
