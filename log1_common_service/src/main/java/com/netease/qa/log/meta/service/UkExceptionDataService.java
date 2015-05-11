package com.netease.qa.log.meta.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.netease.qa.log.meta.UkExceptionData;
import com.netease.qa.log.meta.dao.UkExceptionDataDao;
import com.netease.qa.log.util.MybatisUtil;


public class UkExceptionDataService {
	
	private UkExceptionDataDao ukExceptionDataDao = null;

	public UkExceptionDataService(){
		SqlSessionFactory sqlSessionFactory = MybatisUtil.getSqlSessionFactory();
		SqlSession sqlSession = sqlSessionFactory.openSession(true);
		ukExceptionDataDao = sqlSession.getMapper(UkExceptionDataDao.class);
	}

	
	public int insert(UkExceptionData ukExceptionData){
		ukExceptionDataDao.insert(ukExceptionData);
		return ukExceptionData.getUkExceptionDataId();
	}
	
	
	public int delete(int ukExceptionDataId){
		return ukExceptionDataDao.delete(ukExceptionDataId);
	}
    
    
    public UkExceptionData findByUkExceptionDataId(int ukExceptionDataId){
    	return ukExceptionDataDao.findByUkExceptionDataId(ukExceptionDataId);
    }
    
    
    public List<UkExceptionData> findByLogSourceIdAndTime(int logSourceId, long startTime, long endTime, int limit, int offset){
    	return ukExceptionDataDao.findByLogSourceIdAndTime(logSourceId, startTime, endTime, limit, offset);
    }
    
    
}
