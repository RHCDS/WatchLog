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
			@RequestParam(value = "log_id", required = false) String logsrcid,
			Model model) {
		// 项目id
		//System.out.println("project id: " + projectid);
		// 日志源id   (如果参数中没有带log_id，默认取项目的第一个日志源详情)
		// System.out.println("project id: " + projectid);
		
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
		// 表格row-1 col-2   ：  [{"count":3,"type":"error"},{"count":6,"type":"debug"}]
		JSONObject error_rc_d1 = new JSONObject();
		error_rc_d1.put("type", "error");		
		error_rc_d1.put("count", 3);		
		JSONObject error_rc_d2 = new JSONObject();
		error_rc_d2.put("type", "debug");		
		error_rc_d2.put("count", 6);			
		JSONArray error_rc_1 = new JSONArray();
		error_rc_1.add(error_rc_d1);
		error_rc_1.add(error_rc_d2);
		
		// 表格row-1    ["2015-06-01",[{"count":3,"type":"error"},{"count":6,"type":"debug"}],14]
		JSONArray d1 = new JSONArray();
		// 表格row-1 col-1
		d1.add("2015-06-01");	
		// 表格row-1 col-2
		d1.add(error_rc_1);
		// 表格row-1 col-3
		d1.add(14);
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 表格row-2 col-2   ：  [{"count":3,"type":"error"},{"count":6,"type":"debug"}]
		JSONObject error_rc_d3 = new JSONObject();
		error_rc_d3.put("type", "info");		
		error_rc_d3.put("count", 4);		
		JSONObject error_rc_d4 = new JSONObject();
		error_rc_d4.put("type", "warn");		
		error_rc_d4.put("count", 1);			
		JSONArray error_rc_2 = new JSONArray();
		error_rc_2.add(error_rc_d3);
		error_rc_2.add(error_rc_d4);
		
		// 表格row-1    ["2015-06-01",[{"count":3,"type":"error"},{"count":6,"type":"debug"}],14]
		JSONArray d2 = new JSONArray();
		// 表格row-1 col-1
		d2.add("2015-06-08");	
		// 表格row-1 col-2
		d2.add(error_rc_2);
		// 表格row-1 col-3
		d2.add(10);		
		
		// 表格所有行 
		// 例子：[["2015-06-01",[{"count":3,"type":"error"},{"count":6,"type":"debug"}],14],["2015-06-08",[{"count":4,"type":"info"},{"count":1,"type":"warn"}],10]]
		JSONArray rt_data = new JSONArray();
		rt_data.add(d1);
		rt_data.add(d2);
		model.addAttribute("rt_table", rt_data);
		System.out.println("rt_table");
		System.out.println(rt_data);
			
		
		
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
