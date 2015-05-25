package com.netease.qa.log.storm.spouts;


import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class MQConsumer extends BaseRichSpout {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(MQConsumer.class);

	private SpoutOutputCollector collector;
	private boolean completed = false;

	private static Channel channel;
	private static Connection connection;
	private static final String queueName = "perf.log.test";
	private static final String host = "10.120.153.195";
	private static final int port = 5672;
	

	public void ack(Object msgId) {
//		logger.info("OK:" + msgId);
	}


	public void close() {
	}


	public void fail(Object msgId) {
		logger.error("FAIL:" + msgId);
	}

	
	public static void main(String []args) throws IOException, ShutdownSignalException, 
	                ConsumerCancelledException, InterruptedException{ 
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setPort(port);

		connection = factory.newConnection();
		channel = connection.createChannel();
		channel.queueDeclare(queueName, false, false, false, null); 
		
		QueueingConsumer consumer = new QueueingConsumer(channel); 
		channel.basicConsume(queueName, true, consumer);
		while (true) {  
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();  
            String message = new String(delivery.getBody());  
            logger.info("Consume: " + message);  
            logger.info(delivery.getProperties().getHeaders().get("__DS_.fields.tag"));  
            logger.info(delivery.getProperties().getHeaders().get("__DS_.timestamp"));  
        }  
		
	}

	
	
	/**
	 * The only thing that the methods will do It is emit each file line
	 */
	public void nextTuple() {
		/**
		 * The nextuple it is called forever, so if we have been readed the file
		 * we will wait and then return
		 */
		if (completed) {
			try {
				Thread.sleep(1000);
				channel.close();
				connection.close(); 
			}
			catch (InterruptedException e) {
				logger.error(e); 
			}
			catch (IOException e) {
				logger.error(e); 
			}
			return;
		}
		/*
		 * 从公司内部拿到数据
		 * 数据放在队列中，storm及时消费
		 */

		QueueingConsumer consumer = new QueueingConsumer(channel);
		try {
			channel.basicConsume(queueName, true, consumer);
			while (true) {   
	            QueueingConsumer.Delivery delivery = consumer.nextDelivery(); 
	            //message是一条具体的日志信息
	            String message = new String(delivery.getBody());  
	            
	            Map<String, Object> headers = delivery.getProperties().getHeaders();
	            this.collector.emit(new Values(message, headers), message);
//	            logger.info("Consume: " + message);  
	        }  
		}
		catch (IOException e) {
			logger.error(e);
		}
		catch (ShutdownSignalException e) { 
			logger.error(e);
		}
		catch (ConsumerCancelledException e) {
			logger.error(e);
		}
		catch (InterruptedException e) {
			logger.error(e);
		}
		finally{
			logger.error("ERROR: SET COMPLETED" );
			completed = true;
		}
	}


	/**
	 * We will create the file and get the collector object
	 */
	@SuppressWarnings("rawtypes")
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		try {
			logger.info("==============================");
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(host);
			factory.setPort(port);

			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(queueName, false, false, false, null); 
		}
		catch (IOException e) { 
			logger.error(e);
		}
		this.collector = collector;
	}


	/**
	 * Declare the output field "word"
	 */
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("line", "headers"));
	}
	
}
