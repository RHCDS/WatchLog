package com.netease.qa.log.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.qa.log.meta.ExceptionData;
import com.netease.qa.log.meta.ExceptionDataRecord;
import com.netease.qa.log.meta.LogSource;
import com.netease.qa.log.meta.UkExceptionData;
import com.netease.qa.log.meta.dao.ExceptionDao;
import com.netease.qa.log.meta.dao.ExceptionDataDao;
import com.netease.qa.log.meta.dao.LogSourceDao;
import com.netease.qa.log.meta.dao.UkExceptionDataDao;
import com.netease.qa.log.service.ReadService;
import com.netease.qa.log.util.Const;
import com.netease.qa.log.util.ConstCN;
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
	public int getErrorRecordsCountByLogSourceIdAndExceptionIdAndTime(int logSourceId, int exceptionId, long startTime,
			long endTime) {
		return exceptionDataDao.getErrorRecordsCountByLogSourceIdAndExceptionIdAndTime(logSourceId, exceptionId,
				startTime, endTime);
	}

	@Override
	public JSONObject queryLatestTimeRecords(int logSourceId, long currentTime) {
		// 时间精度取整： xx:xx:00 、xx:xx:30两种精度
		Long formatCurrentTime = currentTime / Const.RT_SHOW_TIME * Const.RT_SHOW_TIME;
		JSONObject result = new JSONObject();
		result.put("projectid", logSourceDao.findByLogSourceId(logSourceId).getProjectId());
		result.put("logsourceid", logSourceId);
		JSONArray records = new JSONArray();
		for (int i = 0; i < Const.RT_SHOW_NUM; i++) {
			long endTime = formatCurrentTime - Const.RT_SHOW_TIME * i;
			long startTime = endTime - Const.RT_SHOW_TIME + 1; // mysql
			// between包含前后区间值，此处取前开后闭，以防止重复数据。
			try {
				ExceptionDataRecord exceptionDataRecord = exceptionDataDao.findSummaryByLogSourceIdAndTime(logSourceId,
						startTime, endTime);
				JSONArray details = new JSONArray();
				if (exceptionDataRecord != null && exceptionDataRecord.getTotalCount() > 0) {
					String[] eids = exceptionDataRecord.getExceptionIds().split(",");
					String[] ecounts = exceptionDataRecord.getExceptionCounts().split(",");
					for (int j = 0; j < eids.length; j++) {
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
				record.put(
						"total_count",
						exceptionDataRecord != null && exceptionDataRecord.getTotalCount() > 0 ? exceptionDataRecord
								.getTotalCount() : 0);
				record.put("error_tc", details);
				records.add(record);
			} catch (Exception e) {
				logger.error("error", e);
				return null;
			}
		}
		result.put("record", records);
		return result;
	}

	@Override
	public JSONArray queryRecordsByTime(int projectid, long start_time, long end_time) {
		// 时间精度取整： xx:xx:00 、xx:xx:30两种精度
		// 获取时间区间，和次数
		int timeRange = MathUtil.getTimeRange(start_time, end_time);
		int num = (int) ((end_time - start_time) / timeRange);
		Long formatStartTime = start_time / timeRange * timeRange;
		JSONArray results = new JSONArray();
		List<LogSource> logSources = logSourceDao.getSortedByProjectId(projectid, "modify_time", "desc", 100, 0);
		for (int i = 0; i < logSources.size(); i++) {
			JSONObject result = new JSONObject();
			LogSource logSource = logSources.get(i);
			JSONArray datas = new JSONArray();
			for (int j = 0; j < num; j++) {
				long startTime = formatStartTime + timeRange * j + 1;
				long endTime = startTime + timeRange;
				int totalCount = 0;
				// between包含前后区间值，此处取前开后闭，以防止重复数据。
				try {
					int logSourceId = logSource.getLogSourceId();
					Integer total = exceptionDataDao.getTotalCountByLogsourceIdAndTime(logSourceId, startTime, endTime);
					if (total != null) {
						totalCount = total;
					} else {
						totalCount = 0;
					}
					JSONObject data = new JSONObject();
					data.put("x", endTime * 1000);
					data.put("y", totalCount);
					datas.add(data);
				} catch (Exception e) {
					logger.error("error", e);
					return null;
				}
			}
			result.put("logsrc_name", logSource.getLogSourceName());
			result.put("data", datas);
			results.add(result);
		}
		return results;
	}

	@Override
	public JSONObject queryTimeRecords(int logSourceId, long startTime, long endTime, String orderBy, String order,
			int limit, int offset) {
		List<ExceptionDataRecord> exceptionDataRecords = null;
		try {
			if (orderBy.equals("sample_time"))
				orderBy = "aaa.sample_time";
			exceptionDataRecords = exceptionDataDao.findTimeRecordsByLogSourceIdAndTime(logSourceId, startTime,
					endTime, orderBy, order, limit, offset);
		} catch (Exception e) {
			logger.error("error", e);
			return null;
		}
		// 组装数据
		JSONObject result = new JSONObject();
		result.put("projectid", logSourceDao.findByLogSourceId(logSourceId).getProjectId());
		result.put("logsourceid", logSourceId);

		JSONArray records = new JSONArray();
		for (ExceptionDataRecord eRecord : exceptionDataRecords) {
			JSONObject record = new JSONObject();
			JSONArray details = new JSONArray();
			record.put("date_time", MathUtil.parse2Str(eRecord.getSampleTime()));
			record.put("total_count", eRecord.getTotalCount());

			String[] eids = eRecord.getExceptionIds().split(",");
			String[] ecounts = eRecord.getExceptionCounts().split(",");
			for (int i = 0; i < eids.length; i++) {
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
	public JSONObject queryErrorRecords(int logSourceId, long startTime, long endTime, String orderBy, String order,
			int limit, int offset) {
		List<ExceptionData> exceptionDatas = null;
		ExceptionData unknownexception = exceptionDataDao.findUnknownTypeByLogSourceIdAndTime(logSourceId, startTime,
				endTime, Const.UNKNOWN_TYPE);
		try {
			exceptionDatas = exceptionDataDao.findErrorRecordsByLogSourceIdAndTime(logSourceId, startTime, endTime,
					orderBy, order, limit, offset);
		} catch (Exception e) {
			logger.error("error", e);
			return null;
		}
		// 组装数据
		JSONObject result = new JSONObject();
		result.put("projectid", logSourceDao.findByLogSourceId(logSourceId).getProjectId());
		result.put("logsourceid", logSourceId);
		JSONObject error = new JSONObject();
		JSONArray errors = new JSONArray();
		if (unknownexception != null) {
			com.netease.qa.log.meta.Exception ukexception = exceptionDao.findByExceptionId(unknownexception
					.getExceptionId());
			error.put("exp_id", ukexception.getExceptionId());
			error.put("error_type", Const.UNKNOWN_TYPE);
			error.put("error_example", Const.UNKNOWN_TYPE);
			error.put("total_count", unknownexception.getExceptionCount());
			errors.add(error);
		}
		for (ExceptionData exceptionData : exceptionDatas) {
			com.netease.qa.log.meta.Exception exception = exceptionDao
					.findByExceptionId(exceptionData.getExceptionId());
			error = new JSONObject();
			if (!exception.getExceptionType().equals(Const.UNKNOWN_TYPE)) {
				error.put("exp_id", exception.getExceptionId());
				error.put("error_type", exception.getExceptionType());
				error.put("error_example", exception.getExceptionDemo());
				error.put("total_count", exceptionData.getExceptionCount());
				errors.add(error);
			}
		}
		result.put("error", errors);
		return result;
	}

	@Override
	public JSONObject queryErrorRecordsWithTimeDetail(int logSourceId, long startTime, long endTime, String orderBy,
			String order, int limit, int offset) {
		List<ExceptionData> exceptionDatas = null;
		ExceptionData unknownexception = exceptionDataDao.findUnknownTypeByLogSourceIdAndTime(logSourceId, startTime,
				endTime, Const.UNKNOWN_TYPE);
		try {
			exceptionDatas = exceptionDataDao.findErrorRecordsByLogSourceIdAndTime(logSourceId, startTime, endTime,
					orderBy, order, limit, offset);
		} catch (Exception e) {
			logger.error("error", e);
			return null;
		}
		// 组装数据
		JSONObject result = new JSONObject();
		result.put("projectid", logSourceDao.findByLogSourceId(logSourceId).getProjectId());
		result.put("logsourceid", logSourceId);
		JSONObject error = new JSONObject();
		JSONArray errors = new JSONArray();
		// 当unknown不为空，且第一页，才把error装入array中
		if (unknownexception != null && offset == 0) {
			com.netease.qa.log.meta.Exception ukexception = exceptionDao.findByExceptionId(unknownexception
					.getExceptionId());
			error.put("exp_id", ukexception.getExceptionId());
			error.put("error_type", Const.UNKNOWN_TYPE);
			error.put("error_example", Const.UNKNOWN_TYPE);
			error.put("total_count", unknownexception.getExceptionCount());
			error.put("detail", new JSONArray());
			errors.add(error);
		}
		for (ExceptionData exceptionData : exceptionDatas) {
			error = new JSONObject();
			com.netease.qa.log.meta.Exception exception = exceptionDao
					.findByExceptionId(exceptionData.getExceptionId());
			if (!exception.getExceptionType().equals(Const.UNKNOWN_TYPE)) {
				error.put("exp_id", exception.getExceptionId());
				error.put("error_type", exception.getExceptionType());
				error.put("error_example", exception.getExceptionDemo());
				error.put("total_count", exceptionData.getExceptionCount());
				/**
				 * detail
				 * jsonarray在web端，没有用（因为detail被写在另外一个接口queryDetailByErrorType中
				 * ），只是在api中才有用
				 */
				JSONArray details = new JSONArray();
				List<ExceptionData> tmp = exceptionDataDao.findErrorRecordsByLogSourceIdAndExceptionIdAndTime(
						logSourceId, exceptionData.getExceptionId(), startTime, endTime, "sample_time", "desc", 99999,
						0);
				for (ExceptionData e : tmp) {
					JSONObject detail = new JSONObject();
					detail.put(MathUtil.parse2Str(e.getSampleTime()), e.getExceptionCount());
					details.add(detail);
				}
				error.put("detail", details);
				errors.add(error);
			}
		}
		result.put("error", errors);
		return result;
	}

	@Override
	public JSONObject queryUnknownExceptions(int logSourceId, long startTime, long endTime, int limit, int offset) {
		List<UkExceptionData> ukExceptionDatas = null;
		try {
			ukExceptionDatas = ukExceptionDataDao.findByLogSourceIdAndTime(logSourceId, startTime, endTime, limit,
					offset);
		} catch (Exception e) {
			logger.error("error", e);
			return null;
		}
		// 组装数据
		JSONObject result = new JSONObject();
		result.put("projectid", this.logSourceDao.findByLogSourceId(logSourceId).getProjectId());
		result.put("logsourceid", logSourceId);
		// 查不到数据，unknown部分为空
		if (ukExceptionDatas.size() == 0) {
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

	@Override
	public JSONObject queryDetailByErrorType(int logSourceId, int exceptionId, long startTime, long endTime,
			String sort, String order, int limit, int offset) {
		JSONObject result = new JSONObject();
		JSONArray details = new JSONArray();
		List<ExceptionData> tmp = exceptionDataDao.findErrorRecordsByLogSourceIdAndExceptionIdAndTime(logSourceId,
				exceptionId, startTime, endTime, sort, order, limit, offset);
		for (ExceptionData e : tmp) {
			JSONObject detail = new JSONObject();
			detail.put("date_time", MathUtil.parse2Str(e.getSampleTime()));
			detail.put("total_count", e.getExceptionCount());
			details.add(detail);
		}
		result.put("details", details);
		return result;
	}

	@Override
	public JSONObject queryExceptionByLogSourceIdAndTime(int logSourceId, long startTime, long endTime) {
		ExceptionDataRecord exceptionDataRecord = null;
		try {
			exceptionDataRecord = exceptionDataDao.findRecordsByLogSourceIdAndTime(logSourceId, startTime, endTime);
		} catch (Exception e) {
			logger.error("error", e);
			return null;
		}
		// 组装数据
		JSONObject result = new JSONObject();
		JSONArray details = new JSONArray();
		if (exceptionDataRecord != null) {
			String[] eids = exceptionDataRecord.getExceptionIds().split(",");
			String[] ecounts = exceptionDataRecord.getExceptionCounts().split(",");
			for (int i = 0; i < eids.length; i++) {
				int exceptionId = Integer.valueOf(eids[i].trim());
				String type = exceptionDao.findByExceptionId(exceptionId).getExceptionType();
				JSONObject detail = new JSONObject();
				detail.put("type", type);
				detail.put("count", ecounts[i]);
				details.add(detail);
			}
			result.put("error_tc", details);
			result.put("total_count", exceptionDataRecord.getTotalCount());
			return result;
		} else {
			result.put("error_tc", details);
			result.put("total_count", 0);
			return result;
		}
	}

	/**
	 * AB平台
	 */
	@Override
	public JSONObject queryErrorRecordsByLogSourceIds(List<Integer> logSourceIds, long start_time, long end_time) {
		JSONObject result = new JSONObject();
		Map<String, JSONArray> temp = new HashMap<String, JSONArray>();
		for (int i = 0; i < logSourceIds.size(); i++) {
			JSONObject detail = new JSONObject(); // detail
			int logsourceId = logSourceIds.get(i);
			detail.put("logsourceid", logsourceId);
			LogSource logSource = logSourceDao.findByLogSourceId(logsourceId);
			String serverName = logSource.getHostname().trim();
			Integer total_count = exceptionDataDao.getLogSourceExceptionTotalCountByTime(logsourceId, start_time,
					end_time);
			if (total_count == null) {
				total_count = 0;
			}
			detail.put("total_count", total_count);
			logger.info("logsourceId=" + logsourceId + ";total_count=" + total_count);
			// 获取error_tc数组
			JSONArray error_tcs = new JSONArray();
			JSONObject error_tc;
			List<ExceptionData> exceptionDatas = exceptionDataDao.findErrorRecordsByLogSourceIdAndTimeByAB(logsourceId,
					start_time, end_time);
			if (exceptionDatas != null && exceptionDatas.size() > 0) {
				for (ExceptionData exceptionData : exceptionDatas) {
					error_tc = new JSONObject();
					error_tc.put("type", exceptionData.getExceptionType());
					error_tc.put("count", exceptionData.getExceptionCount());
					error_tcs.add(error_tc);
				}
			}
			detail.put("error_tc", error_tcs);
			JSONArray details = null;
			if (!temp.containsKey(serverName)) {
				details = new JSONArray();
				details.add(detail);
			} else {
				details = temp.get(serverName);
				details.add(detail);
			}
			temp.put(serverName, details);
		}
		// 把hashmap中的数据封装起来
		JSONArray records = new JSONArray();
		JSONObject record = null;
		@SuppressWarnings("rawtypes")
		Iterator iter = temp.entrySet().iterator();
		while (iter.hasNext()) {
			record = new JSONObject();
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			JSONArray val = (JSONArray) entry.getValue();
			record.put("hostname", key);
			record.put("detail", val);
			records.add(record);
		}
		result.put("record", records);
		return result;
	}
}
