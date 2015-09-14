package com.netease.qa.log.storm.util;

public class Const {

	public static final String REMOTE_ADDR = "remote_addr";
	public static final String REMOTE_USER = "remote_user";
	public static final String TIME_LOCAL = "time_local";
	public static final String REQUEST = "request";
	public static final String STATUS = "status";
	public static final String BODY_BYTES_SENT = "body_bytes_sent";
	public static final String HTTP_REFERER = "http_referer";
	public static final String HTTP_SUER_AGENT = "http_user_agent";
	public static final String HTTP_X_FORWARDED_FOR = "http_x_forwarded_for";
	public static final String REQUEST_TIME = "request_time";
	public static final String HOST = "host";
	public static final String UPSTREAM_ADDR = "upstream_addr";
	public static final String UPSTREAM_STATUS = "upstream_status";
	public static final String UPSTREAM_RESPONSE_TIME = "upstream_response_time";
	public static final String SSL_PROTOCOL = "ssl_protocol";
	public static final String SSL_CIPHER = "ssl_cipher";
	//当upstream_status = "-",upstream_reponse_time="-",特殊处理
	//upstream_status，可能出现"504,200",upstream_reponse_time可能出现"0.32,0.06"
	public static final String REPLACE_MESSAGE = "-";
	
	public static final int TIME_INTERVAL = 10;
}
