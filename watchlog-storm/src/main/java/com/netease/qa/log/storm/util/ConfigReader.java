package com.netease.qa.log.storm.util;

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
	private static final String CONF_FILE = "/storm.conf";

	public static String MYBATIS_ENV;
	public static String MQ_HOST;
	public static int MQ_PORT;
	public static String MQ_QUEUE;
	
	static{
		Properties properties = new Properties();
		InputStream is = null;
		Reader reader = null;
		is = ConfigReader.class.getResourceAsStream(CONF_FILE);
		try {
			reader = new InputStreamReader(is, "UTF-8");
			properties.load(reader);
		}
		catch (UnsupportedEncodingException e) {
			logger.error("error", e); 
		}
		catch (IOException e) {
			logger.error("error", e); 
		}

		MYBATIS_ENV = properties.getProperty("mybatis.env");
		MQ_HOST = properties.getProperty("mq.host");
		MQ_PORT = Integer.valueOf(properties.getProperty("mq.port"));
		MQ_QUEUE = properties.getProperty("mq.queue");
	}
	
	
}
