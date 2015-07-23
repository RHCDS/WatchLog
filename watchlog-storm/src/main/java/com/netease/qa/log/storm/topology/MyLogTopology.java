package com.netease.qa.log.storm.topology;

import org.apache.thrift7.TException;

import com.netease.qa.log.storm.bolts.LogAnalyser;
import com.netease.qa.log.storm.bolts.LogFilter;
import com.netease.qa.log.storm.bolts.LogNormalizer;
import com.netease.qa.log.storm.bolts.ResultWriter;
import com.netease.qa.log.storm.spouts.MQConsumer;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.DRPCExecutionException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;

public class MyLogTopology {

	public static void main(String[] args) throws InterruptedException, AlreadyAliveException, 
	InvalidTopologyException, TException, DRPCExecutionException {

		// Topology definition
//		TopologyBuilder builder = new TopologyBuilder();
//		builder.setSpout("mq-consumer", new MQConsumer());
//		builder.setBolt("log-normalizer", new LogNormalizer()).shuffleGrouping("mq-consumer");
//		builder.setBolt("log-filter", new LogFilter()).shuffleGrouping("log-normalizer");
//		builder.setBolt("log-analyser", new LogAnalyser()).shuffleGrouping("log-filter");
//		builder.setBolt("result-writer", new ResultWriter()).shuffleGrouping("log-analyser");
//
//		// Configuration
//		Config conf = new Config();
//		conf.setDebug(false);
//		
//		// Topology run
//		conf.put(Config.TOPOLOGY_WORKERS, 1);
//		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
//		conf.put(Config.TOPOLOGY_ACKER_EXECUTORS, 0);
//		
//		LocalCluster cluster = new LocalCluster();
//		cluster.submitTopology("log-test", conf, builder.createTopology());
//		Thread.sleep(10000000);
//		cluster.shutdown();
		
		
		// Topology definition
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("mq-consumer", new MQConsumer(), 3);
		builder.setBolt("log-normalizer", new LogNormalizer(), 3).shuffleGrouping("mq-consumer");
		builder.setBolt("log-filter", new LogFilter(), 3).shuffleGrouping("log-normalizer");
		builder.setBolt("log-analyser", new LogAnalyser()).shuffleGrouping("log-filter");
		builder.setBolt("result-writer", new ResultWriter()).shuffleGrouping("log-analyser");

		// Configuration
		Config conf = new Config();
		conf.setDebug(false);
		
		// Topology run
		conf.put(Config.TOPOLOGY_WORKERS, 3);
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
		conf.put(Config.TOPOLOGY_ACKER_EXECUTORS, 0);
		
		StormSubmitter.submitTopology("perf_watchlog_online2", conf, builder.createTopology());

		
	}
	
}

