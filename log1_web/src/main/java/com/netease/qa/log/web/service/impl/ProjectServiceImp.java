package com.netease.qa.log.web.service.impl;


import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.qa.log.meta.LogSource;
import com.netease.qa.log.meta.Project;
import com.netease.qa.log.meta.dao.LogSourceDao;
import com.netease.qa.log.meta.dao.ProjectDao;
import com.netease.qa.log.web.service.ProjectService;

/*
 * return 1+ 表示成功，httpStatus:200
 * return 0 表示内部错误,httpStatus:500
 * return -1 表示不存在,httpStatus:404
 * return -2 表示已存在，有冲突，httpStatus:409
 */
@Service
public class ProjectServiceImp implements ProjectService {

	private static final Logger logger = Logger.getLogger(ProjectServiceImp.class);

	@Resource
	private ProjectDao projectDao;
	
	@Resource
	private LogSourceDao logSourceDao;
	
	@Override
	public int addProject(String name, String name_eng, int accuracy) {
		Project project = projectDao.findByName(name_eng);
		if(project != null){
			//409,Conflict，已存在
			logger.debug("conflict");
			return -2;
		}
		project = new Project();
		project.setProjectName(name);
		project.setProjectEngName(name_eng);
		project.setTimeAccuracy(accuracy);
		try {
			projectDao.insert(project);
			return project.getProjectId();
		} catch (Exception e) {
			logger.error(e);
			return 0;
		}
	}
	

	@Override
	public int updateProject(int projectid, String name, String name_eng,
			int accuracy) {
		Project project = projectDao.findByProjectId(projectid);
		if(project == null){
			return -1;
		}
		project.setProjectName(name);
		project.setProjectEngName(name_eng);
		project.setTimeAccuracy(accuracy);
		try {
			projectDao.update(project);
			return 1;
		} catch (Exception e) {
			logger.error(e);
			return 0;
		}
	}
	

	@Override
	public JSONObject findProject(int projectid) {
		Project project = projectDao.findByProjectId(projectid);
		List<LogSource> logSources = this.logSourceDao.selectAllByProjectId(projectid);
		if(project == null){ //logsource列表可以为空
			return null;
		}
		JSONObject result = new JSONObject();
		JSONArray logsources = new JSONArray();
		
		for(int i = 0; i < logSources.size(); i++){
			LogSource tmp = logSources.get(i);
			JSONObject logsource = new JSONObject();
			logsource.put("logsourceid", tmp.getLogSourceId());
			logsource.put("logsourcename", tmp.getLogSourceName());
			logsource.put("modifytime", tmp.getModifyTime().toString());
			logsource.put("hostname", tmp.getHostname());
			logsource.put("path", tmp.getPath());
			logsource.put("filepattern", tmp.getFilePattern());
			logsource.put("linestart", tmp.getLineStartRegex());
			logsource.put("filterkeyword", tmp.getLineFilterKeyword());
			logsource.put("typeregex", tmp.getLineTypeRegex());
			logsource.put("creator", tmp.getLogSourceCreatorName());
			logsource.put("status", tmp.getLogSourceStatus());
			logsources.add(logsource);
		}
		result.put("projectid", project.getProjectId());
		result.put("name", project.getProjectName());
		result.put("name_eng", project.getProjectEngName());
		result.put("accuracy", project.getTimeAccuracy());
		result.put("logsource", logsources);
		return result;
	}
	
}




