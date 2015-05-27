package com.netease.qa.log.meta.dao;


import com.netease.qa.log.meta.Project;


public interface ProjectDao {
	
	public int insert(Project project);
	
	public int update(Project project);

	public int delete(int projectId);
    
    public Project findByProjectId(int projectId);
    
    public Project findByName(String name_eng);
    
}
