package com.netease.qa.log.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.netease.qa.log.user.service.GetException;
import com.netease.qa.log.user.service.ReadService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/report")
public class ReadServiceController {

	@Resource
	private ReadService readService;
	
	//按时间聚合
	@RequestMapping(value = "/time/{id}", method = RequestMethod.POST,
		   produces = {"application/json;charset=utf-8"})
	public @ResponseBody JSONObject readByTime(@PathVariable int id, @RequestParam("start") String start,
			@RequestParam("end") String end, @RequestParam("limit") String limit, @RequestParam("offset") String offset,Model model){
		//获取请求url的信息
		int logsourceid = id;
		String start_time = start;
		String end_time = end;
		String limit1 = limit;
		String offset1 = offset;		

		//把string的时间转换为标准时间
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			date = format.parse(start_time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//把标准时间转换为时间戳
		Long startTime = date.getTime();
		startTime = startTime / 1000;
		
		//String end_time = end;
		try {
			date = format.parse(end_time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Long endTime = date.getTime();
		endTime = endTime / 1000;
		
		int limit2 = Integer.parseInt(limit1);
		
		int offset2 = Integer.parseInt(offset1);
		
		JSONObject jsonObject = this.readService.queryTimeRecords(logsourceid, startTime, endTime, limit2, offset2);
		
		return jsonObject;
		
//		int projectId = jsonObject.getInteger("projectid");
//		int logsourceId = jsonObject.getInteger("logsourceid");
//		JSONArray records = jsonObject.getJSONArray("record");
//		//detail是一个string的字符串
//		String recordStrc = " ";
//		String recordStr=" ";
//		for(int i = 0;i < records.size();i++){
//			 recordStr = " ";
//			JSONObject record = records.getJSONObject(i);
//			
//			//获取每个JSON中的内容
//			String time = record.getString("time");
//			int totalCount = record.getInteger("totalcount");
//			JSONArray details = record.getJSONArray("detail");
//			
//			String detailStr = "detail: [";
//			    for(int j = 0;j < details.size();j++){
//			    	JSONObject detail = details.getJSONObject(j);
//			    	String type = detail.getString("exceptionType");
//			    	int count = detail.getInteger("count");
//			    	detailStr = "{' " + type + "':'" + count + "'},";
//			    }
//			    detailStr = detailStr + " ]";
//			    
//			 recordStr = " time: " + time + "totalCount: " + totalCount+";" + detailStr;
//			 recordStrc = recordStrc + "{ " + recordStr + " }";
//		}
//		model.addAttribute("projectid", projectId);
//		model.addAttribute("logsourceid", logsourceId);
//		model.addAttribute("message", recordStrc);
//		return "showRecord";
		
	}
	
	//按异常类型聚合
	@RequestMapping(value = "/error/{id}", method = RequestMethod.POST,
			   produces = {"application/json;charset=utf-8"})
	@ResponseBody
    public JSONObject readByError(@PathVariable int id, @RequestParam("start") String start,
			@RequestParam("end") String end, @RequestParam("limit") String limit, @RequestParam("offset") String offset,Model model){
		//获取请求url的信息
		       int logsourceid = id;
		       String start_time = start;
		       String end_time = end;
		       String limit1 = limit;
	           String offset1 = offset;
    			//把string的时间转换为标准时间
    			SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    			Date date = new Date();
    			try {
    				date = format.parse(start_time);
    			} catch (ParseException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    			//把标准时间转换为时间戳
    			Long startTime = date.getTime();
    			startTime = startTime / 1000;
    			try {
    				date = format.parse(end_time);
    			} catch (ParseException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    			Long endTime = date.getTime();
    			endTime = endTime / 1000;
    
    			int limit2 = Integer.parseInt(limit1);
    		
    			int offset2 = Integer.parseInt(offset1);
    			
    			JSONObject jsonObject = this.readService.queryErrorRecords(logsourceid, startTime, endTime, limit2, offset2);
    			return jsonObject;
//    			int projectId = jsonObject.getInteger("projectid");
//    			int logsourceId = jsonObject.getInteger("logsourceid");
//    			JSONArray records = jsonObject.getJSONArray("error");
//    			System.out.println("records.size():" + records.size());
//    			
//    			//detail是一个string的字符串
//    			String recordStrc = " ";
//    			String recordStr=" ";
//    			for(int i=0;i<records.size();i++){
//    				recordStr = " ";
//    				JSONObject record = records.getJSONObject(i);
//    				
//    				String type = record.getString("type");
//    				int totalCount = record.getIntValue("totalcount");
//    				String demo = record.getString("demo");
//    				JSONArray details = record.getJSONArray("detail");
//    				
//    				String detailStr = "detail: [";
//    				//取出details里面的值
//    				for(int j=0;j<details.size();j++){
//    					JSONObject detail = details.getJSONObject(j);
//    					String time = detail.getString("time");
//    					int count = detail.getIntValue("count");
//    					detailStr = "{' " + time + "':'" + count + "'},";
//    				}
//    				 recordStr = " type: " + type + "totalCount: " + totalCount+";" + "demo:" + demo + detailStr;
//    				 recordStrc = recordStrc + "{ " + recordStr + " }";
//    			}
//    			model.addAttribute("projectid", projectId);
//    			model.addAttribute("logsourceid", logsourceId);
//    			model.addAttribute("message", recordStrc);
//    			return "showRecord";
    	
    }
	
	//获取unknown类型异常
	@RequestMapping(value = "/unknown/{id}", method = RequestMethod.POST,
			   produces = {"application/json;charset=utf-8"})
	@ResponseBody
	public JSONObject readByUnknow(@PathVariable int id, @RequestParam("start") String start,
			@RequestParam("end") String end, @RequestParam("limit") String limit, @RequestParam("offset") String offset,Model model){
		
		//获取请求url的信息
	    int logsourceid = id;
	    String start_time = start;
	    String end_time = end;
	    String limit1 = limit;
        String offset1 = offset;
		
		//把string的时间转换为标准时间
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			date = format.parse(start_time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//把标准时间转换为时间戳
		Long startTime = date.getTime();
		startTime = startTime / 1000;
		
		try {
			date = format.parse(end_time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Long endTime = date.getTime();
		endTime = endTime / 1000;

		int limit2 = Integer.parseInt(limit1);
		
		int offset2 = Integer.parseInt(offset1);
		
		JSONObject jsonObject = this.readService.queryUnknownExceptions(logsourceid, startTime, endTime, limit2, offset2);
		 return jsonObject;
//		int projectId = jsonObject.getInteger("projectid");
//		int logsourceId = jsonObject.getInteger("logsourceid");
//		JSONArray records = jsonObject.getJSONArray("unknowns"); 
//		
//		String detail = " [";
//		for(int i=0;i<records.size();i++){
//			JSONObject record = records.getJSONObject(i);
//			String time = record.getString("time");
//			String log = record.getString("log");
//			
//			detail = detail + " { " + time + " : " + log + "}";
//			
//		}
//		detail = detail + " ]";
//		model.addAttribute("projectid", projectId);
//		model.addAttribute("logsourceid", logsourceId);
//		model.addAttribute("message", detail);
//		return "showRecord";
		
	}
	
	
}
