package com.netease.qa.log.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.qa.log.meta.LogSource;

public interface LogSourceService {

	public int createLogSource(LogSource logSource);
	
	public int updateLogSource(LogSource logSource);
	
	public JSONObject getDetailByLogSourceId(int logSourceid);
	
	public LogSource getByLogSourceId(int logSourceid);
	
	public LogSource getByLocation(String hostname, String path, String filePattern);
	
	public int deleteLogSource(int logSourceid);
	
	public int deleteLogSources(int[] ids);
	
	public boolean checkLogSourceExist(String hostname, String path, String filePattern);
	
	public boolean checkLogSourceExist(int logSourceid);
	
	public boolean checkLogSourceExist(String logsourceName);
	
	public int getTotalCountByProjectId(int projectid);
	
	public JSONArray getLogSourcesListByProjectid(int project, int limit, int offset);
	
	public JSONArray getLogSourcesListSortedByProjectid(int project, String field, String order, int limit, int offset);
	
	public int changeMonitorStatus(int[] ids, int status);

}
