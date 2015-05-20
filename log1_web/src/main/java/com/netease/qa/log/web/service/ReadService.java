package com.netease.qa.log.web.service;

import com.alibaba.fastjson.JSONObject;

public interface ReadService {

	public JSONObject queryTimeRecords(int logSourceId, long startTime, long endTime, int limit, int offset);
	public JSONObject queryErrorRecords(int logSourceId, long startTime, long endTime, int limit, int offset);
	public JSONObject queryUnknownExceptions(int logSourceId, long startTime, long endTime, int limit, int offset);
}
