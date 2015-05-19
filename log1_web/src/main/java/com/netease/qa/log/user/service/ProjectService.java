package com.netease.qa.log.user.service;

import com.alibaba.fastjson.JSONObject;


//项目配置API
public interface ProjectService {

	//创建项目
	public int addProject(String name, String name_eng, int accuracy);
	//修改项目
	public int updateProject(int projectid, String name, String name_eng, int accuracy);
	//查询项目
	public JSONObject findProject(int projectid);
}
