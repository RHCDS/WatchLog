package com.netease.qa.log.storm.service;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netease.qa.log.meta.LogSource;
import com.netease.qa.log.meta.dao.LogSourceDao;
import com.netease.qa.log.storm.util.MybatisUtil;


/**
 * 配置信息
 * @author hzzhangweijie
 *
 */
public class ConfigDataService {
	
	private static final Logger logger = LoggerFactory.getLogger(ConfigDataService.class);

	private static ConcurrentHashMap<String, LogSource> logSourceCache;
	private static ConcurrentHashMap<Integer, LogSource> logSourceIdCache;

	static{
		logSourceCache = new ConcurrentHashMap<String, LogSource>();
		logSourceIdCache = new ConcurrentHashMap<Integer, LogSource>();
	}
	
	 
	public static LogSource getLogSource(String hostname, String path, String filePattern){
		String key = hostname + "_" + path + "_" + filePattern;
		if(logSourceCache.containsKey(key)){
			logger.info("---get LogSource from CACHE---");
			return logSourceCache.get(key);
		}
		else{
			logger.info("---get LogSource from DB---");
			SqlSession sqlSession = MybatisUtil.getSqlSessionFactory().openSession(true);
			try{
				LogSourceDao logSourceDao = sqlSession.getMapper(LogSourceDao.class);
				LogSource logSource = logSourceDao.findByLocation(hostname, path, filePattern);
				if(logSource == null) return null; // CACHE 、DB中都没有数据，但是datastream有—— ds agent可能未及时更新，数据应该丢弃，等待内存数据定时更新 
				// TODO 存在性能隐患 
				logSource.convertParams();
				logSourceCache.put(key, logSource);
				logSourceIdCache.put(logSource.getLogSourceId(), logSource);
				return logSource;
			}
			finally{
				sqlSession.close();
			}
		}
	}
	
	
	public static LogSource getLogSource(int logSourceId){
		if(logSourceIdCache.containsKey(logSourceId)){
			return logSourceIdCache.get(logSourceId); 
		}
		else{
			logger.info("---get LogSource from DB---");
			SqlSession sqlSession = MybatisUtil.getSqlSessionFactory().openSession(true);
			try{
				LogSourceDao logSourceDao = sqlSession.getMapper(LogSourceDao.class);
				LogSource logSource = logSourceDao.findByLogSourceId(logSourceId);
				if(logSource == null) return null;
				
				logSource.convertParams();
				logSourceCache.put(logSource.getHostname() + "_" + logSource.getPath() + "_" + logSource.getFilePattern(), logSource);
				logSourceIdCache.put(logSourceId, logSource);
				return logSource;
			}
			finally{
				sqlSession.close();
			}
		}
	}
	
	public static void loadConfig(){
		logger.info("---reload config cache from DB---");
		SqlSession sqlSession = MybatisUtil.getSqlSessionFactory().openSession(true);
		try{
			LogSourceDao logSourceDao = sqlSession.getMapper(LogSourceDao.class);
			//更新logSourceIdCache
			for(Entry<Integer, LogSource> l: logSourceIdCache.entrySet()){
				logSourceIdCache.remove(l.getKey());
				LogSource logSource = logSourceDao.findByLogSourceId(l.getKey());
				if(logSource != null){
					logSource.convertParams();
					logSourceIdCache.put(l.getKey(), logSource);
					logger.info("reload config for logSourceIdCache: " + logSource.getLogSourceName() + " " + l.getKey());
				}
			}
			// 更新logSourceCache
			for (Entry<String, LogSource> l : logSourceCache.entrySet()) {
				logSourceCache.remove(l.getKey());
				String [] keys = l.getKey().split("_");
				LogSource logSource = logSourceDao.findByLocation(keys[0], keys[1], keys[2]);
				if(logSource != null){
					logSource.convertParams();
					logSourceCache.put(l.getKey(), logSource);
					logger.info("reload config for logSourceCache: " + logSource.getLogSourceName() + " " + l.getKey());
				}
			}
			logger.info("logSourceCache:   " + logSourceCache.size()); 
			logger.info("logSourceIdCache: " + logSourceIdCache.size()); 
		}
		finally{
			sqlSession.close();
		}
	}
	
}
