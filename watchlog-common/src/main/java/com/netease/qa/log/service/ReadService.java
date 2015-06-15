package com.netease.qa.log.service;

import com.alibaba.fastjson.JSONObject;

public interface ReadService {

	public JSONObject queryTimeRecords(int logSourceId, long startTime, long endTime, String orderBy, String order, int limit, int offset);
	
	public int getTimeCountByLogSourceIdAndTime(int logSourceId, long startTime, long endTime);

	public JSONObject queryErrorRecords(int logSourceId, long startTime, long endTime, String orderBy, String order, int limit, int offset);
	
	public JSONObject queryErrorRecordsWithTimeDetail(int logSourceId, long startTime, long endTime, String orderBy, String order, int limit, int offset);

	
	public JSONObject queryUnknownExceptions(int logSourceId, long startTime, long endTime, int limit, int offset);
}
