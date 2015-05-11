package com.netease.qa.log.service;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.qa.log.meta.Exception;
import com.netease.qa.log.meta.ExceptionData;
import com.netease.qa.log.meta.UkExceptionData;
import com.netease.qa.log.meta.dao.ExceptionDao;
import com.netease.qa.log.meta.dao.ExceptionDataDao;
import com.netease.qa.log.meta.dao.UkExceptionDataDao;
import com.netease.qa.log.meta.service.ExceptionDataService;
import com.netease.qa.log.meta.service.ExceptionService;
import com.netease.qa.log.meta.service.UkExceptionDataService;


/**
 * 监控数据
 * @author hzzhangweijie
 *
 */

public class MonitorDataService {
	
	private static final Logger logger = Logger.getLogger(MonitorDataService.class);
	
	//ConcurrentHashMap 同步的线程安全的，支持高并发
	private static ConcurrentHashMap<String, Exception> exceptionCache;//高并发的异常缓存
	private static ConcurrentHashMap<Integer, Exception> exceptionIdCache;//异常id缓存
	//???
	private static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> exceptionCountCache;
//	private static ExceptionService exceptionService;
	
	@Autowired
	private static ExceptionDao exceptionDao;
//	private static ExceptionDataService exceptionDataService;
	private static ExceptionDataDao exceptionDataDao;
//	private static UkExceptionDataService ukExceptionDataService;
	private static UkExceptionDataDao ukExceptionDataDao;

	static{
		exceptionCache = new ConcurrentHashMap<String, Exception>();
		exceptionIdCache = new ConcurrentHashMap<Integer, Exception>();
		exceptionCountCache = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>>();
//		exceptionService = new ExceptionService();
//		exceptionDataService = new ExceptionDataService();
//		ukExceptionDataService = new UkExceptionDataService();
	}
	
	
	public static Exception getException(int logSourceId, String exceptionTypeMD5){
		//日志源+异常MD5是一个主键（唯一确定）或异常id
		String key = logSourceId + "_" + exceptionTypeMD5;
		if(exceptionCache.containsKey(key)){
			return exceptionCache.get(key);
		}
		else{
			Exception exception = exceptionDao.findByTwoKey(logSourceId, exceptionTypeMD5);
			if(exception != null){
				exceptionCache.put(key, exception);
				//就是为了下一个函数getException(int exceptionId)的方便使用
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
			System.out.println("!!!!!!!!!!!:" + exceptionId);
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
		
		int exceptionId = exceptionDao.insert(exception);
		exceptionCache.put(logSourceId + "_" + exceptionTypeMD5, exception);
		return exceptionId;
	}
	
	
	public static void putUkExceptionData(int logSourceId, Long originLogTime, String originLog){
		UkExceptionData ukExceptionData = new UkExceptionData();
		ukExceptionData.setLogSourceId(logSourceId);
		ukExceptionData.setOriginLogTime(originLogTime);
		ukExceptionData.setOriginLog(originLog);
		ukExceptionDataDao.insert(ukExceptionData);
	}
	
	
	//统计在一个采样时间段内，所有的异常数量
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
	
	//等把异常数量统计完成后，才能写入数据库中
	public static void writeExceptionData(Long sampleTime){
		
		//for循环的这种用法，注意！.entrySet()方法返回一个set集合，集合的对象是Entry<..>
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
				logger.info(exceptionData);
			}
		}
		exceptionCountCache.clear();
	}

	
}
