package com.netease.qa.log.service;

import org.apache.log4j.Logger;


public class ConfigLoadTask implements Runnable{

	private static final Logger logger = Logger.getLogger(ConfigLoadTask.class);
	private static final int config_load_inteval = 15000;

	@Override
	public void run() {
		while(true){
			ConfigDataService.loadConfig();
			try {
				Thread.sleep(config_load_inteval);
			}
			catch (InterruptedException e) {
				logger.error(e);
			}
		}
	}

}
	
