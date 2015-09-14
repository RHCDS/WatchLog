package com.netease.qa.log.storm.topology;

import com.netease.qa.log.storm.bolts.nginx.AnalyzeNginx;
import com.netease.qa.log.storm.bolts.nginx.FilterUrl;
import com.netease.qa.log.storm.bolts.nginx.NginxNormalizer;
import com.netease.qa.log.storm.spouts.NginxReader;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

public class NginxTopology {

	public static void main(String[] args) throws InterruptedException{
		//topology definition
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("nginx-reader", new NginxReader());
		builder.setBolt("nginx-normalizer", new NginxNormalizer(), 3).shuffleGrouping("nginx-reader");
		builder.setBolt("nginx-filter", new FilterUrl(), 3).shuffleGrouping("nginx-normalizer");
		builder.setBolt("nginx-analyze", new AnalyzeNginx()).shuffleGrouping("nginx-filter");
		
		//configuration
		Config conf = new Config();
//		conf.put("wordsFile", "src/main/resources/nginx2.log");
//		conf.put("wordsFile", "src/main/resources/rt3w.log");
//		conf.put("wordsFile", "src/main/resources/access50w.log");

//		conf.setDebug(true);
		
		//topology run
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING,1);
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("getting-started-topology", conf,builder.createTopology());
//		Thread.sleep(10000);
//		cluster.shutdown();
	}
}

