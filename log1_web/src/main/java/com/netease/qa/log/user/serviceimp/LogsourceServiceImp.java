package com.netease.qa.log.user.serviceimp;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.netease.qa.log.meta.LogSource;
import com.netease.qa.log.meta.Project;
import com.netease.qa.log.meta.dao.LogSourceDao;
import com.netease.qa.log.meta.dao.ProjectDao;
import com.netease.qa.log.user.service.LogsourceService;

@Service
public class LogsourceServiceImp implements LogsourceService{

	private static final Logger logger = Logger.getLogger(LogsourceServiceImp.class);
	
	@Resource
	private LogSourceDao logSourceDao;
	@Resource 
	private ProjectDao projectDao;
	
	
	@Override
	public int addLogsource(int projectId, String hostname, String path,
			String filePattern, String linestart, String filterkeyword,
			String typeregex) {
		// TODO Auto-generated method stub
		LogSource logSource = logSourceDao.findByLocation(hostname, path, filePattern);
		Project project = projectDao.findByProjectId(projectId);
		
		if(logSource != null)
		{
			//相同路径的日志源已存在
			System.out.println("相同路径的日志源已存在");
			return logSource.getLogSourceId();
		}
		if(project == null){
			//如果项目不存在
			System.out.println("项目不存在");
			return 0;
		}
		
		
		LogSource newLogSource = new LogSource();
		newLogSource.setProjectId(projectId);
		newLogSource.setHostname(hostname);
		newLogSource.setPath(path);
		newLogSource.setFilePattern(filePattern);
		newLogSource.setLineStartRegex(linestart);
		newLogSource.setLineFilterKeyword(filterkeyword);
		newLogSource.setLineTypeRegex(typeregex);
		logSourceDao.insert(newLogSource);
		
		
//		logger.info(logSourceid);
		return newLogSource.getLogSourceId();
	}

	@Override
	public int updateLogsource(int logsourceid, String hostname,
			String path, String filepattern, String linestart,
			String filterkeyword, String typeregex) {
		// TODO Auto-generated method stub
		LogSource logSource = logSourceDao.findByLogSourceId(logsourceid);
		if(logSource == null)
		{
			System.out.println("你输入的logsourceid，不存在");
			return 0;
		}
		//把原来的数据中logsource赋给新的
		LogSource updateLogSource = logSource;
		//下面是，传入几个参数，就set几个参数
		updateLogSource.setHostname(hostname);
		updateLogSource.setPath(path);
		updateLogSource.setFilePattern(filepattern);
		updateLogSource.setLineStartRegex(linestart); 
		updateLogSource.setLineFilterKeyword(filterkeyword);
		updateLogSource.setLineTypeRegex(typeregex);
		int updateSuccess = logSourceDao.update(updateLogSource);
		return updateSuccess;
	}

	@Override
	public LogSource findLogsource(int logsourceid) {
		// TODO Auto-generated method stub
		LogSource logSource = logSourceDao.findByLogSourceId(logsourceid);
		if(logSource == null)
		{
			System.out.println("日志源不存在！");
			return null;
		}
		return logSource;
	}

	@Override
	public int deleteLogsource(int logsourceid) {
		// TODO Auto-generated method stub
		LogSource logSource = logSourceDao.findByLogSourceId(logsourceid);
		if(logSource == null)
		{
			System.out.println("日志不存在，无法删除");
			return 0;
		}
		int deleteSuccess = logSourceDao.delete(logsourceid);
		return deleteSuccess;
	}

}
