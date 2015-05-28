package com.netease.qa.log.test.httpclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

public class HttpClientTest {

	private HttpClient client = HttpClientBuilder.create().build();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "http://localhost:8080/watchlog-web/api/logsource/5";
		
		HttpClientTest http = new HttpClientTest();
		String resultGet = "";
		try {
			 resultGet = http.getRequest(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("resultGet:" + resultGet);
		
		String urlPost = "http://localhost:8080/watchlog-web/api/report/error/1";
		String resultPost = "";
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair("start","2013-03-30 16:00:00"));
		postParams.add(new BasicNameValuePair("end","2015-05-30 17:00:00"));
		postParams.add(new BasicNameValuePair("limit","100"));
		postParams.add(new BasicNameValuePair("offset","1"));
		try {
			resultPost = http.postRequest(urlPost, postParams);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("resultPost:" + resultPost);
	}

	//get 
	private String getRequest(String url) throws Exception{
		HttpGet request = new HttpGet(url);
		
		HttpResponse response = client.execute(request);
		int responseStatus = response.getStatusLine().getStatusCode();
		System.out.println("Sending 'GET' request to URL:" + url);
		System.out.println("responseStatus:" + responseStatus);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while((line = rd.readLine()) != null){
			result.append(line);
		}
		return result.toString();
	}
	
	//post 
	private String postRequest(String url, List<NameValuePair> postParams)
	       throws Exception {
		HttpPost request = new HttpPost(url);
		request.setEntity(new UrlEncodedFormEntity(postParams));
		HttpResponse response = client.execute(request);
		int responseStatus = response.getStatusLine().getStatusCode();
		System.out.println("Sending 'POST' request to URL:" + url);
		System.out.println("responseStatus:" + responseStatus);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while((line = rd.readLine()) != null){
			result.append(line);
		}
		return result.toString();
		
	}
}
