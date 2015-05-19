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
 * return 0 表示参数错误,httpStatus:400
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
		// TODO Auto-generated method stub
		LogSource logSource = logSourceDao.findByLocation(hostname, path, filePattern);
		Project project = projectDao.findByProjectId(projectId);
		
		if(logSource != null)
		{
			//返回409状态，Conflict
			//0表示日志源已存在
			return -2;
		}
		if(project == null){
			//返回404 ,不存在
			//-1表示项目不存在
			return -1;
		}
		LogSource newLogSource = new LogSource();
		newLogSource.setLogSourceName(logsourcename);
		newLogSource.setProjectId(projectId);
		newLogSource.setHostname(hostname);
		newLogSource.setPath(path);
		newLogSource.setFilePattern(filePattern);
		newLogSource.setLineStartRegex(linestart);
		newLogSource.setLineFilterKeyword(filterkeyword);
		newLogSource.setLineTypeRegex(typeregex);
		newLogSource.setLogSourceCreatorName(creatorname);
		try {
			logSourceDao.insert(newLogSource);
			return newLogSource.getLogSourceId();
		} catch (Exception e) {
			//返回500，内部执行失败，但是只显示空白，不显示具体错误信息，用户不可见
			e.printStackTrace();
			return 0;
		}	
	}

	@Override
	public int updateLogsource( int logsourceid, String logsourcename, String hostname,
			String path, String filepattern, String linestart,
			String filterkeyword, String typeregex) {
		// TODO Auto-generated method stub
		LogSource logSource = logSourceDao.findByLogSourceId(logsourceid);
		if(logSource == null)
		{
			//返回404状态,-1表示要修改的日志源不存在
			return -1;
		}
		//把原来的数据中logsource赋给新的
		LogSource updateLogSource = logSource;
		//下面是，传入几个参数，就set几个参数
		updateLogSource.setLogSourceName(logsourcename);
		updateLogSource.setHostname(hostname);
		updateLogSource.setPath(path);
		updateLogSource.setFilePattern(filepattern);
		updateLogSource.setLineStartRegex(linestart); 
		updateLogSource.setLineFilterKeyword(filterkeyword);
		updateLogSource.setLineTypeRegex(typeregex);
		try {
			logSourceDao.update(updateLogSource);
			//1表示修改成功
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//返回500执行错误，显示空JSON
			e.printStackTrace();
			//0表示参数错误
			return 0;
		}
	}

	@Override
	public JSONObject findLogsource(int logsourceid) {
		// TODO Auto-generated method stub
		LogSource logSource;
		try {
			logSource = logSourceDao.findByLogSourceId(logsourceid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		if(logSource == null)
		{
			//404
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
		return logsource;
	}

	@Override
	public int deleteLogsource(int logsourceid) {
		// TODO Auto-generated method stub
		LogSource logSource = logSourceDao.findByLogSourceId(logsourceid);
		if(logSource == null)
		{
			//404
			return -1;
		}
		try {
			logSourceDao.delete(logsourceid);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//500,内部执行错误
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public int changeLogsourceStatus(int logsourceid, int status) {
		// TODO Auto-generated method stub
		LogSource oldLogsource = logSourceDao.findByLogSourceId(logsourceid);
		if(oldLogsource == null){
			return -1;
		}
		LogSource newLogsource = oldLogsource;
		newLogsource.setLogSourceStatus(status);
		try {
			logSourceDao.update(newLogsource);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//400,执行错误
			e.printStackTrace();
			return 0;
		}
	}
	
}
