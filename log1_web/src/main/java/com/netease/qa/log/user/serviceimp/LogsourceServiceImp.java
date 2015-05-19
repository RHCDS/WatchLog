package com.netease.qa.log.user.serviceimp;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.netease.qa.log.meta.LogSource;
import com.netease.qa.log.meta.Project;
import com.netease.qa.log.meta.dao.LogSourceDao;
import com.netease.qa.log.meta.dao.ProjectDao;
import com.netease.qa.log.user.service.LogsourceService;


/*
 * return 1+ 表示成功，httpStatus:200
 * return 0 表示内部错误,httpStatus:400
 * return -1 表示不存在,httpStatus:404
 * return -2 表示已存在，有冲突，httpStatus:409
 */
@Service
public class LogsourceServiceImp implements LogsourceService{

	private static final Logger logger = Logger.getLogger(LogsourceServiceImp.class);
	
	@Resource
	private LogSourceDao logSourceDao;
	
	@Resource 
	private ProjectDao projectDao;
	
	
	@Override
	public int addLogsource(String logsourcename, int projectId, String hostname, String path,
			String filePattern, String linestart, String filterkeyword,
			String typeregex, String creatorname) {
		LogSource logSource = logSourceDao.findByLocation(hostname, path, filePattern);
		Project project = projectDao.findByProjectId(projectId);
		
		//返回409状态，Conflict
		if(logSource != null){
			return -2;
		}
		//返回404 ,不存在
		if(project == null){
			return -1;
		}
		logSource = new LogSource();
		logSource.setLogSourceName(logsourcename);
		logSource.setProjectId(projectId);
		logSource.setHostname(hostname);
		logSource.setPath(path);
		logSource.setFilePattern(filePattern);
		logSource.setLineStartRegex(linestart);
		logSource.setLineFilterKeyword(filterkeyword);
		logSource.setLineTypeRegex(typeregex);
		logSource.setLogSourceCreatorName(creatorname);
		try {
			logSourceDao.insert(logSource);
			return logSource.getLogSourceId();
		} catch (Exception e) {
			//返回500，内部执行失败，但是只显示空白，不显示具体错误信息，用户不可见
			logger.error(e);
			return 0;
		}	
	}

	
	@Override
	public int updateLogsource( int logsourceid, String logsourcename, String hostname,
			String path, String filepattern, String linestart,
			String filterkeyword, String typeregex) {
		LogSource logSource = logSourceDao.findByLogSourceId(logsourceid);
		//返回404状态,-1表示要修改的日志源不存在
		if(logSource == null){
			return -1;
		}
		//下面是，传入几个参数，就set几个参数
		logSource.setLogSourceName(logsourcename);
		logSource.setHostname(hostname);
		logSource.setPath(path);
		logSource.setFilePattern(filepattern);
		logSource.setLineStartRegex(linestart); 
		logSource.setLineFilterKeyword(filterkeyword);
		logSource.setLineTypeRegex(typeregex);
		try {
			logSourceDao.update(logSource);
			//1表示修改成功
			return 1;
		} catch (Exception e) {
			//返回500执行错误，显示空JSON
			logger.info(e);
			//0表示参数错误
			return 0;
		}
	}

	
	@Override
	public JSONObject findLogsource(int logsourceid) {
		LogSource logSource;
		try {
			logSource = logSourceDao.findByLogSourceId(logsourceid);
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
		//404
		if(logSource == null){
			return null;
		}
		JSONObject logsource = new JSONObject();
		logsource.put("logsourceid", logSource.getLogSourceId());
		logsource.put("logsourcename", logSource.getLogSourceName());
		logsource.put("projectid", logSource.getProjectId());
		logsource.put("modifytime", logSource.getModifyTime().toString());
		logsource.put("hostname", logSource.getHostname());
		logsource.put("path", logSource.getPath());
		logsource.put("filepattern", logSource.getFilePattern());
		logsource.put("linestart", logSource.getLineStartRegex());
		logsource.put("filterkeyword", logSource.getLineFilterKeyword());
		logsource.put("typeregex", logSource.getLineTypeRegex());
		logsource.put("creator", logSource.getLogSourceCreatorName());
		logsource.put("logsourcestatus", logSource.getLogSourceStatus());
		logger.info(logsource);
		return logsource;
	}
	

	@Override
	public int deleteLogsource(int logsourceid) {
		LogSource logSource = logSourceDao.findByLogSourceId(logsourceid);
		//404
		if(logSource == null){
			return -1;
		}
		try {
			logSourceDao.delete(logsourceid);
			return 1;
		} catch (Exception e) {
			logger.error(e);
			return 0;
		}
	}
	
	
	@Override
	public int changeLogsourceStatus(int logsourceid, int status) {
		LogSource logsource = logSourceDao.findByLogSourceId(logsourceid);
		if(logsource == null){
			return -1;
		}
		logsource.setLogSourceStatus(status);
		try {
			logSourceDao.update(logsource);
			return 1;
		} catch (Exception e) {
			//400,执行错误
			logger.error(e);
			return 0;
		}
	}
	
}
