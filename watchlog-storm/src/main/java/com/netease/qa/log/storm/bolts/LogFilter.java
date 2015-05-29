package com.netease.qa.log.storm.bolts;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.netease.qa.log.meta.LogSource;

public class LogFilter implements IBasicBolt {


	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(LogFilter.class);


	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		String line = input.getString(0);

		LogSource logSource = (LogSource) input.getValue(1);
		String keyword = logSource.getLineFilterKeyword().trim();

		if (keyword.equals("None")) {
			collector.emit(new Values(line, input.getValue(1), input.getValue(2), input.getValue(3)));
		}
		else {
			//TODO 支持多个keyword OR/AND连接
			if (line.indexOf(keyword) != -1) {
				collector.emit(new Values(line, input.getValue(1), input.getValue(2), input.getValue(3)));
				logger.debug("get!");
			}
		}
	}
	
	
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("line", "logsource", "project", "dsTime"));
	}


	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}


	@Override
	public void prepare(@SuppressWarnings("rawtypes") Map paramMap, TopologyContext paramTopologyContext) {
		
	}


	@Override
	public void cleanup() {
		 
	}
	
}
