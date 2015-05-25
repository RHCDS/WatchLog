package com.netease.qa.storm.wordcount;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

public class WordCounter implements IRichBolt {

	private OutputCollector collector;
	Integer id;
	String name;
	Map<String,Integer> counters;
	
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		// TODO Auto-generated method stub
		this.counters = new HashMap<String,Integer>();
		this.collector = collector;
		this.name = context.getThisComponentId();
		this.id = context.getThisTaskId();
	}

	@Override
	public void execute(Tuple input) {
		// TODO Auto-generated method stub
		String str = input.getString(0);
		//count the same word
		if(!counters.containsKey(str)){
			counters.put(str,1);
		}else{
			int c = counters.get(str) + 1;
			counters.put(str, c);
		}
		collector.ack(input);
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		System.out.println("--Word Counter ["+name+"-"+id+"]--");
		for(Map.Entry<String, Integer> entry:counters.entrySet()){
			System.out.println(entry.getKey()+":"+entry.getValue());
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
