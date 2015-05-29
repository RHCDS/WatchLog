package com.netease.qa.log.storm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConfigReader {
	
	private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);
	private static final String CONF_FILE = "storm.conf";

	public static String MYBATIS_ENV;
	public static String MQ_HOST;
	public static int MQ_PORT;
	public static String MQ_QUEUE;
	
	static{
		File configFile = new File(CONF_FILE);
		Properties properties = new Properties();
		InputStream is = null;
		Reader reader = null;
		try {
			is = new FileInputStream(configFile);
			reader = new InputStreamReader(is, "UTF-8");
			properties.load(reader);
			
			MYBATIS_ENV = properties.getProperty("mybatis.env");
			MQ_HOST = properties.getProperty("mq.host");
			MQ_PORT = Integer.valueOf(properties.getProperty("mq.port"));
			MQ_QUEUE = properties.getProperty("mq.queue");
		}
		catch (FileNotFoundException e) {
        	logger.error("error", e);
		}
		catch (UnsupportedEncodingException e) {
        	logger.error("error", e);
		}
		catch (IOException e) {
        	logger.error("error", e);
		}
	}
	
	
}
