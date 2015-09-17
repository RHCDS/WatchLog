package com.netease.qa.log.storm.topology;

import com.netease.qa.log.storm.bolts.nginx.AnalyzeNginx;
import com.netease.qa.log.storm.bolts.nginx.FilterUrl;
import com.netease.qa.log.storm.bolts.nginx.NginxNormalizer;
import com.netease.qa.log.storm.spouts.NginxReader;
import com.netease.qa.log.storm.util.ConfigReader;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

public class NginxTopology {

	public static void main(String[] args) throws InterruptedException{
		//topology definition
//		TopologyBuilder builder = new TopologyBuilder();
		System.out.println("nginx-reader=" + ConfigReader.NGINX_READER_SPOUT);
		System.out.println("nginx-normalizer=" + ConfigReader.NGINX_NORMALIZER_BOLT);
		System.out.println("nginx-filter=" + ConfigReader.NGINX_FILTER_BOLT);
		System.out.println("nginx-analyze=" + ConfigReader.NGINX_ANALYSER_BOLT);
		
		
//		builder.setSpout("nginx-reader", new NginxReader(), ConfigReader.NGINX_READER_SPOUT);
//		builder.setBolt("nginx-normalizer", new NginxNormalizer(), ConfigReader.NGINX_NORMALIZER_BOLT).shuffleGrouping("nginx-reader");
//		builder.setBolt("nginx-filter", new FilterUrl(), ConfigReader.NGINX_FILTER_BOLT).shuffleGrouping("nginx-normalizer");
//		builder.setBolt("nginx-analyze", new AnalyzeNginx(), ConfigReader.NGINX_ANALYSER_BOLT).shuffleGrouping("nginx-filter");
//		
//		//configuration
//		Config conf = new Config();
////		conf.put("wordsFile", "src/main/resources/nginx2.log");
////		conf.put("wordsFile", "src/main/resources/rt3w.log");
////		conf.put("wordsFile", "src/main/resources/access50w.log");
//
////		conf.setDebug(true);
//		
//		//topology run
//		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING,ConfigReader.TOPOLOGY_MAX_SPOUT_PENDING);
//		LocalCluster cluster = new LocalCluster();
//		cluster.submitTopology("getting-started-topology", conf,builder.createTopology());
////		Thread.sleep(10000);
////		cluster.shutdown();
	}
}

