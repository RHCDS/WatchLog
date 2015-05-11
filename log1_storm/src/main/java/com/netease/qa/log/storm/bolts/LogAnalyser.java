package com.netease.qa.log.storm.bolts;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

import com.netease.qa.log.meta.Exception;
import com.netease.qa.log.meta.LogSource;
import com.netease.qa.log.service.MonitorDataService;
import com.netease.qa.log.service.MonitorDataWriteTask;
import com.netease.qa.log.util.MD5Utils;

public class LogAnalyser implements IBasicBolt {


	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(LogAnalyser.class);


	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		
		String line = input.getString(0);
		LogSource logSource = (LogSource) input.getValue(1);
		//Project project = (Project) input.getValue(2); //TODO 鏀寔project璁剧疆涓嶅悓鐨勯噰鏍峰懆鏈焧ime_accuracy
		Long dsTime = Long.valueOf(input.getString(3));
		 
		//TODO 鏀寔澶氫釜姝ｅ垯琛ㄨ揪寮�
		int logSourceId = logSource.getLogSourceId();
		String lineTypeRegex = logSource.getLineTypeRegex();
		String exceptionType = null;
		String exceptionTypeMD5 = null;
		int exceptionId;

		//TODO 鎬ц兘闂锛焭hichi 
		Pattern p = Pattern.compile(lineTypeRegex); 
		Matcher m = p.matcher(line);  
		if(m.find()){
			exceptionType = m.group();
		}
		else{
			exceptionType = "unknown";
		}
		
		//鏌ヨexception缂撳瓨锛屽鏋滀笉瀛樺湪鍒欐彃鍏xception琛�
		exceptionTypeMD5 = MD5Utils.getMD5(exceptionType);
		Exception exception = MonitorDataService.getException(logSourceId, MD5Utils.getMD5(exceptionType));
		if (exception != null) {
			exceptionId = exception.getExceptionId();
		}
		else {
			logger.info("first get! exceptionType: " + exceptionType + ", logSourceId: " + logSourceId);
			exceptionId = MonitorDataService.putException(logSourceId, exceptionTypeMD5, exceptionType, line);
		}
			
		//鏈尮閰嶅埌寮傚父绫诲瀷锛�鎻掑叆unknown_exception_data琛�
		if(exceptionType.equals("unknown")){
			MonitorDataService.putUkExceptionData(logSourceId, dsTime/1000, line);
		}
		
		//璁板綍寮傚父绫诲瀷鍜屾暟閲�
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
