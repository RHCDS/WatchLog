package com.netease.qa.log.storm.bolts;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

import com.netease.qa.log.meta.Exception;
import com.netease.qa.log.meta.LogSource;
import com.netease.qa.log.storm.service.MonitorDataService;
import com.netease.qa.log.storm.service.MonitorDataWriteTask;
import com.netease.qa.log.util.MD5Utils;

public class LogAnalyser implements IBasicBolt {


	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(LogAnalyser.class);
	private static final String UNKNOWN_TYPE = "unknown";

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		
		String line = input.getString(0);
		LogSource logSource = (LogSource) input.getValue(1);
		Long dsTime = Long.valueOf(input.getString(3));
		 
		//TODO 支持多个正则表达式
		int logSourceId = logSource.getLogSourceId();
		String lineTypeRegex = logSource.getLineTypeRegex();
		String exceptionType = null;
		String exceptionTypeMD5 = null;
		int exceptionId;

		//TODO 性能问题？zhichi 
		Pattern p = Pattern.compile(lineTypeRegex); 
		Matcher m = p.matcher(line);  
		if(m.find()){
			exceptionType = m.group();
			logger.debug("match! " + exceptionType);
		}
		else{
			exceptionType = UNKNOWN_TYPE;
			logger.debug("cant match! set as unknown");
		}
		
		//查询exception缓存，如果不存在则插入exception表
		exceptionTypeMD5 = MD5Utils.getMD5(exceptionType);
		Exception exception = MonitorDataService.getException(logSourceId, MD5Utils.getMD5(exceptionType));
		if (exception != null) {
			logger.debug("get exception " + exception);
			exceptionId = exception.getExceptionId();
		}
		else {
			logger.info("first get! exceptionType: " + exceptionType + ", logSourceId: " + logSourceId);
			exceptionId = MonitorDataService.putException(logSourceId, exceptionTypeMD5, exceptionType, line);
		}
			
		//未匹配到异常类型， 插入unknown_exception_data表
		if(exceptionType.equals(UNKNOWN_TYPE)){
			MonitorDataService.putUkExceptionData(logSourceId, dsTime/1000, line);
		}
		
		//记录异常类型和数量 
		MonitorDataService.putExceptionData(logSourceId, exceptionId);

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
		POOL.submit(new MonitorDataWriteTask());
	}


	@Override
	public void cleanup() {
		
	}
	
}
