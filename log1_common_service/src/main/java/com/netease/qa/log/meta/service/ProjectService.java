package com.netease.qa.log.meta.service;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.netease.qa.log.meta.Project;
import com.netease.qa.log.meta.dao.ProjectDao;
import com.netease.qa.log.util.MybatisUtil;


public class ProjectService {
	
	private ProjectDao projectDao = null;

	public ProjectService(){
		SqlSessionFactory sqlSessionFactory = MybatisUtil.getSqlSessionFactory();
		SqlSession sqlSession = sqlSessionFactory.openSession(true);
		projectDao = sqlSession.getMapper(ProjectDao.class);
	}
	
	
	public int insert(Project project){
		projectDao.insert(project);
		return project.getProjectId();
	}
	
	public int update(Project project){
		return projectDao.update(project);
	}

	public int delete(int projectId){
		return projectDao.delete(projectId);
	}
    
    public Project findByProjectId(int projectId){
    	return projectDao.findByProjectId(projectId);
    }
	

}
