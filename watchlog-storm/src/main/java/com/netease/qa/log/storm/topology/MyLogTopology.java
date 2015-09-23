package com.netease.qa.log.storm.topology;


import org.apache.thrift7.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netease.qa.log.storm.bolts.LogAnalyser;
import com.netease.qa.log.storm.bolts.LogFilter;
import com.netease.qa.log.storm.bolts.LogNormalizer;
import com.netease.qa.log.storm.bolts.ResultWriter;
import com.netease.qa.log.storm.bolts.nginx.AnalyzeNginx;
import com.netease.qa.log.storm.bolts.nginx.FilterUrl;
import com.netease.qa.log.storm.bolts.nginx.NginxNormalizer;
import com.netease.qa.log.storm.spouts.MQConsumer;
import com.netease.qa.log.storm.spouts.NginxReader;
import com.netease.qa.log.storm.util.ConfigReader;
import com.netease.qa.log.storm.util.Const;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.DRPCExecutionException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;

public class MyLogTopology {

	private static final Logger logger = LoggerFactory.getLogger(MyLogTopology.class);

	public static void main(String[] args) throws InterruptedException, AlreadyAliveException,
			InvalidTopologyException, TException, DRPCExecutionException {
		// 第一个参数是配置文件路径
		String fileName = args[0];
//		String fileName = "/d:/storm.conf";
		if(fileName.isEmpty()){
			logger.error("lose filename!");
		}
		ConfigReader.setValue(fileName);
		String logType = ConfigReader.LOG_TYPE;
		if (logType.equals(Const.EXCEPTION_LOG)) {
			// Topology definition
			TopologyBuilder builder = new TopologyBuilder();
			builder.setSpout("mq-consumer", new MQConsumer(), ConfigReader.LOG_SPOUT);
			builder.setBolt("log-normalizer", new LogNormalizer(), ConfigReader.LOG_NORMALIZER_BOLT).shuffleGrouping(
					"mq-consumer");
			builder.setBolt("log-filter", new LogFilter(), ConfigReader.LOG_FILTER_BOLT).shuffleGrouping(
					"log-normalizer");
			builder.setBolt("log-analyser", new LogAnalyser(), ConfigReader.LOG_ANALYSER_BOLT).shuffleGrouping(
					"log-filter");
			builder.setBolt("result-writer", new ResultWriter(), ConfigReader.RESULT_WRITER_BOLT).shuffleGrouping(
					"log-analyser");
			// Configuration
			Config conf = new Config();
			conf.setDebug(false);
			// Topology run
			
			conf.put(Const.MQ_HOST, ConfigReader.MQ_HOST);
			conf.put(Const.MQ_PORT, ConfigReader.MQ_PORT);
			conf.put(Const.MQ_QUEUE, ConfigReader.MQ_QUEUE);
			conf.put(Const.MYBATIS_EVN, ConfigReader.MYBATIS_ENV);
			
			conf.put(Config.TOPOLOGY_WORKERS, ConfigReader.TOPOLOGY_WORKERS);
			conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, ConfigReader.TOPOLOGY_MAX_SPOUT_PENDING);
			conf.put(Config.TOPOLOGY_ACKER_EXECUTORS, ConfigReader.TOPOLOGY_ACKER_EXECUTORS);
			StormSubmitter.submitTopology(ConfigReader.TOPOLOGY_NAME, conf, builder.createTopology());
			
		
		} else if (logType.equals(Const.NGINX_LOG)) {
			// topology definition
			TopologyBuilder builder = new TopologyBuilder();
			builder.setSpout("nginx-reader", new NginxReader(), ConfigReader.NGINX_READER_SPOUT);
			builder.setBolt("nginx-normalizer", new NginxNormalizer(), ConfigReader.NGINX_NORMALIZER_BOLT)
					.shuffleGrouping("nginx-reader");
			builder.setBolt("nginx-filter", new FilterUrl(), ConfigReader.NGINX_FILTER_BOLT).shuffleGrouping(
					"nginx-normalizer");
			builder.setBolt("nginx-analyze", new AnalyzeNginx(), ConfigReader.NGINX_ANALYSER_BOLT).shuffleGrouping(
					"nginx-filter");
			// configuration
			Config conf = new Config();
			// conf.put("wordsFile", "src/main/resources/nginx2.log");
			// conf.setDebug(true);
			// topology run
			conf.put(Const.MQ_HOST, ConfigReader.MQ_HOST);
			conf.put(Const.MQ_PORT, ConfigReader.MQ_PORT);
			conf.put(Const.MQ_QUEUE, ConfigReader.MQ_QUEUE);
			conf.put(Const.MYBATIS_EVN, ConfigReader.MYBATIS_ENV);
			
			conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, ConfigReader.TOPOLOGY_MAX_SPOUT_PENDING);
			StormSubmitter.submitTopology(ConfigReader.TOPOLOGY_NAME, conf, builder.createTopology());
		}
	}

}
