package com.netease.qa.log.user.serviceimp;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.qa.log.meta.LogSource;
import com.netease.qa.log.meta.Project;
import com.netease.qa.log.meta.dao.LogSourceDao;
import com.netease.qa.log.meta.dao.ProjectDao;
import com.netease.qa.log.user.service.ProjectService;

@Service
public class ProjectServiceImp implements ProjectService {

	@Resource
	private ProjectDao projectDao;
	@Resource
	private LogSourceDao logSourceDao;
	
	@Override
	public int addProject(String name, String name_eng, int accuracy) {
		// TODO Auto-generated method stub
		Project project = projectDao.findByName(name_eng);
		if(project != null){
			System.out.println("该项目已经存在，不能创建！");
			return project.getProjectId();
		}
		Project newProject = new Project();
		newProject.setProjectName(name);
		newProject.setProjectEngName(name_eng);
		newProject.setTimeAccuracy(accuracy);
		projectDao.insert(newProject);
		return newProject.getProjectId();
	}

	@Override
	public int updateProject(int projectid, String name, String name_eng,
			int accuracy) {
		// TODO Auto-generated method stub
		Project oldProject = projectDao.findByProjectId(projectid);
		if(oldProject == null){
			System.out.println("更改的项目不存在！");
			return 0;
		}
		Project newProject = oldProject;
		newProject.setProjectName(name);
		newProject.setProjectEngName(name_eng);
		newProject.setTimeAccuracy(accuracy);
		int updateSuccess = projectDao.update(newProject);
		return updateSuccess;
	}

	@Override
	public JSONObject findProject(int projectid) {
		// TODO Auto-generated method stub
		Project oldProject = projectDao.findByProjectId(projectid);
		List<LogSource> logSources = this.logSourceDao.selectAllByProjectId(projectid);
		if(logSources.size() == 0 || oldProject == null)
			return new JSONObject();
		
		JSONObject project = new JSONObject();
		JSONArray logsources = new JSONArray();
		JSONObject logsource = new JSONObject();
		
		LogSource logSource = new LogSource();
		
		for(int i=0;i < logSources.size();i++){
			logSource = logSources.get(i);
			logsource.put("logsourceid", logSource.getLogSourceId());
			logsource.put("hostname", logSource.getHostname());
			logsource.put("path", logSource.getPath());
			logsource.put("filepattern", logSource.getFilePattern());
			logsource.put("linestart", logSource.getLineStartRegex());
			logsource.put("filterkeyword", logSource.getLineFilterKeyword());
			logsource.put("typeregex", logSource.getLineTypeRegex());
			logsources.add(logsource);
		}
		project.put("projectid", oldProject.getProjectId());
		project.put("name", oldProject.getProjectName());
		project.put("name_eng", oldProject.getProjectEngName());
		project.put("accuracy", oldProject.getTimeAccuracy());
		project.put("status", oldProject.getProjectStatus());
		project.put("logsource", logsources);
		return project;
	}

	@Override
	public int updateProjectStatus(int projectid, int status) {
		// TODO Auto-generated method stub
		Project project = projectDao.findByProjectId(projectid);
		if(project == null){
			System.out.println("该项目不存在，不能修改状态！");
			return 0;
		}
		project.setProjectStatus(status);
		int updateStatus = projectDao.update(project);
		return updateStatus;
	}

}
