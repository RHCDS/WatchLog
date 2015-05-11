package com.netease.qa.log.meta.service;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.netease.qa.log.meta.Exception;
import com.netease.qa.log.meta.dao.ExceptionDao;
import com.netease.qa.log.util.MybatisUtil;


public class ExceptionService {
	
	private ExceptionDao exceptionDao = null;

	public ExceptionService(){
		SqlSessionFactory sqlSessionFactory = MybatisUtil.getSqlSessionFactory();
		SqlSession sqlSession = sqlSessionFactory.openSession(true);
		exceptionDao = sqlSession.getMapper(ExceptionDao.class);
	}

	
	public int insert(Exception exception){
		exceptionDao.insert(exception);
		return exception.getExceptionId();
	}
	
	
	public int delete(int exceptionId){
		return exceptionDao.delete(exceptionId);
	}
    
	
	public Exception findByExceptionId(int exceptionId){
	    	return exceptionDao.findByExceptionId(exceptionId);
	}
	
    
    public Exception findByTwoKey(int logSourceId, String exceptionTypeMD5){
    	return exceptionDao.findByTwoKey(logSourceId, exceptionTypeMD5);
    }
    
    
}
