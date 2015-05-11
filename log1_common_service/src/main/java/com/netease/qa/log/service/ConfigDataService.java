package com.netease.qa.log.service;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.netease.qa.log.meta.LogSource;
import com.netease.qa.log.meta.Project;
import com.netease.qa.log.meta.service.LogSourceService;
import com.netease.qa.log.meta.service.ProjectService;


/**
 * 配置信息
 * @author hzzhangweijie
 *
 */
public class ConfigDataService {
	
	private static final Logger logger = Logger.getLogger(ConfigDataService.class);

	private static ConcurrentHashMap<String, LogSource> logSourceCache;
	private static ConcurrentHashMap<Integer, LogSource> logSourceIdCache;
	private static ConcurrentHashMap<Integer, Project> projectCache;
	private static LogSourceService logSourceService;
	private static ProjectService projectService;

	static{
		logSourceCache = new ConcurrentHashMap<String, LogSource>();
		logSourceIdCache = new ConcurrentHashMap<Integer, LogSource>();
		projectCache = new ConcurrentHashMap<Integer, Project>();
		logSourceService = new LogSourceService();
		projectService = new ProjectService();
	}
	
	//这几个函数都是相辅相成的
	
	public static LogSource getLogSource(String hostname, String path, String filePattern){
		String key = hostname + "_" + path + "_" + filePattern;
		if(logSourceCache.contains(key)){
			return logSourceCache.get(key);
		}
		else{
			//如果数据库还没有添加这个日资源呢？  logSource返回为null
			LogSource logSource = logSourceService.findByLocation(hostname, path, filePattern);
			logSourceCache.put(key, logSource);
			logSourceIdCache.put(logSource.getLogSourceId(), logSource);
			
			int projectId = logSource.getProjectId();
			if(!projectCache.contains(projectId)){
				Project project = projectService.findByProjectId(projectId);
				projectCache.put(projectId, project); 
			}
			return logSource;
		}
	}
	
	
	public static LogSource getLogSource(int logSourceId){
		if(logSourceIdCache.contains(logSourceId)){
			return logSourceIdCache.get(logSourceId);
		}
		else{
			//为什么取数据库里的日资源到logSourceCache中？？？
			LogSource logSource = logSourceService.findByLogSourceId(logSourceId);
			logSourceCache.put(logSource.getHostname() + "_" + logSource.getPath() + "_" + logSource.getFilePattern(), logSource);
			logSourceIdCache.put(logSourceId, logSource);
			
			//为什么要把project加入到projectCache中？
			int projectId = logSource.getProjectId();
			if(!projectCache.contains(projectId)){
				Project project = projectService.findByProjectId(projectId);
				projectCache.put(projectId, project); 
			}
			return logSource;
		}
	}
	
	//依赖上面的函数，先把有日资源的项目添加到projectCache中，极大地提高速度。
	public static Project getProject(int projectId){
		if(projectCache.contains(projectId)){
			return projectCache.get(projectId);
		}
		else{
	//Cache不到时，才会去数据中找，这个效率就低得多
			Project project = projectService.findByProjectId(projectId);
			projectCache.put(projectId, project); 
			return project;
		}
	}
	
	
	//用户用来更新配置，用户更新配置直接写入数据库，不经过这个Cache??
	public static void loadConfig(){
		
		//更新logSourceIdCache
		logger.info("start to load logSource!");
		for(Entry<Integer, LogSource> l: logSourceIdCache.entrySet()){
			LogSource logSource = logSourceService.findByLogSourceId(l.getKey());
			logSourceIdCache.put(l.getKey(), logSource);
			logger.info(logSource); 
		}

		// 更新logSourceCache
		for (Entry<String, LogSource> l : logSourceCache.entrySet()) {
			String [] keys = l.getKey().split("_");
			LogSource logSource = logSourceService.findByLocation(keys[0], keys[1], keys[2]);
			logSourceCache.put(l.getKey(), logSource);
		}

		// 更新projectCache
		logger.info("start to load project!");
		for (Entry<Integer, Project> p : projectCache.entrySet()) {
			Project project = projectService.findByProjectId(p.getKey());
			projectCache.replace(p.getKey(), project);
			logger.info(project); 
		}
		logger.info("load config success!");

	}
	
	
	
}
