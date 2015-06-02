package com.netease.qa.log.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.qa.log.meta.Project;

public interface ProjectService {

	public int createProject(Project project);
	
	public int updateProject(Project project);
	
	public JSONObject getDetailByProjectId(int projectid);
	
	public Project getByProjectId(int projectid);
	
	public JSONArray getAllProjects();
	
	public boolean checkProjectExsit(int projectid);

	public boolean checkProjectExsit(String name);
	
}
