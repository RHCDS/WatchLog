package com.netease.qa.log.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.qa.log.meta.ExceptionData;
import com.netease.qa.log.meta.ExceptionDataRecord;
import com.netease.qa.log.meta.UkExceptionData;
import com.netease.qa.log.meta.dao.ExceptionDao;
import com.netease.qa.log.meta.dao.ExceptionDataDao;
import com.netease.qa.log.meta.dao.LogSourceDao;
import com.netease.qa.log.meta.dao.UkExceptionDataDao;
import com.netease.qa.log.service.ReadService;
import com.netease.qa.log.util.Const;
import com.netease.qa.log.util.MathUtil;

@Service
public class ReadServiceImpl implements ReadService {

	private static final Logger logger = LoggerFactory.getLogger(ReadServiceImpl.class);

	@Resource
	private ExceptionDao exceptionDao;
	@Resource
	private ExceptionDataDao exceptionDataDao;
	@Resource
	private UkExceptionDataDao ukExceptionDataDao;
	@Resource
	private LogSourceDao logSourceDao;

	@Override
	public int getTimeCountByLogSourceIdAndTime(int logSourceId, long startTime, long endTime) {
		return exceptionDataDao.getTimeRecordsCountByLogSourceIdAndTime(logSourceId, startTime, endTime); 
	}
	
	@Override
	public int getErrorTypeCountByLogSourceId(int logSourceId, long startTime, long endTime) {
		return exceptionDataDao.getErrorRecordsCountByLogSourceIdAndTime(logSourceId, startTime, endTime);
	}
	
