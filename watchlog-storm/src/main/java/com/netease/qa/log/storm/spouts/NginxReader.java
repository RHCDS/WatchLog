package com.netease.qa.log.storm.spouts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netease.qa.log.storm.bolts.nginx.NginxNormalizer;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

@SuppressWarnings("serial")
public class NginxReader implements IRichSpout {

	private FileReader fileReader;
	private SpoutOutputCollector collector;
	private boolean completed = false;
	
	private static final Logger logger = LoggerFactory.getLogger(NginxReader.class);
	private static int count = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see backtype.storm.spout.ISpout#open(java.util.Map,
	 * backtype.storm.task.TopologyContext,
	 * backtype.storm.spout.SpoutOutputCollector)
	 */
	@SuppressWarnings("rawtypes")
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		try {
			String fileName = conf.get("wordsFile").toString();
			
			/*
			 * LogSource logSource=dbServer.getLogsourceId(filename);
			 * int logSourceId = logSource.getLogSourceId();
			 */
			this.fileReader = new FileReader(fileName);
		} catch (Exception e) {
			throw new RuntimeException("Errorreading file[" + conf.get("wordFile") + "]");
		}
		this.collector = collector;
	}

	public void close() {
		// TODO Auto-generated method stub

	}

	public void nextTuple() {
		// TODO Auto-generated method stub
		String str;
		BufferedReader reader = new BufferedReader(fileReader);
		/*
		 * hostname=header.get("****");
		 * path=
		 * filePattern=
		 */
		String hostname = "db50.photo.163.org";
		String path = "/home/test";
		String filePattern = "nginx.log";
		try {
			// read all lines
			while ((str = reader.readLine()) != null) {
				// emit the tuple(value = line)
				logger.info("reader count:" + (++count));
				collector.emit(new Values(hostname, path, filePattern, str));
			}
		} catch (Exception e) {
			throw new RuntimeException("Errorreading tuple", e);
		} 
	}

	public void ack(Object msgId) {
		// TODO Auto-generated method stub
//		System.out.println("OK:" + msgId);
	}

	public void fail(Object msgId) {
		// TODO Auto-generated method stub
//		System.out.println("FAIL:" + msgId);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("hostname", "path", "filePattern", "line"));
	}

	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	public void activate() {
		// TODO Auto-generated method stub

	}

	public void deactivate() {
		// TODO Auto-generated method stub

	}

}
