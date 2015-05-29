package com.netease.qa.log.storm.spouts;


import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import com.netease.qa.log.storm.util.ConfigReader;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class MQConsumer extends BaseRichSpout {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(MQConsumer.class);

	private SpoutOutputCollector collector;
	private boolean completed = false;

	private static Channel channel;
	private static Connection connection;
	private static String queueName = ConfigReader.MQ_QUEUE;
	private static String host = ConfigReader.MQ_HOST;
	private static int port = ConfigReader.MQ_PORT;
	

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
		String message = "";
		while (true) {  
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();  
            message = new String(delivery.getBody());  
            logger.info("Consume: " + message);  
            logger.info(delivery.getProperties().getHeaders().get("__DS_.fields.tag").toString());  
            logger.info(delivery.getProperties().getHeaders().get("__DS_.timestamp").toString());  
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
	        	logger.error("error", e);
			}
			catch (IOException e) {
	        	logger.error("error", e);
			}
			return;
		}
		
		QueueingConsumer consumer = new QueueingConsumer(channel);
		try {
			channel.basicConsume(queueName, true, consumer);
			while (true) {   
	            QueueingConsumer.Delivery delivery = consumer.nextDelivery();  
	            String message = new String(delivery.getBody()); 
	            
	            Map<String, Object> headers = delivery.getProperties().getHeaders();
	            this.collector.emit(new Values(message, headers), message);
	            logger.debug("Consume: " + message);  
	        }  
		}
		catch (IOException e) {
        	logger.error("error", e);
		}
		catch (ShutdownSignalException e) { 
        	logger.error("error", e);
		}
		catch (ConsumerCancelledException e) {
        	logger.error("error", e);
		}
		catch (InterruptedException e) {
        	logger.error("error", e);
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
        	logger.error("error", e);
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
