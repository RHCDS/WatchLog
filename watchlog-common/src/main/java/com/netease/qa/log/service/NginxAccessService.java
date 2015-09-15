package com.netease.qa.log.service;

import com.alibaba.fastjson.JSONObject;

public interface NginxAccessService {
	
	public JSONObject getTopNUrl(int logSourceId, long start, long end, int topN, String sort);
	
	public JSONObject getTopAllData(int logSourceId, long start, long end, int topN, String sort);
	
	public JSONObject getRealSingleData(int logSourceId, String url, long start, long end);
	
	/*
	 * 获取离线曲线数据，自适应300个点，容许一点点误差
	 */
	public JSONObject getOfflineAllData(int logSourceId, String url, long start, long end);
	}
