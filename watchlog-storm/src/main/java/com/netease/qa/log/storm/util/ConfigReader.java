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

	public static String LOG_TYPE;
	public static String MYBATIS_ENV;
	public static String MQ_HOST;
	public static int MQ_PORT;
	public static String MQ_QUEUE;
	// storm的相关配置
	public static int TOPOLOGY_WORKERS;
	public static int TOPOLOGY_MAX_SPOUT_PENDING;
	public static int TOPOLOGY_ACKER_EXECUTORS;
	// 异常日志topology的配置
	public static int LOG_SPOUT;
	public static int LOG_NORMALIZER_BOLT;
	public static int LOG_FILTER_BOLT;
	public static int LOG_ANALYSER_BOLT;
	public static int RESULT_WRITER_BOLT;
	// nginx日志topology的配置
	public static int NGINX_READER_SPOUT;
	public static int NGINX_NORMALIZER_BOLT;
	public static int NGINX_FILTER_BOLT;
	public static int NGINX_ANALYSER_BOLT;

	public static void setValue(String fileName) {
		Properties properties = new Properties();
		InputStream is = null;
		Reader reader = null;
		is = ConfigReader.class.getResourceAsStream(fileName);
		try {
			reader = new InputStreamReader(is, "UTF-8");
			properties.load(reader);
		} catch (UnsupportedEncodingException e) {
			logger.error("error", e);
		} catch (IOException e) {
			logger.error("error", e);
		}

		LOG_TYPE = properties.getProperty("log.type");
		MYBATIS_ENV = properties.getProperty("mybatis.env");
		MQ_HOST = properties.getProperty("mq.host");
		MQ_PORT = Integer.valueOf(properties.getProperty("mq.port"));
		MQ_QUEUE = properties.getProperty("mq.queue");

		TOPOLOGY_WORKERS = Integer.valueOf(properties.getProperty("topology_workers"));
		TOPOLOGY_MAX_SPOUT_PENDING = Integer.valueOf(properties.getProperty("topology_max_spout_pending"));
		TOPOLOGY_ACKER_EXECUTORS = Integer.valueOf(properties.getProperty("topology_acker_executors"));

		LOG_SPOUT = Integer.valueOf(properties.getProperty("mq_consumer.spout"));
		LOG_NORMALIZER_BOLT = Integer.valueOf(properties.getProperty("log_normalizer.bolt"));
		LOG_FILTER_BOLT = Integer.valueOf(properties.getProperty("log_filter.bolt"));
		LOG_ANALYSER_BOLT = Integer.valueOf(properties.getProperty("log_analyser.bolt"));
		RESULT_WRITER_BOLT = Integer.valueOf(properties.getProperty("result_writer.bolt"));

		NGINX_READER_SPOUT = Integer.valueOf(properties.getProperty("nginx_reader.spout"));
		NGINX_NORMALIZER_BOLT = Integer.valueOf(properties.getProperty("nginx_normalizer.bolt"));
		NGINX_FILTER_BOLT = Integer.valueOf(properties.getProperty("nginx_filter.bolt"));
		NGINX_ANALYSER_BOLT = Integer.valueOf(properties.getProperty("nginx_analyze.bolt"));
	}
}
