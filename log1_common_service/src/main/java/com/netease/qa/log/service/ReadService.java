package com.netease.qa.log.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.qa.log.meta.ExceptionData;
import com.netease.qa.log.meta.UkExceptionData;
import com.netease.qa.log.meta.service.ExceptionDataService;
import com.netease.qa.log.meta.service.UkExceptionDataService;


public class ReadService {
	
	private static final Logger logger = Logger.getLogger(ReadService.class);

	private static ExceptionDataService exceptionDataService;
	private static UkExceptionDataService ukExceptionDataService;
	
	static{
		exceptionDataService = new ExceptionDataService();
		ukExceptionDataService = new UkExceptionDataService();
	}
	
	
	//按时间聚合
	public JSONObject queryTimeRecords(int logSourceId, long startTime, long endTime, int limit, int offset){
		List<ExceptionData> exceptionDatas = exceptionDataService.
				findByLogSourceIdAndTime(logSourceId, startTime, endTime, "sample_time", limit, offset);
		if(exceptionDatas.size() == 0) return new JSONObject();
		
		//JSONArray存放的是JSONObject对象的数组
		JSONArray records = new JSONArray();
		JSONObject record = new JSONObject();
		JSONArray details = new JSONArray();
		
		//TODO size校验
		ExceptionData first = exceptionDatas.get(0);
		record.put("time", convert(first.getSampleTime()));
		record.put("totalcount", first.getExceptionCount());

		JSONObject detail = new JSONObject();
		//detail.put(string,count),string是异常类型，count是该类型异常的数量
		//举例detail:java.net.ConnectException: this is a ConnectException" : "1" 前面是异常类型，后面是该类型的数量
		detail.put(MonitorDataService.getException(first.getExceptionId()).getExceptionType(), first.getExceptionCount());
		//details是包含detail的数组
		details.add(detail); 
		
		for(int i = 1; i < exceptionDatas.size(); i++){
			ExceptionData next = exceptionDatas.get(i);
			//若后续的异常数据的采样时间和第一个一样，那么就把他们的数量加起来
			if(convert(next.getSampleTime()).equals(record.getString("time"))){
				record.put("totalcount", record.getInteger("totalcount") + next.getExceptionCount());
				detail = new JSONObject();
				detail.put(MonitorDataService.getException(next.getExceptionId()).getExceptionType(), next.getExceptionCount());
				details.add(detail);
			}
			else{
				record.put("detail", details);
				records.add(record);
				record = new JSONObject();
				details = new JSONArray();
				record.put("time", convert(next.getSampleTime()));
				record.put("totalcount", next.getExceptionCount()); 
				detail = new JSONObject();
				detail.put(MonitorDataService.getException(next.getExceptionId()).getExceptionType(), next.getExceptionCount());
				details.add(detail);
			}
		}
		record.put("detail", details);
		records.add(record);
		JSONObject result = new JSONObject();
		result.put("projectid", ConfigDataService.getLogSource(logSourceId).getProjectId());
		result.put("logsourceid", logSourceId);
		result.put("record", records);
		return result;
	}
	
	
	//按错误类型聚合
	public JSONObject queryErrorRecords(int logSourceId, long startTime, long endTime, int limit, int offset){
		List<ExceptionData> exceptionDatas = exceptionDataService.
				findByLogSourceIdAndTime(logSourceId, startTime, endTime, "exception_id", limit, offset);
		if(exceptionDatas.size() == 0) return new JSONObject();

		JSONArray errors = new JSONArray();
		JSONObject error = new JSONObject();
		JSONArray details = new JSONArray();
		
		ExceptionData first = exceptionDatas.get(0);
		error.put("type", MonitorDataService.getException(first.getExceptionId()).getExceptionType());
		error.put("totalcount", first.getExceptionCount());
		error.put("demo", MonitorDataService.getException(first.getExceptionId()).getExceptionDemo());
		
		JSONObject detail = new JSONObject();
		detail.put(convert(first.getSampleTime()), first.getExceptionCount());
		details.add(detail); 
		
		for(int i = 1; i < exceptionDatas.size(); i++){
			ExceptionData next = exceptionDatas.get(i);
			//按异常聚合，如果后续的异常类型，与第一个的异常类型一致，就加入第一个异常details中
			if(MonitorDataService.getException(next.getExceptionId()).getExceptionType().equals(error.getString("type"))){
				error.put("totalcount", error.getInteger("totalcount") + next.getExceptionCount());
				detail = new JSONObject();
				detail.put(convert(next.getSampleTime()), next.getExceptionCount());
				details.add(detail);
			}
			else{
				error.put("detail", details);
				errors.add(error);
				error = new JSONObject();
				details = new JSONArray();
				error.put("type", MonitorDataService.getException(next.getExceptionId()).getExceptionType());
				error.put("totalcount", next.getExceptionCount());
				error.put("demo", MonitorDataService.getException(next.getExceptionId()).getExceptionDemo());
				detail = new JSONObject();
				detail.put(convert(next.getSampleTime()), next.getExceptionCount());
				details.add(detail);
			}
		}
		error.put("detail", details);
		errors.add(error);
		
		JSONObject result = new JSONObject();
		result.put("projectid", ConfigDataService.getLogSource(logSourceId).getProjectId());
		result.put("logsourceid", logSourceId);
		result.put("error", errors);
		return result;
	}
	
	
	
	public JSONObject queryUnknownExceptions(int logSourceId, long startTime, long endTime, int limit, int offset){
		List<UkExceptionData> ukExceptionDatas =  ukExceptionDataService.
				findByLogSourceIdAndTime(logSourceId, startTime, endTime, limit, offset);
		if(ukExceptionDatas.size() == 0) return new JSONObject();

		JSONArray unknowns = new JSONArray();
		for(UkExceptionData uk : ukExceptionDatas){
			JSONObject unknown = new JSONObject();
			unknown.put(convert(uk.getOriginLogTime()), uk.getOriginLog());
			unknowns.add(unknown);
		}
		
		JSONObject result = new JSONObject();
		result.put("projectid", ConfigDataService.getLogSource(logSourceId).getProjectId());
		result.put("logsourceid", logSourceId);
		result.put("unknowns", unknowns);
		logger.debug(result);
		return result;	
	}
	
	
	private static String convert(long time){ 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(time * 1000));
	}
	
}
