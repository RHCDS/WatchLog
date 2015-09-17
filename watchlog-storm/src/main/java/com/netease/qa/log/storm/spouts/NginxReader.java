package com.netease.qa.log.storm.spouts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;






import com.netease.qa.log.storm.util.ConfigReader;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

@SuppressWarnings("serial")
public class NginxReader implements IRichSpout {

	private FileReader fileReader;
	private SpoutOutputCollector collector;
	
	private static final Logger logger = LoggerFactory.getLogger(NginxReader.class);
	private static Channel channel;
	private static Connection connection;
	private static String queueName = ConfigReader.MQ_QUEUE;
	private static String host = ConfigReader.MQ_HOST;
	private static int port = ConfigReader.MQ_PORT;

	/*
	 * (non-Javadoc)
	 * 
	 * @see backtype.storm.spout.ISpout#open(java.util.Map,
	 * backtype.storm.task.TopologyContext,
	 * backtype.storm.spout.SpoutOutputCollector)
	 */
	@SuppressWarnings("rawtypes")
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
//		try {
//			String fileName = conf.get("wordsFile").toString();
//			
//			/*
//			 * LogSource logSource=dbServer.getLogsourceId(filename);
//			 * int logSourceId = logSource.getLogSourceId();
//			 */
//			this.fileReader = new FileReader(fileName);
//		} catch (Exception e) {
//			throw new RuntimeException("Errorreading file[" + conf.get("wordFile") + "]");
//		}
		this.collector = collector;
	}

	public void close() {
		// TODO Auto-generated method stub

	}

	public void nextTuple() {
		
		while (true) {
			try {
				logger.info("=====get a new channel======");
				Channel channel = getChannel();
				QueueingConsumer consumer = new QueueingConsumer(channel);
				channel.basicConsume(queueName, true, consumer);
				channel.basicQos(1);
				while (true) {
					QueueingConsumer.Delivery delivery = consumer.nextDelivery();
					String message = new String(delivery.getBody());

					Map<String, Object> headers = delivery.getProperties().getHeaders();
					String hostname = "";  
				    String path =  "";  
				    String filePattern =  "";
					try{
						hostname = headers.get("__DS_.fields.hostname").toString();  
					    path =  headers.get("__DS_.fields._ds_target_dir").toString();  
					    filePattern =  headers.get("__DS_.fields._ds_file_pattern").toString();  
					    
					    this.collector.emit(new Values(hostname, path, filePattern, message), message);
						logger.debug("Consume: " + message);
						logger.info("hostname: " + hostname + ", path: " + path + ", filePattern: " + filePattern);
					}
					catch(NullPointerException e){
						logger.error("can't get header, hostname: " + hostname + ", path: " + path + ", file: " + filePattern, e);
					}
				}
			}
			catch (Exception e) {
				logger.error("consume error, close connction", e);
				if (channel != null) {
					try {
						channel.close();
					}
					catch (IOException e1) {
						channel = null;
					}
				}
			}
		}
		
//		String str;
//		BufferedReader reader = new BufferedReader(fileReader);
//		/*
//		 * hostname=header.get("****");
//		 * path=
//		 * filePattern=
//		 */
//		String hostname = "db50.photo.163.org";
//		String path = "/home/test";
//		String filePattern = "nginx.log";
//		try {
//			// read all lines
//			while ((str = reader.readLine()) != null) {
//				// emit the tuple(value = line)
//				collector.emit(new Values(hostname, path, filePattern, str));
//			}
//		} catch (Exception e) {
//			throw new RuntimeException("Errorreading tuple", e);
//		} 
		
	}

	public void ack(Object msgId) {
		// TODO Auto-generated method stub
//		System.out.println("OK:" + msgId);
	}

	public void fail(Object msgId) {
		// TODO Auto-generated method stub
//		System.out.println("FAIL:" + msgId);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("hostname", "path", "filePattern", "line"));
	}

	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	public void activate() {
		// TODO Auto-generated method stub

	}

	public void deactivate() {
		// TODO Auto-generated method stub

	}
	
	private Connection getConnection() throws IOException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setPort(port);
		connection = factory.newConnection();
		return connection;
	}
	
	private Channel getChannel() {
		int count = 3;
		while (count-- > 0) {
			try {
				if (connection == null) {
					connection = getConnection();
				}
				return connection.createChannel();
			}
			catch (Exception e) {
				logger.error("get channel error, try left: " + count, e); 
				if (connection != null) {
					try {
						connection.close();
					}
					catch (Exception e1) {
					}
				}
				connection = null;
				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException e2) {
				}
			}
		}
		return null;
	}

}
