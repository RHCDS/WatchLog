package com.netease.qa.log.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface ReadService {

	public JSONObject queryLatestTimeRecords(int logSourceId, long currentTime);

	public JSONObject queryTimeRecords(int logSourceId, long startTime, long endTime, String orderBy, String order,
			int limit, int offset);

	public int getTimeCountByLogSourceIdAndTime(int logSourceId, long startTime, long endTime);

	public int getErrorTypeCountByLogSourceId(int logSourceId, long startTime, long endTime);

	public int getErrorRecordsCountByLogSourceIdAndExceptionIdAndTime(int logSourceId, int exceptionId, long startTime,
			long endTime);

	public JSONObject queryErrorRecords(int logSourceId, long startTime, long endTime, String orderBy, String order,
			int limit, int offset);

	public JSONObject queryDetailByErrorType(int logSourceId, int exceptionId, long startTime, long endTime,
			String sort, String order, int limit, int offset);

	public JSONObject queryErrorRecordsWithTimeDetail(int logSourceId, long startTime, long endTime, String orderBy,
			String order, int limit, int offset);

	public JSONObject queryUnknownExceptions(int logSourceId, long startTime, long endTime, int limit, int offset);
	
	public JSONObject queryExceptionByLogSourceIdAndTime(int logSourceId, long startTime, long endTime);

	public JSONArray queryRecordsByTime(int projectid, long start_time, long end_time);
	
	public JSONObject queryErrorRecordsByLogSourceIds(List<Integer> logSourceIds, long start_time, long end_time);

	// 按机器聚合所有日志源的异常信息-曲线数据
	public JSONObject queryErrorRecordsGraphByMachine(String host_name, long start, long end , int logtype);
}
