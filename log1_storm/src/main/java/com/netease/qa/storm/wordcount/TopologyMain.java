package com.netease.qa.storm.wordcount;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

public class TopologyMain {

	public static void main(String[] args) throws InterruptedException{
		//topology definition
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("word-reader", new WordReader());
		builder.setBolt("word-normalizer", new WordNormalizer()).shuffleGrouping("word-reader");
		builder.setBolt("word-counter", new WordCounter()).shuffleGrouping("word-normalizer");
		
		//configuration
		Config conf = new Config();
		conf.put("wordsFile", "src/main/resources/words1.txt");
		conf.setDebug(true);
		
		//topology run
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING,1);
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("getting-started-topology", conf,builder.createTopology());
		Thread.sleep(10000);
		cluster.shutdown();
	}
}
