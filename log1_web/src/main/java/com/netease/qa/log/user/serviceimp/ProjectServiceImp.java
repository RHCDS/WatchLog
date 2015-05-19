package com.netease.qa.log.user.serviceimp;


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
import com.netease.qa.log.user.service.ProjectService;

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
		// TODO Auto-generated method stub
		Project project = projectDao.findByName(name_eng);
		if(project != null){
//			System.out.println("该项目已经存在，不能创建！");
			//409,Conflict，已存在
			return -2;
		}
		Project newProject = new Project();
		newProject.setProjectName(name);
		newProject.setProjectEngName(name_eng);
		newProject.setTimeAccuracy(accuracy);
		try {
			projectDao.insert(newProject);
			return newProject.getProjectId();
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			logger.info(e);
			//500,内部错误
			return 0;
		}
	}

	@Override
	public int updateProject(int projectid, String name, String name_eng,
			int accuracy) {
		// TODO Auto-generated method stub
		Project oldProject = projectDao.findByProjectId(projectid);
		if(oldProject == null){
//			System.out.println("更改的项目不存在！");
			//返回404  状态
			return -1;
		}
		Project newProject = oldProject;
		newProject.setProjectName(name);
		newProject.setProjectEngName(name_eng);
		newProject.setTimeAccuracy(accuracy);
		try {
			projectDao.update(newProject);
			return 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//最好返回一个500 内部错误状态码，但是不显示任何信息，封装错误信息，用户不可见
//			e.printStackTrace();
			logger.info(e);
			return 0;
		}
	}

	@Override
	public JSONObject findProject(int projectid) {
		// TODO Auto-generated method stub
		Project oldProject = projectDao.findByProjectId(projectid);
		List<LogSource> logSources = this.logSourceDao.selectAllByProjectId(projectid);
		if(logSources.size() == 0 || oldProject == null)
			return null;
		
		JSONObject project = new JSONObject();
		JSONArray logsources = new JSONArray();
		JSONObject logsource = new JSONObject();
		
		LogSource logSource = new LogSource();
		
		for(int i=0;i < logSources.size();i++){
			logSource = logSources.get(i);
			logsource.put("logsourceid", logSource.getLogSourceId());
			logsource.put("logsourcename", logSource.getLogSourceName());
			logsource.put("logsourcemodifytime", logSource.getModifyTime().toString());
			logsource.put("hostname", logSource.getHostname());
			logsource.put("path", logSource.getPath());
			logsource.put("filepattern", logSource.getFilePattern());
			logsource.put("linestart", logSource.getLineStartRegex());
			logsource.put("filterkeyword", logSource.getLineFilterKeyword());
			logsource.put("typeregex", logSource.getLineTypeRegex());
			logsource.put("logsourcecreator", logSource.getLogSourceCreatorName());
			logsource.put("logsourcestatus", logSource.getLogSourceStatus());
			logsources.add(logsource);
		}
		project.put("projectid", oldProject.getProjectId());
		project.put("name", oldProject.getProjectName());
		project.put("name_eng", oldProject.getProjectEngName());
		project.put("accuracy", oldProject.getTimeAccuracy());
		project.put("logsource", logsources);
		logger.info("找到project项目");
		return project;
	}
}
