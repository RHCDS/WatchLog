package com.netease.qa.log.service.impl;


import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.qa.log.meta.LogSource;
import com.netease.qa.log.meta.Project;
import com.netease.qa.log.meta.dao.LogSourceDao;
import com.netease.qa.log.meta.dao.ProjectDao;
import com.netease.qa.log.service.ProjectService;
import com.netease.qbs.QbsService;

@Service
public class ProjectServiceImpl implements ProjectService {

	private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

	@Resource
	private ProjectDao projectDao;
	
	@Resource
	private LogSourceDao logSourceDao;
	
	
	@Override
	public int createProject(Project project) {
		try {
			projectDao.insert(project);
			return project.getProjectId();
		} catch (Exception e) {
			logger.error("error", e);
			return 0;
		}
	}
	

	@Override
	public int updateProject(Project project) {
		try {
			projectDao.update(project);
			return 1;
		} catch (Exception e) {
			logger.error("error", e);
			return 0;
		}
	}
	

	@Override
	public JSONObject getDetailByProjectId(int projectid) {
		Project project = projectDao.findByProjectId(projectid);
		JSONObject result = new JSONObject();
		result.put("projectid", project.getProjectId());
		result.put("name", project.getProjectName());
		result.put("accuracy", project.getTimeAccuracy());
		
		List<LogSource> list = this.logSourceDao.selectAllByProjectId(projectid);
		JSONArray logsources = new JSONArray();
		
		for(int i = 0; i < list.size(); i++){
			LogSource tmp = list.get(i);
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
			logsource.put("creatorid", tmp.getLogSourceCreatorId());
			logsource.put("status", tmp.getLogSourceStatus());
			logsources.add(logsource);
		}
		result.put("logsources", logsources);
		return result;
	}
	
	
	@Override
	public Project getByProjectId(int projectid) {
		Project project = projectDao.findByProjectId(projectid);
		return project;
	}
	
	
	@Override
	public JSONArray getAllProjects() {
		List<Project> projects = projectDao.getAllProjects();
		if(projects.size() == 0){
			return new JSONArray();
		}
		JSONArray records = new JSONArray();
		Project project = null;
		JSONObject record;
		int i = 0;
		while(i < projects.size()){
			project = projects.get(i);
			record = new JSONObject();
			record.put("id", project.getProjectId());
			record.put("name", project.getProjectName());
			records.add(record);
			i++;
		}
		return records;
	}
	

	@Override
	public boolean checkProjectExsit(int projectid) {
		Project project = projectDao.findByProjectId(projectid);
		return project != null;

	}


	@Override
	public boolean checkProjectExsit(String name) {
		Project project = projectDao.findByName(name);
		return project != null;
	}


	

}




