package com.netease.qa.log.meta.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.netease.qa.log.meta.ExceptionData;
import com.netease.qa.log.meta.dao.ExceptionDataDao;
import com.netease.qa.log.util.MybatisUtil;


public class ExceptionDataService {
	
	private ExceptionDataDao exceptionDataDao = null;

	public ExceptionDataService(){
		SqlSessionFactory sqlSessionFactory = MybatisUtil.getSqlSessionFactory();
		SqlSession sqlSession = sqlSessionFactory.openSession(true);
		exceptionDataDao = sqlSession.getMapper(ExceptionDataDao.class);
	}

	
	public int insert(ExceptionData exceptionData){
		exceptionDataDao.insert(exceptionData);
		return exceptionData.getExceptionDataId();
	}
	
	
	public int delete(int exceptionDataId){
		return exceptionDataDao.delete(exceptionDataId);
	}
    
    
    public ExceptionData findByExceptionDataId(int exceptionDataId){
    	return exceptionDataDao.findByExceptionDataId(exceptionDataId);
    }
    
    
    public List<ExceptionData> findByLogSourceIdAndTime(int logSourceId, long startTime, long endTime, String orderBy, int limit, int offset){
    	return exceptionDataDao.findByLogSourceIdAndTime(logSourceId, startTime, endTime, orderBy, limit, offset);
    }
    
    
    public ExceptionData findByExceptionId(int exceptionId){
    	return exceptionDataDao.findByExceptionId(exceptionId);
    }
    
    
}
