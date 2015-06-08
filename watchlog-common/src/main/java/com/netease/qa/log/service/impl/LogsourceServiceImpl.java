package com.netease.qa.log.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.qa.log.meta.LogSource;
import com.netease.qa.log.meta.dao.LogSourceDao;
import com.netease.qa.log.meta.dao.ProjectDao;
import com.netease.qa.log.service.LogSourceService;
import com.netease.qa.log.util.MathUtil;


@Service
public class LogsourceServiceImpl implements LogSourceService{

	private static final Logger logger = LoggerFactory.getLogger(LogsourceServiceImpl.class);
	
	@Resource
	private LogSourceDao logSourceDao;
	
	@Resource 
	private ProjectDao projectDao;
	
	
	@Override
	public int createLogSource(LogSource logSource) {
		try {
			logSourceDao.insert(logSource);
			return logSource.getLogSourceId();
		} catch (Exception e) {
			logger.error("error", e);
			return 0;
		}	
	}

	
	@Override
	public int updateLogSource(LogSource logSource) {
		try {
			logSourceDao.update(logSource);
			return 1;
		} catch (Exception e) {
			logger.error("error", e);
			return 0;
		}
	}

	
	@Override
	public JSONObject getDetailByLogSourceId(int logsourceid) {
		LogSource logSource = logSourceDao.findByLogSourceId(logsourceid);  
		if(logSource == null)
			return null;
		
		JSONObject result = new JSONObject();
		result.put("logsourceid", logSource.getLogSourceId());
		result.put("logsourcename", logSource.getLogSourceName());
		result.put("projectid", logSource.getProjectId());
		result.put("modifytime", logSource.getModifyTime().toString());
		result.put("hostname", logSource.getHostname());
		result.put("path", logSource.getPath());
		result.put("filepattern", logSource.getFilePattern());
		result.put("linestart", logSource.getLineStartRegex());
		result.put("filterkeyword", logSource.getLineFilterKeyword());
		result.put("typeregex", logSource.getLineTypeRegex());
		result.put("creator", logSource.getLogSourceCreatorName());
		result.put("status", logSource.getLogSourceStatus());
		logger.debug(result.toJSONString());
		return result;
	}
	
	
	@Override
	public LogSource getByLogSourceId(int logSourceid) {
		return logSourceDao.findByLogSourceId(logSourceid);
	}
	

	@Override
	public boolean checkLogSourceExist(String logsourceName) {
			LogSource logsource = logSourceDao.findByLogSourceName(logsourceName);
			return logsource != null;
	}
	
	
	@Override
	public LogSource getByLocation(String hostname, String path, String filePattern) {
		return logSourceDao.findByLocation(hostname, path, filePattern);
	}
	

	@Override
	public int deleteLogSource(int logsourceid) {
		try {
			logSourceDao.delete(logsourceid);
			return 1;
		} catch (Exception e) {
			logger.error("error", e);
			return 0;
		}
	}

	
	@Override
	public int deleteLogSources(int[] ids) {
		for(int i=0; i < ids.length; i++){
			try {
				logSourceDao.delete(ids[i]);
			} catch (Exception e) {
				logger.error("error", e);
				return 0;
			}
		}
		return 1;
	}

	
	@Override
	public boolean checkLogSourceExist(String hostname, String path, String filePattern) {
		LogSource logsource = logSourceDao.findByLocation(hostname, path, filePattern);
		return logsource != null;
	}

	
	
	

	@Override
	public boolean checkLogSourceExist(int logSourceId) {
		LogSource logsource = logSourceDao.findByLogSourceId(logSourceId);
		return logsource != null;
	}


	@Override
	public int getTotalCountByProjectId(int projectid) {
		int count = logSourceDao.getTotalCountByProjectId(projectid);
		return count;
	}


	@Override
	public JSONArray getLogSourcesListByProjectid(int projectid, int limit, int offset) {
		List<LogSource> logSources = null;
		try {
			logSources = logSourceDao.findByProjectId(projectid, limit, offset);
		} catch (Exception e) {
			logger.error("error", e);
			return null;
		}
		
		JSONArray result = new JSONArray();
		if(logSources.size() == 0){
		  result.add(new JSONObject());
		  return result;
		}
		JSONObject record = null;
		JSONArray records = new JSONArray();
		LogSource logSource = new LogSource();
		int i = 0;
		while(i < logSources.size()){
			record = new JSONObject();
			logSource = logSources.get(i);
			record.put("DT_RowId", logSource.getLogSourceId());
			record.put("logsrc_name", logSource.getLogSourceName());
			record.put("host_name", logSource.getHostname());
			record.put("logsrc_path", logSource.getPath());
			record.put("logsrc_file", logSource.getFilePattern());
			record.put("status", logSource.getLogSourceStatus());
			record.put("update_time", MathUtil.parse2Str(logSource.getModifyTime()));
			record.put("creator", logSource.getLogSourceCreatorName());
			records.add(record);
			i++;
		}
		return records;
	}


	@Override
	public JSONArray getLogSourcesListSortedByProjectid(int project, String field, String order, int limit, int offset) {
		List<LogSource> logSources = null;
		try {
			logSources = logSourceDao.getSortedByProjectId(project, field, order, limit, offset);
		} catch (Exception e) {
			logger.error("error", e);
			return null;
		}
		JSONArray result = new JSONArray();
		if(logSources.size() == 0){
		  result.add(new JSONObject());
		  return result;
		}
		JSONObject record = null;
		JSONArray records = new JSONArray();
		LogSource logSource = new LogSource();
		int i = 0;
		while(i < logSources.size()){
			record = new JSONObject();
			logSource = logSources.get(i);
			record.put("id", logSource.getLogSourceId());
			record.put("logsrc_name", logSource.getLogSourceName());
			record.put("host_name", logSource.getHostname());
			record.put("logsrc_path", logSource.getPath());
			record.put("logsrc_file", logSource.getFilePattern());
			record.put("status", logSource.getLogSourceStatus());
			record.put("update_time", MathUtil.parse2Str(logSource.getModifyTime()));
			record.put("creator", logSource.getLogSourceCreatorName());
			records.add(record);
			i++;
		}
		return records;
	}


	@Override
	public int changeMonitorStatus(int[] ids, int status) {
		LogSource logsource;
		for(int i=0; i < ids.length; i++){
			try {
				logsource = logSourceDao.findByLogSourceId(ids[i]);
				logsource.setLogSourceStatus(status);
				logSourceDao.update(logsource);
			} catch (Exception e) {
				logger.error("error", e);
				return -1;
			}
		}
		return 0;
	}


}
