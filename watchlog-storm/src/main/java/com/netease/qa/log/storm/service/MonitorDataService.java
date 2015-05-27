package com.netease.qa.log.storm.service;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netease.qa.log.meta.Exception;
import com.netease.qa.log.meta.ExceptionData;
import com.netease.qa.log.meta.UkExceptionData;
import com.netease.qa.log.meta.dao.ExceptionDao;
import com.netease.qa.log.meta.dao.ExceptionDataDao;
import com.netease.qa.log.meta.dao.UkExceptionDataDao;
import com.netease.qa.log.storm.util.MybatisUtil;


/**
 * 监控数据
 * @author hzzhangweijie
 *
 */

public class MonitorDataService {
	
	private static final Logger logger = LoggerFactory.getLogger(MonitorDataService.class);

	private static ConcurrentHashMap<String, Exception> exceptionCache;
	private static ConcurrentHashMap<Integer, Exception> exceptionIdCache;
	private static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> exceptionCountCache;
	
	private static ExceptionDao exceptionDao;
	private static ExceptionDataDao exceptionDataDao;
	private static UkExceptionDataDao ukExceptionDataDao;
	
	static{
		exceptionCache = new ConcurrentHashMap<String, Exception>();
		exceptionIdCache = new ConcurrentHashMap<Integer, Exception>();
		exceptionCountCache = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>>();

		SqlSessionFactory sqlSessionFactory = MybatisUtil.getSqlSessionFactory();
		SqlSession sqlSession = sqlSessionFactory.openSession(true);
		exceptionDao = sqlSession.getMapper(ExceptionDao.class);
		exceptionDataDao = sqlSession.getMapper(ExceptionDataDao.class);
		ukExceptionDataDao = sqlSession.getMapper(UkExceptionDataDao.class);
	}
	
	
	public static Exception getException(int logSourceId, String exceptionTypeMD5){
		String key = logSourceId + "_" + exceptionTypeMD5;
		if(exceptionCache.containsKey(key)){
			return exceptionCache.get(key);
		}
		else{
			Exception exception = exceptionDao.findByTwoKey(logSourceId, exceptionTypeMD5);
			if(exception != null){
				exceptionCache.put(key, exception);
				exceptionIdCache.put(exception.getExceptionId(), exception);
			}
			return exception;
		}
	}
	
	
	public static Exception getException(int exceptionId){
		if(exceptionIdCache.containsKey(exceptionId)){
			return exceptionIdCache.get(exceptionId);
		}
		else{
			Exception exception = exceptionDao.findByExceptionId(exceptionId);
			if(exception != null){
				exceptionIdCache.put(exception.getExceptionId(), exception);
			}
			return exception;
		}
	}
	

	public static int putException(int logSourceId, String exceptionTypeMD5, String exceptionType, String exceptionDemo){
		Exception exception = new Exception();
		exception.setLogSourceId(logSourceId);
		exception.setExceptionTypeMD5(exceptionTypeMD5);
		exception.setExceptionType(exceptionType);
		exception.setExceptionDemo(exceptionDemo);
		
		exceptionDao.insert(exception);
		exceptionCache.put(logSourceId + "_" + exceptionTypeMD5, exception);
		return exception.getExceptionId();
	}
	
	
	public static void putUkExceptionData(int logSourceId, Long originLogTime, String originLog){
		UkExceptionData ukExceptionData = new UkExceptionData();
		ukExceptionData.setLogSourceId(logSourceId);
		ukExceptionData.setOriginLogTime(originLogTime);
		ukExceptionData.setOriginLog(originLog);
		ukExceptionDataDao.insert(ukExceptionData);
	}
	
	
	public static void putExceptionData(int logSourceId, int exceptionId){
		if(!exceptionCountCache.containsKey(logSourceId)){ 
			ConcurrentHashMap<Integer, Integer> tmp = new ConcurrentHashMap<Integer, Integer>();
			tmp.put(exceptionId, 1);
			exceptionCountCache.put(logSourceId, tmp);
		}
		else{
			ConcurrentHashMap<Integer, Integer> tmp = exceptionCountCache.get(logSourceId);
			if(!tmp.containsKey(exceptionId)){
				tmp.put(exceptionId, 1);
			}
			else{
				tmp.put(exceptionId, tmp.get(exceptionId) + 1);
			}
		}
	}
	
	
	/**
	 * 写入数据库 exception_data表
	 * @param sampleTime
	 */
	public static void writeExceptionData(Long sampleTime){
		logger.info("---write exception data into DB---");
		for(Entry<Integer, ConcurrentHashMap<Integer, Integer>> e1: exceptionCountCache.entrySet()){
			int logSourceId = e1.getKey();
			ConcurrentHashMap<Integer, Integer> tmp = e1.getValue();
			
			for(Entry<Integer, Integer> e2: tmp.entrySet()){
				int exceptionId = e2.getKey();
				int count = e2.getValue();
				
				ExceptionData exceptionData = new ExceptionData();
				exceptionData.setLogSourceId(logSourceId);
				exceptionData.setExceptionId(exceptionId);
				exceptionData.setSampleTime(sampleTime);
				exceptionData.setExceptionCount(count);
				exceptionDataDao.insert(exceptionData);
				logger.info(exceptionData.toString());
			}
		}
		exceptionCountCache.clear();
	}

	
}
