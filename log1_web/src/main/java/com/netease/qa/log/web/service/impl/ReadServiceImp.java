package com.netease.qa.log.web.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.qa.log.meta.ExceptionData;
import com.netease.qa.log.meta.UkExceptionData;
import com.netease.qa.log.meta.dao.ExceptionDao;
import com.netease.qa.log.meta.dao.ExceptionDataDao;
import com.netease.qa.log.meta.dao.LogSourceDao;
import com.netease.qa.log.meta.dao.UkExceptionDataDao;
import com.netease.qa.log.web.service.ReadService;

@Service
public class ReadServiceImp implements ReadService {

	private static final Logger logger = Logger.getLogger(ReadServiceImp.class);

	@Resource
	private ExceptionDao exceptionDao;
	@Resource
	private ExceptionDataDao exceptionDataDao;
	@Resource
	private UkExceptionDataDao ukExceptionDataDao;
	@Resource
	private LogSourceDao logSourceDao;

	@Override
	public JSONObject queryTimeRecords(int logSourceId, long startTime, long endTime, int limit, int offset) {
		List<ExceptionData> exceptionDatas = null;
		try{
			exceptionDatas = this.exceptionDataDao.findByLogSourceIdAndTime(logSourceId, startTime, endTime, "sample_time", limit, offset);
		}catch (Exception e) {
			logger.error(e);
			return null;
		}
		//组装数据
		JSONObject result = new JSONObject();
		result.put("projectid", logSourceDao.findByLogSourceId(logSourceId).getProjectId());
		result.put("logsourceid", logSourceId);
		//查询不到数据，返回的record部分为空
		if (exceptionDatas.size() == 0){
			result.put("record", new JSONArray()); 
			return result;
		}
		//组装record部分数据
		JSONArray records = new JSONArray();
		JSONObject record = new JSONObject();
		JSONArray details = new JSONArray();

		ExceptionData first = exceptionDatas.get(0);
		record.put("time", convert(first.getSampleTime()));
		record.put("totalcount", first.getExceptionCount());

		JSONObject detail = new JSONObject();
		detail.put(this.exceptionDao.findByExceptionId(first.getExceptionId()).getExceptionType(), first.getExceptionCount());
		details.add(detail);
		for (int i = 1; i < exceptionDatas.size(); i++) {
			ExceptionData next = exceptionDatas.get(i);
			// 若后续的异常数据的采样时间和第一个一样，那么就把他们的数量加起来
			if (convert(next.getSampleTime()).equals(record.getString("time"))) {
				record.put("totalcount", record.getInteger("totalcount") + next.getExceptionCount());
				detail = new JSONObject();
				detail.put(this.exceptionDao.findByExceptionId(next.getExceptionId()).getExceptionType(),
						next.getExceptionCount());
				details.add(detail);
			} else {
				record.put("detail", details);
				records.add(record);
				record = new JSONObject();
				details = new JSONArray();
				record.put("time", convert(next.getSampleTime()));
				record.put("totalcount", next.getExceptionCount());
				detail = new JSONObject();
				detail.put(this.exceptionDao.findByExceptionId(next.getExceptionId()).getExceptionType(),
						next.getExceptionCount());
				details.add(detail);
			}
		}
		record.put("detail", details);
		records.add(record);
		result.put("record", records);
		logger.info("queryTimeRecords excutes successful.");
		return result;
	}

	
	@Override
	public JSONObject queryErrorRecords(int logSourceId, long startTime, long endTime, int limit, int offset) {
		// TODO Auto-generated method stub
		List<ExceptionData> exceptionDatas = exceptionDataDao.findByLogSourceIdAndTime(logSourceId, startTime, endTime,
				"exception_id", limit, offset);
		if (exceptionDatas.size() == 0)
			return null;

		JSONArray errors = new JSONArray();
		JSONObject error = new JSONObject();
		JSONArray details = new JSONArray();

		ExceptionData first = exceptionDatas.get(0);
		error.put("type", this.exceptionDao.findByExceptionId(first.getExceptionId()).getExceptionType());
		error.put("totalcount", first.getExceptionCount());
		error.put("demo", this.exceptionDao.findByExceptionId(first.getExceptionId()).getExceptionDemo());

		JSONObject detail = new JSONObject();
		detail.put(convert(first.getSampleTime()), first.getExceptionCount());
		details.add(detail);

		for (int i = 1; i < exceptionDatas.size(); i++) {
			ExceptionData next = exceptionDatas.get(i);
			// 按异常聚合，如果后续的异常类型，与第一个的异常类型一致，就加入第一个异常details中
			if (this.exceptionDao.findByExceptionId(next.getExceptionId()).getExceptionType()
					.equals(error.getString("type"))) {
				error.put("totalcount", error.getInteger("totalcount") + next.getExceptionCount());
				detail = new JSONObject();
				detail.put(convert(next.getSampleTime()), next.getExceptionCount());
				details.add(detail);
			} else {
				error.put("detail", details);
				errors.add(error);
				error = new JSONObject();
				details = new JSONArray();
				error.put("type", this.exceptionDao.findByExceptionId(next.getExceptionId()).getExceptionType());
				error.put("totalcount", next.getExceptionCount());
				error.put("demo", this.exceptionDao.findByExceptionId(next.getExceptionId()).getExceptionDemo());
				detail = new JSONObject();
				detail.put(convert(next.getSampleTime()), next.getExceptionCount());
				details.add(detail);
			}
		}
		error.put("detail", details);
		errors.add(error);

		JSONObject result = new JSONObject();
		result.put("projectid", this.logSourceDao.findByLogSourceId(logSourceId).getProjectId());
		result.put("logsourceid", logSourceId);
		result.put("error", errors);
		logger.info("queryErrorRecords excutes successful.");
		return result;
	}

	@Override
	public JSONObject queryUnknownExceptions(int logSourceId, long startTime, long endTime, int limit, int offset) {
		// TODO Auto-generated method stub
		List<UkExceptionData> ukExceptionDatas = ukExceptionDataDao.findByLogSourceIdAndTime(logSourceId, startTime,
				endTime, limit, offset);
		if (ukExceptionDatas.size() == 0)
			return null;

		JSONArray unknowns = new JSONArray();

		for (UkExceptionData uk : ukExceptionDatas) {
			JSONObject unknown = new JSONObject();
			unknown.put(convert(uk.getOriginLogTime()), uk.getOriginLog());
			unknowns.add(unknown);
		}

		JSONObject result = new JSONObject();
		result.put("projectid", this.logSourceDao.findByLogSourceId(logSourceId).getProjectId());
		result.put("logsourceid", logSourceId);
		result.put("unknowns", unknowns);
		logger.info("queryUnknownExceptions excutes successful.");
		return result;
	}

	private static String convert(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(time * 1000));
	}

}
