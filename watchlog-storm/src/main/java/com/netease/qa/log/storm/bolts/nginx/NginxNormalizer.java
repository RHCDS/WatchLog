package com.netease.qa.log.storm.bolts.nginx;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netease.qa.log.meta.LogSource;
import com.netease.qa.log.storm.bean.Record;
import com.netease.qa.log.storm.service.ConfigDataService;
import com.netease.qa.log.storm.service.nginx.NormalizerService;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class NginxNormalizer implements IRichBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OutputCollector collector;

	private static final Logger logger = LoggerFactory.getLogger(NginxNormalizer.class);
	private static int index = 0;
	private static int count0 = 0;
	private static int count = 0;

	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		// this.config = qbserver.getLogFormat(int )

	}

	public void execute(Tuple input) {
//		logger.info("first bolt start0:" + (++count0));
		// normalizer the record
		String hostname = input.getString(0);
		String path = input.getString(1);
		String filePattern = input.getString(2);
		String line = input.getString(3);
		NormalizerService ns = new NormalizerService();
		LogSource logsource = ConfigDataService.getLogSource(hostname, path, filePattern);
		
//		logger.info("first bolt start:" + (++count));
		
		if (logsource == null) {
			logger.warn("logsource in DB is null, logsource: " + hostname + " " + path + " " + filePattern);
			return;
		}
		String config = logsource.getLogFormat();
		int logsourceId = logsource.getLogSourceId();
		
//		Record record = ns.normalizerInput(line, config);
//		record.setLog_source_id(logsourceId);
//		ArrayList<Tuple> a = new ArrayList<Tuple>();
//		a.add(input);
//		int requestTime = (int) (record.getRequest_time() * 1000);
//		int upstream_response_time = (int) (record.getUpstream_response_time() * 1000);
//		logger.info("first bolt:" + (++index));
//		collector.emit(input, new Values(record.getLog_source_id(), record.getRemote_addr(), record.getTime_local(),
//				record.getRequest(), record.getStatus(), record.getBody_bytes_sent(), requestTime,
//				upstream_response_time));
//		collector.ack(input);
		logger.info("first bolt start0:" + (++count));
		try {
			Record record = ns.normalizerInput(line, config);
			record.setLog_source_id(logsourceId);
			ArrayList<Tuple> a = new ArrayList<Tuple>();
			a.add(input);
			int requestTime = (int) (record.getRequest_time() * 1000);
			int upstream_response_time = (int) (record.getUpstream_response_time() * 1000);
			logger.info("first bolt:" + (++index));
			collector.emit(new Values(record.getLog_source_id(), record.getRemote_addr(), record.getTime_local(),
					record.getRequest(), record.getStatus(), record.getBody_bytes_sent(), requestTime,
					upstream_response_time));
		} catch (Exception e) {
			return;
		}
	}

	public void cleanup() {
		// TODO Auto-generated method stub

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("logSourceId", "Remote_addr", "Time_local", "Request", "Status", "Byte_length",
				"Request_time", "Upstream_response_time"));
	}

	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
