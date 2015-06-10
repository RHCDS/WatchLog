package com.netease.qa.log.web.controller;


import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
public class WlogRTController {
	
	@RequestMapping(value = "logsrc/rt_analyse", method = RequestMethod.GET)
	public String rt_analyse( 
			@RequestParam(value = "proj", required = false) String projectid,
			@RequestParam(value = "log_id", required = false, defaultValue = "-1") String logsrcid,
			Model model) {
		// 项目id
		System.out.println("project id: " + projectid);
		// 日志源id   (如果参数中没有带log_id，默认取项目的第一个日志源详情)
		System.out.println("log id: " + logsrcid);
		
		// 1. add conmmon属性
		model.addAttribute("controller", "WlogRT" );		
		model.addAttribute("action", "rt_analyse" );		
		
		// 2. add  日志源列表  logs   (当前项目下)
		ArrayList<String> logs =  new ArrayList<String>();
		logs.add("1#smartAPP");
		logs.add("2#Nginx's_access");
		logs.add("3#perftest");
		model.addAttribute("logs", logs);	
		System.out.println("logs");
		System.out.println(logs);
		
		// 3. add 表格内容  (如果参数中没有带log_id，默认取项目的第一个日志源详情)
		
		JSONObject error_rc_d1 = new JSONObject();
		error_rc_d1.put("type", "error");		
		error_rc_d1.put("count", 3);		
		JSONObject error_rc_d2 = new JSONObject();
		error_rc_d2.put("type", "debug");		
		error_rc_d2.put("count", 6);			
		JSONArray error_rc_1 = new JSONArray();
		error_rc_1.add(error_rc_d1);
		error_rc_1.add(error_rc_d2);		
		JSONObject record = new JSONObject();		
		record.put("logtc", error_rc_1);
		record.put("datetime",  "2015-06-01 18:00:00");
		record.put("totalcount",  9);
		
		
		JSONArray records = new JSONArray();
		records.add(record);
		
		model.addAttribute("rt_table", records);
		System.out.println("rt_table");
		System.out.println(records);		

		
		
		

		

			

		
		
		
		
		// 4. add 日志源详情   (如果参数中没有带log_id，默认取项目的第一个日志源详情)
		model.addAttribute("logsrc_name", "smartAPP");
		model.addAttribute("host_name", "app-66.photo.163.org");
		model.addAttribute("logsrc_path", "/home/qatest/");
		model.addAttribute("logsrc_file", "catalina.out.2015.22.2");
		model.addAttribute("start_regex", "\\d{4}\\-\\d{2}\\-\\d{2");
		model.addAttribute("filter_keyword", "aaa_AND_bbb_AND_ccc");
		model.addAttribute("reg_regex", "111_OR_222_OR_333");
		
		// 5 add失败信息，如果成功，去掉status和message
//		model.addAttribute("status", -1);
//		model.addAttribute("message", "something got wrong");
		
		return "logsrc/rt_analyse";
	}
	
	
}
