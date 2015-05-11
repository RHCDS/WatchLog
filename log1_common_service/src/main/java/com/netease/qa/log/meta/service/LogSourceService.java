package com.netease.qa.log.meta.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.netease.qa.log.meta.LogSource;
import com.netease.qa.log.meta.dao.LogSourceDao;
import com.netease.qa.log.util.MybatisUtil;


public class LogSourceService {
	
	private LogSourceDao logSourceDao = null;
	
	public LogSourceService(){
		SqlSessionFactory sqlSessionFactory = MybatisUtil.getSqlSessionFactory();
		SqlSession sqlSession = sqlSessionFactory.openSession(true);
		logSourceDao = sqlSession.getMapper(LogSourceDao.class);
	}
	
	
	public int insert(LogSource logSource){
		logSourceDao.insert(logSource);
		return logSource.getLogSourceId();
	}
	
	
	public int update(LogSource logSource){
		return logSourceDao.update(logSource);
	}

	
	public int delete(int logSourceId){
		return logSourceDao.delete(logSourceId);
	}
    
	
    public LogSource findByLogSourceId(int logSourceId){
    	return logSourceDao.findByLogSourceId(logSourceId);
    }
    
    
    public LogSource findByLocation(String hostname, String path, String filePattern){
    	return logSourceDao.findByLocation(hostname, path, filePattern);
    }
    
    
    public List<LogSource> selectAllByProjectId(int projectId){
    	return logSourceDao.selectAllByProjectId(projectId);
    }

    
}
