package com.netease.qa.log.storm.service;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netease.qa.log.meta.LogSource;
import com.netease.qa.log.meta.Project;
import com.netease.qa.log.meta.dao.LogSourceDao;
import com.netease.qa.log.meta.dao.ProjectDao;
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
	private static ConcurrentHashMap<Integer, Project> projectCache;

	static{
		logSourceCache = new ConcurrentHashMap<String, LogSource>();
		logSourceIdCache = new ConcurrentHashMap<Integer, LogSource>();
		projectCache = new ConcurrentHashMap<Integer, Project>();
	}
	
	 
	public static LogSource getLogSource(String hostname, String path, String filePattern){
		String key = hostname + "_" + path + "_" + filePattern;
		if(logSourceCache.containsKey(key)){
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
				logSourceCache.put(key, logSource);
				logSourceIdCache.put(logSource.getLogSourceId(), logSource);

				int projectId = logSource.getProjectId();
				if(!projectCache.containsKey(projectId)){
					ProjectDao projectDao = sqlSession.getMapper(ProjectDao.class);
					Project project = projectDao.findByProjectId(projectId);
					if(project == null) return null;// CACHE 、DB中都没有数据，但是datastream有—— ds agent可能未及时更新，数据应该丢弃，等待内存数据定时更新

					projectCache.put(projectId, project); 
				}
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
				
				logSourceCache.put(logSource.getHostname() + "_" + logSource.getPath() + "_" + logSource.getFilePattern(), logSource);
				logSourceIdCache.put(logSourceId, logSource);
				
				int projectId = logSource.getProjectId();
				if(!projectCache.containsKey(projectId)){
					ProjectDao projectDao = sqlSession.getMapper(ProjectDao.class);
					Project project = projectDao.findByProjectId(projectId);
					if(project == null) return null;
					
					projectCache.put(projectId, project); 
				}
				return logSource;
			}
			finally{
				sqlSession.close();
			}
		}
	}
	
	 
	public static Project getProject(int projectId){
		if(projectCache.containsKey(projectId)){
			return projectCache.get(projectId);
		}
		else{
			SqlSession sqlSession = MybatisUtil.getSqlSessionFactory().openSession(true);
			try{
				logger.info("---get project from DB---");
				ProjectDao projectDao = sqlSession.getMapper(ProjectDao.class);
				Project project = projectDao.findByProjectId(projectId);
				if(project == null) return null;
				projectCache.put(projectId, project); 
				return project;
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
			ProjectDao projectDao = sqlSession.getMapper(ProjectDao.class);

			//更新logSourceIdCache
			for(Entry<Integer, LogSource> l: logSourceIdCache.entrySet()){
				logSourceIdCache.remove(l.getKey());
				LogSource logSource = logSourceDao.findByLogSourceId(l.getKey());
				if(logSource != null){
					logSourceIdCache.put(l.getKey(), logSource);
				}
			}
			// 更新logSourceCache
			for (Entry<String, LogSource> l : logSourceCache.entrySet()) {
				logSourceCache.remove(l.getKey());
				String [] keys = l.getKey().split("_");
				LogSource logSource = logSourceDao.findByLocation(keys[0], keys[1], keys[2]);
				if(logSource != null){
					logSourceCache.put(l.getKey(), logSource);
				}
			}
			// 更新projectCache
			for (Entry<Integer, Project> p : projectCache.entrySet()) {
				projectCache.remove(p.getKey());
				Project project = projectDao.findByProjectId(p.getKey());
				if(project != null){ 
					projectCache.put(p.getKey(), project);
				}
			}
			logger.info("logSourceCache:   " + logSourceCache.size()); 
			logger.info("logSourceIdCache: " + logSourceIdCache.size()); 
			logger.info("projectCache:     " + projectCache.size()); 
		}
		finally{
			sqlSession.close();
		}
	}
	
}
