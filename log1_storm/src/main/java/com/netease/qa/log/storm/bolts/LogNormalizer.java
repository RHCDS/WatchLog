package com.netease.qa.log.storm.bolts;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.netease.qa.log.meta.LogSource;
import com.netease.qa.log.meta.Project;
import com.netease.qa.log.service.ConfigDataService;
import com.netease.qa.log.service.ConfigLoadTask;

public class LogNormalizer implements IBasicBolt {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(LogNormalizer.class);


	@SuppressWarnings("unchecked")
	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		
		Map<String, Object> headers = (Map<String, Object>) input.getValue(1);  
        String hostname = headers.get("__DS_.fields.hostname").toString();  
        String path =  headers.get("__DS_.fields._ds_target_dir").toString();  
        String filePattern =  headers.get("__DS_.fields._ds_file_pattern").toString();  
        String dsTime =  headers.get("__DS_.timestamp").toString();   
		
        LogSource logsource = ConfigDataService.getLogSource(hostname, path, filePattern);
        Project project = ConfigDataService.getProject(logsource.getProjectId());
        logger.debug(logsource.getLineTypeRegex());
        
        //日志源启动了监控
        if(logsource.getLogSourceStatus() == 1){
    		collector.emit(new Values(input.getString(0), logsource, project, dsTime));
        }
        else{
        	;
        }
//		logger.debug("project status: " + project.getProjectStatus() + ", TimeAccuracy: " + project.getTimeAccuracy());
		logger.debug("logsource FilterKeyword: " + logsource.getLineFilterKeyword() + ", LineTypeRegex: " + logsource.getLineTypeRegex());
	}
	
	
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("line", "logsource", "project", "dsTime"));
	}


	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}


	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map paramMap, TopologyContext paramTopologyContext) {
		ExecutorService POOL = Executors.newFixedThreadPool(1);
		POOL.submit(new ConfigLoadTask());
	}

 
	@Override
	public void cleanup() {
		
	}
	
}