	@Override
	public JSONObject queryLatestTimeRecords(int logSourceId, long currentTime) {
		//时间精度取整： xx:xx:00 、xx:xx:30两种精度
		Long formatCurrentTime = currentTime / Const.RT_SHOW_TIME * Const.RT_SHOW_TIME; 
		JSONObject result = new JSONObject();
		result.put("projectid", logSourceDao.findByLogSourceId(logSourceId).getProjectId());
		result.put("logsourceid", logSourceId);
		JSONArray records = new JSONArray();
		for(int i = 0; i < Const.RT_SHOW_NUM; i++){
			long endTime = formatCurrentTime - Const.RT_SHOW_TIME * i;
			long startTime = endTime - Const.RT_SHOW_TIME + 1; //mysql between包含前后区间值，此处取前开后闭，以防止重复数据。  
			try{
				ExceptionDataRecord exceptionDataRecord = exceptionDataDao.findSummaryByLogSourceIdAndTime(logSourceId, startTime, endTime);
				JSONArray details = new JSONArray();
				if(exceptionDataRecord.getTotalCount() > 0){
					String [] eids = exceptionDataRecord.getExceptionIds().split(",");
					String [] ecounts = exceptionDataRecord.getExceptionCounts().split(",");
					for(int j = 0; j < eids.length; j++){
						int exceptionId = Integer.valueOf(eids[j].trim());
						String type = exceptionDao.findByExceptionId(exceptionId).getExceptionType();
						
						JSONObject detail = new JSONObject();
						detail.put("type", type);
						detail.put("count", ecounts[j]);
						details.add(detail);
					}
				}
				JSONObject record = new JSONObject();
				record.put("date_time", MathUtil.parse2Str(endTime));
				record.put("total_count", exceptionDataRecord.getTotalCount());
				record.put("error_tc", details);
				records.add(record);
			}catch (Exception e) {
				logger.error("error", e);
				return null;
			}
		}
		result.put("record", records);
		return result;
	}
	
	
	@Override
	public JSONObject queryTimeRecords(int logSourceId, long startTime, long endTime, 
			String orderBy, String order, int limit, int offset) {
		List<ExceptionDataRecord> exceptionDataRecords = null;
		try{
			if(orderBy.equals("sample_time")) orderBy="aaa.sample_time";
			exceptionDataRecords = exceptionDataDao.findTimeRecordsByLogSourceIdAndTime(logSourceId, startTime, endTime,
					orderBy, order, limit, offset);
		}catch (Exception e) {
			logger.error("error", e);
			return null;
		}
		//组装数据
		JSONObject result = new JSONObject();
		result.put("projectid", logSourceDao.findByLogSourceId(logSourceId).getProjectId());
		result.put("logsourceid", logSourceId);
		
		JSONArray records = new JSONArray();
		for(ExceptionDataRecord eRecord : exceptionDataRecords){
			JSONObject record = new JSONObject();
			JSONArray details = new JSONArray();
			record.put("date_time", MathUtil.parse2Str(eRecord.getSampleTime()));
			record.put("total_count", eRecord.getTotalCount());
			
			String [] eids = eRecord.getExceptionIds().split(",");
			String [] ecounts = eRecord.getExceptionCounts().split(",");
			for(int i = 0; i < eids.length; i++){
				int exceptionId = Integer.valueOf(eids[i].trim());
				String type = exceptionDao.findByExceptionId(exceptionId).getExceptionType();
				
				JSONObject detail = new JSONObject();
				detail.put("type", type);
				detail.put("count", ecounts[i]);
				details.add(detail);
			}
			record.put("error_tc", details);
			records.add(record);
		}
		result.put("record", records);
		return result;
	}

	
	@Override
	public JSONObject queryErrorRecords(int logSourceId, long startTime, long endTime,
			String orderBy, String order, int limit, int offset) {
		List<ExceptionData> exceptionDatas = null;
		try{
			exceptionDatas = exceptionDataDao.findErrorRecordsByLogSourceIdAndTime(logSourceId, startTime, endTime,
					orderBy, order, limit, offset);
		}catch (Exception e) {
			logger.error("error", e);
			return null;
		}
		//组装数据
		JSONObject result = new JSONObject();
		result.put("projectid", logSourceDao.findByLogSourceId(logSourceId).getProjectId());
		result.put("logsourceid", logSourceId);
		
		JSONArray errors = new JSONArray();
		for(ExceptionData exceptionData : exceptionDatas){
			JSONObject error = new JSONObject();
			com.netease.qa.log.meta.Exception exception = exceptionDao.findByExceptionId(exceptionData.getExceptionId());
			error.put("exp_id", exception.getExceptionId());
			error.put("error_type", exception.getExceptionType());
			error.put("error_example", exception.getExceptionDemo());
			error.put("total_count", exceptionData.getExceptionCount());
			errors.add(error);
		}
		result.put("error", errors);
		return result;
	}
	
	
	@Override
	public JSONObject queryErrorRecordsWithTimeDetail(int logSourceId, long startTime, long endTime,
			String orderBy, String order, int limit, int offset) {
		List<ExceptionData> exceptionDatas = null;
		try{
			exceptionDatas = exceptionDataDao.findErrorRecordsByLogSourceIdAndTime(logSourceId, startTime, endTime,
					orderBy, order, limit, offset);
		}catch (Exception e) {
			logger.error("error", e);
			return null;
		}
		//组装数据
		JSONObject result = new JSONObject();
		result.put("projectid", logSourceDao.findByLogSourceId(logSourceId).getProjectId());
		result.put("logsourceid", logSourceId);
		
		JSONArray errors = new JSONArray();
		for(ExceptionData exceptionData : exceptionDatas){
			JSONObject error = new JSONObject();
			com.netease.qa.log.meta.Exception exception = exceptionDao.findByExceptionId(exceptionData.getExceptionId());
			error.put("error_type", exception.getExceptionType());
			error.put("error_example", exception.getExceptionDemo());
			error.put("total_count", exceptionData.getExceptionCount());
			JSONArray details = new JSONArray();
			List<ExceptionData> tmp = exceptionDataDao.findErrorRecordsByLogSourceIdAndExceptionIdAndTime(logSourceId,
					exceptionData.getExceptionId(), startTime, endTime, "sample_time", "desc", 99999, 0);
			for(ExceptionData e : tmp){
				JSONObject detail = new JSONObject();
				detail.put(MathUtil.parse2Str(e.getSampleTime()), e.getExceptionCount());
				details.add(detail);
			}
			error.put("detail", details);
			errors.add(error);
		}
		result.put("error", errors);
		return result;
	}
	

	@Override
	public JSONObject queryUnknownExceptions(int logSourceId, long startTime, long endTime, int limit, int offset) {
		List<UkExceptionData> ukExceptionDatas = null;
		try{
			ukExceptionDatas = ukExceptionDataDao.findByLogSourceIdAndTime(logSourceId, startTime, endTime, limit, offset);
		}catch (Exception e) {
			logger.error("error", e);
			return null;
		}
		//组装数据
		JSONObject result = new JSONObject();
		result.put("projectid", this.logSourceDao.findByLogSourceId(logSourceId).getProjectId());
		result.put("logsourceid", logSourceId);
		//查不到数据，unknown部分为空
		if (ukExceptionDatas.size() == 0){
			result.put("unknowns", new JSONArray());
			return result;
		}
		JSONArray unknowns = new JSONArray();
		for (UkExceptionData uk : ukExceptionDatas) {
			JSONObject unknown = new JSONObject();
			unknown.put(MathUtil.parse2Str(uk.getOriginLogTime()), uk.getOriginLog());
			unknowns.add(unknown);
		}
		result.put("unknowns", unknowns);
		return result;
	}



}
