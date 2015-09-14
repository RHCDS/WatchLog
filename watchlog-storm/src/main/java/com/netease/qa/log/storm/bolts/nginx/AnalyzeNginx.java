package com.netease.qa.log.storm.bolts.nginx;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netease.qa.log.meta.NginxAccess;
import com.netease.qa.log.storm.service.MonitorDataWriteNginxTask;
import com.netease.qa.log.storm.service.nginx.AnalyzeService;
import com.netease.qa.log.storm.util.MathUtil;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

public class AnalyzeNginx implements IRichBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OutputCollector collector;
	private static final Logger logger = LoggerFactory.getLogger(AnalyzeNginx.class);
	private static int index = 0;

	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		try {
			Thread.sleep(1000);
		}
		catch (InterruptedException e) {
			logger.error("error", e);
		} 
		ExecutorService POOL = Executors.newFixedThreadPool(1);
		POOL.submit(new MonitorDataWriteNginxTask());
	}

	public void execute(Tuple input) {
		int logSourceId = (Integer)input.getValue(0);
		long time = (Long) input.getValue(2);
		String url = (String) input.getValue(3);
		int status = (Integer) input.getValue(4);
		int byteLength = (Integer)input.getValue(5);
		int requestTime = (Integer) input.getValue(6);
		int upstreamResponseTime = (Integer)input.getValue(7);
		long startTime = MathUtil.getStartTime(time);
		AnalyzeService.putUrlTps(logSourceId, url, startTime);
		AnalyzeService.putTotalRequestTime(logSourceId, url, startTime, requestTime);
		AnalyzeService.putMaxRequestTime(logSourceId, url, startTime, requestTime);
		AnalyzeService.putTotalUpstreamResponseTime(logSourceId, url, startTime, upstreamResponseTime);
		AnalyzeService.putMaxUpstreamResponseTime(logSourceId, url, startTime, upstreamResponseTime);
		AnalyzeService.putOkCount(logSourceId, url, startTime, status);
		AnalyzeService.putError4Count(logSourceId, url, startTime, status);
		AnalyzeService.putError5Count(logSourceId, url, startTime, status);
		AnalyzeService.putTotalByte(logSourceId, url, startTime, byteLength);
		AnalyzeService.putAllRequestTime(logSourceId, url, startTime, requestTime);
		AnalyzeService.putAllUpstreamResponseTime(logSourceId, url, startTime, upstreamResponseTime);
		
		logger.info("third bolt:" + ++index);
		// collector.ack(input);
	}

	public void cleanup() {
//		System.out.println("-------analyze result --------");
//		List<NginxAccess> results = AnalyzeService.writeResult();
//		System.out.println("results.size():" + results.size());
//		for (int i = 0; i < results.size(); i++){
//			System.out.println("results[" + i + "]: " + results.get(i).toString());
//		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}

	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
