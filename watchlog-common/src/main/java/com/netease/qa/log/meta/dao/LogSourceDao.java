package com.netease.qa.log.meta.dao;

import java.util.List;

import com.netease.qa.log.meta.LogSource;


public interface LogSourceDao {
	
	public int insert(LogSource logSource);
	
	public int update(LogSource logSource);

	public int delete(int logSourceId);
    
    public LogSource findByLogSourceId(int logSourceId);
    
    public LogSource findByLocation(String hostname, String path, String filePattern);
    
    public List<LogSource> selectAllByProjectId(int projectId);
    
    public int countAllByProjectId(int projectId);
    
    public List<LogSource> findByProjectId(int projectId, int limit, int offset);
    
}
