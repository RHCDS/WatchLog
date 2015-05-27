package com.netease.qa.log.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.netease.qa.log.meta.LogSource;
import com.netease.qa.log.meta.dao.LogSourceDao;
import com.netease.qa.log.meta.dao.ProjectDao;
import com.netease.qa.log.service.LogSourceService;


@Service
public class LogsourceServiceImpl implements LogSourceService{

	private static final Logger logger = Logger.getLogger(LogsourceServiceImpl.class);
	
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
			logger.error(e);
			return 0;
		}	
	}

	
	@Override
	public int updateLogSource(LogSource logSource) {
		try {
			logSourceDao.update(logSource);
			return 1;
		} catch (Exception e) {
			logger.info(e);
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
		logger.debug(result);
		return result;
	}
	
	
	@Override
	public LogSource getByLogSourceId(int logSourceid) {
		return logSourceDao.findByLogSourceId(logSourceid);
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
			logger.error(e);
			return 0;
		}
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

	
}
