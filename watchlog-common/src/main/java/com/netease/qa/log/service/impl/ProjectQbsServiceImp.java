package com.netease.qa.log.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.qa.log.service.ProjectQbsService;
import com.netease.qbs.QbsService;
import com.netease.qbs.meta.Project;
import com.netease.qbs.meta.User;

@Service
public class ProjectQbsServiceImp implements ProjectQbsService{

	@Autowired
    private QbsService qbsService;
	
	@Override
	public JSONArray getAllProjectsByQbs(User user) {
		List<Project> projects = qbsService.getProjects(user.getId());
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
			record.put("id", project.getId());
			record.put("name", project.getName());
			records.add(record);
			i++;
		}
		return records;
	}//urs、datastream、timeline

}
