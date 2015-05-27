package com.netease.qa.storm.wordcount;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

@SuppressWarnings("serial")
public class WordReader implements IRichSpout {

	private FileReader fileReader;
	private SpoutOutputCollector collector;
	private boolean completed=false;
	/*
	 * (non-Javadoc)
	 * @see backtype.storm.spout.ISpout#open(java.util.Map, backtype.storm.task.TopologyContext, backtype.storm.spout.SpoutOutputCollector)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		try{
			this.fileReader = new FileReader(conf.get("wordsFile").toString());
		}catch(Exception e){
			throw new RuntimeException("Errorreading file["+conf.get("wordFile")+"]");
		}
		this.collector = collector;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nextTuple() {
		// TODO Auto-generated method stub
		if(completed){
			try{
				Thread.sleep(1000);
			}catch(InterruptedException e){
				//Do nothing
			}
			return;
		}
		String str;
		BufferedReader reader = new BufferedReader(fileReader);
		try{
			//read all lines
			while((str=reader.readLine())!=null){
				//emit the tuple(value = line)
				collector.emit(new Values(str), str);
			}
		}catch(Exception e){
			throw new RuntimeException("Errorreading tuple",e);
		}finally{
			completed = true;
		}
	}

	@Override
	public void ack(Object msgId) {
		// TODO Auto-generated method stub
		System.out.println("OK:" + msgId);
	}

	@Override
	public void fail(Object msgId) {
		// TODO Auto-generated method stub
		System.out.println("FAIL:" + msgId);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("line"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		
	}

}
