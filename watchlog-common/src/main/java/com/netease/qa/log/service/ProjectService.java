package com.netease.qa.log.service;

import com.alibaba.fastjson.JSONArray;
import com.netease.qbs.meta.Project;
import com.netease.qbs.meta.User;

public interface ProjectService {

	public JSONArray getAllProjectsByQbs(User user);
	
	public boolean checkProjectExsit(int projectId);
	
	public Project findByProjectId(int projectId);
}
